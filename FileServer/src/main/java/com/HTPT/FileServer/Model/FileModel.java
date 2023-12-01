package com.HTPT.FileServer.Model;

import lombok.Data;

@Data
public class FileModel {
    private Integer id;
    private String fileName;
    private Double fileSize;
    private String ipAddress;
    private Integer downloadCount;
    private String note;
}
