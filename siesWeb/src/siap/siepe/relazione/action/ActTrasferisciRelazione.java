package siap.siepe.relazione.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import siap.jms.ICostantiJMS;
import siap.jms.SIAPSender;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siepe.SIEPEException;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.jms.controller.ITrasmissioneJMS;
import siap.siepe.util.SIEPELookupRemote;

/**
 * <p>
 * Title: ActTrasferisciRelazione
 * </p>
 * <p>
 * Description: L'Azione impacchetta i dati da inviare nel messaggio, poi attiva l'invio del messaggio stesso
 * ai destinatari.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: bull
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActTrasferisciRelazione extends ActionSiap implements ICostantiJMS {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".ActTrasferisciRelazione: inizio");

		BigDecimal lIdRelazione = getRequestBigDecimalParameter(ICostantiRelazione.CAMPO_ID_RELAZIONE);

		// Lettura dei dati in sessione
		FascicoloSiepeEstesoModel lFascicoloEsteso = (FascicoloSiepeEstesoModel) getSessionAttribute(
				"FascicoloSiepeEsteso");

		// Determinazione dei dati del Destinatario
		String lCodUfficioDestinatario = getRequestStringParameter(ICostantiUfficio.CAMPO_COD_UFFICIO);
		UfficioModel lUfficioDestinatario = getUfficioByCodUfficio(lCodUfficioDestinatario);
		UfficioModel lBDIDestinatario = getUfficioByCodUfficio(lUfficioDestinatario.getCodDistretto());

		// STUB 04/11/2008 Determinazione dei dati dell'ulteriore Destinatario.
		String lCodTipoUfficioDestinatario2 = getRequestStringParameter(
				ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
		String lCodLuogoUfficioDestinatario2 = getRequestStringParameter(
				ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO);
		String lCodUfficioDestinatario2 = "";
		UfficioModel lUfficioDestinatario2 = null, lBDIDestinatario2 = null;
		if (lCodTipoUfficioDestinatario2.length() > 1 & lCodLuogoUfficioDestinatario2.length() > 0) {
			lCodUfficioDestinatario2 = this.getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO),
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO));
			lUfficioDestinatario2 = getUfficioByCodUfficio(lCodUfficioDestinatario2);
			lBDIDestinatario2 = getUfficioByCodUfficio(lUfficioDestinatario2.getCodDistretto());
		}

		// boolean inviato = false; // flag di controllo invio messaggio.

		// Dati BDI mittente.
		UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("BDI Mittente = " + lBDIMittente);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("BDI Destinatario = " + lBDIDestinatario);

		ITrasmissioneJMS lCtrlMess = SIEPELookupRemote.getTrasmissioneJMSRemote();

		MessaggioModel lMessage = lCtrlMess.getMessageForRelazione(lIdRelazione, lFascicoloEsteso,
				getUfficioUtenteConnesso());

		lMessage.setDescrBdiDestinataria(lBDIDestinatario.getDescrComune());
		lMessage.setCodBdiDestinataria(lBDIDestinatario.getCodUfficio());
		lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
		lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
		lMessage.setCodUfficioDestinatario(lCodUfficioDestinatario);
		lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());

		lMessage.setCodTipoMessaggio(RICHIESTA);

		// Esegue il parse del messaggio al fine di verificare se la relazione
		// è associata ad una Richiesta o Attività.
		ParserMessage lPars = null;
		lPars = new ParserMessage(lMessage.getTreeModel());
		// Si verificano le opportune condizioni e si imposta l'appropriato
		// tipo dell'operazione. In caso estremo di non individuazione dell'entità
		// master della relazione da trasmettere

		if (lPars.getRichiesta() != null)
			lMessage.setCodTipoOperazione(TRASFERIMENTO_RELAZIONE_RICHIESTA_UEPE);
		else if (lPars.getAttivita() != null)
			lMessage.setCodTipoOperazione(TRASFERIMENTO_RELAZIONE_ATTIVITA);
		else
			throw new SIEPEException(SIEPEException.USER_MESSAGE,
					"Errore nella trasmissione della relazione, essa non è associata ad una Richiesta o Attività!");

		/*
		 * // Questo non dovrebbe mai accadere, // pertanto, sarebbe meglio sollevare un // Errore di
		 * eccezione. lMessage.setCodTipoOperazione(TRASFERIMENTO_RELAZIONE_UEPE);
		 */

		lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
		lMessage.setDataInvio(DateUtils.getSysDate());

		// RIFERIMENTI FASCICOLO SIEPE.
		if (lFascicoloEsteso.getFascicoloSiepe() != null) {
			lMessage.setChiaveAnnoSiepe(lFascicoloEsteso.getFascicoloSiepe().getChiaveAnno());
			lMessage.setChiaveProgrSiepe(lFascicoloEsteso.getFascicoloSiepe().getChiaveProgr());
		}

		// RIFERIMENTI FASCICOLO SIUS.
		if (lFascicoloEsteso.getFascicoloSius() != null
				&& lFascicoloEsteso.getFascicoloSius().getFascicoloSiusModel() != null) {
			lMessage.setChiaveAnnoSius(
					lFascicoloEsteso.getFascicoloSius().getFascicoloSiusModel().getChiaveAnno());
			lMessage.setChiaveProgrSius(
					lFascicoloEsteso.getFascicoloSius().getFascicoloSiusModel().getChiaveProgr());
		}

		// RIFERIMENTI FASCICOLO SIEP.
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
		siesLogger.debug(
				"MESSAGGIO DA SPEDIRE A " + lBDIDestinatario.getDescrComune() + " : " + lMessage.toString());

		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessage);
		// inviato = true;

		// STUB 04/11/2008 Invio Relazione al secondo destinatario.
		if (lUfficioDestinatario2 != null) {
			lMessage.setDescrBdiDestinataria(lBDIDestinatario2.getDescrComune());
			lMessage.setCodBdiDestinataria(lBDIDestinatario2.getCodUfficio());
			lMessage.setCodUfficioDestinatario(lCodUfficioDestinatario2);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("MESSAGGIO DA SPEDIRE A " + lBDIDestinatario2.getDescrComune() + " : "
					+ lMessage.toString());
		}

		lSender.send(lMessage);
		// inviato = true;

		String lPage = this
				.ritornoDopoCancellazione("La trasmissione Relazione è stata sottoposta al sistema!", null);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".ActTrasferisciRelazione: fine");

		return lPage;
	}

}