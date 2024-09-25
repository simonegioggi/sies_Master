package siap.siep.misuraalternativa.action;

/**
 * <p>Title: ActInserisciRipristinoMA</p>
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
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

public class ActInserisciRipristinoMA extends ActRipristino {

	/**
	 * Azione di Inserimento del MisuraAlternativa
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
		String tipoMisura = getRequestStringParameter("tipomisura");
		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		String PosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		this.setRequestAttribute("posizionegiuridica", PosizioneGiu);
		String lPage = null;

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisuraMod = null;
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lMisuraMod = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		if (lMisuraMod == null) {
			// INSERISCO EVENTO E NOTIFICA
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
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
				lDepOrdMod.setLuogoSvolgimentoProva(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

			// setto il tenore
			TenoreModel lTenMod = setTenore(new BigDecimal(1), "0002");

			// ricerco Pena residua
			// PenaResiduaModel lPenaResMod = new PenaResiduaModel();
			// IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			// lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel
			// .getIdFascicoloSiep());

			// misura alternativa
			Date lDataInizioMisura = null;
			String lTipoUffScar = "-";

			if (!this.isRequestParameterNullObj("tipo")) {
				String lTipo = getRequestStringParameter("tipo");
				if (lTipo.equals("procura")) {
					lTipoUffScar = "PROC";
					lDataInizioMisura = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
				}
				if (lTipo.equals("mds")) {
					lTipoUffScar = "SORV";
					lDataInizioMisura = getRequestDateParameter(
							ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
							ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE);
				}
			}

			lMisMod = setMisuraAlternativa("03", "RG", lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), lTipoUffScar);

			if (lDataInizioMisura != null) {
				lMisMod.setDataInizioMisura(lDataInizioMisura);

				if (lTipoUffScar.equals("SORV"))
					lMisMod.setDataScarcerazione(lDataInizioMisura);
			}

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
				lMisMod.setDescrLuogoProva(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

			lMisMod.setCssIdCssa(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
				lMisMod.setDescrLuogoProva(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

			// MEV10-s3: anticipo questo metodo per prevenire errore inserimento dati ufficio UDS / TDS
			NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();

			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraModel = lCtrlMisura
					.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod, lTenMod, lMisMod);

			// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
			EventoNotificaModel lEveNot = new EventoNotificaModel();
			if (lMisMod != null && lMisMod.getCodTipoUfficioScarcerazione() != null
					&& lMisMod.getCodTipoUfficioScarcerazione().equals("PROC"))
				lEveNot.getEvento().setCodTipoProvvedimento("24");
			else if (lMisMod != null && lMisMod.getCodTipoUfficioScarcerazione() != null
					&& lMisMod.getCodTipoUfficioScarcerazione().equals("SORV"))
				lEveNot.getEvento().setCodTipoProvvedimento("12");
			else
				lEveNot.getEvento().setCodTipoProvvedimento("04");

			String lCodMotivo = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO);
			lEveNot.getEvento().setCodMotivo(getChangeMotivo(lCodMotivo));

			lEveNot.setEvento(setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));
			lEveNot.getMagistrato().setCodMagistrato(this.calcolaMagistrato());
			lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());

			// notifica
			// NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();
			lEveNot.setNotifiche(lNotificheMod);

			// penaresidua
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
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMARipristino&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();
		} else {
			// la misura alternativa esiste
			String codiceMotivo = lMisuraMod.getCodTipoMisura();
			EventoNotificaModel lEve = new EventoNotificaModel();
			this.setRequestAttribute("tipoMisura", tipoMisura);

			if (getRequestStringParameter("tipo").equals("procura"))
				lEve.getEvento().setCodTipoProvvedimento("24");
			else if (getRequestStringParameter("tipo").equals("mds"))
				lEve.getEvento().setCodTipoProvvedimento("12");
			else
				lEve.getEvento().setCodTipoProvvedimento("04");

			lEve.getEvento().setCodMotivo(getChangeMotivo(codiceMotivo));

			lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
			lEve.getMagistrato().setCodMagistrato(calcolaMagistrato());
			lEve.getEvento().setEveIdEvento(lIdOrdinanza);

			NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
			lEve.setNotifiche(lNotifiche);

			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
			PenaResiduaModel lPenaRes = new PenaResiduaModel();
			lPenaRes.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			lMisuraMod.setCodUfficioAggiornamento(lCodiceUfficio);
			lMisuraMod.setCodOperatoreAggiornamento(lCodiceOperatore);
			lMisuraMod.setDataAggiornamento(DateUtils.getSysDate());

			if (this.getRequestStringParameter("tipo").equals("mds")) {
				lMisuraMod.setCodTipoUfficioScarcerazione("SORV");
				lMisuraMod.setDataScarcerazione(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));

				lMisuraMod.setDataInizioMisura(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));
			} else if (this.getRequestStringParameter("tipo").equals("procura")) {
				lMisuraMod.setCodTipoUfficioScarcerazione("PROC");
				lMisuraMod.setDataInizioMisura(getRequestDateParameter(
						ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			}

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
				lMisuraMod.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

			// Inserisco l'array di Notifiche nell'Evento
			EventoNotificaModel lRetModel = new EventoNotificaModel();
			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			lRetModel = lCtrlMisura.ExInserisciOModificaMANotifica(lEve, lPenaRes, lMisuraMod, null);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMARipristino&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
		}
		return lPage;
	}

}