package siap.sige.fascicolo.action;

/**
* <p>Title: ActRicercaFSigePerTitoloEsecutivo</p>
* <p>Description: Classe Action per la ricerca di Procedimenti SIGE per Titolo esecutivo</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: </p>
* @version 1.0
*/
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

public class ActRicercaProcSigePerTitoloEsecutivo extends ActionSige implements ICostantiFascicoloSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Gestione bottone di ritorno
		setLinkRitorno();

		// Istanzio il Model
		FascicoloSiepModel lFasSiepMod = new FascicoloSiepModel();

		// Recupero del parametro di ricerca (Ufficio/Distretto).
		String codUfficioSige = "";
		if (getRequestStringParameter(ICostantiFasSigeSentenza.CAMPO_AMBITO_RICERCA).compareTo("U") == 0)
			codUfficioSige = getCodUfficioUtenteConnesso();

		// Settaggio dei criteri di ricerca.
		if (codUfficioSige == "")
			setRequestAttribute("ambitoRicerca", "D");
		else
			setRequestAttribute("ambitoRicerca", "U");

		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ID_SOGGETTO))
			lFasSiepMod.setSogIdSoggetto(getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO));

		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_ID_SENTENZA))
			lFasSiepMod.setSenIdSentenza(getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ID_SENTENZA));

		if ((getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_DESCR_COMUNE_UFFICIO).equals(""))
				&& (!getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_UFFICIO).equals("-")))
			lFasSiepMod
					.setChiaveUfficio(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_UFFICIO));

		if (!getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_UFFICIO).equals("-")
				&& (!getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_DESCR_COMUNE_UFFICIO)
						.equals(""))) {
			// Si Utilizza il campo setCodUfficioInserimento come veicolo per trasmettere il codice ufficio
			// recuperato dal tipo ufficio e dalla descr ufficio
			lFasSiepMod.setCodUfficioInserimento(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_UFFICIO),
					getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_DESCR_COMUNE_UFFICIO)));
		}

		if (!getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_DESCR_COMUNE_UFFICIO).equals("")) {
			IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
			if (lUffCtrl.verifyUfficioByDescrComune(
					(getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).toUpperCase())))
				lFasSiepMod.setDescrComuneUfficio(
						(getRequestStringParameter(CAMPO_DESCR_COMUNE_UFFICIO).toUpperCase()));
		}

		// Parametri per il range di ANNO/PROGRESSIVO.
		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO))
			lFasSiepMod
					.setChiaveAnno(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO));

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR))
			lFasSiepMod
					.setChiaveProgr(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR));

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE))
			lFasSiepMod.setChiaveAnnoIniziale(
					getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE));

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE))
			lFasSiepMod.setChiaveProgrIniziale(
					getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE));

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE))
			lFasSiepMod.setChiaveAnnoFinale(
					getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE));

		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE))
			lFasSiepMod.setChiaveProgrFinale(
					getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE));

		IFascicoloSige lCtrlFasSige = SIGELookupRemote.getFascicoloSigeRemote();
		// 20181031: aggiunto parametro di passaggio
		Vector lVect = lCtrlFasSige.ExRicercaFasSigeByDatiFasSiep(lFasSiepMod, codUfficioSige, checkMinori());

		String lReturnPage = "";

		if (lVect.size() == 1) {
			lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE + "="
					+ ((FascicoloSigeEstesoModel) lVect.get(0)).getFascicoloSige().getIdFascicoloSige()
							.toString();
		} else {
			setRequestAttribute("fascicoli", lVect);

			// lReturnPage = PG_RICERCAFASCICOLO_SIEP;
			lReturnPage = ICostantiFascicoloSige.PG_RICERCA_FASCICOLIPERFASSIEP;
		}
		return lReturnPage; // restituisce la jsp di VIEW
	}

}