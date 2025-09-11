package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.Utils;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.controller.MisuraSicurezzaController;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActLoadDettaglioOrdinanza - Classe Action per la load dettaglio di DepositoOrdinanzaPc
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadDettaglioOrdinanza extends ActDettaglioEmissioneOrdinanza
		implements ICostantiDepositoOrdinanzaPc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		String retPage = null;
		retPage = super.processRequest();
		// String codEsito=mOrdEveTenPreMod.getEvento().getCodEsito();
		FascicoloGPModel lFasGPMod = new FascicoloGPModel(
				(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
		if (mOrdEveTenPreMod.getOrdinanza() != null
				&& mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza() != null) {
			if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(LIBERAZIONE_ANTICIPATA) == 0
					|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
							.compareTo(RECLAMO_LIBERAZIONE_ANTICIPATA) == 0
					|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
							.compareTo(REVOCA_LIBERAZIONE_ANTICIPATA) == 0
					|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(VIOLAZIONE_CEDU) == 0
					|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(RECLAMI_CEDU) == 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(
						"Liberazione Anticipata / Reclamo Liberazione Anticipata / Revoca Liberazione Anticipata / Violazione CEDU / Reclamo CEDU");
				// Per queste Ordinanze devo cercare i Periodi di L.A.
				ILicenzaPeriodiLibAnticipata lCtrlDep = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
				Vector lLicenzePeriodi = lCtrlDep.ExRicercaLicenzeLibanticipataByEve(mIdEvento);
				this.setRequestAttribute("LicenzePeriodi", lLicenzePeriodi);

				// for(int cc=0; cc < lLicenzePeriodi.size(); cc++ )
				// {
				// LicenzaPeriodiLibAnticipataModel llModel = new LicenzaPeriodiLibAnticipataModel();
				// llModel = (LicenzaPeriodiLibAnticipataModel)lLicenzePeriodi.get(cc);
				// }
			} else if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(LICENZA) == 0) {
				if (mOrdEveTenPreMod.getOrdinanza().getNumGiorniLibanticipata().intValue() > 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Liberazione Anticipata LICENZA");
					ILicenzaPeriodiLibAnticipata lCtrlDep = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
					Vector lLicenze = lCtrlDep.ExRicercaLicenzeByEve(mIdEvento);
					this.setRequestAttribute("Licenze", lLicenze);
				}
			} else if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(RECLAMO_PERMESSO) == 0
					|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(RECLAMO_SCOMPUTO) == 0
					|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
							.compareTo(RECLAMO_REVOCA_LICENZA_PERMESSO) == 0
					|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
							.compareTo(RECLAMO_LICENZA) == 0) {
				ILicenzaPeriodiLibAnticipata lCtrlDep = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
				Vector lLicenze = lCtrlDep.ExRicercaLicenzeByEve(mIdEvento);
				this.setRequestAttribute("Licenze", lLicenze);
			} else if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
					.compareTo(REVOCA_ORDINANZA) == 0) {
				// Lettura dell'Ordinanza Revocata.
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Ordinanza di Revoca");

				BigDecimal lIdEventoProvRevocato = mOrdEveTenPreMod.getEvento().getEveIdEvento();
				if (lIdEventoProvRevocato != null) {
					OrdinanzaEventoTenoriPrescrizioniModel lOrdinanzaRevocata = ricercaOrdinanza(
							lIdEventoProvRevocato);

					if (lOrdinanzaRevocata == null)
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Ordinanza Revocata non trovata !");
					else
						setRequestAttribute("OrdinanzaRevocata", lOrdinanzaRevocata);
				}
			} else if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoDecreto.SOSPENSIONE_ESECUZIONE_SS) == 0) {
				// Lettura del Periodo Altra Sanzione
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("ricercaPeriodoAltraSanzione: inizio");

				// Ricerca il Periodo Altra Sanzione tramite l'ID dell'evento
				IPeriodoAltraSanzione lCtrlPAS = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
				PeriodoAltraSanzioneModel mPerAlSanz = lCtrlPAS
						.ExRicercaSanzioneSostitutivaByIdEvento(mOrdEveTenPreMod.getEvento().getIdEvento());

				// Passa i dati trovati del Periodo Altra Sanzione alla JSP
				setRequestAttribute("PeriodoAltraSanzione", mPerAlSanz);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("ricercaPeriodoAltraSanzione: fine");
			} else if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_SANZIONI_SOSTITUTIVE) == 0) {
				lFasGPMod = new FascicoloGPModel((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
				BigDecimal lIdFascicoloSiusOrigine = lFasGPMod.getFascicoloSiusModel()
						.getIdFascicoloSiusOrigine();
				if (lIdFascicoloSiusOrigine != null) {
					// prendo l'ultimo periodo altra sanzione per visualizzare la pena residua ed espiata
					IPeriodoAltraSanzione lCtrllst = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
					List lListaSanzioniSius = lCtrllst
							.ExRicercaSanzioneSostitutivaByIdFascicolo(lIdFascicoloSiusOrigine);
					if (lListaSanzioniSius != null && lListaSanzioniSius.size() > 0) {
						PeriodoAltraSanzioneModel lPerMod = (PeriodoAltraSanzioneModel) lListaSanzioniSius
								.get(lListaSanzioniSius.size() - 1);
						setRequestAttribute("lPerMod", lPerMod);
					}
				}
			}
			// 05/03/2009 Per Dettaglio Ordinanza Conversione Pene Pecuniarie.
			else if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_PENE_PECUNIARIE) == 0) {
				lFasGPMod = new FascicoloGPModel((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
				BigDecimal lIdFascicoloSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

				// Ticket#202412190123 - Va passata alla JSP un bean "rate" vuoto altrimenti va in errore
				// non riuscendo ad istanziare il bean- Vedi anche il caso
				// CONVERSIONE_PENE_PECUNIARIE_MANCATO_PAGAMENTO
				// dove il bean viene valorizzato
				Vector<RateizzazionePPModel> rate = new Vector<>();
				setRequestAttribute("rate", rate);
				// Ticket#202412190123 - FINE

				if (lIdFascicoloSius != null) {
					// Caricamento delle Richieste Conversioni
					RichiestaConversioneModel aRichiestaConversione = new RichiestaConversioneModel();
					aRichiestaConversione.setFasSiuIdFascicoloSius(lIdFascicoloSius);
					IRichiestaConversione lCtrlRC = SIEPLookupRemote.getRichiestaConversioneRemote();
					Vector lVectRichConversioniPP = lCtrlRC
							.ExRicercaRichiestaConversioneEstesa(aRichiestaConversione);

					if (lVectRichConversioniPP != null)
						setRequestAttribute("richiesteconversioni", lVectRichConversioniPP);
				}
			}
			// Modifica del 19/09/2013 mev "Revisione Misure di Sicurezza SIUS"
			// In fase di Emissione Ordinanza di un procedimento di: Inosservanza delle
			// misure di sicurezza detentive con oggetto "Inosservanza delle Misure di Sicurezza
			// Detentive (art. 214 c.p.)" ed esito "Dispone che ricominci a decorrere il periodo
			// minimi della misura", il sistema deve permettere l’indicazione della nuova Data di
			// Decorrenza della Misura di Sicurezza
			// In questo caso viene estratta la Misura di Sicurezza legata al Fascicolo Sius
			// per recuperare la Data Decorrenza inserita in fase di emissione dell'ordinanza.
			else if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoOrdinanzaPc.MISURA_SICUREZZA) == 0
					&& mOrdEveTenPreMod.getEvento().getCodEsito() != null && mOrdEveTenPreMod.getEvento()
							.getCodEsito().equals(ICostantiDepositoOrdinanzaPc.OGG_DISP_DECOR_PER_MINIM)) {

				IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
				List lMisureSicurezza = lCtrl.ExRicercaMisuraSicurezzaByIdFascicoloSIUS(
						((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel()
								.getIdFascicoloSius());
				if (lMisureSicurezza.size() > 0) {
					Iterator itxMis = lMisureSicurezza.iterator();
					while (itxMis.hasNext()) {
						MisuraSicurezzaModel lMisSicuSius = (MisuraSicurezzaModel) itxMis.next();
						setRequestAttribute("misuraSicurezza", lMisSicuSius);
					}
				}
			} else if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoOrdinanzaPc.TRASFORMA_MISURA_SICUREZZA) == 0
					&& mOrdEveTenPreMod.getEvento().getCodEsito() != null
					&& (mOrdEveTenPreMod.getEvento().getCodEsito()
							.equals(ICostantiDepositoOrdinanzaPc.OGG_SOSTITUISCE_LA_MISURA)
							|| "0134".equals(mOrdEveTenPreMod.getEvento().getCodEsito()))) {

				IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
				List lMisureSicurezza = lCtrl.ExRicercaMisuraSicurezzaByIdFascicoloSIUS(
						((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel()
								.getIdFascicoloSius());
				if (lMisureSicurezza.size() > 0) {
					Iterator itxMis = lMisureSicurezza.iterator();
					while (itxMis.hasNext()) {
						MisuraSicurezzaModel lMisSicuSius = (MisuraSicurezzaModel) itxMis.next();
						setRequestAttribute("misuraSicurezza", lMisSicuSius);
					}
				}
			}
			// MEV_2023-35
			else if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_REVOCA_PENA_SOST) == 0) {
				lFasGPMod = new FascicoloGPModel((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
				BigDecimal lIdFascicoloSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
				if (lIdFascicoloSius != null) {
					// Caricamento delle Richieste Conversioni
					RichiestaConversioneModel aRichiestaConversione = new RichiestaConversioneModel();
					aRichiestaConversione.setFasSiuIdFascicoloSius(lIdFascicoloSius);
					IRichiestaConversione lCtrlRC = SIEPLookupRemote.getRichiestaConversioneRemote();
					Vector lVectRichConversioniPP = lCtrlRC
							.ExRicercaRichiestaConversioneEstesa(aRichiestaConversione);

					if (lVectRichConversioniPP != null)
						setRequestAttribute("richiesteconversioni", lVectRichConversioniPP);

				}
			} else if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoOrdinanzaPc.REVOCA_PENA_SOSTITUTIVA) == 0
					|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(
							ICostantiDepositoOrdinanzaPc.RECLAMO_AVVERSO_REVOCA_PENA_SOSTITUTIVA) == 0) {
				// Ricerco eventuale record ESCEUZIONE_SANS_SOST collegato al deporito ordinanza
				EsecuzioneSanzioneSostitutivaModel lEsecSanSostModel = null;
				BigDecimal idDepositoOrd = mOrdEveTenPreMod.getOrdinanza().getIdDepositoOrdinanzaPc();
				IEsecuzioneSS lCtrlESS = SIUSLookupRemote.getEsecuzioneSSRemote();
				lEsecSanSostModel = lCtrlESS
						.ExRicercaEsecuzioneSanzioneSostitutivaByIdDepositoOrd(idDepositoOrd);
				setRequestAttribute("esecSansSostModel", lEsecSanSostModel);
			} else if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(
					ICostantiDepositoOrdinanzaPc.CONVERSIONE_PENE_PECUNIARIE_MANCATO_PAGAMENTO) == 0) {
				IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
				Vector<RateizzazionePPModel> rate = irpp.exRicercaRateizzazioniByIdFascicoloSius(
						mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				setRequestAttribute("rate", rate);
				IDepositoOrdinanzaPc idop = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
				DepositoOrdinanzaPcModel dopm = idop.ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd(
						mOrdEveTenPreMod.getOrdinanza().getGenPridGeneraleProcedimento(),
						mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza());
				setRequestAttribute("dopm", dopm);
			}
			// MEV_2023-35 - FINE
		}

		// 01/2014 - Decreto legge 146/2013 - Misura alternativa Ammissione in prova
		if (mOrdEveTenPreMod != null && mOrdEveTenPreMod.getOrdinanza() != null
				&& mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
						.compareTo(AMM_PROVVISORIA_AFFIDAMENTO_IN_PROVA_SERVIZI_SOC_ART47_OP) == 0) {
			retPage = PG_DETT_ORDINANZA_AMM_PROVV_AFFIDAMENTO_IN_PROVA;

			if (mOrdEveTenPreMod.getOrdinanza().getCodUffTdsConcessoRiduzione() != null
					&& mOrdEveTenPreMod.getOrdinanza().getCodUffTdsConcessoRiduzione().trim().length() > 0) {
				IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
				UfficioModel lUfficio = lUffCtrl
						.getUfficioByKey(mOrdEveTenPreMod.getOrdinanza().getCodUffTdsConcessoRiduzione());

				if (lUfficio != null)
					setRequestAttribute("ufficioTDS", lUfficio);
			}

			if (mOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp() != null
					&& mOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp().trim().length() > 0) {
				IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
				UfficioModel lUfficio = lUffCtrl
						.getUfficioByKey(mOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp());

				if (lUfficio != null)
					setRequestAttribute("ufficioUDS", lUfficio);
			}

			if (mOrdEveTenPreMod.getOrdinanza().getAutoritaVigilante() != null
					&& mOrdEveTenPreMod.getOrdinanza().getAutoritaVigilante().trim().length() > 0) {
				IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
				UfficioModel lUfficio = null;
				try {
					lUfficio = lUffCtrl
							.getUfficioByKey(mOrdEveTenPreMod.getOrdinanza().getAutoritaVigilante());
					if (lUfficio != null)
						// mOrdEveTenPreMod.getOrdinanza().setAutoritaVigilante(lUfficio.getDescrTipoUfficio()+"
						// di "+lUfficio.getDescrComune());
						setRequestAttribute("ufficioProcura", lUfficio);
				} catch (Exception e) {
					siesLogger.debug("Catturo l'eccezione senza rilanciarla: " + e.getMessage());
				}
			}
		}

		String codOggettoProcedimento = lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
		if ((codOggettoProcedimento.equalsIgnoreCase(
				ICostantiDepositoOrdinanzaPc.COD_OGGETTO_PROC_CONCESSIONE_MISURE_ALTERNATIVA)
				|| codOggettoProcedimento.equalsIgnoreCase(
						ICostantiDepositoOrdinanzaPc.COD_OGGETTO_PROC_CONCESSIONE_LIBERAZIONE_CONDIZIONALE))
				&& super.isUserTDSM()) {

			BigDecimal chiaveAnno = this.mFasGPMod.getFascicoloSiusModel().getChiaveAnno();
			BigDecimal chiaveProgr = this.mFasGPMod.getFascicoloSiusModel().getChiaveProgr();
			IMisuraAlternativa iMisuraAlternativa = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel misuraAlternativa = iMisuraAlternativa
					.ExRicercaMisuraAlternativaCorrenteByAnnoProgr(chiaveAnno, chiaveProgr);
			setRequestAttribute("misuraAlternativa", misuraAlternativa);
		}

		// MEV_63: aggiunto codice in or condition
		if ((codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_APPLICAZIONE_PROVVISORIA_MISURA_ALTERNATIVA)
				|| codOggettoProcedimento
						.equalsIgnoreCase(COD_OGGETTO_ESECUZIONE_PRESSO_DOMICILIO_PENA_DETENTIVA))
				&& super.isUserUDSM()) {
			BigDecimal chiaveAnno = this.mFasGPMod.getFascicoloSiusModel().getChiaveAnno();
			BigDecimal chiaveProgr = this.mFasGPMod.getFascicoloSiusModel().getChiaveProgr();
			IMisuraAlternativa iMisuraAlternativa = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel misuraAlternativa = iMisuraAlternativa
					.ExRicercaMisuraAlternativaCorrenteByAnnoProgr(chiaveAnno, chiaveProgr);
			setRequestAttribute("misuraAlternativa", misuraAlternativa);

		}

		// MERGE v10: aggiunta or condition
		if (codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_APPLICAZIONE_MISURA_SICUREZZA)
				|| codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_RIESAME_PERICOLOSITA_SOCIALE)
				|| codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_LIBERAZIONE_CONDIZIONALE)
				|| (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
						.compareTo(ICostantiDepositoOrdinanzaPc.TRASFORMA_MISURA_SICUREZZA) == 0
						&& mOrdEveTenPreMod.getEvento().getCodEsito() != null
						&& "0134".equals(mOrdEveTenPreMod.getEvento().getCodEsito())) && super.isUserUDSM()) {
			IMisuraSicurezza iMisuraSicurezza = SIEPLookupRemote.getMisuraSicurezzaRemote();
			// List misure = iMisuraSicurezza
			// .ExRicercaMisuraSicurezzaByIdFascicoloSIUS(this.mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			List<MisuraSicurezzaModel> misure = iMisuraSicurezza
					.ExRicercaMisuraSicurezzaByIdEvento(mOrdEveTenPreMod.getEvento().getIdEvento());

			Iterator<MisuraSicurezzaModel> itxMis = misure.iterator();
			while (itxMis.hasNext()) {
				MisuraSicurezzaModel lMisSicuSius = itxMis.next();
				// MERGE v10: modifica all'impostazione dell'attributo
				// scritto come era inseriva sempre l'ultimo
				if (Utils.isPresent(lMisSicuSius.getEveIdEvento())) {
					setRequestAttribute("misuraSicurezza", lMisSicuSius);
					break;
				}
			}
		}

		/*
		 * ISSUE MEV : aggiunto codice per gestione oggetto C029 
		 * Numero MEV : 39 
		 * Autore : Gioggi 
		 * Data : 19/giu/2017 
		 * Branch : MEV_39
		 */
		if (codOggettoProcedimento.equalsIgnoreCase(OGG_ORD_APPELLO_CONTRO_PROVV_MS)) {
			// 20191018 [SG]: aggiunto codice
			if (Utils.isPresent(mOrdEveTenPreMod.getTenori())) {
				for (TenoreModel tenore : mOrdEveTenPreMod.getTenori()) {
					String codEsitoTenore = tenore.getCodEsitoTenore();
					if (ICostantiMisuraSicurezza.COD_ACCOGLIE_APPELLO_E_MODIFICA_MDS
							.equalsIgnoreCase(codEsitoTenore)) {
						IMisuraSicurezza iMisuraSicurezza = SIEPLookupRemote.getMisuraSicurezzaRemote();
						List<MisuraSicurezzaModel> misure = iMisuraSicurezza
								.ExRicercaMisuraSicurezzaByIdEvento(
										mOrdEveTenPreMod.getEvento().getIdEvento());
						Iterator<MisuraSicurezzaModel> itxMis = misure.iterator();
						while (itxMis.hasNext()) {
							MisuraSicurezzaModel lMisSicuSius = itxMis.next();
							if (Utils.isPresent(lMisSicuSius.getFasSiuIdFascicoloSius())
									&& lMisSicuSius.getFasSiuIdFascicoloSius().compareTo(
											lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius()) == 0) {
								setRequestAttribute("misuraSicurezza", lMisSicuSius);
								break;
							}
						}
					}
				}
			}

			// recupero la MS in esecuzione
			MisuraSicurezzaModel msm = new MisuraSicurezzaModel();
			// ricerco non per id fasc sius ma per id fasc sius origine (UDS) se esiste
			// altrimenti ricerco per id fasc sius 20200125 [SG]
			msm.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null
					? lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine()
					: lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			MisuraSicurezzaController msc = new MisuraSicurezzaController();
			Vector lVect = msc.ExRicercaMisuraSicurezza(msm);
			msm = null;
			Vector v = new Vector();
			if (!lVect.isEmpty()) {
				msm = (MisuraSicurezzaModel) lVect.firstElement();
				v.add(msm);
				setRequestAttribute("misuresicurezza", v);
			} else {
				// fascicolo originale
				BigDecimal idFascicoloSiusOrigine = lFasGPMod.getFascicoloSiusModel()
						.getIdFascicoloSiusOrigine();
				// Lettura Misura di Sicurezza in Esecuzione Collegata al Fascicolo SIUS EMS padre
				IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
				FascicoloGPModel fgpm = ifs.ExRicercaFascicoloByKey(idFascicoloSiusOrigine);
				BigDecimal annoEMS = fgpm.getGeneraleProcedimentoModel().getAnnoS1();
				BigDecimal progEMS = fgpm.getGeneraleProcedimentoModel().getProgrS1();
				String uffiEMS = fgpm.getFascicoloSiusModel().getChiaveUfficio();
				fgpm = ifs.ExRicercaFascicoloByAnnoProgrCodUfficio(annoEMS, progEMS, uffiEMS);
				IEsecuzioneMS lEseMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
				EsecuzioneMisuraSicurezzaModel aEMS = lEseMSCtrl
						.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(
								fgpm.getGeneraleProcedimentoModel().getFasSiuIdFascicoloSius());
				setRequestAttribute("esecuzionemisurasicurezza", aEMS);
			}
			// pagina di ritorno
			retPage = PG_LOAD_DET_ORDINANZA_APPELLO_CONTRO_PROVV_MS;
		}
		// ***** FINE INTERVENTO MEV_39 *****//

		// MEV10-s3: aggiunto riferimento all'oggetto "codTipoUfficio"
		String codTipoUfficio = lFasGPMod.getFascicoloSiusModel().getCodTipoUfficio();
		setRequestAttribute("codTipoUfficio", codTipoUfficio);

		// info per il log
		siesLogger.debug("ActLoadDettaglioOrdinanza retPage = " + retPage);

		// valore di ritorno
		return retPage;
	}

}