package com.nikesu.untitled.util;

/**
 * @author 白雪婷
 */
public class StringFormatter {
    /**
     * 将字符串 text 居中在宽度为 lineWidth 的一行中显示，两侧用 fill 填充。
     * 如 alignCenter("test", '-', 10) 将返回 "---test---"，
     * 当不能完全居中时，可以向左或向右偏移一个字符，如 alignCenter("test", '-', 9)
     * 返回 "--test---" 或者 "---test--" 都是可以的
     * 当 text 的长度大于 lineWidth 时，将 text 的长度截断到 lineWidth，
     * 如 alignCenter("a very long test", '-', 10) 将返回 "a very lon"。
     * 注意一个汉字字符的宽度是英文字符的两倍。
     * @param text
     * @param fill
     * @param lineWidth
     * @return
     */
    /**
     * 判断char是否为中文字符
     * 是 返回true
     */
    public static boolean isChineseChar(char c) {
        // return String.valueOf(c).matches("[\u4e00-\u9fa5]");
        return !(c >= ' ' && c <= '~');
    }
    /**
     * 判断char是否为英文字母
     * 是 返回true
     */
    public static boolean isletter(char c) {
        if ((c >= 'A' && c <='Z') || (c >= 'a' && c <= 'z')) {
            return true;
        }else {
            return false;
        }
    }
    /**
     * 居中对齐
     * 宽度为lineWidth
     * 用fill填充
     */
    public static String alignCenter(String text, char fill, int lineWidth) {
        StringBuilder sb=new StringBuilder();
        int chinese=0;//中文字符数量
        int fillnum=0;//填充字符fill数量
        boolean over=false;
        for (int i = 0; i < text.length(); i++) {
            if(isChineseChar(text.charAt(i))) {
                chinese++;
            }
            if (i+chinese+1>=lineWidth) {
                over=true;
                break;
            }
        }
        if(over) {//溢出
            for(int i=0;i<lineWidth-chinese;i++) {
                sb.append(text.charAt(i));//截取字符
            }
        }
        else {
            fillnum=lineWidth-text.length()-chinese;	//if(isChineseChar(fill)) {fillnum/=2;}
            for(int i=0;i<fillnum;i++) {
                sb.append(fill);//填充符号
            }
            sb.insert(fillnum/2,text);//居中插入text
        }
        text=sb.toString();
        return text;
    }

    /**
     * 将字符串 text 左对齐地在宽度为 lineWidth 的一行中显示，左侧留有 indent 个 leftFill，右侧用 fill 填充。
     * 如 alignLeft("test", '.', 10, 1, '-') 将返回 "-test....."，
     * 当 text 的长度 + indent 大于 lineWidth 时，将 text 的长度截断到 lineWidth - indent，
     * 如 alignLeft("a very long test", '-', 10, 2) 将返回 "  a very l"。
     * 注意一个汉字字符的宽度是英文字符的两倍。
     * @param text
     * @param fill
     * @param lineWidth
     * @param indent
     * @return
     */
    public static String alignLeft(String text, char fill, int lineWidth, int indent, char leftFill) {
        StringBuilder sb=new StringBuilder();
        //System.out.println("text长度："+text.length());
        for (int i = 0; i < indent; i++) {
            sb.append(leftFill);//填充左对齐leftFill
        }
        int chinese=0;//中文字符数量
        int fillnum=0;//填充字符fill数量
        boolean over=false;
        for (int i = 0; i < text.length(); i++) {
            if(isChineseChar(text.charAt(i))) {
                chinese++;
            }
            if (i+chinese+indent+1>=lineWidth) {
                over=true;
                break;
            }
        }
        if(over) {//溢出
            for(int i=0;i<lineWidth-chinese-indent;i++) {
                sb.append(text.charAt(i));//截取字符
            }
        }
        else {
            fillnum=lineWidth-text.length()-chinese-indent;
            for(int i=0;i<fillnum;i++) {
                sb.append(fill);//填充右侧fill
            }
            sb.insert(indent,text);//插入text
        }
        text=sb.toString();
        return text;
    }

    public static String alignLeft(String text, char fill, int lineWidth) {
        return alignLeft(text, fill, lineWidth, 0, ' ');
    }

