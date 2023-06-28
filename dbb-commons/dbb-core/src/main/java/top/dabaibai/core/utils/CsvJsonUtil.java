package top.dabaibai.core.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;


/**
 * @description: JSON对象与CSV相互转换服务类
 * @author: 张立檑
 * @dateTime: 2022\9\7 0007 16:14
 **/
@Slf4j
public class CsvJsonUtil {


    /**
     * CSV文件列分隔符
     */
    private static final String CSV_COLUMN_SEPARATOR = ",";

    private static final String CSV_COLUMN_SEPARATOR_BLANK = " ";
    /**
     * WIN CSV文件换行符
     */
    private static final String WIN_CSV_RN = "\r\n";

    /**
     * LINUX CSV文件换行符
     */
    private static final String LIN_CSV_RN = "\n";

    private static final String OS = "Windows";

    private static  String NEW_LINE;

    static {
        // 获取当前操作系统
        String osName = System.getProperty("os.name");
        // 默认设置换行符为linux换行符
        NEW_LINE = LIN_CSV_RN;
        // 如果是windows系统，则使用windows换行符
        if (osName.startsWith(OS)) {
            NEW_LINE = WIN_CSV_RN;
        }
    }


   /**
    * @param obj 需要转格式的json对象
    * @description:  将json对象转化为CSV格式字符串（只转化value）
    * @author: 张立檑
    * @date: 2022-10-17 10:36:38
    * @return: java.lang.String
    * @version: 1.0
    */
    public static String jsonToCsv(Object obj) {
        StringBuilder builder = new StringBuilder();
        // 没有标注数据时返回空串
        if (Objects.isNull(obj)) {
            return builder.toString();
        }
        // 将JSON转化为CSV格式需要的String
        return objectToString(obj, builder);
    }

    /**
     * @param obj 需要转格式的json对象
     * @param function 对JSONObject对象处理的函数
     * @description: 将json对象转化为CSV格式字符串（key value格式）
     * @author: 张立檑
     * @date: 2022-10-17 10:23:48
     * @return: java.lang.String
     * @version: 1.0
     */
    public static String jsonToCsv(Object obj, Function<JSONObject,JSONObject> function) {
        StringBuilder builder = new StringBuilder();
        // 没有标注数据时返回空串
        if (Objects.isNull(obj)) {
            return builder.toString();
        }
        return objectToString(obj, builder,function);
    }


    /**
     * @param obj 需要转格式的json对象
     * @param builder 最终返回结果
     * @param function 对JSONObject对象处理的函数
     * @description: 将json对象转化为CSV格式字符串
     * @author: 张立檑
     * @date: 2022-10-17 10:33:51
     * @return: java.lang.String
     * @version: 1.0
     */
    private static String objectToString(Object obj, StringBuilder builder, Function<JSONObject, JSONObject> function) {
        // 如果json是JSONObject对象则将其转为字符串格式
        if (obj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) obj;
            // 当处理函数不为空时，调用函数处理JSONObject对象
            if(function!=null){
                jsonObject=function.apply(jsonObject);
            }
            List<Map.Entry<String,Object>> values = new ArrayList<>(jsonObject.entrySet());
            // JSONObject对象参数数量
            int size = values.size();
            // 遍历JSONObject对象参数
            for (int i = 0; i < size; i++) {
                Map.Entry<String,Object> o = values.get(i);
                // 判断当前参数是否复杂数据类型，若是复制对象则递归调用解析对象，若是简单类型则直接转化为字符串
                if(isComplicatedType(o.getValue())){
                    builder.append(o.getKey()).append(CSV_COLUMN_SEPARATOR_BLANK).append(o.getValue().toString());
                }else {
                    objectToString(o.getValue(), builder,function);
                }
                builder.append(CSV_COLUMN_SEPARATOR_BLANK);
            }
        }
        // 若转化的JSON对象为JSONArray
        if (obj instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) obj;
            int arrSize = jsonArray.size();
            // 遍历JSONArray，递归解析数组中每个对象
            for(int j=0;j<arrSize;j++){
                if(j!=0){
                    builder.append(NEW_LINE);
                }
                objectToString(jsonArray.get(j), builder,function);
            }
        }
        return builder.toString();
    }


    /**
     * @param obj     json
     * @param builder String结果
     * @description: 将json转化为CSV格式的字符串
     * @author: 张立檑
     * @date: 2022-10-12 17:19:13
     * @return: java.lang.String
     * @version: 1.0
     */
    private static String objectToString(Object obj, StringBuilder builder) {
        if (obj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) obj;
            List<Object> values = new ArrayList<>(jsonObject.values());
            int size = values.size();
            int last = size - 1;
            for (int i = 0; i < size; i++) {
                Object o = values.get(i);
                if (isComplicatedType(o)) {
                    builder.append(o);
                } else {
                    objectToString(o, builder);
                }
                if (i == last) {
                    builder.append(NEW_LINE);
                } else {
                    builder.append(CSV_COLUMN_SEPARATOR);
                }
            }
        }
        if (obj instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) obj;
            for (Object aJsonArray : jsonArray) {
                objectToString(aJsonArray, builder);
            }
        }
        return builder.toString();
    }

    /**
     * @param obj 对象
     * @description: 判断对象是否能直接转化为String
     * @author: 张立檑
     * @date: 2022-10-12 17:21:08
     * @return: boolean
     * @version: 1.0
     */
    private static boolean isComplicatedType(Object obj) {
        return obj instanceof Integer || obj instanceof String ||
                obj instanceof Double || obj instanceof Float || obj instanceof Long || obj instanceof Boolean;
    }


    /**
     * @param str      文本内容
     * @param filePath 文件地址
     * @description: 生成CSV文件
     * @author: 张立檑
     * @date: 2022-09-16 16:44:20
     * @return: java.io.File
     * @version: 1.0
     */
    public static File stringToCsvFile(String str, String filePath) throws IOException {
        File file = new File(filePath);
        /*
         * Excel打开的CSV文件默认是ANSI编码，如果CSV文件的编码方式为UTF-8、Unicode等编码可能就会出现文件乱码的情况
         * 当前方法采用在文件头部添加BOM标记,使excel打开文件时使用UTF-8格式编码
         **/
        if(!file.getParentFile().exists()){
            file.mkdirs();
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        //导出内容不为空时将BOM标记拼接到导出内容，防止中文乱码
        if (StringUtils.isNotEmpty(str)) {
            fos.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        }
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        osw.append(str);
        osw.flush();
        fos.close();
        osw.close();
        return file;
    }
}

