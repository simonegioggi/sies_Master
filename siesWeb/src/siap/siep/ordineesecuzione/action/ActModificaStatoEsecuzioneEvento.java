package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActModificaStatoEsecuzioneEvento
 * </p>
 * <p>
 * Description: Classe Action per la load Stato Esecuzione
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActModificaStatoEsecuzioneEvento extends ActionSiap implements ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		// array contenete tutti gli eventi
		String[] lArrayTuttiEventi = this.getRequestStringParameters(ICostantiEvento.CAMPO_ID_EVENTO);
		ArrayList lListaTuttiEventi = new ArrayList(Arrays.asList(lArrayTuttiEventi));
		// prendo tutti gli id_evento con Stampa checked
		String[] lArrayStampa = this.getRequestStringParameters(ICostantiEvento.CAMPO_FLAG_STAMPA_SIEP);
		// prendo tutti gli id_evento con Video checked
		String[] lArrayVideo = this.getRequestStringParameters(ICostantiEvento.CAMPO_FLAG_VIDEO_SIEP);

		// ricavo tre liste
		// ArrayList lStampa = new ArrayList(Arrays.asList(lArrayStampa));
		ArrayList lVideo = new ArrayList(Arrays.asList(lArrayVideo));

		ArrayList lComune = new ArrayList(Arrays.asList(lArrayStampa));
		// flagStampa ='S' e flagVideo='S'
		lComune.retainAll(lVideo);

		lComune.remove(lComune.indexOf(""));
		ArrayList lSoloStampa = new ArrayList(Arrays.asList(lArrayStampa));
		// flagStampa ='S' e flagVideo='N'
		lSoloStampa.removeAll(lComune);
		lSoloStampa.remove(lSoloStampa.indexOf(""));

		ArrayList lSoloVideo = new ArrayList(Arrays.asList(lArrayVideo));
		// flagStampa ='N' e flagVideo='S'
		lSoloVideo.removeAll(lComune);
		lSoloVideo.remove(lSoloVideo.indexOf(""));

		// elimino dalla lista di tutti gli eventi quelli in comune, quelli stampa e quelli video e ottengo
		// una lista con flagStampa ='N' e flagVideo='N'
		lListaTuttiEventi.removeAll(lComune);
		lListaTuttiEventi.removeAll(lSoloStampa);
		lListaTuttiEventi.removeAll(lSoloVideo);

		// String lCodiceOperatore = this.getCodUtenteConnesso();
		// String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		List lListaModel = new ArrayList();

		for (int i = 0; i < lComune.size(); i++) {
			EventoModel lEveModComune = new EventoModel();

			lEveModComune.setFlagStampaSiep("S");
			lEveModComune.setFlagVideoSiep("S");
			lEveModComune.setIdEvento(new BigDecimal((String) lComune.get(i)));
			// lEveModComune.setCodOperatoreAggiornamento(lCodiceOperatore);
			// lEveModComune.setCodUfficioAggiornamento(lCodiceUfficio);
			// lEveModComune.setDataAggiornamento(DateUtils.getSysDate());
			lListaModel.add(lEveModComune);
		}

		for (int x = 0; x < lSoloStampa.size(); x++) {
			EventoModel lEveModStampa = new EventoModel();

			lEveModStampa.setFlagStampaSiep("S");
			lEveModStampa.setFlagVideoSiep("N");
			lEveModStampa.setIdEvento(new BigDecimal((String) lSoloStampa.get(x)));
			// lEveModStampa.setCodOperatoreAggiornamento(lCodiceOperatore);
			// lEveModStampa.setCodUfficioAggiornamento(lCodiceUfficio);
			// lEveModStampa.setDataAggiornamento(DateUtils.getSysDate());
			lListaModel.add(lEveModStampa);
		}

		for (int y = 0; y < lSoloVideo.size(); y++) {
			EventoModel lEveModVideo = new EventoModel();

			lEveModVideo.setFlagStampaSiep("N");
			lEveModVideo.setFlagVideoSiep("S");
			lEveModVideo.setIdEvento(new BigDecimal((String) lSoloVideo.get(y)));
			// lEveModVideo.setCodOperatoreAggiornamento(lCodiceOperatore);
			// lEveModVideo.setCodUfficioAggiornamento(lCodiceUfficio);
			// lEveModVideo.setDataAggiornamento(DateUtils.getSysDate());
			lListaModel.add(lEveModVideo);
		}

		for (int z = 0; z < lListaTuttiEventi.size(); z++) {
			EventoModel lEveModTutti = new EventoModel();

			lEveModTutti.setFlagStampaSiep("N");
			lEveModTutti.setFlagVideoSiep("N");
			lEveModTutti.setIdEvento(new BigDecimal((String) lListaTuttiEventi.get(z)));
			// lEveModTutti.setCodOperatoreAggiornamento(lCodiceOperatore);
			// lEveModTutti.setCodUfficioAggiornamento(lCodiceUfficio);
			// lEveModTutti.setDataAggiornamento(DateUtils.getSysDate());
			lListaModel.add(lEveModTutti);
		}

		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();

		/* EventoModel lEveModel = */lCtrl.ExAggiornaEventoStatoEsecuzione(lListaModel);

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActStatoEsecuzione&ModificaEseguita=SI";
	}

}