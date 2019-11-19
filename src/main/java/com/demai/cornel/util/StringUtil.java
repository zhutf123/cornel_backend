/**
 * Copyright (c) 2019 dm.com. All Rights Reserved.
 */
package com.demai.cornel.util;

import com.google.common.base.Splitter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * ClassName:StringUtil Reason: TODO 字符串处理函数
 * Create By zhutf  19-10-6  下午4:29
 */
public class StringUtil extends StringUtils {
    public static final char CHAR_NEL = '\u0085'; // UNICODE NEXT LINE (类似CR+LF，一个字符表示回车换行）
    public static final char CHAR_LS = '\u2028'; // UNICODE LINE SEPARATOR （类似html <br>）
    public static final char CHAR_PS = '\u2029'; // UNICODE PARAGRAPH SEPARATOR (类似html <p>）

    public static String filterJSON(String jsonStr) {
        if (!StringUtils.isBlank(jsonStr)) {
            return jsonStr.replace(CHAR_NEL, ' ').replace(CHAR_LS, ' ').replace(CHAR_PS, ' ');
        }
        return jsonStr;
    }

    public static <T> T coalesce(T a, T b) {
        return a != null ? a : b;
    }

    public static <T> T coalesce(T a, T b, T c) {
        return a != null ? a : coalesce(b, c);
    }

    public static <T> T coalesce(T a, T b, T c, T d) {
        return a != null ? a : coalesce(b, c, d);
    }

    public static <T> T coalesce(T a, T b, T c, T d, T e) {
        return a != null ? a : coalesce(b, c, d, e);
    }

    public static <T> T coalesce(T... items) {
        for (T i : items)
            if (i != null)
                return i;
        return null;
    }

