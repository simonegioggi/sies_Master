package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la visualizzazione del Dettaglio del provvedimento di Scomputo Permessi (Titolo Cumulato)
 *
 */
public class ActDettaglioScomputoPermessiCumulo extends ActionModuloCumulo
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

		LibAnticipataCumuloModel LicenzaMod = null;
		if (lStato != null && lStato.getIdStatoEsecTitoloCumulato() != null
				&& lStato.getListaLiberazioniAnticipate() != null
				&& lStato.getListaLiberazioniAnticipate().size() > 0) {
			LicenzaMod = (LibAnticipataCumuloModel) lStato.getListaLiberazioniAnticipate().get(0);
		}

		setRequestAttribute("LicenzaPermesso", LicenzaMod);
		setRequestAttribute("Provvedimento", lStato);

		return PG_LOAD_DETTAGLIO_SCOMPUTO_PERMESSI;
	}

}