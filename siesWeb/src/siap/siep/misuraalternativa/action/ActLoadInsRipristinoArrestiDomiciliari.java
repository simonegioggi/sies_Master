package siap.siep.misuraalternativa.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * <p>
 * Title: ActLoadInsRipristinoArrestiDomiciliari
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Ripristino Detenzione Domiciliare
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
public class ActLoadInsRipristinoArrestiDomiciliari extends ActRipristino {

	public String processRequest() throws F3BException {

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = getRipristino();
		if (!lRitorno.equals(""))
			return lRitorno;

		// ricerca esistenza almeno una misura alternativa cancessa
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		IMisuraAlternativaIndultino lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
		String[] tipoMisura = { "2748", "2749", "2758", "2759", "2752", "2753", "2754", "2755" };
		String[] natura = { "SP" };
		String[] decisione = { "02" };
		lMisAltCtrl.ExRicercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisione(
				lFascMod.getIdFascicoloSiep(), decisione, natura, tipoMisura);

		/*
		 * if (this.isRequestParameterNullObj("warning_2")) { if (lMisAlModConcessa == null ||
		 * lMisAlModConcessa.getIdMisuraAlternativa() == null) { //set goto page set flag misura
		 * setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName()); setRequestAttribute(
		 * IWebConstants.MESSAGE_TEXT,
		 * "Attenzione: è stato richiesto il ripristino di una misura non sospesa.Continuare?");
		 *
		 * return "/jsp/files/siap/siep/misuraalternativa/WarningMisura.jsp"; } }
		 */

		// setto il campo ufficio emittente
		Option lOptionUfficioEmittenteArrestiDomiciliari = new Option(
				DecodificheManager.getInstance().getUfficioEmittenteArrestiDomiciliari());
		setRequestAttribute("tipoUfficioEmittenteArrestiDomiciliari",
				"" + lOptionUfficioEmittenteArrestiDomiciliari);

		// setto il campo motivoProvvTribunale/motivoProvvMagistrato
		Option lOption = new Option(
				DecodificheManager.getInstance().getMotivoProvvedimentoRipristinoArrestiDom());
		lOption.setFilter(new String[] { "-", "2748", "2749", "2758", "2759" });
		setRequestAttribute("motivoProvvTribunale", "" + lOption);
		lOption.setFilter(new String[] { "-", "2752", "2753", "2754", "2755" });
		setRequestAttribute("motivoProvvMagistrato", "" + lOption);
		lOption.setFilter(new String[] { "-" });
		setRequestAttribute("motivoProvvVuoto", "" + lOption);

		// new d.f. DL 146/2013
		Vector<DecodificheModel> lTipoProvvSorv = new Vector<>();
		// lTipoProvvSorv.add(new DecodificheModel("-","-","","","","","","","") );
		lTipoProvvSorv.add(new DecodificheModel("03", "Ordinanza", "", "", "", "", "", "", ""));
		lTipoProvvSorv.add(new DecodificheModel("02", "Decreto", "", "", "", "", "", "", ""));
		Option lOptionTipoProvvSorv = new Option(lTipoProvvSorv);
		lOptionTipoProvvSorv.setSelected("-");
		setRequestAttribute("comboTipoProvvSorv", "" + lOptionTipoProvvSorv);

		setRequestAttribute("tipoSospensione", "DETENZIONE");

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_RIPRISTINO_ARRESTI_DOMICILIARI;
	}

}