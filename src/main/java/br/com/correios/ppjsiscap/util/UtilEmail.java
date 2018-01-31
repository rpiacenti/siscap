package br.com.correios.ppjsiscap.util;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@SuppressWarnings("serial")
@ApplicationScoped
public class UtilEmail implements Serializable {

    public static final String DEFAULT_ENCODING_SCHEME = "UTF-8";

    //@Resource(mappedName = "java:jboss/mail/siscap")
    private Session mailSession;

    public boolean enviarEmail(String destinatario, String assunto, String texto) throws NotaInvalida {
        boolean result = enviarEmail(destinatario, assunto, texto, (File)null);
        return result;
    }

    public boolean enviarEmail(String destinatario, String assunto, String texto, File arquivo) throws NotaInvalida {
        List<File> arquivos = null;
        if (arquivo != null) {
        	arquivos = new ArrayList<File>();
        	arquivos.add(arquivo);
        }
        boolean result = false;
		try {
			result = enviarEmail(destinatario, assunto, texto, arquivos);
		} catch (NotaInvalida e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }

    public boolean enviarEmail(String destinatario, String assunto, String texto, List<File> arquivos) throws NotaInvalida {
        boolean result = false;
        Properties props = new Properties();
		
        props.put("mail.smtp.host", "smtpac.correiosnet.int");
		props.put("mail.smtp.socketFactory.port", "25");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "25");
        
        
        
        try {
        	// mailSession = Session.getDefaultInstance(props,
    		mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
    			protected PasswordAuthentication getPasswordAuthentication() {
    				return new PasswordAuthentication("80128963", "rpj0029.");
    			}
    		});
        	
    		Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress("suportesiscap@correios.com.br")); // Remetente

			InternetAddress addressTo = new InternetAddress(destinatario);
			message.setRecipient(Message.RecipientType.TO, addressTo);

			message.setSubject(assunto);// Assunto
//            MimeMessage msg = new MimeMessage(this.mailSession);                        
//            InternetAddress addressTo = new InternetAddress(destinatario);
//            msg.setRecipient(Message.RecipientType.TO, addressTo);            
//            msg.setSubject(assunto);            
            MimeMultipart mpRoot = new MimeMultipart("mixed");
            MimeBodyPart mimeBodyPart = null;
            if (texto != null) {
                mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setText(texto, DEFAULT_ENCODING_SCHEME);
                mimeBodyPart.setContent(texto, "text/html; charset=iso-8859-1");
                mpRoot.addBodyPart(mimeBodyPart);
            }    
            if (arquivos != null) {
                for (File file : arquivos) {
                    mimeBodyPart = new MimeBodyPart();
                    DataSource dataSource = new FileDataSource(file);
                    mimeBodyPart.setDisposition(Part.ATTACHMENT);
                    mimeBodyPart.setDataHandler(new DataHandler(dataSource));
                    mimeBodyPart.setFileName(file.getName());
                    mpRoot.addBodyPart(mimeBodyPart);
                    
                }
            }
            message.setContent(mpRoot);
            message.saveChanges();   
           
            Transport.send(message);
            result = true;
        } catch (Exception exc) {
            throw new NotaInvalida("Erro ao enviar e-mail, destinatário: " + destinatario + " - " + exc.getMessage());
        }
        return result;
    }


}
