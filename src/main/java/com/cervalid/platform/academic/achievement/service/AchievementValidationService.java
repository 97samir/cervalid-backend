package com.cervalid.platform.academic.achievement.service;


import com.cervalid.platform.academic.achievement.entity.Achievement;
import com.cervalid.platform.academic.achievement.enums.AchievementStatus;
import com.cervalid.platform.academic.achievement.repository.AchievementRepository;
import com.cervalid.platform.academic.student.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AchievementValidationService {

    private final AchievementRepository repository;

    public void validateCreate(
            Student student,
            String title) {

        boolean exists =
                repository.existsByStudentIdAndTitle(
                        student.getId(),
                        title);
        if (exists) {
            throw new RuntimeException(
                    "Achievement already exists");
        }
    }

    public void validateCanUpdate(Achievement achievement) {
        validateActive(achievement);
    }

    public void validateCanDeactivate(Achievement achievement) {
        validateActive(achievement);
    }

    private void validateActive(Achievement achievement) {

        if(achievement.getStatus()
                != AchievementStatus.ACTIVE){
            throw new RuntimeException(
                    "Achievement is not active");

        }
    }
}