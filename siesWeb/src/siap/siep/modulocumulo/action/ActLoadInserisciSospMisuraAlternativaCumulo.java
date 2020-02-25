package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la load inserisci e modifica delle Concessioni Misure Alternative Cumulo
 *
 * @author Intersistemi S.p.A.
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciSospMisuraAlternativaCumulo extends ActionModuloCumulo
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

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		String lIdCompDaModificare = "";

		if ("I".equals(lModalita)) {
			// Inserimento
			siesLogger.debug("Sto In Inserimento");
		} else if ("M".equals(lModalita) || "NP".equals(lModalita)) {
			// Modifica
			BigDecimal lIdStat = getRequestBigDecimalParameter(
					ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);

			if ("M".equals(lModalita))
				lIdCompDaModificare = getRequestStringParameter(
						ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO);

			siesLogger.debug("Sto In modifica (" + lModalita + "), idStat = " + lIdStat + ", lIdComp = "
					+ lIdCompDaModificare);

			// Recupero i dati e li passo alla form
			IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

			lStato = lCtrlStato.ExRicercaStatoEsecTitoloCumulatoByIdFull(lIdStat);
			setRequestAttribute("aProvvedimento", lStato);
			setRequestAttribute("aIdComputo", "" + lIdCompDaModificare);
		} else {
			// Rilanciare Eccezione - Operazione non supportata
		}
		setRequestAttribute("modalita", lModalita);

		// ==========================================================================
		// Caricamento Dati delle Combo
		// ==========================================================================

		// Preparazione codice Motivo Provvedimento per Concessione Misura Alternativa cumulo.
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlt4("SOSPCUM");

		Vector<DecodificheModel> lDecodMotivoProvv = new Vector<>();
		lDecodMotivoProvv.add(new DecodificheModel("-", "-", "-", "", "", "", "", "", ""));
		lDecodMotivoProvv.addAll(lDecodifiche.ExRicercaDecodifiche(lModel));
		Option lOptionMotivoProvv = new Option(lDecodMotivoProvv);

		// Tipo Provvedimento
		Option lOptionTipoProvv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza());

		// Tipo Ufficio Emittente
		Option lOptionTUE = new Option(DecodificheManager.getInstance().getTipoUfficio(), "-");
		lOptionTUE.setFilter(new String[] { "TDS", "UDS", "UDSM", "TDSM", "-" });

		if ("M".equals(lModalita)) {
			ComputiCumuloModel lComputo = null;

			BigDecimal idComp = new BigDecimal(lIdCompDaModificare);

			Vector<ComputiCumuloModel> lListaComputi = lStato.getListaComputi();
			Iterator itxComputi = lListaComputi.iterator();
			while (itxComputi.hasNext()) {
				lComputo = (ComputiCumuloModel) itxComputi.next();
				if (lComputo.getIdComputiCumulo().compareTo(idComp) == 0) {
					break;
				}
			}

			if (lComputo.getCodUfficioEmittenteProvv() != null) {
				UfficioModel lUfficioEmittente = getUfficioByCodUfficio(
						lComputo.getCodUfficioEmittenteProvv());
				lOptionTUE.setSelected(lUfficioEmittente.getCodTipoUfficio());
			}
			if (lStato.getCodMotivo() != null)
				lOptionMotivoProvv.setSelected(lStato.getCodMotivo());
			if (lStato.getCodTipoProvvedimento() != null)
				lOptionTipoProvv.setSelected(lStato.getCodTipoProvvedimento());
		}

		setRequestAttribute("tipoUfficioEmittente", "" + lOptionTUE);
		setRequestAttribute("oggettoProvvedimento", "" + lOptionMotivoProvv);
		setRequestAttribute("tipoProvvedimento", "" + lOptionTipoProvv);

		// Imposta Modalità.
		setRequestAttribute("modalita", lModalita);

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCI_SOSP_MISURAALTERNATIVA_CUMULO;
	}

}