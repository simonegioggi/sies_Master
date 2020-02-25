package siap.siep.ripristino.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import f3b.util.F3BException;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
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
import siap.siep.sospensione.controller.IInterruzione;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.PeriodoInterruzioneModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioRipristino
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Ripristino
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

public class ActLoadDettaglioRipristino extends ActionSiap
		implements ICostantiRipristino, ICostantiDecretoOrdinanzaSiep {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		BigDecimal lIdDecOrd = getRequestBigDecimalParameter(CAMPO_ID_DECRETO_ORDINANZA_SIEP);

		IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();

		DecretoOrdinanzaSiepModel lDecOrdMod = lCtrl.ExRicercaDecretoOrdinanzaSiepByKey(lIdDecOrd);

		setRequestAttribute("decretoordinanza", lDecOrdMod);

		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		setRequestAttribute("posizioneluogoaltra", lPos);

		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

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

		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaPerFascicolo(lIdFascicolo);

		setRequestAttribute("penaresidua", lPenaResidua);

		ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
		SospensioneModel lSospensione = lCtrlSosp
				.ExRicercaSospensioneByIdPenaResidua(lPenaResidua.getIdPenaResidua());

		setRequestAttribute("sospensione", lSospensione);

		// CERCA EVENTI DI INTERRUZIONE
		IInterruzione lIntCtrl = SIEPLookupRemote.getInterruzioneRemote();
		List lListaInterruzioni = lIntCtrl.ExRicercaPeriodiInterruzione(lIdFascicolo);

		setRequestAttribute("listainterruzioni", lListaInterruzioni);

		// TOTALE INTERRUZIONE
		CalendarModel lQuantumSomma = new CalendarModel();
		CalendarUtil lCalUtil = new CalendarUtil();
		Iterator iter = lListaInterruzioni.iterator();
		while (iter.hasNext()) {
			PeriodoInterruzioneModel lPeriodo = (PeriodoInterruzioneModel) iter.next();
			CalendarModel Quantum = lPeriodo.getQuantum();

			lQuantumSomma = lCalUtil.sommaGiorni(lQuantumSomma, Quantum);
		}

		setRequestAttribute("totaleinterruzione", lQuantumSomma);

		return PG_LOAD_DETTAGLIO_RIPRISTINO;
	}
}