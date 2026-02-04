package hellenicRides.backend.service.impl;

import hellenicRides.backend.service.EmailService;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;

  @Value("${app.mail.from}")
  private String senderEmail;

  @PostConstruct
  public void logConfig() {
    if (mailSender instanceof JavaMailSenderImpl impl) {
      log.info(
          "EmailService initialized with Host: {}, Port: {}, Username: {}",
          impl.getHost(),
          impl.getPort(),
          impl.getUsername());
      String pass = impl.getPassword();
      log.info(
          "Password loaded: {} (length: {})",
          (pass != null && !pass.isEmpty()) ? "YES" : "NO",
          (pass != null) ? pass.length() : 0);
    }
    log.info("Email sender (From): {}", senderEmail);
  }

  @Override
  public void sendSimpleEmail(String to, String subject, String content) {
    log.info("Sending simple email to {}", to);
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(senderEmail);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(content);
    mailSender.send(message);
    log.info("Simple email sent to {}", to);
  }

  @Override
  public void sendHtmlEmail(String to, String subject, String htmlContent) {
    log.info("Sending HTML email to {}", to);
    MimeMessage message = mailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setFrom(senderEmail);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlContent, true);
      mailSender.send(message);
      log.info("HTML email sent to {}", to);
    } catch (MessagingException e) {
      log.error("Failed to send HTML email to {}", to, e);
      throw new RuntimeException("Failed to send email", e);
    }
  }

  @Override
  public void sendEmailWithAttachment(String to, String subject, String content, File file) {
    log.info("Sending email with attachment to {}", to);
    MimeMessage message = mailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setFrom(senderEmail);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(content);

      FileSystemResource res = new FileSystemResource(file);
      helper.addAttachment(file.getName(), res);

      mailSender.send(message);
      log.info("Email with attachment sent to {}", to);
    } catch (MessagingException e) {
      log.error("Failed to send email with attachment to {}", to, e);
      throw new RuntimeException("Failed to send email with attachment", e);
    }
  }
}
