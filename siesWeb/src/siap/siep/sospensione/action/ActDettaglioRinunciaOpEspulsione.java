package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.Hashtable;

import siap.sico.cssa.controller.ICSSA;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuraalternativa.action.ActMisuraAlternativa;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioRinunciaOpEspulsione
 * </p>
 * <p>
 * Description: Classe Action per la load DETTAGLIO di rinuncia opposizione espulsione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioRinunciaOpEspulsione extends ActMisuraAlternativa implements ICostantiSospensione {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Id dell'evento inserito
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = getPosizioneGiuridicaLuogoDetenzioneAltraCausa(
				lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("eventonotifica", lEveMod);

		// ricerca Verbale
		IVerbale lVerCtrl = SIEPLookupRemote.getVerbaleRemote();
		VerbaleModel lVerbale = new VerbaleModel();
		lVerbale = lVerCtrl.ExRicercaVerbaleByCodTipoIdEvento(lEveMod.getEvento().getEveIdEvento(), "07");
		setRequestAttribute("verbale", lVerbale);

		// cssa verbale
		CSSAModel lCssaMod = new CSSAModel();
		if (lVerbale != null && lVerbale.getCssIdCssa() != null
				&& lVerbale.getCssIdCssa().compareTo(new BigDecimal(0)) != 0) {
			ICSSA lCtrlCssa = SICOLookupRemote.getCSSARemote();
			lCssaMod = lCtrlCssa.getCSSAByKey(lVerbale.getCssIdCssa());
		}
		setRequestAttribute("cssaVer", lCssaMod);

		// Istituto Detenzione verbale
		IstitutoDetenzioneModel lIst = new IstitutoDetenzioneModel();
		if (lVerbale != null && lVerbale.getIstDetIdIstitutoDetenzione() != null
				&& !lVerbale.getIstDetIdIstitutoDetenzione().equals("-")) {
			IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
			lIst = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lVerbale.getIstDetIdIstitutoDetenzione());
		}
		setRequestAttribute("istitutodetenzioneVer", lIst);

		/************************************
		 * setto le NOTIFICHE
		 *************************************/
		Hashtable lTable = ricercaNotifiche(lEveMod.getNotifiche());

		// istituto
		IstitutoDetenzioneModel lIstMod = null;
		if (lTable.get("lNotIstituto") != null) {
			lIstMod = ((NotificaModel) lTable.get("lNotIstituto")).getIstitutoDetenzione();
			setRequestAttribute("lIstMod", lIstMod);
		}

		// Autorità esterna
		AutoritaEsternaModel lAutE = null;
		String NoteAutE = null;
		if (lTable.get("AutE") != null) {
			lAutE = ((NotificaModel) lTable.get("AutE")).getAutoritaEsterna();
			NoteAutE = ((NotificaModel) lTable.get("AutE")).getNote();
			setRequestAttribute("NoteAutE", NoteAutE);
			setRequestAttribute("autoritaEsternaE", lAutE);
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

		// Autorità esterna PC
		AutoritaEsternaModel lAutPC = null;
		String NoteAutPC = null;

		if (lTable.get("AutPC") != null) {
			lAutPC = ((NotificaModel) lTable.get("AutPC")).getAutoritaEsterna();
			NoteAutPC = ((NotificaModel) lTable.get("AutPC")).getNote();
			setRequestAttribute("NoteAutPC", NoteAutPC);
			setRequestAttribute("autoritaEsternaPC", lAutPC);
		}

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);

		// Pena Residua
		PenaResiduaModel llPenMod = getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", llPenMod);

		// ISTITUTO DI DETENZIONE
		LuogoDetenzioneModel lLuoMod = new LuogoDetenzioneModel();
		ILuogoDetenzione lLuoDetCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
		lLuoMod = lLuoDetCtrl.ExRicercaLuogoDetByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("luogodetenzione", lLuoMod);

		return PG_LOAD_DETTAGLIO_RINUNCIA_OPPOSIZIONE_ESPULSIONE;
	}

}