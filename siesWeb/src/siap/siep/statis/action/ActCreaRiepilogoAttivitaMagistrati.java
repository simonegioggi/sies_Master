package siap.siep.statis.action;

import java.util.Calendar;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.statis.controller.MagistratoFirmatarioController;
import siap.siep.statis.util.StatsUtils;

/**
 * MEV_39: creata nuova classe per gestire statistiche per procedimenti di classe IV (MS)
 * <p>
 * Title: ActCreaRiepilogoAttivitaMagistrati
 * </p>
 * <p>
 * Description: Classe Action per la creazione dei report per attività magistrati
 * </p>
 */
public class ActCreaRiepilogoAttivitaMagistrati extends ActionSiap implements ICostantiStatis {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Lock
		LockModel lm = LockController.lockIfNotLocked(getServletContext(),
				"ESTRAZIONE_RIEPILOGO_ATTIVITA_MAGISTRATI", "1", getCodUtenteConnesso(),
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

		if (!isRequestParameterNullObj("Semestre") && "".compareTo(annoSemestre) != 0)
			semestre = getRequestStringParameters("Semestre");

		if (!isRequestParameterNullObj("Trimestre") && "".compareTo(annoTrimestre) != 0)
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

		if ("-".equals(codiceUffAccorpato))
			codiceUffAccorpato = getCodUfficioUtenteConnesso();

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
		siesLogger.info("TIPO DI RICERCA: " + tipo);
		siesLogger.info("ANNO SEMESTRE: " + annosemestre);
		siesLogger.info("ANNO TRIMESTRE: " + annotrimestre);
		siesLogger.info("UFFICIO CONNESSO: " + codiceUffAccorpato);

		// parametri data Inizio e Data Fine
		dataIniziale = giornoIniziale + "/" + meseIniziale + "/" + annoIniziale;
		dataFinale = giornoFinale + "/" + meseFinale + "/" + annoFinale;
		ggmmIniziali = giornoIniziale + meseIniziale;
		ggmmFinali = giornoFinale + meseFinale;
		tipo = "datadata";

		// parametri Solo ANNO
		if (soloAnnoIniziale.compareTo("") != 0 && soloAnnoFinale.compareTo("") != 0) {
			dataIniziale = "01/01/" + soloAnnoIniziale;
			ggmmIniziali = "0101";
			dataFinale = StatsUtils.calcolaDataFinale(soloAnnoFinale);
			ggmmFinali = StatsUtils.calcolaGiorniMesiFinali(soloAnnoFinale);
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

		// controllo max 5 anni --> 20191021 [SG]: aumentato a 7 anni
		int differenza = 0;
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(DateUtils.getDate(dataIniziale, "dd/MM/yyyy"));
		calendar2.setTime(DateUtils.getDate(dataFinale, "dd/MM/yyyy"));
		differenza = calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR);
		if (calendar1.get(Calendar.MONTH) > calendar2.get(Calendar.MONTH)
				|| (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
						&& calendar1.get(Calendar.DATE) > calendar2.get(Calendar.DATE)))
			differenza--;
		if ((differenza > 6)/* || ((saf - sai) > 6) */) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"La statistica non può essere eseguita per un range di anni superiore a 7!");
			return IWebConstants.PG_MESSAGE;
		}

		String ufficioConnessoDesc = "-";
		if (!"-".equals(getRequestStringParameter(CAMPO_COD_ACCORPATO_1))) {
			UfficioModel um = new UfficioModel();
			um = iUfficio.getUfficioByKey(codiceUffAccorpato);
			ufficioConnessoDesc = um.getDescrComune();
		}

		// Magistrato CONTROLLER
		MagistratoFirmatarioController mfc = new MagistratoFirmatarioController();
		// RICERCA PER RIEMPIRE LA COMBOBOX - Trovo i magistrati dell'ufficio SELEZIONATO
		Vector magistrati = mfc.ExRicercaMagistratiFirmatari(codiceUffAccorpato, dataIniziale, dataFinale);
		setRequestAttribute("magistrati", magistrati);

		setRequestAttribute("dataIniziale", dataIniziale);
		setRequestAttribute("dataFinale", dataFinale);
		setRequestAttribute("ufficioConnesso", codiceUffAccorpato);
		setRequestAttribute("ufficioConnessoDesc", ufficioConnessoDesc);

		// valore di ritorno
		return PG_LOAD_ATTIVITA_MAGISTRATI;
	} // CHIUDE processRequest()

} // CHIUDE CLASSE ActCreaRiepilogoAttivitaMagistrati