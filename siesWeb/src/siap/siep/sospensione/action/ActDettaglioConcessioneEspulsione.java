package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.Hashtable;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuraalternativa.action.ActMisuraAlternativa;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioConcessioneEspulsione
 * </p>
 * <p>
 * Description: Classe Action per la load DETTAGLIO di concessione espulsione
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

public class ActDettaglioConcessioneEspulsione extends ActMisuraAlternativa implements ICostantiSospensione {
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

		BigDecimal lIdEventoDecreto = null;
		if (lEveMod != null && lEveMod.getEvento() != null && lEveMod.getEvento().getEveIdEvento() != null) {
			lIdEventoDecreto = lEveMod.getEvento().getEveIdEvento();
		} else {
			lIdEventoDecreto = lIdEvento;
		}

		// ricerca misura alternativa per id evento
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lDecreto = new MisuraAlternativaModel();
		lDecreto = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdEventoDecreto);
		setRequestAttribute("misuraalternativa", lDecreto);

		// ricerca dell'ufficio emittente
		if (lDecreto != null && lDecreto.getChiaveUfficioFascicoloSius() != null) {
			IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUffMod = new UfficioModel();
			lUffMod = lCtrlUffEmi.getUfficioByKey(lDecreto.getChiaveUfficioFascicoloSius());
			setRequestAttribute("UfficioEmittente", lUffMod);
		}

		// notifiche
		if (lEveMod != null && lEveMod.getEvento() != null && lEveMod.getEvento().getEveIdEvento() != null) {
			Hashtable lTable = this.ricercaNotifiche(lEveMod.getNotifiche());
			// istituto
			IstitutoDetenzioneModel lIstMod = null;
			if (lTable.get("lNotIstituto") != null) {
				lIstMod = ((NotificaModel) lTable.get("lNotIstituto")).getIstitutoDetenzione();
				setRequestAttribute("lIstMod", lIstMod);
			}

			// Autorità esterna PREPOSTA AL CONTROLLO
			AutoritaEsternaModel lAutPC = null;
			String NoteAutPC = null;
			if (lTable.get("AutPC") != null) {
				lAutPC = ((NotificaModel) lTable.get("AutPC")).getAutoritaEsterna();
				NoteAutPC = ((NotificaModel) lTable.get("AutPC")).getNote();
				setRequestAttribute("NoteAutPC", NoteAutPC);
				setRequestAttribute("autoritaEsternaPC", lAutPC);
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

			// Autorità esterna C
			AutoritaEsternaModel lAutC = null;
			String NoteAutC = null;

			if (lTable.get("AutC") != null) {
				lAutC = ((NotificaModel) lTable.get("AutC")).getAutoritaEsterna();
				NoteAutC = ((NotificaModel) lTable.get("AutC")).getNote();
				setRequestAttribute("NoteAutC", NoteAutC);
				setRequestAttribute("autoritaEsternaC", lAutC);
			}

			// Ricerca Magistrato
			IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
			MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
			setRequestAttribute("magistrato", lMagi);
		}

		// Pena Residua
		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", llPenMod);

		// ISTITUTO DI DETENZIONE
		LuogoDetenzioneModel lLuoMod = new LuogoDetenzioneModel();
		ILuogoDetenzione lLuoDetCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
		lLuoMod = lLuoDetCtrl.ExRicercaLuogoDetByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("luogodetenzione", lLuoMod);

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UfficioModel lUfficioMod = this.getUfficioUtenteConnesso();
		String lCodTipoUfficio = lUfficioMod.getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		return PG_LOAD_DETTAGLIO_CONCESSIONE_ESPULSIONE;
	}
}