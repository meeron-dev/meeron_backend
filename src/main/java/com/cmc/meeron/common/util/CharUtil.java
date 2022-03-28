package com.cmc.meeron.common.util;

public class CharUtil {

    public static boolean isEnglish(char ch){
        return (ch >= (int)'A' && ch <= (int)'Z') || (ch >= (int)'a' && ch <= (int)'z');
    }

    public static boolean isKorean(char ch) {
        return ch >= Integer.parseInt("AC00", 16) && ch <= Integer.parseInt("D7A3", 16);
    }

    public static boolean isNumber(char ch) {
        return ch >= (int)'0' && ch <= (int)'9';
    }

    public static boolean isSpecial(char ch) {
        return (ch >= (int)'!' && ch <= (int)'/') // !"#$%&'()*+,-./
            || (ch >= (int)':' && ch <= (int)'@') //:;<=>?@
            || (ch >= (int)'[' && ch <= (int)'`') //[\]^_`
            || (ch >= (int)'{' && ch <= (int)'~'); //{|}~ }
    }
}
