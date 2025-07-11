package org.waterwood.waterfunservice.DTO.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.waterwood.waterfunservice.DTO.common.LoginType;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmailLoginRequestBody extends LoginRequestBody {
    private String email;
    private String emailCode;
    public EmailLoginRequestBody() {
        this.setLoginType(LoginType.EMAIL);
    }
}
