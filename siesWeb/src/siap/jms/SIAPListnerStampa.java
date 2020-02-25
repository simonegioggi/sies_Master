package siap.jms;

import java.math.BigDecimal;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import siap.jms.connection.ConnectionPoolJMS;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoria.controller.IIstruttoria;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;

/**
 * <p>
 * Title: SIAPListnerSender
 * </p>
 * <p>
 * Description: Classe che realizza il Listner in ascolto per i messaggi in arrivo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class SIAPListnerStampa implements MessageListener, ICostantiJMS {

	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);

	private static String idMessage = "";
	private static BigDecimal idStampa = null;
	QueueReceiver mReceiver;
	private ConnectionPoolJMS mPoolConnection;

	protected SIAPListnerStampa() {
		super();
	}

	/**
	 * Metodo statico per l'unico punto di accesso al listner
	 * 
	 * @return
	 * @throws JMSException
	 * @throws NamingException
	 * @throws Exception
	 */
	public synchronized static SIAPListnerStampa newSIAPListnerStampa()
			throws JMSException, NamingException, Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");
		SIAPListnerStampa listner = new SIAPListnerStampa();
		listner.initialize();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
		return listner;
	}

	/**
	 * initialize
	 * 
	 * @throws NamingException
	 * @throws JMSException
	 * @throws Exception
	 */
	/*
	 * protected void initialize() throws NamingException, JMSException,Exception { // [FT] - 03/08/2016 -
	 * MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 * siesLogger.info("[JMS]: inizio");
	 * 
	 * try{ mPoolConnection = ConnectionPoolJMS.getInstance();
	 * 
	 * QueueConnection lConnection = mPoolConnection.getConnection(); // [FT] - 03/08/2016 - MAC_LOG -
	 * Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 * siesLogger.info("[JMS]: HashConnessione "+lConnection.hashCode()); QueueSession qSession =
	 * lConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	 * 
	 * String lNameQueue = "Stampa";
	 * 
	 * Queue lQueue = qSession.createQueue(lNameQueue); QueueReceiver mReceiver =
	 * qSession.createReceiver(lQueue);
	 * 
	 * SIAPListnerStampa qListener = this; mReceiver.setMessageListener(qListener); // [FT] - 03/08/2016 -
	 * MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 * siesLogger.info("[JMS]: Partito il Listner "+qListener.hashCode()+" sulla Coda " + lNameQueue);
	 * 
	 * lConnection.start(); } catch(Exception ex) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
	 * istanza siesLogger al posto di LogF3B.getLogger()
	 * siesLogger.error("[JMS]: Errore in SIAPListnerStampa.inizialize" + ex.getMessage(),ex);
	 * ex.printStackTrace(); }
	 * 
	 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() siesLogger.info("[JMS]: fine"); }
	 */
	protected void initialize() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");
		mPoolConnection = ConnectionPoolJMS.getInstance();

		QueueConnection lConnection = mPoolConnection.getConnection();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: HashConnessione " + lConnection.hashCode());

		try {
			QueueSession qSession = lConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			// String lNameQueue = JMSProperties.getInstance().getProperty(QUEUE_IN_ARRIVO);

			String lNameQueue = "Stampa";
			Queue lQueue = qSession.createQueue(lNameQueue);
			QueueReceiver mReceiver = qSession.createReceiver(lQueue);

			SIAPListnerStampa qListener = this;
			mReceiver.setMessageListener(qListener);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(
					"[JMS]: Partito il Listner " + qListener.hashCode() + " " + "sulla Coda " + lNameQueue);

			lConnection.start();
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: Errore in SIAPListnerStampa.inizialize" + ex.getMessage(), ex);
			// ex.printStackTrace();

			if (ex instanceof javax.jms.IllegalStateException) {
				ConnectionPoolJMS.restart();
				//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				//// LogF3B.getLogger()
				// siesLogger.error("[JMS]: Errore in SIAPListnerStampa.inizialize" + ex.getMessage(),ex);
				this.initialize();
			}

		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
	}

	/**
	 * Metodo che viene attivato alla ricezione di un comando di Stampa
	 * 
	 * @param aMessage
	 * @throws RuntimeException
	 */
	public void onMessage(Message aMessage) throws RuntimeException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");
		try {
			// la coda di stampa tratta text Message dato che deve solo interpretare i comnadi.

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: Letto Comando di STAMPA " + aMessage.getJMSType() + " ID = "
					+ aMessage.getJMSMessageID());

			ObjectMessage lMessage = (ObjectMessage) aMessage;

			// Modifica Anti-loop sui messaggi di richiesta ricerca in Arrivo
			if (!idMessage.equals(lMessage.getJMSMessageID())) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"[JMS]: \n\nNO LOOP " + idMessage + " != " + lMessage.getJMSMessageID() + " \n\n");
				idMessage = lMessage.getJMSMessageID();
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("[JMS]: \n\nLOOP\n\n");
				return;
			}

			/*
			 * lMessage.getStringProperty("TIPO_OPERAZIONE", "StampaInizioEsecuzioneMultiple");
			 * lMessage.getStringProperty("UTENTE", aUtente.getUserId());
			 * lMessage.getStringProperty("PROGR_INIZIALE",""+ aFasc.getChiaveProgrIniziale());
			 * lMessage.getStringProperty("PROGR_FINALE", ""+aFasc.getChiaveProgrFinale());
			 * lMessage.getStringProperty("ANNO_INIZIALE", ""+aFasc.getChiaveAnnoIniziale());
			 * lMessage.getStringProperty("ANNO_FINALE", ""+aFasc.getChiaveAnnoFinale());
			 * lMessage.getStringProperty("COD_UFFICIO", aUfficio.getCodUfficio());
			 * lMessage.getStringProperty("COMUNE_UFFICIO", aUfficio.getCodComune());
			 */

			FascicoloSiepModel lFasMod = new FascicoloSiepModel();

			lFasMod.setChiaveAnnoIniziale(new BigDecimal(lMessage.getStringProperty("ANNO_INIZIALE")));
			lFasMod.setChiaveProgrIniziale(new BigDecimal(lMessage.getStringProperty("PROGR_INIZIALE")));
			lFasMod.setChiaveAnnoFinale(new BigDecimal(lMessage.getStringProperty("ANNO_FINALE")));
			lFasMod.setChiaveProgrFinale(new BigDecimal(lMessage.getStringProperty("PROGR_FINALE")));

			UtenteModel lUtenteMod = (UtenteModel) lMessage.getObject();

			/* lUtenteMod.setUserId(lMessage.getStringProperty("UTENTE")); */

			lFasMod.setChiaveUfficio(lMessage.getStringProperty("COD_UFFICIO"));
			lFasMod.setDescrTipoUfficio(lMessage.getStringProperty("TIPO_UFFICIO"));
			lFasMod.setDescrComuneUfficio(lMessage.getStringProperty("COMUNE_UFFICIO"));

			UfficioModel lUfficio = new UfficioModel();

			lUfficio = lUtenteMod.getUfficioUtente();
			/*
			 * lUfficio.setCodUfficio(lMessage.getStringProperty("COD_UFFICIO"));
			 * lUfficio.setCodComune(lMessage.getStringProperty("COMUNE_UFFICIO"));
			 */

			BigDecimal lSequence = new BigDecimal(lMessage.getStringProperty("ID_STAMPA"));

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: SEQUENCE Now = " + lSequence);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: SEQUENCE Id Vecchio = " + idStampa);

			if (lSequence == idStampa) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("[JMS]: LOOP sulla SEQUENCE!" + idStampa);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("[JMS]: LOOP sulla SEQUENCE!");
				return;
			}

			idStampa = lSequence;

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: Sequence di Stampa = " + lSequence);

			try {
				/* Metodo che stampa */
				IIstruttoria lCtrl = SIEPLookupRemote.getIstruttoriaRemote();
				/* boolean lReport = */lCtrl.ExStampaInizioEsecuzioneMultiple(lFasMod, lUtenteMod, lUfficio,
						lSequence);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("[JMS]: Chiamato il metodo ExStampaInizioEsecuzioneMultiple");

			} catch (Exception ex) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("[JMS]: \n\n Eccezione nella elaborazione della Stampa: " + ex.getMessage());
				ex.printStackTrace();
			}

			// TAGGED - In riposo per 3 minuti
			Thread.sleep(180000);
		} catch (JMSException eJms) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: \n\n Eccezione nella elaborazione della Stampa: " + eJms.getMessage());
			eJms.printStackTrace();
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("[JMS]: \n\n Eccezione nella elaborazione della Stampa: " + ex.getMessage());
			ex.printStackTrace();
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
	}

}