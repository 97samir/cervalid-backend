package com.cervalid.platform.academic.achievement.entity;

import com.cervalid.platform.academic.achievement.enums.AchievementStatus;
import com.cervalid.platform.academic.achievement.enums.AchievementType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "academic_achievements")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID publicId;
    private Long institutionId;
    private Long studentId;
    private String title;

    @Column(length = 3000)
    private String description;

    @Enumerated(EnumType.STRING)
    private AchievementType type;

    private String issuer;
    private LocalDate achievedDate;

    @Enumerated(EnumType.STRING)
    private AchievementStatus status;
}