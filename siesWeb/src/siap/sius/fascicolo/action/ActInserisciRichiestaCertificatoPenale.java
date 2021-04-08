package siap.sius.fascicolo.action;

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
import siap.siep.notifica.model.NotificaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * <p>
 * Title: ActInserisciRichiestaCertificatoPenale
 * </p>
 * <p>
 * Description: Classe che inserisce l'evento legato alla richiedere un certificato penale su NSC
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @version 1.0
 */
public class ActInserisciRichiestaCertificatoPenale extends ActionSiap implements ICostantiSecurity {

	public String processRequest() throws Exception {

		// MEV INTEGRAZIONE SIES ADN: eliminate userId e psw per NSC; inviamo solo utenza ADN
		// UtenteModel lUtenteModel = this.getUtenteConnesso();
		// // La prima volta che si richiede il certificato bisogna reperire la userid
		// // e la password di accesso a NSC dalla form e salvarle sulla tabella UTENTE
		// if (!this.isRequestParameterNullObj(ICostantiUtente.CAMPO_USERID_NSC)) {
		// String useridNSC = getRequestStringParameter(ICostantiUtente.CAMPO_USERID_NSC);
		// String pwdNSC = getRequestStringParameter(ICostantiUtente.CAMPO_PWD_NSC);
		// if (useridNSC != null && !useridNSC.equals("")) {
		// lUtenteModel.setUseridNSC(useridNSC);
		// }
		// if (pwdNSC != null && !pwdNSC.equals("")) {
		// lUtenteModel.setPwdNSC(pwdNSC);
		// }
		// lUtenteModel.setDataAggiornamento(DateUtils.getSysDate());
		// lUtenteModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		//
		// IUtente lCtrl = SICOLookupRemote.getUtenteRemote();
		// UtenteModel lRetModel = lCtrl.ExModificaUtenteDatiAccessoNSC(lUtenteModel);
		//
		// setSessionAttribute(SESSION_UTENTE_CONNESSO, lRetModel);
		// }

		EventoNotificaModel lEve = new EventoNotificaModel();

		lEve.getEvento().setCodTipoEvento("05"); // Tipo Evento = RIchiesta Istruttoria
		lEve.getEvento().setCodTipoProvvedimento("-"); // Tipo Provvedimento = Ordinanza

		lEve.getEvento().setCodMotivo("0050");

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		lEve.getEvento().setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		lEve.getEvento().setDataEmissione(DateUtils.getSysDate());

		UfficioModel lUff = this.getUfficioUtenteConnesso();

		lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());

		lEve.getEvento().setCodLuogoEmittente(lUff.getCodComune());
		lEve.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lUff.getCodUfficio());
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		NotificaModel lNotifiche[] = new NotificaModel[1];
		NotificaModel lNot = new NotificaModel();

		lNot.setCodTipoNotifica("N");
		lNot.setDataInvio(DateUtils.getSysDate());
		lNot.setCodEsito("-");
		lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNot.setDataInserimento(DateUtils.getSysDate());
		lNot.setCodUfficioInserimento(lUff.getCodUfficio());

		AutoritaEsternaModel lAutEstMod = new AutoritaEsternaModel();

		lAutEstMod.setCodTipoAutorita("24");
		// Viene settato di default il codice del comune di Roma
		// al quale viene richiesto il certificato
		lAutEstMod.setCodSede(ICostantiComune.COD_COMUNE_ROMA);

		lAutEstMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAutEstMod.setDataInserimento(DateUtils.getSysDate());
		lAutEstMod.setCodUfficioInserimento(lUff.getCodUfficio());
		lNot.setAutoritaEsterna(lAutEstMod);

		lNotifiche[0] = lNot;

		// Inserisco l'array di Notifiche nell'Evento
		lEve.setNotifiche(lNotifiche);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sico.webservice.action.ActRichiestaCertificatoSiesToNsc&"
				+ "TipoFascicolo=SIUS&IdEvento=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

}