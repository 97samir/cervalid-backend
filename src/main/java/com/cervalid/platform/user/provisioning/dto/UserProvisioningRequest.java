//abastecer a bul con los métodos necesarios
package com.cervalid.platform.user.provisioning.dto;

import com.cervalid.platform.common.validation.DocumentType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProvisioningRequest {

    private String email;
    private String name;
    private String lastName;
    private DocumentType documentType;
    private String document;
    private String phone;
    private String password;
    private Boolean active;
}