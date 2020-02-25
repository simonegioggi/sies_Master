package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaBackupSrc;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.w_magistrato.action.ICostantiWMagistrato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciAmmissioneADetDom extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {

	/**
	 * Azione di Inserimento dei MisuraAlternativa
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
		// setto la natura della MA per chiamare due metodi diversi
		String tipoMisura = getRequestStringParameter("tipomisura");
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		EventoNotificaModel lRetModel = new EventoNotificaModel();
		this.setRequestAttribute("posizionegiuridica",
				this.getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA));
//		String PosizioneGiu = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		String lPage = null;
		String lCodTipoUffSca = null;
		// non c'è un decreto di sospensione in misura alternativa
		if (this.getRequestStringParameter("flagmisura").equals("N")) {
			// non c'è un decreto di sospensione in misura alternativa lo inserisco

			// INSERISCO EVENTO E NOTIFICA DEL MDS

			EventoNotificaModel lEveMod = new EventoNotificaModel();
			EventoModel lEve = new EventoModel();
			lEve.setCodMotivo("0194");
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
			// lEve.setCodMagistrato(getRequestStringParameter("CodMagistrato_comp"));
			lEve.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

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

			// setto il deposito decreto
			DepositoOrdinanzaPcModel lDepPcMod = new DepositoOrdinanzaPcModel();
			lDepPcMod
					.setAnnoS3(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
			lDepPcMod
					.setNumS3(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));
			/*
			 * lDepPcMod.setDataUdienza(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
			 * ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			 */
			lDepPcMod.setDataUdienza(lEve.getDataEmissione());

			lDepPcMod.setCodUfficioInserimento(lCodiceUfficio);
			lDepPcMod.setCodOperatoreInserimento(lCodiceOperatore);
			lDepPcMod.setDataInserimento(DateUtils.getSysDate());
			lDepPcMod.setCodMagistrato(getRequestStringParameter(ICostantiWMagistrato.CAMPO_COD_MAGISTRATO));

			// setto il tenore
			TenoreModel lTenMod = new TenoreModel();
			lTenMod.setCodEsitoTenore("0001");
			/*
			 * lTenMod.setData(getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
			 * ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
			 * ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE));
			 */
			lTenMod.setData(lEve.getDataEmissione());

			lTenMod.setCodOggettoTenore(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			lTenMod.setProgrTenore(new BigDecimal(1));
			lTenMod.setCodUfficioInserimento(lCodiceUfficio);
			lTenMod.setCodOperatoreInserimento(lCodiceOperatore);
			lTenMod.setDataInserimento(DateUtils.getSysDate());

			// ricerco Pena residua
//			PenaResiduaModel lPenaResMod = new PenaResiduaModel();
			IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			/*lPenaResMod = */lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel
					.getIdFascicoloSiep());

			lMisMod.setCodTipoDecisione("03");
			lMisMod.setCodNaturaDecisione("CO");
			lMisMod.setCodTipoMisura("0194");

			lMisMod.setDataDecisione(getRequestDateParameter(
					ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE));

			lMisMod.setChiaveUfficioFascicoloSius(lCodiceUff);
			// lMisMod.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
			lMisMod.setAnnoRegistro(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
			lMisMod.setNumeroRegistro(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));
			lMisMod.setChiaveProgrFascicoloSius(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS));
			lMisMod.setChiaveAnnoFascicoloSius(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS));
			lMisMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

			lMisMod.setDataInizioMisura(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

			lMisMod.setCodUfficioSorveglianza(lCodiceUff);
			lMisMod.setDescrLuogoProva(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));
			lMisMod.setCodUfficioInserimento(lCodiceUfficio);
			lMisMod.setCodOperatoreInserimento(lCodiceOperatore);
			lMisMod.setDataInserimento(DateUtils.getSysDate());

			lCodTipoUffSca = this.getRequestStringParameter("tipo");
			if (lCodTipoUffSca.equals("mds"))
				lMisMod.setCodTipoUfficioScarcerazione("SORV");
			else
				lMisMod.setCodTipoUfficioScarcerazione("PROC");

			// String codPosGiu = null;

			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraModel = lCtrlMisura.ExInserisciMisuraAlternativaEventoNotifica(
					lEveMod, lDepPcMod, lTenMod, lMisMod);

			// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE

			EventoNotificaModel lEveNot = new EventoNotificaModel();
			lEveNot.getEvento().setCodTipoProvvedimento("04");
			lEveNot.getEvento().setCodMotivo("0194");
			// Da rivedere
			// lEveNot.setEvento(setEventoMisuraAlternativaDecretoSosp(lEveNot.getEvento(), tipoMisura));
			lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());
			lEveNot.getMagistrato().setCodMagistrato(
					getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

			NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();

			lEveNot.setNotifiche(lNotificheMod);
			IMisuraAlternativa lCtrlMisuraAlt = SICOLookupRemote.getMisuraAlternativaRemote();

			EventoNotificaModel lEveNotModel = lCtrlMisuraAlt.ExInserisciOModificaMANotifica(lEveNot, null,
					null, null);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioAmmissioneADetDom&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();

		} else {
			// la misura alternativa esiste
			IMisuraAlternativaBackupSrc lMisAltController = SICOLookupRemote
					.getMisuraAlternativaBackupSrcRemote();
			MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();
			lMisAlModConcessa = lMisAltController.ExRicercaMAAmmissioneADetDomByIdFascicolo(lFascicoloModel
					.getIdFascicoloSiep());

			String codiceMotivo = this.getRequestStringParameter("codiceMotivo");

			EventoNotificaModel lEve = new EventoNotificaModel();
			this.setRequestAttribute("tipoMisura", tipoMisura);
			lEve.getEvento().setCodTipoProvvedimento("04");
			lEve.getEvento().setCodMotivo(codiceMotivo);
			// da rivedere
			// lEve.setEvento(setEventoMisuraAlternativaDecretoSosp(lEve.getEvento(), tipoMisura));
			lEve.getMagistrato().setCodMagistrato(
					getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			lEve.getEvento().setEveIdEvento(lMisAlModConcessa.getEveIdEvento());
			// Inserisco l'array di Notifiche nell'Evento
			NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
			lEve.setNotifiche(lNotifiche);
			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			lRetModel = lCtrlMisura.ExInserisciOModificaMANotifica(lEve, null, null, null);
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioAmmissioneADetDom&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
		}
		return lPage;
	}

}