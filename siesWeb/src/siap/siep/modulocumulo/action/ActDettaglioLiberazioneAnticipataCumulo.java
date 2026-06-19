package siap.siep.modulocumulo.action;

import org.apache.log4j.Logger;
import java.math.BigDecimal;
import java.util.Iterator;

import f3b.util.F3BException;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la visualizzazione del Dettaglio del provvedimento di Concessione Liberazione Anticipata (Titolo
 * Cumulato) 0076 - L.A. su provvedimento del TDS 2130 - L.A. su provvedimento di UDS / MDS
 * 
 * @author
 *
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioLiberazioneAnticipataCumulo extends ActionModuloCumulo
		implements ICostantiLibAnticipataCumulo, ICostantiStatoEsecTitoloCumulato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		BigDecimal idStatoEsec = null;
		if (!isRequestParameterNullObj(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO))
			idStatoEsec = getRequestBigDecimalParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);

		IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
		// siesLogger.debug("--XX-- ActDettaglioLiberazioneAnticipataCumulo - id_Stato_esec = "+idStatoEsec);
		StatoEsecTitoloCumulatoModel lStato = lCtrlStato
				.ExRicercaStatoEsecTitoloCumulatoByIdFull(idStatoEsec);

		if (lStato.getListaLiberazioniAnticipate() != null
				&& lStato.getListaLiberazioniAnticipate().size() > 0) {
			// siesLogger.debug("--XX-- ActDettaglioLACumulo - ListaLiberazioni in numero di
			// "+lStato.getListaLiberazioniAnticipate().size());

			Iterator ItLib = lStato.getListaLiberazioniAnticipate().iterator();
			// int k = 0;
			while (ItLib.hasNext()) {
				// k++;
				LibAnticipataCumuloModel LAModel = (LibAnticipataCumuloModel) ItLib.next();
				if (LAModel.getListaPeriodiLibAnticipate() != null
						&& LAModel.getListaPeriodiLibAnticipate().size() > 0) {
					// siesLogger.debug("--XX-- ActDettaglioLACumulo - L.A. N."+k+" --> ha periodi in numero
					// di "+LAModel.getListaPeriodiLibAnticipate().size());
				}
			}

		}

		setRequestAttribute("Provvedimento", lStato);

		return PG_LOAD_DETTAGLIO_LIB_ANTICIPATA_CUMULO;
	}

}