    /**
     * 检查指定的字符串列表是否不为空。
     */
    public static boolean isNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }

    /**
     * 只进行一次分割，例如： A=B=C时，只分割第一个=
     *
     * @param query
     * @param separator
     * @return
     */
    public static String[] splitOnce(String query, String separator) {
        int p = query.indexOf(separator);
        if (p > 0) {
            String key = query.substring(0, p);
            String value = query.substring(p + 1);
            return new String[] { key, value };
        } else {
            return new String[] { query };
        }
    }

    /**
     * 对一个数组内地值进行trim
     *
     * @param values
     * @return
     */
    public static String[] trim(String[] values) {
        String[] tv = new String[values.length];
        int i = 0;
        for (String vv : values) {
            tv[i++] = trimToEmpty(vv);
        }
        return tv;
    }

    /**
     * 当一个值与一组值进行比较时，只要有一个target与me相等，则返回true
     *
     * @param me
     * @param targets
     * @return
     */
    public static boolean equalsOr(String me, String[] targets) {
        for (String target : targets) {
            if (StringUtils.equals(me, target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当一个值与一组值进行比较时，只要有一个target与me相等，则返回true
     *
     * @param me
     * @param targets
     * @return
     */
    public static boolean equalsOr(String me, List<String> targets) {
        for (String target : targets) {
            if (StringUtils.equals(me, target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当一个值与一组值进行比较时，只要有一个target与me相等，则返回true
     *
     * @param me
     * @param targets 以','进行分割
     * @return
     */
    public static boolean equalsOr(String me, String targets) {
        return isNotEmpty(targets) && equalsOr(me, split(targets, ","));
    }

    /**
     * 当一个值与一组值进行比较时，必须所有target与me相等，才返回true
     *
     * @param me
     * @param targets
     * @return
     */
    public static boolean equalsAnd(String me, String[] targets) {
        for (String target : targets) {
            if (!StringUtils.equals(me, target)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 如果指定的target为NULL，使用默认值
     *
     * @param target
     * @param defaultValue
     * @return
     */
    public static String trim(String target, String defaultValue) {
        if (StringUtils.isBlank(target)) {
            return trim(defaultValue);
        }
        return trim(target);
    }

    private static char charInter = '\n';
    private static char charLt = '<';
    private static char charGt = '>';
    private static char charQuot = '"';
    private static char charAmp = '&';

    /**
     * 方法 toHtml 可以把源字符串中的不能在网页中正确显示的 字符替换为可以显示的相应字符串。
     *
     * @param strSource 替换前的字符串
     * @return 替换后的字符串
     */

    public static String toHtml(String strSource) {
        if (StringUtils.isBlank(strSource)) {
            return "";
        }
        StringBuffer strBufReturn = new StringBuffer();
        for (int i = 0; i < strSource.length(); i++) {
            if (strSource.charAt(i) == charInter)
                strBufReturn.append("<BR>");
            else if (strSource.charAt(i) == charLt)
                strBufReturn.append("<");
            else if (strSource.charAt(i) == charGt)
                strBufReturn.append(">");
            else if (strSource.charAt(i) == charQuot)
                strBufReturn.append("\"");
            else if (strSource.charAt(i) == charAmp)
                strBufReturn.append("&");
            else
                strBufReturn.append(strSource.charAt(i));
        }
        return strBufReturn.toString();
    }

    /**
     * 对空格、回车进行正确替换，其它HTML标记，直接转移为可显示字符串
     *
     * @param string
     * @return
     */
    public static String stringToHTMLString(String string) {
        if (StringUtils.isBlank(string)) {
            return StringUtils.EMPTY;
        }
        StringBuffer sb = new StringBuffer(string.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;
        boolean inHtml = false;
        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss
                // word breaking
                if (lastWasBlankChar && !inHtml) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                } else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                }
            } else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                if (c == charLt) {
                    inHtml = true;
                    sb.append(c);
                } else if (c == charGt) {
                    inHtml = false;
                    sb.append(c);
                } else if (c == '&' && (i + 4) < len && (string.charAt(i + 1) != 'n') && (string.charAt(i + 2) != 'b')
                        && (string.charAt(i + 3) != 's') && (string.charAt(i + 4) != 'p'))
                    sb.append("&amp;");
                else if (c == '\n')
                    // Handle Newline
                    sb.append("<br>");
                else {
                    int ci = 0xffff & c;
                    if (ci < 160)
                        // nothing special only 7 Bit
                        sb.append(c);
                    else {
                        // Not 7 Bit use the unicode system
                        sb.append("&#");
                        sb.append(new Integer(ci).toString());
                        sb.append(';');
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将不定数目的字符串连接起来
     *
     * @param args
     * @return
     */
    public static String join(String... args) {
        return StringUtils.join(args);
    }

    private static char CHAR_BlANK = ' ';
    private static char CHAR_DIVIDE = '/';
    private static char QM_DBC = '?', QM_SBC = '？';
    private static char COLON_DBC = ':', COLON_SBC = '：';
    private static char EQUAL_DBC = '=', EQUAL_SBC = '＝';
    private static char AMP_DBC = '&', AMP_SBC = '＆';
    private static char SQM_DBC = '\'', SQM_SBC = '‘';
    private static char DQM_DBC = '"', DQM_SBC = '”';

    /**
     * [" " , /]过滤掉 [: , ? , & , = , ' , "]转变为全角
     *
     * @param title
     * @return
     */
    public static String escapeTitleForSEO(String title) {
        title = trim(title);
        if (isNotBlank(title)) {
            StringBuffer sb = new StringBuffer(title.length());
            char[] chars = title.toCharArray();
            for (char c : chars) {
                if (c != CHAR_BlANK && c != CHAR_DIVIDE) {
                    if (c == COLON_DBC) {
                        sb.append(COLON_SBC);
                    } else if (c == QM_DBC) {
                        sb.append(QM_SBC);
                    } else if (c == AMP_DBC) {
                        sb.append(AMP_SBC);
                    } else if (c == EQUAL_DBC) {
                        sb.append(EQUAL_SBC);
                    } else if (c == SQM_DBC) {
                        sb.append(SQM_SBC);
                    } else if (c == DQM_DBC) {
                        sb.append(DQM_SBC);
                    } else {
                        sb.append(c);
                    }
                }
            }
            title = sb.toString();
        }
        return title;
    }

    public static boolean isEmpty(String s) {
        return (s == null || s.trim().equals("") || "null".equals(s.trim()));
    }

    public static boolean isEmpty(Object ob) {
        return ob == null || ob.equals("") || "null".equals(ob);
    }

    public static boolean isNotEmpty(String s) {
        return s != null && (s.trim().length() > 0);
    }

    /**
     * 返回一个有效的字符串，将null转换为""。
     *
     * @param value - 原字符串。
     * @return 有效的字符串。
     */
    public static String getString(Object value) {
        return (value == null || "null".equals(value)) ? "" : value.toString();
    }

    public static String secureToString(Object o) {
        if (o == null)
            return null;
        return o.toString();
    }

    /**
     * for:{0}adf{1} 赋值
     *
     * @param pattern
     * @param values
     * @return
     */
    public static String format(String pattern, Object... values) {
        if (values == null || values.length == 0) {
            return MessageFormat.format(pattern, values);
        }
        String[] params = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            params[i] = getString(values[i]);
        }
        return MessageFormat.format(pattern, (Object[]) params);
    }

    /**
     * 转换成string类型
     *
     * @param o
     * @return
     */
    public static String toString(Object o) {
        if (o == null)
            return "";

        if (o instanceof String)
            return (String) o;
        return o.toString();
    }

    /**
     * 将 s 进行 BASE64 编码
     *
     * @param s
     * @return
     */
    public static String encodeByBase64(String s) {
        if (StringUtil.isEmpty(s)) {
            return null;
        }
        byte[] buffer = encodeByBase64(s.getBytes());
        return (new String(buffer));
    } //

    public static byte[] encodeByBase64(byte[] bs) {
        org.apache.commons.codec.binary.Base64 decoder = new org.apache.commons.codec.binary.Base64();
        return decoder.encode(bs);
    }

    /**
     * 将 BASE64 编码的字符串 s 进行解码
     *
     * @param s
     * @return
     */
    public static String decodeByBase64(String s) {
        if (StringUtil.isEmpty(s)) {
            return null;
        }
        org.apache.commons.codec.binary.Base64 decoder = new org.apache.commons.codec.binary.Base64();
        byte[] buffer = decoder.decode(s.getBytes());
        return (new String(buffer));
    }

    public static byte[] decoderByBase64(byte[] bs) {
        org.apache.commons.codec.binary.Base64 decoder = new org.apache.commons.codec.binary.Base64();
        return decoder.decode(bs);
    }

    public static boolean toBoolean(Object o) {
        if (o == null)
            return false;
        if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue();
        } else if (o instanceof Number) {
            return ((Number) o).intValue() == 1;
        }
        return o.toString().trim().equalsIgnoreCase("true");
    }

    /**
     * 字符串转换成十六进制值
     *
     * @param str 我们看到的要转换成十六进制的字符串
     * @return
     */
    public static String Str2hex(String str) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }

    /**
     * 十六进制转换字符串
     *
     * @param hex String 十六进制
     * @return String 转换后的字符串
     */
    public static String hex2Str(String hex) {
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return new String(bytes);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * Trims tokens and omits empty tokens.
     * <p>The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using {@code delimitedListToStringArray}
     * @param str the String to tokenize
     * @param delimiters the delimiter characters, assembled as String
     * (each of those characters is individually considered as delimiter).
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * <p>The given delimiters string is supposed to consist of any number of
     * delimiter characters. Each of those characters can be used to separate
     * tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using {@code delimitedListToStringArray}
     * @param str the String to tokenize
     * @param delimiters the delimiter characters, assembled as String
     * (each of those characters is individually considered as delimiter)
     * @param trimTokens trim the tokens via String's {@code trim}
     * @param ignoreEmptyTokens omit empty tokens from the result array
     * (only applies to tokens that are empty after trimming; StringTokenizer
     * will not consider subsequent delimiters as token in the first place).
     * @return an array of the tokens ({@code null} if the input String
     * was {@code null})
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(
            String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    /**
     * Copy the given Collection into a String array.
     * The Collection must contain String elements only.
     * @param collection the Collection to copy
     * @return the String array ({@code null} if the passed-in
     * Collection was {@code null})
     */
    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

    private static final String FOLDER_SEPARATOR = "/";

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String TOP_PATH = "..";

    private static final String CURRENT_PATH = ".";

    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * Normalize the path by suppressing sequences like "path/.." and
     * inner simple dots.
     * <p>The result is convenient for path comparison. For other uses,
     * notice that Windows separators ("\") are replaced by simple slashes.
     * @param path the original path
     * @return the normalized path
     */
    public static String cleanPath(String path) {
        if (path == null) {
            return null;
        }
        String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

        // Strip prefix from path to analyze, to not treat it as part of the
        // first path element. This is necessary to correctly parse paths like
        // "file:core/../core/io/Resource.class", where the ".." should just
        // strip the first "core" directory while keeping the "file:" prefix.
        int prefixIndex = pathToUse.indexOf(":");
        String prefix = "";
        if (prefixIndex != -1) {
            prefix = pathToUse.substring(0, prefixIndex + 1);
            pathToUse = pathToUse.substring(prefixIndex + 1);
        }
        if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
            prefix = prefix + FOLDER_SEPARATOR;
            pathToUse = pathToUse.substring(1);
        }

        String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
        List<String> pathElements = new LinkedList<String>();
        int tops = 0;

        for (int i = pathArray.length - 1; i >= 0; i--) {
            String element = pathArray[i];
            if (CURRENT_PATH.equals(element)) {
                // Points to current directory - drop it.
            }
            else if (TOP_PATH.equals(element)) {
                // Registering top path found.
                tops++;
            }
            else {
                if (tops > 0) {
                    // Merging path element with element corresponding to top path.
                    tops--;
                }
                else {
                    // Normal path element found.
                    pathElements.add(0, element);
                }
            }
        }

        // Remaining top paths need to be retained.
        for (int i = 0; i < tops; i++) {
            pathElements.add(0, TOP_PATH);
        }

        return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
    }

    /**
     * Convenience method to return a Collection as a delimited (e.g. CSV)
     * String. E.g. useful for {@code toString()} implementations.
     * @param coll the Collection to display
     * @param delim the delimiter to use (probably a ",")
     * @param prefix the String to start each element with
     * @param suffix the String to end each element with
     * @return the delimited String
     */
    public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
        if (CollectionUtils.isEmpty(coll)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = coll.iterator();
        while (it.hasNext()) {
            sb.append(prefix).append(it.next()).append(suffix);
            if (it.hasNext()) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    /**
     * Convenience method to return a Collection as a delimited (e.g. CSV)
     * String. E.g. useful for {@code toString()} implementations.
     * @param coll the Collection to display
     * @param delim the delimiter to use (probably a ",")
     * @return the delimited String
     */
    public static String collectionToDelimitedString(Collection<?> coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    /**
     * Take a String which is a delimited list and convert it to a String array.
     * <p>A single delimiter can consists of more than one character: It will still
     * be considered as single delimiter string, rather than as bunch of potential
     * delimiter characters - in contrast to {@code tokenizeToStringArray}.
     * @param str the input String
     * @param delimiter the delimiter between elements (this is a single delimiter,
     * rather than a bunch individual delimiter characters)
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(String str, String delimiter) {
        return delimitedListToStringArray(str, delimiter, null);
    }

    /**
     * Take a String which is a delimited list and convert it to a String array.
     * <p>A single delimiter can consists of more than one character: It will still
     * be considered as single delimiter string, rather than as bunch of potential
     * delimiter characters - in contrast to {@code tokenizeToStringArray}.
     * @param str the input String
     * @param delimiter the delimiter between elements (this is a single delimiter,
     * rather than a bunch individual delimiter characters)
     * @param charsToDelete a set of characters to delete. Useful for deleting unwanted
     * line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a String.
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
        if (str == null) {
            return new String[0];
        }
        if (delimiter == null) {
            return new String[] {str};
        }
        List<String> result = new ArrayList<String>();
        if ("".equals(delimiter)) {
            for (int i = 0; i < str.length(); i++) {
                result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
            }
        }
        else {
            int pos = 0;
            int delPos;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()) {
                // Add rest of String, but not in case of empty input.
                result.add(deleteAny(str.substring(pos), charsToDelete));
            }
        }
        return toStringArray(result);
    }

    /**
     * Delete all occurrences of the given substring.
     * @param inString the original String
     * @param pattern the pattern to delete all occurrences of
     * @return the resulting String
     */
    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    /**
     * Delete any character in a given String.
     * @param inString the original String
     * @param charsToDelete a set of characters to delete.
     * E.g. "az\n" will delete 'a's, 'z's and new lines.
     * @return the resulting String
     */
    public static String deleteAny(String inString, String charsToDelete) {
        if (!StringUtils.isNotEmpty(inString) || !StringUtils.isNotEmpty(charsToDelete)) {
            return inString;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Convenience method to return a String array as a delimited (e.g. CSV)
     * String. E.g. useful for {@code toString()} implementations.
     * @param arr the array to display
     * @param delim the delimiter to use (probably a ",")
     * @return the delimited String
     */
    public static String arrayToDelimitedString(Object[] arr, String delim) {
        if (ObjectUtil.isEmpty(arr)) {
            return "";
        }
        if (arr.length == 1) {
            return toString(arr[0]);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(delim);
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * Convenience method to return a String array as a CSV String.
     * E.g. useful for {@code toString()} implementations.
     * @param arr the array to display
     * @return the delimited String
     */
    public static String arrayToCommaDelimitedString(Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }
}
