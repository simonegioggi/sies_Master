package siap.siep.misurasicurezza.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

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
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * MEV_39
 * <p>
 * Title: ActStampaOLDifferimento
 * </p>
 * <p>
 * Description: Stampa dell'Ordine di liberazione per differimento MS
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
public class ActStampaOLDifferimento extends ActionSiap implements ICostantiMisuraSicurezza,
		ICostantiOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		String idEvento = getRequestStringParameter("IdEvento");
		String flagTemplate = "0";
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel utm = getUtenteConnesso();
		UfficioModel ufm = getUfficioUtenteConnesso();
		IEvento iEvento = SICOLookupRemote.getEventoRemote();
		EventoModel em = iEvento.ExRicercaEventoByKey(new BigDecimal(idEvento));
		EventoNotificaModel enm = new EventoNotificaModel();
		enm.getEvento().setIdEvento(new BigDecimal(idEvento));
		enm.getEvento().setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		enm.getEvento().setDescrLuogoEmittente(ufm.getDescrComune());
		enm.getEvento().setDescrUfficioEmittente(ufm.getDescrTipoUfficio());
		enm.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		enm.getEvento().setCodUfficioAggiornamento(ufm.getCodUfficio());
		enm.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
		enm.getEvento().setFlagDocumentoRegistrato("N");

		// 06 (ORDINE ESECUZIONE) se scarcera il PM, 25 (COMUNICAZIONE) se gia' scarcerato
		// intervento post collaudo 11.3 (non deve essere ORDINE DI ESECUZIONE, ma ORDINE DI LIBERAZIONE CHE HA CODICE 65)
		if ("65".equals(em.getCodTipoProvvedimento()))
			flagTemplate = "1";
		else
			flagTemplate = "2";

		// ========================================
		// Recupero il template se esiste
		// ========================================
		TemplateModel tm = new TemplateModel();
		ITemplate iTemplate = SICOLookupRemote.getTemplateRemote();
		try {
			tm = iTemplate.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(em.getCodTipoEvento(),
					em.getCodTipoProvvedimento(), em.getCodMotivo(), flagTemplate);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("TEMPLATE MODEL >>> " + tm);
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE)
				throw e;
		}

		if (tm != null && tm.getIdTemplate() != null && !tm.getIdTemplate().equals("")) {
			enm.setNomeTemplate(tm.getIdTemplate());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("ID TEMPLATE >>> " + tm.getIdTemplate());
		} else {
			enm.setNomeTemplate("VUOTO");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("ID TEMPLATE Vuoto >>>VUOTO<<<<");
		}

		ByteArrayOutputStream baos = iEvento.ExStampaDocumento(enm, utm);
		// setta la risposta nella request
		setRequestAttribute("report", baos);

		// valore di ritorno
		return IWebConstants.PG_DOWNLOAD;
	}

} // Chiude Classe