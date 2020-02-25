package siap.siepe.attivita.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.SIAPSender;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.jms.controller.ITrasmissioneJMS;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: ActTrasferisciAttivita
 * </p>
 * <p>
 * Description: L'Azione impacchetta i dati da inviare nel messaggio, poi attiva l'invio del messaggio stesso
 * ai destinatari.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: bull
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActTrasferisciAttivita extends ActionSiap implements ICostantiJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".ActTrasferisciAttivita: inizio");

		BigDecimal lIdAttivita = getRequestBigDecimalParameter(ICostantiAttivita.CAMPO_ID_ATTIVITA);

		// Lettura dei dati in sessione
		FascicoloSiepeEstesoModel lFascicoloEsteso = (FascicoloSiepeEstesoModel) getSessionAttribute("FascicoloSiepeEsteso");

		// Determinazione dei dati del Destinatario
		String lCodUfficioDestinatario = getRequestStringParameter(ICostantiUfficio.CAMPO_COD_UFFICIO);
		UfficioModel lUfficioDestinatario = getUfficioByCodUfficio(lCodUfficioDestinatario);
		UfficioModel lBDIDestinatario = getUfficioByCodUfficio(lUfficioDestinatario.getCodDistretto());

//		boolean inviato = false; // flag di controllo invio messaggio.

		// Dati BDI mittente.
		UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("BDI MIttente = " + lBDIMittente);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("BDI Destinatario = " + lBDIDestinatario);

		ITrasmissioneJMS lCtrlMess = SIEPELookupRemote.getTrasmissioneJMSRemote();
		MessaggioModel lMessage = lCtrlMess.getMessageForAttivita(lIdAttivita, lFascicoloEsteso,
				getUfficioUtenteConnesso());

		lMessage.setDescrBdiDestinataria(lBDIDestinatario.getDescrComune());
		lMessage.setCodBdiDestinataria(lBDIDestinatario.getCodUfficio());
		lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
		lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
		lMessage.setCodUfficioDestinatario(lCodUfficioDestinatario);
		lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());

		lMessage.setCodTipoMessaggio(RICHIESTA);
		lMessage.setCodTipoOperazione(TRASFERIMENTO_ATTIVITA);

		lMessage.setCodiceUtenteMittente(getCodUtenteConnesso());
		lMessage.setDataInvio(DateUtils.getSysDate());

		// RIFERIMENTI FASCICOLO SIEPE
		if (lFascicoloEsteso.getFascicoloSiepe() != null) {
			lMessage.setChiaveAnnoSiepe(lFascicoloEsteso.getFascicoloSiepe().getChiaveAnno());
			lMessage.setChiaveProgrSiepe(lFascicoloEsteso.getFascicoloSiepe().getChiaveProgr());
		}

		// RIFERIMENTI FASCICOLO SIUS
		if (lFascicoloEsteso.getFascicoloSius() != null
				&& lFascicoloEsteso.getFascicoloSius().getFascicoloSiusModel() != null) {
			lMessage.setChiaveAnnoSius(lFascicoloEsteso.getFascicoloSius().getFascicoloSiusModel()
					.getChiaveAnno());
			lMessage.setChiaveProgrSius(lFascicoloEsteso.getFascicoloSius().getFascicoloSiusModel()
					.getChiaveProgr());
		}
		// RIFERIMENTI FASCICOLO SIEP
		if (lFascicoloEsteso.getFascicoloSiep() != null) {
			lMessage.setChiaveAnnoSiep(lFascicoloEsteso.getFascicoloSiep().getChiaveAnno());
			lMessage.setChiaveProgrSiep(lFascicoloEsteso.getFascicoloSiep().getChiaveProgr());
		}

		// Riferimenti Soggetto
		if (lFascicoloEsteso.getSoggetto() != null) {
			lMessage.setNomeSoggetto(lFascicoloEsteso.getSoggetto().getNome());
			lMessage.setCognomeSoggetto(lFascicoloEsteso.getSoggetto().getCognome());
			lMessage.setDataNascita(lFascicoloEsteso.getSoggetto().getDataNascita());
			lMessage.setCodComuneNascita(lFascicoloEsteso.getSoggetto().getCodComuneNascita());
			lMessage.setCodStatoNascita(lFascicoloEsteso.getSoggetto().getCodStatoNascita());
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("MESSAGGIO DA SPEDIRE A " + lBDIDestinatario.getDescrComune() + " : "
				+ lMessage.toString());

		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessage);
//		inviato = true;

		String lPage = ritornoDopoCancellazione("La trasmissione Attività è stata sottoposta al sistema!",
				null);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".ActTrasferisciAttivita: fine");

		return lPage;
	}

}