package com.example.ensurify.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.ensurify.common.apiPayload.code.status.ErrorStatus;
import com.example.ensurify.common.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    public byte[] download(String fileKey) {
        try (S3Object s3Object = amazonS3.getObject(bucket, fileKey);
             S3ObjectInputStream stream = s3Object.getObjectContent()) {
            return IOUtils.toByteArray(stream);
        } catch (AmazonS3Exception e) {
            throw new GeneralException(ErrorStatus.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new RuntimeException("S3 파일 다운로드 중 오류 발생: " + e.getMessage(), e);
        }
    }
}