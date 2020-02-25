package siap.sius.misurasicurezza.action;
/**
* <p>Title: ActLoadValidaInizioMisuraSicurezzaUDS</p>
* <p>Description: Classe Action per la load dettaglio di PeriodoAltraMisura</p>
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
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadValidaInizioMisuraSicurezzaUDS extends ActMisuraSicurezza
		implements ICostantiSiusMisuraSicurezza {
	/*****************************************************************************
	 * Azione di caricamento della pagina di Dettaglio dei dati.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Recupera la key del record da Visualizzare
		BigDecimal lIdPeriodoAltraMisura = getRequestBigDecimalParameter(
				ICostantiSiusMisuraSicurezza.CAMPO_ID_PERIODO_ALTRA_MISURA);

		// Recupera i dati del record da modificare
		IPeriodoAltraMisura lCtrl = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
		PeriodoAltraMisuraModel lPerMod = lCtrl.ExRicercaPeriodoAltraMisuraById(lIdPeriodoAltraMisura);

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
		setRequestAttribute("PeriodoAltraMisura", lPerMod);

		// ricerca record esecuzione_misura_sicurezza
		// ricerca del fascicolo
		BigDecimal lIdFasSius = recuperoIdFascicolo();

		// ricerca record esecuzione_misura_sicurezza
		IEsecuzioneMS lEMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
		EsecuzioneMisuraSicurezzaModel lEMSModel = lEMSCtrl
				.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(lIdFasSius);
		if (lEMSModel == null || lEMSModel.getIdEsecuzioneMisuraSicurezza().equals(null))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! ESECUZIONE MISURA SICUREZZA Assente!");

		// 01 Flag Motivo Inizio
		// 02 Flag Motivo Ripresa
		// 03 Flag Motivo Sospensione
		if (lPerMod.getFlagMotivo().equals("01")) {
			lEMSModel = termineFinale(lPerMod, lEMSModel);
		} else {
			// ricerca delle precedenti periodi di esecuzione misura sicurezza
			// per prendere il penultimo periodo ossia la sospensione
			// se la sospensione non è ricominciata subito ma sono passati dei giorni questi sono da
			// recuperare per cui li vado a sommare alla data termine attuale
			IPeriodoAltraMisura lCtrl1 = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
			List lListaMisureSius = lCtrl1.ExRicercaMisuraSicurezzaByIdFascicolo(lIdFasSius);
			PeriodoAltraMisuraModel lPerModPenultimo = new PeriodoAltraMisuraModel();
			lPerModPenultimo = (PeriodoAltraMisuraModel) lListaMisureSius.get(lListaMisureSius.size() - 2);
			lEMSModel.setDataTermineAttuale(DateUtils.moveDateTo(lEMSModel.getDataTermineAttuale(),
					Calendar.DAY_OF_MONTH, DateUtils.getIntervallo(lPerModPenultimo.getDataScadenza(),
							lPerMod.getDataInizioEsecuzione())));
		}
		setRequestAttribute("lEMSModel", lEMSModel);
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
		return PG_LOAD_VALIDAINIZIOMISURASICUREZZA;
	}

}