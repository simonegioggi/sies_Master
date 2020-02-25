package siap.sico.inviosegnalazione.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import siap.sico.inviosegnalazione.config.ISProperties;
import siap.sico.utente.model.UtenteModel;
import siap.sico.versione.util.VersionProperties;
import siap.sico.web.ActionSiap;

import com.sun.mail.smtp.SMTPTransport;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * MEV10-s3: aggiunta classe per gestire l'invio di una segnalazione
 * 
 * @author sgioggi
 * @version 1.0
 */
public class InvioSegnalazione extends ActionSiap implements ISCostanti {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private File file;
	String path1 = "", path2 = "", path3 = "", path4 = "", path5 = "";

	public String processRequest() throws F3BException {

		// info per il log
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// Recupero Info dal file Properties "inviosegnalazione"
		String host = ISProperties.getProperty("mail.smtp.host");
		String user = ISProperties.getProperty("mail.smtp.user");
		String password = ISProperties.getProperty("mail.smtp.pwd");
		String protocol = ISProperties.getProperty("mail.smtp.protocol");
		boolean starttls = ISProperties.getProperty("mail.smtp.starttls.enable") != null
				? Boolean.parseBoolean(ISProperties.getProperty("mail.smtp.starttls.enable"))
				: false;
		int port = -1;
		try {
			port = Integer.parseInt(ISProperties.getProperty("mail.smtp.port"));
		} catch (NumberFormatException nEx) {
			// do nothing
		}

		// Dichiaro Oggetto per inviare email
		SMTPTransport transport = null;
		try {
			// Recupero i dati dell'utente connesso
			UtenteModel utenteConnesso = (UtenteModel) getSessionAttribute("UtenteConnesso");
			// Get system properties
			Properties properties = System.getProperties();
			// Imposto valore properties
			properties.put("mail.smtp.host", host);
			properties.put("mail.smtp.debug", ISProperties.getProperty("mail.smtp.debug"));
			properties.put("mail.smtp.auth", ISProperties.getProperty("mail.smtp.auth"));
			properties.put("mail.smtp.ssl.trust", host);
			// 20190111 [SG]: aggiunta nuova proprieta' per passaggio a TLS 1.2 - decommentare riga sottostante
			// properties.put("mail.smtps.ssl.protocols", ISProperties.getProperty("mail.smtps.ssl.protocols"));
			// Get session
			Session session = Session.getInstance(properties, null);
			// valore x debug
			session.setDebug(Boolean.getBoolean(ISProperties.getProperty("mail.smtp.debug")));

			// Compongo messaggio per la risposta
			MimeMessage msg = new MimeMessage(session);
			// Imposto l'account
			String account = ISProperties.getProperty("mail.smtp.account");
			// Imposto il mittente
			msg.setFrom(new InternetAddress(account));
			// Destinatario email
			// MEV_66: il destinatario è unico, non c'è distinzione siep, sius, sige
			String dest = ISProperties.getProperty("mail.smtp.to");
			String sottoSistema = "";
			String codTipoUff = utenteConnesso.getUfficioUtente().getCodTipoUfficio();
			if (utenteConnesso.getUserProfile().getProfileId().intValue() != 99
					&& utenteConnesso.getUserProfile().getProfileId().intValue() != 90) {
				if (codTipoUff.equals("UEPE") || codTipoUff.equals("UEPESS")) {
					// per SIEPE tale funzionalità non è concessa!!!
					siesLogger.info("UFFICIO SIEPE");
					// MEV_66: aggiunti uffici minorili
				} else if (codTipoUff.startsWith("TDS") || codTipoUff.startsWith("UDS")
						|| codTipoUff.startsWith("TDSM") || codTipoUff.startsWith("UDSM")) {
					// dest = ISProperties.getProperty("mail.smtp.destsius");
					sottoSistema = "SIUS";
				} else if (codTipoUff.equals("PGCAP") || codTipoUff.equals("PM")
						|| codTipoUff.equals("PMM")) {
					// dest = ISProperties.getProperty("mail.smtp.destsiep");
					sottoSistema = "SIEP";
				} else {
					// dest = ISProperties.getProperty("mail.smtp.destsige");
					sottoSistema = "SIGE";
				}
			}

			// MESSAGE TO
			if (dest != null && dest.length() > 0)
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(dest));
			// MESSAGE CC
			String destReferente = getRequestStringParameter("emailReferente");
			// String destUfficio = utenteConnesso.getUfficioUtente().getEMail();
			String destRichiedente = "";
			if (getRequestStringParameter("autoInvio") != null
					&& getRequestStringParameter("autoInvio").length() > 0
					&& "on".equals(getRequestStringParameter("autoInvio")))
				destRichiedente = utenteConnesso.getEmail();
			List<String> cc = new ArrayList<String>();
			if (destReferente != null && destReferente.length() > 0)
				cc.add(destReferente);
			// if (destUfficio != null && destUfficio.length() > 0)
			// cc.add(destUfficio);
			if (destRichiedente != null && destRichiedente.length() > 0)
				cc.add(destRichiedente);
			// MEV_66: aggiunto campo in cc
			String ccFisso = ISProperties.getProperty("mail.smtp.cc");
			if (ccFisso != null && ccFisso.length() > 0)
				cc.add(ccFisso);
			// FINE MEV_66
			InternetAddress[] iaCC = new InternetAddress[cc.size()];
			for (int i = 0; i < cc.size(); i++)
				iaCC[i] = new InternetAddress(cc.get(i));
			if (iaCC.length > 0)
				msg.addRecipients(Message.RecipientType.CC, iaCC);

			// MESSAGE SUBJECT
			msg.setSubject("Segnalazione_SIES_" + sottoSistema + "_" + codTipoUff + "_"
					+ DateUtils.getSysDate("dd/MM/yyyy_HH:mm:ss"));
			// info per il log
			siesLogger.debug(msg.getSubject());

			MimeBodyPart mbp = new MimeBodyPart();

			// Attacco i rimanenti body part alla mail
			Multipart mp = new MimeMultipart();

			// recupero allegati
			// parses the request's content to extract file data
			Hashtable<String, FileItem> h = getRequestMultipart().getHash();
			Enumeration<String> keys = h.keys();
			// Process the uploaded file items
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				FileItem fi = (FileItem) h.get(key);
				if (!fi.isFormField()) {
					// Get the uploaded file parameters
					String fieldName = fi.getFieldName();
					String fileName = fi.getName();
					// intervento per anomalia_Richiesta di supporto e di assistenza formativa(email Maffucci del 20-11-2018)
					siesLogger.debug(fileName);
					// Write the file
					if (fileName != null && fileName.length() > 0) {
						String path = fileName.substring(fileName.lastIndexOf("\\") + 1);
						// intervento per anomalia_Richiesta di supporto e di assistenza formativa(email Maffucci del 20-11-2018)
						siesLogger.debug(path);
						String tempPath = getRequestMultipart().getTempPath();
						siesLogger.debug(tempPath);
						if (tempPath.lastIndexOf("//") >= 0)
							file = new File(tempPath
									+ fileName.substring(fileName.lastIndexOf("\\")));
						else
							file = new File(tempPath + path);
						siesLogger.debug(file.getPath());
						// fine intervento per anomalia_Richiesta di supporto e di assistenza formativa(email Maffucci del 20-11-2018)
						fi.write(file);						
						if (fieldName.endsWith("5"))
							path5 = path;
						else if (fieldName.endsWith("4"))
							path4 = path;
						else if (fieldName.endsWith("3"))
							path3 = path;
						else if (fieldName.endsWith("2"))
							path2 = path;
						else
							path1 = path;
						BodyPart bp = createAttach(file);
						mp.addBodyPart(bp);
					}
				}
			}

