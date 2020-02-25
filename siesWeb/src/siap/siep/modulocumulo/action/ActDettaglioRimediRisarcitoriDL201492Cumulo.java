package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la visualizzazione del Dettaglio del provvedimento di Rimedi Risarcitori DL 2014/92 (Titolo
 * Cumulato)
 *
 */
public class ActDettaglioRimediRisarcitoriDL201492Cumulo extends ActionModuloCumulo
		implements ICostantiLibAnticipataCumulo, ICostantiStatoEsecTitoloCumulato {

	public String processRequest() throws F3BException {

		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		BigDecimal idStatoEsec = null;
		if (!isRequestParameterNullObj(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO))
			idStatoEsec = getRequestBigDecimalParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);

		IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
		// LogF3B.getLogger().debug("--XX-- ActDettaglioLiberazioneAnticipataCumulo - id_Stato_esec =
		// "+idStatoEsec);
		StatoEsecTitoloCumulatoModel lStato = lCtrlStato
				.ExRicercaStatoEsecTitoloCumulatoByIdFull(idStatoEsec);

		// Solo X Log
		/*
		 * if(lStato.getListaLiberazioniAnticipate()!=null && lStato.getListaLiberazioniAnticipate().size()>0)
		 * { //LogF3B.getLogger().debug("--XX-- ActDettaglioLACumulo - ListaLiberazioni in numero di "+lStato.
		 * getListaLiberazioniAnticipate().size());
		 * 
		 * Iterator ItLib = lStato.getListaLiberazioniAnticipate().iterator(); int k = 0;
		 * while(ItLib.hasNext()) { k++; LibAnticipataCumuloModel LAModel =
		 * (LibAnticipataCumuloModel)ItLib.next(); if(LAModel.getListaPeriodiLibAnticipate()!=null &&
		 * LAModel.getListaPeriodiLibAnticipate().size()>0) {
		 * //LogF3B.getLogger().debug("--XX-- ActDettaglioLACumulo - L.A. N."
		 * +k+" --> ha periodi in numero di "+LAModel.getListaPeriodiLibAnticipate().size()); } }
		 * 
		 * }
		 */
		setRequestAttribute("Provvedimento", lStato);

		return PG_LOAD_DETTAGLIO_RIMEDI_RISARCITORI_CUMULO;
	}

}