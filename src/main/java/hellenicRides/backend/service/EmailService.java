package hellenicRides.backend.service;

import java.io.File;

public interface EmailService {
  void sendSimpleEmail(String to, String subject, String content);

  void sendHtmlEmail(String to, String subject, String htmlContent);

  void sendEmailWithAttachment(String to, String subject, String content, File file);
}
