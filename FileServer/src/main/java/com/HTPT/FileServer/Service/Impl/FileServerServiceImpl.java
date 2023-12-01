package com.HTPT.FileServer.Service.Impl;

import com.HTPT.FileServer.Entity.ActionLog;
import com.HTPT.FileServer.Entity.IPAddress;
import com.HTPT.FileServer.Entity.StorageFile;
import com.HTPT.FileServer.Exception.AppException;
import com.HTPT.FileServer.Model.FileModel;
import com.HTPT.FileServer.Model.WebSocketMessage;
import com.HTPT.FileServer.Repository.ActionLogRepository;
import com.HTPT.FileServer.Repository.FileRepository;
import com.HTPT.FileServer.Repository.IPAddressRepository;
import com.HTPT.FileServer.Service.FileServerService;
import com.HTPT.FileServer.Util.Constant;
import com.HTPT.FileServer.Util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Constants;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FileServerServiceImpl implements FileServerService {
    static final String sourceFolderPath = System.getProperty("user.dir")+File.separator+"fileStorage"+File.separator;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private IPAddressRepository ipAddressRepository;
    @Autowired
    private ActionLogRepository actionLogRepository;
    @Override
    public List<FileModel> getListFileName(Integer ipId,String fileName) {
        ipId= (Integer) DataUtil.castToNullIfEmptyOrNull(ipId);
        fileName = (String) DataUtil.castToNullIfEmptyOrNull(fileName);
        List<FileModel> results = fileRepository.getListFile(ipId,fileName);
        return results;
    }
    @Override
    public Resource getFileResource(Integer fileId,String ipAddress) throws AppException {
        StorageFile storageFile = fileRepository.findById(Long.valueOf(fileId)).orElseThrow(() -> new AppException("FS.P0002"));
        IPAddress ipa= checkIfIPExist(ipAddress);
        ipa.setLastActionAt(new Date());
        ipAddressRepository.save(ipa);
        storageFile.setDownloadCount(storageFile.getDownloadCount()+1);
        fileRepository.save(storageFile);
        insertLog(Constant.ACTION_TYPE.DOWNLOAD,ipa.getId(),fileId,Constant.ACTION_STATE.COMPLETE,null);
        return new FileSystemResource(new File(storageFile.getPath()));
    }

    @Override
    public void uploadFile(MultipartFile file,String ipAddress,String note) throws Exception {
        String fileName= file.getOriginalFilename();
        String filePath = sourceFolderPath + fileName;
        IPAddress iad = checkIfIPExist(ipAddress);
        iad.setLastActionAt(new Date());
        StorageFile temp = fileRepository.findByFileName(fileName);
        if(!DataUtil.isNullObject(temp)){
            AppException ex = new AppException("FS.P0001");
            insertLog(Constant.ACTION_TYPE.UPLOAD,iad.getId(),null,Constant.ACTION_STATE.FAILED,ex.getMessage());
            throw ex;
        }
        StorageFile sf = StorageFile.builder()
                .uploaderId(iad.getId())
                .createdAt(new Date())
                .downloadCount(0)
                .name(fileName)
                .size(Double.valueOf(file.getSize()))
                .path(filePath)
                .note(note)
                .status(Constant.FILE_STATE.UPLOADED)
                .build();

        try{
            file.transferTo(new File(filePath));
            fileRepository.save(sf);
            insertLog(Constant.ACTION_TYPE.UPLOAD,iad.getId(),sf.getId(),Constant.ACTION_STATE.COMPLETE,null);
            messagingTemplate.convertAndSend("/topic/file-event", new WebSocketMessage("File uploaded: " + file.getOriginalFilename() + " by "+iad.getAddress()).getContent());
        }
        catch (IOException e){
            AppException ex = new AppException("FS.P0004");
            insertLog(Constant.ACTION_TYPE.UPLOAD,iad.getId(),null,Constant.ACTION_STATE.FAILED,ex.getMessage()+":"+e.getMessage());
            throw ex;
        }
    }

    @Override
    public void deleteFile(Integer fileId,String ipAddress, String note) throws AppException {
        StorageFile storageFile = fileRepository.findById(Long.valueOf(fileId)).orElseThrow(() -> new AppException("FS.P0002"));
        String fileName = storageFile.getName();
        IPAddress ipa = checkIfIPExist(ipAddress);
        ipa.setLastActionAt(new Date());
        try {
            Files.delete(Path.of(storageFile.getPath()));
            storageFile.setPath(null);
            storageFile.setStatus(Constant.FILE_STATE.DELETED);
            fileRepository.save(storageFile);
            insertLog(Constant.ACTION_TYPE.DELETE,ipa.getId(),fileId,Constant.ACTION_STATE.COMPLETE,note);
            messagingTemplate.convertAndSend("/topic/file-event", new WebSocketMessage("File deleted: " +fileName+ " by " +ipAddress).getContent());
        } catch (IOException e) {
            AppException ex = new AppException("FS.P0003");
            insertLog(Constant.ACTION_TYPE.DELETE,ipa.getId(),fileId,Constant.ACTION_STATE.FAILED,ex.getMessage() +":"+e.getMessage());
            throw ex;
        }
    }

    @Override
    public List<IPAddress> getListIp() {
        return ipAddressRepository.findAll();
    }

    public IPAddress checkIfIPExist(String ipAddress){
        IPAddress iad = ipAddressRepository.findByAddress(ipAddress);
        if (iad == null){
            iad= IPAddress.builder()
                    .address(ipAddress)
                    .createdAt(new Date()).build();
            ipAddressRepository.save(iad);
        }
        return iad;
    }
    public void insertLog(String action,Integer ipId,Integer fileId, String state,String comment){
        ActionLog actionLog = ActionLog.builder()
                .action(action)
                .ipId(ipId)
                .time(new Date())
                .state(state)
                .comment(comment)
                .fileId(fileId)
                .build();
        actionLogRepository.save(actionLog);
    }
}
