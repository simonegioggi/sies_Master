package siap.sige.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;

/**
 * MEV_57: aggiunta classe comprendente metodi di controllo età soggetto
 *
 * @author Gioggi
 */
@SuppressWarnings("rawtypes")
public class SigeMaggiorenniUtil {

	public static boolean checkMinorenne(FascicoloSigeEstesoModel lFasEst) throws F3BException {

		// data di sistema
		Date dataSistema = DateUtils.getSysDate();
		// data di nascita
		Date dataNascitaSoggetto = elaboraDataNascitaSoggetto(lFasEst.getSoggetto());
		// data del reato
		Date dataReato = lFasEst.getSoggetto().getDataReatoSius();

		boolean anni_18_Maggiorenne = false;
		if (dataNascitaSoggetto != null) {
			// calcolo gli anni del soggetto
			int anniSoggetto = deltaAnni(dataNascitaSoggetto, dataSistema);
			if (anniSoggetto >= 18)
				anni_18_Maggiorenne = true;
			else
				anni_18_Maggiorenne = false;
		} else if (lFasEst.getSoggetto().getEtaPresuntaAnni() != null && dataReato != null) {
			// calcolo gli anni presunti del soggetto
			Date dataNascitaPresunta = DateUtils.moveDateTo(dataReato, Calendar.YEAR,
					-lFasEst.getSoggetto().getEtaPresuntaAnni().intValue());
			if (lFasEst.getSoggetto().getEtaPresuntaMesi() != null) {
				dataNascitaPresunta = DateUtils.moveDateTo(dataNascitaPresunta, Calendar.MONTH,
						-lFasEst.getSoggetto().getEtaPresuntaMesi().intValue());
			}
			int anniPresunti = deltaAnni(dataNascitaPresunta, dataSistema);

			// quando il calcolo degli anni presunti restituisce 18, bisogna
			// verificare se il giorno della data di sistema è maggiore
			// della data ultimo reato, in questo caso il soggetto è maggiorenne
			if (anniPresunti == 18) {
				int lGiornoDataSistema = Integer.parseInt(DateUtils.getDateToString(dataSistema, "dd"));
				int lGiornoDataReato = Integer.parseInt(DateUtils.getDateToString(dataReato, "dd"));
				if (lGiornoDataSistema > lGiornoDataReato)
					anni_18_Maggiorenne = true;
				else
					anni_18_Maggiorenne = false;
			}

			// mev_57 MODIFICA DEL 31/10/2018
			if (anniPresunti > 18) {
				anni_18_Maggiorenne = true;
			}
		} else if (lFasEst.getSoggetto().getEtaPresuntaAnni() != null && dataReato == null) {
			// caso in cui il soggetto ha età presunta ma non c'è data commesso reato,
			// in questo caso bisogna recuperare le informazioni del Fasicolo Siep associato,
			// per impostare l'etichetta Maggiorenne/Minorenne
			if (lFasEst != null && lFasEst.getFascicoloSiep() != null) {
				BigDecimal idFascicoloSiep = lFasEst.getFascicoloSiep().getFasSieIdFascicoloSiep();
				if (Utils.isPresent(idFascicoloSiep)) {
					// Reati (SIEP)
					IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
					Vector lReati = lReaCtrl.ExRicercaReatoCircostanzaByFascicolo(idFascicoloSiep);
					// data del primo reato e ultimo legati al Fascicolo Siep
					Date dataPrimoReato = elaboraDataPrimoReato(lReati);
					Date dataUltimoReato = elaboraDataUltimoReato(lReati);
					if (dataPrimoReato != null) {
						// calcolo gli anni presunti del soggetto
						Date dataNascitaPresunta = DateUtils.moveDateTo(dataPrimoReato, Calendar.YEAR,
								-lFasEst.getSoggetto().getEtaPresuntaAnni().intValue());
						if (lFasEst.getSoggetto().getEtaPresuntaMesi() != null)
							dataNascitaPresunta = DateUtils.moveDateTo(dataNascitaPresunta, Calendar.MONTH,
									-lFasEst.getSoggetto().getEtaPresuntaMesi().intValue());
						int anniPresunti = deltaAnni(dataNascitaPresunta, dataSistema);
						// quando il calcolo degli anni presunti restituisce 18,
						// bisogna verificare se il giorno della data di sistema è
						// maggiore della data ultimo reato, in questo caso il soggetto è maggiorenne
						if (anniPresunti == 18) {
							int lGiornoDataSistema = Integer
									.parseInt(DateUtils.getDateToString(dataSistema, "dd"));
							int lGiornoDataUltimoReato = Integer
									.parseInt(DateUtils.getDateToString(dataUltimoReato, "dd"));
							if (lGiornoDataSistema > lGiornoDataUltimoReato)
								anni_18_Maggiorenne = true;
							else
								anni_18_Maggiorenne = false;
						}
					}
				}
			}
		}

		// valore di ritorno
		return anni_18_Maggiorenne;
	}

