package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSenderServiceImpl {

    @Value("#{'${gpb.support.email-to}'}")
    private Set<String> emailTo;

    /**
     * Кол-во запросов
     */
    @Value("${gpb.support.email-from}")
    private String emailFrom;



    protected final JavaMailSender sender;

    public void sendHtml() {

        sender.send(createHtmlMessage("ss"));
    }

    protected SimpleMailMessage createSimpleMessage(String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailTo.toArray(new String[0]));
        message.setFrom(emailFrom);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }

    protected MimeMessage createHtmlMessage(String data) {
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailTo.toArray(new String[0]));
            helper.setFrom(emailFrom);
            helper.setSubject("data.getSubject()");
            helper.setText("data.getHtml()", true);
            return message;
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

}
