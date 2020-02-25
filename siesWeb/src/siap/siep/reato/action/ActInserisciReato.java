package siap.siep.reato.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciReato
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Reato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciReato extends ActionSiap implements ICostantiReato {

	// [EC] - 16/10/2017 - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Reato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	/**
	 * Elenco Reati da inserire. Definito come membro di classe per poter essere utilizzato da classe che
	 * specializza questa.
	 */
	protected ArrayList lReati = new ArrayList();

	public String processRequest() throws Exception {

		if (!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		FascicoloSiepModel lFascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Funzione di lettura dei dati di input
		letturaDati(lFascicolo.getIdFascicoloSiep());

		if (lReati.size() == 0)
			throw new F3BException(F3BException.USER_MESSAGE, "Specificare almeno un reato!");

		IReato lCtrl = SIEPLookupRemote.getReatoRemote();
		ReatoModel lReatoPrincipale = lCtrl.ExInserisciReati(lReati);

		// setta la risposta nella request
		setRequestAttribute("ComingFromInsert", "YES");

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.reato.action.ActRicercaReato&" + CAMPO_PROGR_REATO + "="
				+ lReatoPrincipale.getProgrReato();

		return lPage;
	}

	/**
	 * Lettura dei dati dalla form di input e valorizzazione dell'elenco lReati.
	 * 
	 * @param aIdFascicoloSiep
	 * @throws Exception
	 */
	protected void letturaDati(BigDecimal aIdFascicoloSiep) throws Exception {

		String fonte = new String("");
		String articolo = new String("");
		String comma = new String("");
		String numero = new String("");

		int spacePos1;
		int spacePos2;
		ReatoModel lReaMod = new ReatoModel();

		String[] CodFonti = getRequestStringParameters(CAMPO_COD_FONTE);
		String[] AnnoFonti = getRequestStringParameters(CAMPO_ANNO_FONTE);
		String[] NumeroFonti = getRequestStringParameters(CAMPO_NUMERO_FONTE);
		String[] SottoNum = getRequestStringParameters(CAMPO_COD_SOTTONUMERAZIONE);
		String[] Commi = getRequestStringParameters(CAMPO_COMMA);
		// ***********************************************************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		String[] CommaQualif = getRequestStringParameters(CAMPO_COMMA_QUALIFICANTE);
		// ***********************************************************************************
		String[] Lettere = getRequestStringParameters(CAMPO_LETTERA);
		String[] Numeri = getRequestStringParameters(CAMPO_NUMERO);
		String[] Articoli = getRequestStringParameters(CAMPO_ARTICOLO);
		String[] Cablati = { "" };
		String[] Cablati2 = { "" };
		if (getRequestStringParameters("cablati") != null)
			Cablati = getRequestStringParameters("cablati");
		if (getRequestStringParameters("cablati2") != null)
			Cablati2 = getRequestStringParameters("cablati2");

		if (getRequestStringParameters("cablati2") != null) {
			ReatoModel lReaMod2;
			for (int i = 0; i < Cablati2.length - 1; i++) // -1 perchè c'è sempre almeno un cablati in hidden
			{
				lReaMod2 = new ReatoModel();
				lReaMod2.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lReaMod2.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lReaMod2.setDataInserimento(DateUtils.getSysDate());

				comma = "";
				spacePos1 = Cablati2[i].indexOf(" ");
				articolo = Cablati2[i].substring(0, spacePos1).trim();

				spacePos2 = Cablati2[i].lastIndexOf(" ");
				if (spacePos2 == spacePos1) {
					fonte = Cablati2[i].substring(spacePos1).trim();
					comma = "";
				} else {
					fonte = Cablati2[i].substring(spacePos1, spacePos2).trim();
					char c = 'C';
					char nullChar = ' ';
					comma = Cablati2[i].substring(spacePos2).replace(c, nullChar).trim();
				}

				lReaMod2.setCodFonte(fonte);
				lReaMod2.setComma(comma);
				lReaMod2.setArticolo(articolo);
				lReaMod2.setCodSottonumerazione("-");
				// *******************************************
				// Federica - a9-rr-078
				// aggiunto campo Comma-Qualificante
				lReaMod2.setCommaQualificante("-");
				// *******************************************

				// * Parte comune *
				lReaMod2.setProgrNumeroManuale(getRequestStringParameter(CAMPO_PROGR_NUMERO_MANUALE));

				lReaMod2.setCodTipoReato(getRequestStringParameter(CAMPO_COD_TIPO_REATO));

				// ?-- lReaMod2.setDataReato( getRequestDateParameter(
				// CAMPO_ANNO_DATA_REATO,CAMPO_MESE_DATA_REATO,CAMPO_GIORNO_DATA_REATO) );

				lReaMod2.setCodPeriodoConsumazione(getRequestStringParameter(CAMPO_COD_PERIODO_CONSUMAZIONE));
				lReaMod2.setDescLuogo(getRequestStringParameter(CAMPO_DESC_LUOGO).toUpperCase());

				// <Data1>

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_INIZIO))
					lReaMod2.setAnnoInizio(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_INIZIO));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_MESE_DATA_INIZIO))
					lReaMod2.setMeseInizio(getRequestBigDecimalParameter(CAMPO_MESE_DATA_INIZIO));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_GIORNO_DATA_INIZIO))
					lReaMod2.setGiornoInizio(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_INIZIO));

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_INIZIO)) {
					lReaMod2.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO,
							CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO));
				}

				// <Data2>

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_FINE))
					lReaMod2.setAnnoFine(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_FINE));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_MESE_DATA_FINE))
					lReaMod2.setMeseFine(getRequestBigDecimalParameter(CAMPO_MESE_DATA_FINE));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_GIORNO_DATA_FINE))
					lReaMod2.setGiornoFine(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_FINE));

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_FINE)) {
					lReaMod2.setDataFine(getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE,
							CAMPO_GIORNO_DATA_FINE));
				}

				//

				lReaMod2.setNote(getRequestStringParameter(CAMPO_NOTE));

				lReaMod2.setCodTipoPenaDetentiva("-"); // Per le join
				lReaMod2.setCodTipoSanzione("-"); // Per le join

				lReaMod2.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lReaMod2.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lReaMod2.setDataInserimento(DateUtils.getSysDate());

				lReaMod2.setFasSieIdFascicoloSiep(aIdFascicoloSiep);

				lReati.add(lReaMod2);
			}
		}

		if (CodFonti.length > 0) {
			for (int i = 0; i < 5; i++) {
				if (CodFonti[i] != null && !CodFonti[i].equals("-")) {
					lReaMod = new ReatoModel();
					lReaMod.setCodFonte(CodFonti[i]);
					if (AnnoFonti[i] != null && !AnnoFonti[i].equals("")) {
						lReaMod.setAnnoFonte(new BigDecimal(AnnoFonti[i]));
					}
					lReaMod.setNumeroFonte(NumeroFonti[i]);
					lReaMod.setCodSottonumerazione(SottoNum[i]);
					lReaMod.setComma(Commi[i]);
					// *******************************************
					// Federica - a9-rr-078
					// aggiunto campo Comma-Qualificante
					lReaMod.setCommaQualificante(CommaQualif[i]);
					// *******************************************
					lReaMod.setLettera(Lettere[i]);
					lReaMod.setNumero(Numeri[i]);
					lReaMod.setArticolo(Articoli[i]);

					// * Parte comune *
					lReaMod.setProgrNumeroManuale(getRequestStringParameter(CAMPO_PROGR_NUMERO_MANUALE));

					lReaMod.setCodTipoReato(getRequestStringParameter(CAMPO_COD_TIPO_REATO));

					// ?-- lReaMod.setDataReato( getRequestDateParameter(
					// CAMPO_ANNO_DATA_REATO,CAMPO_MESE_DATA_REATO,CAMPO_GIORNO_DATA_REATO) );

					lReaMod.setCodPeriodoConsumazione(getRequestStringParameter(CAMPO_COD_PERIODO_CONSUMAZIONE));
					lReaMod.setDescLuogo(getRequestStringParameter(CAMPO_DESC_LUOGO).toUpperCase());

					// <Data1>

					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_INIZIO))
						lReaMod.setAnnoInizio(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_INIZIO));
					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_MESE_DATA_INIZIO))
						lReaMod.setMeseInizio(getRequestBigDecimalParameter(CAMPO_MESE_DATA_INIZIO));
					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_GIORNO_DATA_INIZIO))
						lReaMod.setGiornoInizio(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_INIZIO));

					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_INIZIO)) {
						lReaMod.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO,
								CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO));
					}

					// <Data2>

					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_FINE))
						lReaMod.setAnnoFine(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_FINE));
					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_MESE_DATA_FINE))
						lReaMod.setMeseFine(getRequestBigDecimalParameter(CAMPO_MESE_DATA_FINE));
					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_GIORNO_DATA_FINE))
						lReaMod.setGiornoFine(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_FINE));

					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_FINE)) {
						lReaMod.setDataFine(getRequestDateParameter(CAMPO_ANNO_DATA_FINE,
								CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE));
					}

					// inizio calcolo data remota commissione reato
					// 7)Il calcolo della <Data remota commissione reato> deve avvenire:
					// f) tenendo conto anche delle date inserite in modo parziale
					// g) selezionando tra le <Data1> e <Data2> prima quella con l'anno più vecchio,
					// poi (se possibile) quella con il mese + vecchio
					// ed infine quella (se possibile) con il giorno più vecchio.
					// h) nelle date parziali in presenza del solo anno il mese è = a 1 ed il giorno a 1
					// i) nelle date parziali in presenza del solo anno e del mese il giorno a 1

					// [EC] 20171016: GESTITA LA PROBLEMATICA DELLA NON PRESENZA IN SESSIONE DEL FASCICOLO
					// SIEP, QUANDO STO ARRIVANDO IN QUESTO METODO
					// DA ISCRIZIONE REATI SIGE
					FascicoloSiepModel lFascicolo = null;
					try {
						lFascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");
					} catch (f3b.util.F3BException e) {
						siesLogger.debug(" FascicoloSiepModel Non presente in Sessione!");
					}
					if (lFascicolo == null || lFascicolo.getIdFascicoloSiep() == null) {
						// Se il fascicoloSIEP è null, molto probabilmente sto inserendo un reato per un
						// fascicolo SIGE
						// pertanto provo a recupeare il fascicolo sige in sessione
						FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
						if (lFasSigeEsteso != null) {
							// se fascicolo sige in sessione è NON NULL, recupero il fascicolo SIEP associato
							lFascicolo = (FascicoloSiepModel) lFasSigeEsteso.getFascicoloSiep();
							if (lFascicolo == null)
								throw new F3BException(F3BException.USER_MESSAGE,
										" Fascicolo SIEP non presente in Sessione!");
						} else {
							throw new F3BException(F3BException.USER_MESSAGE,
									" Fascicolo SIGE non presente in Sessione!");
						}
					}
					// fine [EC] 20171016
					SoggettoModel soggettoModel = lFascicolo.getSoggetto();
					BigDecimal etaPresuntaAnni = null;
					BigDecimal etaPresuntaMesi = null;
					etaPresuntaAnni = soggettoModel.getEtaPresuntaAnni();
					etaPresuntaMesi = soggettoModel.getEtaPresuntaMesi();
					if (etaPresuntaAnni != null || etaPresuntaMesi != null) {
						BigDecimal annoPiuVecchio = null;
						BigDecimal mesePiuVecchio = null;
						BigDecimal giornoPiuVecchio = null;
						if (lReaMod.getAnnoInizio() != null && lReaMod.getAnnoFine() != null) {
							if (lReaMod.getAnnoInizio().compareTo(lReaMod.getAnnoFine()) < 0) {
								annoPiuVecchio = lReaMod.getAnnoInizio();
								if (lReaMod.getMeseInizio() != null) {
									mesePiuVecchio = lReaMod.getMeseInizio();
								} else {
									mesePiuVecchio = new BigDecimal(1);
								}
								if (lReaMod.getGiornoInizio() != null) {
									giornoPiuVecchio = lReaMod.getGiornoInizio();
								} else {
									giornoPiuVecchio = new BigDecimal(1);
								}
							} else if (lReaMod.getAnnoInizio().compareTo(lReaMod.getAnnoFine()) > 0) {
								annoPiuVecchio = lReaMod.getAnnoFine();
								if (lReaMod.getMeseFine() != null) {
									mesePiuVecchio = lReaMod.getMeseFine();
								} else {
									mesePiuVecchio = new BigDecimal(1);
								}
								if (lReaMod.getGiornoFine() != null) {
									giornoPiuVecchio = lReaMod.getGiornoFine();
								} else {
									giornoPiuVecchio = new BigDecimal(1);
								}
							} else if (lReaMod.getAnnoInizio().compareTo(lReaMod.getAnnoFine()) == 0) {
								annoPiuVecchio = lReaMod.getAnnoInizio();
								if (lReaMod.getMeseInizio() != null && lReaMod.getMeseFine() != null) {
									if (lReaMod.getMeseInizio().compareTo(lReaMod.getMeseFine()) < 0) {
										mesePiuVecchio = lReaMod.getMeseInizio();
										if (lReaMod.getGiornoInizio() != null) {
											giornoPiuVecchio = lReaMod.getGiornoInizio();
										} else {
											giornoPiuVecchio = new BigDecimal(1);
										}
									} else if (lReaMod.getMeseInizio().compareTo(lReaMod.getMeseFine()) > 0) {
										mesePiuVecchio = lReaMod.getMeseFine();
										if (lReaMod.getGiornoFine() != null) {
											giornoPiuVecchio = lReaMod.getGiornoFine();
										} else {
											giornoPiuVecchio = new BigDecimal(1);
										}
									} else if (lReaMod.getMeseInizio().compareTo(lReaMod.getMeseFine()) == 0) {
										mesePiuVecchio = lReaMod.getMeseInizio();
										if (lReaMod.getGiornoInizio() != null
												&& lReaMod.getGiornoFine() != null) {
											if (lReaMod.getGiornoInizio().compareTo(lReaMod.getGiornoFine()) < 0) {
												giornoPiuVecchio = lReaMod.getGiornoInizio();
											} else if (lReaMod.getGiornoInizio().compareTo(
													lReaMod.getGiornoFine()) >= 0) {
												giornoPiuVecchio = lReaMod.getGiornoFine();
											}
										} else {
											if (lReaMod.getGiornoInizio() != null) {
												giornoPiuVecchio = lReaMod.getGiornoInizio();
											} else {
												giornoPiuVecchio = new BigDecimal(1);
											}
											if (lReaMod.getGiornoFine() != null) {
												giornoPiuVecchio = lReaMod.getGiornoFine();
											} else {
												giornoPiuVecchio = new BigDecimal(1);
											}
										}
									}
								}
							}
						} else if (lReaMod.getAnnoInizio() != null) {
							annoPiuVecchio = lReaMod.getAnnoInizio();
							if (lReaMod.getMeseInizio() != null) {
								mesePiuVecchio = lReaMod.getMeseInizio();
							} else {
								mesePiuVecchio = new BigDecimal(1);
							}
							if (lReaMod.getGiornoInizio() != null) {
								giornoPiuVecchio = lReaMod.getGiornoInizio();
							} else {
								giornoPiuVecchio = new BigDecimal(1);
							}
						} else if (lReaMod.getAnnoFine() != null) {
							annoPiuVecchio = lReaMod.getAnnoFine();
							if (lReaMod.getMeseFine() != null) {
								mesePiuVecchio = lReaMod.getMeseFine();
							} else {
								mesePiuVecchio = new BigDecimal(1);
							}
							if (lReaMod.getGiornoFine() != null) {
								giornoPiuVecchio = lReaMod.getGiornoFine();
							} else {
								giornoPiuVecchio = new BigDecimal(1);
							}
						}

						// ricerca della data Reato più vecchia nella tabella REATO
						// BigDecimal aId = null;
						// aId = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
						IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
						DettaglioFascicoloModel lDettaglio = lCtrl.ExDettaglioFascicoloSiep(lFascicolo
								.getIdFascicoloSiep());
						Collection lReatiColl = (Collection) lDettaglio.getReatiCircostanze();
						Vector lReatiVect = new Vector(lReatiColl);
						Date dataPrimoReato = elaboraDataPrimoReato(lReatiVect);
						Date dataUnoDataDue = null;
						if (annoPiuVecchio != null && mesePiuVecchio != null && giornoPiuVecchio != null)
							dataUnoDataDue = DateUtils.getDate(annoPiuVecchio.toString(),
									mesePiuVecchio.toString(), giornoPiuVecchio.toString());

						Date dataRemotaCommissioneReato = null;
						if (dataPrimoReato != null && dataUnoDataDue != null
								&& dataPrimoReato.compareTo(dataUnoDataDue) < 0) {
							dataRemotaCommissioneReato = dataPrimoReato;
						} else if (dataPrimoReato != null && dataUnoDataDue != null) {
							dataRemotaCommissioneReato = dataUnoDataDue;
						} else if (dataPrimoReato != null) {
							dataRemotaCommissioneReato = dataPrimoReato;
						} else if (dataUnoDataDue != null) {
							dataRemotaCommissioneReato = dataUnoDataDue;
						}

						ISoggetto lCtrlSoggetto = SICOLookupRemote.getSoggettoRemote();

						// 1. il sistema se il soggetto ha il campo della <Età Presunta> valorizzata determina
						// la data di commesso reato meno recente (più vecchia)
						// 2. il sistema dalla data di commesso reato più vecchia sottrae prima gli anni e poi
						// i mesi della <Età Presunta> determinando il valore della <DATA_NASCITA_PRESUNTA>
						// 3. il sistema memorizza la <DATA_NASCITA_PRESUNTA>.
						// 4. se la data di commesso reato più vecchia è vuota allora sarà vuota la data del
						// campo <DATA_NASCITA_PRESUNTA >.
						Calendar dataMenoEtaPresunta = Calendar.getInstance();
						if (dataRemotaCommissioneReato != null) {
							dataMenoEtaPresunta.setTime(dataRemotaCommissioneReato);
							if (etaPresuntaAnni != null) {
								dataMenoEtaPresunta.add(GregorianCalendar.YEAR, -etaPresuntaAnni.intValue());
							}
							if (etaPresuntaMesi != null) {
								dataMenoEtaPresunta.add(GregorianCalendar.MONTH, -etaPresuntaMesi.intValue());
							}
							int anno = dataMenoEtaPresunta.get(GregorianCalendar.YEAR);
							int mese = dataMenoEtaPresunta.get(GregorianCalendar.MONTH); // i mesi partono da
																							// 0
							int giorno = dataMenoEtaPresunta.get(GregorianCalendar.DATE);

							GregorianCalendar gc = new GregorianCalendar();
							gc.set(Calendar.YEAR, anno);
							gc.set(Calendar.MONTH, mese);
							gc.set(Calendar.DATE, giorno);
							gc.set(Calendar.HOUR_OF_DAY, 0);
							gc.set(Calendar.MINUTE, 0);
							gc.set(Calendar.SECOND, 0);

							dataRemotaCommissioneReato = gc.getTime();
							soggettoModel.setDataNascitaPresuntaCalc(dataRemotaCommissioneReato);
							lCtrlSoggetto.ExModificaSoggetto(soggettoModel);
						}

					}
					// fine calcolo data remota commissione reato

					lReaMod.setNote(getRequestStringParameter(CAMPO_NOTE));

					lReaMod.setCodTipoPenaDetentiva("-"); // Per le join
					lReaMod.setCodTipoSanzione("-"); // Per le join

					lReaMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lReaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lReaMod.setDataInserimento(DateUtils.getSysDate());

					lReaMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);

					lReati.add(lReaMod);
				}
			}
		}

		fonte = new String("");
		articolo = new String("");
		comma = new String("");
		numero = new String("");

		if (getRequestStringParameters("cablati") != null) {
			ReatoModel lReaMod2;
			for (int i = 0; i < Cablati.length - 1; i++) // -1 perchè c'è sempre almeno un cablati in hidden
			{
				lReaMod2 = new ReatoModel();
				lReaMod2.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lReaMod2.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lReaMod2.setDataInserimento(DateUtils.getSysDate());

				comma = "";
				spacePos1 = Cablati[i].indexOf(" ");
				articolo = Cablati[i].substring(0, spacePos1).trim();

				spacePos2 = Cablati[i].lastIndexOf(" ");
				if (spacePos2 == spacePos1) {
					fonte = Cablati[i].substring(spacePos1).trim();
					comma = "";
					numero = ""; // AMBROSINO 04/2013 mac
				} else {
					fonte = Cablati[i].substring(spacePos1, spacePos2).trim();

					if (Cablati[i].substring(spacePos2, spacePos2 + 2).trim().equals("N")) {
						char n = 'N';
						char nullChar = ' ';
						numero = Cablati[i].substring(spacePos2).replace(n, nullChar).trim();
						comma = ""; // AMBROSINO 04/2013 mac

					} else if (Cablati[i].substring(spacePos2, spacePos2 + 2).trim().equals("C")) {
						char c = 'C';
						char nullChar = ' ';
						comma = Cablati[i].substring(spacePos2).replace(c, nullChar).trim();
						numero = ""; // AMBROSINO 04/2013 mac
					}
				}

				lReaMod2.setCodFonte(fonte);
				lReaMod2.setComma(comma);
				lReaMod2.setNumero(numero);

				lReaMod2.setArticolo(articolo);
				lReaMod2.setCodSottonumerazione("-");
				// *******************************************
				// Federica - a9-rr-078
				// aggiunto campo Comma-Qualificante
				lReaMod2.setCommaQualificante("-");
				// *******************************************

				// * Parte comune *
				lReaMod2.setProgrNumeroManuale(getRequestStringParameter(CAMPO_PROGR_NUMERO_MANUALE));

				lReaMod2.setCodTipoReato(getRequestStringParameter(CAMPO_COD_TIPO_REATO));

				// ?-- lReaMod2.setDataReato( getRequestDateParameter(
				// CAMPO_ANNO_DATA_REATO,CAMPO_MESE_DATA_REATO,CAMPO_GIORNO_DATA_REATO) );

				lReaMod2.setCodPeriodoConsumazione(getRequestStringParameter(CAMPO_COD_PERIODO_CONSUMAZIONE));
				lReaMod2.setDescLuogo(getRequestStringParameter(CAMPO_DESC_LUOGO).toUpperCase());

				// <Data1>

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_INIZIO))
					lReaMod2.setAnnoInizio(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_INIZIO));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_MESE_DATA_INIZIO))
					lReaMod2.setMeseInizio(getRequestBigDecimalParameter(CAMPO_MESE_DATA_INIZIO));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_GIORNO_DATA_INIZIO))
					lReaMod2.setGiornoInizio(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_INIZIO));

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_INIZIO)) {
					lReaMod2.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO,
							CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO));
				}

				// <Data2>

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_FINE))
					lReaMod2.setAnnoFine(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_FINE));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_MESE_DATA_FINE))
					lReaMod2.setMeseFine(getRequestBigDecimalParameter(CAMPO_MESE_DATA_FINE));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_GIORNO_DATA_FINE))
					lReaMod2.setGiornoFine(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_FINE));

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_FINE)) {
					lReaMod2.setDataFine(getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE,
							CAMPO_GIORNO_DATA_FINE));
				}

				//
				lReaMod2.setNote(getRequestStringParameter(CAMPO_NOTE));

				lReaMod2.setCodTipoPenaDetentiva("-"); // Per le join
				lReaMod2.setCodTipoSanzione("-"); // Per le join

				lReaMod2.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lReaMod2.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lReaMod2.setDataInserimento(DateUtils.getSysDate());

				lReaMod2.setFasSieIdFascicoloSiep(aIdFascicoloSiep);

				lReati.add(lReaMod2);
			}
		}
	}

	Date elaboraDataPrimoReato(Vector reatiVect) {
		// data del primo reato
		Date dataPrimoReato = null;

		Iterator itx = reatiVect.iterator();
		while (itx.hasNext()) {

			ReatoModel lReato = null;
			Object lObj = itx.next();
			if (lObj instanceof ReatoModel) {
				lReato = (ReatoModel) lObj;
			} else if (lObj instanceof ReatoCircostanzaModel) {
				ReatoCircostanzaModel lReatoCirc = (ReatoCircostanzaModel) lObj;
				lReato = lReatoCirc.getReato();
			}

			Date dataReato = elaboraDataReato(lReato);
			if (dataReato != null) {
				if (dataPrimoReato == null) {
					dataPrimoReato = dataReato;
				} else if (dataPrimoReato.compareTo(dataReato) > 0) {
					dataPrimoReato = dataReato;
				}
			}
		}

		return dataPrimoReato;
	}

	Date elaboraDataReato(ReatoModel lReato) {
		Date ret = null;
		if (lReato.getDataInizio() != null) {
			ret = lReato.getDataInizio();
		} else if (lReato.getMeseInizio() != null && lReato.getAnnoInizio() != null) {
			ret = DateUtils.getDate(lReato.getAnnoInizio().intValue(), lReato.getMeseInizio().intValue(), 1);
		} else if (lReato.getAnnoInizio() != null) {
			ret = DateUtils.getDate(lReato.getAnnoInizio().intValue(), 1, 1);
		}
		return ret;
	}

}