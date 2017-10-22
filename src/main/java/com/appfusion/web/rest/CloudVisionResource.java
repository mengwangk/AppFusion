package com.appfusion.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.http.HttpHeaders;

import com.appfusion.domain.CloudVisionOutput;
import com.appfusion.domain.CloudVisionInput;
import com.appfusion.security.AuthoritiesConstants;
import com.appfusion.service.StorageService;
import com.appfusion.web.rest.errors.StorageFileNotFoundException;
import com.appfusion.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * @see <a href=
 *      "https://github.com/spring-guides/gs-uploading-files/blob/master/complete/src/main/java/hello/FileUploadController.java">Spring
 *      Boot example</a>
 *
 */
@RestController
@RequestMapping("/api")
public class CloudVisionResource {

	private final Logger log = LoggerFactory.getLogger(CloudVisionResource.class);

	private static final String ENTITY_NAME = "cloudVision";

	private final StorageService storageService;

	@Autowired
	public CloudVisionResource(StorageService storageService) {
		this.storageService = storageService;
	}

	
	// TODO
	@PostMapping("/vision")
	@Timed
	@Secured(AuthoritiesConstants.ANONYMOUS)
	public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) throws URISyntaxException {
		log.debug("File uploaded: " + file.getOriginalFilename());
		return ResponseEntity.created(new URI("/api/vision/"))
				.headers(HeaderUtil.createAlert("cloudVision.uploaded", file.getOriginalFilename()))
				.body(file.getOriginalFilename());
	}

	@PostMapping("/vision/multi")
	public ResponseEntity<?> uploadFileMulti(
			@RequestParam(value = "extraField", required = false) Optional<String> extraField,
			@RequestParam("file") MultipartFile[] uploadfiles) throws URISyntaxException {

		// List of file names
		final String fileNames = Arrays.stream(uploadfiles).map(file -> file.getOriginalFilename())
				.filter(fileName -> !StringUtils.isEmpty(fileName)).collect(Collectors.joining(","));

		CloudVisionOutput output = new CloudVisionOutput(fileNames);

		return ResponseEntity.created(new URI("/api/vision/multi"))
				.headers(HeaderUtil.createAlert("cloudVision.uploaded", fileNames)).body(output);
	}

	// TODO
	@PostMapping("/vision/multi/model")
	public ResponseEntity<?> multiUploadFileModel(@ModelAttribute CloudVisionInput model) throws URISyntaxException {
		CloudVisionOutput output = new CloudVisionOutput(model.getExtraField());
		return ResponseEntity.created(new URI("/api/vision/multi"))
				.headers(HeaderUtil.createAlert("cloudVision.uploaded", "")).body(output);
	}

	@GetMapping("/vision/list")
	public String listUploadedFiles(Model model) throws IOException {

		model.addAttribute("files",
				storageService.loadAll()
						.map(path -> MvcUriComponentsBuilder
								.fromMethodName(CloudVisionResource.class, "serveFile", path.getFileName().toString())
								.build().toString())
						.collect(Collectors.toList()));
		return "cloud-vision"; // Map the view file name
	}

	@GetMapping("/vision/{filename:.+}")
	@Timed
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
}
