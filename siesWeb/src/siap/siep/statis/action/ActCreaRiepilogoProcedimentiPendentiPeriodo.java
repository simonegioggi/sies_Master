package siap.siep.statis.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.statis.controller.StatisticheMSController;
import siap.siep.statis.model.StatisticheMSModel;
import siap.siep.statis.util.StatsUtils;

/**
 * MEV_39: creata nuova classe per gestire statistiche per procedimenti di classe IV (MS)
 * <p>
 * Title: ActCreaRiepilogoProcedimentiPendentiPeriodo
 * </p>
 * <p>
 * Description: Classe Action per la creazione dei report per Iscrizioni e Tipologia Misura
 * </p>
 */
public class ActCreaRiepilogoProcedimentiPendentiPeriodo extends ActionSiap implements ICostantiStatis {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// Lock
		LockModel lm = LockController.lockIfNotLocked(getServletContext(),
				"ESTRAZIONE_RIEPILOGO_PROCEDIMENTI_PENDENTI_PERIODO", "1", getCodUtenteConnesso(),
				getSession().getId());

		if (lm != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti!<BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		// Recupero dati della maschera
		int annoIniziale = getRequestIntParameter(CAMPO_ANNO_INIZIALE);
		String meseIniziale = getRequestStringParameter(CAMPO_MESE_INIZIALE);
		String giornoIniziale = getRequestStringParameter(CAMPO_GIORNO_INIZIALE);

		int annoFinale = getRequestIntParameter(CAMPO_ANNO_FINALE);
		String meseFinale = getRequestStringParameter(CAMPO_MESE_FINALE);
		String giornoFinale = getRequestStringParameter(CAMPO_GIORNO_FINALE);

		String soloAnnoIniziale = getRequestStringParameter(CAMPO_SOLO_ANNO_INIZIALE);
		String soloAnnoFinale = getRequestStringParameter(CAMPO_SOLO_ANNO_FINALE);

		String annoSemestre = getRequestStringParameter(CAMPO_ANNO_SEMESTRE);
		String annoTrimestre = getRequestStringParameter(CAMPO_ANNO_TRIMESTRE);

		String[] semestre = { "" };
		String[] trimestre = { "" };

		if (!isRequestParameterNullObj("Semestre") && annoSemestre.compareTo("") != 0)
			semestre = getRequestStringParameters("Semestre");

		if (!isRequestParameterNullObj("Trimestre") && annoTrimestre.compareTo("") != 0)
			trimestre = getRequestStringParameters("Trimestre");

		String codiceUffAccorpato = getRequestStringParameter(CAMPO_COD_ACCORPATO_1);
		String ufficioConnesso = "";
		if ("-".equals(codiceUffAccorpato))
			ufficioConnesso = getCodUfficioUtenteConnesso();
		else
			ufficioConnesso = codiceUffAccorpato;

		// Setto Data Inizio Ricerca e Data Fine Ricerca
		String dataIniziale = "";
		String dataFinale = "";
		String ggmmIniziali = "";
		String ggmmFinali = "";
		String tipo = "";
		int annosemestre = 0;
		int annotrimestre = 0;
		String numeroTrimestreSemestre = "";
		siesLogger.info("GIORNI E MESI INIZIALI: " + ggmmIniziali);
		siesLogger.info("GIORNI E MESI FINALI: " + ggmmFinali);
		siesLogger.info("TIPOLOGIA RICERCA: " + tipo);
		siesLogger.info("ANNO SEMESTRE: " + annosemestre);
		siesLogger.info("ANNO TRIMESTRE: " + annotrimestre);

		// parametri data Inizio e Data Fine
		dataIniziale = giornoIniziale + "/" + meseIniziale + "/" + annoIniziale;
		// 20191021 [SG]: aggiunta la possibilita' di avere solo data finale
		if (dataIniziale.equals("//0"))
			dataIniziale = "01/01/2000"; // DATA FITTIZIA
		dataFinale = giornoFinale + "/" + meseFinale + "/" + annoFinale;
		ggmmIniziali = giornoIniziale + meseIniziale;
		ggmmFinali = giornoFinale + meseFinale;
		tipo = "datadata";

		// parametri Solo ANNO
		// 20191021 [SG]: aggiunta la possibilita' di avere solo anno finale
		if (/* soloAnnoIniziale.compareTo("") != 0 && */soloAnnoFinale.compareTo("") != 0) {
			if (soloAnnoIniziale.compareTo("") == 0)
				dataIniziale = "01/01/2000"; // DATA FITTIZIA
			else
				dataIniziale = "01/01/" + soloAnnoIniziale;
			dataFinale = StatsUtils.calcolaDataFinale(soloAnnoFinale);
			ggmmFinali = StatsUtils.calcolaGiorniMesiFinali(soloAnnoFinale);
			ggmmIniziali = "0101";
			tipo = "annoanno";
		}

		// parametri solo ANNO semestre
		if (annoSemestre.compareTo("") != 0 && semestre.length > 0) {
			for (int i = 0; i < semestre.length; i++) {
				if (semestre[i].compareTo("1") == 0) {
					dataIniziale = "01/01/" + annoSemestre;
					dataFinale = "30/06/" + annoSemestre;
					ggmmIniziali = "0101";
					ggmmFinali = "3006";
					numeroTrimestreSemestre += "1, 2";
				}

				if (semestre[i].compareTo("2") == 0) {
					if (numeroTrimestreSemestre.compareTo("") == 0) {
						dataIniziale = "01/07/" + annoSemestre;
						ggmmIniziali = "0107";
						numeroTrimestreSemestre += "3, 4";
					} else {
						numeroTrimestreSemestre += ", 3, 4";
					}
					dataFinale = StatsUtils.calcolaDataFinale(annoSemestre);
					ggmmFinali = StatsUtils.calcolaGiorniMesiFinali(annoSemestre);
				}
			}
			tipo = "semestre";
		}

		// parametri solo ANNO trimestre
		if (annoTrimestre.compareTo("") != 0 && trimestre.length > 0) {
			for (int i = 0; i < trimestre.length; i++) {
				if (trimestre[i].compareTo("1") == 0) {
					dataIniziale = "01/01/" + annoTrimestre;
					dataFinale = "31/03/" + annoTrimestre;
					ggmmIniziali = "0101";
					ggmmFinali = "3103";
					numeroTrimestreSemestre += "1";
				}

				if (trimestre[i].compareTo("2") == 0) {
					if (numeroTrimestreSemestre.compareTo("") == 0) {
						dataIniziale = "01/04/" + annoTrimestre;
						ggmmIniziali = "0104";
						numeroTrimestreSemestre += "2";
					} else {
						numeroTrimestreSemestre += ", 2";
					}
					dataFinale = "30/06/" + annoTrimestre;
					ggmmFinali = "3006";
				}

				if (trimestre[i].compareTo("3") == 0) {
					if (numeroTrimestreSemestre.compareTo("") == 0) {
						dataIniziale = "01/07/" + annoTrimestre;
						ggmmIniziali = "0107";
						numeroTrimestreSemestre += "3";
					} else {
						numeroTrimestreSemestre += ", 3";
					}
					dataFinale = "30/09/" + annoTrimestre;
					ggmmFinali = "3009";
				}

				if (trimestre[i].compareTo("4") == 0) {
					if (numeroTrimestreSemestre.compareTo("") == 0) {
						dataIniziale = "01/10/" + annoTrimestre;
						ggmmIniziali = "0110";
						numeroTrimestreSemestre += "4";
					} else {
						numeroTrimestreSemestre += ", 4";
					}
					dataFinale = StatsUtils.calcolaDataFinale(annoTrimestre);
					ggmmFinali = StatsUtils.calcolaGiorniMesiFinali(annoTrimestre);
				}
			}
			tipo = "trimestre";
		}

		siesLogger.info("TIPOLOGIA DI RICERCA: " + tipo);
		siesLogger.info("GIORNI E MESI INIZIALI: " + ggmmIniziali);
		siesLogger.info("GIORNI E MESI FINALI: " + ggmmFinali);
		siesLogger.info("ANNO SEMESTRE: " + annosemestre);
		siesLogger.info("ANNO TRIMESTRE: " + annotrimestre);

		// 20191122: aggiunto try catch
		try {
			// valore di ritorno
			return elaboraStatistica(dataIniziale, dataFinale, ufficioConnesso);
		} catch (Exception e) {
			// info per il log
			siesLogger.error(e.getMessage() + " ### " + e);
			if (e instanceof DAOException || e instanceof SQLException)
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Errore nell'elaborazione dei dati presenti nel database!");
			else if (e.getMessage().contains("65536") || e.getMessage().contains("65535"))
				// risultato della ricerca supera 65536 record
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Attenzione! Ridurre i parametri di ricerca, in quanto il risultato non è interamente visualizzabile.");
			else
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Errore nell'elaborazione della Statistica!");
			// valore di ritorno
			return IWebConstants.PG_MESSAGE;
		}
	} // CHIUDE processRequest()

