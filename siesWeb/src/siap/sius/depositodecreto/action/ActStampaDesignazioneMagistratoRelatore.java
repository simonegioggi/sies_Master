package siap.sius.depositodecreto.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_9: aggiunta action di caricamento dati
 *
 * @author Gioggi
 */
public class ActStampaDesignazioneMagistratoRelatore extends ActionSiap implements ICostantiDepositoDecreto {

	public String processRequest() throws Exception {

		// Preleva dati di sessione
		FascicoloGPModel fgpm = new FascicoloGPModel();
		fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		// Preleva l'evento dalla request
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Legge l'evento + notifiche dal dbase
		EventoNotificaModel enm = new EventoNotificaModel();
		IEvento ie = SICOLookupRemote.getEventoRemote();
		enm = ie.ExRicercaEventoNotificaByKey(idEvento);

		// Ufficio utente connesso
		UfficioModel um = getUfficioUtenteConnesso();

		enm.getEvento().setIdEvento(idEvento);
		enm.getEvento().setFasSieIdFascicoloSiep(fgpm.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		enm.getEvento().setDescrLuogoEmittente(um.getDescrComune());
		enm.getEvento().setDescrUfficioEmittente(um.getDescrTipoUfficio());
		enm.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		enm.getEvento().setCodUfficioAggiornamento(um.getCodUfficio());
		enm.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
		enm.getEvento().setFlagDocumentoRegistrato("N");
		enm.getEvento().setTemIdTemplate(TEMPLATE_DECRETO_DESIGNAZIONE_MAGISTRATO_RELATORE);
		enm.setNomeTemplate(TEMPLATE_DECRETO_DESIGNAZIONE_MAGISTRATO_RELATORE);

		IDepositoDecreto idd = SIUSLookupRemote.getDepositoDecretoRemote();
		ByteArrayOutputStream baos = idd.ExStampEmissioneDecreto(enm.getEvento(), getUfficioUtenteConnesso(),
				super.getUtenteConnesso());

		// setta la risposta nella request
		setRequestAttribute("eventonotifica", enm);
		setRequestAttribute("report", baos);

		// valore di ritorno
		return IWebConstants.PG_DOWNLOAD_NEW;
	}

}