package siap.sico.webservice.action;

import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.DATIUTENTEDocument;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActNscToSiesLoadSostituzionePene extends ActWsBase {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String mCodUfficio = "";

	public ActNscToSiesLoadSostituzionePene(String aCodUfficio) {
		mCodUfficio = aCodUfficio;
	}

	public Vector processRequest(ANAGRAFICADocument.ANAGRAFICA adatiAnagrafica,
			DATIUTENTEDocument.DATIUTENTE adatiUtente) throws Exception {

		int ContaNumAnni = 0, ContaNumMesi = 0, ContaNumGiorni = 0, j = 0;
		String lCodTipoSanzione = "-";
		float ContaSanzioneMulta = 0, ContaSanzioneAmmenda = 0;

		int ContaSostituzionePena = 0;

		Vector lSanzioneSostitutivaVector = new Vector();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("------------------------------------------------");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("ActNscToLoadSostituzionePene - TITOLO ESECUTIVO");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("------------------------------------------------");

		if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
				.getArrayPenaComplessiva() != null) {
			// Ciclo Prima su ARRAY PENA COMPLESSIVA - DISPISITIVO
			for (j = 0; j <= adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
					.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray().length - 1; j++) {
				// NOTA: NSC puo passare più Pene Complessive, dato che SIES prevede una SOLA Sanzione
				// SOSTITUTIVA sommiamo le sanzioni.

				// SanzioneSostitutivaModel lSanzioneSostitutivaModel = new SanzioneSostitutivaModel();

				// Decodifica Codice TIPO SANZIONE (lato SIES) - Codice Tipo Beneficio (lato NSC)
				// "S" - SEMIDETENZIONE
				// "L" - Libertà CONTROLLATA
				// "P" - PENA PECUNIARIA
				// "E" - ESPULSIONE

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ActNscToLoadSostituzionePene - Indice: " + j);
				if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
						.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
						.getSOSTITUZIONEPENA() != null) {
					ContaSostituzionePena++;

					// DECODIFICA COD_TIPO_SANZIONE
					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
							.getSOSTITUZIONEPENA().getCODITIPOBENEFICIO() != null) {
						CodiciSiesNscModel lCodiciSIESNSCModel = Decodifica("TIPO_SANZIONE",
								adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
										.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
										.getDISPOSITIVO().getSOSTITUZIONEPENA().getCODITIPOBENEFICIO());
						lCodTipoSanzione = lCodiciSIESNSCModel.getCoSies().trim();
					} else {
						lCodTipoSanzione = "-";
					}

					if (lCodTipoSanzione.equals("S")) // SEMIDETENZIONE
					{
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getSOSTITUZIONEPENA().getDURATASEMIDET() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getSOSTITUZIONEPENA().getDURATASEMIDET().getANNIDURATA() != 0) {
							ContaNumAnni = ContaNumAnni + adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getSOSTITUZIONEPENA().getDURATASEMIDET().getANNIDURATA();
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getSOSTITUZIONEPENA().getDURATASEMIDET() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getSOSTITUZIONEPENA().getDURATASEMIDET().getMESIDURATA() != 0) {
							ContaNumMesi = ContaNumMesi + adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getSOSTITUZIONEPENA().getDURATASEMIDET().getMESIDURATA();
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getSOSTITUZIONEPENA().getDURATASEMIDET() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getSOSTITUZIONEPENA().getDURATASEMIDET().getGIORNIDURATA() != 0) {
							ContaNumGiorni = ContaNumGiorni + adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getSOSTITUZIONEPENA().getDURATASEMIDET().getGIORNIDURATA();
						}
					}

					if (lCodTipoSanzione.equals("L")) // LIBERTA' CONTROLLATA
					{
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getSOSTITUZIONEPENA().getDURATALIBERTACONTROLLATA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getSOSTITUZIONEPENA().getDURATALIBERTACONTROLLATA()
										.getANNIDURATA() != 0) {
							ContaNumAnni = ContaNumAnni + adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getSOSTITUZIONEPENA().getDURATALIBERTACONTROLLATA().getANNIDURATA();
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getSOSTITUZIONEPENA().getDURATALIBERTACONTROLLATA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getSOSTITUZIONEPENA().getDURATALIBERTACONTROLLATA()
										.getMESIDURATA() != 0) {
							ContaNumMesi = ContaNumMesi + adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getSOSTITUZIONEPENA().getDURATALIBERTACONTROLLATA().getMESIDURATA();
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getSOSTITUZIONEPENA().getDURATALIBERTACONTROLLATA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getSOSTITUZIONEPENA().getDURATALIBERTACONTROLLATA()
										.getGIORNIDURATA() != 0) {
							ContaNumGiorni = ContaNumGiorni + adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getSOSTITUZIONEPENA().getDURATALIBERTACONTROLLATA().getGIORNIDURATA();
						}
					}

					if (lCodTipoSanzione.equals("E")) // ESPULSIONE
					{
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getSOSTITUZIONEPENA().getDURATAESPULSTATO() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getSOSTITUZIONEPENA().getDURATAESPULSTATO().getANNIDURATA() != 0) {
							ContaNumAnni = ContaNumAnni + adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getSOSTITUZIONEPENA().getDURATAESPULSTATO().getANNIDURATA();
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getSOSTITUZIONEPENA().getDURATAESPULSTATO() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getSOSTITUZIONEPENA().getDURATAESPULSTATO().getMESIDURATA() != 0) {
							ContaNumMesi = ContaNumMesi + adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getSOSTITUZIONEPENA().getDURATAESPULSTATO().getMESIDURATA();
						}

						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getSOSTITUZIONEPENA().getDURATAESPULSTATO() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getSOSTITUZIONEPENA().getDURATAESPULSTATO().getGIORNIDURATA() != 0) {
							ContaNumGiorni = ContaNumGiorni + adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getSOSTITUZIONEPENA().getDURATAESPULSTATO().getGIORNIDURATA();
						}
					}

					/*
					 * //---> Accordo preso con ENG 15/12/2008 // Valorizzare SanzionePecuniaria facendo la
					 * Somma di IMPO_MULTA_SOST e IMPO_AMMENDA_SOST se entrambe valorizzate altrimenti o l'una
					 * o l'altra. // [MEV REL. 5.0] Accordo superato con l'aggiunta del campo
					 * SANZIONE_PECUNIARIA_AMMENDA if
					 * (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().
					 * getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().
					 * getSOSTITUZIONEPENA().getIMPOAMMENDASOST() != null &&
					 * adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().
					 * getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().
					 * getSOSTITUZIONEPENA().getIMPOMULTASOST() != null) { float lSanzionePecuniariaTotale=0;
					 * if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().
					 * getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().
					 * getSOSTITUZIONEPENA().getCODIVALUTA() != null &&
					 * adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().
					 * getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().
					 * getSOSTITUZIONEPENA().getCODIVALUTA().equals("ITL")) { lSanzionePecuniariaTotale =
					 * Utils.toEuro(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().
					 * getDATIPROVVEDIMENTO().getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).
					 * getDISPOSITIVO().getSOSTITUZIONEPENA().getIMPOAMMENDASOST().toString()).floatValue() +
					 * Utils.toEuro(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().
					 * getDATIPROVVEDIMENTO().getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).
					 * getDISPOSITIVO().getSOSTITUZIONEPENA().getIMPOMULTASOST().toString()).floatValue(); }
					 * else { lSanzionePecuniariaTotale =
					 * adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().
					 * getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().
					 * getSOSTITUZIONEPENA().getIMPOAMMENDASOST().floatValue() +
					 * adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().
					 * getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO().
					 * getSOSTITUZIONEPENA().getIMPOMULTASOST().floatValue(); }
					 * 
					 * ContaSanzione = ContaSanzione + lSanzionePecuniariaTotale; }
					 */
					// [MEV REL. 5.0] - Sanzione Sostitutiva distinzione MULTA/AMMENDA SOSTITUTIVA
					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
							.getSOSTITUZIONEPENA().getIMPOAMMENDASOST() != null) {
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getSOSTITUZIONEPENA().getCODIVALUTA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getSOSTITUZIONEPENA().getCODIVALUTA().equals("ITL")) {
							ContaSanzioneAmmenda = ContaSanzioneAmmenda + Utils
									.toEuro(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getSOSTITUZIONEPENA().getIMPOAMMENDASOST().toString())
									.floatValue();
						} else {
							ContaSanzioneAmmenda = ContaSanzioneAmmenda + adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getSOSTITUZIONEPENA().getIMPOAMMENDASOST().floatValue();
						}
					}

					if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
							.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
							.getSOSTITUZIONEPENA().getIMPOMULTASOST() != null) {
						if (adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO().getDATIPROVVEDIMENTO()
								.getArrayPenaComplessiva().getDISPOSITIVOPENACOMPLESSIVAArray(j)
								.getDISPOSITIVO().getSOSTITUZIONEPENA().getCODIVALUTA() != null
								&& adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
										.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
										.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
										.getSOSTITUZIONEPENA().getCODIVALUTA().equals("ITL")) {
							ContaSanzioneMulta = ContaSanzioneMulta + Utils
									.toEuro(adatiAnagrafica.getPROCEDIMENTO().getTITOLOESECUTIVO()
											.getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
											.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
											.getSOSTITUZIONEPENA().getIMPOMULTASOST().toString())
									.floatValue();
						} else {
							ContaSanzioneMulta = ContaSanzioneMulta + adatiAnagrafica.getPROCEDIMENTO()
									.getTITOLOESECUTIVO().getDATIPROVVEDIMENTO().getArrayPenaComplessiva()
									.getDISPOSITIVOPENACOMPLESSIVAArray(j).getDISPOSITIVO()
									.getSOSTITUZIONEPENA().getIMPOMULTASOST().floatValue();
						}
					}

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("CodTipoSanzione: " + lCodTipoSanzione);
				} // Test
			} // Ciclo For

			// NORMALIZZAZIONE DURATA
			if (ContaNumGiorni > 30) {
				int tmp = ContaNumGiorni / 30;
				ContaNumMesi += tmp;
				ContaNumGiorni -= tmp * 30;
			}

			if (ContaNumGiorni == 30) {
				ContaNumMesi++;
				ContaNumGiorni = 0;
			}

			if (ContaNumMesi > 12) {
				int tmp = ContaNumMesi / 12;
				ContaNumAnni += tmp;
				ContaNumMesi -= tmp * 12;
			}

			if (ContaNumMesi == 12) {
				ContaNumAnni++;
				ContaNumMesi = 0;
			}

			SanzioneSostitutivaModel lSanzioneSostitutivaModel = new SanzioneSostitutivaModel();

			if (ContaSostituzionePena > 1) {
				// Se ci sono più elementi come COD_TIPO_SANZIONE mettiamo "-".
				lCodTipoSanzione = "-";
			}
			lSanzioneSostitutivaModel.setCodTipoSanzione(lCodTipoSanzione);
			lSanzioneSostitutivaModel.setNumAnni(new BigDecimal(ContaNumAnni));
			lSanzioneSostitutivaModel.setNumMesi(new BigDecimal(ContaNumMesi));
			lSanzioneSostitutivaModel.setNumGiorni(new BigDecimal(ContaNumGiorni));
			lSanzioneSostitutivaModel.setSanzionePecuniariaMulta(new BigDecimal(ContaSanzioneMulta));
			lSanzioneSostitutivaModel.setSanzionePecuniariaAmmenda(new BigDecimal(ContaSanzioneAmmenda));

			lSanzioneSostitutivaModel
					.setCodOperatoreInserimento("nsc-" + adatiUtente.getUSERNAME().toString());
			lSanzioneSostitutivaModel.setDataInserimento(DateUtils.getSysDate());
			lSanzioneSostitutivaModel.setCodUfficioInserimento(mCodUfficio);
			lSanzioneSostitutivaModel.setCodOperatoreAggiornamento(null);
			lSanzioneSostitutivaModel.setDataAggiornamento(null);
			lSanzioneSostitutivaModel.setCodUfficioAggiornamento(null);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("CodTipoSanzione: " + lSanzioneSostitutivaModel.getCodTipoSanzione());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Sanzione Sostitutita Anni-Mesi_giorni:" + lSanzioneSostitutivaModel.getNumAnni()
					+ "-" + lSanzioneSostitutivaModel.getNumMesi() + "-"
					+ lSanzioneSostitutivaModel.getNumGiorni());

			lSanzioneSostitutivaVector.add(lSanzioneSostitutivaModel);
		}
		return lSanzioneSostitutivaVector;
	}

}