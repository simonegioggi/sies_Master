package siap.siep.misuraalternativa.action;

/**
 * <p>Title: ActVariazioneMAAmmProvDetDom</p>
 * <p>Description: Classe Action per l'inserimento di MisuraAlternativa Amm Provv Det Dom </p>
 * <p>             con data inizio misura variata </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActVariazioneMADecSca extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {

	/**
	 * Azione di Inserimento del MisuraAlternativa
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// setto la natura della MA per chiamare due metodi diversi
		// String tipoMisura = getRequestStringParameter("tipomisura");
		// String flagverbale = "N";

		// Cerco AVVOCATO per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = null;
		lAvvocati = lAvv.ExRicercaAvvocatiByFascicolo(lFascicoloModel.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// POS GIU
		String PosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		this.setRequestAttribute("posizionegiuridica", PosizioneGiu);
		String lPage = null;

		// AMBROSINO - Passo l'evento scritto in fase di variazione dataInizio misura
		// che serve per visualizzare alcuni dati nel dettaglio .
		// Stessa cosa per la Misura Alternativa veriata

		BigDecimal lIdEventoScri = new BigDecimal(this.getRequestStringParameter("idevento"));
		BigDecimal lIdMisuraVar = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA);
		// BigDecimal lIdAvvSiep = getRequestBigDecimalParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO);

		MisuraAlternativaModel lMisuVar = new MisuraAlternativaModel();
		IMisuraAlternativa lMisAltCtrlVar = SICOLookupRemote.getMisuraAlternativaRemote();
		lMisuVar = lMisAltCtrlVar.ExRicercaMisuraAlternativaByKey(lIdMisuraVar);
		setRequestAttribute("misaltvariata", lMisuVar);

		// Fine AMBRODSINO

		// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
		EventoNotificaModel lEveNot = new EventoNotificaModel();

		lEveNot.getEvento().setCodTipoProvvedimento("12");
		lEveNot.getEvento().setCodTipoEvento("01");
		lEveNot.getEvento().setCodMotivo("5415");

		// AMBROSINO - setto 'eveidevento' con l'id evento scritto in fase di variazione dataInizio misura,
		// che servirà per visualizzare alcuni dati nel dettaglio .
		// Stessa cosa per la Misura Alt veriata e campo nota

		lEveNot.getEvento().setEveIdEvento(lIdEventoScri);
		//
		lEveNot.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEveNot.getEvento().setDataEmissione(lDataEmissione);
		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		lEveNot.getEvento().setDataTrasmissioneAtti(lDataTrasmissione);
		lEveNot.getEvento()
				.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

		UfficioModel lUff = this.getUfficioUtenteConnesso();

		lEveNot.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		lEveNot.getEvento().setCodLuogoEmittente(lUff.getCodComune());
		lEveNot.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
		lEveNot.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEveNot.getEvento().setCodUfficioInserimento(lUff.getCodUfficio());
		lEveNot.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveNot.getEvento().setCodEsito("-");
		lEveNot.getEvento().setCodLuogoDestinatario("-");
		lEveNot.getEvento().setCodTipoUfficioDestinatario("-");

		lEveNot.getEvento().setFlagStampaSiep("S");
		lEveNot.getEvento().setFlagVideoSiep("S");
		lEveNot.getEvento().setFlagDocumentoRegistrato("N");

		// Preparo Notifiche

		ArrayList lNotifiche = new ArrayList();

		// autorità di polizia // notifica al Condannato (nella form precedente)
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E).equals("-")) {
			String lPolizia = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E);
			String lSedePolizia = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E);
			NotificaModel lNotModPol = new NotificaModel();

			// indirizzo
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E)) {
				String lNotePolizia = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E);
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

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(lUff.getCodUfficio());
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);
			lNotModPol.setAutEstIdAutoritaEsterna(lAut.getIdAutoritaEsterna());
			lNotifiche.add(lNotModPol);
		}

		// Pubblico CSSA (Servizio Sociale UEPE nella form precedente)

		if (!isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)
				&& getRequestStringParameter(ICostantiCSSA.CAMPO_ID_CSSA) != null
				&& !getRequestStringParameter(ICostantiCSSA.CAMPO_ID_CSSA).equals("-")) {

			NotificaModel lNotCS = new NotificaModel();

			lNotCS.setCodTipoNotifica("N");
			lNotCS.setDataInvio(lDataTrasmissione);
			lNotCS.setCodEsito("-");
			lNotCS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotCS.setDataInserimento(DateUtils.getSysDate());
			lNotCS.setCodUfficioInserimento(lUff.getCodUfficio());

			String Cssa = getRequestStringParameter(ICostantiCSSA.CAMPO_ID_CSSA);
			BigDecimal lIdCssa = new BigDecimal(Cssa);
			lNotCS.setCssIdCssa(lIdCssa);

			lNotCS.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA));

			lNotifiche.add(lNotCS);
		}

		// SETTO MDS
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS).equals(""))

		{
			NotificaModel lNotModMDS = new NotificaModel();
			String lTipoUfficio = "UDS";
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO) != null
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO).equals("")) {
				lTipoUfficio = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO);
			}
			String lSedeUfficio = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS);
			String lMDS = this.getCodUfficioByCodTipoUfficioDescrComune(lTipoUfficio, lSedeUfficio);

			lNotModMDS.setCodEsito("-");
			lNotModMDS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModMDS.setDataInserimento(DateUtils.getSysDate());
			lNotModMDS.setCodUfficioInserimento(lUff.getCodUfficio());
			lNotModMDS.setCodTipoNotifica("N");
			lNotModMDS.setDataInvio(lDataTrasmissione);
			lNotModMDS.setUffCodUfficio(lMDS);
			lNotModMDS.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_UDS));
			lNotifiche.add(lNotModMDS);
		}

		// SETTO TDS
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS).equals(""))

		{
			NotificaModel lNotModTDS = new NotificaModel();
			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lUff.getCodUfficio());
			lNotModTDS.setCodTipoNotifica("N");
			lNotModTDS.setDataInvio(lDataTrasmissione);
			String lTribunale = this
					.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE);
			String lSedeTribunale = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS);
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(lTribunale, lSedeTribunale);
			lNotModTDS.setUffCodUfficio(lCodiceUff);
			lNotModTDS.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_TDS));
			lNotifiche.add(lNotModTDS);
		}
		// Avvocati

		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)
				&& getRequestStringParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO) != null
				&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO).equals("-")
				&& !isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)
				&& getRequestStringParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO) != null) {

			// int lIdxAvv = 0;
			Iterator lItxAvv = lAvvocati.iterator();
			while (lItxAvv.hasNext()) {
				AvvocatoSiepModel lAvvMod = (AvvocatoSiepModel) lItxAvv.next();
				NotificaModel lNotModPol = new NotificaModel();

				lNotModPol.setCodEsito("-");
				lNotModPol.setCodOperatoreInserimento(getCodUtenteConnesso());
				lNotModPol.setDataInserimento(DateUtils.getSysDate());
				lNotModPol.setCodUfficioInserimento(lUff.getCodUfficio());
				lNotModPol.setCodTipoNotifica("N");
				lNotModPol.setDataInvio(lDataTrasmissione);
				lNotModPol.setAvvIdAvvocatoFascicoloSiep(
						lAvvMod.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep());

				// Destintari per notifica (nella form precedente)
				if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
						&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA) != null
						&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
								.equals("-")) {
					String lPolizia = this
							.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
					String lSedePolizia = this
							.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);

					// indirizzo
					if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI)) {
						String lNotePolizia = this
								.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI);
						lNotModPol.setNote(lNotePolizia);
					}

					AutoritaEsternaModel lAut = new AutoritaEsternaModel();
					lAut.setCodTipoAutorita(lPolizia);

					ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
					lAut.setCodSede(lComMod.getCodComune());
					lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
					lAut.setCodUfficioInserimento(lUff.getCodUfficio());
					lAut.setDataInserimento(DateUtils.getSysDate());
					lNotModPol.setAutoritaEsterna(lAut);
					lNotModPol.setAutEstIdAutoritaEsterna(lAut.getIdAutoritaEsterna());

				}

				lNotifiche.add(lNotModPol);
			}

		}

		// Inserisco l'array di Notifiche nell'Evento
		lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveNot = lCtrl.ExInserisciEventoNotifica(lEveNot);

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misuraalternativa.action.ActDettaglioVariazioneMADecSca&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNot.getEvento().getIdEvento();

		return lPage;
	}

}