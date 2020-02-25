package siap.siep.statis.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.statis.controller.MagistratoFirmatarioController;
import siap.siep.statis.controller.StatisController;

/**
 * <p>
 * Title: ActCreaAttivitaMagistrati
 * </p>
 * <p>
 * Description: Classe Action per la creazione delle attività magistrati
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class ActCreaAttivitaMagistrati extends ActionSiap implements ICostantiStatis {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "ATTIVITA_MAGISTRATI", "1",
				getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// Recupero dati della maschera
		String lCodMag = getRequestStringParameter(CAMPO_LISTA_MAGISTRATI);

		int annoIni = getRequestIntParameter(CAMPO_ANNO_INIZIALE);
		String meseIni = getRequestStringParameter(CAMPO_MESE_INIZIALE);
		String giornoIni = getRequestStringParameter(CAMPO_GIORNO_INIZIALE);

		int annoFin = getRequestIntParameter(CAMPO_ANNO_FINALE);
		String meseFin = getRequestStringParameter(CAMPO_MESE_FINALE);
		String giornoFin = getRequestStringParameter(CAMPO_GIORNO_FINALE);

		// NGG - CodAcco è il Cod Ufficio SELEZIONATO
		String lCodAcco = getRequestStringParameter(CAMPO_COD_ACCORPATO_1);
		// Recupero Ufficio Utente
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSession().getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lUffUtente = lUtenteMod.getUfficioUtente().getCodUfficio();
		String lTipoUffUte = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		// recupero lista uffici accorpati
		IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote();
		Vector lListaUffici = lCtrlUff.ListaUfficiAccorpati(lTipoUffUte, lUffUtente);

		UfficioModel UffMod = new UfficioModel();
		UffMod.setUfficiAccorpati(lListaUffici);
		UffMod.setCodTipoUfficio(lTipoUffUte);
		UffMod.setCodUfficio(lUffUtente);

		// recupero i codoci Uff Accorpati -
		// ---------------> AMBROS NGG - Caso in cui si vuole la statistica estendibile a tutti gli uffici
		// ACCORPATI
		/*
		 * String Accorpato1 = "-"; String Accorpato2 = "-"; String Accorpato3 = "-"; String
		 * UfficioConnesso="";
		 *
		 * Vector<String> lVec = new Vector<String>(); int i=0; if(lListaUffici.size()==0) {
		 *
		 * } else { Iterator itx = lListaUffici.iterator(); while ( itx.hasNext()) { UfficioAccorpatoModel
		 * lUff = (UfficioAccorpatoModel)itx.next(); String uff1 = lUff.getCodUfficio(); lVec.add(i, uff1);
		 * i++; }
		 *
		 * }
		 *
		 * if(lCodAcco.equals("-")) { UfficioConnesso = this.getCodUfficioUtenteConnesso(); if(i==1) {
		 * Accorpato1 = lVec.get(0); } if(i==2) { Accorpato1 = lVec.get(0); Accorpato2 = lVec.get(1); }
		 * if(i==3) { Accorpato1 = lVec.get(0); Accorpato2 = lVec.get(1); Accorpato3 = lVec.get(2); } } else {
		 * UfficioConnesso = lCodAcco; }
		 */
		// END NGG.1

		// ---------> AMBROS NGG - Caso in cui si vuole la statistica per un solo ufficio (O SOLO Accorpato ,
		// o SOLO Accorpante)

		String CodUfficioConnesso = "";
		if (lCodAcco.equals("-"))
			CodUfficioConnesso = this.getCodUfficioUtenteConnesso();
		else {
			Iterator itx1 = lListaUffici.iterator();
			while (itx1.hasNext()) {
				UfficioAccorpatoModel lUff1 = (UfficioAccorpatoModel) itx1.next();
				if (lUff1.getDescrizione().equals(lCodAcco)) {
					CodUfficioConnesso = lUff1.getCodUfficio();
				}
			}
		}

		// END NGG

		// ISTANZA CONTROLLER
		StatisController lCtrl = new StatisController();

		String dataIni = giornoIni + "/" + meseIni + "/" + annoIni;
		String dataFin = giornoFin + "/" + meseFin + "/" + annoFin;

		// RICHIAMO STORED PROCEDURE
		// ---------------> AMBROS NGG - Caso in cui si vuole la statistica estendibile a tutti gli uffici
		// ACCORPATI
		// lCtrl.ExAttivitaMagistratiStoredProcedure(dataIni, dataFin, UfficioConnesso, Accorpato1,
		// Accorpato2, Accorpato3);
		// ---------> AMBROS NGG - Caso in cui si vuole la statistica per un solo ufficio (O SOLO Accorpato ,
		// o SOLO Accorpante)
		lCtrl.ExAttivitaMagistratiStoredProcedure(dataIni, dataFin, CodUfficioConnesso);

		// Recupero dati per riepilogo generale
		// Si prepara Tutte le tipologie attività Con CODICE e DESCIZIONE, ANNO per ANNO
		Vector lAttMag = lCtrl.ExRiepilogoGeneraleAttivita(annoIni, annoFin);

		// Ricerca per caricare i model dei magistrati
		// MagistratoController lCtrlMag = new MagistratoController();
		Vector lMagModVect = new Vector();
		if (lCodMag.equals("0")) {
			MagistratoFirmatarioController lCtrlMagistrato = new MagistratoFirmatarioController();
			lMagModVect = lCtrlMagistrato.ExRicercaMagistratiFirmatari(CodUfficioConnesso, dataIni, dataFin);

			// ----Modificata la Query---- lMagModVect =
			// lCtrlMag.ExRicercaMagistratoByCodUfficio(this.getCodUfficioUtenteConnesso());
		} else {
			// NGG --> cambio query- non pià su MAGISTRATO, MA SU W-MAGISTRATO
			// MagistratoModel magMod = lCtrlMag.ExRicercaMagistratoByCod(lCodMag);
			MagistratoFirmatarioController lCtrlMagistrato = new MagistratoFirmatarioController();
			MagistratoModel magMod = lCtrlMagistrato.ExRicercaW_MagistratoByCod(lCodMag);
			lMagModVect.add(magMod);
		}

		// Estrazione dei dati aggregati per Anno e Tipologia per ogni magistrato
		Iterator itx = lMagModVect.iterator();
		while (itx.hasNext()) {
			MagistratoModel magMod = (MagistratoModel) itx.next();
			lCtrl.ExAttivitaMagistrato(annoIni, annoFin, magMod, lAttMag);
		}

		// ----> NGG Statistiche SIEP - Trova la descrizione dell'eventuale ufficio accorpato per le
		// INTESTAZIONI

		IUfficio lCtrlu = SICOLookupRemote.getUfficioRemote();
		UfficioModel ufMod = new UfficioModel();
		ufMod = lCtrlu.getUfficioByKey(CodUfficioConnesso);
		String DescUffIntesta = ufMod.getDescrComune();

		// --Ricerca dei totali Cod_Motivo per Anno (per Riepilogo Generale)
		Vector VMotiviAnno = lCtrl.ExTotaleMotiviAnno(annoIni, annoFin);
		// -- Ricerca dei totali Cod_Motivo per Magistrato e per Anno (Per dettaglio Magistrato)
		Vector VMotiviMagAnno = lCtrl.ExTotaleMotiviMagAnno(annoIni, annoFin);

		// ----> END NGG
		HSSFWorkbook wb = new HSSFWorkbook();

		// Scrittura del file excel (Riepilogo Generale / Dettaglio)
		lCtrl.ExCreateAttivitaMagistrati(lAttMag, wb, this.getUfficioUtenteConnesso(), dataIni, dataFin,
				DescUffIntesta, VMotiviAnno, VMotiviMagAnno);

		if (!lCodMag.equals("0")) {
			// -- Ricerca dei procedimenti ordinati per foglio elenco
			Vector VElencoMotiviMagAnno = lCtrl.ExElencoMotiviMagAnno(lCodMag);

			siesLogger.debug("--XXXXXXXXXX-- >>>>>>>>>>>>>>>>>>>>>> - VElencoMotiviMagAnno size = "
					+ VElencoMotiviMagAnno.size());

			// Scrittura del foglio xls (Elenco Procedimenti) per il magistrato
			// selezionato
			lCtrl.ExCreateAttivitaMagistratiElenco(wb, this.getUfficioUtenteConnesso(), dataIni, dataFin,
					DescUffIntesta, VElencoMotiviMagAnno, (MagistratoModel) lMagModVect.elementAt(0));
		}

		// Generazione file xls
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException("ActCreaAttivitaMagistrati.processRequest: " + ioe);
		}

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}