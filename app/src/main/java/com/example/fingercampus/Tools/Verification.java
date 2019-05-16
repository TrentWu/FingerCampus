package com.example.fingercampus.Tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Verification {

    /**
     * 通过正则表达式验证手机号码
     * <p>
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、184、187、178、188、147
     * 联通号码段:130、131、132、155、156、176、185、186、145
     * 电信号码段:133、153、180、189、177、181
     * 虚拟运营商:170、171
     *
     * @param cellphone 需要验证的手机号
     * @return 验证结果
     */
    public static boolean phoneNumber(String cellphone) {
        //正则表达式
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[0-1,6-8])|(18[0-9]))\\d{8}$";
        //编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        //定义匹配器，验证手机号码
        Matcher matcher = pattern.matcher(cellphone);
        return matcher.matches();
    }

    /**
     * 使用正则表达式验证密码
     * 密码位数限制在6-16位
     * 可使用大小写英文字母，阿拉伯数字，部分特殊符号
     *
     * @param password 密码
     * @return 验证结果
     */
    public static boolean password(String password){
        //正则表达式
        String regex = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;,\\\\.\\[\\]<>/?！￥…（）—【】‘；：”“’。，、？]){6,16}$";
        //编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        //定义匹配器，验证密码
        Matcher matcher = pattern.matcher(password);
        return !matcher.matches();
    }
}
