package siap.siep.misuraalternativa.action;

import java.util.Collection;
import java.util.Iterator;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * <p>
 * Title: ActLoadInserisciSospProvvEspPressoDom
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Sospensione Provvisoria Espiazione Pena presso
 * Domicilio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadInserisciSospProvvEspPressoDom extends ActSospensioneProvvisoria {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = getSospensioni();
		if (!lRitorno.equals(""))
			return lRitorno;

		// ricerca esistenza almeno una misura alternativa cancessa
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		IMisuraAlternativaIndultino lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
		MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
		String[] tipoMisura = { ICostantiMisuraAlternativa.ESP_PRESSO_DOM_MOTIVO };
		String[] natura = { "CO" };
		String[] decisione = { "03" };
		lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisione(
				lFascMod.getIdFascicoloSiep(), decisione, natura, tipoMisura);

		if (this.isRequestParameterNullObj("warning_2")) {
			if (lMisAlModConcessa == null || lMisAlModConcessa.getIdMisuraAlternativa() == null) {
				setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName());
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Attenzione: è stata richiesta la sospensione provvisoria di una misura non concessa.Continuare?");
				return "/jsp/files/siap/siep/misuraalternativa/WarningMisura.jsp";
			}
		}

		// risetto il campo codice motivo perchè nella mscehra nn deve vedere la tendina
		// ma un campo singolo non modificabile
		Collection lmotivo = DecodificheManager.getInstance().getMotivoProvvedimentoMASospEspPressoDom();

		String lDesMotivo = "";
		String lCodiceMotivo = "";
		if (lmotivo != null && !lmotivo.isEmpty()) {
			Iterator lIter = lmotivo.iterator();
			if (lIter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) lIter.next();
				lDesMotivo = lDecMod.getDescription();
				lCodiceMotivo = lDecMod.getCode();
			}
		}

		setRequestAttribute("motivoProvv", lDesMotivo);
		setRequestAttribute("codicemotivo", lCodiceMotivo);
		setRequestAttribute("tipoSospensione", ICostantiMisuraAlternativa.ESP_PRESSO_DOM);

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_DECRETO_SOSP;
	}
}