package siap.siep.richiesta.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.richiesta.controller.IRichiesta;
import siap.siep.util.SIEPLookupRemote;

public class ActUploadOrdineScarcerazioneProvv extends ActionSiap implements ICostantiEvento {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		EventoModel lModel = new EventoModel();
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		lModel = lCtrlEvento
				.ExRicercaEventoByKey(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		lModel.setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		// ========================================================================
		// Recupero l'evento al quale è agganciata l'annotazione manuale
		// ========================================================================
		EventoModel lEveModRic = null;
		if (lModel != null) {
			IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
			AnnotazioneManualeModel lAnnManMod = lCtrlAnn
					.ExRicercaAnnotazioneManualeByKey(lModel.getAnnIdAnnotazioneManuale());

			if (lAnnManMod != null) {
				lEveModRic = lCtrlEvento.ExRicercaEventoByKey(lAnnManMod.getEveIdEvento());

				if (lEveModRic != null && lEveModRic.getAnnIdAnnotazioneManuale() != null) {
					lEveModRic = null;
				}

				if (lEveModRic != null) {
					lEveModRic.setDataAggiornamento(DateUtils.getSysDate());
					lEveModRic.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					lEveModRic.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				}
			}
		}

		/*
		 * EventoModel lEveModRic = new EventoModel(); Vector evento= null;
		 * lEveModRic.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 * lEveModRic.setCodTipoProvvedimento("04"); lEveModRic.setCodTipoEvento("01");
		 * lEveModRic.setCodMotivo("0122"); try { // STUB 12-10-2005 REWORK STATO ESECUZIONE. /* String
		 * lFalgDocReg = null; evento = lCtrlEvento.ExRicercaEventoTipoEveTipoProvMot(lEveModRic,lFalgDocReg);
		 * if (evento.size() > 0) lEveModRic = (EventoModel) evento.get(0); /
		 * 
		 * String[] motivi ={"0122"}; String[] provv = {"04","26"}; lEveModRic =
		 * lCtrlEvento.ExRicercaEventoPerMotivoPerProvv(motivi,provv,lEveModRic);
		 * 
		 * lEveModRic.setDataAggiornamento(DateUtils.getSysDate());
		 * lEveModRic.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		 * lEveModRic.setCodOperatoreAggiornamento(getCodUtenteConnesso()); } catch (Exception e) { // [FT] -
		 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.info("eccezione" + e); }
		 */
		InputStream lInput = getFile(ICostantiEvento.CAMPO_BLOB);

		if (lInput != null) {
			byte[] lBuffer = new byte[lInput.available()];

			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			lModel.setDocBlobIn(lSt);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("BYTE ARRAY >>> " + lSt.toString());
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("BYTE ARRAY <<<" + lInput);

		lModel.setDataAggiornamento(DateUtils.getSysDate());
		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		// String codice = lModel.getCodMotivo();
		String lCodiceNomProvv = "NP077";

		IRichiesta lCtrlRich = SIEPLookupRemote.getRichiestaRemote();
		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.info(" REQ >>> " + this.getRequest().getParameter( CAMPO_VALIDA));

		// ==========================================================================
		// Recupero le LA da passare alla funzione di validazione in caso di
		// Interruzione della pena per Scarcerazione Provvisoria Indulto
		// ==========================================================================
		BigDecimal lGiorniLA = null;
		ActCalcoloPenaMain lActCalcoloPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcPenaModel = lActCalcoloPenaMain.calcoloPena(lFascMod.getIdFascicoloSiep(),
				null);
		lGiorniLA = new BigDecimal(lCalcPenaModel.getLiberazioneAnticipata());

		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA)) {
			lModel.setFlagDocumentoRegistrato("S");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("per aggiornare il flag@@@@@@@@@@" + lModel);

			lCtrlRich.ExUpdateValidaRichiesteConCodice(lEveModRic, lModel, lCodiceNomProvv, lFascMod,
					lGiorniLA);
		} else {
			lModel.setFlagDocumentoRegistrato("N");

			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lCtrl.ExUpdateDocument(lModel);
		}

		// Prepara la "pagina" di destinAction
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO)) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction(getRequestStringParameter(ICostantiEvento.CAMPO_AZIONE_DETTAGLIO) + "&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "="
					+ getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		}

		return IWebConstants.PG_MESSAGE;
	}
}