package siap.siep.ordinescarcerazione.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordinescarcerazione.controller.IOrdineScarcerazione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 *
 * <p>
 * Title: ActInserisciOSLiberazioneAnticipataMA
 * </p>
 * <p>
 * Description: ActInserisciOSLiberazioneAnticipataMA
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciOSLiberazioneAnticipataMA extends ActOrdineScarcerazione
		implements ICostantiOrdineScarcerazione {

	/**
	 * Azione di Inserimento dell'oridine di scarcerazione a seguito di concessione LA oer condannato in
	 * regime di Misura Altrenativa
	 * 
	 * @return Nome della action di dettaglio da richiamere al termine dell'inserimento
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// setto la natura della MA
		// String tipoMisura = "OS_LIBERAZIONE_ANTICIPATA_MA";

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		EventoNotificaModel lEveMod = null;
		EventoNotificaModel lEveModOS = null;

		// --- Ricerca
		String lPosizione = this
				.getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		this.setRequestAttribute("posizionegiuridica", lPosizione);

		String lPage = null;

		// BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		String UffUDS = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS);
		setRequestAttribute("UffUDS", UffUDS);
		Date DataNuovaFinePena = null;

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		// Aggiorno tabella pena residua, Inserendo un record identico al precedente con la nuova data fine
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl
				.ExRicercaPenaResiduaUltimaPerFascicolo(lFascicoloModel.getIdFascicoloSiep());

		lPenaResMod.setDataFine(DataNuovaFinePena);
		lPenaResMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lPenaResMod.setDataInserimento(DateUtils.getSysDate());
		lPenaResMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
		lPenaResMod.setFlagValidato("N");

		// ==========================================================================
		// Recupero le Notifiche
		// ==========================================================================
		//Ticket#20210521012 - La dataInvio delle notifihe deve essere la data di trasmissione
		Date dataInvio = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		//Ticket#20210521012 -FINE		
		
		lEveMod = new EventoNotificaModel();
		ArrayList lNotifiche = new ArrayList();
		// SETTO CSSA
		if (!this.isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)) {
			NotificaModel lNotModCSSA = new NotificaModel();
			BigDecimal lCssa = this.getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA)) {
				String lNoteCssa = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA);
				lNotModCSSA.setNote(lNoteCssa);
			}

			lNotModCSSA.setCodEsito("-");
			lNotModCSSA.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModCSSA.setDataInserimento(DateUtils.getSysDate());
			lNotModCSSA.setCodUfficioInserimento(lCodiceUfficio);

			lNotModCSSA.setCodTipoNotifica("N");
			//Ticket#20210521012 - La dataInvio delle notifihe deve essere la data di trasmissione
			//lNotModCSSA.setDataInvio(DateUtils.getSysDate());
			lNotModCSSA.setDataInvio (dataInvio);
			//Ticket#20210521012 - FINE
			lNotModCSSA.setCssIdCssa(lCssa);
			lNotifiche.add(lNotModCSSA);

		}

		// SETTO UDS
		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS).equals("")
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS).equals("-")) {

			NotificaModel lNotModUDS = new NotificaModel();

			String lUdsTipo = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO);

			String lUDS = this.getCodUfficioByCodTipoUfficioDescrComune(lUdsTipo,
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS));
			// String lSedeMagistrato =
			// this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_MAG);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_UDS)) {
				String lNoteUDS = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_UDS);
				lNotModUDS.setNote(lNoteUDS);
			}
			lNotModUDS.setCodEsito("-");
			lNotModUDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModUDS.setDataInserimento(DateUtils.getSysDate());
			lNotModUDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModUDS.setCodTipoNotifica("N");
			//Ticket#20210521012 - La dataInvio delle notifihe deve essere la data di trasmissione
			//lNotModUDS.setDataInvio(DateUtils.getSysDate());
			lNotModUDS.setDataInvio (dataInvio);
			//Ticket#20210521012 - FINE
			lNotModUDS.setUffCodUfficio(lUDS);
			lNotifiche.add(lNotModUDS);
		}

		// SETTO TDS
		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE).equals("")
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE).equals("-")
				&& !this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS).equals("")
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS).equals("-")) {
			NotificaModel lNotModTDS = new NotificaModel();
			// String lTribunale = this
			// .getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE);
			// String lSedeTribunale = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_TDS)) {
				String lNoteTribunale = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_TDS);
				lNotModTDS.setNote(lNoteTribunale);
			}
			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("E");
			//Ticket#20210521012 - La dataInvio delle notifihe deve essere la data di trasmissione
			//lNotModTDS.setDataInvio(DateUtils.getSysDate());
			lNotModTDS.setDataInvio (dataInvio);
			//Ticket#20210521012 - FINE
			
			
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS));
			lNotModTDS.setUffCodUfficio(lCodiceUff);

			lNotifiche.add(lNotModTDS);
		}

		// SETTO ISTITUTO DETENZIONE
		if ((!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
				&& !getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
						.equals("")
				&& !getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
						.equals("-")) {
			NotificaModel lNotModIst = new NotificaModel();
			String lIstituto = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_IST)) {
				String lNoteIstituto = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_IST);
				lNotModIst.setNote(lNoteIstituto);
			}
			lNotModIst.setCodEsito("-");
			lNotModIst.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModIst.setDataInserimento(DateUtils.getSysDate());
			lNotModIst.setCodUfficioInserimento(lCodiceUfficio);
			lNotModIst.setCodTipoNotifica("N");
			// Ticket#20210521012 - La dataInvio delle notifihe deve essere la data di trasmissione
			//lNotModIst.setDataInvio(DateUtils.getSysDate());
			lNotModIst.setDataInvio (dataInvio);
			// Ticket#20210521012 - FINE
			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);
			lNotifiche.add(lNotModIst);
		}

		// SETTO AUTORITA ESTERNA PER OS
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E).equals("")
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
						.equals("-")) {
			String lTipoAutoritaEsternaE = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			String lSedeAutoritaEsternaE = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			String lSedeNoteE = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

			NotificaModel lNotModPolE = new NotificaModel();

			lNotModPolE.setNote(lSedeNoteE);
			lNotModPolE.setCodEsito("-");
			lNotModPolE.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPolE.setDataInserimento(DateUtils.getSysDate());
			lNotModPolE.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPolE.setCodTipoNotifica("N");
			// Ticket#20210521012 - La dataInvio delle notifihe deve essere la data di trasmissione
			//lNotModPolE.setDataInvio(DateUtils.getSysDate());
			lNotModPolE.setDataInvio (dataInvio);
			// Ticket#20210521012 - FINE
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lTipoAutoritaEsternaE);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedeAutoritaEsternaE));

			lAut.setCodSede(lComMod.getCodComune());
			lAut.setDescrSede(lSedeAutoritaEsternaE);
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPolE.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPolE);
		}

		lEveMod.getEvento()
				.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

		// Ticket#20210521012 - ??? Verificare
		lEveMod.getEvento()
				.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		// ---------------------------------------------
		lEveMod.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEveMod.getEvento().setCodUfficioInserimento(lCodiceUfficio);
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		// ---------------------------------------------

		setRequestAttribute("flagmisura", "S");

		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaResMod.setDataFine(lPenaResMod.getDataFine());

		lEveMod.getEvento().setCodMotivo("0083");

		lEveModOS = getEventoOrdineScarcerazione(lEveMod.getEvento());
		lEveModOS.getEvento().setCodLuogoEmittente(lUtenteMod.getUfficioUtente().getCodComune());
		lEveModOS.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEveModOS.getEvento()
				.setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_EVE_ID_EVENTO));

		lEveModOS.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// --------------++++++++++++++++-----------------------------------------
		IOrdineScarcerazione lOScarc = SIEPLookupRemote.getOrdineScarcerazione();
		EventoNotificaModel lEveModel = lOScarc.ExInserisciOModificaEventoNotifica(lEveModOS);
		// --------------++++++++++++++++-----------------------------------------

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOSLiberazioneAnticipataMA&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveModel.getEvento().getIdEvento();

		return lPage;
	}

}