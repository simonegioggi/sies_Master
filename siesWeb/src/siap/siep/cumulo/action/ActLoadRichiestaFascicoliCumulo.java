package siap.siep.cumulo.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadRicercaCumulo
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di Cumulo
 * </p>
 * Questa action viene richiamata tre volte: - direttamente dalla voce di menù orizzontale - dalla form di
 * inserimento sotto il tasto Carica. - dalla ActInserisciCumulo
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */

public class ActLoadRichiestaFascicoliCumulo extends ActCumulo
		implements ICostantiCumulo, ICostantiIstruttoriaCumulo {
	public String processRequest() throws F3BException {
		BigDecimal lFascID = null;
		String lStatoFasc = null;
		String lFlagVal = null;

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP)) {
			FascicoloSiepModel lFascMod = new FascicoloSiepModel();
			lFascID = new BigDecimal(
					getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP));

			IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
			lFascMod = lCtrlFasc.ExRicercaFascicoloByKey(lFascID);

			lStatoFasc = lFascMod.getCodStatoFascicolo();
			lFlagVal = lFascMod.getFlagValidato();

			this.setSessionAttribute("fascicolo", lFascMod);
		} else {
			if (this.isSessionAttributeNullObj("fascicolo"))
				return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_CUMULANTE + getClass().getName();

			lFascID = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
			lStatoFasc = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getCodStatoFascicolo();
			lFlagVal = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagValidato();

		}

		if (lStatoFasc.equals("01"))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Fascicolo Archiviato/Definito. Impossibile effettuare una operazione di cumulo");

		if (lFlagVal.equals("N"))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Fascicolo non Validato. Impossibile effettuare una operazione di cumulo");

		PosizioneGiuridicaModel lPG = new PosizioneGiuridicaModel();
		IPosizioneGiuridica iPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPG = iPG.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascID);

		if (lPG == null)
			throw new SIEPException(SIEPException.USER_MESSAGE, "Posizione Giuridica Inesistente");

		boolean libero = lPG.getCodPosizioneGiuridica().equals("07")
				|| lPG.getCodPosizioneGiuridica().equals("10") || lPG.getCodPosizioneGiuridica().equals("16")
				|| lPG.getCodPosizioneGiuridica().equals("17");

		// ==========================================================================
		//
		// ==========================================================================
		if ((libero
				&& ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagAltraCausa().equals("S"))
				|| !libero) {
			// LuogoDetenzioneModel lDtMod = new LuogoDetenzioneModel();
			ILuogoDetenzione lIld = SIEPLookupRemote.getLuogoDetenzioneRemote();
			/* lDtMod = */lIld.ExRicercaLuogoDetenzioneCorrenteByFascicoloSiep(lFascID);
		}

		// ==========================================================================
		//
		// ==========================================================================
		IPenaResidua lPen = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenRes = null;
		lPenRes = lPen.ExRicercaPenaResiduaUltimaValidata(lFascID);

		setRequestAttribute("PosizioneGiuridica", lPG);
		setRequestAttribute("PenaResidua", lPenRes);

		return PG_LOAD_CARICAFASCICOLI; // restituisce la jsp di VIEW
	}
}