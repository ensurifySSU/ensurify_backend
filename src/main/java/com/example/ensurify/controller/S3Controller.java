package com.example.ensurify.controller;

import com.example.ensurify.common.apiPayload.BasicResponse;
import com.example.ensurify.dto.request.LoginRequest;
import com.example.ensurify.dto.response.LoginResponse;
import com.example.ensurify.dto.response.PostContractPdfResponse;
import com.example.ensurify.dto.response.UploadFileResponse;
import com.example.ensurify.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.Principal;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "S3", description = "AWS S3 관련 API입니다.")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/upload")
    @Operation(summary = "파일 업로드", description = "파일을 업로드하여 URL을 생성합니다.")
    public BasicResponse<UploadFileResponse> uploadFile(@RequestPart(value = "file") MultipartFile multipartFile) throws IOException {

        String fileUrl = s3Service.saveFile(multipartFile);

        UploadFileResponse response = UploadFileResponse
                .builder()
                .fileUrl(fileUrl)
                .build();

        return BasicResponse.onSuccess(response);
    }

    @GetMapping("/download")
    @Operation(summary = "파일 다운로드", description = "파일을 로컬 기기에 다운로드합니다.")
    public ResponseEntity<ByteArrayResource> downloadFiles(@RequestParam String fileName) {
        try {
            byte[] data = s3Service.download(fileName);
            ByteArrayResource resource = new ByteArrayResource(data);
            return ResponseEntity
                    .ok()
                    .contentLength(data.length)
                    .header("Content-type", "application/octet-stream")
                    .header("Content-disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "utf-8") + "\"")
                    .body(resource);
        } catch (IOException ex) {
            return ResponseEntity.badRequest().contentLength(0).body(null);
        }
    }
}
