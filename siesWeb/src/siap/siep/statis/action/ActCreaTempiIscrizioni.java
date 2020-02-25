package siap.siep.statis.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.statis.controller.StatisController;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActCreaTempiIscrizioni
 * </p>
 * <p>
 * Description: Classe Action per la creazione dei report per Tempi Iscrizioni
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
@SuppressWarnings("rawtypes")
public class ActCreaTempiIscrizioni extends ActionSiap implements ICostantiStatis {

	public String processRequest() throws F3BException {

		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "TEMPI_ISCRIZIONE_FASCICOLI", "1",
				getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// Recupero dati della maschera
		int lCodDist = getRequestIntParameter(CAMPO_LISTA_DISTINTE);
		int lCodInter = getRequestIntParameter(CAMPO_LISTA_INTERVALLI);

		int annoIni = getRequestIntParameter(CAMPO_ANNO_INIZIALE);
		String meseIni = getRequestStringParameter(CAMPO_MESE_INIZIALE);
		String giornoIni = getRequestStringParameter(CAMPO_GIORNO_INIZIALE);

		int annoFin = getRequestIntParameter(CAMPO_ANNO_FINALE);
		String meseFin = getRequestStringParameter(CAMPO_MESE_FINALE);
		String giornoFin = getRequestStringParameter(CAMPO_GIORNO_FINALE);

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

		// ------------------------------------------
		// ----------> AMBROS NGG - Caso in cui si vuole la statistica estendibile a tutti gli uffici
		// ACCORPATI
		// recupero i codoci Uff Accorpati
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
		// ---------------------------------------------------------------
		// ---------> AMBROS NGG - Caso in cui si vuole la statistica per un solo ufficio (O SOLO Accorpato ,
		// o SOLO Accorpante)

		String Accorpato = "";
		String UfficioConnesso = "";
		if (lCodAcco.equals("-")) {
			UfficioConnesso = this.getCodUfficioUtenteConnesso();
			Accorpato = "NO";
		} else
			UfficioConnesso = lCodAcco;

		// END NGG

		StatisController lCtrl = new StatisController();

		String dataIni = giornoIni + "/" + meseIni + "/" + annoIni;
		String dataFin = giornoFin + "/" + meseFin + "/" + annoFin;

		// RICHIAMO STORED PROCEDURE
		// ---------------> AMBROS NGG - Caso in cui si vuole la statistica estendibile a tutti gli uffici
		// ACCORPATI
		// lCtrl.ExTempiIscrizioneStoredProcedure(dataIni, dataFin, UfficioConnesso, Accorpato1, Accorpato2,
		// Accorpato3 );
		// -----------------------------
		// ---------> AMBROS NGG - Caso in cui si vuole la statistica per un solo ufficio (O SOLO Accorpato ,
		// o SOLO Accorpante)
		lCtrl.ExTempiIscrizioneStoredProcedure(dataIni, dataFin, UfficioConnesso);

		HSSFWorkbook wb = new HSSFWorkbook();

		Vector lTempi = null;
		Vector<Vector> lRiepilogo = new Vector<Vector>(); // Vettore di vettori

		// ==========================================================================
		// Recupero dati per riepilogo generale per le tre 'Tipologia di Distinta'
		// pre creare il Foglio Riepilogo.
		// n.b. tale foglio contiene tutti i "tipi di Distinta" e gli "intervelli di
		// tempo" indipendentemente da ciò che è stato selezionato, è il foglio
		// di dettaglio che applica il filtro
		// ==========================================================================
		for (int tipo = 1; tipo < 4; tipo++) {
			// Ciclo per ogni tipo
			lTempi = lCtrl.ExRicercaRiepilogoGeneraleTempi(annoIni, annoFin, tipo);

			// Aggiungo il vector dei tempi al vector del riepilogo generale
			lRiepilogo.add(lTempi);
		}

		// NGG Statistiche SIEP - Trova la descrizione dell'eventuale ufficio accorpato per le INTESTAZIONI
		IUfficio lCtrlu = SICOLookupRemote.getUfficioRemote();
		UfficioModel ufMod = new UfficioModel();
		ufMod = lCtrlu.getUfficioByKey(UfficioConnesso);
		String DescUffIntesta = ufMod.getDescrComune();
		//

		// Creazione del Foglio "Riepilogo" del file Excel
		lCtrl.ExCreateRiepilogoTempi(lRiepilogo, wb, this.getUfficioUtenteConnesso(), dataIni, dataFin,
				DescUffIntesta);

		// ==========================================================================
		// A seconda del tipo di distinta richiamo il metodo di ricerca appropriato
		// ==========================================================================
		if (lCodDist == 2) { // Da GIUDICATO a RICEZIONE
			lTempi = lCtrl.ExRicercaDettaglioTempiRicezione(lCodInter);
		} else {
			lTempi = lCtrl.ExRicercaDettaglioTempiIscrizione(lCodDist, lCodInter);
		}

		// Creazione del Foglio "Dettaglio" del file Excel in funzione della
		// selezione "Tipo Distinta" e "Intervallo Tempo"
		lCtrl.ExCreateDettaglioTempi(lTempi, wb, this.getUfficioUtenteConnesso(), dataIni, dataFin, lCodDist,
				lCodInter, Accorpato, DescUffIntesta);

		// Generazione file xls
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException("ActCreaTempiIscrizioni.processRequest: " + ioe);
		}

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}