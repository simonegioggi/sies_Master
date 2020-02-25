package siap.siep.statis.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
import siap.siep.statis.controller.StatisControllerCPP;
import siap.siep.statis.model.DettaglioIscrizioniAttivitaCPPModel;
import siap.siep.statis.model.RiepilogoIscrizioniAttivitaCPPModel;

/**
 * <p>
 * Title: ActCreaRiepilogoIscrizioniAttivitaCPP
 * </p>
 * <p>
 * Description: Classe Action per la creazione dei report per Iscrizioni Fascicoli CPP
 * </p>
 */
public class ActCreaRiepilogoIscrizioniAttivitaCPP extends ActionSiap implements ICostantiStatis {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "ESTRAZIONE_RIEPILOGO_ATTIVITA",
				"1", getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// Recupero dati della maschera
		int annoIni = getRequestIntParameter(CAMPO_ANNO_INIZIALE);
		String meseIni = getRequestStringParameter(CAMPO_MESE_INIZIALE);
		String giornoIni = getRequestStringParameter(CAMPO_GIORNO_INIZIALE);

		int annoFin = getRequestIntParameter(CAMPO_ANNO_FINALE);
		String meseFin = getRequestStringParameter(CAMPO_MESE_FINALE);
		String giornoFin = getRequestStringParameter(CAMPO_GIORNO_FINALE);

		String soloAnnoIni = getRequestStringParameter(CAMPO_SOLO_ANNO_INIZIALE);
		String soloAnnoFin = getRequestStringParameter(CAMPO_SOLO_ANNO_FINALE);

		String AnnoSem = getRequestStringParameter(CAMPO_ANNO_SEMESTRE);
		String AnnoTri = getRequestStringParameter(CAMPO_ANNO_TRIMESTRE);

		String[] Semestre = { "" };
		String[] Trimestre = { "" };

		if (!isRequestParameterNullObj("Semestre") && AnnoSem.compareTo("") != 0) {
			Semestre = getRequestStringParameters("Semestre");
		}

		if (!isRequestParameterNullObj("Trimestre") && AnnoTri.compareTo("") != 0) {
			Trimestre = getRequestStringParameters("Trimestre");
		}

		// NGG
		String lCodAcco = getRequestStringParameter(CAMPO_COD_ACCORPATO_1);
		// Recupero Ufficio Utente
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSession().getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lUffUtente = lUtenteMod.getUfficioUtente().getCodUfficio();
		String lTipoUffUte = lUtenteMod.getUfficioUtente().getCodTipoUfficio();

		IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote();
		Vector lListaUffici = lCtrlUff.ListaUfficiAccorpati(lTipoUffUte, lUffUtente);

		UfficioModel UffMod = new UfficioModel();
		UffMod.setUfficiAccorpati(lListaUffici);
		UffMod.setCodTipoUfficio(lTipoUffUte);
		UffMod.setCodUfficio(lUffUtente);

		String UfficioConnesso = "";
		if (lCodAcco.equals("-")) {
			UfficioConnesso = getCodUfficioUtenteConnesso();
		} else
			UfficioConnesso = lCodAcco;

		// Setto Data Inizio Ricerca e Data Fine Ricerca

		String dataIni = "";
		String dataFin = "";
		String ggmmIni = "";
		String ggmmFin = "";
		String Tipo = "";
		int annosemestre = 0;
		int annotrimestre = 0;
		String Quale_Trime_Seme = "";

		// parametri data Inizio e Data Fine
		dataIni = giornoIni + "/" + meseIni + "/" + annoIni;
		dataFin = giornoFin + "/" + meseFin + "/" + annoFin;
		ggmmIni = giornoIni + meseIni;
		ggmmFin = giornoFin + meseFin;
		Tipo = "datadata";

		// parametri solo ANNO
		if (soloAnnoIni.compareTo("") != 0 && soloAnnoFin.compareTo("") != 0) {
			dataIni = "01/01/" + soloAnnoIni;
			dataFin = "31/12/" + soloAnnoFin;
			ggmmIni = "0101";
			ggmmFin = "3112";
			Tipo = "annoanno";
		}

