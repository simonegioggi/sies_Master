package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.LibAnticipataCumuloModel;
import siap.siep.modulocumulo.model.PeriodoLibAntCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action per l'inserimento del Ridimensionamento LA e della Revoca LA
 *
 *
 */
public class ActInserisciLiberazioneAnticipataCumulo extends ActionModuloCumulo implements
		ICostantiStatoEsecTitoloCumulato, ICostantiLibAnticipataCumulo, ICostantiPeriodoLibAntCumulo {

	/**
	 *    
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		String Concessione = "C"; // per default vengono aggiunti

		StatoEsecTitoloCumulatoModel lStatoEsec = new StatoEsecTitoloCumulatoModel();
		IStatoEsecTitoloCumulato lCtrlStatoEsec = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();

		String lModalita = "";
		lModalita = getRequestStringParameter("modalita"); // I=Inserisci, M=Modifica, C=Cancella, NP=nuovo
															// Periodo

		String lStato = "I";
		if (lModalita.equals("M")) {
			// Cambia stato solo se il dato NON è Iscritto manualmente
			if (getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_FLAG_STATO).equals("E")
					|| getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_FLAG_STATO).equals("M")) {
				lStato = "M";
			}
		}

		if (!lModalita.equals("C")) {
			// Casi di INSERIMENTO e MODIFICA
			String lCodTipoUfficio = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_ALTRO);
			String lDescrComune = getRequestStringParameter(CAMPO_COD_LUOGO_ALTRO);
			String lCodUfficio = getUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lDescrComune)
					.getCodUfficio();
			ComuneModel lComune = getCodComuneByDescr(lDescrComune);

			if ("M".equals(lModalita)) {
				// LogF3B.getLogger().debug(" ----- > StatoEsec - modifica ");
				lStatoEsec.setIdStatoEsecTitoloCumulato(
						getRequestBigDecimalParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO));

				lStatoEsec.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				lStatoEsec.setDataAggiornamento(DateUtils.getSysDate());
				lStatoEsec.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			} else if ("I".equals(lModalita)) {
				lStatoEsec.setCodOperatoreInserimento(getCodUtenteConnesso());
				lStatoEsec.setDataInserimento(DateUtils.getSysDate());
				lStatoEsec.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			}

			lStatoEsec.setCodUfficioEmittente(lCodUfficio);
			lStatoEsec.setCodLuogoEmittente(lComune.getCodComune());

			lStatoEsec.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE_ALTRO,
					CAMPO_MESE_DATA_EMISSIONE_ALTRO, CAMPO_GIORNO_DATA_EMISSIONE_ALTRO));

			lStatoEsec.setAnnoProcedimento(getRequestBigDecimalParameter(CAMPO_ANNO_PROCEDIMENTO));
			lStatoEsec.setProgrProcedimento(getRequestBigDecimalParameter(CAMPO_PROGR_PROCEDIMENTO));
			lStatoEsec.setAnnoProvvedimento(getRequestBigDecimalParameter(CAMPO_ANNO_PROVVEDIMENTO));
			lStatoEsec.setProgrProvvedimento(getRequestBigDecimalParameter(CAMPO_PROGR_PROVVEDIMENTO));

			lStatoEsec.setCodTipoEvento("01"); // 01 = Provvedimento.
			lStatoEsec.setCodTipoProvvedimento(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO)); // 02
																											// =
																											// Decreto
																											// ;
																											// 03
																											// =
																											// Ordinanza.

			lStatoEsec.setCodEsitoTenore("-");
			lStatoEsec.setCodEsito(getRequestStringParameter(CAMPO_COD_ESITO));

			if (getRequestStringParameter(CAMPO_COD_ESITO).equals("0020")) // Concede
			{
				if (lCodTipoUfficio.equals("UDS") || // Ufficio di Sorveglianza/Magistrato di Sorveglianza
						lCodTipoUfficio.equals("UDSM")) // Ufficio di Sorveglianza Minori /Magistrato di
														// Sorveglianza Minori
				{
					lStatoEsec.setCodMotivo("2130"); // 2130 = Concessione Liberazione Anticipata
				} else // TDS o TDSM (Tribunale di Sorveglianza / Tribunale di Sorveglianza Minori
				{
					lStatoEsec.setCodMotivo("0076"); // 0076 = Concessione Liberazione Anticipata
				}
			} else if (getRequestStringParameter(CAMPO_COD_ESITO).equals("0406")) // Concede a seguito reclamo
			{
				lStatoEsec.setCodMotivo("0113"); // Reclamo su Liberazione Anticipata
			} else if (getRequestStringParameter(CAMPO_COD_ESITO).equals("0407")) // Riduce a seguito Reclamo
			{
				Concessione = "S";
				lStatoEsec.setCodMotivo("0113"); // Reclamo su Liberazione Anticipata
			} else if (getRequestStringParameter(CAMPO_COD_ESITO).equals("0023")) // Revoca per il Periodo
			{
				Concessione = "S";

				if (lCodTipoUfficio.equals("UDS") || // Ufficio di Sorveglianza/Magistrato di Sorveglianza
						lCodTipoUfficio.equals("UDSM")) // Ufficio di Sorveglianza Minori /Magistrato di
														// Sorveglianza Minori
				{
					lStatoEsec.setCodMotivo("2135"); // 2135 = Revoca Liberazione Anticipata
				} else // TDS o TDSM (Tribunale di Sorveglianza / Tribunale di Sorveglianza Minori
				{
					lStatoEsec.setCodMotivo("0028"); // 0028 = Revoca Liberazione Anticipata
				}
			}

			if (getRequestStringParameter(CAMPO_NOTE) != null
					&& !getRequestStringParameter(CAMPO_NOTE).equals(""))
				lStatoEsec.setNote(getRequestStringParameter(CAMPO_NOTE));
			else
				lStatoEsec.setNote("");

			lStatoEsec.setTitIdTitoloCumulato(
					getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
			lStatoEsec.setIstrIdIstruttoriaCumulo(
					getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
			lStatoEsec.setFlagStato(lStato);
			lStatoEsec.setMotivoModifica(null);

		} // Chiude if(!lModalita.equals("C"))

		// Se dati selezionati dalla lista
		// String lIdSorveglianzaLa = getRequestStringParameter(CAMPO_ID_LIB_ANTICIPATA_CUMULO);

		// ==========================================================================
		// Gestione L.A. ORDINARIA, L.A. SPECIALE, L.A. INTEGRAZIONE
		// ==========================================================================
		// Gestione di L.A. ordinaria
		// ==============================================
		PeriodoLibAntCumuloModel lPeriodoLibAnt_LA = null;
		LibAnticipataCumuloModel LibAnt_LA = null;

		if ((!isRequestParameterNullObj(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA)
				&& !getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA).equals("")
				&& !getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA).equals("0"))
				&& !lModalita.equals("C") // Casi di INSERIMENTO e MODIFICA
		) {

			// LogF3B.getLogger().debug(" ----- > L.A. Giorni - NumggLA =
			// "+this.getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA) );
			LibAnt_LA = new LibAnticipataCumuloModel();

			LibAnt_LA.setNumeroGiorni(new BigDecimal(
					this.getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA)));

			LibAnt_LA.setCodTipoLicenza("LA");
			LibAnt_LA.setTipoLa("LA");

			if ("I".equals(lModalita)) {
				LibAnt_LA.setCodOperatoreInserimento(getCodUtenteConnesso());
				LibAnt_LA.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				LibAnt_LA.setDataInserimento(DateUtils.getSysDate());
			} else if ("M".equals(lModalita)) {
				LibAnt_LA.setCodOperatoreInserimento(getCodUtenteConnesso());
				LibAnt_LA.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				LibAnt_LA.setDataInserimento(DateUtils.getSysDate());
				LibAnt_LA.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				LibAnt_LA.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				LibAnt_LA.setDataAggiornamento(DateUtils.getSysDate());
			}

			LibAnt_LA.setTitIdTitoloCumulato(
					getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
			LibAnt_LA.setFlagStato(lStato);
			LibAnt_LA.setMotivoModifica(null);

			LibAnt_LA.setFlagConcesso(Concessione);

			// ==========================================================================
			// Gestione dei PERIODI di L.A. Ordinaria
			// ======================================================
			lPeriodoLibAnt_LA = new PeriodoLibAntCumuloModel();

			Date[] lDateInizio = null;
			Date[] lDateFine = null;

			lDateInizio = getRequestDateParameters(ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO,
					ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO,
					ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO);

			lDateFine = getRequestDateParameters(ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE,
					ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE,
					ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE);

			int num = 0;
			for (int jj = 0; jj < 6; jj++)
				if (lDateInizio[jj] != null && lDateFine[jj] != null)
					num++;

			// LogF3B.getLogger().debug("ci sono "+num+" periodi validi di L.A. ");

			if (num > 0) {
				Vector<PeriodoLibAntCumuloModel> lVecPeriodiLA = new Vector();

				for (int jj = 0; jj < 6; jj++) {
					if (lDateInizio[jj] != null && lDateFine[jj] != null) {
						lPeriodoLibAnt_LA = new PeriodoLibAntCumuloModel();

						lPeriodoLibAnt_LA.setDataInizio(lDateInizio[jj]);
						lPeriodoLibAnt_LA.setDataFine(lDateFine[jj]);

						lPeriodoLibAnt_LA.setFlagStato(lStato);
						lPeriodoLibAnt_LA.setMotivoModifica(null);

						if ("I".equals(lModalita)) {
							lPeriodoLibAnt_LA.setCodOperatoreInserimento(getCodUtenteConnesso());
							lPeriodoLibAnt_LA.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
							lPeriodoLibAnt_LA.setDataInserimento(DateUtils.getSysDate());
						} else if ("M".equals(lModalita)) {
							lPeriodoLibAnt_LA.setCodOperatoreInserimento(getCodUtenteConnesso());
							lPeriodoLibAnt_LA.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
							lPeriodoLibAnt_LA.setDataInserimento(DateUtils.getSysDate());
							lPeriodoLibAnt_LA.setCodOperatoreAggiornamento(getCodUtenteConnesso());
							lPeriodoLibAnt_LA.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
							lPeriodoLibAnt_LA.setDataAggiornamento(DateUtils.getSysDate());
						}

						lVecPeriodiLA.addElement(lPeriodoLibAnt_LA);
					}
				}

				// Aggiungo i Periodi al Model LiberazioneAnticipataCumuloModel
				LibAnt_LA.setListaPeriodiLibAnticipate(lVecPeriodiLA);
			} // Chiude if(Num > 0)
		}
		// End L.A. Ordinaria

		// ==========================================================================
		// Gestione di L.A. SPECIALE
		// ==========================================================================
		PeriodoLibAntCumuloModel lPeriodoLibAnt_LASPE = null;
		LibAnticipataCumuloModel LibAnt_LASPE = null;

		if ((!isRequestParameterNullObj(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE)
				&& !getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE).equals("")
				&& !getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE)
						.equals("0"))
				&& !lModalita.equals("C") // Casi di INSERIMENTO e MODIFICA
		) {

			// LogF3B.getLogger().debug(" ----- > L.A. SPECIALE Giorni - NumggLA_SPE =
			// "+getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE) );
			LibAnt_LASPE = new LibAnticipataCumuloModel();

			LibAnt_LASPE.setNumeroGiorni(new BigDecimal(
					getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE)));
			LibAnt_LASPE.setCodTipoLicenza("LA");
			LibAnt_LASPE.setTipoLa("LS");

			if ("I".equals(lModalita)) {
				LibAnt_LASPE.setCodOperatoreInserimento(getCodUtenteConnesso());
				LibAnt_LASPE.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				LibAnt_LASPE.setDataInserimento(DateUtils.getSysDate());
			} else if ("M".equals(lModalita)) {
				LibAnt_LASPE.setCodOperatoreInserimento(getCodUtenteConnesso());
				LibAnt_LASPE.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				LibAnt_LASPE.setDataInserimento(DateUtils.getSysDate());
				LibAnt_LASPE.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				LibAnt_LASPE.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				LibAnt_LASPE.setDataAggiornamento(DateUtils.getSysDate());
			}

			LibAnt_LASPE.setTitIdTitoloCumulato(
					getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
			LibAnt_LASPE.setFlagStato(lStato);
			LibAnt_LASPE.setMotivoModifica(null);

			LibAnt_LASPE.setFlagConcesso(Concessione);

			// ==========================================================================
			// LEggo i periodi di LA SPECIALE
			// ==========================================================================

			Date[] lDateInizio_spe = null;
			Date[] lDateFine_spe = null;

			lDateInizio_spe = getRequestDateParameters(
					ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO_SPE,
					ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO_SPE,
					ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_SPE);

			lDateFine_spe = getRequestDateParameters(ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE_SPE,
					ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE_SPE,
					ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE_SPE);

			int num_spe = 0;
			for (int jj = 0; jj < 6; jj++)
				if (lDateInizio_spe[jj] != null && lDateFine_spe[jj] != null)
					num_spe++;

			// LogF3B.getLogger().debug("ci sono "+num_spe+" periodi validi di L.A. SPECIALE");

			if (num_spe > 0) {
				Vector<PeriodoLibAntCumuloModel> lVecPeriodiLASPE = new Vector();

				for (int jj = 0; jj < 6; jj++) {
					if (lDateInizio_spe[jj] != null && lDateFine_spe[jj] != null) {
						lPeriodoLibAnt_LASPE = new PeriodoLibAntCumuloModel();

						lPeriodoLibAnt_LASPE.setDataInizio(lDateInizio_spe[jj]);
						lPeriodoLibAnt_LASPE.setDataFine(lDateFine_spe[jj]);

						lPeriodoLibAnt_LASPE.setFlagStato(lStato);
						lPeriodoLibAnt_LASPE.setMotivoModifica(null);

						if ("I".equals(lModalita)) {
							lPeriodoLibAnt_LASPE.setCodOperatoreInserimento(getCodUtenteConnesso());
							lPeriodoLibAnt_LASPE.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
							lPeriodoLibAnt_LASPE.setDataInserimento(DateUtils.getSysDate());
						} else if ("M".equals(lModalita)) {
							lPeriodoLibAnt_LASPE.setCodOperatoreInserimento(getCodUtenteConnesso());
							lPeriodoLibAnt_LASPE.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
							lPeriodoLibAnt_LASPE.setDataInserimento(DateUtils.getSysDate());
							lPeriodoLibAnt_LASPE.setCodOperatoreAggiornamento(getCodUtenteConnesso());
							lPeriodoLibAnt_LASPE.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
							lPeriodoLibAnt_LASPE.setDataAggiornamento(DateUtils.getSysDate());
						}

						lVecPeriodiLASPE.addElement(lPeriodoLibAnt_LASPE);
					}
				}

				// Aggiungo i Periodi di LA Spec al Model LiberazioneAnticipataCumuloModel
				LibAnt_LASPE.setListaPeriodiLibAnticipate(lVecPeriodiLASPE);
			} // Chiude if(num_spe > 0)
		}
		// End L.A. SPECIALE

		// ==========================================================================
		// Gestione di L.A. INTEGRAZIONE
		// ==============================================

		PeriodoLibAntCumuloModel lPeriodoLibAnt_LAINT = null;
		LibAnticipataCumuloModel LibAnt_LAINT = null;

		if ((!isRequestParameterNullObj(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT)
				&& !getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT).equals("")
				&& !getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT)
						.equals("0"))
				&& !lModalita.equals("C") // Casi di INSERIMENTO e MODIFICA
		) {

			// LogF3B.getLogger().debug(" ----- > L.A. INTEGRAZIONE Giorni - NumggLA_INT =
			// "+getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT) );
			LibAnt_LAINT = new LibAnticipataCumuloModel();
			LibAnt_LAINT.setNumeroGiorni(new BigDecimal(
					getRequestStringParameter(ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT)));

			LibAnt_LAINT.setCodTipoLicenza("LA");
			LibAnt_LAINT.setTipoLa("LI");

			if ("I".equals(lModalita)) {
				LibAnt_LAINT.setCodOperatoreInserimento(getCodUtenteConnesso());
				LibAnt_LAINT.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				LibAnt_LAINT.setDataInserimento(DateUtils.getSysDate());
			} else if ("M".equals(lModalita)) {
				LibAnt_LAINT.setCodOperatoreInserimento(getCodUtenteConnesso());
				LibAnt_LAINT.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				LibAnt_LAINT.setDataInserimento(DateUtils.getSysDate());
				LibAnt_LAINT.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				LibAnt_LAINT.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				LibAnt_LAINT.setDataAggiornamento(DateUtils.getSysDate());
			}

			LibAnt_LAINT.setTitIdTitoloCumulato(
					getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
			LibAnt_LAINT.setFlagStato(lStato);
			LibAnt_LAINT.setMotivoModifica(null);

			LibAnt_LAINT.setFlagConcesso(Concessione);

			// ==========================================================================
			// LEggo i periodi di LA INTEGRAZIONE
			// ==========================================================================
			// __________________________
			Date[] lDateInizio_int = null;
			Date[] lDateFine_int = null;

			lDateInizio_int = getRequestDateParameters(
					ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO_INT,
					ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO_INT,
					ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_INT);

			lDateFine_int = getRequestDateParameters(ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE_INT,
					ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE_INT,
					ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE_INT);

			int num_int = 0;
			for (int jj = 0; jj < 6; jj++)
				if (lDateInizio_int[jj] != null && lDateFine_int[jj] != null)
					num_int++;

			// LogF3B.getLogger().debug("ci sono "+num_int+" periodi validi di L.A. INTEGRAZIONE ");

			if (num_int > 0) {
				Vector<PeriodoLibAntCumuloModel> lVecPeriodiLAINT = new Vector();

				for (int jj = 0; jj < 6; jj++) {
					if (lDateInizio_int[jj] != null && lDateFine_int[jj] != null) {
						lPeriodoLibAnt_LAINT = new PeriodoLibAntCumuloModel();

						lPeriodoLibAnt_LAINT.setDataInizio(lDateInizio_int[jj]);
						lPeriodoLibAnt_LAINT.setDataFine(lDateFine_int[jj]);

						lPeriodoLibAnt_LAINT.setFlagStato(lStato);
						lPeriodoLibAnt_LAINT.setMotivoModifica(null);

						if ("I".equals(lModalita)) {
							lPeriodoLibAnt_LAINT.setCodOperatoreInserimento(getCodUtenteConnesso());
							lPeriodoLibAnt_LAINT.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
							lPeriodoLibAnt_LAINT.setDataInserimento(DateUtils.getSysDate());
						} else if ("M".equals(lModalita)) {
							lPeriodoLibAnt_LAINT.setCodOperatoreInserimento(getCodUtenteConnesso());
							lPeriodoLibAnt_LAINT.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
							lPeriodoLibAnt_LAINT.setDataInserimento(DateUtils.getSysDate());
							lPeriodoLibAnt_LAINT.setCodOperatoreAggiornamento(getCodUtenteConnesso());
							lPeriodoLibAnt_LAINT.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
							lPeriodoLibAnt_LAINT.setDataAggiornamento(DateUtils.getSysDate());
						}

						lVecPeriodiLAINT.addElement(lPeriodoLibAnt_LAINT);
					}
				}

				// Aggiungo i Periodi di LA Spec al Model LiberazioneAnticipataCumuloModel
				LibAnt_LAINT.setListaPeriodiLibAnticipate(lVecPeriodiLAINT);

			} // Chiude if(num_int > 0)

		}
		// End L.A. INTEGRAZIONE

		BigDecimal IdStatoEsec = null;

		if ("I".equals(lModalita)) {
			IdStatoEsec = lCtrlStatoEsec.ExInserisciLiberazioneAnticipataCumulo(lStatoEsec, LibAnt_LA,
					LibAnt_LASPE, LibAnt_LAINT);
		} else if ("M".equals(lModalita)) {
			IdStatoEsec = lCtrlStatoEsec.ExModificaLiberazioneAnticipataCumulo(lStatoEsec, LibAnt_LA,
					LibAnt_LASPE, LibAnt_LAINT);
		} else if ("C".equals(lModalita)) {
			lCtrlStatoEsec.ExCancellaStatoEsecTitoloCumulatoById(
					getRequestBigDecimalParameter(CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO), null);
		}

		String lPage = "";

		if ("I".equals(lModalita) || "M".equals(lModalita)) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioLiberazioneAnticipataCumulo" + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
					+ "&" + ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO + "="
					+ IdStatoEsec;
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActRicercaLiberazioneAnticipataCumulo" + "&"
					+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "="
					+ this.getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO)
					+ "&" + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
					+ this.getRequestBigDecimalParameter(
							ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO);
		}
		return lPage;
	}

}