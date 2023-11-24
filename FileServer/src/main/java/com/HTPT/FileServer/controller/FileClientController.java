package com.HTPT.FileServer.controller;

import com.HTPT.FileServer.Model.APIResponse;
import com.HTPT.FileServer.Model.APISucResponse;
import com.HTPT.FileServer.Service.FileServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/client")
public class FileClientController {
    @Autowired
    FileServerService fileServerService;
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileServerService.getFileResource(fileName));
    }
    @GetMapping("/get-list-file")
    public APIResponse getListFileName(){
        return new APISucResponse().withData(fileServerService.getListFileName());
    }
    @PostMapping("/upload")
    public APIResponse uploadFile(@RequestPart MultipartFile file) throws IOException {
        fileServerService.uploadFile(file);
        return new APISucResponse().emptyBody();
    }
}
