package com.HTPT.FileServer.Repository;

import com.HTPT.FileServer.Entity.StorageFile;
import com.HTPT.FileServer.Repository.Custom.FileCustomRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileRepository extends JpaRepository<StorageFile,Long>, FileCustomRepo {
    @Query(value = """
            select * from file where name=:fileName
            """,nativeQuery = true)
    StorageFile findByFileName(String fileName);
}
