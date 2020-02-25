package siap.sius.statistiche.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.sico.web.ActionSiap;
import siap.siep.statis.controller.StatisController;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.RicercaOrdinanzaModel;
import siap.sius.statistiche.model.RicercaProvvedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActStampaElencoProcInExcel
 * </p>
 * <p>
 * Description: Attiva la Ricerca dei Procedimenti per stamparne l'elenco in un foglio excel.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Eunics
 * </p>
 * 
 * @author Luigi
 * @version 2.0
 */
public class ActStampaElencoProcInExcel extends ActionSiap {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	String lCriterio1 = "";
	String lCriterio2 = "";
	String lCriterio3 = "";
	String mCriterio4 = "";

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// Controller per la Ricerca
		HSSFWorkbook wb = new HSSFWorkbook();

		// parametro che indica il tipo di ricerca da effettuare
		String modalitaRicerca = "";
		if (!isRequestParameterNullObj("modalitaRicerca")) {
			modalitaRicerca = getRequestStringParameter("modalitaRicerca");
		}

		// MEV10s-3: aggiunto controllo a seconda del model
		RicercaProvvedimentoModel lRicercaProvvModel = null;
		RicercaOrdinanzaModel lRicercaOrdModel = null;
		if (getSessionAttribute("ProvvRicercaModel") instanceof RicercaProvvedimentoModel) {
			lRicercaProvvModel = new RicercaProvvedimentoModel();
			lRicercaProvvModel = (RicercaProvvedimentoModel) getSessionAttribute("ProvvRicercaModel");
		} else {
			lRicercaOrdModel = new RicercaOrdinanzaModel();
			lRicercaOrdModel = (RicercaOrdinanzaModel) getSessionAttribute("ProvvRicercaModel");
		}

		IStatisticheSius lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();

		// MEV10s-3: aggiunto controllo a seconda del model
		// Creazione del file excel
		// String titolo = creaTitoloFoglio(lRicercaModel);
		StatisController lStatisCtrl = new StatisController();
		Vector mProvv;
		if (getSessionAttribute("ProvvRicercaModel") instanceof RicercaProvvedimentoModel) {
			mProvv = lCtrlStatSius.ExRicercaProcSiusXProvvedimentiPaginata(lRicercaProvvModel, -1);
			if (lRicercaProvvModel.isRicercaXFoglioComplementare() && !modalitaRicerca.equals("")) {
				lStatisCtrl.creaFoglioElencoFogliComplementari(wb, getUfficioUtenteConnesso(), lCriterio1,
						lCriterio2, lCriterio3, mProvv);
			} else {
				if (lRicercaProvvModel.getModalitaRicerca().compareTo(
						ICostantiStatistiche.RICERCA_IMPUGNAZIONE) == 0) {
					lStatisCtrl.creaFoglioElencoRicorsiImpugnazioni(wb, getUfficioUtenteConnesso(),
							lCriterio1, lCriterio2, lCriterio3, mProvv);
				} else if (lRicercaProvvModel.getModalitaRicerca().compareTo(
						ICostantiStatistiche.RICERCA_FOGLIO_COMPLEMENTARE) == 0) {
					wb = lStatisCtrl.creaFoglioFoglioComplementare(getUfficioUtenteConnesso(),
							lRicercaProvvModel);
				} else {
					lStatisCtrl.creaFoglioElencoProvvedimenti(wb, lRicercaProvvModel.getModalitaRicerca(),
							getUfficioUtenteConnesso(), lCriterio1, lCriterio2, lCriterio3, mCriterio4,
							mProvv);
				}
			}
		} else {
			mProvv = lCtrlStatSius.ExRicercaProcSiusXProvvedimentiPaginata(lRicercaOrdModel, -1);
			if (lRicercaOrdModel.isRicercaXFoglioComplementare() && !modalitaRicerca.equals("")) {
				lStatisCtrl.creaFoglioElencoFogliComplementari(wb, getUfficioUtenteConnesso(), lCriterio1,
						lCriterio2, lCriterio3, mProvv);
			} else {
				if (lRicercaOrdModel.getModalitaRicerca()
						.compareTo(ICostantiStatistiche.RICERCA_IMPUGNAZIONE) == 0) {
					lStatisCtrl.creaFoglioElencoRicorsiImpugnazioni(wb, getUfficioUtenteConnesso(),
							lCriterio1, lCriterio2, lCriterio3, mProvv);
				} else if (lRicercaOrdModel.getModalitaRicerca().compareTo(
						ICostantiStatistiche.RICERCA_FOGLIO_COMPLEMENTARE) == 0) {
					wb = lStatisCtrl.creaFoglioFoglioComplementare(getUfficioUtenteConnesso(),
							lRicercaOrdModel);
				} else {
					lStatisCtrl.creaFoglioElencoProvvedimenti(wb, lRicercaOrdModel.getModalitaRicerca(),
							getUfficioUtenteConnesso(), lCriterio1, lCriterio2, lCriterio3, mCriterio4,
							mProvv);
				}
			}
		}

