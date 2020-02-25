package siap.siep.jms.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPSender;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.jms.controller.IPresaInCarico;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import f3b.controller.GenericTask;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BProperties;

public class PresaInCaricoJMSTask extends GenericTask implements ICostantiJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@Override
	public void run() {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Call Test --- > at : " + new Date());
			Vector<MessaggioModel> lVect = ricercaMessaggi();

			if (lVect != null) {
				Iterator<MessaggioModel> litx = lVect.iterator();
				while (litx.hasNext()) {
					MessaggioModel lMsg = (MessaggioModel) litx.next();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("<--- MessaggioModel Inizio Presa in Carico --->");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("MessaggioModel Id     				 : " + lMsg.getIdMessaggio());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("MessaggioModel CodBdidest     : " + lMsg.getCodBdiDestinataria());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("MessaggioModel JmsIdMesg      : " + lMsg.getJmsIdMessaggio());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("MessaggioModel CodTipoOper    : " + lMsg.getCodTipoOperazione());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("MessaggioModel CodUtenteMitt  : " + lMsg.getCodiceUtenteMittente());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("MessaggioModel CodUfficioDest : " + lMsg.getCodUfficioDestinatario());

					presaInCaricoOrdinanza(lMsg.getIdMessaggio());

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("<--- MessaggioModel Fine Presa in Carico --->");
				}
			}

		} catch (Exception ex) {
			// /// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// /siesLogger.error("[JMS] : Errore su Connessione al JMS : ", ex);

			// n.b. verificare se è il caso di rilanciare l'eccezione in caso di errore
			// JMS oppure se è più opportuno visualizzare un messaggio di warning
			// ma consentire comunque all'utente di visualizzare i messaggi già
			// ricevuti.
			// throw ex;
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");

	}

	/**
	 * Per il momento tratta solo le ordinanzae, rifare metodo con parametro per ordinanza e decreto.
	 * <p>
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Vector<MessaggioModel> ricercaMessaggi() throws Exception {

		MessaggioModel lMessaggio = new MessaggioModel();
		// lMessaggio.setCodUfficioDestinatario(getCodUfficioUtenteConnesso());
		lMessaggio.setCodTipoMessaggio("01");
		// lMessaggio.setCodTipoOperazione(getRequestStringParameter(ICostantiMessaggio.CAMPO_TIPO_OPERAZIONE));
		lMessaggio.setCodTipoOperazione("00003"); // Ordinanza

		// Ricerca Messaggi
		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		Vector<MessaggioModel> lVect = lCrtl.ExRicercaMessaggio(lMessaggio);

		return lVect;
	}

	/**
	 * 
	 * @param lIdMess
	 * @throws Exception
	 */
//	private void presaInCaricoDecreto(BigDecimal lIdMess) throws Exception {
//
//		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
//		MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);
//
//		// UfficioModel lBDI = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());
//
//		String lEsito = "00000"; // Setto l'esito positivo
//
//		MessaggioModel lMessIns = new MessaggioModel(lMess);
//
//		MisuraAlternativaModel lMisAlt = new MisuraAlternativaModel();
//		// lMisAlt.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
//		// lMisAlt.setCodOperatoreAggiornamento(getCodUtenteConnesso());
//		// lMisAlt.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
//		// lMisAlt.setDataAggiornamento(DateUtils.getSysDate());
//
//		IPresaInCarico lPres = SICOLookupRemote.getPresaInCaricoRemote();
//		MessaggioModel lMessReturn = lPres.ExPresaInCaricoDecreto(lMessIns, lMisAlt);
//
//		MessaggioModel lMessage = new MessaggioModel();
//
//		lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
//		lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
//		lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());
//		lMessage.setCodBdiMittente(F3BProperties.getProperty("Bdi.DistrictCode"));
//		lMessage.setCodUfficioMittente(lMess.getCodUfficioDestinatario());
//		// lMessage.setCodiceUtenteMittente(getCodUtenteConnesso());
//		lMessage.setCodTipoMessaggio(ESITO);
//		lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_DECRETO);
//		lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());
//		lMessage.setDataInvio(DateUtils.getSysDate());
//		lMessage.setDataEsito(DateUtils.getSysDate());
//		lMessage.setCodEsito(lEsito);
//		lMessage.setTreeModel(lMess.getTreeModel());
//
//		SIAPSender lSender = new SIAPSender();
//		lSender.send(lMessage);
//
//		lMess.setFlagVisto("S");
//		lMess.setDataEsito(DateUtils.getSysDate());
//		lCrtl.ExModificaMessaggio(lMess);
//
//		F3BProperties.getProperty("Bdi.DistrictCode");
//
//	}

	/**
   * 
   * 
   */
	private void presaInCaricoOrdinanza(BigDecimal lIdMess) throws Exception {

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);

		// UfficioModel lBDI = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso());

		String lEsito = "00000"; // Setto l'esito positivo

		MessaggioModel lMessIns = new MessaggioModel(lMess);

		MisuraAlternativaModel lMisAlt = new MisuraAlternativaModel();
		// lMisAlt.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
		// lMisAlt.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		// lMisAlt.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		// lMisAlt.setDataAggiornamento(DateUtils.getSysDate());

		// 13/12/2007 Cambiato il riferimento da SIUS.PresaInCaricoJMSController a
		// SICO.PresaInCaricoController.
		// IPresaInCaricoJMS lPres = SIUSLookupRemote.getPresaInCarico();

		IPresaInCarico lPres = SICOLookupRemote.getPresaInCaricoRemote();
		/*MessaggioModel lMessReturn = */lPres.ExPresaInCaricoOrdinanza(lMessIns, lMisAlt);

		MessaggioModel lMessage = new MessaggioModel();

		lMessage.setDescrBdiDestinataria(lMess.getDescrBdiMittente());
		lMessage.setCodBdiDestinataria(lMess.getCodBdiMittente());
		lMessage.setCodUfficioDestinatario(lMess.getCodUfficioMittente());
		lMessage.setCodBdiMittente(F3BProperties.getProperty("Bdi.DistrictCode"));
		lMessage.setCodUfficioMittente(lMess.getCodUfficioDestinatario());
		// lMessage.setCodiceUtenteMittente(getCodUtenteConnesso());
		lMessage.setCodTipoMessaggio(ESITO);
		lMessage.setCodTipoOperazione(ESITO_TRASFERIMENTO_ORDINANZA);
		lMessage.setJmsCorrelationIdMessage(lMess.getJmsCorrelationIdMessage());
		lMessage.setDataInvio(DateUtils.getSysDate());
		lMessage.setDataEsito(DateUtils.getSysDate());
		lMessage.setCodEsito(lEsito);
		lMessage.setTreeModel(lMess.getTreeModel());

		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessage);

		lMess.setFlagVisto("S");
		lMess.setDataEsito(DateUtils.getSysDate());
		lCrtl.ExModificaMessaggio(lMess);
	}

}