	private static int deltaAnni(Date dataStart, Date dataEnd) {

		int anni = 0;
		if (dataStart != null && dataEnd != null) {
			long lStart = dataStart.getTime();
			long lEnd = dataEnd.getTime();
			long delta = lEnd - lStart;
			long days = Math.round((delta / (1000 * 60 * 60 * 24)));
			anni = Math.round(days / 365);
		}

		// valore di ritorno
		return anni;
	}

	private static Date elaboraDataNascitaSoggetto(SoggettoModel soggMod) {

		Date ret = null;
		if (soggMod.getDataNascita() != null)
			ret = soggMod.getDataNascita();
		else if (soggMod.getMeseNascita() != null && soggMod.getAnnoNascita() != null)
			ret = DateUtils.getDate(soggMod.getAnnoNascita().intValue(), soggMod.getMeseNascita().intValue(),
					1);
		else if (soggMod.getAnnoNascita() != null)
			ret = DateUtils.getDate(soggMod.getAnnoNascita().intValue(), 1, 1);

		// valore di ritorno
		return ret;
	}

	private static Date elaboraDataPrimoReato(Vector reatiVect) {

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
				if (dataPrimoReato == null)
					dataPrimoReato = dataReato;
				else if (dataPrimoReato.compareTo(dataReato) > 0)
					dataPrimoReato = dataReato;
			}
		}

		// valore di ritorno
		return dataPrimoReato;
	}

	private static Date elaboraDataReato(ReatoModel lReato) {

		Date ret = null;
		if (lReato.getDataInizio() != null)
			ret = lReato.getDataInizio();
		else if (lReato.getMeseInizio() != null && lReato.getAnnoInizio() != null)
			ret = DateUtils.getDate(lReato.getAnnoInizio().intValue(), lReato.getMeseInizio().intValue(), 1);
		else if (lReato.getAnnoInizio() != null)
			ret = DateUtils.getDate(lReato.getAnnoInizio().intValue(), 1, 1);

		// valore di ritorno
		return ret;
	}

	private static Date elaboraDataUltimoReato(Vector reatiVect) {

		// data ultimo reato
		Date dataUltimoReato = null;

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
				if (dataUltimoReato == null)
					dataUltimoReato = dataReato;
				else if (dataUltimoReato.compareTo(dataReato) < 0)
					dataUltimoReato = dataReato;
			}
		}

		// valore di ritorno
		return dataUltimoReato;
	}

	// // MEV_57: aggiunto metodo
	// public static String checkMinorenne(SoggettoModel soggMod, FascicoloSigeModel fascicoloSigeModel)
	// throws F3BException {
	//
	// // data di sistema
	// Date dataSistema = DateUtils.getSysDate();
	// // data di nascita
	// Date dataNascitaSoggetto = elaboraDataNascitaSoggetto(soggMod);
	// // data del reato
	// Date dataReato = soggMod.getDataReatoSius();
	//
	// String ret = "";
	//
	// // uguale a false se il soggetto ha 18 anni ed è quindi minorenne
	// // uguale a true se il soggetto ha 18 anni ed 1 giorno ed è quindi maggiorenne
	// boolean anni_18_Maggiorenne = false;
	// if (dataNascitaSoggetto != null) {
	// // calcolo gli anni del soggetto
	// int anniSoggetto = deltaAnni(dataNascitaSoggetto, dataSistema);
	// ret = impostaEtichetta(anniSoggetto, fascicoloSigeModel, anni_18_Maggiorenne);
	// } else if (soggMod.getEtaPresuntaAnni() != null && dataReato != null) {
	// // calcolo gli anni presunti del soggetto
	// Date dataNascitaPresunta = DateUtils.moveDateTo(dataSistema, Calendar.YEAR, -soggMod
	// .getEtaPresuntaAnni().intValue());
	// if (soggMod.getEtaPresuntaMesi() != null) {
	// dataNascitaPresunta = DateUtils.moveDateTo(dataNascitaPresunta, Calendar.MONTH, -soggMod
	// .getEtaPresuntaMesi().intValue());
	// }
	// int anniPresunti = deltaAnni(dataNascitaPresunta, dataSistema);
	// int anniPresuntiReato = deltaAnni(dataNascitaPresunta, dataReato);
	//
	// // quando il calcolo degli anni presunti restituisce 18, bisogna
	// // verificare se il giorno della data di sistema è maggiore
	// // della data ultimo reato, in questo saco il soggetto è maggiorenne
	// if (anniPresunti == 18) {
	// int lGiornoDataSistema = Integer.parseInt(DateUtils.getDateToString(dataSistema, "dd"));
	// int lGiornoDataUltimoReato = Integer.parseInt(DateUtils.getDateToString(null, "dd"));
	// if (lGiornoDataSistema > lGiornoDataUltimoReato)
	// anni_18_Maggiorenne = true;
	// else
	// anni_18_Maggiorenne = false;
	// }
	// ret = impostaEtichetta(anniPresunti, fascicoloSigeModel, anni_18_Maggiorenne);
	// }
	//
	// // valore di ritorno
	// return ret;
	// }

	// MEV_57: aggiunto metodo
	public static String checkMinorenneEtichetta(FascicoloSigeEstesoModel lFasEst) throws F3BException {

		// data di sistema
		Date dataSistema = DateUtils.getSysDate();
		// data di nascita
		Date dataNascitaSoggetto = elaboraDataNascitaSoggetto(lFasEst.getSoggetto());
		// data del reato
		Date dataReato = lFasEst.getSoggetto().getDataReatoSius();

		String ret = "";

		// uguale a false se il soggetto ha 18 anni ed è quindi minorenne
		// uguale a true se il soggetto ha 18 anni ed 1 giorno ed è quindi maggiorenne
		boolean anni_18_Maggiorenne = false;
		if (dataNascitaSoggetto != null) {
			// calcolo gli anni del soggetto
			int anniSoggetto = deltaAnni(dataNascitaSoggetto, dataSistema);
			ret = impostaEtichetta(anniSoggetto, lFasEst.getFascicoloSige(), anni_18_Maggiorenne);
		} else if (lFasEst.getSoggetto().getEtaPresuntaAnni() != null && dataReato != null) {
			// calcolo gli anni presunti del soggetto
			Date dataNascitaPresunta = DateUtils.moveDateTo(dataReato, Calendar.YEAR,
					-lFasEst.getSoggetto().getEtaPresuntaAnni().intValue());
			if (lFasEst.getSoggetto().getEtaPresuntaMesi() != null) {
				dataNascitaPresunta = DateUtils.moveDateTo(dataNascitaPresunta, Calendar.MONTH,
						-lFasEst.getSoggetto().getEtaPresuntaMesi().intValue());
			}
			int anniPresunti = deltaAnni(dataNascitaPresunta, dataSistema);

			// quando il calcolo degli anni presunti restituisce 18, bisogna
			// verificare se il giorno della data di sistema è maggiore
			// della data ultimo reato, in questo saco il soggetto è maggiorenne
			// if (anniPresunti == 18) {
			// int lGiornoDataSistema = Integer.parseInt(DateUtils.getDateToString(dataSistema, "dd"));
			// int lGiornoDataUltimoReato = Integer.parseInt(DateUtils.getDateToString(null, "dd"));
			// if (lGiornoDataSistema > lGiornoDataUltimoReato)
			// anni_18_Maggiorenne = true;
			// else
			// anni_18_Maggiorenne = false;
			// }
			// MEV_57 MODIFICA DEL 31/10/2018
			if (anniPresunti == 18) {
				int lGiornoDataSistema = Integer.parseInt(DateUtils.getDateToString(dataSistema, "dd"));
				int lGiornoDataReato = Integer.parseInt(DateUtils.getDateToString(dataReato, "dd"));
				if (lGiornoDataSistema > lGiornoDataReato) {
					anni_18_Maggiorenne = true;
				} else {
					anni_18_Maggiorenne = false;
				}
			}
			ret = impostaEtichetta(anniPresunti, lFasEst.getFascicoloSige(), anni_18_Maggiorenne);
		} else if (lFasEst.getSoggetto().getEtaPresuntaAnni() != null && dataReato == null) {
			// caso in cui il soggetto ha età presunta ma non c'è data commesso reato,
			// in questo caso bisogna recuperare le informazioni del Fasicolo Siep associato,
			// per impostare l'etichetta Maggiorenne/Minorenne
			if (lFasEst != null && lFasEst.getFascicoloSige() != null) {
				if (lFasEst.getFascicoloSiep() != null) {
					BigDecimal idFascicoloSiep = lFasEst.getFascicoloSiep().getIdFascicoloSiep();
					// String codTipoUfficioSiep = "";
					if (Utils.isPresent(idFascicoloSiep)) {
						IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
						/* DettaglioFascicoloModel lDettaglio = */lCtrl
								.ExDettaglioFascicoloSiep(idFascicoloSiep);
						// FascicoloSiepModel lFasMod = lDettaglio.getFascicoloSiep();
						// codTipoUfficioSiep = lFasMod.getCodTipoUfficio();

						// Reati (SIEP)
						IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
						Vector lReati = lReaCtrl.ExRicercaReatoCircostanzaByFascicolo(idFascicoloSiep);

						// data del primo reato e ultimo legati al Fascicolo Siep
						Date dataPrimoReato = elaboraDataPrimoReato(lReati);
						Date dataUltimoReato = elaboraDataUltimoReato(lReati);

						if (dataPrimoReato != null) {
							// calcolo gli anni presunti del soggetto
							Date dataNascitaPresunta = DateUtils.moveDateTo(dataPrimoReato, Calendar.YEAR,
									-lFasEst.getSoggetto().getEtaPresuntaAnni().intValue());
							if (lFasEst.getSoggetto().getEtaPresuntaMesi() != null)
								dataNascitaPresunta = DateUtils.moveDateTo(dataNascitaPresunta,
										Calendar.MONTH,
										-lFasEst.getSoggetto().getEtaPresuntaMesi().intValue());
							int anniPresunti = deltaAnni(dataNascitaPresunta, dataSistema);

							// quando il calcolo degli anni presunti restituisce 18, bisogna
							// verificare se il giorno della data di sistema è maggiore
							// della data ultimo reato, in questo caso il soggetto è maggiorenne
							if (anniPresunti == 18) {
								int lGiornoDataSistema = Integer
										.parseInt(DateUtils.getDateToString(dataSistema, "dd"));
								int lGiornoDataUltimoReato = Integer
										.parseInt(DateUtils.getDateToString(dataUltimoReato, "dd"));
								if (lGiornoDataSistema > lGiornoDataUltimoReato) {
									anni_18_Maggiorenne = true;
								} else {
									anni_18_Maggiorenne = false;
								}
							}

							ret = impostaEtichetta(anniPresunti, lFasEst.getFascicoloSige(),
									anni_18_Maggiorenne);
						}
					}
				}
			}
		}

		// valore di ritorno
		return ret;
	}

	// MEV_57: aggiunto metodo
	private static String impostaEtichetta(int anni, FascicoloSigeModel fascicoloSigeModel,
			boolean anni_18_Maggiorenne) throws F3BException {

		String etichettaEta = "";

		// a) il soggetto iscritto dai seguenti uffici : PM-GIP-DIB-TDS-UDS-CAP
		// è sempre 'MAGGIORENNE';
		if (fascicoloSigeModel.getCodTipoUfficioInserimento() != null
				&& !fascicoloSigeModel.getCodTipoUfficioInserimento().equals("")
				&& (fascicoloSigeModel.getCodTipoUfficioInserimento().equals("PM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("GIP")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("DIB")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("TDS")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("UDS")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("CAP"))) {
			// etichetta non visibile
			etichettaEta = "";
		}
		// b) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// e dagli uffici DIBM-GIPM-CAPSM-UDSM-TDSM
		// è 'MINORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta' alla
		// data di sistema ha meno di 18 anni (per precisione, meno di 18 anni ed un giorno);
		else if (fascicoloSigeModel.getCodTipoUfficioInserimento() != null
				&& !fascicoloSigeModel.getCodTipoUfficioInserimento().equals("")
				&& (fascicoloSigeModel.getCodTipoUfficioInserimento().equals("PMM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("DIBM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("GIPM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("CAPSM")
						// MEV_57 MODIFICA DEL 05/11/2018
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("GUPM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("UDSM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("TDSM"))
				&& anni <= 18 && !anni_18_Maggiorenne) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#FF0040'>&nbsp;Minorenne (Anni "
					+ anni + ")&nbsp;</span>";
		}
		// c) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// e dagli uffici DIBM-GIPM-CAPSM-UDSM-TDSM
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo la
		// 'Età presunta' alla data di sistema ha più di 18 anni ma meno di 25 anni
		// ed avente campo VISIBILITA_EX_MINORENNE = 'N';
		// MERGE v10 COLLAUDO: sostituito 25 con 24
		else if (fascicoloSigeModel.getCodTipoUfficioInserimento() != null
				&& !fascicoloSigeModel.getCodTipoUfficioInserimento().equals("")
				&& (fascicoloSigeModel.getCodTipoUfficioInserimento().equals("PMM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("DIBM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("GIPM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("CAPSM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("GUPM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("UDSM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("TDSM"))
				&& anni <= 24) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
		}
		// d) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// e dagli uffici DIBM-GIPM-CAPSM-UDSM-TDSM
		// è 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta' alla data di
		// sistema ha più di 18 anni ma meno di 26 anni ed avente campo VISIBILITA_EX_MINORENNE = '';
		else if (fascicoloSigeModel.getCodTipoUfficioInserimento() != null
				&& !fascicoloSigeModel.getCodTipoUfficioInserimento().equals("")
				&& (fascicoloSigeModel.getCodTipoUfficioInserimento().equals("PMM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("DIBM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("GIPM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("CAPSM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("GUPM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("UDSM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("TDSM"))
				&& anni <= 24) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
		}
		// e) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// e dagli uffici DIBM-GIPM-CAPSM-UDSM-TDSM
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo
		// la 'Età presunta' alla data di sistema ha più di 25 anni;
		// MERGE v10 COLLAUDO: sostituito 25 con 24
		else if (fascicoloSigeModel.getCodTipoUfficioInserimento() != null
				&& !fascicoloSigeModel.getCodTipoUfficioInserimento().equals("")
				&& (fascicoloSigeModel.getCodTipoUfficioInserimento().equals("PMM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("DIBM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("GIPM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("CAPSM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("GUPM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("UDSM")
						|| fascicoloSigeModel.getCodTipoUfficioInserimento().equals("TDSM"))
				&& anni > 24) {
			// etichetta non visibile
			etichettaEta = "";
		}

		// f) il soggetto iscritto dalla Procura Generale presso la Corte di Appello è sempre 'MAGGIORENNE';
		// CONDIZIONE INSERITA NEL PUNTO a)

		// g) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è 'MINORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta' alla
		// data di sistema ha meno di 18 anni (per precisione, meno di 18 anni ed un giorno);
		else if (fascicoloSigeModel.getCodTipoUfficioInserimento() != null
				&& !fascicoloSigeModel.getCodTipoUfficioInserimento().equals("")
				&& fascicoloSigeModel.getCodTipoUfficioInserimento().equals("PGCAP") && anni <= 18
				&& !anni_18_Maggiorenne) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#FF0040'>&nbsp;Minorenne (Anni "
					+ anni + ")&nbsp;</span>";
		}
		// h) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo
		// la 'Età presunta' alla data di sistema ha più di 18 anni ma meno di 25 anni
		// ed avente campo VISIBILITA_EX_MINORENNE = 'N';
		// MERGE v10 COLLAUDO: sostituito 25 con 24
		else if (fascicoloSigeModel.getCodTipoUfficioInserimento() != null
				&& !fascicoloSigeModel.getCodTipoUfficioInserimento().equals("")
				&& fascicoloSigeModel.getCodTipoUfficioInserimento().equals("PGCAP") && anni <= 24) {

			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
		}
		// i) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta'
		// alla data di sistema ha più di 18 anni ma meno di 26 anni ed avente
		// campo VISIBILITA_EX_MINORENNE = '';
		else if (fascicoloSigeModel.getCodTipoUfficioInserimento() != null
				&& !fascicoloSigeModel.getCodTipoUfficioInserimento().equals("")
				&& fascicoloSigeModel.getCodTipoUfficioInserimento().equals("PGCAP") && anni <= 24) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
		}
		// j) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure
		// secondo la 'Età presunta' alla data di sistema ha più di 25 anni.
		// MERGE v10 COLLAUDO: sostituito 25 con 24
		else if (fascicoloSigeModel.getCodTipoUfficioInserimento() != null
				&& !fascicoloSigeModel.getCodTipoUfficioInserimento().equals("")
				&& fascicoloSigeModel.getCodTipoUfficioInserimento().equals("PGCAP") && anni > 24) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
		}

		// valore di ritorno
		return etichettaEta;
	}

}