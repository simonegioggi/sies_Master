package siap.siep.sospensione.action;

/**
 * <p>Title: ActInserisciSospensioneArt91</p>
 * <p>Description: Classe Action per l'inserimento di Sospensione Art. 91</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciSospensioneArt91 extends ActionSiap
		implements ICostantiSospensione, ICostantiEvento {

	/**
	 * Azione di Inserimento del Sospensione art 91
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		String lSedeCodiceUfficio = this.getCodComuneUtenteConnesso();
		BigDecimal idEventoGenerato = this.getRequestBigDecimalParameter("idEventoGenerato");

		EventoModel lEveMod = new EventoModel();
		EventoNotificaModel lEveNot = new EventoNotificaModel();

		lEveMod.setCodTipoEvento("01");

		// Il Tipo Provvedimento dipende dallo stato del detenuto (Scarcerato o non)
		// Tipo e Motivo sono già stati elaborati e passati dalla jsp. Luigi 21-9-2005
		lEveMod.setCodTipoProvvedimento(getRequestStringParameter("CodTipoProvvedimento"));
		lEveMod.setCodMotivo(getRequestStringParameter("CodMotivo"));
		// lEveMod.setCodTipoProvvedimento("04");
		// lEveMod.setCodMotivo("0264");

		lEveMod.setCodUfficioEmittente(lCodiceUfficio);
		lEveMod.setCodLuogoEmittente(lSedeCodiceUfficio);
		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodEsito("-");
		lEveMod.setEveIdEvento(idEventoGenerato);

		lEveMod.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

		lEveMod.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		lEveMod.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
		lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveMod.setFlagVideoSiep("S");
		lEveMod.setFlagStampaSiep("S");

		lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveMod.setCodOperatoreInserimento(lCodiceOperatore);
		lEveMod.setCodUfficioInserimento(lCodiceUfficio);
		lEveMod.setDataInserimento(DateUtils.getSysDate());
		// setto l'evento();
		lEveNot.setEvento(lEveMod);
		ArrayList lNotifiche = new ArrayList();

		// SETTO TDS

		if (!this.isRequestParameterNullObj(ICostantiSospensione.CAMPO_SEDE_TDS)
				&& !this.isRequestParameterNullObj(ICostantiSospensione.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_TDS) != null
				&& !getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_TDS).equals("")) {
			NotificaModel lNotModTDS = new NotificaModel();
			String lTribunale = this.getCodUfficioByCodTipoUfficioDescrComune("TDS",
					getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_TDS));
			// String lSedeTribunale = this.getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_TDS);

			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("E");
			lNotModTDS.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
			lNotModTDS.setUffCodUfficio(lTribunale);

			lNotifiche.add(lNotModTDS);
		}

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
			lNotModIst.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));

			// lNotModCSSA.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndMisura]));

			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);
			lNotifiche.add(lNotModIst);

		}

		// SETTO UDS

		if (!this.isRequestParameterNullObj(ICostantiSospensione.CAMPO_CODICE_MAGISTRATO)
				&& getRequestStringParameter(ICostantiSospensione.CAMPO_CODICE_MAGISTRATO) != null
				&& !getRequestStringParameter(ICostantiSospensione.CAMPO_CODICE_MAGISTRATO).equals("")) {
			NotificaModel lNotModTDS = new NotificaModel();
			String lUDS = this.getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameter(ICostantiSospensione.CAMPO_CODICE_MAGISTRATO));

			// String lSedeMagistrato =
			// this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_MAG);

			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("NC");
			lNotModTDS.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
			lNotModTDS.setUffCodUfficio(lUDS);
			lNotifiche.add(lNotModTDS);
		}

		// SETTO NOTIFICA AUTORITA
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
				&& this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA) != null
				&& !this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
						.equals("-")) {
			String lPolizia = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
			String lSedePolizia = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
			NotificaModel lNotModPol = new NotificaModel();

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("N");
			lNotModPol.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// setto CSSA
		if (!this.isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)
				&& getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA) != null
				&& getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA)
						.compareTo(new BigDecimal(0)) != 0
				&& !"-".equals(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA).toString())) {

			NotificaModel lNotModCSSA = new NotificaModel();
			BigDecimal lCssa = this.getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);

			// String lSedeCssa = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_CSSA);

			lNotModCSSA.setCodEsito("-");
			lNotModCSSA.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModCSSA.setDataInserimento(DateUtils.getSysDate());
			lNotModCSSA.setCodUfficioInserimento(lCodiceUfficio);
			lNotModCSSA.setCodTipoNotifica("C");

			lNotModCSSA.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
			lNotModCSSA.setCssIdCssa(lCssa);
			lNotifiche.add(lNotModCSSA);

		}

		lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));
		// Prepara la pagina di destinazione
		String lPage = "";
		EventoNotificaModel lEveNotMod = null;
		ISospensione lCtrlEve = SIEPLookupRemote.getSospensioneRemote();
		lEveNotMod = lCtrlEve.ExInserisciOModificaEventoNotificaSosp(lEveNot);
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sospensione.action.ActLoadDettaglioSospensioneArt91&" + CAMPO_ID_EVENTO + "="
				+ lEveNotMod.getEvento().getIdEvento().toString();

		return lPage;
	}

}