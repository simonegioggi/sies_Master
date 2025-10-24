package siap.siep.misuraalternativa.action;

/**
 * <p>Title: ActInserisciMAProrogaProvvDetDomTemp</p>
 * <p>Description: Classe Action per l'inserimento di MisuraAlternativa</p>
 * <p>Copyright: Copyright (c) 2006</p>
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
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.tenore.model.TenoreModel;

public class ActInserisciMAProrogaProvvDetDomTemp extends ActMisuraAlternativa
		implements ICostantiMisuraAlternativa {

	/**
	 * Azione di Inserimento del MisuraAlternativa proroga provvisoria detenzione domiciliare a termine
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// String lCodiceOperatore = this.getCodUtenteConnesso();
		// String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		EventoNotificaModel lRetModel = new EventoNotificaModel();

		String PosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		this.setRequestAttribute("posizionegiuridica", PosizioneGiu);
		PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
		lPosMod.setCodPosizioneGiuridica(PosizioneGiu);

		// posizione giuridica precedente
		// PosizioneGiuridicaModel lPosPre = new PosizioneGiuridicaModel();
		// IPosizioneGiuridica lCtrPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		// lPosPre = lCtrPos.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascicoloModel
		// .getIdFascicoloSiep());

		String lPage = null;

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModConcessa = null;
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		// EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		if (lMisAlModConcessa == null) {
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
			lEveMod.setEvento(setEventoOrdinanzaDecretoMisuraAlternativa(lEveMod.getEvento(), "02",
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// setto il deposito ordinanza
			DepositoDecretoModel lDepDecMod = this.setDepositoDecreto(lCodiceUffEmi);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA)) {
				lDepDecMod.setLuogoSvolgimentoProva(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));
			}

			// setto il tenore
			TenoreModel lTenMod = setTenore(new BigDecimal(1), "0001");

			// ricerco Pena residua
			// PenaResiduaModel lPenaResMod = new PenaResiduaModel();
			// IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			// lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel
			// .getIdFascicoloSiep());

			// misura alternativa
			String lUfficioScar = "-";
			if (PosizioneGiu.equals("03")) {
				if (this.getRequestStringParameter("tipo").equals("scarcerato"))
					lUfficioScar = "SORV";
				else if (this.getRequestStringParameter("tipo").equals("scarcerare"))
					lUfficioScar = "PROC";
			}
			lMisMod = setMisuraAlternativa("02", "DD", lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), lUfficioScar);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE))
				lMisMod.setDataScarcerazione(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCADENZA_PROROGA)
					&& this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCADENZA_PROROGA) != null
					&& !this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCADENZA_PROROGA).equals("")) {
				lMisMod.setDataScadenzaProroga(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCADENZA_PROROGA,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCADENZA_PROROGA,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCADENZA_PROROGA));
			}

			if (!this.isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA))
				lMisMod.setCssIdCssa(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
				lMisMod.setDescrLuogoProva(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

			// Controllo sul Check
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE))
				lMisMod.setFlagDecisioneTribunale("1");

			// MEV10-s3: anticipo questo metodo per prevenire errore inserimento dati ufficio UDS / TDS
			NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();

			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraModel = lCtrlMisura.ExInserisciDecretoSospEventoNotifica(lEveMod,
					lDepDecMod, lTenMod, lMisMod);

			// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
			EventoNotificaModel lEveNot = new EventoNotificaModel();
			lEveNot.getEvento().setCodTipoEvento("01");
			lEveNot.getEvento().setCodTipoProvvedimento("12");
			if ("03".equals(PosizioneGiu) || "12".equals(PosizioneGiu))
				lEveNot.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			else
				lEveNot.getEvento().setCodMotivo("0000");

			lEveNot.getEvento().setCodEsito("-");
			lEveNot.setEvento(setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));
			lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());

			// notifiche
			// NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();
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
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMAProrogaProvvDetDomTemp&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();
		} else {
			// la misura alternativa esiste
			EventoNotificaModel lEve = new EventoNotificaModel();
			lEve.getEvento().setCodTipoEvento("01");
			lEve.getEvento().setCodTipoProvvedimento("12");
			if ("03".equals(PosizioneGiu) || "12".equals(PosizioneGiu))
				lEve.getEvento().setCodMotivo(lMisAlModConcessa.getCodTipoMisura());
			else
				lEve.getEvento().setCodMotivo("0000");

			lEve.getEvento().setCodEsito("-");
			lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
			lEve.getEvento().setEveIdEvento(lIdOrdinanza);

			// Inserisco l'array di Notifiche nell'Evento
			NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
			lEve.setNotifiche(lNotifiche);
			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();

			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
			PenaResiduaModel lPenaRes = new PenaResiduaModel();
			lPenaRes.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCADENZA_PROROGA)
					&& this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCADENZA_PROROGA) != null
					&& !this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCADENZA_PROROGA).equals("")) {
				lMisAlModConcessa.setDataScadenzaProroga(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCADENZA_PROROGA,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCADENZA_PROROGA,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCADENZA_PROROGA));
				lMisAlModConcessa.setFlagDecisioneTribunale(null);
			}

			// Controllo sul Check
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE)) {
				lMisAlModConcessa.setFlagDecisioneTribunale("1");
				lMisAlModConcessa.setDataScadenzaProroga(null);
			}

			if (PosizioneGiu.equals("03")) {
				if (this.getRequestStringParameter("tipo").equals("scarcerato")) {
					lMisAlModConcessa.setCodTipoUfficioScarcerazione("SORV");

					lMisAlModConcessa.setDataScarcerazione(
							getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
									ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
									ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));
				} else if (this.getRequestStringParameter("tipo").equals("scarcerare")) {
					lMisAlModConcessa.setCodTipoUfficioScarcerazione("PROC");
				}
			}

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
				lMisAlModConcessa.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

			lRetModel = lCtrlMisura.ExInserisciOModificaMANotifica(lEve, lPenaRes, lMisAlModConcessa, null);
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMAProrogaProvvDetDomTemp&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
		}

		return lPage;
	}

}