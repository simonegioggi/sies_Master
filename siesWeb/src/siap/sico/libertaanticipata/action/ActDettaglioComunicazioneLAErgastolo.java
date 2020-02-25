package siap.sico.libertaanticipata.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.ordinescarcerazione.action.ICostantiOrdineScarcerazione;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioComunicazioneLAErgastolo
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Comunicazione LA Ergastolo
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
public class ActDettaglioComunicazioneLAErgastolo extends ActSIESDettaglioProvvedimento
		implements ICostantiOrdineScarcerazione, ICostantiLicenzaLibanticipata {

	public String processRequest() throws F3BException {

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// chiama il controller

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);

		/*
		 * REWORK PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); IPosizioneGiuridica lPosCtrl =
		 * SIEPLookupRemote.getPosizioneGiuridicaRemote(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lEveMod.
		 * getEvento().getFasSieIdFascicoloSiep());
		 */
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento,
						lEveMod.getEvento().getFasSieIdFascicoloSiep());
		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Al Procedimento non ha una Posizione Giuridica");

		setRequestAttribute("posizioneluogoaltra", lPos);

		// Ricerco la liberazione anticipata
		// 20/05/2014 Nuova L.A. : E' indispensabile inserire nella request il Vector VectLA per poter
		// distingure le diverse
		// tipologie di L.A. con i rispettivi giorni concessi
		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		LicenzaLibAnticipataModel llibAntMod = null;
		Vector VectLA = new Vector();

		if (lEveMod != null && lEveMod.getEvento() != null && lEveMod.getEvento().getIdEvento() != null) {
			VectLA = lCtrlLib.ExRicercaLicenzeByEve(lEveMod.getEvento().getEveIdEvento());
			if (!VectLA.isEmpty()) {
				llibAntMod = (LicenzaLibAnticipataModel) VectLA.get(0);
			}
		}

		// 20/05/2014 Nuova L.A.
		setRequestAttribute("Licenze", VectLA);

		if (llibAntMod != null && llibAntMod.getCodUfficioEmittente() != null) {
			String lCodUffEmi = llibAntMod.getCodUfficioEmittente();
			UfficioModel lUffMod = this.getUfficioByCodUfficio(lCodUffEmi);
			llibAntMod.setDescrUfficioEmittente(lUffMod.getDescrTipoUfficio());
		}

		/*
		 * GDV --- Non serve piu'!!! int lTotGiorniConcessi =
		 * lCtrlLib.ExTotalePeriodiConcessiComputatiByIdFascicoloSiep(lEveMod.getEvento().
		 * getFasSieIdFascicoloSiep()); setRequestAttribute("lTotGiorniConcessi",""+lTotGiorniConcessi);
		 */
		// 20/05/2014 - int lTotGiorniConcessi serve perchè con la nuova Gestione LA, i gg concessi totali
		// si prendono da DepositoordinanzaPC lDepOrdMod.getNumGiorniLibanticipata(), e NON si possono
		// inserire nelle licenze (llibAntMod.setNumeroGiorni(lDepOrdMod.getNumGiorniLibanticipata());) in
		// quanto serve anche
		// il n.ro gg della singola Licenza; i gg concessi totali Vanno inseriti nella request in un campo
		// stringa lTotGiorniConcessi come era prima

		int lTotGiorniConcessi = 0;

		// Ricerco il deposito ordinanza pc
		DepositoOrdinanzaPcModel lDepOrdMod = null;

		if (lEveMod != null && lEveMod.getEvento() != null && lEveMod.getEvento().getEveIdEvento() != null) {
			// RICERCA DEPOSITO_ORDINANZA_PC
			IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			lDepOrdMod = lCtrlDep.ExRicercaDepositoOrdinanzaPcByEvento(lEveMod.getEvento().getEveIdEvento());
			if (lDepOrdMod != null && lDepOrdMod.getNumGiorniLibanticipata() != null) {
				// llibAntMod.setNumeroGiorni(lDepOrdMod.getNumGiorniLibanticipata());
				lTotGiorniConcessi = lDepOrdMod.getNumGiorniLibanticipata().intValue();
			}
		}

		setRequestAttribute("lTotGiorniConcessi", "" + lTotGiorniConcessi);
		setRequestAttribute("liberazione", llibAntMod);

		// modifica 27-02-2006 -- Dario -- Viviana
		// Bisogna visualizzare i Rigettati,Inammissibili e N.L.P./N.D.P.
		Vector llibAntModNonConcessi = null;
		llibAntModNonConcessi = lCtrlLib.ExRicercaLicenzaLibanticipataNonConcesseByIDFascicoloSIEP(
				lEveMod.getEvento().getFasSieIdFascicoloSiep(), llibAntMod.getEveIdEvento());

		setRequestAttribute("liberazioninonconcesse", llibAntModNonConcessi);

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		/*
		 * REWORK IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel llPenMod =
		 * lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 */
		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento,
				lEveMod.getEvento().getFasSieIdFascicoloSiep());
		if (llPenMod != null) {
			lDataInizioPena = llPenMod.getDataInizio();
			// lDataFinePenaM = llPenMod.getDataFine();
			lDataFinePenaA = llPenMod.getDataFinePresunta();
			setRequestAttribute("StrdataInizioPena",
					DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
			setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
			setRequestAttribute("penaresidua", llPenMod);
		}

		setRequestAttribute("eventonotifica", lEveMod);

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", lMagi);

		// setto Ufficio Emittente
		UfficioModel lUffEmiMod = new UfficioModel();
		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
		IstitutoDetenzioneModel lIstMod = new IstitutoDetenzioneModel();
		IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();

		for (int i = 0; i < lEveMod.getNotifiche().length; i++) {
			if (lEveMod.getNotifiche()[i].getCodTipoNotifica().equals("C")) {
				lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lEveMod.getNotifiche()[i].getUffCodUfficio());
				setRequestAttribute("sedesorveglianza", lUffEmiMod.getDescrComune());
				setRequestAttribute("tiposorveglianza", lUffEmiMod.getDescrTipoUfficio());
			}
			if (lEveMod.getNotifiche()[i].getCodTipoNotifica().equals("E")) {
				lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(
						lEveMod.getNotifiche()[i].getIstDetIdIstitutoDetenzione());

				setRequestAttribute("istitutodetenzione", lIstMod);
			}
		}
		return PG_DETTAGLIO_COMUNICAZIONE_ERGASTOLO;
	}

}