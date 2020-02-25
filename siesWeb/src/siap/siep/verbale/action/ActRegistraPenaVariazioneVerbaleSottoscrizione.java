package siap.siep.verbale.action;

/**
* <p>Title: ActRegistraPenaVariazioneVerbaleSottoscrizione</p>
* <p>Description: Classe Action per Registra PenaResidua in Variazione Data Inizio Misura</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.camponota.controller.ICampoNota;
import siap.sico.camponota.model.CampoNotaModel;
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

public class ActRegistraPenaVariazioneVerbaleSottoscrizione extends ActionSiap implements ICostantiVerbale {

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
		BigDecimal lIdEvento = new BigDecimal(this.getRequestStringParameter("EventoId"));

		// Ricerca Verbale :
		// Il Verbale potrebbe anche non essere presente nel procedimento; comunque se esiste l'IDVerbale
		// facciamo la getRequest e andiamo a cercare il verbale by key

		BigDecimal lKeyVer = null;
		if (!this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_ID_VERBALE)) {
			lKeyVer = new BigDecimal(this.getRequestStringParameter(ICostantiVerbale.CAMPO_ID_VERBALE));
		} else {
			lKeyVer = null;
		}

		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		lPenMod = IPenRes.ExRicercaPenaResiduaByKey(lIdPenaResidua);
		lPenMod.setDataFine(getRequestDateParameter("APV", "MPV", "GPV"));
		lPenMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lPenMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lPenMod.setDataAggiornamento(DateUtils.getSysDate());
		lPenMod.setFlagValidato("S");
		VerbaleModel lVerMod = new VerbaleModel();

		if (lKeyVer != null) {
			IVerbale lCtrlVer = SIEPLookupRemote.getVerbaleRemote();
			lVerMod = lCtrlVer.ExRicercaVerbaleByKey(lKeyVer);
		} else {
			lVerMod = null;
		}

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

		// chiamata al SIEP-UC-022-LV
		// metodo modificato per consentire il rollback
		MisuraAlternativaModel lMisuraModel = new MisuraAlternativaModel();
		lMisuraModel = lMisAltCtrl.ExCalcolaFineEspiazionePenaMAConcessa(lVerMod, lPenMod, lPosMod, lMisAlMod,
				lFascMod.getIdFascicoloSiep());

		setRequestAttribute("penaresidua", lPenMod);
		setRequestAttribute("verbale", lVerMod);
		setRequestAttribute("misuraposold", lMisuraModel);

		// AMBROSINO 18/08/2010 - Ricerca evento tipo=01 Tipo Prov=25 Motivo=5414
		// Scritto in fase di variazione Data Inizio Misura e
		// Campo Nota legato

		EventoModel lEveModRet = new EventoModel();
		IEvento lCtrlEve1 = SICOLookupRemote.getEventoRemote();
		lEveModRet = lCtrlEve1.ExRicercaEventoByKey(lIdEvento);

		// campo note
		ICampoNota lCtrlCam = SICOLookupRemote.getCampoNotaRemote();
		CampoNotaModel lCamMod = lCtrlCam.ExRicercaCampoNotaByIdEvento(lEveModRet.getIdEvento());

		setRequestAttribute("Comunicazione", lEveModRet);
		setRequestAttribute("camponota", lCamMod);
		setRequestAttribute("misuraalternativa", lMisAlMod);

		return PG_REGISTRA_PENA_RESIDUA_VARIAZIONE_VERBALE_SOTTOSCRIZIONE;
	}

}