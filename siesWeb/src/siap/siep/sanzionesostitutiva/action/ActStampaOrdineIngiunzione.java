package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-13: aggiunta classe per la stampa dell'ordine di ingiunzione
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActStampaOrdineIngiunzione extends ActionSiap {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("unchecked")
	public String processRequest() throws F3BException {

    	// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		// ==========================================================================
		// Recupero l'evento per il quale produrre la Stampa (comunicazione)
		// ==========================================================================
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(lIdEvento);

		// MEV33 - Differenziati i template per "LIBERO" e "DETENUTO"
		// flagTemplate:
		//  - 0 = rata unica           LIBERO
		//  - 1 = pagamento rateizzato LIBERO
		//  - 2 = rata unica           DETENUTO
		//  - 3 = pagamento rateizzato DETENUTO
		boolean isLibero = false;
		// Recupero la POG
		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascicoloModel.getIdFascicoloSiep());		
		String lCodPG = lPos.getPosizioneGiuridica().getCodPosizioneGiuridica();
		
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		DecodificheModel lModel = new DecodificheModel();
		lModel.setContesto("POSIZIONE_GIURIDICA");
		
		Collection <DecodificheModel> listaPg = lDecodifiche.ExRicercaDecodifiche(lModel);
		siesLogger.debug("listaPg.size() = "+listaPg.size());
		
		for (DecodificheModel docode : listaPg) {
			if (docode.getCode().equals(lCodPG)) {
				if ("EI".equals(docode.getCodiceAlt5()))
					isLibero = false;
				else
					isLibero = true;

				siesLogger.debug("isLibero = "+isLibero);
				
				break;
			}
		}
		
		IRateizzazionePP lCtrlRate = SIEPLookupRemote.getRateizzazionePPRemote();
		Vector<RateizzazionePPModel> listaRateEvento = lCtrlRate.exRicercaRateizzazioniByIdEvento(lIdEvento);
		String flagTemplate = "0";
		if ("U".equals(listaRateEvento.elementAt(0).getTipoRateizzazione())) {
			if (isLibero) flagTemplate = "0";
			else flagTemplate = "2";
		}
		else if ("R".equals(listaRateEvento.elementAt(0).getTipoRateizzazione())) {
			if (isLibero) flagTemplate = "1";
			else flagTemplate = "3";
		}

		
		/*
		String flagTemplate = "0"; // 0 = rata unica, 1 = pagamento rateizzato
		IRateizzazionePP lCtrlRate = SIEPLookupRemote.getRateizzazionePPRemote();
		Vector<RateizzazionePPModel> listaRateEvento = lCtrlRate.exRicercaRateizzazioniByIdEvento(lIdEvento);
		if ("U".equals(listaRateEvento.elementAt(0).getTipoRateizzazione()))
			flagTemplate = "0";
		else if ("R".equals(listaRateEvento.elementAt(0).getTipoRateizzazione()))
			flagTemplate = "1";
		*/
		// FINE - MEV33
		
		// ==========================================================================
		// Recupero il template
		// ==========================================================================
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		TemplateModel lTemMod = new TemplateModel();
		lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(
				lEventoModel.getCodTipoEvento(), lEventoModel.getCodTipoProvvedimento(),
				lEventoModel.getCodMotivo(), flagTemplate);

		siesLogger.debug("lTemMod = " + lTemMod);

		// ==========================================================================
		// Genero il model Evento da passare alla funzione di stampa
		// ==========================================================================
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		lEveNotMod.setNomeTemplate(lTemMod.getIdTemplate());

		lEveNotMod.getEvento().setIdEvento(lIdEvento);
		lEveNotMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveNotMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveNotMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		lEveNotMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveNotMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveNotMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());

		lEveNotMod.getEvento().setFlagDocumentoRegistrato("N");

		// ==========================================================================
		// Produce la stampa
		// ==========================================================================
		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveNotMod, lUtenteMod);

		// ==============================================
		// Setta il documento di stampa sulla response
		// ==============================================
		setRequestAttribute("report", lReport);

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return IWebConstants.PG_DOWNLOAD;
	}

}