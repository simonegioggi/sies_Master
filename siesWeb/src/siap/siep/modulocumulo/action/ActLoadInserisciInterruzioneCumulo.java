package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la Load del form di Inserimento/Modifica dei provvedimenti di annotazione Interruzione
 *
 * @author Intersistemi S.p.A.
 *
 */
public class ActLoadInserisciInterruzioneCumulo extends ActionModuloCumulo
		implements ICostantiStatoEsecTitoloCumulato, ICostantiComputiCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("unchecked")
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

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		if ("I".equals(lModalita)) {
			// Inserimento
			siesLogger.debug("Sto In Inserimento");
		} else if ("M".equals(lModalita)) {
			// Modifica
			BigDecimal lIdStat = getRequestBigDecimalParameter(
					ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);

			siesLogger.debug("Sto In modifica (" + lModalita + "), idStat = " + lIdStat);

			// Recupero i dati e li passo alla form
			IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

			lStato = lCtrlStato.ExRicercaStatoEsecTitoloCumulatoByIdFull(lIdStat);
			setRequestAttribute("aProvvedimento", lStato);
		} else {
			// Rilanciare Eccezione - Operazione non supportata
		}

		// ==========================================================================
		// Caricamento Dati delle Combo
		// ==========================================================================
		Collection<DecodificheModel> lMotiviInterruzione = DecodificheManager.getInstance()
				.getMotivoInterruzione();
		ArrayList<DecodificheModel> lMotiviInterruzioneFiltro = new ArrayList<>();
		for (DecodificheModel lDecode : lMotiviInterruzione) {
			if ("0266".equals(lDecode.getCode()) || "0366".equals(lDecode.getCode())) {
				continue;
			} else {
				lMotiviInterruzioneFiltro.add(lDecode);
			}
		}
		// Option lOptionMotivoInterr = new Option(DecodificheManager.getInstance().getMotivoInterruzione());
		Option lOptionMotivoInterr = new Option(lMotiviInterruzioneFiltro);

		// Combo Motivo Interruzione
		if (lStato != null && lStato.getCodMotivo() != null)
			lOptionMotivoInterr.setSelected(lStato.getCodMotivo());

		setRequestAttribute("motivointerruzione", "" + lOptionMotivoInterr);

		// Imposta Modalità.
		setRequestAttribute("modalita", lModalita);

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCI_INTERRUZIONE_CUMULO;
	}

}