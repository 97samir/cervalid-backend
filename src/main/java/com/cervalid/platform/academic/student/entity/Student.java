package com.cervalid.platform.academic.student.entity;

import com.cervalid.platform.audit.entity.AuditableEntity;
import com.cervalid.platform.academic.student.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "academic_students", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "institution_membership_id"}),

        @UniqueConstraint(columnNames = {
                        "institution_id",
                        "student_code"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student extends AuditableEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private UUID publicId;

        private Long userId;

        @Column(nullable = false)
        private Long institutionId;

        @Column(name = "institution_membership_id", nullable = false)
        private Long institutionMembershipId;

        @Column(name = "student_code", nullable = false)
        private String studentCode;

        @Enumerated(EnumType.STRING)
        private StudentStatus status;

        private LocalDate admissionDate;
        private LocalDate graduationDate;

        @Builder.Default
        private boolean deleted = false;

        @Version
        private Long version;

        //@Builder.Default
        //private Integer version = 1;
}