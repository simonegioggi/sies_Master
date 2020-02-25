package siap.siep.misuraalternativa.action;

import java.util.Collection;
import java.util.Iterator;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * <p>
 * Title: ActLoadInserisciMARipristinoEspPressoDom
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Ripristino Espiazione Pena presso Domicilio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadInserisciMARipristinoEspPressoDom extends ActRipristino {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = getRipristino();
		if (!lRitorno.equals(""))
			return lRitorno;

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// ricerca esistenza almeno una misura alternativa sospesa
		IMisuraAlternativaIndultino lMisCtrl = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
		MisuraAlternativaModel lMisAlMod = new MisuraAlternativaModel();
		String[] tipoMisura = { ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_MOTIVO };
		String[] natura = { "SP" };
		String[] decisione = { "02" };
		lMisAlMod = lMisCtrl.ExRicercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisione(
				lFascMod.getIdFascicoloSiep(), decisione, natura, tipoMisura);

		if (this.isRequestParameterNullObj("warning_2")) {
			if (lMisAlMod == null || lMisAlMod.getIdMisuraAlternativa() == null) {
				// set goto page set flag misura
				setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName());
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Attenzione: è stato richiesto il ripristino di una misura non sospesa.Continuare?");

				return "/jsp/files/siap/siep/misuraalternativa/WarningMisura.jsp";
			}
		}

		// setto il campo codice motivo
		Collection lmotivo = DecodificheManager.getInstance()
				.getMotivoProvvedimentoMARipristinoEspPressoDom();

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

		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS(), "UDS");
		setRequestAttribute("tipoUfficioSIUS", "" + lOption);
		setRequestAttribute("tipoMisura", ICostantiMisuraAlternativa.ESP_PRESSO_DOM);

		setRequestAttribute("motivoProvv", lDesMotivo);
		setRequestAttribute("codicemotivo", lCodiceMotivo);
		setRequestAttribute("tipoSospensione", ICostantiMisuraAlternativa.ESP_PRESSO_DOM);

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_RIPRISTINO;
	}
}