package siap.siep.fascicolo.action;

/**
 * <p>Title: ActRicercaFascicolo</p>
 * <p>Description: Azione di ricerca dei Procedimenti (Fascicoli SIEP) non validati</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

public class ActRicercaFascicoliNonValidati extends ActionSiap implements ICostantiFascicoloSiep {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Istanzio il Model
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		lFasMod.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE));
		lFasMod.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE));
		lFasMod.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE));
		lFasMod.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE));

		if (!isRequestParameterNullObj("tipoClasse")) {
			String[] lClassiFascicolo = this.getRequestStringParameters("tipoClasse");
			lFasMod.setClassiFascicolo(lClassiFascicolo);
		}

		// Ticket#20220801014 - nella ricerca dei procedimenti SIEP "non validati" escono anche procedimenti 
		//                      Archiviati e molti classe 9 in quanto è possibile Archiviare un procedimento 
		//                      senza validarlo. Es se isritto per errore.
		// La condizione sul FLAG_VALIDATO non è quindi sufficiente per quel tipo di ricerca.
		// La si sostituisce con lo COD_STATO_FASCICOLO = '02' Iscritto , ovvero non ancora validato 
		//lFasMod.setFlagValidato("N");
		lFasMod.setCodStatoFascicolo("02"); // ISCRITTO (non validato)
		// Ticket#20220801014 - FINE
		
		lFasMod.setChiaveUfficio(this.getCodUfficioUtenteConnesso());

		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		Vector lVect = lCtrl.ExRicercaFascicoloOnViewPaged(lFasMod, Integer.parseInt(lPagina));

		String lReturnPage = "";

		if (lVect.size() == 1) {
			lReturnPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIEP + "="
					+ ((FascicoloSiepModel) lVect.get(0)).getIdFascicoloSiep().toString();
		} else {
			setRequestAttribute("fascicoli", lVect);
			BigDecimal CountRisultati;

			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExgetCountFascicoli(lFasMod);
			} else {
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");
			}

			setRequestAttribute("CountRisultati", CountRisultati);
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			String lAzione = "siap.siep.fascicolo.action.ActRicercaProcedimentiNonValidati";
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);

			setRequestAttribute("LabelFunzioneNonValidati", "Non Validati");

			lReturnPage = PG_RICERCAFASCICOLO_SIEP;
		}

		return lReturnPage; // restituisce la jsp di VIEW
	}
}