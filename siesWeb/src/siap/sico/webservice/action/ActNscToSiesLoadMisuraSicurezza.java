package siap.sico.webservice.action;

import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.DATIUTENTEDocument;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActNscToSiesLoadMisuraSicurezza extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String mCodUfficio = "";

	public ActNscToSiesLoadMisuraSicurezza(String aCodUfficio) {
		mCodUfficio = aCodUfficio;
	}

	public Vector processRequest(ANAGRAFICADocument.ANAGRAFICA adatiAnagrafica,
			DATIUTENTEDocument.DATIUTENTE adatiUtente) throws Exception {

		Vector lMisuraSicurezzaVector = new Vector();

		String lCodTipo = "-";
		// String lCodNatura = "-";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("ActNscToLoadMisuraSicurezza - TITOLO ESECUTIVO");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");

		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getArrayPenaComplessiva() != null) {
			// Ciclo Prima su ARRAY PENA COMPLESSIVA - DISPISITIVO
			for (int j = 0; j <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
					.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray().length - 1; j++) {
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
						.getArrayMisureSicurezza() != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Lunghezza Array Misura Sicurezza :" + adatiAnagrafica.getPROCEDIMENTO()
							.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
							.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().getArrayMisureSicurezza()
							.getMISURADISICUREZZAArray().length);

					// Ciclo su Array Misura Sicurezza
					for (int i = 0; i <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
							.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
							.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().getArrayMisureSicurezza()
							.getMISURADISICUREZZAArray().length - 1; i++) {
						MisuraSicurezzaModel lMisuraSicurezzaModel = new MisuraSicurezzaModel();

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayMisureSicurezza().getMISURADISICUREZZAArray(i)
								.getCODITIPOMS() != null) {
							// DECODIFICA COD_TIPO e COD_NATURA [01=Detentiva 02=Non Detentiva 03=Patrimoniale
							// ]
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_MISURA_SICUREZZA",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getArrayMisureSicurezza().getMISURADISICUREZZAArray(i)
											.getCODITIPOMS());
							lCodTipo = lCodiciSIESNSCModel.getCoSies().trim();
							/*
							 * if (lCodiciSIESNSCModel.getCoVal1() != null) {
							 * lCodNatura=lCodiciSIESNSCModel.getCoVal1().trim(); }
							 */
						}
						lMisuraSicurezzaModel.setCodTipo(lCodTipo);
						lMisuraSicurezzaModel.setCodNatura("-");

						// NUM_ANNI
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayMisureSicurezza().getMISURADISICUREZZAArray(i)
								.getDURATAMISURA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayMisureSicurezza().getMISURADISICUREZZAArray(i)
										.getDURATAMISURA().getANNIDURATA() != 0) {
							lMisuraSicurezzaModel.setNumAnni(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayMisureSicurezza().getMISURADISICUREZZAArray(i).getDURATAMISURA()
									.getANNIDURATA()));
						} else {
							lMisuraSicurezzaModel.setNumAnni(null);
						}
						// NUM_MESI
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayMisureSicurezza().getMISURADISICUREZZAArray(i)
								.getDURATAMISURA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayMisureSicurezza().getMISURADISICUREZZAArray(i)
										.getDURATAMISURA().getMESIDURATA() != 0) {
							lMisuraSicurezzaModel.setNumMesi(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayMisureSicurezza().getMISURADISICUREZZAArray(i).getDURATAMISURA()
									.getMESIDURATA()));
						} else {
							lMisuraSicurezzaModel.setNumMesi(null);
						}
						// NUM_GIORNI
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayMisureSicurezza().getMISURADISICUREZZAArray(i)
								.getDURATAMISURA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayMisureSicurezza().getMISURADISICUREZZAArray(i)
										.getDURATAMISURA().getGIORNIDURATA() != 0) {
							lMisuraSicurezzaModel.setNumGiorni(new BigDecimal(adatiAnagrafica
									.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
									.getDISPOSITIVO().getArrayMisureSicurezza().getMISURADISICUREZZAArray(i)
									.getDURATAMISURA().getGIORNIDURATA()));
						} else {
							lMisuraSicurezzaModel.setNumGiorni(null);
						}
						lMisuraSicurezzaModel.setAnnoReg38(null);
						lMisuraSicurezzaModel.setNumReg38(null);

						lMisuraSicurezzaModel
								.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
						lMisuraSicurezzaModel.setDataInserimento(DateUtils.getSysDate());
						lMisuraSicurezzaModel.setCodUfficioInserimento(mCodUfficio);
						lMisuraSicurezzaModel.setCodOperatoreAggiornamento(null);
						lMisuraSicurezzaModel.setDataAggiornamento(null);
						lMisuraSicurezzaModel.setCodUfficioAggiornamento(null);

						lMisuraSicurezzaModel.setEveIdEvento(null);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Cod_Tipo:" + lMisuraSicurezzaModel.getCodTipo());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Cod_Natura:" + lMisuraSicurezzaModel.getCodNatura());

						lMisuraSicurezzaVector.add(lMisuraSicurezzaModel);
					}
				}
			}
		}
		return lMisuraSicurezzaVector;
	}

}