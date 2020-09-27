package com.wahaha.demo.controller.Base;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class BaseAuthController extends BaseJUnitTest {



    public MockHttpServletRequestBuilder upload(String api, File file) throws Exception {
        Assert.isTrue(file.exists(), "file not exists");
        Assert.isTrue(file.isFile(), "choose is not file");


        final HashMap<String, String> contentTypeParams = new HashMap<String, String>();
        contentTypeParams.put("boundary", "##" + Integer.MAX_VALUE + "!#");

        MediaType mediaType = new MediaType(MediaType.MULTIPART_FORM_DATA, contentTypeParams);
        MockMultipartHttpServletRequestBuilder builder = multipart(api);

        final MockMultipartFile mockMultipartFile = new MockMultipartFile(file.getName(),
                file.getName(), MediaType.APPLICATION_OCTET_STREAM_VALUE,
                new FileInputStream(file));

        builder.file(mockMultipartFile);

        return builder.contentType(mediaType);

    }

}
