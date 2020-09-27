package com.wahaha.demo.controller;

import com.wahaha.demo.controller.Base.BaseAuthController;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableAutoConfiguration
public class UploadFileControllerTest extends BaseAuthController {


    private static File file = new File("E:\\777.png");


    @Test
    public void uploadFile() throws Exception {
        final MvcResult mvcResultPost = getMockMvc()
                .perform(upload("/uploadFile", file)
                )
                .andExpect(status().isOk())    //返回的状态是200
                .andDo(print())         //打印出请求和相应的内容
                .andReturn();

        final String mvcResult = mvcResultPost.getResponse().getContentAsString();
        System.out.println(mvcResult);
    }
}