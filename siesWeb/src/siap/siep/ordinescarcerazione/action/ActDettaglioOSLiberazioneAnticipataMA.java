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
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
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
 * Title: ActDettaglioOSLiberazioneAnticipataMA
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio di Liberazione Anticipata Misura Alternativa
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
public class ActDettaglioOSLiberazioneAnticipataMA extends ActSIESDettaglioProvvedimento implements
		ICostantiOrdineScarcerazione {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		BigDecimal lIdEve = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

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

		int lIndex = 0;

		Vector lVectAvvocati = new Vector();

		for (lIndex = 0; lIndex < lEveMod.getNotifiche().length; lIndex++) {
			// Controllo se c'e' un Avvocato associato alla Notifica
			if (lEveMod.getNotifiche()[lIndex].getAvvIdAvvocatoFascicoloSiep() != null) {
				IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
				AvvocatoSiepModel lAvvocato = lAvvCtrl.ExRicercaAvvocatoByKeyAvvocatoFasSiep(lEveMod
						.getNotifiche()[lIndex].getAvvIdAvvocatoFascicoloSiep());
				lVectAvvocati.add(lAvvocato);
			}
		}

		if (lVectAvvocati.size() > 0) {
			setRequestAttribute("avvocati", lVectAvvocati);
		}

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

		MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		lMisAlModConcessa = lMisAltCtrl
				.ExRicercaMisuraAlternativaPerOSLiberazioneAnticipataMAByIdFascicolo(lEveMod.getEvento()
						.getFasSieIdFascicoloSiep());

		setRequestAttribute("misuraalternativa", lMisAlModConcessa);

		/*
		 * //Data Sottoscrizione Verbale Obblighi VerbaleModel lVerMod = new VerbaleModel(); IVerbale lCtrlVe
		 * = SIEPLookupRemote.getVerbaleRemote(); VerbaleModel lVerbMod =
		 * lCtrlVe.ExRicercaVerbaleObblighiByIdEvento(lMisAlModConcessa.getEveIdEvento());
		 * setRequestAttribute("verbale", lVerbMod);
		 */
		// magistrato con TIPO NOTIFICA = C
		/*
		 * MagistratoModel lMagSorvMod = new MagistratoModel(); IMagistrato lCtrlMagSorv =
		 * SICOLookupRemote.getMagistratoRemote(); lMagSorvMod =
		 * lCtrlMagSorv.ExRicercaMagistratoByCod(lMisAlModConcessa.getCodMagistrato());
		 * setRequestAttribute("magistratosorveglianza", lMagSorvMod);
		 */
		// setto Ufficio Emittente
		for (int i = 0; i < lEveMod.getNotifiche().length; i++) {
			if (lEveMod.getNotifiche()[i].getCodTipoNotifica().equals("E")) {
				String codUff = lEveMod.getNotifiche()[i].getUffCodUfficio();
				UfficioModel lUffEmiMod = new UfficioModel();
				IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
				lUffEmiMod = lCtrlUffEmi.getUfficioByKey(codUff);
				setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);
			}
		}
//		String ritorno = "N";
//		if (!this.isRequestParameterNullObj("flagRitorno")) {
//			ritorno = "S";
//		} else {
//			ritorno = "E";
//		}

		ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		// LicenzaLibAnticipataModel llibAntMod =
		// lCtrlLib.ExRicercaLicenzaLibanticipataConcessayIDFascicoloSIEP(lEveMod.getEvento().getFasSieIdFascicoloSiep(),
		// ritorno);
		LicenzaLibAnticipataModel llibAntMod = null;

		Vector lListLA = lCtrlLib.ExRicercaLicenzeByEve(lEveMod.getEvento().getEveIdEvento());
		if (!lListLA.isEmpty()) {
			llibAntMod = (LicenzaLibAnticipataModel) lListLA.get(0);
		}

		if (llibAntMod != null && llibAntMod.getCodUfficioEmittente() != null) {
			String lCodUffEmi = llibAntMod.getCodUfficioEmittente();
			UfficioModel lUffMod = this.getUfficioByCodUfficio(lCodUffEmi);
			llibAntMod.setDescrUfficioEmittente(lUffMod.getDescrTipoUfficio());
		}

		// int lTotGiorniConcessi =
		// lCtrlLib.ExTotalePeriodiConcessiComputatiByIdFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		int lTotGiorniConcessi = 0;

		// verifica esistenza evento liberazione anticipata
		/*
		 * EventoModel lEvemodMod =null; IEventoSimeone lEventoCtrl =
		 * SICOLookupRemote.getEventoSimeoneRemote(); lEvemodMod =
		 * lEventoCtrl.ExRicercaEventoByFascicoloSiepTipEventoTipProvCodMotivo
		 * (lEveMod.getEvento().getFasSieIdFascicoloSiep(),"01","03","2130"); if(lEvemodMod == null) {
		 * lEvemodMod =
		 * lEventoCtrl.ExRicercaEventoByFascicoloSiepTipEventoTipProvCodMotivo(lEveMod.getEvento()
		 * .getFasSieIdFascicoloSiep(),"01","03","0076"); }
		 */

		DepositoOrdinanzaPcModel lDepOrdMod = null;

		if (lEveMod != null && lEveMod.getEvento() != null && lEveMod.getEvento().getEveIdEvento() != null) {
			// RICERCA DEPOSITO_ORDINANZA_PC
			IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			lDepOrdMod = lCtrlDep.ExRicercaDepositoOrdinanzaPcByEvento(lEveMod.getEvento().getEveIdEvento());
			if (lDepOrdMod != null && lDepOrdMod.getNumGiorniLibanticipata() != null) {
				if (llibAntMod != null) {
					// llibAntMod.setNumeroGiorni(lDepOrdMod.getNumGiorniLibanticipata());
					lTotGiorniConcessi = lDepOrdMod.getNumGiorniLibanticipata().intValue();
				}
			}
		}

		setRequestAttribute("liberazione", llibAntMod);

		setRequestAttribute("lTotGiorniConcessi", "" + lTotGiorniConcessi);
		setRequestAttribute("VetLicenze", lListLA);

		// modifica 27-02-2006 -- Dario -- Viviana
		// Bisogna visualizzare i Rigettati,Inammissibili e N.L.P./N.D.P.
		Vector llibAntModNonConcessi = null;
		llibAntModNonConcessi = lCtrlLib.ExRicercaLicenzaLibanticipataNonConcesseByIDFascicoloSIEP(lEveMod
				.getEvento().getFasSieIdFascicoloSiep(), llibAntMod.getEveIdEvento());

		setRequestAttribute("liberazioninonconcesse", llibAntModNonConcessi);

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

		return PG_DETTAGLIO_OS_LIBERAZIONE_ANTICIPATA_MA;
	}

}