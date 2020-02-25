package siap.siep.modulocumulo.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActCaricaElencoTitoliPerSelezione extends ActionModuloCumulo
		implements ICostantiRichiestePmInCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();

		String lPage = "";
		String lParentFormName = getRequestStringParameter(CAMPO_PARENT_FORM_NAME);
		String lParentFormType = getRequestStringParameter(CAMPO_PARENT_FORM_TYPE);

		setRequestAttribute(CAMPO_PARENT_FORM_NAME, lParentFormName);
		setRequestAttribute(CAMPO_PARENT_FORM_TYPE, lParentFormType);

		// ==========================================================================
		// Effettua la ricerca dei Titoli in funzione della tipologia di Form chiamante
		// ==========================================================================
		siesLogger.debug("lParentFormType = " + lParentFormType);

		if (FORM_TYPE_RIC_APP_BENEFICI.equals(lParentFormType)) {
			getDatiRichiestaTitoli(lIstruttoriaModel);
			lPage = PG_POPUP_LISTA_TITOLI;
		} else if (FORM_TYPE_RIC_REV_SAN_SOST.equals(lParentFormType)) {
			// richiama una jsp di test per il doc analisi
			getDatiRichiestaTitoli(lIstruttoriaModel);
			lPage = PG_POPUP_TEST;
		} else if (FORM_TYPE_RIC_REV_TEST.equals(lParentFormType)) {
			// richiama una jsp di test per il doc analisi
			getDatiRichiestaTitoli(lIstruttoriaModel);
			lPage = PG_POPUP_TEST;
		} else if (FORM_TYPE_RIC_APPL_PA.equals(lParentFormType)) {
			getDatiRichiestaTitoli(lIstruttoriaModel);
			lPage = PG_POPUP_LISTA_TITOLI_PA;
		} else {
			// Aggiungere gli altri casi
		}

		return lPage;
	}

	/**
	 * Recupera la lista dei Titoli su cui applicare i benefici di Amnistia/Indulto
	 * 
	 */
	private void getDatiRichiestaTitoli(IstruttoriaCumuloModel aIstruttoriaModel) throws Exception {

		// ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();

		// TODO Alleggerire la query
		String lOrdinamento = aIstruttoriaModel.getOrdinamentoTitoli();

		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		Vector<TitoloCumulatoModel> lListaTitoli = lIstrCtrl.ExRicercaTitoliByIstruttoriaOrderBy(
				aIstruttoriaModel.getIdIstruttoriaCumulo(), lOrdinamento);
		setRequestAttribute("ListaTitoli", lListaTitoli);

	}

}