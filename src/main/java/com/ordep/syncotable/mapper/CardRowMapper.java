package com.ordep.syncotable.mapper;

import com.ordep.syncotable.dto.row.request.CreateRowRequest;
import com.ordep.syncotable.dto.row.request.UpdateRowRequest;
import com.ordep.syncotable.dto.row.response.RowResponse;
import com.ordep.syncotable.model.CardRow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CardRowMapper {

    @Mapping(target = "cardId", source = "card.id")
    @Mapping(target = "values", source = "valuesJson")
    RowResponse toResponse(CardRow cardRow);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "card", ignore = true)
    @Mapping(target = "valuesJson", source = "values")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    CardRow toEntity(CreateRowRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "card", ignore = true)
    @Mapping(target = "valuesJson", source = "values")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntity(UpdateRowRequest request, @MappingTarget CardRow cardRow);

}
