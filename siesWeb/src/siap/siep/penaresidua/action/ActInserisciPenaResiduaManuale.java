package siap.siep.penaresidua.action;

/**
* <p>Title: ActInserisciPenaResidua</p>
* <p>Description: Classe Action per l'inserimento di PenaResidua Manuale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.action.ICostantiPenaComplessiva;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciPenaResiduaManuale extends ActionSiap implements ICostantiPenaResidua {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del della Pena Residua manuale. Preleva i dati dalla form e li inserisce a
	 * sistema.
	 * 
	 * n.b. Viene effettuato un preventivo controllo tra i quantum inseriti e le date di espiazione. Se le
	 * date calcolate coincidono con quelle inserite dall'utente, viene effettuato l'inserimento diretto
	 * dell'evento, della pena residua e dell'eventuale LA. Tutti i dati vengono inseriti VALIDATI. Se invece
	 * le date non coincidono, NON VIENE EFFETTUATO L'INSERIMENTO, ma l'utente viene reindirizzato su una
	 * finestra di warning. Da tale finestra l'utente può decidere di confermare i dati inseriti o di
	 * modificarli. In caso confermi viene di nuovo invocata questa action con il parametro 'datiConfermati'
	 * valorizzato.
	 * 
	 * Quindi questa Action viene invocata da due punti: - direttamente dalla pagina di inserimento dei dati
	 * della PRM - dalla pagina di Warning in caso di Conferma
	 * 
	 * L'unica differenza è che nel secondo caso non viene effettuato il controllo sulle date di decorrenza in
	 * quanto l'utente ha comunque confermato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		PenaResiduaModel lPenMod = new PenaResiduaModel();

		lPenMod.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_DECORRENZA_PENA,
				CAMPO_MESE_DATA_DECORRENZA_PENA, CAMPO_GIORNO_DATA_DECORRENZA_PENA));

		lPenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lPenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lPenMod.setDataInserimento(DateUtils.getSysDate());

		lPenMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		// ICostantiPenaResidua.CAMPO_FLAG_TIPO_PENA
		lPenMod.setFlagPenaSospesa(this.getRequestStringParameter("tipo"));
		lPenMod.setFlagValidato("S");
		lPenMod.setDiesAQuo("S");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("!!!TIPO = " + this.getRequestStringParameter("tipo"));
		// ==========================================================================
		// Recupero quantum, importi e date di decorrenza
		// ==========================================================================
		// se ergastolo false
		if (!this.isRequestChecked(CAMPO_FLAG_ERGASTOLO)) {
			lPenMod.setFlagErgastolo("N");
			lPenMod.setDataFine(getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE,
					CAMPO_GIORNO_DATA_FINE));
			lPenMod.setDataFinePresunta(getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE,
					CAMPO_GIORNO_DATA_FINE));
			lPenMod.setDataFineReclusione(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_RECLUSIONE,
					CAMPO_MESE_DATA_FINE_RECLUSIONE, CAMPO_GIORNO_DATA_FINE_RECLUSIONE));
			lPenMod.setDataInizioArresto(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_ARRESTO,
					CAMPO_MESE_DATA_INIZIO_ARRESTO, CAMPO_GIORNO_DATA_INIZIO_ARRESTO));

			// Quantum Reclusione
			lPenMod.setNumAnniReclusione(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_RECLUSIONE));
			lPenMod.setNumMesiReclusione(getRequestBigDecimalParameter(CAMPO_NUM_MESI_RECLUSIONE));
			lPenMod.setNumGiorniReclusione(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_RECLUSIONE));

			// Multa
			if ((getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA) != null
					&& !(getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA))
							.equals(""))
					|| (getRequestStringParameter(
							ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA) != null
							&& !(getRequestStringParameter(
									ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA)).equals(""))) {

				if (getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_VALUTA_IMPORTO_MULTA)
						.equals("LIT")) {
					lPenMod.setImportoMulta(Utils.toEuro(
							getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA)));
				} else {
					lPenMod.setImportoMulta(new BigDecimal(
							getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA)
									+ "." + getRequestStringParameter(
											ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA)));
				}
			}

			// Quantum Arresto e Ammenda
			lPenMod.setNumAnniArresto(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ARRESTO));
			lPenMod.setNumMesiArresto(getRequestBigDecimalParameter(CAMPO_NUM_MESI_ARRESTO));
			lPenMod.setNumGiorniArresto(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ARRESTO));

			if ((getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA) != null
					&& !(getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA))
							.equals(""))
					|| (getRequestStringParameter(
							ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA) != null
							&& !(getRequestStringParameter(
									ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA)).equals(""))) {
				if (getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_VALUTA_IMPORTO_AMMENDA)
						.equals("LIT")) {
					lPenMod.setImportoAmmenda(Utils.toEuro(getRequestStringParameter(
							ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA)));
				} else {
					lPenMod.setImportoAmmenda(new BigDecimal(
							getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA)
									+ "." + getRequestStringParameter(
											ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA)));
				}
			}
		} else { // SE ERGASTOLO SI
			lPenMod.setDataFine(DateUtils.getDate("31-12-9999", "dd-MM-yyyy"));
			lPenMod.setDataFinePresunta(DateUtils.getDate("31-12-9999", "dd-MM-yyyy"));

			lPenMod.setNumAnniIsolamentoDiurno(
					getRequestBigDecimalParameter(ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO));
			lPenMod.setNumMesiIsolamentoDiurno(
					getRequestBigDecimalParameter(ICostantiPenaComplessiva.CAMPO_NUM_MESI_ISOLAMENTO_DIURNO));
			lPenMod.setNumGiorniIsolamentoDiurno(getRequestBigDecimalParameter(
					ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO));

			lPenMod.setDataInizioIsolamentoDiurno(
					getRequestDateParameter(ICostantiPenaComplessiva.CAMPO_ANNO_DATA_INIZIO_ISOLAMENTO_DIURNO,
							ICostantiPenaComplessiva.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO,
							ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO));
			lPenMod.setDataFineIsolamentoDiurno(
					getRequestDateParameter(ICostantiPenaComplessiva.CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO,
							ICostantiPenaComplessiva.CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO,
							ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO));

			if (!getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO)
					.equals("")
					|| !getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_NUM_MESI_ISOLAMENTO_DIURNO)
							.equals("")
					|| !getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO)
							.equals("")
					|| (!getRequestStringParameter(
							ICostantiPenaComplessiva.CAMPO_ANNO_DATA_INIZIO_ISOLAMENTO_DIURNO).equals("")
							&& !getRequestStringParameter(
									ICostantiPenaComplessiva.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO)
											.equals("")
							&& !getRequestStringParameter(
									ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO)
											.equals(""))
					|| (!getRequestStringParameter(
							ICostantiPenaComplessiva.CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO).equals("")
							&& !getRequestStringParameter(
									ICostantiPenaComplessiva.CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO)
											.equals("")
							&& !getRequestStringParameter(
									ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO)
											.equals(""))) {
				lPenMod.setFlagErgastolo("D");
			} else {
				lPenMod.setFlagErgastolo("S");
			}
		}

		// ==========================================================================
		//
		// getLicenzaModel
		Vector<LicenzaLibAnticipataModel> lListaLicenze = new Vector<>();
		this.getLicenzaModel("LA", lListaLicenze);
		this.getLicenzaModel("LS", lListaLicenze);
		this.getLicenzaModel("LI", lListaLicenze);
		this.getLicenzaModel("RD", lListaLicenze);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" lListaLicenze.size() = " + lListaLicenze.size());

		int lGiorniLA = 0;
		int lGiorniRD = 0;
		int lTotGGAnticipazione = 0;

		for (int i = 0; i < lListaLicenze.size(); i++) {
			LicenzaLibAnticipataModel lLicenza = lListaLicenze.elementAt(i);
			if ("LA".equals(lLicenza.getCodTipoLicenza()))
				lGiorniLA += lLicenza.getNumeroGiorni().intValue();
			else if ("RD".equals(lLicenza.getCodTipoLicenza()))
				lGiorniRD += lLicenza.getNumeroGiorni().intValue();
		}
		lTotGGAnticipazione = lGiorniLA + lGiorniRD;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" Giorni di LIBERAZIONE ANTICIPATA TOTALI = " + lGiorniLA);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" Giorni di RISARCIMENTO DANNI TOTALI = " + lGiorniRD);

		// ==========================================================================
		// NUOVO EVENTO per la pena residua manuale
		// ==========================================================================
		EventoModel lEventoModel = new EventoModel();

		lEventoModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		// da stabilire i codici
		lEventoModel.setCodTipoEvento("01");
		lEventoModel.setCodTipoProvvedimento("04"); // Provvedimento
		lEventoModel.setCodMotivo("0925"); // Pena Residua Manuale Per Correzione Errore Materiale

		lEventoModel.setFlagDocumentoRegistrato("S"); // n.b. Nasce Validato
		lEventoModel.setFlagStampaSiep("S");
		lEventoModel.setFlagVideoSiep("S");

		lEventoModel.setDataEmissione(DateUtils.getDate(DateUtils.getYearToString(DateUtils.getSysDate()),
				DateUtils.getMonthToString(DateUtils.getSysDate()),
				DateUtils.getDayToString(DateUtils.getSysDate())));
		lEventoModel.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		lEventoModel.setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());

		lEventoModel.setDataTrasmissioneAtti(null);
		lEventoModel.setCodMagistrato(null);
		lEventoModel.setCodEsito("-");
		lEventoModel.setCodTipoUfficioDestinatario("-");
		lEventoModel.setCodLuogoDestinatario("-");
		lEventoModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEventoModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEventoModel.setDataInserimento(DateUtils.getSysDate());
		// L'evento nasce validato per cui inserisco anche i dati dell'aggiornamento
		lEventoModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lEventoModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lEventoModel.setDataAggiornamento(DateUtils.getSysDate());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lEventoModel = " + lEventoModel);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lPenMod = " + lPenMod);

		// ==========================================================================
		// Prima di procedere con l'inserimento controllo che le date inserite siano
		// coerenti con i quantum e la data di decorrenza
		// ==========================================================================
		// ==========================================================================
		// Determino le date di espiazione a partire dai quantum
		// ==========================================================================
		if (isRequestParameterNullObj("datiConfermati")) { // n.b. il campo 'datiConfermati' esiste solo nella
															// form di Warning. In
															// questo caso vuol dire che l'utente ha
															// confermato i suoi dati e quindi
															// non rieffettuo i controlli
			if (!this.isRequestChecked(CAMPO_FLAG_ERGASTOLO) && lPenMod.getDataInizio() != null) {
				CalendarUtil lCalUtil = new CalendarUtil();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Determino le date di espiazione a partire dai quantum");

				// Ricalcolo le date in base ai quantum e alla data inizio specificata
				// e ai giorni di LA imputati
				PenaResiduaModel lPenaRicalcolata = new PenaResiduaModel();
				lPenaRicalcolata.setQuantumReclusione(lPenMod.getQuantumReclusione());
				lPenaRicalcolata.setQuantumArresto(lPenMod.getQuantumArresto());
				lPenaRicalcolata.setDataInizio(lPenMod.getDataInizio());

				ICalcoloPena lCalPenCtrl = SIEPLookupRemote.getCalcoloPenaRemote();
				Vector lDateFine = lCalPenCtrl.exCalcolaDataFinePena(lPenMod.getDataInizio(),
						lPenaRicalcolata, true);
				if (lDateFine.size() == 1) { // solo Reclusione o Arresti: ho quindi solo data fine
												// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di
												// istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("Ho solo una data: " + lDateFine.get(0));

					if (!lCalUtil.isZero(lPenMod.getQuantumArresto())) {
						// Solo arresti
						lPenaRicalcolata.setDataInizioArresto(lPenMod.getDataInizio());
					}

					lPenaRicalcolata.setDataFine((Date) lDateFine.get(0));
				} else if (lDateFine.size() == 2) { // Sono presenti sia Reclusione che Arresti
					lPenaRicalcolata.setDataFineReclusione((Date) lDateFine.get(0));
					lPenaRicalcolata.setDataInizioArresto(
							DateUtils.getDayAfter(lPenaRicalcolata.getDataFineReclusione()));
					lPenaRicalcolata.setDataFine((Date) lDateFine.get(1));
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"Attenzione quantum negativi o nulli Impossibile determinare la data fine pena");
				}

				// Anticipo il fine pena per effetto delle LA
				// 20/05/2014 - Nuova L.A.
				// if (libAntMod.getNumeroGiorni()!=null){
				// int lGiorniLA = (libAntMod.getNumeroGiorni()).intValue();

				if (lTotGGAnticipazione != 0) {
					// End Nuova L.A.
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(" ---------------- Anticipo il fine pena per effetto di "
							+ lTotGGAnticipazione + " giorni di Anticipazione");

					// Arretro le data fine reclusione, inizio arresto, fine pena
					if (lPenaRicalcolata.getDataFineReclusione() != null) {
						lPenaRicalcolata.setDataFineReclusione(
								DateUtils.moveDateTo(lPenaRicalcolata.getDataFineReclusione(),
										Calendar.DAY_OF_MONTH, -lTotGGAnticipazione));
					}
					if (lPenaRicalcolata.getDataInizioArresto() != null) {
						lPenaRicalcolata.setDataInizioArresto(
								DateUtils.moveDateTo(lPenaRicalcolata.getDataInizioArresto(),
										Calendar.DAY_OF_MONTH, -lTotGGAnticipazione));
					}
					if (lPenaRicalcolata.getDataFine() != null) {
						lPenaRicalcolata.setDataFine(DateUtils.moveDateTo(lPenaRicalcolata.getDataFine(),
								Calendar.DAY_OF_MONTH, -lTotGGAnticipazione));
					}

					// Azzero le date se per effetto delle LA sono arretrate oltre la data inizio pena
					if (lPenaRicalcolata.getDataFineReclusione() != null && DateUtils.isGreater(
							lPenaRicalcolata.getDataInizio(), lPenaRicalcolata.getDataFineReclusione())) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger
								.debug("Data fine reclusione < data inizio pena per effetto arretramento LA");
						lPenaRicalcolata.setDataFineReclusione(null);
					}

					if (lPenaRicalcolata.getDataInizioArresto() != null && DateUtils.isGreater(
							lPenaRicalcolata.getDataInizio(), lPenaRicalcolata.getDataInizioArresto())) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger
								.debug("Data inizio arresto < data inizio pena per effetto arretramento LA");
						lPenaRicalcolata.setDataInizioArresto(null);
					}

					//
					// if ( lPenaRicalcolata.getDataFine()!=null
					// && DateUtils.isGreater(lPenaRicalcolata.getDataInizio(), lPenaRicalcolata.getDataFine()
					// )) {
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("Attenzione!!! INIZIO pena < fine pena rideterminato per effetto delle
					// LA ");
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("In teoria ho un periodo fungibile");
					// }

				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Pena Manuale = " + lPenMod);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Pena Ricalcolata = " + lPenaRicalcolata);
				setRequestAttribute("errFineReclusione", "NO");
				setRequestAttribute("errInizioArresto", "NO");
				setRequestAttribute("errFinePena", "NO");
				// ========================================================================
				// Confronto le date ricalcolate con quelle imputate dall'utente
				// n.b. se è stata specificata la data inizio pena e le LA, le date
				// ricalcolate sono state anticipate per effetto delle LA e così
				// devono essere anche le date imputate dall'utente
				// n.b. nella form già esistono i controlli per verificare almeno che
				// vengano digitati gli opportuni campi
				// ========================================================================
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inizio i controlli sulle date ");
				boolean lIsErroreDate = false;
				// data fine reclusione
				if ((lPenMod.getDataFineReclusione() != null
						&& lPenaRicalcolata.getDataFineReclusione() == null)
						|| (lPenMod.getDataFineReclusione() == null
								&& lPenaRicalcolata.getDataFineReclusione() != null)
						|| (lPenMod.getDataFineReclusione() != null
								&& lPenaRicalcolata.getDataFineReclusione() != null
								&& !DateUtils.isEquals(lPenMod.getDataFineReclusione(),
										lPenaRicalcolata.getDataFineReclusione()))) {
					lIsErroreDate = true;
					setRequestAttribute("errFineReclusione", "SI");

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Attenzione!! Data fine reclusione non congruente");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("             Data Fine reclusione dichiarata: "
							+ lPenMod.getDataFineReclusione());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("             Data Fine reclusione calcolata : "
							+ lPenaRicalcolata.getDataFineReclusione());
				}

				// data inizio arresto
				if ((lPenMod.getDataInizioArresto() != null
						&& lPenaRicalcolata.getDataInizioArresto() == null)
						|| (lPenMod.getDataInizioArresto() == null
								&& lPenaRicalcolata.getDataInizioArresto() != null)
						|| (lPenMod.getDataInizioArresto() != null
								&& lPenaRicalcolata.getDataInizioArresto() != null
								&& !DateUtils.isEquals(lPenMod.getDataInizioArresto(),
										lPenaRicalcolata.getDataInizioArresto()))) {
					lIsErroreDate = true;
					setRequestAttribute("errInizioArresto", "SI");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Attenzione!! Data inizio arresto non congruente");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"             Data inizio arresto dichiarata: " + lPenMod.getDataInizioArresto());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("             Data inizio arresto calcolata : "
							+ lPenaRicalcolata.getDataInizioArresto());
				}

				// data fine
				if ((lPenMod.getDataFine() != null && lPenaRicalcolata.getDataFine() == null)
						|| (lPenMod.getDataFine() == null && lPenaRicalcolata.getDataFine() != null)
						|| (lPenMod.getDataFine() != null && lPenaRicalcolata.getDataFine() != null
								&& !DateUtils.isEquals(lPenMod.getDataFine(),
										lPenaRicalcolata.getDataFine()))) {
					lIsErroreDate = true;
					setRequestAttribute("errFinePena", "SI");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Attenzione!! Data fine pena non congruente");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("             Data fine pena dichiarata: " + lPenMod.getDataFine());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(
							"             Data fine pena calcolata : " + lPenaRicalcolata.getDataFine());
				}

				// Se i dati non sono congruenti lo comunico in una finesta di warning
				// prima di procedere n.b. non effettuo l'inserimento
				if (lIsErroreDate) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Date non coerenti");
					setRequestAttribute("lPenaManuale", lPenMod);
					setRequestAttribute("lPenaRicalcolata", lPenaRicalcolata);

					setRequestAttribute("lGiorniLA", "" + lGiorniLA);
					setRequestAttribute("lGiorniRD", "" + lGiorniRD);

					return IWebConstants.ROOT_DIR
							+ "files/siap/siep/penacomplessiva/WarningPenaResiduaManuale.jsp";
				}
			}
		}

		// ==========================================================================
		// Effettuo inserimento della pena residua manuale, delle Licenze e
		// dell'evento e aggiorno eventualemente lo scadenzario
		// n.b. nuova versione 12/2006
		// ==========================================================================
		// 20/05/2014 Nuova L.A. : viene inserita una licenza per ogni tipo ci Concessione L.A. presente :
		// L.A. , L.A. Speciale e L.A. Integrazione
		IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		EventoModel lEveModRet = lCtrl.ExInserisciPenaResiduaManuale(lEventoModel, lPenMod, lListaLicenze,
				"02"); // 02 = TIPO_SCADENZARIO - Fine pena

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.penaresidua.action.ActLoadDettaglioPenaResiduaManuale&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveModRet.getIdEvento().toString();
		return lPage;

		// Old
		// // Effettuo inserimento della pena residua (old version)
		// IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		// PenaResiduaModel llPenModRet = lCtrl.ExInserisciPenaResiduaScadenzario(lPenMod,"02"); // 02 =
		// TIPO_SCADENZARIO - Fine pena
		// // setta la risposta nella request
		// setRequestAttribute("penaresidua", llPenModRet);
		// //Prepara la pagina di destinazione
		// String lPage = "";
		// lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.siep.penaresidua.action.ActLoadDettaglioPenaResiduaManuale&"+CAMPO_ID_PENA_RESIDUA+"="+llPenModRet.getIdPenaResidua().toString();
		// return lPage;
	}

	/**
	 * Crea il model LicenzaLibAnticipataModel con i dati opportuni caricando il vettore in input (add) solo
	 * se gg concessi >0
	 * 
	 * @param aTipoLicenza
	 * @param aListaLicenze
	 *            vettore da caricare
	 * @throws Exception
	 */
	private void getLicenzaModel(String aTipoLicenza, Vector<LicenzaLibAnticipataModel> aListaLicenze)
			throws F3BException {
		LicenzaLibAnticipataModel libAntMod = null;
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lGGLicenza = "";

		if ("LA".equals(aTipoLicenza))
			lGGLicenza = getRequestStringParameter(ICostantiPenaResidua.CAMPO_NUM_GIORNI_LIBER_ANT_ORDINARIA);
		else if ("LS".equals(aTipoLicenza))
			lGGLicenza = getRequestStringParameter(ICostantiPenaResidua.CAMPO_NUM_GIORNI_LIBER_ANT_SPECIALE);
		else if ("LI".equals(aTipoLicenza))
			lGGLicenza = getRequestStringParameter(
					ICostantiPenaResidua.CAMPO_NUM_GIORNI_LIBER_ANT_INTEGRAZIONE);
		else if ("RD".equals(aTipoLicenza))
			lGGLicenza = getRequestStringParameter(
					ICostantiPenaResidua.CAMPO_NUM_GIORNI_RISARCIMENTO_DANNI_DL92);

		if (lGGLicenza.length() > 0 && Integer.parseInt(lGGLicenza) > 0) {
			libAntMod = new LicenzaLibAnticipataModel();

			libAntMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

			libAntMod.setNumeroGiorni(new BigDecimal(lGGLicenza));

			libAntMod.setFlagConcesso("C");
			libAntMod.setFlagElaborato("N"); // NON elaborate, verranno poste a S solo se computati sul fine
												// pena

			if ("LA".equals(aTipoLicenza)) {
				libAntMod.setCodTipoLicenza("LA");
				libAntMod.setDescrStatoPermesso("LA");
			} else if ("LI".equals(aTipoLicenza)) {
				libAntMod.setCodTipoLicenza("LA");
				libAntMod.setDescrStatoPermesso("LI");
			} else if ("LS".equals(aTipoLicenza)) {
				libAntMod.setCodTipoLicenza("LA");
				libAntMod.setDescrStatoPermesso("LS");
			} else if ("RD".equals(aTipoLicenza)) {
				libAntMod.setCodTipoLicenza("RD");
			}

			libAntMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			libAntMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			libAntMod.setDataInserimento(DateUtils.getSysDate());

			libAntMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			libAntMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			libAntMod.setDataAggiornamento(DateUtils.getSysDate());

			aListaLicenze.add(libAntMod);

		}

	}

}