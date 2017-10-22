package com.appfusion.domain;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CloudVisionInput {

	private String extraField;

    private MultipartFile[] files;
    
}
