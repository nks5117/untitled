package com.nikesu.untitled.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class sensitiveWordFilter {
    private static Set<String> sensitiveWords;
    private static final String wordsFileName = "words.txt";
    private static final String articleFileName = "article.txt";
    /**
     * 返回将输入的字符串中的所有敏感词替换为 "*" 后的字符串
     * 敏感词指出现在集合 sensitiveWords 中的字符串
     * @param s
     * @return
     */
    public static String replaceSensitiveWords(String s) {
        Iterator<String> it=sensitiveWords.iterator();

        while(it.hasNext()) {
            s=s.replace(it.next(),"*");
        }
        return s;
    }

    // 这个方法用来测试
    public static void main(String[] args) {
        long begin = System.nanoTime();
        sensitiveWords = new TreeSet<>((s1, s2)
                ->((s2.length() - s1.length() == 0)? (s1.compareTo(s2)) : (s2.length() - s1.length())));
        Scanner scanner = new Scanner(System.in);

        FileReader fileReader;
        BufferedReader bufferedReader;

        try {
            fileReader = new FileReader(wordsFileName);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sensitiveWords.add(line);
            }
            bufferedReader.close();
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



        StringBuilder sb = new StringBuilder();
        try {
            fileReader = new FileReader(articleFileName);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        String content = sb.toString();

        System.out.println("敏感词数量：" + sensitiveWords.size() + "\t文章长度：" + content.length());
        System.out.println("替换前：");
        System.out.println(content);
        System.out.println("替换后：");

        String newContent = replaceSensitiveWords(content);
        long end = System.nanoTime();
        System.out.println(newContent);
        System.out.println("共耗时：" + (end - begin)*1.0e-9 + "s");
    }
}
