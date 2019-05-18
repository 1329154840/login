package com.example.login.controller;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class read_file {

    public static void main(String[] args) throws Exception {
        String filePath = "g:\\aaa\\789.txt";
//      String filePath = "D:/article333.txt";
//      String filePath = "D:/article111.txt";
        String content = readTxt(filePath);
        System.out.println(content);

    }



    /**
     * 解析普通文本文件  流式文件 如txt
     * @param path
     * @return
     */
    @SuppressWarnings("unused")
    public static String readTxt(String path){
        StringBuilder content = new StringBuilder("");
        try {
            String code = resolveCode(path);
            File file = new File(path);
            InputStream is = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(is, code);
            BufferedReader br = new BufferedReader(isr);
//          char[] buf = new char[1024];
//          int i = br.read(buf);
//          String s= new String(buf);
//          System.out.println(s);
            String str = "";
            while (null != (str = br.readLine())) {
                content.append(str);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("读取文件:" + path + "失败!");
        }
        return content.toString();
    }



    public static String resolveCode(String path) throws Exception {
        InputStream inputStream = new FileInputStream(path);
        byte[] head = new byte[3];
        inputStream.read(head);
        String code = "gb2312";  //或GBK
        if (head[0] == -1 && head[1] == -2 )
            code = "UTF-16";
        else if (head[0] == -2 && head[1] == -1 )
            code = "Unicode";
        else if(head[0]==-17 && head[1]==-69 && head[2] ==-65)
            code = "UTF-8";

        inputStream.close();

        System.out.println(code);
        return code;
    }

}
