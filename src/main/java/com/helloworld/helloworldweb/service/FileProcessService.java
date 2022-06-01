package com.helloworld.helloworldweb.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.helloworld.helloworldweb.s3.FileFolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileProcessService {

    private final FileService amazonS3Service;

    //이미지를 S3 버킷에 업로드 하는 함수
    public String uploadImage(MultipartFile file) {
        String fileName = amazonS3Service.getFileFolder(FileFolder.IMAGE) + createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException ioe) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생했습니다 (%s)", file.getOriginalFilename()));
        }

        return amazonS3Service.getFileUrl(fileName);
    }

    public String uploadMusic(MultipartFile file) {
        String fileName = amazonS3Service.getFileFolder(FileFolder.MUSIC) + createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException ioe) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생했습니다 (%s)", file.getOriginalFilename()));
        }

        return amazonS3Service.getFileUrl(fileName);
    }

    //버킷에서 파일을 삭제하는 함수
    public void deleteImage(String fileName) {
        amazonS3Service.deleteFile(amazonS3Service.getFileFolder(FileFolder.IMAGE) + fileName);
    }
    public void deleteMusic(String fileName) {
        amazonS3Service.deleteFile(amazonS3Service.getFileFolder(FileFolder.MUSIC) + fileName);
    }

    //유니크한 새로운 파일 이름을 생성하는 함수
    private String createFileName(String originalFileName) {
        String newName = UUID.randomUUID().toString().concat(originalFileName);
        return newName;
    }

    //파일의 확장자를 리턴하는 함수
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public String getFileName(String url) {
        String[] paths = url.split("/");
        return paths[paths.length-1];
    }
}
