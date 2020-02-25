package siap.sius.richiestaatti.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.richiestaatti.controller.IRichiestaAtti;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActInserisciSollecito extends ActInserisciRicAtti implements ICostantiRichiestaAtti {

	/**
	 * Azione di Stampa del Sollecito
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
//		String lCodComune = getCodComuneUtenteConnesso();
		DocumentoAllegatoModel lDocAMod = null;
		DocumentoAllegatoModel lDocOutMod = null;
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// Dati ufficio connesso
		UfficioModel lUfficio = getUfficioUtenteConnesso();

		// Model Generale Procedimento
//		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
//		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Model Evento Notifica
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		// Chiama il controller di Evento.
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrlEve.ExRicercaEventoNotificaByKey(lIdEvento);

		// Setto il Model DocumentoAllegato
		lDocAMod = new DocumentoAllegatoModel();
		lDocAMod.setEveIdEvento(lIdEvento);
		lDocAMod.setDataEmissione(DateUtils.getSysDate());
		lDocAMod.setCodTipoDocumento("05"); // Codifica di COD_TIPO_DOCUMENTO_ALLEGATO = Atto Ric. Istruttoria
		lDocAMod.setFlagDocumentoRegistrato("N");
		lDocAMod.setCodUfficioInserimento(lCodiceUfficio);
		lDocAMod.setCodOperatoreInserimento(lCodiceOperatore);
		lDocAMod.setDataInserimento(DateUtils.getSysDate());
		lDocAMod.setTemIdTemplate("SIUS_IS_021");

		// Si Recupera l'utente dalla sessione.
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

		// Chiama il controller di richiestaatti.
		IRichiestaAtti lCtrlAtti = SIUSLookupRemote.getRichiestaAttiRemote();
		lDocOutMod = lCtrlAtti.ExInserisciSollecito(lEveMod, lDocAMod, lUfficio, lUtenteMod);

		/*
		 * 
		 * 
		 * String lId = getRequestStringParameter( ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO);
		 * 
		 * DocumentoAllegatoModel lDAMod = new DocumentoAllegatoModel(); // chiama il controller di Documento
		 * Allegato. IDocumentoAllegato lCtrlDA = SIUSLookupRemote.getDocumentoAllegatoRemote(); lDAMod =
		 * lCtrlDA.ExRicercaDocumentoAllegatoByKey(new BigDecimal(lId));
		 * 
		 * //lEveMod.getEvento().setIdEvento(new BigDecimal(lId)); lDAMod.setIdDocumentoAllegato(new
		 * BigDecimal(lId));
		 * 
		 * UfficioModel lUff = new UfficioModel(this.getUfficioUtenteConnesso());
		 * 
		 * lDAMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		 * lDAMod.setCodUfficioAggiornamento(lUff.getCodUfficio());
		 * lDAMod.setDataAggiornamento(DateUtils.getSysDate()); lDAMod.setFlagDocumentoRegistrato("N");
		 * 
		 * lDAMod.setDescrUfficioAggiornamento(lUff.getDescrTipoUfficio());
		 * 
		 * IDepositoDecreto lCtrl = SIUSLookupRemote.getDepositoDecretoRemote(); ByteArrayOutputStream lReport
		 * = lCtrl.ExStampaDocumentoAllegato(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(),lDAMod,
		 * lUff.getCodUfficio());
		 * 
		 * setRequestAttribute("documentoAllegato", lDAMod); setRequestAttribute("report", lReport);
		 * 
		 * 
		 * return IWebConstants.PG_DOWNLOAD;
		 */
		IDocumentoAllegato lCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
		ByteArrayOutputStream lReport = lCtrl.ExGetDocumentoByKey(lDocOutMod.getIdDocumentoAllegato());

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}