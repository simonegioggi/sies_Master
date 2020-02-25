package siap.bdmc.sbpren.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.bdmc.sbperipren.model.SbPeriprenModel;
import siap.bdmc.sbpren.model.ProvvedimentoModelBDMC;
import siap.bdmc.sbviewprocpena.action.ICostantiSbViewProcpena;
import siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/*********************************************************
 * <p>
 * Title: ActDettaglioProvvedimento
 * </p>
 * <p>
 * Description: Dettaglio Provvedimento proveniente da BDMC
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 ********************************************************/
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActConfermaImportaDatiBDMC extends ActionSiap implements ICostantiSbPren {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {
		String lReturnPage = "";
		try {
			ProvvedimentoModelBDMC lProvvedimento = getProvvedimentoBDMCInSession();

			// Testo se è stato selezionata la modalità si integrazione di uun fascicolo già presente
			/*
			 * if (!isRequestParameterNullObj("importa") &&
			 * getRequestStringParameter("importa").equals("VECCHIO") &&
			 * !isSessionAttributeNullObj("fascicolo") && getSessionAttribute("fascicolo") != null) {
			 * FascicoloSiepModel lFasc = (FascicoloSiepModel) getSessionAttribute("fascicolo"); if
			 * (lFasc.getFlagValidato().equalsIgnoreCase("S")) throw new
			 * SIEPException(SIEPException.USER_MESSAGE, "Il Procedimento N." + lFasc.getChiaveAnno() + "/" +
			 * lFasc.getChiaveProgr() + " è stato Validato. Impossibile eseguire l'integrazione.");
			 * 
			 * lProvvedimento.setEstendiFascicoloSiep(true); lReturnPage =
			 * ActIntegrazioneDatiBDMC(lProvvedimento); } else {
			 */
			// Check dell'esistenza di un soggetto omonimo
			if (!getRequestStringParameter(CAMPO_SOGGETTO_OMONIMO).equals("NUOVO")) {
				// E' stato scelto un soggetto omonimo presente in SIEP
				// Piuttosto che un nuovo inserimento da BDMC
				BigDecimal lKeySoggettoOmonimo = getRequestBigDecimalParameter(CAMPO_SOGGETTO_OMONIMO);
				lProvvedimento.setIdSoggettoOmonimo(lKeySoggettoOmonimo);
				// Riempio il soggetto Omonimo
				ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
				SoggettoModel lSogRetMod = new SoggettoModel();
				lSogRetMod = lSogCtrl.ExRicercaSoggettoByKey(lKeySoggettoOmonimo);

				lProvvedimento.setSoggetto(lSogRetMod);
			}

			// ??
			SbViewProcpenaModel lProcPenaMod = new SbViewProcpenaModel();
			if (lProvvedimento.getSbViewProcpena() != null) {
				lProcPenaMod = (SbViewProcpenaModel) lProvvedimento.getSbViewProcpena().get(0);
				if (lProcPenaMod.getDataPassGiud() == null)
					((SbViewProcpenaModel) lProvvedimento.getSbViewProcpena().get(0))
							.setDataPassGiud(getRequestDateParameter(CAMPO_ANNO_DATA_IRR,
									CAMPO_MESE_DATA_IRR, CAMPO_GIORNO_DATA_IRR));
			}

			lProvvedimento.setDataArrivoAtto(getRequestDateParameter(CAMPO_ANNO_DATA_ARRIVO_ATTO,
					CAMPO_MESE_DATA_ARRIVO_ATTO, CAMPO_GIORNO_DATA_ARRIVO_ATTO));

			// Setto i parametri da importare in SIEP
			if (!isRequestParameterNullObj(ICostantiSbViewProcpena.CAMPO_NUME_SENT_1GRA)
					&& lProcPenaMod.getNumeSent1gra() == null) {
				lProcPenaMod.setNumeSent1gra(this
						.getRequestBigDecimalParameter(ICostantiSbViewProcpena.CAMPO_NUME_SENT_1GRA));

			}
			if (!isRequestParameterNullObj(ICostantiSbViewProcpena.CAMPO_ANNO_SENT_1GRA)
					&& lProcPenaMod.getAnnoSent1gra() == null) {
				lProcPenaMod.setAnnoSent1gra(this
						.getRequestBigDecimalParameter(ICostantiSbViewProcpena.CAMPO_ANNO_SENT_1GRA));

			}

			if (lProcPenaMod.getDataSent1gra() == null && lProcPenaMod.getDataSentGippGupp() == null)
				lProcPenaMod.setDataSent1gra(getRequestDateParameter(
						ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENTENZA,
						ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENTENZA,
						ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENTENZA));
			try {
				if (!isRequestParameterNullObj(ICostantiSbPren.CAMPO_AUTORITA)
						&& (lProcPenaMod.getCodiUffiDibb() == null || lProcPenaMod.getCodiUffiDibb().length() == 0)) {
					lProcPenaMod.setCodiUffiDibb(getCodUfficioByCodTipoUfficioDescrComune(
							getRequestStringParameter(ICostantiSbPren.CAMPO_AUTORITA),
							getRequestStringParameter(ICostantiSbPren.CAMPO_LUOGO_AUTORITA)));
					lProcPenaMod
							.setDescriComuUffiDibb(getRequestStringParameter(ICostantiSbPren.CAMPO_LUOGO_AUTORITA));
					lProcPenaMod.setDescriUffiDibb(getRequestStringParameter(ICostantiSbPren.CAMPO_AUTORITA));
				}
			} catch (Exception ex) {
				setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Ufficio inesistente");
				return ISIAPCostantiWeb.PG_MESSAGE;

			}
			Vector ricaricaSbviewProcPena = new Vector();
			ricaricaSbviewProcPena.add(lProcPenaMod);
			lProvvedimento.setSbViewProcpena(ricaricaSbviewProcPena);
			lProvvedimento = setCheckField(lProvvedimento);

			// Fatto il check sugli attributi lo riporto in sessione
			setSessionAttribute("provvedimentoBDMC", lProvvedimento);
			setRequestAttribute("provvedimento", lProvvedimento);
			setSessionAttribute("esitoSoggetto", "0");

			lReturnPage = PG_INSERISCIFASCICOLO;
			// }
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Errore = " + ex);
			ex.printStackTrace();
		}
		return lReturnPage;
	}

	/*********************************************************
	 * Metodo che integra il fascicolo Siep con i dati di BDMC
	 * 
	 * @param lProvvedimento
	 * @return percorso pagina JSP
	 ********************************************************/
	/*
	 * private String ActIntegrazioneDatiBDMC(ProvvedimentoModelBDMC lProvvedimento) throws F3BException {
	 * 
	 * setCheckField(lProvvedimento);
	 * 
	 * //recupero l'intero fascicoo SIEP dalla sessione
	 * 
	 * FascicoloSiepModel lFasc = (FascicoloSiepModel) getSessionAttribute("fascicolo"); SoggettoModel lSogg =
	 * (SoggettoModel) getSessionAttribute("soggetto"); SentenzaModel lSent = (SentenzaModel)
	 * getSessionAttribute("sentenza");
	 * 
	 * lProvvedimento.setFascicoloSiep(lFasc); lProvvedimento.setSentenza(lSent);
	 * lProvvedimento.setSoggetto(lSogg);
	 * 
	 * //Ricerca la sentenza in BDMC //IBDMCSentenza lCtrl = BDMCSiesLookupRemote.getBDMCSentenzaRemote();
	 * //ProvvedimentoModelBDMC lProv = lCtrl.ExIntegraFascicoloSiep(lProvvedimento); //Setto i parametri da
	 * importare in SIEP setCheckField(lProvvedimento);
	 * 
	 * //Fatto il check sugli attributi lo riporto in sessione setSessionAttribute("provvedimentoBDMC",
	 * lProvvedimento); setRequestAttribute("provvedimento", lProvvedimento);
	 * 
	 * return PG_RIASSUNTO_INTEGRAZIONE; }
	 */

	/*********************************************************
	 * Metodo che setta le entità selezionate da Importare
	 * 
	 * @param lProvvedimento
	 ********************************************************/
	private ProvvedimentoModelBDMC setCheckField(ProvvedimentoModelBDMC lProvvedimento) throws F3BException {
		Vector periodiDaImportare = new Vector();
		Vector elencoPeriodi = lProvvedimento.getSbPeriPren();
		if (!isRequestParameterNullObj(CAMPO_CHECK_PERIODI)) {
			if (elencoPeriodi != null && elencoPeriodi.size() == 1) {
				lProvvedimento.setSbPeriPren(null);
				int i = 0;
				SbPeriprenModel lPeriodo = (SbPeriprenModel) elencoPeriodi.elementAt(i);
				lPeriodo.setDataDaPeri(getRequestDateParameter(CAMPO_ANNO_DALLA_DATA + i,
						CAMPO_MESE_DALLA_DATA + i, CAMPO_GIORNO_DALLA_DATA + i));
				lPeriodo.setDataAPeri(getRequestDateParameter(CAMPO_ANNO_ALLA_DATA + i,
						CAMPO_MESE_ALLA_DATA + i, CAMPO_GIORNO_ALLA_DATA + i));
				periodiDaImportare.add(lPeriodo);
				lProvvedimento.setSbPeriPren(periodiDaImportare);
			} else {
				String[] idPeriodo = getRequestStringParameters(CAMPO_CHECK_PERIODI);
				lProvvedimento.setSbPeriPren(null);
				for (int i = 0; i < idPeriodo.length; i++) {
					SbPeriprenModel lPeriodo = (SbPeriprenModel) elencoPeriodi.elementAt(Integer
							.parseInt(idPeriodo[i]));
					lPeriodo.setDataDaPeri(getRequestDateParameter(
							CAMPO_ANNO_DALLA_DATA + Integer.parseInt(idPeriodo[i]),
							CAMPO_MESE_DALLA_DATA + Integer.parseInt(idPeriodo[i]),
							CAMPO_GIORNO_DALLA_DATA + Integer.parseInt(idPeriodo[i])));
					lPeriodo.setDataAPeri(getRequestDateParameter(
							CAMPO_ANNO_ALLA_DATA + Integer.parseInt(idPeriodo[i]),
							CAMPO_MESE_ALLA_DATA + Integer.parseInt(idPeriodo[i]),
							CAMPO_GIORNO_ALLA_DATA + Integer.parseInt(idPeriodo[i])));
					periodiDaImportare.add(lPeriodo);
				}
				for (int i = 0; i < elencoPeriodi.size(); i++) {
					SbPeriprenModel lPeriodo = (SbPeriprenModel) elencoPeriodi.elementAt(i);
					if (lPeriodo.getCodStatPrenPeri().compareTo("3") == 0) {
						lPeriodo.setDataDaPeri(lPeriodo.getDataInizPeri());
						lPeriodo.setDataAPeri(lPeriodo.getDataFinePeri());
						periodiDaImportare.add(lPeriodo);
					}
				}
				lProvvedimento.setSbPeriPren(periodiDaImportare);
			}
		} else {
			for (int i = 0; i < elencoPeriodi.size(); i++) {
				SbPeriprenModel lPeriodo = (SbPeriprenModel) elencoPeriodi.elementAt(i);
				if (lPeriodo.getCodStatPrenPeri().compareTo("3") == 0) {
					lPeriodo.setDataDaPeri(lPeriodo.getDataInizPeri());
					lPeriodo.setDataAPeri(lPeriodo.getDataFinePeri());
					periodiDaImportare.add(lPeriodo);
				}
			}
			lProvvedimento.setSbPeriPren(periodiDaImportare);
		}
		if (!isRequestParameterNullObj(CAMPO_CHECK_SENTENZA)) {
			if (getRequestStringParameter(CAMPO_CHECK_SENTENZA) != null)
				lProvvedimento.setIsSbProcPenaCheck(true);
		}

		if (!isRequestParameterNullObj(CAMPO_CHECK_REATO)) {
			if (getRequestStringParameter(CAMPO_CHECK_REATO) != null)
				lProvvedimento.setIsSbViewCapoImpuCheck(true);
			lProvvedimento.setIsSbViewReatCheck(true);
		}

		if (!isRequestParameterNullObj(CAMPO_CHECK_CIRCOSTANZA)) {
			if (getRequestStringParameter(CAMPO_CHECK_CIRCOSTANZA) != null)
				lProvvedimento.setIsSbCircostanzeCheck(true);
		}
		return lProvvedimento;
	}

	/**
	 * Restuituisce il ProvvedimentoBDMC in sessione
	 * 
	 * @return il provvedimento BDMC in sessione
	 * @throws F3BException
	 *             - se il Provvedimento non è in sessione
	 */
	protected ProvvedimentoModelBDMC getProvvedimentoBDMCInSession() throws F3BException {
		ProvvedimentoModelBDMC lProvv = (ProvvedimentoModelBDMC) getSession().getAttribute(
				"provvedimentoBDMC");
		if (lProvv == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Impossibile eseguire l'operazione richiesta. "
					+ "Non è stato selezionato nessun Provvedimento BDMC");

		return lProvv;
	}

	/**
	 * Verifica che il provvedimento BDMC sia in sessione
	 * 
	 * @throws F3BException
	 *             - se il Provvedimento non è in sessione
	 */
	protected void isProvvedimentoBDMCInSession() throws F3BException {
		ProvvedimentoModelBDMC lProvv = (ProvvedimentoModelBDMC) getSession().getAttribute(
				"provvedimentoBDMC");
		if (lProvv == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Impossibile eseguire l'operazione richiesta. "
					+ "Non è stato selezionato nessun Provvedimento BDMC");
	}

}