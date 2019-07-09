package com.nikesu.untitled.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class SensitiveWordFilter {
    private static Set<String> sensitiveWords;
    private static final String wordsFileName = "words.txt";
    private static final String articleFileName = "article.txt";
    private static Map<String, Integer> trie;
    private static Map<String, String> fail;

    private SensitiveWordFilter() {}

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

    private static void insert(String word) {
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i <= word.length(); i++) {
            if (trie.containsKey(sb.toString())) {
                if (i == word.length()) {
                    trie.replace(sb.toString(), word.length());
                }
                else {
                    sb.append(word.charAt(i));
                }
            }
            else {
                if (i == word.length()) {
                    trie.put(sb.toString(), word.length());
                }
                else {
                    trie.put(sb.toString(), 0);
                    sb.append(word.charAt(i));
                }
            }
        }
    }

    private static void buildFail() {
        Set<String> set = new TreeSet<>((s1, s2)
                ->((s1.length() - s2.length() == 0)? (s1.compareTo(s2)) : (s1.length() - s2.length())));
        Iterator<String> it = trie.keySet().iterator();
        while (it.hasNext()) {
            set.add(it.next());
        }

        begin:
        for (String node : set) {
            if (node.equals("") || node.length() == 1) {
                fail.put(node, "");
                continue;
            }


            for (int i = 1; i < node.length(); i++) {
                String father = node.substring(0, node.length() - i);
                String fatherFail = fail.get(father);
                String nodeFail = fatherFail + node.substring(node.length() - i,node.length());
                if (trie.containsKey(nodeFail)) {
                    fail.put(node, nodeFail);
                    continue begin;
                }
            }

            fail.put(node, "");
        }
    }

    static
    {
        init();
    }

    private static void init() {
        sensitiveWords = new TreeSet<>((s1, s2)
                ->((s2.length() - s1.length() == 0)? (s1.compareTo(s2)) : (s2.length() - s1.length())));


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



        sensitiveWords.add("she");
        sensitiveWords.add("her");
        sensitiveWords.add("him");
        sensitiveWords.add("he");
        sensitiveWords.add("his");
    }

    private static void acInit() {
        trie = new HashMap<>();
        fail = new HashMap<>();
        for (String s : sensitiveWords) {
            insert(s);
        }
        buildFail();
    }

    private static String ac(String content) {
        StringBuilder result = new StringBuilder();
        StringBuilder now = new StringBuilder();
        Integer l;
        for (int i = 0; i < content.length(); i++) {
            String key = now.toString() + content.charAt(i);
            if (trie.containsKey(key)) {
                now.append(content.charAt(i));
            } else if (now.length() == 0) {
                result.append(content.charAt(i));
            } else if ((l = trie.get(now.toString())) != null && l > 0) {
                for (int j = 0; j < l; j++){
                    result.append("*");
                }
                now.delete(0, now.length());
                i--;
            } else {
                StringBuilder next = new StringBuilder(fail.get(now.toString()));
                result.append(now.substring(0, now.length() - next.length()));
                now = next;
                i--;
            }
        }
        return result.append(now).toString();
    }

    // 这个方法用来测试
    public static void main(String[] args) {

        acInit();


        Scanner scanner = new Scanner(System.in);

        FileReader fileReader;
        BufferedReader bufferedReader;
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
        //System.out.println("替换前：        " + content);
        //System.out.println("替换后：");
        long begin = System.nanoTime();
        String newContent = replaceSensitiveWords(content);
        long end = System.nanoTime();

        long acbegin = System.nanoTime();
        String acNewContent = ac(content);
        long acend = System.nanoTime();
        //System.out.println(" 朴素算法：     "+newContent);
        //System.out.println(" AC 自动机算法："+acNewContent);
        System.out.println("朴素算法耗时：     " + (end - begin)*1.0e-9 + "s");
        System.out.println("AC 自动机算法耗时：" + (acend - acbegin)*1.0e-9 + "s");
    }
}
