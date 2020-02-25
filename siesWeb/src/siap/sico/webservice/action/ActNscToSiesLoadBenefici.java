package siap.sico.webservice.action;

import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.DATIUTENTEDocument;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.sico.util.CalendarUtil;
import siap.siep.beneficio.model.BeneficioModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActNscToSiesLoadBenefici extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String mCodUfficio = "";

	public ActNscToSiesLoadBenefici(String aCodUfficio) {
		mCodUfficio = aCodUfficio;
	}

	public Vector processRequest(ANAGRAFICADocument.ANAGRAFICA adatiAnagrafica,
			DATIUTENTEDocument.DATIUTENTE adatiUtente) throws Exception {

		Vector lBeneficiVector = new Vector();

		String lCodiceTipoBeneficio = "-", lCodiceSottoTipoBeneficio = "-", lCodiceDPR = "-";
		String lCodiceNaturaBeneficio = "C";
		// String lAppoTipoBeneficio = "", lAppoDPR = "";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("ActNscToLoadBenefici - TITOLO ESECUTIVO");
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
						.getArrayBenefici() != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Lunghezza Array Benefici :"
							+ adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
									.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray().length);
					// Ciclo su Array Benefici
					for (int i = 0; i <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
							.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
							.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().getArrayBenefici()
							.getBENEFICIOArray().length - 1; i++) {
						BeneficioModel lBeneficioModel = new BeneficioModel();

						// DECODIFICA CodTipoBeneficio - CodNaturaBeneficio - CodTipoSospSubordinata - CodDpr
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
								.getCODITIPOBENEFICIO() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_BENEFICIO",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getArrayBenefici().getBENEFICIOArray(i).getCODITIPOBENEFICIO());
							lCodiceTipoBeneficio = lCodiciSIESNSCModel.getCoSies().trim(); // Codice Tipo
																							// Beneficio
							if (lCodiciSIESNSCModel.getCoVal1() != null
									&& !lCodiciSIESNSCModel.getCoVal1().equals("")) {
								lCodiceSottoTipoBeneficio = lCodiciSIESNSCModel.getCoVal1().trim();
							}
						} else {
							lCodiceTipoBeneficio = "-";
							lCodiceSottoTipoBeneficio = "-";
							lCodiceNaturaBeneficio = "C";
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
								.getCODICEDPR() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("DPR",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getArrayBenefici().getBENEFICIOArray(i).getCODICEDPR());
							// lBeneficioModel.setCodDpr(lCodiciSIESNSCModel.getCoSies().trim());
							lCodiceDPR = lCodiciSIESNSCModel.getCoSies().trim();
						} else {
							// lBeneficioModel.setCodDpr("-");
							lCodiceDPR = "-";
						}

						lBeneficioModel.setCodTipoBeneficio(lCodiceTipoBeneficio);
						lBeneficioModel.setCodDpr(lCodiceDPR);
						lBeneficioModel.setCodTipoSospSubordinata("-");
						lBeneficioModel.setCodSottotipoBeneficio(lCodiceSottoTipoBeneficio);
						lBeneficioModel.setCodNaturaBeneficio(lCodiceNaturaBeneficio);

						// -----------------------------------------------------

						// ANNI RECLUSIONE
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
								.getDURATARECLUSIONE() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayBenefici().getBENEFICIOArray(i).getDURATARECLUSIONE()
										.getANNIDURATA() != 0) {
							lBeneficioModel.setNumAnniReclusione(new BigDecimal(adatiAnagrafica
									.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
									.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
									.getDURATARECLUSIONE().getANNIDURATA()));
						} else {
							lBeneficioModel.setNumAnniReclusione(null);
						}
						// MESI RECLUSIONE
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
								.getDURATARECLUSIONE() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayBenefici().getBENEFICIOArray(i).getDURATARECLUSIONE()
										.getMESIDURATA() != 0) {
							lBeneficioModel.setNumMesiReclusione(new BigDecimal(adatiAnagrafica
									.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
									.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
									.getDURATARECLUSIONE().getMESIDURATA()));
						} else {
							lBeneficioModel.setNumMesiReclusione(null);
						}
						// GIORNI RECLUSIONE
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
								.getDURATARECLUSIONE() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayBenefici().getBENEFICIOArray(i).getDURATARECLUSIONE()
										.getGIORNIDURATA() != 0) {
							lBeneficioModel.setNumGiorniReclusione(new BigDecimal(adatiAnagrafica
									.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
									.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
									.getDURATARECLUSIONE().getGIORNIDURATA()));
						} else {
							lBeneficioModel.setNumGiorniReclusione(null);
						}

						// ANNI ARRESTO
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
								.getDURATAARRESTO() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayBenefici().getBENEFICIOArray(i).getDURATAARRESTO()
										.getANNIDURATA() != 0) {
							lBeneficioModel.setNumAnniArresto(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().getArrayBenefici()
									.getBENEFICIOArray(i).getDURATAARRESTO().getANNIDURATA()));
						} else {
							lBeneficioModel.setNumAnniArresto(null);
						}
						// MESI ARRESTO
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
								.getDURATAARRESTO() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayBenefici().getBENEFICIOArray(i).getDURATAARRESTO()
										.getMESIDURATA() != 0) {
							lBeneficioModel.setNumMesiArresto(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().getArrayBenefici()
									.getBENEFICIOArray(i).getDURATAARRESTO().getMESIDURATA()));
						} else {
							lBeneficioModel.setNumMesiArresto(null);
						}
						// GIORNI ARRESTO
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
								.getDURATAARRESTO() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayBenefici().getBENEFICIOArray(i).getDURATAARRESTO()
										.getGIORNIDURATA() != 0) {
							lBeneficioModel.setNumGiorniArresto(new BigDecimal(adatiAnagrafica
									.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
									.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
									.getDURATAARRESTO().getGIORNIDURATA()));
						} else {
							lBeneficioModel.setNumGiorniArresto(null);
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
								.getIMPOMULTA() != null) {
							if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
									.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
									.getCODIVALUTA() != null
									&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getArrayBenefici().getBENEFICIOArray(i).getCODIVALUTA()
											.equals("ITL")) {
								lBeneficioModel.setImportoMulta(Utils.toEuro(adatiAnagrafica.getPROCEDIMENTO()
										.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayBenefici().getBENEFICIOArray(i).getIMPOMULTA().toString()));
							} else {
								lBeneficioModel.setImportoMulta(adatiAnagrafica.getPROCEDIMENTO()
										.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayBenefici().getBENEFICIOArray(i).getIMPOMULTA());
							}
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
								.getIMPOAMMENDA() != null) {
							if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
									.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
									.getCODIVALUTA() != null
									&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getArrayBenefici().getBENEFICIOArray(i).getCODIVALUTA()
											.equals("ITL")) {
								lBeneficioModel.setImportoAmmenda(Utils.toEuro(adatiAnagrafica
										.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
										.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
										.getDISPOSITIVO().getArrayBenefici().getBENEFICIOArray(i)
										.getIMPOAMMENDA().toString()));
							} else {
								lBeneficioModel.setImportoAmmenda(adatiAnagrafica.getPROCEDIMENTO()
										.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getArrayBenefici().getBENEFICIOArray(i).getIMPOAMMENDA());
							}
						}

						lBeneficioModel.setRifCodTipoProvvedimento("-");
						lBeneficioModel.setRifCodTipoAutoEmittente("-");
						lBeneficioModel.setRifCodTipoAutoEmittente("-");

						lBeneficioModel
								.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
						lBeneficioModel.setDataInserimento(DateUtils.getSysDate());
						lBeneficioModel.setCodUfficioInserimento(mCodUfficio);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("CodTipoBeneficio:" + lBeneficioModel.getCodTipoBeneficio());

						int FlagTrovato = 0;
						for (int x = 0; x <= lBeneficiVector.size() - 1; x++) {
							BeneficioModel lBeneficioModelVector = (BeneficioModel) lBeneficiVector
									.elementAt(x);

							if (lCodiceTipoBeneficio.equals(lBeneficioModelVector.getCodTipoBeneficio())
									&& lCodiceDPR.equals(lBeneficioModelVector.getCodDpr())) {
								FlagTrovato = 1;
								// Somma Anni-Mesi-Giorni Arresto e Importo Ammenda
								CalendarModel lCalendarArrestoNSC = lBeneficioModel.getQuantumArresto();
								CalendarModel lCalendarArrestoVect = lBeneficioModelVector
										.getQuantumArresto();
								CalendarUtil lCalendarUtil = new CalendarUtil();
								CalendarModel lCalendarTotArresto = lCalendarUtil
										.sommaGiornieValute(lCalendarArrestoNSC, lCalendarArrestoVect);

								lBeneficioModelVector
										.setNumAnniArresto(new BigDecimal(lCalendarTotArresto.getNumAnni()));
								lBeneficioModelVector
										.setNumMesiArresto(new BigDecimal(lCalendarTotArresto.getNumMesi()));
								lBeneficioModelVector.setNumGiorniArresto(
										new BigDecimal(lCalendarTotArresto.getNumGiorni()));
								lBeneficioModelVector.setImportoAmmenda(
										new BigDecimal(lCalendarTotArresto.getImportoAmmenda()));

								// Somma Anni-Mesi-Giorni Reclusione e Importo Multa
								CalendarModel lCalendarReclusioneNSC = lBeneficioModel.getQuantumReclusione();
								CalendarModel lCalendarReclusioneVect = lBeneficioModelVector
										.getQuantumReclusione();
								lCalendarUtil = new CalendarUtil();
								CalendarModel lCalendarTotReclusione = lCalendarUtil
										.sommaGiornieValute(lCalendarReclusioneNSC, lCalendarReclusioneVect);

								lBeneficioModelVector.setNumAnniReclusione(
										new BigDecimal(lCalendarTotReclusione.getNumAnni()));
								lBeneficioModelVector.setNumMesiReclusione(
										new BigDecimal(lCalendarTotReclusione.getNumMesi()));
								lBeneficioModelVector.setNumGiorniReclusione(
										new BigDecimal(lCalendarTotReclusione.getNumGiorni()));
								lBeneficioModelVector.setImportoMulta(
										new BigDecimal(lCalendarTotReclusione.getImportoMulta()));

							}
						}

						if (FlagTrovato == 0) {
							lBeneficiVector.add(lBeneficioModel);
						}
					}
				}

				// Caso di Sospensione Condzionale Proveniente da NSC
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
						.getCODIPENASOSPESA() != null) {
					BeneficioModel lBeneficioModel = new BeneficioModel();

					CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_BENEFICIO",
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
									.getDISPOSITIVO().getCODIPENASOSPESA());
					lCodiceTipoBeneficio = lCodiciSIESNSCModel.getCoSies().trim();
					if (lCodiciSIESNSCModel.getCoVal1() != null) {
						lCodiceSottoTipoBeneficio = lCodiciSIESNSCModel.getCoVal1().trim();
					} else {
						lCodiceSottoTipoBeneficio = "-";
					}

					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
							.getCODITIPOSOSPSUB() != null) {
						lCodiciSIESNSCModel = Decodifica("TIPO_SOSP_SUBORDINATA",
								adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
										.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
										.getDISPOSITIVO().getCODITIPOSOSPSUB());
						lBeneficioModel.setCodTipoSospSubordinata(lCodiciSIESNSCModel.getCoSies().trim());
					} else {
						lBeneficioModel.setCodTipoSospSubordinata("-");
					}

					lBeneficioModel.setCodTipoBeneficio(lCodiceTipoBeneficio);
					lBeneficioModel.setCodSottotipoBeneficio(lCodiceSottoTipoBeneficio);

					lBeneficioModel.setCodNaturaBeneficio("C");
					lBeneficioModel.setCodDpr("-");

					lBeneficioModel.setRifCodTipoProvvedimento("-");
					lBeneficioModel.setRifCodTipoAutoEmittente("-");
					lBeneficioModel.setRifCodTipoAutoEmittente("-");

					lBeneficioModel.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
					lBeneficioModel.setDataInserimento(DateUtils.getSysDate());
					lBeneficioModel.setCodUfficioInserimento(mCodUfficio);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("CodTipoBeneficio Sospensione Condizionale:"
							+ lBeneficioModel.getCodTipoBeneficio());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("CodTipoBeneficio Sospensione Condizionale Subordinata:"
							+ lBeneficioModel.getCodTipoSospSubordinata());

					lBeneficiVector.add(lBeneficioModel);

				}

				// Caso di NON MENZIONE
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
						.getCODICENONMENZIONE() != null) {

					BeneficioModel lBeneficioModel = new BeneficioModel();

					CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_BENEFICIO",
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
									.getDISPOSITIVO().getCODICENONMENZIONE());
					lCodiceTipoBeneficio = lCodiciSIESNSCModel.getCoSies().trim();
					if (lCodiciSIESNSCModel.getCoVal1() != null) {
						lCodiceSottoTipoBeneficio = lCodiciSIESNSCModel.getCoVal1().trim();
					} else {
						lCodiceSottoTipoBeneficio = "-";
					}

					lBeneficioModel.setCodTipoSospSubordinata("-");
					lBeneficioModel.setCodTipoBeneficio(lCodiceTipoBeneficio);
					lBeneficioModel.setCodSottotipoBeneficio(lCodiceSottoTipoBeneficio);

					lBeneficioModel.setCodNaturaBeneficio("C");
					lBeneficioModel.setCodDpr("-");

					lBeneficioModel.setRifCodTipoProvvedimento("-");
					lBeneficioModel.setRifCodTipoAutoEmittente("-");
					lBeneficioModel.setRifCodTipoAutoEmittente("-");

					lBeneficioModel.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
					lBeneficioModel.setDataInserimento(DateUtils.getSysDate());
					lBeneficioModel.setCodUfficioInserimento(mCodUfficio);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("CodTipoBeneficio Non Menzione:" + lBeneficioModel.getCodTipoBeneficio());

					lBeneficiVector.add(lBeneficioModel);

				}

			}
		}
		return lBeneficiVector;
	}

}