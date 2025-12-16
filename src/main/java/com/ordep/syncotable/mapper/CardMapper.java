package com.ordep.syncotable.mapper;

import com.ordep.syncotable.dto.card.request.CreateCardRequest;
import com.ordep.syncotable.dto.card.request.UpdateCardRequest;
import com.ordep.syncotable.dto.card.response.CardResponse;
import com.ordep.syncotable.dto.card.response.CardSummaryResponse;
import com.ordep.syncotable.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CardMapper {

    @Mapping(target = "lockedBy", source = "lockedBy.username")
    @Mapping(target = "createdBy", source = "createdBy.username")
    CardResponse toResponse(Card card);

    CardSummaryResponse toSummaryResponse(Card card);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lockedBy", ignore = true)
    @Mapping(target = "lockedAt", ignore = true)
    Card toEntity(CreateCardRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lockedBy", ignore = true)
    @Mapping(target = "lockedAt", ignore = true)
    void updateEntity(UpdateCardRequest request, @MappingTarget Card card);

}
