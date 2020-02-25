package siap.siep.sospensione.action;

/**
 * <p>Title: ActInserisciSospensioneArt47</p>
 * <p>Description: Classe Action per l'inserimento di Sospensione Art. 47</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
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
public class ActInserisciSospensioneArt47 extends ActionSiap
		implements ICostantiSospensione, ICostantiEvento {

	/**
	 * Azione di Inserimento Sospensione Art. 47
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
		// lEveMod.setCodMotivo("0263");

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
			lNotModIst.setCodTipoNotifica("N");
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
			lNotModTDS.setCodTipoNotifica("C");
			lNotModTDS.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
			lNotModTDS.setUffCodUfficio(lUDS);
			lNotifiche.add(lNotModTDS);
		}
		lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// Prepara la pagina di destinazione
		String lPage = "";
		EventoNotificaModel lEveNotMod = null;
		ISospensione lCtrlEve = SIEPLookupRemote.getSospensioneRemote();
		lEveNotMod = lCtrlEve.ExInserisciOModificaEventoNotificaSosp(lEveNot);
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sospensione.action.ActLoadDettaglioSospensioneArt47&" + CAMPO_ID_EVENTO + "="
				+ lEveNotMod.getEvento().getIdEvento().toString();

		return lPage;
	}

}