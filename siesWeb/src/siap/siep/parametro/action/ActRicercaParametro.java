package siap.siep.parametro.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaParametro
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Parametro
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
public class ActRicercaParametro extends ActionSiap implements ICostantiParametro {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		ParametroModel lParMod = new ParametroModel();
		lParMod.setIdParametro(getRequestBigDecimalParameter(CAMPO_ID_PARAMETRO));
		lParMod.setNomeParametro(getRequestStringParameter(CAMPO_NOME_PARAMETRO));
		lParMod.setValore(getRequestStringParameter(CAMPO_VALORE));
		lParMod.setAnni(getRequestBigDecimalParameter(CAMPO_ANNI));
		lParMod.setMesi(getRequestBigDecimalParameter(CAMPO_MESI));
		lParMod.setGiorni(getRequestBigDecimalParameter(CAMPO_GIORNI));
		lParMod.setImporto(getRequestBigDecimalParameter(CAMPO_IMPORTO));
		lParMod.setDataInizioValidita(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_VALIDITA,
				CAMPO_MESE_DATA_INIZIO_VALIDITA, CAMPO_GIORNO_DATA_INIZIO_VALIDITA));
		lParMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
				CAMPO_MESE_DATA_FINE_VALIDITA, CAMPO_GIORNO_DATA_FINE_VALIDITA));
		lParMod.setCodUfficioValidita(getRequestStringParameter(CAMPO_COD_UFFICIO_VALIDITA));
		lParMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lParMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lParMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));
		lParMod.setCodOperatoreAggiornamento(getRequestStringParameter(CAMPO_COD_OPERATORE_AGGIORNAMENTO));
		lParMod.setDataAggiornamento(getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		lParMod.setCodUfficioAggiormanento(getRequestStringParameter(CAMPO_COD_UFFICIO_AGGIORMANENTO));

		IParametro lCtrl = SIEPLookupRemote.getParametroRemote();
		Vector lVect = lCtrl.ExRicercaParametro(lParMod);
		setRequestAttribute("parametro", lVect);

		return PG_RICERCAPARAMETRO;
	}

}