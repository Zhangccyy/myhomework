//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖保佑             永无BUG
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test01 {
    static int totalnum;//第一等级数量
    static int switchnum;//第二等级
    static int i_e_num;//if_else的个数
    static int i_ei_e_num;//if_else if_else的个数
    static String[] keywords={
            "int","long","short","float","double","char","unsigned",
            "signed", "const","void","volatile","enum","struct",
            "union","if","else", "goto","switch","case","do",
            "while","for","continue","break","return","default","elseif",
            "typedef","auto","register","extern", "static", "sizeof"
    };//32个关键字段加elseif，总共33个


    static ArrayList<String> words=new ArrayList<String>();//存储文件读入的分割完的字符串数组
    static Map<String,Integer> keymap=new HashMap<String,Integer>();//存储关键字段及其个数
    static ArrayList<String>keystring=new ArrayList<String>();//从文件中提取出来的关键字列表

    //等级一处理
    static void grade1(){
        for (Map.Entry<String,Integer> entry: keymap.entrySet()){
            totalnum+=entry.getValue();
            if(entry.getKey().equals("elseif")) totalnum+= entry.getValue();
            //计算关键字段个数时，碰到elseif，则多加一次
        }
        System.out.println("totalnum: "+totalnum);
    }

    //等级三处理
    static void grade3(){//循环遍历统计每组switch的case个数并输出
        for(int i=0;i<keystring.size();i++){
            if(keystring.get(i).equals("switch")){
                int num=0;
                for(int j=i+1;j<keystring.size();j++){
                    if(keystring.get(j).equals("case")){
                        num++;
                    }
                    if(keystring.get(j).equals("switch")||j==keystring.size()-1){
                        i=j-1;
                        System.out.print(num+" ");break;
                    }
                }
            }
        }
    }

    //等级4等级5处理
    static void grade4_5(int grade){
        i_e_num=0;
        i_ei_e_num=0;
        ArrayList<String>if_else=new ArrayList<String>();
        for(String word:words){
            if(word.equals("if")||word.equals("else")||word.equals("elseif"))
                if_else.add(word);
        }
        int nums= if_else.size();
        ArrayList<String>ifword=new ArrayList<String>();
        int top=-1;
        //用栈操作计算第if_else 和if_else if_else个数
        for(int i=0;i<nums;i++){
            String word=if_else.get(i);
            if(word.equals("if")) {ifword.add(word);++top;}
            else if(word.equals("else")){
                if(ifword.get(top).equals("if")){
                    i_e_num++;ifword.remove(top);top--;
                }
                else{
                    while(true){
                        if(ifword.get(top).equals("if")) break;
                        ifword.remove(top);top--;
                    }
                    ifword.remove(top);top--;i_ei_e_num++;
                }
            }
            else{ifword.add(word);top++;}
        }
        if(grade==4) System.out.println("if_else num: "+i_e_num);
        if(grade==5) System.out.println("if_elseif_else num: "+i_ei_e_num);
    }


    public static void main(String[] args) {
        for (String word:keywords) {
            keymap.put(word,0);
        }

        Scanner in=new Scanner(System.in);
        String src=in.next();
        int grade=in.nextInt();
        File file=new File(src);
        FileReader fread=null;
        BufferedReader f=null;
        try {//读入文件
            fread=new FileReader(file);
            f=new BufferedReader(fread);
            String s=null;
            String mys=" ";
            while((s=f.readLine())!=null){//按行读入
                Pattern pattern=Pattern.compile("//.*");
                //正则匹配，如果遇到单行注释，则去掉注释号及后面改行内容
                Matcher matcher= pattern.matcher(s);
                s=matcher.replaceAll("");
                mys+=s;//将所有读入行拼接成一个串
            }
            Pattern pattern=Pattern.compile("/\\*(.*?)\\*/",Pattern.DOTALL);
            //正则匹配，去掉多行注释符号及其中间内容
            Matcher matcher= pattern.matcher(mys);
            mys=matcher.replaceAll("");
            Pattern pattern2=Pattern.compile("(?<=\").*?(?=\")");
            //正则匹配去掉代码中字符串符号及中间内容
            Matcher matcher2= pattern2.matcher(mys);
            mys=matcher2.replaceAll("");
            mys=mys.replace("else if","elseif");
            //将所有“else if”字段替换成“elseif”,作为一个字段处理
            //将读入的串按空白符、标点符和各种括号分割，得到一个只有单词的字段
            String[] array=mys.split("\\s+|<+|>+|;+|\\(+|\\)+|\\{+|\\}+|:|\\?|!|,+|:+");
            for(String word:array){
                if(word.length()!=0){
                    words.add(word);
                }
            }
            for(String word:words){//按照读入分割完的字符串数组更新关键字段出现的次数
                if(keymap.containsKey(word)){
                    keystring.add(word);
                    keymap.replace(word,keymap.get(word),keymap.get(word)+1);
                }
            }
            switch(grade){//根据读入的操作等级进行分步操作
                case 1:grade1();break;
                case 2:System.out.println("switch num: "+keymap.get("switch"));break;
                case 3:{
                    System.out.print("case num: ");
                    grade3();
                    break;
                }
                case 4: grade4_5(4);break;
                case 5: grade4_5(5);break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                f.close();
                fread.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
