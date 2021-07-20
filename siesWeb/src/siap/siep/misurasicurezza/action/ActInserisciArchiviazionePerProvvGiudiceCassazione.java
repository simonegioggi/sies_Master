package siap.siep.misurasicurezza.action;

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
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.archiviazione.action.ICostantiArchiviazione;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciArchiviazionePerProvvGEsecuzione
 * </p>
 * <p>
 * Description: Classe per Inserimento di Archiviazione
 * </p>
 * <p>
 * ( Tipo_ve = 01 e Tipo_Provv = 25 (Annotazione) )
 * </p>
 * <p>
 * per Provvedimenti emesso dal Giudice/Cassazione
 * </p>
 * <p>
 * (Definizione. MIS. SIC. Provvisorie o Disposte fuori Sentenza)
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciArchiviazionePerProvvGiudiceCassazione extends ActionSiap implements
		ICostantiMisuraSicurezza, ICostantiArchiviazione {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// dati operatore/ufficio Inserimento
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		Date lDataArch = getRequestDateParameter(ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE,
				ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE,
				ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE);

		// evento notifica da passare al metodo di inserimento
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();

		// evento ================
		EventoModel lEveMod = new EventoModel();

		lEveMod.setCodTipoEvento("01");
		lEveMod.setCodTipoProvvedimento("25");
		lEveMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));

		lEveMod.setDataEmissione(lDataArch);
		// Ufficio Emittente annotazione = ufficio inserimento dell'evento
		lEveMod.setCodUfficioEmittente(lCodiceUfficio);
		lEveMod.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());

		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodUfficioDestinatario("-");
		lEveMod.setCodEsito("-");
		lEveMod.setCodMagistrato(calcolaMagistrato());
		lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveMod.setFlagVideoSiep("S"); //
		lEveMod.setFlagStampaSiep("S"); //

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO) != null
				&& !getRequestStringParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO).equals("")) {
			lEveMod.setAnnoProtocollo(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO));
			lEveMod.setProgrProtocollo(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO));
		} else
			lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEveMod.setCodOperatoreInserimento(lCodiceOperatore);
		lEveMod.setCodUfficioInserimento(lCodiceUfficio);
		lEveMod.setDataInserimento(DateUtils.getSysDate());

		// setto l'evento dentro l'eventonotificaModel
		lEveNotMod.setEvento(lEveMod);

		// - - - - - - - - - - - - - - -
		// Notifiche ---------> Altra Autorità

		ArrayList lNotificheArray = new ArrayList();

		String lSedeDestinatario_E = null;
		String lDestinatario_E = null;
		String lNote_E = null;

		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& !this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
						.equals("-")) {
			lDestinatario_E = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			lSedeDestinatario_E = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
		}

		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E))
			lNote_E = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

		NotificaModel lNotMod = new NotificaModel();
		lNotMod.setCodTipoNotifica("AA");
		lNotMod.setDataInvio(lDataArch);
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setNote(lNote_E);

		if (lDestinatario_E != null) {
			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

			lAutMod.setCodTipoAutorita(lDestinatario_E);
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescrFlagVal(lSedeDestinatario_E));
			lAutMod.setCodSede(lComModel.getCodComune());
			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setIstDetIdIstitutoDetenzione("");

			lNotMod.setAutoritaEsterna(lAutMod);
			lNotificheArray.add(lNotMod);
		}

		// Notifica Istituto di detenzione
		String lDestinatario_ist = null;
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& this.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) != null
				&& !this.getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE).equals(""))
			lDestinatario_ist = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

		if (lDestinatario_ist != null) {
			NotificaModel lNotistMod = new NotificaModel();
			lNotistMod.setCodTipoNotifica("E");
			lNotistMod.setDataInvio(lDataArch);
			lNotistMod.setCodEsito("-");
			lNotistMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotistMod.setDataInserimento(DateUtils.getSysDate());
			lNotistMod.setCodUfficioInserimento(lCodiceUfficio);

			lNotistMod.setIstDetIdIstitutoDetenzione(lDestinatario_ist);
			lNotificheArray.add(lNotistMod);
		}

		// Notifiche ---------> Destinatari UDS
		if (!isRequestParameterNullObj("tipoUDS") && !getRequestStringParameter("tipoUDS").equals("-")) {
			String lTipoUds = getRequestStringParameter("tipoUDS"); // per es. TDS
			String lComuneUds = getRequestStringParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO); // per es.
																								// BARI
			String lCodUffUDS = getCodUfficioByCodTipoUfficioDescrComune(lTipoUds, lComuneUds);

			NotificaModel lNotUffUDS = new NotificaModel();

			lNotUffUDS.setCodEsito("-");
			lNotUffUDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotUffUDS.setDataInserimento(DateUtils.getSysDate());
			lNotUffUDS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotUffUDS.setCodTipoNotifica("MS");
			lNotUffUDS.setDataInvio(lDataArch);

			lNotUffUDS.setUffCodUfficio(lCodUffUDS);

			lNotificheArray.add(lNotUffUDS);
		}

		// ========================================================================
		// Recupera, se presenti le notifiche agli avvocati.
		// Le notifiche possono essere effettuate o Tramite Unep o tramite il
		// 'Sistema Notifiche Telematiche'. Ciò che cambia è il record Autorità esterna.
		if (isRequestChecked("Difesa")) {
			String[] lAvvocati = null;
			if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
				lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
			}

			String[] lSedeDestinatario_avv = null;
			String[] lDestinatario_avv = null;
			String[] lNote_avv = null;

			// Tipo Autorità
			if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)) {
				lDestinatario_avv = this
						.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
			}

			// Sede
			if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_SEDE)) {
				lSedeDestinatario_avv = this
						.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
			}

			// Indirizzo
			if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)) {
				lNote_avv = this.getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
			}

			int lIndNotifiche = 0;
			int lNumAvvNotifiche = 0;

			if (lAvvocati != null)
				lNumAvvNotifiche = lAvvocati.length;

			if (lSedeDestinatario_avv != null) {
				if (lAvvocati != null) {
					while (lIndNotifiche < lNumAvvNotifiche) {
						NotificaModel lNot = new NotificaModel();

						lNot.setCodTipoNotifica("ND");

						lNot.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndNotifiche]));
						lNot.setNote(lNote_avv[lIndNotifiche]);
						lNot.setDataInvio(lDataArch);
						lNot.setCodEsito("-");

						lNot.setCodOperatoreInserimento(this.getCodUtenteConnesso());
						lNot.setDataInserimento(DateUtils.getSysDate());
						lNot.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

						//
						AutoritaEsternaModel lAut = new AutoritaEsternaModel();
						if (isRequestChecked("SiNoTe")) { // Sistema Notifiche Telematiche
															// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la
															// variabile di istanza siesLogger al posto di
															// LogF3B.getLogger()
															// siesLogger.debug("Sistema Notifiche Telematiche");
							lAut.setCodTipoAutorita("C0");
							lAut.setCodSede("-");
						} else {
							// Notifica tramite UNEP
							// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							// siesLogger.debug("Notifiche all'UNEP");
							lAut.setCodTipoAutorita(lDestinatario_avv[lIndNotifiche]);
							//INIZIO: MEV_21 (avvocati) - si inibisce la selezione di comuni non validi (tipo NAPOLI NORD)
//							ComuneModel lComMod = new ComuneModel(
//									getCodComuneByDescr(lSedeDestinatario_avv[lIndNotifiche]));
							ComuneModel lComMod = new ComuneModel(
									getCodComuneByDescrFlagVal(lSedeDestinatario_avv[lIndNotifiche]));
							//FINE: MEV_21
							lAut.setCodSede(lComMod.getCodComune());
							lAut.setDescrizione(lComMod.getDescrizione());
						}

						lAut.setCodOperatoreInserimento(this.getCodUtenteConnesso());
						lAut.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
						lAut.setDataInserimento(DateUtils.getSysDate());

						// Setto l'Autorita Esterna per la notifica corrente
						lNot.setAutoritaEsterna(lAut);
						lIndNotifiche++;
						lNotificheArray.add(lNot);
					}
				}
			}
		}
		// ========================================================================
		NotificaModel[] lNotifiche = (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);

		lEveNotMod.setNotifiche(lNotifiche);

		// = = = = = = = = = = = = =

		// archiviazione =====================
		ArchiviazioneModel lArcMod = new ArchiviazioneModel();

		lArcMod.setCodTipoProvvedimento("25"); // ANNTAZIONE

		// Ufficio Emittente: Archiviazione provvedimento Giudice / Cassazione (COD = 0002 - RV_Domain =
		// DEFI_ALTRO - RV_Meaning = 'Altra Autorità' )
		lArcMod.setCodProvvedimento("0002");

		// Ordinanza/decreto - Evento.COD_TIPO_PROVVEDIMENTO / Archiviazione.COD_TIPO_PROVVEDIMENTO_ARC
		lArcMod.setCodTipoProvvedimentoArc(getRequestStringParameter(ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC));

		// Evento.Cod Motivo = Archiviazione.Cod oggetto definizione
		lArcMod.setCodOggettoDefinizione(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));

		lArcMod.setDataDefinizione(getRequestDateParameter(
				ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE,
				ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE,
				ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE));

		lArcMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE,
				CAMPO_MESE_DATA_EMISSIONE, CAMPO_GIORNO_DATA_EMISSIONE));
		lArcMod.setDataRicezione(getRequestDateParameter(CAMPO_ANNO_DATA_RICEZIONE,
				CAMPO_MESE_DATA_RICEZIONE, CAMPO_GIORNO_DATA_RICEZIONE));

		lArcMod.setCodTipoEmittente("-");

		if (!this.isRequestParameterNullObj(CAMPO_COD_TIPO_AUTORITA_EMITTENTE)
				&& this.getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE) != null
				&& !this.getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE).equals("-")) {
			lArcMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
			ComuneModel lCom = new ComuneModel(
					getCodComuneByDescrFlagVal(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
			// MEV2 STEP2 - 01-03-16 - Metodo inserito per controllare l'esistenza dell'uffico nel comune
			// selezionato
			/*String lCodiceuf = */getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE), lCom.getDescrizione());

			lArcMod.setCodLuogoEmittente(lCom.getCodComune());
		}

		/*
		 * if(! isRequestParameterNullObj(ICostantiArchiviazione.CAMPO_INDIRIZZO_EMITTENTE) &&
		 * getRequestStringParameter(ICostantiArchiviazione.CAMPO_INDIRIZZO_EMITTENTE) != null &&
		 * !getRequestStringParameter(ICostantiArchiviazione.CAMPO_INDIRIZZO_EMITTENTE).equals("")) {
		 * lArcMod.setIndirizzoEmittente(getRequestStringParameter(CAMPO_INDIRIZZO_EMITTENTE)); }
		 */

		if (!isRequestParameterNullObj(ICostantiArchiviazione.CAMPO_NOTE)
				&& getRequestStringParameter(ICostantiArchiviazione.CAMPO_NOTE) != null
				&& !getRequestStringParameter(ICostantiArchiviazione.CAMPO_NOTE).equals("")) {
			lArcMod.setNote(getRequestStringParameter(ICostantiArchiviazione.CAMPO_NOTE));
		}

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO) != null
				&& !getRequestStringParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO).equals("")) {
			lArcMod.setAnnoProvvedimento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO));
			lArcMod.setNumProvvedimento(getRequestStringParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO));
		}

		lArcMod.setCodOperatoreInserimento(lCodiceOperatore);
		lArcMod.setCodUfficioInserimento(lCodiceUfficio);
		lArcMod.setDataInserimento(DateUtils.getSysDate());
		lArcMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		// inserimento
		IArchiviazione lCtrl = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel lArcModRes = lCtrl.ExInserisciEventoNotificaArchiviazione(lEveNotMod, lArcMod,
				lFascMod);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActLoadDettaglioArchiviazionePerProvvGiudiceCassazione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lArcModRes.getEveIdEvento();

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

}