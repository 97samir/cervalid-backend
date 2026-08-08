package com.cervalid.platform.bulk.invitation.parser.service;

import com.cervalid.platform.bulk.invitation.parser.dto.ParsedFileResult;
import org.springframework.web.multipart.MultipartFile;

public interface BulkFileParser {

    ParsedFileResult parse(MultipartFile file);
}
