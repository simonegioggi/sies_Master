package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActInserisciSospensioneEsecPenaDispPm
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Sospensione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciSospensioneEsecPenaDispPm extends ActionSiap
		implements ICostantiSospensione, ICostantiDecretoOrdinanzaSiep {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Sospensione
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lSedeCodiceUfficio = getCodComuneUtenteConnesso();

		EventoModel lEveMod = new EventoModel();

		lEveMod.setCodTipoEvento("01");
		lEveMod.setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
		lEveMod.setCodTipoProvvedimento(getCodTipoProvvedimento(lIdFascicolo, lEveMod.getCodMotivo()));
		lEveMod.setCodUfficioEmittente(lCodiceUfficio);
		lEveMod.setCodLuogoEmittente(lSedeCodiceUfficio);
		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodEsito("-");

		lEveMod.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

		lEveMod.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));

		lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveMod.setFlagVideoSiep("S");
		lEveMod.setFlagStampaSiep("S");

		if (!isRequestParameterNullObj(FOGLIO_COMPLEMENTARE))
			lEveMod.setFlagPiuMeno("S");
		else
			lEveMod.setFlagPiuMeno("N");

		// lEveMod.setFlagDocumentoRegistrato("N");
		lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEveMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEveMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEveMod.setDataInserimento(DateUtils.getSysDate());

		// setto l'evento();
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.setEvento(lEveMod);

		// NOTIFICHE
		ArrayList lNotifiche = new ArrayList();

		// MEV_66: cambiata gestione uds + tds
		// SETTO TDS
		if (!isRequestParameterNullObj(ICostantiSospensione.CAMPO_SEDE_TDS)
				&& !isRequestParameterNullObj(ICostantiSospensione.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_TDS) != null
				&& !"".equals(getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_TDS))) {
			NotificaModel lNotModTDS = new NotificaModel();
			// "TDS"
			String lTribunale = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter("ufficioTds"),
					getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_TDS));

			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("NC");
			lNotModTDS.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
			lNotModTDS.setUffCodUfficio(lTribunale);

			lNotifiche.add(lNotModTDS);
		}

		// SETTO UDS
		if (!isRequestParameterNullObj(ICostantiSospensione.CAMPO_SEDE_UDS)
				&& !isRequestParameterNullObj(ICostantiSospensione.CAMPO_SEDE_UDS)
				&& getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_UDS) != null
				&& !"".equals(getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_UDS))) {
			NotificaModel lNotModTDS = new NotificaModel();
			// "UDS"
			String lTribunale = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter("ufficioMdS"),
					getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_UDS));

			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("NC");
			lNotModTDS.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
			lNotModTDS.setUffCodUfficio(lTribunale);

			lNotifiche.add(lNotModTDS);
		}

		// SETTO GE
		if (!isRequestParameterNullObj(ICostantiSospensione.CAMPO_SEDE_GE)
				&& getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_GE) != null
				&& !getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_GE).equals("")) {
			NotificaModel lNotModGE = new NotificaModel();
			String lGE = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiUfficio.CAMPO_TIPO_UFFICIO),
					getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_GE));

			lNotModGE.setCodEsito("-");
			lNotModGE.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModGE.setDataInserimento(DateUtils.getSysDate());
			lNotModGE.setCodUfficioInserimento(lCodiceUfficio);
			lNotModGE.setCodTipoNotifica("NG");
			lNotModGE.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
			lNotModGE.setUffCodUfficio(lGE);

			lNotifiche.add(lNotModGE);
		}

		// SETTO ISTITUTO DETENZIONE
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
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

			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);
			lNotifiche.add(lNotModIst);
		}

		// SETTO NOTIFICA AUTORITA
		if (!isRequestParameterNullObj(ICostantiSospensione.CAMPO_COD_POLIZIA)
				&& getRequestStringParameter(ICostantiSospensione.CAMPO_COD_POLIZIA) != null
				&& !getRequestStringParameter(ICostantiSospensione.CAMPO_COD_POLIZIA).equals("-")) {
			String lPolizia = getRequestStringParameter(ICostantiSospensione.CAMPO_COD_POLIZIA);
			String lSedePolizia = getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_POLIZIA);
			NotificaModel lNotModPol = new NotificaModel();

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("ND");
			lNotModPol.setNote(getRequestStringParameter(ICostantiSospensione.CAMPO_NOTE_POLIZIA));
			lNotModPol.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// SETTO NOTIFICA UFFICILI GIUDIZIARI PER CONDANNATO
		if (!isRequestParameterNullObj(ICostantiSospensione.CAMPO_COD_UGCONDANNATO)
				&& getRequestStringParameter(ICostantiSospensione.CAMPO_COD_UGCONDANNATO) != null
				&& !getRequestStringParameter(ICostantiSospensione.CAMPO_COD_UGCONDANNATO).equals("-")) {
			String lPolizia = getRequestStringParameter(ICostantiSospensione.CAMPO_COD_UGCONDANNATO);
			String lSedePolizia = this
					.getRequestStringParameter(ICostantiSospensione.CAMPO_SEDE_UGCONDANNATO);
			NotificaModel lNotModUFC = new NotificaModel();

			lNotModUFC.setCodEsito("-");
			lNotModUFC.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModUFC.setDataInserimento(DateUtils.getSysDate());
			lNotModUFC.setCodUfficioInserimento(lCodiceUfficio);
			lNotModUFC.setCodTipoNotifica("E");
			lNotModUFC.setNote(getRequestStringParameter(ICostantiSospensione.CAMPO_NOTE_UGCONDANNATO));
			lNotModUFC.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModUFC.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModUFC);
		}

		// MEV_66: aggiunto controllo preventivo
		// setto CSSA
		if (!isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)
				&& !"null".equals(getRequestStringParameter(ICostantiCSSA.CAMPO_ID_CSSA))
				&& getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA) != null
				&& getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA)
						.compareTo(new BigDecimal(0)) != 0) {
			NotificaModel lNotModCSSA = new NotificaModel();
			BigDecimal lCssa = getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);

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

		// AVVOCATI
		int lIndex = 0;
		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
			String[] lAvvocati = getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
			for (lIndex = 0; lIndex < lAvvocati.length; lIndex++) {
				NotificaModel lNotAvv = new NotificaModel();
				lNotAvv.setCodTipoNotifica("N");
				lNotAvv.setDataInvio(getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
						ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
				lNotAvv.setCodEsito("-");
				lNotAvv.setCodOperatoreInserimento(lCodiceOperatore);
				lNotAvv.setDataInserimento(DateUtils.getSysDate());
				lNotAvv.setCodUfficioInserimento(lCodiceUfficio);
				lNotAvv.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndex]));

				if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF)) {
					String[] lTipoAutoritaEsternaAvvocato = this
							.getRequestStringParameters(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF);
					String[] lSedeAutoritaEsternaAvvocato = this
							.getRequestStringParameters(ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF);

					String[] lNoteAvvocato = getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
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

		// Notifica corrispondente al Foglio Complementare
		NotificaModel lNot = new NotificaModel();
		if (!isRequestParameterNullObj(FOGLIO_COMPLEMENTARE)) {
			if (isRequestChecked(FOGLIO_COMPLEMENTARE)) {
				lNot = new NotificaModel();

				lNot.setCodTipoNotifica("CS");
				lNot.setDataInvio(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
				lNot.setCodEsito("-");
				lNot.setCodOperatoreInserimento(lCodiceOperatore);
				lNot.setDataInserimento(DateUtils.getSysDate());
				lNot.setCodUfficioInserimento(lCodiceUfficio);

				ComuneModel lComCasellarioMod = new ComuneModel(getCodComuneByDescr(
						getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS)));
				/*
				 * SedeGiudiziariaModel lSedeGiuMod = null; String lCodCas =
				 * lFascicoloModel.getSoggetto().getCodComuneCasellario(); ISedeGiudiziaria lCtrl =
				 * SIEPLookupRemote.getSedeGiudiziariaRemote(); SedeGiudiziariaModel lSedGiuMod =
				 * lCtrl.ExRicercaSedeGiudiziariaByKey(lCodCas);
				 */

				AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

				lAutMod.setCodTipoAutorita("24");

				// lAutMod.setCodSede(lSedGiuMod.getCodComune());
				lAutMod.setCodSede(lComCasellarioMod.getCodComune());
				lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
				lAutMod.setCodUfficioInserimento(lCodiceUfficio);
				lAutMod.setDataInserimento(DateUtils.getSysDate());

				// Setto l'Autorita Esterna per la notifica corrente
				lNot.setAutoritaEsterna(lAutMod);

				lNotifiche.add(lNot);
			}
		}

		lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// DEPOSITO ORDINANZA SIEP

		// l'id dell'eventuale record presente
		DecretoOrdinanzaSiepModel lDecMod = new DecretoOrdinanzaSiepModel();

		lDecMod.setIdDecretoOrdinanzaSiep(
				getRequestBigDecimalParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ID_DECRETO_ORDINANZA_SIEP));

		lDecMod.setDataSospensioneEsecuzione(getRequestDateParameter(
				ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO, ICostantiSospensione.CAMPO_MESE_DATA_INIZIO,
				ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO));
		lDecMod.setDataDepositoIstanza(
				getRequestDateParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_DEPOSITO_ISTANZA,
						ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_DEPOSITO_ISTANZA,
						ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DEPOSITO_ISTANZA));

		lDecMod.setMotivazioni(getRequestStringParameter(ICostantiDecretoOrdinanzaSiep.CAMPO_MOTIVAZIONI));

		String lIstanzaPresentata = getRequestStringParameter("istanzapresentata");
		lDecMod.setFlagPresentanteIstanza(lIstanzaPresentata);

		lDecMod.setCodEsito("-");
		lDecMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		lDecMod.setCodTipoRegistroOrdinanza("-");
		lDecMod.setCodTipoAutoritaEmittente("-");
		lDecMod.setCodTipoProvvedimento("-");
		lDecMod.setCodLuogoEmittente("-");
		lDecMod.setCodOggettoDecisione("-");
		lDecMod.setCodContenutoDecreto("-");
		lDecMod.setCodOggettoProcedimento("-");

		lDecMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lDecMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lDecMod.setDataInserimento(DateUtils.getSysDate());

		// INSERIMENTO E CALCOLO
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Recupero la situazione attuale della Pena (dati Validati)");
		ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Calcolo Pena -> " + lCalcoloPenaModel);

		ISospensione lCtrl = SIEPLookupRemote.getSospensioneRemote();
		EventoModel lEvento = lCtrl.ExInserisciEventoDecretoOrdinanzaSiep(lDecMod, lEveNot,
				lCalcoloPenaModel);

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sospensione.action.ActLoadDettaglioSospensioneEsecPenaDispPm&IdEvento="
				+ lEvento.getIdEvento().toString();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lPage;
	}

	// Il tipo di provvedimento emesso dipende dalla posizione giuridica del condannato.
	// Se Libero il codice Tipo Provvedimento è 04, altrimenti è 09.
	// Luigi 7-10-2005
	private String getCodTipoProvvedimento(BigDecimal aIdFascicoloSiep, String aCodMotivo)
			throws F3BException {

		String lCodTipoProv = "";

		if (aCodMotivo == null) {
			throw new F3BException(F3BException.USER_MESSAGE, "Codice Motivo assente");
		} else if (aCodMotivo.compareTo("0902") == 0 || aCodMotivo.compareTo("0920") == 0
				|| aCodMotivo.compareTo("0921") == 0 || aCodMotivo.compareTo("0937") == 0
				// Aggiunti i 2 nuovi codice MOTIVO_PROVVEDIMENTO Luigi 13-08-2010
				|| aCodMotivo.compareTo("0947") == 0 || aCodMotivo.compareTo("0952") == 0
				// Aggiunti i 2 nuovi codice MOTIVO_PROVVEDIMENTO Decreto 06/2013 AMBROS
				|| aCodMotivo.compareTo("1020") == 0 || aCodMotivo.compareTo("1021") == 0) {
			lCodTipoProv = "04";
		} else if (aCodMotivo.compareTo("0900") == 0 || aCodMotivo.compareTo("0901") == 0
				|| aCodMotivo.compareTo("0903") == 0) {
			// Esegue ricerca della posizione giuridica.
			IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			PosizioneGiuridicaModel lPosizione = lCtrlPosGiu
					.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(aIdFascicoloSiep);

			if (lPosizione == null) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile determinare la posizione giuridica");
			} else {
				if (lPosizione.isLibero()) {
					lCodTipoProv = "04";
				} else {
					lCodTipoProv = "09";
				}
			}
		} else {
			throw new F3BException(F3BException.USER_MESSAGE, "Codice Motivo errato -> " + aCodMotivo);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("CodTipoProvvedimento -> " + lCodTipoProv);

		return lCodTipoProv;
	}

}