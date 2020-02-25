package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaSoggettoUnicoFascicoloSius extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings({ "rawtypes" })
	public String processRequest() throws F3BException {

		SoggettoModel lSogMod = new SoggettoModel();

		// paginazione
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// parse della request
		if (getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA).length() > 1) {
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
			lSogMod.setCodComuneNascita(lComMod.getCodComune());
		}

		// riempie il model
		lSogMod.setCognome(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME).toUpperCase());
		lSogMod.setNome(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME).toUpperCase());

		if (getRequestStringParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA).length() > 2)
			lSogMod.setDataNascita(getRequestDateParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA,
					ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA, ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA));

		if (!getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA).equals("-"))
			lSogMod.setCodStatoNascita(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA));

		lSogMod.setPaternita(getRequestStringParameter(ICostantiSoggetto.CAMPO_PATERNITA));
		lSogMod.setCognomeMadre(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME_MADRE));
		lSogMod.setNomeMadre(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME_MADRE));

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lSogMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		// chiama il controller per SIEP

//		IFascicoloSius lFascSogCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		// Vector lFascicoliSoggetti =
		// lFascSogCtrl.ExRicercaFascicoloSiusBySoggettoUnicoPage(lSogMod,Integer.parseInt(lPagina));
		Vector lFascicoliSoggetti = null;

		String lReturnPage = "";

		// Paginazione
		BigDecimal CountRisultati = null;
		if (isRequestParameterNullObj("CountRisultati")) {
			// CountRisultati = lFascSogCtrl.ExGetNumRicercaFascicoloSiusBySoggettoUnico(lSogMod);
			CountRisultati = null;
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		// setta la risposta nella request
		setRequestAttribute("fascicoli", lFascicoliSoggetti);

		lReturnPage = ICostantiFascicoloSius.PG_RICERCAFASCICOLO_SIUS_UNICO;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}