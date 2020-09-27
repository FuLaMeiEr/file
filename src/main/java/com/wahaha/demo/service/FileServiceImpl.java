package com.wahaha.demo.service;

import com.wahaha.demo.exception.FileException;
import com.wahaha.demo.property.FileProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService {

    private final Path fileStorageLocation; // 文件在本地存储的地址

    public FileServiceImpl(FileProperties fileProperties) {
        // 获取上传文件夹的绝对路径
        this.fileStorageLocation = Paths.get(fileProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /**
     * 存储文件到系统
     *
     * @param file 文件
     * @return 文件名
     */
    public String storeFile(MultipartFile file) {

        // 得到上传时的文件名
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // 判断对象是否有无效字符
            if(fileName.contains("..")) {
                throw new FileException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // 将文件复制到目标位置(用相同的名称替换现有文件)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
