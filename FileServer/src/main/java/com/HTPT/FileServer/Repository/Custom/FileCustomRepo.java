package com.HTPT.FileServer.Repository.Custom;

import com.HTPT.FileServer.Model.FileModel;

import java.util.List;

public interface FileCustomRepo {
    List<FileModel> getListFile(Integer ipAddres,String fileName);
}
