package com.ordep.syncotable.mapper;

import com.ordep.syncotable.dto.column.request.CreateCardColumnRequest;
import com.ordep.syncotable.dto.column.request.UpdateCardColumnRequest;
import com.ordep.syncotable.dto.column.response.CardColumnResponse;
import com.ordep.syncotable.model.CardColumn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CardColumnMapper {

    @Mapping(target = "cardId", source = "card.id")
    CardColumnResponse toResponse(CardColumn cardColumn);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "card", ignore = true)
    CardColumn toEntity(CreateCardColumnRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "card", ignore = true)
    @Mapping(target = "key", ignore = true)
    void updateEntity(UpdateCardColumnRequest request, @MappingTarget CardColumn cardColumn);

}
