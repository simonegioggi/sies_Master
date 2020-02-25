package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per la Load del form di Inserimento/Modifica dei provvedimenti di Annotazione Rideterminazione Pena
 * PM - Altro
 *
 * @author v.ascione
 *
 */
public class ActLoadInserisciRidetPenaPMAltroCumulo extends ActionModuloCumulo
		implements ICostantiStatoEsecTitoloCumulato, ICostantiComputiCumulo {

	// Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
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

		// ==========================================================================
		// Caricamento Dati delle Combo
		// ==========================================================================

		// Combo codice motivo
		Option lOption = new Option(DecodificheManager.getInstance().getRideterminazionePenaAltro());
		// if ( "M".equals(lModalita) )
		// lOption.setSelected (?);
		// else
		// lOption.setSelected ("?"); //Indulto
		setRequestAttribute("oggetto", "" + lOption);

		// ==========================================================================
		// Previsti 2 casi: D'ufficio - In esecuzione di provvedimento altro ufficio
		// Se 'In esecuzione di provvedimento altro ufficio' previsti 3 casi
		// Su provvedimento altro ufficio 3 casi: Altra Autorità
		// Giudice Esecuzione
		// Giudice Sorveglianza
		// Il contenuto della combo oggetto varia in funzione dell'ufficio, quindi
		// sono presenti in tutto 4 casi per la combo oggetto.
		// Inoltre cambia anche il contenuto della combo Autorità emittente

		if (!isRequestParameterNullObj("codMotivo")) {
			String codMotivo = getRequestStringParameter("codMotivo");
			siesLogger.debug("codMotivo = " + codMotivo);
			// Provengo dalle Decisioni della Sorveglianza. In questo caso dovo
			// caricare una form semplificata. Il codice motivo è già caricato.
		}

		// =======================================================================
		// Carico la combo con i soli codici motivo previsti per i provvedimenti
		// di questo ufficio
		// =======================================================================
		String lOggettoDufficio = "-";
		if ("M".equals(lModalita)
				&& StatoEsecuzioneCumuloUtils.isRidPenaPMAltroDufficio(lStato.getCodMotivo()))
			lOggettoDufficio = lStato.getCodMotivo();

		Option lOptionDufficio = new Option(
				DecodificheManager.getInstance().getRideterminazionePenaAltroDufficio(), lOggettoDufficio);

		setRequestAttribute("oggettoDufficio", "" + lOptionDufficio);

		// =======================================================================
		// Carico la combo con i soli codici motivo previsti per i provvedimenti
		// di altro ufficio
		// =======================================================================
		Option lOptionAltroUfficio = new Option(
				DecodificheManager.getInstance().getRideterminazionePenaAltroAUfficio());
		setRequestAttribute("oggettoAltroUfficio", "" + lOptionAltroUfficio);

		// In caso di modifica e di provv. Altre Autorità,
		// si impostano gli indici delle 2 combo : OGGETTO e ALTRA_AUTORITA
		Option lOptionSorv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza(), "-");
		setRequestAttribute("tipoprovvedimento", "" + lOptionSorv);

		if ("M".equals(lModalita) && lStato.getListaComputi().get(0).getCodTipoProvv() != null) {
			String lTipoSorv = "-";
			lTipoSorv = lStato.getListaComputi().get(0).getCodTipoProvv();
			lOptionSorv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza(), lTipoSorv);
			setRequestAttribute("tipoprovvedimento", "" + lOptionSorv);

			// Impostazione indice di combo ALTRA_AUTORITA
			UfficioModel lUfficio = this
					.getUfficioByCodUfficio(lStato.getListaComputi().get(0).getCodUfficioEmittenteProvv());
			Collection lAltraAutorita = null;
			if (StatoEsecuzioneCumuloUtils.isRidPenaPMAltroAltAut(lStato.getCodMotivo()))
				lAltraAutorita = DecodificheManager.getInstance().getAutoritaRdpAltro();
			else if (StatoEsecuzioneCumuloUtils.isRidPenaPMAltroGE(lStato.getCodMotivo()))
				lAltraAutorita = DecodificheManager.getInstance().getAutoritaRdpGE();
			else if (StatoEsecuzioneCumuloUtils.isRidPenaPMAltroSorv(lStato.getCodMotivo()))
				lAltraAutorita = DecodificheManager.getInstance().getAutoritaRdpSorv();

			Iterator iter = lAltraAutorita.iterator();
			int j = 0;
			while (iter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) iter.next();
				if (lDecMod.getCodiceAlternativo().equals(lUfficio.getCodTipoUfficio()))
					break;
				j++;
			}
			setRequestAttribute("indAltraAutorita", "" + j);

			// Impostazione indice di combo OGGETTO_PROVVEDIMENTO
			Collection lOggettoProvv = null;
			if (StatoEsecuzioneCumuloUtils.isRidPenaPMAltroAltAut(lStato.getCodMotivo()))
				lOggettoProvv = DecodificheManager.getInstance().getRideterminazionePenaAltroAUfficio();
			else if (StatoEsecuzioneCumuloUtils.isRidPenaPMAltroGE(lStato.getCodMotivo()))
				lOggettoProvv = DecodificheManager.getInstance().getRideterminazionePenaAltroGE();
			else if (StatoEsecuzioneCumuloUtils.isRidPenaPMAltroSorv(lStato.getCodMotivo()))
				lOggettoProvv = DecodificheManager.getInstance().getRideterminazionePenaAltroSORV();

			iter = lOggettoProvv.iterator();
			int k = 0;
			while (iter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) iter.next();
				if (lDecMod.getCode().equals(lStato.getCodMotivo()))
					break;
				k++;
			}
			setRequestAttribute("indOggettoProvv", "" + k);
		}

		// ==========================================================================
		// Vettore con i tipo emittenti 'Provvedimento Altra Autorità'
		// ==========================================================================
		Collection lEmessoDa = new Vector();
		lEmessoDa.add(new DecodificheModel("0000", "-", "", "", "", "", "", "", ""));
		lEmessoDa.add(new DecodificheModel("0001", "Altra Autorità", "", "", "", "", "", "", ""));
		lEmessoDa.add(new DecodificheModel("0002", "Giudice Esecuzione", "", "", "", "", "", "", ""));
		lEmessoDa.add(new DecodificheModel("0003", "Giudice Sorveglianza", "", "", "", "", "", "", ""));
		setRequestAttribute("emessoDa", lEmessoDa); // Provvedimento emesso da

		// ==========================================================================
		// Carico le 3 Collection contenenti i codici motivo per i per i tre casi
		// Altro Ufficio
		// ==========================================================================
		// ==========================================================================
		// Autorità da caricare dentro la combo. In pratica l'oggetto 'autorita'
		// passato alla jsp è un ArrayList di tante collection quante sono gli
		// oggetti della collection emessoDa. Il collegamento tre le due collection
		// è puramente posizionale. Se si seleziona il primo elemento della 'emessoDa',
		// le tipologie di Uffici vengono recuperate dalla prima collection dell'Array
		// 'autorita'. Stesso per i codici motivo (oggettoProvvedimento). Quindi
		// vanno caricate nell'ordine giusto
		// ==========================================================================
		ArrayList lAutorita = new ArrayList();
		Collection lAutorVuota = new Vector();
		lAutorVuota.add(new DecodificheModel("-", "-               ", "", "", "", "", "", "", ""));
		lAutorita.add(lAutorVuota);
		lAutorita.add(DecodificheManager.getInstance().getAutoritaRdpAltro());
		lAutorita.add(DecodificheManager.getInstance().getAutoritaRdpGE());
		lAutorita.add(DecodificheManager.getInstance().getAutoritaRdpSorv());
		setRequestAttribute("autorita", lAutorita);

		// ==========================================================================
		// Carico le 3 Collection contenenti i codici motivo per i per i tre casi
		// Altro Ufficio
		// ==========================================================================
		ArrayList lOggetto = new ArrayList();
		Collection lOggettoVuota = new Vector();
		lOggettoVuota.add(new DecodificheModel("-", "-               ", "", "", "", "", "", "", ""));
		lOggetto.add(lOggettoVuota);
		lOggetto.add(DecodificheManager.getInstance().getRideterminazionePenaAltroAUfficio());
		lOggetto.add(DecodificheManager.getInstance().getRideterminazionePenaAltroGE());
		lOggetto.add(DecodificheManager.getInstance().getRideterminazionePenaAltroSORV());
		setRequestAttribute("oggettoProvvedimento", lOggetto);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		// Autorità esterna E
		Option lOptionAutoritaE = null;
		lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaE", "" + lOptionAutoritaE);

		// Autorità esterna altra
		Option lOptionAutoritaAltra = null;
		lOptionAutoritaAltra = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaAltra", "" + lOptionAutoritaAltra);

		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice());
		String desLuogoUfficio = "";
		lOption.setFilter(new String[] { "CAP", "CAS", "CASAP", "CSS", "GIP", "GIPM", "GUPM", "GUP", "TRIBSD",
				"CAPSM", "DIB", "DIBM" });

		ComputiCumuloModel lComputo = null;
		if ("M".equals(lModalita)) {
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
				lOption.setSelected(lUfficioEmittente.getCodTipoUfficio());
				desLuogoUfficio = lUfficioEmittente.getDescrComune();
			}
		}

		setRequestAttribute("tipoUfficioEmittente", "" + lOption);
		setRequestAttribute("luogoUfficioEmittente", "" + desLuogoUfficio);

		// Imposta Modalità.
		setRequestAttribute("modalita", lModalita);

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCI_RIDET_PENA_PM_ALTRO;
	}

}