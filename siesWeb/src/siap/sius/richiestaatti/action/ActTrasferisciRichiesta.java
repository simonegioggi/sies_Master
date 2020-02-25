package siap.sius.richiestaatti.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.jms.ICostantiJMS;
import siap.jms.SIAPSender;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.jms.controller.ITrasmissioneJMS;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActTrasferisciRichiesta
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
public class ActTrasferisciRichiesta extends ActionSiap implements ICostantiRichiestaAtti, ICostantiJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		boolean inviato = false; // flag di controllo invio messaggio.

		// Si Prepara la trasmissione della Richiesta.
		// Il destinatario è un UEPE o un UEPESS.
		String lTipoUff = getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
		String lSedeUff = getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO);
		// String lTipoUfficio = getUfficioUtenteConnesso().getCodUfficio();

		// Fascicolo SIUS in sessione.
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Dati BDI mittente.
		UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("BDI MIttente = " + lBDIMittente);
		String lCodiceUfficio = new String();
		UfficioModel lLocal = new UfficioModel();
		UfficioModel lBDI = new UfficioModel();

		if (lTipoUff.trim().compareTo("-") != 0) {
			lCodiceUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lSedeUff);
			lLocal = getUfficioByCodUfficio(lCodiceUfficio);
			lBDI = getUfficioByCodUfficio(lLocal.getCodDistretto());

			ITrasmissioneJMS lCtrlMess = SIUSLookupRemote.getTrasmissioneJMS();
			MessaggioModel lMessage = lCtrlMess.getMessageForRichiesta(lEveId);

			lMessage.setDescrBdiDestinataria(lBDI.getDescrComune());
			lMessage.setCodBdiDestinataria(lBDI.getCodUfficio());
			lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
			lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
			lMessage.setCodUfficioDestinatario(lCodiceUfficio);
			lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
			lMessage.setCodTipoMessaggio(RICHIESTA);
			lMessage.setCodTipoOperazione(TRASFERIMENTO_RICHIESTA_RELAZIONE);
			lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
			lMessage.setDataInvio(DateUtils.getSysDate());

			// SETTA RIFERIMENTI FASCICOLO SIEP/SIUS
			lMessage.setChiaveAnnoSiep(lFasGPMod.getFascicoloSiusModel().getChiaveAnnoSIEP());
			lMessage.setChiaveProgrSiep(lFasGPMod.getFascicoloSiusModel().getChiaveProgrSIEP());
			lMessage.setChiaveAnnoSius(lFasGPMod.getFascicoloSiusModel().getChiaveAnno());
			lMessage.setChiaveProgrSius(lFasGPMod.getFascicoloSiusModel().getChiaveProgr());

			// UEPE
			if (lFasGPMod.getFascicoloSiusModel().getSoggetto() != null) {
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto().getNome() != null)
					lMessage.setNomeSoggetto(lFasGPMod.getFascicoloSiusModel().getSoggetto().getNome());
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto().getCognome() != null)
					lMessage.setCognomeSoggetto(lFasGPMod.getFascicoloSiusModel().getSoggetto().getCognome());
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto().getDataNascita() != null)
					lMessage.setDataNascita(lFasGPMod.getFascicoloSiusModel().getSoggetto().getDataNascita());
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto().getCodComuneNascita() != null)
					lMessage.setCodComuneNascita(
							lFasGPMod.getFascicoloSiusModel().getSoggetto().getCodComuneNascita());
				if (lFasGPMod.getFascicoloSiusModel().getSoggetto().getCodStatoNascita() != null)
					lMessage.setCodStatoNascita(
							lFasGPMod.getFascicoloSiusModel().getSoggetto().getCodStatoNascita());
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("MESSAGGIO DA SPEDIRE A " + lBDI.getDescrComune() + " : " + lMessage.toString());

			SIAPSender lSender = new SIAPSender();
			lSender.send(lMessage);
			inviato = true;
		}

		if (inviato == false)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione: selezionare almeno un destinatario! ");

		setRequestAttribute("IdEvento", lEveId.toString());

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Richiesta sottomessa al Sistema!");

		// Prepara la "pagina" di destinAction.
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setParameter("IdEvento", lEveId.toString()); // 04/06/2004
		if (this.isRequestParameterNullObj("codTipoOperazione")) {
			// setta la risposta nella request
			lRedirigi.setAction("siap.sius.richiestaatti.action.ActLoadTrasferisciRichiesta");
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Richiesta sottomessa al Sistema!");
		}

		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE;
	}

}