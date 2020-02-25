package siap.siep.cumulo.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaCumulo
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Cumulo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @deprecated mai utilizzata, generata dal framework
 * @version 1.0
 */

public class ActRicercaCumulo extends ActionSiap implements ICostantiCumulo {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		CumuloModel lCumMod = new CumuloModel();
		lCumMod.setIdCumulo(getRequestBigDecimalParameter(CAMPO_ID_CUMULO));
		lCumMod.setIdFascicoloSiepCumulato(getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIEP_CUMULATO));
		lCumMod.setChiaveAnnoFasCumulato(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FAS_CUMULATO));
		lCumMod.setChiaveProgrFasCumulato(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FAS_CUMULATO));
		lCumMod.setCodTipoUfficioFasCumulato(getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_FAS_CUMULATO));
		lCumMod.setCodLuogoUfficioFasCumulato(
				getRequestStringParameter(CAMPO_COD_LUOGO_UFFICIO_FAS_CUMULATO));
		lCumMod.setCodUfficioFasCumulato(getRequestStringParameter(CAMPO_COD_UFFICIO_FAS_CUMULATO));
		lCumMod.setCodTipoCumulo(getRequestStringParameter(CAMPO_COD_TIPO_CUMULO));
		lCumMod.setDataRichiestaFascicolo(getRequestDateParameter(CAMPO_ANNO_DATA_RICHIESTA_FASCICOLO,
				CAMPO_MESE_DATA_RICHIESTA_FASCICOLO, CAMPO_GIORNO_DATA_RICHIESTA_FASCICOLO));
		lCumMod.setDataPervenimentoFascicolo(getRequestDateParameter(CAMPO_ANNO_DATA_PERVENIMENTO_FASCICOLO,
				CAMPO_MESE_DATA_PERVENIMENTO_FASCICOLO, CAMPO_GIORNO_DATA_PERVENIMENTO_FASCICOLO));
		lCumMod.setDataCumulo(getRequestDateParameter(CAMPO_ANNO_DATA_CUMULO, CAMPO_MESE_DATA_CUMULO,
				CAMPO_GIORNO_DATA_CUMULO));
		lCumMod.setCodMotivoSospensioneCumulo(getRequestStringParameter(CAMPO_COD_MOTIVO_SOSPENSIONE_CUMULO));
		lCumMod.setDataSospensioneCumulo(getRequestDateParameter(CAMPO_ANNO_DATA_SOSPENSIONE_CUMULO,
				CAMPO_MESE_DATA_SOSPENSIONE_CUMULO, CAMPO_GIORNO_DATA_SOSPENSIONE_CUMULO));
		// lCumMod.setCodTipoPenaDetentiva( getRequestStringParameter( CAMPO_COD_TIPO_PENA_DETENTIVA) );
		/*
		 * lCumMod.setNumAnniReclusione( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_RECLUSIONE) );
		 * lCumMod.setNumMesiReclusione( getRequestBigDecimalParameter( CAMPO_NUM_MESI_RECLUSIONE) );
		 * lCumMod.setNumGiorniReclusione( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_RECLUSIONE) );
		 * lCumMod.setImportoMulta( getRequestBigDecimalParameter( CAMPO_IMPORTO_MULTA) );
		 * lCumMod.setNumAnniArresto( getRequestBigDecimalParameter( CAMPO_NUM_ANNI_ARRESTO) );
		 * lCumMod.setNumMesiArresto( getRequestBigDecimalParameter( CAMPO_NUM_MESI_ARRESTO) );
		 * lCumMod.setNumGiorniArresto( getRequestBigDecimalParameter( CAMPO_NUM_GIORNI_ARRESTO) );
		 * lCumMod.setImportoAmmenda( getRequestBigDecimalParameter( CAMPO_IMPORTO_AMMENDA) );
		 */
		lCumMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		lCumMod.setCodOperatoreInserimento(getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO));
		lCumMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lCumMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));
		lCumMod.setCodOperatoreAggiornamento(getRequestStringParameter(CAMPO_COD_OPERATORE_AGGIORNAMENTO));
		lCumMod.setDataAggiornamento(getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		lCumMod.setCodUfficioAggiornamento(getRequestStringParameter(CAMPO_COD_UFFICIO_AGGIORNAMENTO));
		lCumMod.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP));

		ICumulo lCtrl = SIEPLookupRemote.getCumuloRemote();
		Vector lVect = lCtrl.ExRicercaCumulo(lCumMod);
		setRequestAttribute("cumulo", lVect);

		return PG_RICERCACUMULO;
	}

}