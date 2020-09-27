package com.wahaha.demo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class UploadFileDTO implements Serializable {


    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public UploadFileDTO(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

}