package siap.siep.ordinescarcerazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
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
import siap.siep.ordinescarcerazione.controller.IOrdineScarcerazione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Inserimento degli Ordini di Esecuzione
 * <p>
 * Title:ActInserisciOSFuturaMemoria
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
public class ActInserisciOSFuturaMemoria extends ActOrdineScarcerazione
		implements ICostantiOrdineScarcerazione {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Evento
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lPage = "";

		Integer lPosInt = new Integer(
				getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA));

		switch (lPosInt.intValue()) {
		case 01: {
			lPage = InserisciOSDetenutoCarcere();
			break;
		}
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
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lPage;
	}

	/**
	 * InserisciOSDetenutoCarcere
	 * 
	 * @return lPage
	 * @throws Exception
	 */
	private String InserisciOSDetenutoCarcere() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".InserisciOSDetenutoCarcere: inizio");

		String lPage = ""; // pagina di ritorno

		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("SC");
		lEve.getEvento().setCodMotivo("0082");
		lEve = getEventoOrdineScarcerazione(lEve.getEvento());
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOS();

		lEve.setNotifiche(lNotifiche);

		// Controllo se c'è FUNGIBILITA'
		String lFlagFungi = new String(getRequestStringParameter("fungibilita"));
		if (lFlagFungi.equals("S")) {
			lPage = calcolaFungibilita(lEve);
		} else {
			// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
			IOrdineScarcerazione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineScarcerazione();
			EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaEventoNotifica(lEve);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
					+ "&modalita=I";
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".InserisciOSDetenutoCarcere: fine");

		return lPage;
	}

	/**
	 * p Detenuto Domiciliare
	 * 
	 * @return
	 * @throws Exception
	 */
	private String InserisciOSDetenutoDomiciliare() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".InserisciOSDetenutoDomiciliare: inizio");
		String lPage = ""; // pagina di ritorno

		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("SC");
		lEve.getEvento().setCodMotivo("0082");
		lEve = getEventoOrdineScarcerazione(lEve.getEvento());
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOS();

		lEve.setNotifiche(lNotifiche);

		// Controllo se c'è FUNGIBILITA'
		String lFlagFungi = new String(getRequestStringParameter("fungibilita"));
		if (lFlagFungi.equals("S")) {
			lPage = calcolaFungibilita(lEve);
		} else {
			// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
			IOrdineScarcerazione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineScarcerazione();
			EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaEventoNotifica(lEve);
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
					+ "&modalita=I";
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".InserisciOSDetenutoDomiciliare: fine");
		return lPage;

	}

	private String InserisciOSSemiliberta() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".InserisciOSSemiliberta: inizio");
		String lPage = ""; // pagina di ritorno

		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("SC");
		lEve.getEvento().setCodMotivo("0082");
		lEve = getEventoOrdineScarcerazione(lEve.getEvento());
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOS();
		lEve.setNotifiche(lNotifiche);

		// Controllo se c'è FUNGIBILITA'
		String lFlagFungi = new String(getRequestStringParameter("fungibilita"));
		if (lFlagFungi.equals("S")) {
			lPage = calcolaFungibilita(lEve);
		} else {
			// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
			IOrdineScarcerazione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineScarcerazione();
			EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaEventoNotifica(lEve);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
					+ "&modalita=I";

		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".InserisciOSSemiliberta: fine");
		return lPage;

	}

	private String InserisciOSAltrePosizioni() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".InserisciOSAltrePosizioni: inizio");
		String lPage = ""; // pagina di ritorno

		EventoNotificaModel lEve = new EventoNotificaModel();
		// COMMENTATO DA SERENA PERCHE' INSERISCE IL CODICE MOTIVO A NULL
		// lEve.getEvento().setDescrMotivo("0000");
		lEve.getEvento().setCodMotivo("0000");
		// lEve.getEvento().setFlagDocumentoRegistrato("N");
		lEve = getEventoOrdineScarcerazione(lEve.getEvento());
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOS();
		lEve.setNotifiche(lNotifiche);

		// Controllo se c'è FUNGIBILITA'
		String lFlagFungi = new String(getRequestStringParameter("fungibilita"));
		if (lFlagFungi.equals("S")) {
			lPage = calcolaFungibilita(lEve);
		} else {
			// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
			IOrdineScarcerazione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineScarcerazione();
			EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaEventoNotifica(lEve);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordinescarcerazione.action.ActDettaglioOrdineScarcerazione&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento()
					+ "&modalita=I";

		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".InserisciOSAltrePosizioni: fine");
		return lPage;

	}

	@SuppressWarnings("rawtypes")
	private String calcolaFungibilita(EventoNotificaModel lEve) throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".calcolaFungibilita: inizio");

		// Gestione Pena Residua
		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		IPenaResidua lPenCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaRes = lPenCtrl.ExRicercaPenaResiduaByKey(lIdPenaRes);

		if (!this.isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE)) {
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
		}

		if (lPenaRes == null || lPenaRes.getDataFine() == null)
			throw new F3BException("Impossibbile Calcolo Fine Pena!");

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

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsterna", "" + lOption);

		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lEve.getEvento().getFasSieIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		setRequestAttribute("fungibilita", lFunMod);
		setRequestAttribute("evento", lEve);
		setRequestAttribute("penaresidua", lPenaRes);
		String lPage = PG_INSERISCI_OS_FUNGIBILITA;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("§§§Passo l'evento e pena residua in una form di inserimento!!");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".calcolaFungibilita: fine");

		return lPage;
	}

}