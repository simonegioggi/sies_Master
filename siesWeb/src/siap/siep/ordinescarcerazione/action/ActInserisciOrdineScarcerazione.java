package siap.siep.ordinescarcerazione.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.CalendarUtil;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Inserimento degli Ordini di Esecuzione
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
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
@SuppressWarnings("rawtypes")
public class ActInserisciOrdineScarcerazione extends ActOrdineScarcerazione
		implements ICostantiOrdineScarcerazione {

	/**
	 * Azione di Inserimento del Evento
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lPage = "";

		Integer lPosInt = new Integer(
				getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA));

		switch (lPosInt.intValue()) {
		case 03: {
			lPage = InserisciOSDetenutoCarcere();
			break;
		}
		case 12: {
			lPage = InserisciOSDetenutoDomiciliare();
			break;
		}
		case 04: {
			lPage = InserisciOSDetenutoDomiciliare();
			break;
		}
		case 14: {
			lPage = InserisciOSSemiliberta();
			break;
		}
		default:
			if (lFascicoloModel.getFlagAltraCausa() != null
					&& lFascicoloModel.getFlagAltraCausa().equals("S")) {
				lPage = InserisciOSDetenutoCarcere();
				break;
			} else {
				lPage = InserisciOSAltrePosizioni();
				break;
			}
		}

		ScadenzarioModel lScaModFasc = null;
		IScadenzario lCtrl = SIEPLookupRemote.getScadenzarioRemote();
		try {
			List lScaden = lCtrl.ExScadenzarioByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());
			Iterator lIter = lScaden.iterator();

			while (lIter.hasNext()) {
				lScaModFasc = (ScadenzarioModel) lIter.next();
				lCtrl.ExCancellaScadenzarioSimeone(lScaModFasc);
			}
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE) {
				throw e;
			}
		}
		return lPage;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private String InserisciOSDetenutoCarcere() throws Exception {
		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("SC");
		lEve.getEvento().setCodMotivo("0082");
		// lEve.getEvento().setFlagDocumentoRegistrato("N");
		lEve = getEventoOrdineScarcerazione(lEve.getEvento());
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOS();

		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE)) {
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			// Controllo se c'è FUNGIBILITA'
			String lFlagFungi = new String(getRequestStringParameter("fungibilita"));
			if (lFlagFungi.equals("S")) {

				CalendarModel lCalMod = new CalendarModel();
				CalendarUtil lCalUtil = new CalendarUtil();
				lCalMod.setDataInizio(lPenaRes.getDataFine());
				lCalMod.setDataFine(lEve.getEvento().getDataEmissione());
				lCalMod = lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod);
				FungibilitaModel lFunMod = new FungibilitaModel();
				lFunMod.setNumAnni(new BigDecimal(lCalMod.getNumAnni()));
				lFunMod.setNumMesi(new BigDecimal(lCalMod.getNumMesi()));
				lFunMod.setNumGiorni(new BigDecimal(lCalMod.getNumGiorni()));

				// Dalla Tabella Posizione Giuridica estrarre i dati con DataFine =Null e Fascicolo Corrente
				PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
				IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
				lPos = lPosCtrl.ExRicercaPosizioneLuogoDetAltraCausaByIdFascicoloDataFineNull(
						lEve.getEvento().getFasSieIdFascicoloSiep());
				setRequestAttribute("posizioneluogoaltra", lPos);

				Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
				setRequestAttribute("autoritaEsterna", "" + lOption);

				IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
				Vector lAvvocati = lAvvCtrl
						.ExRicercaAvvocatiByFascicolo(lEve.getEvento().getFasSieIdFascicoloSiep());
				setRequestAttribute("avvocati", lAvvocati);

				// lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimento(), "01");
				// setRequestAttribute("sentenza", "" + lOption);

				setRequestAttribute("fungibilita", lFunMod);
				setRequestAttribute("evento", lEve);
				setRequestAttribute("penaresidua", lPenaRes);
				String lPage = PG_INSERISCI_OS_FUNGIBILITA;
				return lPage;
			} else {
				// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
				IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
				EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve,
						lPenaRes);

				String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
						+ "&modalita=I";

				return lPage;
			}
		} else {
			// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
			IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
			EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve,
					lPenaRes);

			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
					+ "&modalita=I";

			return lPage;
		}

	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private String InserisciOSDetenutoDomiciliare() throws Exception {
		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("SC");
		lEve.getEvento().setCodMotivo("0082");
		// lEve.getEvento().setFlagDocumentoRegistrato("N");
		lEve = getEventoOrdineScarcerazione(lEve.getEvento());
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOS();

		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE)) {
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			// Controllo se c'è FUNGIBILITA'
			String lFlagFungi = new String(getRequestStringParameter("fungibilita"));
			if (lFlagFungi.equals("S")) {

				CalendarModel lCalMod = new CalendarModel();
				CalendarUtil lCalUtil = new CalendarUtil();
				lCalMod.setDataInizio(lPenaRes.getDataFine());
				lCalMod.setDataFine(lEve.getEvento().getDataEmissione());
				lCalMod = lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod);
				FungibilitaModel lFunMod = new FungibilitaModel();
				lFunMod.setNumAnni(new BigDecimal(lCalMod.getNumAnni()));
				lFunMod.setNumMesi(new BigDecimal(lCalMod.getNumMesi()));
				lFunMod.setNumGiorni(new BigDecimal(lCalMod.getNumGiorni()));
				// lFunMod.setCodTipoFungibilita("02");
				// lFunMod.setFlagValidato("S");
				// lFunMod.setFasSieIdFascicoloSiep(lEve.getEvento().getFasSieIdFascicoloSiep());

				// inserisco Fungibilità
				/*
				 * IFungibilita lCtrlFung = SIEPLookupRemote.getFungibilitaRemote(); FungibilitaModel
				 * lFunModel = lCtrlFung.ExInserisciFungibilita(lFunMod);
				 */

				// Dalla Tabella Posizione Giuridica estrarre i dati con DataFine =Null e Fascicolo Corrente
				PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
				IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
				lPos = lPosCtrl.ExRicercaPosizioneLuogoDetAltraCausaByIdFascicoloDataFineNull(
						lEve.getEvento().getFasSieIdFascicoloSiep());
				setRequestAttribute("posizioneluogoaltra", lPos);

				Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
				setRequestAttribute("autoritaEsterna", "" + lOption);

				IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
				Vector lAvvocati = lAvvCtrl
						.ExRicercaAvvocatiByFascicolo(lEve.getEvento().getFasSieIdFascicoloSiep());
				setRequestAttribute("avvocati", lAvvocati);

				// lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimento(), "01");
				// setRequestAttribute("sentenza", "" + lOption);

				setRequestAttribute("fungibilita", lFunMod);
				setRequestAttribute("evento", lEve);
				setRequestAttribute("penaresidua", lPenaRes);

				String lPage = PG_INSERISCI_OS_FUNGIBILITA;

				return lPage;
			} else {
				// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
				IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
				EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve,
						lPenaRes);
				String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
						+ "&modalita=I";
				return lPage;
			}
		} else {
			// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
			IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
			EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve,
					lPenaRes);
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
					+ "&modalita=I";
			return lPage;
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private String InserisciOSSemiliberta() throws Exception {
		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("SC");
		lEve.getEvento().setCodMotivo("0082");
		// lEve.getEvento().setFlagDocumentoRegistrato("N");
		lEve = getEventoOrdineScarcerazione(lEve.getEvento());
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		// lEve.getMagistrato().getMagistratoCompetente().setMagCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOS();
		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE)) {
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			// Controllo se c'è FUNGIBILITA'
			String lFlagFungi = new String(getRequestStringParameter("fungibilita"));
			if (lFlagFungi.equals("S")) {

				CalendarModel lCalMod = new CalendarModel();
				CalendarUtil lCalUtil = new CalendarUtil();
				lCalMod.setDataInizio(lPenaRes.getDataFine());
				lCalMod.setDataFine(lEve.getEvento().getDataEmissione());
				lCalMod = lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod);
				FungibilitaModel lFunMod = new FungibilitaModel();
				lFunMod.setNumAnni(new BigDecimal(lCalMod.getNumAnni()));
				lFunMod.setNumMesi(new BigDecimal(lCalMod.getNumMesi()));
				lFunMod.setNumGiorni(new BigDecimal(lCalMod.getNumGiorni()));
				// lFunMod.setCodTipoFungibilita("02");
				// lFunMod.setFlagValidato("S");
				// lFunMod.setFasSieIdFascicoloSiep(lEve.getEvento().getFasSieIdFascicoloSiep());

				// inserisco Fungibilità
				/*
				 * IFungibilita lCtrlFung = SIEPLookupRemote.getFungibilitaRemote(); FungibilitaModel
				 * lFunModel = lCtrlFung.ExInserisciFungibilita(lFunMod);
				 */

				// Dalla Tabella Posizione Giuridica estrarre i dati con DataFine =Null e Fascicolo Corrente
				PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
				IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
				lPos = lPosCtrl.ExRicercaPosizioneLuogoDetAltraCausaByIdFascicoloDataFineNull(
						lEve.getEvento().getFasSieIdFascicoloSiep());
				setRequestAttribute("posizioneluogoaltra", lPos);

				Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
				setRequestAttribute("autoritaEsterna", "" + lOption);

				IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
				Vector lAvvocati = lAvvCtrl
						.ExRicercaAvvocatiByFascicolo(lEve.getEvento().getFasSieIdFascicoloSiep());
				setRequestAttribute("avvocati", lAvvocati);

				// lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimento(), "01");
				// setRequestAttribute("sentenza", "" + lOption);

				setRequestAttribute("fungibilita", lFunMod);
				setRequestAttribute("evento", lEve);
				setRequestAttribute("penaresidua", lPenaRes);

				String lPage = PG_INSERISCI_OS_FUNGIBILITA;
				return lPage;
			} else {
				// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
				IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
				EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve,
						lPenaRes);

				String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
						+ "&modalita=I";

				return lPage;
			}
		} else {
			// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
			IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
			EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve,
					lPenaRes);

			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
					+ "&modalita=I";

			return lPage;

		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private String InserisciOSAltrePosizioni() throws Exception {

		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("0000");
		lEve.getEvento().setCodMotivo("0000");
		// lEve.getEvento().setFlagDocumentoRegistrato("N");
		lEve = getEventoOrdineScarcerazione(lEve.getEvento());
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		// lEve.getMagistrato().getMagistratoCompetente().setMagCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOS();
		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);

		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE)) {
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			// Controllo se c'è FUNGIBILITA'
			String lFlagFungi = new String(getRequestStringParameter("fungibilita"));
			if (lFlagFungi.equals("S")) {

				CalendarModel lCalMod = new CalendarModel();
				CalendarUtil lCalUtil = new CalendarUtil();
				lCalMod.setDataInizio(lPenaRes.getDataFine());
				lCalMod.setDataFine(lEve.getEvento().getDataEmissione());
				lCalMod = lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod);
				FungibilitaModel lFunMod = new FungibilitaModel();
				lFunMod.setNumAnni(new BigDecimal(lCalMod.getNumAnni()));
				lFunMod.setNumMesi(new BigDecimal(lCalMod.getNumMesi()));
				lFunMod.setNumGiorni(new BigDecimal(lCalMod.getNumGiorni()));

				// Dalla Tabella Posizione Giuridica estrarre i dati con DataFine =Null e Fascicolo Corrente
				PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
				IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
				lPos = lPosCtrl.ExRicercaPosizioneLuogoDetAltraCausaByIdFascicoloDataFineNull(
						lEve.getEvento().getFasSieIdFascicoloSiep());
				setRequestAttribute("posizioneluogoaltra", lPos);

				Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
				setRequestAttribute("autoritaEsterna", "" + lOption);

				IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
				Vector lAvvocati = lAvvCtrl
						.ExRicercaAvvocatiByFascicolo(lEve.getEvento().getFasSieIdFascicoloSiep());
				setRequestAttribute("avvocati", lAvvocati);

				setRequestAttribute("fungibilita", lFunMod);
				setRequestAttribute("evento", lEve);
				setRequestAttribute("penaresidua", lPenaRes);

				String lPage = PG_INSERISCI_OS_FUNGIBILITA;
				return lPage;
			} else {
				// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
				IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
				EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve,
						lPenaRes);

				String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
						+ "&modalita=I";

				return lPage;
			}
		} else {
			// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
			IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
			EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve,
					lPenaRes);

			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
					+ "&modalita=I";

			return lPage;
		}
	}

}