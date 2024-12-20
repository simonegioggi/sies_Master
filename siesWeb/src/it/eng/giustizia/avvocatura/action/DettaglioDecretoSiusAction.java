/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.action;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import it.eng.giustizia.avvocatura.controller.IAvvisiSius;
import it.eng.giustizia.avvocatura.util.DecretoMapper;
import it.eng.giustizia.avvocatura.util.Mapper;
import it.eng.giustizia.avvocatura.util.PropertyUtil;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.AVVOCATOTYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.DATIAVVISO;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.DATIDECRETO;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.DATIRIEPILOGOPROCEDIMENTO;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.DESTINATARIOTYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.MOTIVAZIONITYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.OUTPUTDETTAGLIODECRETO;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.PERMESSOTYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.PRESCRIZIONETYPE;
import it.eng.giustizia.avvocatura.ws.type.dettaglioDecreto.TENORETYPE;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.curatore.controller.ICuratore;
import siap.sige.curatore.model.CuratoreModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.ActionSius;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.curatore.controller.ICuratoreSius;
import siap.sius.curatore.model.CuratoreSiusModel;
import siap.sius.depositodecreto.action.ICostantiDepositoDecreto;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel;
import siap.sius.depositodecreto.model.DepositoDecretoFascicoloModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.prescrizione.controller.IPrescrizione;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.tenore.controller.ITenore;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * @author Gioggi
 */
