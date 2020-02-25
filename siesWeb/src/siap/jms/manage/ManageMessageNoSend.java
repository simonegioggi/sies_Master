package siap.jms.manage;

import java.math.BigDecimal;

import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPSender;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ManageMessageNoSend implements ICostantiJMS {

	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);

	public ManageMessageNoSend() {
	}

	/**
	 * Elabora i messaggi di richiesta ricerca non Spediti
	 * 
	 * @param aMessage
	 * @throws F3BException
	 */
	public void elaboraMessaggioNonSpedito(ObjectMessage aMessage) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		try {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ObjectMessage : " + aMessage.toString());

			MessaggioModel lMessage = new MessaggioModel();

			lMessage.setDescrBdiDestinataria(aMessage.getStringProperty(BDI_MITTENTE));
			lMessage.setCodBdiDestinataria(aMessage.getStringProperty(COD_BDI_MITTENTE));
			lMessage.setCodBdiMittente(aMessage.getStringProperty(COD_BDI_DESTINATARIA));
			lMessage.setDescrBdiMittente(aMessage.getStringProperty(BDI_DESTINATARIA));
			lMessage.setCodTipoOperazione(ESITO_RICERCA_SOGGETTO);
			lMessage.setCodTipoMessaggio(ESITO_RICERCA);
			lMessage.setCodEsito(DESTINAZIONE_NON_RAGGIUNGIBILE); // DEstinazione non raggiungibile
			// Messaggio Rispedito al Mittente
			lMessage.setCodUfficioDestinatario("-");
			lMessage.setCodUfficioMittente("-");
			lMessage.setCodiceUtenteMittente("OPENJMS");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(">>>new elaboraMessaggioNonSpedito() <<<<<<<<<");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Correlation ID = " + aMessage.getStringProperty(CORRELATION_ID_MESSAGGIO));
			lMessage.setJmsCorrelationIdMessage(aMessage.getStringProperty(CORRELATION_ID_MESSAGGIO));
			lMessage.setDataInvio(DateUtils.getSysDate());
			lMessage.setIdMessaggio(new BigDecimal(aMessage.getStringProperty(ID_MESSAGGIO)));

			lMessage.setFlagVisto("N");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lMessage : " + lMessage.toString());

			IMessaggio lCtrMess = JMSLookupRemote.getMessaggioRemote();

			if (lCtrMess.ExRicercaMessaggioUgualeNonSpedito(lMessage)) {
				/* MessaggioModel lInd = */lCtrMess.ExInserisciMessaggio(lMessage);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("MESSAGGIO ESITO NON SPEDITO SCRITTO IN LOCALE  : " + lMessage.toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("\n\nERRORE DURANTE L'INSERIMENTO DEL MESSAGGIO NON SPEDITO\n\n", ex);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
	}

	/**
	 * Rispedisci il MEssaggio non Spedito
	 * 
	 * @param aMessage
	 * @throws Exception
	 */
	public void rispedisciMessaggioNonSpedito(MessaggioModel aRichiesta, MessaggioModel aEsito)	{

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		try {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("\n MessaggioModel aEsito = " + aEsito);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("\n MessaggioModel aEsito.getJmsCorrelationIdMessage() = "
					+ aEsito.getJmsCorrelationIdMessage());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("\nRispedisci Messaggio Non Spedito = " + aRichiesta);

			aRichiesta.setCodBdiDestinataria(aEsito.getCodBdiMittente());
			aRichiesta.setDescrBdiDestinataria(aEsito.getDescrBdiMittente());
			aRichiesta.setJmsCorrelationIdMessage(aEsito.getJmsCorrelationIdMessage());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("\nRispedisci Messaggio Non Spedito dopo correlation id = " + aRichiesta);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("\n MessaggioModel aRichiesta.getJmsCorrelationIdMessage() = "
					+ aRichiesta.getJmsCorrelationIdMessage());

			SIAPSender lSender = new SIAPSender();
			lSender.sendToTrueDestination(aRichiesta);

			// Spedizione andata a buon fine, si cancella il messaggio non spedito.
			IMessaggio lCtrMess = JMSLookupRemote.getMessaggioRemote();
			lCtrMess.ExCancellaMessaggio(aEsito.getIdMessaggio());
		} catch (Exception ex) {
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("\n\nERRORE DURANTE IL REINVIO DEL MESSAGGIO NON SPEDITO!!!\n\n", ex);
			return; // 2010-11-02 - introdotto il "return" per interrompere su errore l'esecuzione della
					// funzione.
					// Tale modifica, per il momento è temporanea, poichè propagare l'errore di eccezione può
					// comportare la rivisitazione dell'intera funzione con i moduli di reference.
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
	}

}