package top.dabaibai.core.utils;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * @description: 中文转拼音工具类
 * @author: 白剑民
 * @dateTime: 2022/11/21 11:10
 */
@Slf4j
public class PinYinUtils {

    /**
     * @param str 待转换字符串
     * @description: 将中文转拼音首字母大写
     * @author: 白剑民
     * @date: 2022-11-21 11:10:40
     * @return: java.lang.String
     * @version: 1.0
     */
    public static String getPinYinHeadChar(String str) {
        StringBuilder convert = new StringBuilder();
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert.append(pinyinArray[0].charAt(0));
            } else {
                convert.append(word);
            }
        }
        return convert.toString().toUpperCase();
    }

}
