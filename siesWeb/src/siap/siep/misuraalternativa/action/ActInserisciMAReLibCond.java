package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciMAReLibCond extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {

	/**
	 * Azione di Inserimento del MisuraAlternativa
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
		EventoNotificaModel lRetModel = new EventoNotificaModel();
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

//		String tipoMisura = "";
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		String lPosizione = this
				.getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		String lPage = null;

		setRequestAttribute("posizionegiuridica",
				this.getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA));

		if (this.getRequestStringParameter("flagmisura").equals("N")) {
			// se non è presente la misuralaternativa concessa la inserisco

			// INSERISCO EVENTO E NOTIFICA DEL TDS

			EventoNotificaModel lEveMod = new EventoNotificaModel();
			EventoModel lEve = new EventoModel();
			lEve.setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			lEve.setCodTipoEvento("01");
			lEve.setCodTipoProvvedimento("03");
			lEve.setCodEsito("-");
			lEve.setCodOperatoreInserimento(lCodiceOperatore);
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));
			lEve.setCodUfficioEmittente(lCodiceUff);
			ComuneModel lComModAut = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));
			lEve.setCodLuogoEmittente(lComModAut.getCodComune());
			lEve.setDataInserimento(DateUtils.getSysDate());
			lEve.setCodUfficioInserimento(lCodiceUfficio);
			lEve.setCodLuogoDestinatario("-");
			lEve.setCodTipoUfficioDestinatario("-");

			lEve.setDataEmissione(getRequestDateParameter(
					ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE));

			lEve.setDataTrasmissioneAtti(getRequestDateParameter(
					ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE));

			lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
			lEve.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

			// modifica 15-02-2006 -- Dario -- Viviana
			// il FlagStampaSiep viene settato non ad 'N' ma ad 'S' in modo da comparire
			// nello stato di esecuzione sui template
			// lEve.setFlagStampaSiep("N");
			lEve.setFlagStampaSiep("S");

			lEve.setFlagVideoSiep("S");
			lEve.setFlagDocumentoRegistrato("S");

			lEveMod.setEvento(lEve);

			// setto le notifiche
			NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
			lEveMod.setNotifiche(lNotifiche);

			// setto il deposito ordinanza
			DepositoOrdinanzaPcModel lDepOrdMod = new DepositoOrdinanzaPcModel();
			lDepOrdMod
					.setAnnoS3(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
			lDepOrdMod
					.setNumS3(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));

			lDepOrdMod.setDataUdienza(getRequestDateParameter(
					ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE));

			lDepOrdMod.setCodUfficioInserimento(lCodiceUfficio);
			lDepOrdMod.setCodOperatoreInserimento(lCodiceOperatore);
			lDepOrdMod.setDataInserimento(DateUtils.getSysDate());
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE)) {
				String lCodiceUffEmitt = getCodUfficioByCodTipoUfficioDescrComune(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS));
				lDepOrdMod.setCodUfficioMagistratoComp(lCodiceUffEmitt);
			}

			// setto il tenore
			TenoreModel lTenMod = new TenoreModel();
			lTenMod.setCodEsitoTenore("0006");

			lTenMod.setData(getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE));

			lTenMod.setCodOggettoTenore(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			lTenMod.setProgrTenore(new BigDecimal(1));
			lTenMod.setCodUfficioInserimento(lCodiceUfficio);
			lTenMod.setCodOperatoreInserimento(lCodiceOperatore);
			lTenMod.setDataInserimento(DateUtils.getSysDate());
			// ricerco Pena residua

			PenaResiduaModel lPenaResMod = new PenaResiduaModel();
			IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel
					.getIdFascicoloSiep());

			// setto la misuraalternativa - ricerco se esite un altra MA

			MisuraAlternativaModel lMisAlModCorr = new MisuraAlternativaModel();
			IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
			lMisAlModCorr = lMisAltCtrl.ExRicercaMisuraAlternativaCorrenteByIdFascicolo(lFascicoloModel
					.getIdFascicoloSiep());

			// setto la misuraalternativa
			if (lMisAlModCorr != null) {
				lMisAlModCorr.setDataFineMisura(getRequestDateParameter(
						ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
				/*MisuraAlternativaModel llMisModRet = */lMisAltCtrl.ExModificaMisuraAlternativa(lMisAlModCorr);
			}

			lMisMod.setCodTipoDecisione("03");
			lMisMod.setCodNaturaDecisione("RE");
			lMisMod.setCodTipoMisura(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			lMisMod.setDataDecisione(getRequestDateParameter(
					ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE));

			lMisMod.setChiaveUfficioFascicoloSius(lCodiceUff);
			lMisMod.setAnnoRegistro(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
			lMisMod.setNumeroRegistro(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));
			lMisMod.setChiaveProgrFascicoloSius(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS));
			lMisMod.setChiaveAnnoFascicoloSius(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS));
			lMisMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
			if (!(lPosizione.equals("07") || lPosizione.equals("10") || lPosizione.equals("16")
					|| lPosizione.equals("17") || lPosizione.equals("46") || lPosizione.equals("47")
					|| lPosizione.equals("20") || lPosizione.equals("26") || lPosizione.equals("30")
					|| lPosizione.equals("13") || lPosizione.equals("54"))) {
				if (lPenaResMod != null) {
					lMisMod.setDataInizioMisura(lPenaResMod.getDataInizio());
					lMisMod.setDataFineMisura(lPenaResMod.getDataFine());

				}
			}

			lMisMod.setCodUfficioInserimento(lCodiceUfficio);
			lMisMod.setCodOperatoreInserimento(lCodiceOperatore);
			lMisMod.setDataInserimento(DateUtils.getSysDate());
			lMisMod.setCodTipoUfficioScarcerazione("-");
			lMisMod.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
			// String codPosGiu = null;

			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraModel = lCtrlMisura.ExInserisciMisuraAlternativaEventoNotifica(
					lEveMod, lDepOrdMod, lTenMod, lMisMod);

			// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
			EventoNotificaModel lEveNot = new EventoNotificaModel();

			lEveNot.getEvento().setCodTipoProvvedimento("04");
			lEveNot.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));

			lEveNot.setEvento(setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));
			lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());
			lEveNot.getMagistrato().setCodMagistrato(
					getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

			NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();

			lEveNot.setNotifiche(lNotificheMod);

			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
			PenaResiduaModel lPenaRes = new PenaResiduaModel();
			lPenaRes.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			IMisuraAlternativa lCtrlMisuraAlt = SICOLookupRemote.getMisuraAlternativaRemote();
			EventoNotificaModel lEveNotModel = lCtrlMisuraAlt.ExInserisciOModificaMANotifica(lEveNot,
					lPenaRes, null, null);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMAReLibCond&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();

		} else {
			// la misura alternativa esiste
			IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
			lMisAlModConcessa = lMisAltCtrl
					.ExRicercaMisuraAlternativaByKey(getRequestBigDecimalParameter(CAMPO_ID_MISURA_ALTERNATIVA));

			EventoNotificaModel lEve = new EventoNotificaModel();
			lEve.getEvento().setCodTipoProvvedimento("04");
			lEve.getEvento().setCodMotivo(lMisAlModConcessa.getCodTipoMisura());
			lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
			lEve.getMagistrato().setCodMagistrato(
					getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			lEve.getEvento().setEveIdEvento(lMisAlModConcessa.getEveIdEvento());

			// Inserisco l'array di Notifiche nell'Evento

			NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
			lEve.setNotifiche(lNotifiche);

			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
			PenaResiduaModel lPenaRes = new PenaResiduaModel();
			lPenaRes.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			lRetModel = lCtrlMisura.ExInserisciOModificaMANotifica(lEve, lPenaRes, null, null);
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMAReLibCond&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		}

		return lPage;
	}

}