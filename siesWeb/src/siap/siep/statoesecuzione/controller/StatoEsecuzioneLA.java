package siap.siep.statoesecuzione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.libertaanticipata.dao.LicenzaLibanticipataSqlDAO;
import siap.sico.libertaanticipata.dao.PeriodoLibanticipataSqlDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.siep.fungibilita.dao.FungibilitaSqlDAO;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.statoesecuzione.model.DettaglioLAModel;
import siap.siep.statoesecuzione.model.EventoModel;
import siap.siep.statoesecuzione.model.EventoSorveglianzaModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * Classe preposta all'elaborazione dello stato di esecuzione per gli eventi che necessitano la
 * visualizzazione dei giorni di LA e dei periodi, eventi assegnati alla 'famiglia LA'.
 * 
 * Sono per ora classificati in tale classe le ordinanze di concessione e gli ordini di scarcerazione per
 * rideterminazione pena. Non sono invece classificati in tale Famiglia le comunicazioni nel caso di
 * condannato libero o i nergastolo.
 * 
 * {@link siap.siep.statoesecuzione.controller.CreatorStatoEsecuzione#isEventoConLiberazioneAnticipata}
 * 
 * 
 * @author Giselda De Vita
 */
public class StatoEsecuzioneLA extends StatoEsecuzioneElement {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/* Stringa che indica LA detratta e da detrarre */
	protected String mStringLiberazioneAnticipata = null;

	public StatoEsecuzioneLA() {
	}

	public StatoEsecuzioneLA(StatoEsecuzioneElement aStat) {
		super(aStat);
	}

	/**
	 * Elabora l'elemento dello Stato di Esecuzione. P
	 */
	public void elabora(siap.sico.evento.model.EventoModel aEvento) {
		try {
			EventoModel lEvento = new EventoModel(aEvento);
			lEvento.setFamiglia("LA");

			if (lEvento.getCodTipoProvvedimento().equals("26") // Richiesta
					|| lEvento.getCodTipoProvvedimento().equals("12") // Comunicazione
					|| lEvento.getCodTipoProvvedimento().equals("03") // Ordinanza
			) {
				lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F")); // emessa in data
			} else {
				lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M")); // emesso in data
			}

			lEvento.setData(aEvento.getDataEmissione());
			lEvento.setDescrLuogoEmittente(null);
			lEvento.setDescrUfficioEmittente(null);

			// tags per la composizione delle stringhe nel template
			// lEvento.setStringaRevocaDal(mCostanti.getProperty("REVOCA_DAL"));

			// ========================================================================
			// Ricerco l'ordinanza legata al Provvedimento in questione
			// Recupero anche le LA
			// ========================================================================
			// lEventoEsecuzioneModel.setEventoSorveglianza (...)
			// lEventoEsecuzioneModel.setLibAnticipate (...)
			// lEventoEsecuzioneModel.setStringLiberazioneAnticipata(...)
			// lEventoEsecuzioneModel.setPenaResidua(...);
			// lEventoEsecuzioneModel.setStringaDecorrenzaPenaResidua(...);
			// lEventoEsecuzioneModel.setStringaPenaResidua(...); NON PIU'
			// ========================================================================
			if (mHashEventiRiferimento.containsKey(aEvento.getEveIdEvento())) {
				siap.sico.evento.model.EventoModel lEventoOrdinanza = (siap.sico.evento.model.EventoModel) mHashEventiRiferimento
						.get(aEvento.getEveIdEvento());

				EventoSorveglianzaModel lEveSorv = new EventoSorveglianzaModel(lEventoOrdinanza);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("TROVATO SIUS LEGATO A LA = " + lEveSorv.getIdEvento());

				if (lEveSorv.getCodTipoProvvedimento().equals("26") // Richiesta (?)
						|| lEveSorv.getCodTipoProvvedimento().equals("12") // Comunicazione
						|| lEveSorv.getCodTipoProvvedimento().equals("03") // Ordinanza
				)
					lEveSorv.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F"));
				else
					lEveSorv.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));

				lEvento.setEventoSorveglianza(lEveSorv);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("* * * --> Addizionato LA e SORV ---> * * * " + lEvento.getIdEvento());

				// -----------------------------appendLiberazioneAnticipata();
				// Si recuperano le LA collegate al provvedimento SIUS e si costriusce
				// la relativa stringa
				// Vector lLibAnticipate = ricercaLiberazioneAnticipata(lEveSorv.getIdEvento(),
				// lEvento.getIdEvento());
				// lEvento.setLibAnticipate(lLibAnticipate);
				String lTipoLA = "LA"; // LA o RD per rimedi risarcitori
				if (lEvento.getCodMotivo().equals("5491") || lEvento.getCodMotivo().equals("5492")
						|| lEvento.getCodMotivo().equals("5493") || lEvento.getCodMotivo().equals("5494")
						|| lEvento.getCodMotivo().equals("5495"))
					lTipoLA = "RD";

				// MEV29 -
				Vector<DettaglioLAModel> lListaDettagliLA = ricercaLiberazioneAnticipataNew(
						lEveSorv.getIdEvento(), lEvento.getIdEvento(), lTipoLA);
				lEvento.setDettagliLibAnticipate(lListaDettagliLA);

				// Stringa della LA da inserire d'ora in poi
				// (?????) d.f. 08/2015 in questo IF non si entra mai!!!!!!!!!!
				if (lEvento.getStringLiberazioneAnticipata() != null
						&& lEvento.getStringLiberazioneAnticipata().length() > 0) {
					lEvento.setStringLiberazioneAnticipata(lEvento.getStringLiberazioneAnticipata());

					if (lEvento.getCodTipoProvvedimento().equals("12")) {
						lEvento.setStringDetrazioneLiberazioneAnticipata(mCostanti
								.getProperty("LA_DA_DETRARRE"));
					} else {
						lEvento.setStringDetrazioneLiberazioneAnticipata(mCostanti.getProperty("LA_DETRATTA"));
					}
				}

				// ======================================================================
				// Gestione PENA? Se l'eveneto ha rideterminato la pena, setto la
				// pena sull'evento
				// ======================================================================
				if (mHashPenaResidua.containsKey(aEvento.getIdEvento())) {
					PenaResiduaModel lPena = (PenaResiduaModel) mHashPenaResidua.get(aEvento.getIdEvento());
					lEvento.setPenaResidua(lPena);

					// flag ergastolo
					boolean flagErgastolo = false;

					// Valorizzo la Stringa Decorrenza pena
					if (lPena.getDataFine() != null) {
						String lPenaDate = mCostanti.getProperty("PENA_LA");
						lPenaDate += " " + DateUtils.getDateToString(lPena.getDataFine(), "dd-MM-yyyy");

						// verifico che non si tratti di ergastolo
						if (lPena.getFlagErgastolo().equals("S") || lPena.getFlagErgastolo().equals("D")) {
							lPenaDate += mCostanti.getProperty("PENA_SCADENZA_ERGASTOLO");
							flagErgastolo = true;
						}
						/*
						 * else if(lPena.getDataFine()!=null){ lPenaDate += " " +
						 * mCostanti.getProperty("PENA_SCADENZA"); lPenaDate += " " +
						 * DateUtils.getDateToString(lPena.getDataFine(),"dd-MM-yyyy"); }
						 */
						lEvento.setStringaDecorrenzaPenaResidua(lPenaDate);
					}
					// Valorizzo la Stringa pena residua rideterminata
					// solo se il flag per l'ergastolo = false
					if (!flagErgastolo) {
						/*
						 * lPena.calcolaStringaReclusione(); lPena.calcolaStringaArresto(); BigDecimal
						 * tmpIndex = new BigDecimal(0); String tempPenaRes = ""; boolean flag_str = false;
						 * //RECLUSIONE - MULTA if(lPena.getStringaReclusioneResidua()!=null){ flag_str =
						 * true; tempPenaRes += mCostanti.getProperty("PENA_RIDETERMINATA_DA_ESPIARE") + " ";
						 * tempPenaRes += mCostanti.getProperty("PENA_RECLUSIONE"); tempPenaRes += " " +
						 * lPena.getStringaReclusioneResidua(); }
						 * if(lPena.getImportoMulta().compareTo(tmpIndex)>0){ tempPenaRes += " " +
						 * mCostanti.getProperty("PENA_MULTA_EURO"); tempPenaRes += " " +
						 * lPena.getImportoMulta(); } //ARRESTO - AMMENDA
						 * if(lPena.getStringaArrestoResidua()!=null){ if(!flag_str)tempPenaRes += " " +
						 * mCostanti.getProperty("PENA_RIDETERMINATA_DA_ESPIARE"); tempPenaRes += " " +
						 * mCostanti.getProperty("PENA_ARRESTO"); tempPenaRes += " " +
						 * lPena.getStringaArrestoResidua(); }
						 * if(lPena.getImportoAmmenda().compareTo(tmpIndex)>0){ tempPenaRes += " " +
						 * mCostanti.getProperty("PENA_AMMENDA_EURO"); tempPenaRes += " " +
						 * lPena.getImportoAmmenda(); } lEvento.setStringaPenaResidua(tempPenaRes);
						 */
					}
				}

				// -----------------------------------------
				// Cerco la Fungibilità
				// -----------------------------------------
				FungibilitaModel lFungModel = ricercaFungibilita(lEvento.getIdEvento());
				if (lFungModel != null) {
					lEvento.setFungibilita(lFungModel);
				}

			}

			// lEvento.setStringLiberazioneAnticipata(this.mStringLiberazioneAnticipata);
			lEvento.setDescrMotivo(null);

			this.mEventoStatoEsecuzione = lEvento;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("AAAA* * * --> Settato Evento ---> " + lEvento.getIdEvento());
		}

		catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore in StampaProperties", ex);
			ex.printStackTrace();
		}
	}

	/**
	 * append Liberazione Anticipata all'evento di uno stato esecuzione
	 * 
	 * n.b. il vettore restituito è un vettore di oggetti eterogenei. Contiene: 1 LAModel per le LA concesse
	 * (n.b. contiene solo la Property PeriodoLAModel con la stringa che descrive i periodi) 1 LAModel per
	 * ogni tipologia di non concessione (Inammissibile,Rigettata,NLP) 1 TotalePeriodoModel (gg,mm,aaaa)
	 * contiene il totale giorni concessi 1 FungibilitaModel
	 * 
	 * @param aIdEventoSorveglianza
	 * @param aIdEvento
	 * @return Vector
	 *         <LAModel(C),LAModel(I),LAModel(R),LAModel(NLP),TotalePeriodoModel(1),FungibilitaModel(1)>
	 * @throws F3BException
	 */
