package siap.siep.reato.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

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
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActModificaReato
 * </p>
 * <p>
 * Description: Classe Action per la modifica di Reato
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
public class ActModificaReato extends ActionSiap implements ICostantiReato {

	protected ReatoModel mReaModRet;

	/**
	 * Azione di Modifica del Reato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// riempie il model
		ReatoModel lReaMod = new ReatoModel();

		lReaMod.setIdReato(getRequestBigDecimalParameter(CAMPO_ID_REATO));

		if (!isRequestParameterNullObj(CAMPO_PROGR_NUMERO_MANUALE))
			lReaMod.setProgrNumeroManuale(getRequestStringParameter(CAMPO_PROGR_NUMERO_MANUALE));

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_REATO))
			lReaMod.setCodTipoReato(getRequestStringParameter(CAMPO_COD_TIPO_REATO));
		else
			lReaMod.setCodTipoReato("-");

		// lReaMod.setDataReato( getRequestDateParameter(
		// CAMPO_ANNO_DATA_REATO,CAMPO_MESE_DATA_REATO,CAMPO_GIORNO_DATA_REATO) );

		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_INIZIO)
				&& !isRequestParameterNullObj(CAMPO_MESE_DATA_INIZIO)
				&& !isRequestParameterNullObj(CAMPO_GIORNO_DATA_INIZIO)) {
			lReaMod.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO,
					CAMPO_GIORNO_DATA_INIZIO));
		}

		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_INIZIO))
			lReaMod.setAnnoInizio(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_INIZIO));
		if (!isRequestParameterNullObj(CAMPO_MESE_DATA_INIZIO))
			lReaMod.setMeseInizio(getRequestBigDecimalParameter(CAMPO_MESE_DATA_INIZIO));
		if (!isRequestParameterNullObj(CAMPO_GIORNO_DATA_INIZIO))
			lReaMod.setGiornoInizio(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_INIZIO));

		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_FINE)
				&& !isRequestParameterNullObj(CAMPO_MESE_DATA_FINE)
				&& !isRequestParameterNullObj(CAMPO_GIORNO_DATA_FINE)) {
			lReaMod.setDataFine(getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE,
					CAMPO_GIORNO_DATA_FINE));
		}

		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_FINE))
			lReaMod.setAnnoFine(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_FINE));
		if (!isRequestParameterNullObj(CAMPO_MESE_DATA_FINE))
			lReaMod.setMeseFine(getRequestBigDecimalParameter(CAMPO_MESE_DATA_FINE));
		if (!isRequestParameterNullObj(CAMPO_GIORNO_DATA_FINE))
			lReaMod.setGiornoFine(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_FINE));

		if (!isRequestParameterNullObj(CAMPO_COD_PERIODO_CONSUMAZIONE))
			lReaMod.setCodPeriodoConsumazione(getRequestStringParameter(CAMPO_COD_PERIODO_CONSUMAZIONE));
		else
			lReaMod.setCodPeriodoConsumazione("-");

		if (!isRequestParameterNullObj(CAMPO_DESC_LUOGO))
			lReaMod.setDescLuogo(getRequestStringParameter(CAMPO_DESC_LUOGO));

		lReaMod.setCodFonte(getRequestStringParameter(CAMPO_COD_FONTE));
		lReaMod.setAnnoFonte(getRequestBigDecimalParameter(CAMPO_ANNO_FONTE));
		lReaMod.setNumeroFonte(getRequestStringParameter(CAMPO_NUMERO_FONTE));
		lReaMod.setCodSottonumerazione(getRequestStringParameter(CAMPO_COD_SOTTONUMERAZIONE));
		lReaMod.setComma(getRequestStringParameter(CAMPO_COMMA));
		lReaMod.setLettera(getRequestStringParameter(CAMPO_LETTERA));
		lReaMod.setNumero(getRequestStringParameter(CAMPO_NUMERO));
		lReaMod.setArticolo(getRequestStringParameter(CAMPO_ARTICOLO));

		if (!isRequestParameterNullObj(CAMPO_DESC_LUOGO))
			lReaMod.setNote(getRequestStringParameter(CAMPO_NOTE));

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PENA_DETENTIVA))
			lReaMod.setCodTipoPenaDetentiva(getRequestStringParameter(CAMPO_COD_TIPO_PENA_DETENTIVA));
		else
			lReaMod.setCodTipoPenaDetentiva("-");

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_SANZIONE))
			lReaMod.setCodTipoSanzione(getRequestStringParameter(CAMPO_COD_TIPO_SANZIONE));
		else
			lReaMod.setCodTipoSanzione("-");

		lReaMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lReaMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lReaMod.setDataAggiornamento(DateUtils.getSysDate());

		// lReaMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP)
		// );

		lReaMod.setProgrCircostanza(getRequestBigDecimalParameter(CAMPO_PROGR_CIRCOSTANZA));

		// **************************************************************************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		lReaMod.setCommaQualificante(getRequestStringParameter(CAMPO_COMMA_QUALIFICANTE));
		// **************************************************************************************************

		// inizio calcolo data remota commissione reato
		// 7)Il calcolo della <Data remota commissione reato> deve avvenire:
		// f) tenendo conto anche delle date inserite in modo parziale
		// g) selezionando tra le <Data1> e <Data2> prima quella con l'anno più vecchio,
		// poi (se possibile) quella con il mese + vecchio
		// ed infine quella (se possibile) con il giorno più vecchio.
		// h) nelle date parziali in presenza del solo anno il mese è = a 1 ed il giorno a 1
		// i) nelle date parziali in presenza del solo anno e del mese il giorno a 1
		FascicoloSiepModel lFascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");
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
							if (lReaMod.getGiornoInizio() != null && lReaMod.getGiornoFine() != null) {
								if (lReaMod.getGiornoInizio().compareTo(lReaMod.getGiornoFine()) < 0) {
									giornoPiuVecchio = lReaMod.getGiornoInizio();
								} else if (lReaMod.getGiornoInizio().compareTo(lReaMod.getGiornoFine()) >= 0) {
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
//			ReatoContinuazioneController lRCtrl = new ReatoContinuazioneController();
			Collection lReatiColl = (Collection) lDettaglio.getReatiCircostanze();
			Vector lReatiVect = new Vector(lReatiColl);
			Date dataPrimoReato = elaboraDataPrimoReato(lReatiVect);

			Date dataUnoDataDue = null;
			if (annoPiuVecchio != null && mesePiuVecchio != null && giornoPiuVecchio != null)
				dataUnoDataDue = DateUtils.getDate(annoPiuVecchio.toString(), mesePiuVecchio.toString(),
						giornoPiuVecchio.toString());

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

			// 1. il sistema se il soggetto ha il campo della <Età Presunta> valorizzata determina la data di
			// commesso reato meno recente (più vecchia)
			// 2. il sistema dalla data di commesso reato più vecchia sottrae prima gli anni e poi i mesi
			// della <Età Presunta> determinando il valore della <DATA_NASCITA_PRESUNTA>
			// 3. il sistema memorizza la <DATA_NASCITA_PRESUNTA>.
			// 4. se la data di commesso reato più vecchia è vuota allora sarà vuota la data del campo
			// <DATA_NASCITA_PRESUNTA >.
			Calendar dataMenoEtaPresunta = Calendar.getInstance();
			if (dataRemotaCommissioneReato != null && dataUnoDataDue != null) {
				dataMenoEtaPresunta.setTime(dataRemotaCommissioneReato);
				if (etaPresuntaAnni != null) {
					dataMenoEtaPresunta.add(GregorianCalendar.YEAR, -etaPresuntaAnni.intValue());
				}
				if (etaPresuntaMesi != null) {
					dataMenoEtaPresunta.add(GregorianCalendar.MONTH, -etaPresuntaMesi.intValue());
				}
				int anno = dataMenoEtaPresunta.get(GregorianCalendar.YEAR);
				int mese = dataMenoEtaPresunta.get(GregorianCalendar.MONTH); // i mesi partono da 0
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
			} else if (dataUnoDataDue == null) {
				soggettoModel.setDataNascitaPresuntaCalc(null);
				lCtrlSoggetto.ExModificaSoggetto(soggettoModel);
			}

		}
		// fine calcolo data remota commissione reato

		// chiama il controller
		IReato lCtrl = SIEPLookupRemote.getReatoRemote();
		mReaModRet = lCtrl.ExModificaReato(lReaMod);

		// INIZIO ////////////////////////////////////////////////////////////////////
		// MODIFICA PER FARE L'UPDATE DELLE ULTERIORI NORME OLTRE LA PRIMA
		// PER NON PERDERE L'ALLINEAMENTO CON LA NORMA PRINCIPALE

		lReaMod.setProgrReato(getRequestBigDecimalParameter(CAMPO_PROGR_REATO));
		lReaMod.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP));
		// CONTROLLO SE LA NORMA MODIFICATA HA IL PROGRESSIVO UNO
		if (lReaMod.getProgrCircostanza().intValue() == 1 && lReaMod.getFasSieIdFascicoloSiep() != null) {

			// Cerco altre eventuali norme
			Vector lNorme = lCtrl.ExRicercaReato(lReaMod);

			// Se ci sono ulteriori norme richiamo il metodo per modificare
			// i campi in comune con la prima norma
			if (lNorme.size() > 1) {

				lCtrl.ExModificaUlterioriNorme(lReaMod, lNorme);
			}
		}
		// FINE
		// ///////////////////////////////////////////////////////////////////////

		setRequestAttribute("reato", mReaModRet);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.reato.action.ActLoadDettaglioReato&" + CAMPO_ID_REATO + "="
				+ mReaModRet.getIdReato().toString();

		return lPage;
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