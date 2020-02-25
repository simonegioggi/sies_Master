package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.EventoVerbaleModel;
import siap.sico.lock.model.LockModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DecretoEventoTenoriFascicoloSiusModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_39
 * <p>
 * Title: ActLoadModificaOLDifferimento
 * </p>
 * <p>
 * Description: Classe Action per la load Modifica dei provvedimenti di
 * </p>
 * <p>
 * Ordine di Esecuzione per Differimento, esecuzione MS
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
 */
@SuppressWarnings("rawtypes")
public class ActLoadModificaOLDifferimento extends ActionSiap
		implements ICostantiMisuraSicurezza, ICostantiOrdineEsecuzione {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		BigDecimal idFascSius = getRequestBigDecimalParameter("idFascSius");
		BigDecimal idEveFascSius = getRequestBigDecimalParameter("idEveFascSius");
		IDepositoOrdinanzaPc idop = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		OrdinanzaEventoTenoriFascicoloSiusModel provvSorv = idop
				.ExRicercaEventoProvvDiffSIUSByFascSiepEFascSius(fsm.getIdFascicoloSiep(), idFascSius,
						idEveFascSius, idEvento);
		
		if (provvSorv != null && provvSorv.getEvento() != null
				&& "03".equals(provvSorv.getEvento().getCodTipoProvvedimento())) {
			
			setRequestAttribute("provvSorv", provvSorv);
			
			if (idEvento != null) {			
				IMisuraAlternativa ima = SIEPLookupRemote.getMisuraAlternativaRemote();
				MisuraAlternativaModel misurAlt = ima.ExRicercaMisuraAlternativaByIdEvento(idEvento);
				if(misurAlt != null && misurAlt.getDataFineMisura()!= null){
					setRequestAttribute("dataScadenza", misurAlt.getDataFineMisura());
					if(provvSorv != null && provvSorv.getOrdinanza() != null){
						provvSorv.getOrdinanza().setDataFineMisura(misurAlt.getDataFineMisura());
						setRequestAttribute("provvSorv", provvSorv);
					}
				}
		}
			
		}else if (provvSorv != null && provvSorv.getEvento() != null
				&& "02".equals(provvSorv.getEvento().getCodTipoProvvedimento())) {
			
			// intervento per gestire anche i DECRETI
			DecretoEventoTenoriFascicoloSiusModel depoDec = null;
			IDepositoDecreto lDepoDecCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
			depoDec = lDepoDecCtrl
					.ExRicercaEventoProvvDiffSIUSByFascSiepEFascSius(fsm.getIdFascicoloSiep(), idFascSius,
							idEveFascSius, idEvento);
			
			if (idEvento != null) {			
				IMisuraAlternativa ima = SIEPLookupRemote.getMisuraAlternativaRemote();
				MisuraAlternativaModel misurAlt = ima.ExRicercaMisuraAlternativaByIdEvento(idEvento);
				if(misurAlt != null && misurAlt.getDataFineMisura()!= null){
					setRequestAttribute("dataScadenza", misurAlt.getDataFineMisura());
					if(depoDec != null && depoDec.getDecreto() != null){
						depoDec.getDecreto().setDataScadenzaSospensioneSS(misurAlt.getDataFineMisura());
						setRequestAttribute("provvSorv", provvSorv);
					}
				}
			}
			
			setRequestAttribute("provvSorv", depoDec);
		}	

		// evento inserito manualmente
		if (Utils.isNullObj(idFascSius)) { // 20190605 [SG]: idFascSius --> idEveFascSius
			IMisuraAlternativa ima = SIEPLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel mam = ima.ExRicercaMisuraAlternativaByIdEvento(idEvento);
			setRequestAttribute("misuraAlternativa", mam);
			// UFFICIO EMITTENTE
			IUfficio iUfficio = SICOLookupRemote.getUfficioRemote();
			UfficioModel um = new UfficioModel();
			um = iUfficio.getUfficioByKey(mam.getChiaveUfficioFascicoloSius());
			setRequestAttribute("ufficioEmittente", um);
			// recupero records x la varie combo
			Option mp = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
			mp.setFilter(new String[] { "-", "0428", "0429", "0430", "0431", "0432", "0433" });
			Option et = new Option(DecodificheManager.getInstance().getEsitoProvvedimento());
			et.setFilter(
					new String[] { "-", "0002", "0003", "0004", "0005", "0035", "0119", "0145", "0360" });
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
		
		
//		if (idEvento != null) {			
//			IMisuraAlternativa ima = SIEPLookupRemote.getMisuraAlternativaRemote();
//			MisuraAlternativaModel misurAlt = ima.ExRicercaMisuraAlternativaByIdEvento(idEvento);
//			if(misurAlt != null && misurAlt.getDataFineMisura()!= null){
//				setRequestAttribute("dataScadenza", misurAlt.getDataFineMisura());				
//				if(provvSorv != null && provvSorv.getOrdinanza() != null){
//					provvSorv.getOrdinanza().setDataFineMisura(misurAlt.getDataFineMisura());
//					setRequestAttribute("provvSorv", provvSorv);
//				}
//			}
//		}

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lm = lockIfNotLocked("evento", getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO),
				getCodUtenteConnesso());
		if (lm != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"La " + lm.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		// Controllo Esistenza pena residua non validata per quel fascicolo
		PenaResiduaModel prm = new PenaResiduaModel();
		IPenaResidua ipr = SIEPLookupRemote.getPenaResiduaRemote();
		prm = ipr.ExRicercaPenaResiduaCorrenteByFascicoloSiep(fsm.getIdFascicoloSiep());

		if (prm != null && prm.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		Date dataInizioPena = null;
		// Date dataFinePenaA = null;
		if (prm != null) {
			dataInizioPena = prm.getDataInizio();
			// dataFinePenaA = prm.getDataFinePresunta();
		}
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(dataInizioPena, "dd-MM-yyyy"));
		// setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(dataFinePenaA, "dd-MM-yyy"));
		setRequestAttribute("penaresidua", prm);

		// Avvocati
		IAvvocato iAvvocato = SIEPLookupRemote.getAvvocatoRemote();
		try {
			Vector avvocati = iAvvocato.ExRicercaAvvocatiByFascicolo(fsm.getIdFascicoloSiep());
			setRequestAttribute("avvocati", avvocati);
		} catch (SIEPException e) {
			// NON Rilancio l'eccezione: Deve Passare anche se Fascicolo è PRIVO di Avvocato
			siesLogger.info("Fascicolo PRIVO di Avvocato");
		}

		// Posizione Giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				fsm.getIdFascicoloSiep());
		setRequestAttribute("posizione", pgldacm);

		// Misure Sicurezza
		// Ricerca Misure sicurezza già presenti nel fascicolo : Sono ORDINATE per DATA_INSERIMENTO
		List listaMS = new ArrayList();
		// MisuraSicurezzaModel msm = null;
		IMisuraSicurezza ims = SIEPLookupRemote.getMisuraSicurezzaRemote();
		listaMS = ims.ExRicercaMisuraSicurezzaByIdFascicoloOrd(fsm.getIdFascicoloSiep());
		// prendo solo l'ultima MISURA (28/10/2014 se esiste)
		if (listaMS.size() > 0)
			// msm = (MisuraSicurezzaModel) listaMS.get(listaMS.size() - 1);
			siesLogger.info("Ultima MISURA su: " + listaMS.size());
		setRequestAttribute("listaMisure", listaMS);
		// setRequestAttribute("MisuraModel", msm);

		// Magistrato
		IMagistrato iMagistrato = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel mm = iMagistrato.ExRicercaMagistratoByEvento(idEvento);
		setRequestAttribute("magistrato", mm);

		// EventoNotifica
		EventoNotificaModel enm = new EventoNotificaModel();
		IEvento iEvento = SICOLookupRemote.getEventoRemote();
		enm = iEvento.ExRicercaEventoNotificaByKey(idEvento);
		setRequestAttribute("eventonotifica", enm);

		// Autorità per Notifiche
		String codTipoAutorita1 = "-";
		String codTipoAutorita2 = "-";
		String codTipoAutoritaND = "-";

		UfficioModel um = new UfficioModel();
		String codTipoUfficio = "-";
		String descrSedeUfficio = "";

		if (enm.getNotifiche() != null && enm.getNotifiche().length > 0) {
			for (int i = 0; i < enm.getNotifiche().length; i++) {
				if (enm.getNotifiche()[i] != null && ("E").equals(enm.getNotifiche()[i].getCodTipoNotifica())
						&& enm.getNotifiche()[i].getAutoritaEsterna() != null) {
					if (codTipoAutorita1.compareTo("-") == 0)
						codTipoAutorita1 = enm.getNotifiche()[i].getAutoritaEsterna().getCodTipoAutorita();
					else
						codTipoAutorita2 = enm.getNotifiche()[i].getAutoritaEsterna().getCodTipoAutorita();
				}
				if (enm.getNotifiche()[i] != null
						&& ("MS").equals(enm.getNotifiche()[i].getCodTipoNotifica())) {
					if (enm.getNotifiche()[i].getUfficio() != null) {
						um = enm.getNotifiche()[i].getUfficio();
						codTipoUfficio = um.getCodTipoUfficio();
						descrSedeUfficio = um.getDescrComune();
					}
				}
				if (enm.getNotifiche()[i] != null && ("ND").equals(enm.getNotifiche()[i].getCodTipoNotifica())
						&& enm.getNotifiche()[i].getAutoritaEsterna() != null)
					codTipoAutoritaND = enm.getNotifiche()[i].getAutoritaEsterna().getCodTipoAutorita();
			}
		}

		// Riempimento ComboBox Autorità (Prima)
		Option lOptionE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		if (codTipoAutorita1.compareTo("-") != 0)
			lOptionE.setSelected(codTipoAutorita1);
		setRequestAttribute("autoritaEsternaE", "" + lOptionE);

		// Riempimento ComboBox Autorità (Seconda)
		Option lOptionC = new Option(DecodificheManager.getInstance().getTipoAutorita());
		if (codTipoAutorita2.compareTo("-") != 0)
			lOptionC.setSelected(codTipoAutorita2);
		setRequestAttribute("autoritaEsternaC", "" + lOptionC);

		// Aggiunto Desinatatio Sorveglianza
		Option lOptionSor = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOptionSor.setFilter(new String[] { "-", "TDS", "UDS", "UDSM" });
		if (codTipoUfficio.compareTo("-") != 0)
			lOptionSor.setSelected(codTipoUfficio);
		setRequestAttribute("tipoUDS", "" + lOptionSor);
		setRequestAttribute("comuneUDS", "" + descrSedeUfficio);

		// Riempimento ComboBox notifica Difensore
		Option lOptionND = new Option(DecodificheManager.getInstance().getTipoAutorita());
		String[] lFiltroAvvo = { "22", "C0" };
		lOptionND.setFilter(lFiltroAvvo);
		if (codTipoAutoritaND.compareTo("-") != 0)
			lOptionND.setSelected(codTipoAutoritaND);
		setRequestAttribute("autoritaEsterna", "" + lOptionND);
		setRequestAttribute("NotificaAvvocati", "" + codTipoAutoritaND);

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
			IEvento lCtrlDes = SICOLookupRemote.getEventoRemote();
			lEveVerMod = lCtrlDes.ExRicercaEventoVerbaleByIdEve(eventoDesignazioneIStituto.getIdEvento());			
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
				
		// valore di ritorno
		if (provvSorv != null && provvSorv.getEvento() != null
				&& "03".equals(provvSorv.getEvento().getCodTipoProvvedimento())) {			
			return PG_MODIFICA_OE_DIFFERIMENTO;
		}
		else return PG_MODIFICA_OE_DIFFERIMENTO_DEC;
		
		
	} // chiude processRequest

} // Chiude Classe