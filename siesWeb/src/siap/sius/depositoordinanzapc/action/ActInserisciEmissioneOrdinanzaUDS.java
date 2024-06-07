package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.misurasicurezza.controller.MisuraSicurezzaController;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ActInserisciEmissioneDecreto;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel;
import siap.sius.depositoordinanzapc.util.RicercaProvvedimentiCollegati;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EMSFascGPModel;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.tenore.controller.ITenore;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadEmissioneOrdinanzaUDS
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Emissione Ordinanza UDS
 * </p>
 * Poichè l'azione deve implementare la stessa funzione implementata da ActLoadEmissioneDecreto, viene estesa
 * questa in modo di utilizzare il suo processRequest(). Si sfrutta l'override della funzione
 * generaListaTipi() per differenziare la jsp.
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
public class ActInserisciEmissioneOrdinanzaUDS extends ActInserisciEmissioneDecreto
		implements ICostantiDepositoOrdinanzaPc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*
	 * Metodo sovrascritto della classe ActInserisciEmissioneDecreto. La funzione verifica l'esistenza di una
	 * ordinanza con la stessa data di emissione.
	 *
	 * Non più usato !! public boolean verificaEsistenzaDoc( BigDecimal aIdGenProc, Date aDataEmissione )
	 * throws Exception { boolean retValue = false; IDepositoOrdinanzaPc lDepOrdCtrl =
	 * SIUSLookupRemote.getDepositoOrdinanzaPcRemote(); if
	 * (lDepOrdCtrl.ExVerificaEsistenzaDepositoOrdinanzaByIdGenProc(aIdGenProc, aDataEmissione)) {
	 * passaggioParametri(); // Viene richiamata la pagina di Warning mRetPage = super.PG_WARNING;
	 * setRequestAttribute(IWebConstants.MESSAGE_TEXT,
	 * "Operazione non consentita. Per il procedimento indicato è già stata emessa una ordinanza nella stessa data."
	 * ); retValue = true; } return retValue; }
	 */
	/**
	 * Metodo sovrascritto della classe ActInserisciEmissioneDecreto. La funzione in base al codice tipo
	 * ordinanza richiesto seleziona la jsp di input per l'ordinanza specifica.
	 */
	public String selezione() throws Exception {

		// Lettura contenuto
		String lCodContenuto = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("cod Tipo lCodContenuto = " + lCodContenuto);
		// lettura tipo di ordinanza Sempre automatica 13-9-04
		String lCodTipoDec = null;

		// UtenteModel lUtenteModel = this.getUtenteConnesso();

		// Generazione automatica in base al contenuto
		Collection lOggetti = DecodificheManager.getInstance().getOggettoProcedimento();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("N° Oggetti = " + lOggetti.size());
		lCodTipoDec = DecodificheUtils.getCodAltebyCode(lOggetti, lCodContenuto);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("cod Tipo Ordinanza = " + lCodTipoDec);

		// per l'ordinanza di conversione serve trovare il fascicolo origine
		// anche detto precedimento collegato
		if (getSessionAttribute("fascicoloSiusGP") != null) {
			FascicoloGPModel lFasGPMod = new FascicoloGPModel(
					(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
			BigDecimal lIdFascicoloSiusOrigine = lFasGPMod.getFascicoloSiusModel()
					.getIdFascicoloSiusOrigine();

			if (lIdFascicoloSiusOrigine != null) {
				IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
				FascicoloGPModel lFasGPModOrigine = lCtrl.ExRicercaFascicoloByKey(lIdFascicoloSiusOrigine);
				FascicoloSiusModel lFasSiusOrigine = lFasGPModOrigine.getFascicoloSiusModel();
				setRequestAttribute("lFasSiusOrigine", lFasSiusOrigine);

				// prendo l'ultimo periodo altra sanzione per visualizzare la pena residua ed espiata
				IPeriodoAltraSanzione lCtrllst = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
				List lListaSanzioniSius = lCtrllst
						.ExRicercaSanzioneSostitutivaByIdFascicolo(lIdFascicoloSiusOrigine);
				if (lListaSanzioniSius != null && lListaSanzioniSius.size() > 0) {
					PeriodoAltraSanzioneModel lPerMod = (PeriodoAltraSanzioneModel) lListaSanzioniSius
							.get(lListaSanzioniSius.size() - 1);
					setRequestAttribute("lPerMod", lPerMod);
				}

				// 04/06/2008 Puntamento al fascicolo Padre di E.S.S. per recupero dei quantum.
				else if ((lFasGPModOrigine.getGeneraleProcedimentoModel().getCodTipoRegistro()
						.compareTo("S12") == 0)
						&& (lFasGPModOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
								.compareTo("U019") != 0)) {
					FascicoloGPModel lFasGPModPadreESS = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(
							lFasGPModOrigine.getGeneraleProcedimentoModel().getAnnoS1(),
							lFasGPModOrigine.getGeneraleProcedimentoModel().getProgrS1(),
							lFasGPModOrigine.getGeneraleProcedimentoModel().getCodUfficioInserimento());
					// prendo l'ultimo periodo altra sanzione per visualizzare la pena residua ed espiata
					lListaSanzioniSius = lCtrllst.ExRicercaSanzioneSostitutivaByIdFascicolo(
							lFasGPModPadreESS.getFascicoloSiusModel().getIdFascicoloSius());
					if (lListaSanzioniSius != null && lListaSanzioniSius.size() > 0) {
						PeriodoAltraSanzioneModel lPerMod = (PeriodoAltraSanzioneModel) lListaSanzioniSius
								.get(lListaSanzioniSius.size() - 1);
						setRequestAttribute("lPerMod", lPerMod);
					}
				}
			}
		}
		if (lCodTipoDec == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Tipo ordinanza automatico non definito");

		if (lCodTipoDec.compareTo(LIBERAZIONE_ANTICIPATA) == 0) {
			// Ordinanza di Liberazione Anticipata
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_LIBERAZIONE_ANTICIPATA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza di Liberazione Anticipata " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(ICostantiDepositoOrdinanzaPc.LICENZA) == 0) {
			// Ordinanza di Licenza
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_LICENZA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza di Licenza " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(MISURA_ALTERNATIVA) == 0) {
			// Ordinanza di Misurs Alternativa
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_MA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Misurs Alternativa " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(INDULTINO) == 0) {
			// Ordinanza di Indultino
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_INDULTINO;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Indultino " + lCodTipoDec);
			// Si ricava la data di fine pena
			ricavaDataFinepena();
		} else if (lCodTipoDec.compareTo(ESEC_PRESSO_DOMICILIO) == 0) {
			// Ordinanza di Esecuzione presso Domicilio
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_ESEC_PRESSO_DOMICILIO;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Esecuzione presso Domicilio " + lCodTipoDec);
			// Si ricava la data di fine pena
			ricavaDataFinepena();
		} else if (lCodTipoDec.compareTo(GENERICA) == 0) {
			// Ordinanza Generica
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_GENERICA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Generica " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(REMISSIONE_DEBITO) == 0) {
			// Ordinanza Remissione Debito
			mRetPage = PG_LOAD_INSERISCI_REMISSIONE_DEBITO;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Remissione Debito " + lCodTipoDec);
			// Attivazione punto di Ritorno
			setLinkRitorno();
		} else if (lCodTipoDec.compareTo(MISURA_SICUREZZA) == 0) {
			// Lettura elenco Misure di Sicurezza Collegate al Fascicolo SIUS
			FascicoloGPModel lFasGPMod = new FascicoloGPModel(
					(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
			MisuraSicurezzaModel aMisuraSicurezza = new MisuraSicurezzaModel();
			aMisuraSicurezza.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			MisuraSicurezzaController lCtrl = new MisuraSicurezzaController();
			Vector lVect = lCtrl.ExRicercaMisuraSicurezza(aMisuraSicurezza);
			// Vector lVect = lCtrl.ExRicercaMisuraSicurezzaEstesa(aMisuraSicurezza); // Nuova funzione
			// ricerca per dati fascicolo SIEP

			setRequestAttribute("misuresicurezza", lVect);

			// Occorre passare alla jsp di inserimento anche le option per l'eventuale scelta di una nuova
			// misura
			Option lOptionN = new Option(DecodificheManager.getInstance().getNaturaMisuraSicurezza());
			Option lOptionT = new Option(DecodificheManager.getInstance().getTipoMisuraSicurezza());
			setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);
			setRequestAttribute("tipoMisuraSicurezza", "" + lOptionT);

			// Ordinanza Misura Sicurezza
			mRetPage = PG_LOAD_INSERISCI_MISURA_SICUREZZA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Misura Sicurezza " + lCodTipoDec);
			// Attivazione punto di Ritorno
			// setLinkRitorno();
		} else if (lCodTipoDec.compareTo(ORD_INOSSERVANZA_OBBLIGHI_MS) == 0) {
			// Lettura Misura di Sicurezza in Esecuzione Collegata al Fascicolo SIUS EMS padre
			FascicoloGPModel lFasGPMod = new FascicoloGPModel(
					(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
			IEsecuzioneMS lEseMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
			EsecuzioneMisuraSicurezzaModel aEMS = new EsecuzioneMisuraSicurezzaModel();
			EMSFascGPModel lEmsFasModel = new EMSFascGPModel();
			String annoEMS = lFasGPMod.getGeneraleProcedimentoModel().getAnnoS1().toString();
			String progEMS = lFasGPMod.getGeneraleProcedimentoModel().getProgrS1().toString();
			String uffiEMS = lFasGPMod.getFascicoloSiusModel().getChiaveUfficio();

			Vector lVect = lEseMSCtrl.ExRicercaEsecuzioneMisureSicurezza(annoEMS, progEMS, null, null, null,
					null, uffiEMS, 1);

			if (lVect.size() == 1)
				lEmsFasModel = ((EMSFascGPModel) lVect.get(0));
			else
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Impossibile leggere la Misura Sicurezza in Esecuzione !");

			aEMS = lEmsFasModel.getEsecuzioneMSModel();

			if (aEMS == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Impossibile leggere la Misura Sicurezza in Esecuzione !");

			aEMS.setDescrTipoMisura(lEmsFasModel.getGeneraleProcedimentoModel().getDescrDefinizione());
			setRequestAttribute("esecuzionemisurasicurezza", aEMS);

			// Occorre passare alla jsp di inserimento anche le option per l'eventuale scelta di una nuova
			// misura
			// Option lOptionN = new Option( DecodificheManager.getInstance().getNaturaMisuraSicurezza());
			Option lOptionT = new Option(DecodificheManager.getInstance().getTipoMisuraSicurezza());
			// setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN );
			setRequestAttribute("tipoMisuraSicurezza", "" + lOptionT);

			// Diffida Misure Sicurezza
			// Occorre passare la misura sicurezza in atto, perchè la "Libertà Vigilata" potrebbe essere
			// trasformata
			mRetPage = PG_INSERISCI_INOSSERVANZA_OBBLIGHI_MS;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Inosservanza Obblighi su Misure Sicurezza " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(TRASFORMA_MISURA_SICUREZZA) == 0) {

			// Lettura elenco Misure di Sicurezza Collegate al Fascicolo SIUS
			FascicoloGPModel lFasGPMod = new FascicoloGPModel(
					(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
			MisuraSicurezzaModel aMisuraSicurezza = new MisuraSicurezzaModel();
			aMisuraSicurezza.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			MisuraSicurezzaController lCtrl = new MisuraSicurezzaController();
			Vector lVectMS = lCtrl.ExRicercaMisuraSicurezza(aMisuraSicurezza);
			setRequestAttribute("misuresicurezza", lVectMS);

			// Lettura Misura di Sicurezza in Esecuzione Collegata al Fascicolo SIUS EMS padre
			IEsecuzioneMS lEseMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
			EsecuzioneMisuraSicurezzaModel aEMS = new EsecuzioneMisuraSicurezzaModel();
			EMSFascGPModel lEmsFasModel = new EMSFascGPModel();
			String annoEMS = lFasGPMod.getGeneraleProcedimentoModel().getAnnoS1().toString();
			String progEMS = lFasGPMod.getGeneraleProcedimentoModel().getProgrS1().toString();
			String uffiEMS = lFasGPMod.getFascicoloSiusModel().getChiaveUfficio();

			Vector lVectEMS = lEseMSCtrl.ExRicercaEsecuzioneMisureSicurezza(annoEMS, progEMS, null, null,
					null, null, uffiEMS, 1);

			if (lVectEMS.size() == 1)
				lEmsFasModel = ((EMSFascGPModel) lVectEMS.get(0));
			else
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Impossibile leggere la Misura Sicurezza in Esecuzione !");

			aEMS = lEmsFasModel.getEsecuzioneMSModel();

			if (aEMS == null)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Impossibile leggere la Misura Sicurezza in Esecuzione !");

			aEMS.setDescrTipoMisura(lEmsFasModel.getGeneraleProcedimentoModel().getDescrDefinizione());
			setRequestAttribute("esecuzionemisurasicurezza", aEMS);

			// Occorre passare alla jsp di inserimento anche le option per l'eventuale scelta di una nuova
			// misura
			Option lOptionN = new Option(DecodificheManager.getInstance().getNaturaMisuraSicurezza());
			Option lOptionT = new Option(DecodificheManager.getInstance().getTipoMisuraSicurezza());
			setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);
			setRequestAttribute("tipoMisuraSicurezza", "" + lOptionT);

			// Ordinanza Misura Sicurezza
			mRetPage = PG_LOAD_INSERISCI_RIESAME_MISURA_SICUREZZA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Riesame Misura Sicurezza " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(RICOVERO_OPG) == 0) {
			// Ordinanza Ricovero OPG
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_RICOVERO_OPG;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Ricovero OPG " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(ESTINZIONE_PENA) == 0) {
			// Estinzione Pena
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_ESTINZIONE_PENA;
			ricercaFascicoloOrigine();
		} else if (lCodTipoDec.compareTo(REVOCA_LC) == 0) {
			// Revoca Liberazione Condizionale
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_REVOCA_LC;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Revoca Liberazione Condizionale" + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(EST_PENA_LIB_CONDIZIONALE) == 0) {
			// Dichiarazione Estinzione Pena Liberazione Condizionale
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_EST_PENA_LIB_CONDIZIONALE;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Dichiarazione Estinzione Pena Liberazione Condizionale" + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(PROROGA_DETENZIONE_SPECIALE) == 0) {
			// Dichiarazione Proroga Detenzione Domiciliare Speciale
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_PROROGA_DETENZIONE_SPE;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Dichiarazione Proroga Detenzione Domiciliare Speciale" + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(PROROGA_DETENZIONE_DOMICILIARE) == 0) {
			// Dichiarazione Proroga Detenzione Domiciliare
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_PROROGA_DETENZIONE_DOM;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Dichiarazione Proroga Detenzione Domiciliare" + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(SOSPENSIONE_ESECUTIVA_ORDINANZA) == 0) {
			// Ordinanza di Sospensione Esecutiva Ordinanza TdS
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_SOSP_ESEC_ORD;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Sospensione Esecutiva Ordinanza TdS" + lCodTipoDec);

			// 28/03/2007 Imposta Tipo Ufficio con Trattino.
			// MEV 10 SIUS
			// La lista 'Tipo Ufficio' varia in base all'ufficio di appartenenza
			// dell'utente connesso (UDS-TDS/UDSM-TDSM)
			Option lOption = null;
			if (getUtenteConnesso().getUfficioUtente().getCodTipoUfficio().equals("TDS")
					|| getUtenteConnesso().getUfficioUtente().getCodTipoUfficio().equals("UDS")) {
				lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
			} else if (getUtenteConnesso().getUfficioUtente().getCodTipoUfficio().equals("TDSM")
					|| getUtenteConnesso().getUfficioUtente().getCodTipoUfficio().equals("UDSM")) {
				lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUSMinor());
			}
			setRequestAttribute("tipoUfficioSIUS", "-" + lOption);
		} else if (lCodTipoDec.compareTo(SOPRAVVENIENZA_NT) == 0) {
			// Esiste il decreto oltre all'ordinanza. Luigi 28-4-2006
			// Ordinanza di Sopravvenienza Nuovo Titolo
			mRetPage = PG_INSERISCI_SOPRAVVENIENZA_NT;
			setRequestAttribute("Action", "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Sopravvenienza Nuovo Titolo" + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(RICOVERI) == 0) {
			// Esiste il decreto oltre all'ordinanza. Luigi 17-05-2006
			// Ordinanza di Ricoveri
			mRetPage = PG_INSERISCI_DECRETO_RICOVERI;
			setRequestAttribute("Action", "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricoveri" + lCodTipoDec);
		}
		// STUB: Luigi 30-03-2007
		else if (lCodTipoDec.compareTo(RICOVERO_OPG_OSS_PSICHE) == 0) {
			// Esiste il decreto oltre all'ordinanza.
			// Ricovero OPG per Osservazioni Psichiche
			mRetPage = PG_INSERISCI_RICOVERO_OPG_OSS_PSICHE;
			setRequestAttribute("Action", "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricovero OPG Per Oss. Psic." + lCodTipoDec);
		}

		else if (lCodTipoDec.compareTo(REVOCA_MA) == 0) {
			// Ordinanza di Revoca Misura Alternativa
			// Occorre attivare una Action intermedia
			RedirectTo lRedirectTo = new RedirectTo();
			lRedirectTo.setPage(IWebConstants.PG_MAIN);
			lRedirectTo.setAction("siap.sius.depositoordinanzapc.action.ActLoadInserisciOrdinanzaRevocaMA");
			mRetPage = lRedirectTo.toString();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza di Revoca Misura Alternativa " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(CONC_RINVIO_EP) == 0) {
			// Ordinanza di Concessione Rinvio Esecuzione Pena
			// Occorre attivare una Action intermedia
			RedirectTo lRedirectTo = new RedirectTo();
			lRedirectTo.setPage(IWebConstants.PG_MAIN);
			lRedirectTo.setAction(
					"siap.sius.depositoordinanzapc.action.ActLoadInserisciOrdinanzaConcessioneRinvioEP");
			mRetPage = lRedirectTo.toString();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza di Concessione Rinvio Esecuzione Pena " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(RECLAMO_PERMESSO) == 0
				|| lCodTipoDec.compareTo(RECLAMO_LICENZA) == 0) {
			// Ordinanza di Reclamo Permesso
			// Occorre attivare una Action intermedia per la ricerca del decreto permesso
			RedirectTo lRedirectTo = new RedirectTo();
			lRedirectTo.setPage(IWebConstants.PG_MAIN);
			lRedirectTo.setAction(
					"siap.sius.depositoordinanzapc.action.ActLoadInserisciOrdinanzaReclamoPermesso");
			lRedirectTo.setParameter(ICostantiDepositoDecreto.CAMPO_TIPO_DECRETO_DA_PRODURRE, lCodTipoDec);
			mRetPage = lRedirectTo.toString();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza di Reclamo Permesso " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(RECLAMO_SCOMPUTO) == 0
				|| lCodTipoDec.compareTo(RECLAMO_REVOCA_LICENZA_PERMESSO) == 0) {
			// Ordinanza di Reclamo Scomputo
			// Occorre attivare una Action intermedia per la ricerca del decreto permesso
			RedirectTo lRedirectTo = new RedirectTo();
			lRedirectTo.setPage(IWebConstants.PG_MAIN);
			lRedirectTo.setAction(
					"siap.sius.depositoordinanzapc.action.ActLoadInserisciOrdinanzaReclamoPermesso");
			lRedirectTo.setParameter(ICostantiDepositoDecreto.CAMPO_TIPO_DECRETO_DA_PRODURRE, lCodTipoDec);
			mRetPage = lRedirectTo.toString();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza di Reclamo Scomputo " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(RECLAMO_LIBERAZIONE_ANTICIPATA) == 0) {
			// Ordinanza di Reclamo Liberazione Anticipata
			// Occorre attivare una Action intermedia per la ricerca dell'ordinanza
			RedirectTo lRedirectTo = new RedirectTo();
			lRedirectTo.setPage(IWebConstants.PG_MAIN);
			lRedirectTo.setAction(
					"siap.sius.depositoordinanzapc.action.ActLoadInserisciOrdinanzaReclamoPermesso");
			lRedirectTo.setParameter(ICostantiDepositoDecreto.CAMPO_TIPO_DECRETO_DA_PRODURRE, lCodTipoDec);
			mRetPage = lRedirectTo.toString();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza di Reclamo Liberazione Anticipata " + lCodTipoDec);
		}
		// AMBROS - Luglio 2014 -->
		else if (lCodTipoDec.compareTo(REVOCA_LIBERAZIONE_ANTICIPATA) == 0) {
			// Ordinanza di Revoca Liberazione Anticipata
			// Occorre attivare una Action intermedia per la ricerca dell'ordinanza
			RedirectTo lRedirectTo = new RedirectTo();
			lRedirectTo.setPage(IWebConstants.PG_MAIN);
			lRedirectTo.setAction(
					"siap.sius.depositoordinanzapc.action.ActLoadInserisciOrdinanzaReclamoPermesso");
			lRedirectTo.setParameter(ICostantiDepositoDecreto.CAMPO_TIPO_DECRETO_DA_PRODURRE, lCodTipoDec);
			mRetPage = lRedirectTo.toString();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ORDINANZA di Revoca Liberazione Anticipata " + lCodTipoDec);
		}
		// <--
		else if (lCodTipoDec.compareTo(APPLICAZIONE_SANZIONI_SOSTITUTIVE) == 0) {
			// Applicazione Sanzione Sostitutiva
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_APPLICAZIONE_SS;
			ricercaPenaComplessivaSanzioneSostitutivaByIdFasSiep();
			preparaListaTipoUfficiCompetente();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza di Applicazione Sanzione Sostitutiva " + lCodTipoDec);
		}
		// MEV_2023-35 si aggiunge un nuovo codice per il decreto generico (GENERICO2=GE)
		//else if (lCodTipoDec.compareTo(ICostantiDepositoDecreto.GENERICO) == 0) {
		  else if (   lCodTipoDec.compareTo(ICostantiDepositoDecreto.GENERICO) == 0
		           || lCodTipoDec.compareTo(ICostantiDepositoDecreto.GENERICO2) == 0) {
			// Decreto Generico diventa Ordinanza Generica
			lCodTipoDec = GENERICA;
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_GENERICA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Generica per Decreto Generico " + lCodTipoDec);
		}

		else if (lCodTipoDec.compareTo(REVOCA_ORDINANZA) == 0) {
			// throw new SIUSException(SIUSException.USER_MESSAGE,
			// "Ordinanza di Revoca non ancora prevista ma in fase di rilascio.");

			// Esiste il decreto oltre all'ordinanza. Luigi 8-11-2006
			// Ordinanza di Revoca Ordinanza

			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_REVOCA;
			ricercaFascicoloOrigine();
			// setRequestAttribute("Action", "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza di Revoca Ordinanza" + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(DECLARATORIA_ESTINZIONE_SS) == 0) {
			// Declaratoria Estinzione Sanzioni Sostitutive
			mRetPage = PG_INSERISCI_DECLARATORIA_ESTINZIONE_SANZIONI_SOSTITUTIVE;
			setRequestAttribute("Action", "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza di Declaratoria Estinzione Sanzioni Sostitutive " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(MODIFICA_PERMANENTE_SS) == 0) {
			// Modifica Permanente Sanzioni Sostitutive
			mRetPage = PG_INSERISCI_MODIFICA_PERMANENTE_SANZIONI_SOSTITUTIVE;
			setRequestAttribute("Action", "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza di Modifica Permanente Sanzioni Sostitutive " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(SOSPENSIONE_ESECUZIONE_SS) == 0
				// MEV_2023-35: aggiunta condizione di inserimento
				&& !"U137".equals(lCodContenuto)) {
			// Sospensione Esecuzione su Sanzioni Sostitutive
			mRetPage = PG_INSERISCI_SOSPENSIONE_ESECUZIONE_SANZIONI_SOSTITUTIVE;
			// setRequestAttribute("Action", "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS");
			setRequestAttribute("Action",
					"siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaPeriodoAltraSanzioneModificaESS");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Sospensione Esecuzione su Sanzioni Sostitutive " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(REVOCA_SANZIONE_SOSTITUTIVA) == 0) {
			// Ordinanza Revoca Sanzione Sostitutiva
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_REVOCA_SS;
			ricercaFascicoloOrigine();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Revoca Sanzione Sostitutiva " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(CONVERSIONE_SANZIONI_SOSTITUTIVE) == 0) {
			// Conversione su Sanzioni Sostitutive
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_CONVERSIONE_SS;
			setRequestAttribute("Action", "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Conversione su Sanzioni Sostitutive " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(RINVIO_SANZIONI_SOSTITUTIVE) == 0) {
			// Rinvio su Sanzioni Sostitutive
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_RINVIO_SS;
			setRequestAttribute("Action", "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Rinvio su Sanzioni Sostitutive " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(CONVERSIONE_PENE_PECUNIARIE) == 0) {
			// Controllo selezione Oggetti x Conversione Pene Pecuniarie.
			String lCodOggetto = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_OGGETTO);
			if (lCodOggetto.length() > 5)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Ordinanza di Conversione Pene Pecuniare possibile con un solo Oggetto");
			if (lCodOggetto.indexOf("2471") < 0 && lCodOggetto.indexOf("2470") < 0)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Ordinanza di Conversione Pene Pecuniare richiede un oggetto specifico");

			// Lettura dell'eventuale Richiesta di Conversione Pene Pecuniarie.
			RichiestaConversioneModel aRichiestaConversione = new RichiestaConversioneModel();
			FascicoloGPModel lFasGPMod = new FascicoloGPModel(
					(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
			aRichiestaConversione
					.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			IRichiestaConversione lCtrlRC = SIEPLookupRemote.getRichiestaConversioneRemote();
			Vector lVectRichConversioniPP = lCtrlRC
					.ExRicercaRichiestaConversioneEstesa(aRichiestaConversione);

			if (!(lVectRichConversioniPP.size() > 0))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Ordinanza di Conversione Pene Pecuniare impossibile senza Richiesta Conversione");

			setRequestAttribute("richiesteconversioni", lVectRichConversioniPP);

			// Conversione Pene Pecuniarie
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_CONVERSIONE_PP;
			setRequestAttribute("Action", "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Conversione Pene Pecuniarie " + lCodTipoDec);
		}
		/**
		 * else if (lCodTipoDec.compareTo(ORD_INOSSERVANZA_OBBLIGHI_MS ) == 0) { //Diffida Misure Sicurezza //
		 * Occorre passare la misura sicurezza in atto, perchè la "Libertà Vigilata" potrebbe essere
		 * trasformata mRetPage = PG_INSERISCI_INOSSERVANZA_OBBLIGHI_MS ; // [FT] - 03/08/2016 - MAC_LOG -
		 * Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug("Ordinanza Inosservanza Obblighi su Misure Sicurezza " + lCodTipoDec); }
		 **/
		else if (lCodTipoDec.compareTo(ORD_SOSPENSIONE_ESECUZIONE_MS) == 0) {
			// Sospensione Misure Sicurezza
			mRetPage = PG_INSERISCI_ORD_SOSPENSIONE_MS;
			// setRequestAttribute("Action", "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS");
			setRequestAttribute("Action",
					"siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaPeriodoAltraMisuraModificaEMS");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Sospensione Misure Sicurezza " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(CESSAZIONE_MS) == 0) {
			// Declaratoria Estinzione Misure Sicurezza
			mRetPage = PG_INSERISCI_CESSAZIONE_MISURE_SICUREZZA;
			setRequestAttribute("Action", "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza di Declaratoria Cessazione Misure Sicurezza " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(ICostantiDepositoOrdinanzaPc.RINVIO_ESECUZIONE_MS) == 0) {
			// Rinvio su Misure Sicurezza
			mRetPage = PG_LOAD_INSERISCI_ORDINANZA_RINVIO_MS;
			setRequestAttribute("Action", "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Rinvio su Misure Sicurezza " + lCodTipoDec);
		} else if (lCodTipoDec.compareTo(AMM_PROVVISORIA_AFFIDAMENTO_IN_PROVA_SERVIZI_SOC_ART47_OP) == 0) {
			mRetPage = PG_INS_ORDINANZA_AMM_PROVV_AFFIDAMENTO_IN_PROVA;
			Option lOption = new Option(DecodificheManager.getInstance().getStatoLibertatis(), "-", 75);
			setRequestAttribute("statolibertatis", "" + lOption);
			setRequestAttribute("Action",
					"siap.sius.depositoordinanzapc.action.ActInserisciOrdinanza_AmmProvv_AffiInProva_ServSoc");
			FascicoloGPModel lFasGPMod = new FascicoloGPModel(
					(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
			ITenore lCtrl = SIUSLookupRemote.getTenoreRemote();
			Vector lTenori = lCtrl.ExRicercaTenoreByGenProc(
					lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			setRequestAttribute("listaTenori", lTenori);
			String Codmag = getRequestStringParameter(ICostantiMagistratoRelatore.CAMPO_MAG_COD_MAGISTRATO);
			setRequestAttribute("CodMagRel", Codmag);
		} else if (lCodTipoDec.compareTo(RICHIESTA_OTTEMPERANZA) == 0) {
			// Ordinanza Generica
			mRetPage = PG_LOAD_INSERISCI_RICHIESTA_OTTEMPERANZA;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Richiesta Ottemperanza " + lCodTipoDec);
		}
		// 10102014 - DL 92/2014 Violazione CEDU
		else if (lCodTipoDec.compareTo(VIOLAZIONE_CEDU) == 0) {
			// Ordinanza Rimedi Risarcitori per Violazione ART. 3 CEDU
			mRetPage = PG_LOAD_INS_ORDINANZA_RISARCIMENTO_VIOLAZIONE_CEDU;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza di Risarcimrnto Art. 3 CEDU " + lCodTipoDec);
		} else if (lCodTipoDec.equalsIgnoreCase(RECLAMI_CEDU)) {
			mRetPage = PG_LOAD_INS_ORDINANZA_RECLAMI_VIOLAZIONE_CEDU;
		}
		// MEV_39 Appello Contro Provvedimento su Misura di Sicurezza
		else if (lCodTipoDec.equalsIgnoreCase(APPELLO_MS)) {
			// Lettura elenco Misure di Sicurezza Collegate al Fascicolo SIUS
			FascicoloGPModel lFasGPMod = new FascicoloGPModel(
					(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
			
//29/10/2019 eliminazione BLOCCO su richiesta di GASBARRI!!!
			if (!Utils.isNullObj(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine())){
	//				throw new SIUSException(SIUSException.USER_MESSAGE,
	//						"Impossibile leggere la Misura Sicurezza in Esecuzione!");
				// fascicolo originale
				BigDecimal idFascicoloSiusOrigine = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine();
				// info per il log
				siesLogger.debug("ID del Fascicolo Origine ->" + idFascicoloSiusOrigine);
				// ricerco provvedimento impugnato
				RicercaProvvedimentiCollegati rpc = new RicercaProvvedimentiCollegati(idFascicoloSiusOrigine);
				// Ricerca dell'Ordinanza
				OrdinanzaEventoTenoriPrescrizioniModel oetpm = rpc.RicercaOrdinanza("ALL");
				if (oetpm != null && oetpm.getOrdinanza() != null)
					setRequestAttribute("ordinanza", oetpm.getOrdinanza());
				else {
					// Ricerca del Decreto
					DepositoDecretoModel ddm = rpc.RicercaDecreto("ALL");
					if (ddm != null)
						setRequestAttribute("decreto", ddm);
// risoluzione anomalia n.2 del verbale di collaudo 11.3 (terza sessione)
//					else
//						throw new SIUSException(SIUSException.USER_MESSAGE,
//								"Impossibile leggere la Misura Sicurezza in Esecuzione!");
				}
			}
			
			//29/10/2019 eliminazione BLOCCO su richiesta di GASBARRI!!!
			BigDecimal idFascicoloSiusOrigine = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine();
			BigDecimal idFascicoloSiusLavorazione = null;
			if (idFascicoloSiusOrigine != null){
				idFascicoloSiusLavorazione = idFascicoloSiusOrigine;			
			}
			else{
				idFascicoloSiusLavorazione = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
			}	
			
			MisuraSicurezzaModel aMisuraSicurezza = new MisuraSicurezzaModel();
			// ricerco non per id fasc sius ma per id fasc sius origine (UDS)
			aMisuraSicurezza
					.setFasSiuIdFascicoloSius(idFascicoloSiusLavorazione);
			MisuraSicurezzaController lCtrl = new MisuraSicurezzaController();
			Vector lVect = lCtrl.ExRicercaMisuraSicurezza(aMisuraSicurezza);
			MisuraSicurezzaModel msm = null;
			Vector v = new Vector();
			if (!lVect.isEmpty()) {
				msm = (MisuraSicurezzaModel) lVect.firstElement();
				v.add(msm);
				setRequestAttribute("misuresicurezza", v);
			} 
			else if(lVect.isEmpty()){
				// ricerco non per id fasc sius ma per id fasc sius 
				aMisuraSicurezza
						.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				lCtrl = new MisuraSicurezzaController();
				lVect = lCtrl.ExRicercaMisuraSicurezza(aMisuraSicurezza);
				if (!lVect.isEmpty()) {
					msm = (MisuraSicurezzaModel) lVect.firstElement();
					v.add(msm);
					setRequestAttribute("misuresicurezza", v);
				} 
			}
			else {
				// Lettura Misura di Sicurezza in Esecuzione Collegata al Fascicolo SIUS EMS padre
				IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
				FascicoloGPModel fgpm = ifs.ExRicercaFascicoloByKey(idFascicoloSiusLavorazione);
				BigDecimal annoEMS = fgpm.getGeneraleProcedimentoModel().getAnnoS1();
				BigDecimal progEMS = fgpm.getGeneraleProcedimentoModel().getProgrS1();
				String uffiEMS = fgpm.getFascicoloSiusModel().getChiaveUfficio();
				fgpm = ifs.ExRicercaFascicoloByAnnoProgrCodUfficio(annoEMS, progEMS, uffiEMS);
				IEsecuzioneMS lEseMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
				EsecuzioneMisuraSicurezzaModel aEMS = lEseMSCtrl
						.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(idFascicoloSiusLavorazione);
// risoluzione anomalia n.2 del verbale di collaudo 11.3 (terza sessione)					
//					if (aEMS == null)
//						throw new SIUSException(SIUSException.USER_MESSAGE,
//								"Impossibile leggere la Misura Sicurezza in Esecuzione! Inserire la Misura di Sicurezza.");
				setRequestAttribute("esecuzionemisurasicurezza", aEMS);
			}
			
			// Occorre passare alla jsp di inserimento anche le option per l'eventuale scelta di una nuova
			// misura
			// Option lOptionN = new Option(DecodificheManager.getInstance().getNaturaMisuraSicurezza());
			Option lOptionT = new Option(DecodificheManager.getInstance().getTipoMisuraSicurezza());
			// setRequestAttribute("naturaMisuraSicurezza", "" + lOptionN);
			setRequestAttribute("tipoMisuraSicurezza", "" + lOptionT);
			// valore di ritorno
			mRetPage = PG_LOAD_INS_ORDINANZA_APPELLO_CONTRO_PROVV_MS;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ordinanza Appello contro Provvedimento Misura Sicurezza: " + lCodTipoDec);
		} 
	    // MEV_2023-35 - Applicazione Pene Sostitutive
        else if (lCodTipoDec.compareTo(APPLICAZIONE_PENE_SOSTITUTIVE) == 0) {
            // Applicazione Sanzione Sostitutiva
            mRetPage = PG_LOAD_INSERISCI_ORDINANZA_APPLICAZIONE_SP; 
            ricercaPenaComplessivaSanzioneSostitutivaByIdFasSiep(); // DA VERIFICARE
            preparaListaTipoUfficiCompetente();
            siesLogger.debug("Ordinanza di Applicazione Pene Sostitutiva " + lCodTipoDec);
        }
        // MEV_2023-35 - FINE
        // MEV_2023-35 - Revoca Autorizzazioni pene sostitutive 
        else if (lCodTipoDec.compareTo(REVOCA_AUTORIZZAZIONE_PS) == 0) {
          // Attenzione si utilizzano le stesse costanti del Decreto (per ora ) ICostantiDepositoDecreto
            // Revoca Autorizzazioni pene sostitutive 
            mRetPage = PG_INSERISCI_REVOCA_AUTORIZZAZIONE_PENA_SOSTITUTIVA_ORD; 
            ricercaFascicoloOrigine();
            siesLogger.debug("Ordinanza di ARevoca Autorizzazioni pene sostitutive " + lCodTipoDec);
        }
        // MEV_2023-35 - Revoca / Conversione Pena Pecuniaria Sostitutiva 
        else if (lCodTipoDec.compareTo(CONVERSIONE_REVOCA_PENA_SOST) == 0) {
          // Revoca / Conversione Pena Pecuniaria Sostitutiva 
          mRetPage = PG_LOAD_INSERISCI_ORDINANZA_REV_CONV_PPS;
          // Provo a recuperare l'importo da pagare dal fascicolo SIEP collegato se esiste
          RateizzazionePPModel lRataMancatoPagamento = ricercaMancatoPagamento();
          
          setRequestAttribute("RataMancatoPagamento",lRataMancatoPagamento);
          setRequestAttribute("Action", "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS");
          siesLogger.debug("Ordinanza Revoca Conversione Pena Pecuniaria Sostitutiva " + lCodTipoDec);
       // MEV_2023-35 - FINE
        } 	
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Ordinanza non prevista per il contenuto indicato");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("cod Tipo ordinanza decodificata" + lCodTipoDec);
		return lCodTipoDec;
	}

	// Viene ricavata la data di fine pena e passata alla request
	private void ricavaDataFinepena() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricavaDataFinepena: inizio");

		// Si preleva dalla sessione il fascicolo GPModel.
		if (!isSessionAttributeNullObj("fascicoloSiusGP")) {
			FascicoloGPModel lFasGPMod = new FascicoloGPModel(
					(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
			if (lFasGPMod.getFascicoloSiusModel() != null) {
				BigDecimal lIdFascicoloSiep = lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep();
				if (lIdFascicoloSiep != null) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("ID Fascicolo SIEP ->" + lIdFascicoloSiep);
					IPenaResidua lPenaResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
					PenaResiduaModel lPenaRes = lPenaResCtrl
							.ExRicercaPenaResiduaUltimaValidata(lIdFascicoloSiep);
					if (lPenaRes != null) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("ID Pena Residua ->" + lPenaRes.getIdPenaResidua());
						if (lPenaRes.getDataFine() != null) {
							setRequestAttribute("data_fine_misura_dd",
									DateUtils.getDateToString(lPenaRes.getDataFine(), "dd"));
							setRequestAttribute("data_fine_misura_MM",
									DateUtils.getDateToString(lPenaRes.getDataFine(), "MM"));
							setRequestAttribute("data_fine_misura_yyyy",
									DateUtils.getDateToString(lPenaRes.getDataFine(), "yyyy"));
						}
					}
				}
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricavaDataFinepena: fine");
	}

	// Viene ricavata la Sanzione Residua o la Sanzione Sostitutiva e passata alla request
	private void ricercaPenaComplessivaSanzioneSostitutivaByIdFasSiep() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaPenaComplessivaSanzioneSostitutivaByIdFasSiep: inizio");

		// Si preleva dalla sessione il fascicolo GPModel.
		if (!isSessionAttributeNullObj("fascicoloSiusGP")) {
			FascicoloGPModel lFasGPMod = new FascicoloGPModel(
					(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
			if (lFasGPMod.getFascicoloSiusModel() != null) {
				BigDecimal lIdFascicoloSiep = lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep();
				if (lIdFascicoloSiep != null) {
					// Se esiste il Fascicolo SIEP

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("ID Fascicolo SIEP ->" + lIdFascicoloSiep);

					// Si cerca la SANZIONE RESIDUA
					ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
					SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lIdFascicoloSiep,
							null);

					if (lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null) {
						// Se non è stata trovata la Sanzione Residua si ricerca la Sanzione Sostitutiva

						// Pena Complessiva e (al massimo 1 e al massimo 1)
						IPenaComplessiva lPenComCtr = SIEPLookupRemote.getPenaComplessivaRemote();
						PenaComplessivaSanzioneSostitutivaModel lPenCompSanzSost = lPenComCtr
								.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(
										lIdFascicoloSiep);
						// Se è stata trovata la Sanzione Sostitutiva si passa nella request
						if (lPenCompSanzSost != null && lPenCompSanzSost.getSanzioneSostitutiva() != null)
							setRequestAttribute("sanzione_sostitutiva",
									lPenCompSanzSost.getSanzioneSostitutiva());
					} else {
						// Se è stata trovata la Sanzione Residua si passa nella request
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("lSSResiduaModel = " + lSSResiduaModel);
						setRequestAttribute("sanzione_residua", lSSResiduaModel);
					}
				}
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ricercaPenaComplessivaSanzioneSostitutivaByIdFasSiep: fine");
	}

	/*
	 * La funzione prepara la lista con le Opzioni "Tipo Ufficio Competente" e la passa nella request per
	 * valorizzare la combo corrispondente nella form di inserimento.
	 */
	private void preparaListaTipoUfficiCompetente() throws Exception {
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		// MEV10-s3: aggiunte tipologie di ufficio
		lOption.setFilter(new String[] { "-", "TDS", "UDS", "TDSM", "UDSM", "CAP", "CAS", "CASAP", "CAPMI",
				"CAPMID", "CSS", "GIPMI", "GIP", "GIPM", "GP", "GUP", "GUPM", "GUPMI", "PT", "PM", "PMM",
				"PMPT", "PGCAP", "PGMI", "PGMID", "PMI", "TRIBSD", "CAPSM", "TMI", "DIB", "DIBM" }); // solo
																										// le
																										// Autorità
																										// Emittenti.
		setRequestAttribute("tipoUfficioCompetente", "" + lOption);
	}
	
    // MEV_2023-35 Recupero Se Presente la Rateizzazione collegata all'ultimo avviso mancato pagamento
    // del SIEP collegato
    private RateizzazionePPModel ricercaMancatoPagamento() throws Exception {
      siesLogger.debug("ricercaMancatoPagamento");
      RateizzazionePPModel lRataMancatoPagamento = null;

      if (!isSessionAttributeNullObj("fascicoloSiusGP")) {
        FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
        BigDecimal lIdFasicoloSIEP = null;
        if (   lFasGPMod.getFascicoloSiusModel() != null 
            && lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep()!=null)  {
          
          lIdFasicoloSIEP = lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep();
          
          // Ricerca ultimo evento 01-04-1308-Avviso mancato pagamento Pena Pecuniaria
          // validato
          
          EventoModel lEveRicerca = new EventoModel();
          lEveRicerca.setFlagDocumentoRegistrato("S");
          lEveRicerca.setFasSieIdFascicoloSiep(lIdFasicoloSIEP);
          lEveRicerca.setCodMotivo("1308");
          lEveRicerca.setCodTipoProvvedimento("04");
          
          IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
          //Vector <EventoModel> lListaAvvisi = lEveCtrl.ricercaEvento(new String[]{"1308"}, new String[]{"04"}, lEveRicerca);
          Vector <EventoModel> lListaAvvisi = lEveCtrl.ExRicercaEvento(lEveRicerca);
          
          
          if (lListaAvvisi!=null && lListaAvvisi.size()>0) {
            BigDecimal idEvento = lListaAvvisi.elementAt(0).getIdEvento();
            IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
            Vector <RateizzazionePPModel> listaRateizzazioni = irpp.exRicercaRateizzazioniByIdEvento(idEvento);
            
            if (listaRateizzazioni!=null && listaRateizzazioni.size()>0)
              lRataMancatoPagamento = listaRateizzazioni.elementAt(0); //presente 1 solo di tipo U
          }
        }
      }
      
      return lRataMancatoPagamento;
    }
}