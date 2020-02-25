package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la load dell'inserimento del provvedimento di fungibilità (Titolo Cumulato) All'interno della
 * form c'è uma comboBox per la scelta del tipo di fungibilità: 0212 - Fungibilità per computo Misura
 * Cautelare Altro Reato art. 657 c.p.p 0213 - Fungibilità per computo Pena Detentiva Espiata per Altro Reato
 * art. 657 c.p.p
 *
 * @author
 *
 */
public class ActLoadInserisciFungibilitaCumulo extends ActionModuloCumulo
		implements ICostantiPresoffertoCumulo {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche di precaricare tutti i dati
	 * da visualizzare i tale pagina (es: combo)
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();
		// BigDecimal lIdTitolo = getRequestBigDecimalParameter(
		// ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

		String lModalita = "I"; // default inserimento

		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		siesLogger.debug("Sto In modalita " + lModalita);

		BigDecimal IdStatoEsec = null;
		StatoEsecTitoloCumulatoModel Stato = null;

		BigDecimal lIdCompDaModificare = null;
		ComputiCumuloModel lComputi = null;
		if ("M".equals(lModalita) || "C".equals(lModalita)) {
			IdStatoEsec = getRequestBigDecimalParameter(
					ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
			IStatoEsecTitoloCumulato CtrlS = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
			Stato = CtrlS.ExRicercaStatoEsecTitoloCumulatoById(IdStatoEsec);

			siesLogger
					.debug("Sto In modifica/cancellazione (" + lModalita + "), IdStatoEsec = " + IdStatoEsec);

			if ("M".equals(lModalita)) {
				lIdCompDaModificare = getRequestBigDecimalParameter(
						ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO);
				siesLogger.debug("Sto In modifica (" + lModalita + "), IdStatoEsec = " + IdStatoEsec
						+ ", lIdComp = " + lIdCompDaModificare);
				IComputiCumulo CtrlC = SIEPLookupRemote.getComputiCumuloRemote();
				lComputi = CtrlC.ExRicercaComputiCumuloById(lIdCompDaModificare);

				if (lComputi.getIstDetIdIstitutoDetenzione() != null) {
					IIstitutoDetenzione CtlrI = SIEPLookupRemote.getIstitutoDetenzioneRemote();
					IstitutoDetenzioneModel lIstMod = CtlrI
							.ExRicercaIstitutoDetenzioneByKey(lComputi.getIstDetIdIstitutoDetenzione());
					if (lIstMod != null && lIstMod.getIdIstitutoDetenzione() != null)
						lComputi.setIstitutoDetenzione(lIstMod);
				}
			}
		}

		if (lModalita.equals("NP")) {
			IdStatoEsec = getRequestBigDecimalParameter(
					ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);
			siesLogger.debug("Sto In Inserisci Periodo (" + lModalita + "), IdStatoEsec = " + IdStatoEsec);
			IStatoEsecTitoloCumulato CtrlS = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
			Stato = CtrlS.ExRicercaStatoEsecTitoloCumulatoById(IdStatoEsec);
		}

		siesLogger.debug("Sto 2 In modalita " + lModalita);
		// ----------------------------------------------------
		// ==========================================================================
		// Caricamento Dati delle Combo
		// ==========================================================================

		// Combo Autorita Emittente
		Option lOptionAutEmi = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		if (lComputi != null && lComputi.getIdComputiCumulo() != null
				&& lComputi.getCodTipoAutoritaRege() != null) {
			lOptionAutEmi.setSelected(lComputi.getCodTipoAutoritaRege());
		}
		setRequestAttribute("autoritaEmi", "" + lOptionAutEmi);

		// ---------------------------------------------------------------------------------------------------------------------
		// Tipo Misura - Detentiva/NON Detentiva
		Option lOptionDetentive = new Option(StatoEsecuzioneCumuloUtils.getCodiciMisureDetentive());
		if (lComputi != null && lComputi.getIdComputiCumulo() != null
				&& lComputi.getCodTipoMisura() != null) {
			lOptionDetentive.setSelected(lComputi.getCodTipoMisura());
		}
		setRequestAttribute("tipoMisuraDetentive", "" + lOptionDetentive);

		Option lOptionNonDetentive = new Option(StatoEsecuzioneCumuloUtils.getCodiciMisureNonDetentive());
		if (lComputi != null && lComputi.getIdComputiCumulo() != null
				&& lComputi.getCodTipoMisura() != null) {
			lOptionNonDetentive.setSelected(lComputi.getCodTipoMisura());
		}
		setRequestAttribute("tipoMisuraNonDetentive", "" + lOptionNonDetentive);
		// ----------------------------------------------------------------------------------------------------------------------------

		// ufficio Esecuzione emittente
		String[] aFiltroUffici = { "CAP", "CAS", "CASAP", "GIP", "DIB", "TRIBSD", "CAPSM", "DIBM", "GIPM",
				"GUPM" };
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), true);
		lOption.setValueBlankItem("-");
		lOption.setFilter(aFiltroUffici);

		if (Stato != null && Stato.getIdStatoEsecTitoloCumulato() != null
				&& Stato.getCodTipoUfficioAltro() != null)
		// if(lComputi!=null && lComputi.getIdComputiCumulo()!=null &&
		// lComputi.getCodUfficioEmittenteProvv()!=null)
		{
			// UfficioModel
			// lUfficio=(UfficioModel)getUfficioByCodUfficio(lComputi.getCodUfficioEmittenteProvv());
			// lOption.setSelected(lUfficio.getCodTipoUfficio());

			lOption.setSelected(Stato.getCodTipoUfficioAltro());
		}
		setRequestAttribute("UfficioEsecEmittente", "" + lOption);

		// Autorità Emittente sentenza relativa al procedimento SIEP
		Option lOptionSIEP = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		if (lComputi != null && lComputi.getIdComputiCumulo() != null
				&& lComputi.getCodTipoAutoritaEmittente() != null) {
			lOptionSIEP.setSelected(lComputi.getCodTipoAutoritaEmittente());
		}
		setRequestAttribute("autoritaEmiSIEP", "" + lOptionSIEP);

		// Autorita Emittente - Ufficio PM
		String[] aFiltroUfficiPM = { "PM", "PMM", "PGCAP" };
		Option lOptionPM = new Option(DecodificheManager.getInstance().getTipoUfficio(), true);
		lOptionPM.setValueBlankItem("-");
		lOptionPM.setFilter(aFiltroUfficiPM);
		if (lComputi != null && lComputi.getIdComputiCumulo() != null
				&& lComputi.getCodTipoUfficioPM() != null) {
			lOptionPM.setSelected(lComputi.getCodTipoUfficioPM());
		}
		setRequestAttribute("UfficioPM", "" + lOptionPM);

		// ---------------------------------------------------------------------------------------------------
		// causale Computi
		Option lCompMCAR = new Option(DecodificheManager.getInstance().getTipoCausaleComputoMCAltroTitolo(),
				"-");
		if (lComputi != null && lComputi.getIdComputiCumulo() != null
				&& lComputi.getCodCausaleComputo() != null) {
			lCompMCAR.setSelected(lComputi.getCodCausaleComputo());
		}
		setRequestAttribute("ComputiMCAR", "" + lCompMCAR);

		//
		Option lCausaCompPDAR = new Option(
				DecodificheManager.getInstance().getTipoCausaleComputoMCSenzaTitolo(), "-");
		if (lComputi != null && lComputi.getIdComputiCumulo() != null
				&& lComputi.getCodCausaleComputo() != null) {
			lCausaCompPDAR.setSelected(lComputi.getCodCausaleComputo());
		}
		setRequestAttribute("ComputiPDAR", "" + lCausaCompPDAR);
		// ------------------------------------------------------------------------------------------------------------

		// Imposta Modalità.
		setRequestAttribute("modalita", lModalita);

		setRequestAttribute("StatoEsecTitoloCum", Stato);
		setRequestAttribute("ComputoCum", lComputi);

		siesLogger.debug("Sto In 3 modalita " + lModalita);

		// Restituisce la pagina di Inserimento dei Dati
		String lpage = "";

		if (lModalita.equals("I") || lModalita.equals("M")) {
			lpage = PG_LOAD_INSERISCI_FUNGIBILITA_CUMULO;
		} else if (lModalita.equals("NP")) {
			lpage = PG_LOAD_INSERISCI_PERIODO_FUNGIBILITA_CUMULO;
		}

		return lpage;
	}

}