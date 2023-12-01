package com.HTPT.FileServer.Repository;

import com.HTPT.FileServer.Entity.IPAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IPAddressRepository extends JpaRepository<IPAddress,Long> {
    @Query(value = """
            Select * from ip_address where address =:address
            """,nativeQuery = true)
    IPAddress findByAddress(String address);
}
