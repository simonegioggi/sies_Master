package siap.sico.webservice.action;

import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.DATIUTENTEDocument;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActNscToSiesLoadPenaComplessiva extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String mCodUfficio = "";

	public ActNscToSiesLoadPenaComplessiva(String aCodUfficio) {
		mCodUfficio = aCodUfficio;
	}

	public Vector processRequest(ANAGRAFICADocument.ANAGRAFICA adatiAnagrafica,
			DATIUTENTEDocument.DATIUTENTE adatiUtente) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("ActNscToLoadPenaComplessiva - TITOLO ESECUTIVO");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");

		int ContaNumAnniReclusione = 0, ContaNumMesiReclusione = 0, ContaNumGiorniReclusione = 0;
		int ContaNumAnniArresto = 0, ContaNumMesiArresto = 0, ContaNumGiorniArresto = 0;
		int ContaNumAnniIsolamentoDiurno = 0, ContaNumMesiIsolamentoDiurno = 0,
				ContaNumGiorniIsolamentoDiurno = 0;
		String lCodTipoPenaDetentiva = "-";
		float ContaImpoMulta = 0, ContaImpoAmmenda = 0;
		Vector lPenaComplVector = new Vector();

		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getArrayPenaComplessiva() != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Lunghezzas Array Pena Complessiva: "
					+ adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray().length);
			for (int i = 0; i <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
					.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray().length - 1; i++) {

				// PenaComplessivaModel lPenaComplModel = new PenaComplessivaModel();

				// NOTA: NSC puo passare più Pene Complessive in questo caso dato che SIES prevede una SOLA
				// Pena Complessiva sommiamo le pene.

				// Se ci sono più elementi come COD_TIPO_PENA_DETENTIVA mettiamo "-".
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray().length > 1) {
					lCodTipoPenaDetentiva = "-";
				} else if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO()
						.getCODTIPOPENADETENTIVA() != null) {
					// DECODIFICA COD_TIPO_PENA_DETENTIVA con CODI_ERGASTOLO
					CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_PENA_DETENTIVA",
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
									.getDISPOSITIVO().getCODTIPOPENADETENTIVA());
					lCodTipoPenaDetentiva = lCodiciSIESNSCModel.getCoSies().trim();
				}

				// NUM_ANNI_RECLUSIONE
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO()
						.getRECLUSIONE() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
								.getDISPOSITIVO().getRECLUSIONE().getANNIDURATA() != 0) {
					ContaNumAnniReclusione = ContaNumAnniReclusione
							+ adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
									.getDISPOSITIVO().getRECLUSIONE().getANNIDURATA();
				}

				// NUM_MESI_RECLUSIONE
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO()
						.getRECLUSIONE() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
								.getDISPOSITIVO().getRECLUSIONE().getMESIDURATA() != 0) {
					ContaNumMesiReclusione = ContaNumMesiReclusione
							+ adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
									.getDISPOSITIVO().getRECLUSIONE().getMESIDURATA();
				}

				// NUM_GIORNI_RECLUSIONE
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO()
						.getRECLUSIONE() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
								.getDISPOSITIVO().getRECLUSIONE().getGIORNIDURATA() != 0) {
					ContaNumGiorniReclusione = ContaNumGiorniReclusione
							+ adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
									.getDISPOSITIVO().getRECLUSIONE().getGIORNIDURATA();
				}

				// IMPORTO MULTA
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO()
						.getIMPOMULTA() != null) {
					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO()
							.getCODIVALUTA() != null
							&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
									.getDISPOSITIVO().getCODIVALUTA().equals("ITL")) {
						ContaImpoMulta = ContaImpoMulta + Utils.toEuro(
								adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
										.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
										.getDISPOSITIVO().getIMPOMULTA().toString())
								.floatValue();
					} else {
						ContaImpoMulta = ContaImpoMulta + adatiAnagrafica.getPROCEDIMENTO()
								.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
								.getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO().getIMPOMULTA()
								.floatValue();
					}
				}

				// NUM_ANNI_ARRESTO
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO()
						.getARRESTO() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
								.getDISPOSITIVO().getARRESTO().getANNIDURATA() != 0) {
					ContaNumAnniArresto = ContaNumAnniArresto
							+ adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
									.getDISPOSITIVO().getARRESTO().getANNIDURATA();
				}

				// NUM_MESI_ARRESTO
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO()
						.getARRESTO() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
								.getDISPOSITIVO().getARRESTO().getMESIDURATA() != 0) {
					ContaNumMesiArresto = ContaNumMesiArresto
							+ adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
									.getDISPOSITIVO().getARRESTO().getMESIDURATA();
				}

				// NUM_GIORNI_ARRESTO
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO()
						.getARRESTO() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
								.getDISPOSITIVO().getARRESTO().getGIORNIDURATA() != 0) {
					ContaNumGiorniArresto = ContaNumGiorniArresto
							+ adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
									.getDISPOSITIVO().getARRESTO().getGIORNIDURATA();
				}

				// IMPORTO AMMENDA
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO()
						.getIMPOAMMENDA() != null) {
					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO()
							.getCODIVALUTA() != null
							&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
									.getDISPOSITIVO().getCODIVALUTA().equals("ITL")) {
						ContaImpoAmmenda = ContaImpoAmmenda + Utils.toEuro(
								adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
										.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
										.getDISPOSITIVO().getIMPOAMMENDA().toString())
								.floatValue();
					} else {
						ContaImpoAmmenda = ContaImpoAmmenda + adatiAnagrafica.getPROCEDIMENTO()
								.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
								.getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO().getIMPOAMMENDA()
								.floatValue();
					}
				}

				// NUM_ANNI_ISOLAMENTO_DIURNO
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO()
						.getISOLAMENTODIURNO() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
								.getDISPOSITIVO().getISOLAMENTODIURNO().getANNIDURATA() != 0) {
					ContaNumAnniIsolamentoDiurno = ContaNumAnniIsolamentoDiurno
							+ adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
									.getDISPOSITIVO().getISOLAMENTODIURNO().getANNIDURATA();
				}

				// NUM_MESI_ISOLAMENTO_DIURNO
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO()
						.getISOLAMENTODIURNO() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
								.getDISPOSITIVO().getISOLAMENTODIURNO().getMESIDURATA() != 0) {
					ContaNumMesiIsolamentoDiurno = ContaNumMesiIsolamentoDiurno
							+ adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
									.getDISPOSITIVO().getISOLAMENTODIURNO().getMESIDURATA();
				}

				// NUM_GIORNI_ISOLAMENTO_DIURNO
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i).getDISPOSITIVO()
						.getISOLAMENTODIURNO() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
								.getDISPOSITIVO().getISOLAMENTODIURNO().getGIORNIDURATA() != 0) {
					ContaNumGiorniIsolamentoDiurno = ContaNumGiorniIsolamentoDiurno
							+ adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(i)
									.getDISPOSITIVO().getISOLAMENTODIURNO().getGIORNIDURATA();
				}

			}

			// NORMALIZZAZIONE RECLUSIONE
			if (ContaNumGiorniReclusione > 30) {
				int tmp = ContaNumGiorniReclusione / 30;
				ContaNumMesiReclusione += tmp;
				ContaNumGiorniReclusione -= tmp * 30;
			}

			if (ContaNumGiorniReclusione == 30) {
				ContaNumMesiReclusione++;
				ContaNumGiorniReclusione = 0;
			}

			if (ContaNumMesiReclusione > 12) {
				int tmp = ContaNumMesiReclusione / 12;
				ContaNumAnniReclusione += tmp;
				ContaNumMesiReclusione -= tmp * 12;
			}

			if (ContaNumMesiReclusione == 12) {
				ContaNumAnniReclusione++;
				ContaNumMesiReclusione = 0;
			}

			// NORMALIZZAZIONE ARRESTO
			if (ContaNumGiorniArresto > 30) {
				int tmp = ContaNumGiorniArresto / 30;
				ContaNumMesiArresto += tmp;
				ContaNumGiorniArresto -= tmp * 30;
			}

			if (ContaNumGiorniArresto == 30) {
				ContaNumMesiArresto++;
				ContaNumGiorniArresto = 0;
			}

			if (ContaNumMesiArresto > 12) {
				int tmp = ContaNumMesiArresto / 12;
				ContaNumAnniArresto += tmp;
				ContaNumMesiArresto -= tmp * 12;
			}

			if (ContaNumMesiArresto == 12) {
				ContaNumAnniArresto++;
				ContaNumMesiArresto = 0;
			}

			// NORMALIZZAZIONE ISOLAMENTO DIURNO
			if (ContaNumGiorniIsolamentoDiurno > 30) {
				int tmp = ContaNumGiorniIsolamentoDiurno / 30;
				ContaNumMesiIsolamentoDiurno += tmp;
				ContaNumGiorniIsolamentoDiurno -= tmp * 30;
			}

			if (ContaNumGiorniIsolamentoDiurno == 30) {
				ContaNumMesiIsolamentoDiurno++;
				ContaNumGiorniIsolamentoDiurno = 0;
			}

			if (ContaNumMesiIsolamentoDiurno > 12) {
				int tmp = ContaNumMesiIsolamentoDiurno / 12;
				ContaNumAnniIsolamentoDiurno += tmp;
				ContaNumMesiIsolamentoDiurno -= tmp * 12;
			}

			if (ContaNumMesiIsolamentoDiurno == 12) {
				ContaNumAnniIsolamentoDiurno++;
				ContaNumMesiIsolamentoDiurno = 0;
			}

			PenaComplessivaModel lPenaComplModel = new PenaComplessivaModel();

			lPenaComplModel.setCodTipoPenaDetentiva(lCodTipoPenaDetentiva);

			lPenaComplModel.setNumAnniReclusione(new BigDecimal(ContaNumAnniReclusione));
			lPenaComplModel.setNumMesiReclusione(new BigDecimal(ContaNumMesiReclusione));
			lPenaComplModel.setNumGiorniReclusione(new BigDecimal(ContaNumGiorniReclusione));
			lPenaComplModel.setImportoMulta(new BigDecimal(ContaImpoMulta));

			lPenaComplModel.setNumAnniArresto(new BigDecimal(ContaNumAnniArresto));
			lPenaComplModel.setNumMesiArresto(new BigDecimal(ContaNumMesiArresto));
			lPenaComplModel.setNumGiorniArresto(new BigDecimal(ContaNumGiorniArresto));
			lPenaComplModel.setImportoAmmenda(new BigDecimal(ContaImpoAmmenda));

			lPenaComplModel.setNumAnniIsolamentoDiurno(new BigDecimal(ContaNumAnniIsolamentoDiurno));
			lPenaComplModel.setNumMesiIsolamentoDiurno(new BigDecimal(ContaNumMesiIsolamentoDiurno));
			lPenaComplModel.setNumGiorniIsolamentoDiurno(new BigDecimal(ContaNumGiorniIsolamentoDiurno));

			// COD_TIPO_RITO
			lPenaComplModel.setCodTipoRito("-");

			lPenaComplModel.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
			lPenaComplModel.setDataInserimento(DateUtils.getSysDate());
			lPenaComplModel.setCodUfficioInserimento(mCodUfficio);
			lPenaComplModel.setCodOperatoreAggiornamento(null);
			lPenaComplModel.setDataAggiornamento(null);
			lPenaComplModel.setCodUfficioAggiornamento(null);

			lPenaComplVector.add(lPenaComplModel);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("CodiTipoPenaDetentiva: " + lCodTipoPenaDetentiva);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Reclusione: Anni-Mesi-Giorni: " + ContaNumAnniReclusione + "-"
					+ ContaNumMesiReclusione + "-" + ContaNumGiorniReclusione);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Impo Multa: " + ContaImpoMulta);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Arresto: Anni-Mesi-Giorni: " + ContaNumAnniArresto + "-" + ContaNumMesiArresto
					+ "-" + ContaNumGiorniArresto);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Impo Ammenda: " + ContaImpoAmmenda);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Isolamento Diurno: Anni-Mesi-Giorni: " + ContaNumAnniIsolamentoDiurno + "-"
					+ ContaNumMesiIsolamentoDiurno + "-" + ContaNumGiorniIsolamentoDiurno);
		}

		return lPenaComplVector;
	}

}