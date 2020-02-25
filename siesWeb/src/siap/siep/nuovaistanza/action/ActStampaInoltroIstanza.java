package siap.siep.nuovaistanza.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActStampaInoltroIstanza extends ActionSiap implements ICostantiNuovaIstanza {

	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		// lEveMod.getEvento().setFlagDocumentoRegistrato("S"); //Per defalut si assume l'istanza a 'S'
		lEveMod.setNomeTemplate(TEMPLATE_INOLTRO_ISTANZA);

		INuovaIstanza lCtrlNuovaIstanza = SIEPLookupRemote.getNuovaIstanzaRemote();
		// 20180110: [SG] aggiunto parametro di passaggio x gestione NOTIFICHE: prendo solo l'ultima
		ByteArrayOutputStream lReport = lCtrlNuovaIstanza.ExStampaTrasmissioneNuovaIstanza(lEveMod,
				lUtenteMod, "II");

		// setta la risposta nella request
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}