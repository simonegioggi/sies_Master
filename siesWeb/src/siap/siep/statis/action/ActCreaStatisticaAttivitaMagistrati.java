package siap.siep.statis.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
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
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.statis.controller.MagistratoFirmatarioController;
import siap.siep.statis.controller.StatisticheMSController;
import siap.siep.statis.model.StatisticheMSModel;

/**
 * MEV_39: aggiunta classe per la creazione del file .xls per le attività dei magistrati
 * <p>
 * Title: ActCreaStatisticaAttivitaMagistrati
 * </p>
 * <p>
 * Description: Classe Action per la creazione delle attività magistrati
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActCreaStatisticaAttivitaMagistrati extends ActionSiap implements ICostantiStatis {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// Lock
		LockModel lm = LockController.lockIfNotLocked(getServletContext(), "STATISTICA_ATTIVITA_MAGISTRATI",
				"1", getCodUtenteConnesso(), getSession().getId());

		if (lm != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// Setto Data Inizio Ricerca e Data Fine Ricerca
		String dataIniziale = getRequestStringParameter("dataIniziale");
		String dataFinale = getRequestStringParameter("dataFinale");
		String ufficioConnesso = getRequestStringParameter("ufficioConnesso");
		String ufficioConnessoDesc = getRequestStringParameter("ufficioConnessoDesc");
		siesLogger.info("DATA INIZIALE: " + dataIniziale);
		siesLogger.info("DATA FINALE: " + dataFinale);
		siesLogger.info("CODICE UFFICIO CONNESSO: " + ufficioConnesso);
		siesLogger.info("UFFICIO CONNESSO: " + ufficioConnessoDesc);

		// Recupero dati della maschera
		String codMagistrato = getRequestStringParameter(CAMPO_LISTA_MAGISTRATI);
		// Magistrato CONTROLLER
		MagistratoFirmatarioController mfc = new MagistratoFirmatarioController();
		// RICERCA PER RIEMPIRE LA COMBOBOX - Trovo i magistrati dell'ufficio SELEZIONATO
		Vector<MagistratoModel> magistrati = mfc.ExRicercaMagistratiFirmatari(ufficioConnesso, dataIniziale,
				dataFinale);
		String cognomeNomeMag = "MAGISTRATO NON ASSEGNATO";
		if ("0".equals(codMagistrato))
			cognomeNomeMag = "TUTTI I MAGISTRATI";
		Iterator<MagistratoModel> i = magistrati.iterator();
		while (i.hasNext()) {
			MagistratoModel mm = i.next();
			if ("-".equals(codMagistrato) || "0".equals(codMagistrato))
				break;
			else if (mm.getCodMagistrato() != null && mm.getCodMagistrato().equals(codMagistrato)) {
				cognomeNomeMag = mm.getCognome() + " " + mm.getNome();
				break;
			}
		}

		// recupero i checkbox selezionati
		String pppm = "", am = "";
		if (!isRequestParameterNullObj("pppm"))
			pppm = getRequestStringParameter("pppm");
		if (!isRequestParameterNullObj("am"))
			am = getRequestStringParameter("am");

		String descrComune = "";
		// Trova la descrizione dell'eventuale ufficio accorpato per le INTESTAZIONI
		UfficioModel um = new UfficioModel();
		IUfficio iUfficio = SICOLookupRemote.getUfficioRemote();
		um = iUfficio.getUfficioByKey(ufficioConnesso);
		descrComune = um.getDescrComune();

		HSSFWorkbook wb = new HSSFWorkbook();

		// 20191122: aggiunto try catch
		try {
			// statistiche per procedimenti pendenti nel periodo
			if (!"".equals(pppm))
				wb = elaboraStatisticaProcPendMag(dataIniziale, dataFinale, ufficioConnesso, cognomeNomeMag,
						codMagistrato, wb, descrComune);

			// statistiche per attività magistrati
			if (!"".equals(am))
				wb = elaboraStatisticaAttMag(dataIniziale, dataFinale, ufficioConnesso, cognomeNomeMag,
						codMagistrato, wb, descrComune);
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
	}

	private HSSFWorkbook elaboraStatisticaAttMag(String dataIniziale, String dataFinale,
			String ufficioConnesso, String cognomeNomeMag, String codMagistrato, HSSFWorkbook wb,
			String descrComune) throws Exception {

		// ISTANZA CONTROLLER
		StatisticheMSController smsc = new StatisticheMSController();
		// RICHIAMO STORED PROCEDURE
		smsc.elaboraStatisticaAttMagStoreProcedure(dataIniziale, dataFinale, ufficioConnesso);
		// RICERCA PER RIEMPIRE FOGLIO EXCEL
		Vector<StatisticheMSModel>[] attivitaMagistratiRiepilogo = smsc
				.ricercaAttivitaMagistratiRiepilogo(dataIniziale, dataFinale);

		Vector tuttiMagsDett = new Vector<>();
		Vector<StatisticheMSModel>[] attivitaMagistratiDettaglio = null;
		Vector tuttiMagsAnno = new Vector<>();
		Vector<StatisticheMSModel>[] motiviMagAnno = null;
		Vector tuttiMagsCognomeNome = new Vector<>();
		if ("0".equals(codMagistrato)) {
			// Magistrato CONTROLLER
			MagistratoFirmatarioController mfc = new MagistratoFirmatarioController();
			// RICERCA PER RIEMPIRE LA COMBOBOX - Trovo i magistrati dell'ufficio SELEZIONATO
			Vector<MagistratoModel> magistrati = mfc.ExRicercaMagistratiFirmatari(ufficioConnesso,
					dataIniziale, dataFinale);
			Iterator<MagistratoModel> i = magistrati.iterator();
			while (i.hasNext()) {
				MagistratoModel mm = i.next();
				if (!Utils.isPresent(mm.getCodMagistrato()))
					continue;
				attivitaMagistratiDettaglio = smsc.ricercaAttivitaMagistratiDettaglio(dataIniziale,
						dataFinale, mm.getCodMagistrato(), mm.getCognome() + " " + mm.getNome());
				tuttiMagsDett.add(attivitaMagistratiDettaglio);
				// ricerca dei totali Cod_Motivo per Magistrato e per Anno (Per dettaglio Magistrato)
				motiviMagAnno = smsc.contaMotiviMagAnno(dataIniziale, dataFinale, mm.getCodMagistrato());
				tuttiMagsAnno.add(motiviMagAnno);
				// if (!"0".equals(mm.getCodMagistrato()))
				tuttiMagsCognomeNome.add(mm.getCognome() + " " + mm.getNome());
				// else
				// tuttiMagsCognomeNome.add(cognomeNomeMag);
			}
		} else {
			attivitaMagistratiDettaglio = smsc.ricercaAttivitaMagistratiDettaglio(dataIniziale, dataFinale,
					codMagistrato, cognomeNomeMag);
			tuttiMagsDett.add(attivitaMagistratiDettaglio);
			// ricerca dei totali Cod_Motivo per Magistrato e per Anno (Per dettaglio Magistrato)
			motiviMagAnno = smsc.contaMotiviMagAnno(dataIniziale, dataFinale, codMagistrato);
			tuttiMagsAnno.add(motiviMagAnno);
			tuttiMagsCognomeNome.add(cognomeNomeMag);
		}

		// ricerca dei totali Cod_Motivo per Anno (per Riepilogo Generale)
		Vector<StatisticheMSModel>[] motiviAnno = smsc.contaMotiviAnno(dataIniziale, dataFinale);
		// Creazione e Scrittura DEI 2 FOGLI del FILE Excel
		smsc.creaStatisticaAttMagRiep(attivitaMagistratiRiepilogo, wb, getUfficioUtenteConnesso(),
				dataIniziale, dataFinale, descrComune, motiviAnno, "RIEPILOGO GENERALE");
		smsc.creaStatisticaAttMagDett(tuttiMagsDett, wb, getUfficioUtenteConnesso(), dataIniziale, dataFinale,
				descrComune, tuttiMagsCognomeNome, tuttiMagsAnno);

		// valore di ritorno
		return wb;
	}

	private HSSFWorkbook elaboraStatisticaProcPendMag(String dataIniziale, String dataFinale,
			String ufficioConnesso, String cognomeNomeMag, String codMagistrato, HSSFWorkbook wb,
			String descrComune) throws Exception {

		StatisticheMSController smsc = new StatisticheMSController();
		siesLogger.debug(dataIniziale + " # " + dataFinale + " # " + ufficioConnesso);

		// RICERCA DALLE TABELLE STATISTICHE per Preparare i File di Input dei fogli xls
		Vector<StatisticheMSModel> primoFoglio = new Vector<>();
		Vector<StatisticheMSModel> secondoFoglio = new Vector<>();
		Vector<StatisticheMSModel> primoFoglioMag = new Vector<>();
		Vector<StatisticheMSModel> secondoFoglioMag = new Vector<>();
		Vector<StatisticheMSModel> terzoFoglioA = new Vector<>();
		Vector<StatisticheMSModel> terzoFoglioB = new Vector<>();
		Vector<StatisticheMSModel> terzoFoglioC = new Vector<>();
		Vector<StatisticheMSModel> terzoFoglioD = new Vector<>();
		Vector<StatisticheMSModel> terzoFoglioE = new Vector<>();

		// instanzio vettori
		Vector tuttiPrimoFoglioMag = new Vector<>();
		Vector tuttiSecondoFoglioMag = new Vector<>();
		Vector tuttiTerzoFoglioA = new Vector<>();
		Vector tuttiTerzoFoglioB = new Vector<>();
		Vector tuttiTerzoFoglioC = new Vector<>();
		Vector tuttiTerzoFoglioD = new Vector<>();
		Vector tuttiTerzoFoglioE = new Vector<>();
		Vector tuttiMagsCognomeNome = new Vector<>();

		primoFoglio = smsc.ricercaRiepilogoProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
				ufficioConnesso, "");
		secondoFoglio = smsc.ricercaRiepilogoPPPTipologiaMisura(dataIniziale, dataFinale, ufficioConnesso,
				"");

		// differenzio per selezione di tutti i magistrati (0) oppure singolo magistrato (ramo else)
		if ("0".equals(codMagistrato)) {
			// Magistrato CONTROLLER
			MagistratoFirmatarioController mfc = new MagistratoFirmatarioController();
			// RICERCA PER RIEMPIRE LA COMBOBOX - Trovo i magistrati dell'ufficio SELEZIONATO
			Vector<MagistratoModel> magistrati = mfc.ExRicercaMagistratiFirmatari(ufficioConnesso,
					dataIniziale, dataFinale);
			Iterator<MagistratoModel> i = magistrati.iterator();
			while (i.hasNext()) {
				MagistratoModel mm = i.next();
				if (!Utils.isPresent(mm.getCodMagistrato()))
					continue;
				primoFoglioMag = smsc.ricercaRiepilogoProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
						ufficioConnesso, mm.getCodMagistrato());
				tuttiPrimoFoglioMag.add(primoFoglioMag);
				secondoFoglioMag = smsc.ricercaRiepilogoPPPTipologiaMisura(dataIniziale, dataFinale,
						ufficioConnesso, mm.getCodMagistrato());
				tuttiSecondoFoglioMag.add(secondoFoglioMag);
				terzoFoglioA = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
						ufficioConnesso, mm.getCodMagistrato(), "A");
				tuttiTerzoFoglioA.add(terzoFoglioA);
				terzoFoglioB = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
						ufficioConnesso, mm.getCodMagistrato(), "B");
				tuttiTerzoFoglioB.add(terzoFoglioB);
				terzoFoglioC = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
						ufficioConnesso, mm.getCodMagistrato(), "C");
				tuttiTerzoFoglioC.add(terzoFoglioC);
				terzoFoglioD = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
						ufficioConnesso, mm.getCodMagistrato(), "D");
				tuttiTerzoFoglioD.add(terzoFoglioD);
				terzoFoglioE = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
						ufficioConnesso, mm.getCodMagistrato(), "E");
				tuttiTerzoFoglioE.add(terzoFoglioE);
				tuttiMagsCognomeNome.add(mm.getCognome() + " " + mm.getNome());
			}
			// Creazione e Scrittura DEGLI n FOGLI del FILE Excel
			if (primoFoglio != null && !primoFoglio.isEmpty() && primoFoglio.size() > 1)
				// Manipolazione Primo Foglio
				manipolaPrimoFoglio(primoFoglio);
			smsc.creaRiepilogoProcedimentiPendentiPeriodo(primoFoglio, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, "");
			smsc.creaRiepilogoPPPTipologiaMisura(secondoFoglio, wb, getUfficioUtenteConnesso(), dataIniziale,
					dataFinale, descrComune, "");
			// Manipolazione Primo Foglio
			// if (primoFoglioMag != null && !primoFoglioMag.isEmpty() && primoFoglioMag.size() > 1) {
			// manipolaPrimoFoglio(primoFoglioMag);
			// smsc.creaRiepilogoProcedimentiPendentiPeriodo(primoFoglioMag, wb, getUfficioUtenteConnesso(),
			// dataIniziale, dataFinale, descrComune, tuttiMagsCognomeNome);
			// }
			Vector tuttiPrimoFoglioMagManip = new Vector<>();
			Vector tuttiMagsCognomeNomeManip = new Vector<>(tuttiMagsCognomeNome);
			for (int k = tuttiPrimoFoglioMag.size() - 1; k >= 0; k--) {
				Vector<StatisticheMSModel> smsm = (Vector<StatisticheMSModel>) tuttiPrimoFoglioMag.get(k);
				if (smsm != null && !smsm.isEmpty() && smsm.size() > 1) {
					manipolaPrimoFoglio(smsm);
					tuttiPrimoFoglioMagManip.add(0, smsm);
				} else
					tuttiMagsCognomeNomeManip.remove(k);
			}

			smsc.creaRiepilogoProcedimentiPendentiPeriodoVett(tuttiPrimoFoglioMagManip, wb,
					getUfficioUtenteConnesso(), dataIniziale, dataFinale, descrComune,
					tuttiMagsCognomeNomeManip);
			smsc.creaRiepilogoPPPTipologiaMisuraVett(tuttiSecondoFoglioMag, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, tuttiMagsCognomeNome);
			smsc.creaDettaglioProcedimentiPendentiPeriodoVett(tuttiTerzoFoglioA, wb,
					getUfficioUtenteConnesso(), dataIniziale, dataFinale, descrComune, "Pendenti Inizio",
					tuttiMagsCognomeNome);
			smsc.creaDettaglioProcedimentiPendentiPeriodoVett(tuttiTerzoFoglioB, wb,
					getUfficioUtenteConnesso(), dataIniziale, dataFinale, descrComune, "Sopravvenuti",
					tuttiMagsCognomeNome);
			smsc.creaDettaglioProcedimentiPendentiPeriodoVett(tuttiTerzoFoglioC, wb,
					getUfficioUtenteConnesso(), dataIniziale, dataFinale, descrComune, "Esauriti",
					tuttiMagsCognomeNome);
			smsc.creaDettaglioProcedimentiPendentiPeriodoVett(tuttiTerzoFoglioD, wb,
					getUfficioUtenteConnesso(), dataIniziale, dataFinale, descrComune, "Riaperti",
					tuttiMagsCognomeNome);
			smsc.creaDettaglioProcedimentiPendentiPeriodoVett(tuttiTerzoFoglioE, wb,
					getUfficioUtenteConnesso(), dataIniziale, dataFinale, descrComune, "Pendenti Fine",
					tuttiMagsCognomeNome);
		} else {
			primoFoglioMag = smsc.ricercaRiepilogoProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
					ufficioConnesso, codMagistrato);
			secondoFoglioMag = smsc.ricercaRiepilogoPPPTipologiaMisura(dataIniziale, dataFinale,
					ufficioConnesso, codMagistrato);
			terzoFoglioA = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
					ufficioConnesso, codMagistrato, "A");
			terzoFoglioB = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
					ufficioConnesso, codMagistrato, "B");
			terzoFoglioC = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
					ufficioConnesso, codMagistrato, "C");
			terzoFoglioD = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
					ufficioConnesso, codMagistrato, "D");
			terzoFoglioE = smsc.ricercaDettaglioProcedimentiPendentiPeriodo(dataIniziale, dataFinale,
					ufficioConnesso, codMagistrato, "E");

			// Creazione e Scrittura DEGLI n FOGLI del FILE Excel
			if (primoFoglio != null && !primoFoglio.isEmpty() && primoFoglio.size() > 1)
				manipolaPrimoFoglio(primoFoglio);
			smsc.creaRiepilogoProcedimentiPendentiPeriodo(primoFoglio, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, "");
			smsc.creaRiepilogoPPPTipologiaMisura(secondoFoglio, wb, getUfficioUtenteConnesso(), dataIniziale,
					dataFinale, descrComune, "");
			if (primoFoglioMag != null && !primoFoglioMag.isEmpty() && primoFoglioMag.size() > 1) {
				manipolaPrimoFoglio(primoFoglioMag);
				smsc.creaRiepilogoProcedimentiPendentiPeriodo(primoFoglioMag, wb, getUfficioUtenteConnesso(),
						dataIniziale, dataFinale, descrComune, cognomeNomeMag);
			}
			smsc.creaRiepilogoPPPTipologiaMisura(secondoFoglioMag, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, cognomeNomeMag);
			smsc.creaDettaglioProcedimentiPendentiPeriodo(terzoFoglioA, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, "Pendenti Inizio", cognomeNomeMag);
			smsc.creaDettaglioProcedimentiPendentiPeriodo(terzoFoglioB, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, "Sopravvenuti", cognomeNomeMag);
			smsc.creaDettaglioProcedimentiPendentiPeriodo(terzoFoglioC, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, "Esauriti", cognomeNomeMag);
			smsc.creaDettaglioProcedimentiPendentiPeriodo(terzoFoglioD, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, "Riaperti", cognomeNomeMag);
			smsc.creaDettaglioProcedimentiPendentiPeriodo(terzoFoglioE, wb, getUfficioUtenteConnesso(),
					dataIniziale, dataFinale, descrComune, "Pendenti Fine", cognomeNomeMag);
		}

		// valore di ritorno
		return wb;
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
}