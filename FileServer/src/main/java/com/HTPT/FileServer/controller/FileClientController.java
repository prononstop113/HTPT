package com.HTPT.FileServer.controller;

import com.HTPT.FileServer.Exception.AppException;
import com.HTPT.FileServer.Model.APIResponse;
import com.HTPT.FileServer.Model.APISucResponse;
import com.HTPT.FileServer.Service.FileServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping()
public class FileClientController {
    @Autowired
    FileServerService fileServerService;
    @Autowired
    private Environment environment;

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam Integer fileId,@RequestParam String ipAddress) throws AppException, IOException {
        String serverPort = environment.getProperty("local.server.port");
        System.out.println("Downloading from "+serverPort);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileServerService.getFileResource(fileId,ipAddress));
    }
    @GetMapping("/get-list-ip")
    public APIResponse getListIP(){
        return new APISucResponse().withData(fileServerService.getListIp());
    }
    @GetMapping("/get-list-file")
    public APIResponse getListFileName(@RequestParam(required = false) Integer ipAddressId, @RequestParam(required = false) String fileName){

        return new APISucResponse().withData(fileServerService.getListFileName(ipAddressId,fileName));
    }
    @PostMapping("/upload")
    public APIResponse uploadFile(@RequestPart MultipartFile file,@RequestParam String ipAddress, @RequestParam String note) throws Exception {
        fileServerService.uploadFile(file,ipAddress,note);
        return new APISucResponse().emptyBody();
    }
    @DeleteMapping("/delete")
    public APIResponse deleteFile(@RequestParam Integer fileId,@RequestParam String ipAddress,@RequestParam String note) throws Exception {
        fileServerService.deleteFile(fileId,ipAddress,note);
        return new APISucResponse().emptyBody();
    }
}
