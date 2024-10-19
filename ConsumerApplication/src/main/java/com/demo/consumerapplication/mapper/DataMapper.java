package com.demo.consumerapplication.mapper;

import com.demo.consumerapplication.entity.DataRecord;
import com.demo.consumerapplication.model.event.DataEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DataMapper {

    @Mapping(target = "nestedRecords", ignore = true)
    DataRecord eventToEntity(DataEvent dataEvent);
}