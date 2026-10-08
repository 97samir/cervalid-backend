package com.cervalid.platform.academic.credential.dto.view;

import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.user.entity.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialDetailContext {

    private Credential credential;
    private Student student;
    private User user;
    private AcademicProfile profile;
    private Institution institution;
}