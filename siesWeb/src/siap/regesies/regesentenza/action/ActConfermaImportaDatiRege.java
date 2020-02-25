package siap.regesies.regesentenza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regesentenza.model.ProvvedimentoModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/*********************************************************
 * <p>
 * Title: ActDettaglioProvvedimento
 * </p>
 * <p>
 * Description: Dettaglio Provvedimento proveniente da ReGe
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 ********************************************************/
public class ActConfermaImportaDatiRege extends ActionRegeSiap implements ICostantiRegeSentenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		String lReturnPage = "";
		try {
			ProvvedimentoModel lProvvedimento = getProvvedimentoRegeInSession();

			// Testo se è stato selezionata la modalità si integrazione di uun fascicolo già presente
			if (!isRequestParameterNullObj("importa")
					&& getRequestStringParameter("importa").equals("VECCHIO")
					&& !isSessionAttributeNullObj("fascicolo") && getSessionAttribute("fascicolo") != null) {
				FascicoloSiepModel lFasc = (FascicoloSiepModel) getSessionAttribute("fascicolo");
				if (lFasc.getFlagValidato().equalsIgnoreCase("S"))
					throw new SIEPException(SIEPException.USER_MESSAGE, "Il Procedimento N."
							+ lFasc.getChiaveAnno() + "/" + lFasc.getChiaveProgr()
							+ " è stato Validato. Impossibile eseguire l'integrazione.");

				lProvvedimento.setEstendiFascicoloSiep(true);
				lReturnPage = ActIntegrazioneDatiRege(lProvvedimento);
			} else {
				// Check dell'esistenza di un soggetto omonimo
				if (!getRequestStringParameter(CAMPO_SOGGETTO_OMONIMO).equals("NUOVO")) {
					// E' stato scelto un soggetto omonimo presente in SIEP
					// Piuttosto che un nuovo inserimento da REGE
					BigDecimal lKeySoggettoOmonimo = getRequestBigDecimalParameter(CAMPO_SOGGETTO_OMONIMO);
					lProvvedimento.setIdSoggettoOmonimo(lKeySoggettoOmonimo);
					// Riempio il soggetto Omonimo
					ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
					SoggettoModel lSogRetMod = new SoggettoModel();
					lSogRetMod = lSogCtrl.ExRicercaSoggettoByKey(lKeySoggettoOmonimo);

					lProvvedimento.setSoggetto(lSogRetMod);
				}

				lProvvedimento.getRegeSentenza().setDataArrivoAtto(
						getRequestDateParameter(CAMPO_ANNO_DATA_ARRIVO_ATTO, CAMPO_MESE_DATA_ARRIVO_ATTO,
								CAMPO_GIORNO_DATA_ARRIVO_ATTO));
				lProvvedimento.getRegeSentenza().setDataIscrizione(
						getRequestDateParameter(CAMPO_ANNO_DATA_ISCRIZIONE, CAMPO_MESE_DATA_ISCRIZIONE,
								CAMPO_GIORNO_DATA_ISCRIZIONE));
				lProvvedimento.getRegeSentenza().setDataInserimento(DateUtils.getSysDate());

				// Setto i parametri da importare in SIEP
				setCheckField(lProvvedimento);

				// Fatto il check sugli attributi lo riporto in sessione
				setSessionAttribute(PROVVEDIMENTO_IN_SESSION, lProvvedimento);
				setRequestAttribute(PROVVEDIMENTO_IN_SESSION, lProvvedimento);

				lReturnPage = PG_INSERISCIFASCICOLO;
			}
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Errore = " + ex);
			ex.printStackTrace();
		}
		return lReturnPage;
	}

	/*********************************************************
	 * Metodo che integra il fascicolo Siep con i dati di Rege
	 * 
	 * @param lProvvedimento
	 * @return percorso pagina JSP
	 ********************************************************/
	private String ActIntegrazioneDatiRege(ProvvedimentoModel lProvvedimento) throws F3BException {

		setCheckField(lProvvedimento);

		// recupero l'intero fascicoo SIEP dalla sessione

		FascicoloSiepModel lFasc = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		SoggettoModel lSogg = (SoggettoModel) getSessionAttribute("soggetto");
		SentenzaModel lSent = (SentenzaModel) getSessionAttribute("sentenza");

		lProvvedimento.setFascicoloSiep(lFasc);
		lProvvedimento.setSentenza(lSent);
		lProvvedimento.setSoggetto(lSogg);

		// Ricerca la sentenza in Rege
		// IRegeSentenza lCtrl = RegeSiesLookupRemote.getRegeSentenzaRemote();
		// ProvvedimentoModel lProv = lCtrl.ExIntegraFascicoloSiep(lProvvedimento);
		// Setto i parametri da importare in SIEP
		setCheckField(lProvvedimento);

		// Fatto il check sugli attributi lo riporto in sessione
		setSessionAttribute(PROVVEDIMENTO_IN_SESSION, lProvvedimento);
		setRequestAttribute("provvedimento", lProvvedimento);

		return PG_RIASSUNTO_INTEGRAZIONE;
	}

	/*********************************************************
	 * Metodo che setta le entità selezionate da Importare
	 * 
	 * @param lProvvedimento
	 ********************************************************/
	private void setCheckField(ProvvedimentoModel lProvvedimento) throws F3BException {

		if (!isRequestParameterNullObj(CAMPO_CHECK_RESIDENZA)) {
			if (getRequestStringParameter(CAMPO_CHECK_RESIDENZA) != null)
				lProvvedimento.setIsResidenzeCheck(true);
		}

		if (!isRequestParameterNullObj(CAMPO_CHECK_REATO)) {
			if (getRequestStringParameter(CAMPO_CHECK_REATO) != null)
				lProvvedimento.setIsReatoCheck(true);
		}

		if (!isRequestParameterNullObj(CAMPO_CHECK_NOTIZIAREATO)) {
			if (getRequestStringParameter(CAMPO_CHECK_NOTIZIAREATO) != null)
				lProvvedimento.setIsNotiziaReatoCheck(true);
		}
		if (!isRequestParameterNullObj(CAMPO_CHECK_CIRCOSTANZA)) {
			if (getRequestStringParameter(CAMPO_CHECK_CIRCOSTANZA) != null)
				lProvvedimento.setIsCircostanzaCheck(true);
		}
		if (!isRequestParameterNullObj(CAMPO_CHECK_DISPOSITIVO)) {
			if (getRequestStringParameter(CAMPO_CHECK_DISPOSITIVO) != null)
				lProvvedimento.setIsDispositivoCheck(true);
		}
		if (!isRequestParameterNullObj(CAMPO_CHECK_DIFENSORI)) {
			if (getRequestStringParameter(CAMPO_CHECK_DIFENSORI) != null)
				lProvvedimento.setIsDifensoriCheck(true);
		}
	}

}