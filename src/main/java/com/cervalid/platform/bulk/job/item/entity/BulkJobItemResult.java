package com.cervalid.platform.bulk.job.item.entity;

import com.cervalid.platform.bulk.job.entity.BulkJob;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bulk_job_item_results")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkJobItemResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rowNumber;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private boolean success;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private Integer retryCount;
    private LocalDateTime processedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulk_job_id")
    private BulkJob bulkJob;

    private String status;
    // SENT_EMAIL, TOKEN_CREATED, FAILED_EMAIL
}