package siap.sius.depositosentenza.action;

import java.util.Collection;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ActInserisciEmissioneDecreto;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import f3b.log.LogF3B;

/**
 * <p>
 * Title: ActInserisciEmissioneSentenzaTDS
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Emissione Sentenza TDS
 * </p>
 * Poichè l'azione deve implementare la stessa funzione implementata da ActLoadEmissioneDecreto, viene estesa
 * questa in modo di utilizzare il suo processRequest(). Si sfrutta l'override della funzione
 * generaListaTipi() per differenziare la jsp.
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciEmissioneSentenzaTDS extends ActInserisciEmissioneDecreto implements
		ICostantiDepositoSentenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Metodo sovrascritto della classe ActInserisciEmissioneDecreto. La funzione in base al codice tipo
	 * ordinanza richiesto seleziona la jsp di input per l'ordinanza specifica.
	 */
	@SuppressWarnings("rawtypes")
	public String selezione() throws Exception {

		// Lettura contenuto
		String lCodContenuto = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("cod Tipo lCodContenuto = " + lCodContenuto);

		String lCodTipoDec = null;

		// Generazione automatica in base al contenuto
		Collection lOggetti = DecodificheManager.getInstance().getOggettoProcedimentoTDS();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("N° Oggetti = " + lOggetti.size());
		lCodTipoDec = DecodificheUtils.getCodAltebyCode(lOggetti, lCodContenuto);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("cod Tipo Sentenza = " + lCodTipoDec);

		if (lCodTipoDec == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Tipo sentenza non definito");

		// Sentenza Generica
		mRetPage = PG_LOAD_INSERISCI_SENTENZA_GENERICA;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Sentenza Generica " + lCodTipoDec);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("cod Tipo sentenza decodificata" + lCodTipoDec);

		return lCodTipoDec;
	}

	// Viene ricavata la data di fine pena e passata alla request
//	private void ricavaDataFinepena() throws Exception {
//
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("ricavaDataFinepena: inizio");
//
//		// Si preleva dalla sessione il fascicolo GPModel.
//		if (!isSessionAttributeNullObj("fascicoloSiusGP")) {
//			FascicoloGPModel lFasGPMod = new FascicoloGPModel(
//					(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
//			if (lFasGPMod.getFascicoloSiusModel() != null) {
//				BigDecimal lIdFascicoloSiep = lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep();
//				if (lIdFascicoloSiep != null) {
//					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//					// LogF3B.getLogger()
//					siesLogger.debug("ID Fascicolo SIEP ->" + lIdFascicoloSiep);
//					IPenaResidua lPenaResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
//					PenaResiduaModel lPenaRes = lPenaResCtrl
//							.ExRicercaPenaResiduaUltimaValidata(lIdFascicoloSiep);
//					if (lPenaRes != null) {
//						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
//						// di LogF3B.getLogger()
//						siesLogger.debug("ID Pena Residua ->" + lPenaRes.getIdPenaResidua());
//						if (lPenaRes.getDataFine() != null) {
//							setRequestAttribute("data_fine_misura_dd",
//									DateUtils.getDateToString(lPenaRes.getDataFine(), "dd"));
//							setRequestAttribute("data_fine_misura_MM",
//									DateUtils.getDateToString(lPenaRes.getDataFine(), "MM"));
//							setRequestAttribute("data_fine_misura_yyyy",
//									DateUtils.getDateToString(lPenaRes.getDataFine(), "yyyy"));
//						}
//					}
//				}
//			}
//		}
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("ricavaDataFinepena: fine");
//	}

	// Viene ricavata la Sanzione Residua o la Sanzione Sostitutiva e passata alla request
//	private void ricercaPenaComplessivaSanzioneSostitutivaByIdFasSiep() throws Exception {
//
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("ricercaPenaComplessivaSanzioneSostitutivaByIdFasSiep: inizio");
//
//		// Si preleva dalla sessione il fascicolo GPModel.
//		if (!isSessionAttributeNullObj("fascicoloSiusGP")) {
//			FascicoloGPModel lFasGPMod = new FascicoloGPModel(
//					(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
//			if (lFasGPMod.getFascicoloSiusModel() != null) {
//				BigDecimal lIdFascicoloSiep = lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep();
//				if (lIdFascicoloSiep != null) {
//					// Se esiste il Fascicolo SIEP
//
//					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//					// LogF3B.getLogger()
//					siesLogger.debug("ID Fascicolo SIEP ->" + lIdFascicoloSiep);
//
//					// Si cerca la SANZIONE RESIDUA
//					ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
//					SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lIdFascicoloSiep,
//							null);
//
//					if (lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null) {
//						// Se non è stata trovata la Sanzione Residua si ricerca la Sanzione Sostitutiva
//
//						// Pena Complessiva e (al massimo 1 e al massimo 1)
//						IPenaComplessiva lPenComCtr = SIEPLookupRemote.getPenaComplessivaRemote();
//						PenaComplessivaSanzioneSostitutivaModel lPenCompSanzSost = lPenComCtr
//								.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(lIdFascicoloSiep);
//						// Se è stata trovata la Sanzione Sostitutiva si passa nella request
//						if (lPenCompSanzSost != null && lPenCompSanzSost.getSanzioneSostitutiva() != null)
//							setRequestAttribute("sanzione_sostitutiva",
//									lPenCompSanzSost.getSanzioneSostitutiva());
//					} else {
//						// Se è stata trovata la Sanzione Residua si passa nella request
//						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
//						// di LogF3B.getLogger()
//						siesLogger.debug("lSSResiduaModel = " + lSSResiduaModel);
//						setRequestAttribute("sanzione_residua", lSSResiduaModel);
//					}
//				}
//			}
//		}
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("ricercaPenaComplessivaSanzioneSostitutivaByIdFasSiep: fine");
//	}

	/*
	 * La funzione prepara la lista con le Opzioni "Tipo Ufficio Competente" e la passa nella request per
	 * valorizzare la combo corrispondente nella form di inserimento.
	 */
//	private void preparaListaTipoUfficiCompetente() throws Exception {
//
//		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
//		lOption.setFilter(new String[] { "-", "TDS", "UDS", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID", "CSS",
//				"GIPMI", "GIP", "GIPM", "GP", "GUP", "GUPM", "GUPMI", "PT", "PM", "PMM", "PMPT", "PGCAP",
//				"PGMI", "PGMID", "PMI", "TRIBSD", "CAPSM", "TMI", "DIB", "DIBM" }); // solo le Autorità
//																					// Emittenti.
//		setRequestAttribute("tipoUfficioCompetente", "" + lOption);
//	}

}