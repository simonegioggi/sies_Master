package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils;
import siap.siep.util.SIEPLookupRemote;

/**
 * Funzione che effettua la cancellazione dei dati di un provvedimento generico dello stato di esecuzione
 * 
 * !!! Attenzione Modificare il nome delle classe in ActProvvStatoEsec
 * 
 * @author
 *
 */
public class ActCancellaProvvStatEsec extends ActionModuloCumulo
		implements ICostantiStatoEsecuzioneCumulo, ICostantiStatoEsecTitoloCumulato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		/* IstruttoriaCumuloModel lIstruttoriaModel = */super.getDatiIstruttoria();
		/* TitoloCumulatoModel lTitoloModel = */super.getDatiTitoloCumulato();

		// ==========================================================================
		// Effettua la cancellazione
		// ==========================================================================
		String lAzione = getRequestStringParameter("azione");
		BigDecimal lIdStatoEsec = getRequestBigDecimalParameter(
				ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);

		siesLogger.debug("lIdStatoEsec = " + lIdStatoEsec);

		IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
		StatoEsecTitoloCumulatoModel lStatoModel = lCtrlStato
				.ExRicercaStatoEsecTitoloCumulatoById(lIdStatoEsec);

		String lPage = null;
		if (AZIONE_DETTAGLIO.equals(lAzione))
			lPage = exDettaglioProvvedimento(lStatoModel);
		else if (AZIONE_MODIFICA.equals(lAzione))
			lPage = exModificaProvvedimento(lStatoModel);
		else if (AZIONE_CANCELLA.equals(lAzione))
			lPage = exCancellaProvvedimento(lStatoModel);

		siesLogger.debug("lPage = " + lPage);

		return lPage;
	}

	/**
	 * 
	 * @param aStatoEsecModel
	 * @return
	 * @throws F3BException
	 */
	private String exDettaglioProvvedimento(StatoEsecTitoloCumulatoModel aStatoEsecModel)
			throws F3BException {
		siesLogger.debug("aStatoEsecModel.getCodMotivo() = " + aStatoEsecModel.getCodMotivo());
		String lAzioneDettaglio = StatoEsecuzioneCumuloUtils.getActionDettaglio(aStatoEsecModel);
		siesLogger.debug("lAzioneDettaglio = " + lAzioneDettaglio);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=" + lAzioneDettaglio + "&"
				+ ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
				+ aStatoEsecModel.getIstrIdIstruttoriaCumulo() + "&"
				+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
				+ aStatoEsecModel.getTitIdTitoloCumulato() + "&"
				+ ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="
				+ aStatoEsecModel.getIdStatoEsecTitoloCumulato();

		siesLogger.debug("lPage = " + lPage);
		return lPage;
	}

	/**
	 * 
	 * @return
	 */
	private String exModificaProvvedimento(StatoEsecTitoloCumulatoModel aStatoEsecModel) {
		// Per ora non implementato. Da verificare
		return "";
	}

	/**
	 * 
	 * @param aStatoEsecModel
	 * @return
	 * @throws F3BException
	 */
	private String exCancellaProvvedimento(StatoEsecTitoloCumulatoModel aStatoEsecModel) throws F3BException {

		IStatoEsecTitoloCumulato lCtrlSET = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
		lCtrlSET.ExCancellaStatoEsecTitoloCumulatoById(aStatoEsecModel.getIdStatoEsecTitoloCumulato(), null);

		// ==========================================================================
		// Ritorno sulla griglia dati analitici
		// ==========================================================================
		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActLoadGrigliaDatiAnalitici" + "&"
				+ ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
				+ aStatoEsecModel.getIstrIdIstruttoriaCumulo() + "&"
				+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
				+ aStatoEsecModel.getTitIdTitoloCumulato();

		return lPage;
	}

}