	private String elaboraStatistica(String dataIniziale, String dataFinale, String ufficioConnesso)
			throws Exception {

		String descrComune = "";
		// Trova la descrizione dell'eventuale ufficio accorpato per le INTESTAZIONI
		UfficioModel um = new UfficioModel();
		IUfficio iUfficio = SICOLookupRemote.getUfficioRemote();
		um = iUfficio.getUfficioByKey(ufficioConnesso);
		descrComune = um.getDescrComune();

		StatisticheMSController smsc = new StatisticheMSController();
		siesLogger.debug(dataIniziale + " # " + dataFinale + " # " + ufficioConnesso);

		// RICERCA DALLE TABELLE STATISTICHE per Preparare i File di Input dei fogli xls
		Vector<StatisticheMSModel> primoFoglio = new Vector<>();
		Vector<StatisticheMSModel> secondoFoglio = new Vector<>();
		Vector<StatisticheMSModel> terzoFoglioA = new Vector<>();
		Vector<StatisticheMSModel> terzoFoglioB = new Vector<>();
		Vector<StatisticheMSModel> terzoFoglioC = new Vector<>();
		Vector<StatisticheMSModel> terzoFoglioD = new Vector<>();
		Vector<StatisticheMSModel> terzoFoglioE = new Vector<>();

		primoFoglio = smsc.ricercaRiepilogoProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
				ufficioConnesso, "");
		secondoFoglio = smsc.ricercaRiepilogoPPPTipologiaMisura(dataIniziale, dataFinale, ufficioConnesso,
				"");
		terzoFoglioA = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
				ufficioConnesso, "", "A");
		terzoFoglioB = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
				ufficioConnesso, "", "B");
		terzoFoglioC = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
				ufficioConnesso, "", "C");
		terzoFoglioD = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
				ufficioConnesso, "", "D");
		terzoFoglioE = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
				ufficioConnesso, "", "E");

		if ((!Utils.isNullObj(primoFoglio) && !primoFoglio.isEmpty())
				|| (!Utils.isNullObj(secondoFoglio) && !secondoFoglio.isEmpty())
				|| (!Utils.isNullObj(terzoFoglioA) && !terzoFoglioA.isEmpty())
				|| (!Utils.isNullObj(terzoFoglioB) && !terzoFoglioB.isEmpty())
				|| (!Utils.isNullObj(terzoFoglioC) && !terzoFoglioC.isEmpty())
				|| (!Utils.isNullObj(terzoFoglioD) && !terzoFoglioD.isEmpty())
				|| (!Utils.isNullObj(terzoFoglioE) && !terzoFoglioE.isEmpty())) {
			// Creazione e Scrittura DEGLI n FOGLI del FILE Excel
			// 20191021 [SG]: data fittizia
			dataIniziale = dataIniziale.equals("01/01/2000") ? "" : dataIniziale;
			HSSFWorkbook wb = new HSSFWorkbook();
			if (primoFoglio != null && !primoFoglio.isEmpty() && primoFoglio.size() > 1)
				manipolaPrimoFoglio(primoFoglio);
			smsc.creaRiepilogoProcedimentiPendentiPeriodo(primoFoglio, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, "");
			smsc.creaRiepilogoPPPTipologiaMisura(secondoFoglio, wb, getUfficioUtenteConnesso(), dataIniziale,
					dataFinale, descrComune, "");
			smsc.creaDettaglioProcedimentiPendentiPeriodo(terzoFoglioA, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, "Pendenti Inizio", "");
			smsc.creaDettaglioProcedimentiPendentiPeriodo(terzoFoglioB, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, "Sopravvenuti", "");
			smsc.creaDettaglioProcedimentiPendentiPeriodo(terzoFoglioC, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, "Esauriti", "");
			smsc.creaDettaglioProcedimentiPendentiPeriodo(terzoFoglioD, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, "Riaperti", "");
			smsc.creaDettaglioProcedimentiPendentiPeriodo(terzoFoglioE, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, "Pendenti Fine", "");

			// Generazione del file xls e Produzione dell' OUTPUT
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				wb.write(baos);
			} catch (IOException ioe) {
				throw new F3BException("ActCreaRiepilogoProcedimentiPendentiPeriodo.processRequest: " + ioe);
			}

			setRequestAttribute("report", baos);
			setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);
			return IWebConstants.PG_DOWNLOAD_DOCUMENT;
		} else {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "La statistica non ha prodotto alcun risultato!");
			return IWebConstants.PG_MESSAGE;
		}
	}

	// Metodo per modificare il dato dell'elaborazione (fine periodo anno x deve essere uguale ad inizio
	// periodo anno x+1)
	private void manipolaPrimoFoglio(Vector<StatisticheMSModel> primoFoglio) {

		int x = 0;
		StatisticheMSModel ppip = primoFoglio.get(0);
		for (int i = 0; i < primoFoglio.size(); i++) {
			StatisticheMSModel smsm = primoFoglio.get(i);
			if ("Procedimenti pendenti fine periodo".equals(smsm.getTipoMS()))
				x = i;
		}
		StatisticheMSModel ppfp = primoFoglio.get(x);
		for (int i = 0; i < ppip.getAnni().size() - 1; i++)
			ppip.getIscrittiParziali().set(i + 1, ppfp.getIscrittiParziali().get(i));
	}

} // CHIUDE CLASSE ActCreaRiepilogoProcedimentiPendentiPeriodo