package com.lgz.grace.api.utils.num;

import com.lgz.grace.api.utils.collection.ListUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class NumUtil {
    private static final DecimalFormat myformat = new DecimalFormat("0.00");
    private static final Random r = new Random();

    public static long calcRang(long value, Long min, Long max) {
        if (min != null) {
            value = Math.max(min, value);
        }
        if (max != null) {
            value = Math.min(max, value);
        }
        return value;
    }

    public static boolean isRatio(int numerator, final int denominator) {
        if (numerator < 1 || denominator < 1) {
            return false;
        }
        if (numerator >= denominator) {
            return true;
        }
        return numerator > randomInt(denominator);
    }

    public static boolean isPercentage(int numerator) {
        return isRatio(numerator, 100);
    }

    public static boolean isTrue(Boolean b) {
        return Boolean.TRUE.equals(b);
    }

    public static boolean isTrue(String b) {
        return "true".equalsIgnoreCase(b);
    }

    public static boolean gt(Number number1, Number number2) {
        if (number1 == null || number2 == null) {
            return false;
        }
        if (number1 instanceof Double || number2 instanceof Double) {
            return number1.doubleValue() > number2.doubleValue();
        } else {
            return number1.longValue() > number2.longValue();
        }
    }

    public static boolean ge(Number number1, Number number2) {
        if (number1 == null || number2 == null) {
            return false;
        }
        if (number1 instanceof Double || number2 instanceof Double) {
            return number1.doubleValue() >= number2.doubleValue();
        } else {
            return number1.longValue() >= number2.longValue();
        }
    }

    public static boolean lt(Number number1, Number number2) {
        if (number1 == null || number2 == null) {
            return false;
        }
        if (number1 instanceof Double || number2 instanceof Double) {
            return number1.doubleValue() < number2.doubleValue();
        } else {
            return number1.longValue() < number2.longValue();
        }
    }

    public static boolean le(Number number1, Number number2) {
        if (number1 == null || number2 == null) {
            return false;
        }
        if (number1 instanceof Double || number2 instanceof Double) {
            return number1.doubleValue() <= number2.doubleValue();
        } else {
            return number1.longValue() <= number2.longValue();
        }
    }

    public static int randomInt(int i) {
        return r.nextInt(i);
    }

    public static int compare(Number number1, Number number2, boolean sTob) {
        if (number1 == null && number2 == null) {
            return 0;
        } else if (number1 == null) {
            return sTob ? -1 : 1;
        } else if (number2 == null) {
            return sTob ? 1 : -1;
        }
        if (number1 instanceof Double || number2 instanceof Double
                || number1 instanceof Float || number2 instanceof Float) {
            Double value1 = number1.doubleValue();
            Double value2 = number2.doubleValue();
            return sTob ? value1.compareTo(value2) : value2.compareTo(value1);
        } else {
            Long value1 = number1.longValue();
            Long value2 = number2.longValue();
            return sTob ? value1.compareTo(value2) : value2.compareTo(value1);
        }
    }

    /**
     * 将字符串转化为字节数组
     *
     * @param str
     * @return
     * @throws RuntimeException
     */
    public static byte[] toBytes(String str) throws RuntimeException {
        if (str.length() > 0 && (str.length() + 1) % 3 == 0) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream((str.length() + 1) / 3);
            int length = str.length() - 1;
            for (int i = 0; i < length; i += 3) {
                char c1 = Character.toUpperCase(str.charAt(i));
                char c2 = Character.toUpperCase(str.charAt(i + 1));
                if (validHexChar(c1) && validHexChar(c2)) {
                    byte b = parseByte4Hex(c2);
                    b = (byte) (b | (parseByte4Hex(c1) << 4));
                    baos.write(b);
                } else {
                    throw new RuntimeException("数据格式不正确!要求为00 01 02的格式");
                }
            }
            byte[] bytes = baos.toByteArray();
            try {
                baos.close();
            } catch (IOException e) {
            }
            return bytes;
        } else {
            throw new RuntimeException("数据长度格式不正确!要求为00 01 02的格式");
        }
    }

    /**
     * 验证是否是合法16进制字符
     *
     * @param c
     * @return
     */
    private static boolean validHexChar(char c) {
        return (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
    }

    /**
     * 将字节数组转化为16进制字符串
     *
     * @param bytes
     * @return
     */
    public static String toHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder(bytes.length * 3);
        if (bytes.length > 0) {
            builder.append(toHexString(bytes[0]));
            for (int i = 1; i < bytes.length; i++) {
                builder.append(" ").append(toHexString(bytes[i]));
            }
        }
        return builder.toString();
    }

    /**
     * 将16进制字符转化为数值
     *
     * @param c1
     * @return
     */
    public static byte parseByte4Hex(char c1) {
        byte b = 0;
        if (c1 >= 'A' && c1 <= 'F') {
            b = (byte) (c1 - 'F' + 15);
        } else if (c1 >= '0' && c1 <= '9') {
            b = (byte) (c1 - '0');
        }
        return b;
    }

    /**
     * 将一个字节转化为16进制字符串
     *
     * @param b
     * @return
     */
    public static String toHexString(byte b) {
        char c[] = {'0', '0'};
        byte temp = (byte) (b & 0x0f);
        if (temp > 9) {
            c[1] = (char) ('A' + temp - 10);
        } else {
            c[1] = (char) ('0' + temp);
        }
        temp = (byte) (b >> 4 & 0x0f);
        if (temp > 9) {
            c[0] = (char) ('A' + temp - 10);
        } else {
            c[0] = (char) ('0' + temp);
        }
        return new String(c);
    }

    /**
     * 计算和
     *
     * @param number
     * @return
     */
    public static double sum(Number... number) {
        double result = 0;
        if (number != null) {
            for (Number num : number) {
                if (num != null) {
                    result += num.doubleValue();
                }
            }
        }
        return result;
    }

    /**
     * 计算和
     *
     * @param number
     * @return
     */
    public static double sum(List<? extends Number> number) {
        double result = 0;
        if (number != null) {
            for (Number num : number) {
                if (num != null) {
                    result += num.doubleValue();
                }
            }
        }
        return result;
    }

    /**
     * 将数字格式化成2位小数
     *
     * @param number
     * @return
     */
    public static String fmt2p(Number number) {
        return myformat.format(number);
    }

    public static boolean anyEquals(Number obj, Number... param) {
        if (ListUtil.isNotEmpty(param)) {
            for (Number o : param) {
                if (NumUtil.isEquals(obj, o)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean anyNotEquals(Number obj, Number... param) {
        return !anyEquals(obj, param);
    }

    public static boolean isEquals(Number o1, Number o2) {
        return eq(o1, o2);
    }

    /**
     * 判断数值o1 和数值o2是否相等
     *
     * @param o1
     * @param o2
     * @return
     */
    public static boolean eq(Number o1, Number o2) {
        if (o1 == o2) {
            return true;
        } else if (o1 == null || o2 == null) {
            return false;
        } else {
            if (o1.equals(o2)) {
                return true;
            } else if (o1 instanceof Double || o1 instanceof Float || o1 instanceof BigDecimal
                    || o2 instanceof Double || o2 instanceof Float || o2 instanceof BigDecimal) {
                return o1.doubleValue() == o2.doubleValue();
            } else {
                return o1.longValue() == o2.longValue();
            }
        }
    }

    public static boolean isNotEquals(Number o1, Number o2) {
        return !eq(o1, o2);
    }

    public static boolean neq(Number o1, Number o2) {
        return !eq(o1, o2);
    }

    public static Short pShort(Object o) {
        if (o instanceof Short) {
            return (Short) o;
        } else if (o instanceof Number) {
            return ((Number) o).shortValue();
        } else if (o instanceof String) {
            try {
                return Short.parseShort((String) o);
            } catch (NumberFormatException e) {
                Double d = parseDouble(o);
                if (d != null)
                    return d.shortValue();
            }
        }
        return null;
    }

    public static Short parseShort(Object o) {
        return pShort(o);
    }

    public static Byte pByte(Object o) {
        if (o instanceof Byte) {
            return (Byte) o;
        } else if (o instanceof Number) {
            return ((Number) o).byteValue();
        } else if (o instanceof String) {
            try {
                return Byte.parseByte((String) o);
            } catch (NumberFormatException e) {
                Double d = parseDouble(o);
                if (d != null)
                    return d.byteValue();
            }
        }
        return null;
    }

    public static Byte parseByte(Object o) {
        return pByte(o);
    }


    public static Integer parseInt(Object o) {
        return pInt(o);
    }

    public static Integer pInt(Object o) {
        if (o instanceof Integer) {
            return (Integer) o;
        } else if (o instanceof Number) {
            return ((Number) o).intValue();
        } else if (o instanceof String) {
            try {
                return Integer.parseInt((String) o);
            } catch (NumberFormatException e) {
                Double d = parseDouble(o);
                if (d != null)
                    return d.intValue();
            }
        }
        return null;
    }

    public static Long parseLong(Object o) {
        return pLong(o);
    }

    public static boolean pBool(Object o) {
        if (o == null) {
            return false;
        } else if (o instanceof Boolean) {
            return (Boolean) o;
        }
        return Boolean.parseBoolean(o.toString());
    }

    public static Boolean pBoolean(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Boolean) {
            return (Boolean) o;
        }
        return Boolean.parseBoolean(o.toString());
    }

    public static Long pLong(Object o) {
        if (o instanceof Long) {
            return (Long) o;
        } else if (o instanceof Number) {
            return ((Number) o).longValue();
        } else if (o instanceof String) {
            try {
                return Long.parseLong((String) o);
            } catch (NumberFormatException e) {
                Double d = parseDouble(o);
                if (d != null)
                    return d.longValue();
            }
        }
        return null;
    }

    /**
     * 如果是null 则返回0
     *
     * @param value
     * @return
     */
    public static int isNull0(Integer value) {
        return isNull(value, 0);
    }

    public static double isNull0(Double value) {
        return value == null ? 0 : value;
    }

    public static long isNull0(Long value) {
        return value == null ? 0 : value;
    }

    /**
     * 如果是null 则返回newValue
     *
     * @param value
     * @return
     */
    public static int isNull(Integer value, int newValue) {
        return value == null ? newValue : value;
    }

    /**
     * 如果是null
     *
     * @param value
     * @return
     */
    public static boolean isNull(Object value) {
        return value == null;
    }

    public static Float pFloat(Object o) {
        if (o instanceof Float) {
            return (Float) o;
        } else if (o instanceof Number) {
            return ((Number) o).floatValue();
        } else if (o instanceof String) {
            try {
                return Float.parseFloat((String) o);
            } catch (NumberFormatException e) {
                Double d = parseDouble(o);
                if (d != null)
                    return d.floatValue();
            }
        }
        return null;
    }

    public static Float parseFloat(Object o) {
        return pFloat(o);
    }

    public static Double pDouble(Object o) {
        if (o instanceof Double) {
            return (Double) o;
        } else if (o instanceof Number) {
            return ((Number) o).doubleValue();
        } else if (o instanceof String) {
            try {
                return Double.parseDouble((String) o);
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }


    public static BigDecimal pDecimal(Object o) {
        if (o instanceof BigDecimal) {
            return (BigDecimal) o;
        } else if (o instanceof Number) {
            return new BigDecimal(((Number) o).doubleValue());
        } else if (o instanceof String) {
            try {
                return new BigDecimal((String) o);
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    public static Double parseDouble(Object o) {
        return pDouble(o);
    }

    /**
     * Description: 四舍五入
     *
     * @param enDouble 被四舍五入值
     * @param rountNum 精确到的位数：如：0,保留整数；1，保留一位小数
     * @return
     * @Version1.0 2015-7-31 下午12:03:05 by 陈永旺（yw.chen02@10101111.com）创建
     */
    public static Double roundDouble(Double enDouble, int rountNum) {
        if (enDouble == null) {
            return null;
        }

        BigDecimal bd = new BigDecimal(enDouble);
        return bd.setScale(rountNum, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将一个long类型数字转化为字节数组 低字节在前 高字节在后
     *
     * @param value
     * @return
     */
    public static byte[] toBytes(long value) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (value & 0xff);
            value = value >> 8;
        }
        int len;
        for (len = bytes.length - 1; len > 0; len--) {
            if (bytes[len] != 0) {
                break;
            }
        }
        byte[] result = new byte[len + 1];
        System.arraycopy(bytes, 0, result, 0, len + 1);
        return result;
    }

    /**
     * 将字节数组转化为long数组 当bytes是空或者数据大于8字节则返回null 数组 低字节在前
     *
     * @param bytes
     * @return
     */
    public static Long parseFromBytes(byte[] bytes) {
        if (bytes != null && bytes.length > 0 && bytes.length < 9) {
            long value = 0;
            for (int i = bytes.length - 1; i > 0; i--) {
                value += (bytes[i] & 0xff);
                value = value << 8;
            }
            value += (bytes[0] & 0xff);
            return value;
        }
        return null;
    }

    private static final char[] convert62Arr = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };


    public static String convert62(long num) {
        StringBuilder builder = new StringBuilder(10);
        if (num < 0) {
            builder.append("-");
            num = Math.abs(num);
        }
        final int len = convert62Arr.length;
        do {
            int k = (int) (num % len);
            num = num / len;
            builder.append(convert62Arr[k]);
        } while (num > 0);

        return builder.toString();
    }

    public static Long convert62(String num) {
        if (num == null || num.isEmpty()) {
            return null;
        }
        int stop = -1;
        boolean flag = false;
        if (num.charAt(0) == '-') {
            flag = true;
            stop = 0;
        }
        long value = 0;
        for (int i = num.length() - 1; i > stop; i--) {
            char c = num.charAt(i);
            value *= convert62Arr.length;
            int idx = 0;
            for (; idx < convert62Arr.length; idx++) {
                if (convert62Arr[idx] == c) {
                    value += idx;
                    break;
                }
            }
        }

        if (flag) {
            value = (0 - value);
        }
        return value;
    }
}
