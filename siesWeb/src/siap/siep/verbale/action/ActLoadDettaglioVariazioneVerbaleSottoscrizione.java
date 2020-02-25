package siap.siep.verbale.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.camponota.controller.ICampoNota;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.cssa.controller.ICSSA;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleDataInizioModel;
import siap.siep.verbale.model.VerbaleModel;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadDettaglioVariazioneVerbaleSottoscrizione
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Variazione Verbale di Sottoscrizione
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

public class ActLoadDettaglioVariazioneVerbaleSottoscrizione extends ActSIESDettaglioProvvedimento
		implements ICostantiVerbale {
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		EventoModel lEveMod = new EventoModel();
		BigDecimal lIdVerbale = null;

		BigDecimal lIdEvento = new BigDecimal(this.getRequestStringParameter("IdEvento"));

		VerbaleModel lVerMod = new VerbaleModel();
		IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();

		// AMBROSINO 10/2010 - Quando vengo da 'Elenco Provvedim del PM' e devo visualizzare solo il Dettaglio
		// ho solamente l'Id dell'evento ; Durante la 'Variazione Data Misura' (non devo
		// fermarmi al solo dettaglio) mi porto anche l'Id del Verbale (sempre ammesso che esista)

		if (this.isRequestParameterNullObj(CAMPO_ID_VERBALE)) {
			VerbaleDataInizioModel lVerDatMod = new VerbaleDataInizioModel();
			lVerDatMod = lCtrl.ExRicercaVerbaleByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			// Dopo mi servirà VerbaleModel e non VerbaleDataInizioModel, quindi faccio quest'altra ricerca :
			lVerMod = lCtrl.ExRicercaVerbaleByKey(lVerDatMod.getIdVerbale());
		} else {
			// Siamo nel caso della variazione data inizio misura con verbale esistente
			lIdVerbale = getRequestBigDecimalParameter(CAMPO_ID_VERBALE);
			lVerMod = lCtrl.ExRicercaVerbaleByKey(lIdVerbale);
		}

		BigDecimal lIdFasc = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
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

		// AMBROSINO 08/2010 - Ricerca Evento inserito in fase di varizione data Inizio misura(provvedimento)

		lEveMod = null;
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrlEve.ExRicercaEventoByKey(lIdEvento);

		// model misura alternativa
		MisuraAlternativaModel lMisAltMod = new MisuraAlternativaModel();
		IMisuraAlternativa lCtrlMisAlt = SICOLookupRemote.getMisuraAlternativaRemote();
		lMisAltMod = lCtrlMisAlt.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEveIdEvento());

		// posizione giuridica
		/* PosizioneGiuridicaModel lPosMododel = */this.getPosizioneGiuridica(lIdEvento, lIdFasc);

		// posizione giuridica precedente
		// PosizioneGiuridicaModel lPosPre = null;
		IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		/* lPosPre = */lCtrlPos
				.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascMod.getIdFascicoloSiep());

		// campo note
		ICampoNota lCtrlCam = SICOLookupRemote.getCampoNotaRemote();
		CampoNotaModel lCamMod = lCtrlCam.ExRicercaCampoNotaByIdEvento(lEveMod.getIdEvento());
		setRequestAttribute("verbale", lVerMod);

		setRequestAttribute("misuraalternativa", lMisAltMod);
		setRequestAttribute("evento", lEveMod);
		setRequestAttribute("camponota", lCamMod);

		// String vedoDataIntermedia = "N";

		PenaResiduaModel lPenMod = this.getPenaResidua(lIdEvento, lIdFasc);
		setRequestAttribute("penaresidua", lPenMod);

		// if (lPenMod != null && lPenMod.getDataFinePresunta() != null && lPenMod.getDataFineReclusione() !=
		// null)
		// {
		// vedoDataIntermedia = "S";
		// }

		if (!this.isRequestAttributeNullObj("vedoDataIntermedia")) {
			setRequestAttribute("vedoDataIntermedia", this.getRequestAttribute("vedoDataIntermedia"));
		}

		// Ambrosino - Durante la 'Variazione Data Misura' (non devo fermarmi al solo dettaglio) e quindi
		// mi porto "dettaglioProvvedimento" = NO
		// Quando vengo da 'Elenco Provvedim del PM' e devo visualizzare solo il Dettaglio ,
		// "dettaglioProvvedimento" non esiste propio.

		String lPage = "";
		if (this.isRequestAttributeNullObj("dettaglioProvvedimento")) {
			setRequestAttribute("dettaglioProvvedimento", "SI");
			lPage = PG_LOAD_DETTAGLIO_PENA_RESIDUA_VARIAZIONE_VERBALE_SOTTOSCRIZIONE;
		} else {
			String lNoDett = (String) getRequestAttribute("dettaglioProvvedimento");
			if (lNoDett.equals("NO")) {
				setRequestAttribute("dettaglioProvvedimento", "NO");
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.verbale.action.ActCalcoloPenaVariazioneVerbaleSottoscrizione";
			}
		}

		return lPage;
	}

}