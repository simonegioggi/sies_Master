package siap.sico.webservice.action;

import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.DATIUTENTEDocument;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.siep.continuazione.model.ContinuazioneModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActNscToSiesLoadContinuazione extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String mCodUfficio = "";

	public ActNscToSiesLoadContinuazione(String aCodUfficio) {
		mCodUfficio = aCodUfficio;
	}

	public Vector processRequest(ANAGRAFICADocument.ANAGRAFICA adatiAnagrafica,
			DATIUTENTEDocument.DATIUTENTE adatiUtente) throws Exception {

		String lAnnoAppo, lMeseAppo, lGiornoAppo;
		Integer lAnno, lMese, lGiorno;
		String lCodTipoContinuazione = "-";
		String lCodTipoAutorita = "-";
		String lCodLuogoAutorita = "-";

		Vector lContinuazioneVector = new Vector();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("ActNscToLoadContinuazione - TITOLO ESECUTIVO");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");

		int contaProg = 0;
		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getArrayPenaComplessiva() != null) {
			// Ciclo Prima su ARRAY PENA COMPLESSIVA - DISPISITIVO
			for (int j = 0; j <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
					.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray().length - 1; j++) {
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
						.getArrayPeneAggiunte() != null) {
					// Ciclo su Array Pene Aggiuntive
					for (int i = 0; i <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
							.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
							.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().getArrayPeneAggiunte()
							.getPENAAGGIUNTAArray().length - 1; i++) {
						ContinuazioneModel lContinuazioneModel = new ContinuazioneModel();
						// DECODIFICA CODI_TIPO_CONTINUAZIONE DECODIFICA
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
								.getCODITIPOPENAAGGIUNTA() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_CONTINUAZIONE",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
											.getCODITIPOPENAAGGIUNTA());
							lCodTipoContinuazione = lCodiciSIESNSCModel.getCoSies().trim();
						} else {
							lCodTipoContinuazione = "-";
						}
						lContinuazioneModel.setCodTipoContinuazione(lCodTipoContinuazione);

						contaProg++;

						// PROG_CONTINUAZIONE
						lContinuazioneModel.setProgrContinuazione(new BigDecimal(contaProg));

						// DECODIFICA - CODI_TIPO_AUTORITA
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
								.getAUTORITAGIUDIZIARIAPA() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_UFFICIO_EMITTENTE",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
											.getAUTORITAGIUDIZIARIAPA());
							lCodTipoAutorita = lCodiciSIESNSCModel.getCoSies().trim();
						} else {
							lCodTipoAutorita = "-";
						}
						lContinuazioneModel.setCodTipoAutorita(lCodTipoAutorita);

						// DECODIFICA COD_LUOGO_AUTORITA
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
								.getCODISEDEAUTORITAPRINDIST() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("COMUNE",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
											.getCODISEDEAUTORITAPRINDIST());
							lCodLuogoAutorita = lCodiciSIESNSCModel.getCoSies().trim();
						} else {
							lCodLuogoAutorita = "-";
						}
						lContinuazioneModel.setCodLuogoAutorita(lCodLuogoAutorita);

						// DATA_SENTENZA
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
								.getDATAPROVVEDIMENTOPA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
										.getDATAPROVVEDIMENTOPA().getANNO() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
										.getDATAPROVVEDIMENTOPA().getANNO().equals("")) {
							lAnnoAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
									.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayPeneAggiunte().getPENAAGGIUNTAArray(i).getDATAPROVVEDIMENTOPA()
									.getANNO();
						} else {
							lAnnoAppo = null;
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
								.getDATAPROVVEDIMENTOPA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
										.getDATAPROVVEDIMENTOPA().getMESE() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
										.getDATAPROVVEDIMENTOPA().getMESE().equals("")) {
							lMeseAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
									.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayPeneAggiunte().getPENAAGGIUNTAArray(i).getDATAPROVVEDIMENTOPA()
									.getMESE();
						} else {
							lMeseAppo = null;
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
								.getDATAPROVVEDIMENTOPA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
										.getDATAPROVVEDIMENTOPA().getGIORNO() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
										.getDATAPROVVEDIMENTOPA().getGIORNO().equals("")) {
							lGiornoAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
									.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayPeneAggiunte().getPENAAGGIUNTAArray(i).getDATAPROVVEDIMENTOPA()
									.getGIORNO();
						} else {
							lGiornoAppo = null;
						}

						Date lDataAppo;
						if ((lAnnoAppo != null && !lAnnoAppo.equals(""))
								&& (lMeseAppo != null && !lMeseAppo.equals(""))
								&& (lGiornoAppo != null && !lGiornoAppo.equals(""))) {
							lAnno = new Integer(lAnnoAppo);
							lMese = new Integer(lMeseAppo);
							lGiorno = new Integer(lGiornoAppo);
							lDataAppo = DateUtils.getDate(lAnno.intValue(), lMese.intValue(),
									lGiorno.intValue());
						} else {
							lDataAppo = null;
						}
						lContinuazioneModel.setDataSentenza(lDataAppo);

						// ANNO SENTENZA
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
								.getANNOSENTENZA() != null) {
							lContinuazioneModel.setAnnoSentenza(new BigDecimal(adatiAnagrafica
									.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
									.getDISPOSITIVO().getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
									.getANNOSENTENZA()));
						} else {
							lContinuazioneModel.setAnnoSentenza(null);
						}

						// NMERO SENTENZA - CAST da Int a String
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
								.getNUMEROSENTENZA() != 0) {
							lContinuazioneModel.setNumSentenza(String.valueOf(adatiAnagrafica
									.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
									.getDISPOSITIVO().getArrayPeneAggiunte().getPENAAGGIUNTAArray(i)
									.getNUMEROSENTENZA()));
						} else {
							lContinuazioneModel.setNumSentenza(null);
						}

						lContinuazioneModel
								.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
						lContinuazioneModel.setDataInserimento(DateUtils.getSysDate());
						lContinuazioneModel.setCodUfficioInserimento(mCodUfficio);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info(
								"Cod_Tipo_Continuazione:" + lContinuazioneModel.getCodTipoContinuazione());

						lContinuazioneVector.add(lContinuazioneModel);

					}
				}
			}
		}
		return lContinuazioneVector;
	}

}