package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import f3b.util.F3BException;
import siap.sico.camponota.controller.ICampoNota;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;

/**
 * <p>
 * Title: ActDettaglioVariazioneMAAmmProvDT
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Ammissione provvisoria
 * </p>
 * <p>
 * con data inizio misura variata
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActDettaglioVariazioneMADecSca extends ActMisuraAlternativa
		implements ICostantiMisuraAlternativa {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento annotazione inserito
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// POSIZIONE GIURIDICA

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// ricerca evento Annotazione Variazione Decorrenza scadenza Misura
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("eventonotifica", lEveMod);
		/* Hashtable lTable = */ricercaNotifiche(lEveMod.getNotifiche());

		// AMBROSINO - Ricevo e Passo l'evento scritto in fase di variazione dataInizio misura
		// che serve per visualizzare alcuni dati nel dettaglio .
		// Stessa cosa per la Misura Alt veriata e campo nota

		// ricerca evento prima (Annotazione Pervenimento Richiesta Variazione)
		lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEveModAnn = new EventoModel();
		lEveModAnn = lCtrlEvento.ExRicercaEventoByKey(lEveMod.getEvento().getEveIdEvento());
		setRequestAttribute("eventoannota", lEveModAnn);
		// Hashtable lTable = ricercaNotifiche(lEveMod.getNotifiche());

		// Campo Nota

		ICampoNota lCtrlCam = SICOLookupRemote.getCampoNotaRemote();
		CampoNotaModel lCamMod = lCtrlCam.ExRicercaCampoNotaByIdEvento(lEveModAnn.getIdEvento());
		setRequestAttribute("camponota", lCamMod);

		// ricerca misura per il fascicolo
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lDecreto = new MisuraAlternativaModel();
		lDecreto = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveModAnn.getEveIdEvento());
		setRequestAttribute("misuraalternativa", lDecreto);
		// setRequestAttribute("misaltvariata", lDecreto);

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		;
		setRequestAttribute("magistrato", lMagi);

		// Data Sottoscrizione Verbale Obblighi
		if (lDecreto != null) {
			EventoModel lEveVer = new EventoModel();
			IEventoSimeone lCtrlEven = SICOLookupRemote.getEventoSimeoneRemote();
			lEveVer = lCtrlEven.ExRicercaEventoByEveIdEventoTipoProvCodMotivo(lDecreto.getEveIdEvento(), "07",
					"18", "0314");

			// VerbaleModel lVerMod = new VerbaleModel();
			IVerbale lCtrlVe = SIEPLookupRemote.getVerbaleRemote();
			VerbaleModel lVerbMod = lCtrlVe.ExRicercaVerbaleObblighiByIdEvento(lEveVer.getIdEvento());
			setRequestAttribute("verbale", lVerbMod);
		}

		// PENA RESIDUA LEGATA ALL'EVENTO

		PenaResiduaModel llPenMod = getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", llPenMod);

		// NOTIFICHE
		List lListAutorita = new ArrayList();
		List IListIstituti = new ArrayList();
		List lListUffici = new ArrayList();
		List lListCssa = new ArrayList();
		List lListAvvocatiSiep = new ArrayList();

		NotificaModel[] lNotifiche = lEveMod.getNotifiche();

		for (int i = 0; i < lNotifiche.length; i++) {
			// Autorita Esterne Dest X Notifiche e Aut POL_E
			if (lNotifiche[i].getAutEstIdAutoritaEsterna() != null) {
				// Avvocati Siep
				if (lNotifiche[i].getAvvIdAvvocatoFascicoloSiep() != null) {
					lListAvvocatiSiep.add(lNotifiche[i]);
				} else {
					lListAutorita.add(lNotifiche[i]);
				}
			}

			// Istituto
			if (lNotifiche[i].getIstDetIdIstitutoDetenzione() != null
					&& !lNotifiche[i].getIstDetIdIstitutoDetenzione().equals("")) {
				IListIstituti.add(lNotifiche[i]);
			}

			// Uffici TDS MDS
			if (lNotifiche[i].getUffCodUfficio() != null) {
				lListUffici.add(lNotifiche[i]);
			}

			// Cssa UEPE Serv. Soc.
			if (lNotifiche[i].getCssIdCssa() != null) {
				lListCssa.add(lNotifiche[i]);
			}

			/*
			 * // Avvocati Siep if( lNotifiche[i].getAvvIdAvvocatoFascicoloSiep() != null ) {
			 * lListAvvocatiSiep.add(lNotifiche[i]); }
			 */
		}

		// Nuovi
		setRequestAttribute("listaAutorita", lListAutorita);
		setRequestAttribute("listaIstituti", IListIstituti);
		setRequestAttribute("listaUffici", lListUffici);
		setRequestAttribute("listaCssa", lListCssa);
		setRequestAttribute("listaAvvSiep", lListAvvocatiSiep);
		// Vecchi
		setRequestAttribute("tipoMisura", "DETENZIONE");

		return PG_LOAD_DETTAGLIO_VARIAZIONE_MA_DEC_SCA;
	}

}