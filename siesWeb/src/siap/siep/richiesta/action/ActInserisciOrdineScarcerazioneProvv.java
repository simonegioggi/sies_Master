package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.richiesta.controller.IRichiesta;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciOrdineScarcerazioneProvv
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento dell'Ordine di scarcerazione provvisorio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciOrdineScarcerazioneProvv extends ActionSiap implements ICostantiRichiesta {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal lIdAnnotazioneManuale = this
				.getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE);

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		EventoNotificaModel lRetModel = new EventoNotificaModel();

		String lPage = null;

		String codiceMotivo = getRequestStringParameter("codicemotivo");

		// INSERISCO EVENTO E NOTIFICA
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		EventoModel lEveModel = new EventoModel();

		lEveModel.setCodTipoEvento("01");
		lEveModel.setCodTipoProvvedimento("09");
		lEveModel.setCodMotivo(codiceMotivo);
		lEveModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveModel.setCodUfficioEmittente(lCodiceUfficio);
		lEveModel.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEveModel.setCodUfficioDestinatario("-");
		lEveModel.setCodTipoUfficioDestinatario("-");

		lEveModel.setCodLuogoDestinatario("-");
		lEveModel.setCodEsito("-");

		lEveModel.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

		lEveModel.setDataTrasmissioneAtti(
				getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
						ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
						ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

		lEveModel.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveModel.setAnnIdAnnotazioneManuale(lIdAnnotazioneManuale);

		lEveModel.setCodOperatoreInserimento(lCodiceOperatore);
		lEveModel.setDataInserimento(DateUtils.getSysDate());
		lEveModel.setCodUfficioInserimento(lCodiceUfficio);

		lEveModel.setFlagStampaSiep("S");
		lEveModel.setFlagVideoSiep("S");

		lEveMod.setEvento(lEveModel);

		// setto le notifiche
		ArrayList lNotifiche = new ArrayList();

		// SETTO ISTITUTO DETENZIONE
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) != null
				&& !getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
						.equals("")) {

			NotificaModel lNotModIst = new NotificaModel();
			String lIstituto = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			lNotModIst.setCodEsito("-");
			lNotModIst.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModIst.setDataInserimento(DateUtils.getSysDate());
			lNotModIst.setCodUfficioInserimento(lCodiceUfficio);
			lNotModIst.setCodTipoNotifica("E");
			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);
			lNotModIst.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
					ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
					ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

			lNotifiche.add(lNotModIst);
		}

		// SETTO CSSA
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_CSS_ID_CSSA)
				&& getRequestBigDecimalParameter(ICostantiNotifica.CAMPO_CSS_ID_CSSA) != null
				&& getRequestBigDecimalParameter(ICostantiNotifica.CAMPO_CSS_ID_CSSA)
						.compareTo(new BigDecimal(0)) != 0
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_CSS_ID_CSSA).equals("-")) {
			NotificaModel lNotModCSSA = new NotificaModel();
			BigDecimal lCssa = this.getRequestBigDecimalParameter(ICostantiNotifica.CAMPO_CSS_ID_CSSA);

			lNotModCSSA.setCodEsito("-");
			lNotModCSSA.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModCSSA.setDataInserimento(DateUtils.getSysDate());
			lNotModCSSA.setCodUfficioInserimento(lCodiceUfficio);
			lNotModCSSA.setCodTipoNotifica("N");
			lNotModCSSA
					.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
							ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
							ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

			lNotModCSSA.setCssIdCssa(lCssa);
			lNotifiche.add(lNotModCSSA);
		}

		// SETTO MDS
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_MDS)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS).equals(""))

		{
			NotificaModel lNotModMDS = new NotificaModel();
			String lMDS = this.getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS));

			lNotModMDS.setCodEsito("-");
			lNotModMDS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModMDS.setDataInserimento(DateUtils.getSysDate());
			lNotModMDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModMDS.setCodTipoNotifica("N");
			lNotModMDS.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
					ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
					ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

			lNotModMDS.setUffCodUfficio(lMDS);
			lNotifiche.add(lNotModMDS);
		}

		// SETTO TDS
		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS).equals(""))

		{
			NotificaModel lNotModTDS = new NotificaModel();
			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("N");
			lNotModTDS.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
					ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
					ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune("TDS",
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS));
			lNotModTDS.setUffCodUfficio(lCodiceUff);

			lNotifiche.add(lNotModTDS);
		}

		// autorità di polizia
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
				&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA) != null
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA).equals("-")) {
			String lPolizia = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
			String lSedePolizia = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
			NotificaModel lNotModPol = new NotificaModel();

			// indirizzo
			if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)) {
				String lNotePolizia = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE);
				lNotModPol.setNote(lNotePolizia);
			}

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("N");
			lNotModPol.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
					ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
					ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		lEveMod.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		IRichiesta lCtrlRich = SIEPLookupRemote.getRichiestaRemote();
		lRetModel = lCtrlRich.ExInserisciOModificaNotifica(lEveMod);
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.richiesta.action.ActDettaglioOrdineScarcerazioneProvv&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

}