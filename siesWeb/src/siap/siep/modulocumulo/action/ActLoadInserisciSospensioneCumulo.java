package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la Load del form di Inserimento/Modifica dei provvedimenti di annotazione Sospensione
 * 
 * @author Intersistemi S.p.A.
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciSospensioneCumulo extends ActionModuloCumulo
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
		ComputiCumuloModel lComputo = null;

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

			lComputo = lStato.getListaComputi().elementAt(0);
			// Risalgo al Contenuto decisione per precaricarlo in combo
			String lCodContenutoDecisione = lComputo.getCodOggettoDecisione();
			setRequestAttribute("ContenutoDecisione", lCodContenutoDecisione);
		} else {
			// Rilanciare Eccezione - Operazione non supportata
		}

		// ==========================================================================
		// Caricamento Dati delle Combo
		// ==========================================================================
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice());
		// String desLuogoUfficio = "";
		lOption.setFilter(new String[] { "CAP", "CAS", "CASAP", "CSS", "GIP", "GIPM", "GUPM", "GUP", "TRIBSD",
				"CAPSM", "DIB", "DIBM" });
		setRequestAttribute("autoritaemittente", "" + lOption);

		// TIPO REGISTRO ORDINANZA
		Collection lCollTipoReg = DecodificheManager.getInstance().getTipoRegistroOrdinanza();
		Iterator iter = lCollTipoReg.iterator();
		lCollTipoReg = new ArrayList();
		while (iter.hasNext()) {
			DecodificheModel lDecMod = (DecodificheModel) iter.next();
			if (!lDecMod.getCodiceAlternativo().equals("0001"))// SIUS
				lCollTipoReg.add(lDecMod);
		}
		setRequestAttribute("tiporegistroordinanza", lCollTipoReg);

		// TIPO PROVVEDIMENTO
		lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOption.setFilter(new String[] { "02", "03" }); // solo DECRETO o ORDINANZA
		if (lStato != null && lStato.getCodTipoProvvedimento() != null) {
			lOption.setSelected(lStato.getCodTipoProvvedimento());
		}
		setRequestAttribute("tipoprovvedimento", "" + lOption);

		// AUTORITA' EMITTENTE (TIPO_UFFICIO_SOSP)
		ArrayList lListAutoritaSospensione = (ArrayList) DecodificheManager.getInstance()
				.getListaAutoritaSospensione();
		setRequestAttribute("autoritaemittente", lListAutoritaSospensione);

		// CONTENUTO DECISIONE
		ArrayList lContenutoDecisione = (ArrayList) DecodificheManager.getInstance()
				.getListaOggettiSospensione();
		setRequestAttribute("contenutodecisione", lContenutoDecisione);

		// OGGETTO DECISIONE
		ArrayList lMotiviProvvedimento = (ArrayList) DecodificheManager.getInstance()
				.getListaMotiviProvvedimentoSospensione();
		setRequestAttribute("oggettodecisione", lMotiviProvvedimento);

		// TIPOLOGIA DECISIONE
		ArrayList lEsitiTenore = (ArrayList) DecodificheManager.getInstance()
				.getListaEsitiTenoreSospensione();
		setRequestAttribute("tipologiadecisione", lEsitiTenore);

		// Imposta Modalità.
		setRequestAttribute("modalita", lModalita);

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCI_SOSPENSIONE_CUMULO;
	}

}