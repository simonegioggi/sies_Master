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
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la load inserisci e modifica delle Concessioni Misure Alternative Cumulo
 *
 * @author Intersistemi S.p.A.
 *
 */
public class ActLoadInserisciSospEsecuzionePenaCumulo extends ActionModuloCumulo
		implements ICostantiStatoEsecTitoloCumulato, ICostantiComputiCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		if (isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		TitoloCumulatoModel lTitoloCum = super.getDatiTitoloCumulato();

		StatoEsecTitoloCumulatoModel lStato = null;

		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		String lIdCompDaModificare = "";

		if ("I".equals(lModalita)) {
			// Inserimento
			siesLogger.debug("--XX-- Sto In Inserimento - id_Titolo = " + lTitoloCum.getIdTitoloCumulato());
			PenaResiduaModel lPenRes = new PenaResiduaModel();
			if (lTitoloCum.getProcedimentoCumulato() != null
					&& lTitoloCum.getProcedimentoCumulato().getIdProcedimentoCumulato() != null) {
				if (lTitoloCum.getProcedimentoCumulato().getIdFascicoloSiepOrigine() != null) {
					IPenaResidua lCtrlPR = SIEPLookupRemote.getPenaResiduaRemote();
					lPenRes = lCtrlPR.ExRicercaPenaResiduaUltimaByDate(
							lTitoloCum.getProcedimentoCumulato().getIdFascicoloSiepOrigine());
				}
			}
			setRequestAttribute("lPenaResidua", lPenRes);

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

		// Preparazione codice Motivo Provvedimento per SospensioneEsecuzione Pena cumulo.
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlt4("SOSPECUM");

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
		return PG_LOAD_INSERISCI_SOSP_ESECUZIONEPENA_CUMULO;
	}

}