@SuppressWarnings("rawtypes")
public class DettaglioDecretoSiusAction extends ActionSius implements ICostantiDepositoDecreto {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	/**
	 * Metodo per l'elaborazione del dettaglio del decreto
	 * 
	 * @param codiceEsito
	 * @param codiTipoProvvedimento
	 * @param idEvento
	 * @param datiAvviso
	 * @return OUTPUTDETTAGLIODECRETO
	 * @throws Exception
	 */
	public OUTPUTDETTAGLIODECRETO dettaglioDecreto(String codiceEsito, String codiTipoProvvedimento,
			String idEvento, DATIAVVISO datiAvviso) throws Exception {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: DettaglioDecretoSiusAction, metodo: dettaglioDecreto");

		BigDecimal idEv = new BigDecimal(idEvento);
		// Ricerca del decreto in DEPOSITO_DECRETO.
		IDepositoDecreto idd = SIUSLookupRemote.getDepositoDecretoRemote();
		DepositoDecretoModel ddm = null;
		DepositoDecretoModel ddmRevoca = null;
		DepositoDecretoModel ddmRevocato = null;
		DepositoDecretoEventoMotivazioniModel ddemm = null;

		// Ricerca del fascicolo sius
		IFascicoloSius ifss = SIUSLookupRemote.getFascicoloSiusRemote();
		FascicoloGPModel fgpm = null;
		FascicoloSiepModel fspm = null;
		FascicoloGPModel fgpmRevoca = null;
		FascicoloGPModel fgpmOrigine = null;
		FascicoloGPModel fgpmRevocato = null;
		FascicoloGPModel fgpmOrigineRevocato = null;
		UdienzaModel um = null;
		IstitutoDetenzioneModel idm = null;
		FascicoloGPModel fascicoloUnificante = null;
		FascicoloGPModel fascicoloUnificato = null;
		// Ricerca del fascicolo siep
		IFascicoloSiep ifsp = SIEPLookupRemote.getFascicoloSiepRemote();
		IEvento iEvento = SICOLookupRemote.getEventoRemote();
		EventoModel em = null;
		String tipoDecreto = null;
		IAvvocato iAvvocato = SIUSLookupRemote.getAvvocatoRemote();
		Vector difensori = null;
		ITenore iTenore = SIUSLookupRemote.getTenoreRemote();
		Vector tenori = null;
		MagistratoRelatoreModel mrm = null;
		boolean isDecretoDiRevoca = false;

		try {
			// ricerca dell'evento
			em = iEvento.ExRicercaEventoByKey(idEv);
			if (em != null && em.getCodMotivo() != null && !"0600".equals(em.getCodMotivo())) {
				// Ricerca del decreto
				ddemm = idd.ExRicercaDecretoEventoMotivazioniInammissibilitaByIdEvento(idEv);
				// Ricavo il decreto
				ddm = ddemm.getDepositoDecreto();
				// Ricavo il tipo di decreto
				tipoDecreto = ddm.getCodTipoDecreto();
				// Ricerca del fascicolo sius
				fgpm = ifss.ExRicercaFascicoloByKey(ddemm.getEvento().getFasSiuIdFascicoloSius());
				// Ricerca del fascicolo siep
				BigDecimal idFascicoloSiep = ddemm.getEvento().getFasSieIdFascicoloSiep();
				if (!PropertyUtil.isPresent(idFascicoloSiep))
					idFascicoloSiep = fgpm.getFascicoloSiusModel().getFasSieIdFascicoloSiep();
				fspm = ifsp.ExRicercaFascicoloByKey(idFascicoloSiep);
			} else {
				// Lettura dei fascicoli SIUS Unificante e Unificato.
				fascicoloUnificato = ifss.ExRicercaFascicoloByKey(em.getFasSiuIdFascicoloSius());
				fascicoloUnificante = ifss.ExRicercaFascicoloByKey(fascicoloUnificato.getFascicoloSiusModel()
						.getFasSiuIdFascicoloSius());
				tipoDecreto = UNIFICAZIONE;
			}
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.info("Eccezione nella ricerca del fascicolo SIUS e SIEP: " + e.getMessage(), e);
		}

		if (!UNIFICAZIONE.equals(tipoDecreto)) {
			if (ddemm != null) {
				// Se il decreto è revocato si ricercano i dati relativi al Decreto di revoca
				if (ddemm.getEvento() != null && ddemm.getEvento().getEveIdEventoRevoca() != null) {
					Object[] obj = ricercaDecretoDiRevoca(ddemm.getEvento().getEveIdEventoRevoca(), idd);
					// decreto di revoca
					ddmRevoca = (DepositoDecretoModel) obj[0];
					// fascicolo di revoca
					fgpmRevoca = (FascicoloGPModel) obj[1];
					// info per il log
					avvocaturaLogger.info("Trattasi di decreto di revoca? " + ddmRevoca != null ? "SI" : "NO");
					avvocaturaLogger.info("Esiste fascicolo di revoca? " + fgpmRevoca != null ? "SI" : "NO");
					if (ddmRevoca != null)
						isDecretoDiRevoca = true;
				}

				// Valorizzazione dell'id evento nel model.
				ddemm.getEvento().setIdEvento(idEv);
			}
	
			// Ricerca dei tenori collegati al decreto.
			tenori = iTenore.ExRicercaTenoreByDecreto(ddm.getIdDepositoDecreto());
	
			// Carica il magistrato per evento.
			mrm = gestioneMagistratoByEvento(idEv);
	
			// Ricerca avvocati assegnati al fascicolo
			difensori = iAvvocato.ExRicercaAvvocatiByFascicoloNoError(fgpm.getFascicoloSiusModel()
					.getIdFascicoloSius());
		}

		// Collection statoLibertatis = DecodificheManager.getInstance().getStatoLibertatis();
		BigDecimal idUdienza = null;
		Vector prescrizioni = null;
		Vector permessi = null;
		Vector licenze = null;
		LicenzaLibAnticipataModel revoca = null;
		Vector destinatari = null;
		CuratoreSiusModel csm = null;
		Vector permessiRevocati = null;
		Vector licenzePeriodi = null;
		PeriodoAltraSanzioneModel pasm = null;
		PeriodoAltraMisuraModel pamm = null;
		boolean isDecretoRevocato = false;
		boolean isFascicoloOrigineRevocato = false;

		// Ricerca delle notifiche (destinatari)
		INotifica iNotifica = SIEPLookupRemote.getNotificaRemote();
		
		// MEV_2023-35 si aggiunge un nuovo codice per il decreto generico (GENERICO2=GE)
		if (GENERICO.equals(tipoDecreto) || GENERICO2.equals(tipoDecreto)
				|| ICostantiDepositoOrdinanzaPc.RICHIESTA_OTTEMPERANZA.equals(tipoDecreto)
				|| LIMITAZIONI_CONTROLLI_CORRISPONDENZA.equals(tipoDecreto)
				|| APPLICAZIONE_PROVVISORIA_MA.equals(tipoDecreto)
				|| MODIFICA_PRESCRIZIONI.equals(tipoDecreto) || AUTORIZZAZIONE_MA.equals(tipoDecreto)
				|| AUTORIZZAZIONE_SS.equals(tipoDecreto) || AUTORIZZAZIONE_MS.equals(tipoDecreto)
				|| MODIFICA_PRESCRIZIONI_MS.equals(tipoDecreto)
				|| DEC_INOSSERVANZA_OBBLIGHI_MS.equals(tipoDecreto)) {
			prescrizioni = ricercaPrescrizioni(ddm.getIdEventoGenerato());
		} else if (CITAZIONE.equals(tipoDecreto)) {
			// evento
			EventoNotificaModel enm = iEvento.ExRicercaEventoNotificaByKey(idEv);
			// udienza procedimento
			IUdienzaProcedimento iup = SIUSLookupRemote.getUdienzaProcedimentoRemote();
			UdienzaProcedimentoModel upm = iup.ExRicercaUdienzaProcedimentoByEve(idEv);
			if (upm != null)
				idUdienza = upm.getUdiIdUdienza();
			// fascicolo
			BigDecimal idFascicolo = enm.getEvento().getFasSiuIdFascicoloSius();
			IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
			fgpm = ifs.ExRicercaFascicoloByKey(idFascicolo);
			// udienza
			if (idUdienza == null)
				idUdienza = fgpm.getGeneraleProcedimentoModel().getUdiIdUdienza();
			um = new UdienzaModel();
			IUdienza iUdienza = SIUSLookupRemote.getUdienzaRemote();
			um = iUdienza.ExRicercaUdienzaByKey(idUdienza);
			// avvocato
			AvvocatoFascicoloSiusModel afsm = new AvvocatoFascicoloSiusModel();
			afsm.setFasSiuIdFascicoloSius((fgpm.getFascicoloSiusModel()).getIdFascicoloSius());
			difensori = iAvvocato.ExRicercaAvvocatiAttualiFascicolo(null, afsm);
			// magistrato relatore
			IMagistratoRelatore imr = SIUSLookupRemote.getMagistratoRelatoreRemote();
			mrm = imr.ExRicercaEstesaMagRelByFascicolo(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
			// notifiche
			destinatari = iNotifica.ExRicercaEstesaNotificaByKeyEvento(idEv);
			// deposito decreto
			ddm = idd.ExRicercaDepositoDecretoByIdEvento(enm.getEvento().getIdEvento());
			// tenori
			tenori = iTenore.ExRicercaTenoreByDecreto(ddm.getIdDepositoDecreto());
			// oggetti
			// String oggetti = new String();
			// if (fgpm.getTenori() != null) {
			// for (int i = 0; i < fgpm.getTenori().length; i++) {
			// oggetti += fgpm.getTenori()[i].getDescrOggettoTenore() + "\n";
			// }
			// }
			// curatori
			csm = ricercaCuratori(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
		} else if (UNIFICAZIONE.equals(tipoDecreto)) {
			// info per il log
			avvocaturaLogger.debug("Trattasi di Decreto di Unificazione Unificato");
			// Lettura dei fascicoli SIUS Unificante e Unificato.
			fascicoloUnificato = ifss.ExRicercaFascicoloByKey(em.getFasSiuIdFascicoloSius());
			fascicoloUnificante = ifss.ExRicercaFascicoloByKey(fascicoloUnificato.getFascicoloSiusModel()
					.getFasSiuIdFascicoloSius());
		} else if (IRREPERIBILITA.equals(tipoDecreto)) {
			// BigDecimal lIdGenProcredimento =
			// fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
			// BigDecimal aIdFascicoloSius = fgpm.getFascicoloSiusModel().getIdFascicoloSius();
			ddemm = idd.ExRicercaDecretoEventoMotivazioniIncompetenzaByIdEvento(idEv);
			ddm = ddemm.getDepositoDecreto();
			ddemm.getEvento().setIdEvento(idEv);
			tenori = iTenore.ExRicercaTenoreByDecreto(ddemm.getDepositoDecreto().getIdDepositoDecreto());
			IMagistratoRelatore imr = SIUSLookupRemote.getMagistratoRelatoreRemote();
			mrm = imr.ExRicercaEstesaMagRelByFascicolo(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
			// AvvocatoSiusModel asm =
			// iAvvocato.ExRicercaAvvocatoByKeyAvvocatoFasSius(fgpm.getFascicoloSiusModel()
			// .getIdFascicoloSius());
			// IStampaSius iss = SIUSLookupRemote.getStampaRemote();
			// int[] tipoDati = {ICostantiStampaSius.TREE_AVVOCATO};
			// TreeModel tm = iss.ExPrelevaDatiVideo(aIdFascicoloSius, tipoDati);
			// ParserMessageRec pmr = new ParserMessageRec(tm);
			// List avvocati = pmr.getAvvocato();
		} else if (PERMESSO.equals(tipoDecreto) || LICENZA.equals(tipoDecreto)) {
			licenze = ricercaPermessi(ddm.getIdEventoGenerato());
			permessi = ricercaPermessi(ddm.getIdEventoGenerato());
			prescrizioni = ricercaPrescrizioni(ddm.getIdEventoGenerato());
		} else if (INOSSERVANZA_OBBLIGHI.equals(tipoDecreto) || SOPRAVVENIENZA_NT.equals(tipoDecreto)) {
			if (ddm.getIstDetIdIstitutoDetenzione() != null
					&& !"-".equals(ddm.getIstDetIdIstitutoDetenzione())) {
				IIstitutoDetenzione iid = SIEPLookupRemote.getIstitutoDetenzioneRemote();
				idm = iid.ExRicercaIstitutoDetenzioneByKey(ddm.getIstDetIdIstitutoDetenzione());
			}
		} else if (REVOCA_PERMESSO.equals(tipoDecreto) || REVOCA_LICENZA.equals(tipoDecreto)
				|| ESCLUSIONE_COMPUTO.equals(tipoDecreto) || ESCLUSIONE_COMPUTO_LICENZA.equals(tipoDecreto)) {
			permessi = ricercaPermessi(ddm.getIdEventoGenerato());

			if (REVOCA_PERMESSO.equals(tipoDecreto) && permessi != null && permessi.size() > 0)
				revoca = (LicenzaLibAnticipataModel) permessi.get(0);

			prescrizioni = ricercaPrescrizioni(ddm.getIdEventoGenerato());
			// Ricerca del decreto Revocato
			Object[] rev = new Object[3];
			if (ddemm.getEvento().getEveIdEvento() != null) {
				rev = ricercaDecretoDiRiferimento(ddemm.getEvento().getEveIdEvento(), idd);
				ddmRevocato = (DepositoDecretoModel) rev[0];
				permessiRevocati = (Vector) rev[1];
				fgpmRevocato = (FascicoloGPModel) rev[2];
				if (ddmRevocato != null)
					isDecretoRevocato = true;
			}
		} else if (REVOCA_DECRETO.equals(tipoDecreto)) {
			Object[] rev = new Object[3];
			if (ddemm.getEvento().getEveIdEvento() != null) {
				rev = ricercaDecretoRevocato(ddemm.getEvento().getEveIdEvento(), idd);
				ddmRevocato = (DepositoDecretoModel) rev[0];
				permessiRevocati = (Vector) rev[1];
				fgpmOrigineRevocato = (FascicoloGPModel) rev[2];
				if (fgpmOrigineRevocato != null)
					isFascicoloOrigineRevocato = true;
				if (ddmRevocato != null)
					isDecretoRevocato = true;
			}
		} else if (ICostantiDepositoOrdinanzaPc.REVOCA_LIBERAZIONE_ANTICIPATA.equals(tipoDecreto)
				|| ICostantiDepositoOrdinanzaPc.VIOLAZIONE_CEDU.equals(tipoDecreto)) {
			ILicenzaPeriodiLibAnticipata ilpla = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
			licenzePeriodi = ilpla.ExRicercaLicenzeLibanticipataByEve(idEv);
		} else if (SOSPENSIONE_ESECUZIONE_SS.equals(tipoDecreto)) {
			pasm = ricercaPeriodoAltraSanzione(ddemm.getEvento().getIdEvento());
		} else if (REVOCA_AUTORIZZAZIONE_SS.equals(tipoDecreto)) {
			// Ricerca Fascicolo origine
			if (fgpm.getFascicoloSiusModel() != null
					&& fgpm.getFascicoloSiusModel().getIdFascicoloSiusOrigine() != null)
				fgpmOrigine = ricercaFascicoloSIUS(fgpm.getFascicoloSiusModel().getIdFascicoloSiusOrigine());
		} else if (SOSPENSIONE_ESECUZIONE_MS.equals(tipoDecreto)) {
			pamm = ricercaPeriodoAltraMisura(ddemm.getEvento().getIdEvento());
		}

		// se presente, aggiorno la tabella degli avvisi
		if (PropertyUtil.isPresent(datiAvviso)) {
			IAvvisiSius ias = SIUSLookupRemote.getAvvisiSiusRemote();
			ias.aggiornaAvvisiAvvocato(new BigDecimal(datiAvviso.getIdAvviso()));
		}

		// info per il log
		avvocaturaLogger.debug("Valorizzo l'oggetto di output: OUTPUTDETTAGLIODECRETO");
		// copia dei dati dal model al type
		OUTPUTDETTAGLIODECRETO odd = copyModelToType(ddemm, tenori, mrm, fgpm, fspm, isDecretoDiRevoca,
				permessi, prescrizioni, difensori, destinatari, csm, fgpmRevoca, ddmRevoca, permessiRevocati,
				fgpmOrigine, um, isDecretoRevocato, ddmRevocato, fgpmRevocato, isFascicoloOrigineRevocato,
				fgpmOrigineRevocato, idm, licenze, revoca, licenzePeriodi, pasm, fascicoloUnificante,
				fascicoloUnificato, pamm);

		// valore di ritorno
		return odd;
	}

	/**
	 * Metodo per la ricerca di altre misure
	 * 
	 * @param idEvento
	 * @return PeriodoAltraMisuraModel
	 * @throws F3BException
	 */
	private PeriodoAltraMisuraModel ricercaPeriodoAltraMisura(BigDecimal idEvento) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioDecretoSiusAction, metodo: ricercaPeriodoAltraMisura");

		// Ricerca il Periodo Altra Misura tramite l'ID dell'evento
		IPeriodoAltraMisura ipam = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
		PeriodoAltraMisuraModel pamm = ipam.ExRicercaMisuraSicurezzaByIdEvento(idEvento);

		// valore di ritorno
		return pamm;
	}

	/**
	 * Metodo per la ricerca del periodo altra senzione
	 * 
	 * @param idEvento
	 * @return PeriodoAltraSanzioneModel
	 * @throws F3BException
	 */
	private PeriodoAltraSanzioneModel ricercaPeriodoAltraSanzione(BigDecimal idEvento) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioDecretoSiusAction, metodo: ricercaPeriodoAltraSanzione");

		// Ricerca il Periodo Altra Sanzione tramite l'ID dell'evento
		IPeriodoAltraSanzione ipas = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
		PeriodoAltraSanzioneModel pasm = ipas.ExRicercaSanzioneSostitutivaByIdEvento(idEvento);

		// valore di ritorno
		return pasm;
	}

	/**
	 * Metodo per la ricerca del curatore
	 * 
	 * @param idFascicoloSius
	 * @return
	 * @throws F3BException
	 */
	private CuratoreSiusModel ricercaCuratori(BigDecimal idFascicoloSius) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioDecretoSiusAction, metodo: ricercaCuratori");

		// curatore Sius
		CuratoreSiusModel csm = null;
		ICuratoreSius ics = SIUSLookupRemote.getCuratoreSiusRemote();
		csm = ics.ExRicercaCurSiusByFascicolo(idFascicoloSius);
		if (csm != null) {
			if (csm.getCurIdCuratore() != null) {
				ICuratore iCuratore = SIGELookupRemote.getCuratoreRemote();
				CuratoreModel cm = iCuratore.ExRicercaCuratoreByKey(csm.getCurIdCuratore());
				csm.setCuratore(cm);
			}
		}
		// valore di ritorno
		return csm;
	}

