package com.cervalid.platform.academic.transcript.mapper;

import com.cervalid.platform.academic.transcript.dto.item.TranscriptItemDTO;
import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import org.springframework.stereotype.Component;

@Component
public class TranscriptItemMapper {

    public TranscriptItemDTO toResponse(
            TranscriptItem item) {

        return TranscriptItemDTO.builder()
                .publicId(item.getPublicId())
                .courseCode(item.getCourseCode())
                .courseName(item.getCourseName())
                .credits(item.getCredits())
                .grade(item.getGrade())
                .build();
    }
}