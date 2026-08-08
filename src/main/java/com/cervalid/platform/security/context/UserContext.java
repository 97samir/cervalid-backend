package com.cervalid.platform.security.context;

import com.cervalid.platform.common.enums.RoleName;

// saber la institución en cualquier capa
public class UserContext {

    private static final ThreadLocal<Long> institutionId = new ThreadLocal<>();
    private static final ThreadLocal<RoleName> role = new ThreadLocal<>();
    private static final ThreadLocal<Long> userId = new ThreadLocal<>();
    private static final ThreadLocal<Long> institutionUserId = new ThreadLocal<>();

    public static void setInstitutionUserId(Long id){
        institutionUserId.set(id);
    }

    public static Long getInstitutionUserId(){
        return institutionUserId.get();
    }

    // -------- INSTITUTION --------
    public static void setInstitutionId(Long id) {
        institutionId.set(id);
    }

    public static Long getInstitutionId() {

        Long id = institutionId.get();

        if (id == null) {
            throw new IllegalStateException(
                    "UserContext.institutionId is NULL in thread: "
                            + Thread.currentThread().getName()
            );
        }

        return id;
    }

    // -------- ROLE --------
    public static void setRole(RoleName roleName) {
        role.set(roleName);
    }

    public static RoleName getRole() {
        return role.get();
    }

    // -------- USER ID (opcional pero recomendado) --------
    public static void setUserId(Long id) {
        userId.set(id);
    }

    public static Long getUserId() {
        return userId.get();
    }

    // -------- CLEAR (muy importante) --------
    public static void clear() {
        institutionId.remove();
        institutionUserId.remove();
        role.remove();
        userId.remove();
    }
}
