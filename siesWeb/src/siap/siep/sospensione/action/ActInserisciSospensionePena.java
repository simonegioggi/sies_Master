package siap.siep.sospensione.action;

/**
 * <p>Title: ActInserisciSospensionePena</p>
 * <p>Description: Classe Action per l'inserimento di Sospensione Pena</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
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
public class ActInserisciSospensionePena extends ActionSiap implements ICostantiSospensione, ICostantiEvento {

	/**
	 * Azione di Inserimento del Sospensione Pena
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
		// Tipo e Motivo sono già stati elaborati e passati dalla jsp. Luigi 22-9-2005
		lEveMod.setCodTipoProvvedimento(getRequestStringParameter("CodTipoProvvedimento"));
		lEveMod.setCodMotivo(getRequestStringParameter("CodMotivo"));
		// lEveMod.setCodTipoProvvedimento("04");
		// lEveMod.setCodMotivo("0265");
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

		// SETTO ISTITUTO DETENZIONE E
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

		// SETTO NOTIFICA AUTORITA E
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E) != null
				&& !this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
						.equals("-")) {
			String lPolizia = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			String lSedePolizia = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			NotificaModel lNotModPol = new NotificaModel();

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("E");
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

		// SETTO NOTIFICA AUTORITA C
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
				&& this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C) != null
				&& !this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
						.equals("-")) {
			String lPolizia = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C);
			String lSedePolizia = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C);
			NotificaModel lNotModPol = new NotificaModel();

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("C");
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

		// AVVOCATI
		int lIndex = 0;
		if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
			String[] lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
			for (lIndex = 0; lIndex < lAvvocati.length; lIndex++) {
				NotificaModel lNotAvv = new NotificaModel();
				lNotAvv.setCodTipoNotifica("N");
				// lNot.setNote(lArrayNote[lIndMisura]);
				lNotAvv.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
						ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
				lNotAvv.setCodEsito("-");
				lNotAvv.setCodOperatoreInserimento(lCodiceOperatore);
				lNotAvv.setDataInserimento(DateUtils.getSysDate());
				lNotAvv.setCodUfficioInserimento(lCodiceUfficio);
				lNotAvv.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndex]));

				if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF)) {
					String[] lTipoAutoritaEsternaAvvocato = this
							.getRequestStringParameters(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF);
					String[] lSedeAutoritaEsternaAvvocato = this
							.getRequestStringParameters(ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF);

					String[] lNoteAvvocato = this.getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
					lNotAvv.setNote(lNoteAvvocato[lIndex]);
					// String lNoteAvvocato = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE);
					// lNotAvv.setNote(lNoteAvvocato);
					AutoritaEsternaModel lAut = new AutoritaEsternaModel();
					lAut = new AutoritaEsternaModel();
					lAut.setCodTipoAutorita(lTipoAutoritaEsternaAvvocato[lIndex]);

					//INIZIO: MEV_21 (avvocati) - si inibisce la selezione di comuni non validi (tipo NAPOLI NORD)
//					ComuneModel lComMod = new ComuneModel(
//							getCodComuneByDescr(lSedeAutoritaEsternaAvvocato[lIndex]));
					ComuneModel lComMod = new ComuneModel(
							getCodComuneByDescrFlagVal(lSedeAutoritaEsternaAvvocato[lIndex]));
					//FINE: MEV_21
					
					lAut.setCodSede(lComMod.getCodComune());
					lAut.setCodOperatoreInserimento(lCodiceOperatore);
					lAut.setCodUfficioInserimento(lCodiceUfficio);
					lAut.setDataInserimento(DateUtils.getSysDate());
					lNotAvv.setAutoritaEsterna(lAut);
				}
				lNotifiche.add(lNotAvv);
			}
		}

		lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// Prepara la pagina di destinazione
		String lPage = "";
		EventoNotificaModel lEveNotMod = null;
		ISospensione lCtrlEve = SIEPLookupRemote.getSospensioneRemote();
		lEveNotMod = lCtrlEve.ExInserisciOModificaEventoNotificaSosp(lEveNot);
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sospensione.action.ActLoadDettaglioSospensionePena&" + CAMPO_ID_EVENTO + "="
				+ lEveNotMod.getEvento().getIdEvento().toString();

		return lPage;
	}

}