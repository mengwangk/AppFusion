package com.appfusion.web.rest.errors;

import lombok.Data;

@Data
public class CloudVisionError {
	
	String errCode;
    String errDesc;

    public CloudVisionError(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

}
