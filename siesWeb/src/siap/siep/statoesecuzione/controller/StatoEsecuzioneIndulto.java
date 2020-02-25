package siap.siep.statoesecuzione.controller;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.statoesecuzione.model.EventoModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

/**
 * Provvedimento Generico
 * 
 * @author Giselda De Vita
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StatoEsecuzioneIndulto extends StatoEsecuzioneElement {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecuzioneIndulto() {
	}

	public StatoEsecuzioneIndulto(StatoEsecuzioneElement aStat) {
		super(aStat);
	}

	/**
	 * elabora EventoModel
	 */
	public void elabora(siap.sico.evento.model.EventoModel aEvento) {
		// boolean lSorveglianza = false;
		boolean lAnticipazioneEffetti = false;
		EventoModel lEvento = new EventoModel(aEvento);
		StatoEsecuzioneUtilController lUtil = new StatoEsecuzioneUtilController();

		try {
			lEvento.setFamiglia("INDULTO");

			lEvento.setDescrLuogoEmittente(null);
			lEvento.setDescrUfficioEmittente(null);

			// lEvento.setDescrizioneData("emesso in data");
			lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));
			if (lEvento.getCodTipoProvvedimento().equals("26")
					|| lEvento.getCodTipoProvvedimento().equals("12")
					|| lEvento.getCodTipoProvvedimento().equals("03"))
				lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F"));

			// Per l'indulto il legame è al contrario
			// L'ordinanza si lega al provveimento
			// L'ordinanza DEL GE 0284 va visualizzata da sola!

			/*
			 * if(mHashEventiRiferimento!=null && aEvento.getEveIdEvento()!=null &&
			 * mHashEventiRiferimento.containsKey(aEvento.getIdEvento())) { lSorveglianza = true;
			 * 
			 * siap.sico.evento.model.EventoModel lEventoOrdinanza =
			 * (siap.sico.evento.model.EventoModel)mHashEventiRiferimento.get(aEvento.getIdEvento());
			 * 
			 * EventoSorveglianzaModel lEveSorv = new EventoSorveglianzaModel(lEventoOrdinanza);
			 * 
			 * if(lEveSorv.getCodTipoProvvedimento().equals("26") ||
			 * lEveSorv.getCodTipoProvvedimento().equals("12") ||
			 * lEveSorv.getCodTipoProvvedimento().equals("03"))
			 * lEveSorv.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F")); else
			 * lEveSorv.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));
			 * 
			 * lEvento.setEventoSorveglianza(lEveSorv);
			 * 
			 * 
			 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			 * LogF3B.getLogger() siesLogger.info("* * * --> Addizionato MA e SORV" + lEvento); } else {//Non
			 * c'e' Ordinanza // lEvento.setDescrTipoProvvedimento(lEvento.getDescrTipoProvvedimento() + " " +
			 * lEvento.getDescrMotivo()); }
			 */

			// Cerco la Pena Residua sull'hash table
			PenaResiduaModel lPenResStatoMod = (PenaResiduaModel) mHashPenaResidua.get(lEvento.getIdEvento());
			/*---INutile
			// Se la Pena è significativa ( nel senso
			// che i quantum e gli importi sono != 0 )
			// setta a null il campo dies a quo
			// per problemi di compatibilità sui template
			// con i dati migrati da RES
			if (lPenResStatoMod != null && !lPenResStatoMod.isSignificativa())
				lPenResStatoMod.setDiesAQuo(null);*/

			// all'OS
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug(" >>> Ordine Scar INDULTO = " + lEvento.getDescrTipoProvvedimento() + " "
					+ lEvento.getDescrMotivo());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug(" >>> EveIdEvento = " + aEvento.getEveIdEvento());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug(" >>> IdEvento = " + lEvento.getIdEvento());
			if (aEvento.getEveIdEvento() != null)
				lPenResStatoMod = (PenaResiduaModel) mHashPenaResidua.get(aEvento.getEveIdEvento());

			// 0367 String lDescrMotivo = " a seguito di concessione indulto emesso in data " +
			// DateUtils.getDateToString(lEvento.getDataEmissione(), "dd-MM-yyyy");

			// ---------------------
			// Ordine Scarcerazione
			// ---------------------
			if (lPenResStatoMod != null && lEvento.getCodTipoProvvedimento().equals("09")) {

				// Soggetto Scarcerato
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(" >>> Ordine Scar Data Fine = "
						+ DateUtils.getDateToString(lPenResStatoMod.getDataFine(), "dd/MM/yyyy"));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(" >>> Ordine Scar Data Emissione = "
						+ DateUtils.getDateToString(lEvento.getDataEmissione(), "dd/MM/yyyy"));

				if (lPenResStatoMod.getDataFine() != null)
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug(" >>> Ordine Scar Compare = "
							+ lPenResStatoMod.getDataFine().compareTo(lEvento.getDataEmissione()));

				if (lPenResStatoMod.getDataFine() != null
						&& lPenResStatoMod.getDataFine().compareTo(lEvento.getDataEmissione()) < 0) { // Cambia
																										// la
																										// dicitura
																										// per
																										// l'OS
																										// di
																										// soggetto
																										// scarcerato
																										// data
																										// fine
																										// pena
																										// <
																										// data
																										// provvedimento
																										// ---Da
																										// capire...
																										// lDescrMotivo
																										// +=
																										// ".
																										// Scarcerazione
																										// avvenuta
																										// in
																										// data
																										// " +
																										// DateUtils.getDateToString(lPenResStatoMod.getDataFine(),
																										// "dd-MM-yyyy");
				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(" >>> >>> DESCR OS INDULTO = " + lEvento.getDescrTipoProvvedimento() + " "
						+ lEvento.getDescrMotivo());
				// ---da capire lEvento.setDescrMotivo(lDescrMotivo);
				String lStringPena = mCostanti.getProperty("INDULTO_OS_PENA");
				lPenResStatoMod.calcolaStringaArresto();
				lPenResStatoMod.calcolaStringaReclusione();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(" >>> >>> DESCR lPenResStatoMod = " + lPenResStatoMod);
				String lPena = "";
				if (lPenResStatoMod.getStringaReclusione() != null
						&& lPenResStatoMod.getStringaReclusione().length() > 0)
					lPena += lPenResStatoMod.getStringaReclusione() + " "
							+ mCostanti.getProperty("CONC_RECLUSIONE");
				if (lPenResStatoMod.getStringaArresto() != null
						&& lPenResStatoMod.getStringaArresto().length() > 0)
					lPena += " " + lPenResStatoMod.getStringaArresto() + " "
							+ mCostanti.getProperty("CONC_ARRESTO");
				if (lPenResStatoMod.getImportoMulta() != null
						&& lPenResStatoMod.getImportoMulta().intValue() != 0)
					lPena += " " + lPenResStatoMod.getImportoMulta() + " "
							+ mCostanti.getProperty("CONC_MULTA");
				if (lPenResStatoMod.getImportoAmmenda() != null
						&& lPenResStatoMod.getImportoAmmenda().intValue() != 0)
					lPena += " " + lPenResStatoMod.getImportoAmmenda() + " "
							+ mCostanti.getProperty("CONC_AMMENDA");

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug(" >>> >>> DESCR lPenResStatoMod = " + lPena);

				lEvento.setStringaPenaResidua(lStringPena + " " + lPena);

				String lStringAnn = mCostanti.getProperty("PENA_LA");
				if (lPenResStatoMod.getDataFine() != null)
					lEvento.setStringaAnnotazioniTotale(lStringAnn + " "
							+ DateUtils.getDateToString(lPenResStatoMod.getDataFine(), "dd-MM-yyyy"));

			}

			// ================================================================================
			// Annotazioni Manuali
			// ================================================================================
			// Cerco l'annotazione manuale nell'hash table
			// -------------------------------------------------------------------------
			// lAnnoSqlDao.ricercaAnnotazioneManualeByIdEvento(lEveNot.getIdEvento());
			// List lListAnnMod = (ArrayList) lAnnoSqlDao.getModels();
			// -------------------------------------------------------------------------
			Vector lListAnnMod = (Vector) mHashAnnotazioni.get(lEvento.getIdEvento());
			Vector lAnnotazioni = new Vector();
			String lLegge = "";
			String lDpr = "";

			if (lListAnnMod != null) {
				for (Iterator i = lListAnnMod.iterator(); i.hasNext();) {
					AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) i.next();

					if (lAnnMod.getFlagAppProvvisoria().equals("A"))
						lAnticipazioneEffetti = true;

					if (lEvento.getCodTipoProvvedimento() != null
							&& !lEvento.getCodTipoProvvedimento().equals("03")) {
						if (lAnnMod != null && lAnnMod.getDataReclusioneDa() != null
								&& lAnnMod.getDataReclusioneA() != null) {
							lAnnMod.setBeneficio("N");
						} else {
							lAnnMod.setBeneficio("S");
						}
					}

					if (lLegge.length() == 0) {
						lLegge = lAnnMod.getDescrTipoAnnotazione() + " " + lAnnMod.getDescrDpr();
						lDpr = lAnnMod.getDescrDpr();
					}

					lAnnMod.calcolaStringaArresto();
					lAnnMod.calcolaStringaReclusione();
					lAnnotazioni.add(lAnnMod);
				}

				if (lEvento.getCodTipoProvvedimento().equals("26")) {

					String lStringAnn = mCostanti.getProperty("MISURA_INDULTO");
					String lTotaleAnnotazione = lUtil.getAnnotazioneTotale(lAnnotazioni);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("STRINGA ANNOTAZIONEEE =--->>> " + lTotaleAnnotazione);

					if (lTotaleAnnotazione.length() > 0)
						lEvento.setStringaAnnotazioniTotale(lStringAnn + " " + lTotaleAnnotazione);

					// Stringa dopo provv se il condannato è in espiazione
					// AMBROS ----> ??
					if (lAnticipazioneEffetti)
						lEvento.setStringaDopoProvvedimento(mCostanti.getProperty("INDULTO_LIBERO"));
					// }
					// solo per le richieste non per lordine di scarcerazione
					// if(lEvento.getCodTipoProvvedimento().equals("26")||lEvento.getCodMotivo().equals("0367"))
					// {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("MOTIVO PRIMA DI INDULTO =--->>> " + lEvento.getDescrMotivo());
					String lMotivo = lEvento.getDescrMotivo();

					lMotivo = lMotivo.replaceFirst("- ex art. 174 c.p. e 672 c.p.p.", "");
					lEvento.setDescrMotivo(lMotivo + " " + lLegge);

					if (lAnticipazioneEffetti)
						lEvento.setStringaDopoProvvedimento(mCostanti.getProperty("INDULTO_LIBERO"));
				}

				if (lEvento.getCodMotivo().equals("0367"))
					lEvento.setDescrMotivo(lEvento.getDescrMotivo() + " " + lDpr);

				if (lEvento.getCodMotivo().equals("0296"))
					lEvento.setDescrMotivo(lEvento.getDescrMotivo() + " per " + lDpr);

				lEvento.setAnnotazioniManuali(lAnnotazioni);
			}

			// Paolo Cherubini 06/07/2011 sposto questa sezione che era in testa qui sotto l'annotazione
			// questo per testare se l'annotazione ha o meno l'anticipazione degli effetti
			// cosi posso aggiungere il controllo che se ci sono anticipazione degli effetti caricole stringhe
			// di pena residua e decorrenza
			// questo per ovviare che sullo stato di esecuzione non escono tali stringhe per la richiesta di
			// indulto con anticipazione

			if (mHashPenaResidua.containsKey(aEvento.getIdEvento())) {
				if (!aEvento.getCodTipoProvvedimento().equals("12")) {
					if (aEvento.getCodTipoProvvedimento().equals("26") && !lAnticipazioneEffetti) // aggiungo
																									// solo
																									// questo
																									// controllo
																									// !lAnticipazioneEffetti
					{
					} else {
						PenaResiduaModel lPena = (PenaResiduaModel) mHashPenaResidua
								.get(aEvento.getIdEvento());
						lEvento.setPenaResidua(lPena);

						// flag ergastolo
						boolean flagErgastolo = false;

						// Valorizzo la Stringa Decorrenza pena
						if (lPena.getDataInizio() != null) {
							String lPenaDate = mCostanti.getProperty("PENA_DECORRENZA");
							lPenaDate += " " + DateUtils.getDateToString(lPena.getDataInizio(), "dd-MM-yyyy");
							// verifico che non si tratti di ergastolo
							if (lPena.getFlagErgastolo().equals("S")
									|| lPena.getFlagErgastolo().equals("D")) {
								lPenaDate += " " + mCostanti.getProperty("PENA_SCADENZA_ERGASTOLO");
							} else if (lPena.getDataFine() != null) {
								lPenaDate += " " + mCostanti.getProperty("PENA_SCADENZA");
								lPenaDate += " "
										+ DateUtils.getDateToString(lPena.getDataFine(), "dd-MM-yyyy");
							}
							lEvento.setStringaDecorrenzaPenaResidua(lPenaDate);
						}
						// Valorizzo la Stringa pena residua
						// solo se il flag per l'ergastolo = false
						if (!flagErgastolo) {
							lPena.calcolaStringaReclusione();
							lPena.calcolaStringaArresto();
							BigDecimal tmpIndex = new BigDecimal(0);
							String tempPenaRes = "";
							boolean flag_str = false;
							// RECLUSIONE - MULTA
							if (lPena.getStringaReclusione() != null) {
								flag_str = true;
								tempPenaRes += mCostanti.getProperty("PENA_DA_ESPIARE") + " ";
								tempPenaRes += mCostanti.getProperty("PENA_RECLUSIONE");
								tempPenaRes += " " + lPena.getStringaReclusione();
							}
							if (lPena.getImportoMulta().compareTo(tmpIndex) > 0) {
								tempPenaRes += " " + mCostanti.getProperty("PENA_MULTA_EURO");
								tempPenaRes += " " + lPena.getImportoMulta();
							}
							// ARRESTO - AMMENDA
							if (lPena.getStringaArresto() != null) {
								if (!flag_str)
									tempPenaRes += " " + mCostanti.getProperty("PENA_DA_ESPIARE");
								tempPenaRes += " " + mCostanti.getProperty("PENA_ARRESTO");
								tempPenaRes += " " + lPena.getStringaArresto();
							}
							if (lPena.getImportoAmmenda().compareTo(tmpIndex) > 0) {
								tempPenaRes += " " + mCostanti.getProperty("PENA_AMMENDA_EURO");
								tempPenaRes += " " + lPena.getImportoAmmenda();
							}

							lEvento.setStringaPenaResidua(tempPenaRes);
						}
					}
				}
			} // ----

			// -----------------------------------------
			// Cerco la Fungibilità
			// -----------------------------------------
			String lFungibilita = lUtil.getFungibilita(lEvento.getIdEvento());
			if (lFungibilita != null && lFungibilita.length() > 0)
				lEvento.setNotifica1(lFungibilita);

			this.mEventoStatoEsecuzione = lEvento;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("* * * --> Settato Evento" + lEvento);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore in StatoEsecuzioneIndulto", ex);
			ex.printStackTrace();
		}

	}

}