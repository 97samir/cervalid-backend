package com.cervalid.platform.tenant.entity;

import com.cervalid.platform.common.validation.*;
import com.cervalid.platform.tenant.enums.RequestStatus;
import com.cervalid.platform.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "institution_requests")
public class InstitutionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---------------- DATOS DE LA INSTITUCIÓN ----------------
    @NotBlank
    @Column(nullable = false, unique = true)
    private String institutionName;

    @NotBlank
    @RucValid
    @Column(nullable = false, unique = true, length = 11)
    private String ruc;

    @NotBlank
    private String institutionType; // Universidad, Instituto, Colegio

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    private String address;

    // ---------------- DATOS DEL SOLICITANTE ----------------
    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;

    @NotBlank
    @Column(nullable = false, length = 9, unique = true)
    private String document;

    @NotBlank
    @Pattern(regexp = "\\d{9}", message = "Teléfono inválido")
    @Column(nullable = false, length = 9)
    private String phone;

    @NotBlank
    private String position; // Rector, Director, etc.

    @NotBlank
    @Email(message = "Correo de contacto inválido")
    @Column(nullable = false, unique = true)
    private String contactEmail;

    // ---------------- SEGURIDAD ----------------
    @NotBlank
    @Column(nullable = false)
    private String password; // Encriptada luego en el servicio

    // ---------------- VALIDACIÓN ----------------
    @NotBlank
    @Column(nullable = false)
    private String documentAcreditationUrl ; // PDF RUC, Licencia SUNEDU, etc.

    // ---------------- ESTADO DE APROBACIÓN ----------------
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // ---------------- DATOS OPCIONALES ----------------
    private Boolean tienePresenciaDigital;

    @Column(nullable = true)
    private String website;

    @EducationalDomain
    @Column(nullable = true)
    private String institutionalDominio;

    @EducationalEmail
    @Column(nullable = true, unique = true)
    private String institutionalEmail;

    @Column(nullable = true, length = 1000)
    private String description;

    // ============

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    private LocalDateTime approvedAt;

    @ManyToOne
    @JoinColumn(name = "rejected_by")
    private User rejectedBy;

    private LocalDateTime rejectedAt;

    @Column(length = 500)
    private String rejectReason;

    public String resolveLoginEmail() {
        return (institutionalEmail != null && !institutionalEmail.isBlank())
                ? institutionalEmail
                : contactEmail;
    }
}
