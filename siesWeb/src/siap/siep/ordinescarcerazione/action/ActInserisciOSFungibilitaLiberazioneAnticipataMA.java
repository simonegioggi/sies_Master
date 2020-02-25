package siap.siep.ordinescarcerazione.action;

/**
 * <p>Title: ActDettaglioLSLiberoIstanzaProdotta</p>
 * <p>Description: Classe Action per la load dettaglio di Evento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.action.ICostantiFungibilita;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

public class ActInserisciOSFungibilitaLiberazioneAnticipataMA extends ActOrdineScarcerazione
		implements ICostantiOrdineScarcerazione {

	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		String tipoMisura = "OS_LIBERAZIONE_ANTICIPATA";
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		NotificaModel[] lNotifiche = this.setNotificheOS();
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
				ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaPerOSLiberazioneAnticipataMAByIdFascicolo(
				lFascicoloModel.getIdFascicoloSiep());

		// Controllo se c'è misura alternativa
		if (lMisAlModConcessa == null) {
			EventoNotificaModel lEveMisMod = new EventoNotificaModel();
			String lCodiceUff = getRequestStringParameter("ufficioemittente");

			lEveMisMod.getEvento().setCodUfficioEmittente(lCodiceUff);
			ComuneModel lComModAut = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));
			lEveMisMod.getEvento().setCodLuogoEmittente(lComModAut.getCodComune());
			// setto il deposito ordinanza
			DepositoOrdinanzaPcModel lDepOrdMod = new DepositoOrdinanzaPcModel();
			lDepOrdMod
					.setAnnoS3(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
			lDepOrdMod.setNumS3(
					getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));
			lDepOrdMod.setDataUdienza(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			// lDepOrdMod.setIdCssaComp(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));
			lDepOrdMod.setCodUfficioInserimento(lCodiceUfficio);
			lDepOrdMod.setCodOperatoreInserimento(lCodiceOperatore);
			lDepOrdMod.setDataInserimento(DateUtils.getSysDate());
			String lCodiceUffEmitt = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS));
			lDepOrdMod.setCodUfficioMagistratoComp(lCodiceUffEmitt);
			lDepOrdMod.setLuogoSvolgimentoProva(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));
			// setto il tenore
			TenoreModel lTenMod = new TenoreModel();
			lTenMod.setCodEsitoTenore("0001");
			lTenMod.setData(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lTenMod.setCodOggettoTenore("008");
			lTenMod.setProgrTenore(new BigDecimal(1));
			lTenMod.setCodUfficioInserimento(lCodiceUfficio);
			lTenMod.setCodOperatoreInserimento(lCodiceOperatore);
			lTenMod.setDataInserimento(DateUtils.getSysDate());

			// ricerco Pena residua

			PenaResiduaModel lPenaResMod = new PenaResiduaModel();
			IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			lPenaResMod = lPenResCtrl
					.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

			// setto evento di misura alternativa
			lEveMisMod.getEvento()
					.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lEveMisMod.getEvento().setDataTrasmissioneAtti(getRequestDateParameter(
					ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lEveMisMod.getEvento().setCodTipoEvento("01");
			lEveMisMod.getEvento().setCodTipoProvvedimento("03");
			lEveMisMod.getEvento().setCodEsito("-");
			lEveMisMod.getEvento().setFlagStampaSiep("S");
			lEveMisMod.getEvento().setFlagVideoSiep("S");
			lEveMisMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
			lEveMisMod.getEvento().setCodMotivo("0083");
			lEveMisMod.getEvento().setDataInserimento(DateUtils.getSysDate());
			lEveMisMod.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
			lEveMisMod.getEvento().setCodUfficioInserimento(lCodiceUfficio);
			lEveMisMod.getEvento().setCodLuogoDestinatario("-");
			lEveMisMod.getEvento().setCodTipoUfficioDestinatario("-");
			lEveMisMod.getEvento()
					.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			lEveMisMod.setNotifiche(lNotifiche);

			// setto misura alternativa
			MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();

			lMisMod.setCodTipoDecisione("03");
			lMisMod.setCodNaturaDecisione("CO");
			lMisMod.setCodTipoMisura("0083");
			lMisMod.setDataDecisione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			// lMisMod.setCodUfficioSorveglianza(lCodiceUff);
			lMisMod.setChiaveUfficioFascicoloSius(lCodiceUff);
			lMisMod.setDescrLuogoProva(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));
			lMisMod.setAnnoRegistro(
					getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
			lMisMod.setNumeroRegistro(
					getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));
			lMisMod.setChiaveProgrFascicoloSius(getRequestBigDecimalParameter(
					ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS));
			lMisMod.setChiaveAnnoFascicoloSius(getRequestBigDecimalParameter(
					ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS));
			lMisMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
			lMisMod.setNumGiorniMisura(
					getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA));
			lMisMod.setDataInizioMisura(DateUtils.getSysDate());
			lMisMod.setDataFineMisura(lPenaResMod.getDataFine());
			lMisMod.setCodUfficioInserimento(lCodiceUfficio);
			lMisMod.setCodOperatoreInserimento(lCodiceOperatore);
			lMisMod.setDataInserimento(DateUtils.getSysDate());
			lMisMod.setCodTipoUfficioScarcerazione("-");
			lMisMod.setCodUfficioSorveglianza(lCodiceUff);
			lMisMod.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
			// Inserisco Misura Alternativa
			// IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			// ---GDV MisuraAlternativaModel lMisuraModel =
			// lCtrlMisura.ExInserisciMisuraAlternativaEventoNotifica(lEveMisMod, lDepOrdMod,lTenMod,
			// lMisMod);

		}
		lEveMod.getEvento().setCodMotivo("0083");
		lEveMod.getEvento().setCodTipoEvento("01");
		lEveMod.getEvento().setCodTipoProvvedimento("06");
		lEveMod.getEvento().setCodEsito("-");
		lEveMod.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEveMod.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioInserimento(lCodiceUfficio);
		lEveMod.getEvento().setCodLuogoDestinatario("-");
		lEveMod.getEvento().setCodTipoUfficioDestinatario("-");
		lEveMod.getEvento()
				.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		lEveMod.getEvento()
				.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEveMod.getEvento()
				.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveMod.getEvento().setFlagStampaSiep("S");
		lEveMod.getEvento().setFlagVideoSiep("S");
		lEveMod.getEvento().setFlagPiuMeno("-");
		lEveMod.getEvento().setFlagPiuMeno("-");
		// --NO-- lEveMod.getEvento().setFlagDocumentoRegistrato("N");
		lEveMod.getEvento().setCodLuogoEmittente(lUtenteMod.getUfficioUtente().getCodComune());
		lEveMod.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEveMod.setNotifiche(lNotifiche);

		EventoNotificaModel lEve = new EventoNotificaModel();

		IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
		lEve = lCtrlMisura.ExInserisciOModificaOSNotifica(lEveMod, tipoMisura);

		// Inserimento Fungibilita
		FungibilitaModel lFunMod = new FungibilitaModel();
		lFunMod.setCodTipoFungibilita("02");
		lFunMod.setFlagValidato("N");
		lFunMod.setNumAnni(new BigDecimal(getRequestStringParameter(ICostantiFungibilita.CAMPO_NUM_ANNI)));
		lFunMod.setNumMesi(new BigDecimal(getRequestStringParameter(ICostantiFungibilita.CAMPO_NUM_MESI)));
		lFunMod.setNumGiorni(
				new BigDecimal(getRequestStringParameter(ICostantiFungibilita.CAMPO_NUM_GIORNI)));
		lFunMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lFunMod.setDataInserimento(DateUtils.getSysDate());
		lFunMod.setCodOperatoreInserimento(lCodiceOperatore);
		lFunMod.setCodUfficioInserimento(lCodiceUfficio);
		lFunMod.setEveIdEvento(lEve.getEvento().getIdEvento());
		IFungibilita lCtrlFung = SIEPLookupRemote.getFungibilitaRemote();
		/* FungibilitaModel lFunModel = */lCtrlFung.ExInserisciFungibilita(lFunMod);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOSLiberazioneAnticipataMA&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEve.getEvento().getIdEvento();

		return lPage;
	}

}