package com.example.GesPeSpring.Tablas.Usuario;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    public void enviarFacturaPorCorreo(String destinatario, String asunto, String cuerpo, byte[] pdfBytes, String nombreArchivo) throws Exception {

        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(cuerpo, true);

        ByteArrayResource pdfAdjunto = new ByteArrayResource(pdfBytes);

        helper.addAttachment(nombreArchivo, pdfAdjunto);

        mailSender.send(mensaje);
    }
}
