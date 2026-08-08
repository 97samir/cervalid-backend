package com.cervalid.platform.bulk.event.repository;

import com.cervalid.platform.bulk.event.entity.AcademicEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AcademicEventRepository
        extends JpaRepository<AcademicEvent, Long> {

    List<AcademicEvent> findByAcademicProfile_IdOrderByDateAsc(Long profileId);
    List<AcademicEvent> findByAcademicProfile_Id(Long profileId);

    List<AcademicEvent> findByAcademicProfile_IdOrderByTimelineOrderAsc(Long profileId);
}