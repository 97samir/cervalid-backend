package com.cervalid.platform.notification.email;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String apiKey;

    public void sendActivationEmail(String to, String link) {
        try {
            Email from = new Email("no-reply@cervalid.com");
            Email toEmail = new Email(to);
            Content content = new Content(
                    "text/plain",
                    "Activa tu cuenta aquí: " + link
            );

            Mail mail = new Mail(from, "Activación de cuenta", toEmail, content);

            SendGrid sg = new SendGrid(apiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 400) {
                System.err.println("Error enviando email: " + response.getBody());
                return;
            }

            System.out.println("STATUS EMAIL: " + response.getStatusCode());
            System.out.println("BODY EMAIL: " + response.getBody());

        } catch (Exception e) {
            System.err.println("Error enviando correo de activación");
            e.printStackTrace();
        }
    }
}
