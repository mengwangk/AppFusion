package com.appfusion.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.appfusion.security.AuthoritiesConstants;
import com.appfusion.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/api")
public class CloudVisionResource {

	private final Logger log = LoggerFactory.getLogger(CloudVisionResource.class);

	private static final String ENTITY_NAME = "cloudVision";

	public CloudVisionResource() {
		
	}
	
	@PostMapping("/vision")
	@Timed
	@Secured(AuthoritiesConstants.ANONYMOUS)
	public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws URISyntaxException {

		log.info("original file name --- " + file.getOriginalFilename());
		return ResponseEntity.created(new URI("/api/vision/"))
				.headers(HeaderUtil.createAlert("cloudVision.uploaded", file.getOriginalFilename()))
				.body(file.getOriginalFilename());
	}
	
	@GetMapping("/vision")
	@Timed
	@Secured(AuthoritiesConstants.ANONYMOUS)
	public String getFiles() {
		return "HTTP GET request";
	}

	/*
	 * //
	 * https://github.com/spring-guides/gs-uploading-files/blob/master/complete/src/
	 * main/java/hello/FileUploadController.java
	 * 
	 * @ExceptionHandler(StorageFileNotFoundException.class) public
	 * ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc)
	 * { return ResponseEntity.notFound().build(); }
	 */
}
