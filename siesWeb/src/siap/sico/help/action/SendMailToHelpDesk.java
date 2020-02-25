package siap.sico.help.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import siap.jms.config.JMSProperties;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.help.config.HELPDESKProperties;
import siap.sico.log.controller.ILogAttivita;
import siap.sico.log.model.LogAttivitaModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;

public class SendMailToHelpDesk extends ActionSiap implements ICostantiHelp {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		boolean attivo = HELPDESKProperties.getProperty("ACTIVATED").toUpperCase().equals("S");
		String MailTO = HELPDESKProperties.getProperty("HELPDESKMAIL");
		String ServerSMTP = HELPDESKProperties.getProperty("SMTPSERVER");
		String LogFILE = HELPDESKProperties.getProperty("LOGFILE");
		String SENDERMAIL = HELPDESKProperties.getProperty("SENDERMAIL");
		String SENDERPWD = HELPDESKProperties.getProperty("SENDERPWD");
		if (SENDERPWD.equals("-"))
			SENDERPWD = "";
		int LogTIME = Integer.parseInt(HELPDESKProperties.getProperty("LOGTIME"));
		String Server = JMSProperties.getInstance().getProperty("JMS_LOCAL_MITTENTE");

		if (attivo) {
			try {
				Multipart mp = new MimeMultipart();
				Properties props = System.getProperties();
				// props.put("mail.smtp.host", ServerSMTP);
				props.put("mail.smtp.auth", "true");

				Session session = Session.getInstance(props, null);

				MimeMessage msg = new MimeMessage(session);
				UtenteModel user = (UtenteModel) getSessionAttribute("UtenteConnesso");
				String Nome = user.getNome();
				String Cognome = user.getCognome();
				String Email = user.getEmail();
				String Ip = getRequest().getRemoteAddr();
				String Telefono = user.getTelefono();
				String Ufficio = user.getUfficioUtente().getDescrTipoUfficio() + " di "
						+ user.getUfficioUtente().getDescrComune() + " ("
						+ user.getUfficioUtente().getDescProvincia() + " )";

				msg.setFrom(new InternetAddress("SIES_Server_" + Server));
				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(MailTO, false));

				// msg.setRecipients(Message.RecipientType.BCC,InternetAddress.parse("filippo.lioy@bull.it",
				// false));
				msg.setSubject("Segnalazione Anomalia");

				// Mime part n°1 ----- input utente
				MimeBodyPart mbp1 = new MimeBodyPart();
				String BodyText = "Informazioni Utente\n________________________________________________________________\n\n";
				BodyText += "Utente : " + Nome + " " + Cognome + "\n";
				BodyText += "Email : " + StringUtils.toStringJSP(Email, " - ") + "\n";
				BodyText += "Indirizzo IP : " + StringUtils.toStringJSP(Ip, " - ") + "\n";
				BodyText += "Telefono : " + StringUtils.toStringJSP(Telefono, " - ") + "\n";
				BodyText += "Ufficio : " + Ufficio + "\n";
				BodyText += "________________________________________________________________\n";
				BodyText += "Note Aggiuntive :\n";
				BodyText += getRequestStringParameter("note") + "\n";
				BodyText += "________________________________________________________________\n";

				// part N° 2 ----- LOG
				MimeBodyPart mbp2 = new MimeBodyPart();
				ILogAttivita ilog = SICOLookupRemote.getLogAttivitaRemote();
				LogAttivitaModel logMod = new LogAttivitaModel();
				logMod.setCodOperatore(getCodUtenteConnesso());

				Date DataFine = DateUtils.getSysDate();
				Date DataInizio = DateUtils.moveDateTo(DataFine, GregorianCalendar.MINUTE, -LogTIME);

				Vector allLogs = ilog.ExRicercaLogAttivita(logMod, DataInizio, DataFine, "");

				String HtmlLOG = "";

				HtmlLOG += "<table width=100% border=1>";
				HtmlLOG += "<tr>";
				HtmlLOG += "<td width=5% bgcolor=#cccccc><font size=-1>Utente</font></td>";
				HtmlLOG += "<td width=5% bgcolor=#cccccc><font size=-1>Cognome Nome</font></td>";
				HtmlLOG += "<td width=5% bgcolor=#cccccc><font size=-1>IP</font></td>";
				HtmlLOG += "<td width=5% bgcolor=#cccccc><font size=-1>Data/Ora</font></td>";
				HtmlLOG += "<td width=5% bgcolor=#cccccc><font size=-1>Azione</font></td>";
				HtmlLOG += "<td><font size=-1 bgcolor=#cccccc>Parametri</font></td>";
				HtmlLOG += "</tr>";

				Iterator itx = allLogs.iterator();
				String AzCon = "";
				String[] AzSplit;
				String Rec = "";
//				String[] tmp;
//				int partialLenght = 0;
				while (itx.hasNext()) {
//					partialLenght = 0;
					LogAttivitaModel log = (LogAttivitaModel) itx.next();
					Rec = log.getRecord();

					AzSplit = log.getAzioneContestoJava().split("[.]");
					if (AzSplit.length > 4) {
						AzCon = "<font color=red>" + AzSplit[2].toUpperCase() + "</font><BR>" + AzSplit[4];
					} else {
						AzCon = "<font color=red>" + AzSplit[2].toUpperCase() + "</font><BR>" + AzSplit[3];
					}
					HtmlLOG += "<tr>";
					HtmlLOG += "<td><font size=-2>" + log.getCodOperatore() + "</font></td>";
					HtmlLOG += "<td><font size=-2>" + log.getCognome() + "&nbsp;" + log.getNome() + "</td>";
					HtmlLOG += "<td><font size=-2>" + log.getIpUtente() + "</font></td>";
					HtmlLOG += "<td nowrap><font size=-2>"
							+ (DateUtils.getDateToString(log.getData(), "dd-MM-yyyy HH:mm:ss")).replaceAll(
									" ", "<br>") + "</font></td>";
					HtmlLOG += "<td><font size=-2>" + AzCon + "</font></td>";
					HtmlLOG += "<td><font size=-2>" + Rec + "</font></td>";

					HtmlLOG += "</tr>";

				}
				HtmlLOG += "</table>";
				mbp2.setDataHandler(new DataHandler(HtmlLOG, "text/html"));
				mbp2.setFileName(getCodUtenteConnesso() + DateUtils.getSysDate("yyyy_MM_dd-hhmmss") + ".htm");

				// Mime part n°3 ----- doc da Tab evento
				MimeBodyPart mbp3 = new MimeBodyPart();
				IEvento iEve = SICOLookupRemote.getEventoRemote();
				String docEvento = "";
				EventoModel lEveMod = iEve.ExRicercaUltimoEventoGeneratoByCodUtente(getCodUtenteConnesso());
				if (lEveMod != null) {
					if (lEveMod.getExistBlob()) {
						ByteArrayOutputStream ldoc = iEve.ExGetDocumento(lEveMod);

//						ByteArrayInputStream lByteArrayInput = new ByteArrayInputStream(ldoc.toByteArray());
						docEvento = getCodUtenteConnesso() + DateUtils.getSysDate("yyyy_MM_dd-hhmmss")
								+ ".rtf";
						FileOutputStream file = new FileOutputStream(docEvento);
						file.write(ldoc.toByteArray());
						file.close();
						FileDataSource fds = new FileDataSource(docEvento);
						mbp3.setDataHandler(new DataHandler(fds));
						mbp3.setFileName(docEvento);
						BodyText += "Data Documento RTF : "
								+ DateUtils.getDateToString(lEveMod.getDataAggiornamento(),
										"dd/MM/yyyy - hh:mm:ss");
						BodyText += "\n________________________________________________________________\n";
						mp.addBodyPart(mbp3);
					}
				}
				// Mime part n° 4 ----- html log di sistema
				MimeBodyPart mbp4 = new MimeBodyPart();
				String thisServer = getRequest().getServerName();
				int thisServerPort = getRequest().getServerPort();
				String thisServerProtocol = "http";

				URL thisServerLog = new URL(thisServerProtocol, thisServer, thisServerPort, LogFILE);
				mbp4.setDataHandler(new DataHandler(thisServerLog));
				mbp4.setFileName("ServerLog.htm");
				mp.addBodyPart(mbp4);

				// Attacco i rimanenti body part alla mail
				mbp1.setText(BodyText);
				mp.addBodyPart(mbp1);
				mp.addBodyPart(mbp2);

				// add the Multipart to the message
				msg.setContent(mp);

				// set the Date: header
				msg.setSentDate(new Date());

				// send the message

				Transport tr = session.getTransport("smtp");
				tr.connect(ServerSMTP, SENDERMAIL, SENDERPWD);
				msg.saveChanges(); // don't forget this
				tr.sendMessage(msg, msg.getAllRecipients());
				tr.close();

				// cancello i file
				if (!docEvento.equals("")) {
					File f = new File(docEvento);
					if (f.exists())
						f.delete();
				}
				return PG_PAGINAHELPDESKOK;
			} catch (SendFailedException sfe) {
				throw new F3BException("SendMail Error!" + sfe);
			} catch (Exception sqe) {
				sqe.printStackTrace();
				throw new F3BException("SendMail Error! non posso inviare la mail  : " + sqe);
			}
		} else
			return PG_PAGINAHELPDESKNOTACTIVE;
	}

}