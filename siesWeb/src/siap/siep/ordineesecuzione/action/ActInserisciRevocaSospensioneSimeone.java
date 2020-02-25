package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.motivoevento.action.ICostantiMotivoEvento;
import siap.siep.motivoevento.model.MotivoEventoModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * Inserimento degli Ordini di Revoca Simeone
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ActInserisciRevocaSospensioneSimeone extends ActOrdineEsecuzione
		implements ICostantiOrdineEsecuzione {
	public String processRequest() throws Exception {
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		String lCodiceLuogo = this.getCodComuneUtenteConnesso();

		// String lPosizione =
		// this.getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		String lPosizione = CercaPosizioneGiuridicaSeEsisteCercaAltraCausaString();

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		EventoNotificaModel lRetModel = new EventoNotificaModel();

		// ricerco Pena residua
		// PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		/* lPenaResMod = */lPenResCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		// PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		// PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		/* lPos = */lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascicoloModel.getIdFascicoloSiep());

		String lPage = "";

		// Motivo_Evento
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		MotivoEventoModel lMotEve = new MotivoEventoModel();
		lMotEve.setCodMotivoRevoca(getRequestStringParameter(ICostantiMotivoEvento.CAMPO_COD_MOTIVO_REVOCA));
		if (lMotEve.getCodMotivoRevoca().equals("0001"))// revoca pm
		{
			lMotEve.setCodMotivoRevocaPm(
					getRequestStringParameter(ICostantiMotivoEvento.CAMPO_COD_MOTIVO_REVOCA_PM));
			lMotEve.setMotivazioni(getRequestStringParameter(ICostantiMotivoEvento.CAMPO_MOTIVAZIONI));
		} else
			lMotEve.setCodMotivoRevocaPm("-");

		IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		/* PosizioneGiuridicaModel lPosizioneG = */lCtrlPosGiu
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

		if (lMotEve.getCodMotivoRevoca().equals("0002"))// reiezione
		{
			IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraMod = null;
			BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
					ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
			// EventoNotificaModel lEveNotMod = new EventoNotificaModel();
			if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
				lMisuraMod = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

			if (lMisuraMod == null) {

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
				ComuneModel lComModAut = new ComuneModel(getCodComuneByDescr(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));
				lEve.setCodLuogoEmittente(lComModAut.getCodComune());
				lEve.setDataInserimento(DateUtils.getSysDate());
				lEve.setCodUfficioInserimento(lCodiceUfficio);
				lEve.setCodLuogoDestinatario("-");
				lEve.setCodTipoUfficioDestinatario("-");
				lEve.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
				lEve.setDataTrasmissioneAtti(getRequestDateParameter(
						ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
				lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
				lEve.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
				lEve.setFlagStampaSiep("N");
				lEve.setFlagVideoSiep("S");
				lEve.setFlagDocumentoRegistrato("S");

				lEveMod.setEvento(lEve);

				// setto le notifiche
				NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
				lEveMod.setNotifiche(lNotifiche);

				// setto il deposito ordinanza
				DepositoOrdinanzaPcModel lDepOrdMod = new DepositoOrdinanzaPcModel();
				lDepOrdMod.setAnnoS3(
						getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
				lDepOrdMod.setNumS3(
						getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));
				lDepOrdMod.setDataUdienza(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
				lDepOrdMod.setCodUfficioInserimento(lCodiceUfficio);
				lDepOrdMod.setCodOperatoreInserimento(lCodiceOperatore);
				lDepOrdMod.setDataInserimento(DateUtils.getSysDate());

				// setto il tenore
				TenoreModel lTenMod = new TenoreModel();
				lTenMod.setCodEsitoTenore("0002");
				lTenMod.setData(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
				lTenMod.setCodOggettoTenore(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
				lTenMod.setProgrTenore(new BigDecimal(1));
				lTenMod.setCodUfficioInserimento(lCodiceUfficio);
				lTenMod.setCodOperatoreInserimento(lCodiceOperatore);
				lTenMod.setDataInserimento(DateUtils.getSysDate());

				// setto la misuraalternativa
				lMisMod.setCodTipoDecisione("03");
				lMisMod.setCodNaturaDecisione("RG");
				lMisMod.setCodTipoMisura(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
				lMisMod.setDataDecisione(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE));
				lMisMod.setFlagUfficioInserimento("P");
				lMisMod.setChiaveUfficioFascicoloSius(lCodiceUff);
				lMisMod.setAnnoRegistro(
						getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
				lMisMod.setNumeroRegistro(
						getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));
				lMisMod.setChiaveProgrFascicoloSius(getRequestBigDecimalParameter(
						ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS));
				lMisMod.setChiaveAnnoFascicoloSius(getRequestBigDecimalParameter(
						ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS));
				lMisMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

				lMisMod.setCodUfficioInserimento(lCodiceUfficio);
				lMisMod.setCodOperatoreInserimento(lCodiceOperatore);
				lMisMod.setDataInserimento(DateUtils.getSysDate());
				lMisMod.setCodTipoUfficioScarcerazione("-");
				lMisMod.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

				IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
				MisuraAlternativaModel lMisuraModel = lCtrlMisura
						.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod, lTenMod, lMisMod);

				// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
				EventoNotificaModel lEveNot = new EventoNotificaModel();
				lEveNot.getEvento().setCodTipoProvvedimento("04");

				// if(lFascicoloModel.getFlagAltraCausa().equals("S"))
				// {
				// lEveNot.getEvento().setCodMotivo("0079");
				// }
				// else
				if (lPosizione.equals("07") || lPosizione.equals("10") || lPosizione.equals("16")
						|| lPosizione.equals("17") || lPosizione.equals("46") || lPosizione.equals("47")
						|| lPosizione.equals("20") || lPosizione.equals("26") || lPosizione.equals("30")) {
					lEveNot.getEvento().setCodMotivo("0078");
				} else if (lPosizione.equals("02") || lPosizione.equals("04") || lPosizione.equals("49")) {
					lEveNot.getEvento().setCodMotivo("0080");
				} else if (lPosizione.equals("82")) {
					lEveNot.getEvento().setCodMotivo("5514");
				} else if (lPosizione.equals("83")) {
					lEveNot.getEvento().setCodMotivo("5515");
				} else if (lPosizione.equals("84")) {
					lEveNot.getEvento().setCodMotivo("5516");
				} else if (lPosizione.equals("76")) {
					lEveNot.getEvento().setCodMotivo("0079");
				} else if (lPosizione.equals("77")) {
					lEveNot.getEvento().setCodMotivo("5517");
				} else if (lPosizione.equals("78")) {
					lEveNot.getEvento().setCodMotivo("5518");
				} else if (lPosizione.equals("79")) {
					lEveNot.getEvento().setCodMotivo("5519");
				} else if (lPosizione.equals("80")) {
					lEveNot.getEvento().setCodMotivo("5520");
				} else if (lPosizione.equals("81")) {
					lEveNot.getEvento().setCodMotivo("5521");
				} else {
					lEveNot.getEvento().setCodMotivo("0000");
				}

				lEveNot.setEvento(setEventoMisuraAlternativa(lEveNot.getEvento()));
				lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());
				lEveNot.getMagistrato().setCodMagistrato(
						getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

				NotificaModel[] lNotificheMod = this.setNotificheOrdineEsecuzione();
				lEveNot.setNotifiche(lNotificheMod);

				PenaResiduaModel lPenaRes = new PenaResiduaModel();
				lPenaRes.setIdPenaResidua(lIdPenaRes);
				if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
					lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
							ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
							ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

				EventoNotificaModel lEveNotModel = lCtrlOrdineEsecuzione
						.ExInserisciOModificaMANotificaRevocaLS(lEveNot, lPenaRes, null, lMotEve);

				// String lFc = "0";
				// if (!isRequestParameterNullObj(FOGLIO_COMPLEMENTARE))
				// lFc = "1";

				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.ordineesecuzione.action.ActDettaglioRevocaLSAltrePosizioni&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();

			} else {
				// la misura alternativa esiste
				EventoNotificaModel lEve = new EventoNotificaModel();
				lEve.getEvento().setCodTipoProvvedimento("04");

				// if(lFascicoloModel.getFlagAltraCausa().equals("S"))
				// {
				// lEve.getEvento().setCodMotivo("0079");
				// }
				// else
				if (lPosizione.equals("07") || lPosizione.equals("10") || lPosizione.equals("16")
						|| lPosizione.equals("17") || lPosizione.equals("46") || lPosizione.equals("47")
						|| lPosizione.equals("20") || lPosizione.equals("26") || lPosizione.equals("30")) {
					lEve.getEvento().setCodMotivo("0078");
				} else if (lPosizione.equals("02") || lPosizione.equals("04") || lPosizione.equals("49")) {
					lEve.getEvento().setCodMotivo("0080");
				} else if (lPosizione.equals("82")) {
					lEve.getEvento().setCodMotivo("5514");
				} else if (lPosizione.equals("83")) {
					lEve.getEvento().setCodMotivo("5515");
				} else if (lPosizione.equals("84")) {
					lEve.getEvento().setCodMotivo("5516");
				} else if (lPosizione.equals("76")) {
					lEve.getEvento().setCodMotivo("0079");
				} else if (lPosizione.equals("77")) {
					lEve.getEvento().setCodMotivo("5517");
				} else if (lPosizione.equals("78")) {
					lEve.getEvento().setCodMotivo("5518");
				} else if (lPosizione.equals("79")) {
					lEve.getEvento().setCodMotivo("5519");
				} else if (lPosizione.equals("80")) {
					lEve.getEvento().setCodMotivo("5520");
				} else if (lPosizione.equals("81")) {
					lEve.getEvento().setCodMotivo("5521");
				} else {
					lEve.getEvento().setCodMotivo("0000");
				}

				lEve.setEvento(setEventoMisuraAlternativa(lEve.getEvento()));
				lEve.getMagistrato().setCodMagistrato(
						getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
				lEve.getEvento().setEveIdEvento(lMisuraMod.getEveIdEvento());

				// Inserisco l'array di Notifiche nell'Evento

				NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
				lEve.setNotifiche(lNotifiche);

				PenaResiduaModel lPenaRes = new PenaResiduaModel();
				lPenaRes.setIdPenaResidua(lIdPenaRes);
				if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
					lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
							ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
							ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

				lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaMANotificaRevocaLS(lEve, lPenaRes,
						lMisuraMod, lMotEve);

				// String lFc = "0";
				// if (!isRequestParameterNullObj(FOGLIO_COMPLEMENTARE))
				// lFc = "1";

				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.ordineesecuzione.action.ActDettaglioRevocaLSAltrePosizioni&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
			}
		} else // omessa e revoca pm
		{

			// EVENTO PROVVEDIMENTO
			EventoNotificaModel lEveMod = new EventoNotificaModel();
			lEveMod.getEvento().setCodTipoEvento("01");
			lEveMod.getEvento().setCodTipoProvvedimento("04");

			// if(lFascicoloModel.getFlagAltraCausa().equals("S"))
			// lEveMod.getEvento().setCodMotivo("0079");
			// else
			if (lPosizione.equals("07") || lPosizione.equals("10") || lPosizione.equals("16")
					|| lPosizione.equals("17") || lPosizione.equals("46") || lPosizione.equals("47")
					|| lPosizione.equals("20") || lPosizione.equals("26") || lPosizione.equals("30")) {
				lEveMod.getEvento().setCodMotivo("0078");
			} else if (lPosizione.equals("02") || lPosizione.equals("04") || lPosizione.equals("49"))
				lEveMod.getEvento().setCodMotivo("0080");
			else if (lPosizione.equals("82")) {
				lEveMod.getEvento().setCodMotivo("5514");
			} else if (lPosizione.equals("83")) {
				lEveMod.getEvento().setCodMotivo("5515");
			} else if (lPosizione.equals("84")) {
				lEveMod.getEvento().setCodMotivo("5516");
			} else if (lPosizione.equals("76")) {
				lEveMod.getEvento().setCodMotivo("0079");
			} else if (lPosizione.equals("77")) {
				lEveMod.getEvento().setCodMotivo("5517");
			} else if (lPosizione.equals("78")) {
				lEveMod.getEvento().setCodMotivo("5518");
			} else if (lPosizione.equals("79")) {
				lEveMod.getEvento().setCodMotivo("5519");
			} else if (lPosizione.equals("80")) {
				lEveMod.getEvento().setCodMotivo("5520");
			} else if (lPosizione.equals("81")) {
				lEveMod.getEvento().setCodMotivo("5521");
			} else
				lEveMod.getEvento().setCodMotivo("0000");

			lEveMod.getEvento()
					.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lEveMod.getEvento().setCodUfficioEmittente(lCodiceUfficio);
			lEveMod.getEvento().setCodLuogoEmittente(lCodiceLuogo);
			lEveMod.getEvento()
					.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			lEveMod.getEvento().setFlagStampaSiep("S");
			lEveMod.getEvento().setFlagVideoSiep("S");
			lEveMod.getEvento().setCodEsito("-");
			lEveMod.getEvento().setCodLuogoDestinatario("-");
			lEveMod.getEvento().setCodTipoUfficioDestinatario("-");
			lEveMod.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
			lEveMod.getEvento().setDataInserimento(DateUtils.getSysDate());
			lEveMod.getEvento().setCodUfficioInserimento(lCodiceUfficio);
			lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
			String lCodiceUff = "";
			if (!this
					.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_ENTE_SORVEGLIANZA)
					&& !this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_TDS)) {
				lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(
						getRequestStringParameter(
								ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_ENTE_SORVEGLIANZA),
						getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS));
				lEveMod.getEvento().setCodUfficioEmittente(lCodiceUff);
			}

			// NOTIFICHE PROVVEDIMENTO
			NotificaModel[] lNotificheMod = this.setNotificheOrdineEsecuzione();
			lEveMod.setNotifiche(lNotificheMod);

			// DATA FINE PENA
			PenaResiduaModel lPenaRes = new PenaResiduaModel();
			lPenaRes.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
			// String lFc = "0";
			// if (!isRequestParameterNullObj(FOGLIO_COMPLEMENTARE))
			// lFc = "1";

			lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaRevocaLSNotifica(lEveMod, lPenaRes,
					lMotEve);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordineesecuzione.action.ActDettaglioRevocaLSAltrePosizioni&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		}

		return lPage;
	}

}