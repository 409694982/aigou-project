package cn.itsource.aigou.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author yaohuaipeng
 * @date 2018/10/26-16:16
 */
public class StrUtils {
    /**
     * 把传入的分隔符来分隔字符串转，换字符串数组
     *
     * @param str
     * @param splicStr
     * @return
     */
    public static String[] splitStr2StrArr(String str,String splicStr) {
        if (str != null && !str.equals("")) {
            return str.split(splicStr);
        }
        return null;
    }


    /**
     * 把把传入的分隔符来分隔字符串，转换List的Long
     *
     * @param str
     * @return
     */
    public static List<Long> splitStr2LongArr(String str,String splicStr) {
        String[] strings = splitStr2StrArr(str,splicStr);
        if (strings == null) return null;

        List<Long> result = new ArrayList<>();
        for (String string : strings) {
            result.add(Long.parseLong(string));
        }

        return result;
    }

    public static String getRandomString(int length) {
        String str = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(10);
            sb.append(str.charAt(number));
        }
        return sb.toString();

    }

    public static String getComplexRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String convertPropertiesToHtml(String properties){
        //1:容量:6:32GB_4:样式:12:塑料壳
        StringBuilder sBuilder = new StringBuilder();
        String[] propArr = properties.split("_");
        for (String props : propArr) {
            String[] valueArr = props.split(":");
            sBuilder.append(valueArr[1]).append(":").append(valueArr[3]).append("<br>");
        }
        return sBuilder.toString();
    }

}
