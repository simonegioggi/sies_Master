package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciRichiestaGenerica
 * </p>
 * <p>
 * Description: ActInserisciRichiestaGenerica
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciRichiestaGenerica extends ActionSiap implements ICostantiRichiesta {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		EventoNotificaModel lEve = new EventoNotificaModel();

		// il tipo provvedimento quando è '31' Trasmissione atti deve essere impostato a provvedimento ossia
		// '04'
		if (getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO) != null
				&& getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO).equals("31")) {
			lEve.getEvento().setCodTipoEvento("01"); // Tipo Evento = Provvedimento
			lEve.getEvento().setCodTipoProvvedimento("31");
		} else {
			lEve.getEvento().setCodTipoEvento("02"); // Tipo Evento = Richiesta
			lEve.getEvento().setCodTipoProvvedimento(
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO));
		}

		lEve.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.getEvento().setDataEmissione(lDataEmissione);
		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		lEve.getEvento().setDataTrasmissioneAtti(lDataTrasmissione);

		// Gestisco l'inserimento del firmatario
		if (!isRequestParameterNullObj("selFirm")) {
			if (getRequestStringParameter("selFirm").equalsIgnoreCase("mag")) {
				lEve.getEvento().setCodMagistrato(
						getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			} else if (getRequestStringParameter("selFirm").equalsIgnoreCase("fun")) {
				lEve.getEvento().setNomeSoggettoPresentante(getRequestStringParameter("NomeFunzionario"));
				lEve.getEvento()
						.setCognomeSoggettoPresentante(getRequestStringParameter("CognomeFunzionario"));
				lEve.getEvento().setCodMagistrato("-");
			}
		} else {
			lEve.getEvento()
					.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		}

		UfficioModel lUff = getUfficioUtenteConnesso();

		lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		lEve.getEvento().setCodLuogoEmittente(lUff.getCodComune());
		lEve.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lUff.getCodUfficio());
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		// per visto e ricorso i flag sono ad 'N'
		if (getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO) != null
				&& (getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO).equals("29")
						|| getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO)
								.equals("30"))) {
			lEve.getEvento().setFlagStampaSiep("N");
			lEve.getEvento().setFlagVideoSiep("N");
		} else {
			lEve.getEvento().setFlagStampaSiep("S");
			lEve.getEvento().setFlagVideoSiep("S");
		}

		// imposto il campo contenuto nella tabella CampoNote
		if (!isRequestParameterNullObj("camponote") && getRequestStringParameter("camponote") != null
				&& !getRequestStringParameter("camponote").equals("")) {
			ArrayList lCampoNote = new ArrayList();
			CampoNotaModel lCampMod = new CampoNotaModel();
			lCampMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCampMod.setCodUfficioInserimento(lUff.getCodUfficio());
			lCampMod.setDataInserimento(DateUtils.getSysDate());
			lCampMod.setDescr(getRequestStringParameter("camponote"));
			lCampoNote.add(lCampMod);

			lEve.setCampoNote((CampoNotaModel[]) lCampoNote.toArray(new CampoNotaModel[0]));
		}

		// Preparo le notifiche
		ArrayList lNotifiche = new ArrayList();

		// Giudice dell'esecuzione
		// Controllo sull'esistenza dell'ufficio per quel comune
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_COD_UFFICIO_GE)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_COD_UFFICIO_GE) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_COD_UFFICIO_GE).equals("-")
				&& !isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_UFFICIO_GE)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_UFFICIO_GE) != null) {
			String lCodiceUffGE = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiNotifica.CAMPO_COD_UFFICIO_GE),
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_UFFICIO_GE));
			NotificaModel lNotGE = new NotificaModel();

			lNotGE.setCodTipoNotifica("E");
			lNotGE.setDataInvio(lDataTrasmissione);
			lNotGE.setCodEsito("-");
			lNotGE.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotGE.setDataInserimento(DateUtils.getSysDate());
			lNotGE.setCodUfficioInserimento(lUff.getCodUfficio());
			lNotGE.setUffCodUfficio(lCodiceUffGE);

			lNotifiche.add(lNotGE);
		}

		// SETTO MDS
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_MDS)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS).equals("")) {
			NotificaModel lNotModMDS = new NotificaModel();
			// MEV_66: aggiunto campo in visualizzazione = tdsm + udsm + uepe
			String descrTipoUfficio = "UDS";
			if (!isRequestParameterNullObj("ufficioMdS"))
				descrTipoUfficio = getRequestStringParameter("ufficioMdS");
			String lMDS = getCodUfficioByCodTipoUfficioDescrComune(descrTipoUfficio,
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS));

			lNotModMDS.setCodEsito("-");
			lNotModMDS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModMDS.setDataInserimento(DateUtils.getSysDate());
			lNotModMDS.setCodUfficioInserimento(lUff.getCodUfficio());
			lNotModMDS.setCodTipoNotifica("N");
			lNotModMDS.setDataInvio(lDataTrasmissione);
			lNotModMDS.setUffCodUfficio(lMDS);
			lNotifiche.add(lNotModMDS);
		}

		// SETTO TDS
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS).equals("")) {
			NotificaModel lNotModTDS = new NotificaModel();
			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lUff.getCodUfficio());
			lNotModTDS.setCodTipoNotifica("N");
			lNotModTDS.setDataInvio(lDataTrasmissione);
			// MEV_66: aggiunto campo in visualizzazione = tdsm + udsm + uepe
			String descrTipoUfficio = "TDS";
			if (!isRequestParameterNullObj("ufficioTds"))
				descrTipoUfficio = getRequestStringParameter("ufficioTds");
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(descrTipoUfficio,
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS));
			lNotModTDS.setUffCodUfficio(lCodiceUff);

			lNotifiche.add(lNotModTDS);
		}

		// Pubblico ministero
		// Controllo sull'esistenza dell'ufficio per quel CAMPO_COD_UFFICIO_PM
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_COD_UFFICIO_PM)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_COD_UFFICIO_PM) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_COD_UFFICIO_PM).equals("-")
				&& !isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM) != null) {

			String lCodiceUffPM = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiNotifica.CAMPO_COD_UFFICIO_PM),
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM));
			NotificaModel lNotPM = new NotificaModel();

			lNotPM.setCodTipoNotifica("N");
			lNotPM.setDataInvio(lDataTrasmissione);
			lNotPM.setCodEsito("-");
			lNotPM.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotPM.setDataInserimento(DateUtils.getSysDate());
			lNotPM.setCodUfficioInserimento(lUff.getCodUfficio());
			lNotPM.setUffCodUfficio(lCodiceUffPM);

			lNotifiche.add(lNotPM);
		}

		// Istituto di detenzione
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) != null
				&& !getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
						.equals("")) {
			NotificaModel lNotModIst = new NotificaModel();
			String lIstituto = getRequestStringParameter(
					ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
			lNotModIst.setCodEsito("-");
			lNotModIst.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModIst.setDataInserimento(DateUtils.getSysDate());
			lNotModIst.setCodUfficioInserimento(lUff.getCodUfficio());
			lNotModIst.setCodTipoNotifica("N");
			lNotModIst.setDataInvio(lDataTrasmissione);
			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);
			lNotifiche.add(lNotModIst);
		}

		// autorità di polizia
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
				&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA) != null
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA).equals("-")) {
			String lPolizia = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
			String lSedePolizia = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
			NotificaModel lNotModPol = new NotificaModel();

			// indirizzo
			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)) {
				String lNotePolizia = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE);
				lNotModPol.setNote(lNotePolizia);
			}

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lUff.getCodUfficio());
			lNotModPol.setCodTipoNotifica("N");
			lNotModPol.setDataInvio(lDataTrasmissione);

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(lUff.getCodUfficio());
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// MEV_66: aggiunto campo in visualizzazione = tdsm + udsm + uepe
		if (!isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)
				&& !"null".equals(getRequestStringParameter(ICostantiCSSA.CAMPO_ID_CSSA))
				&& getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA) != null
				&& getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA)
						.compareTo(new BigDecimal(0)) != 0) {
			NotificaModel lNotModCSSA = new NotificaModel();
			BigDecimal lCssa = getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);

			lNotModCSSA.setCodEsito("-");
			lNotModCSSA.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModCSSA.setDataInserimento(DateUtils.getSysDate());
			lNotModCSSA.setCodUfficioInserimento(lUff.getCodUfficio());
			lNotModCSSA.setCodTipoNotifica("C");

			lNotModCSSA.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
			lNotModCSSA.setCssIdCssa(lCssa);
			lNotifiche.add(lNotModCSSA);
		}

		// Inserisco l'array di Notifiche nell'Evento
		lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.richiesta.action.ActDettaglioRichiestaGenerica&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

}