package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.EventoVerbaleModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DecretoEventoTenoriFascicoloSiusModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.ProvvedimentoEventoTenoreFascicoloSiusModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * MEV_39
 * <p>
 * Title: ActLoadInserisciOLDifferimento
 * </p>
 * <p>
 * Description: Classe la load Emissione O.L. per Differimento
 * </p>
 * <p>
 * per esecuzione Misura di Sicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: EII
 * </p>
 *
 * @author SGIOGGI
 * @version 1.0
 *
 */
public class ActLoadInserisciOLDifferimento extends ActionSiap implements ICostantiMisuraSicurezza {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		if (isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		setRequestAttribute("idfascicolo", fsm.getIdFascicoloSiep() + "");

		// CONTROLLI SUGLI ELEMENTO DEL FASCICOLO PER POTER ESEGUIRE PROVVEDIMENTI
		// Funzione di esclusiva competenza dei proc. di classe IV.
		if (fsm.getChiaveProgr().intValue() < 40000 || fsm.getChiaveProgr().intValue() >= 50000) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Operazione consentita solo per procedimento di classe IV!");
			return IWebConstants.PG_MESSAGE;
		}

		if (fsm.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + fsm.getChiaveAnno() + "/" + fsm.getChiaveProgr()
							+ " non è stato Validato. Impossibile inserire una Misura di Sicurezza!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		isFascicoloSiepDiCompetenza();

		if (fsm.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr() + " Il fascicolo risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		isEventoNonValidato();

		// Controllo Esistenza pena residua non validata per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(fsm.getIdFascicoloSiep());

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		Date lDataInizioPena = null;
		// Date lDataFinePenaA = null;
		if (lPenaResMod != null) {
			lDataInizioPena = lPenaResMod.getDataInizio();
			// lDataFinePenaA = lPenaResMod.getDataFinePresunta();
		}
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		// setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyy"));
		setRequestAttribute("penaresidua", lPenaResMod);

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		try {
			Vector lAvvVect = lAvv.ExRicercaAvvocatiByFascicolo(fsm.getIdFascicoloSiep());
			setRequestAttribute("avvocati", lAvvVect);
		} catch (SIEPException e) {
			// NON Rilancio l'eccezione: Deve passare anche in assenza del Difensore
			siesLogger.info(
					"NON CI SONO AVVOCATI DIFENSORI LEGATI AL FASCICOLO SIEP: " + fsm.getIdFascicoloSiep());
		}

		// Posizione Giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				fsm.getIdFascicoloSiep());

		if (pgldacm == null || pgldacm.getPosizioneGiuridica() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("posizioneluogoaltra", pgldacm);

		// Ricerca Misure sicurezza gia' presenti nel fascicolo : Sono ORDINATE per DATA_INSERIMENTO
		List lListMis = new ArrayList();
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(fsm.getIdFascicoloSiep());
		// prendo solo l'ultima MISURA (se esiste)
		// MisuraSicurezzaModel lMisSicMod = null;
		if (lListMis.size() > 0)
			// lMisSicMod = (MisuraSicurezzaModel) lListMis.get(lListMis.size() - 1);
			siesLogger.info("Ultima MISURA su: " + lListMis.size());
		else {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Procedimento privo di Misura di Sicurezza, impossibile procedere!");
			return IWebConstants.PG_MESSAGE;
		}

		// setRequestAttribute("MisuraModel", lMisSicMod);
		setRequestAttribute("listaMisure", lListMis);

		// Magistrato
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(fsm.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// Riempimento ComboBox autorita destinatario per esecuzione
		Option lOptionE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaE", "" + lOptionE);

		// Riempimento ComboBox autorita destinatario notifica Difensore
		Option lOptiona = new Option(DecodificheManager.getInstance().getTipoAutorita());
		String[] lFiltroAutEst = { "22", "C0" };
		lOptiona.setFilter(lFiltroAutEst);
		setRequestAttribute("autoritaEsterna", "" + lOptiona);

		// Aggiunto Destinatario Sorveglianza
		Option lOptionSor = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOptionSor.setFilter(new String[] { "-", "TDS", "UDS", "UDSM" });
		setRequestAttribute("tipoUDS", "" + lOptionSor);
		
		//
		Vector depoDec = new Vector<OrdinanzaEventoTenoriFascicoloSiusModel>();
		// Dati PROVVEDIMENTO SPECIFICO SIUS
		ProvvedimentoEventoTenoreFascicoloSiusModel petfsm = new ProvvedimentoEventoTenoreFascicoloSiusModel();

		// se provengo dall'ANNOTAZIONE DECISIONE SORVEGLIANZA, carico i dati del provvedimento SIUS
		if (!isRequestParameterNullObj("from") && "dads".equals(getRequestStringParameter("from"))) {
			// Lista PROVV. SIUS
			IPeriodoAltraMisura ipam = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
			Vector provvedimenti = ipam.ExRicercaProvvedimentoEventoByFascicoloSiep(fsm.getIdFascicoloSiep());
			// 20190506 [SG]: aggiunto codice per gestire casistica particolare
			// MEV_39 ***** inizio *****
			// se il fascicolo di classe I è legato ad un fascicolo di classe IV recupero i provvedimenti
			// SIUS legatial fascicolo di classe IV
			IMisuraSicurezza ims = SIEPLookupRemote.getMisuraSicurezzaRemote();
			BigDecimal fasSiefascCollegato = new BigDecimal(0);
			Vector vectFasIV = new Vector();
			vectFasIV = ims.ExRicercaFascicoliCollegati(fsm.getIdFascicoloSiep());
			if (vectFasIV.size() > 0) {
				Iterator iteIV = vectFasIV.iterator();
				while (iteIV.hasNext()) {
					FascMsToFascSiepModel fascMSMod = (FascMsToFascSiepModel) iteIV.next();
					fasSiefascCollegato = fascMSMod.getFasSieIdFascicoloCollegato();
				}
			}
			if (vectFasIV.size() > 0) {
				Vector provvSIUS_classeIV = new Vector();
				provvSIUS_classeIV = ipam.ExRicercaProvvedimentoEventoByFascicoloSiep(fasSiefascCollegato);
				if (!provvSIUS_classeIV.isEmpty())
					provvedimenti.addAll(provvSIUS_classeIV);
			}
			// MEV_39 ***** fine *****
			// EventoNotifica (Provvedimento di annotazione Inserito)
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			EventoNotificaModel lEveMod = lCtrl.ExRicercaEventoNotificaByKey(
					getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			setRequestAttribute("eventonotifica", lEveMod);
			// Dati PROVVEDIMENTO SPECIFICO SIUS
			petfsm = new ProvvedimentoEventoTenoreFascicoloSiusModel();
			Iterator iter = provvedimenti.iterator();
			while (iter.hasNext()) {
				petfsm = (ProvvedimentoEventoTenoreFascicoloSiusModel) iter.next();
				if (petfsm.getEvento().getIdEvento().equals(lEveMod.getEvento().getEveIdEvento()))
					break;
			}

			// selezione delle varie combo
			// if ("Tribunale di Sorveglianza".equalsIgnoreCase(petfsm.getDescrTipoUfficio()))
			// a.setSelected("TDS");
			// else if
			// ("Tribunale per i minorenni in funzione di Tribunale
			// Sorveglianza".equalsIgnoreCase(petfsm.getDescrTipoUfficio()))
			// a.setSelected("TDSM");
			// else if ("Ufficio di Sorveglianza".equalsIgnoreCase(petfsm.getDescrTipoUfficio()))
			// a.setSelected("UDS");
			// else if
			// ("Ufficio di Sorveglianza presso il Tribunale per
			// minorenni".equalsIgnoreCase(petfsm.getDescrTipoUfficio()))
			// a.setSelected("UDSM");
			// et.setSelected(petfsm.getEvento().getCodEsito());
			// mp.setSelected(petfsm.getEvento().getCodMotivo());
			// tp.setSelected(petfsm.getEvento().getCodTipoProvvedimento());

			// MEV 39 [EC] 04/06/2019 - Cerco in CG_REF_CODES IL CAMPO RV_HIGH_VALUE PER
			// DOMINIO=MOTIVO_PROVVEDIMENTO E COME RV_LOW_VALUE = COD_MOTIVO DELL'EVNTO
			// QUESTO PER CAPIRE SE IL CONTENUTO SIUS è UNO TRA I SEGUETNI (U077, U082 O C036)
			// PERCHè SOLO PER QUESTI CONTENUTI DEVE ESSERE POSSIBILE INSERIRE UN ORDINE DI LIBERAZIONE PER
			// DIFFERIMENTO DELLA MS
			if (petfsm != null && petfsm.getEvento() != null) {
				DecodificheModel lModel = new DecodificheModel();
				IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
				lModel.setContesto("MOTIVO_PROVVEDIMENTO");
				lModel.setCode(petfsm.getEvento().getCodMotivo());
				Vector lVec = new Vector(lDecodifiche.ExRicercaDecodifiche(lModel));
				if (lVec.size() > 0) {
					DecodificheModel dec = (DecodificheModel) lVec.get(0);
					String[] codContenuti = { "C036", "U077", "U082" };
					// inoltre si prosegue con l'inserimento in scadenzario solo per questi contenuto
					if (Arrays.binarySearch(codContenuti, dec.getCodiceAlternativo()) < 0) {
						setRequestAttribute(IWebConstants.MESSAGE_TEXT,
								"Provvedimento della Sorveglianza non compatibile per emissione di Ordine di Liberazione per Differimento, impossibile procedere!");
						return IWebConstants.PG_MESSAGE;
					}
				}
				setRequestAttribute("provvedimento", petfsm);
			} else // 20190506 [SG]: aggiunto metodo per gestire casistica particolare
				caricaOptionsVuote();
		} else {
			// Dati ORDINANZA SIUS
			IDepositoOrdinanzaPc lDepoCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			Vector depoOrdinanze = lDepoCtrl
					.ExRicercaEventoProvvedimentiDifferimentoSIUSByFascicoloSiep(fsm.getIdFascicoloSiep());
			// if (depoOrdinanze.isEmpty()) {
			// setRequestAttribute(
			// IWebConstants.MESSAGE_TEXT,
			// "NON ci sono Provvedimenti SIUS associati al Procedimento N. "
			// + fsm.getChiaveAnno() + "/" + fsm.getChiaveProgr());
			// return IWebConstants.PG_MESSAGE;
			// }
			if (!depoOrdinanze.isEmpty()) {
				OrdinanzaEventoTenoriFascicoloSiusModel oetfsm = (OrdinanzaEventoTenoriFascicoloSiusModel) depoOrdinanze
						.lastElement();
	            setRequestAttribute("ordinanza", oetfsm);
			} else if(depoOrdinanze.isEmpty()){
				// se vuota, vuol dire che probabilmente sta arrivando un DECRETO
				// Dati DECRETO SIUS
				IDepositoDecreto lDepoDecCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
				depoDec = lDepoDecCtrl
						.ExRicercaEventoProvvedimentiDifferimentoSIUSByFascicoloSiep(fsm.getIdFascicoloSiep());
				if(!depoDec.isEmpty()){
					DecretoEventoTenoriFascicoloSiusModel oetfsm = (DecretoEventoTenoriFascicoloSiusModel) depoDec
							.lastElement();
					setRequestAttribute("decreto", oetfsm);
				}
				else
					caricaOptionsVuote();
			}			
			
		}

		// MEV_39: DEVO RECUPERARE LA STRUTTURA DESIGNATA se esiste un evento di DESIGNAZIONTE ISTITUTO
		EventoModel lEveDaRicercare = new EventoModel();
		lEveDaRicercare.setCodTipoEvento("01");	
		lEveDaRicercare.setCodTipoProvvedimento("25");
		lEveDaRicercare.setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel eventoDesignazioneIStituto = lCtrlEvento
				.ExRicercaEventoPerMotivo(new String[] { "1130" }, lEveDaRicercare);
		
		if(eventoDesignazioneIStituto!= null &&  !"A".equals(eventoDesignazioneIStituto.getFlagDocumentoRegistrato()) ){
			// EventoVerbale DesignazioneIStituto
			EventoVerbaleModel lEveVerMod = new EventoVerbaleModel();
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lEveVerMod = lCtrl.ExRicercaEventoVerbaleByIdEve(eventoDesignazioneIStituto.getIdEvento());			
			IstitutoDetenzioneModel strutturaDesignata = new IstitutoDetenzioneModel();
			if (lEveVerMod != null && lEveVerMod.getVerbale() != null
					&& lEveVerMod.getVerbale().getIstDetIdIstitutoDetenzione() != null
					&& !lEveVerMod.getVerbale().getIstDetIdIstitutoDetenzione().equals("-")) {
				IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
				strutturaDesignata = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lEveVerMod.getVerbale()
						.getIstDetIdIstitutoDetenzione());
			}
			setRequestAttribute("strutturaDesignataModel", strutturaDesignata);
		}
		
		if(petfsm!=null && petfsm.getEvento()!=null && "02".equals(petfsm.getEvento().getCodTipoProvvedimento())){
			// restituisce la jsp di VIEW
			return PG_LOAD_INS_OE_DIFFERIMENTO_DEC;
		}
		else if(!depoDec.isEmpty()){
			// restituisce la jsp di VIEW
			return PG_LOAD_INS_OE_DIFFERIMENTO_DEC;
		}
		else{
			// restituisce la jsp di VIEW
			return PG_LOAD_INS_OE_DIFFERIMENTO;
		}
	} // CHIUDE processRequest

	// 20190506 [SG]: aggiunto metodo per gestire casistica particolare
	private void caricaOptionsVuote() throws Exception {

		// recupero records x la varie combo
		Option mp = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
		mp.setFilter(new String[] { "-", "0428", "0429", "0430", "0431", "0432", "0433" });
		Option et = new Option(DecodificheManager.getInstance().getEsitoProvvedimento());
		et.setFilter(new String[] { "-", "0002", "0003", "0004", "0005", "0035", "0119", "0145", "0360" });
		// Oggetti per il caricamento della combo Autorità Emittente
		Option a = new Option(DecodificheManager.getInstance().getTipoUfficio());
		a.setFilter(new String[] { "-", "TDS", "TDSM", "UDS", "UDSM" });
		// Oggetti per il caricamento delle combo Tipo Provvedimento
		Option tp = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza());
		// imposto gli attributi nelle richiesta
		setRequestAttribute("autorita", "" + a);
		setRequestAttribute("et", "" + et);
		setRequestAttribute("mp", "" + mp);
		setRequestAttribute("tipoprovvedimento", "" + tp);
	}

} // CHIUDE Classe ActLoad...