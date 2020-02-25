package siap.siep.sospensione.action;

/**
 * <p>Title: ActInserisciSospensioneOE</p>
 * <p>Description: Classe Action per l'inserimento di Sospensione Ordine Esecuzione,
 * Ordine di Scarcerazione, Comunicazione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciSospensioneOE extends ActionSiap implements ICostantiSospensione, ICostantiEvento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Provvedimento conseguente l'interruzione di pena disposta dal PM o dal GE
	 * Inserisce uno dei seguenti provvedimenti: - Ordine Esecuzione - Ordine di Scarcerazione - Comunicazione
	 * 
	 * N.B. DI FATTO NON INSERISCE NULLA DI NUOVO, ME SVALIDA L'EVENTO DI INTERRUZIONE PRECEDENTEMENTE
	 * INSERITO E VALIDATO E AGGIUNGE I DESTINATARI
	 * 
	 * @return Azione di dettaglio
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		String lSedeCodiceUfficio = this.getCodComuneUtenteConnesso();

		// ==========================================================================
		// Ricerco l'evento legato all'Interruzione (decreto_ordinanza_siep)
		// ==========================================================================
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = new EventoModel();
		lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		// modifica 24-02-05 dario -- luciana -- per emettere l'ordine di esecuzione quando è
		// altro-interruzione

		// Who works and reworks never looses time !!
		// Cambiato il codice Tipo Provvedimento.
		// Comunque si mantiene la compatibilità con i vecchi codici. Luigi 11-10-2005
		String[] lTipoProv = { "04", "12", "04", "12", "04", "25", "04", "25", "04", "25", "25" };
		String[] lMotivo = { "0266", "0266", "0267", "0267", "0268", "0268", "0269", "0269", "0270", "0270",
				"0366" };

		// String[] lMotivo ={"0268","0269","0267","0266","0270"};

		EventoModel lEventoRicercato = new EventoModel();
		lEventoRicercato = lCtrlEvento.ExRicercaEventoUnicoTipoProvTipoMot(lEveMod, lTipoProv, lMotivo);
		// lEventoRicercato=lCtrlEvento.ExRicercaEventoPerMotivo(lMotivo,lEveMod);

		// ==========================================================================
		// Imposto l'evento e le notifiche recuperando tipo_provvedimento e codMotivo
		// dall'evento di interruzione
		// ==========================================================================
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveMod.setCodTipoEvento("01");
		// Cod Tipo Provvedimento non è più 04 . Luigi 11-10-2005
		// lEveMod.setCodTipoProvvedimento("04");
		lEveMod.setCodTipoProvvedimento(lEventoRicercato.getCodTipoProvvedimento());
		lEveMod.setCodMotivo(lEventoRicercato.getCodMotivo());

		lEveMod.setCodUfficioEmittente(lCodiceUfficio);
		lEveMod.setCodLuogoEmittente(lSedeCodiceUfficio);
		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodEsito("-");

		lEveMod.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

		lEveMod.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

		lEveMod.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));

		lEveMod.setFlagVideoSiep("S");
		lEveMod.setFlagStampaSiep("S");

		lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveMod.setDecIdDecretoOrdinanzaSiep(this.getRequestBigDecimalParameter(
				ICostantiDecretoOrdinanzaSiep.CAMPO_ID_DECRETO_ORDINANZA_SIEP));

		lEveMod.setCodOperatoreInserimento(lCodiceOperatore);
		lEveMod.setCodUfficioInserimento(lCodiceUfficio);
		lEveMod.setDataInserimento(DateUtils.getSysDate());

		// setto l'evento();
		lEveNot.setEvento(lEveMod);

		// ==========================================================================
		// Recupero le notifiche
		// ==========================================================================
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

		// Magistrato
		if (!this.isRequestParameterNullObj(ICostantiSospensione.CAMPO_CODICE_MAGISTRATO)
				&& getRequestStringParameter(ICostantiSospensione.CAMPO_CODICE_MAGISTRATO) != null
				&& !getRequestStringParameter(ICostantiSospensione.CAMPO_CODICE_MAGISTRATO).equals("")) {
			NotificaModel lNotModTDS = new NotificaModel();
			String lUDS = this.getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameter(ICostantiSospensione.CAMPO_CODICE_MAGISTRATO));

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

		// Aggiungo le notifiche al model
		lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// ==========================================================================
		// EFFETTUO L'INSERIMENTO DELL'EVENTO E DELLE NOTIFICHE
		// ==========================================================================
		EventoModel lEveModel = null;
		ISospensione lCtrlEve = SIEPLookupRemote.getSospensioneRemote();
		lEveModel = lCtrlEve.ExModificaEventoInserisciNotificaSosp(lEveNot);

		// Prepara la pagina di destinazione
		String lPage = "";
		if (this.getRequestStringParameter("azione").equals("OS")) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.sospensione.action.ActLoadDettaglioSospensioneOS&" + CAMPO_ID_EVENTO + "="
					+ lEveModel.getIdEvento().toString();
		} else {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.sospensione.action.ActLoadDettaglioSospensioneOE&" + CAMPO_ID_EVENTO + "="
					+ lEveModel.getIdEvento().toString();
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lPage;
	}

}