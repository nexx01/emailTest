package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.Properties;
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



//    protected final JavaMailSender sender;

    public void sendHtml() throws MessagingException {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.getJavaMailProperties();

        sender.setHost("smtp.gmail.com");
        sender.setPort(587);

        Properties props = sender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "easy.reserve.kazan@gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("spring.mail.password", "vsondnkaonnwkmfj");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        sender.testConnection();


//        sender.send(crea
//        teHtmlMessage("ss",sender));
    }

    protected SimpleMailMessage createSimpleMessage(String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailTo.toArray(new String[0]));
        message.setFrom(emailFrom);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }

    protected MimeMessage createHtmlMessage(String data, JavaMailSender sender) {
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
