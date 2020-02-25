package siap.siep.calcolopena.action;

/**
 * <p>Title: ActCalcolatrice</p>
 * <p>Description: Azione per il calcolo rapido della pena</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActCalcolatrice extends ActionSiap implements ICostantiCalcoloPena {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// ==========================================================================
		// Il primo record lo carico sempre come pena iniziale con segno positivo,
		// i restanti record come annotazioni con segno
		// ==========================================================================
		PenaResiduaModel lPenaIniziale = new PenaResiduaModel();

		// ==========================================================================
		// Carico i rimanenti quantum e le LA
		// ==========================================================================
		BigDecimal lMaxRecordQuantum = getRequestBigDecimalParameter("max_record_quantum");
		Vector lQuantumVect = new Vector();
		Vector lLibAntVect = new Vector();

		for (int i = 1; i <= lMaxRecordQuantum.intValue(); i++) {
			String lSegno = getRequestStringParameter("PM_" + i);

			// ==========================================================================
			// Recupero quantum, importi
			// ==========================================================================
			if (lSegno != null && lSegno.length() > 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Record " + i);

				AnnotazioneManualeModel lAnnManMod = new AnnotazioneManualeModel();
				if (getRequestStringParameter("AA_Rec_" + i).length() > 0
						|| getRequestStringParameter("MM_Rec_" + i).length() > 0
						|| getRequestStringParameter("GG_Rec_" + i).length() > 0
						|| getRequestStringParameter("Multa_int_" + i).length() > 0
						|| getRequestStringParameter("Multa_dec_" + i).length() > 0
						// Arresti
						|| getRequestStringParameter("AA_Arr_" + i).length() > 0
						|| getRequestStringParameter("MM_Arr_" + i).length() > 0
						|| getRequestStringParameter("GG_Arr_" + i).length() > 0
						|| getRequestStringParameter("Ammenda_int_" + i).length() > 0
						|| getRequestStringParameter("Ammenda_dec_" + i).length() > 0) {

					// Memorizzo la posizione del rigo nella maschera per poter ricaricare
					// il dato nella stessa posizione
					lAnnManMod.setIdAnnotazioneManuale(new BigDecimal(i));

					lAnnManMod.setFlagPiuMeno(lSegno);

					// Quantum Reclusione
					lAnnManMod.setNumAnniReclusione(getRequestBigDecimalParameter("AA_Rec_" + i));
					lAnnManMod.setNumMesiReclusione(getRequestBigDecimalParameter("MM_Rec_" + i));
					lAnnManMod.setNumGiorniReclusione(getRequestBigDecimalParameter("GG_Rec_" + i));

					// Normalizzo i quantum di Reclusione
					CalendarModel lCalAppo = lAnnManMod.getQuantumReclusione();
					CalendarUtil lCalUtil = new CalendarUtil();
					lCalAppo = lCalUtil.ricalcolaGAM(lCalAppo);
					if (lCalAppo.getNumAnni() > 0)
						lAnnManMod.setNumAnniReclusione(new BigDecimal(lCalAppo.getNumAnni()));
					else
						lAnnManMod.setNumAnniReclusione(null);

					if (lCalAppo.getNumMesi() > 0)
						lAnnManMod.setNumMesiReclusione(new BigDecimal(lCalAppo.getNumMesi()));
					else
						lAnnManMod.setNumMesiReclusione(null);

					if (lCalAppo.getNumGiorni() > 0)
						lAnnManMod.setNumGiorniReclusione(new BigDecimal(lCalAppo.getNumGiorni()));
					else
						lAnnManMod.setNumGiorniReclusione(null);

					// Multa
					if ((getRequestStringParameter("Multa_int_" + i) != null
							&& !(getRequestStringParameter("Multa_int_" + i).equals("")))
							|| (getRequestStringParameter("Multa_dec_" + i) != null
									&& !(getRequestStringParameter("Multa_dec_" + i).equals("")))) {
						lAnnManMod.setImportoMulta(new BigDecimal(getRequestStringParameter("Multa_int_" + i)
								+ "." + getRequestStringParameter("Multa_dec_" + i)));
					}

					// Quantum Arresto e Ammenda
					lAnnManMod.setNumAnniArresto(getRequestBigDecimalParameter("AA_Arr_" + i));
					lAnnManMod.setNumMesiArresto(getRequestBigDecimalParameter("MM_Arr_" + i));
					lAnnManMod.setNumGiorniArresto(getRequestBigDecimalParameter("GG_Arr_" + i));

					// Normalizzo i quantum di Arresto
					lCalAppo = lAnnManMod.getQuantumArresto();
					lCalAppo = lCalUtil.ricalcolaGAM(lCalAppo);
					if (lCalAppo.getNumAnni() > 0)
						lAnnManMod.setNumAnniArresto(new BigDecimal(lCalAppo.getNumAnni()));
					else
						lAnnManMod.setNumAnniArresto(null);

					if (lCalAppo.getNumMesi() > 0)
						lAnnManMod.setNumMesiArresto(new BigDecimal(lCalAppo.getNumMesi()));
					else
						lAnnManMod.setNumMesiArresto(null);

					if (lCalAppo.getNumGiorni() > 0)
						lAnnManMod.setNumGiorniArresto(new BigDecimal(lCalAppo.getNumGiorni()));
					else
						lAnnManMod.setNumGiorniArresto(null);
					// lAnnManMod.setQuantumArresto(lCalAppo);

					if ((getRequestStringParameter("Ammenda_int_" + i) != null
							&& !(getRequestStringParameter("Ammenda_int_" + i).equals("")))
							|| (getRequestStringParameter("Ammenda_dec_" + i) != null
									&& !(getRequestStringParameter("Ammenda_int_" + i).equals("")))) {
						lAnnManMod.setImportoAmmenda(
								new BigDecimal(getRequestStringParameter("Ammenda_int_" + i) + "."
										+ getRequestStringParameter("Ammenda_dec_" + i)));
					}

					//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					//// LogF3B.getLogger()
					// siesLogger.debug(""+lAnnManMod.toString2());
				}
				lQuantumVect.add(lAnnManMod);

				// ======================================================================
				// LA
				// ======================================================================
				LicenzaLibAnticipataModel lLibAntMod = new LicenzaLibAnticipataModel();
				if (!isRequestParameterNullObj("GG_LA_" + i)
						&& getRequestStringParameter("GG_LA_" + i).length() > 0
				// && !(getRequestBigDecimalParameter("GG_LA_"+i).compareTo(new BigDecimal(0))==0)
				) {
					lLibAntMod.setIdLicenzaLibanticipata(new BigDecimal(i));

					lLibAntMod.setNumeroGiorni(getRequestBigDecimalParameter("GG_LA_" + i));

					if (lSegno.equals("+")) {
						lLibAntMod.setFlagConcesso("R"); // Concesse
					} else {
						lLibAntMod.setFlagConcesso("C"); // Revocate
					}

					//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					//// LogF3B.getLogger()
					// siesLogger.debug(""+lLibAntMod);
				}
				lLibAntVect.add(lLibAntMod);

			}
		}

		// ==========================================================================
		// Recupero i PRESOFFERTI
		// ==========================================================================
		BigDecimal lMaxRecPresofferto = getRequestBigDecimalParameter("max_rec_presofferto");
		Vector lPresoffertiVect = new Vector();

		for (int i = 1; i <= lMaxRecPresofferto.intValue(); i++) {
			AnnotazioneManualeModel lAnnManMod = new AnnotazioneManualeModel();
			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			//// LogF3B.getLogger()
			// siesLogger.debug("Record = "+i);

			if (!isRequestParameterNullObj("GG_DAL_" + i)
					&& getRequestStringParameter("GG_DAL_" + i).length() > 0) {
				Date lDataDal = getRequestDateParameter("AA_DAL_" + i, "MM_DAL_" + i, "GG_DAL_" + i);
				Date lDataAl = getRequestDateParameter("AA_AL_" + i, "MM_AL_" + i, "GG_AL_" + i);

				CalendarModel lCalPenaEspiata = new CalendarModel();
				CalendarUtil lCalUtil = new CalendarUtil();

				lCalPenaEspiata.setDataInizio(lDataDal);
				lCalPenaEspiata.setDataFine(lDataAl);

				String diesAquo = "S"; // forzato a S per conteggiare anche la data inizio
				if (diesAquo.equals("S")) {
					lCalPenaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiata, false);
				} else {
					lCalPenaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiata, true);
				}
				// Normalizzo i quantum
				lCalPenaEspiata = lCalUtil.ricalcolaGAM(lCalPenaEspiata);

				// Carico i dati nell'annotazione manuale
				lAnnManMod.setIdAnnotazioneManuale(new BigDecimal(i));
				lAnnManMod.setCodTipoAnnotazione("005"); // Presofferto stesso reato
				lAnnManMod.setFlagPiuMeno("-");
				lAnnManMod.setDataReclusioneDa(lDataDal);
				lAnnManMod.setDataReclusioneA(lDataAl);
				lAnnManMod.setNumAnniReclusione(new BigDecimal(lCalPenaEspiata.getNumAnni()));
				lAnnManMod.setNumMesiReclusione(new BigDecimal(lCalPenaEspiata.getNumMesi()));
				lAnnManMod.setNumGiorniReclusione(new BigDecimal(lCalPenaEspiata.getNumGiorni()));
			}

			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			//// LogF3B.getLogger()
			// siesLogger.debug("Presofferto = "+lAnnManMod.toString2());
			lPresoffertiVect.add(lAnnManMod);
		}

		// ==========================================================================
		// Recupero la data di decorrenza se specificata
		// ==========================================================================
		Date lDataDecorrenza = null;
		if (!isRequestParameterNullObj("GG_decorrenza")
				&& getRequestStringParameter("GG_decorrenza").length() > 0) {
			lDataDecorrenza = getRequestDateParameter("AA_decorrenza", "MM_decorrenza", "GG_decorrenza");
		}

		// ==========================================================================
		// Recupero la data di scarcerazione se specificata
		// ==========================================================================
		Date lDataScarcerazione = null;
		Date lDataSistemaPerCalcoli = null;
		if (lDataDecorrenza != null) {
			if (!isRequestParameterNullObj("GG_scarcerazione")
					&& getRequestStringParameter("GG_scarcerazione").length() > 0) {
				lDataScarcerazione = getRequestDateParameter("AA_scarcerazione", "MM_scarcerazione",
						"GG_scarcerazione");
			}

			if (lDataScarcerazione != null) {
				lDataSistemaPerCalcoli = lDataScarcerazione;
			} else {
				Date lOggi = DateUtils.getSysDate();
				// Devo eliminare minuti e secondi dalla sysdate altrimenti le funzioni
				// di compare tra date falliscono
				String lGiorno = DateUtils.getDateToString(lOggi, "dd");
				String lMese = DateUtils.getDateToString(lOggi, "MM");
				String lAnno = DateUtils.getDateToString(lOggi, "yyyy");
				lDataSistemaPerCalcoli = DateUtils.getDate(lAnno, lMese, lGiorno);
			}
		}

		// ==========================================================================
		// Recupero la data di interruzione se specificata
		// ==========================================================================
		Date lDataInterruzione = null;
		if (lDataDecorrenza != null) {
			if (!isRequestParameterNullObj("GG_interruzione")
					&& getRequestStringParameter("GG_interruzione").length() > 0) {
				lDataInterruzione = getRequestDateParameter("AA_interruzione", "MM_interruzione",
						"GG_interruzione");
			}
		}

		// ==========================================================================
		// CARICO la pena iniziale, i computi e le LA nel CalcoloPenaModel ed effettuo
		// i calcoli
		// ==========================================================================
		CalcoloPenaModel lCalcPenaModel = new CalcoloPenaModel();

		lCalcPenaModel.setPenaResiduaManuale(lPenaIniziale);
		lCalcPenaModel.setTipoPenaIniziale(PENA_MANUALE);

		lCalcPenaModel.setIndulto(lQuantumVect);
		lCalcPenaModel.setLibAnticipate(lLibAntVect);
		lCalcPenaModel.setPresoffertoAltroReato(lPresoffertiVect);

		PenaResiduaModel lPenaRideterminata = null;
		lPenaRideterminata = lCalcPenaModel.getPenaDaEspiare(lDataDecorrenza, lDataSistemaPerCalcoli, "all",
				null);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Pena rideterminata: " + lPenaRideterminata);

		// ==========================================================================
		//
		// ==========================================================================
		// Date lDataFine = lPenaRideterminata.getDataFine();
		// if (lDataDecorrenza!=null
		//// && lDataSistemaPerCalcoli!=null
		// && lDataFine!=null
		// && lDataScarcerazione!=null
		//// && DateUtils.isGreater(lDataFine, lDataSistemaPerCalcoli)
		// && DateUtils.isGreater(lDataFine, lDataScarcerazione)
		// )
		// {
		// // E' stata indicata una data scarcerazione < data fine pena calcolata
		// // il che indica una probabile interruzione
		// lDataInterruzione = lDataScarcerazione;
		// }

		// ==========================================================================
		// Calcolo la pena espiata e il residuo da espiare se è stata specificata la
		// data di interruzione.
		// ==========================================================================
		CalendarModel lPenaEspiata = null;
		PenaResiduaModel lPenaResiduaDaInterr = null;
		if (lDataInterruzione != null) {
			CalcoloPenaModel lCalcPenaModelInt = new CalcoloPenaModel();
			PenaResiduaModel lPenaTot = new PenaResiduaModel(lPenaRideterminata);
			lCalcPenaModelInt.calcolaPenaDaSospensione(lPenaTot, lDataInterruzione);
			lPenaEspiata = lCalcPenaModelInt.getPenaEspiata();
			lPenaResiduaDaInterr = lCalcPenaModelInt.getPenaResiduaRicalcolata();
		}

		// ==========================================================================
		// Restituisco la pagina della calcoloatrice che deve precaricare i dati
		// digitati + i risultati del calcolo
		// ==========================================================================
		// setRequestAttribute("aPenaIniziale",lPenaIniziale);
		setRequestAttribute("aListaQuantum", lQuantumVect);
		setRequestAttribute("aListaLA", lLibAntVect);
		setRequestAttribute("aListaPresofferti", lPresoffertiVect);
		setRequestAttribute("aPenaRideterminata", lCalcPenaModel);
		setRequestAttribute("aDataDecorrenza", lDataDecorrenza);
		setRequestAttribute("aDataScarcerazione", lDataScarcerazione);
		setRequestAttribute("aDataInterruzione", lDataInterruzione);
		setRequestAttribute("aPenaResiduaDaInterr", lPenaResiduaDaInterr);
		setRequestAttribute("aPenaEspiata", lPenaEspiata);

		setRequestAttribute("aDaCalcolo", "S");

		String lPage = IWebConstants.ROOT_DIR + "/files/siap/siep/calcolopena/Calcolatrice.jsp";

		return lPage;
	}

}