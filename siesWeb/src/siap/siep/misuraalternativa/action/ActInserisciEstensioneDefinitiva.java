package siap.siep.misuraalternativa.action;

/**
 * <p>Title: ActInserisciEstensioneProvvisoria</p>
 * <p>Description: Classe Action per l'inserimento di MisuraAlternativa</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

public class ActInserisciEstensioneDefinitiva extends ActEstensioneDefinitiva {
	/**
	 * Azione di Inserimento della MisuraAlternativa di Estensione Definitiva
	 * 
	 * @return Nome della azione di dettaglio al termine dell'elaborazione
	 * @throws F3BException
	 */

	public String processRequest() throws F3BException {
		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// String lCodiceOperatore = this.getCodUtenteConnesso();
		// String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		String lPage = null;
		String tipoMisura = this.getRequestStringParameter("tipomisura");

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascicoloModel.getIdFascicoloSiep());

		// Cerca gli Eventi "validati" con motivo "2005"
		// Instanzia il model dell'evento
		EventoModel lEvent = new EventoModel();

		// carica nel model il Tipo evento
		lEvent.setCodTipoEvento("01");
		// carica nel model il flag documento registrato
		lEvent.setFlagDocumentoRegistrato("S");
		// carica nel model il tipo motivo
		String[] lMotivo = { "2005" };
		// carica nel model il codice dei provvedimenti
		String[] lProvv = { "04", "09", "12" };

		// Ricerca nella tabella EVENTO
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEveAmm = lCtrlEvento.ExRicercaEventoPerMotivoPerProvv(lMotivo, lProvv, lEvent);

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisuraAmm = null;
		if (lEveAmm != null && lEveAmm.getEveIdEvento() != null) {
			lMisuraAmm = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveAmm.getEveIdEvento());
		}

		MisuraAlternativaModel lMisuraMod = null;
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lMisuraMod = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		// se non è presente la misuralaternativa concessa la inserisco
		if (lMisuraMod == null) {
			// INSERISCO EVENTO E NOTIFICA DEL TDS
			EventoNotificaModel lEveMod = new EventoNotificaModel();
			lEveMod.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			String lCodiceUffEmi = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));
			ComuneModel lComModAutEmi = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));
			Date lDataEmisTras = getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);
			lEveMod.setEvento(setEventoOrdinanzaDecretoMisuraAlternativa(lEveMod.getEvento(), "03",
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// setto il deposito ordinanza
			DepositoOrdinanzaPcModel lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);
			lDepOrdMod.setIdCssaComp(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));

			// setto il tenore
			TenoreModel lTenMod = setTenore(new BigDecimal(1), "0009");

			// ricerco Pena residua
			// PenaResiduaModel lPenaResMod = new PenaResiduaModel();
			IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			/* lPenaResMod = */lPenResCtrl
					.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

			// setto la misuraalternativa
			String lNatura = null;
			if (tipoMisura != null && (tipoMisura.equals("AFFIDAMENTOCUMULO")
					|| tipoMisura.equals("DETENZIONECUMULO") || tipoMisura.equals("SEMILIBERTACUMULO")))
				lNatura = "EC";
			else
				lNatura = "ED";

			lMisMod = setMisuraAlternativa("03", lNatura, lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), "-");
			lMisMod.setCssIdCssa(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));

			// a7/rr/325 per la posizione 29 la data inizio misura è uguale a quella dell'ammissione
			// provvisoria
			if (lPos.getPosizioneGiuridica() != null
					&& "29".equals(lPos.getPosizioneGiuridica().getCodPosizioneGiuridica())
					&& lMisuraAmm != null && lMisuraAmm.getDataInizioMisura() != null) {
				lMisMod.setDataInizioMisura(lMisuraAmm.getDataInizioMisura());
			} else {
				lMisMod.setDataInizioMisura(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

			}
			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraModel = lCtrlMisura
					.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod, lTenMod, lMisMod);

			// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
			EventoNotificaModel lEveNot = new EventoNotificaModel();
			lEveNot = getCodiceMotivoTipoEventoEstensione(tipoMisura,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			lEveNot.setEvento(setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));
			lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());
			lEveNot.getMagistrato().setCodMagistrato(this.calcolaMagistrato());

			// notifica provvedimento
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
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMAEstensioneDefinitiva&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();

		} else // la misura alternativa esiste
		{
			EventoNotificaModel lEve = new EventoNotificaModel();

			lEve = getCodiceMotivoTipoEventoEstensione(tipoMisura, lMisuraMod.getCodTipoMisura());
			lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
			lEve.getMagistrato().setCodMagistrato(this.calcolaMagistrato());
			lEve.getEvento().setEveIdEvento(lIdOrdinanza);

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

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
				lMisuraMod.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

			// a7/rr/325 per la posizione 29 la data inizio misura è uguale a quella dell'ammissione
			// provvisoria
			if (lPos.getPosizioneGiuridica() != null
					&& "29".equals(lPos.getPosizioneGiuridica().getCodPosizioneGiuridica())
					&& lMisuraAmm != null && lMisuraAmm.getDataInizioMisura() != null) {
				lMisuraMod.setDataInizioMisura(lMisuraAmm.getDataInizioMisura());
			}

			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			EventoNotificaModel lRetModel = lCtrlMisura.ExInserisciOModificaMANotifica(lEve, lPenaRes,
					lMisuraMod, null);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMAEstensioneDefinitiva&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		}

		return lPage;
	}
}