package siap.siep.ordinescarcerazione.action;

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
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
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
 * Title: ActDettaglioOSLiberazioneAnticipata
 * </p>
 * <p>
 * Description: Classe Action per il Dettaglio Ordine di Scarcerazione a seguito di ordinanza liberazione
 * anticipata
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
public class ActDettaglioOSLiberazioneAnticipata extends ActSIESDettaglioProvvedimento implements
		ICostantiOrdineScarcerazione {
	public String processRequest() throws F3BException {
		// String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		BigDecimal lIdEve = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// String ritorno = "N";
		// if (!this.isRequestParameterNullObj("flagRitorno")) {
		// ritorno = "S";
		// } else {
		// ritorno = "E";
		// }

		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// chiama il controller

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEve);

		/*
		 * REWORK DETTAGLIO PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); IPosizioneGiuridica lPosCtrl =
		 * SIEPLookupRemote.getPosizioneGiuridicaRemote(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo
		 * (lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 */

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEve, lEveMod.getEvento()
						.getFasSieIdFascicoloSiep());

		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Al Procedimento non ha una Posizione Giuridica");

		setRequestAttribute("posizioneluogoaltra", lPos);

		// int lIndex = 0;

		/*
		 * Vector lVectAvvocati = new Vector();
		 * 
		 * for (lIndex = 0; lIndex < lEveMod.getNotifiche().length; lIndex++) { //Controllo se c'e' un
		 * Avvocato associato alla Notifica if (lEveMod.getNotifiche()[lIndex].getAvvIdAvvocatoFascicoloSiep()
		 * != null) { IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote(); AvvocatoSiepModel lAvvocato =
		 * lAvvCtrl
		 * .ExRicercaAvvocatoByKeyAvvocatoFasSiep(lEveMod.getNotifiche()[lIndex].getAvvIdAvvocatoFascicoloSiep
		 * ()); lVectAvvocati.add(lAvvocato); } }
		 * 
		 * if (lVectAvvocati.size() > 0) { setRequestAttribute("avvocati", lVectAvvocati); }
		 */

		// Ricerco la liberazione anticipata
		// 20/05/2014 Nuova L.A. : E' indispensabile inserire nella request il Vector VectLA per poter
		// distingure le diverse
		// tipologie di L.A. con i rispettivi giorni concessi

		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		// LicenzaLibAnticipataModel llibAntMod =
		// lCtrlLib.ExRicercaLicenzaLibanticipataConcessayIDFascicoloSIEP(lEveMod.getEvento().getFasSieIdFascicoloSiep(),
		// ritorno);
		LicenzaLibAnticipataModel llibAntMod = null;
		Vector VectLA = lCtrlLib.ExRicercaLicenzeByEve(lEveMod.getEvento().getEveIdEvento());

		if (!VectLA.isEmpty()) {
			llibAntMod = (LicenzaLibAnticipataModel) VectLA.get(0);
		}

		if (llibAntMod != null && llibAntMod.getCodUfficioEmittente() != null) {
			String lCodUffEmi = llibAntMod.getCodUfficioEmittente();
			UfficioModel lUffMod = this.getUfficioByCodUfficio(lCodUffEmi);
			llibAntMod.setDescrUfficioEmittente(lUffMod.getDescrTipoUfficio());
		}

		// verifica esistenza evento liberazione anticipata
		/*
		 * EventoModel lEvemodMod = null; IEventoSimeone lEventoCtrl =
		 * SICOLookupRemote.getEventoSimeoneRemote(); lEvemodMod =
		 * lEventoCtrl.ExRicercaEventoByFascicoloSiepTipEventoTipProvCodMotivo
		 * (lEveMod.getEvento().getFasSieIdFascicoloSiep(),"01","03","2130"); if(lEvemodMod == null) {
		 * lEvemodMod =
		 * lEventoCtrl.ExRicercaEventoByFascicoloSiepTipEventoTipProvCodMotivo(lEveMod.getEvento()
		 * .getFasSieIdFascicoloSiep(),"01","03","0076"); }
		 */
		DepositoOrdinanzaPcModel lDepOrdMod = null;
		int lTotGiorniConcessi = 0;

		if (lEveMod != null && lEveMod.getEvento() != null && lEveMod.getEvento().getEveIdEvento() != null) {
			// RICERCA DEPOSITO_ORDINANZA_PC
			IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			lDepOrdMod = lCtrlDep.ExRicercaDepositoOrdinanzaPcByEvento(lEveMod.getEvento().getEveIdEvento());
			if (lDepOrdMod != null && lDepOrdMod.getNumGiorniLibanticipata() != null) {
				// llibAntMod.setNumeroGiorni(lDepOrdMod.getNumGiorniLibanticipata());
				lTotGiorniConcessi = lDepOrdMod.getNumGiorniLibanticipata().intValue();
			}
		}

		setRequestAttribute("liberazione", llibAntMod);
		// 20/05/2014 Nuova L.A. E' indispensabile prendere i gg concessi totali da
		// lDepOrdMod.getNumGiorniLibanticipata(), ma NON si possono
		// inserire nelle licenze (llibAntMod.setNumeroGiorni(lDepOrdMod.getNumGiorniLibanticipata());) in
		// quanto serve anche
		// il n.ro gg della singola Licenza; i gg concessi totali Vanno inseriti nella request in un campo
		// stringa lTotGiorniConcessi
		setRequestAttribute("Licenze", VectLA);
		setRequestAttribute("lTotGiorniConcessi", "" + lTotGiorniConcessi);

		// modifica 27-02-2006 -- Dario -- Viviana
		// Bisogna visualizzare i Rigettati,Inammissibili e N.L.P./N.D.P.
		Vector llibAntModNonConcessi = null;
		if (llibAntMod != null) {
			llibAntModNonConcessi = lCtrlLib.ExRicercaLicenzaLibanticipataNonConcesseByIDFascicoloSIEP(
					lEveMod.getEvento().getFasSieIdFascicoloSiep(), llibAntMod.getEveIdEvento());
		}

		setRequestAttribute("liberazioninonconcesse", llibAntModNonConcessi);

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
//		Date lDataFinePenaM = null;

		/*
		 * REWORK DETTAGLIO IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel
		 * llPenMod =
		 * lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 */

		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEve, lEveMod.getEvento()
				.getFasSieIdFascicoloSiep());

		lDataInizioPena = llPenMod.getDataInizio();
