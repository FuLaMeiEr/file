package com.wahaha.demo.controller;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadThread extends Thread {

    private String url;
    private long from;
    private long to;
    private String savePath;

    public DownloadThread(String url, long from,
                          long to, String savePath) {
        this.url = url;
        this.from = from;
        this.to = to;
        this.savePath = savePath;

    }


    @Override
    public void run() {
        try {
            URL link = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            //设置请求属性，请求部分资源
            conn.setRequestProperty("Range", "bytes=" + from + "-" + to);
            int code = conn.getResponseCode();
            String md5 = conn.getHeaderField("md5");
            System.out.println(md5);
            if (code == 206) {
                InputStream in = conn.getInputStream();

                RandomAccessFile file = new RandomAccessFile(savePath, "rwd");
                file.seek(from);
                byte[] buffer = new byte[1024];
                int num;
                while ((num = in.read(buffer)) != -1) {
                    file.write(buffer, 0, num);
                }
               /* String downlownMd5 = DigestUtils.md5Hex(file.toString());
                String localMd5 = DigestUtils.md5Hex(new FileInputStream(new File(savePath)));*/
                in.close();
                file.close();
                //return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

