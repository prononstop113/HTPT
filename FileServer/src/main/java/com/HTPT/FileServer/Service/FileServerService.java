package com.HTPT.FileServer.Service;

import com.HTPT.FileServer.Entity.IPAddress;
import com.HTPT.FileServer.Exception.AppException;
import com.HTPT.FileServer.Model.FileModel;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileServerService {
    List<FileModel> getListFileName(Integer ipId,String fileName);
    Resource getFileResource(Integer fileName,String ipAddress) throws IOException, AppException;
    void uploadFile(MultipartFile file,String ipAddress, String note) throws Exception;
    void deleteFile(Integer fileId,String ipAddress,String note) throws Exception;

    List<IPAddress> getListIp();

}