//	private Vector ricercaLiberazioneAnticipata(BigDecimal aIdEventoSorveglianza, BigDecimal aIdEvento)
//			throws F3BException {
//		LicenzaLibanticipataSqlDAO lLibDAO = null;
//		PeriodoLibanticipataSqlDAO lPerDao = null;
//		FungibilitaSqlDAO lFunDao = null;
//		FungibilitaModel lFunMod = null;
//
//		Vector lLAModels = new Vector();
//		Vector lLibAnticipate = new Vector();
//
//		Connection lConn = null;
//
//		try {
//			lConn = getDBConnection();
//
//			lLibDAO = new LicenzaLibanticipataSqlDAO(lConn);
//			lPerDao = new PeriodoLibanticipataSqlDAO(lConn);
//			// Liberazione Anticipata + Periodi Liberazione
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
//			siesLogger.info("\n LIBERAZIONE ANTICIPATA -- inizio\n");
//
//			// ========================================================================
//			// In questa prima fase si recuparano tutti i record di LA di CONCESSIONE
//			// al fine di calcolare il totale giorni concessi (somma) e una stringa che
//			// descriva i periodi di concessione (dal-al).
//			// Quindi in conclusione si hanno 2 soli dati finali: totGiorni (int), periodi (String)
//			//
//			// Il risultato dell'elaborazione viene restituito in un oggetto LAModel che ha due properti
//			// - LAModel
//			// - LicenzaLibAnticipataModel (se presenti + LA di concessione sull'evento contiene l'ultima
//			// estratta dalla query)
//			// - Vector <PeriodoLAModel> di fatto contiene UN SOLO oggetto PeriodoLAModel con una sola
//			// property, solo la Descrizione
//			//
//			//
//			// Il LAModel viene poi aggiunto (add) come primo elemento del vettore rilasciato
//			// in output Vector lLAModels = new Vector();
//			//
//			// n.b. il TOTALE giorni concessi non viene caricato in queste strutture.
//			// Viene aggiunto alla fine
//			//
//			//
//			// ========================================================================
//
//			/* Prendere prima i preriodi concessi che possono essere piu' di una Licenza Lib anticipata */
//			lLibDAO.ricercaLicenzaLibanticipataConcessaByEve(aIdEventoSorveglianza);
//			lLibAnticipate = new Vector(lLibDAO.getModels());
//			int lTotGiorni = 0;
//
//			if (lLibAnticipate != null && lLibAnticipate.size() > 0) {
//				Vector lPeriodiLA = new Vector();
//
//				/* Fare l'if sul caso in cui ci sia un solo periodo concesso */
//				if (lLibAnticipate.size() == 1) {
//					// ------------------------------------------------------------
//					// Esiste una sola Licenza lIB Anticipata
//					// ------------------------------------------------------------
//					String lDescrizione = "";
//
//					LAModel lLAMod = new LAModel();
//					LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) lLibAnticipate
//							.firstElement();
//					if (lLibAnt != null) {
//						lLAMod.setLAModel(lLibAnt);
//
//						if (lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) != 0) { // n.b. alcuni
//																							// migrati RES
//																							// hanno
//																							// NUMERO_GIORNI =
//																							// 0
//							lTotGiorni += lLibAnt.getNumeroGiorni().intValue();
//
//							// Ricerca dei Periodi relativi alla licenza
//							lPerDao.ricercaPeriodoLibanticipataByLic(lLibAnt.getIdLicenzaLibanticipata());
//							Vector lPeriodi = new Vector(lPerDao.getModels());
//							/* lLAMod.setPeriodiLA(lPeriodi); */
//							if (lPeriodi != null) {
//								if (lPeriodi.size() == 1) { // QUando il periodo è uno solo....
//									PeriodoLibAnticipataModel lPeriodoModel = (PeriodoLibAnticipataModel) lPeriodi
//											.firstElement();
//
//									lDescrizione += "periodo dal "
//											+ DateUtils.getDateToString(lPeriodoModel.getDataInizio(),
//													"dd-MM-yyyy");
//									lDescrizione += " al "
//											+ DateUtils.getDateToString(lPeriodoModel.getDataFine(),
//													"dd-MM-yyyy");
//								} else {
//									// Piu' di un periodo
//									lDescrizione += "periodi";
//
//									Iterator lIterPeriodi = lPeriodi.iterator();
//									while (lIterPeriodi.hasNext()) {
//										PeriodoLibAnticipataModel lPeriodoModel = (PeriodoLibAnticipataModel) lIterPeriodi
//												.next();
//										lDescrizione += " dal "
//												+ DateUtils.getDateToString(lPeriodoModel.getDataInizio(),
//														"dd-MM-yyyy");
//										lDescrizione += " al "
//												+ DateUtils.getDateToString(lPeriodoModel.getDataFine(),
//														"dd-MM-yyyy");
//
//										if (lIterPeriodi.hasNext())
//											lDescrizione += ",";
//									}
//								}
//							}
//						}
//
//						PeriodoLAModel lPeriodo = new PeriodoLAModel();
//						lPeriodo.setDescrizione(lDescrizione);
//
//						lPeriodiLA.add(lPeriodo);
//					}
//					lLAMod.setPeriodiLA(lPeriodiLA);
//					lLAModels.add(lLAMod);
//
//				} else {
//					// -------------------------------------------------------
//					// Esiste piu' di una lib licenza anticipata
//					// -------------------------------------------------------
//					LAModel lLAMod = new LAModel();
//					String lDescrizione = "periodi";
//
//					// n.b. tutto questo ciclo serve solo a valorizzare: lTotGiorni e lDescrizione
//					// tutti gli altri dati sono non significativi
//					for (int i = 0; i < lLibAnticipate.size(); i++) {
//						if (i != 0)
//							lDescrizione += ",";
//
//						LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) lLibAnticipate.get(i);
//
//						if (lLibAnt != null && lLibAnt.getFlagConcesso() != null) {
//							lLAMod.setLAModel(lLibAnt); // ?? sovrascrive l'utimo inserito
//
//							if (lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) != 0) {// prendo i
//																								// periodi
//								lTotGiorni += lLibAnt.getNumeroGiorni().intValue();
//
//								lPeriodiLA = new Vector(); // se si istanzia uno nuovo, contiene solo gli
//															// ultimi
//								// Ricerca dei Periodi relativi alla licenza
//								lPerDao.ricercaPeriodoLibanticipataByLic(lLibAnt.getIdLicenzaLibanticipata());
//								Vector lPeriodi = new Vector(lPerDao.getModels());
//								/* lLAMod.setPeriodiLA(lPeriodi); */
//								if (lPeriodi != null) {
//									Iterator lIterPeriodi = lPeriodi.iterator();
//									while (lIterPeriodi.hasNext()) {
//										PeriodoLibAnticipataModel lPeriodoModel = (PeriodoLibAnticipataModel) lIterPeriodi
//												.next();
//										lDescrizione += " dal "
//												+ DateUtils.getDateToString(lPeriodoModel.getDataInizio(),
//														"dd-MM-yyyy");
//										lDescrizione += " al "
//												+ DateUtils.getDateToString(lPeriodoModel.getDataFine(),
//														"dd-MM-yyyy");
//
//										if (lIterPeriodi.hasNext())
//											lDescrizione += ",";
//									}
//
//									PeriodoLAModel lPeriodo = new PeriodoLAModel();
//									lPeriodo.setDescrizione(lDescrizione);
//
//									lPeriodiLA.add(lPeriodo);
//								}
//							}
//						}
//
//					}
//
//					// Aggiungo un solo model per tutti i record concessi
//					// palese porcata: lLAMod e lPeriodiLA vengono istanziati ogni volta
//					// nel ciclo per cui si esce solo con l'ultimo record. I precedenti
//					// vengono persi. Tanto valeva uscire direttamente con lDescrizione
//					// Inoltre lLAMod è l'ultima LA ma non significativa, una vale l'altra,
//					// il dato non viene utilizzato
//					lLAMod.setPeriodiLA(lPeriodiLA);
//					lLAModels.add(lLAMod);
//
//				}
//
//			}
//
//			lLibDAO.stop();
//
//			// ========================================================================
//			// Recupero se presenti i periodi NON concessi: R=rigettati, I=Inammissibili
//			// N = non di
//			// Per costruire l'opportuna stringa da visualizzare. La stringa viene caricata
//			// sempre in un oggetto LAModel.
//			// - LAModel
//			// - LicenzaLibAnticipataModel (se presenti + LA di rigetto sull'evento contiene l'ultima estratta
//			// dalla query)
//			// - Vector <PeriodoLAModel> di fatto contiene UN SOLO oggetto PeriodoLAModel con una sola
//			// property, solo la Descrizione
//			// Anche questo oggetto LAModel viene aggiunto come secondo elemento del
//			// vettore di output.
//			// ========================================================================
//			lLibDAO.ricercaLicenzaLibanticipataByEve(aIdEventoSorveglianza);
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
//			siesLogger.info("\n LIBERAZIONE ANTICIPATA -- fine\n");
//
//			lLibAnticipate = new Vector(lLibDAO.getModels());
//
//			for (int i = 0; i < lLibAnticipate.size(); i++) {
//				LAModel lLAMod = new LAModel();
//				LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) lLibAnticipate.get(i);
//
//				if (lLibAnt != null && lLibAnt.getFlagConcesso() != null
//						&& !(lLibAnt.getFlagConcesso().equals("C")))
//				// && (lLibAnt.getFlagConcesso().equals("C")||lLibAnt.getFlagConcesso().equals("C")))
//				// && (lLibAnt.getFlagElaborato() == null || lLibAnt.getFlagElaborato().equals("N") ||
//				// lLibAnt.getFlagElaborato().equals("E")))
//				{
//					lLAMod.setLAModel(lLibAnt);
//
//					if (lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) != 0) {
//						Vector lPeriodiLA = new Vector();
//
//						// Ricerca dei Periodi relativi alla licenza
//						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
//						// di mLog
//						siesLogger.info("\n PERIODI LIBERAZIONE ANTICIPATA -- inizio\n");
//						lPerDao.ricercaPeriodoLibanticipataByLic(lLibAnt.getIdLicenzaLibanticipata());
//
//						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
//						// di mLog
//						siesLogger.info("\n PERIODI LIBERAZIONE ANTICIPATA -- inizio\n");
//
//						Vector lPeriodi = new Vector(lPerDao.getModels());
//						/* lLAMod.setPeriodiLA(lPeriodi); */
//						if (lPeriodi != null) {
//							Iterator lIterPeriodi = lPeriodi.iterator();
//							PeriodoLAModel lPeriodo = new PeriodoLAModel();
//
//							String lDescrizione = "";
//
//							if (lLibAnt.getFlagConcesso().equals("R"))
//								lDescrizione = mCostanti.getProperty("LA_RIGETTA") + " "; // Rigetta
//
//							if (lLibAnt.getFlagConcesso().equals("I"))
//								lDescrizione = mCostanti.getProperty("LA_INAMMISSIBILE") + " "; // Dichiara
//																								// inammissibile
//
//							if (lLibAnt.getFlagConcesso().equals("N"))
//								lDescrizione = mCostanti.getProperty("LA_NDP") + " "; // Dichiara ndp\nlp
//
//							if (lPeriodi.size() == 1) { // QUando il periodo è uno solo....
//								PeriodoLibAnticipataModel lPeriodoModel = (PeriodoLibAnticipataModel) lPeriodi
//										.firstElement();
//
//								lDescrizione += "periodo dal "
//										+ DateUtils.getDateToString(lPeriodoModel.getDataInizio(),
//												"dd-MM-yyyy");
//								lDescrizione += " al "
//										+ DateUtils
//												.getDateToString(lPeriodoModel.getDataFine(), "dd-MM-yyyy");
//							} else {
//								// Piu' di un periodo
//								lDescrizione += "periodi";
//
//								while (lIterPeriodi.hasNext()) {
//									PeriodoLibAnticipataModel lPeriodoModel = (PeriodoLibAnticipataModel) lIterPeriodi
//											.next();
//									lDescrizione += " dal "
//											+ DateUtils.getDateToString(lPeriodoModel.getDataInizio(),
//													"dd-MM-yyyy");
//									lDescrizione += " al "
//											+ DateUtils.getDateToString(lPeriodoModel.getDataFine(),
//													"dd-MM-yyyy");
//
//									if (lIterPeriodi.hasNext())
//										lDescrizione += ",";
//								}
//
//							}
//
//							lPeriodo.setDescrizione(lDescrizione);
//							lPeriodiLA.add(lPeriodo);
//
//						}
//						lLAMod.setPeriodiLA(lPeriodiLA);
//					}
//				}
//
//				lLAModels.add(lLAMod);
//			}
//
//			// ========================================================================
//			// Aggiunge il totale dei giorni di Licenza Anticipata in un oggetto
//			// TotalePeriodoModel aggiunto al vettore di output
//			// ========================================================================
//			if (lTotGiorni != 0) {
//				TotalePeriodoModel lTotLibAnt = new TotalePeriodoModel(0, 0, lTotGiorni);
//				lLAModels.add(lTotLibAnt);
//				/*
//				 * TreeModel lTreeTotLibAnt = new TreeModel(lTotLibAnt); lStatEve.add(lTreeTotLibAnt);
//				 */
//			}
//
//			// FRASE_LA=Liberazione Anticipata gg
//			this.mStringLiberazioneAnticipata = mCostanti.getProperty("FRASE_LA") + " " + lTotGiorni + " ";
//
//			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
//			// siesLogger.info("Settata la stringa di LA === " + getStringLiberazioneAnticipata);
//
//			// + mCostanti.getProperty("LA_DA_DETRARRE"));
//
//			// -----------------------------------------
//			// Cerco la Fungibilità
//			// -----------------------------------------
//
//			lFunDao = new FungibilitaSqlDAO(lConn);
//			lFunDao.ricercaFungibilitaByKeyEvento(aIdEvento);
//			lFunMod = (FungibilitaModel) lFunDao.getModelByKey();
//
//			if (lFunMod != null) {
//				lFunMod.calcolaStringaFungibilita();
//				lFunMod.setStringaFungibilita(mCostanti.getProperty("LA_FUNGIBILITA") + " "
//						+ lFunMod.getStringaFungibilita());
//				lLAModels.add(lFunMod);
//			}
//
//		} catch (DAOException daoEx) {
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
//			siesLogger.error("StatoEsecuzioneLA.appendLiberazioneAnticipata: " + daoEx, daoEx);
//			throw new F3BException("StatoEsecuzioneLA.ricercaLiberazioneAnticipata: " + daoEx);
//		} catch (Exception sqe) {
//			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
//			siesLogger.error("StatoEsecuzioneLA.appendLiberazioneAnticipata: " + sqe, sqe);
//			throw new F3BException("StatoEsecuzioneLA.ricercaLiberazioneAnticipata: Eccezione Generica: "
//					+ sqe);
//		} finally {
//			cleanup(lLibDAO);
//			cleanup(lPerDao);
//			cleanup(lFunDao);
//			cleanup(lConn);
//		}
//
//		// debug
//		for (int i = 0; i < lLAModels.size(); i++) {
//			Object lOggetto = lLAModels.elementAt(i);
//
//			if (lOggetto instanceof LAModel) {
//				Vector lPeriodi = ((LAModel) lOggetto).getPeriodiLA();
//				if (lPeriodi != null && lPeriodi.size() > 0)
//					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//					// LogF3B.getLogger()
//					siesLogger.debug("DF Periodi = "
//							+ ((PeriodoLAModel) lPeriodi.elementAt(i)).getDescrizione());
//			} else if (lOggetto instanceof TotalePeriodoModel) {
//				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//				// LogF3B.getLogger()
//				siesLogger.debug("DF Tot Giorni = " + ((TotalePeriodoModel) lOggetto).getTotaleGiorni());
//			}
//		}
//
//		return lLAModels;
//	}

	/**
	 * Test per recuperare le LA concesse sull'evento distinguendo tra LA,LS,LE e relativi periodi
	 * 
	 * @param aIdEventoSorveglianza
	 * @param aIdEvento
	 * @return
	 * @throws F3BException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Vector<DettaglioLAModel> ricercaLiberazioneAnticipataNew(BigDecimal aIdEventoSorveglianza,
			BigDecimal aIdEvento, String aTipoLA) throws F3BException {

		LicenzaLibanticipataSqlDAO lLibSqlDAO = null;
		PeriodoLibanticipataSqlDAO lPerSqlDAO = null;

		Vector<DettaglioLAModel> lVectOut = new Vector<DettaglioLAModel>();

		Vector<LicenzaLibAnticipataModel> lLibAnticipate = null;

		Connection lConn = null;

		try {
			lConn = getDBConnection();

			lLibSqlDAO = new LicenzaLibanticipataSqlDAO(lConn);
			lPerSqlDAO = new PeriodoLibanticipataSqlDAO(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("\n LIBERAZIONE ANTICIPATA -- inizio\n");

			// ========================================================================
			// In questa prima fase si recuparano tutti i record di LA di CONCESSIONE
			// al fine di calcolare il totale giorni concessi (somma) e una stringa che
			// descriva i periodi di concessione (dal-al).
			// Quindi in conclusione si hanno 2 soli dati finali: totGiorni (int), periodi (String)
			//
			// ========================================================================

			/* Prendere prima i periodi concessi che possono essere piu' di una Licenza Lib anticipata */
			if ("RD".equals(aTipoLA)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("RICERCO RIMEDI RISARCITORI");
				lLibSqlDAO.ricercaRimediRisarcitoriESommeConcessiByEve(aIdEventoSorveglianza);
			} else {
				lLibSqlDAO.ricercaLicenzaLibanticipataConcessaByEve(aIdEventoSorveglianza);
			}
			// FIXME provare ad utilizzare ricercaLicenzaLibanticipataByEve che recupera TUTTE le licenze
			// collegate all'evento

			lLibAnticipate = new Vector<LicenzaLibAnticipataModel>(lLibSqlDAO.getModels());

			int lTotGiorniLA = 0;
			int lTotGiorniLS = 0;
			int lTotGiorniLI = 0;

			int lTotGiorniRD = 0; // DL92
			BigDecimal lSommaLiquidata = null; // DL92

			int lContaPeriodiLA = 0;
			int lContaPeriodiLS = 0;
			int lContaPeriodiLI = 0;
			int lContaPeriodiRD = 0; //
			int lContaPeriodiSL = 0; // Somma Liquidata DL92

			String lStrPeriodiLA = "";
			String lStrPeriodiLS = "";
			String lStrPeriodiLI = "";
			String lStrPeriodiRD = "";
			String lStrPeriodiSL = "";

			if (lLibAnticipate != null && lLibAnticipate.size() > 0) {
				// n.b. tutto questo ciclo serve solo a valorizzare: lTotGiorni e lDescrizione
				// tutti gli altri dati sono non significativi
				for (int i = 0; i < lLibAnticipate.size(); i++) {
					LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) lLibAnticipate.get(i);

					if (lLibAnt != null && lLibAnt.getFlagConcesso() != null) {
						if (lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) != 0
								|| (lLibAnt.getSommaRisarcDanni() != null && lLibAnt.getSommaRisarcDanni()
										.intValue() > 0)) {
							// Calcolo il totale per tipologia.
							// n.b. DescrStatoPermesso viene utilizzato dal DL 146, le
							// precedenti LA NON HANNO IL CAMPO VALORIZZATO. Inoltre il campo
							// vale LA o LAU per i periodi unici
							if (lLibAnt.getCodTipoLicenza().equals("LA")) {
								if (lLibAnt.getDescrStatoPermesso() != null) {
									if (lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LA"))
										lTotGiorniLA += lLibAnt.getNumeroGiorni().intValue();
									else if (lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LS"))
										lTotGiorniLS += lLibAnt.getNumeroGiorni().intValue();
									else if (lLibAnt.getDescrStatoPermesso().substring(0, 2).equals("LI"))
										lTotGiorniLI += lLibAnt.getNumeroGiorni().intValue();
								} else
									lTotGiorniLA += lLibAnt.getNumeroGiorni().intValue();
							} else if (lLibAnt.getCodTipoLicenza().equals("RD")) {
								lTotGiorniRD += lLibAnt.getNumeroGiorni().intValue();
							} else if (lLibAnt.getCodTipoLicenza().equals("SL")) {
								// n.b. esiste un solo record per la Somma Liqidata
								lSommaLiquidata = lLibAnt.getSommaRisarcDanni();
							}

							// Ricerca dei Periodi relativi alla licenza (LA,LI,LS
							lPerSqlDAO.ricercaPeriodoLibanticipataByLic(lLibAnt.getIdLicenzaLibanticipata());
							Vector lPeriodi = new Vector(lPerSqlDAO.getModels());

							if (lPeriodi != null) {
								Iterator lIterPeriodi = lPeriodi.iterator();
								while (lIterPeriodi.hasNext()) {
									PeriodoLibAnticipataModel lPeriodoModel = (PeriodoLibAnticipataModel) lIterPeriodi
											.next();

									if (lLibAnt.getCodTipoLicenza().equals("LA")) {
										// solo le nuove LA hanno DescrStatoPermesso valorizzato
										if (lLibAnt.getDescrStatoPermesso() != null) {
											if (lLibAnt.getDescrStatoPermesso().substring(0, 2)
													.equals("LA")) {
												lContaPeriodiLA++;
												if (lContaPeriodiLA > 1)
													// Ticket#202001070115 — STAMPA CERTIFICATO ESECUZIONE
													// (STATO DI ESECUZIONE) DA ALTRO UFFICIO --> modifica
													// globale anche per LA (LA, LS, LI), RD e SL
													// 20200108 [SG]: per la stampa in .pdf mando a capo se ci
													// sono più periodi di LA --> \n
													// anche per la dicitura: "relativamente ai periodi"
													lStrPeriodiLA += ",\n";
												lStrPeriodiLA += " dal " + DateUtils.getDateToString(
														lPeriodoModel.getDataInizio(), "dd-MM-yyyy");
												lStrPeriodiLA += " al " + DateUtils.getDateToString(
														lPeriodoModel.getDataFine(), "dd-MM-yyyy");
											} else if (lLibAnt.getDescrStatoPermesso().substring(0, 2)
													.equals("LS")) {
												lContaPeriodiLS++;
												if (lContaPeriodiLS > 1)
													lStrPeriodiLS += ",\n";
												lStrPeriodiLS += " dal " + DateUtils.getDateToString(
														lPeriodoModel.getDataInizio(), "dd-MM-yyyy");
												lStrPeriodiLS += " al " + DateUtils.getDateToString(
														lPeriodoModel.getDataFine(), "dd-MM-yyyy");
											} else if (lLibAnt.getDescrStatoPermesso().substring(0, 2)
													.equals("LI")) {
												lContaPeriodiLI++;
												if (lContaPeriodiLI > 1)
													lStrPeriodiLI += ",\n";
												lStrPeriodiLI += " dal " + DateUtils.getDateToString(
														lPeriodoModel.getDataInizio(), "dd-MM-yyyy");
												lStrPeriodiLI += " al " + DateUtils.getDateToString(
														lPeriodoModel.getDataFine(), "dd-MM-yyyy");
											}
										} else {
											lContaPeriodiLA++;
											if (lContaPeriodiLA > 1)
												lStrPeriodiLA += ",\n";
											lStrPeriodiLA += " dal " + DateUtils.getDateToString(
													lPeriodoModel.getDataInizio(), "dd-MM-yyyy");
											lStrPeriodiLA += " al " + DateUtils.getDateToString(
													lPeriodoModel.getDataFine(), "dd-MM-yyyy");
										}
									} else if (lLibAnt.getCodTipoLicenza().equals("RD")) {
										lContaPeriodiRD++;
										if (lContaPeriodiRD > 1)
											lStrPeriodiRD += ",\n";
										lStrPeriodiRD += " dal " + DateUtils
												.getDateToString(lPeriodoModel.getDataInizio(), "dd-MM-yyyy");
										lStrPeriodiRD += " al " + DateUtils
												.getDateToString(lPeriodoModel.getDataFine(), "dd-MM-yyyy");
									} else if (lLibAnt.getCodTipoLicenza().equals("SL")) {
										lContaPeriodiSL++;
										if (lContaPeriodiSL > 1)
											lStrPeriodiSL += ",\n";
										lStrPeriodiSL += " dal " + DateUtils
												.getDateToString(lPeriodoModel.getDataInizio(), "dd-MM-yyyy");
										lStrPeriodiSL += " al " + DateUtils
												.getDateToString(lPeriodoModel.getDataFine(), "dd-MM-yyyy");
									}
								}
							}
						}
					}
				}

				// Completo le stringhe dei periodi in funzione del numero di periodi
				if (lContaPeriodiLA == 1)
					lStrPeriodiLA = "relativamente al periodo" + lStrPeriodiLA;
				else if (lContaPeriodiLA > 1)
					lStrPeriodiLA = "relativamente ai periodi\n" + lStrPeriodiLA;

				if (lContaPeriodiLS == 1)
					lStrPeriodiLS = "relativamente al periodo" + lStrPeriodiLS;
				else if (lContaPeriodiLS > 1)
					lStrPeriodiLS = "relativamente ai periodi\n" + lStrPeriodiLS;

				if (lContaPeriodiLI == 1)
					lStrPeriodiLI = "relativamente al periodo" + lStrPeriodiLI;
				else if (lContaPeriodiLI > 1)
					lStrPeriodiLI = "relativamente ai periodi\n" + lStrPeriodiLI;

				if (lContaPeriodiRD == 1)
					lStrPeriodiRD = "relativamente al periodo" + lStrPeriodiRD;
				else if (lContaPeriodiRD > 1)
					lStrPeriodiRD = "relativamente ai periodi\n" + lStrPeriodiRD;

				if (lContaPeriodiSL == 1)
					lStrPeriodiSL = "relativamente al periodo" + lStrPeriodiSL;
				else if (lContaPeriodiSL > 1)
					lStrPeriodiSL = "relativamente ai periodi\n" + lStrPeriodiSL;

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("LA: " + lTotGiorniLA + " " + lStrPeriodiLA);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("LS: " + lTotGiorniLS + " " + lStrPeriodiLS);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("LI: " + lTotGiorniLI + " " + lStrPeriodiLI);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("RD: " + lTotGiorniRD + " " + lStrPeriodiRD);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("SL: " + lSommaLiquidata + " " + lStrPeriodiSL);

				if (lTotGiorniLA > 0) {
					DettaglioLAModel lDettLAMod = new DettaglioLAModel();
					lDettLAMod.setTipoLA(DettaglioLAModel.LA_ORDINARIA);
					lDettLAMod.setDescTipoLA("Liberazione Anticipata");
					lDettLAMod.setTotGiorni(new BigDecimal(lTotGiorniLA));
					lDettLAMod.setDescrPeriodi(lStrPeriodiLA);

					lVectOut.add(lDettLAMod);
				}

				if (lTotGiorniLS > 0) {
					DettaglioLAModel lDettLAMod = new DettaglioLAModel();
					lDettLAMod.setTipoLA(DettaglioLAModel.LA_SPECIALE);
					lDettLAMod.setDescTipoLA("Liberazione Anticipata Speciale");
					lDettLAMod.setTotGiorni(new BigDecimal(lTotGiorniLS));
					lDettLAMod.setDescrPeriodi(lStrPeriodiLS);

					lVectOut.add(lDettLAMod);
				}

				if (lTotGiorniLI > 0) {
					DettaglioLAModel lDettLAMod = new DettaglioLAModel();
					lDettLAMod.setTipoLA(DettaglioLAModel.LA_INTEGRAZIONE);
					lDettLAMod.setDescTipoLA("Integrazione Liberazione Anticipata");
					lDettLAMod.setTotGiorni(new BigDecimal(lTotGiorniLI));
					lDettLAMod.setDescrPeriodi(lStrPeriodiLI);

					lVectOut.add(lDettLAMod);
				}

				if (lTotGiorniRD > 0) {
					DettaglioLAModel lDettLAMod = new DettaglioLAModel();
					lDettLAMod.setTipoLA(DettaglioLAModel.RD_RISARCIMENTO);
					lDettLAMod.setDescTipoLA("a titolo risarcimento danni D.L. 92/2014 ");
					lDettLAMod.setTotGiorni(new BigDecimal(lTotGiorniRD));
					lDettLAMod.setDescrPeriodi(lStrPeriodiRD);

					lVectOut.add(lDettLAMod);
				}

				if (lSommaLiquidata != null && lSommaLiquidata.intValue() > 0) {
					DettaglioLAModel lDettLAMod = new DettaglioLAModel();
					lDettLAMod.setTipoLA(DettaglioLAModel.RD_SOMMALIQUIDATA);
					lDettLAMod.setDescTipoLA("a titolo risarcimento danni D.L. 92/2014 ");
					lDettLAMod.setSommaLiquidata(lSommaLiquidata);
					lDettLAMod.setDescrPeriodi(lStrPeriodiSL);

					lVectOut.add(lDettLAMod);
				}

			}

			lLibSqlDAO.stop();

			// ========================================================================
			// Recupero se presenti i periodi NON concessi: R=rigettati, I=Inammissibili
			// N = non luogo a procedere
			// ========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Recupero i periodi NON concessi");
			lLibSqlDAO.ricercaLicenzaLibanticipataByEve(aIdEventoSorveglianza);

			lLibAnticipate = new Vector(lLibSqlDAO.getModels());

			for (int i = 0; i < lLibAnticipate.size(); i++) {
				LicenzaLibAnticipataModel lLibAnt = (LicenzaLibAnticipataModel) lLibAnticipate.get(i);

				if (lLibAnt != null && lLibAnt.getFlagConcesso() != null
						&& !lLibAnt.getFlagConcesso().equals("C") // ?? attenzione recupera anche le S già
																	// recuperate dalla prima queri
				) {
					if (lLibAnt.getNumeroGiorni().compareTo(new BigDecimal(0)) == 0) {
						// n.b. presenti a sistema dati migrati RES con getFlagConcesso = R e giorni
						// valorizzati
						// Ricerca dei Periodi relativi alla licenza
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("Estrazione PERIODI non concessi ");
						lPerSqlDAO.ricercaPeriodoLibanticipataByLic(lLibAnt.getIdLicenzaLibanticipata());

						Vector lPeriodi = new Vector(lPerSqlDAO.getModels());

						if (lPeriodi != null) {
							Iterator lIterPeriodi = lPeriodi.iterator();

							String lDescrizione = "";

							if (lLibAnt.getFlagConcesso().equals("R"))
								lDescrizione = mCostanti.getProperty("LA_RIGETTA") + " "; // Rigetta

							if (lLibAnt.getFlagConcesso().equals("I"))
								lDescrizione = mCostanti.getProperty("LA_INAMMISSIBILE") + " "; // Dichiara
																								// inammissibile

							if (lLibAnt.getFlagConcesso().equals("N"))
								lDescrizione = mCostanti.getProperty("LA_NDP") + " "; // Dichiara ndp\nlp

							if (lPeriodi.size() == 1) { // Quando il periodo è uno solo....
								PeriodoLibAnticipataModel lPeriodoModel = (PeriodoLibAnticipataModel) lPeriodi
										.firstElement();

								lDescrizione += "periodo dal "
										+ DateUtils.getDateToString(lPeriodoModel.getDataInizio(),
												"dd-MM-yyyy");
								lDescrizione += " al "
										+ DateUtils
												.getDateToString(lPeriodoModel.getDataFine(), "dd-MM-yyyy");
							} else {
								// Piu' di un periodo
								lDescrizione += "periodi";

								while (lIterPeriodi.hasNext()) {
									PeriodoLibAnticipataModel lPeriodoModel = (PeriodoLibAnticipataModel) lIterPeriodi
											.next();
									lDescrizione += " dal "
											+ DateUtils.getDateToString(lPeriodoModel.getDataInizio(),
													"dd-MM-yyyy");
									lDescrizione += " al "
											+ DateUtils.getDateToString(lPeriodoModel.getDataFine(),
													"dd-MM-yyyy");

									if (lIterPeriodi.hasNext())
										lDescrizione += ",";
								}
							}

							DettaglioLAModel lDettLAMod = new DettaglioLAModel();
							if (lLibAnt.getFlagConcesso().equals("R"))
								lDettLAMod.setTipoLA(DettaglioLAModel.LA_RIGETTATI);
							else if (lLibAnt.getFlagConcesso().equals("I"))
								lDettLAMod.setTipoLA(DettaglioLAModel.LA_INAMMISSIBILI);
							else if (lLibAnt.getFlagConcesso().equals("N"))
								lDettLAMod.setTipoLA(DettaglioLAModel.LA_NONLUOGO);

							lDettLAMod.setTotGiorni(null);
							lDettLAMod.setDescrPeriodi(lDescrizione);

							lVectOut.add(lDettLAMod);
						}
					}
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Concesso: " + lLibAnt);
				}
			}

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StatoEsecuzioneLA.ricercaLiberazioneAnticipataNew: " + daoEx, daoEx);
			throw new F3BException("StatoEsecuzioneLA.ricercaLiberazioneAnticipataNew: " + daoEx);
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StatoEsecuzioneLA.ricercaLiberazioneAnticipataNew: " + sqe, sqe);
			throw new F3BException("StatoEsecuzioneLA.ricercaLiberazioneAnticipataNew: Eccezione Generica: "
					+ sqe);
		} finally {
			cleanup(lLibSqlDAO);
			cleanup(lPerSqlDAO);

			cleanup(lConn);
		}

		// debug
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		if (siesLogger.isDebugEnabled()) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Riepilogo LA estratte");
			for (int i = 0; i < lVectOut.size(); i++) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("" + (DettaglioLAModel) lVectOut.elementAt(i));
			}
		}

		return lVectOut;
	}

	/**
	 * Verifica se presente fungibilità associata all'evento (es Ordinene di scarceraczione)
	 * 
	 * @param aIdEvento
	 * @return
	 */
	private FungibilitaModel ricercaFungibilita(BigDecimal aIdEvento) throws F3BException {

		FungibilitaSqlDAO lFunSqlDao = null;
		FungibilitaModel lFunMod = null;

		Connection lConn = null;

		try {
			lConn = getDBConnection();

			lFunSqlDao = new FungibilitaSqlDAO(lConn);
			lFunSqlDao.ricercaFungibilitaByKeyEvento(aIdEvento);
			lFunMod = (FungibilitaModel) lFunSqlDao.getModelByKey();

			if (lFunMod != null) {
				lFunMod.calcolaStringaFungibilita();
				lFunMod.setStringaFungibilita(mCostanti.getProperty("LA_FUNGIBILITA") + " "
						+ lFunMod.getStringaFungibilita());
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StatoEsecuzioneLA.ricercaFungibilita: " + daoEx, daoEx);
			throw new F3BException("StatoEsecuzioneLA.ricercaFungibilita: " + daoEx);
		} catch (Exception sqe) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("StatoEsecuzioneLA.ricercaFungibilita: " + sqe, sqe);
			throw new F3BException("StatoEsecuzioneLA.ricercaFungibilita: Eccezione Generica: " + sqe);
		} finally {
			cleanup(lFunSqlDao);

			cleanup(lConn);
		}

		return lFunMod;
	}

}