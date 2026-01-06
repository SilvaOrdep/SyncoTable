package com.ordep.syncotable.service.audit;

import com.ordep.syncotable.dto.audit.response.AuditLogResponse;
import com.ordep.syncotable.mapper.AuditLogMapper;
import com.ordep.syncotable.model.AuditLog;
import com.ordep.syncotable.model.User;
import com.ordep.syncotable.repository.AuditLogRepository;
import com.ordep.syncotable.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuditLogService {
    private final AuditLogRepository logs;
    private final AuditLogMapper logMapper;

    public AuditLogResponse createAuditLog(String entityType, String action, User user){
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityType(entityType);
        auditLog.setAction(action);
        auditLog.setUser(user);
        return logMapper.toResponse(logs.save(auditLog));
    }

    public List<AuditLogResponse> findAllAuditLogs(){
               return logs.findAll().stream().map(logMapper::toResponse).toList();
    }
}
