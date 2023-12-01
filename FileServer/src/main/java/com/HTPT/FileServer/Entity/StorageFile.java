package com.HTPT.FileServer.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="file")
public class StorageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer uploaderId;
    @Column
    private String name;
    @Column
    private Double size;
    @Column
    private Date createdAt;
    @Column
    private String status;
    @Column
    private Integer downloadCount;
    @Column
    private String note;
    @Column
    private String path;
}
