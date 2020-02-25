package siap.siep.modulocumulo.action;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.util.CalendarUtil;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.calcolopena.action.ICostantiCalcoloPena;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.controller.IComputiCumulo;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.model.ComputiCumuloModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action che effettua il calcolo pena se non già presente e visualizza il dettaglio
 *
 * @author d.fiorletta
 *
 */
@SuppressWarnings("unchecked")
public class ActDettaglioPenaCumulo extends ActionModuloCumulo implements ICostantiModuloCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		IstruttoriaCumuloModel lIstruttoriaModel = super.getDatiIstruttoria();
		DatiFinaliCumuloAggregatoModel lDatiAggregati = super.getDatiFinaliCumuloAggregato();

		if (!lIstruttoriaModel.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA)) {
			// Se l'istruttoria non è in stato APERTA non rieffettuo i calcoli, ma
			// visualizzo solo il dettaglio se presente
			if (lDatiAggregati.getPenaResiduaCumulo() == null)
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessuna pena ricalcolata presente.");
		}

		// ==========================================================================
		// Recupera i dati da PenaRideterminata
		// ==========================================================================
		PenaRideterminataCumuloModel lPenaRideterminata = lDatiAggregati.getPenaRideterminataCumulo();
		if (lPenaRideterminata == null) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Rideterminata non Valorizzata. Impossibile procedere al calcolo pena.");
		}

		// ==========================================================================
		// Verifico se stata indicata la posizione giuridica necessaria per effettuare
		// i calcoli
		// ==========================================================================
		if (lDatiAggregati.getPosizioneGiuridicaCumulo() == null) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Posizione giuridica non ancora definita. Impossibile procedere al cacolo pena");
		}

		// ==========================================================================
		// Verifico se il primo calcolo è già stato effettuato, in questo caso
		// rieffettuo i calcoli e confronto se presenti discrepanze
		// visualizzo il dettaglio.
		// FIXME da completare

		// Recupero le richieste con anticipazione
		Vector<ComputiCumuloModel> lElencoComputi = new Vector<>();
		IComputiCumulo lCtrlComputi = SIEPLookupRemote.getComputiCumuloRemote();
		lElencoComputi = lCtrlComputi.ExRicercaComputiCumuloByIdIstruttoria(
				lIstruttoriaModel.getIdIstruttoriaCumulo(),
				lDatiAggregati.getDatiFinaliCumulo().getIdDatiFinaliCumulo());

		// ==========================================================================
		// Inizializzo e carico il lPenaReasidua
		// Carico il CalcoloPenaModel con i dati recuperati per poter procedere
		// al calcolo pena
		// ==========================================================================
		CalcoloPenaModel lCalcoloPenaModel = new CalcoloPenaModel();

		lCalcoloPenaModel.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_MANUALE);
		PenaResiduaModel lPenaResiduaEquivalente = getPenaResidua(lPenaRideterminata);
		lCalcoloPenaModel.setPenaResiduaManuale(lPenaResiduaEquivalente);

		// Aggiungo i giorni di LA
		lCalcoloPenaModel.setLibAnticipate(getLiberazioniAnticipate(lPenaRideterminata));

		// Carico i computi e calcolo i parziali da visualizzare in form
		CalendarModel lComputiConcessiReclusione = new CalendarModel();
		CalendarModel lComputiConcessiArresto = new CalendarModel();
		CalendarModel lComputiRevocatiReclusione = new CalendarModel();
		CalendarModel lComputiRevocatiArresto = new CalendarModel();

		CalendarUtil lCalUtil = new CalendarUtil();

		Iterator<ComputiCumuloModel> lIterComputi = lElencoComputi.iterator();
		while (lIterComputi.hasNext()) {
			ComputiCumuloModel lComputo = lIterComputi.next();

			AnnotazioneManualeModel lAnnMod = getAnnotazione(lComputo);

			siesLogger.debug("Aggiungo computo = " + lAnnMod);
			lCalcoloPenaModel.getIndulto().add(lAnnMod);

			if (lComputo.getFlagPiuMeno().equals("-")) {
				lComputiConcessiReclusione = lCalUtil.sommaGiornieValute(lComputiConcessiReclusione,
						lComputo.getReclusioneMultaAsCalendar());
				lComputiConcessiArresto = lCalUtil.sommaGiornieValute(lComputiConcessiArresto,
						lComputo.getArrestoAmmendaAsCalendar());
			} else if (lComputo.getFlagPiuMeno().equals("+")) {
				lComputiRevocatiReclusione = lCalUtil.sommaGiornieValute(lComputiRevocatiReclusione,
						lComputo.getReclusioneMultaAsCalendar());
				lComputiRevocatiArresto = lCalUtil.sommaGiornieValute(lComputiRevocatiArresto,
						lComputo.getArrestoAmmendaAsCalendar());
			}
		}

		setRequestAttribute("ComputiConcessiReclusione", lComputiConcessiReclusione);
		setRequestAttribute("ComputiConcessiArresto", lComputiConcessiArresto);
		setRequestAttribute("ComputiRevocatiReclusione", lComputiRevocatiReclusione);
		setRequestAttribute("ComputiRevocatiArresto", lComputiRevocatiArresto);

		// SOLO SE ISTRUTTORIA APERTA PROCEDO A RICALCOLARE LA PENA
		if (lIstruttoriaModel.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA)) {
			// ==========================================================================
			// Effettuo il calcolo della pena
			// ==========================================================================
			Date lDataInizioPena = null;
			if (lDatiAggregati.getPosizioneGiuridicaCumulo() != null
					&& lDatiAggregati.getPosizioneGiuridicaCumulo().getDataInizio() != null) {
				lDataInizioPena = lDatiAggregati.getPosizioneGiuridicaCumulo().getDataInizio();
			}

			Date lDataInizioPenaXcalcoli = lDataInizioPena;

			Date lDataSistema = DateUtils.getSysDateAsDate("dd/MM/yyyy");
			String lTipoCalcolo = "all";

			// Se ergastolo, effettuo comunque il calcolo pena per la parte detentiva
			// quantum e importi, ma non per la decorrenza/scadenza
			if (lPenaResiduaEquivalente.isErgastolo()) {
				lDataInizioPenaXcalcoli = null;
			}

			// ==========================================================================
			// Note di Calcolo:
			// - la pena Inflitta potrebbe essere contestualmente un Ergastolo e una
			// detentiva. In questo caso vanno fatti i calcoli sulla detentiva senza
			// decorrenza e scadenza come se la pena non fosse in decorrenza
			// Se presente inizio pena, il fine pena diventa automaticamente MAI
			// ==========================================================================
			PenaResiduaModel lPenaDaEspiare = null;
			try {
				lPenaDaEspiare = lCalcoloPenaModel.getPenaDaEspiare(lDataInizioPenaXcalcoli, lDataSistema,
						lTipoCalcolo, null);
				siesLogger.debug("lPenaDaEspiare = " + lPenaDaEspiare);

				lPenaDaEspiare.setDataInizio(lDataInizioPena);
				if (lPenaResiduaEquivalente.isErgastolo())
					lPenaDaEspiare.setDataFine(DateUtils.getDate("31/12/9999", "dd/MM/yyyy"));
			} catch (Exception e) {
				throw new F3BException(e);
			}

			//
			if (lPenaDaEspiare.getDataInizio() != null && lPenaDaEspiare.getDataFinePresunta() == null
					&& !lPenaResiduaEquivalente.isErgastolo() && lPenaDaEspiare.isQuantumReclusioneZero()
					&& lPenaDaEspiare.isQuantumArrestoZero()) {
				// Pena in decorrenza ma rideterminata con quantum nulli.
				// Forza la data fine presunta alla data odierna (scarcerazione Immediata)
				lPenaDaEspiare.setDataFinePresunta(lDataSistema);
			}

			// ==========================================================================
			// Salvo / aggiorno la PR
			// ==========================================================================
			IDatiFinaliCumulo lCtrlDatiFinali = SIEPLookupRemote.getDatiFinaliCumuloRemote();
			if (lDatiAggregati.getPenaResiduaCumulo() == null) {
				siesLogger.debug("Inserisco PR");

				PenaRideterminataCumuloModel lPenaRidet = new PenaRideterminataCumuloModel(lPenaDaEspiare);

				lPenaRidet.setIstrIdIstruttoriaCumulo(lIstruttoriaModel.getIdIstruttoriaCumulo());
				lPenaRidet.setDatIdDatiFinaliCumulo(
						lDatiAggregati.getDatiFinaliCumulo().getIdDatiFinaliCumulo());

				// Recupero i dati del tipo pena e Ergastolo da Pena Rideterminata
				lPenaRidet.setCodTipoPenaDetentiva(
						lDatiAggregati.getPenaRideterminataCumulo().getCodTipoPenaDetentiva());

				lPenaRidet.setNumGiorniIsolamentoDiurno(
						lDatiAggregati.getPenaRideterminataCumulo().getNumGiorniIsolamentoDiurno());
				lPenaRidet.setNumMesiIsolamentoDiurno(
						lDatiAggregati.getPenaRideterminataCumulo().getNumMesiIsolamentoDiurno());
				lPenaRidet.setNumAnniIsolamentoDiurno(
						lDatiAggregati.getPenaRideterminataCumulo().getNumAnniIsolamentoDiurno());

				lPenaRidet.setCodOperatoreInserimento(getCodUtenteConnesso());
				lPenaRidet.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lPenaRidet.setDataInserimento(DateUtils.getSysDate());

				lPenaRidet = lCtrlDatiFinali.ExInserisciPenaRideterminataCumulo(lPenaRidet);

				// lPenaDaEspiare = lCtrlPenaResidua.ExInserisciPenaResidua (lPenaDaEspiare);

				lDatiAggregati.setPenaResiduaCumulo(lPenaRidet);
			} else {
				siesLogger.debug("Aggiorno la pena residua = " + lPenaDaEspiare);

				PenaRideterminataCumuloModel lPenaRidet = new PenaRideterminataCumuloModel(lPenaDaEspiare);

				lPenaRidet.setIdPenaRideterminataCumulo(
						lDatiAggregati.getPenaResiduaCumulo().getIdPenaRideterminataCumulo());

				// Recupero i dati del tipo pena e Ergastolo da Pena Rideterminata
				lPenaRidet.setCodTipoPenaDetentiva(
						lDatiAggregati.getPenaRideterminataCumulo().getCodTipoPenaDetentiva());

				lPenaRidet.setNumGiorniIsolamentoDiurno(
						lDatiAggregati.getPenaRideterminataCumulo().getNumGiorniIsolamentoDiurno());
				lPenaRidet.setNumMesiIsolamentoDiurno(
						lDatiAggregati.getPenaRideterminataCumulo().getNumMesiIsolamentoDiurno());
				lPenaRidet.setNumAnniIsolamentoDiurno(
						lDatiAggregati.getPenaRideterminataCumulo().getNumAnniIsolamentoDiurno());

				if (lPenaRidet.getIdPenaRideterminataCumulo() == null)
					throw new SIEPException(SIEPException.USER_MESSAGE,
							"Id PR assente impossibile aggiornare.");

				lPenaRidet.setIstrIdIstruttoriaCumulo(lIstruttoriaModel.getIdIstruttoriaCumulo());
				lPenaRidet.setDatIdDatiFinaliCumulo(
						lDatiAggregati.getDatiFinaliCumulo().getIdDatiFinaliCumulo());

				lPenaRidet.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				lPenaRidet.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				lPenaRidet.setDataAggiornamento(DateUtils.getSysDate());

				lCtrlDatiFinali.ExModificaPenaRideterminataCumulo(lPenaRidet);

				lDatiAggregati.setPenaResiduaCumulo(lPenaRidet);
			}

			setRequestAttribute("CalcoloPenaModel", lCalcoloPenaModel);
			setRequestAttribute("PenaDaEspiare", lPenaDaEspiare);
		} else {
			PenaResiduaModel lPenaDaEspiare = lDatiAggregati.getPenaResiduaCumulo().getPenaResidua();

			setRequestAttribute("CalcoloPenaModel", lCalcoloPenaModel);
			setRequestAttribute("PenaDaEspiare", lPenaDaEspiare);
		}

		//

		return PG_LOAD_DETTAGLIO_PENA_CUMULO;
	}

	/**
	 * 
	 * @param aPenaRideterminata
	 * @return
	 */
	private PenaResiduaModel getPenaResidua(PenaRideterminataCumuloModel aPenaRideterminata) {
		PenaResiduaModel lPenaResidua = new PenaResiduaModel();

		// 03 - Ergastolo Normale = S
		// 04 - Ergastolo con isolamento Diurno = D
		if ("03".equals(aPenaRideterminata.getCodTipoPenaDetentiva())) {
			lPenaResidua.setFlagErgastolo("S");
		} else if ("04".equals(aPenaRideterminata.getCodTipoPenaDetentiva())) {
			lPenaResidua.setFlagErgastolo("D");

			lPenaResidua.setNumAnniIsolamentoDiurno(aPenaRideterminata.getNumAnniIsolamentoDiurno());
			lPenaResidua.setNumMesiIsolamentoDiurno(aPenaRideterminata.getNumMesiIsolamentoDiurno());
			lPenaResidua.setNumGiorniIsolamentoDiurno(aPenaRideterminata.getNumGiorniIsolamentoDiurno());
		}

		// Reclusione
		lPenaResidua.setNumAnniReclusione(aPenaRideterminata.getNumAnniReclusione());
		lPenaResidua.setNumMesiReclusione(aPenaRideterminata.getNumMesiReclusione());
		lPenaResidua.setNumGiorniReclusione(aPenaRideterminata.getNumGiorniReclusione());

		lPenaResidua.setImportoMulta(aPenaRideterminata.getImportoMulta());

		// Arresto
		lPenaResidua.setNumAnniArresto(aPenaRideterminata.getNumAnniArresto());
		lPenaResidua.setNumMesiArresto(aPenaRideterminata.getNumMesiArresto());
		lPenaResidua.setNumGiorniArresto(aPenaRideterminata.getNumGiorniArresto());

		lPenaResidua.setImportoAmmenda(aPenaRideterminata.getImportoAmmenda());

		// Ergastolo
		lPenaResidua.setNumAnniIsolamentoDiurno(aPenaRideterminata.getNumAnniIsolamentoDiurno());
		lPenaResidua.setNumMesiIsolamentoDiurno(aPenaRideterminata.getNumMesiIsolamentoDiurno());
		lPenaResidua.setNumGiorniIsolamentoDiurno(aPenaRideterminata.getNumGiorniIsolamentoDiurno());

		return lPenaResidua;
	}

	/**
	 * 
	 * @param aPenaRideterminata
	 * @return
	 */
	private Vector<LicenzaLibAnticipataModel> getLiberazioniAnticipate(
			PenaRideterminataCumuloModel aPenaRideterminata) {
		Vector<LicenzaLibAnticipataModel> lListaLA = new Vector<>();

		if (aPenaRideterminata.getNumeroGiorniLA() != null
				&& aPenaRideterminata.getNumeroGiorniLA().intValue() > 0) {
			LicenzaLibAnticipataModel lLicenza = new LicenzaLibAnticipataModel();
			lLicenza.setCodTipoLicenza("LA");
			lLicenza.setDescrStatoPermesso("LA");

			lLicenza.setNumeroGiorni(aPenaRideterminata.getNumeroGiorniLA());

			lListaLA.add(lLicenza);
		}

		if (aPenaRideterminata.getNumeroGiorniLS() != null
				&& aPenaRideterminata.getNumeroGiorniLS().intValue() > 0) {
			LicenzaLibAnticipataModel lLicenza = new LicenzaLibAnticipataModel();
			lLicenza.setCodTipoLicenza("LA");
			lLicenza.setDescrStatoPermesso("LS");

			lLicenza.setNumeroGiorni(aPenaRideterminata.getNumeroGiorniLS());

			lListaLA.add(lLicenza);
		}

		if (aPenaRideterminata.getNumeroGiorniLI() != null
				&& aPenaRideterminata.getNumeroGiorniLI().intValue() > 0) {
			LicenzaLibAnticipataModel lLicenza = new LicenzaLibAnticipataModel();
			lLicenza.setCodTipoLicenza("LA");
			lLicenza.setDescrStatoPermesso("LI");

			lLicenza.setNumeroGiorni(aPenaRideterminata.getNumeroGiorniLI());

			lListaLA.add(lLicenza);
		}

		if (aPenaRideterminata.getNumeroGiorniRiduzione() != null
				&& aPenaRideterminata.getNumeroGiorniRiduzione().intValue() > 0) {
			LicenzaLibAnticipataModel lLicenza = new LicenzaLibAnticipataModel();
			lLicenza.setCodTipoLicenza("LA");
			lLicenza.setDescrStatoPermesso("RD");

			lLicenza.setNumeroGiorni(aPenaRideterminata.getNumeroGiorniRiduzione());

			lListaLA.add(lLicenza);
		}

		// Gli scomputi il CalcoloPenaModel li preleva direttamente dalla lista LA
		if (aPenaRideterminata.getNumeroGiorniScomputo() != null
				&& aPenaRideterminata.getNumeroGiorniScomputo().intValue() > 0) {
			LicenzaLibAnticipataModel lLicenza = new LicenzaLibAnticipataModel();
			lLicenza.setCodTipoLicenza("PP");

			lLicenza.setNumeroGiorni(aPenaRideterminata.getNumeroGiorniScomputo());

			lListaLA.add(lLicenza);
		}

		return lListaLA;
	}

	/**
	 * 
	 * @param lComputoCumulo
	 * @return
	 */
	private AnnotazioneManualeModel getAnnotazione(ComputiCumuloModel aComputoCumulo) {
		AnnotazioneManualeModel lAnnotazione = new AnnotazioneManualeModel();

		lAnnotazione.setCodTipoAnnotazione("002"); // Indulto

		lAnnotazione.setFlagPiuMeno(aComputoCumulo.getFlagPiuMeno());

		lAnnotazione.setNumAnniReclusione(aComputoCumulo.getNumAnniReclusione());
		lAnnotazione.setNumMesiReclusione(aComputoCumulo.getNumMesiReclusione());
		lAnnotazione.setNumGiorniReclusione(aComputoCumulo.getNumGiorniReclusione());

		lAnnotazione.setImportoMulta(aComputoCumulo.getImportoMulta());

		lAnnotazione.setNumAnniArresto(aComputoCumulo.getNumAnniArresto());
		lAnnotazione.setNumMesiArresto(aComputoCumulo.getNumMesiArresto());
		lAnnotazione.setNumGiorniArresto(aComputoCumulo.getNumGiorniArresto());

		lAnnotazione.setImportoAmmenda(aComputoCumulo.getImportoAmmenda());

		return lAnnotazione;
	}

	/**
	 * 
	 * @param aListaComputi
	 * @return
	 */
	// private PenaResiduaModel getTotaleComputi(Vector<AnnotazioneManualeModel> aListaComputi) {
	//
	// PenaResiduaModel lTotaleComputi = new PenaResiduaModel();
	//
	// CalcoloPenaModel lCalcoloPenaModel = new CalcoloPenaModel();
	//
	// lCalcoloPenaModel.setTipoPenaIniziale(ICostantiCalcoloPena.PENA_MANUALE);
	// lCalcoloPenaModel.setPenaResiduaManuale(new PenaResiduaModel());
	// lCalcoloPenaModel.setIndulto(aListaComputi);
	//
	// try {
	// lTotaleComputi = lCalcoloPenaModel.getPenaDaEspiare(null, null, "all", null);
	// siesLogger.debug("lTotaleComputi = " + lTotaleComputi);
	// } catch (Exception e) {
	// }
	//
	// return lTotaleComputi;
	// }

}