package siap.siep.istanza.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.istanza.controller.IIstanza;
import siap.siep.istanza.model.IstanzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaIstanza
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Istanza
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

public class ActRicercaIstanza extends ActionSiap implements ICostantiIstanza {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		IstanzaModel lIstMod = new IstanzaModel();

		lIstMod.setIdIstanza(getRequestBigDecimalParameter(CAMPO_ID_ISTANZA));
		lIstMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_MOTIVO));
		lIstMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		lIstMod.setCognomeSoggettoPresentante(getRequestStringParameter(CAMPO_COGNOME_SOGGETTO_PRESENTANTE));
		lIstMod.setNomeSoggettoPresentante(getRequestStringParameter(CAMPO_NOME_SOGGETTO_PRESENTANTE));
		lIstMod.setDataPresentazione(getRequestDateParameter(CAMPO_ANNO_DATA_PRESENTAZIONE,
				CAMPO_MESE_DATA_PRESENTAZIONE, CAMPO_GIORNO_DATA_PRESENTAZIONE));
		lIstMod.setCodEsito(getRequestStringParameter(CAMPO_COD_ESITO));
		lIstMod.setAnnoRegistro(getRequestBigDecimalParameter(CAMPO_ANNO_REGISTRO));
		lIstMod.setProgrRegistro(getRequestBigDecimalParameter(CAMPO_PROGR_REGISTRO));
		lIstMod.setCodTipoUfficioDestinatario(getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_DESTINATARIO));
		lIstMod.setCodLuogoDestinatario(getRequestStringParameter(CAMPO_COD_LUOGO_DESTINATARIO));
		lIstMod.setCodUfficioDestinatario(getRequestStringParameter(CAMPO_COD_UFFICIO_DESTINATARIO));
		lIstMod.setCognomeAvvocato(getRequestStringParameter(CAMPO_COGNOME_AVVOCATO));
		lIstMod.setNomeAvvocato(getRequestStringParameter(CAMPO_NOME_AVVOCATO));
		lIstMod.setForoCompetenza(getRequestStringParameter(CAMPO_FORO_COMPETENZA));
		lIstMod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA));
		lIstMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA));
		lIstMod.setDataSentenza(getRequestDateParameter(CAMPO_ANNO_DATA_SENTENZA, CAMPO_MESE_DATA_SENTENZA,
				CAMPO_GIORNO_DATA_SENTENZA));
		lIstMod.setDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_DATA_IRREVOCABILITA,
				CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA));
		lIstMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
		lIstMod.setCodLuogoEmittente(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE));
		lIstMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lIstMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lIstMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));
		lIstMod.setCodOperatoreAggiornamento(getRequestStringParameter(CAMPO_COD_OPERATORE_AGGIORNAMENTO));
		lIstMod.setDataAggiornamento(getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		lIstMod.setCodUfficioAggiornamento(getRequestStringParameter(CAMPO_COD_UFFICIO_AGGIORNAMENTO));
		lIstMod.setSogIdSoggetto(getRequestBigDecimalParameter(CAMPO_SOG_ID_SOGGETTO));

		IIstanza lCtrl = SIEPLookupRemote.getIstanzaRemote();

		Vector lVect = lCtrl.ExRicercaIstanza(lIstMod);

		setRequestAttribute("istanza", lVect);

		return PG_RICERCAISTANZA;
	}

}