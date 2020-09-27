package com.wahaha.demo.service;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface FileService {

    default MultipartFile getMultipartFile(final HttpServletRequest request) {
        Map<String, List<MultipartFile>> multiFileMap = null;
        if (request instanceof MultipartHttpServletRequest) {
            final MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            multiFileMap = multipartRequest.getMultiFileMap();
        } else {
            // MultipartResolver 用于处理文件上传
            final MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            if (resolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
                multiFileMap = multipartRequest.getMultiFileMap();
            }
        }

        if (multiFileMap == null || multiFileMap.size() == 0) {
            multiFileMap = getMockMultiValueMap(request);
        } else if (multiFileMap.size() == 1) {
            return multiFileMap.get(multiFileMap.keySet().iterator().next()).get(0);
        }
        return null;
    }

    default MultiValueMap<String, MultipartFile> getMockMultiValueMap(final HttpServletRequest request) {

        try {
            Class cls = Class.forName("org.springframework.mock.web.MockMultipartHttpServletRequest");
            if (!request.getClass().isAssignableFrom(cls)) {
                return null;
            }
            Method method = cls.getDeclaredMethod("getMultiFileMap");
            MultiValueMap<String, MultipartFile> map = (MultiValueMap<String, MultipartFile>) method.invoke(request);
            return map;
        } catch (Exception e) {
        }
        return null;
    }

    String storeFile(MultipartFile file);
}
