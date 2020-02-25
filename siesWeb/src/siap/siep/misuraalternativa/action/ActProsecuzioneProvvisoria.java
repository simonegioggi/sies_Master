package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActProsecuzioneProvvisoria
 * </p>
 * <p>
 * Description: Classe Action per il padre della Prosecuzione provvisoria
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

public class ActProsecuzioneProvvisoria extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {
	@SuppressWarnings("rawtypes")
	protected String getProsecuzioneProvvisoria() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		isFascicoloSiepDiCompetenza();

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);

		// Controllo Esistenza pena residua
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();

		if (!this.isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA)
				&& this.getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA) != null) {
			BigDecimal lKeyPenaRe = new BigDecimal(
					getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA));
			lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaByKey(lKeyPenaRe);
			setRequestAttribute("penaresidua", lPenaResMod);

			PenaResiduaModel lPenModel = (PenaResiduaModel) getSessionAttribute("PROMApenaresidua");
			setRequestAttribute("nuovapenaresidua", lPenModel);
		} else {
			// rimuovo la pena dalla sessione
			this.removeSessionAttribute("PROMApenaresidua");

			lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaUltimaValidata(lFascMod.getIdFascicoloSiep());
			if ((lPenaResMod == null || lPenaResMod.getIdPenaResidua() == null)
					&& lPos.getPosizioneGiuridica().isLibero()) {
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Non risulta nessun procedimento per questo fascicolo. Impossibile eseguire la revoca della misura alternativa.");
			} else if ((lPenaResMod == null || lPenaResMod.getIdPenaResidua() == null)
					&& !lPos.getPosizioneGiuridica().isLibero()) {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Pena Residua da Espiare Inesistente o non Validata. Eseguire Calcolo della pena?");
				lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
						+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return IWebConstants.PG_MESSAGE;
			}
			setRequestAttribute("penaresidua", lPenaResMod);
		}

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		// Posizione Giuridica precedente
		PosizioneGiuridicaModel lPosPreMod = new PosizioneGiuridicaModel();
		lPosPreMod = lPosCtrl
				.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneprecedente", lPosPreMod);

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();

		MisuraAlternativaModel lProsecuzione = null;
		if (!isRequestParameterNullObj(CAMPO_ID_DOCUMENTO_SIUS)) {
			BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
					ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
			if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
				lProsecuzione = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);
		}

		if (lProsecuzione != null && lProsecuzione.getIdMisuraAlternativa() != null) {
			UfficioModel lUffMod = new UfficioModel();
			lUffMod = lCtrlUffEmi.getUfficioByKey(lProsecuzione.getChiaveUfficioFascicoloSius());
			setRequestAttribute("UfficioEmittente", lUffMod);
			setRequestAttribute("misuraalternativa", lProsecuzione);
		}

		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), "UDS");
		setRequestAttribute("tipoUfficio", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		setRequestAttribute("autoritaEmi", "" + lOption);

		return "";
	}

}