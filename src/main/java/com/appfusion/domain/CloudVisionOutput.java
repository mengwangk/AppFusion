package com.appfusion.domain;

import lombok.Data;

@Data
public class CloudVisionOutput {
	
	private String fileName;
	
	public CloudVisionOutput(final String fileName) {
		this.fileName = fileName;
	}
}
