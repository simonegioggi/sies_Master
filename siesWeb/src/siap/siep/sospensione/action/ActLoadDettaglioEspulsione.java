package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.Hashtable;

import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuraalternativa.action.ActMisuraAlternativa;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;

/**
 * <p>
 * Title: ActLoadDettaglioEspulsione
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Avvenuta Espulsione
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

public class ActLoadDettaglioEspulsione extends ActMisuraAlternativa implements ICostantiSospensione {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("eventonotifica", lEveMod);

		// ricerca verbale
		IVerbale lCtrlVer = SIEPLookupRemote.getVerbaleRemote();
		VerbaleModel lVerMod = lCtrlVer
				.ExRicercaVerbaleByCodTipoIdEvento(lEveMod.getEvento().getEveIdEvento(), "05");
		setRequestAttribute("verbale", lVerMod);

		// pena residua calcolata ed inserita
		PenaResiduaModel lPenMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("nuovapenaresidua", lPenMod);

		// ricerca sospensione
		ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
		SospensioneModel lSospMod = lCtrlSosp.ExRicercaSospensioneByIdPenaResidua(lPenMod.getIdPenaResidua());
		setRequestAttribute("sospensione", lSospMod);

		/*
		 * Si deve cercare la pena precedente a quella appena inserita. Oppure nel caso di dettaglio da elenco
		 * si deve far riferimento alla pena precedente all'evento corrente!!
		 */
		PenaResiduaModel lPenValidata = this.getPenaResiduaPrecedenteValidata(lIdEvento,
				lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", lPenValidata);

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);

		// ISTITUTO DI DETENZIONE
		LuogoDetenzioneModel lLuoMod = new LuogoDetenzioneModel();
		ILuogoDetenzione lLuoDetCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
		lLuoMod = lLuoDetCtrl.ExRicercaLuogoDetByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("luogodetenzione", lLuoMod);

		// notifiche
		if (lEveMod != null && lEveMod.getEvento() != null && lEveMod.getEvento().getEveIdEvento() != null) {
			Hashtable lTable = this.ricercaNotifiche(lEveMod.getNotifiche());

			// Autorità esterna
			AutoritaEsternaModel lAutE = null;
			String NoteAutE = null;
			if (lTable.get("AutE") != null) {
				lAutE = ((NotificaModel) lTable.get("AutE")).getAutoritaEsterna();
				NoteAutE = ((NotificaModel) lTable.get("AutE")).getNote();
				setRequestAttribute("NoteAutE", NoteAutE);
				setRequestAttribute("autoritaEsternaE", lAutE);
			}

			// Ufficio TDS
			String UffTDS = null;
			if (lTable.get("UffTDS") != null) {
				UffTDS = ((NotificaModel) lTable.get("UffTDS")).getUfficio().getDescrComune();
				setRequestAttribute("UffTDS", UffTDS);
			}

			// Ufficio UDS
			String UffUDS = null;
			if (lTable.get("UffUDS") != null) {
				UffUDS = ((NotificaModel) lTable.get("UffUDS")).getUfficio().getDescrComune();
				setRequestAttribute("UffUDS", UffUDS);
			}

			// Ufficio URC
			UfficioModel UffURC = null;
			String NoteSA = null;
			if (lTable.get("UffURC") != null) {
				UffURC = ((NotificaModel) lTable.get("UffURC")).getUfficio();
				NoteSA = ((NotificaModel) lTable.get("UffURC")).getNote();
				setRequestAttribute("UffURC", UffURC);
				setRequestAttribute("NoteSA", NoteSA);
			}
		}

		return PG_LOAD_DETTAGLIOSOSPENSIONE_ESPULSIONE;
	}
}