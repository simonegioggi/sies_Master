package siap.siep.revoca.action;

/**
 * <p>Title: ActInserisciOrdineOrdineEsecuzioneRevoca</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciOrdineEsecuzioneRevoca extends ActionSiap
		implements ICostantiRevoca, ICostantiDecretoOrdinanzaSiep {

	/**
	 * Azione di Inserimento dell'OE Revoca Sospensione
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		BigDecimal lIdEventoGenerato = getRequestBigDecimalParameter(
				ICostantiDecretoOrdinanzaSiep.CAMPO_ID_EVENTO_GENERATO);
		String lCodMotivo = getRequestStringParameter("CodMotivo");

		EventoModel lEve = new EventoModel();

		lEve.setCodTipoEvento("01"); // PROVVEDIMENTO

		// STUB 20-09-2005 Rework ricodifiche Eventi SIEP.
		// lEve.setCodTipoProvvedimento("04"); //TIPO PROVVEDIMENTO
		// lEve.setCodMotivo("0057");
		lEve.setCodTipoProvvedimento("06"); // TIPO PROVVEDIMENTO

		// 26/11/2010 Esecuzione pena presso Domicilio
		if (lCodMotivo.trim().compareTo("0443") == 0 || lCodMotivo.trim().compareTo("0444") == 0
				|| lCodMotivo.trim().compareTo("0449") == 0)
			lEve.setCodMotivo("0494");
		else
			lEve.setCodMotivo("0217");

		lEve.setCodUfficioEmittente(lCodiceUfficio);
		lEve.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.setDataEmissione(lDataEmissione);
		lEve.setCodEsito("-");
		// lEve.setFlagPiuMeno();
		lEve.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
		// lEve.setDataRicezioneAtti();
		// lEve.setCodUfficioDestinatario();
		lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		// lEve.setProgrProtocollo();
		lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEve.setCodLuogoDestinatario("-");

		// *** Il FlagDocumentoRegistrato(null) INDICA CHE IL DOCUMENTO NON E' PRESENTE
		lEve.setFlagDocumentoRegistrato(null);
		lEve.setCodMagistrato(calcolaMagistrato());
		lEve.setCodTipoUfficioDestinatario("-");

		lEve.setFlagStampaSiep("S");
		lEve.setFlagVideoSiep("S");

		lEve.setEveIdEvento(lIdEventoGenerato);

		lEve.setCodOperatoreInserimento(lCodiceOperatore);
		lEve.setCodUfficioInserimento(lCodiceUfficio);
		lEve.setDataInserimento(DateUtils.getSysDate());

		lEve.setCodOperatoreAggiornamento(lCodiceOperatore);
		lEve.setCodUfficioAggiornamento(lCodiceUfficio);
		lEve.setDataAggiornamento(DateUtils.getSysDate());

		NotificaModel[] lNotifiche = setNotifiche(lEve);

		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.setEvento(lEve);
		lEveNot.setNotifiche(lNotifiche);

		IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
		EventoNotificaModel lRetModel = lCtrl.ExInserisciOModificaEventoNotifica(lEveNot);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.revoca.action.ActLoadDettaglioOrdineEsecuzioneRevoca&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

	/**
	 * calcolaMagistrato
	 * 
	 * @return
	 */
	protected String calcolaMagistrato() throws F3BException {
		String lCodiceMagistrato = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);

		if (lCodiceMagistrato.compareTo("") == 0) {
			MagistratoModel lMagMod = new MagistratoModel();
			lMagMod.setCognome(getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME).toUpperCase());
			lMagMod.setNome(getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME).toUpperCase());

			IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
			Vector lVect = new Vector();
			try {
				lVect = lCtrl.ExRicercaMagistrato(lMagMod);
			} catch (F3BException exF3b) {
				throw new F3BException(F3BException.USER_MESSAGE, "Magistrato Inesistente");
			}

			lMagMod = (MagistratoModel) lVect.firstElement();
			lCodiceMagistrato = lMagMod.getCodMagistrato();
		}

		return lCodiceMagistrato;
	}

	/**
	 * Imposta tutte le notifiche associate all'evento prodotto
	 * 
	 * @param lKey
	 * @return
	 * @throws F3BException
	 */
	protected NotificaModel[] setNotifiche(EventoModel lEve) throws F3BException {
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		ArrayList lNotifiche = new ArrayList();

		// SETTO NOTIFICA AUTORITA E
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)) {
			String lCodTipo = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			String lSede = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			String lNote = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

			NotificaModel lNotMod = new NotificaModel();

			lNotMod.setCodTipoNotifica("E");
			// lNotMod.setDataAvvenutaNotifica();
			lNotMod.setDataInvio(lEve.getDataTrasmissioneAtti());
			lNotMod.setCodEsito("-");
			lNotMod.setNote(lNote);

			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lCodTipo);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSede));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lAut.setCodUfficioInserimento(lCodiceUfficio);

			lNotMod.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotMod);
		}

		// SETTO NOTIFICA AUTORITA N
		// int lIndex = 0;
		if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
			String[] lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
			for (int lIndex = 0; lIndex < lAvvocati.length; lIndex++) {
				NotificaModel lNotAvv = new NotificaModel();

				lNotAvv.setCodTipoNotifica("N");
				lNotAvv.setDataInvio(lEve.getDataTrasmissioneAtti());
				lNotAvv.setCodEsito("-");
				lNotAvv.setCodOperatoreInserimento(lCodiceOperatore);
				lNotAvv.setDataInserimento(DateUtils.getSysDate());
				lNotAvv.setCodUfficioInserimento(lCodiceUfficio);
				lNotAvv.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndex]));

				if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)) {
					String[] lTipoAutoritaEsternaAvvocato = this
							.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
					String[] lSedeAutoritaEsternaAvvocato = this
							.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);

					String[] lNoteAvvocato = this.getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
					lNotAvv.setNote(lNoteAvvocato[lIndex]);
					AutoritaEsternaModel lAut = new AutoritaEsternaModel();
					lAut = new AutoritaEsternaModel();
					lAut.setCodTipoAutorita(lTipoAutoritaEsternaAvvocato[lIndex]);

					ComuneModel lComMod = new ComuneModel(
							getCodComuneByDescr(lSedeAutoritaEsternaAvvocato[lIndex]));
					lAut.setCodSede(lComMod.getCodComune());
					lAut.setCodOperatoreInserimento(lCodiceOperatore);
					lAut.setCodUfficioInserimento(lCodiceUfficio);
					lAut.setDataInserimento(DateUtils.getSysDate());
					lNotAvv.setAutoritaEsterna(lAut);
				}

				lNotifiche.add(lNotAvv);
			}
		}

		return (NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]);
	}

}