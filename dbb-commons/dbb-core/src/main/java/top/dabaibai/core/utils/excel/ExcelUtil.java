package top.dabaibai.core.utils.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import top.dabaibai.core.enums.ExcelTypeEnum;
import top.dabaibai.core.utils.DateUtils;
import top.dabaibai.thread.DbbThreadPool;
import top.dabaibai.thread.Task;
import top.dabaibai.thread.ThreadNoBlockingModel;
import top.dabaibai.web.commons.http.DbbException;
import top.dabaibai.web.commons.http.SystemErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: excel工具类
 * @author: 白剑民
 * @dateTime: 2022/6/28 13:54
 */
@Slf4j
public class ExcelUtil<T> {

    /**
     * 读取表头时的最大读取列数
     */
    private static final int READ_HEAD_END = 300;
    /**
     * excel文件格式
     */
    private static final String XLSX_FILE_PREFIX = "xlsx";

    private ExcelUtil() {
    }

    /**
     * @param response  响应对象
     * @param fileName  不带后缀的文件名
     * @param list      数据列表
     * @param pojoClass 数据实体类
     * @param excelType excel格式
     * @description: Excel导出
     * @author: 白剑民
     * @date: 2022-07-10 21:15:17
     * @version: 1.0
     */
    public static void exportExcel(HttpServletResponse response, String fileName, Collection<?> list,
                                   Class<?> pojoClass, ExcelTypeEnum excelType) {
        if (StringUtils.isBlank(fileName)) {
            // 当前日期
            fileName = DateUtils.parseTime(LocalDateTime.now(), DateUtils.TimeFormat.SHORT_DATE_PATTERN_LINE);
        }
        ServletOutputStream out = null;
        try {
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), pojoClass, list);
            // 导出到第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            // 编码格式UTF-8
            response.setCharacterEncoding("UTF-8");
            String fileSuffix;
            // 根据入参枚举判断excel导出格式
            if (excelType == ExcelTypeEnum.XLSX) {
                response.setHeader("content-Type",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                fileSuffix = ".xlsx";
            } else {
                response.setHeader("content-Type", "application/vnd.ms-excel");
                fileSuffix = ".xls";
            }
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + fileSuffix);
            out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("excel导出错误: {}, 错误行: {}", e.getMessage(), e.getStackTrace()[0]);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("输出流关闭出错: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * @param response 响应
     * @param fileName 文件名称
     * @param list     填充数据列表
     * @param extra    额外自定义填充数据
     * @param template 模板
     * @description: 模板导出excel
     * @author: 白剑民
     * @date: 2023-03-15 11:35:57
     * @return: void
     * @version: 1.0
     */
    public static void exportTemplateExcel(HttpServletResponse response, String fileName, Collection<?> list,
                                           Map<String, Object> extra, Resource template) throws IOException {
        exportTemplateExcel(response, fileName, list, extra, template, null);
    }

    /**
     * @param response 响应
     * @param fileName 文件名称
     * @param list     填充数据列表
     * @param extra    额外自定义填充数据
     * @param template 模板
     * @param handler  自定义处理器
     * @description: 模板导出excel
     * @author: 白剑民
     * @date: 2023-03-15 11:35:57
     * @return: void
     * @version: 1.0
     */
    public static void exportTemplateExcel(HttpServletResponse response, String fileName, Collection<?> list,
                                           Map<String, Object> extra, Resource template, CustomCellWriteHandler handler) throws IOException {
        if (StringUtils.isBlank(fileName)) {
            fileName = DateUtils.parseTime(LocalDateTime.now(), DateUtils.TimeFormat.SHORT_DATE_PATTERN_LINE);
        }
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        // 接口Response返回
        ExcelWriter writer = EasyExcel.write(response.getOutputStream()).withTemplate(template.getInputStream()).build();
        // 操作sheet(注册自定义的CellWriteHandler)
        WriteSheet sheet;
        if (handler != null) {
            sheet = EasyExcel.writerSheet().registerWriteHandler(handler).build();
        } else {
            sheet = EasyExcel.writerSheet().build();
        }
        writer.fill(list, FillConfig.builder().forceNewRow(Boolean.TRUE).build(), sheet).fill(extra, sheet);
        // 设置强制计算公式：不然公式会以字符串的形式显示在excel中
        Workbook workbook = writer.writeContext().writeWorkbookHolder().getWorkbook();
        workbook.setForceFormulaRecalculation(true);
        // 数据刷新
        writer.finish();
    }

    /**
     * @param response 响应
     * @param fileName 文件名称
     * @param map      多sheet填充数据列表
     * @param extra    额外自定义填充数据
     * @param template 模板
     * @param handler  自定义处理器
     * @description: 多sheet模板导出excel
     * @author: 白剑民
     * @date: 2023-03-15 11:35:57
     * @return: void
     * @version: 1.0
     */
    public static void exportTemplateExcel(HttpServletResponse response, String fileName, Map<TemplateSheet, Collection<?>> map,
                                           Map<String, Object> extra, Resource template, CustomCellWriteHandler handler) throws IOException {
        if (StringUtils.isBlank(fileName)) {
            fileName = DateUtils.parseTime(LocalDateTime.now(), DateUtils.TimeFormat.SHORT_DATE_PATTERN_LINE);
        }
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        // 接口Response返回
        ExcelWriter writer = EasyExcel.write(response.getOutputStream()).withTemplate(template.getInputStream()).build();
        // 操作sheet(注册自定义的CellWriteHandler)
        map.forEach((sheetInfo, list) -> {
            WriteSheet sheet;
            if (handler != null) {
                sheet = EasyExcel.writerSheet(sheetInfo.getSheetIndex()).registerWriteHandler(handler).build();
            } else {
                sheet = EasyExcel.writerSheet(sheetInfo.getSheetIndex()).build();
            }
            writer.fill(list, FillConfig.builder().forceNewRow(Boolean.TRUE).build(), sheet).fill(extra, sheet);
        });
        // 设置强制计算公式：不然公式会以字符串的形式显示在excel中
        Workbook workbook = writer.writeContext().writeWorkbookHolder().getWorkbook();
        map.forEach((sheetInfo, list) -> workbook.setSheetName(sheetInfo.getSheetIndex(), sheetInfo.getSheetName()));
        workbook.setForceFormulaRecalculation(true);
        // 数据刷新
        writer.finish();
    }

    /**
     * @param filePath 文件全路径
     * @param colsInfo 表中每列对应的字段名称和类型
     * @param clazz    返回的实体(按属性值顺序赋值，超出部分字段赋null值)
     * @description: 同步单线程读取excel信息(默认从第0行开始读取, 并读取至第一个sheet为止)
     * @author: 白剑民
     * @date: 2022-07-17 14:06:38
     * @return: java.util.List<T>
     * @version: 1.0
     */
    public static <T> List<T> readExcelSingleThread(String filePath, LinkedHashMap<String, Class<?>> colsInfo,
                                                    Class<T> clazz) {
        List<LinkedHashMap<String, Object>> list = readExcel(filePath, null, colsInfo, 0,
                1, null, clazz);
        return transfer(list, clazz);
    }

    /**
     * @param list  excel读取数据
     * @param clazz 待转换目标实体
     * @description: excel读取结果转换
     * @author: 白剑民
     * @date: 2022-11-28 18:00:30
     * @return: java.util.List<T>
     * @version: 1.0
     */
    private static <T> List<T> transfer(List<LinkedHashMap<String, Object>> list, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        try {
            for (LinkedHashMap<String, Object> m : list) {
                T t = clazz.newInstance();
                // 取所有属性字段
                Field[] fields = clazz.getDeclaredFields();
                // 赋予所有属性字段的写权限
                AccessibleObject.setAccessible(fields, true);
                // 获取excel表的数据实体属性字段数
                int size = m.entrySet().size();
                // 由于赋值按传入的实体字段值顺序赋予
                // 所以传入的实体字段数必须大于等于excel实体字段数
                // 传入的实体字段数如果超过excel实体字段数，那么赋予null值
                if (fields.length >= size) {
                    // 属性值指针，每次赋值递增1，直至最后一个属性
                    AtomicInteger point = new AtomicInteger(0);
                    m.forEach((key, value) -> {
                        int num = point.intValue();
                        Object val = null;
                        // 判断递增边界，超出赋予null值
                        if (num < size) {
                            val = value;
                        }
                        try {
                            // 赋值
                            fields[num].set(t, val);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        // 递增
                        point.getAndIncrement();
                    });
                    result.add(t);
                } else {
                    throw new DbbException(SystemErrorCode.SERVER_EXCEPTION);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("excel实体转换出错: {}", e.getMessage());
        }
        return result;
    }

    /**
     * @param fileName    带后缀的文件名
     * @param inputStream 输入流
     * @param colsInfo    表中每列对应的字段名称和类型
     * @param startRow    从第几行开始读取
     * @param endSheet    读到第endSheet为止(包含该sheet)
     * @param clazz       目标实体
     * @description: 同步单线程读取excel信息
     * @author: 白剑民
     * @date: 2022-07-22 12:59:51
     * @return: java.util.List<java.util.LinkedHashMap < java.lang.String, java.lang.Object>>
     * @version: 1.0
     */
    public static <T> List<T> readExcelSingleThread(String fileName,
                                                    InputStream inputStream,
                                                    LinkedHashMap<String, Class<?>> colsInfo,
                                                    int startRow, int endSheet, Class<T> clazz) {
        List<LinkedHashMap<String, Object>> list =
                readExcel(fileName, inputStream, colsInfo, startRow, endSheet, null, clazz);
        return transfer(list, clazz);
    }

    /**
     * @param filePath 文件全路径
     * @param colsInfo 表中每列对应的字段名称和类型
     * @description: 同步单线程读取excel信息(默认从第0行开始读取, 并读取至第一个sheet为止)
     * @author: 白剑民
     * @date: 2022-07-09 21:14:08
     * @return: java.util.List<java.util.LinkedHashMap < java.lang.String, java.lang.Object>>
     * @version: 1.0
     */
    public static List<LinkedHashMap<String, Object>> readExcelSingleThread(String filePath, LinkedHashMap<String,
            Class<?>> colsInfo) {
        return readExcelSingleThread(filePath, colsInfo, 0, 1);
    }

    /**
     * @param filePath 文件全路径
     * @param colsInfo 表中每列对应的字段名称和类型
     * @param startRow 从第几行开始读取
     * @param endSheet 读到第endSheet为止(包含该sheet)
     * @description: 同步单线程读取excel信息
     * @author: 白剑民
     * @date: 2022-07-09 21:14:08
     * @return: java.util.List<java.util.LinkedHashMap < java.lang.String, java.lang.Object>>
     * @version: 1.0
     */
    public static <T> List<LinkedHashMap<String, Object>> readExcelSingleThread(String filePath, LinkedHashMap<String, Class<?>> colsInfo,
                                                                                int startRow, int endSheet) {
        return readExcel(filePath, null, colsInfo, startRow, endSheet, null, null);
    }

    /**
     * @param fileName    带后缀的文件名
     * @param inputStream 输入流
     * @param colsInfo    表中每列对应的字段名称和类型
     * @param startRow    从第几行开始读取
     * @param endSheet    读到第endSheet为止(包含该sheet)
     * @description: 同步单线程读取excel信息
     * @author: 白剑民
     * @date: 2022-07-22 12:59:51
     * @return: java.util.List<java.util.LinkedHashMap < java.lang.String, java.lang.Object>>
     * @version: 1.0
     */
    public static <T> List<LinkedHashMap<String, Object>> readExcelSingleThread(String fileName,
                                                                                InputStream inputStream,
                                                                                LinkedHashMap<String, Class<?>> colsInfo,
                                                                                int startRow, int endSheet) {
        return readExcel(fileName, inputStream, colsInfo, startRow, endSheet, null, null);
    }

    /**
     * @param fileName    带后缀的文件名
     * @param inputStream 输入流
     * @param colsInfo    表中每列对应的字段名称和类型
     * @description: 同步单线程读取excel信息
     * @author: 白剑民
     * @date: 2022-07-22 12:59:51
     * @return: java.util.List<java.util.LinkedHashMap < java.lang.String, java.lang.Object>>
     * @version: 1.0
     */
    public static <T> List<LinkedHashMap<String, Object>> readExcelSingleThread(String fileName,
                                                                                InputStream inputStream,
                                                                                LinkedHashMap<String, Class<?>> colsInfo) {
        return readExcel(fileName, inputStream, colsInfo, 0, 1, null, null);
    }

    /**
     * @param filePath 文件全路径
     * @param colsInfo 表中每列对应的字段名称和类型
     * @param model    非阻塞生产者-消费者线程模型
     * @description: 异步多线程读取excel信息，由于是异步读取，调用者需自定义消费逻辑(默认从第0行开始读取, 并读取至第一个sheet为止)
     * @author: 白剑民
     * @date: 2022-07-09 21:14:08
     * @version: 1.0
     */
    public static void readExcelAsync(String filePath, LinkedHashMap<String, Class<?>> colsInfo,
                                      ThreadNoBlockingModel model) {
        readExcel(filePath, null, colsInfo, 0, 1, model, null);
    }

    /**
     * @param filePath 文件全路径
     * @param colsInfo 表中每列对应的字段名称和类型
     * @param startRow 从第几行开始读取
     * @param endSheet 读到第endSheet为止(包含该sheet)
     * @param model    非阻塞生产者-消费者线程模型
     * @description: 异步多线程读取excel信息，由于是异步读取，调用者需自定义消费逻辑(可指定读取的起始行与终止Sheet)
     * @author: 白剑民
     * @date: 2022-07-09 21:14:08
     * @return: java.util.List<java.util.LinkedHashMap < java.lang.String, java.lang.Object>>
     * @version: 1.0
     */
    public static List<LinkedHashMap<String, Object>> readExcelAsync(String filePath, LinkedHashMap<String, Class<?>> colsInfo,
                                                                     int startRow, int endSheet, ThreadNoBlockingModel model) {
        return readExcel(filePath, null, colsInfo, startRow, endSheet, model, null);
    }

    /**
     * @param filePathOrName 文件全路径或带后缀的文件名
     * @param inputStream    文件输入流
     * @param colsInfo       表中每列对应的字段名称和类型
     * @param startRow       从第几行开始读取
     * @param endSheet       读到第endSheet为止(包含该sheet)
     * @param model          非阻塞生产者-消费者线程模型
     * @description: 异步多线程或同步单线程读取excel信息
     * @author: 白剑民
     * @date: 2022-06-29 09:27:11
     * @return: java.util.List<java.util.LinkedHashMap < java.lang.String, java.lang.Object>>
     * @version: 1.2
     */
    public static <T> List<LinkedHashMap<String, Object>> readExcel(String filePathOrName, InputStream inputStream,
                                                                    LinkedHashMap<String, Class<?>> colsInfo,
                                                                    int startRow, int endSheet, ThreadNoBlockingModel model,
                                                                    Class<T> clazz) {
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        Workbook wb;
        // 建立输入流，如果传了inputStream就使用传入的
        try (InputStream input = inputStream != null ? inputStream : new FileInputStream(filePathOrName)) {

            // 根据文件格式(2003或者2007)来初始化
            if (filePathOrName.endsWith(XLSX_FILE_PREFIX)) {
                wb = new XSSFWorkbook(input);
            } else {
                wb = new HSSFWorkbook(input);
            }
            int sheetNumber = wb.getNumberOfSheets();
            if (sheetNumber < 1) {
                return result;
            }
            if (endSheet != 0 && endSheet < sheetNumber) {
                sheetNumber = endSheet;
            }
            // 循环sheet
            for (int numSheet = 0; numSheet < sheetNumber; numSheet++) {
                // 获得表单
                Sheet sheet = wb.getSheetAt(numSheet);
                // 获取总行数
                int rowNum = sheet.getLastRowNum();
                if (rowNum < startRow) {
                    return result;
                }
                // 表头读取
                readHeader(colsInfo, sheet);
                // 异步读取时定义生产者线程数
                int producerThreadNum;
                // 异步读取时行数除以线程数向上取整即等于每个线程要处理的行数
                int pageNum;
                // 定义一个线程相互等待的障栅，在异步读取时所有生产者线程都执行完毕后以空对象给出终止信号量
                CyclicBarrier cyclicBarrier;
                if (model == null) {
                    result = executeRead(null, null, startRow,
                            rowNum + 1, colsInfo, sheet, null);
                } else {
                    producerThreadNum = 2;
                    pageNum = (int) Math.ceil((double) rowNum / producerThreadNum);
                    cyclicBarrier = new CyclicBarrier(2, model.new Producer(new Task()));
                    // 初始化生产者线程池
                    Executor producerPool = DbbThreadPool.initThreadPool();
                    // 遍历excel行，将读取结果放入生产者线程
                    for (int i = 0; i < producerThreadNum; i++) {
                        final int finalI = i;
                        producerPool.execute(() -> {
                            // 初始化读取的起始行，跳过标题行读取
                            int initRow = finalI == 0 ? 1 : finalI * pageNum;
                            // 定义终止行
                            int endRow = (finalI + 1) * pageNum;
                            executeRead(model, cyclicBarrier, initRow, endRow, colsInfo, sheet, producerPool);
                        });
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error("excel读取出错: {}, 错误行: {}", ex.getMessage(), ex.getStackTrace()[0]);
        }
        return result;
    }

    /**
     * @param model        非阻塞生产者-消费者线程模型
     * @param producerPool 生产者线程池
     * @param initRow      读取起始行
     * @param endRow       读取终止行
     * @param colsInfo     表头信息
     * @param sheet        sheet页
     * @description: 执行异步读取Excel
     * @author: 白剑民
     * @date: 2022-07-10 10:11:19
     * @return: java.util.concurrent.CompletableFuture<java.util.List < java.util.LinkedHashMap < java.lang.String, java.lang.Object>>>
     * @version: 1.0
     */
    private static List<LinkedHashMap<String, Object>> executeRead(ThreadNoBlockingModel model, CyclicBarrier cyclicBarrier,
                                                                   int initRow, int endRow, LinkedHashMap<String, Class<?>> colsInfo,
                                                                   Sheet sheet, Executor producerPool) {
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        // 循环行
        for (int numRow = initRow; numRow < endRow; numRow++) {
            // 获得行数据
            Row row = sheet.getRow(numRow);
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            Set<String> keySet = colsInfo.keySet();
            int y = 0;
            // 是否有效行,有些行为全空的(原来是有数据,然后选中后delete,在读取时还是会读进来)
            boolean isValidRow = false;
            // 循环单行列
            for (String key : keySet) {
                Object cellValue = getValueForCell(row.getCell(y), colsInfo.get(key));
                if (cellValue != null && cellValue.toString().trim().length() > 0) {
                    isValidRow = true;
                }
                map.put(key, cellValue);
                y++;
            }
            if (isValidRow) {
                // 加载excel行数据
                if (model == null) {
                    result.add(map);
                } else {
                    Task task = new Task();
                    task.setIndex(numRow);
                    task.setData(map);
                    // 放入生产者线程
                    producerPool.execute(model.new Producer(task));
                }
            }
        }
        if (cyclicBarrier != null) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
                // 待所有生产者线程执行完毕
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
                log.error("excel生产者线程意外中断: {}", e.getMessage());
            }
        }
        return result;
    }

    /**
     * @param colsInfo 表头map
     * @param sheet    sheet页
     * @description: 表头读取
     * @author: 白剑民
     * @date: 2022-07-09 18:24:16
     * @version: 1.0
     */
    private static void readHeader(LinkedHashMap<String, Class<?>> colsInfo, Sheet sheet) {
        // 如果没有定义表头，则默认获取第一行作为表头
        if (colsInfo.isEmpty()) {
            // 获取第一行
            Row firstRow = sheet.getRow(0);
            // 读取到空时结束
            boolean isEnd = false;
            // 为防止意外情况读不到空而产生死循环，默认设置最大读取列数为300
            int maxCol = 0;
            while (!isEnd && maxCol <= READ_HEAD_END) {
                Object cellValue = getValueForCell(firstRow.getCell(maxCol), String.class);
                if (cellValue != null && !"".equals(cellValue.toString().trim())) {
                    colsInfo.put(cellValue.toString(), String.class);
                } else {
                    isEnd = true;
                }
                maxCol++;
            }
        }
    }

    /**
     * @param cell 单元格对象
     * @param type 单元格数据类型
     * @description: 获取单元格数据
     * @author: 白剑民
     * @date: 2022-07-09 18:23:42
     * @return: java.lang.Object
     * @version: 1.0
     */
    private static Object getValueForCell(Cell cell, Class<?> type) {
        Object value;
        if (cell == null) {
            return null;
        }
        // TODO 读取excel时 存在只需要获取它的值而不是对应的公式，读取公式单元格时需要判断当前单元格的数据类型，然后调用不同的读取方法
        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }
        // 根据cell中的类型来输出数据
        switch (cellType) {
            case NUMERIC:
                DecimalFormat df = new DecimalFormat("#.####");
                value = cell.getNumericCellValue();
                value = df.format(value);
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case FORMULA:
                value = cell.getCellFormula();
                break;
            default:
                value = cell.getStringCellValue();
                break;
        }
        if (value == null || value.toString().length() == 0) {
            return value;
        }
        String valueStr = value.toString();
        if (type == String.class) {
            return valueStr;
        }
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(valueStr);
        }
        if (type == long.class || type == Long.class) {
            return Long.parseLong(valueStr);
        }
        if (type == float.class || type == Float.class) {
            return Float.parseFloat(valueStr);
        }
        if (type == double.class || type == Double.class) {
            return Double.parseDouble(valueStr);
        }
        if (type == Date.class) {
            try {
                Instant instant = cell.getDateCellValue().toInstant();
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDate localDate = instant.atZone(zoneId).toLocalDate();
                return localDate.toString();
            } catch (Exception e) {
                // 在日期读取异常时，可能是读取到了未知的excel自定义日期格式，或者读取到了中文表头
                if (cellType == CellType.STRING) {
                    return cell.getStringCellValue();
                }
                e.printStackTrace();
                log.error("excel读取日期单元格出错，错误信息: {}", e.getMessage());
            }
        }
        return value;
    }

    /**
     * @param fileName     文件名
     * @param list         数据列表
     * @param pojoClass    数据实体类
     * @param excelType    excel格式
     * @param fileTypePath 文件类型地址
     * @description: 生成本地excel文件
     * @author: 白剑民
     * @date: 2022-07-10 21:15:17
     * @version: 1.0
     */
    public static String exportLocalExcel(String fileName, Collection<?> list,
                                          Class<?> pojoClass, ExcelTypeEnum excelType, String fileTypePath) throws IOException {
        String fileSuffix;
        // 根据入参枚举判断excel导出格式
        if (excelType == ExcelTypeEnum.XLSX) {
            fileSuffix = ".xlsx";
        } else {
            fileSuffix = ".xls";
        }
        if (StringUtils.isBlank(fileName)) {
            // 当前日期
            fileName = DateUtils.parseTime(LocalDateTime.now(), DateUtils.TimeFormat.SHORT_DATE_PATTERN_LINE);
        }
        OutputStream outputStreamExcel;
        File folderFile = new File(System.getProperty("user.dir") + fileTypePath);
        //如果文件夹不存在  创建文件夹
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        File tmpFile = new File(System.getProperty("user.dir") + fileTypePath + fileName + fileSuffix);
        if (!tmpFile.getParentFile().exists()) {
            tmpFile.getParentFile().mkdirs();//创建目录
        }
        if (!tmpFile.exists()) {
            tmpFile.createNewFile();//创建文件
        }

        try {
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), pojoClass, list);
            outputStreamExcel = new FileOutputStream(tmpFile);
            workbook.write(outputStreamExcel);
            log.info("生成excel文件");
            outputStreamExcel.flush();
            outputStreamExcel.close();
            return fileName + fileSuffix;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("excel导出错误: {}, 错误行: {}", e.getMessage(), e.getStackTrace()[0]);
        }
        return null;
    }

    /**
     * @param response  响应对象
     * @param fileName  不带后缀的文件名
     * @param list      多sheet 信息 （map key固定为title、entity、data：
     *                  title:存放sheet名称、表头名称 ,value数据类型为ExportParams
     *                  entity:导出的数据对应的实体类型 ,value数据类型为Class
     *                  data:对应sheet中导出的数据 ,value数据类型为Collection<T>）
     * @param excelType excel格式
     * @description: 导出多sheet Excel
     * @author: 张立檑
     * @date: 2023-02-10 10:48:57
     * @return: java.lang.String
     * @version: 1.0
     */
    public static void exportSheetsExcel(HttpServletResponse response, String fileName, List<Map<String, Object>> list,
                                         ExcelTypeEnum excelType) {
        if (StringUtils.isBlank(fileName)) {
            // 当前日期
            fileName = DateUtils.parseTime(LocalDateTime.now(), DateUtils.TimeFormat.SHORT_DATE_PATTERN_LINE);
        }
        ServletOutputStream out = null;
        try {
            Workbook workbook;
            // 编码格式UTF-8
            response.setCharacterEncoding("UTF-8");
            String fileSuffix;
            // 根据入参枚举判断excel导出格式
            if (excelType == ExcelTypeEnum.XLSX) {
                workbook = ExcelExportUtil.exportExcel(list, ExcelType.XSSF);
                response.setHeader("content-Type",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                fileSuffix = ".xlsx";
            } else {
                workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
                response.setHeader("content-Type", "application/vnd.ms-excel");
                fileSuffix = ".xls";
            }
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + fileSuffix);
            out = response.getOutputStream();
            workbook.write(out);
            workbook.close();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("excel导出错误: {}, 错误行: {}", e.getMessage(), e.getStackTrace()[0]);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("输出流关闭出错: {}", e.getMessage());
                }
            }
        }
    }
}
