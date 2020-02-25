package siap.sico.magistrato.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
// per la decodifica
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaMagistrato
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Magistrato
 * </p>
 * La ricerca è ristretta ai magistrati impiegati nello stesso ufficio dell'utente collegato
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
@SuppressWarnings("rawtypes")
public class ActRicercaMagistrato extends ActionSiap implements ICostantiMagistrato {

	public String processRequest() throws F3BException {

		String lReturnPage = ""; // pagina jsp di ritorno
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		MagistratoModel lMagMod = new MagistratoModel();
		lMagMod.setCodMagistrato(StringUtils
				.convertSqlString((getRequestStringParameter(CAMPO_COD_MAGISTRATO)).toUpperCase()));
		lMagMod.setCognome(
				StringUtils.convertSqlString((getRequestStringParameter(CAMPO_COGNOME)).toUpperCase()));
		lMagMod.setNome(StringUtils.convertSqlString((getRequestStringParameter(CAMPO_NOME)).toUpperCase()));
		// lMagMod.setFlagStato( getRequestStringParameter( CAMPO_FLAG_STATO) );

		lMagMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		// lMagMod.setDataInizioValidita( getRequestDateParameter(
		// CAMPO_ANNO_DATA_INIZIO_VALIDITA,CAMPO_MESE_DATA_INIZIO_VALIDITA,CAMPO_GIORNO_DATA_INIZIO_VALIDITA)
		// );
		// lMagMod.setDataFineValidita( getRequestDateParameter(
		// CAMPO_ANNO_DATA_FINE_VALIDITA,CAMPO_MESE_DATA_FINE_VALIDITA,CAMPO_GIORNO_DATA_FINE_VALIDITA) );

		IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();

		Vector lVect = lCtrl.ExRicercaMagistratoPaged(lMagMod, Integer.parseInt(lPagina));

		// Decodifica di Flag_stato
		Collection lCol = (DecodificheManager.getInstance()).getFlagStato();

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrl.ExGetCountMagistratiPaged(lMagMod);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");
		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		if (lVect.size() == 1 && CountRisultati.compareTo(new BigDecimal(1)) == 0) { // unico Magistrato
			MagistratoModel lMagModel = new MagistratoModel((MagistratoModel) lVect.firstElement());
			lMagModel.setFlagStato(DecodificheUtils.getDescbyCode(lCol, lMagModel.getFlagStato()));

			// Inserisce il model Magistrato nella request
			setRequestAttribute("magistrato", lMagModel);
			setFunctionsAvailableToRequest("siap.sico.magistrato.action.ActLoadDettaglioMagistrato");
			lReturnPage = PG_LOAD_DETTAGLIOMAGISTRATO;
		} else { // lista di Magistrati
					// Inserisce il Vector magistrati nella request
			setRequestAttribute("magistrati", lVect);
			// Inserisce la Collection di decodifica del flag disponibilità
			setRequestAttribute("flags", lCol);
			lReturnPage = PG_RICERCAMAGISTRATO;
		}

		return lReturnPage;
	}

}