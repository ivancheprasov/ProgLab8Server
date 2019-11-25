import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
class JavaMail {
    private static final String ENCODING = "UTF-8";
    private String password;
    public JavaMail(String password){
        this.password=password;
    }
    void registration(String email,String login) {
        String subject = "Confirm registration";
        String smtpHost = "mail.11cows.com";
        String from = "ivan@11cows.org";
        String smtpPort = "25";
        try {
            sendSimpleMessage(login, password, from, email, subject, smtpPort, smtpHost);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private static void sendSimpleMessage(String login, String password, String from, String to, String subject, String smtpPort, String smtpHost)
            throws MessagingException {
        Authenticator auth = new MyAuthenticator(login, password);
        Properties props = System.getProperties();
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.mime.charset", ENCODING);
        Session session = Session.getDefaultInstance(props,auth);
        Message msg = new MimeMessage(session);
        msg.setText("Ваш пароль: "+password);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        Transport.send(msg);
    }
    static class MyAuthenticator extends Authenticator {
        private String user;
        private String password;

        MyAuthenticator(String user, String password) {

            this.user = user;
            this.password = password;
        }
        public PasswordAuthentication getPasswordAuthentication() {
            String user = this.user;
            String password = this.password;
            return new PasswordAuthentication(user, password);
        }
    }
}