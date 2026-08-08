package com.cervalid.platform.bulk.job.entity;

import com.cervalid.platform.bulk.job.enums.BulkJobStatus;
import com.cervalid.platform.bulk.job.item.entity.BulkJobItemResult;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bulk_job")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobType; // INVITATION, ACADEMIC, CERTIFICATE

    @Enumerated(EnumType.STRING)
    private BulkJobStatus status;// PENDING, PROCESSING, COMPLETED, FAILED

    private Long institutionId;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;

    private Integer totalRows;
    private Integer successCount;
    private Integer failedCount;

    @OneToMany(mappedBy = "bulkJob", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BulkJobItemResult> itemResults;
}