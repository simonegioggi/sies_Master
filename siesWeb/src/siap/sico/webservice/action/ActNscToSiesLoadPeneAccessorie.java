package siap.sico.webservice.action;

import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.DATIUTENTEDocument;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActNscToSiesLoadPeneAccessorie extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String mCodUfficio = "";

	public ActNscToSiesLoadPeneAccessorie(String aCodUfficio) {
		mCodUfficio = aCodUfficio;
	}

	public Vector processRequest(ANAGRAFICADocument.ANAGRAFICA adatiAnagrafica,
			DATIUTENTEDocument.DATIUTENTE adatiUtente) throws Exception {

		String lAnnoAppo, lMeseAppo, lGiornoAppo;
		Integer lAnno, lMese, lGiorno;
		String lCodiTipoPenaAccessoria = "-"/* , lDurata = "-" */;

		Vector lPeneAccessorieVector = new Vector();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("ActNscToLoadPeneAccessorie - TITOLO ESECUTIVO");
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
						.getArrayPeneAccessorie() != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Lunghezza Array Pene Accessorie :" + adatiAnagrafica.getPROCEDIMENTO()
							.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
							.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().getArrayPeneAccessorie()
							.getPENAACCESSORIAArray().length);
					// Ciclo su Array Pene Accessorie
					for (int i = 0; i <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
							.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
							.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().getArrayPeneAccessorie()
							.getPENAACCESSORIAArray().length - 1; i++) {
						PenaAccessoriaModel lPenaAccessoriaModel = new PenaAccessoriaModel();

						// CODICE TIPO PENA ACCESSORIA
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
								.getCODITIPOPENAACCESSORIA() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_PENA_ACCESSORIA",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
											.getCODITIPOPENAACCESSORIA());
							lCodiTipoPenaAccessoria = lCodiciSIESNSCModel.getCoSies().trim();
							/*
							 * if (lCodiciSIESNSCModel.getCoVal2() != null) {
							 * lDurata=lCodiciSIESNSCModel.getCoVal2().trim(); }
							 */
						} else {
							lCodiTipoPenaAccessoria = "-";
							// lDurata = "-";
						}

						lPenaAccessoriaModel.setCodTipoPenaAccessoria(lCodiTipoPenaAccessoria);

						/*
						 * if (!lDurata.equals("-")) { if (lDurata.equals("T")) {
						 * lPenaAccessoriaModel.setDurata("D"); } else {
						 * lPenaAccessoriaModel.setDurata(lDurata); } } else { if
						 * (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().
						 * getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().
						 * getArrayPeneAccessorie().getPENAACCESSORIAArray(i).getFLAGDURATAPENAACCESSORIA()
						 * !=null) { if
						 * (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().
						 * getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().
						 * getArrayPeneAccessorie().getPENAACCESSORIAArray(i).getFLAGDURATAPENAACCESSORIA().
						 * equals("T")) { lPenaAccessoriaModel.setDurata("D"); } else {
						 * lPenaAccessoriaModel.setDurata(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO
						 * ().getDATIPROVVEDIMENTO().getArrayPenaComplessiva().
						 * getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().getArrayPeneAccessorie().
						 * getPENAACCESSORIAArray(i).getFLAGDURATAPENAACCESSORIA()); } } }
						 */

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
								.getDURATAPENAACCESSORIA() != null
								&& (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
										.getDURATAPENAACCESSORIA().getANNIDURATA() != 0
										|| adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
												.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
												.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
												.getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
												.getDURATAPENAACCESSORIA().getMESIDURATA() != 0
										|| adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
												.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
												.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
												.getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
												.getDURATAPENAACCESSORIA().getGIORNIDURATA() != 0)) {
							lPenaAccessoriaModel.setDurata("-"); // Per SIES significa Temporanea
						} else {
							// Se la durata non c'è, impostiamo come "Flag Tipo Durata" quello passato da Eng.
							if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
									.getDISPOSITIVO().getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
									.getFLAGDURATAPENAACCESSORIA().equals("T")) {
								lPenaAccessoriaModel.setDurata("D"); // Per l'intera Durata dellas Pena
							} else {
								// Perpetua
								lPenaAccessoriaModel.setDurata(adatiAnagrafica.getPROCEDIMENTO()
										.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
										.getFLAGDURATAPENAACCESSORIA());
							}
						}

						// NUM_ANNI
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
								.getDURATAPENAACCESSORIA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
										.getDURATAPENAACCESSORIA().getANNIDURATA() != 0) {
							lPenaAccessoriaModel.setNumAnni(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
									.getDURATAPENAACCESSORIA().getANNIDURATA()));
						} else {
							lPenaAccessoriaModel.setNumAnni(null);
						}
						// NUM_MESI
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
								.getDURATAPENAACCESSORIA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
										.getDURATAPENAACCESSORIA().getMESIDURATA() != 0) {
							lPenaAccessoriaModel.setNumMesi(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
									.getDURATAPENAACCESSORIA().getMESIDURATA()));
						} else {
							lPenaAccessoriaModel.setNumMesi(null);
						}
						// NUM_GIORNI
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
								.getDURATAPENAACCESSORIA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
										.getDURATAPENAACCESSORIA().getGIORNIDURATA() != 0) {
							lPenaAccessoriaModel.setNumGiorni(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
									.getDURATAPENAACCESSORIA().getGIORNIDURATA()));
						} else {
							lPenaAccessoriaModel.setNumGiorni(null);
						}

						// DATA SCADENZA
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
								.getDATASCADENZA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAccessorie().getPENAACCESSORIAArray(i).getDATASCADENZA()
										.getANNO() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAccessorie().getPENAACCESSORIAArray(i).getDATASCADENZA()
										.getANNO().equals("")) {
							lAnnoAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
									.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayPeneAccessorie().getPENAACCESSORIAArray(i).getDATASCADENZA()
									.getANNO();
						} else {
							lAnnoAppo = null;
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
								.getDATASCADENZA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAccessorie().getPENAACCESSORIAArray(i).getDATASCADENZA()
										.getMESE() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAccessorie().getPENAACCESSORIAArray(i).getDATASCADENZA()
										.getMESE().equals("")) {
							lMeseAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
									.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayPeneAccessorie().getPENAACCESSORIAArray(i).getDATASCADENZA()
									.getMESE();
						} else {
							lMeseAppo = null;
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayPeneAccessorie().getPENAACCESSORIAArray(i)
								.getDATASCADENZA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAccessorie().getPENAACCESSORIAArray(i).getDATASCADENZA()
										.getGIORNO() != null
								&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayPeneAccessorie().getPENAACCESSORIAArray(i).getDATASCADENZA()
										.getGIORNO().equals("")) {
							lGiornoAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
									.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getArrayPeneAccessorie().getPENAACCESSORIAArray(i).getDATASCADENZA()
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
						lPenaAccessoriaModel.setDataFineValidita(lDataAppo);

						lPenaAccessoriaModel.setFlagDichiarazioneFalsita("N");

						lPenaAccessoriaModel
								.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
						lPenaAccessoriaModel.setDataInserimento(DateUtils.getSysDate());
						lPenaAccessoriaModel.setCodUfficioInserimento(mCodUfficio);
						lPenaAccessoriaModel.setCodOperatoreAggiornamento(null);
						lPenaAccessoriaModel.setDataAggiornamento(null);
						lPenaAccessoriaModel.setCodUfficioAggiornamento(null);

						// Pate Revoche in fase di Sviluppo in SIEP - Verificare
						lPenaAccessoriaModel.setFlagRevocaCondono("N");
						lPenaAccessoriaModel.setCodTipoUfficioSentenzaRevo("-");
						lPenaAccessoriaModel.setCodLuogoSentenzaRevoca("-");
						/*
						 * lPenaAccessoriaModel.setCodNuovoTipoPenaAccessoria("-");
						 * lPenaAccessoriaModel.setCodTipoUfficioOrdinanzaGE("-");
						 * lPenaAccessoriaModel.setCodLuogoUfficioOrdinanzaGE("-");
						 * lPenaAccessoriaModel.setCodFonteGE("-");
						 * lPenaAccessoriaModel.setCodSottonumerazioneGE("-");
						 * lPenaAccessoriaModel.setCodTipoUfficioOrdinanzaPA("-");
						 * lPenaAccessoriaModel.setCodLuogoUfficioOrdinanzaPA("-");
						 */

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info(
								"CodTipoPenaAccessoria: " + lPenaAccessoriaModel.getCodTipoPenaAccessoria());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Flag Durata: " + lPenaAccessoriaModel.getDurata());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Pena Accessoria Anni-Mesi_Giorni: "
								+ lPenaAccessoriaModel.getNumAnni() + "-" + lPenaAccessoriaModel.getNumMesi()
								+ "-" + lPenaAccessoriaModel.getNumGiorni());

						lPeneAccessorieVector.add(lPenaAccessoriaModel);
					}
				}
			}
		}
		return lPeneAccessorieVector;
	}

}