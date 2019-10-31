/**
 *
 */
package com.demai.cornel.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

/**
 */
public class GenJavaCode4Pg {
    /**
     * meta data
     *
     * @author waynechen
     */
    private static class GenJavaCodeData {
        public String srcfield;
        public String field;
        public String desc;

        @Override
        public String toString() {
            return "GenJavaCodeData [srcfield=" + srcfield + ", field=" + field + ", desc=" + desc + "]";
        }

    }

    /**
     * rename to bean
     *
     * @param name
     * @return
     * @author waynechen
     */
    public static String rename2Bean(String name) {
        StringBuilder sb = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '_') {
                if (i + 1 < name.length()) {
                    c = name.charAt(++i);
                    sb.append(Character.toUpperCase(c));
                }
            } else {
                sb.append(c);
            }
        }
        //		Matcher m = pattern2Bean.matcher(name);
        //		StringBuffer sb = new StringBuffer();
        //		while (m.find())
        //			m.appendReplacement(sb, m.group(2).toUpperCase());
        //		m.appendTail(sb);
        return sb.toString();
    }

    public static String rename2Column(String name) {
        StringBuilder sb = new StringBuilder(name.length() + 20);
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c))
                sb.append("_" + Character.toLowerCase(c));
            else
                sb.append(c);
        }
        return sb.toString();
    }

    //	private static Pattern pattern2Bean = Pattern.compile("(_)(.)");
    private static String tableName;
    private static String javabeanName;
    private static Random ran = new Random();

    public static void main(String[] args) throws IOException {
        System.out.println("*************需要 复制 完整的建表语句(可以在mysq数据库中执行的建表语句)**************");
        System.out.println("*************请手动生成toString()方法 shift+alt+s,s**************");
        System.out.println("\n\n");

        BufferedReader reader = new BufferedReader(new InputStreamReader(GenJavaCode4Pg.class.getResourceAsStream("/auth.sql")));

        List<GenJavaCodeData> list = new ArrayList<GenJavaCodeData>();
        for (String line : readData(reader)) {
            try {
                String[] strings = line.split(" ");

                GenJavaCodeData data = new GenJavaCodeData();
                data.srcfield = strings[0];
                data.field = rename2Bean(data.srcfield);
                data.desc = strings[1]
                        .replaceAll("\\binteger\\b", "Integer")
                        .replaceAll("\\bsmallint\\b", "Integer")
                        .replaceAll("\\bdatetime\\b", "Date")
                        .replaceAll("\\btimestamp\\b", "Date")
                        .replaceAll("\\bcharacter\\b", "String")
                        .replaceAll("\\bvarchar(\\d+)\\b", "String")
                        .replaceAll("\\bvarchar\\b", "String")
                        .replaceAll("\\bvarying(\\d+)\\b", "")
                        .replaceAll("\\bvarying\\b", "")
                        .replaceAll("\\binteger\\b", "Integer")
                        .replaceAll("\\bbigint\\b", "Long")
                        .replaceAll("\\bint\\b", "Integer")
                        .replaceAll("\\bint2\\b", "Integer")
                        .replaceAll("\\bint4\\b", "Integer")
                        .replaceAll("\\bint8\\b", "Integer")
                        .replaceAll("\\bnumeric\\b", "BigDecimal")
                        .replaceAll("\\b::character\\b", "")
                        .replaceAll("\\btinyint\\b", "Integer")
                        .replaceAll("\\bhstore\\b", "Map<String,String>");
                data.desc = StringUtils.removeEnd(data.desc, ",");

                list.add(data);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        reader.close();

        // ----------------------------------------------------------------------------------------------------------
        genResultMap(list);
        System.out.println("");
        genMainColumn(list);
        System.out.println("");
        genUpdateSql(list);
        System.out.println("");
        genInsertSql(list);
        System.out.println("");
        genJavabean(list);
    }

    private static void genInsertSql(List<GenJavaCodeData> list) {
        boolean flag = true;
        System.out.println(String.format(//
                "<insert id=\"save\" parameterType=\"com.demai.cornel.model.%s\" useGeneratedKeys=\"true\" keyProperty=\"%s\" >", //
                javabeanName, list.get(0).field));

        System.out.println(String.format("insert into %s(", tableName));
        System.out.println("  <trim suffixOverrides=\",\">");
        for (GenJavaCodeData data : list) {
            if (flag) {
                flag = false;
                continue;
            }
            System.out.println(String.format("    <if test=\"%s != null\"> %s ,</if>", data.field, data.srcfield));
        }
        System.out.println("  </trim>");
        System.out.println("  )values(");
        System.out.println("  <trim suffixOverrides=\",\">");
        flag = true;
        for (GenJavaCodeData data : list) {
            if (flag) {
                flag = false;
                continue;
            }
            System.out.println(String.format("    <if test=\"%s != null\"> %s ,</if>", data.field, "#{" + data.field + "}"));
        }
        System.out.println("  </trim>");
        System.out.println("  )");
        System.out.println("</insert>");
    }

    private static void genUpdateSql(List<GenJavaCodeData> list) {
        boolean flag = true;
        System.out.println(String.format(//
                "<update id=\"update\" parameterType=\"com.demai.cornel.model.%s\">", javabeanName));
        System.out.println(String.format("  update %s ", tableName));
        System.out.println("  <trim prefix=\"set\" suffixOverrides=\",\">");
        for (GenJavaCodeData data : list) {
            if (flag) {
                flag = false;
                continue;
            }
            String format = String.format("    <if test=\"%s != null\"> %s = #{%s},</if>", data.field, data.srcfield, data.field);
            System.out.println(format);
        }
        System.out.println("  </trim>");
        System.out.println(String.format("  where %s = #{%s}", list.get(0).srcfield, list.get(0).field));
        System.out.println("</update>");
    }

    private static void genResultMap(List<GenJavaCodeData> list) {
        System.out.println(String.format(//
                "<resultMap type=\"com.demai.cornel.model.%s\" id=\"%sResultMap\">", //
                javabeanName, firstChar2LowerCase(javabeanName)));
        boolean flag = true;
        String format;
        for (GenJavaCodeData data : list) {
            if (flag) {
                format = String.format("  <id column=\"%s\" property=\"%s\" />", data.srcfield, data.field);
                flag = false;
            } else
                format = String.format("  <result column=\"%s\" property=\"%s\" />", data.srcfield, data.field);
            System.out.println(format);
        }
        System.out.println("</resultMap>");
    }

    private static void genMainColumn(List<GenJavaCodeData> list) {
        System.out.println(String.format(//
                "<sql id=\"main_column\">"));
        boolean flag = true;
        String format;
        for (GenJavaCodeData data : list) {
            if (flag) {
                System.out.print("\t" + data.srcfield);
                flag = false;
            } else {
                System.out.println(",");
                System.out.print("\t" + data.srcfield);
            }
        }
        System.out.println("");
        System.out.println("</sql>");
    }

    private static void genJavabean(List<GenJavaCodeData> list) {
        System.out.println("public class " + javabeanName + " implements Serializable {");
        System.out.println(String.format("  private static final long serialVersionUID = %dL;", ran.nextLong()));
//		System.out.println(String.format("  public static final String table = \"%s\" ;", tableName));

//		for (GenJavaCodeData data : list) {
//			System.out.println(String.format("  public static final String %sColumn = \"%s.%s\" ;", data.field, tableName, data.srcfield));
//		}
        for (GenJavaCodeData data : list) {
//			String format1 = String.format("  @Column(name=\"%s\")", data.srcfield);
//			System.out.println(format1);
            System.out.println(String.format("  private %s %s ;", data.desc, data.field));
        }
        for (GenJavaCodeData data : list) {
            System.out.println(String.format("  public %s get%s() { return %s; }", data.desc, firstChar2UpperCase(data.field), data.field));
            System.out.println(String.format("  public void set%s(%s %s) { this.%s = %s; }", firstChar2UpperCase(data.field), data.desc, data.field, data.field, data.field));
        }
        System.out.println("}");
    }

    /**
     * collects data and sets table name, javabean name
     *
     * @param reader
     * @return
     * @throws java.io.IOException
     * @author waynechen
     */
    private static List<String> readData(BufferedReader reader) throws IOException {
        List<String> lines = new ArrayList<String>();
        boolean isStart = false;
        for (String line; null != (line = reader.readLine()); ) {
            line = line.replace("`", "").trim().toLowerCase();
            line = line.replaceAll("\\t+| +|\\(\\d+\\)|`", " ");
            if (line.startsWith("create table")) {
                tableName = line.replaceAll("\\(| +|create table", "").trim();
                String rename = rename2Bean(tableName);
                javabeanName = firstChar2UpperCase(rename);
                isStart = true;
            } else if (line.startsWith("("))
                continue;
            else if (line.startsWith("primary key"))
                break;
            else if (isStart)
                lines.add(line);
        }
        return lines;
    }

    /**
     * sets first char to upper case
     *
     * @param rename
     * @return
     * @author waynechen
     */
    public static String firstChar2UpperCase(String rename) {
        return rename.substring(0, 1).toUpperCase() + rename.substring(1);
    }

    /**
     * sets first char to lower case
     *
     * @param rename
     * @return
     * @author waynechen
     */
    public static String firstChar2LowerCase(String rename) {
        return rename.substring(0, 1).toLowerCase() + rename.substring(1);
    }

    /**
     * 时间 类型 转换
     *
     * @param timestamp
     * @return
     * @author waynechen
     */
    public static java.util.Date toDate(java.sql.Timestamp timestamp) {
        long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
        return new java.util.Date(milliseconds);
    }
}
//
//
//
//
//
//
//
//
//
//
//
//
//