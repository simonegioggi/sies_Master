package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;

/**
 *
 * <p>
 * Title: ActInserisciOrdineEsecuzione
 * </p>
 * <p>
 * Description: Inserimento degli Ordini di Esecuzione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
public class ActInserisciOrdineEsecuzioneSanSos extends ActOrdineEsecuzione
		implements ICostantiOrdineEsecuzione {

	/**
	 * Azione di Inserimento del Evento
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lPage = "";
		if (lFascicoloModel.getFlagAltraCausa() != null && lFascicoloModel.getFlagAltraCausa().equals("S")) {
			lPage = InserisciOEAltraCausa();
		} else {
			Integer lPosInt = new Integer(
					getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA));

			switch (lPosInt.intValue()) {
			// 20191002 [SG]: intervento post collaudo 11.3 --> aggiunto caso "01"
			// Custodia Cautelare per Questa Causa in Regime di Detenzione
			case 1: {
				lPage = InserisciOEDetenutoQC();
				break;
			}
			/*
			 * case 2: { lPage = InserisciOEArrestiDomiciliari(); break; } //--- Va in altre posizioni ---
			 * case 3: { lPage = InserisciOEDetenutoQC();break;} case 4: { lPage =
			 * InserisciOEArrestiDomiciliari(); break; } case 7: case 16: case 20: case 46: case 47: { lPage =
			 * InserisciOELibero(); break; } //--- Va in altre posizioni --- case 11:{ lPage =
			 * InserisciOEDetenutoQC();break;}
			 */
			case 07:
			case 10: {
				lPage = InserisciOELibero();
				break;
			}
			/*
			 * case 12: { lPage = InserisciOEDetDom(); break; }
			 */
			default: {
				/*
				 * //Metodo per posizioni diverse da quelle precedenti lPage = InserisciLSAltrePosizioni();
				 * break;
				 */
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Posizione Giuridica non gestita, impossibile procedere!");

				return IWebConstants.PG_MESSAGE;
			}
			}
		}

		return lPage;
	}

	/**
	 * Inserisci l'ordine di Esecuzione per Libero
	 *
	 * @return
	 * @throws Exception
	 */
	private String InserisciOELibero() throws Exception {

		EventoNotificaModel lEve = new EventoNotificaModel();
		// lEve.getEvento().setDescrMotivo("OE-LIB");

		lEve.setEvento(setEventoOrdineEsecuzioneSanSos(lEve.getEvento()));

		lEve.getEvento().setCodMotivo("0397"); // Per la carcerazione - Libero - Conversione Sanzione
												// Sostitutiva

		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();

		lEve.setNotifiche(lNotifiche);

		/*
		 * BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		 * PenaResiduaModel lPenaRes = new PenaResiduaModel(); lPenaRes.setIdPenaResidua(lIdPenaRes); if
		 * (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
		 * lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
		 * ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
		 */
		PenaResiduaModel lPenaRes = setPenaResiduaSanSos(
				getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOELiberoSanSos&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

	/**
	 * Inserisci l'Ordine di Esecuzione per un Detenuto Altra Causa
	 *
	 * @return
	 * @throws Exception
	 */
	private String InserisciOEAltraCausa() throws Exception {

		EventoNotificaModel lEve = new EventoNotificaModel();
		// lEve.getEvento().setDescrMotivo("OE-DET-AC");

		lEve.setEvento(setEventoOrdineEsecuzioneSanSos(lEve.getEvento()));

		lEve.getEvento().setCodMotivo("0398"); // Per la carcerazione - Detenuto altra causa - Conversione
												// Sanzione Sostitutiva

		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();

		lEve.setNotifiche(lNotifiche);

		/*
		 * BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		 * PenaResiduaModel lPenaRes = new PenaResiduaModel(); lPenaRes.setIdPenaResidua(lIdPenaRes); if
		 * (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
		 * lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
		 * ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
		 */

		PenaResiduaModel lPenaRes = setPenaResiduaSanSos(
				getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOEAltraCausaSanSos&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

	/**
	 * Inserisci l'ordine di Esecuzione per Detenzione Domiciliare
	 *
	 * @return
	 * @throws Exception
	 */
	/*
	 * private String InserisciOEDetDom() throws Exception { EventoNotificaModel lEve = new
	 * EventoNotificaModel(); lEve.getEvento().setDescrMotivo("OE-DETD");
	 *
	 * lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
	 * lEve.getMagistrato().setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
	 *
	 * //Inserisco l'array di Notifiche nell'Evento NotificaModel[] lNotifiche =
	 * this.setNotificheOrdineEsecuzione();
	 *
	 * lEve.setNotifiche(lNotifiche);
	 *
	 * BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
	 * PenaResiduaModel lPenaRes = new PenaResiduaModel(); lPenaRes.setIdPenaResidua(lIdPenaRes); if
	 * (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
	 * lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
	 * ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
	 *
	 * // Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
	 * IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
	 * EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);
	 *
	 * String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
	 * "=siap.siep.ordineesecuzione.action.ActDettaglioOEAltrePosizioni&" + ICostantiEvento.CAMPO_ID_EVENTO +
	 * "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";
	 *
	 * return lPage; }
	 */

	/**
	 * Inserisci l'Ordine di Esecuzione per un Detenuto Questa Causa
	 *
	 * @return
	 * @throws Exception
	 */

	private String InserisciOEDetenutoQC() throws Exception {

		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("OE-DET-QC");

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOEDetenutoQC&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

	/**
	 * Inserisci l'Ordine di Esecuzione per un Detenuto Arresti Domiciliari
	 *
	 * @return
	 * @throws Exception
	 */
	/*
	 * private String InserisciOEArrestiDomiciliari() throws Exception { EventoNotificaModel lEve = new
	 * EventoNotificaModel(); lEve.getEvento().setDescrMotivo("OE-ARR");
	 *
	 * lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
	 * lEve.getMagistrato().setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
	 *
	 * //Inserisco l'array di Notifiche nell'Evento NotificaModel[] lNotifiche =
	 * this.setNotificheOrdineEsecuzione(); lEve.setNotifiche(lNotifiche);
	 *
	 * BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
	 * PenaResiduaModel lPenaRes = new PenaResiduaModel(); lPenaRes.setIdPenaResidua(lIdPenaRes); if
	 * (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
	 * lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
	 * ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
	 *
	 * // Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
	 * IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
	 * EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);
	 *
	 * String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
	 * "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOEArrestiDomiciliari&" +
	 * ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";
	 *
	 * return lPage; }
	 */

	/**
	 * Inserisci l'Ordine di Esecuzione per Altre posizioni giuridiche
	 *
	 * @return
	 * @throws Exception
	 */
	/*
	 * private String InserisciOEAltrePosizioni() throws Exception { EventoNotificaModel lEve = new
	 * EventoNotificaModel(); lEve.getEvento().setDescrMotivo("0000");
	 *
	 * lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
	 * lEve.getMagistrato().setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
	 * //lEve.getMagistrato().getMagistratoCompetente().setMagCodMagistrato(getRequestStringParameter(
	 * ICostantiEvento.CAMPO_COD_MAGISTRATO));
	 *
	 * //Inserisco l'array di Notifiche nell'Evento NotificaModel[] lNotifiche =
	 * this.setNotificheOrdineEsecuzione(); lEve.setNotifiche(lNotifiche);
	 *
	 * BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
	 * PenaResiduaModel lPenaRes = new PenaResiduaModel(); lPenaRes.setIdPenaResidua(lIdPenaRes); if
	 * (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
	 * lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
	 * ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
	 *
	 * // Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
	 * IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
	 * EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);
	 *
	 * String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
	 * "=siap.siep.ordineesecuzione.action.ActDettaglioOEAltrePosizioni&" + ICostantiEvento.CAMPO_ID_EVENTO +
	 * "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";
	 *
	 * return lPage; }
	 */

	protected EventoModel setEventoOrdineEsecuzioneSanSos(EventoModel aEvento) throws F3BException {
		EventoModel lEve = new EventoModel(aEvento);

		lEve.setCodTipoEvento("01"); // Tipo Evento = PROVVEDIMENTO
		lEve.setCodTipoProvvedimento("06"); // Tipo Provvedimento = ORDINE ESECUZIONE

		lEve.setFlagStampaSiep("S");
		lEve.setFlagVideoSiep("S");

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.setDataEmissione(lDataEmissione);

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		lEve.setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		lEve.setCodOperatoreInserimento(lCodiceOperatore);
		lEve.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEve.setCodUfficioEmittente(lCodiceUfficio);
		lEve.setDataInserimento(DateUtils.getSysDate());
		lEve.setCodUfficioInserimento(lCodiceUfficio);
		lEve.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.setCodOperatoreAggiornamento(lCodiceOperatore);
		lEve.setCodUfficioAggiornamento(lCodiceUfficio);
		lEve.setDataAggiornamento(DateUtils.getSysDate());

		lEve.setCodEsito("-");
		lEve.setCodLuogoDestinatario("-");
		lEve.setCodTipoUfficioDestinatario("-");
		lEve.setCodMagistrato(this.calcolaMagistrato());

		return lEve;
	}

	protected PenaResiduaModel setPenaResiduaSanSos(BigDecimal aIdEventoSanSos) throws F3BException {
		PenaResiduaModel lPenaSanSosConvertita = null;

		// Nell'annotazione legata all'evento di "Revoca Sanzione Sostitutiva"
		// sono contenuti i dati della nuova pena residua da eseguire
		IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
		AnnotazioneManualeModel lAnnMod = lCtrlAnn.ExRicercaAnnotazioniManualiByIdEvento(aIdEventoSanSos);

		if (lAnnMod != null) {
			lPenaSanSosConvertita = new PenaResiduaModel();

			lPenaSanSosConvertita.setQuantumReclusione(lAnnMod.getQuantumReclusione());
			lPenaSanSosConvertita.setQuantumArresto(lAnnMod.getQuantumArresto());
			lPenaSanSosConvertita.setImportoMulta(lAnnMod.getImportoMulta());
			lPenaSanSosConvertita.setImportoAmmenda(lAnnMod.getImportoAmmenda());

			lPenaSanSosConvertita.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lPenaSanSosConvertita.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lPenaSanSosConvertita.setDataInserimento(DateUtils.getSysDate());

			lPenaSanSosConvertita.setDiesAQuo("N"); // ?
			FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
			lPenaSanSosConvertita.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

			lPenaSanSosConvertita.setFlagErgastolo("N");

			lPenaSanSosConvertita.setFlagValidato("N");
		} else {
			throw new F3BException(F3BException.USER_MESSAGE,
					"I dati della nuova pena da eseguire non sono presenti. Impossibile proseguire.");
		}

		return lPenaSanSosConvertita;
	}

}