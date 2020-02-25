package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;

/**
 * <p>
 * Title: ActLoadDettaglioEstremiSentenza
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di EstremiSentenza
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioEstremiSentenza extends ActDettaglioEmissioneOrdinanza implements
		ICostantiDepositoOrdinanzaPc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String retPage = null;
		retPage = super.processRequest();

		if (mOrdEveTenPreMod.getOrdinanza() != null
				&& mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza() != null)

			if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(LIBERAZIONE_ANTICIPATA) == 0
					|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
							.compareTo(RECLAMO_LIBERAZIONE_ANTICIPATA) == 0
					|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
							.compareTo(REVOCA_LIBERAZIONE_ANTICIPATA) == 0
					|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(VIOLAZIONE_CEDU) == 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger
						.debug("Liberazione Anticipata / Reclamo Liberazione Anticipata / Revoca Liberazione Anticipata / Violazione CEDU");
				// Per queste Ordinanze devo cercare i Periodi di L.A.
				ILicenzaPeriodiLibAnticipata lCtrlDep = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
				Vector lLicenzePeriodi = lCtrlDep.ExRicercaLicenzeLibanticipataByEve(mIdEvento);
				setRequestAttribute("LicenzePeriodi", lLicenzePeriodi);

			} else if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(LICENZA) == 0) {
				if (mOrdEveTenPreMod.getOrdinanza().getNumGiorniLibanticipata().intValue() > 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Liberazione Anticipata LICENZA");
					ILicenzaPeriodiLibAnticipata lCtrlDep = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
					Vector lLicenze = lCtrlDep.ExRicercaLicenzeByEve(mIdEvento);
					setRequestAttribute("Licenze", lLicenze);
				}
			} else if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(RECLAMO_PERMESSO) == 0
					|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(RECLAMO_SCOMPUTO) == 0
					|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
							.compareTo(RECLAMO_REVOCA_LICENZA_PERMESSO) == 0
					|| mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(RECLAMO_LICENZA) == 0) {
				ILicenzaPeriodiLibAnticipata lCtrlDep = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
				Vector lLicenze = lCtrlDep.ExRicercaLicenzeByEve(mIdEvento);
				setRequestAttribute("Licenze", lLicenze);
			} else if (mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza().compareTo(REVOCA_ORDINANZA) == 0) {
				// Lettura dell'Ordinanza Revocata.
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Ordinanza di Revoca");

				BigDecimal lIdEventoProvRevocato = mOrdEveTenPreMod.getEvento().getEveIdEvento();
				if (lIdEventoProvRevocato != null) {
					OrdinanzaEventoTenoriPrescrizioniModel lOrdinanzaRevocata = ricercaOrdinanza(lIdEventoProvRevocato);

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
				FascicoloGPModel lFasGPMod = new FascicoloGPModel(
						(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
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
				FascicoloGPModel lFasGPMod = new FascicoloGPModel(
						(FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"));
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
			}
			// TODO carmela
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
					&& mOrdEveTenPreMod.getEvento().getCodEsito() != null
					&& mOrdEveTenPreMod.getEvento().getCodEsito()
							.equals(ICostantiDepositoOrdinanzaPc.OGG_DISP_DECOR_PER_MINIM)) {

				IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
				List lMisureSicurezza = lCtrl
						.ExRicercaMisuraSicurezzaByIdFascicoloSIUS(((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
								.getFascicoloSiusModel().getIdFascicoloSius());
				if (lMisureSicurezza.size() > 0) {
					Iterator itxMis = lMisureSicurezza.iterator();
					while (itxMis.hasNext()) {
						MisuraSicurezzaModel lMisSicuSius = (MisuraSicurezzaModel) itxMis.next();
						setRequestAttribute("misuraSicurezza", lMisSicuSius);
					}
				}
			}

		// 01/2014 - Decreto legge 146/2013 - Misuraa lternativa Ammissione in prova
		if (mOrdEveTenPreMod != null
				&& mOrdEveTenPreMod.getOrdinanza() != null
				&& mOrdEveTenPreMod.getOrdinanza().getCodTipoOrdinanza()
						.compareTo(AMM_PROVVISORIA_AFFIDAMENTO_IN_PROVA_SERVIZI_SOC_ART47_OP) == 0) {
			retPage = PG_DETT_ORDINANZA_AMM_PROVV_AFFIDAMENTO_IN_PROVA;

			if (mOrdEveTenPreMod.getOrdinanza().getCodUffTdsConcessoRiduzione() != null
					&& mOrdEveTenPreMod.getOrdinanza().getCodUffTdsConcessoRiduzione().trim().length() > 0) {
				IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
				UfficioModel lUfficio = lUffCtrl.getUfficioByKey(mOrdEveTenPreMod.getOrdinanza()
						.getCodUffTdsConcessoRiduzione());

				if (lUfficio != null)
					setRequestAttribute("ufficioTDS", lUfficio);
			}

			if (mOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp() != null
					&& mOrdEveTenPreMod.getOrdinanza().getCodUfficioMagistratoComp().trim().length() > 0) {
				IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
				UfficioModel lUfficio = lUffCtrl.getUfficioByKey(mOrdEveTenPreMod.getOrdinanza()
						.getCodUfficioMagistratoComp());

				if (lUfficio != null)
					setRequestAttribute("ufficioUDS", lUfficio);
			}

			if (mOrdEveTenPreMod.getOrdinanza().getAutoritaVigilante() != null
					&& mOrdEveTenPreMod.getOrdinanza().getAutoritaVigilante().trim().length() > 0) {
				IUfficio lUffCtrl = SICOLookupRemote.getUfficioRemote();
				UfficioModel lUfficio = null;
				try {
					lUfficio = lUffCtrl.getUfficioByKey(mOrdEveTenPreMod.getOrdinanza()
							.getAutoritaVigilante());

					if (lUfficio != null)
						// mOrdEveTenPreMod.getOrdinanza().setAutoritaVigilante(lUfficio.getDescrTipoUfficio()+" di "+lUfficio.getDescrComune());
						setRequestAttribute("ufficioProcura", lUfficio);
				} catch (Exception e) {
					// nulla
				}

			}

		}

		return retPage;
	}

}