			// set di proprietà
			mbp.setDataHandler(new DataHandler(buildhtmlMail(utenteConnesso, sottoSistema), "text/html"));
			mbp.setFileName("Segnalazione_SIES_" + sottoSistema + "_" + codTipoUff + "_"
					+ DateUtils.getSysDate("dd/MM/yyyy_HH:mm:ss") + ".htm");
			mp.addBodyPart(mbp);
			// add the Multipart to the message
			msg.setContent(mp);
			// set the Date: header
			msg.setSentDate(new Date());
			// send the message
			msg.saveChanges();

			// Inizializzo oggetto per la comunicazione con il server di posta
			transport = (SMTPTransport) session.getTransport(protocol);
			transport.setStartTLS(starttls);
			// Eseguo connessione al server
			if (port == -1)
				transport.connect(host, user, password);
			else
				transport.connect(host, port, user, password);
			// Invio messaggio
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();

			// info per il log
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(getClass().getName() + ".processRequest: fine");

			// svuoto cartella temp
			File f = new File(getRequestMultipart().getTempPath());
			if (f.exists()) {
				File[] files = f.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile() && (path1.equals(files[i].getName())
							|| path2.equals(files[i].getName()) || path3.equals(files[i].getName())
							|| path4.equals(files[i].getName()) || path5.equals(files[i].getName())))
						files[i].delete();
				}
			}

			// VALORE DI RITORNO
			return PG_PAGINA_INVIO_SEGNALAZIONE_OK;
		} catch (SendFailedException sfe) {
			throw new F3BException("SendMail Error!" + sfe);
		} catch (Exception sqe) {
			sqe.printStackTrace();
			throw new F3BException("SendMail Error! non posso inviare la mail  : " + sqe);
		}
	}

	/**
	 * Metodo per costruire la segnalazione in formato HTML
	 * 
	 * @param utenteConnesso
	 * @param sottoSistema
	 * @return String
	 * @throws F3BException
	 */
	private String buildhtmlMail(UtenteModel utenteConnesso, String sottoSistema) throws F3BException {

		// SEGNALAZIONE - COSTANTI
		String htmlMail = "";
		String titoloReferente = getRequestStringParameter("titoloReferente");
		String cognomeReferente = getRequestStringParameter("cognomeReferente");
		String nomeReferente = getRequestStringParameter("nomeReferente");
		String telefonoReferente = getRequestStringParameter("telefonoReferente");
		String emailReferente = getRequestStringParameter("emailReferente");
		String titoloRichiedente = getRequestStringParameter("titoloRichiedente");
		String funzionalita = getRequestStringParameter("funzionalita");
		String azione = getRequestStringParameter("azione");
		String tipoSegnalazione = getRequestStringParameter("tipoSegnalazione");
		String gravitaSegnalazione = getRequestStringParameter("gravitaSegnalazione");
		String annoProcedimento = getRequestStringParameter("annoProcedimento");
		String numeroProcedimento = getRequestStringParameter("numeroProcedimento");
		String oggettoSegnalazione = getRequestStringParameter("oggettoSegnalazione");
		String descSegnalazione = getRequestStringParameter("descSegnalazione");
		String ai = (getRequestStringParameter("autoInvio") != null
				&& getRequestStringParameter("autoInvio").length() > 0
				&& "on".equals(getRequestStringParameter("autoInvio"))) ? "" : " NON";

		// SEGNALAZIONE - HTML
		String l = "style='border-width :thin thin thin thin; border-style:solid; padding: 1px 1px 1px 1px; text-align: left; border-color: #DEDEDE; font-family: 'Tahoma'; font-size: 12px; font-weight: bold;'";
		String label = "style='color: navy; border: white; font-family: 'Tahoma'; font-size: 12px; font-weight: bold; text-align: left;'";
		String campo = "style='color: Blue; border: white; font-family: 'Tahoma'; font-size: 12px; font-weight: bold; text-align: left; text-transform: uppercase;'";
		String titolo = "style='background-color: #BEC6FC; color: white; font-weight: bold; font-family: 'Tahoma'; border: 2.5px solid #F0F0F0;'";
		String tabella = "style='border: 2.5px solid black; width: 100%;'";
		htmlMail += "<table " + tabella + ">";
		htmlMail += "<tr>";
		htmlMail += "<td align='center' " + titolo + " height='32' colspan='3'>UFFICIO</td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + " width='33%'><font " + label + ">Tipologia Ufficio:</font></td>";
		htmlMail += "<td " + l + " width='33%'><font " + label + ">Sede Ufficio:</font></td>";
		htmlMail += "<td " + l + "><font " + label + ">Telefono Ufficio:</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr><td " + l + "><font " + campo + ">"
				+ utenteConnesso.getUfficioUtente().getDescrTipoUfficio() + "</font></td>";
		htmlMail += "<td " + l + "><font " + campo + ">" + utenteConnesso.getUfficioUtente().getDescrComune()
				+ " ( " + utenteConnesso.getUfficioUtente().getDescProvincia() + " )</font></td>";
		String telUff = (utenteConnesso.getUfficioUtente().getTelefono() != null)
				? utenteConnesso.getUfficioUtente().getTelefono()
				: "";
		htmlMail += "<td " + l + "><font " + campo + ">" + telUff + "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "></td><td " + l + " width='66%' colspan='2'><font " + label
				+ ">Email Ufficio:</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		String emailUff = (utenteConnesso.getUfficioUtente().getEMail() != null)
				? utenteConnesso.getUfficioUtente().getEMail()
				: "";
		htmlMail += "<td " + l + "></td><td " + l + " colspan='2'><font " + campo + ">" + emailUff
				+ "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "</table>";
		htmlMail += "<table " + tabella + ">";
		htmlMail += "<tr>";
		htmlMail += "<td align='center' " + titolo + " height='32' colspan='4'>REFERENTE</td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + " width='25%'><font " + label + ">Titolo Referente:</font></td>";
		htmlMail += "<td " + l + " width='25%'><font " + label + ">Cognome Referente:</font></td>";
		htmlMail += "<td " + l + " width='25%'><font " + label + ">Nome Referente:</font></td>";
		htmlMail += "<td " + l + "><font " + label + ">Telefono Referente:</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "><font " + campo + ">" + titoloReferente + "</font></td>";
		htmlMail += "<td " + l + "><font " + campo + ">" + cognomeReferente + "</font></td>";
		htmlMail += "<td " + l + "><font " + campo + ">" + nomeReferente + "</font></td>";
		htmlMail += "<td " + l + "><font " + campo + ">" + telefonoReferente + "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "></td>";
		htmlMail += "<td " + l + " colspan='3'><font " + label + ">Email Referente:</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "></td>";
		htmlMail += "<td " + l + " colspan='3'><font " + campo + ">" + emailReferente + "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "</table>";
		htmlMail += "<table " + tabella + ">";
		htmlMail += "<tr>";
		htmlMail += "<td align='center' " + titolo + " height='32' colspan='4'>RICHIEDENTE</td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + " width='25%'><font " + label + ">Titolo Richiedente:</font></td>";
		htmlMail += "<td " + l + " width='25%'><font " + label + ">Cognome Richiedente:</font></td>";
		htmlMail += "<td " + l + " width='25%'><font " + label + ">Nome Richiedente:</font></td>";
		htmlMail += "<td " + l + "><font " + label + ">Telefono Richiedente:</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "><font " + campo + ">" + titoloRichiedente + "</font></td>";
		htmlMail += "<td " + l + "><font " + campo + ">" + utenteConnesso.getCognome() + "</font></td>";
		htmlMail += "<td " + l + "><font " + campo + ">" + utenteConnesso.getNome() + "</font></td>";
		String telRich = (utenteConnesso.getTelefono() != null) ? utenteConnesso.getTelefono() : "";
		htmlMail += "<td " + l + "><font " + campo + ">" + telRich + "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "></td>";
		htmlMail += "<td " + l + " colspan='3'><font " + label + ">Email Richiedente:</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "></td>";
		String emailRich = (utenteConnesso.getEmail() != null) ? utenteConnesso.getEmail() : "";
		htmlMail += "<td " + l + " colspan='3'><font " + campo + ">" + emailRich + "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "</table>";
		htmlMail += "<table " + tabella + ">";
		htmlMail += "<tr>";
		htmlMail += "<td align='center' " + titolo + " height='32' colspan='4'>SEGNALAZIONE</td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + " width='25%'><font " + label + ">Sottosistema:</font></td>";
		htmlMail += "<td " + l + "><font " + label + ">Funzionalità:</font></td>";
		htmlMail += "<td " + l + " width='25%'><font " + label + ">Azione:</font></td>";
		htmlMail += "<td " + l + " width='25%'><font " + label + ">Tipologia Segnalazione:</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "><font " + campo + ">" + sottoSistema + "</font></td>";
		htmlMail += "<td " + l + "><font " + campo + ">" + funzionalita + "</font></td>";
		htmlMail += "<td " + l + "><font " + campo + ">" + azione + "</font></td>";
		htmlMail += "<td " + l + "><font " + campo + ">" + tipoSegnalazione + "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + " width='25%'><font " + label + ">Versione Software:</font></td>";
		htmlMail += "<td " + l + " width='25%'><font " + label + ">Gravità Segnalazione:</font></td>";
		htmlMail += "<td " + l + " width='25%'><font " + label + ">Anno Procedimento:</font></td>";
		htmlMail += "<td " + l + "><font " + label + ">Numero Procedimento:</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "><font " + campo + ">" + VersionProperties.getVersion() + "</font></td>";
		htmlMail += "<td " + l + "><font " + campo + ">" + gravitaSegnalazione + "</font></td>";
		htmlMail += "<td " + l + "><font " + campo + ">" + annoProcedimento + "</font></td>";
		htmlMail += "<td " + l + "><font " + campo + ">" + numeroProcedimento + "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + " colspan='4'><font " + label + ">Oggetto Segnalazione</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + " colspan='4'><font " + campo + ">" + oggettoSegnalazione + "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + " colspan='4'><font " + label + ">Descrizione Segnalazione</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + " colspan='4'><font " + campo + ">" + descSegnalazione + "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "</table>";
		htmlMail += "<table " + tabella + ">";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "><font " + label + ">Copia email" + ai
				+ " inviata nella propria casella di posta</font></td>";
		htmlMail += "</tr>";
		htmlMail += "</table>";
		htmlMail += "<table " + tabella + ">";
		htmlMail += "<tr>";
		htmlMail += "<td align='center' " + titolo
				+ " height='32'>ALLEGATI (Dimensione totale massima dei 5 files pari a due megabyte)</td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "><font " + campo + ">" + path1 + "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "><font " + campo + ">" + path2 + "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "><font " + campo + ">" + path3 + "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "><font " + campo + ">" + path4 + "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "<tr>";
		htmlMail += "<td " + l + "><font " + campo + ">" + path5 + "</font></td>";
		htmlMail += "</tr>";
		htmlMail += "</table>";

		// valore di ritorno
		return htmlMail;
	}

	/**
	 * Metodo per allegare file all'email di risposta
	 * 
	 * @param file
	 *            oggetto file
	 * @return MimeMultipart oggetto
	 * @throws Exception
	 *             Gestione eccezioni
	 */
	private BodyPart createAttach(File file) throws Exception {

		// info per il log
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".createAttach: inizio");

		if (file != null && file.exists()) {
			// create a DataSource for the file.
			BodyPart messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(file);
			// Use a DataHandler object to attach the data source to the message.
			// Simply create a DataHandler for the source and attach it to the message:
			messageBodyPart.setDataHandler(new DataHandler(source));
			// Remember to set the filename of the attachment. This permits the
			// recipient to know the name (and type) of the received file.
			messageBodyPart.setFileName(file.getName());
			// info per il log
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(getClass().getName() + ".createAttach: fine");
			// valore di ritorno
			return messageBodyPart;
		}

		// valore di ritorno
		return null;
	}

	/**
	 * Metodo di controllo dimensione file path
	 * 
	 * @param f
	 * @param num
	 * @throws F3BException
	 */
	// private void controllaDimensioneFile(File f, String num) throws F3BException {
	//
	// long unMegaByte = 1048576;
	// // The length is in bytes; 1 Megabyte = 1.048.576 Bytes
	// // = 1.048.576 / 1.024 = 1.024 Kilobytes
	// if (f.length() > unMegaByte)
	// throw new F3BException(
	// "Attenzione! Il file allegato n° " + num + " ha dimensione maggiore del consentito.");
	// }

}