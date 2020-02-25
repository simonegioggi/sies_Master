package siap.sius.prescrizione.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.sius.prescrizione.controller.IPrescrizione;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaPrescrizione
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Prescrizione
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

public class ActRicercaPrescrizione extends ActionSiap implements ICostantiPrescrizione {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		PrescrizioneModel lPreMod = new PrescrizioneModel();
		lPreMod.setIdPrescrizione(getRequestBigDecimalParameter(CAMPO_ID_PRESCRIZIONE));
		lPreMod.setCodTipoPrescrizione(getRequestStringParameter(CAMPO_COD_TIPO_PRESCRIZIONE));
		lPreMod.setCodLuogoAffidamento(getRequestStringParameter(CAMPO_COD_LUOGO_AFFIDAMENTO));
		lPreMod.setCodUffMagistratoCompetente(getRequestStringParameter(CAMPO_COD_UFF_MAGISTRATO_COMPETENTE));
		lPreMod.setCodLuogoAutorizzato(getRequestStringParameter(CAMPO_COD_LUOGO_AUTORIZZATO));
		lPreMod.setIdCssaCompetente(getRequestBigDecimalParameter(CAMPO_ID_CSSA_COMPETENTE));
		lPreMod.setDescrMansioneLavorativa(getRequestStringParameter(CAMPO_DESCR_MANSIONE_LAVORATIVA));
		lPreMod.setDescrLuogoLavoro(getRequestStringParameter(CAMPO_DESCR_LUOGO_LAVORO));
		lPreMod.setCodProvinciaAutorizzata(getRequestStringParameter(CAMPO_COD_PROVINCIA_AUTORIZZATA));
		lPreMod.setOraUscitaAbitazione(getRequestStringParameter(CAMPO_ORA_USCITA_ABITAZIONE));
		lPreMod.setOraRientroAbitazione(getRequestStringParameter(CAMPO_ORA_RIENTRO_ABITAZIONE));
		lPreMod.setAutoritaCompetenteControllo(
				getRequestStringParameter(CAMPO_AUTORITA_COMPETENTE_CONTROLLO));
		lPreMod.setNumVolteControllo(getRequestBigDecimalParameter(CAMPO_NUM_VOLTE_CONTROLLO));
		lPreMod.setDescrAltraPrescrizione(getRequestStringParameter(CAMPO_DESCR_ALTRA_PRESCRIZIONE1));
		lPreMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lPreMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lPreMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));
		lPreMod.setCodOperatoreAggiornamento(getRequestStringParameter(CAMPO_COD_OPERATORE_AGGIORNAMENTO));
		lPreMod.setDataAggiornamento(getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		lPreMod.setCodUfficioAggiornamento(getRequestStringParameter(CAMPO_COD_UFFICIO_AGGIORNAMENTO));
		lPreMod.setEveIdEve(getRequestBigDecimalParameter(CAMPO_EVE_ID_EVENTO));
		// lPreMod.setEveIdEve( getRequestBigDecimalParameter( CAMPO_DEP_OPID_DEPOSITO_ORDINANZA_PC) );

		IPrescrizione lCtrl = SIUSLookupRemote.getPrescrizioneRemote();
		Vector lVect = lCtrl.ExRicercaPrescrizione(lPreMod);
		setRequestAttribute("prescrizione", lVect);

		return PG_RICERCAPRESCRIZIONE;
	}

}