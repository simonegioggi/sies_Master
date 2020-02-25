package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioFungibilita
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio: Rideterminazione Pena - Computo Pena Detentiva Espiata
 * per Altro reato (fungibilità)
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
public class ActDettaglioFungibilita extends ActCalcoloPena {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ==========================================================================
		// Controllo Esistenza pena residua non validata per quel fascicolo
		// ==========================================================================
		// PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		/*PenaResiduaModel lPenaResMod = */lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod
				.getIdFascicoloSiep());

		// ==========================================================================
		// Ricerca posizione giuridica
		// ==========================================================================
		// se ritorna dall'UpLoad e flagRitorno = 'S' la posizione giuridica è cambiata
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ==========================================================================
		// ricerca evento notifica
		// ==========================================================================
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveMod);

		Hashtable lTable = this.ricercaNotificheAM(lEveMod.getNotifiche());

		Vector lAnnMod = new Vector();
		IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
		try {
			lAnnMod = lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lEveMod.getEvento().getIdEvento());
		} catch (Exception e) {
		}

		if (lAnnMod.size() > 0) {
			this.setRequestAttribute("annotazioneManuale", lAnnMod);
		}

		// Annotazione Manuale GE
		if (!isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE)) {
			BigDecimal lIdAnnotazione = this
					.getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE);
			AnnotazioneManualeModel lAnnManGE = lCtrlAnn.ExRicercaAnnotazioneManualeByKey(lIdAnnotazione);
			this.setRequestAttribute("annotazioneManualeGE", lAnnManGE);
		}

		// ==========================================================================
		//
		// ==========================================================================
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

		// Ufficio
		UfficioModel lUffModel = null;
		String NoteUff = null;

		if (lTable.get("Ufficio") != null) {
			lUffModel = ((NotificaModel) lTable.get("Ufficio")).getUfficio();
			NoteUff = ((NotificaModel) lTable.get("Ufficio")).getNote();

			setRequestAttribute("NoteUfficio", NoteUff);
			setRequestAttribute("autoritaUfficio", lUffModel);
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

		/************************************* ricerca evento 06 ************************************************************/
		EventoModel lEveModelEve = new EventoModel();
		lEveModelEve.setCodTipoEvento("01");
		lEveModelEve.setCodTipoProvvedimento("06"); // Ordine esecuzione
		lEveModelEve.setFlagDocumentoRegistrato("S");
		try {
			lEveModelEve.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			Vector eventi = lCtrlEvento.ExRicercaEvento(lEveModelEve);

			EventoModel lEve = (EventoModel) eventi.get(0);
			this.setRequestAttribute("evento06", lEve);
		} catch (Exception ex) {
		}
		/**************************************************************************************/

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);

		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// Pena Residua
		IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel llPenMod = lCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod
				.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", llPenMod);

		// if(lPosizione.getCodPosizioneGiuridica().equals("03")||lPosizione.getCodPosizioneGiuridica().equals("14"))
		// ISTITUTO DI DETENZIONE
		LuogoDetenzioneModel lLuoMod = new LuogoDetenzioneModel();
		ILuogoDetenzione lLuoDetCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
		lLuoMod = lLuoDetCtrl.ExRicercaLuogoDetByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("luogodetenzione", lLuoMod);

		if (!this.isRequestParameterNullObj("flagPage")) {
			String pagina = this.getRequestStringParameter("flagPage");
			this.setRequestAttribute("flagPage", pagina);
		} else {
			setRequestAttribute("dettaglioPM", "SI"); // Arrivo dal dettaglio del PM
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

		String lPage = new String(f3b.web.IWebConstants.ROOT_DIR
				+ "/files/siap/siep/calcolopena/DettaglioFungibilita.jsp");

		return lPage; // restituisce la jsp di VIEW
	}

}