	/**
	 * Metodo per la ricerca dei permessi
	 * 
	 * @param idEventoGenerato
	 * @return Vector
	 * @throws F3BException
	 */
	private Vector ricercaPermessi(BigDecimal idEventoGenerato) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioDecretoSiusAction, metodo: ricercaPermessi");

		// Ricerca Permessi
		ILicenzaPeriodiLibAnticipata ilpla = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		Vector permessi = null;
		try {
			permessi = ilpla.ExRicercaLicenzeByEve(idEventoGenerato);
		} catch (Exception e) {
			avvocaturaLogger.debug("Nessun Elemento Licenza Anticipata trovato");
			// creo vettore vuoto
			permessi = new Vector();
		}
		// valore di ritorno
		return permessi;
	}

	/**
	 * Metodo per la ricerca delle prescrizioni
	 * 
	 * @param idEventoGenerato
	 * @return Vector
	 * @throws F3BException
	 */
	private Vector ricercaPrescrizioni(BigDecimal idEventoGenerato) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioDecretoSiusAction, metodo: ricercaPrescrizioni");

		Vector prescrizioni = null;
		// Ricerca Prescrizioni
		IPrescrizione iPrescrizione = SIUSLookupRemote.getPrescrizioneRemote();
		try {
			prescrizioni = iPrescrizione.ExRicercaPrescrizioneByEvento(idEventoGenerato);
		} catch (Exception e) {
			avvocaturaLogger.debug("Nessun Elemento Prescrizione trovato");
			// creo vettore vuoto
			prescrizioni = new Vector();
		}
		// valore di ritorno
		return prescrizioni;
	}

	/**
	 * Metodo per la ricerca dati magistrato
	 * 
	 * @param idEv
	 * @return
	 * @throws Exception
	 */
	private MagistratoRelatoreModel gestioneMagistratoByEvento(BigDecimal idEv) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioDecretoSiusAction, metodo: gestioneMagistratoByEvento");

		IMagistrato iMagistrato = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel mm = iMagistrato.ExRicercaMagistratoByEvento(idEv);
		MagistratoRelatoreModel mrm = new MagistratoRelatoreModel();
		mrm.setMagistrato(mm);

		// valore di ritorno
		return mrm;
	}

	/**
	 * Metodo per la ricerca di un decreto di revoca
	 * 
	 * @param eveIdEventoRevoca
	 * @param idd
	 * @return Object[]
	 * @throws Exception
	 */
	private Object[] ricercaDecretoDiRevoca(BigDecimal idEventoRevoca, IDepositoDecreto idd)
			throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioDecretoSiusAction, metodo: ricercaDecretoDiRevoca");

		DepositoDecretoEventoMotivazioniModel ddemm = idd
				.ExRicercaDecretoEventoMotivazioniInammissibilitaByIdEvento(idEventoRevoca);

		// Ricerca Fascicolo di revoca
		FascicoloGPModel fgpm = null;
		DepositoDecretoModel ddm = null;
		if (ddemm != null && ddemm.getDepositoDecreto() != null) {
			ddm = ddemm.getDepositoDecreto();
			if (ddemm.getEvento() != null && ddemm.getEvento().getFasSiuIdFascicoloSius() != null) {
				fgpm = ricercaFascicoloSIUS(ddemm.getEvento().getFasSiuIdFascicoloSius());
			}
		}
		Object[] obj = new Object[2];
		obj[0] = ddm;
		obj[1] = fgpm;

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
	private FascicoloGPModel ricercaFascicoloSIUS(BigDecimal fasSiuIdFascicoloSius) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioDecretoSiusAction, metodo: ricercaFascicoloSIUS");

		FascicoloGPModel lFascicolo = null;
		IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
		lFascicolo = ifs.ExRicercaFascicoloByKey(fasSiuIdFascicoloSius);

		// valore di ritorno
		return lFascicolo;
	}

	/**
	 * Metodo per la valorizzazione del type di output
	 * 
	 * @param ddemm
	 * @param tenori
	 * @param mrm
	 * @param fgpm
	 * @param fsm
	 * @param isDecretoDiRevoca
	 * @param permessi
	 * @param prescrizioni
	 * @param difensori
	 * @param destinatari
	 * @param csm
	 * @param fgpmRev
	 * @param ddmRev
	 * @param permessiRev
	 * @param fgpmOrigine
	 * @param um
	 * @param isDecretoRevocato
	 * @param ddmRevocato
	 * @param fgpmRevocato
	 * @param isFascicoloOrigineRevocato
	 * @param fgpmOrigineRevocato
	 * @param idm
	 * @param licenze
	 * @param revoca
	 * @param licenzePeriodi
	 * @param pasm
	 * @param fascicoloUnificante
	 * @param fascicoloUnificato
	 * @param pamm
	 * @return OUTPUTDETTAGLIODECRETO
	 * @throws Exception
	 */
	private OUTPUTDETTAGLIODECRETO copyModelToType(DepositoDecretoEventoMotivazioniModel ddemm,
			Vector tenori, MagistratoRelatoreModel mrm, FascicoloGPModel fgpm, FascicoloSiepModel fsm,
			boolean isDecretoDiRevoca, Vector permessi, Vector prescrizioni, Vector difensori,
			Vector destinatari, CuratoreSiusModel csm, FascicoloGPModel fgpmRevoca,
			DepositoDecretoModel ddmRevoca, Vector permessiRev, FascicoloGPModel fascicoloOrigine,
			UdienzaModel um, boolean isDecretoRevocato, DepositoDecretoModel ddmRevocato,
			FascicoloGPModel fgpmRevocato, boolean isFascicoloOrigineRevocato,
			FascicoloGPModel fgpmOrigineRevocato, IstitutoDetenzioneModel idm, Vector licenze,
			LicenzaLibAnticipataModel revoca, Vector licenzePeriodi, PeriodoAltraSanzioneModel pasm,
			FascicoloGPModel fascicoloUnificante, FascicoloGPModel fascicoloUnificato, PeriodoAltraMisuraModel pamm)
			throws Exception {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioDecretoSiusAction, metodo: copyModelToType");

		// instanzio ed inizializzo un oggetto di tipo "OUTPUTDETTAGLIODECRETO"
		OUTPUTDETTAGLIODECRETO odd = new OUTPUTDETTAGLIODECRETO();

		try {
			// controllo preventivo
			DATIDECRETO dd = DecretoMapper.mapDatiDecreto(ddemm, fgpmRevoca, ddmRevoca, isDecretoDiRevoca,
					um, isDecretoRevocato, ddmRevocato, fgpmRevocato, isFascicoloOrigineRevocato,
					fgpmOrigineRevocato, permessiRev, idm, licenze, revoca, licenzePeriodi, pasm,
					fascicoloOrigine, fascicoloUnificante,fascicoloUnificato, fgpm, pamm);
			odd.setDATIDECRETO(dd);

			DATIRIEPILOGOPROCEDIMENTO drp = DecretoMapper.mapDatiRiepilogoProcedimento(fgpm, fsm, mrm);
			odd.setDATIRIEPILOGOPROCEDIMENTO(drp);

			List<DESTINATARIOTYPE> ldt = DecretoMapper.mapDestinatari(destinatari, csm);
			if (ldt != null)
				odd.getElencoDestinatari().addAll(ldt);

			List<TENORETYPE> ltt = DecretoMapper.mapTenori(tenori);
			if (ltt != null)
				odd.getElencoEsiti().addAll(ltt);

			if (ddemm != null) {
				List<MOTIVAZIONITYPE> lmt = DecretoMapper.mapMotivazioni(ddemm.getMotivazioniDecreto());
				if (lmt != null)
					odd.getElencoMotivazioniDecreto().addAll(lmt);
			}

			List<PERMESSOTYPE> lpt = DecretoMapper.mapPermessi(permessi);
			if (lpt != null)
				odd.getElencoPermessi().addAll(lpt);

			List<PRESCRIZIONETYPE> lrt = DecretoMapper.mapPrescrizioni(prescrizioni);
			if (lrt != null)
				odd.getElencoPrescrizioni().addAll(lrt);

			List<AVVOCATOTYPE> lat = DecretoMapper.mapDifensori(difensori);
			if (lat != null)
				odd.getListaAvvocati().addAll(lat);

			odd.setDatiRevocaLibertaAnticipata(DecretoMapper.mapDatiRevocaLibertaAnticipata(licenzePeriodi));

			// imposto l'ERRORE
			odd.setERRORE(Mapper.mapErroreDecreto("000", ""));
		} catch (Exception e) {
			// info per il log
			avvocaturaLogger.error("Errore nella mappatura dei dati del decreto: " + e.getMessage(), e);
			// imposto l'ERRORE
			odd.setERRORE(Mapper.mapErroreDecreto("004", e.getMessage()));
		}

		// valore di ritorno
		return odd;
	}

	/**
	 * Metodo per la ricerca di dati di revoca
	 * 
	 * @param idEvento
	 * @param idd
	 * @return
	 * @throws Exception
	 */
	private Object[] ricercaDecretoDiRiferimento(BigDecimal idEvento, IDepositoDecreto idd)
			throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioDecretoSiusAction, metodo: ricercaDecretoDiRiferimento");

		DepositoDecretoFascicoloModel ddfm = idd.ExRicercaDecretoFascicoloByIdEvento(idEvento);
		Object[] rev = new Object[3];

		if (ddfm != null) {
			DepositoDecretoModel decretoRev = ddfm.getDepositoDecreto();
			// Ricerca dei permessi
			Vector permessiRev = ricercaPermessi(idEvento);
			FascicoloSiusModel fsm = ddfm.getFascicolo();
			FascicoloGPModel fgpmRev = null;
			if (fsm != null)
				fgpmRev = ricercaFascicoloSIUS(fsm.getIdFascicoloSius());
			rev[0] = decretoRev;
			rev[1] = permessiRev;
			rev[2] = fgpmRev;
		}
		// valore di ritorno
		return rev;
	}

	/**
	 * Metodo per la ricerca del decreto revocato
	 * 
	 * @param idEvento
	 * @param idd
	 * @return Object[]
	 * @throws Exception
	 */
	private Object[] ricercaDecretoRevocato(BigDecimal idEvento, IDepositoDecreto idd) throws F3BException {

		// info per il log
		avvocaturaLogger.debug("Starting Point della classe: DettaglioDecretoSiusAction, metodo: ricercaDecretoRevocato");

		DepositoDecretoEventoMotivazioniModel ddemm = idd
				.ExRicercaDecretoEventoMotivazioniInammissibilitaByIdEvento(idEvento);
		Object[] rev = new Object[3];
		FascicoloGPModel fgpmOrigine = null;

		if (ddemm != null && ddemm.getDepositoDecreto() != null) {
			rev[0] = ddemm.getDepositoDecreto();
			rev[1] = ricercaPermessi(idEvento);
			if (ddemm.getEvento() != null && ddemm.getEvento().getFasSiuIdFascicoloSius() != null) {
				fgpmOrigine = new FascicoloGPModel();
				fgpmOrigine = ricercaFascicoloSIUS(ddemm.getEvento().getFasSiuIdFascicoloSius());
			}
			rev[2] = fgpmOrigine;
		}
		// valore di ritorno
		return rev;
	}

}