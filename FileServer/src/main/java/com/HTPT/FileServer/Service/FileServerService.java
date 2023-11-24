package com.HTPT.FileServer.Service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileServerService {
    List<String> getListFileName();
    Resource getFileResource(String fileName);
    void uploadFile(MultipartFile file) throws IOException;
}
