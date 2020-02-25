package siap.sico.evento.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.SIAPSender;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.controller.ITrasmissioneJMS;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActTrasferisciDocumento
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
public class ActTrasferisciDocumento extends ActionSiap implements ICostantiEvento, ICostantiJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		FascicoloSiepModel lFascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		boolean inviato = false; // flag di controllo invio messaggio.

		// Si Prepara la trasmissione del Documento ai 2 destinatari.
		String[] lTipoUff = getRequestStringParameters(ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
		String[] lSedeUff = getRequestStringParameters(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO);

		// Dati BDI mittente.
		UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("BDI MIttente = " + lBDIMittente);

		String lCodiceUfficio = new String();
		UfficioModel lLocal = new UfficioModel();
		UfficioModel lBDI = new UfficioModel();

		ITrasmissioneJMS lCtrlMess = SIEPLookupRemote.getTrasmissioneJMS();
		MessaggioModel lMessage = lCtrlMess.getMessageForProvvedimento(lEveId,
				lFascicolo.getIdFascicoloSiep());

		if (lTipoUff[0].trim().compareTo("-") != 0) {
			lCodiceUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(lTipoUff[0], lSedeUff[0]);
			lLocal = getUfficioByCodUfficio(lCodiceUfficio);
			lBDI = getUfficioByCodUfficio(lLocal.getCodDistretto());

			lMessage.setDescrBdiDestinataria(lBDI.getDescrComune());
			lMessage.setCodBdiDestinataria(lBDI.getCodUfficio());
			lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
			lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
			lMessage.setCodUfficioDestinatario(lCodiceUfficio);
			lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
			lMessage.setCodTipoMessaggio(RICHIESTA);
			lMessage.setCodTipoOperazione(TRASFERIMENTO_PROVVEDIMENTO);
			lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
			lMessage.setDataInvio(DateUtils.getSysDate());

			// SETTA RIFERIMENTI FASCICOLO SIEP/SIUS
			lMessage.setChiaveAnnoSiep(lFascicolo.getChiaveAnno());
			lMessage.setChiaveProgrSiep(lFascicolo.getChiaveProgr());

			if (lFascicolo.getSoggetto() != null) {
				if (lFascicolo.getSoggetto().getNome() != null)
					lMessage.setNomeSoggetto(lFascicolo.getSoggetto().getNome());
				if (lFascicolo.getSoggetto().getCognome() != null)
					lMessage.setCognomeSoggetto(lFascicolo.getSoggetto().getCognome());
				if (lFascicolo.getSoggetto().getDataNascita() != null)
					lMessage.setDataNascita(lFascicolo.getSoggetto().getDataNascita());
				if (lFascicolo.getSoggetto().getCodComuneNascita() != null)
					lMessage.setCodComuneNascita(lFascicolo.getSoggetto().getCodComuneNascita());
				if (lFascicolo.getSoggetto().getCodStatoNascita() != null)
					lMessage.setCodStatoNascita(lFascicolo.getSoggetto().getCodStatoNascita());
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("MESSAGGIO DA SPEDIRE A " + lBDI.getDescrComune());

			SIAPSender lSender = new SIAPSender();
			lSender.send(lMessage);
			inviato = true;
		}

		// Ulteriore Destinazione UEPE.
		if (lTipoUff[1].trim().compareTo("-") != 0) {
			lCodiceUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(lTipoUff[1], lSedeUff[1]);
			lLocal = getUfficioByCodUfficio(lCodiceUfficio);
			lBDI = getUfficioByCodUfficio(lLocal.getCodDistretto());

			lMessage.setDescrBdiDestinataria(lBDI.getDescrComune());
			lMessage.setCodBdiDestinataria(lBDI.getCodUfficio());
			lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
			lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
			lMessage.setCodUfficioDestinatario(lCodiceUfficio);
			lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
			lMessage.setCodTipoMessaggio(RICHIESTA);
			lMessage.setCodTipoOperazione(TRASFERIMENTO_PROVVEDIMENTO);
			lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
			lMessage.setDataInvio(DateUtils.getSysDate());

			// SETTA RIFERIMENTI FASCICOLO SIEP/SIUS
			lMessage.setChiaveAnnoSiep(lFascicolo.getChiaveAnno());
			lMessage.setChiaveProgrSiep(lFascicolo.getChiaveProgr());

			// UEPE
			if (lFascicolo.getSoggetto() != null) {
				if (lFascicolo.getSoggetto().getNome() != null)
					lMessage.setNomeSoggetto(lFascicolo.getSoggetto().getNome());
				if (lFascicolo.getSoggetto().getCognome() != null)
					lMessage.setCognomeSoggetto(lFascicolo.getSoggetto().getCognome());
				if (lFascicolo.getSoggetto().getDataNascita() != null)
					lMessage.setDataNascita(lFascicolo.getSoggetto().getDataNascita());
				if (lFascicolo.getSoggetto().getCodComuneNascita() != null)
					lMessage.setCodComuneNascita(lFascicolo.getSoggetto().getCodComuneNascita());
				if (lFascicolo.getSoggetto().getCodStatoNascita() != null)
					lMessage.setCodStatoNascita(lFascicolo.getSoggetto().getCodStatoNascita());
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
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Documento sottomessa al Sistema!");

		// Prepara la "pagina" di destinAction.
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setParameter("IdEvento", lEveId.toString());
		if (this.isRequestParameterNullObj("codTipoOperazione")) {
			// setta la risposta nella request
			lRedirigi.setAction("siap.sico.evento.action.ActLoadTrasferisciDocumento");
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Documento sottomessa al Sistema!");
		}
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE;
	}

}