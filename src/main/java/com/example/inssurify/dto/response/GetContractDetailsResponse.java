package com.example.inssurify.dto.response;

import com.example.inssurify.domain.enums.ContractCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetContractDetailsResponse {

    private String name;
    private ContractCategory category;
    private String pdfUrl;
    private List<String> keywords;
}
