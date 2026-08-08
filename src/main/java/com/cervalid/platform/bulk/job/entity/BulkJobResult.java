package com.cervalid.platform.bulk.job.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bulk_job_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkJobResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long jobId;
    private Integer total;
    private Integer success;
    private Integer failed;

    @Column(columnDefinition = "TEXT")
    private String jsonResult;
}