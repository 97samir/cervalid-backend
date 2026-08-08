package com.cervalid.platform.academic.profile.entity;

import com.cervalid.platform.audit.entity.AuditableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "academic_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicProfile extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID publicId;

    @Column(name = "student_id", nullable = false, unique = true)
    private Long studentId;

    private Long institutionId;
    private String program;
    private String faculty;
    private String modality;
    private String curriculumVersion;
    private Integer currentCycle;
    private String advisor; // tutor
    private Boolean active;
}
