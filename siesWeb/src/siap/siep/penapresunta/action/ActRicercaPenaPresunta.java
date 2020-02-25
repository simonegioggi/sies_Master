package siap.siep.penapresunta.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.penapresunta.controller.IPenaPresunta;
import siap.siep.penapresunta.model.PenaPresuntaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaPenaPresunta
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di PenaPresunta
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

public class ActRicercaPenaPresunta extends ActionSiap implements ICostantiPenaPresunta {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		PenaPresuntaModel lPenMod = new PenaPresuntaModel();
		lPenMod.setIdPenaPresunta(getRequestBigDecimalParameter(CAMPO_ID_PENA_PRESUNTA));
		lPenMod.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO,
				CAMPO_GIORNO_DATA_INIZIO));
		lPenMod.setDataFine(
				getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE));
		lPenMod.setNumAnniReclusione(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_RECLUSIONE));
		lPenMod.setNumMesiReclusione(getRequestBigDecimalParameter(CAMPO_NUM_MESI_RECLUSIONE));
		lPenMod.setNumGiorniReclusione(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_RECLUSIONE));
		lPenMod.setImportoMulta(getRequestBigDecimalParameter(CAMPO_IMPORTO_MULTA));
		lPenMod.setNumAnniArresto(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ARRESTO));
		lPenMod.setNumMesiArresto(getRequestBigDecimalParameter(CAMPO_NUM_MESI_ARRESTO));
		lPenMod.setNumGiorniArresto(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ARRESTO));
		lPenMod.setImportoAmmenda(getRequestBigDecimalParameter(CAMPO_IMPORTO_AMMENDA));
		lPenMod.setDiesAQuo(getRequestStringParameter(CAMPO_DIES_A_QUO));
		lPenMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lPenMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lPenMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));
		lPenMod.setCodOperatoreAggiornamento(getRequestStringParameter(CAMPO_COD_OPERATORE_AGGIORNAMENTO));
		lPenMod.setDataAggiornamento(getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		lPenMod.setCodUfficioAggiornamento(getRequestStringParameter(CAMPO_COD_UFFICIO_AGGIORNAMENTO));
		lPenMod.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP));
		lPenMod.setDataFineReclusione(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_RECLUSIONE,
				CAMPO_MESE_DATA_FINE_RECLUSIONE, CAMPO_GIORNO_DATA_FINE_RECLUSIONE));
		lPenMod.setDataInizioArresto(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_ARRESTO,
				CAMPO_MESE_DATA_INIZIO_ARRESTO, CAMPO_GIORNO_DATA_INIZIO_ARRESTO));

		IPenaPresunta lCtrl = SIEPLookupRemote.getPenaPresuntaRemote();
		Vector lVect = lCtrl.ExRicercaPenaPresunta(lPenMod);
		setRequestAttribute("penapresunta", lVect);

		return PG_RICERCAPENAPRESUNTA;
	}

}