package com.example.ensurify.dto.response;

import com.example.ensurify.domain.enums.ContractCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetContractDocumentInfoResponse {

    private String name;
    private ContractCategory category;
    private String pdfUrl;
    private List<String> keywords;
}
