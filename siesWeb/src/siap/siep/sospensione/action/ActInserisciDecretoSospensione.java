package siap.siep.sospensione.action;

/**
 * <p>Title: ActInserisciDecretoSospensione</p>
 * <p>Description: Classe Action per l'inserimento di Sospensione</p>
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
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciDecretoSospensione extends ActionSiap
		implements ICostantiSospensione, ICostantiEvento {

	/**
	 * Azione di Inserimento del Decreto di Sospensione
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

		EventoModel lEveMod = new EventoModel();
		EventoNotificaModel lEveNot = new EventoNotificaModel();

		lEveMod.setCodTipoEvento("01");
		// CodTipoProvvedimento da 04 diventa 02. Luigi 05-10-2005
		lEveMod.setCodTipoProvvedimento("02");
		lEveMod.setCodMotivo("0282");
		lEveMod.setCodUfficioEmittente(lCodiceUfficio);
		lEveMod.setCodLuogoEmittente(lSedeCodiceUfficio);
		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodEsito("-");
		lEveMod.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveMod.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveMod.setFlagVideoSiep("S");
		lEveMod.setFlagStampaSiep("S");

		lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveMod.setCodOperatoreInserimento(lCodiceOperatore);
		lEveMod.setCodUfficioInserimento(lCodiceUfficio);
		lEveMod.setDataInserimento(DateUtils.getSysDate());

		lEveNot.setEvento(lEveMod);
		ArrayList lNotifiche = new ArrayList();

		// AVVOCATI
		int lIndex = 0;
		if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
			String[] lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
			for (lIndex = 0; lIndex < lAvvocati.length; lIndex++) {
				NotificaModel lNotAvv = new NotificaModel();
				lNotAvv.setCodTipoNotifica("N");
				lNotAvv.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
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

		// ==========================================================================
		// AUTORITA' PER LA RESTITUZIONE ORDINE ESECUZIONE
		// ==========================================================================
		if (!isRequestParameterNullObj(CAMPO_RESTITUZIONE_OE)) {
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
					&& this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R) != null
					&& !this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
							.equals("")
					&& !this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
							.equals("-")) {

				NotificaModel lNotModPol = new NotificaModel();

				if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R)) {
					String lNotePolizia = this
							.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R);
					lNotModPol.setNote(lNotePolizia);
				}

				lNotModPol.setCodEsito("-");
				lNotModPol.setCodTipoNotifica("R");
				lNotModPol.setDataInvio(
						getRequestDateParameter(ICostantiEvento.CAMPO_DATA_EMISSIONE, "dd-MM-yyyy"));

				lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
				lNotModPol.setDataInserimento(DateUtils.getSysDate());
				lNotModPol.setCodUfficioInserimento(lCodiceUfficio);

				AutoritaEsternaModel lAut = new AutoritaEsternaModel();
				String lPolizia = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R);
				String lSedePolizia = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_R);

				lAut.setCodTipoAutorita(lPolizia);

				ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
				lAut.setCodSede(lComMod.getCodComune());

				lAut.setCodOperatoreInserimento(lCodiceOperatore);
				lAut.setCodUfficioInserimento(lCodiceUfficio);
				lAut.setDataInserimento(DateUtils.getSysDate());

				lNotModPol.setAutoritaEsterna(lAut);

				lNotifiche.add(lNotModPol);
			}
		}

		lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// Prepara la pagina di destinazione
		String lPage = "";
		EventoNotificaModel lEveNotMod = null;

		ISospensione lCtrlEve = SIEPLookupRemote.getSospensioneRemote();
		lEveNotMod = lCtrlEve.ExInserisciOModificaEventoNotificaSosp(lEveNot);
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sospensione.action.ActLoadDettaglioDecretoSospensione&" + CAMPO_ID_EVENTO + "="
				+ lEveNotMod.getEvento().getIdEvento().toString();

		return lPage;
	}

}