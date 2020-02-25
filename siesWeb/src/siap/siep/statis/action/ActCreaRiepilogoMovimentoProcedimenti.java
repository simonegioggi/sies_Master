package siap.siep.statis.action;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.statis.controller.StatisticheMSController;
import siap.siep.statis.model.StatoFascicoloResModel;
import siap.siep.statis.util.StatsUtils;

/**
 * MEV_39: creata nuova classe per gestire statistiche per procedimenti di classe IV (MS)
 * <p>
 * Title: ActCreaRiepilogoMovimentoProcedimenti
 * </p>
 * <p>
 * Description: Classe Action per la creazione dei report per riepilogo movimento procedimenti
 * </p>
 */
public class ActCreaRiepilogoMovimentoProcedimenti extends ActionSiap implements ICostantiStatis {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// pagina di ritorno
		String paginaRitorno = PG_LOAD_MOVIMENTO_PROCEDIMENTI;

		// Lock
		LockModel lm = LockController.lockIfNotLocked(getServletContext(),
				"ESTRAZIONE_RIEPILOGO_MOVIMENTO_PROCEDIMENTI", "1", getCodUtenteConnesso(),
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
		// Recupero Ufficio Utente
		UtenteModel utm = new UtenteModel(
				(UtenteModel) getSession().getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String codUfficio = utm.getUfficioUtente().getCodUfficio();
		String codTipoUfficio = utm.getUfficioUtente().getCodTipoUfficio();

		IUfficio iUfficio = SICOLookupRemote.getUfficioRemote();
		Vector listaUfficiAccorpati = iUfficio.ListaUfficiAccorpati(codTipoUfficio, codUfficio);

		UfficioModel ufm = new UfficioModel();
		ufm.setUfficiAccorpati(listaUfficiAccorpati);
		ufm.setCodTipoUfficio(codTipoUfficio);
		ufm.setCodUfficio(codUfficio);
		ufm.setDescrComune(utm.getUfficioUtente().getDescrComune());

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
					numeroTrimestreSemestre += "1";
				}

				if (semestre[i].compareTo("2") == 0) {
					if (numeroTrimestreSemestre.compareTo("") == 0) {
						dataIniziale = "01/07/" + annoSemestre;
						ggmmIniziali = "0107";
						numeroTrimestreSemestre += "2";
					} else {
						numeroTrimestreSemestre += ",2";
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
						numeroTrimestreSemestre += ",2";
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
						numeroTrimestreSemestre += ",3";
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
						numeroTrimestreSemestre += ",4";
					}
					dataFinale = StatsUtils.calcolaDataFinale(annoTrimestre);
					ggmmFinali = StatsUtils.calcolaGiorniMesiFinali(annoTrimestre);
				}
			}
			tipo = "trimestre";
		}

		// RECUPERO SYSDATE
		String dataVerifica = DateUtils.getSysDate("dd/MM/yyyy");

		String descrComune = "";
		// Trova la descrizione dell'eventuale ufficio accorpato per le INTESTAZIONI
		UfficioModel um = new UfficioModel();
		um = iUfficio.getUfficioByKey(ufficioConnesso);
		descrComune = um.getDescrComune();
		siesLogger.info(descrComune);
		siesLogger.info(dataIniziale);
		siesLogger.info(dataFinale);

		// 20191021 [SG]: data fittizia
		setRequestAttribute("dataIniziale", dataIniziale.equals("01/01/2000") ? "" : dataIniziale);
		setRequestAttribute("dataFinale", dataFinale);
		setRequestAttribute("ufficioModel", um);
		setRequestAttribute("listaUfficiAccorpati", listaUfficiAccorpati);
		setRequestAttribute("ufficioConnesso", codiceUffAccorpato);

		String accorpato1 = "";
		String accorpato2 = "";
		String accorpato3 = "";

		Vector<String> v = new Vector<>();
		int i = 0;
		if (listaUfficiAccorpati.size() == 0) {
			siesLogger.info("LISTA UFFICI ACCORPATI VUOTA!");
		} else {
			Iterator itx = listaUfficiAccorpati.iterator();
			while (itx.hasNext()) {
				UfficioAccorpatoModel uam = (UfficioAccorpatoModel) itx.next();
				String s = uam.getCodUfficio();
				v.add(i, s);
				i++;
			}
		}

		if (!"-".equals(codiceUffAccorpato)) {
			ufficioConnesso = getCodUfficioUtenteConnesso();
			if (i == 1) {
				accorpato1 = v.get(0);
			}
			if (i == 2) {
				accorpato1 = v.get(0);
				accorpato2 = v.get(1);
			}
			if (i == 3) {
				accorpato1 = v.get(0);
				accorpato2 = v.get(1);
				accorpato3 = v.get(2);
			}
		} else
			ufficioConnesso = getCodUfficioUtenteConnesso();

		// ISTANZA CONTROLLER
		StatisticheMSController smsc = new StatisticheMSController();
		try {
			// RICHIAMO STORED PROCEDURE
			smsc.ricercaStatiFascicoloStoredProcedure(ufficioConnesso, accorpato1, accorpato2, accorpato3,
					dataVerifica, dataIniziale, dataFinale);
		} catch (Exception e) {
			siesLogger.info("ERRORE in ricercaStatiFascicoloStoredProcedure! Parametri di passaggio: "
					+ ufficioConnesso + " # " + accorpato1 + " # " + accorpato2 + " # " + accorpato3 + " # "
					+ dataVerifica + " # " + dataIniziale + " # " + dataFinale);
			siesLogger.info(e.getMessage());
		}

		// 20191122: aggiunto try catch
		try {
			// RICERCA PER RIEMPIRE LA COMBOBOX
			Vector<StatoFascicoloResModel> statiFascicolo = smsc.ricercaStatiFascicoloResMS();
			setRequestAttribute("statiFascicolo", statiFascicolo);
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

		// pagina di ritorno
		return paginaRitorno;
	} // CHIUDE processRequest()

} // CHIUDE CLASSE ActCreaRiepilogoMovimentoProcedimenti