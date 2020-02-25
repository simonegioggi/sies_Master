package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
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

/**
 * <p>
 * Title: ActLoadInserisciMARevocaEspPressoDom
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Revoca EspPressoDom
 * </p>
 * Viene invocata due volta. La prima dalla griglia dell'Espiazione Presso il domicilio per
 * l'inserimento/selezione dell'ordinanza di Revoca. La seconda dalla Action di inserimento dell'ordinanza
 * (CAMPO_ID_DOCUMENTO_SIUS) per procedere all'inserimento del provvedimento di esecuzione
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciMARevocaEspPressoDom extends ActRevoca {

	public String processRequest() throws F3BException {

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = this.getRevocaConCalcolo();
		if (!lRitorno.equals(""))
			return lRitorno;

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();

		MisuraAlternativaModel lrevoca = null;
		if (!isRequestParameterNullObj(CAMPO_ID_DOCUMENTO_SIUS)) { // secondo giro, recupero l'ordinanza dei
																	// revoca inserita/selezionata
			BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
			if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
				lrevoca = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);
		}

		// ==========================================================================
		// Ricerca misura alternativa sospesa deve assolutamente cercare l'ultima prima
		// di entrare nella revoca.
		// Inoltre se esiste la misura di revoca chiamata lrevoca devo cercare
		// una misura precedente di tipo
		// sospensione affidamento mentre se lrevoca non esiste vuol dire che è la prima volta che ci passo e
		// quindi
		// rerco la corrente misura.
		// ==========================================================================
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
					|| lMisAlModSospesa.getCodTipoMisura() == null ||
					// !lMisAlModSospesa.getCodTipoMisura().equals("2280"))
					!lMisAlModSospesa.getCodTipoMisura().equals(
							ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_MOTIVO)) {
				lMisAlModSospesa = null;
			}
		}

		setRequestAttribute("misurasospesa", lMisAlModSospesa);

		if (lMisAlModConcessa != null && lMisAlModConcessa.getIdMisuraAlternativa() != null) {
			if (lMisAlModConcessa.getCodTipoDecisione() == null
					|| !lMisAlModConcessa.getCodTipoDecisione().equals("03")
					|| lMisAlModConcessa.getCodNaturaDecisione() == null
					|| !lMisAlModConcessa.getCodNaturaDecisione().equals("CO")
					|| lMisAlModConcessa.getCodTipoMisura() == null ||
					// !lMisAlModConcessa.getCodTipoMisura().equals("2245"))
					!lMisAlModConcessa.getCodTipoMisura().equals(
							ICostantiMisuraAlternativa.ESP_PRESSO_DOM_MOTIVO)) {
				lMisAlModConcessa = null;
			}
		}

		setRequestAttribute("misuraconcessa", lMisAlModConcessa);

		/* 
		 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato
		 * Numero MEV : SIES v10
		 * Autore    : gioggi
		 * Data      : 28/gen/2016
		 * Branch    : MEV_SIES v10
		 */
		// Setto il campo codice motivo
		// Option lOption = new
		// Option(DecodificheManager.getInstance().getMotivoProvvedimentoRevocaMAEspPressoDom());
		// setRequestAttribute("motivoProvv", "" + lOption);
		//***** FINE INTERVENTO MEV_SIES v10 *****//

		// MEV 29 - nel caso di Espiazione Presso il Domicilio si prova a differenziare i codici motivo tra
		// TDS e UDS
		// con il caricamento dinamico delle combo
		Collection lMotivi = DecodificheManager.getInstance().getMotivoProvvedimentoRevocaMAEspPressoDom();
		Collection lMotivoProvvTDS = new Vector();
		Collection lMotivoProvvUDS = new Vector();

		Iterator lItrMotivi = lMotivi.iterator();
		while (lItrMotivi.hasNext()) {
			DecodificheModel lDecModel = (DecodificheModel) lItrMotivi.next();
			if (lDecModel.getCode().equals(ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO))
				lMotivoProvvTDS.add(lDecModel);
			else if (lDecModel.getCode()
					.equals(ICostantiMisuraAlternativa.REVOCA_ESP_PRESSO_DOM_MOTIVO_DAUDS))
				lMotivoProvvUDS.add(lDecModel);
		}
		setRequestAttribute("motivoProvvTDS", lMotivoProvvTDS);
		setRequestAttribute("motivoProvvUDS", lMotivoProvvUDS);

		setRequestAttribute("tipoRevoca", ICostantiMisuraAlternativa.ESP_PRESSO_DOM);

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		return PG_LOAD_INSERISCI_MA_REVOCA_AFF_PROV;
	}

}