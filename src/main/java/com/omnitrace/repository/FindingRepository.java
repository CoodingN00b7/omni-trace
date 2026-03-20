package com.omnitrace.repository;

import com.omnitrace.model.FindingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FindingRepository extends JpaRepository<FindingRecord, Integer> {
    List<FindingRecord> findByFlaggedTrue();
    List<FindingRecord> findByDataType(FindingRecord.DataType dataType);
    long countByFlaggedTrue();
}
