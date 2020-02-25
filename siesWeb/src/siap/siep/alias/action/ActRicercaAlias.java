package siap.siep.alias.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.alias.controller.AliasController;

/**
 * <p>
 * Title: ActRicercaAlias
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Alias
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
public class ActRicercaAlias extends ActionSiap implements ICostantiAlias {

	public String processRequest() throws F3BException {

		// AliasModel lAliMod = new AliasModel();
		// lAliMod.setIdAlias(getRequestBigDecimalParameter(CAMPO_ID_ALIAS));
		/*
		 * lAliMod.setCognome( getRequestStringParameter( CAMPO_COGNOME) ); lAliMod.setNome(
		 * getRequestStringParameter( CAMPO_NOME) ); lAliMod.setPaternita( getRequestStringParameter(
		 * CAMPO_PATERNITA) ); lAliMod.setCodFiscale( getRequestStringParameter( CAMPO_COD_FISCALE) );
		 * lAliMod.setCodCs( getRequestStringParameter( CAMPO_COD_CS) ); lAliMod.setCodAfis(
		 * getRequestStringParameter( CAMPO_COD_AFIS) ); lAliMod.setAttoNascita( getRequestStringParameter(
		 * CAMPO_ATTO_NASCITA) ); lAliMod.setSesso( getRequestStringParameter( CAMPO_SESSO) );
		 * lAliMod.setCodComuneNascita( getRequestStringParameter( CAMPO_COD_COMUNE_NASCITA) );
		 * lAliMod.setCodProvinciaNascita( getRequestStringParameter( CAMPO_COD_PROVINCIA_NASCITA) );
		 * lAliMod.setCodStatoNascita( getRequestStringParameter( CAMPO_COD_STATO_NASCITA) );
		 * lAliMod.setDataNascita( getRequestDateParameter(
		 * CAMPO_ANNO_DATA_NASCITA,CAMPO_MESE_DATA_NASCITA,CAMPO_GIORNO_DATA_NASCITA) ); lAliMod.setNote(
		 * getRequestStringParameter( CAMPO_NOTE) ); lAliMod.setCodOperatoreInserimento(
		 * getRequestStringParameter( CAMPO_COD_OPERATORE_INSERIMENTO) ); lAliMod.setDataInserimento(
		 * getRequestDateParameter(
		 * CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
		 * lAliMod.setCodUfficioInserimento( getRequestStringParameter( CAMPO_COD_UFFICIO_INSERIMENTO) );
		 * lAliMod.setCodOperatoreAggiornamento( getRequestStringParameter( CAMPO_COD_OPERATORE_AGGIORNAMENTO)
		 * ); lAliMod.setDataAggiornamento( getRequestDateParameter(
		 * CAMPO_ANNO_DATA_AGGIORNAMENTO,CAMPO_MESE_DATA_AGGIORNAMENTO,CAMPO_GIORNO_DATA_AGGIORNAMENTO) );
		 * lAliMod.setCodUfficioAggiornamento( getRequestStringParameter( CAMPO_COD_UFFICIO_AGGIORNAMENTO) );
		 * lAliMod.setSogIdSoggetto( getRequestBigDecimalParameter( CAMPO_SOG_ID_SOGGETTO) );
		 */
		// IAlias lCtrl = SICOLookupRemote.getAliasRemote();

		// Prendo Soggetto in Sessione
		BigDecimal lIdSoggetto;
		// = this.getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO);
		SoggettoModel lSogMod = new SoggettoModel();

		// if(lIdSoggetto.intValue()==0)
		// {
		lSogMod = (SoggettoModel) getSessionAttribute("soggetto");
		lIdSoggetto = lSogMod.getIdSoggetto();
		// }

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		AliasController lCtrl = new AliasController();
		Vector lVect = lCtrl.ExRicercaAliasByIdSoggettoPaged(lIdSoggetto, Integer.parseInt(lPagina));

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrl.ExGetCountAliasByIdSoggetto(lIdSoggetto);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		setRequestAttribute("aliasvect", lVect);

		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		SoggettoModel lSoggetto = lSogCtrl.ExRicercaSoggettoByKey(lIdSoggetto);

		setRequestAttribute("soggetto", lSoggetto);

		// Inserisce il model soggetto in sessione
		// setSessionAttribute("soggetto", lSoggetto);

		return PG_RICERCAALIAS;
	}

}