		// parametri solo ANNO Semestre
		if (AnnoSem.compareTo("") != 0 && Semestre.length > 0) {
			for (int i = 0; i < Semestre.length; i++) {
				if (Semestre[i].compareTo("PRIMO") == 0 || Semestre[i].compareTo("1") == 0) {
					dataIni = "01/01/" + AnnoSem;
					dataFin = "30/06/" + AnnoSem;
					ggmmIni = "0101";
					ggmmFin = "3006";
					Quale_Trime_Seme += "PRIMO";
				}

				if (Semestre[i].compareTo("SECONDO") == 0 || Semestre[i].compareTo("2") == 0) {
					if (Quale_Trime_Seme.compareTo("") == 0) {
						dataIni = "01/07/" + AnnoSem;
						ggmmIni = "0107";
						Quale_Trime_Seme += "SECONDO ";
					} else {
						Quale_Trime_Seme += ", SECONDO ";
					}
					dataFin = "31/12/" + AnnoSem;
					ggmmFin = "3112";
				}
			}
			Tipo = "semestre";
		}

		// parametri solo ANNO Trimestre
		if (AnnoTri.compareTo("") != 0 && Trimestre.length > 0) {
			for (int i = 0; i < Trimestre.length; i++) {
				if (Trimestre[i].compareTo("PRIMO") == 0 || Trimestre[i].compareTo("1") == 0) {
					dataIni = "01/01/" + AnnoTri;
					dataFin = "31/03/" + AnnoTri;
					ggmmIni = "0101";
					ggmmFin = "3103";
					Quale_Trime_Seme += "PRIMO";
				}

				if (Trimestre[i].compareTo("SECONDO") == 0 || Trimestre[i].compareTo("2") == 0) {
					if (Quale_Trime_Seme.compareTo("") == 0) {
						dataIni = "01/04/" + AnnoTri;
						ggmmIni = "0104";
						Quale_Trime_Seme += "SECONDO";
					} else {
						Quale_Trime_Seme += ", SECONDO ";
					}
					dataFin = "30/06/" + AnnoTri;
					ggmmFin = "3006";
				}

				if (Trimestre[i].compareTo("TERZO") == 0 || Trimestre[i].compareTo("3") == 0) {
					if (Quale_Trime_Seme.compareTo("") == 0) {
						dataIni = "01/07/" + AnnoTri;
						ggmmIni = "0107";
						Quale_Trime_Seme += "TERZO";
					} else {
						Quale_Trime_Seme += ", TERZO";
					}
					dataFin = "30/09/" + AnnoTri;
					ggmmFin = "3009";
				}

				if (Trimestre[i].compareTo("QUARTO") == 0 || Trimestre[i].compareTo("4") == 0) {
					if (Quale_Trime_Seme.compareTo("") == 0) {
						dataIni = "01/10/" + AnnoTri;
						ggmmIni = "0110";
						Quale_Trime_Seme += "QUARTO";
					} else {
						Quale_Trime_Seme += ", QUARTO";
					}
					dataFin = "31/12/" + AnnoTri;
					ggmmFin = "3112";
				}
			}
			Tipo = "trimestre";
		}

		String DescUffIntesta = "";
		// NGG Statistiche SIEP - Trova la descrizione dell'eventuale ufficio accorpato per le INTESTAZIONI
		IUfficio lCtrlu = SICOLookupRemote.getUfficioRemote();
		UfficioModel ufMod = new UfficioModel();
		ufMod = lCtrlu.getUfficioByKey(UfficioConnesso);
		DescUffIntesta = ufMod.getDescrComune();

		// ------------------------------------
		// RICHIAMO STORED-PROCEDURE per popolare TABELLE STATISTICHE
		StatisControllerCPP lCtrl = new StatisControllerCPP();
		lCtrl.ExRiepilogoIscrizioniCPPStoredProcedure(dataIni, dataFin, UfficioConnesso);

