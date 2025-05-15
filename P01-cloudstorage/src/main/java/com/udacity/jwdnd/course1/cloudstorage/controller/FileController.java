package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/files")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize maxFileSize;

    @PostMapping("/saving")
    public String fileUpload(@RequestParam("fileUpload") MultipartFile multipartFile, Model model, Authentication authentication) throws IOException {
        Integer userId = userService.getUser(authentication.getName()).getUserId();
        String filename = multipartFile.getOriginalFilename();

        logger.info("Attempting to upload file: {}", filename);

        // Check if a file is selected
        if (multipartFile.isEmpty() || filename == null) {
            logger.error("No file selected for upload.");
            model.addAttribute("error", "Please select a file to upload.");
            model.addAttribute("success", false);
            return "result";
        }

        // Check if the file already exists
        if (!fileService.isFilenameAvailable(filename, userId)) {
            logger.error("File already exists: {}", filename);
            model.addAttribute("error", "The file already exists.");
            model.addAttribute("success", false);
            return "result";
        }

        // Check file size limit
        Long fileSize = multipartFile.getSize();
        if (fileSize > maxFileSize.toBytes()) {
            logger.error("File size exceeds the maximum limit: {}", filename);
            model.addAttribute("error", "File size exceeds the maximum limit.");
            model.addAttribute("success", false);
            return "result";
        }

        // Upload the file
        try {
            String contentType = multipartFile.getContentType();
            byte[] fileData = multipartFile.getBytes();
            fileService.addFile(new File(null, filename, contentType, fileSize, userId, fileData));
            model.addAttribute("success", true);
            logger.info("File uploaded successfully: {}", filename);
        } catch (IOException e) {
            logger.error("Error uploading file: {}", e.getMessage());
            model.addAttribute("error", "An error occurred during file upload.");
            model.addAttribute("success", false);
        }

        return "result";
    }

    @GetMapping("/view/{fileId}")
    public String viewFile(@PathVariable("fileId") Integer fileId, HttpServletResponse response) throws IOException {
        File file = fileService.getFile(fileId);
        response.setContentType(file.getContentType());
        response.setContentLength(file.getFileSize().intValue());
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getFilename() + "\"");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(file.getFileData());
        outputStream.close();
        return "result";
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") Integer fileId, Model model) {
        fileService.deleteFile(fileId);
        model.addAttribute("success", true);
        return "result";
    }
}
