package siap.siep.misuraalternativa.action;

/**
 * <p>Title: ActInserisciMAPerditaEfficacia</p>
 * <p>Description: Classe Action per l'inserimento di MisuraAlternativa</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.cssa.action.ICostantiCSSA;
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
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.refertoscarcerazione.action.ICostantiRefertoScarcerazione;
import siap.siep.refertoscarcerazione.controller.IRefertoScarcerazione;
import siap.siep.refertoscarcerazione.model.RefertoScarcerazioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.tenore.model.TenoreModel;

public class ActInserisciMAPerditaEfficacia extends ActMisuraAlternativa
		implements ICostantiMisuraAlternativa {

	/**
	 * Azione di Inserimento del MisuraAlternativa
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		String lCodiceLuogo = this.getCodComuneUtenteConnesso();
		// String tipoMisura = this.getRequestStringParameter("tipoSospensione");
		String lPage = null;

		EventoNotificaModel lEveMod = new EventoNotificaModel();
		EventoModel lEve = new EventoModel();
		RefertoScarcerazioneModel lRefScaMod = new RefertoScarcerazioneModel();
		MisuraAlternativaModel lMiModel = new MisuraAlternativaModel();

		String lFlagReferto = this.getRequestStringParameter("lFlagReferto");
		BigDecimal lIdEve = null;

		if (lFlagReferto.equals("S"))
			lIdEve = this.getRequestBigDecimalParameter("eventoreferto");

		String tipo = this.getRequestStringParameter("tipo");
		String lCodMotivo = "";
		if (tipo.equals("COMUNICAZIONE"))
			lCodMotivo = getRequestStringParameter("CodMotivoComunicazione");
		else
			lCodMotivo = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO);

		if (tipo.equals("COMUNICAZIONE")) {
			if (lFlagReferto.equals("N")) {
				// EVENTO REFERTO_SCARCERAZIONE
				lEve.setCodMotivo("0315");
				lEve.setCodTipoEvento("14");
				lEve.setCodTipoProvvedimento("19");
				lEve.setCodEsito("-");
				lEve.setCodOperatoreInserimento(lCodiceOperatore);
				lEve.setDataInserimento(DateUtils.getSysDate());
				lEve.setCodUfficioInserimento(lCodiceUfficio);
				lEve.setCodLuogoDestinatario("-");
				lEve.setCodTipoUfficioDestinatario("-");
				lEve.setCodUfficioEmittente(lCodiceUfficio);
				lEve.setCodLuogoEmittente(lCodiceLuogo);
				lEve.setDataEmissione(
						getRequestDateParameter(ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_SCARCERAZIONE,
								ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE,
								ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE));
				lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
				lEve.setFlagStampaSiep("S");
				lEve.setFlagVideoSiep("S");
				lEve.setFlagDocumentoRegistrato("S");
				lEve.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

				// REFERTO_SCARCERAZIONE
				lRefScaMod.setIstDetIdIstitutoDetenzione(getRequestStringParameter(
						ICostantiRefertoScarcerazione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
				lRefScaMod.setAnnoNota(
						this.getRequestBigDecimalParameter(ICostantiRefertoScarcerazione.CAMPO_ANNO_NOTA));
				lRefScaMod
						.setNumNota(getRequestStringParameter(ICostantiRefertoScarcerazione.CAMPO_NUM_NOTA));
				lRefScaMod.setDataNota(
						getRequestDateParameter(ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_NOTA,
								ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_NOTA,
								ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_NOTA));
				lRefScaMod.setDataScarcerazione(
						getRequestDateParameter(ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_SCARCERAZIONE,
								ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE,
								ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE));
				lRefScaMod.setNote(getRequestStringParameter(ICostantiRefertoScarcerazione.CAMPO_NOTE));
				lRefScaMod.setCodOperatoreInserimento(lCodiceOperatore);
				lRefScaMod.setDataInserimento(DateUtils.getSysDate());
				lRefScaMod.setCodUfficioInserimento(lCodiceUfficio);

				// MISURA ALTERNATIVA GENERICA PER FORZARE IL REFERTO SCARCERAZIONE COME SE FOSSE
				// UN'ORDINANZA--VIVIANA--DARIO
				lMiModel.setCodTipoDecisione("02");
				lMiModel.setCodNaturaDecisione("CE");
				lMiModel.setCodTipoMisura(lCodMotivo);
				lMiModel.setFlagUfficioInserimento("P");
				lMiModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
				lMiModel.setCodUfficioInserimento(lCodiceUfficio);
				lMiModel.setCodOperatoreInserimento(lCodiceOperatore);
				lMiModel.setDataInserimento(DateUtils.getSysDate());
				lMiModel.setDataDecisione(
						getRequestDateParameter(ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_SCARCERAZIONE,
								ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE,
								ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE));
				lMiModel.setCodTipoUfficioScarcerazione("-");
				lMiModel.setChiaveUfficioFascicoloSius("-");
			}

			// EVENTO PROVVEDIMENTO
			lEveMod.getEvento().setCodTipoEvento("01");
			lEveMod.getEvento().setCodTipoProvvedimento("12");
			lEveMod.getEvento().setCodMotivo(lCodMotivo);
			lEveMod.getEvento()
					.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			lEveMod.getEvento().setCodUfficioEmittente(lCodiceUfficio);
			lEveMod.getEvento().setCodLuogoEmittente(lCodiceLuogo);
			lEveMod.getEvento().setCodMagistrato(this.calcolaMagistrato());
			lEveMod.getEvento().setFlagStampaSiep("S");
			lEveMod.getEvento().setFlagVideoSiep("S");
			lEveMod.getEvento().setCodEsito("-");
			lEveMod.getEvento().setCodLuogoDestinatario("-");
			lEveMod.getEvento().setCodTipoUfficioDestinatario("-");
			lEveMod.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
			lEveMod.getEvento().setDataInserimento(DateUtils.getSysDate());
			lEveMod.getEvento().setCodUfficioInserimento(lCodiceUfficio);
			lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
			lEve.setCodMagistrato(this.calcolaMagistrato());

			// NOTIFICHE PROVVEDIMENTO
			NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
			lEveMod.setNotifiche(lNotifiche);

			// DATA FINE PENA
			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
			PenaResiduaModel lPenaRes = new PenaResiduaModel();
			lPenaRes.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			EventoModel lEventoMod = new EventoModel();
			IRefertoScarcerazione lRefScaCtrl = SIEPLookupRemote.getRefertoScarcerazioneRemote();
			if (lFlagReferto.equals("N"))
				lEventoMod = lRefScaCtrl.ExInserisciEventoRefertoScarcerazione(lEve, lRefScaMod, lEveMod,
						lPenaRes, lMiModel);
			else
				lEventoMod = lRefScaCtrl.ExInserisciEventoRefertoScarcerazione(lIdEve, lEveMod, lPenaRes);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMAPerditaEfficacia&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEventoMod.getIdEvento();
		} else if (tipo.equals("PROVVEDIMENTO")) {
			MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
			EventoNotificaModel lRetModel = new EventoNotificaModel();
			this.setRequestAttribute("posizionegiuridica", this
					.getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA));
			// String lPosizione = this
			// .getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

			// if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS)) {
			// String lUDS = this.getCodUfficioByCodTipoUfficioDescrComune("UDS",
			// getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS));
			// }

			IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisPerdita = null;

			BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
					ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);

			// EventoNotificaModel lEveNotMod = new EventoNotificaModel();
			if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
				lMisPerdita = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

			if (lMisPerdita == null) {
				// INSERISCO EVENTO E NOTIFICA DEL MDS
				lEveMod.getEvento().setCodMotivo(lCodMotivo);
				String lCodiceUffEmi = getCodUfficioByCodTipoUfficioDescrComune(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));
				ComuneModel lComModAutEmi = new ComuneModel(getCodComuneByDescr(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));
				Date lDataEmisTras = getRequestDateParameter(
						ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
						ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);
				lEveMod.setEvento(setEventoOrdinazaDecretoMisuraAlternativa(lEveMod.getEvento(), "02",
						lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

				// setto il deposito decreto
				DepositoDecretoModel lDepDecMod = setDepositoDecreto(lCodiceUffEmi);
				lDepDecMod.setCodMagistrato(this.calcolaMagistrato());
				if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
					lDepDecMod.setLuogoSvolgimentoProva(
							getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

				// setto il tenore
				TenoreModel lTenMod = setTenore(new BigDecimal(1), "0064");

				// setto la misuraalternativa
				lMisMod = setMisuraAlternativa("02", "PE", lCodiceUffEmi, lCodMotivo, "-");
				lMisMod.setCssIdCssa(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));
				if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
					lMisMod.setDescrLuogoProva(
							getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

				// MEV10-s3: anticipo questo metodo per prevenire errore inserimento dati ufficio UDS / TDS
				NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();

				IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
				MisuraAlternativaModel lMisuraModel = lCtrlMisura
						.ExInserisciDecretoSospEventoNotifica(lEveMod, lDepDecMod, lTenMod, lMisMod);

				// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
				EventoNotificaModel lEveNot = new EventoNotificaModel();

				lEveNot.getEvento().setCodTipoProvvedimento("12");
				lEveNot.getEvento().setCodMotivo(lCodMotivo);
				lEveNot.setEvento(setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));
				lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());
				lEveNot.getMagistrato().setCodMagistrato(this.calcolaMagistrato());

				// notifica
				// NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();
				lEveNot.setNotifiche(lNotificheMod);

				BigDecimal lIdPenaRes = getRequestBigDecimalParameter(
						ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
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
						+ "=siap.siep.misuraalternativa.action.ActDettaglioMAPerditaEfficacia&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();
			} else // la misura esiste
			{
				EventoNotificaModel lEven = new EventoNotificaModel();

				lEven.getEvento().setCodTipoProvvedimento("12");
				lEven.getEvento().setCodMotivo(lMisPerdita.getCodTipoMisura());
				lEven.setEvento(setEventoProvvedimentoMisuraAlternativa(lEven.getEvento()));
				lEven.getMagistrato().setCodMagistrato(
						getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
				lEven.getEvento().setEveIdEvento(lIdOrdinanza);

				// Inserisco l'array di Notifiche nell'Evento
				NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
				lEven.setNotifiche(lNotifiche);

				BigDecimal lIdPenaRes = getRequestBigDecimalParameter(
						ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
				PenaResiduaModel lPenaRes = new PenaResiduaModel();
				lPenaRes.setIdPenaResidua(lIdPenaRes);
				if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
					lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
							ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
							ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

				lMisPerdita.setCodTipoUfficioScarcerazione("-");
				if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
					lMisPerdita.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

				IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
				lRetModel = lCtrlMisura.ExInserisciOModificaMANotifica(lEven, lPenaRes, lMisPerdita, null);

				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misuraalternativa.action.ActDettaglioMAPerditaEfficacia&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
			}
		}
		return lPage;
	}

}