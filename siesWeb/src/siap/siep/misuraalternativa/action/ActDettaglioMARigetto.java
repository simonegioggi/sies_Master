package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Hashtable;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioMARigetto
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Rigetto
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

public class ActDettaglioMARigetto extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ricerca posizione giuridica
		/*
		 * REWORK DETTAGLIO IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo
		 * (lFascMod.getIdFascicoloSiep());
		 */
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		/*
		 * Non viene utilizzato //ricerca posizione giuridica PRECEDENTE PosizioneGiuridicaModel lPosPre = new
		 * PosizioneGiuridicaModel(); IPosizioneGiuridica lCtrPos =
		 * SIEPLookupRemote.getPosizioneGiuridicaRemote(); lPosPre =
		 * lCtrPos.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		 */
		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = null;
		EventoModel lEveSorMod = null;
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlMod = new MisuraAlternativaModel();
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = new MagistratoModel();

		/*
		 * String lPassaggioDetenuto = "N"; if(!this.isRequestParameterNullObj("lRitorno") && lPosPre != null
		 * && lPosPre.getCodPosizioneGiuridica() != null && lPosPre.getCodPosizioneGiuridica().equals("29") &&
		 * lPos != null && lPos.getPosizioneGiuridica() != null &&
		 * lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null &&
		 * lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("03")) { lPassaggioDetenuto = "S"; }
		 * this.setRequestAttribute("passaggioDetenuto", lPassaggioDetenuto);
		 * if(lPassaggioDetenuto.equals("S") || (lPos != null && lPos.getPosizioneGiuridica() != null &&
		 * lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null &&
		 * !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("03")) ) {
		 */

		lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		if (lEveMod != null && lEveMod.getEvento() != null && lEveMod.getEvento().getIdEvento() != null) {
			this.setRequestAttribute("eventonotifica", lEveMod);
			// Hashtable per le notifiche
			Hashtable lTable = this.ricercaNotifiche(lEveMod.getNotifiche());

			// Autorità esterna E
			AutoritaEsternaModel lAutE = null;
			String NoteAutE = null;
			if (lTable.get("AutE") != null) {
				lAutE = ((NotificaModel) lTable.get("AutE")).getAutoritaEsterna();
				NoteAutE = ((NotificaModel) lTable.get("AutE")).getNote();
				setRequestAttribute("NoteAutE", NoteAutE);
				setRequestAttribute("autoritaEsternaE", lAutE);
			}
			// ricerca misura per l'evento
			if (lEveMod.getEvento().getEveIdEvento() != null)
				lMisAlMod = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEvento()
						.getEveIdEvento());
			else
				lMisAlMod = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEvento()
						.getIdEvento());

			// Ricerca Magistrato
			lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
			setRequestAttribute("magistrato", lMagi);

		} else {
			lEveSorMod = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);
			// ricerca misura per l'evento
			if (lEveSorMod != null && lEveSorMod.getIdEvento() != null)
				lMisAlMod = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveSorMod.getIdEvento());

			this.setRequestAttribute("eventosor", lEveSorMod);

		}

		/*
		 * } else { }
		 */

		setRequestAttribute("misuraalternativa", lMisAlMod);

		// Sede Ufficio di Sorveglianza
		UfficioModel lUffEmiMod = new UfficioModel();
		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
		// lUffEmiMod = new UfficioModel();

		if (lMisAlMod != null)
			lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lMisAlMod.getChiaveUfficioFascicoloSius());
		setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		// Pena Residua
		/*
		 * REWORK DETTAGLIO IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel
		 * llPenMod = lCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 */
		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", llPenMod);

		/*
		 * non viene utilizzato dalla jsp //ISTITUTO DI DETENZIONE LuogoDetenzioneModel lLuoMod = new
		 * LuogoDetenzioneModel(); ILuogoDetenzione lLuoDetCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
		 * lLuoMod = lLuoDetCtrl.ExRicercaLuogoDetByFascicolo(lFascMod.getIdFascicoloSiep());
		 * setRequestAttribute("luogodetenzione", lLuoMod);
		 */
		return PG_LOAD_DETTAGLIO_MA_RIGETTO;
	}
}