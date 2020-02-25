package siap.sige.collegio.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActRicercaCollegio
 * </p>
 * <p>
 * Description: Classe Action per la ricerca del Collegio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaCollegio extends ActionSiap implements ICostantiCollegio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Esegue la ricerca del Collegio.
	 * <p>
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 *         <p>
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		super.setLinkRitorno();

		String lPagina = "1";

		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		String lReturnPage = ""; // pagina jsp di ritorno

		CollegioModel lCollMod = new CollegioModel();

		// introdotto per sies 11.2.1
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_GIUDICE)
				&& !getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_GIUDICE).equals("-")) {
			siesLogger.debug(
					"CODICE GIUDICE>>" + getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_GIUDICE));
			ArrayList lArrayList = new ArrayList();
			CollegioMagistratoModel magColl = new CollegioMagistratoModel();
			magColl.setMagCodMagistrato(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_GIUDICE));
			lArrayList.add(magColl);
			if (lArrayList.size() != 0)
				lCollMod.setCollegioMagistrati(
						(CollegioMagistratoModel[]) lArrayList.toArray(new CollegioMagistratoModel[0]));
		}

		if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE)
				&& !getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).equals(""))
			lCollMod.setSezIdSezione(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));

		// Impostazione filtri sulla data di creazione collegio
		Date dataI = getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_VALIDITA, CAMPO_MESE_DATA_INIZIO_VALIDITA,
				CAMPO_GIORNO_DATA_INIZIO_VALIDITA);

		if (dataI != null)
			// 20190507 [SG]: modificate le impostazioni dei campi
			// lCollMod.setDataInizioValidita(dataI);
			lCollMod.setDataInserimento(dataI);

		// Impostazione filtri sulla data udienza dal - al
		Date dataUdienzaDAL = getRequestDateParameter(CAMPO_ANNO_ISCRIZIONE_INIZIALE,
				CAMPO_MESE_ISCRIZIONE_INIZIALE, CAMPO_GIORNO_ISCRIZIONE_INIZIALE);
		if (dataUdienzaDAL != null)
			lCollMod.setDataInizioValidita(dataUdienzaDAL);
		Date dataUdienzaAL = getRequestDateParameter(CAMPO_ANNO_ISCRIZIONE_FINALE,
				CAMPO_MESE_ISCRIZIONE_FINALE, CAMPO_GIORNO_ISCRIZIONE_FINALE);
		if (dataUdienzaAL != null)
			lCollMod.setDataFineValidita(dataUdienzaAL);

		if (isRequestChecked("flagDataFine@Null"))
			lCollMod.setMessage("dataFineisNull");

		// La ricerca è sempre filtrata per ufficio
		lCollMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
		// Vector lVect = lCtrl.ExRicercaCollegio(lCollMod);
		// 20171012: [SG] ricerca Paginata! Apro direttamente il dettaglio solo se il risultato è unico
		// cioè pagina 1 e count 1, non per pagina 2 e count 21 x esempio
		Vector lVect = lCtrl.ExRicercaCollegioPaged(lCollMod, Integer.parseInt(lPagina));
		if (lVect.size() == 1 && "1".equals(lPagina)) { // Unico Collegio.
			CollegioModel lCollegio = new CollegioModel((CollegioModel) lVect.firstElement());
			// Inserisce il model Collegio nella request
			setFunctionsAvailableToRequest("siap.sige.collegio.action.ActLoadDettaglioCollegio");
			RedirectTo lRedirect = new RedirectTo();
			lRedirect.setPage(IWebConstants.PG_MAIN);
			lRedirect.setAction("siap.sige.collegio.action.ActLoadDettaglioCollegio");
			lRedirect.setParameter(ICostantiCollegio.CAMPO_ID_COLLEGIO, lCollegio.getIdCollegio().toString());
			lReturnPage = lRedirect.toString();
		} else {
			// Paginazione
			int CountRisultati;
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrl.ExGetNumRicercaCollegio(lCollMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.warn("CountRisultati : " + CountRisultati);
			} else
				CountRisultati = getRequestIntParameter("CountRisultati");

			setRequestAttribute("CountRisultati", new BigDecimal("" + CountRisultati));
			setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
			setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

			// lista delle sezioni
			setRequestAttribute("collegi", lVect);
			lReturnPage = PG_RICERCACOLLEGIO;
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return lReturnPage;
	}

}