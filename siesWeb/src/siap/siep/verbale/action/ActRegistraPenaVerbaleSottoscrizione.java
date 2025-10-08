package siap.siep.verbale.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.cssa.controller.ICSSA;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;

/**
 * ActInserisciPenaResidua - Classe Action per l'inserimento di PenaResidua
 *
 * @version 1.0
 */
public class ActRegistraPenaVerbaleSottoscrizione extends ActionSiap implements ICostantiVerbale {

	/**
	 * Azione di Aggiornamento del PenaResidua
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		PenaResiduaModel lPenMod = new PenaResiduaModel();

		BigDecimal lIdPenaResidua = new BigDecimal(this.getRequestStringParameter("idpenaresidua"));
		BigDecimal lIdMis = new BigDecimal(
				this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA));

		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		lPenMod = IPenRes.ExRicercaPenaResiduaByKey(lIdPenaResidua);

		lPenMod.setDataFine(getRequestDateParameter("APV", "MPV", "GPV"));
		lPenMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lPenMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lPenMod.setDataAggiornamento(DateUtils.getSysDate());
		lPenMod.setFlagValidato("S");

		// PenaResiduaModel lModPen = new PenaResiduaModel();
		// lModPen = IPenRes.ExModificaPenaResidua(lPenMod);

		// IPenRes.ExModificaPenaResidua(lPenMod);
		/**************************************************************************/
		/*
		 * String lGiorno = this.getRequestStringParameter("GiornoInizio"); String lMese =
		 * this.getRequestStringParameter("MeseInizio"); String lAnno =
		 * this.getRequestStringParameter("AnnoInizio"); Date lDataInizio =
		 * DateUtils.getDate(lAnno,lMese,lGiorno);
		 *
		 * String lGiornoP = this.getRequestStringParameter("ggpervenimento"); String lMeseP =
		 * this.getRequestStringParameter("mmpervenimento"); String lAnnoP =
		 * this.getRequestStringParameter("aapervenimento"); Date lDataPerv =
		 * DateUtils.getDate(lAnnoP,lMeseP,lGiornoP);
		 */

		BigDecimal lKeyVer = new BigDecimal(this.getRequestStringParameter("verbaleid"));

		VerbaleModel lVerMod = new VerbaleModel();
		IVerbale lCtrlVer = SIEPLookupRemote.getVerbaleRemote();
		lVerMod = lCtrlVer.ExRicercaVerbaleByKey(lKeyVer);

		/*
		 * lVerMod.setNote(this.getRequestStringParameter("Note"));
		 * lVerMod.setCssIdCssa(this.getRequestBigDecimalParameter("cssa"));
		 * lVerMod.setDataEmissione(lDataInizio); lVerMod.setDataPervenimento(lDataPerv);
		 */

		// model cssa
		CSSAModel lCssaMod = new CSSAModel();
		if (lVerMod != null && lVerMod.getCssIdCssa() != null
				&& lVerMod.getCssIdCssa().compareTo(new BigDecimal(0)) != 0) {
			ICSSA lCtrlCssa = SICOLookupRemote.getCSSARemote();
			lCssaMod = lCtrlCssa.getCSSAByKey(lVerMod.getCssIdCssa());
		}
		setRequestAttribute("cssa", lCssaMod);

		// model Istituto Detenzione
		IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();
		if (lVerMod != null && lVerMod.getIstDetIdIstitutoDetenzione() != null
				&& !lVerMod.getIstDetIdIstitutoDetenzione().equals("-")) {
			IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
			lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lVerMod.getIstDetIdIstitutoDetenzione());
		}
		setRequestAttribute("istitutodetenzione", lIstMod);

		MisuraAlternativaModel lMisAlMod = new MisuraAlternativaModel();
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		lMisAlMod = lMisAltCtrl.ExRicercaMisuraAlternativaByKey(lIdMis);

		// POSIZIONE GIURIDICA PRECEDENTE PASSATA AL CONTROLLER
		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosMod = lCtrlPos.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

		// Model Posizione Giuridica
		/*
		 * PosizioneGiuridicaModel lPosModel = new PosizioneGiuridicaModel();
		 *
		 * lPosModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 * lPosModel.setCodPosizioneProcessuale("-");
		 * lPosModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		 * lPosModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		 * lPosModel.setDataInserimento(DateUtils.getSysDate()); //lPosModel.setDataInizio(lDataInizio);
		 * lPosModel.setDataInizio(lVerMod.getDataPervenimento()); if(lVerMod != null &&
		 * lVerMod.getCssIdCssa() != null && lVerMod.getCssIdCssa().compareTo(new BigDecimal(0)) != 0) {
		 * lPosModel.setCodPosizioneGiuridica("13"); } if(lVerMod != null &&
		 * lVerMod.getIstDetIdIstitutoDetenzione()!= null &&
		 * !lVerMod.getIstDetIdIstitutoDetenzione().equals("-")) { lPosModel.setCodPosizioneGiuridica("14"); }
		 * if(lVerMod != null && lVerMod.getCodTipoUfficioFirmatario() != null &&
		 * !lVerMod.getCodTipoUfficioFirmatario().equals("-") && lVerMod.getCodLuogoUfficioFirmatario()!= null
		 * && !lVerMod.getCodLuogoUfficioFirmatario().equals("-")) { lPosModel.setCodPosizioneGiuridica("12");
		 * }
		 *
		 * lCtrlPos.ExInserisciPosizioneGiuridicaVerbaleSotto(lPosModel,lFascMod.getIdFascicoloSiep());
		 */

		/*
		 * // CERCA IL TOTALE GIORNI LIB ANTICIPATA ILicenzaPeriodiLibAnticipata lCtrlLib =
		 * SICOLookupRemote.getLicenzaPeriodiLibAntRemote(); // N.B. cerca quelli con FLAG_ELABORATO E int
		 * lTotGiorni =
		 * lCtrlLib.ExTotalePeriodiConcessiElaboratiByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 */
		// chiamata al SIEP-UC-022-LV
		// metodo modificato per consentire il rollback
		MisuraAlternativaModel lMisuraModel = new MisuraAlternativaModel();
		lMisuraModel = lMisAltCtrl.ExCalcolaFineEspiazionePenaMAConcessa(lVerMod, lPenMod, lPosMod, lMisAlMod,
				lFascMod.getIdFascicoloSiep());

		// MEV_9-SIEP mi serve anche l'evento decreto/ord puntato dalla MA per testare l'esito
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoModel provvSorv = lCtrlEve.ExRicercaEventoByKey(lMisuraModel.getEveIdEvento());
		setRequestAttribute("provvSorv", provvSorv);
		// MEV_9-SIEP - FINE

		setRequestAttribute("penaresidua", lPenMod);
		setRequestAttribute("vedoDataIntermedia", this.getRequestStringParameter("vedoDataIntermedia"));
		setRequestAttribute("verbale", lVerMod);
		setRequestAttribute("misuraposold", lMisuraModel);
		// setRequestAttribute("lTotGiorniConcessi", ""+lTotGiorni);

		return PG_REGISTRA_PENA_RESIDUA_VERBALE_SOTTOSCRIZIONE;
	}

}