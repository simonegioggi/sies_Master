package siap.sico.webservice.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.DATIUTENTEDocument;
import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.siep.reato.model.ReatoModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActNscToSiesLoadReato extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String mCodUfficio = "";

	public ActNscToSiesLoadReato(String aCodUfficio) {
		mCodUfficio = aCodUfficio;
	}

	public Vector processRequest(ANAGRAFICADocument.ANAGRAFICA adatiAnagrafica,
			DATIUTENTEDocument.DATIUTENTE adatiUtente) throws Exception {

		Integer lAnno, lMese, lGiorno;
		String lAnnoAppo, lMeseAppo, lGiornoAppo, lDescLuogo;
		BigDecimal lAnnoInizioReato, lMeseInizioReato, lGiornoInizioReato, lAnnoFineReato, lMeseFineReato,
				lGiornoFineReato, lnumeroOrdine, /* lprogReato, */lprogCircostanza;
		Date /* lDataReato, */ lDataInizioReato, lDataFineReato;
		String lCodTipoReato = "-", lCodPeriodoConsumazione = "-", lCodSottNum = "-",
				lCodTipoPenaDetentiva = "-", lCodFonte = "-", lCommaQual = "-";

		int lIdContinuzazione = 0;
		long lProg_ReatoNSC = 0;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("ActNscToLoadReato - TITOLO ESECUTIVO");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("---------------------------------------------");

		Vector lReatoVector = new Vector();
		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getArrayReati() != null) {
			for (int i = 0; i <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
					.getArrayReati().getREATOArray().length - 1; i++) {

				ReatoModel lReatoModel = new ReatoModel();

				// Scrivo la KEY NSC del REATO
				lReatoModel
						.setKeyReatoNsc(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getPROGREATO()));

				lProg_ReatoNSC = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getPROGREATO();

				// DECODIFICA COD_TIPO_RATO
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getCODITIPOREATO() != null) {
					CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_REATO",
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getCODITIPOREATO());
					lCodTipoReato = lCodiciSIESNSCModel.getCoSies().trim();
				} else {
					lCodTipoReato = "-";
				}
				lReatoModel.setCodTipoReato(lCodTipoReato);

				lReatoModel.setProgrNumeroManuale(null);

				// Verificare valorizzazione PROGRESSIVO REATO
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getNUMEORDINEREATO() != 0) {
					lnumeroOrdine = new BigDecimal(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
							.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getNUMEORDINEREATO());
				} else {
					lnumeroOrdine = null;
				}
				lReatoModel.setProgrReato(lnumeroOrdine);

				/*
				 * PROGRESSIVO CIRCOSTANZA - Se = 1 indica il Reato - Viene scritto Dopo le circostanze
				 * Speciali lprogCircostanza = new BigDecimal(1);
				 * lReatoModel.setProgrCircostanza(lprogCircostanza);
				 */

				// Se è presente la DATA_REATO scriviamo tale DATA in DATA_INIZIO_REATO
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getDATAREATO() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getDATAREATO().getANNO() != null
						&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getDATAREATO().getANNO().equals("")) {
					// DATA_REATO
					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDATAREATO() != null
							&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDATAREATO().getANNO() != null
							&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDATAREATO().getANNO().equals("")) {
						lAnnoAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getDATAREATO()
								.getANNO();
						lAnnoInizioReato = new BigDecimal(lAnnoAppo);
					} else {
						lAnnoAppo = null;
						lAnnoInizioReato = null;
					}

					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDATAREATO() != null
							&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDATAREATO().getMESE() != null
							&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDATAREATO().getMESE().equals("")) {
						lMeseAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getDATAREATO()
								.getMESE();
						lMeseInizioReato = new BigDecimal(lMeseAppo);
					} else {
						lMeseAppo = null;
						lMeseInizioReato = null;
					}

					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDATAREATO() != null
							&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDATAREATO().getGIORNO() != null
							&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDATAREATO().getGIORNO().equals("")) {
						lGiornoAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getDATAREATO()
								.getGIORNO();
						lGiornoInizioReato = new BigDecimal(lGiornoAppo);
					} else {
						lGiornoAppo = null;
						lGiornoInizioReato = null;
					}
				} else {
					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDATAINIZIOREATO() != null
							&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDATAINIZIOREATO().getANNO() != null
							&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDATAINIZIOREATO().getANNO()
									.equals("")) {
						lAnnoAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getDATAINIZIOREATO()
								.getANNO();
						lAnnoInizioReato = new BigDecimal(lAnnoAppo);
					} else {
						lAnnoAppo = null;
						lAnnoInizioReato = null;
					}

					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDATAINIZIOREATO() != null
							&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDATAINIZIOREATO().getMESE() != null
							&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDATAINIZIOREATO().getMESE()
									.equals("")) {
						lMeseAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getDATAINIZIOREATO()
								.getMESE();
						lMeseInizioReato = new BigDecimal(lMeseAppo);

					} else {
						lMeseAppo = null;
						lMeseInizioReato = null;
					}

					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDATAINIZIOREATO() != null
							&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDATAINIZIOREATO().getGIORNO() != null
							&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDATAINIZIOREATO().getGIORNO()
									.equals("")) {
						lGiornoAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getDATAINIZIOREATO()
								.getGIORNO();
						lGiornoInizioReato = new BigDecimal(lGiornoAppo);
					} else {
						lGiornoAppo = null;
						lGiornoInizioReato = null;
					}
				}
				// Date lDataInizioReato;
				if ((lAnnoAppo != null && !lAnnoAppo.equals(""))
						&& (lMeseAppo != null && !lMeseAppo.equals(""))
						&& (lGiornoAppo != null && !lGiornoAppo.equals(""))) {
					lAnno = new Integer(lAnnoAppo);
					lMese = new Integer(lMeseAppo);
					lGiorno = new Integer(lGiornoAppo);
					lDataInizioReato = DateUtils.getDate(lAnno.intValue(), lMese.intValue(),
							lGiorno.intValue());
				} else {
					lDataInizioReato = null;
				}
				lReatoModel.setDataInizio(lDataInizioReato);

				lReatoModel.setAnnoInizio(lAnnoInizioReato);
				lReatoModel.setMeseInizio(lMeseInizioReato);
				lReatoModel.setGiornoInizio(lGiornoInizioReato);

				// DATA_FINE
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getDATAFINEREATO() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getDATAFINEREATO().getANNO() != null
						&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getDATAFINEREATO().getANNO().equals("")) {
					lAnnoAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDATAFINEREATO().getANNO();
					lAnnoFineReato = new BigDecimal(lAnnoAppo);
				} else {
					lAnnoAppo = null;
					lAnnoFineReato = null;
				}
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getDATAFINEREATO() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getDATAFINEREATO().getMESE() != null
						&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getDATAFINEREATO().getMESE().equals("")) {
					lMeseAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDATAFINEREATO().getMESE();
					lMeseFineReato = new BigDecimal(lMeseAppo);
				} else {
					lMeseAppo = null;
					lMeseFineReato = null;
				}
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getDATAFINEREATO() != null
						&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getDATAFINEREATO().getGIORNO() != null
						&& !adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getDATAFINEREATO().getGIORNO().equals("")) {
					lGiornoAppo = adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
							.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getDATAFINEREATO()
							.getGIORNO();
					lGiornoFineReato = new BigDecimal(lGiornoAppo);
				} else {
					lGiornoAppo = null;
					lGiornoFineReato = null;
				}
				// Date lDataFineReato;
				if ((lAnnoAppo != null && !lAnnoAppo.equals(""))
						&& (lMeseAppo != null && !lMeseAppo.equals(""))
						&& (lGiornoAppo != null && !lGiornoAppo.equals(""))) {
					lAnno = new Integer(lAnnoAppo);
					lMese = new Integer(lMeseAppo);
					lGiorno = new Integer(lGiornoAppo);
					lDataFineReato = DateUtils.getDate(lAnno.intValue(), lMese.intValue(),
							lGiorno.intValue());
				} else {
					lDataFineReato = null;
				}
				lReatoModel.setDataFine(lDataFineReato);

				lReatoModel.setAnnoFine(lAnnoFineReato);
				lReatoModel.setMeseFine(lMeseFineReato);
				lReatoModel.setGiornoFine(lGiornoFineReato);

				// DECODIFICA COD_PERIODO_CONSUMAZIONE
				lCodPeriodoConsumazione = "-";
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getCODIPERIODOCONSUMAZIONE() != null) {
					CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("PERIODO_CONSUMAZIONE",
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getCODIPERIODOCONSUMAZIONE());
					lCodPeriodoConsumazione = lCodiciSIESNSCModel.getCoSies().trim();
				}
				lReatoModel.setCodPeriodoConsumazione(lCodPeriodoConsumazione);

				// DESC_LUOGO REATO Concateniamo CODICE_LUOGO_REATO con DESC_ULTERIORE_LUOGO_REATO
				lDescLuogo = "";
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getCODILUOGOREATO() != null) {
					CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("COMUNE",
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getCODILUOGOREATO());
					lDescLuogo = lCodiciSIESNSCModel.getCoSiesDes();
				}

				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getDESCULTERIORELUOGOREATO() != null) {
					lDescLuogo = lDescLuogo + " "
							+ adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDESCULTERIORELUOGOREATO();
				}

				if (lDescLuogo.length() > 300) {
					lReatoModel.setDescLuogo(lDescLuogo.substring(0, 300).trim());
				} else {
					lReatoModel.setDescLuogo(lDescLuogo);
				}

				// DECODIFICA COD_FONTE
				lCodFonte = "-";
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getCODITL() != null) {
					CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("FONTE",
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getCODITL());
					lCodFonte = lCodiciSIESNSCModel.getCoSies().trim();
				}
				lReatoModel.setCodFonte(lCodFonte);

				// ANNO_Fonte
				BigDecimal lAnnoFonte;
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getANNOLS() != 0) {
					lAnnoFonte = new BigDecimal(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
							.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getANNOLS());
				} else {
					lAnnoFonte = null;
				}
				lReatoModel.setAnnoFonte(lAnnoFonte);

				// NUMERO_FONTE
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getNUMELS() != 0) {
					lReatoModel.setNumeroFonte(
							String.valueOf(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
									.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getNUMELS()));
				} else {
					lReatoModel.setNumeroFonte(null);
				}

				// Decodifica COD_SOTTONUMERAZIONE
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getARTIBTQ() != null) {
					CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("SOTTONUMERAZIONE",
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getARTIBTQ());
					lCodSottNum = lCodiciSIESNSCModel.getCoSies().trim();
				} else {
					lCodSottNum = "-";
				}
				lReatoModel.setCodSottonumerazione(lCodSottNum);

				// COMMA
				lReatoModel.setComma(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
						.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getARTICOMMA());

				// [MEV REL. 5.0] - Gestione Decodifica COMMA_QUALIFICANTE
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getARTICOMMABTQ() != null) {
					CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("SOTTONUMERAZIONE",
							adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getARTICOMMABTQ());
					lCommaQual = lCodiciSIESNSCModel.getCoSies().trim();
				} else {
					lCommaQual = "-";
				}
				lReatoModel.setCommaQualificante(lCommaQual);

				// LETTERA
				lReatoModel.setLettera(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
						.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getARTILETTERA());
				// NUMERO
				lReatoModel.setNumero(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
						.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getARTINUMELETTERACOMMA());
				// ARTICOLO
				lReatoModel.setArticolo(String.valueOf(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
						.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getARTINUME()));
				// Note
				lReatoModel.setNote(null);

				lReatoModel.setCodTipoPenaDetentiva("-"); // Per prevenire l'assenza del Dispositivo del Reato
				/**********************************************************************************************************/
				/* Inizio Blocco Dati DISPOSITIVO DEL REATO */
				/**********************************************************************************************************/

				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getDISPOSITIVO() != null) {
					// DECODIFICA CODI_TIPO_PENA_DETENTIVA con CODI_ERGASTOLO
					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDISPOSITIVO()
							.getCODTIPOPENADETENTIVA() != null) {
						CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_PENA_DETENTIVA",
								adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
										.getArrayReati().getREATOArray(i).getDISPOSITIVO()
										.getCODTIPOPENADETENTIVA());
						lCodTipoPenaDetentiva = lCodiciSIESNSCModel.getCoSies().trim();
					} else {
						lCodTipoPenaDetentiva = "-";
					}
					lReatoModel.setCodTipoPenaDetentiva(lCodTipoPenaDetentiva);

					// NUM_ANNI RECLUSIONE
					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDISPOSITIVO().getRECLUSIONE() != null
							&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDISPOSITIVO().getRECLUSIONE()
									.getANNIDURATA() != 0) {
						lReatoModel.setNumAnni(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO()
								.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
								.getDISPOSITIVO().getRECLUSIONE().getANNIDURATA()));
					} else {
						lReatoModel.setNumAnni(null);
					}
					// NUM_MESI RECLUSIONE
					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDISPOSITIVO().getRECLUSIONE() != null
							&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDISPOSITIVO().getRECLUSIONE()
									.getMESIDURATA() != 0) {
						lReatoModel.setNumMesi(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO()
								.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
								.getDISPOSITIVO().getRECLUSIONE().getMESIDURATA()));
					} else {
						lReatoModel.setNumMesi(null);
					}
					// NUM_GIORNI RECLUSIONE
					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDISPOSITIVO().getRECLUSIONE() != null
							&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDISPOSITIVO().getRECLUSIONE()
									.getGIORNIDURATA() != 0) {
						lReatoModel.setNumGiorni(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO()
								.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
								.getDISPOSITIVO().getRECLUSIONE().getGIORNIDURATA()));
					} else {
						lReatoModel.setNumGiorni(null);
					}

					// SANZIONE PECUNIARIA
					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDISPOSITIVO().getIMPOMULTA() != null) {
						lReatoModel.setCodTipoSanzione("01"); // Multa
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getDISPOSITIVO().getCODIVALUTA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
										.getDISPOSITIVO().getCODIVALUTA().equals("ITL")) {
							lReatoModel.setSanzionePecuniaria(Utils.toEuro(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayReati()
									.getREATOArray(i).getDISPOSITIVO().getIMPOMULTA().toString()));
						} else {
							lReatoModel.setSanzionePecuniaria(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayReati()
									.getREATOArray(i).getDISPOSITIVO().getIMPOMULTA());
						}
					}

					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDISPOSITIVO().getIMPOAMMENDA() != null) {
						lReatoModel.setCodTipoSanzione("02"); // Ammenda
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getDISPOSITIVO().getCODIVALUTA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
										.getDISPOSITIVO().getCODIVALUTA().equals("ITL")) {
							lReatoModel.setSanzionePecuniaria(Utils.toEuro(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayReati()
									.getREATOArray(i).getDISPOSITIVO().getIMPOAMMENDA().toString()));
						} else {
							lReatoModel.setSanzionePecuniaria(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayReati()
									.getREATOArray(i).getDISPOSITIVO().getIMPOAMMENDA());
						}
					}

					// ANNI_ISOLAMENTO_DIURNO
					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDISPOSITIVO().getISOLAMENTODIURNO() != null
							&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDISPOSITIVO().getISOLAMENTODIURNO()
									.getANNIDURATA() != 0) {
						lReatoModel.setNumAnniIsolamentoDiurno(new BigDecimal(adatiAnagrafica
								.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayReati()
								.getREATOArray(i).getDISPOSITIVO().getISOLAMENTODIURNO().getANNIDURATA()));
					} else {
						lReatoModel.setNumAnniIsolamentoDiurno(null);
					}
					// MESI_ISOLAMENTO_DIURNO
					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDISPOSITIVO().getISOLAMENTODIURNO() != null
							&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDISPOSITIVO().getISOLAMENTODIURNO()
									.getMESIDURATA() != 0) {
						lReatoModel.setNumMesiIsolamentoDiurno(new BigDecimal(adatiAnagrafica
								.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayReati()
								.getREATOArray(i).getDISPOSITIVO().getISOLAMENTODIURNO().getMESIDURATA()));
					} else {
						lReatoModel.setNumMesiIsolamentoDiurno(null);
					}
					// GIORNI_ISOLAMENTO_DIURNO
					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDISPOSITIVO().getISOLAMENTODIURNO() != null
							&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getREATOArray(i).getDISPOSITIVO().getISOLAMENTODIURNO()
									.getGIORNIDURATA() != 0) {
						lReatoModel.setNumGiorniIsolamentoDiurno(new BigDecimal(adatiAnagrafica
								.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayReati()
								.getREATOArray(i).getDISPOSITIVO().getISOLAMENTODIURNO().getGIORNIDURATA()));
					} else {
						lReatoModel.setNumGiorniIsolamentoDiurno(null);
					}

					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayReati().getREATOArray(i).getDISPOSITIVO()
							.getCODTIPOPENADETENTIVA() != null) {
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getDISPOSITIVO().getCODTIPOPENADETENTIVA()
								.equals("77")
								|| adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
										.getDISPOSITIVO().getCODTIPOPENADETENTIVA().equals("78")) {
							lReatoModel.setFlagErgastolo("S");
						}
					}
				}
				/**********************************************************************************************************/
				/* FINE Blocco Dati DISPOSITIVO DEL REATO */
				/**********************************************************************************************************/

				lReatoModel.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
				lReatoModel.setDataInserimento(DateUtils.getSysDate());
				lReatoModel.setCodUfficioInserimento(mCodUfficio);
				lReatoModel.setCodOperatoreAggiornamento(null);
				lReatoModel.setDataAggiornamento(null);
				lReatoModel.setCodUfficioAggiornamento(null);

				lReatoModel.setCodTipoSanzione("-");

				/**********************************************************************************************************/
				/* Blocco VERIFICA CONTINUAZIONE REATI */
				/**********************************************************************************************************/
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getArrayContinuazione() != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("-----------VERIFICA CONTINUAZIONE REATI-----------");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Array Continuazione Riga :"
							+ adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
									.getArrayReati().getArrayContinuazione().getContinuazioneArray().length);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("Array Continuazione Colonna:" + adatiAnagrafica.getPROCEDIMENTO()
							.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayReati()
							.getArrayContinuazione().getContinuazioneArray(0).getNumeroArray().length);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("ID REATO NSC" + lProg_ReatoNSC);

					for (int x = 0; x <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
							.getDATIPROVVEDIMENTO().getArrayReati().getArrayContinuazione()
							.getContinuazioneArray().length - 1; x++) {
						for (int y = 0; y <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getArrayContinuazione()
								.getContinuazioneArray(x).getNumeroArray().length - 1; y++) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Riga: " + x + " Colonna: " + y);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Reato Continuazione:" + adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayReati()
									.getArrayContinuazione().getContinuazioneArray(x).getNumeroArray(y));

							if (lProg_ReatoNSC == adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
									.getDATIPROVVEDIMENTO().getArrayReati().getArrayContinuazione()
									.getContinuazioneArray(x).getNumeroArray(y)) {
								lIdContinuzazione = x + 1;
								lReatoModel.setIdContinuazioneReato(new BigDecimal(lIdContinuzazione));
								lReatoModel.setTipoContinuazioneReato(adatiAnagrafica.getPROCEDIMENTO()
										.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayReati()
										.getArrayContinuazione().getContinuazioneArray(x)
										.getTipoContinuazione().toString());
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.info("Identificativo Reato uguale");
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.info(
										"ID_CONTINUAZIONE_REATO:" + lReatoModel.getIdContinuazioneReato());
								// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
								// al posto di LogF3B.getLogger()
								siesLogger.info("TIPO_CONTINUAZIONE_REATO:"
										+ lReatoModel.getTipoContinuazioneReato());
							}
						}
					}
				}
				// -------------------------------- FINE Blocco VERIFICA CONTINUAZIONE REATI
				// --------------------------------

				/*----------------------------------INIZIO BLOCCO CIRCOSTANZE SPECIALI -------------------------------------*/
				int ContaProgCirc = 0;
				// Verifichiamo ed Eventualmente Scriviamo per Prime le Circostanze Speciali
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getArrayCircostanzeSpeciali() != null) {
					for (int j = 0; j <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
							.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
							.getArrayCircostanzeSpeciali().getCIRCOSTANZAArray().length - 1; j++) {
						ReatoModel lReatoModel1 = new ReatoModel();

						lReatoModel1.setKeyReatoNsc(null);

						// DECODIFCA Tipo Reato (gia fatta)
						lReatoModel1.setCodTipoReato(lCodTipoReato);

						lReatoModel1.setProgrNumeroManuale(null);

						// PROGRESSIVO REATO che identifica l'accoppiata Reati - Circostanze
						lReatoModel1.setProgrReato(lnumeroOrdine);

						// PROGRESSIVO CIRCOSTANZA - Se = 1 indica il Reato - Se = n indice le circostanze del
						// Reato
						ContaProgCirc++;
						lprogCircostanza = new BigDecimal(ContaProgCirc);
						lReatoModel1.setProgrCircostanza(lprogCircostanza);

						lReatoModel1.setDataInizio(lDataInizioReato);
						lReatoModel1.setDataFine(lDataFineReato);

						lReatoModel1.setAnnoInizio(lAnnoInizioReato);
						lReatoModel1.setMeseInizio(lMeseInizioReato);
						lReatoModel1.setGiornoInizio(lGiornoInizioReato);

						lReatoModel1.setAnnoFine(lAnnoFineReato);
						lReatoModel1.setMeseFine(lMeseFineReato);
						lReatoModel1.setGiornoFine(lGiornoFineReato);

						// DECODIFICA COD_PERIODO_CONSUMAZIONE (gia fatta)
						lReatoModel1.setCodPeriodoConsumazione(lCodPeriodoConsumazione);

						// DESC_LUOGO
						lReatoModel1.setDescLuogo(lDescLuogo);

						// DECODIFICA COD_FONTE
						lCodFonte = "-";
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getArrayCircostanzeSpeciali()
								.getCIRCOSTANZAArray(j).getCODITL() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("FONTE",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
											.getArrayCircostanzeSpeciali().getCIRCOSTANZAArray(j)
											.getCODITL());
							lCodFonte = lCodiciSIESNSCModel.getCoSies().trim();
						}
						lReatoModel1.setCodFonte(lCodFonte);

						// ANNO_Fonte
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getArrayCircostanzeSpeciali()
								.getCIRCOSTANZAArray(j).getANNOLS() != 0) {
							lReatoModel1.setAnnoFonte(new BigDecimal(adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayReati()
									.getREATOArray(i).getArrayCircostanzeSpeciali().getCIRCOSTANZAArray(j)
									.getANNOLS()));
						} else {
							lReatoModel1.setAnnoFonte(null);
						}

						// NUMERO_FONTE
						lReatoModel1.setNumeroFonte(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
								.getArrayCircostanzeSpeciali().getCIRCOSTANZAArray(j).getNUMELS());

						// DECODIFICA COD_SOTTONUMERAZIONE
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getArrayCircostanzeSpeciali()
								.getCIRCOSTANZAArray(j).getARTIBTQ() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("SOTTONUMERAZIONE",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
											.getArrayCircostanzeSpeciali().getCIRCOSTANZAArray(j)
											.getARTIBTQ());
							lCodSottNum = lCodiciSIESNSCModel.getCoSies().trim();
						} else {
							lCodSottNum = "-";
						}
						lReatoModel1.setCodSottonumerazione(lCodSottNum);

						// COMMA
						lReatoModel1.setComma(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
								.getArrayCircostanzeSpeciali().getCIRCOSTANZAArray(j).getARTICOMMA());

						// [MEV REL. 5.0] - Gestione Decodifica COMMA_QUALIFICANTE
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getArrayCircostanzeSpeciali()
								.getCIRCOSTANZAArray(j).getARTICOMMABTQ() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("SOTTONUMERAZIONE",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
											.getArrayCircostanzeSpeciali().getCIRCOSTANZAArray(j)
											.getARTICOMMABTQ());
							lCommaQual = lCodiciSIESNSCModel.getCoSies().trim();
						} else {
							lCommaQual = "-";
						}
						lReatoModel1.setCommaQualificante(lCommaQual);

						// LETTERA
						lReatoModel1.setLettera(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
								.getArrayCircostanzeSpeciali().getCIRCOSTANZAArray(j).getARTILETTERA());
						// NUMERO
						lReatoModel1.setNumero(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
								.getArrayCircostanzeSpeciali().getCIRCOSTANZAArray(j).getARTINUMEARTICOLO());
						// ARTICOLO
						lReatoModel1.setArticolo(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
								.getArrayCircostanzeSpeciali().getCIRCOSTANZAArray(j).getARTILS());
						// Note
						lReatoModel1.setNote(null);

						lReatoModel1
								.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
						lReatoModel1.setDataInserimento(DateUtils.getSysDate());
						lReatoModel1.setCodUfficioInserimento(mCodUfficio);
						lReatoModel1.setCodOperatoreAggiornamento(null);
						lReatoModel1.setDataAggiornamento(null);
						lReatoModel1.setCodUfficioAggiornamento(null);

						lReatoModel1.setCodTipoSanzione("-");
						lReatoModel1.setCodTipoPenaDetentiva(lCodTipoPenaDetentiva);

						// Ripetiamo per le circostanze l'eventuale presenza della continuazione di reato
						// (cosi si comporta l'applicativo SIES)
						if (lReatoModel.getIdContinuazioneReato() != null) {
							lReatoModel1.setIdContinuazioneReato(lReatoModel.getIdContinuazioneReato());
						}
						if (lReatoModel.getTipoContinuazioneReato() != null) {
							lReatoModel1.setTipoContinuazioneReato(lReatoModel.getTipoContinuazioneReato());
						}

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("-----------CIRCOSTANZE Speciali affogate nei REATI-----------");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Prog Reato:" + lReatoModel1.getProgrReato());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Prog Circostanza:" + lReatoModel1.getProgrCircostanza());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("KEY_REATO_NSC:#" + lReatoModel1.getKeyReatoNsc() + "#");

						lReatoVector.add(lReatoModel1);
					}
				}
				/*----------------------------------- FINE BLOCCO CIRCOSTANZE SPECIALI -----------------------------------*/

				ContaProgCirc++;
				lprogCircostanza = new BigDecimal(ContaProgCirc);
				lReatoModel.setProgrCircostanza(lprogCircostanza);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("CodiTipoReato:" + lReatoModel.getCodTipoReato());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Prog Reato:" + lReatoModel.getProgrReato());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("Prog Circostanza:" + lReatoModel.getProgrCircostanza());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("KEY_REATO_NSC:" + lReatoModel.getKeyReatoNsc());

				lReatoVector.add(lReatoModel);

				/*************************************************************/
				/* Ciclo sull'array delle Circostanze presente nel TAG REATO */
				/*************************************************************/
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayReati().getREATOArray(i).getArrayCircostanze() != null) {
					for (int j = 0; j <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
							.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getArrayCircostanze()
							.getCIRCOSTANZAArray().length - 1; j++) {

						ReatoModel lReatoModel1 = new ReatoModel();

						lReatoModel1.setKeyReatoNsc(null);

						// DECODIFCA Tipo Reato (gia fatta)
						lReatoModel1.setCodTipoReato(lCodTipoReato);

						// DATA_REATO
						// lReatoModel1.setDataReato(lDataReato);

						lReatoModel1.setProgrNumeroManuale(null);

						// PROGRESSIVO REATO che identifica l'accoppiata Reati - Circostanze
						lReatoModel1.setProgrReato(lnumeroOrdine);

						// PROGRESSIVO CIRCOSTANZA - Se = 1 indica il Reato - Se = n indice le circostanze del
						// Reato
						ContaProgCirc++;
						lprogCircostanza = new BigDecimal(ContaProgCirc);
						lReatoModel1.setProgrCircostanza(lprogCircostanza);

						lReatoModel1.setDataInizio(lDataInizioReato);
						lReatoModel1.setDataFine(lDataFineReato);

						lReatoModel1.setAnnoInizio(lAnnoInizioReato);
						lReatoModel1.setMeseInizio(lMeseInizioReato);
						lReatoModel1.setGiornoInizio(lGiornoInizioReato);

						lReatoModel1.setAnnoFine(lAnnoFineReato);
						lReatoModel1.setMeseFine(lMeseFineReato);
						lReatoModel1.setGiornoFine(lGiornoFineReato);

						// DECODIFICA COD_PERIODO_CONSUMAZIONE (gia fatta)
						lReatoModel1.setCodPeriodoConsumazione(lCodPeriodoConsumazione);

						// DESC_LUOGO
						lReatoModel1.setDescLuogo(lDescLuogo);

						// DECODIFICA COD_FONTE
						lCodFonte = "-";
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getArrayCircostanze().getCIRCOSTANZAArray(j)
								.getCODITL() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("FONTE",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
											.getArrayCircostanze().getCIRCOSTANZAArray(j).getCODITL());
							lCodFonte = lCodiciSIESNSCModel.getCoSies().trim();
						}
						lReatoModel1.setCodFonte(lCodFonte);

						// ANNO_Fonte
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getArrayCircostanze().getCIRCOSTANZAArray(j)
								.getANNOLS() != 0) {
							lReatoModel1.setAnnoFonte(
									new BigDecimal(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
											.getArrayCircostanze().getCIRCOSTANZAArray(j).getANNOLS()));
						} else {
							lReatoModel1.setAnnoFonte(null);
						}

						// NUMERO_FONTE
						lReatoModel1.setNumeroFonte(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getArrayCircostanze()
								.getCIRCOSTANZAArray(j).getNUMELS());

						// DECODIFICA COD_SOTTONUMERAZIONE
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getArrayCircostanze().getCIRCOSTANZAArray(j)
								.getARTIBTQ() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("SOTTONUMERAZIONE",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
											.getArrayCircostanze().getCIRCOSTANZAArray(j).getARTIBTQ());
							lCodSottNum = lCodiciSIESNSCModel.getCoSies().trim();
						} else {
							lCodSottNum = "-";
						}
						lReatoModel1.setCodSottonumerazione(lCodSottNum);

						// COMMA
						lReatoModel1.setComma(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getArrayCircostanze()
								.getCIRCOSTANZAArray(j).getARTICOMMA());

						// [MEV REL. 5.0] - Gestione Decodifica COMMA_QUALIFICANTE
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayReati().getREATOArray(i).getArrayCircostanze().getCIRCOSTANZAArray(j)
								.getARTICOMMABTQ() != null) {
							CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("SOTTONUMERAZIONE",
									adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i)
											.getArrayCircostanze().getCIRCOSTANZAArray(j).getARTICOMMABTQ());
							lCommaQual = lCodiciSIESNSCModel.getCoSies().trim();
						} else {
							lCommaQual = "-";
						}
						lReatoModel1.setCommaQualificante(lCommaQual);

						// LETTERA
						lReatoModel1.setLettera(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getArrayCircostanze()
								.getCIRCOSTANZAArray(j).getARTILETTERA());
						// NUMERO
						lReatoModel1.setNumero(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getArrayCircostanze()
								.getCIRCOSTANZAArray(j).getARTINUMEARTICOLO());
						// ARTICOLO
						lReatoModel1.setArticolo(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
								.getDATIPROVVEDIMENTO().getArrayReati().getREATOArray(i).getArrayCircostanze()
								.getCIRCOSTANZAArray(j).getARTILS());
						// Note
						lReatoModel1.setNote(null);

						lReatoModel1
								.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
						lReatoModel1.setDataInserimento(DateUtils.getSysDate());
						lReatoModel1.setCodUfficioInserimento(mCodUfficio);
						lReatoModel1.setCodOperatoreAggiornamento(null);
						lReatoModel1.setDataAggiornamento(null);
						lReatoModel1.setCodUfficioAggiornamento(null);

						lReatoModel1.setCodTipoSanzione("-");
						lReatoModel1.setCodTipoPenaDetentiva(lCodTipoPenaDetentiva);

						// Ripetiamo per le circostanze l'eventuale presenza della continuazione di reato
						// (cosi si comporta l'applicativo SIES)
						if (lReatoModel.getIdContinuazioneReato() != null) {
							lReatoModel1.setIdContinuazioneReato(lReatoModel.getIdContinuazioneReato());
						}
						if (lReatoModel.getTipoContinuazioneReato() != null) {
							lReatoModel1.setTipoContinuazioneReato(lReatoModel.getTipoContinuazioneReato());
						}

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("-----------CIRCOSTANZA affogate nei REATI-----------");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Prog Reato:" + lReatoModel1.getProgrReato());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Prog Circostanza:" + lReatoModel1.getProgrCircostanza());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("KEY_REATO_NSC:#" + lReatoModel1.getKeyReatoNsc() + "#");

						lReatoVector.add(lReatoModel1);

					}
				}
			}
		}
		return lReatoVector;
	}

}