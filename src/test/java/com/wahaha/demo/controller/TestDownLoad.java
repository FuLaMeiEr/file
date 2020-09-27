package com.wahaha.demo.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class TestDownLoad {

    private static int threadCount = 3;

    private static String savaPath = "E:\\888.jpeg";

    private static String link = "http://localhost:8080/downloadFile?url=http://localhost:8080/idea.jpeg";

    public static void downLoadFile() throws Exception {

        String path = link.substring(link.indexOf("=") + 1);
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");

        //获取相应码
        int code = conn.getResponseCode();
        int length = conn.getContentLength();
        String uuid = conn.getRequestProperty("uuid");
        if (code == 200) {//请求成功
            long blockSize = length / threadCount;
            for (int threadId = 1; threadId <= threadCount; threadId++) {
                long startIndex = blockSize * (threadId - 1);
                long endIndex = blockSize * threadId - 1;
                if (threadId == threadCount) {
                    endIndex = length;
                }

                System.out.println("线程【" + threadId + "】开始下载：" + startIndex + "---->" + endIndex);
                new DownloadThread(link, startIndex, endIndex, savaPath).start();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        TestDownLoad.downLoadFile();
    }

}
