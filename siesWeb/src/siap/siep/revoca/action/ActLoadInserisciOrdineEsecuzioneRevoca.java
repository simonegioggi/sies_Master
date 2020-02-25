package siap.siep.revoca.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
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
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciOrdineEsecuzioneRevoca
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Sospensione
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

public class ActLoadInserisciOrdineEsecuzioneRevoca extends ActionSiap
		implements ICostantiRevoca, ICostantiDecretoOrdinanzaSiep {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		BigDecimal lIdDecOrd = getRequestBigDecimalParameter(CAMPO_ID_DECRETO_ORDINANZA_SIEP);

		IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();

		DecretoOrdinanzaSiepModel lDecOrdMod = lCtrl.ExRicercaDecretoOrdinanzaSiepByKey(lIdDecOrd);

		setRequestAttribute("decretoordinanza", lDecOrdMod);
		setRequestAttribute("CodiceMotivo", getRequestStringParameter("CodMotivo")); // 26/11/2010 Esecuzione
																						// pena presso
																						// Domicilio.

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

		/*
		 * ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote(); SospensioneModel lSospensione =
		 * lCtrlSosp.ExRicercaSospensioneByIdPenaResidua(lPenaResidua.getIdPenaResidua());
		 * 
		 * setRequestAttribute("sospensione", lSospensione);
		 */

		// Ricerca Magistrato Competente
		IMagistratoCompetente lCtrlMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagiComp = lCtrlMagComp
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lIdFascicolo);

		setRequestAttribute("magistratocompetente", lMagiComp);

		// Avvocati
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		/*
		 * AvvocatoModel lAvvMod = new AvvocatoModel(); AvvocatoFascicoloSiepModel lAvvFasMod = new
		 * AvvocatoFascicoloSiepModel(); lAvvFasMod.setFasSieIdFascicoloSiep(lIdFascicolo); Vector lAvvocati =
		 * lAvvCtrl.ExRicercaAvvocatiAttualiFascicolo(lAvvMod, lAvvFasMod);
		 */
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lIdFascicolo);

		setRequestAttribute("avvocati", lAvvocati);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaE", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		return PG_LOAD_INSERISCI_ORDINE_ESECUZIONE_REVOCA;
	}
}