		// ----------------------------------------------
		// RICERCA DALLE TABELLE STATISTICHE per Preparare i File di Input dei fogli xls
		Vector<RiepilogoIscrizioniAttivitaCPPModel> lRiepilogoIscr = new Vector<>();
		Vector<DettaglioIscrizioniAttivitaCPPModel> lDettaglioElenchi = new Vector<>();

		if (Tipo.compareTo("datadata") == 0) {
			lRiepilogoIscr = lCtrl.ExRicercaRiepilogoGeneraleIscrizioniAttivita(annoIni, annoFin, ggmmIni,
					ggmmFin);
			lDettaglioElenchi = lCtrl.ExRicercaDettagliAttivitaCPP(annoIni, annoFin, ggmmIni, ggmmFin);
		} else if (Tipo.compareTo("annoanno") == 0) {
			annoIni = Integer.parseInt(soloAnnoIni);
			annoFin = Integer.parseInt(soloAnnoFin);
			lRiepilogoIscr = lCtrl.ExRicercaRiepilogoGeneraleIscrizioniAttivita(annoIni, annoFin, ggmmIni,
					ggmmFin);
			lDettaglioElenchi = lCtrl.ExRicercaDettagliAttivitaCPP(annoIni, annoFin, ggmmIni, ggmmFin);

		} else if (Tipo.compareTo("semestre") == 0) {
			annosemestre = Integer.parseInt(AnnoSem);
			lRiepilogoIscr = lCtrl.ExRicercaRiepilogoGeneraleIscrizioniAttivitaPerMese(annosemestre, ggmmIni,
					ggmmFin, Tipo, Quale_Trime_Seme);
			lDettaglioElenchi = lCtrl.ExRicercaDettagliAttivitaCPPPerMese(annosemestre, ggmmIni, ggmmFin,
					Tipo, Quale_Trime_Seme);
		} else if (Tipo.compareTo("trimestre") == 0) {
			annotrimestre = Integer.parseInt(AnnoTri);
			lRiepilogoIscr = lCtrl.ExRicercaRiepilogoGeneraleIscrizioniAttivitaPerMese(annotrimestre, ggmmIni,
					ggmmFin, Tipo, Quale_Trime_Seme);
			lDettaglioElenchi = lCtrl.ExRicercaDettagliAttivitaCPPPerMese(annotrimestre, ggmmIni, ggmmFin,
					Tipo, Quale_Trime_Seme);
		}

		// Creazione e Scrittura DEI FOGLI 'RIEPILOGO' ed 'ELENCHI DETTAGLIO' del FILE Excel
		HSSFWorkbook wb = new HSSFWorkbook();
		if (Tipo.compareTo("datadata") == 0 || Tipo.compareTo("annoanno") == 0) {
			lCtrl.ExCreateRiepilogoIscrizioni_Attivita(lRiepilogoIscr, wb, getUfficioUtenteConnesso(),
					dataIni, dataFin, DescUffIntesta);
			lCtrl.ExCreateElencoDettaglioIscrizioni_Attivita(lDettaglioElenchi, wb,
					getUfficioUtenteConnesso(), dataIni, dataFin, DescUffIntesta);
		} else if (Tipo.compareTo("semestre") == 0 || Tipo.compareTo("trimestre") == 0) {
			lCtrl.ExCreateRiepilogoIscrizioni_Attivita_PerMese(lRiepilogoIscr, wb, getUfficioUtenteConnesso(),
					dataIni, dataFin, DescUffIntesta, Tipo, Quale_Trime_Seme);
			lCtrl.ExCreateElencoDettaglioIscrizioni_Attivita_PerMese(lDettaglioElenchi, wb,
					getUfficioUtenteConnesso(), dataIni, dataFin, DescUffIntesta, Tipo, Quale_Trime_Seme);
		}

		// Generazione del file xls e Produzione dell' OUTPUT
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException("ActCreaRiepilogoIscrizioniAttivitaCPP.processRequest: " + ioe);
		}

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);
		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	} // CHIUDE processRequest()

} // CHIUDE CLASSE ActCreaRiepilogoIscrizioniAttivitaCPP()