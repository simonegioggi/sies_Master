package siap.siep.scarti.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.scarti.controller.IWScarti;
import siap.siep.scarti.model.WScartiModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaWScarti
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di WScarti
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

public class ActRicercaWScarti extends ActionSiap implements ICostantiWScarti {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// pagina
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		IWScarti lCtrl = SIEPLookupRemote.getWScartiRemote();
		Vector lVect = null;
		WScartiModel lWScMod = new WScartiModel();

		// prende parametri dalla form, quindi non serve (almeno per ora)
		/*
		 * lWScMod.setIdScarti( getRequestBigDecimalParameter( CAMPO_ID_SCARTI) ); lWScMod.setTabella(
		 * getRequestStringParameter( CAMPO_TABELLA) ); lWScMod.setAnnRes( getRequestBigDecimalParameter(
		 * CAMPO_ANN_RES) ); lWScMod.setNumRes( getRequestStringParameter( CAMPO_NUM_RES) );
		 * lWScMod.setLetRes( getRequestStringParameter( CAMPO_LET_RES) ); lWScMod.setChiaveAlternativa(
		 * getRequestStringParameter( CAMPO_CHIAVE_ALTERNATIVA) ); lWScMod.setNoteScarto(
		 * getRequestStringParameter( CAMPO_NOTE_SCARTO) ); lWScMod.setCausaScarto( getRequestStringParameter(
		 * CAMPO_CAUSA_SCARTO) );
		 */

		lVect = lCtrl.ExRicercaScartiPaged(lWScMod, Integer.parseInt(lPagina));
		setRequestAttribute("wscarti", lVect);

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrl.ExGetCountScarti(lWScMod);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		return PG_LISTA_WSCARTI;
	}

}
