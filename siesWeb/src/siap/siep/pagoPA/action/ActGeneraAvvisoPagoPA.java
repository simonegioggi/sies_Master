package siap.siep.pagoPA.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;

/**
 * Classe che permette di generare un avviso di pagamento per PagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 * @deprecated
 */
public class ActGeneraAvvisoPagoPA extends ActionSiap implements ICostantiSecurity {

	public String processRequest() throws Exception {

		EventoNotificaModel enm = new EventoNotificaModel();

		enm.getEvento().setCodTipoEvento("05"); // Tipo Evento = Genera Avviso PagoPA
		enm.getEvento().setCodTipoProvvedimento("-");
		enm.getEvento().setCodMotivo("0050"); // Codice Motivo = Richiesta Avviso PagoPA

		// data_richiesta e data_ricezione vanno su EVENTO nei campi:
		// DATA_TRASMISSIONE_ATTI & DATA_RICEZIONE_ATTI

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		enm.getEvento().setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());

		enm.getEvento().setDataEmissione(DateUtils.getSysDate());

		UfficioModel um = getUfficioUtenteConnesso();

		enm.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());

		enm.getEvento().setCodLuogoEmittente(um.getCodComune());
		enm.getEvento().setCodUfficioEmittente(um.getCodUfficio());
		enm.getEvento().setDataInserimento(DateUtils.getSysDate());
		enm.getEvento().setCodUfficioInserimento(um.getCodUfficio());
		enm.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		enm.getEvento().setCodEsito("-");
		enm.getEvento().setCodLuogoDestinatario("-");
		enm.getEvento().setCodTipoUfficioDestinatario("-");

		NotificaModel nmArray[] = new NotificaModel[1];
		NotificaModel nm = new NotificaModel();

		nm.setCodTipoNotifica("N");
		nm.setDataInvio(DateUtils.getSysDate());
		nm.setCodEsito("-");
		nm.setCodOperatoreInserimento(getCodUtenteConnesso());
		nm.setDataInserimento(DateUtils.getSysDate());
		nm.setCodUfficioInserimento(um.getCodUfficio());

		AutoritaEsternaModel aem = new AutoritaEsternaModel();

		aem.setCodTipoAutorita("24");
		// Viene settato di default il codice del comune di Roma
		// al quale viene richiesto il certificato
		aem.setCodSede(ICostantiComune.COD_COMUNE_ROMA);
		aem.setCodOperatoreInserimento(getCodUtenteConnesso());
		aem.setDataInserimento(DateUtils.getSysDate());
		aem.setCodUfficioInserimento(um.getCodUfficio());
		nm.setAutoritaEsterna(aem);

		nmArray[0] = nm;

		// Inserisco l'array di Notifiche nell'Evento
		enm.setNotifiche(nmArray);

		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enmNew = ie.ExInserisciEventoNotifica(enm);

		String returnAction = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.pagoPA.action.ActDownloadAvvisoPagoPA&TipoFascicolo=SIEP&IdEvento="
				+ enmNew.getEvento().getIdEvento();

		return returnAction;
	}

}