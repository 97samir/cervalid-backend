package com.cervalid.platform.academic.transcript.mapper;

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.transcript.domain.TranscriptSnapshotDTO;
import com.cervalid.platform.academic.transcript.dto.item.TranscriptItemDTO;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TranscriptSnapshotBuilder {

    public TranscriptSnapshotDTO build(
            Transcript transcript,
            Student student,
            List<TranscriptItem> items) {

        TranscriptSnapshotDTO dto = new TranscriptSnapshotDTO();

        dto.setTranscriptPublicId(transcript.getPublicId());
        dto.setStudentPublicId(student.getPublicId());
        dto.setAcademicPeriod(transcript.getAcademicPeriod());

        List<TranscriptItemDTO> itemDTOs = items.stream()
                .map(i -> {
                    TranscriptItemDTO dtoItem = new TranscriptItemDTO();
                    dtoItem.setCourseCode(i.getCourseCode());
                    dtoItem.setCourseName(i.getCourseName());
                    dtoItem.setCredits(i.getCredits());
                    dtoItem.setGrade(i.getGrade());
                    return dtoItem;
                })
                .toList();

        dto.setItems(itemDTOs);

        return dto;
    }
}