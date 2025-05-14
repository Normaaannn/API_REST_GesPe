
package com.example.GesPeSpring.Tablas.Usuario;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Norman
 */
@Getter
@Setter
public class PasswordRecoveryDTO {
    
    private String email;
    private String token;
    private String newPassword;
}
