package siap.siep.misuraalternativa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciSospProvvDetDom
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Sospensione Provvisoria Detenzione Domiciliare
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
public class ActLoadInsSospProvvArrestiDomiciliari extends ActSospensioneProvvisoria {

	public String processRequest() throws F3BException {

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = getSospensioni();
		if (!lRitorno.equals(""))
			return lRitorno;

		// ricerca esistenza almeno una misura alternativa cancessa
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		IMisuraAlternativaIndultino lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
//		MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
		// Inizio MAC 2016/10/21
		// Gestione codice MOTIVO_PROVVEDIMENTO = 2291
		// Il codice 2291 sostituisce i codici (2741,2742,2743,2756)
		// eliminati dalla base dati
		
		// MAC 2017/04/01  Ripristinati i codici (2741,2742,2743,2756)
		//String[] tipoMisura = { "2756", "2741", "2742", "2743", "2291" };
		
		//PEC m_dg.DOG07.01-10-2018.0030206.U (deve essere censito anche i codice 2291)
		String[] tipoMisura = { "2756", "2741", "2742", "2743", "2291"};		
		String[] natura = { "CO", "DD", "SP" };
		String[] decisione = { "02", "03" };
		//fine intervento  
		
		/*lMisAlModConcessa = */lMisAltCtrl.ExRicercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisione(
				lFascMod.getIdFascicoloSiep(), decisione, natura, tipoMisura);

		/*
		 * if (this.isRequestParameterNullObj("warning_2")) { if (lMisAlModConcessa == null ||
		 * lMisAlModConcessa.getIdMisuraAlternativa() == null) {
		 * setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName()); setRequestAttribute(
		 * IWebConstants.MESSAGE_TEXT,
		 * "Attenzione: è stata richiesta la sospensione provvisoria di una misura non concessa.Continuare?");
		 * 
		 * return "/jsp/files/siap/siep/misuraalternativa/WarningMisura.jsp"; } }
		 */

		// setto il campo ufficio emittente
		Option lOptionUfficioEmittenteArrestiDomiciliari = new Option(DecodificheManager.getInstance()
				.getUfficioEmittenteArrestiDomiciliari());
		setRequestAttribute("tipoUfficioEmittenteArrestiDomiciliari", ""
				+ lOptionUfficioEmittenteArrestiDomiciliari);

		// setto il campo codice motivo
		Option lOption = new Option(DecodificheManager.getInstance()
				.getMotivoProvvedimentoSospProvvArrestiDom());
		setRequestAttribute("motivoProvv", "" + lOption);

		setRequestAttribute("tipoSospensione", "DETENZIONE");

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_SOSP_PROVV_ARRESTI_DOMICILIARI;
	}

}