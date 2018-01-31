package br.com.correios.ppjsiscap.util;

/*
 Some SMTP servers require a username and password authentication before you
 can use their Server for Sending mail. This is most common with couple
 of ISP's who provide SMTP Address to Send Mail.

 This Program gives any example on how to do SMTP Authentication
 (User and Password verification)

 This is a free source code and is provided as it is without any warranties and
 it can be used in any your code for free.

 Author : Sudhir Ancha
 */

import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 To use this program, change values for the following three constants,

 SMTP_HOST_NAME -- Has your SMTP Host Name
 SMTP_AUTH_USER -- Has your SMTP Authentication UserName
 SMTP_AUTH_PWD  -- Has your SMTP Authentication Password

 Next change values for fields

 emailMsgTxt  -- Message Text for the Email
 emailSubjectTxt  -- Subject for email
 emailFromAddress -- Email Address whose name will appears as "from" address

 Next change value for "emailList".
 This String array has List of all Email Addresses to Email Email needs to be sent to.


 Next to run the program, execute it as follows,

 SendMail authProg = new SendMail();

 */

public class UtilSendMail {

	private static final String SMTP_HOST_NAME = "smtpac.correiosnet.int";
	private static final String SMTP_HOST_PORT = "25";
	private static final String SMTP_AUTH_USER = "ronaldpiacenti@correios.com.br";
	private static final String SMTP_AUTH_PWD = "rpj0026.";

	private String emailMsgTxt;
	private String emailSubjectTxt;
	private String emailFromAddress = "suportesiscap@correios.com.br";

	// Add List of Email address to who email needs to be sent to
	public String pMail;
	public static String pSenha;
	public String pMsg;

	
	public static void SendMailJV() {
		// SendMail smtpMailSender = new SendMailJV();

		System.out.println("Passei aqui 1!");
	}

	public String postMailFull() throws NotaInvalida {
		System.out.println("Passei aqui 2!");
		
//		Context initCtx = new InitialContext();
//		Context envCtx = (Context) initCtx.lookup("java:comp/env");
//		Session session = (Session) envCtx.lookup("mail/Session");
//
//		Message message = new MimeMessage(session);
//		message.setFrom(new InternetAddress(request.getParameter("from"));
//		InternetAddress to[] = new InternetAddress[1];
//		to[0] = new InternetAddress(request.getParameter("to"));
//		message.setRecipients(Message.RecipientType.TO, to);
//		message.setSubject(request.getParameter("subject"));
//		message.setContent(request.getParameter("content"), "text/plain");
//		Transport.send(message);

		try {
			boolean debug = true;
			String[] recipients = { this.getPMail() };
			System.out.println("Passei aqui 3!");
			// Set the host smtp address
			Properties props = new Properties();
		
			props.put("mail.smtp.from", emailFromAddress);
		    props.put("mail.smtp.host", SMTP_HOST_NAME);
		    props.put("mail.smtp.port", SMTP_HOST_PORT);
		    props.put("mail.smtp.auth", true);
		    props.put("mail.smtp.socketFactory.port", SMTP_HOST_PORT);
		    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		    props.put("mail.smtp.socketFactory.fallback", "false");
		    props.put("mail.smtp.starttls.enable", "true");
		        
		    Session mailSession = Session.getDefaultInstance(props, new Authenticator() {

		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		            return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
		    }
		    });
			
			Authenticator auth = new SMTPAuthenticator();
			
			Session session = Session.getDefaultInstance(props,null).getDefaultInstance(props, auth);
		//	Transport transport = session.getTransport();
			
//			System.out.println("Email:" + this.getPMail());
//			System.out.println("Senha:" + this.getPSenha());
			session.setDebug(debug);

			// create a message
			Message msg = new MimeMessage(mailSession);

			// set the from and to address
			InternetAddress addressFrom;

			addressFrom = new InternetAddress(this.emailFromAddress);

			msg.setFrom(addressFrom);

			InternetAddress[] addressTo = new InternetAddress[recipients.length];
			for (int i = 0; i < recipients.length; i++) {
				addressTo[i] = new InternetAddress(recipients[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, addressTo);

			// Setting the Subject and Content Type
			msg.setSubject(this.emailSubjectTxt);
			String vMessage = this.getpMsg();
			System.out.println("Msg:" + vMessage);
			msg.setContent(vMessage, "text/plain");

		
			//transport.connect();
		    Transport.send(msg);
		   // transport.close();
		} catch (AddressException ex1) {
			// TODO Auto-generated catch block
			ex1.printStackTrace();
			throw new NotaInvalida("Erro no envio do e-mail.<br>"
					+ ex1.getMessage());
		} catch (MessagingException ex2) {
			// TODO Auto-generated catch block
			ex2.printStackTrace();
			throw new NotaInvalida("Erro no envio do e-mail.<br>"
					+ ex2.getMessage());
		}
		return ("E-mail enviado com suscesso");
	}

	public String postMail(String recipients[], String subject, String message,
			String from) throws MessagingException, NotaInvalida {
		boolean debug = false;
		try{
		// Set the host smtp address
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.jcom.net");

		// create some properties and get the default Session
		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(debug);

		// create a message
		Message msg = new MimeMessage(session);

		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Optional : You can also set your custom headers in the Email if you
		// Want
		msg.addHeader("MyHeaderName", "myHeaderValue");

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		Transport.send(msg);
		
		} catch (AddressException ex1) {
			// TODO Auto-generated catch block
			ex1.printStackTrace();
			throw new NotaInvalida("Erro no envio do e-mail.<br>"
					+ ex1.getMessage());
		} catch (MessagingException ex2) {
			// TODO Auto-generated catch block
			ex2.printStackTrace();
			throw new NotaInvalida("Erro no envio do e-mail.<br>"
					+ ex2.getMessage());
		}
		return ("E-mail enviado com suscesso");
	}

	/**
	 * SimpleAuthenticator is used to do simple authentication when the SMTP
	 * server requires it.
	 */
	private class SMTPAuthenticator extends javax.mail.Authenticator {

		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}

	/**
	 * @return the pMail
	 */
	public String getPMail() {
		return pMail;
	}

	/**
	 * @param mail
	 *            the pMail to set
	 */
	public void setPMail(String mail) {
		pMail = mail;
	}

	/**
	 * @return the pSenha
	 */
	public static String getPSenha() {
		return pSenha;
	}

	/**
	 * @param senha
	 *            the pSenha to set
	 */
	public static void setPSenha(String senha) {
		pSenha = senha;
	}

	public String getpMsg() {
		return pMsg;
	}

	public void setpMsg(String pMsg) {
		this.pMsg = pMsg;
	}

}

