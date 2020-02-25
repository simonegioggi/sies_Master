package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioComputoCustodiaCautelare
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio dei provvedimenti: Rideterminazione Pena - Computo Misura
 * Cautelere Stesso Reato (presofferto) Rideterminazione Pena - Computo Misura Cautelere Altro Reato
 * (fungibilità)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioComputoCustodiaCautelare extends ActCalcoloPena {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		Vector lAnnMod = new Vector();

		// Recupero l'Id dell'evento
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("");

		// Controllo Esistenza pena residua non validata per quel fascicolo
		/*
		 * REWORK PenaResiduaModel lPenaResMod = new PenaResiduaModel(); IPenaResidua lPenResCtrl =
		 * SIEPLookupRemote.getPenaResiduaRemote(); lPenaResMod =
		 * lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 */
		// REWORK --- E' stato chiamato direttamente il metodo per impossibiità di estendere il dettaglio
		// con la classe ActSIESDettaglioProvvedimento
		ActSIESDettaglioProvvedimento lActDett = new ActSIESDettaglioProvvedimento();
		PenaResiduaModel lPenaResMod = lActDett.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", lPenaResMod);

		// ricerca posizione giuridica
		// se ritorna dall'UpLoad e flagRitorno = 'S' la posizione giuridica è cambiata
		/*
		 * REOWROK PosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); PosizioneGiuridicaModel lPosGiuModificata = new
		 * PosizioneGiuridicaModel(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo
		 * (lFascMod.getIdFascicoloSiep());
		 */

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = lActDett
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lEveMod = " + lEveMod);

		// ===========================================================================
		// Se non sono presenti le Notifiche vuol dire che non ho ancora inserito il
		// provvedimento, ma solo i periodi da computare (annotazioni manuali). In
		// questo caso mi comporto come se l'utente avesse selezionato la voce di
		// menu: Rideterminazione Pena - Computo Misura Cautelare....
		// Carico cioè il dettaglio dell'annotazione e propongo il calcolo o la
		// stampa.
		if (lEveMod.getNotifiche().length == 0
				&& (lEveMod.getEvento().getFlagDocumentoRegistrato() == null || (lEveMod.getEvento()
						.getFlagDocumentoRegistrato() != null && lEveMod.getEvento()
						.getFlagDocumentoRegistrato().equals("N")))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("xxx");
			// String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
			// "=siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniStessoTitolo";
			//
			// return lPage;
		}
		// ===========================================================================

		this.setRequestAttribute("eventonotifica", lEveMod);

		Hashtable lTable = this.ricercaNotificheAM(lEveMod.getNotifiche());
		IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
		try {// ricerco le annotazioni manuali per l'evento
			lAnnMod = lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lEveMod.getEvento().getIdEvento());
		} catch (Exception e) {
		}
		if (lAnnMod.size() > 0)
			this.setRequestAttribute("annotazioneManuale", lAnnMod);

		// Annotazione Manuale GE
		if (!isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE)) {
			BigDecimal lIdAnnotazione = this
					.getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE);
			AnnotazioneManualeModel lAnnManGE = lCtrlAnn.ExRicercaAnnotazioneManualeByKey(lIdAnnotazione);
			this.setRequestAttribute("annotazioneManualeGE", lAnnManGE);
		}

		// Autorità esterna E
		AutoritaEsternaModel lAutE = null;
		String NoteAutE = null;
		if (lTable.get("AutE") != null) {
			lAutE = ((NotificaModel) lTable.get("AutE")).getAutoritaEsterna();
			NoteAutE = ((NotificaModel) lTable.get("AutE")).getNote();
			setRequestAttribute("NoteAutE", NoteAutE);
			setRequestAttribute("autoritaEsternaE", lAutE);
		}

		// Autorità esterna N
		AutoritaEsternaModel lAutN = null;
		String NoteAutN = null;

		if (lTable.get("AutN") != null) {
			lAutN = ((NotificaModel) lTable.get("AutN")).getAutoritaEsterna();
			NoteAutN = ((NotificaModel) lTable.get("AutN")).getNote();
			setRequestAttribute("NoteAutN", NoteAutN);
			setRequestAttribute("autoritaEsternaN", lAutN);
		}

		// Autorità esterna NC
		AutoritaEsternaModel lAutNC = null;
		String NoteAutNC = null;

		if (lTable.get("AutNC") != null) {
			lAutNC = ((NotificaModel) lTable.get("AutNC")).getAutoritaEsterna();
			NoteAutNC = ((NotificaModel) lTable.get("AutNC")).getNote();
			setRequestAttribute("NoteAutNC", NoteAutNC);
			setRequestAttribute("autoritaEsternaNC", lAutNC);
		}
		// Cssa
		String lCssa = null;
		String NoteCssa = null;
		if (lTable.get("NotCssa") != null) {
			lCssa = ((NotificaModel) lTable.get("NotCssa")).getCSSA().getComune() + " "
					+ ((NotificaModel) lTable.get("NotCssa")).getCSSA().getIndirizzo();
			NoteCssa = ((NotificaModel) lTable.get("NotCssa")).getNote();
			setRequestAttribute("NoteCssa", NoteCssa);
			setRequestAttribute("Cssa", lCssa);
			setRequestAttribute("daticssa", ((NotificaModel) lTable.get("NotCssa")).getCSSA());
		}

		// Ufficio UDS
		String UffUDS = null;
		String NoteUDS = null;

		if (lTable.get("UffUDS") != null) {
			UffUDS = ((NotificaModel) lTable.get("UffUDS")).getUfficio().getDescrComune();
			NoteUDS = ((NotificaModel) lTable.get("UffUDS")).getNote();
			setRequestAttribute("UffUDS", UffUDS);
			setRequestAttribute("NoteUDS", NoteUDS);
		}

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);

		/*
		 * Parametro mai usato dall JSP //Avvocato IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		 * Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		 * setRequestAttribute("avvocati", lAvvocati);
		 */

		/*
		 * Veniva ricercata per la seconda volta //Pena Residua IPenaResidua lCtrl =
		 * SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel llPenMod =
		 * lCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 */
		/*
		 * E' inutile! La jso prende l'istituto Detenzione da PosizioneGiuridicaLuogoDetenzioneAltraCausaModel
		 * //ISTITUTO DI DETENZIONE LuogoDetenzioneModel lLuoMod = new LuogoDetenzioneModel();
		 * ILuogoDetenzione lLuoDetCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote(); lLuoMod =
		 * lLuoDetCtrl.ExRicercaLuogoDetByFascicolo(lFascMod.getIdFascicoloSiep());
		 * setRequestAttribute("luogodetenzione", lLuoMod);
		 */
		if (!this.isRequestParameterNullObj("flagPage")) {// Sono nella funzione che produce il provvedimento
			String pagina = this.getRequestStringParameter("flagPage");
			this.setRequestAttribute("flagPage", pagina);
		} else {
			this.setRequestAttribute("ArrivoDaDettaglioProvv", "SI");
		}

		// Gestione Foglio Complementare
		for (int i = 0; i < lEveMod.getNotifiche().length; i++) {
			if (lEveMod.getNotifiche()[i].getCodTipoNotifica().equals("FC")) {
				setRequestAttribute("fogliocomplementare", "1");
				break; // se lo trova esce, altrimenti potrebbe "sporcare" l'attributo nel successivo ciclo
						// del for
			} else {
				setRequestAttribute("fogliocomplementare", "0"); // se non lo trova continua a cercare nel
																	// successivo ciclo del for
			}
		}

		return f3b.web.IWebConstants.ROOT_DIR
				+ "/files/siap/siep/calcolopena/DettaglioComputoCustodiaCautelare.jsp"; // restituisce la jsp
																						// di VIEW
	}

}