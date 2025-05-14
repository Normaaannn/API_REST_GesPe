
package com.example.GesPeSpring.Tablas.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 *
 * @author Norman
 */

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetLink(String to, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("GesPe - Reiniciar contraseña");
        message.setText("Haz click en este link para reiniciar tu contraseña: " + link);
        mailSender.send(message);
    }
}

