package com.tianshu.common.utils;

import cn.hutool.core.date.DateUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

@Component
public class RandomCodeUtil {

    //生成随机码的固定长度
    private final static int LENGTH = 15;

    /**
     * 获取数据的序列id 17位时间戳+15位随机数
     * @return 序列id 32位
     */
    public static String getDataId(){
        StringBuffer resultBuffer = new StringBuffer();
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        resultBuffer = resultBuffer.append(dataFormat.format(new Date())).append(usingMath(LENGTH));
        return resultBuffer.toString();
    }

    /**
     * 获取随机数
     * @return
     */
    public static Long getDataIdLong(){
        String mathLong = usingMath(15);
        return Long.parseLong(mathLong);
    }

    /**
     * 用于生成指定长度的随机序列码
     * @param length 指定的长度
     * @return 返回的随机码
     */
    public static String usingMath(int length) {
//        String alphabetsInUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        String alphabetsInLowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        // create a super set of all characters
        String allCharacters =  numbers;
        // initialize a string to hold result
        StringBuffer randomString = new StringBuffer();
        // loop for 10 times
        for (int i = 0; i < length; i++) {
            // generate a random number between 0 and length of all characters
            int randomIndex = (int)(Math.random() * allCharacters.length());
            // retrieve character at index and add it to result
            randomString.append(allCharacters.charAt(randomIndex));
        }
        return randomString.toString();
    }



    /**
     * @param china (字符串 汉字)
     * @return 汉字转拼音 其它字符不变
     */
    public static String getPinyin(String china) {
        HanyuPinyinOutputFormat formart = new HanyuPinyinOutputFormat();
        formart.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        formart.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        formart.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] arrays = china.trim().toCharArray();
        String result = "";
        try {
            for (int i = 0; i < arrays.length; i++) {
                char ti = arrays[i];
                if (Character.toString(ti).matches("[\\u4e00-\\u9fa5]")) { //匹配是否是中文
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(ti, formart);
                    result += temp[0];
                } else {
                    result += ti;
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 重置密码
     *
     * @param name
     * @return
     */
    public static String getBackPassword(String name) {
        //获取姓名的首字母
        char[] namePinyin = name.toCharArray();
        StringBuffer nameBuffer = new StringBuffer();
        for (int i = 0; i < namePinyin.length; i++) {
            String pinyin1 = getPinyin(String.valueOf(namePinyin[i]));
            char[] namePinyins = pinyin1.toCharArray();
            nameBuffer.append(namePinyins);
        }
//        char[] companyPinyin = companyName.toCharArray();
//        StringBuffer companyBuffer = new StringBuffer();
//        for (int j = 0; j < companyPinyin.length; j++) {
//            String pinyin2 = getPinyin(String.valueOf(companyPinyin[j]));
//            char[] companyPinyins = pinyin2.toCharArray();
//            companyBuffer.append(companyPinyins[0]);
//        }
        //这里与王俊姐和郝工沟通后暂时写死为天书易销 tsyx
        StringBuffer append = nameBuffer.append("tsyx");
        System.out.println(append.toString());
        return append.toString();
    }

    /**
     * 创建账号
     *
     * @param name
     * @param company
     * @return
     */
    public static String getUserName(String name, String company) {
        //获取姓名的首字母
        char[] namePinyin = name.toCharArray();
        StringBuffer nameBuffer = new StringBuffer();
        for (int i = 0; i < namePinyin.length; i++) {
            String pinyin1 = getPinyin(String.valueOf(namePinyin[i]));
            char[] namePinyins = pinyin1.toCharArray();
            nameBuffer.append(namePinyins[0]);
        }
        char[] companyPinyin = company.toCharArray();
        StringBuffer companyName = new StringBuffer();
        for (int j = 0; j < companyPinyin.length; j++) {
            String pinyin2 = getPinyin(String.valueOf(companyPinyin[j]));
            char[] companyPinyins = pinyin2.toCharArray();
            companyName.append(companyPinyins[0]);
        }
        StringBuffer append = nameBuffer.append("@").append(companyName);
        return append.toString();
    }
}
