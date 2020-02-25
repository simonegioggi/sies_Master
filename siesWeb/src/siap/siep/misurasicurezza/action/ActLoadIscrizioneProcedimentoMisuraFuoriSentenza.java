package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadIscrizioneProcedimentoMisuraFuoriSentenza
 * </p>
 * <p>
 * Description: Classe Action per la load Iscrizione Procedimento
 * </p>
 * <p>
 * di Misura Sicurezza Disposta Fuori Sentenza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: Intersistemi Italia s.p.a.
 * </p>
 * 
 * @version 8.2
 */
public class ActLoadIscrizioneProcedimentoMisuraFuoriSentenza extends ActionSiap
		implements ICostantiSoggetto, ICostantiDepositoOrdinanzaPc {


	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {


		removeSessionAttribute("fascicolo");
		removeSessionAttribute("soggetto");
		removeSessionAttribute("penaresidua");

		// indica se inserisco istanza o sentenza+istanza o soggetto+sentenza+istanza
		String lTipoInserimento = "nuovo";
		BigDecimal idEve = null;
		BigDecimal idFascSius = null;

		SoggettoModel lSog = null;
		DepositoOrdinanzaPcModel lDepoMod = null;
		EventoModel lEveMod = null;
		MisuraSicurezzaModel lMisMod = null;

		// precarica soggetto e precarica provvedimento
		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ID_SOGGETTO)
				&& !isRequestParameterNullObj(ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC)) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug(" -------------PRECARICO------------");
			setRequestAttribute("idsoggetto",
					"" + getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO));
			setRequestAttribute("idordinanza", "" + getRequestBigDecimalParameter(
					ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC));
			lTipoInserimento = "soggetto";

			ISoggetto lCtrl = SICOLookupRemote.getSoggettoRemote();

			lSog = (SoggettoModel) lCtrl.ExRicercaSoggettoByKey(
					getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO));
			if (lSog != null && lSog.getIdSoggetto() != null) {
			} else {
				throw new SIEPException(SIEPException.USER_MESSAGE, "Soggetto non individuato");
			}

			IDepositoOrdinanzaPc lCtrlOrd = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			lDepoMod = (DepositoOrdinanzaPcModel) lCtrlOrd
					.ExRicercaDepositoOrdinanzaPcByKey(getRequestBigDecimalParameter(
							ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC));
			if (lDepoMod != null && lDepoMod.getIdDepositoOrdinanzaPc() != null) {
				idEve = lDepoMod.getIdEventoGenerato();
			} else {
				throw new SIEPException(SIEPException.USER_MESSAGE, "Provvedimento SIUS non individuato");
			}

			if (idEve != null) {
				IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
				lEveMod = (EventoModel) lCtrlEve.ExRicercaEventoByKey(idEve);
				if (lEveMod != null && lEveMod.getIdEvento() != null) {
					idFascSius = lEveMod.getFasSiuIdFascicoloSius();
				} else {
					throw new SIEPException(SIEPException.USER_MESSAGE, "Evento non individuato");
				}
			}

			IMisuraSicurezza lCtrlMis = SIEPLookupRemote.getMisuraSicurezzaRemote();
			List listaMis = lCtrlMis.ExRicercaMisuraSicurezzaByIdFascicoloSIUS(idFascSius);
			if (listaMis.size() > 0) {
				lMisMod = (MisuraSicurezzaModel) listaMis.get(listaMis.size() - 1);
			} 
			else {
				// provo a cercarle sul fascicolo PADRE
				FascicoloGPModel lFasGPMod = new FascicoloGPModel();
				lFasGPMod.getFascicoloSiusModel().setIdFascicoloSius(idFascSius);
				IFascicoloSius lCtrlS = SIUSLookupRemote.getFascicoloSiusRemote();
				lFasGPMod = lCtrlS.ExRicercaFascicoloByKey(idFascSius);
				// Fascicolo Padre
				FascicoloGPModel lFasPadre = null;
				if (lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null) {
					// 29/01/2008 Per fascicoli ExtraUfficio Il Fascicolo Padre può anche non esistere in archivio.
					if (ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO.compareTo(lFasGPMod.getFascicoloSiusModel().getChiaveUfficio()) == 0)
						lFasPadre = lCtrlS.ExRicercaFascicoloByKey(lFasGPMod.getFascicoloSiusModel()
								.getIdFascicoloSiusOrigine());
					else
						lFasPadre = lCtrlS.ExRicercaFascicoloCollegato(lFasGPMod.getFascicoloSiusModel()
								.getIdFascicoloSiusOrigine());
				
					listaMis = lCtrlMis.ExRicercaMisuraSicurezzaByIdFascicoloSIUS(lFasPadre.getFascicoloSiusModel().getIdFascicoloSius());
				}
				
				if (listaMis.size() > 0) {
					lMisMod = (MisuraSicurezzaModel) listaMis.get(listaMis.size() - 1);
				}
				else
				throw new SIEPException(SIEPException.USER_MESSAGE, "Misura Sicurezza non trovata");
			}

		} else { // NON PRECARICA NULLA ->
			lTipoInserimento = "nuovo";
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" --XX-- ActLoadIscrizione - NumerazioneManuale =
		// "+getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS));
		setRequestAttribute("NumerazioneManualeMisureProvvFS", getRequestStringParameter(
				ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS));

		setRequestAttribute("tipoinserimento", lTipoInserimento);
		setSessionAttribute("soggetto", lSog);
		setRequestAttribute("soggetto", lSog);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ------------SOGGETTO = "+lSog);
		setSessionAttribute("provvedimento", lDepoMod);
		setRequestAttribute("provvedimento", lDepoMod);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ------------PROVVEDIMENTO = "+lDepoMod);
		setSessionAttribute("evento", lEveMod);
		setRequestAttribute("evento", lEveMod);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ------------ EVENTO = "+lEveMod);
		setSessionAttribute("misura", lMisMod);
		setRequestAttribute("misura", lMisMod);
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ------------ MISURA SIC = "+lMisMod);

		// Riempie la combo delle nazioni
		Option lOption = new Option(
				DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"),
				"039");
		if (lSog != null && lSog.getIdSoggetto() != null && lSog.getCodStatoNascita() != null) {
			lOption.setSelected(lSog.getCodStatoNascita());
		}
		setRequestAttribute("nazioni", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getSesso(), "M");
		if (lSog != null && lSog.getIdSoggetto() != null && lSog.getSesso() != null) {
			lOption.setSelected(lSog.getSesso());
		}
		setRequestAttribute("sesso", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getStatoCittadinanza(), "-");
		setRequestAttribute("StatoCittadinanza", "" + lOption);

		// Flag Data Nascita Presunta
		lOption = new Option(DecodificheManager.getInstance().getFlagSNTrattino(), "N");
		setRequestAttribute("dataNascitaPresunta", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoRitoSentenza(), "-");
		setRequestAttribute("tipoRito1", "" + lOption);

		// Tipo Provvedimento Sorv : Decreto / Ordinanza
		Option lOptionProvv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza(), "-");
		if (lEveMod != null && lEveMod.getIdEvento() != null && lEveMod.getCodTipoProvvedimento() != null) {
			lOptionProvv.setSelected(lEveMod.getCodTipoProvvedimento());
		}

		setRequestAttribute("tipoProvvedimenti", "" + lOptionProvv);

		// Autorita Emittente: Uffici della Sorveglianza
		Option lAutoOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), "-");
		// MEV_66: aggiunto ufficio TDSM
		lAutoOption.setFilter(new String[] { "TDS", "TDSM", "UDS", "UDSM", "-" });

		if (lEveMod != null && lEveMod.getIdEvento() != null && lEveMod.getCodUfficioEmittente() != null) {
			UfficioModel lUfficioEmittente = getUfficioByCodUfficio(lEveMod.getCodUfficioEmittente());
			lAutoOption.setSelected(lUfficioEmittente.getCodTipoUfficio());
		}

		setRequestAttribute("autoritaEmi", "" + lAutoOption);

		// ComboBOX X Natura Misura Sicurezza
		Option lOptionN = new Option(DecodificheManager.getInstance().getNaturaMisuraSicurezza());
		if (lMisMod != null && lMisMod.getIdMisuraSicurezza() != null && lMisMod.getCodNatura() != null) {
			lOptionN.setSelected(lMisMod.getCodNatura());
		}
		setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);

		// ComboBOX X Tipo Misura Sicurezza
		Vector lVec = (Vector) DecodificheManager.getInstance().getTipoMisuraSicurezza();
		setRequestAttribute("VectipoMisuraSicurezza", lVec);

		// Imposta la Modalità a Inserimento.
		setRequestAttribute("modalita", "I");

			// ========================================================================
			// New d.f. 14/04/2015
			// Devo controllare se per l'anno corrente sono stati iscritti procedimenti
			// di classe IV. In caso negativo devo obbligare l'utente a indicare
			// manualmente il numero di procedimento che rappresenterà l'inizio della
			// numerazione automatica per i fascicoli telematici dell'anno corrente.
			// Infatti sono stati già iscritti sicuramente fascicoli cartacei che
			// andranno eventualmente caricati manualmente. La numerazione automatica
			// vale solo per i nuovi e non puù sovrapporsi a quella cartecea già assegnata
			// dall'ufficio
			//
			// Verificare se subordinare il controllo al 2015
			// ========================================================================
			BigDecimal lAnnoCorrente = new BigDecimal(DateUtils.getSysDate("yyyy"));

			if (lAnnoCorrente.intValue() == 2015) {
				// n.b. controllo solo per il 2015, anno di avvio delle Misure Sicurezza
				IMisuraSicurezza lCtrlMS = SIEPLookupRemote.getMisuraSicurezzaRemote();
				boolean lEsisteFascicoloClasseIVAnnoCorrente = lCtrlMS
						.ExEsistonoFascicoliClasseIVAnno(getUfficioUtenteConnesso(), lAnnoCorrente);

				// Se non esiste devo forzare la numerazione manuale
				if (!lEsisteFascicoloClasseIVAnnoCorrente) {
					setRequestAttribute("NumerazioneManualeMisureProvvFS", "S");
					setRequestAttribute("EsisteFascicoloClasseIVAnnoCorrente", "N");
					setRequestAttribute("AnnoCorrente", DateUtils.getSysDate("yyyy"));
			}
		}

		// Restituisce la pagina di Inserimento dei Dati
		return ICostantiMisuraSicurezza.PG_LOAD_ISCR_PROC_MIS_SIC_FUORI_SENTENZA;
	}

}