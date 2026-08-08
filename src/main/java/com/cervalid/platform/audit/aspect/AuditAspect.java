package com.cervalid.platform.audit.aspect;

import com.cervalid.platform.audit.annotation.Auditable;
import com.cervalid.platform.audit.entity.AuditLog;
import com.cervalid.platform.audit.service.AuditService;
import com.cervalid.platform.security.context.SecurityContextService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    private final SecurityContextService securityContext;
    private final HttpServletRequest request;

    @AfterReturning(
            value = "@annotation(auditable)",
            returning = "result"
    )
    public void audit(Auditable auditable, Object result) {

        try {

            System.out.println("AUDIT EJECUTANDOSE");
            System.out.println("RESULT CLASS: " + (result != null ? result.getClass() : "NULL"));
            System.out.println("RESOURCE FIELD: " + auditable.resourceIdField());

            AuditLog log = new AuditLog();

            log.setAction(auditable.action());
            log.setResource(auditable.resource());

            // Protección contra null en security context
            Long userId = securityContext.getUserId();
            if (userId != null) {
                log.setActorUserId(userId);
            }

            String email = securityContext.getEmail();
            if (email != null) {
                log.setActorEmail(email);
            }

            Long institutionId = securityContext.getInstitutionId();
            if (institutionId != null) {
                log.setInstitutionId(institutionId);
            }

            // Protección request
            if (request != null) {
                log.setIpAddress(request.getRemoteAddr());
            }

            // Extraer resource id
            log.setResourceId(extractId(result, auditable.resourceIdField()));

            // Guardar log
            auditService.log(log);

        } catch (Exception e) {
            // Nunca romper flujo principal
            System.out.println("ERROR EN AUDIT ASPECT");
            e.printStackTrace();

        }
    }

    private String extractId(Object result, String fieldName) {

        if (result == null) {return null;}

        if (fieldName == null || fieldName.isBlank()) {return null;}

        try {

            Field field = result.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            Object value = field.get(result);

            return value != null ? value.toString() : null;

        } catch (Exception e) {

            System.out.println("NO SE PUDO EXTRAER RESOURCE ID: " + e.getMessage());
            return null;

        }
    }
}