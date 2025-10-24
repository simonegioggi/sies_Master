package siap.siep.misuraalternativa.action;

/**
 * <p>Title: ActInserisciSospProvv</p>
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
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.tenore.model.TenoreModel;

public class ActInserisciSospProvv extends ActSospensioneProvvisoria {

	/**
	 * Azione di Inserimento del MisuraAlternativa
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();

		// setto la natura della MA per chiamare due metodi diversi
		String tipoMisura = getRequestStringParameter("tipomisura");
		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		String PosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		this.setRequestAttribute("posizionegiuridica", PosizioneGiu);

		String lPage = null;

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModSospesa = null;
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lMisAlModSospesa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		// non c'è un decreto di sospensione in misura alternativa
		if (lMisAlModSospesa == null) {
			// INSERISCO EVENTO E NOTIFICA DEL MDS
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

			lEveMod.setEvento(setEventoOrdinazaDecretoMisuraAlternativa(lEveMod.getEvento(), "02",
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// setto il deposito decreto
			DepositoDecretoModel lDepDecMod = setDepositoDecreto(lCodiceUffEmi);

			// setto il tenore
			TenoreModel lTenMod = setTenore(new BigDecimal(1), "0062");

			// ricerco Pena residua
			// PenaResiduaModel lPenaResMod = new PenaResiduaModel();
			// IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			// lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel
			// .getIdFascicoloSiep());

			// misura aletrantiva
			String lUfficioScarc = "-";
			if (this.getRequestStringParameter("tipo").equals("mds"))
				lUfficioScarc = "SORV";
			else if (this.getRequestStringParameter("tipo").equals("procura"))
				lUfficioScarc = "PROC";

			lMisMod = setMisuraAlternativa("02", "SP", lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), lUfficioScarc);

			if (!this
					.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO)
					&& !this.isRequestParameterNullObj(
							ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO)
					&& !this.isRequestParameterNullObj(
							ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO)
					&& !this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO).equals("")
					&& !this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO).equals("")
					&& !this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO).equals("")) {
				lMisMod.setDataIngressoIstituto(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO));
				lMisMod.setDataInizioMisura(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO));
			} else {

				lMisMod.setDataInizioMisura(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			}

			// MEV10-s3: anticipo questo metodo per prevenire errore inserimento dati ufficio UDS / TDS
			NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();

			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraModel = lCtrlMisura.ExInserisciDecretoSospEventoNotifica(lEveMod,
					lDepDecMod, lTenMod, lMisMod);

			// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
			EventoNotificaModel lEveNot = new EventoNotificaModel();

			String lProcSorv = this.getRequestStringParameter("tipo");
			String lCodiceMotivo = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO);

			lEveNot = getCodiceMotivoTipoEventoSospensioni(lCodiceMotivo, tipoMisura, lProcSorv);
			lEveNot.setEvento(setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));
			lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());

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
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMASospProvv&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();
		} else {
			String codiceMotivo = lMisAlModSospesa.getCodTipoMisura();

			EventoNotificaModel lEve = new EventoNotificaModel();
			this.setRequestAttribute("tipoMisura", tipoMisura);
			String lProcSorv = getRequestStringParameter("tipo");

			lEve = getCodiceMotivoTipoEventoSospensioni(codiceMotivo, tipoMisura, lProcSorv);
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

			lMisAlModSospesa.setCodUfficioAggiornamento(lCodiceUfficio);
			lMisAlModSospesa.setCodOperatoreAggiornamento(lCodiceOperatore);
			lMisAlModSospesa.setDataAggiornamento(DateUtils.getSysDate());

			if (!this
					.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO)
					&& !this.isRequestParameterNullObj(
							ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO)
					&& !this.isRequestParameterNullObj(
							ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO)
					&& !this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO).equals("")
					&& !this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO).equals("")
					&& !this.getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO).equals("")) {
				lMisAlModSospesa.setDataIngressoIstituto(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO));
				lMisAlModSospesa.setDataInizioMisura(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO));
			} else {
				lMisAlModSospesa.setDataInizioMisura(getRequestDateParameter(
						ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			}

			if (this.getRequestStringParameter("tipo").equals("mds"))
				lMisAlModSospesa.setCodTipoUfficioScarcerazione("SORV");
			else if (this.getRequestStringParameter("tipo").equals("procura"))
				lMisAlModSospesa.setCodTipoUfficioScarcerazione("PROC");

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
				lMisAlModSospesa.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

			EventoNotificaModel lRetModel = new EventoNotificaModel();
			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			lRetModel = lCtrlMisura.ExInserisciOModificaMANotifica(lEve, lPenaRes, lMisAlModSospesa, null);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioMASospProvv&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		}
		return lPage;
	}

}