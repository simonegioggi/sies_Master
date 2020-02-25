package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;

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
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * Inserimento degli Ordini di Revoca Alfano
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
public class ActInserisciRevocaSospensioneAlfano extends ActOrdineEsecuzione implements
		ICostantiOrdineEsecuzione {

	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		String lCodiceLuogo = this.getCodComuneUtenteConnesso();

		String lPosizione = this
				.getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		EventoNotificaModel lRetModel = new EventoNotificaModel();

		// ricerco Pena residua
//		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		/*lPenaResMod = */lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel
				.getIdFascicoloSiep());

//		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		/*lPos = */lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascicoloModel
						.getIdFascicoloSiep());

		String lPage = "";

		// Motivo_Evento
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		MotivoEventoModel lMotEve = new MotivoEventoModel();
		lMotEve.setCodMotivoRevoca(getRequestStringParameter(ICostantiMotivoEvento.CAMPO_COD_MOTIVO_REVOCA));
		if (lMotEve.getCodMotivoRevoca().equals("0001"))// revoca pm
		{
			lMotEve.setCodMotivoRevocaPm(getRequestStringParameter(ICostantiMotivoEvento.CAMPO_COD_MOTIVO_REVOCA_PM));
			lMotEve.setMotivazioni(getRequestStringParameter(ICostantiMotivoEvento.CAMPO_MOTIVAZIONI));
		} else
			lMotEve.setCodMotivoRevocaPm("-");

		if (lMotEve.getCodMotivoRevoca().equals("0002"))// reiezione
		{
			IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraMod = null;
			BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
//			EventoNotificaModel lEveNotMod = new EventoNotificaModel();
			if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
				lMisuraMod = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

			if (lMisuraMod == null) {

				// INSERISCO EVENTO E NOTIFICA DEL TDS
				EventoNotificaModel lEveMod = new EventoNotificaModel();
				EventoModel lEve = new EventoModel();
				lEve.setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
				lEve.setCodTipoEvento("01");
				// TODO INIZIO codice sovrascritto con intervento 14 della MEV 29 (fatta da Diego Forletta)
				lEve.setCodTipoProvvedimento("03");
				// lEve.setCodTipoProvvedimento("12");
				// TODO INIZIO codice sovrascritto con intervento 14 della MEV 29 (fatta da Diego Forletta)
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

				/* 
				 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato
				 * Numero MEV : SIES v10
				 * Autore    : gioggi
				 * Data      : 28/gen/2016
				 * Branch    : MEV_SIES v10
				 */
//				lEve.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
//						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
//						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
//				lEve.setDataTrasmissioneAtti(getRequestDateParameter(
//						ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
//						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
				//***** FINE INTERVENTO MEV_SIES v10 *****//

				Date lDataEmissOrd = getRequestDateParameter(
						ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
						ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);
				lEve.setDataEmissione(lDataEmissOrd);
				lEve.setDataTrasmissioneAtti(lDataEmissOrd);

				lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
				lEve.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
				lEve.setFlagStampaSiep("N");
				lEve.setFlagVideoSiep("S");
				
	      // 30-05-2016 - MEV_YY da rilasciare dopo Primo Collaudo per V.10
	      //lEve.setFlagDocumentoRegistrato("S");
	      // 30-05-2016 - END MEV_YY
				lEveMod.setEvento(lEve);

				// setto le notifiche
				NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
				lEveMod.setNotifiche(lNotifiche);

				// setto il deposito ordinanza
				DepositoOrdinanzaPcModel lDepOrdMod = new DepositoOrdinanzaPcModel();
				lDepOrdMod
						.setAnnoS3(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
				lDepOrdMod
						.setNumS3(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));
				
				/* 
				 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato
				 * Numero MEV : SIES v10
				 * Autore    : gioggi
				 * Data      : 28/gen/2016
				 * Branch    : MEV_SIES v10
				 */
//				lDepOrdMod.setDataUdienza(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
//						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
//						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
				//***** FINE INTERVENTO MEV_SIES v10 *****//

				lDepOrdMod.setDataUdienza(lDataEmissOrd);

				lDepOrdMod.setCodUfficioInserimento(lCodiceUfficio);
				lDepOrdMod.setCodOperatoreInserimento(lCodiceOperatore);
				lDepOrdMod.setDataInserimento(DateUtils.getSysDate());

				// setto il tenore
				TenoreModel lTenMod = new TenoreModel();
				lTenMod.setCodEsitoTenore("0002");

				/* 
				 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato
				 * Numero MEV : SIES v10
				 * Autore    : gioggi
				 * Data      : 28/gen/2016
				 * Branch    : MEV_SIES v10
				 */
//				lTenMod.setData(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
//				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
//				ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
				//***** FINE INTERVENTO MEV_SIES v10 *****//
				
				lTenMod.setData(lDataEmissOrd);

				lTenMod.setCodOggettoTenore(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
				lTenMod.setProgrTenore(new BigDecimal(1));
				lTenMod.setCodUfficioInserimento(lCodiceUfficio);
				lTenMod.setCodOperatoreInserimento(lCodiceOperatore);
				lTenMod.setDataInserimento(DateUtils.getSysDate());

				// setto la misuraalternativa
				lMisMod.setCodTipoDecisione("03");
				lMisMod.setCodNaturaDecisione("RG");
				lMisMod.setCodTipoMisura(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
				lMisMod.setDataDecisione(getRequestDateParameter(
						ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
						ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE));
				lMisMod.setFlagUfficioInserimento("P");
				lMisMod.setChiaveUfficioFascicoloSius(lCodiceUff);
				lMisMod.setAnnoRegistro(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
				lMisMod.setNumeroRegistro(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));
				lMisMod.setChiaveProgrFascicoloSius(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS));
				lMisMod.setChiaveAnnoFascicoloSius(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS));
				lMisMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

				lMisMod.setCodUfficioInserimento(lCodiceUfficio);
				lMisMod.setCodOperatoreInserimento(lCodiceOperatore);
				lMisMod.setDataInserimento(DateUtils.getSysDate());
				lMisMod.setCodTipoUfficioScarcerazione("-");
				lMisMod.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE + "_MA"));// n.b.
																											// aggiunto
																											// MS
																											// per
																											// conflitto
																											// nomi
																											// campi
																											// destinatari

				IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
				MisuraAlternativaModel lMisuraModel = lCtrlMisura.ExInserisciMisuraAlternativaEventoNotifica(
						lEveMod, lDepOrdMod, lTenMod, lMisMod);

				// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
				EventoNotificaModel lEveNot = new EventoNotificaModel();
				// lEveNot.getEvento().setCodTipoProvvedimento("12");
				// TODO INIZIO codice sovrascritto con intervento 14 della MEV 29 (fatta da Diego Forletta)
				lEveNot.getEvento().setCodTipoProvvedimento("04");
				// TODO INIZIO codice sovrascritto con intervento 14 della MEV 29 (fatta da Diego Forletta)
				if (lPosizione.equals("07") || lPosizione.equals("10") || lPosizione.equals("16")
						|| lPosizione.equals("17") || lPosizione.equals("46") || lPosizione.equals("47")) {
					lEveNot.getEvento().setCodMotivo("0495");
				} else if (lPosizione.equals("01") || lPosizione.equals("03") || lPosizione.equals("73")) {
					lEveNot.getEvento().setCodMotivo("0496");
					lEveNot.getEvento().setCodTipoProvvedimento("12");
				} else if (lPosizione.equals("02") || lPosizione.equals("53") || lPosizione.equals("70")
						|| lPosizione.equals("71") || lPosizione.equals("72")) {
					lEveNot.getEvento().setCodTipoProvvedimento("12");
					lEveNot.getEvento().setCodMotivo("0497");
				}
				// MEV 10 S3 *********
				else if (lPosizione.equals("85")) {
					lEveNot.getEvento().setCodTipoProvvedimento("12");
					lEveNot.getEvento().setCodMotivo("5530");
				} else if (lPosizione.equals("86")) {
					lEveNot.getEvento().setCodTipoProvvedimento("12");
					lEveNot.getEvento().setCodMotivo("5531");
				} else if (lPosizione.equals("87")) {
					lEveNot.getEvento().setCodTipoProvvedimento("12");
					lEveNot.getEvento().setCodMotivo("5532");
				}
				// *******************
				else {
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
						.ExInserisciOModificaMANotificaRevocaLAlf(lEveNot, lPenaRes, null, lMotEve);

//				String lFc = "0";
//				if (!isRequestParameterNullObj(FOGLIO_COMPLEMENTARE))
//					lFc = "1";

				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.ordineesecuzione.action.ActDettaglioRevocaLAlfAltrePosizioni&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();

			} else {
				// la misura alternativa esiste
				EventoNotificaModel lEve = new EventoNotificaModel();
				lEve.getEvento().setCodTipoProvvedimento("04");

				if (lPosizione.equals("07") || lPosizione.equals("10") || lPosizione.equals("16")
						|| lPosizione.equals("17") || lPosizione.equals("46") || lPosizione.equals("47")) {
					lEve.getEvento().setCodMotivo("0495");
				} else if (lPosizione.equals("01") || lPosizione.equals("03") || lPosizione.equals("73")) {
					lEve.getEvento().setCodMotivo("0496");
					lEve.getEvento().setCodTipoProvvedimento("12");
				} else if (lPosizione.equals("02") || lPosizione.equals("53") || lPosizione.equals("70")
						|| lPosizione.equals("71") || lPosizione.equals("72")) {
					lEve.getEvento().setCodMotivo("0497");
					lEve.getEvento().setCodTipoProvvedimento("12");
				}
				// MEV 10 S3 *********
				else if (lPosizione.equals("85")) {
					lEve.getEvento().setCodMotivo("5530");
					lEve.getEvento().setCodTipoProvvedimento("12");
				} else if (lPosizione.equals("86")) {
					lEve.getEvento().setCodMotivo("5531");
					lEve.getEvento().setCodTipoProvvedimento("12");
				} else if (lPosizione.equals("87")) {
					lEve.getEvento().setCodMotivo("5532");
					lEve.getEvento().setCodTipoProvvedimento("12");
				}
				// *******************
				else {
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

				lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaMANotificaRevocaLAlf(lEve, lPenaRes,
						lMisuraMod, lMotEve);

//				String lFc = "0";
//				if (!isRequestParameterNullObj(FOGLIO_COMPLEMENTARE))
//					lFc = "1";

				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.ordineesecuzione.action.ActDettaglioRevocaLAlfAltrePosizioni&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
			}
		} else // omessa e revoca pm
		{

			// EVENTO PROVVEDIMENTO
			EventoNotificaModel lEveMod = new EventoNotificaModel();
			lEveMod.getEvento().setCodTipoEvento("01");
			lEveMod.getEvento().setCodTipoProvvedimento("04");

			if (lPosizione.equals("07") || lPosizione.equals("10") || lPosizione.equals("16")
					|| lPosizione.equals("17") || lPosizione.equals("46") || lPosizione.equals("47")) {
				lEveMod.getEvento().setCodMotivo("0495");
			} else if (lPosizione.equals("01") || lPosizione.equals("03") || lPosizione.equals("73")) {
				lEveMod.getEvento().setCodMotivo("0496");
				lEveMod.getEvento().setCodTipoProvvedimento("12");
			} else if (lPosizione.equals("02") || lPosizione.equals("53") || lPosizione.equals("70")
					|| lPosizione.equals("71") || lPosizione.equals("72")) {
				lEveMod.getEvento().setCodMotivo("0497");
				lEveMod.getEvento().setCodTipoProvvedimento("12");
			}
			// MEV 10 S3 *********
			else if (lPosizione.equals("85")) {
				lEveMod.getEvento().setCodMotivo("5530");
				lEveMod.getEvento().setCodTipoProvvedimento("12");
			} else if (lPosizione.equals("86")) {
				lEveMod.getEvento().setCodMotivo("5531");
				lEveMod.getEvento().setCodTipoProvvedimento("12");
			} else if (lPosizione.equals("87")) {
				lEveMod.getEvento().setCodMotivo("5532");
				lEveMod.getEvento().setCodTipoProvvedimento("12");
			}
			// *******************
			else
				lEveMod.getEvento().setCodMotivo("0000");

			lEveMod.getEvento().setDataEmissione(
					getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lEveMod.getEvento().setCodUfficioEmittente(lCodiceUfficio);
			lEveMod.getEvento().setCodLuogoEmittente(lCodiceLuogo);
			lEveMod.getEvento().setCodMagistrato(
					getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			lEveMod.getEvento().setFlagStampaSiep("S");
			lEveMod.getEvento().setFlagVideoSiep("S");
			lEveMod.getEvento().setCodEsito("-");
			lEveMod.getEvento().setCodLuogoDestinatario("-");
			lEveMod.getEvento().setCodTipoUfficioDestinatario("-");
			lEveMod.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
			lEveMod.getEvento().setDataInserimento(DateUtils.getSysDate());
			lEveMod.getEvento().setCodUfficioInserimento(lCodiceUfficio);
			lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

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

//			String lFc = "0";
//			if (!isRequestParameterNullObj(FOGLIO_COMPLEMENTARE))
//				lFc = "1";

			lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaRevocaLAlfNotifica(lEveMod, lPenaRes,
					lMotEve);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordineesecuzione.action.ActDettaglioRevocaLAlfAltrePosizioni&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		}

		return lPage;
	}

}