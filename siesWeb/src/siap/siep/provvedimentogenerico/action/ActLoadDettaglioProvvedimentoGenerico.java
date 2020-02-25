package siap.siep.provvedimentogenerico.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.camponota.controller.ICampoNota;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.controller.ITenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioProvvedimentoGenerico
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio del Provvedimento Generico
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
public class ActLoadDettaglioProvvedimentoGenerico extends ActProvvedimentoGenerico {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Evento
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);
		setRequestAttribute("evento", lEveMod);

		// campo note
		ICampoNota lCtrlCam = SICOLookupRemote.getCampoNotaRemote();
		CampoNotaModel lCamMod = lCtrlCam.ExRicercaCampoNotaByIdEvento(lIdEvento);
		setRequestAttribute("camponota", lCamMod);

		// Decreto o Ordinanza dipende dal tipo provvedimento
		DepositoDecretoModel lDepDecMod = new DepositoDecretoModel();
		DepositoOrdinanzaPcModel lOrdPCMod = new DepositoOrdinanzaPcModel();

		ITenore lCtrlTen = SIUSLookupRemote.getTenoreRemote();
		Vector lVect = null;

		if (lEveMod != null && "02".equals(lEveMod.getCodTipoProvvedimento())) {
			IDepositoDecreto lCtrlDepDec = SIUSLookupRemote.getDepositoDecretoRemote();
			lDepDecMod = lCtrlDepDec.ExRicercaDepositoDecretoByEveIdEventoNoDescTipoDecreto(lIdEvento);
			// tenore
			lVect = lCtrlTen.ExRicercaTenoreByDecretoOrderByPesoNoGenProc(lDepDecMod.getIdDepositoDecreto());
		} else if (lEveMod != null && "03".equals(lEveMod.getCodTipoProvvedimento())) {
			IDepositoOrdinanzaPc lCtrlDepOrd = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			lOrdPCMod = lCtrlDepOrd.ExRicercaDepositoOrdinanzaPcByEvento(lIdEvento);
			// tenore
			lVect = lCtrlTen.ExRicercaTenoreByOrdinanzaOrderByPesoNoGenProc(lOrdPCMod
					.getIdDepositoOrdinanzaPc());
		}

		setRequestAttribute("depositodecreto", lDepDecMod);
		setRequestAttribute("depositoordinanza", lOrdPCMod);

		if (lVect != null)
			setRequestAttribute("tenore", (TenoreModel) lVect.get(0));

		// posizione giuridica
		/*
		 * REWORK DETTAGLIO IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo
		 * (lFascMod.getIdFascicoloSiep());
		 */

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());

		setRequestAttribute("posizioneluogoaltra", lPos);

		// penaresidua
		/*
		 * REWORK DETTAGLIO IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		 * PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaPerFascicolo(lIdFascicolo);
		 */

		PenaResiduaModel lPenaResidua = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());

		setRequestAttribute("penaresidua", lPenaResidua);

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UfficioModel lUfficioMod = this.getUfficioUtenteConnesso();
		String lCodTipoUfficio = lUfficioMod.getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		return PG_LOAD_DETTAGLIO_PROVVEDIMENTO_GENERICO;
	}

}