//		lDataFinePenaM = llPenMod.getDataFine();
		lDataFinePenaA = llPenMod.getDataFinePresunta();
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", llPenMod);

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
			if (lEveMod.getNotifiche()[i].getCodTipoNotifica().equals("C")
					&& lEveMod.getNotifiche()[i].getUffCodUfficio() != null) {
				lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lEveMod.getNotifiche()[i].getUffCodUfficio());

				String descrTipoUfficio = lUffEmiMod.getDescrTipoUfficio();
				if (lUffEmiMod != null && lUffEmiMod.getCodTipoUfficio() != null
						&& lUffEmiMod.getCodTipoUfficio().equals("UDSM")) {
					descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
				}
				setRequestAttribute("sedesorveglianza", lUffEmiMod.getDescrComune());
				setRequestAttribute("tiposorveglianza", descrTipoUfficio);
			}

			if (lEveMod.getNotifiche()[i].getCodTipoNotifica().equals("E")
					&& lEveMod.getNotifiche()[i].getIstDetIdIstitutoDetenzione() != null) {
				lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lEveMod.getNotifiche()[i]
						.getIstDetIdIstitutoDetenzione());
				setRequestAttribute("istitutodetenzione", lIstMod);
			}

			// MEV29 - d.f. - 13/03/2015 -
			if (lEveMod.getNotifiche()[i].getCodTipoNotifica().equals("C")
					&& lEveMod.getNotifiche()[i].getUffCodUfficio() == null) {
				setRequestAttribute("autPoliziaNotifica", lEveMod.getNotifiche()[i]);
			}
		}

		// Ricerca se c'è Fungibilità
		FungibilitaModel lFungiMod = new FungibilitaModel();

		IFungibilita lFungiCtrl = SIEPLookupRemote.getFungibilitaRemote();
		lFungiMod = lFungiCtrl.ExRicercaFungibilitaByKeyEvento(lEveMod.getEvento().getIdEvento());

		if (lFungiMod == null) {
			setRequestAttribute("flagfungibilita", "N");

		} else {
			setRequestAttribute("fungibilita", lFungiMod);

			setRequestAttribute("flagfungibilita", "S");

		}

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UfficioModel lUfficioMod = this.getUfficioUtenteConnesso();
		String lCodTipoUfficio = lUfficioMod.getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		return PG_DETTAGLIO_OS_LIBERAZIONE_ANTICIPATA;
	}
}