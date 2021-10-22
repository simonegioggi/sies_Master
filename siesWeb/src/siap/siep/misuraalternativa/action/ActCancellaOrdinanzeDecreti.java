package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActCancellaOrdinanzeDecreti
 * </p>
 * <p>
 * Description: Classe Action per la Cancellazione dei decreti e delle ordinanze e dei provvedimenti relativi
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class ActCancellaOrdinanzeDecreti extends ActionSiap implements ICostantiMisuraAlternativa {

	public String processRequest() throws F3BException {

		String motivazioni = null;
		BigDecimal lId = this.getRequestBigDecimalParameter("IdEvento");

		DepositoOrdinanzaPcModel lDepPCMod = new DepositoOrdinanzaPcModel();
		IDepositoOrdinanzaPc lCtrlDepPc = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		lDepPCMod = lCtrlDepPc.ExRicercaDepositoOrdinanzaPcByEvento(lId);

		if (lDepPCMod != null && lDepPCMod.getGenPridGeneraleProcedimento() != null) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Impossibile eseguire l'annullamento del Decreto/Ordinanza.");
		} else {
			DepositoDecretoModel lDepDec = new DepositoDecretoModel();
			IDepositoDecreto lCtrlDepDec = SIUSLookupRemote.getDepositoDecretoRemote();
			lDepDec = lCtrlDepDec.ExRicercaDepositoDecretoByIdEvento(lId);
			if (lDepDec != null && lDepDec.getGenPridGeneraleProcedimento() != null) {
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Impossibile eseguire l'annullamento del Decreto/Ordinanza.");
			}
		}

		// riempie il model
		EventoModel lEveMod = new EventoModel();
		EventoModel lEveModRic = new EventoModel();

		CampoNotaModel lCampoMod = new CampoNotaModel();

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		lEveModRic = lCtrlEvento.ExRicercaEventoByKey(lId);

		if (lEveModRic != null) {
			if (lEveModRic.getFlagDocumentoRegistrato() == null
					|| (lEveModRic.getFlagDocumentoRegistrato().equals("")
							// provvedimenti non validati cancellazione fisica
							|| lEveModRic.getFlagDocumentoRegistrato().equals("N"))) {
				lCtrl.ExCancellaEventoConStoreProcedure(lEveModRic);
			} // provvedimenti validati cancellazione logica
			else {
				motivazioni = this.getRequestStringParameter("motivazioni");

				lEveMod.setIdEvento(lId);
				lEveMod.setFlagDocumentoRegistrato("A");
				lEveMod.setFasSieIdFascicoloSiep(lEveModRic.getFasSieIdFascicoloSiep());

				lCampoMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lCampoMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lCampoMod.setDataInserimento(DateUtils.getSysDate());
				lCampoMod.setEveIdEvento(lId);
				lCampoMod.setDescr(motivazioni);
				IMisuraAlternativaIndultino lCtrlMis = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
				lCtrlMis.ExAggiornaEventoInserisciCampoNota(lEveMod, lCampoMod);
			}
		}
		FascicoloSiepModel lFasc = (FascicoloSiepModel) this.getSessionAttribute("fascicolo");

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.fascicolo.action.ActRicercaProcedimentiPerNumeroSIEP&"
				+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lFasc.getIdFascicoloSiep();
	}
}