package com.HTPT.FileServer.Service.Impl;

import com.HTPT.FileServer.Model.FileModel;
import com.HTPT.FileServer.Service.FileServerService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServerServiceImpl implements FileServerService {
    static final String sourceFolderPath = System.getProperty("user.dir") + File.separator + "fileStorage" + File.separator;

    @Override
    public List<FileModel> getListFileName() {
        List<FileModel> results = new ArrayList<FileModel>();
        File[] files = new File(sourceFolderPath).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    results.add(new FileModel(file.getName(), file.length()));
                }
            }
        }
        return results;
    }

    @Override
    public Resource getFileResource(String fileName) {
        String filePath = sourceFolderPath + fileName;
        return new FileSystemResource(new File(filePath));
    }

    @Override
    public void uploadFile(MultipartFile file) throws IOException {
        String filePath = sourceFolderPath + file.getOriginalFilename();
        file.transferTo(new File(filePath));
    }
}
