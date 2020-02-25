package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadValidaInterruzione
 * </p>
 * <p>
 * Description: Classe Action per la Validazione di Sospensione Interruzione
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

public class ActLoadValidaInterruzione extends ActionSiap
		implements ICostantiSospensione, ICostantiDecretoOrdinanzaSiep {
	/**
	 * Recupera i dati dell'interruzione ed effettua la validazione
	 * 
	 * @return la jsp di visualizzazione del detteglio e delle stampe
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// ==========================================================================
		// Recupero il decreto_ordinanza a cui è associato l'evento Interruzione
		// ==========================================================================
		BigDecimal lIdDecOrd = getRequestBigDecimalParameter(CAMPO_ID_DECRETO_ORDINANZA_SIEP);
		IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();

		DecretoOrdinanzaSiepModel lDecOrdMod = lCtrl.ExRicercaDecretoOrdinanzaSiepByKey(lIdDecOrd);
		this.setRequestAttribute("decretoordinanza", lDecOrdMod);

		// ==========================================================================
		// Recupero l'evento (12,25) legato all'interruzione
		// ==========================================================================
		EventoModel lEveRic = new EventoModel();
		lEveRic.setDecIdDecretoOrdinanzaSiep(lIdDecOrd);
		lEveRic.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoModel lEve = new EventoModel();
		Vector leventi = new Vector();
		leventi = lCtrlEve.ExRicercaEvento(lEveRic);
		lEve = (EventoModel) leventi.get(0);

		lEve.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lEve.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lEve.setDataAggiornamento(DateUtils.getSysDate());

		// ==========================================================================
		// Recupero l'ultima pena residua a sistema
		// ==========================================================================
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		setRequestAttribute("penaresidua", lPenaResidua);

		// ==========================================================================
		// Recupero la Posizione Giuridica
		// ==========================================================================
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosMod = lPosCtrl
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);

		// ==========================================================================
		// Recupero la Pena Complessiva
		// ==========================================================================
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		String lFlagErgastolo = "N";
		// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		if (lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "") {
			if (lPenComMod.getCodTipoPenaDetentiva().equals("03")) {
				lFlagErgastolo = "S";
			} else if (lPenComMod.getCodTipoPenaDetentiva().equals("04")) {
				lFlagErgastolo = "D";
			}
		}

		setRequestAttribute("flagergastolo", lFlagErgastolo);

		// ==========================================================================
		// Recupero il record Sospensione (se presente)
		// ==========================================================================
		ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
		SospensioneModel lSospensione = lCtrlSosp
				.ExRicercaSospensioneByIdPenaResidua(lPenaResidua.getIdPenaResidua());

		setRequestAttribute("sospensione", lSospensione);

		// ==========================================================================
		// Effettuo la VALIDAZIONE dell'evento, del decreto, della pena residua
		// ed eventuale aggiornamento della posizione giuridica e dello stato
		// del procedimento
		// ==========================================================================
		lCtrlSosp.ExUpdateValidaInterruzione(lEve, lFascMod, lPosMod, lPenaResidua, lDecOrdMod);

		// ==========================================================================
		// Recupero la NUOVA posizione giuridica
		// ==========================================================================
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		setRequestAttribute("posizioneluogoaltra", lPos);

		return PG_LOAD_DETTAGLIO_INTERRUZIONE_STAMPE;
	}
}