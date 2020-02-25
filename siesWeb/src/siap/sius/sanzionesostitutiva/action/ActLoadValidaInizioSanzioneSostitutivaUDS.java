package siap.sius.sanzionesostitutiva.action;
/**
* <p>Title: ActLoadValidaInizioSanzioneSostitutivaUDS</p>
* <p>Description: Classe Action per la load dettaglio di PeriodoAltraSanzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadValidaInizioSanzioneSostitutivaUDS extends ActSanzioneSostitutiva
		implements ICostantiSanzioneSostitutiva {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Dettaglio dei dati.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Recupera la key del record da Visualizzare
		BigDecimal lIdPeriodoAltraSanzione = getRequestBigDecimalParameter(
				ICostantiSanzioneSostitutiva.CAMPO_ID_PERIODO_ALTRA_SANZIONE);

		// Recupera i dati del record da modificare
		IPeriodoAltraSanzione lCtrl = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
		PeriodoAltraSanzioneModel lPerMod = lCtrl.ExRicercaPeriodoAltraSanzioneById(lIdPeriodoAltraSanzione);

		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (lPerMod == null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
			// Specificare eventualmente la jump page dove verrà ridirezionata la
			// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
			// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
			// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
			// della root_dir es /siap/frame.htm
			setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR);
			return IWebConstants.PG_MESSAGE;
		}

		// ====================================================
		// Passa il Model alla componente di visualizzazione
		// ====================================================
		setRequestAttribute("PeriodoAltraSanzione", lPerMod);

		// ricerca record esecuzione_sanzione_sost
		// ricerca del fascicolo
		BigDecimal lIdFasSius = recuperoIdFascicolo();

		// ricerca record esecuzione_sanzione_sost
		IEsecuzioneSS lESSCtrl = SIUSLookupRemote.getEsecuzioneSSRemote();
		EsecuzioneSanzioneSostitutivaModel lESSModel = lESSCtrl
				.ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(lIdFasSius);
		if (lESSModel == null || lESSModel.getIdEsecuzioneSanzioneSost().equals(null))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! ESECUZIONE SANZIONE SOSTITUTIVA Assente!");

		// 01 Flag Motivo Inizio
		// 02 Flag Motivo Ripresa
		// 03 Flag Motivo Sospensione
		if (lPerMod.getFlagMotivo().equals("01")) {
			lESSModel = termineFinale(lPerMod, lESSModel);
		} else {
			// ricerca delle precedenti periodi di esecuzione sanzione sostitutiva
			// per prendere il penultimo periodo ossia la sospensione
			// se la sospensione non è ricominciata subito ma sono passati dei giorni questi sono da
			// recuperare per cui li vado a sommare alla data termine attuale
			IPeriodoAltraSanzione lCtrl1 = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
			List lListaSanzioniSius = lCtrl1.ExRicercaSanzioneSostitutivaByIdFascicolo(lIdFasSius);
			PeriodoAltraSanzioneModel lPerModPenultimo = new PeriodoAltraSanzioneModel();
			lPerModPenultimo = (PeriodoAltraSanzioneModel) lListaSanzioniSius
					.get(lListaSanzioniSius.size() - 2);
			lESSModel.setDataTermineAttuale(DateUtils.moveDateTo(lESSModel.getDataTermineAttuale(),
					Calendar.DAY_OF_MONTH, DateUtils.getIntervallo(lPerModPenultimo.getDataScadenza(),
							lPerMod.getDataInizioEsecuzione())));
		}
		setRequestAttribute("lESSModel", lESSModel);
		// =========================================================
		// Restituisce la pagina di Visualizzazione del Dettaglio.
		// =========================================================
		// Imposta Modalità.
		setRequestAttribute("modalita", "D");

		// model Istituto Detenzione
		IstitutoDetenzioneModel lIstMod = null;
		if (lPerMod != null && lPerMod.getIstDetIdIstitutoDetenzione() != null
				&& !lPerMod.getIstDetIdIstitutoDetenzione().equals("-")) {
			IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
			lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lPerMod.getIstDetIdIstitutoDetenzione());
		}

		setRequestAttribute("istitutodetenzione", lIstMod);
		return PG_LOAD_VALIDAINIZIOSANZIONESOSTITUTIVA;
	}

}