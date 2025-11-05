package siap.siep.archiviazione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
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

/**
 * ActStampaPassaggioClasse - Classe per la stampa della definizione procedimento per passaggio di classe
 *
 * @since MEV_2025-48
 * @author sgioggi
 * @version 1.0
 */
public class ActStampaPassaggioClasse extends ActionSiap implements ICostantiArchiviazione {

	public String processRequest() throws F3BException {

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel utm = getUtenteConnesso();
		UfficioModel ufm = getUfficioUtenteConnesso();

		String idEvento = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoModel em = ie.ExRicercaEventoByKey(new BigDecimal(idEvento));

		EventoNotificaModel enm = new EventoNotificaModel();

		enm.getEvento().setIdEvento(new BigDecimal(idEvento));
		enm.getEvento().setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());

		enm.getEvento().setDescrLuogoEmittente(ufm.getDescrComune());
		enm.getEvento().setDescrUfficioEmittente(ufm.getDescrTipoUfficio());

		enm.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		enm.getEvento().setCodUfficioAggiornamento(ufm.getCodUfficio());
		enm.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
		enm.getEvento().setFlagDocumentoRegistrato("N");

		String flagTemplate = "0";

		ITemplate it = SICOLookupRemote.getTemplateRemote();
		TemplateModel tm = new TemplateModel();
		tm = it.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(em.getCodTipoEvento(),
				em.getCodTipoProvvedimento(), em.getCodMotivo(), flagTemplate);
		enm.setNomeTemplate(tm.getIdTemplate());

		ByteArrayOutputStream baos = ie.ExStampaDocumento(enm, utm);

		setRequestAttribute("report", baos);

		// pagina di ritorno
		return IWebConstants.PG_DOWNLOAD;
	}

}