		if (mProvv != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Risultati ricerca completa : " + mProvv.size());

		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();

		// Generazione file xls
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException("ActStampaElencoProcInExcel.processRequest: " + ioe);
		}

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);
		// setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.INLINE_DISPOSITION_FILE );

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

//	private String creaTitoloFoglio(RicercaOrdinanzaModel aFiltroRicerca) throws F3BException {
//		String lTitolo = new String();
//
//		if (aFiltroRicerca.isTipoIntervalloRicercaXEstremiProvvedimento()) {
//			String lNumIniziale = aFiltroRicerca.getAnnoIniziale().toString() + "/"
//					+ aFiltroRicerca.getNumIniziale().toString();
//			String lNumFinale = aFiltroRicerca.getAnnoFinale().toString() + "/"
//					+ aFiltroRicerca.getNumFinale().toString();
//			lCriterio1 = "Dal N. " + lNumIniziale + " al " + lNumFinale;
//		} else if (aFiltroRicerca.isRicercaXDateDeposito()) {
//			String lDataIniziale = " "
//					+ StringUtils
//							.toStringJSP(DateUtils.getDateToString(aFiltroRicerca.getDataDepositoIniziale(),
//									"dd-MM-yyyy"), "-");
//			String lDataFinale = " "
//					+ StringUtils.toStringJSP(
//							DateUtils.getDateToString(aFiltroRicerca.getDataDepositoFinale(), "dd-MM-yyyy"),
//							"-");
//			lCriterio1 = "Data di deposito tra  " + lDataIniziale + " e   " + lDataFinale;
//		} else if (aFiltroRicerca.isRicercaXDateArrivoCancelleria()) {
//			String lDataIniziale = " "
//					+ StringUtils.toStringJSP(DateUtils.getDateToString(
//							aFiltroRicerca.getDataArrivoInCancelleriaIniziale(), "dd-MM-yyyy"), "-");
//			String lDataFinale = " "
//					+ StringUtils.toStringJSP(DateUtils.getDateToString(
//							aFiltroRicerca.getDataArrivoInCancelleriaFinale(), "dd-MM-yyyy"), "-");
//			lCriterio1 = " Data di arrivo in cancelleria tra  " + lDataIniziale + " e   " + lDataFinale;
//		} else if (aFiltroRicerca.isRicercaXDateEmissione()) {
//			String lDataIniziale = " "
//					+ StringUtils.toStringJSP(DateUtils.getDateToString(
//							aFiltroRicerca.getDataEmissioneIniziale(), "dd-MM-yyyy"), "-");
//			String lDataFinale = " "
//					+ StringUtils.toStringJSP(
//							DateUtils.getDateToString(aFiltroRicerca.getDataEmissioneFinale(), "dd-MM-yyyy"),
//							"-");
//			lCriterio1 = "Data di emissione tra  " + lDataIniziale + " e   " + lDataFinale;
//		}
//
//		if (aFiltroRicerca.getStatoValidazione() != null) {
//			if (aFiltroRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.ANNULLATI))
//				lCriterio2 = "Annullato";
//			else if (aFiltroRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.VALIDATI))
//				lCriterio2 = "Validato";
//			else if (aFiltroRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.TUTTI))
//				lCriterio2 = "Tutti";
//			else if (aFiltroRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_VALIDATI))
//				lCriterio2 = "Non Validato";
//			else if (aFiltroRicerca.getStatoValidazione()
//					.equalsIgnoreCase(ICostantiStatistiche.NON_ANNULLATI))
//				lCriterio2 = "Non Annullato";
//			lCriterio2 = "Stato: " + lCriterio2;
//		}
//
//		if (aFiltroRicerca.getCodMagistrato().compareToIgnoreCase("Tutti") == 0) {
//			lCriterio3 = "Tutti i procedimenti con magistrato assegnato";
//		} else if (aFiltroRicerca.getCodMagistrato().compareToIgnoreCase("Nessuno") == 0) {
//			lCriterio3 = "Tutti i procedimenti privi di Magistrato Assegnato";
//		} else if ((aFiltroRicerca.getCodMagistrato().length() > 0)
//				&& (aFiltroRicerca.getCodMagistrato().compareToIgnoreCase("-") != 0)) {
//			String lMagistrato = " ";
//			IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
//			MagistratoModel lMagModel = lMagCtrl.ExRicercaMagistratoByCod(aFiltroRicerca.getCodMagistrato());
//
//			if (lMagModel != null)
//				lMagistrato = lMagModel.getCognome() + " " + lMagModel.getNome();
//
//			lCriterio3 = "Magistrato: " + lMagistrato;
//		} else if ((aFiltroRicerca.getCodEsperto() != null)
//				&& (aFiltroRicerca.getCodEsperto().intValue() != 9999)) {
//			String lEsperto = " ";
//			IEsperto lEspCtrl = SIUSLookupRemote.getEspertoRemote();
//			EspertoModel lEspModel = lEspCtrl.ExRicercaEspertoByKey(aFiltroRicerca.getCodEsperto());
//
//			if (lEspModel != null)
//				lEsperto = lEspModel.getCognome() + " " + lEspModel.getNome();
//			lCriterio3 = "Esperto: " + lEsperto;
//		} else if ((aFiltroRicerca.getCodEsperto() != null)
//				&& (aFiltroRicerca.getCodEsperto().intValue() == 9999)) {
//			lCriterio3 = "Tutti i procedimenti con esperto assegnato";
//		}
//
//		if (aFiltroRicerca.getTipiControlliEsecuzione().length > 1
//				&& aFiltroRicerca.getTipiControlliEsecuzione()[0] != null
//				&& aFiltroRicerca.getTipiControlliEsecuzione()[1] != null) {
//			mCriterio4 = "Ordinanze con Controllo tramite mezzi elettronici e Controllo tramite altri strumenti tecnici ";
//		} else if (aFiltroRicerca.getTipiControlliEsecuzione().length == 1) {
//			if (aFiltroRicerca.getTipiControlliEsecuzione()[0].equals("E"))
//				mCriterio4 = "Solo ordinanze con Controllo tramite mezzi elettronici.";
//			else if (aFiltroRicerca.getTipiControlliEsecuzione()[0].equals("T"))
//				mCriterio4 = "Solo ordinanze con Controllo tramite altri strumenti tecnici.";
//		}
//
//		lTitolo = lCriterio1 + " -- " + lCriterio2 + " -- " + lCriterio3 + " -- " + mCriterio4;
//
//		return lTitolo;
//	}

}