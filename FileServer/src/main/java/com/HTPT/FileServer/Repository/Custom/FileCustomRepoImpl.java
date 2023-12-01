package com.HTPT.FileServer.Repository.Custom;

import com.HTPT.FileServer.Model.FileModel;
import com.HTPT.FileServer.Util.DataUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class FileCustomRepoImpl implements FileCustomRepo{
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<FileModel> getListFile(Integer ipAddress, String fileName) {
        String sql = """
                select f.id, f.name as "fileName", f.size as "fileSize", i.address  as "ipAddress", f.download_count, f.note
                from file f
                         join ip_address i on f.uploader_id = i.id
                where f.status='UPLOADED'
                  and (cast(:ipAddress as int) is null or f.uploader_id = :ipAddress)
                  and (cast(:fileName as varchar) is null or f.name like :fileName)
                """;
        Query query = entityManager.createNativeQuery(sql, Tuple.class);
        query.setParameter("ipAddress",ipAddress);
        query.setParameter("fileName",DataUtil.queryLike(fileName));
        List<FileModel> results= DataUtil.convertFromQueryResult(query.getResultList(), FileModel.class);
        return results;
    }
}
