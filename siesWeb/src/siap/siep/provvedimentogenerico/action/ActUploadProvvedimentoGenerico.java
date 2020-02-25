package siap.siep.provvedimentogenerico.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.controller.ITenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActUploadProvvedimentoGenerico
 * </p>
 * <p>
 * Description: Validazione del Provvedimento Generico
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActUploadProvvedimentoGenerico extends ActProvvedimentoGenerico {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		EventoModel lModel = new EventoModel();
		lModel.setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		lModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		// Evento
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrl.ExRicercaEventoByKey(lModel.getIdEvento());

		// Decreto o Ordinanza dipende dal tipo provvedimento
		ITenore lCtrlTen = SIUSLookupRemote.getTenoreRemote();
		Vector lVect = null;

		if (lEveMod != null && "02".equals(lEveMod.getCodTipoProvvedimento())) {
			IDepositoDecreto lCtrlDepDec = SIUSLookupRemote.getDepositoDecretoRemote();
			DepositoDecretoModel lDepDecMod = lCtrlDepDec
					.ExRicercaDepositoDecretoByEveIdEventoNoDescTipoDecreto(lModel.getIdEvento());
			// tenore
			lVect = lCtrlTen.ExRicercaTenoreByDecretoOrderByPesoNoGenProc(lDepDecMod.getIdDepositoDecreto());
		} else if (lEveMod != null && "03".equals(lEveMod.getCodTipoProvvedimento())) {
			IDepositoOrdinanzaPc lCtrlDepOrd = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			DepositoOrdinanzaPcModel lOrdPCMod = lCtrlDepOrd.ExRicercaDepositoOrdinanzaPcByEvento(lModel
					.getIdEvento());
			// tenore
			lVect = lCtrlTen.ExRicercaTenoreByOrdinanzaOrderByPesoNoGenProc(lOrdPCMod
					.getIdDepositoOrdinanzaPc());
		}

		// recupero l'esito tenore
		TenoreModel lTenMod = (TenoreModel) lVect.get(0);
		String lEsito = lTenMod.getCodEsitoTenore();

		// Recupero il codice alternativo dalla collection di motivo provvedimento
		Collection lOggetto = (Collection) DecodificheManager.getInstance().getMotivoProvvedimento();
//		DecodificheUtils lUtils = new DecodificheUtils();
		String lCodiceAlternativo = DecodificheUtils.getCodAltebyCode(lOggetto, lEveMod.getCodMotivo());

		// recupero il codice dello stato_procedimento
		DecodificheModel lDecMod = new DecodificheModel();
		lDecMod.setContesto("STATO_PROC_GENERICO");
		lDecMod.setCodiceAlternativo(lCodiceAlternativo);
		lDecMod.setFiltro(lEsito);
		IDecodifiche lCtrlDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lDecMod = lCtrlDecodifiche.ExRicercaDecodificheByAbbByHigh(lDecMod);

		String lCodStatoProc = null;
		if (lDecMod != null)
			lCodStatoProc = lDecMod.getCode();

		InputStream lInput = null;
		if (isRequestParameterNullObj("noblob")) {
			lInput = getFile(ICostantiEvento.CAMPO_BLOB);
		}

		if (lInput != null) {
			byte[] lBuffer = new byte[lInput.available()];

			lInput.read(lBuffer);
			ByteArrayInputStream lSt = new ByteArrayInputStream(lBuffer);
			lModel.setDocBlobIn(lSt);
		}

		lModel.setDataAggiornamento(DateUtils.getSysDate());
		lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());

		if (isRequestChecked(ICostantiEvento.CAMPO_VALIDA)) {
			lModel.setFlagDocumentoRegistrato("S");
			IDecretoOrdinanzaSiep lCtrlDe = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
			lCtrlDe.ExUpdateValidaProvvedimentoGenerico(lModel, lCodStatoProc);
		} else {
			lModel.setFlagDocumentoRegistrato("N");
			IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
			lCtrlEv.ExUpdateDocument(lModel);
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