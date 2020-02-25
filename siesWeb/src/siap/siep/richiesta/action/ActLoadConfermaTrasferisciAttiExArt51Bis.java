package siap.siep.richiesta.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadConfermaTrasferisciAttiExArt51Bis
 * </p>
 * <p>
 * Description: Trasferisce Il provvedimento
 * </p>
 * <p>
 * 'ATTI di Richiesta Cessazione/Prosecuzione Mis. Alt (Ex art 51 Bis)'
 * </p>
 * <p>
 * verso il tribunale/Magistrato di sorveglianza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author C.Ambrosino
 * @version 1.0
 */

public class ActLoadConfermaTrasferisciAttiExArt51Bis extends ActionSiap implements ICostantiRichiesta {

	public String processRequest() throws F3BException {

		EventoNotificaModel EveNotMod = new EventoNotificaModel();

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		String lTipoUff = getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
		String lSedeUff = getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO);

		String lCodiceUfficio = getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lSedeUff);

		UfficioModel lLocal = getUfficioByCodUfficio(lCodiceUfficio);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lEveId);

		setRequestAttribute("eventonotifica", EveNotMod);
		setRequestAttribute("UfficioDestinatario", lLocal);

		// notifiche da Visualizzare
		for (int i = 0; i < EveNotMod.getNotifiche().length; i++) {
			NotificaModel lNotMod = new NotificaModel();
			lNotMod = EveNotMod.getNotifiche()[i];

			if (lNotMod != null && lNotMod.getAutEstIdAutoritaEsterna() != null) {
				setRequestAttribute("autoritaEsterna", lNotMod.getAutoritaEsterna());
				setRequestAttribute("noteautoritaEsterna", lNotMod.getNote());
			} else if (lNotMod != null && lNotMod.getIstDetIdIstitutoDetenzione() != null) {
				setRequestAttribute("istituto", lNotMod.getIstitutoDetenzione());
			} else if (lNotMod != null && lNotMod.getUffCodUfficio() != null
					&& lNotMod.getCodTipoNotifica().equals("E")) {
				setRequestAttribute("ufficioge", lNotMod.getUfficio());
			} else if (lNotMod != null && lNotMod.getUffCodUfficio() != null
					&& !lNotMod.getCodTipoNotifica().equals("T")
					&& lNotMod.getUfficio().getCodTipoUfficio().equals("TDS")) {
				setRequestAttribute("ufficiotds", lNotMod.getUfficio());
			} else if (lNotMod != null && lNotMod.getUffCodUfficio() != null
					&& !lNotMod.getCodTipoNotifica().equals("T")
					&& lNotMod.getUfficio().getCodTipoUfficio().equals("UDS")) {
				setRequestAttribute("ufficiouds", lNotMod.getUfficio());
			} else if (lNotMod != null && lNotMod.getUffCodUfficio() != null
					&& !lNotMod.getCodTipoNotifica().equals("T")) {
				setRequestAttribute("ufficiopm", lNotMod.getUfficio());
			}
		}

		// Prepara la nuova Notifica tipo TRASFERIMENTO
		NotificaModel lNot = new NotificaModel();
		lNot.setCodTipoNotifica("T");
		lNot.setDataInvio(DateUtils.getSysDate());
		lNot.setCodEsito("-");
		lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNot.setDataInserimento(DateUtils.getSysDate());
		lNot.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lNot.setUffCodUfficio(lCodiceUfficio);
		lNot.setEveIdEvento(lEveId);
		lNot.setUfficio(lLocal);

		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		/* NotificaModel lRetModel = */lCtrlNot.ExInserisciNotifica(lNot);

		return PG_LOAD_CONFERMA_TRASM_ATTI_51_BIS;
	}

}