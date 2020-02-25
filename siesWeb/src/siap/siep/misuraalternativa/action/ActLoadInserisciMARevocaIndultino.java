package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciMARevocaIndultino
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Revoca Indultino
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
public class ActLoadInserisciMARevocaIndultino extends ActRevoca {

	public String processRequest() throws F3BException {

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = this.getRevocaConCalcolo();
		if (!lRitorno.equals(""))
			return lRitorno;

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();

		MisuraAlternativaModel lrevoca = null;
		if (!isRequestParameterNullObj(CAMPO_ID_DOCUMENTO_SIUS)) {
			BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
			if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
				lrevoca = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);
		}
		/*
		 * ricerca misura alternativa sospesa deve assolutamente cercare l'ultima prima di entrare nella
		 * revoca. Inoltre se esiste la misura di revoca chiamata lrevoca devo cercare una misura precedente
		 * di tipo sospensione affidamento mentre se lrevoca non esiste vuol dire che è la prima volta che ci
		 * passo e quindi cerco la corrente misura.
		 */
		MisuraAlternativaModel lMisAlModSospesa = null;
		IMisuraAlternativaIndultino lMisCtrl = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
		if (lrevoca != null && lrevoca.getIdMisuraAlternativa() != null) {
			UfficioModel lUffMod = new UfficioModel();
			lUffMod = lCtrlUffEmi.getUfficioByKey(lrevoca.getChiaveUfficioFascicoloSius());

			setRequestAttribute("UfficioEmittente", lUffMod);
			setRequestAttribute("misuraalternativa", lrevoca);

			// la seconda volta che ci passo la revoca esite!
			lMisAlModSospesa = lMisCtrl.ExRicercaMisuraAlternativaPrecedenteByIdFascicolo(lFascMod
					.getIdFascicoloSiep());
		} else {
			// la prima volta che ci passo la revoca non esite!
			lMisAlModSospesa = lMisAltCtrl.ExRicercaMisuraAlternativaCorrenteByIdFascicolo(lFascMod
					.getIdFascicoloSiep());
		}

		// istanzio un nuovo di tipo per concessione per sapere se
		// l'ultima misura in questione è una concessione
		MisuraAlternativaModel lMisAlModConcessa = null;
		if (lMisAlModSospesa != null)
			lMisAlModConcessa = new MisuraAlternativaModel(lMisAlModSospesa);

		if (lMisAlModSospesa != null && lMisAlModSospesa.getIdMisuraAlternativa() != null) {
			if (lMisAlModSospesa.getCodTipoDecisione() == null
					|| !lMisAlModSospesa.getCodTipoDecisione().equals("02")
					|| lMisAlModSospesa.getCodNaturaDecisione() == null
					|| !lMisAlModSospesa.getCodNaturaDecisione().equals("SP")
					|| lMisAlModSospesa.getCodTipoMisura() == null
					|| !lMisAlModSospesa.getCodTipoMisura().equals("2280")) {
				lMisAlModSospesa = null;
			}
		}

		setRequestAttribute("misurasospesa", lMisAlModSospesa);

		if (lMisAlModConcessa != null && lMisAlModConcessa.getIdMisuraAlternativa() != null) {
			if (lMisAlModConcessa.getCodTipoDecisione() == null
					|| !lMisAlModConcessa.getCodTipoDecisione().equals("03")
					|| lMisAlModConcessa.getCodNaturaDecisione() == null
					|| !lMisAlModConcessa.getCodNaturaDecisione().equals("CO")
					|| lMisAlModConcessa.getCodTipoMisura() == null
					|| !lMisAlModConcessa.getCodTipoMisura().equals("2245")) {
				lMisAlModConcessa = null;
			}
		}

		setRequestAttribute("misuraconcessa", lMisAlModConcessa);

		// setto il campo codice motivo
		Option lOption = new Option(DecodificheManager.getInstance()
				.getMotivoProvvedimentoRevocaMAIndultino());
		setRequestAttribute("motivoProvv", "" + lOption);

		setRequestAttribute("tipoRevoca", "INDULTINO");

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_REVOCA_AFF_PROV;
	}

}