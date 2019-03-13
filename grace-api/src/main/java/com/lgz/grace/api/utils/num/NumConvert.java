package com.lgz.grace.api.utils.num;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class NumConvert {
    private static final String MINUS = "Z";
    private static final char[] CHARS_62 = new char[]{'w', 'n', 'r', 'H', 'E', '1', 'd', 't', 'R', 'V', 's', 'l', 'x', 'k', 'U', 'g', '0', 'A', 'P', '5', 'm', 'K', 'M', 'F', 'b', 'f', 'O', '6', 'v', 'i', 'C', 'L', '4', 'h', 'j', 'I', 'q', 'D', 'a', 'Q', 'B', 'T', 'X', 'Y', 'y', '2', 'u', '9', 'S', '7', 'p', 'o', '3', 'G', 'N', 'e', 'z', '8', 'W', 'c', 'J'};
    private static final Map<Character, Integer> map;

    public NumConvert() {
    }

    public static String numTo62(long value) {
        StringBuilder result = new StringBuilder(20);
        boolean minus = value < 0L;
        if (minus) {
            value = Math.abs(value);
        }

        do {
            int idx = (int)(value % (long)CHARS_62.length);
            char c = getCharByIndex(idx);
            result.append(c);
            value /= (long)CHARS_62.length;
        } while(value != 0L);

        if (minus) {
            result.append("Z");
        }

        return result.reverse().toString();
    }

    public static Long str62ToLong(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else {
            int i = 0;
            if (value.startsWith("Z")) {
                ++i;
            }

            long result;
            for(result = 0L; i < value.length(); ++i) {
                char c = value.charAt(i);
                Integer temp = getValueByChar(c);
                if (temp == null) {
                    return null;
                }

                result *= (long)CHARS_62.length;
                result += (long)temp;
            }

            if (value.startsWith("Z")) {
                result = -result;
            }

            return result;
        }
    }

    public static Character getCharByIndex(int i) {
        return CHARS_62[i];
    }

    public static Integer getValueByChar(char c) {
        return (Integer)map.get(c);
    }

    static {
        map = new HashMap(CHARS_62.length);

        for(int i = 0; i < CHARS_62.length; ++i) {
            map.put(CHARS_62[i], i);
        }

    }
}
