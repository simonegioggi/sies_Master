package siap.sico.magistratocompetente.action;

import java.util.Vector;

import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActRicercaMagistratoCompetente
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di MagistratoCompetente
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
public class ActRicercaMagistratoCompetente extends ActionSiap implements ICostantiMagistratoCompetente {

	public String processRequest() throws F3BException {

		MagistratoCompetenteModel lMagMod = new MagistratoCompetenteModel();
		lMagMod.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO,
				CAMPO_GIORNO_DATA_INIZIO));
		lMagMod.setDataFine(
				getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE));
		lMagMod.setCodRuoloMagistrato(getRequestStringParameter(CAMPO_COD_RUOLO_MAGISTRATO));
		lMagMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lMagMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lMagMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));
		lMagMod.setCodOperatoreAggiornamento(getRequestStringParameter(CAMPO_COD_OPERATORE_AGGIORNAMENTO));
		lMagMod.setDataAggiornamento(getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		lMagMod.setCodUfficioAggiornamento(getRequestStringParameter(CAMPO_COD_UFFICIO_AGGIORNAMENTO));
		lMagMod.setMagCodMagistrato(getRequestStringParameter(CAMPO_MAG_COD_MAGISTRATO));
		lMagMod.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP));

		IMagistratoCompetente lCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		Vector lVect = lCtrl.ExRicercaMagistratoCompetente(lMagMod);
		setRequestAttribute("magistratocompetente", lVect);

		return PG_RICERCAMAGISTRATOCOMPETENTE;
	}

}