    /**
     * 将字符串 text 右对齐地在宽度为 lineWidth 的一行中显示，右侧留有 indent 个 rightFill，左侧用 fill 填充。
     * 如 alignRight("test", ' ', 10, 1, '.') 将返回 "     test."，
     * 当 text 的长度 + indent 大于 lineWidth 时，将 text 的长度截断到 lineWidth - indent，
     * 如 alignRight("a very long test", '-', 10, 2, ' ') 将返回 "a very l  "。
     * 注意一个汉字字符的宽度是英文字符的两倍。
     * @param text
     * @param fill
     * @param lineWidth
     * @param indent
     * @return
     */
    //右对齐
    public static String alignRight(String text, char fill, int lineWidth, int indent, char rightFill) {
        StringBuilder sb=new StringBuilder();

        int chinese=0;//中文字符数量
        int fillnum=0;//填充字符fill数量
        boolean over=false;
        for (int i = 0; i < text.length(); i++) {
            if(isChineseChar(text.charAt(i))) {
                chinese++;
            }
            if (i+chinese+indent+1>=lineWidth) {
                over=true;
                break;
            }
        }
        if(over) {//溢出
            //System.out.println("溢出");
            for(int i=0;i<lineWidth-chinese-indent;i++) {
                sb.append(text.charAt(i));
            }
        }
        else {
            fillnum=lineWidth-text.length()-chinese-indent;
            for(int i=0;i<fillnum;i++) {
                sb.append(fill);
            }
            sb.insert(fillnum,text);
        }
        for (int i = 0; i < indent; i++) {
            sb.append(rightFill);
        }
        text=sb.toString();
        return text;
    }

    public static String alignRight(String text, char fill, int lineWidth) {
        return alignRight(text, fill, lineWidth, 0, ' ');
    }

    /**
     * 在字符串 text 的适当地方插入换行符，以保证每行长度不超过 lineWidth。
     * 非强制：尽量避免在标点符号前、单词中间换行，行末空格不计在行宽度内，行首不要出现空格。
     * 如 wrapText("This is a very very very very very long text", 10) 应返回
     * "This is a \nvery very \nvery long \ntext."
     * @param text
     * @param lineWidth
     * @return
     */
    public static String wrapText(String text, int lineWidth) {
        StringBuilder sb=new StringBuilder();
        int i=0;//记录当前行的宽度
        int flag=0;//记录上一个可以插入的下标
        int tail=0;//记录下一个需要插入的下标
        int j=0;//遍历字符用
        while(j< text.length()-1){
            if (isChineseChar(text.charAt(j))) {
                i+=2;//记录当前行的宽度
            }else {
                i+=1;
            }
            //记录上一个可以插入的位置
            if (!isletter(text.charAt(j)) && (isletter(text.charAt(j+1)) || isChineseChar(text.charAt(j+1)))) {
                flag=j;	//避免在标点符号前、单词中间换行
            } else if (text.charAt(j+1)==' ' && i==lineWidth) {
                flag=j+1;//行末空格不计在行宽度内
            }
            if (i>=lineWidth ) {
                if(flag==0) {
                    flag=j;
                }
                // System.out.println("flag:"+flag);
                for ( ;tail <=flag; tail++) {
                    sb.append(text.charAt(tail));
                }

                while(text.charAt(tail)==' ') {//行首不要出现空格
                    sb.append(text.charAt(tail));
                    tail++;
                }
                sb.append("\n");
                j=tail-1;
                i=0;
                flag=0;
            }
            j++;
        }
        for(i=tail;i<text.length();i++){
            sb.append(text.charAt(i));
        }
        text=sb.toString();
        return text;
    }

    /**
     * 返回一条长度为 lineWidth 的分割线。
     * 如 splitLine("-", 5) 将返回 "-----"
     * 而 splitLine("-*", 5) 将返回 "-*-*-"
     * @param s
     * @param lineWidth
     * @return
     */
    public static String splitLine(String s, int lineWidth) {
        StringBuilder sb=new StringBuilder();
        for (int i = 1; i <lineWidth; i+=s.length()) {
            sb.append(s);
        }//超出长度前
        if(sb.length()<lineWidth) {
            for(int i=0;i<lineWidth-sb.length();i++) {
                sb.append(s.charAt(i));//超出后
            }
        }
        s=sb.toString();
        return s;
    }

    public static int strWidth(String s) {
        int l = 0;
        for (int i = 0; i < s.length(); i++) {
            if (isChineseChar(s.charAt(i))) {
                l += 2;
            }
            else {
                l += 1;
            }
        }
        return l;
    }
}
