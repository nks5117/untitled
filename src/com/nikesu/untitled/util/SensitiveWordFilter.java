package com.nikesu.untitled.util;

import com.mysql.cj.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * @author 卢琦
 * @author 王馨怡
 */
public class SensitiveWordFilter {
    private static Set<String> sensitiveWords;
    private static final String wordsFileName = "words.txt";
    private static final String articleFileName = "article.txt";
    private static Node root;
    /*敏感词长度对应的*个数*/
    private static String[] stars=new String[50];
    private static int cnt = 0;

    /*覆盖文本中的所有敏感词，若文本中某一子串同时包含有重叠部分的非子串关系的两敏感词，覆盖先查询到的敏感词*/
    public static String replaceSensitiveWords(String text){
        cnt = 0;
        StringBuilder sb=new StringBuilder();
        boolean flag=false;
        Node curr = root;
        int i = 0;
        int len=text.length();
        sb.append(text);
        while(i<len){

            /*获取文本中的字符*/
            String ch=text.substring(i,i+1);
            /*文本中的字符和AC自动机中的字符进行比较*/
            if(curr.table.get(ch)!=null){
                /*若相等，自动机进入下一状态*/
                curr=curr.table.get(ch);
                /*找到该敏感词在文本中的位置，用stars[length]进行覆盖*/
                if(curr.isWord()){
                    int length=curr.str.length();
                    sb.replace(i-length+1, i+1, stars[length]);
                    cnt++;
                }

                /*该敏感词的中间某部分字符串可能正好包含另一个敏感词，
                 * 即使当前结点不表示一个敏感词的终点，但到当前结点为止可能恰好包含了一个敏感词*/
                if(curr.fail!=null &&curr.fail.isWord()){
                    int length=curr.fail.str.length();
                    sb.replace(i-length+1, i+1, stars[length]);
                    cnt++;
                }

                /*索引自增，指向文本中下一个字符*/
                i++;
            }else{
                /*若不相等，找到下一个应该比较的状态*/
                curr=curr.fail;

                /*到根结点仍未找到，说明文本中以ch作为结束的字符片段不是任何敏感词的前缀，
                 * 重置，比较下一个字符*/
                if(curr==null){
                    curr=root;
                    i++;
                }
            }
        }
        return sb.toString();
    }

    /**
     * 返回将输入的字符串中的所有敏感词替换为 "*" 后的字符串
     * 敏感词指出现在集合 sensitiveWords 中的字符串
     * @param text
     * @return
     */
    public static String naiveReplace(String text) {
        for (String s : sensitiveWords) {
            text = text.replace(s,stars[s.length()]);
        }
        return text;
    }

    public static String veryNaiveReplace(String text) {
        StringBuilder sb = new StringBuilder(text);
        for (String s : sensitiveWords) {
            for (int i = 0; i < sb.length() - s.length() + 1; i++) {
                boolean flag = true;
                for (int j = 0; j < s.length(); j++) {
                    if (sb.charAt(i+j) != s.charAt(j)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    sb.replace(i, i+s.length(), stars[s.length()]);
                }
            }
        }
        return sb.toString();
    }

    /*内部静态类，用于表示AC自动机的每个结点*/
    private static class Node{
        /*如果从根结点到该结点表示了一个敏感词，则str != null, 且str就表示该敏感词*/
        String str;

        /*如果该结点之后还有字，则用table保存下一个字以及创建对应的孩子结点，其中键是字，值是孩子结点*/
        HashMap<String,Node> table=new HashMap<String,Node>();

        /*当前结点的孩子结点不能匹配文本串中的某个字符时，下一个应该查找的结点*/
        Node fail;

        public boolean isWord(){
            return str!=null;
        }
    }

    private SensitiveWordFilter() {}

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

        root = new Node();
        StringBuilder sb=new StringBuilder();
        buildTrieTree();
        build_AC_FromTrie();
        for(int i=1;i<50;i++) {
            sb.append("*");
            stars[i]=sb.toString();
        }
    }

    private static void buildTrieTree(){
        for(String sensitiveStr :  sensitiveWords){
            Node curr = root;
            int len=sensitiveStr.length();
            for(int i=0;i<len;i++){
                String ch=sensitiveStr.substring(i, i+1);
                if(!curr.table.containsKey(ch)){
                    curr.table.put(ch, new Node());
                }
                curr=curr.table.get(ch);
            }

            /*将每个目标字符串的最后一个字符对应的结点变成终点*/
            curr.str=sensitiveStr;
        }
    }

    /*由Trie树构建AC自动机*/
    private static void build_AC_FromTrie(){
        /*广度优先遍历所使用的队列*/
        LinkedList<Node> queue = new LinkedList<Node>();

        /*单独处理根结点的所有孩子结点*/
        for(String x : root.table.keySet()){

            /*根结点的所有孩子结点的fail都指向根结点*/
            Node n=root.table.get(x);
            n.fail=root;
            queue.addLast(n);/*所有根结点的孩子结点入列*/

        }

        while(!queue.isEmpty()){
            /*确定出列结点的所有孩子结点的fail的指向*/
            Node p=queue.removeFirst();
            for(String x : p.table.keySet()){
                /*孩子结点入列*/

                queue.addLast(p.table.get(x));
                /*从p.fail开始找起*/
                Node Fail=p.fail;
                while(true){
                    /*说明找到了根结点还没有找到*/
                    if(Fail==null){
                        p.table.get(x).fail=root;
                        break;
                    }

                    /*说明有公共前缀*/
                    if(Fail.table.get(x)!=null){
                        p.table.get(x).fail=Fail.table.get(x);
                        break;
                    }
                    else{/*继续向上寻找*/
                        Fail=Fail.fail;
                    }
                }
            }
        }
    }




    // 这个方法用来测试
    public static void main(String[] args) {
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
        //System.out.println("替换前：             " + content);
        //System.out.println("替换后：");

        long nbegin = System.nanoTime();
        String newContent1 = veryNaiveReplace(content);
        long nend = System.nanoTime();

        long rbegin = System.nanoTime();
        String newContent2 = naiveReplace(content);
        long rend = System.nanoTime();

        long acbegin = System.nanoTime();
        String newContent3 = replaceSensitiveWords(content);
        long acend = System.nanoTime();

        //System.out.println(" 朴素算法：          " + newContent1);
        //System.out.println(" 使用String.replace：" + newContent2);
        //System.out.println(" AC 自动机算法：     " + newContent3);
        System.out.println("文章中含有敏感词 " + cnt + "个");
        System.out.println("朴素算法耗时：      " + (nend - nbegin) * 1.0e-9 + "s");
        System.out.println("使用String.replace：" + (rend - rbegin) * 1.0e-9 + "s");
        System.out.println("AC 自动机算法耗时： " + (acend - acbegin) * 1.0e-9 + "s");
    }
}
