/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.action;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.controller.IAvvisiSius;
import it.eng.giustizia.avvocatura.util.Mapper;
import it.eng.giustizia.avvocatura.util.OrdinanzaMapper;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.CONVERSIONEPENEPECUNIARIETYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.DATIAVVISO;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.DATIORDINANZA;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.DATIRIEPILOGOPROCEDIMENTO;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.DESTINATARIOTYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.ESITITYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.MISURASICUREZZATYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.OUTPUTDETTAGLIOORDINANZA;
import it.eng.giustizia.avvocatura.ws.type.dettaglioOrdinanza.PRESCRIZIONETYPE;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * @author Gioggi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DettaglioOrdinanzaSiusAction extends ActionSius implements ICostantiDepositoOrdinanzaPc {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	/**
	 * Metodo per l'elaborazione del dettaglio dell'ordinanza
	 * 
	 * @param codTipoUfficio
	 *            --> in realtà xsd prevede codiceEsito --> non utilizzato
	 * @param codiTipoProvvedimento
	 *            --> non utilizzato
	 * @param idEvento
	 * @param datiAvviso
	 * @return OUTPUTDETTAGLIOORDINANZA
	 * @throws Exception
	 */
	public OUTPUTDETTAGLIOORDINANZA dettaglioOrdinanza(String codTipoUfficio, String codiTipoProvvedimento,
			String idEvento, DATIAVVISO datiAvviso) throws Exception {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DettaglioOrdinanzaSiusAction, metodo: dettaglioOrdinanza");

		// Ricerca i dati relativi all'Ordinanza
		OrdinanzaEventoTenoriPrescrizioniModel oetpm = ricercaOrdinanza(new BigDecimal(idEvento));
		IFascicoloSius ifss = SIUSLookupRemote.getFascicoloSiusRemote();
		FascicoloGPModel fgpm = null;
		FascicoloSiepModel fsm = null;
		OrdinanzaEventoTenoriPrescrizioniModel oetpmRev = null;
		FascicoloGPModel fgpmRev = null;

		try {
			// Ricerca del fascicolo sius
			fgpm = ifss.ExRicercaFascicoloByKey(oetpm.getEvento().getFasSiuIdFascicoloSius());
			// Ricerca del fascicolo siep
			IFascicoloSiep ifsp = SIEPLookupRemote.getFascicoloSiepRemote();
			BigDecimal idFascicoloSiep = oetpm.getEvento().getFasSieIdFascicoloSiep();
			if (!PropertyUtil.isPresent(idFascicoloSiep))
				idFascicoloSiep = fgpm.getFascicoloSiusModel().getFasSieIdFascicoloSiep();
			//@emma 09072018 intervento post COLLAUDO 11.2
			// correzione MEV AVVOCATURA post COLLAUDO (INVOCO LA RICERCA DEL FASCICOLO SIEP SOLO SE idFascicoloSiep IS NOT NULL!!!)
			if (PropertyUtil.isPresent(idFascicoloSiep))
				fsm = ifsp.ExRicercaFascicoloByKey(idFascicoloSiep);
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.info("Eccezione nella ricerca del fascicolo SIUS e SIEP: " + e.getMessage(), e);
		}

		// Se l'Ordinanza è revocata si cercano i dati relativi all'Ordinanza di Revoca
		boolean isOrdinanzaRevocata = false;
		if (oetpm != null && oetpm.getEvento() != null && oetpm.getEvento().getEveIdEventoRevoca() != null) {
			Object[] obj = ricercaOrdinanzaDiRevoca(oetpm.getEvento().getEveIdEventoRevoca());
			// decreto di revoca
			oetpmRev = (OrdinanzaEventoTenoriPrescrizioniModel) obj[0];
			// fascicolo di revoca
			fgpmRev = (FascicoloGPModel) obj[1];
			// info per il log
			avvocaturaLogger.info("Trattasi di ordinanza di revoca? " + oetpmRev.getOrdinanza() != null ? "SI" : "NO");
			avvocaturaLogger.info("Esiste fascicolo di revoca? " + fgpmRev != null ? "SI" : "NO");
			if (oetpmRev.getOrdinanza() != null)
				isOrdinanzaRevocata = true;
		}

		IMagistrato iMagistrato = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel mm = iMagistrato.ExRicercaMagistratoByEvento(new BigDecimal(idEvento));
		MagistratoRelatoreModel mrm = new MagistratoRelatoreModel();
		mrm.setMagistrato(mm);

		Vector prescrizioni = null;
		if (oetpm.getPrescrizioni().length > 0) {
			prescrizioni = new Vector();
			for (int i = 0; i < oetpm.getPrescrizioni().length; i++)
				prescrizioni.add((PrescrizioneModel) oetpm.getPrescrizioni()[i]);
			if ((fgpm != null && fgpm.getGeneraleProcedimentoModel() != null
					&& fgpm.getGeneraleProcedimentoModel().getCodTipoRegistro() != null
					&& "S09".equals(fgpm.getGeneraleProcedimentoModel().getCodTipoRegistro()))
					|| (ICostantiDepositoOrdinanzaPc.APPLICAZIONE_SANZIONI_SOSTITUTIVE.equals(oetpm.getOrdinanza().getCodTipoOrdinanza())
							|| ICostantiDepositoOrdinanzaPc.CONVERSIONE_PENE_PECUNIARIE.equals(oetpm.getOrdinanza().getCodTipoOrdinanza())
							|| ICostantiDepositoOrdinanzaPc.MISURA_SICUREZZA.equals(oetpm.getOrdinanza().getCodTipoOrdinanza()))) {
				// Chiama la funzione di decodifica
				List parsePrescrizioni = DecodificheUtils.parsePrescrizioni(null, prescrizioni);
				prescrizioni = new Vector();
				prescrizioni.addAll(parsePrescrizioni);
			}
		}

		UfficioModel um = null;
		if (oetpm != null && oetpm.getOrdinanza() != null
				&& oetpm.getOrdinanza().getCodUffTdsConcessoRiduzione() != null
				&& oetpm.getOrdinanza().getCodUffTdsConcessoRiduzione().trim().length() > 0) {
			IUfficio iUfficio = SICOLookupRemote.getUfficioRemote();
			um = iUfficio.getUfficioByKey(oetpm.getOrdinanza().getCodUffTdsConcessoRiduzione());
			// info per il log
			avvocaturaLogger.debug("Effettuata ricerca Ufficio Concesso Riduzione");
		}

		// Nel caso di Ordinanza di Rimessione Atti occorre caricare il vettore delle notifiche
		Vector notifiche = null;
		if (oetpm != null && oetpm.getOrdinanza() != null
				&& oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(RIMESSIONE_ATTI) == 0) {
			INotifica iNotifica = SIEPLookupRemote.getNotificaRemote();
			notifiche = iNotifica.ExRicercaEstesaNotificaByKeyEvento(new BigDecimal(idEvento));
			// info per il log
			avvocaturaLogger.debug("Effettuata ricerca notifiche");
		}

		// Nel caso di Ordinanza di Applicazione Misure Sicurezza occorre caricare il vettore delle misure
		Vector misureSicurezza = null;
		MisuraSicurezzaModel msm = null;
		if (oetpm != null && oetpm.getOrdinanza() != null
				&& oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(MISURA_SICUREZZA) == 0) {
			IMisuraSicurezza ims = SIEPLookupRemote.getMisuraSicurezzaRemote();
			MisuraSicurezzaModel msmTemp = new MisuraSicurezzaModel();
			msmTemp.setFasSiuIdFascicoloSius(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
			misureSicurezza = ims.ExRicercaMisuraSicurezza(msmTemp);
			if (PropertyUtil.isPresent(misureSicurezza))
				msm = (MisuraSicurezzaModel) misureSicurezza.get(0);
			else
				msm = null;
			// info per il log
			avvocaturaLogger.debug("Effettuata ricerca misura sicurezza");
		}

		int numTenori = oetpm.getTenori().length;
		String unificazione = "";
		for (int x = 0; x < numTenori; x++) {
			// verifico se si tratta di un oggetto "Unificazione delle misure di sicurezza (art. 209 C.P.)"
			// (2442)
			if ("2442".equals(oetpm.getTenori()[x].getCodOggettoTenore()))
				unificazione = "SI";
		}

		// Nel caso di Ordinanza di Esecuzione Misure Sicurezza con Trasformazione occorre caricare le misure
		// prima e dopo la trasformazione
		EsecuzioneMisuraSicurezzaModel emsmOld = null;
		EsecuzioneMisuraSicurezzaModel emsmNew = null;
		if (oetpm != null && oetpm.getOrdinanza() != null
				&& oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(TRASFORMA_MISURA_SICUREZZA) == 0
				&& "".equals(unificazione)) {
			IEsecuzioneMS iems = SIUSLookupRemote.getEsecuzioneMSRemote();
			emsmOld = new EsecuzioneMisuraSicurezzaModel();
			emsmNew = new EsecuzioneMisuraSicurezzaModel();
			fgpm = ifss.ExRicercaFascicoloByAnnoProgrCodUfficio(fgpm.getGeneraleProcedimentoModel()
					.getAnnoS1(), fgpm.getGeneraleProcedimentoModel().getProgrS1(), fgpm
					.getGeneraleProcedimentoModel().getCodUfficioInserimento());
			if (fgpm != null && fgpm.getFascicoloSiusModel() != null
					&& fgpm.getFascicoloSiusModel().getIdFascicoloSius() != null)
				emsmOld = iems.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(fgpm.getFascicoloSiusModel()
						.getIdFascicoloSius());
			emsmNew = iems.ExRicercaEsecuzioneMisuraSicurezzaByIdOrdinanza(oetpm.getOrdinanza()
					.getIdDepositoOrdinanzaPc());
			// info per il log
			avvocaturaLogger.debug("Effettuata ricerca misure prima e dopo la trasformazione");
		}

		// Nel caso di Ordinanza di Esecuzione Misure Sicurezza con Trasformazione occorre caricare
		// le misure rideterminate a seguito unificazione
		Vector misureSicurezzaRideterminate = null;
		if (oetpm != null && oetpm.getOrdinanza() != null
				&& oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(TRASFORMA_MISURA_SICUREZZA) == 0
				&& "SI".equals(unificazione)) {
			// Misure di Sicurezza Rideterminate a seguito Unificazione
			IEsecuzioneMS iems = SIUSLookupRemote.getEsecuzioneMSRemote();
			misureSicurezzaRideterminate = iems.ExRicercaEsecuzioneMisureSicRidByIdOrdinanza(oetpm
					.getOrdinanza().getIdDepositoOrdinanzaPc());

			// Misure di Sicurezza Inserite dall'UDS
			IMisuraSicurezza ims = SIEPLookupRemote.getMisuraSicurezzaRemote();
			MisuraSicurezzaModel msmTemp = new MisuraSicurezzaModel();
			msmTemp.setFasSiuIdFascicoloSius(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
			misureSicurezza = ims.ExRicercaMisuraSicurezza(msmTemp);

			// Misura di Sicurezza in Esecuzione
			emsmOld = new EsecuzioneMisuraSicurezzaModel();
			fgpm = ifss.ExRicercaFascicoloByAnnoProgrCodUfficio(fgpm.getGeneraleProcedimentoModel()
					.getAnnoS1(), fgpm.getGeneraleProcedimentoModel().getProgrS1(), fgpm
					.getGeneraleProcedimentoModel().getCodUfficioInserimento());
			if (fgpm != null && fgpm.getFascicoloSiusModel() != null
					&& fgpm.getFascicoloSiusModel().getIdFascicoloSius() != null)
				emsmOld = iems.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(fgpm.getFascicoloSiusModel()
						.getIdFascicoloSius());
			// la nuova ms viene impostata a null
			emsmNew = null;
			// info per il log
			avvocaturaLogger.debug("Effettuata ricerca misure rideterminate");
		}

		PeriodoAltraMisuraModel pamm = null;
		if (oetpm.getEvento() != null
				&& oetpm.getEvento().getCodMotivo() != null
				&& ("2410".equals(oetpm.getEvento().getCodMotivo())
						|| "2411".equals(oetpm.getEvento().getCodMotivo()) || "2412".equals(oetpm.getEvento()
						.getCodMotivo()))) {
			// Ricerca il Periodo Altra Misura tramite l'ID dell'evento
			IPeriodoAltraMisura ipam = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
			pamm = ipam.ExRicercaMisuraSicurezzaByIdEvento(oetpm.getEvento().getIdEvento());
			// info per il log
			avvocaturaLogger.debug("Effettuata ricerca periodo altra misura");
		}

		// ricerca fascicolo di origine
		FascicoloGPModel fgpmOrigine = null;
		if (fgpm != null && fgpm.getFascicoloSiusModel() != null
				&& fgpm.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null) {
			BigDecimal idFascicoloOrigine = fgpm.getFascicoloSiusModel().getIdFascicoloSiusOrigine();
			fgpmOrigine = ricercaFascicoloSIUS(idFascicoloOrigine);
			// info per il log
			avvocaturaLogger.debug("Effettuata ricerca Fascicolo Origine");
		}

		// fine "ActLoadDettaglioEmissioneOrdinanza" ed inizio "ActLoadDettaglioOrdinanza"
		Vector licenzePeriodi = null;
		Vector licenze = null;
		PeriodoAltraSanzioneModel pasm = null;
		Vector richiesteConversioni = null;
		if (oetpm.getOrdinanza() != null && oetpm.getOrdinanza().getCodTipoOrdinanza() != null) {
			if (oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(LIBERAZIONE_ANTICIPATA) == 0
					|| oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(RECLAMO_LIBERAZIONE_ANTICIPATA) == 0
					|| oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(REVOCA_LIBERAZIONE_ANTICIPATA) == 0
					|| oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(VIOLAZIONE_CEDU) == 0
					|| oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(RECLAMI_CEDU) == 0) {
				// info per il log
				avvocaturaLogger.debug("Liberazione Anticipata / Reclamo Liberazione Anticipata / Revoca Liberazione Anticipata / Violazione CEDU / Reclamo CEDU");
				// Per queste Ordinanze devo cercare i Periodi di L.A.
				ILicenzaPeriodiLibAnticipata ilpla = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
				licenzePeriodi = ilpla.ExRicercaLicenzeLibanticipataByEve(new BigDecimal(idEvento));
			} else if (oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(LICENZA) == 0) {
				if (oetpm.getOrdinanza().getNumGiorniLibanticipata().intValue() > 0) {
					// info per il log
					avvocaturaLogger.debug("Liberazione Anticipata LICENZA");
					ILicenzaPeriodiLibAnticipata ilpla = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
					licenze = ilpla.ExRicercaLicenzeByEve(new BigDecimal(idEvento));
				}
			} else if (oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(RECLAMO_PERMESSO) == 0
					|| oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(RECLAMO_SCOMPUTO) == 0
					|| oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(RECLAMO_REVOCA_LICENZA_PERMESSO) == 0
					|| oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(RECLAMO_LICENZA) == 0) {
				ILicenzaPeriodiLibAnticipata ilpla = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
				licenze = ilpla.ExRicercaLicenzeByEve(new BigDecimal(idEvento));
			} else if (oetpm.getOrdinanza().getCodTipoOrdinanza().compareTo(REVOCA_ORDINANZA) == 0) {
				// info per il log
				avvocaturaLogger.debug("Ordinanza di Revoca");
				// Lettura dell'Ordinanza Revocata.
				BigDecimal idEventoProvRevocato = oetpm.getEvento().getEveIdEvento();
				Object[] obj = null;
				if (idEventoProvRevocato != null)
					obj = ricercaOrdinanzaDiRevoca(idEventoProvRevocato);
				if (obj != null)
					oetpmRev = (OrdinanzaEventoTenoriPrescrizioniModel) obj[0];
			} else if (oetpm.getOrdinanza().getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoDecreto.SOSPENSIONE_ESECUZIONE_SS) == 0) {
				// Ricerca il Periodo Altra Sanzione tramite l'ID dell'evento
				IPeriodoAltraSanzione ipas = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
				pasm = ipas.ExRicercaSanzioneSostitutivaByIdEvento(oetpm.getEvento().getIdEvento());
				// info per il log
				avvocaturaLogger.debug("fine Ricerca Periodo Altra Sanzione");
			} else if (oetpm.getOrdinanza().getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_SANZIONI_SOSTITUTIVE) == 0) {
				BigDecimal idFascicoloSiusOrigine = fgpm.getFascicoloSiusModel().getIdFascicoloSiusOrigine();
				if (idFascicoloSiusOrigine != null) {
					// prendo l'ultimo periodo altra sanzione per visualizzare la pena residua ed espiata
					IPeriodoAltraSanzione ipas = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
					List listaSanzioniSius = ipas
							.ExRicercaSanzioneSostitutivaByIdFascicolo(idFascicoloSiusOrigine);
					if (listaSanzioniSius != null && listaSanzioniSius.size() > 0) {
						pasm = (PeriodoAltraSanzioneModel) listaSanzioniSius
								.get(listaSanzioniSius.size() - 1);
					}
				}
			}
			// Dettaglio Ordinanza Conversione Pene Pecuniarie.
			else if (oetpm.getOrdinanza().getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoOrdinanzaPc.CONVERSIONE_PENE_PECUNIARIE) == 0) {
				BigDecimal idFascicoloSius = fgpm.getFascicoloSiusModel().getIdFascicoloSius();
				if (idFascicoloSius != null) {
					// Caricamento delle Richieste Conversioni
					RichiestaConversioneModel rcm = new RichiestaConversioneModel();
					rcm.setFasSiuIdFascicoloSius(idFascicoloSius);
					IRichiestaConversione irc = SIEPLookupRemote.getRichiestaConversioneRemote();
					richiesteConversioni = irc.ExRicercaRichiestaConversioneEstesa(rcm);
				}
			}
			// In fase di Emissione Ordinanza di un procedimento di: Inosservanza delle
			// misure di sicurezza detentive con oggetto "Inosservanza delle Misure di Sicurezza
			// Detentive (art. 214 c.p.)" ed esito "Dispone che ricominci a decorrere il periodo
			// minimi della misura", il sistema deve permettere l'indicazione della nuova Data di
			// Decorrenza della Misura di Sicurezza
			// In questo caso viene estratta la Misura di Sicurezza legata al Fascicolo Sius
			// per recuperare la Data Decorrenza inserita in fase di emissione dell'ordinanza.
			else if (oetpm.getOrdinanza().getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoOrdinanzaPc.MISURA_SICUREZZA) == 0
					&& oetpm.getEvento().getCodEsito() != null
					&& oetpm.getEvento().getCodEsito()
							.equals(ICostantiDepositoOrdinanzaPc.OGG_DISP_DECOR_PER_MINIM)) {
				BigDecimal idFascicoloSius = fgpm.getFascicoloSiusModel().getIdFascicoloSius();
				IMisuraSicurezza ims = SIEPLookupRemote.getMisuraSicurezzaRemote();
				List listaMisureSicurezza = ims.ExRicercaMisuraSicurezzaByIdFascicoloSIUS(idFascicoloSius);
				if (listaMisureSicurezza.size() > 0) {
					Iterator iterator = listaMisureSicurezza.iterator();
					while (iterator.hasNext()) {
						msm = (MisuraSicurezzaModel) iterator.next();
					}
				}
			} else if (oetpm.getOrdinanza().getCodTipoOrdinanza()
					.compareTo(ICostantiDepositoOrdinanzaPc.TRASFORMA_MISURA_SICUREZZA) == 0
					&& oetpm.getEvento().getCodEsito() != null
					&& (oetpm.getEvento().getCodEsito()
							.equals(ICostantiDepositoOrdinanzaPc.OGG_SOSTITUISCE_LA_MISURA) || "0134"
							.equals(oetpm.getEvento().getCodEsito()))) {
				BigDecimal idFascicoloSius = fgpm.getFascicoloSiusModel().getIdFascicoloSius();
				IMisuraSicurezza ims = SIEPLookupRemote.getMisuraSicurezzaRemote();
				List lMisureSicurezza = ims.ExRicercaMisuraSicurezzaByIdFascicoloSIUS(idFascicoloSius);
				if (lMisureSicurezza.size() > 0) {
					Iterator iterator = lMisureSicurezza.iterator();
					while (iterator.hasNext()) {
						msm = (MisuraSicurezzaModel) iterator.next();
					}
				}
			}
		}

		// Decreto legge 146/2013 - Misura alternativa Ammissione in prova
		UfficioModel umTDS = null;
		UfficioModel umUDS = null;
		UfficioModel umProcura = null;
		if (oetpm != null
				&& oetpm.getOrdinanza() != null
				&& oetpm.getOrdinanza().getCodTipoOrdinanza()
						.compareTo(AMM_PROVVISORIA_AFFIDAMENTO_IN_PROVA_SERVIZI_SOC_ART47_OP) == 0) {
			if (oetpm.getOrdinanza().getCodUffTdsConcessoRiduzione() != null
					&& oetpm.getOrdinanza().getCodUffTdsConcessoRiduzione().trim().length() > 0) {
				IUfficio iUfficio = SICOLookupRemote.getUfficioRemote();
				umTDS = iUfficio.getUfficioByKey(oetpm.getOrdinanza().getCodUffTdsConcessoRiduzione());
			}
			if (oetpm.getOrdinanza().getCodUfficioMagistratoComp() != null
					&& oetpm.getOrdinanza().getCodUfficioMagistratoComp().trim().length() > 0) {
				IUfficio iUfficio = SICOLookupRemote.getUfficioRemote();
				umUDS = iUfficio.getUfficioByKey(oetpm.getOrdinanza().getCodUfficioMagistratoComp());
			}
			if (oetpm.getOrdinanza().getAutoritaVigilante() != null
					&& oetpm.getOrdinanza().getAutoritaVigilante().trim().length() > 0) {
				IUfficio iUfficio = SICOLookupRemote.getUfficioRemote();
				umProcura = iUfficio.getUfficioByKey(oetpm.getOrdinanza().getAutoritaVigilante());
			}
		}

		String codOggettoProcedimento = fgpm.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
		MisuraAlternativaModel mam = null;
		if ((codOggettoProcedimento
				.equalsIgnoreCase(ICostantiDepositoOrdinanzaPc.COD_OGGETTO_PROC_CONCESSIONE_MISURE_ALTERNATIVA) || codOggettoProcedimento
				.equalsIgnoreCase(ICostantiDepositoOrdinanzaPc.COD_OGGETTO_PROC_CONCESSIONE_LIBERAZIONE_CONDIZIONALE))
				&& "TDSM".equals(codTipoUfficio)) {
			BigDecimal chiaveAnno = fgpm.getFascicoloSiusModel().getChiaveAnno();
			BigDecimal chiaveProgr = fgpm.getFascicoloSiusModel().getChiaveProgr();
			IMisuraAlternativa ima = SICOLookupRemote.getMisuraAlternativaRemote();
			mam = ima.ExRicercaMisuraAlternativaCorrenteByAnnoProgr(chiaveAnno, chiaveProgr);
		}

		if (codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_APPLICAZIONE_PROVVISORIA_MISURA_ALTERNATIVA)
				&& "UDSM".equals(codTipoUfficio)) {
			BigDecimal chiaveAnno = fgpm.getFascicoloSiusModel().getChiaveAnno();
			BigDecimal chiaveProgr = fgpm.getFascicoloSiusModel().getChiaveProgr();
			IMisuraAlternativa ima = SICOLookupRemote.getMisuraAlternativaRemote();
			mam = ima.ExRicercaMisuraAlternativaCorrenteByAnnoProgr(chiaveAnno, chiaveProgr);
		}

		if (codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_APPLICAZIONE_MISURA_SICUREZZA)
				|| codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_RIESAME_PERICOLOSITA_SOCIALE)
				|| codOggettoProcedimento.equalsIgnoreCase(COD_OGGETTO_LIBERAZIONE_CONDIZIONALE)
				|| (oetpm.getOrdinanza().getCodTipoOrdinanza()
						.compareTo(ICostantiDepositoOrdinanzaPc.TRASFORMA_MISURA_SICUREZZA) == 0
						&& oetpm.getEvento().getCodEsito() != null && "0134".equals(oetpm.getEvento()
						.getCodEsito())) && "UDSM".equals(codTipoUfficio)) {
			IMisuraSicurezza ims = SIEPLookupRemote.getMisuraSicurezzaRemote();
			List<MisuraSicurezzaModel> misure = ims.ExRicercaMisuraSicurezzaByIdEvento(oetpm.getEvento()
					.getIdEvento());
			Iterator<MisuraSicurezzaModel> iterator = misure.iterator();
			while (iterator.hasNext()) {
				msm = iterator.next();
				if (PropertyUtil.isPresent(msm.getEveIdEvento()))
					break;
			}
		}

		// se presente, aggiorno la tabella degli avvisi
		if (PropertyUtil.isPresent(datiAvviso)) {
			IAvvisiSius ias = SIUSLookupRemote.getAvvisiSiusRemote();
			ias.aggiornaAvvisiAvvocato(new BigDecimal(datiAvviso.getIdAvviso()));
		}

		// info per il log
		avvocaturaLogger.debug("Valorizzo l'oggetto di output: OUTPUTDETTAGLIOORDINANZA");
		// copia dei dati dal model al type
		OUTPUTDETTAGLIOORDINANZA odo = copyModelToType(oetpm, mrm, fgpm, fsm, isOrdinanzaRevocata, notifiche,
				oetpmRev, fgpmRev, prescrizioni, um, misureSicurezza, msm, emsmOld, emsmNew,
				misureSicurezzaRideterminate, pamm, fgpmOrigine, licenzePeriodi, licenze, pasm,
				richiesteConversioni, umTDS, umUDS, umProcura, mam, codTipoUfficio);

		// valore di ritorno
		return odo;
	}
	/**
	 * Metodo per la ricerca di un'ordinanza di revoca
	 * 
	 * @param idEventoRevoca
	 * @return Object[]
	 * @throws Exception
	 */
	private Object[] ricercaOrdinanzaDiRevoca(BigDecimal idEventoRevoca) throws Exception {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioOrdinanzaSiusAction, metodo: ricercaOrdinanzaDiRevoca");

		// ricerca dell'ordinanza di revoca
		IDepositoOrdinanzaPc idop = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		OrdinanzaEventoTenoriPrescrizioniModel oetpmRev = idop
				.ExRicercaOrdinanzaEventoTenoriPrescrizioniByIdEvento(idEventoRevoca);

		// ricerca del fascicolo di revoca
		FascicoloGPModel fgpmRev = null;
		if (oetpmRev != null && oetpmRev.getOrdinanza() != null) {
			if (oetpmRev.getEvento() != null && oetpmRev.getEvento().getFasSiuIdFascicoloSius() != null)
				fgpmRev = ricercaFascicoloSIUS(oetpmRev.getEvento().getFasSiuIdFascicoloSius());
		}
		Object[] obj = new Object[2];
		obj[0] = oetpmRev;
		obj[1] = fgpmRev;

		// valore di ritorno
		return obj;
	}

	/**
	 * Metodo per la ricerca del fascicolo SIUS
	 * 
	 * @param fasSiuIdFascicoloSius
	 * @return
	 * @throws Exception
	 */
	private FascicoloGPModel ricercaFascicoloSIUS(BigDecimal fasSiuIdFascicoloSius) throws Exception {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioOrdinanzaSiusAction, metodo: ricercaFascicoloSIUS");

		FascicoloGPModel lFascicolo = null;
		IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
		lFascicolo = ifs.ExRicercaFascicoloByKey(fasSiuIdFascicoloSius);

		// valore di ritorno
		return lFascicolo;
	}

	/**
	 * Metodo per la valorizzazione del type di output
	 * 
	 * @param isOrdinanzaRevocata
	 * @param fsm
	 * @param fgpm
	 * @param mrm
	 * @param oetpm
	 * @param notifiche
	 * @param oetpmRev
	 * @param fgpmRev
	 * @param prescrizioni
	 * @param um
	 * @param misureSicurezza
	 * @param msm
	 * @param emsmOld
	 * @param emsmNew
	 * @param misureSicurezzaRideterminate
	 * @param pamm
	 * @param fgpmOrigine
	 *            --> NON UTILIZZATO
	 * @param licenzePeriodi
	 * @param licenze
	 * @param pasm
	 * @param richiesteConversioni
	 * @param umTDS
	 * @param umUDS
	 * @param umProcura
	 * @param mam
	 * @param codTipoUfficio
	 * @return OUTPUTDETTAGLIOORDINANZA
	 * @throws Exception
	 */
	private OUTPUTDETTAGLIOORDINANZA copyModelToType(OrdinanzaEventoTenoriPrescrizioniModel oetpm,
			MagistratoRelatoreModel mrm, FascicoloGPModel fgpm, FascicoloSiepModel fsm,
			boolean isOrdinanzaRevocata, Vector notifiche, OrdinanzaEventoTenoriPrescrizioniModel oetpmRev,
			FascicoloGPModel fgpmRev, Vector prescrizioni, UfficioModel um, Vector misureSicurezza,
			MisuraSicurezzaModel msm, EsecuzioneMisuraSicurezzaModel emsmOld,
			EsecuzioneMisuraSicurezzaModel emsmNew, Vector misureSicurezzaRideterminate,
			PeriodoAltraMisuraModel pamm, FascicoloGPModel fgpmOrigine, Vector licenzePeriodi,
			Vector licenze, PeriodoAltraSanzioneModel pasm, Vector richiesteConversioni, UfficioModel umTDS,
			UfficioModel umUDS, UfficioModel umProcura, MisuraAlternativaModel mam, String codTipoUfficio)
			throws Exception {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioOrdinanzaSiusAction, metodo: copyModelToType");

		// instanzio ed inizializzo un oggetto di tipo "OUTPUTDETTAGLIOORDINANZA"
		OUTPUTDETTAGLIOORDINANZA odo = new OUTPUTDETTAGLIOORDINANZA();

		try {
			// recupero dati ordinanza e lista esiti
			DATIORDINANZA dr = OrdinanzaMapper.mapDatiOrdinanza(oetpm, pamm, msm, licenzePeriodi,
					isOrdinanzaRevocata, oetpmRev, fgpm);
			List<ESITITYPE> let = OrdinanzaMapper.mapEsiti(oetpm.getTenori(), oetpm.getOrdinanza());
			if (PropertyUtil.isPresent(let))
				dr.getElencoEsiti().addAll(let);
			odo.setDATIORDINANZA(dr);

			// recupero dati procedimento
			DATIRIEPILOGOPROCEDIMENTO drp = OrdinanzaMapper.mapDatiRiepilogoProcedimento(fgpm, fsm, mrm,
					isOrdinanzaRevocata, fgpmRev);
			odo.setDATIRIEPILOGOPROCEDIMENTO(drp);

			// recupero dati destinatari
			List<DESTINATARIOTYPE> ldt = OrdinanzaMapper.mapDestinatari(notifiche);
			if (PropertyUtil.isPresent(ldt))
				odo.getElencoDestinatari().addAll(ldt);

			// recupero dati prescrizioni
			List<PRESCRIZIONETYPE> lpt = OrdinanzaMapper.mapPrescrizioni(prescrizioni);
			if (PropertyUtil.isPresent(lpt))
				odo.getElencoPrescrizioni().addAll(lpt);

			// recupero dati conversioni pecuniarie
			List<CONVERSIONEPENEPECUNIARIETYPE> lcppt = OrdinanzaMapper.mapDatiConversionePenePecuniarie(
					oetpm, richiesteConversioni);
			if (PropertyUtil.isPresent(lcppt))
				odo.getElencoConversioniPecuniarie().addAll(lcppt);

			// recupero dati destinatari rimessione atti
			List<DESTINATARIOTYPE> ldrat = OrdinanzaMapper.mapDestinatariRimessionAtti(notifiche);
			if (PropertyUtil.isPresent(ldrat))
				odo.getElencoDestinatariRimessionAtti().addAll(ldrat);

			// recupero dati misure di sicurezza
			List<MISURASICUREZZATYPE> mst = OrdinanzaMapper.mapMisureSicurezza(misureSicurezza);
			if (PropertyUtil.isPresent(mst))
				odo.getElencoMisureSicurezza().addAll(mst);

			// recupero dati misure di sicurezza rideterminate
			List<MISURASICUREZZATYPE> msrt = OrdinanzaMapper.mapMisureSicurezza(misureSicurezzaRideterminate);
			if (PropertyUtil.isPresent(msrt))
				odo.getElencoMisureRideterminate().addAll(msrt);
			odo.setDatiEsecuzioneMisureSicurezzaAttuali(OrdinanzaMapper.mapDatiMisuraSicurezza(emsmNew));
			odo.setDatiEsecuzioneMisureSicurezzaPrecedenti(OrdinanzaMapper.mapDatiMisuraSicurezza(emsmOld));
			// TODO: gestire questi dati ove necessario
			// UfficioModel umTDS;
			// UfficioModel umUDS;
			// UfficioModel umProcura;

			odo.setDatiConcessioneRinvio(OrdinanzaMapper.mapDatiConcessioneRinvio(oetpm, codTipoUfficio));
			odo.setDatiConversioneSanzSost(OrdinanzaMapper.mapDatiConversioneSanzSost(oetpm, pasm));
			odo.setDatiEsecuzDomicilio(OrdinanzaMapper.mapDatiEsecuzDomicilio(oetpm, codTipoUfficio));
			odo.setDatiEstinzionePena(OrdinanzaMapper.mapDatiEstinzionePena(oetpm));
			odo.setDatiEstinzionePenaLibCondizionale(OrdinanzaMapper
					.mapDatiEstinzionePenaLibCondizionale(oetpm));
			odo.setDatiEstinzioneSanzSost(OrdinanzaMapper.mapDatiEstinzioneSanzSost(oetpm));
			odo.setDatiIndultino(OrdinanzaMapper.mapDatiIndultino(oetpm));
			odo.setDatiLibertaAnticipata(OrdinanzaMapper.mapDatiLibertaAnticipata(oetpm, licenzePeriodi));
			odo.setDatiLicenza(OrdinanzaMapper.mapDatiLicenza(licenze));
			odo.setDatiMisuraAlternativa(OrdinanzaMapper.mapDatiMisuraAlternativa(oetpm));
			odo.setDatiModificaPermanSanziSost(OrdinanzaMapper.mapDatiModificaPermanSanziSost(oetpm));
			odo.setDatiOrdinanzaReclamata(OrdinanzaMapper.mapDatiOrdinanzaReclamata(oetpm));
			odo.setDatiOrdinanzaSospesa(OrdinanzaMapper.mapDatiOrdinanzaSospesa(oetpm, codTipoUfficio));
			odo.setDatiProrogaDetenDomic(OrdinanzaMapper.mapDatiProrogaDetenDomic(oetpm));
			odo.setDatiProrogaDetenDomicSpeciale(OrdinanzaMapper.mapDatiProrogaDetenDomicSpeciale(oetpm));
			odo.setDatiReclamiCEDU(OrdinanzaMapper.mapDatiReclamiCEDU(licenzePeriodi));
			odo.setDatiReclamoPermesso(OrdinanzaMapper.mapDatiReclamoPermesso(oetpm, licenze));
			odo.setDatiRevoca(OrdinanzaMapper.mapDatiRevoca(oetpmRev));
			odo.setDatiRevocaLiberazioneAnticipata(OrdinanzaMapper.mapDatiRevocaLiberazioneAnticipata(oetpm));
			odo.setDatiRevocaLibertaAnticipata(OrdinanzaMapper.mapDatiRevocaLibertaAnticipata(oetpm,
					licenzePeriodi));
			odo.setDatiRevocaMisuraAlternativa(OrdinanzaMapper.mapDatiRevocaMisuraAlternativa(oetpm,
					codTipoUfficio, um));
			odo.setDatiRicoveri(OrdinanzaMapper.mapDatiRicoveri(oetpm));
			odo.setDatiRicoveriOssPsich(OrdinanzaMapper.mapDatiRicoveriOssPsich(oetpm));
			odo.setDatiRinvioSanzSost(OrdinanzaMapper.mapDatiRinvioSanzSost(oetpm));
			odo.setDatiSanzioneSostitutiva(OrdinanzaMapper.mapDatiSanzioneSostitutiva(oetpm));
			odo.setDatiScomputo(OrdinanzaMapper.mapDatiScomputo(licenze));
			odo.setDatiSopravvenienzaNuovoTitolo(OrdinanzaMapper.mapDatiSopravvenienzaNuovoTitolo(oetpm,
					codTipoUfficio));
			odo.setDatiSospEsecSanzSost(OrdinanzaMapper.mapDatiSospEsecSanzSost(oetpm, codTipoUfficio, pasm));
			// stringhe
			String formaMisuraMA = null;
			String comunitaMA = null;
			if (mam != null && mam.getFlFormaMisura() != null) {
				formaMisuraMA = "Permanenza in casa";
				if (mam.getFlFormaMisura().compareTo(new BigDecimal(2)) == 0)
					formaMisuraMA = "Collocamento in comunità";
				if (mam.getFlFormaMisura().compareTo(new BigDecimal(2)) == 0
						&& mam.getDescrizioneComunita() != null) {
					comunitaMA = (mam.getDescrizioneComunita() == null) ? "" : mam.getDescrizioneComunita();
				}
			}
			String formaMisuraMS = null;
			String comunitaMS = null;
			if (msm != null && msm.getFlFormaMisura() != null) {
				formaMisuraMS = "Permanenza in casa";
				if (msm.getFlFormaMisura().compareTo(new BigDecimal(2)) == 0)
					formaMisuraMS = "Collocamento in comunità";
	      		if (msm.getFlFormaMisura().compareTo(new BigDecimal(2)) == 0
	      				&& msm.getDescrizioneComunita() != null)
	      			comunitaMS = (msm.getDescrizioneComunita() == null) ? "" : msm.getDescrizioneComunita();
			}
			odo.setDescrFormaMisuraMA(formaMisuraMA);
			odo.setDescrComunitaMA(comunitaMA);
			odo.setDescrFormaMisuraMS(formaMisuraMS);
			odo.setDescrComunitaMS(comunitaMS);
			odo.setDescrDiagnosiPsichiatrica(oetpm.getOrdinanza().getCodNaturaProvvedimento());
			odo.setDescTipoControlloEsecuzione(oetpm.getOrdinanza().getDescrTipoControlloEsecuzione());
			odo.setDatiFascicoloOrigine(OrdinanzaMapper.mapDatiFascioloOrigine(fgpmOrigine));		   
			// imposto l'ERRORE
			odo.setERRORE(Mapper.mapErroreOrdinanza("000", ""));
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.error("Errore nella mappatura dei dati dell'ordinanza: " + e.getMessage(), e);
			// imposto l'ERRORE
			odo.setERRORE(Mapper.mapErroreOrdinanza("005", e.getMessage()));
		}

		// valore di ritorno
		return odo;
	}

	/**
	 * Funzione di ricerca dei dati relativi all'Ordinanza.
	 * 
	 * @param idEvento
	 * @throws Exception
	 */
	private OrdinanzaEventoTenoriPrescrizioniModel ricercaOrdinanza(BigDecimal idEvento) throws Exception {

		// Lettura dell'Ordinanza di Revoca.
		avvocaturaLogger.debug("Ricerca Ordinanza");
		IDepositoOrdinanzaPc idop = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		OrdinanzaEventoTenoriPrescrizioniModel oetpm = idop
				.ExRicercaOrdinanzaEventoTenoriPrescrizioniByIdEvento(idEvento);
		// valore di ritorno
		return oetpm;
	}

}