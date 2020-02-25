package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.EventoVerbaleModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DecretoEventoTenoriFascicoloSiusModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.generaleprocedimento.controller.IGeneraleProcedimento;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_39
 * <p>
 * Title: ActDettaglioOLDifferimento
 * </p>
 * <p>
 * Description: Classe per il Dettaglio dell' O.L. per Differimento per esecuzione Misura di Sicurezza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 *
 * @author SGIOGGI
 * @version 1.0
 */
public class ActDettaglioOLDifferimento extends ActSIESDettaglioProvvedimento
		implements ICostantiMisuraSicurezza, ICostantiOrdineEsecuzione {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		siesLogger.info("ID EVENTO = " + idEvento);

		// EventoNotifica
		EventoNotificaModel enm = new EventoNotificaModel();
		IEvento iEvento = SICOLookupRemote.getEventoRemote();
		enm = iEvento.ExRicercaEventoNotificaByKey(idEvento);

		setRequestAttribute("eventonotifica", enm);

		// Cerco Isstituto Detenzione
		IstitutoDetenzioneModel idm = new IstitutoDetenzioneModel();
		IIstitutoDetenzione iid = SIEPLookupRemote.getIstitutoDetenzioneRemote();

		for (int i = 0; i < enm.getNotifiche().length; i++) {
			if (enm.getNotifiche()[i].getCodTipoNotifica().equals("E")) {
				if (enm.getNotifiche()[i].getIstDetIdIstitutoDetenzione() != null) {
					idm = iid.ExRicercaIstitutoDetenzioneByKey(
							enm.getNotifiche()[i].getIstDetIdIstitutoDetenzione());
				}
			}
		}
		setRequestAttribute("istitutodetenzione", idm);

		// Posizione Giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				fsm.getIdFascicoloSiep());
		setRequestAttribute("posizioneAltra", pgldacm);

		// Ricerca Misure sicurezza già presenti nel fascicolo : Sono ORDINATE per DATA_INSERIMENTO
		List listaMS = new ArrayList();
		MisuraSicurezzaModel msm = null;
		IMisuraSicurezza ims = SIEPLookupRemote.getMisuraSicurezzaRemote();
		listaMS = ims.ExRicercaMisuraSicurezzaByIdFascicoloOrd(fsm.getIdFascicoloSiep());
		// prendo solo l'ultima MISURA
		if (listaMS != null && listaMS.size() > 0)
			msm = (MisuraSicurezzaModel) listaMS.get(listaMS.size() - 1);

		setRequestAttribute("MisuraModel", msm);
		setRequestAttribute("listaMisure", listaMS);

		// Dati ORDINANZA SIUS ---> Controllo del tipo di DECISIONE / ESITO di MDS
		// Vector DepoOrdinanze;
		// IDepositoOrdinanzaPc lDepoCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		// DepoOrdinanze = lDepoCtrl.ExRicercaDepositoOrdinanzaPcEventoByFascicoloSiep(fsm
		// .getIdFascicoloSiep());
		// setRequestAttribute("VecOrd", DepoOrdinanze);

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato iAvvocato = SIEPLookupRemote.getAvvocatoRemote();
		try {
			Vector avvocati = iAvvocato.ExRicercaAvvocatiByFascicolo(fsm.getIdFascicoloSiep());
			setRequestAttribute("avvocati", avvocati);
		} catch (SIEPException e) {
			// NON Rilancio l'eccezione: Deve passare anche in assenza del Difensore
			siesLogger.info(
					"NON CI SONO AVVOCATI DIFENSORI LEGATI AL FASCICOLO SIEP: " + fsm.getIdFascicoloSiep());
		}

		// provvedimento della sorveglianza
		IDepositoOrdinanzaPc idopc = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		BigDecimal idFascSius = null;
		BigDecimal idEveFascSius = null;
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS)
				&& getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS) != null
				&& !"".equals(getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS)
						.toString())
				&& !isRequestParameterNullObj("idEventoFascSius")
				&& getRequestBigDecimalParameter("idEventoFascSius") != null
				&& !"".equals(getRequestBigDecimalParameter("idEventoFascSius").toString())) {
			// provengo da dettaglio
			idFascSius = getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);
			idEveFascSius = getRequestBigDecimalParameter("idEventoFascSius");
		} else {
			// provengo da elenco
			idFascSius = enm.getEvento().getFasSiuIdFascicoloSius();
			// recupero da generale_procedimento
			IGeneraleProcedimento igp = SIUSLookupRemote.getGeneraleProcedimentoRemote();
			GeneraleProcedimentoModel gpm = igp.ExRicercaGeneraleProcedimentoByFascicolo(idFascSius);
			DepositoOrdinanzaPcModel dopcm = null;
			if (gpm != null)
				dopcm = idopc.ExRicercaDepositoOrdinanzaPcByGenProc(gpm.getIdGeneraleProcedimento());
			if (!Utils.isNullObj(dopcm))
				idEveFascSius = dopcm.getIdEventoGenerato();
			// if (Utils.isNullObj(idEveFascSius)) {
			// // info per il log
			// siesLogger
			// .info("Ordine di Esecuzione per Differimento MS non disponibile per il fascicolo SIUS: "
			// + idFascSius);
			// setRequestAttribute(IWebConstants.MESSAGE_TEXT,
			// "Ordine di Esecuzione per Differimento MS non disponibile!");
			// return IWebConstants.PG_MESSAGE;
			// }
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
		}

		OrdinanzaEventoTenoriFascicoloSiusModel provvSorv = idopc
				.ExRicercaEventoProvvDiffSIUSByFascSiepEFascSius(fsm.getIdFascicoloSiep(), idFascSius,
						idEveFascSius, idEvento);

		// nel caso di ordinanza
		if (provvSorv != null && provvSorv.getEvento() != null
				&& "03".equals(provvSorv.getEvento().getCodTipoProvvedimento())) {

			setRequestAttribute("provvSorv", provvSorv);

			if (idEvento != null) {
				IMisuraAlternativa ima = SIEPLookupRemote.getMisuraAlternativaRemote();
				MisuraAlternativaModel misurAlt = ima.ExRicercaMisuraAlternativaByIdEvento(idEvento);
				if (misurAlt != null && misurAlt.getDataFineMisura() != null) {
					setRequestAttribute("dataScadenza", misurAlt.getDataFineMisura());
					if (provvSorv != null && provvSorv.getOrdinanza() != null) {
						provvSorv.getOrdinanza().setDataFineMisura(misurAlt.getDataFineMisura());
						setRequestAttribute("provvSorv", provvSorv);
					}
				}
			}
			// caso di decreto
		} else if (provvSorv != null && provvSorv.getEvento() != null
				&& "02".equals(provvSorv.getEvento().getCodTipoProvvedimento())) {

			// intervento per gestire anche i DECRETI
			DecretoEventoTenoriFascicoloSiusModel depoDec = null;
			IDepositoDecreto lDepoDecCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
			depoDec = lDepoDecCtrl.ExRicercaEventoProvvDiffSIUSByFascSiepEFascSius(fsm.getIdFascicoloSiep(),
					idFascSius, idEveFascSius, idEvento);

			if (idEvento != null) {
				IMisuraAlternativa ima = SIEPLookupRemote.getMisuraAlternativaRemote();
				MisuraAlternativaModel misurAlt = ima.ExRicercaMisuraAlternativaByIdEvento(idEvento);
				if (misurAlt != null && misurAlt.getDataFineMisura() != null) {
					setRequestAttribute("dataScadenza", misurAlt.getDataFineMisura());
					if (depoDec != null && depoDec.getDecreto() != null) {
						depoDec.getDecreto().setDataScadenzaSospensioneSS(misurAlt.getDataFineMisura());
						setRequestAttribute("provvSorv", provvSorv);
					}
				}
			}
			setRequestAttribute("provvSorv", depoDec);
		}

		// se DataFineMisura è null, la devo calcolare!
		// if (provvSorv != null && provvSorv.getOrdinanza() != null
		// && provvSorv.getOrdinanza().getDataFineMisura() == null) {
		// if (provvSorv.getMisuraAlternativa() != null
		// && provvSorv.getMisuraAlternativa().getDataInizioMisura() != null) {
		//
		// Date dataFineMis = provvSorv.getMisuraAlternativa().getDataInizioMisura();
		// Calendar data = Calendar.getInstance();
		// data.setTime(dataFineMis);
		// BigDecimal ggM = provvSorv.getOrdinanza().getSospensioneGGSS() != null
		// ? provvSorv.getOrdinanza().getSospensioneGGSS()
		// : provvSorv.getMisuraAlternativa().getNumGiorniMisura();
		// if (ggM != null)
		// data.add(Calendar.DAY_OF_YEAR, ggM.intValue());
		//
		// BigDecimal mmM = provvSorv.getOrdinanza().getSospensioneMMSS() != null
		// ? provvSorv.getOrdinanza().getSospensioneMMSS()
		// : provvSorv.getMisuraAlternativa().getNumMesiMisura();
		// if (mmM != null)
		// data.add(Calendar.MONTH, mmM.intValue());
		//
		// BigDecimal aaM = provvSorv.getOrdinanza().getSospensioneAASS() != null
		// ? provvSorv.getOrdinanza().getSospensioneAASS()
		// : provvSorv.getMisuraAlternativa().getNumAnniMisura();
		// if (aaM != null)
		// data.add(Calendar.YEAR, aaM.intValue());
		//
		// setRequestAttribute("dataScadenza", data.getTime());
		// }
		// }

		// MEV_39: DEVO RECUPERARE LA STRUTTURA DESIGNATA se esiste un evento di DESIGNAZIONTE ISTITUTO
		EventoModel lEveDaRicercare = new EventoModel();
		lEveDaRicercare.setCodTipoEvento("01");
		lEveDaRicercare.setCodTipoProvvedimento("25");
		lEveDaRicercare.setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel eventoDesignazioneIStituto = lCtrlEvento.ExRicercaEventoPerMotivo(new String[] { "1130" },
				lEveDaRicercare);

		if (eventoDesignazioneIStituto != null
				&& !"A".equals(eventoDesignazioneIStituto.getFlagDocumentoRegistrato())) {
			// EventoVerbale DesignazioneIStituto
			EventoVerbaleModel lEveVerMod = new EventoVerbaleModel();
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			lEveVerMod = lCtrl.ExRicercaEventoVerbaleByIdEve(eventoDesignazioneIStituto.getIdEvento());
			IstitutoDetenzioneModel strutturaDesignata = new IstitutoDetenzioneModel();
			if (lEveVerMod != null && lEveVerMod.getVerbale() != null
					&& lEveVerMod.getVerbale().getIstDetIdIstitutoDetenzione() != null
					&& !lEveVerMod.getVerbale().getIstDetIdIstitutoDetenzione().equals("-")) {
				IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
				strutturaDesignata = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(
						lEveVerMod.getVerbale().getIstDetIdIstitutoDetenzione());
			}
			setRequestAttribute("strutturaDesignataModel", strutturaDesignata);
		}

		// pagine di ritorno
		if (provvSorv != null && provvSorv.getEvento() != null
				&& "03".equals(provvSorv.getEvento().getCodTipoProvvedimento())) {
			return PG_DETTAGLIO_OE_DIFFERIMENTO;
		} else
			return PG_DETTAGLIO_OE_DIFFERIMENTO_DEC;
	} // chiude processRequest

} // Chiude Classe