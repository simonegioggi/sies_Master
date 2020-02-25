package siap.sico.webservice.action;

import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.DATIUTENTEDocument;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActNscToSiesLoadCircostanze extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String mCodUfficio = "";

	public ActNscToSiesLoadCircostanze(String aCodUfficio) {
		mCodUfficio = aCodUfficio;
	}

	public Vector processRequest(ANAGRAFICADocument.ANAGRAFICA adatiAnagrafica,
			DATIUTENTEDocument.DATIUTENTE adatiUtente, SentenzaModel lSentenzaModel) throws Exception {

		Vector lCircostanzeVector = new Vector();

		String lCodBilanciamentoCircostanze = "-";
		String lCodFonte = "-";
		String lCodSottoNumerazione = "-";
		String lCommaQualificante = "-";
		String lFlagSentenzaApplicazionePena = "", lFlagGiudizioAbbreviato = "";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("ActNscToLoadCircostanze - TITOLO ESECUTIVO");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");

		// [MEV REL. 5.0] - Gestione Eliminazione dalla Sentenza del FlagSentenzaApplicazPena -
		// FlagGiudizioAbbreviato

		// ----> Determino il FlagSentenzaApplicazionePena dalla decodifica del COD_TIPO_PROVVEDIMENTO
		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getCODITIPOPROVV() != null) {
			CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_PROVVEDIMENTO", adatiAnagrafica
					.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getCODITIPOPROVV());
			if (lCodiciSIESNSCModel.getCoVal1() != null) {
				lFlagSentenzaApplicazionePena = lCodiciSIESNSCModel.getCoVal1().trim(); // FlagSentenzaApplicazionePena
			}
		} else {
			lFlagSentenzaApplicazionePena = "N";
		}

		// ----> Determino il Flag Giudizio Abbreviato
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
			lFlagGiudizioAbbreviato = "S";
		} else {
			lFlagGiudizioAbbreviato = "N";
		}

		// ****************************************************** FINE MEV
		// ****************************************************

		// Gestione Flag Sentenza Applicazione Pena - Flag Giudizio Abbreviato
		if (lFlagSentenzaApplicazionePena.equals("S") && lFlagGiudizioAbbreviato.equals("S")) {
			CircostanzaModel lCircostanzaModel;
			// Giudizio Abbreviato
			lCircostanzaModel = new CircostanzaModel();
			lCircostanzaModel.setCodTipoCircostanza("-");
			lCircostanzaModel.setCodBilanciamentoCircostanze("-");
			lCircostanzaModel.setCodFonte("-");
			lCircostanzaModel.setCodSottonumerazione("-");
			lCircostanzaModel.setCommaQualificante("-");
			lCircostanzaModel.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
			lCircostanzaModel.setDataInserimento(DateUtils.getSysDate());
			lCircostanzaModel.setCodUfficioInserimento(mCodUfficio);

			lCircostanzaModel.setCodFonte("25");
			lCircostanzaModel.setArticolo("442");
			lCircostanzaModel.setFlagGiudizioAbbreviato("S");
			lCircostanzaModel.setFlagSentenzaApplicazPena("S");

			lCircostanzeVector.add(lCircostanzaModel);

			// Sentenza Applicazione Pena
			lCircostanzaModel = new CircostanzaModel();

			lCircostanzaModel.setCodTipoCircostanza("-");
			lCircostanzaModel.setCodBilanciamentoCircostanze("-");
			lCircostanzaModel.setCodFonte("-");
			lCircostanzaModel.setCodSottonumerazione("-");
			lCircostanzaModel.setCommaQualificante("-");
			lCircostanzaModel.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
			lCircostanzaModel.setDataInserimento(DateUtils.getSysDate());
			lCircostanzaModel.setCodUfficioInserimento(mCodUfficio);

			lCircostanzaModel.setCodFonte("25");
			lCircostanzaModel.setArticolo("444");
			lCircostanzaModel.setFlagGiudizioAbbreviato("S");
			lCircostanzaModel.setFlagSentenzaApplicazPena("S");

			lCircostanzeVector.add(lCircostanzaModel);
		} else if (lFlagGiudizioAbbreviato.equals("S")) {
			// Solo Giudizio Abbreviato
			CircostanzaModel lCircostanzaModel = new CircostanzaModel();
			lCircostanzaModel.setCodTipoCircostanza("-");
			lCircostanzaModel.setCodBilanciamentoCircostanze("-");
			lCircostanzaModel.setCodFonte("-");
			lCircostanzaModel.setCodSottonumerazione("-");
			lCircostanzaModel.setCommaQualificante("-");
			lCircostanzaModel.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
			lCircostanzaModel.setDataInserimento(DateUtils.getSysDate());
			lCircostanzaModel.setCodUfficioInserimento(mCodUfficio);

			lCircostanzaModel.setCodFonte("25");
			lCircostanzaModel.setArticolo("442");
			lCircostanzaModel.setFlagGiudizioAbbreviato("S");
			lCircostanzaModel.setFlagSentenzaApplicazPena("N");

			lCircostanzeVector.add(lCircostanzaModel);
		} else if (lFlagSentenzaApplicazionePena.equals("S")) {
			// Sentenza Applicazione Pena
			CircostanzaModel lCircostanzaModel = new CircostanzaModel();

			lCircostanzaModel.setCodTipoCircostanza("-");
			lCircostanzaModel.setCodBilanciamentoCircostanze("-");
			lCircostanzaModel.setCodFonte("-");
			lCircostanzaModel.setCodSottonumerazione("-");
			lCircostanzaModel.setCommaQualificante("-");
			lCircostanzaModel.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
			lCircostanzaModel.setDataInserimento(DateUtils.getSysDate());
			lCircostanzaModel.setCodUfficioInserimento(mCodUfficio);

			lCircostanzaModel.setCodFonte("25");
			lCircostanzaModel.setArticolo("444");
			lCircostanzaModel.setFlagGiudizioAbbreviato("N");
			lCircostanzaModel.setFlagSentenzaApplicazPena("S");

			lCircostanzeVector.add(lCircostanzaModel);

		}

		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getArrayPenaComplessiva() != null) {
			// Ciclo Prima su ARRAY PENA COMPLESSIVA - DISPISITIVO
			for (int j = 0; j <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
					.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray().length - 1; j++) {
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
						.getArrayCircostanze() != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Lunghezza Array Circostanze :"
							+ adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
									.getDISPOSITIVO().getArrayCircostanze().getCIRCOSTANZAArray().length);
					// Ciclo su Array Circostanze
					for (int i = 0; i <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
							.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
							.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().getArrayCircostanze()
							.getCIRCOSTANZAArray().length - 1; i++) {
						CircostanzaModel lCircostanzaModel = new CircostanzaModel();

						// DECODIFICA COD_BILANCIAMENTO_CIRCOSTANZE - (COD_TIPO_CIRCOSTANZA ??)
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayCircostanze().getCIRCOSTANZAArray(i)
								.getFLAGCIRCGENERICHE() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("BILANCIAMENTO_CIRCOSTANZE",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getArrayCircostanze().getCIRCOSTANZAArray(i)
											.getFLAGCIRCGENERICHE());
							lCodBilanciamentoCircostanze = lCodiciSIESNSCModel.getCoSies().trim();
						} else {
							lCodBilanciamentoCircostanze = "-";
						}
						lCircostanzaModel.setCodBilanciamentoCircostanze(lCodBilanciamentoCircostanze);
						lCircostanzaModel.setCodTipoCircostanza("-");

						// DECODIFICA COD_FONTE con CODI_TL
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayCircostanze().getCIRCOSTANZAArray(i)
								.getCODITL() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("FONTE",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getArrayCircostanze().getCIRCOSTANZAArray(i).getCODITL());
							lCodFonte = lCodiciSIESNSCModel.getCoSies().trim();
						} else {
							lCodFonte = "-";
						}
						lCircostanzaModel.setCodFonte(lCodFonte);

						// ANNO_FONTE
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayCircostanze().getCIRCOSTANZAArray(i)
								.getANNOLS() != 0) {
							lCircostanzaModel.setAnnoFonte(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayCircostanze().getCIRCOSTANZAArray(i).getANNOLS()));
						} else {
							lCircostanzaModel.setAnnoFonte(null);
						}

						// NUMERO_FONTE
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayCircostanze().getCIRCOSTANZAArray(i)
								.getNUMELS() != null) {
							lCircostanzaModel.setNumeroFonte(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayCircostanze().getCIRCOSTANZAArray(i).getNUMELS());
						} else {
							lCircostanzaModel.setNumeroFonte(null);
						}

						// DECODIFICA COD_SOTTONUMERAZIONE
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayCircostanze().getCIRCOSTANZAArray(i)
								.getARTIBTQ() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("SOTTONUMERAZIONE",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getArrayCircostanze().getCIRCOSTANZAArray(i).getARTIBTQ());
							lCodSottoNumerazione = lCodiciSIESNSCModel.getCoSies().trim();
						} else {
							lCodSottoNumerazione = "-";
						}
						lCircostanzaModel.setCodSottonumerazione(lCodSottoNumerazione);

						// COMMA
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayCircostanze().getCIRCOSTANZAArray(i)
								.getARTICOMMA() != null) {
							lCircostanzaModel.setComma(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
									.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayCircostanze().getCIRCOSTANZAArray(i).getARTICOMMA());
						} else {
							lCircostanzaModel.setComma(null);
						}

						// [MEV REL. 5.0] - Gestione Comma qualificante
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayCircostanze().getCIRCOSTANZAArray(i)
								.getARTICOMMABTQ() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("SOTTONUMERAZIONE",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getArrayCircostanze().getCIRCOSTANZAArray(i).getARTICOMMABTQ());
							lCommaQualificante = lCodiciSIESNSCModel.getCoSies().trim();
						} else {
							lCommaQualificante = "-";
						}
						lCircostanzaModel.setCommaQualificante(lCommaQualificante);

						// LETTERA
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayCircostanze().getCIRCOSTANZAArray(i)
								.getARTILETTERA() != null) {
							lCircostanzaModel.setLettera(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayCircostanze().getCIRCOSTANZAArray(i).getARTILETTERA());
						} else {
							lCircostanzaModel.setLettera(null);
						}

						// NUMERO
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayCircostanze().getCIRCOSTANZAArray(i)
								.getARTINUMEARTICOLO() != null) {
							lCircostanzaModel.setNumero(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
									.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayCircostanze().getCIRCOSTANZAArray(i).getARTINUMEARTICOLO());
						} else {
							lCircostanzaModel.setNumero(null);
						}

						// ARTICOLO
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayCircostanze().getCIRCOSTANZAArray(i)
								.getARTILS() != null) {
							lCircostanzaModel.setArticolo(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayCircostanze().getCIRCOSTANZAArray(i).getARTILS());
						} else {
							lCircostanzaModel.setArticolo(null);
						}

						// NOTE
						lCircostanzaModel.setNote(null);

						lCircostanzaModel
								.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
						lCircostanzaModel.setDataInserimento(DateUtils.getSysDate());
						lCircostanzaModel.setCodUfficioInserimento(mCodUfficio);
						lCircostanzaModel.setCodOperatoreAggiornamento(null);
						lCircostanzaModel.setDataAggiornamento(null);
						lCircostanzaModel.setCodUfficioAggiornamento(null);

						if (lFlagSentenzaApplicazionePena.equals("S")) {
							lCircostanzaModel.setFlagSentenzaApplicazPena("S");
						} else {
							lCircostanzaModel.setFlagSentenzaApplicazPena("N");
						}

						if (lFlagGiudizioAbbreviato.equals("S")) {
							lCircostanzaModel.setFlagGiudizioAbbreviato("S");
						} else {
							lCircostanzaModel.setFlagGiudizioAbbreviato("N");
						}

						// lCircostanzaModel.setCodBilanciamentoCircostanze("-");
						lCircostanzaModel.setNoteBilanciamento(null);

						lCircostanzeVector.add(lCircostanzaModel);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("AnnoFonte:" + lCircostanzaModel.getAnnoFonte());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("NumeroFonte:" + lCircostanzaModel.getNumeroFonte());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Comma:" + lCircostanzaModel.getComma());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Lettera:" + lCircostanzaModel.getLettera());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Articolo:" + lCircostanzaModel.getArticolo());
					}
				}
			}
		}

		return lCircostanzeVector;
	}

}