package com.demo.consumerapplication.repository;

import com.demo.consumerapplication.entity.DataRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRecordRepository extends MongoRepository<DataRecord, String> {

}