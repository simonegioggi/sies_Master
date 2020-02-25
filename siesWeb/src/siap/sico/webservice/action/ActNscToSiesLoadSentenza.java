package siap.sico.webservice.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.DATIUTENTEDocument;
import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.siep.sentenza.model.SentenzaModel;

public class ActNscToSiesLoadSentenza extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	private String mCodUfficio = "";

	public ActNscToSiesLoadSentenza(String aCodUfficio) {
		mCodUfficio = aCodUfficio;
	}

	public SentenzaModel processRequest(ANAGRAFICADocument.ANAGRAFICA adatiAnagrafica,
			DATIUTENTEDocument.DATIUTENTE adatiUtente) throws Exception {

		SentenzaModel lSentenzaModel = new SentenzaModel();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("ActNscToLoadSentenza - TITOLO ESECUTIVO");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");

		Integer lAnno, lMese, lGiorno;
		String lCodiceProvvedimento = "-", lCodiceAutorita = "-", lCodiceTipoRito = "-", lCodiceTipoRif = "-";
		String lCodiceAutoritaRif = "-", lCodLuogoEmittente = "-", lCodLuogoEmittenteDist = "-",
				/* lFlagSentenzaApplicazionePena = "", */ lCodLuogoProvvRif = "-";
		String lAnnoAppo, lMeseAppo, lGiornoAppo, lCodLuogoProvvRifDist = "-";
		String lCodTipoDecisioneCassazione = "-";
		String lDesLuogoEmittenteDist = "", lDesLuogoEmittenteDistRif = "";
		// String lCodiceSezione="-", lCodiceSezioneRif="-";

		/*************************************************************/
		/********************* TITOLO ESECUTIVO *********************/
		/*************************************************************/

		// ----> GESTIONE DECODIFICA COD_TIPO_PROVVEDIMENTO
		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getCODITIPOPROVV() != null) {
			CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_PROVVEDIMENTO", adatiAnagrafica
					.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getCODITIPOPROVV());
			lCodiceProvvedimento = lCodiciSIESNSCModel.getCoSies().trim(); // Tipo Provvedimento
			if (lCodiciSIESNSCModel.getCoVal1() != null) {
				// lFlagSentenzaApplicazionePena = lCodiciSIESNSCModel.getCoVal1().trim();
			}
		} else {
			lCodiceProvvedimento = "-";
			// lFlagSentenzaApplicazionePena = "N";
		}
		if (lCodiceProvvedimento.equals("01")
				&& adatiAnagrafica.getPROCEDIMENTO().getFLAGSENTENZASTRANIERA().equals("S")) {
			lCodiceProvvedimento = "05"; // Flag Sentenza Straniera
		}
		lSentenzaModel.setCodTipoProvvedimento(lCodiceProvvedimento);
		// [MEV REL. 5.0] - Gestione Eliminazione dalla Sentenza del FlagSentenzaApplicazPena -
		// FlagGiudizioAbbreviato
		// lSentenzaModel.setFlagSentenzaApplicazPena(lFlagSentenzaApplicazionePena);
		// ----> FINE GESTIONE DECODIFICA COD_TIPO_PROVVEDIMENTO

		// SENTENZA.ANNO_REGE_PM
		if (adatiAnagrafica.getPROCEDIMENTO().getANNONOTIZIAREATO() != 0) {
			lSentenzaModel
					.setAnnoRegePm(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO().getANNONOTIZIAREATO()));
		} else {
			lSentenzaModel.setAnnoRegePm(null);
		}

		// SENTENZA.NUMERO_REGE_PM
		if (adatiAnagrafica.getPROCEDIMENTO().getNUMENOTIZIAREATO() != null) {
			lSentenzaModel.setNumeroRegePm(adatiAnagrafica.getPROCEDIMENTO().getNUMENOTIZIAREATO());
		} else {
			lSentenzaModel.setNumeroRegePm(null);
		}

		// [MEV REL. 5.0] - Gestione Eliminazione DATA_ARRIVO_ATTO dalla Sentenza
		// SENTENZA.DATA_ARRIVO_ATTO
		// lSentenzaModel.setDataArrivoAtto(DateUtils.getSysDate());

		// ANNO.DATA_PROVVEDIMENTO
		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getDATAPROVVEDIMENTO() != null
				&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getDATAPROVVEDIMENTO().getANNO() != null
				&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getDATAPROVVEDIMENTO().getANNO().equals("")) {
			lAnnoAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
					.getDATAPROVVEDIMENTO().getANNO();
		} else {
			lAnnoAppo = null;
		}
		// MESE.DATA_PROVVEDIMENTO
		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getDATAPROVVEDIMENTO() != null
				&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getDATAPROVVEDIMENTO().getMESE() != null
				&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getDATAPROVVEDIMENTO().getMESE().equals("")) {
			lMeseAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
					.getDATAPROVVEDIMENTO().getMESE();
		} else {
			lMeseAppo = null;
		}
		// GIORNO.DATA_PROVVEDIMENTO
		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getDATAPROVVEDIMENTO() != null
				&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getDATAPROVVEDIMENTO().getGIORNO() != null
				&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getDATAPROVVEDIMENTO().getGIORNO().equals("")) {
			lGiornoAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
					.getDATAPROVVEDIMENTO().getGIORNO();
		} else {
			lGiornoAppo = null;
		}
		Date lDataProvvedimento;
		if ((lAnnoAppo != null && !lAnnoAppo.equals("")) && (lMeseAppo != null && !lMeseAppo.equals(""))
				&& (lGiornoAppo != null && !lGiornoAppo.equals(""))) {
			lAnno = new Integer(lAnnoAppo);
			lMese = new Integer(lMeseAppo);
			lGiorno = new Integer(lGiornoAppo);
			lDataProvvedimento = DateUtils.getDate(lAnno.intValue(), lMese.intValue(), lGiorno.intValue());
		} else {
			lDataProvvedimento = null;
		}
		lSentenzaModel.setDataProvvedimento(lDataProvvedimento);

		// DECODIFICA COD_TIPO_AUTORITA_EMITTENTE
		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getCODIAUTORITA() != null) {
			CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_UFFICIO_EMITTENTE", adatiAnagrafica
					.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getCODIAUTORITA());
			lCodiceAutorita = lCodiciSIESNSCModel.getCoSies().trim();
			if (!lCodiciSIESNSCModel.getCoVal1().equals("-")) {
				lCodiceTipoRito = lCodiciSIESNSCModel.getCoVal1().trim();
			}
			/*
			 * if (!lCodiciSIESNSCModel.getCoVal2().equals("-")) { lCodiceSezione =
			 * lCodiciSIESNSCModel.getCoVal2().trim(); }
			 */
		} else {
			lCodiceAutorita = "-";
			lCodiceTipoRito = "-";
			// lCodiceSezione="-";
		}
		lSentenzaModel.setCodTipoAutoritaEmittente(lCodiceAutorita);
		lSentenzaModel.setCodTipoRito(lCodiceTipoRito);

		// ------------------------------------------------------------------------------------------------
		// DECODIFICA - SENTENZA.COD_LUOGO_EMITTENTE (PRINCIPALE)
		lCodLuogoEmittente = "-";
		lCodLuogoEmittenteDist = "-";
		lDesLuogoEmittenteDist = "";
		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getCODISEDEAUTORITAPRINDIST() != null
				&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getCODISEDEAUTORITAPRINDIST().equals("")) {
			CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("COMUNE", adatiAnagrafica.getPROCEDIMENTO()
					.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getCODISEDEAUTORITAPRINDIST());
			lCodLuogoEmittenteDist = lCodiciSIESNSCModel.getCoSies().trim();
			lDesLuogoEmittenteDist = lCodiciSIESNSCModel.getCoSiesDes().trim();
		}

		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getCODISEDEAUTORITAPRIN() != null
				&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getCODISEDEAUTORITAPRIN().equals("")) {
			CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("COMUNE", adatiAnagrafica.getPROCEDIMENTO()
					.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getCODISEDEAUTORITAPRIN());
			lCodLuogoEmittente = lCodiciSIESNSCModel.getCoSies().trim();
		}

		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getCODISEDEAUTORITAPRIN() != null
				&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getCODISEDEAUTORITAPRIN().equals("")) {
			lSentenzaModel.setCodLuogoEmittente(lCodLuogoEmittente);
			// SENTANZA.NUM_SEZIONE_AUTORITA_EMITTENTE (IN PRINDIST c'è la DISTACCATA)
			String lNumSezioneAutoritaEmittente = "Distaccata" + " " + lDesLuogoEmittenteDist;
			if (lNumSezioneAutoritaEmittente.length() > 100) {
				lSentenzaModel.setNumSezioneAutoritaEmittente(lNumSezioneAutoritaEmittente.substring(0, 100));
			} else {
				lSentenzaModel.setNumSezioneAutoritaEmittente(lNumSezioneAutoritaEmittente);
			}
		} else {
			lSentenzaModel.setCodLuogoEmittente(lCodLuogoEmittenteDist);
			// In questo caso non c'è la Sede DISTACCATA
		}
		// ------------------------------------------------------------------------------------------------

		// ANNO_SENTENZA
		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getANNOSENTENZA() != 0) {
			lSentenzaModel.setAnnoSentenza(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO()
					.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getANNOSENTENZA()));
		} else {
			lSentenzaModel.setAnnoSentenza(new BigDecimal(0));
		}
		// NUMERO_SENTENZA
		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getNUMEROSENTENZA() != null) {
			lSentenzaModel.setNumeroSentenza(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
					.getDATIPROVVEDIMENTO().getNUMEROSENTENZA());
		} else {
			// FATTO SOLO PER TEST - VERIFICARE
			lSentenzaModel.setNumeroSentenza("1"); // VERIFICARE COME COMPORTARSI NEI CASI IN CUI NSC NON
													// PASSA ANNO E NUMERO SENTENZA.
		}

		// [MEV REL. 5.0] - Gestione Eliminazione dalla Sentenza della DATA_IRREVOCABILITA
		// ANNO DATAPASSAGGIOGIUDICATO
		/*
		 * if ( adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO() != null &&
		 * adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getANNO() != null &&
		 * !adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getANNO().equals("")) { lAnnoAppo =
		 * adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getANNO(); } else { lAnnoAppo=null; }
		 * // MESE DATAPASSAGGIOGIUDICATO if ( adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO()
		 * != null && adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getMESE() != null &&
		 * !adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getMESE().equals("")) { lMeseAppo =
		 * adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getMESE(); } else { lMeseAppo=null; }
		 *
		 * // GIORNO DATAPASSAGGIOGIUDICATO if ( adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO()
		 * != null && adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getGIORNO() != null &&
		 * !adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getGIORNO().equals("")) {
		 * lGiornoAppo = adatiAnagrafica.getPROCEDIMENTO().getDATAPASSAGGIOGIUDICATO().getGIORNO(); } else {
		 * lGiornoAppo=null; }
		 *
		 * Date lDataIrrevocabilita; if ((lAnnoAppo != null && !lAnnoAppo.equals("")) && (lMeseAppo != null &&
		 * !lMeseAppo.equals("")) && (lGiornoAppo != null && !lGiornoAppo.equals(""))) { lAnno = new
		 * Integer(lAnnoAppo); lMese = new Integer(lMeseAppo); lGiorno = new Integer(lGiornoAppo);
		 * lDataIrrevocabilita = DateUtils.getDate(lAnno.intValue(),lMese.intValue(), lGiorno.intValue()); }
		 * else { lDataIrrevocabilita=null; } lSentenzaModel.setDataIrrevocabilita(lDataIrrevocabilita);
		 */

		// SENTENZA.FLAG_SENTENZA_APPLICAZ_PENA - SENTENZA.FLAG_GIUDIZIO_ABBREVIATO - Da Verificare ??????????
		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getCODIFORMAPROC() != null
				&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getCODIFORMAPROC().equals("06")) {
			// lSentenzaModel.setFlagSentenzaApplicazPena("S"); // Valorizzato attraverso la decodifica del
			// TIPO_PROVVEDIMENTO
		} else if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getCODIFORMAPROC() != null
				&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getCODIFORMAPROC().equals("01")) {
			lSentenzaModel.setFlagGiudizioAbbreviato("S");
		} else {
			lSentenzaModel.setFlagGiudizioAbbreviato("N");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------- TITOLO ESECUTIVO ----------  ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("SentenzaModel ANNO_REGE_PM            : " + lSentenzaModel.getAnnoRegePm());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("SentenzaModel NUMERO_REGE_PM          : " + lSentenzaModel.getNumeroRegePm());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("SentenzaModel DATA_Provvedimento      : " + lSentenzaModel.getDataProvvedimento());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(
				"SentenzaModel NumSezAutorEmittente    : " + lSentenzaModel.getNumSezioneAutoritaEmittente());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------- DATI QUERY TE ---------  ");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.info("SentenzaModel CodiceProvvedimento     : " + lSentenzaModel.getCodTipoProvvedimento());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(
				"SentenzaModel CodiceTipoAutoritaEmit  : " + lSentenzaModel.getCodTipoAutoritaEmittente());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("SentenzaModel CodiceLuogoEmittente    : " + lSentenzaModel.getCodLuogoEmittente());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("SentenzaModel Anno Sentenza           : " + lSentenzaModel.getAnnoSentenza());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("SentenzaModel Numero Sentenza         : " + lSentenzaModel.getNumeroSentenza());

		/*************************************************************************************************/
		/* Prelevo dati PRIMO GRADO se utente PROCURA GENERALE ALTRIMENTI Prelevo i dati SECONDO GRADO */
		/*************************************************************************************************/

		// DECODIFICA CODICE_TIPO_UFFICIO
		// String lCodTipoUfficio = "";
		// if (adatiUtente.getDATIUFFICIO().getCODICETIPOUFFICIO() != null) {
		// CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_UFFICIO",
		// adatiUtente.getDATIUFFICIO().getCODICETIPOUFFICIO());
		// lCodTipoUfficio = lCodiciSIESNSCModel.getCoSies().trim();
		// }

		// Inizializziamo tali campi con "-" nel caso non scriviamo "ALtro Grado di Giudizio"
		lSentenzaModel.setCodTipoProvvRif(lCodiceTipoRif);
		lSentenzaModel.setCodTipoAutoritaProvvRif(lCodiceAutoritaRif);
		lSentenzaModel.setCodLuogoProvvRif(lCodLuogoProvvRif);

		// Se l'autorità emittente del Titolo Esecutivo è di 2° allora scriviamo in Altro Grado di Giudizio il
		// 1°
		if (lCodiceAutorita.equals("CAP") || lCodiceAutorita.equals("CAS") || lCodiceAutorita.equals("CASAP")
				|| lCodiceAutorita.equals("CAPSM") || lCodiceAutorita.equals("CAPMI")) {
			if (adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Len ArrayPrimoGrado:"
						+ adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado().getPRIMOGRADOArray().length);
				for (int i = 0; i <= adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
						.getPRIMOGRADOArray().length - 1; i++) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("ArrayPRIMOGRADO - Indice: " + i);
					// ---> Devo Scrivere L'ULTIMO ELEMENTO dell'ARRAY
					if (i == adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
							.getPRIMOGRADOArray().length - 1) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Sto Trattando ultimo elemento del PRIMO GRADO");
						if (adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado().getPRIMOGRADOArray(i)
								.getIMPUGNAZIONE().getCODITIPORIFERIMENTO() != null) {
							if (adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado().getPRIMOGRADOArray(i)
									.getIMPUGNAZIONE().getCODITIPORIFERIMENTO().equals("5")) {
								// conferma (Titolo Esecutivo di 2° Grado conferma Altro Grado di Giudizio di
								// 1°)
								lCodiceTipoRif = "03";
							} else {
								// in riforma (Titolo Esecutivo di 2° Grado in riforma Altro Grado di Giudizio
								// di 1°)
								lCodiceTipoRif = "04";
							}
						} else {
							lCodiceTipoRif = "-";
						}
						lSentenzaModel.setCodTipoProvvRif(lCodiceTipoRif);

						// SENTENZA.DATA_PROVV_RIF
						if (adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado().getPRIMOGRADOArray(i)
								.getIMPUGNAZIONE().getDATAPROVVEDIMENTO() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
										.getPRIMOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
										.getANNO() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
										.getPRIMOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
										.getANNO().equals("")) {
							lAnnoAppo = adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
									.getPRIMOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO().getANNO();
						} else {
							lAnnoAppo = null;
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado().getPRIMOGRADOArray(i)
								.getIMPUGNAZIONE().getDATAPROVVEDIMENTO() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
										.getPRIMOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
										.getMESE() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
										.getPRIMOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
										.getMESE().equals("")) {
							lMeseAppo = adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
									.getPRIMOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO().getMESE();
						} else {
							lMeseAppo = null;
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado().getPRIMOGRADOArray(i)
								.getIMPUGNAZIONE().getDATAPROVVEDIMENTO() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
										.getPRIMOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
										.getGIORNO() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
										.getPRIMOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
										.getGIORNO().equals("")) {
							lGiornoAppo = adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
									.getPRIMOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
									.getGIORNO();
						} else {
							lGiornoAppo = null;
						}

						Date lDataProvvedimento1G;
						if ((lAnnoAppo != null && !lAnnoAppo.equals(""))
								&& (lMeseAppo != null && !lMeseAppo.equals(""))
								&& (lGiornoAppo != null && !lGiornoAppo.equals(""))) {
							lAnno = new Integer(lAnnoAppo);
							lMese = new Integer(lMeseAppo);
							lGiorno = new Integer(lGiornoAppo);
							lDataProvvedimento1G = DateUtils.getDate(lAnno.intValue(), lMese.intValue(),
									lGiorno.intValue());
						} else {
							lDataProvvedimento1G = null;
						}
						lSentenzaModel.setDataProvvRif(lDataProvvedimento1G);

						// DECODIFICA COD_TIPO_AUTORITA_PROVV_RIF
						if (adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado().getPRIMOGRADOArray(i)
								.getIMPUGNAZIONE().getCODIAUTORITA() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_UFFICIO_EMITTENTE",
									adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
											.getPRIMOGRADOArray(i).getIMPUGNAZIONE().getCODIAUTORITA());
							lCodiceAutoritaRif = lCodiciSIESNSCModel.getCoSies().trim();
							/*
							 * if (!lCodiciSIESNSCModel.getCoVal2().equals("-")) { lCodiceSezioneRif =
							 * lCodiciSIESNSCModel.getCoVal2().trim(); }
							 */
						} else {
							lCodiceAutoritaRif = "-";
							// lCodiceSezioneRif="-";
						}
						lSentenzaModel.setCodTipoAutoritaProvvRif(lCodiceAutoritaRif);

						// ANNO_PROVV_RIF
						if (adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado().getPRIMOGRADOArray(i)
								.getIMPUGNAZIONE().getANNOSENTENZA() != 0) {
							lSentenzaModel.setAnnoProvvRif(
									new BigDecimal(adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
											.getPRIMOGRADOArray(i).getIMPUGNAZIONE().getANNOSENTENZA()));
						} else {
							lSentenzaModel.setAnnoProvvRif(null);
						}

						// NUMERO_PROVV_RIF
						if (adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado().getPRIMOGRADOArray(i)
								.getIMPUGNAZIONE().getNUMEROSENTENZA() != null) {
							lSentenzaModel.setNumeroProvvRif(adatiAnagrafica.getPROCEDIMENTO()
									.getArrayPrimoGrado().getPRIMOGRADOArray(i).getIMPUGNAZIONE()
									.getNUMEROSENTENZA().toString());
						} else {
							lSentenzaModel.setNumeroProvvRif(null);
						}

						// ------------------------------------------------------------------------------------------------
						// DECODIFICA - SENTENZA.COD_LUOGO_PROVV_RIF (PRINCIPALE)
						lCodLuogoProvvRif = "-";
						lCodLuogoProvvRifDist = "-";
						lDesLuogoEmittenteDistRif = "";
						if (adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado().getPRIMOGRADOArray(i)
								.getIMPUGNAZIONE().getCODISEDEAUTORITAPRINDIST() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
										.getPRIMOGRADOArray(i).getIMPUGNAZIONE().getCODISEDEAUTORITAPRINDIST()
										.equals("")) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("COMUNE",
									adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
											.getPRIMOGRADOArray(i).getIMPUGNAZIONE()
											.getCODISEDEAUTORITAPRINDIST());
							lCodLuogoProvvRifDist = lCodiciSIESNSCModel.getCoSies().trim();
							lDesLuogoEmittenteDistRif = lCodiciSIESNSCModel.getCoSiesDes().trim();
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado().getPRIMOGRADOArray(i)
								.getIMPUGNAZIONE().getCODISEDEAUTORITAPRIN() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
										.getPRIMOGRADOArray(i).getIMPUGNAZIONE().getCODISEDEAUTORITAPRIN()
										.equals("")) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("COMUNE",
									adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
											.getPRIMOGRADOArray(i).getIMPUGNAZIONE()
											.getCODISEDEAUTORITAPRIN());
							lCodLuogoProvvRif = lCodiciSIESNSCModel.getCoSies().trim();
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado().getPRIMOGRADOArray(i)
								.getIMPUGNAZIONE().getCODISEDEAUTORITAPRIN() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getArrayPrimoGrado()
										.getPRIMOGRADOArray(i).getIMPUGNAZIONE().getCODISEDEAUTORITAPRIN()
										.equals("")) {
							lSentenzaModel.setCodLuogoProvvRif(lCodLuogoProvvRif);
							// SENTANZA.NUM_SEZIONE_AUTORITA_EMITTENTE (IN PRINDIST c'è la DISTACCATA)
							String lNumSezioneAutoritaProvvRif = "Distaccata" + " "
									+ lDesLuogoEmittenteDistRif;
							if (lNumSezioneAutoritaProvvRif.length() > 100) {
								lSentenzaModel.setNumSezioneAutoritaProvvRif(
										lNumSezioneAutoritaProvvRif.substring(0, 100));
							} else {
								lSentenzaModel.setNumSezioneAutoritaProvvRif(lNumSezioneAutoritaProvvRif);
							}
						} else {
							lSentenzaModel.setCodLuogoProvvRif(lCodLuogoProvvRifDist);
							// In questo caso non c'è la Sede DISTACCATA
						}
						// ------------------------------------------------------------------------------------------------

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("---------- DATI QUERY ARRAY PRIMOGRADO ----------   ");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("SentenzaModel CodiceProvvedimentoRif   : "
								+ lSentenzaModel.getCodTipoProvvRif());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("SentenzaModel DataProvvRif             : "
								+ lSentenzaModel.getDataProvvRif());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("SentenzaModel CodiceTipoAutoritaRif    : "
								+ lSentenzaModel.getCodTipoAutoritaProvvRif());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("SentenzaModel AnnoProvvRif             : "
								+ lSentenzaModel.getAnnoProvvRif());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("SentenzaModel NumeroProvvRif           : "
								+ lSentenzaModel.getNumeroProvvRif());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("SentenzaModel CodLuogoProvvRif         : "
								+ lSentenzaModel.getCodLuogoProvvRif());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("---------- ARRAY PRIMOGRADO ----------   ");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("SentenzaModel NumsezAutoritaProvvRif   : "
								+ lSentenzaModel.getNumSezioneAutoritaProvvRif());
					} // Chiude Test per prendere ultimo elemento dell'array
				} // chiude ciclo for
			} // Array Null
		} // utente ufficio che trascrive titolo esecutivo
		else {
			// Titolo Esecutivo emesse da Autorità di 1° scriviamo in Altro Grado di Giudizio ultima sentenza
			// di 2°
			if (adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Len Secondo Grado:" + adatiAnagrafica.getPROCEDIMENTO()
						.getArraySecondoGrado().getSECONDOGRADOArray().length);

				for (int i = 0; i <= (adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
						.getSECONDOGRADOArray().length - 1); i++) {
					if (i == adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
							.getSECONDOGRADOArray().length - 1) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Sto Trattando ultimo elemento del SECONDO GRADO");

						// SENTENZA.DATA_PROVV_RIF
						if (adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado().getSECONDOGRADOArray(i)
								.getIMPUGNAZIONE().getDATAPROVVEDIMENTO() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
										.getSECONDOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
										.getANNO() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
										.getSECONDOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
										.getANNO().equals("")) {
							lAnnoAppo = adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
									.getSECONDOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
									.getANNO();
						} else {
							lAnnoAppo = null;
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado().getSECONDOGRADOArray(i)
								.getIMPUGNAZIONE().getDATAPROVVEDIMENTO() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
										.getSECONDOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
										.getMESE() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
										.getSECONDOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
										.getMESE().equals("")) {
							lMeseAppo = adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
									.getSECONDOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
									.getMESE();
						} else {
							lMeseAppo = null;
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado().getSECONDOGRADOArray(i)
								.getIMPUGNAZIONE().getDATAPROVVEDIMENTO() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
										.getSECONDOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
										.getGIORNO() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
										.getSECONDOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
										.getGIORNO().equals("")) {
							lGiornoAppo = adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
									.getSECONDOGRADOArray(i).getIMPUGNAZIONE().getDATAPROVVEDIMENTO()
									.getGIORNO();
						} else {
							lGiornoAppo = null;
						}

						Date lDataProvvedimento2G;
						if ((lAnnoAppo != null && !lAnnoAppo.equals(""))
								&& (lMeseAppo != null && !lMeseAppo.equals(""))
								&& (lGiornoAppo != null && !lGiornoAppo.equals(""))) {
							lAnno = new Integer(lAnnoAppo);
							lMese = new Integer(lMeseAppo);
							lGiorno = new Integer(lGiornoAppo);
							lDataProvvedimento2G = DateUtils.getDate(lAnno.intValue(), lMese.intValue(),
									lGiorno.intValue());
						} else {
							lDataProvvedimento2G = null;
						}

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Data Provv 2°:" + lDataProvvedimento2G);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Data Provv   :" + lDataProvvedimento);

						// Se la Data del Provvedimento di 2 Grado è maggiore della Data del Provvedimento del
						// Titolo Esecutivo allora Scriviamo i
						// dati nella sezione "Altro Gardo di Giudizio" (_RIF) (in quanto T.E. è quello di 1°
						// Grado Corte di Appello / Cassazione confermano il 1°)
						// if (lDataProvvedimento2G.after(lDataProvvedimento))
						if (DateUtils.isGreater(lDataProvvedimento2G, lDataProvvedimento)) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Data Provv 2° > Data Provv T.E.");
							// SENTENZA.CODI_TIPO_PROVV_RIF - Sentenza da eseguire è un 2 Grado
							if (adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
									.getSECONDOGRADOArray(i).getIMPUGNAZIONE()
									.getCODITIPORIFERIMENTO() != null) {
								if (adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
										.getSECONDOGRADOArray(i).getIMPUGNAZIONE().getCODITIPORIFERIMENTO()
										.equals("5")) {
									lCodiceTipoRif = "01"; // confermata (Titolo Esecutivo di 1° confermato da
															// Altro Grado di Giudizio di 2°)
								} else {
									lCodiceTipoRif = "02"; // riformata (Titolo Esecutivo di 1° riformata da
															// Altro Grado di Giudizio di 2°)
								}
							} else {
								lCodiceTipoRif = "-";
							}
							lSentenzaModel.setCodTipoProvvRif(lCodiceTipoRif);

							// SENTENZA.DATA_PROVV_RIF
							lSentenzaModel.setDataProvvRif(lDataProvvedimento2G);

							// SENTENZA.CODI_TIPO_AUTORITA_PROVV_RIF
							if (adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
									.getSECONDOGRADOArray(i).getIMPUGNAZIONE().getCODIAUTORITA() != null) {
								CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_UFFICIO_EMITTENTE",
										adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
												.getSECONDOGRADOArray(i).getIMPUGNAZIONE().getCODIAUTORITA());
								lCodiceAutoritaRif = lCodiciSIESNSCModel.getCoSies().trim();
								/*
								 * if (!lCodiciSIESNSCModel.getCoVal2().equals("-")) { lCodiceSezioneRif =
								 * lCodiciSIESNSCModel.getCoVal2().trim(); }
								 */
							} else {
								lCodiceAutoritaRif = "-";
								// lCodiceSezioneRif="-";
							}
							lSentenzaModel.setCodTipoAutoritaProvvRif(lCodiceAutoritaRif);

							// SENTENZA.ANNO_PROVV_RIF
							if (adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
									.getSECONDOGRADOArray(i).getIMPUGNAZIONE().getANNOSENTENZA() != 0) {
								lSentenzaModel.setAnnoProvvRif(new BigDecimal(adatiAnagrafica
										.getPROCEDIMENTO().getArraySecondoGrado().getSECONDOGRADOArray(i)
										.getIMPUGNAZIONE().getANNOSENTENZA()));
							} else {
								lSentenzaModel.setAnnoProvvRif(null);
							}

							// SENTENZA.NUMERO_PROVV_RIF
							if (adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
									.getSECONDOGRADOArray(i).getIMPUGNAZIONE().getNUMEROSENTENZA() != null) {
								lSentenzaModel.setNumeroProvvRif(adatiAnagrafica.getPROCEDIMENTO()
										.getArraySecondoGrado().getSECONDOGRADOArray(i).getIMPUGNAZIONE()
										.getNUMEROSENTENZA().toString());
							} else {
								lSentenzaModel.setNumeroProvvRif(null);
							}

							// ------------------------------------------------------------------------------------------------
							// DECODIFICA - SENTENZA.COD_LUOGO_PROVV_RIF (PRINCIPALE)
							lCodLuogoProvvRif = "-";
							lCodLuogoProvvRifDist = "-";
							lDesLuogoEmittenteDistRif = "";

							if (adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
									.getSECONDOGRADOArray(i).getIMPUGNAZIONE()
									.getCODISEDEAUTORITAPRINDIST() != null
									&& !adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
											.getSECONDOGRADOArray(i).getIMPUGNAZIONE()
											.getCODISEDEAUTORITAPRINDIST().equals("")) {
								CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("COMUNE",
										adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
												.getSECONDOGRADOArray(i).getIMPUGNAZIONE()
												.getCODISEDEAUTORITAPRINDIST());
								lCodLuogoProvvRifDist = lCodiciSIESNSCModel.getCoSies().trim();
								lDesLuogoEmittenteDistRif = lCodiciSIESNSCModel.getCoSiesDes().trim();
							}

							if (adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
									.getSECONDOGRADOArray(i).getIMPUGNAZIONE()
									.getCODISEDEAUTORITAPRIN() != null
									&& !adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
											.getSECONDOGRADOArray(i).getIMPUGNAZIONE()
											.getCODISEDEAUTORITAPRIN().equals("")) {
								CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("COMUNE",
										adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
												.getSECONDOGRADOArray(i).getIMPUGNAZIONE()
												.getCODISEDEAUTORITAPRIN());
								lCodLuogoProvvRif = lCodiciSIESNSCModel.getCoSies().trim();
							}

							if (adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
									.getSECONDOGRADOArray(i).getIMPUGNAZIONE()
									.getCODISEDEAUTORITAPRIN() != null
									&& !adatiAnagrafica.getPROCEDIMENTO().getArraySecondoGrado()
											.getSECONDOGRADOArray(i).getIMPUGNAZIONE()
											.getCODISEDEAUTORITAPRIN().equals("")) {
								lSentenzaModel.setCodLuogoProvvRif(lCodLuogoProvvRif);
								// SENTANZA.NUM_SEZIONE_AUTORITA_EMITTENTE (IN PRINDIST c'è la DISTACCATA)
								String lNumSezioneAutoritaProvvRif = "Distaccata" + " "
										+ lDesLuogoEmittenteDistRif;
								if (lNumSezioneAutoritaProvvRif.length() > 100) {
									lSentenzaModel.setNumSezioneAutoritaProvvRif(
											lNumSezioneAutoritaProvvRif.substring(0, 100));
								} else {
									lSentenzaModel.setNumSezioneAutoritaProvvRif(lNumSezioneAutoritaProvvRif);
								}
							} else {
								lSentenzaModel.setCodLuogoProvvRif(lCodLuogoProvvRifDist);
								// In questo caso non c'è la Sede DISTACCATA
							}

							// ------------------------------------------------------------------------------------------------
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("---------- QUERY ARRAY SECONDOGRADO ----------   ");
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("SentenzaModel CodiceProvvedimentoRif   : "
									+ lSentenzaModel.getCodTipoProvvRif());
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("SentenzaModel DataProvvRif             : "
									+ lSentenzaModel.getDataProvvRif());
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("SentenzaModel CodiceTipoAutoritaRif    : "
									+ lSentenzaModel.getCodTipoAutoritaProvvRif());
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("SentenzaModel AnnoProvvRif             : "
									+ lSentenzaModel.getAnnoProvvRif());
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("SentenzaModel NumeroProvvRif           : "
									+ lSentenzaModel.getNumeroProvvRif());
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("SentenzaModel CodLuogoProvvRif         : "
									+ lSentenzaModel.getCodLuogoProvvRif());
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("---------- ARRAY SECONDOGRADO ----------   ");
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("SentenzaModel NumsezAutoritaProvvRif   : "
									+ lSentenzaModel.getNumSezioneAutoritaProvvRif());
						} // Chiude Test sulle date
					} // Chiude Test su Ultimo elemento array
				} // Chiude ciclo For
			} // Test Null Array Secondo Grado
		}

		lSentenzaModel.setCodTipoDecisioneCassazione(lCodTipoDecisioneCassazione);
		// MEV 16 CUMULO: gestione data del terzo grado nel campo note
		Date lDataProvvedimentoCassazione = null;

		if (adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione() != null) {
			for (int i = 0; i <= adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione()
					.getCASSAZIONEArray().length - 1; i++) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ArrayCassazione - Indice: " + i);

				if (adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
						.getIMPUGNAZIONE().getDATAPROVVEDIMENTO() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
								.getIMPUGNAZIONE().getDATAPROVVEDIMENTO().getANNO() != null
						&& !adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
								.getIMPUGNAZIONE().getDATAPROVVEDIMENTO().getANNO().equals("")) {
					lAnnoAppo = adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
							.getIMPUGNAZIONE().getDATAPROVVEDIMENTO().getANNO();
				} else {
					lAnnoAppo = null;
				}

				if (adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
						.getIMPUGNAZIONE().getDATAPROVVEDIMENTO() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
								.getIMPUGNAZIONE().getDATAPROVVEDIMENTO().getMESE() != null
						&& !adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
								.getIMPUGNAZIONE().getDATAPROVVEDIMENTO().getMESE().equals("")) {
					lMeseAppo = adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
							.getIMPUGNAZIONE().getDATAPROVVEDIMENTO().getMESE();
				} else {
					lMeseAppo = null;
				}

				if (adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
						.getIMPUGNAZIONE().getDATAPROVVEDIMENTO() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
								.getIMPUGNAZIONE().getDATAPROVVEDIMENTO().getGIORNO() != null
						&& !adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
								.getIMPUGNAZIONE().getDATAPROVVEDIMENTO().getGIORNO().equals("")) {
					lGiornoAppo = adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
							.getIMPUGNAZIONE().getDATAPROVVEDIMENTO().getGIORNO();
				} else {
					lGiornoAppo = null;
				}

				if ((lAnnoAppo != null && !lAnnoAppo.equals(""))
						&& (lMeseAppo != null && !lMeseAppo.equals(""))
						&& (lGiornoAppo != null && !lGiornoAppo.equals(""))) {
					lAnno = new Integer(lAnnoAppo);
					lMese = new Integer(lMeseAppo);
					lGiorno = new Integer(lGiornoAppo);
					lDataProvvedimentoCassazione = DateUtils.getDate(lAnno.intValue(), lMese.intValue(),
							lGiorno.intValue());
				} /* else { lDataProvvedimentoCassazione = null; } */

				// if (lDataProvvedimentoCassazione.after(lDataProvvedimento))
				if (DateUtils.isGreater(lDataProvvedimentoCassazione, lDataProvvedimento)) {
					// DECODIFICA - SENTENZA.CODI_TIPO_DECISIONE_CASSAZIONE
					// String lCodTipoDecisioneCassazione="-";
					if (adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
							.getIMPUGNAZIONE().getCODITIPORIFERIMENTO() != null) {
						CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_DECISIONE_CASSAZIONE",
								adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
										.getIMPUGNAZIONE().getCODITIPORIFERIMENTO());
						lCodTipoDecisioneCassazione = lCodiciSIESNSCModel.getCoSies().trim();
					} else {
						lCodTipoDecisioneCassazione = "-";
					}
					lSentenzaModel.setCodTipoDecisioneCassazione(lCodTipoDecisioneCassazione);

					// SENTENZA.ANNO_SENTENZA_CASSAZIONE
					if (adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
							.getIMPUGNAZIONE().getANNOSENTENZA() != 0) {
						lSentenzaModel.setAnnoSentenzaCassazione(
								new BigDecimal(adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione()
										.getCASSAZIONEArray(i).getIMPUGNAZIONE().getANNOSENTENZA()));
					} else {
						lSentenzaModel.setAnnoSentenzaCassazione(null);
					}

					// SENTENZA.NUMERO_SENTENZA_CASSAZIONE
					String lNumSentCass;
					if (adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
							.getIMPUGNAZIONE().getNUMEROSENTENZA() != null) {
						lNumSentCass = adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione()
								.getCASSAZIONEArray(i).getIMPUGNAZIONE().getNUMEROSENTENZA();
						lSentenzaModel.setNumeroSentenzaCassazione(
								adatiAnagrafica.getPROCEDIMENTO().getArrayCassazione().getCASSAZIONEArray(i)
										.getIMPUGNAZIONE().getNUMEROSENTENZA().toString());
					} else {
						lNumSentCass = null;
					}
					lSentenzaModel.setNumeroSentenzaCassazione(lNumSentCass);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("---------- ARRAY CASSAZIONE ----------   ");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("SentenzaModel CodTipoDecisioneCassazione   : "
							+ lSentenzaModel.getCodTipoDecisioneCassazione());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("SentenzaModel AnnoSentenzaCassazione       : "
							+ lSentenzaModel.getAnnoSentenzaCassazione());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("SentenzaModel NumeroSentenzaCassazione       : "
							+ lSentenzaModel.getNumeroSentenzaCassazione());
				} // Test Date
			} // Ciclo for
		} // Test Array null

		BigDecimal lAnnoRaccGen;
		String lNumeRaccGen;
		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getANNOREGISTROGENERALE() != 0) {
			lAnnoRaccGen = new BigDecimal(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
					.getDATIPROVVEDIMENTO().getANNOREGISTROGENERALE());
		} else
			lAnnoRaccGen = null;

		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getNUMEROREGISTROGENERALE() != null) {
			lNumeRaccGen = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
					.getNUMEROREGISTROGENERALE();
		} else
			lNumeRaccGen = null;

		// SENTENZA.ANNO_REGE_GIP - SENTENZA.NUMERO_REGE_GIP
		if (lSentenzaModel.getCodTipoAutoritaEmittente().equals("GIP")) {
			lSentenzaModel.setAnnoRegeGip(lAnnoRaccGen);
			lSentenzaModel.setNumeroRegeGip(lNumeRaccGen);
		}

		// SENTENZA.ANNO_REGE_DIB - SENTENZA.NUMERO_REGE_DIB
		if (lSentenzaModel.getCodTipoAutoritaEmittente().equals("DIB")) {
			lSentenzaModel.setAnnoRegeDib(lAnnoRaccGen);
			lSentenzaModel.setNumeroRegeDib(lNumeRaccGen);
		}

		// SENTENZA.ANNO_REGE_CAS - SENTENZA.NUMERO_REGE_CAS
		if (lSentenzaModel.getCodTipoAutoritaEmittente().equals("CAS")) {
			lSentenzaModel.setAnnoRegeCas(lAnnoRaccGen);
			lSentenzaModel.setNumeroRegeCas(lNumeRaccGen);
		}

		// SENTENZA.ANNO_REGE_CAP - SENTENZA.NUMERO_REGE_CAP
		if (lSentenzaModel.getCodTipoAutoritaEmittente().equals("CAP")) {
			lSentenzaModel.setAnnoRegeCap(lAnnoRaccGen);
			lSentenzaModel.setNumeroRegeCap(lNumeRaccGen);
		}

		// SENTENZA.ANNO_REGE_CASAP - SENTENZA.NUMERO_REGE_CASAP
		if (lSentenzaModel.getCodTipoAutoritaEmittente().equals("CASAP")) {
			lSentenzaModel.setAnnoRegeCasap(lAnnoRaccGen);
			lSentenzaModel.setNumeroRegeCasap(lNumeRaccGen);
		}

		// MEV_66: aggiunte quattro nuove proprietà
		// SENTENZA.ANNO_REGE_GUP - SENTENZA.NUMERO_REGE_GUP
		if (lSentenzaModel.getCodTipoAutoritaEmittente().equals("GUP")) {
			lSentenzaModel.setAnnoRegeGup(lAnnoRaccGen);
			lSentenzaModel.setNumeroRegeGup(lNumeRaccGen);
		}
		// SENTENZA.ANNO_REGE_CAPSM - SENTENZA.NUMERO_REGE_CAPSM
		if (lSentenzaModel.getCodTipoAutoritaEmittente().equals("CAPSM")) {
			lSentenzaModel.setAnnoRegeCapsm(lAnnoRaccGen);
			lSentenzaModel.setNumeroRegeCapsm(lNumeRaccGen);
		}

		// SENTENZA.COD_OPERATORE_INSERIMENTO
		lSentenzaModel.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME());

		// SENTENZA.DATA_INSERIMENTO
		lSentenzaModel.setDataInserimento(DateUtils.getSysDate());
		// SENTENZA.COD_UFFICIO_INSERIMENTO
		lSentenzaModel.setCodUfficioInserimento(mCodUfficio);

		// SENTENZA.DATA_ISCRIZIONE
		lSentenzaModel.setDataIscrizione(DateUtils.getSysDate());

		// ----> CAMPI VALORIZZATI a NULL
		// SENTENZA.NOTE1_DECISIONE_CASSAZIONE
		lSentenzaModel.setNote1DecisioneCassazione(null);
		// SENTENZA.NOTE2_DECISIONE_CASSAZIONE
		lSentenzaModel.setNote2DecisioneCassazione(null);
		// SENTENZA.ANNO_RACCOLTA_GENERALE
		lSentenzaModel.setAnnoRaccoltaGenerale(null);
		// SENTENZA.NUMERO_RACCOLTA_GENERALE
		lSentenzaModel.setNumeroRaccoltaGenerale(null);
		// SENTENZA.FLAG_ALTRE_SENTENZE
		lSentenzaModel.setFlagAltreSentenze(null);
		// SENTENZA.DESCR_ALTRE_SENTENZE
		lSentenzaModel.setDescrAltreSentenze(null);
		// SENTENZA.ANNO_REGISTRO_35
		lSentenzaModel.setAnnoRegistro35(null);
		// SENTENZA.NUMERO_REGISTRO_35
		lSentenzaModel.setNumRegistro35(null);
		// SENTENZA.NOTE
		lSentenzaModel.setNote(null);

		// [MEV REL. 5.0] - Campo Eliminato DESCR_NUM_CAMPIONE_PENALE
		// lSentenzaModel.setDescrNumCampionePenale(null);

		// SENTENZA.COD_OPERATORE_AGGIORNAMENTO
		lSentenzaModel.setCodOperatoreAggiornamento(null);
		// SENTENZA.DATA_AGGIORNAMENTO
		lSentenzaModel.setDataAggiornamento(null);
		// SENTENZA.COD_UFFICIO_AGGIORNAMENTO
		lSentenzaModel.setCodUfficioAggiornamento(null);
		// SENTENZA.COD_BILANCIAMENTO_CIRCOSTANZE
		lSentenzaModel.setCodBilanciamentoCircostanze("-");

		// [MEV REL. 5.0] - Gestione 3 Nuovi Campi Revisione Sentenza
		lSentenzaModel.setCodTipoProvvedimentoRif("-"); // "Sentenza" o "Ordinanza di inammisibilità"
		lSentenzaModel.setCodTipoProvvedimentoAltro("-"); // "Sentenza" o "Ordinanza di inammisibilità"
		lSentenzaModel.setCodSedeNotiziaReato("-"); // Sede PM

		/*******************************************************************************************/
		/* Se NSC manda il TAg Sentenza Straniera scriviamo qualche info sulle note della Sentenza */
		/*******************************************************************************************/
		String NoteSentenzaStraniera = "";
		if (adatiAnagrafica.getPROCEDIMENTO().getFLAGSENTENZASTRANIERA().equals("S")) {
			if (adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getDATAPROVVEDIMENTO() != null
					&& adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getDATAPROVVEDIMENTO()
							.getANNO() != null
					&& !adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getDATAPROVVEDIMENTO()
							.getANNO().equals("")) {
				lAnnoAppo = adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getDATAPROVVEDIMENTO()
						.getANNO();
			} else {
				lAnnoAppo = null;
			}

			if (adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getDATAPROVVEDIMENTO() != null
					&& adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getDATAPROVVEDIMENTO()
							.getMESE() != null
					&& !adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getDATAPROVVEDIMENTO()
							.getMESE().equals("")) {
				lMeseAppo = adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getDATAPROVVEDIMENTO()
						.getMESE();
			} else {
				lMeseAppo = null;
			}

			if (adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getDATAPROVVEDIMENTO() != null
					&& adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getDATAPROVVEDIMENTO()
							.getGIORNO() != null
					&& !adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getDATAPROVVEDIMENTO()
							.getGIORNO().equals("")) {
				lGiornoAppo = adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getDATAPROVVEDIMENTO()
						.getGIORNO();
			} else {
				lGiornoAppo = null;
			}

			if ((lAnnoAppo != null && !lAnnoAppo.equals("")) && (lMeseAppo != null && !lMeseAppo.equals(""))
					&& (lGiornoAppo != null && !lGiornoAppo.equals(""))) {
				NoteSentenzaStraniera = "Data Provvedimento:" + lAnnoAppo + "/" + lMeseAppo + "/"
						+ lGiornoAppo + " ";
			}

			if (adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getCODIAUTORITA() != null) {
				CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_UFFICIO_EMITTENTE",
						adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getCODIAUTORITA());
				NoteSentenzaStraniera = NoteSentenzaStraniera + "Codice autorità emittente:"
						+ lCodiciSIESNSCModel.getCoSies().trim() + " ";
			}

			// Decodifica Stato Straniero
			if (adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getCODISTATOCONDESTERO() != null) {
				CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("NAZIONE",
						adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getCODISTATOCONDESTERO());
				NoteSentenzaStraniera = NoteSentenzaStraniera + "Codice stato estero emittente:"
						+ lCodiciSIESNSCModel.getCoSies().trim() + " ";
			}

			if (adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getDESCLUOGOCONDESTERO() != null) {
				NoteSentenzaStraniera = NoteSentenzaStraniera + "Luogo emessa sentenza:"
						+ adatiAnagrafica.getPROCEDIMENTO().getSENTENZASTRANIERA().getDESCLUOGOCONDESTERO()
						+ " ";
			}
		}

		// Sentenza Straniera
		if (lSentenzaModel.getCodTipoProvvedimento().equals("05")) {
			lSentenzaModel.setNote(NoteSentenzaStraniera);
		} else if (lDataProvvedimentoCassazione != null) { // MEV 16 CUMULO: aggiunto ramo else if
			lSentenzaModel.setNote("Provvedimento emesso in data "
					+ DateUtils.getDateToString(lDataProvvedimentoCassazione, "dd/MM/yyyy"));
		} else {
			lSentenzaModel.setNote(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
					.getDATIPROVVEDIMENTO().getDESCNOTE());
		}

		return lSentenzaModel;
	}

}