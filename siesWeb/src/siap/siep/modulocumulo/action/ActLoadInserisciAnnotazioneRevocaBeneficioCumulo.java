package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la Load del form di Inserimento/Modifica dei provvedimenti di annotazione Revoca Sopsensione
 * Condizionale / Non menzione
 *
 * @author Intersistemi S.p.A.
 *
 */
public class ActLoadInserisciAnnotazioneRevocaBeneficioCumulo extends ActionModuloCumulo
		implements ICostantiStatoEsecTitoloCumulato, ICostantiComputiCumulo {

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

		StatoEsecTitoloCumulatoModel lStato = null;
		// String lTipoUff = "";

		String lModalita = "I"; // default inserimento
		String ltipoBeneficiodaRevocare = "";

		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		if ("I".equals(lModalita)) {
			// Inserimento
			siesLogger.debug("Sto In Inserimento");

			ltipoBeneficiodaRevocare = getRequestStringParameter("tipoSospensione");
			setRequestAttribute("ltipoSospensione", ltipoBeneficiodaRevocare);
		} else if ("M".equals(lModalita)) {
			// Modifica
			BigDecimal lIdStat = getRequestBigDecimalParameter(
					ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);

			// siesLogger.debug("Sto In modifica ("+lModalita+"), idStat = "+lIdStat);

			// Recupero i dati e li passo alla form
			IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
			lStato = lCtrlStato.ExRicercaStatoEsecTitoloCumulatoByIdFull(lIdStat);
			setRequestAttribute("aProvvedimento", lStato);
			siesLogger.debug("Sto In Modifica StatoEsec trovato = " + lStato);

		} else {
			// Rilanciare Eccezione - Operazione non supportata
		}

		// ==========================================================================
		// Caricamento Dati delle Combo
		// ==========================================================================
		// Tipo Provvedimento
		Option lOptionTipoProvv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza());
		if (lStato != null && lStato.getIdStatoEsecTitoloCumulato() != null) {
			if (lStato.getCodTipoProvvedimento() != null && (lStato.getCodTipoProvvedimento().equals("02")
					|| lStato.getCodTipoProvvedimento().equals("03"))) {

				lOptionTipoProvv.setSelected(lStato.getCodTipoProvvedimento());
			}
		}
		setRequestAttribute("tipoProvvedimento", "" + lOptionTipoProvv);

		// Tipo Ufficio Emittente
		Option lOptionUff = new Option(DecodificheManager.getInstance().getTipoUfficioGE(), "-");
		if (lStato != null && lStato.getIdStatoEsecTitoloCumulato() != null) {
			if (lStato.getCodUfficioEmittente() != null && !"".equals(lStato.getCodUfficioEmittente())) {
				UfficioModel lUff = getUfficioByCodUfficio(lStato.getCodUfficioEmittente());
				lOptionUff.setSelected(lUff.getCodTipoUfficio());
			}
		}
		setRequestAttribute("tipoUfficioEmittente", "" + lOptionUff);

		// Imposta Modalità.
		setRequestAttribute("modalita", lModalita);

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCI_REVOCA_BENEFICIO_CUMULO;
	}

}