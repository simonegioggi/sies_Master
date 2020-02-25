package siap.sico.webservice.action;

import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.DATIUTENTEDocument;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.siep.beneficio.model.BeneficioModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActNscToSiesLoadRevoche extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String mCodUfficio = "";

	public ActNscToSiesLoadRevoche(String aCodUfficio) {
		mCodUfficio = aCodUfficio;
	}

	public Vector processRequest(ANAGRAFICADocument.ANAGRAFICA adatiAnagrafica,
			DATIUTENTEDocument.DATIUTENTE adatiUtente) throws Exception {

		String lAnnoAppo, lMeseAppo, lGiornoAppo;
		Integer lAnno, lMese, lGiorno;
		String lCodTipoBeneficio = "-"/* , lCodiceSottoTipoBeneficio = "-" */;

		Vector lRevocheVector = new Vector();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("ActNscToLoadRevoche  - TITOLO ESECUTIVO");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");

		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getArrayRevoche() != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Lunghezza Array Revoche: " + adatiAnagrafica.getPROCEDIMENTO()
					.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayRevoche().getREVOCAArray().length);

			for (int i = 0; i <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
					.getArrayRevoche().getREVOCAArray().length - 1; i++) {
				BeneficioModel lBeneficioModel = new BeneficioModel();

				// DECODIFICA CodTipoBeneficio - CodNaturaBeneficio - CodTipoSospSubordinata - CodDpr
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayRevoche().getREVOCAArray(i).getCODITIPOPC() != null) {
					CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_REVOCA",
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayRevoche().getREVOCAArray(i).getCODITIPOPC());
					lCodTipoBeneficio = lCodiciSIESNSCModel.getCoSies().trim();
				}

				lBeneficioModel.setCodTipoSospSubordinata("-");
				lBeneficioModel.setCodTipoBeneficio(lCodTipoBeneficio);
				lBeneficioModel.setCodSottotipoBeneficio("-");
				lBeneficioModel.setCodNaturaBeneficio("R");

				lBeneficioModel.setCodDpr("-");

				/*
				 * NON GESTITO DA NSC a livello di Benefici e Revoche if
				 * (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().
				 * getArrayRevoche().getREVOCAArray(i).getCODITL() != null &&
				 * !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().
				 * getArrayRevoche().getREVOCAArray(i).getCODITL().equals("")) { CodiciSiesNscModel
				 * lCodiciSIESNSCModel =
				 * Decodifica("DPR",adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().
				 * getDATIPROVVEDIMENTO().getArrayRevoche().getREVOCAArray(i).getCODITL());
				 * lBeneficioModel.setCodDpr(lCodiciSIESNSCModel.getCoSies()); } else {
				 * lBeneficioModel.setCodDpr("-"); }
				 */

				/*---------------------------------------------------------------------------------------------------------------------*/
				/*---> I dati che seguono sono i dati della Sentenza che ha generato il BENEFICIO che l'attuale Sentenza sta REVOCANDO */
				/*---------------------------------------------------------------------------------------------------------------------*/

				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayRevoche().getREVOCAArray(i).getDATAPROVVEDIMENTOREVOCA() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayRevoche().getREVOCAArray(i).getDATAPROVVEDIMENTOREVOCA()
								.getANNO() != null
						&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayRevoche().getREVOCAArray(i).getDATAPROVVEDIMENTOREVOCA().getANNO()
								.equals("")) {
					lAnnoAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayRevoche().getREVOCAArray(i).getDATAPROVVEDIMENTOREVOCA().getANNO();
				} else {
					lAnnoAppo = null;
				}

				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayRevoche().getREVOCAArray(i).getDATAPROVVEDIMENTOREVOCA() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayRevoche().getREVOCAArray(i).getDATAPROVVEDIMENTOREVOCA()
								.getMESE() != null
						&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayRevoche().getREVOCAArray(i).getDATAPROVVEDIMENTOREVOCA().getMESE()
								.equals("")) {
					lMeseAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayRevoche().getREVOCAArray(i).getDATAPROVVEDIMENTOREVOCA().getMESE();
				} else {
					lMeseAppo = null;
				}

				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayRevoche().getREVOCAArray(i).getDATAPROVVEDIMENTOREVOCA() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayRevoche().getREVOCAArray(i).getDATAPROVVEDIMENTOREVOCA()
								.getGIORNO() != null
						&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayRevoche().getREVOCAArray(i).getDATAPROVVEDIMENTOREVOCA().getGIORNO()
								.equals("")) {
					lGiornoAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
							.getDATIPROVVEDIMENTO().getArrayRevoche().getREVOCAArray(i)
							.getDATAPROVVEDIMENTOREVOCA().getGIORNO();
				} else {
					lGiornoAppo = null;
				}

				Date lDataProvvedimento;
				if ((lAnnoAppo != null && !lAnnoAppo.equals(""))
						&& (lMeseAppo != null && !lMeseAppo.equals(""))
						&& (lGiornoAppo != null && !lGiornoAppo.equals(""))) {
					lAnno = new Integer(lAnnoAppo);
					lMese = new Integer(lMeseAppo);
					lGiorno = new Integer(lGiornoAppo);
					lDataProvvedimento = DateUtils.getDate(lAnno.intValue(), lMese.intValue(),
							lGiorno.intValue());
				} else {
					lDataProvvedimento = null;
				}
				lBeneficioModel.setRifDataProvvedimento(lDataProvvedimento);

				// DECODIFICA COD_TIPO_AUTORITA_EMITTENTE

				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayRevoche().getREVOCAArray(i).getAUTORITAGIUDIZIARIA() != null) {
					CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_UFFICIO_EMITTENTE",
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayRevoche().getREVOCAArray(i).getAUTORITAGIUDIZIARIA());
					lBeneficioModel.setRifCodTipoAutoEmittente(lCodiciSIESNSCModel.getCoSies().trim()); // CodTipoAutoritaEmittente(lCodiciSIESNSCModel.getCoSies().trim());
				} else {
					lBeneficioModel.setRifCodTipoAutoEmittente("-");
				}

				// ----------------------------------- GESTIONE SEDE PRINCIPALE o DISTACCATA
				// -------------------------
				String lCodLuogoProvvRif = "-";
				String lCodLuogoProvvRifDist = "-";
				String lDesLuogoEmittenteDistRif = "";
				// DECODIFICA COD_LUOGO_EMITTENTE
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayRevoche().getREVOCAArray(i).getCODISEDEAUTORITAPRINDIST() != null
						&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayRevoche().getREVOCAArray(i).getCODISEDEAUTORITAPRINDIST()
								.equals("")) {
					CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("COMUNE",
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayRevoche().getREVOCAArray(i).getCODISEDEAUTORITAPRINDIST());
					lCodLuogoProvvRifDist = lCodiciSIESNSCModel.getCoSies().trim();
					lDesLuogoEmittenteDistRif = lCodiciSIESNSCModel.getCoSiesDes().trim();
				}

				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayRevoche().getREVOCAArray(i).getCODISEDEAUTORITAPRIN() != null
						&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayRevoche().getREVOCAArray(i).getCODISEDEAUTORITAPRIN().equals("")) {
					CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("COMUNE",
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayRevoche().getREVOCAArray(i).getCODISEDEAUTORITAPRIN());
					lCodLuogoProvvRif = lCodiciSIESNSCModel.getCoSies().trim();
				}

				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayRevoche().getREVOCAArray(i).getCODISEDEAUTORITAPRIN() != null
						&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayRevoche().getREVOCAArray(i).getCODISEDEAUTORITAPRIN().equals("")) {
					lBeneficioModel.setRifCodLuogoAutoEmittente(lCodLuogoProvvRif);
					String lNumSezioneAutoritaProvvRif = "Distaccata" + " " + lDesLuogoEmittenteDistRif;
					if (lNumSezioneAutoritaProvvRif.length() > 100) {
						lBeneficioModel
								.setRifNumSezioneAutoEmittente(lNumSezioneAutoritaProvvRif.substring(0, 100));
					} else {
						lBeneficioModel.setRifNumSezioneAutoEmittente(lNumSezioneAutoritaProvvRif);
					}
				} else {
					lBeneficioModel.setRifCodLuogoAutoEmittente(lCodLuogoProvvRifDist);
					// In questo caso non c'è la Sede DISTACCATA
				}
				// ------------------------------- FINE GESTIONE SEDE PRINCIPALE o DISTACCATA
				// -------------------------

				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayRevoche().getREVOCAArray(i).getANNOSENTENZA() != 0) {
					lBeneficioModel.setRifAnnoProvvedimento(new BigDecimal(
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayRevoche().getREVOCAArray(i).getANNOSENTENZA()));
				}

				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayRevoche().getREVOCAArray(i).getNUMEROSENTENZA() != 0)

				{
					lBeneficioModel.setRifNumeroProvvedimento(String.valueOf(
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayRevoche().getREVOCAArray(i).getNUMEROSENTENZA()));
				}

				lBeneficioModel.setRifCodTipoProvvedimento("-");
				// lBeneficioModel.setRifCodTipoAutoEmittente("-");
				// lBeneficioModel.setRifCodTipoAutoEmittente("-");
				lBeneficioModel.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
				lBeneficioModel.setDataInserimento(DateUtils.getSysDate());
				lBeneficioModel.setCodUfficioInserimento(mCodUfficio);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Cod Revoca:" + lBeneficioModel.getCodTipoBeneficio());

				lRevocheVector.add(lBeneficioModel);
			}
		}

		return lRevocheVector;
	}

}