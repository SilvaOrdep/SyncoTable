package com.ordep.syncotable.mapper;

import com.ordep.syncotable.dto.audit.response.AuditLogResponse;
import com.ordep.syncotable.model.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuditLogMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "diff", ignore = true)
    AuditLogResponse toResponse(AuditLog auditLog);

}
