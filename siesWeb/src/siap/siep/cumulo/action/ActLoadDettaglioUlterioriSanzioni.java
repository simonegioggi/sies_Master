package siap.siep.cumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.ulterioresanzionecumulo.controller.IUlterioreSanzioneCumulo;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciUlterioriSanzioni.j
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Cumulo
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
public class ActLoadDettaglioUlterioriSanzioni extends ActionSiap implements ICostantiCumulo {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		BigDecimal lFascID = null;
		String lStatoFasc = null;
		String lFlagVal = null;
//		String lFlagCumulante = null;
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

			if (this.isSessionAttributeNullObj("fascicolo")) {
				return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_CUMULANTE + getClass().getName();
			}

			lFascID = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
			lStatoFasc = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getCodStatoFascicolo();
			lFlagVal = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagValidato();
//			lFlagCumulante = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagCumulante();
		}
		if (lStatoFasc.equals("01"))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Fascicolo Archiviato/Definito. Impossibile effettuare una operazione di cumulo");
		if (lFlagVal.equals("N"))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Fascicolo non Validato. Impossibile effettuare una operazione di cumulo");
		// if (lFlagCumulante == null || !lFlagCumulante.equals("S"))
		// throw new F3BException(F3BException.USER_MESSAGE,
		// "Attenzione : Il fascicolo non risulta soggetto a cumulo! Selezionare un altro fascicolo.");

		PosizioneGiuridicaModel lPG = new PosizioneGiuridicaModel();
		IPosizioneGiuridica iPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPG = iPG.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascID);
//		String LuogoDet = new String("");
		if (lPG == null) {
			throw new SIEPException(SIEPException.USER_MESSAGE, "Posizione Giuridica Inesistente");
		}

		setRequestAttribute("PosizioneGiuridica", lPG);

		Option lOption = new Option(DecodificheManager.getInstance().getFlagErgastolo());
		setRequestAttribute("FlagErgastolo", "" + lOption);

		// Ricerca cumulo per FasSieIdFascicoloSiep
		CumuloModel lCumMod = new CumuloModel();
		ICumulo iCum = SIEPLookupRemote.getCumuloRemote();
		Vector cumuli = iCum.ExRicercaFascicoliCumulobyIdFascicoloSiepFlagValidato(lFascID);

		if (cumuli.size() > 0) {
			lCumMod = ((CumuloModel) (cumuli).get(0));
		}

		IUlterioreSanzioneCumulo lUlt = SIEPLookupRemote.getUlterioreSanzioneCumuloRemote();
		Vector lUltMod = lUlt.ExRicercaUlterioreSanzioneCumuloByFascicoloIDCumul0(lFascID,
				lCumMod.getIdCumulo());

		setRequestAttribute("ulterioriSanzione", lUltMod);

		setSessionAttribute("cumulowiz", "");

		return PG_LOAD_DETTAGLIO_ULTERIORI_SANZIONI;
	}

}