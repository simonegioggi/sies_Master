package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadConfermaTrasmissioneRichiestaAccertaPericoloSociale
 * </p>
 * <p>
 * Description: Classe Action per la load della form del trasferimento della Richiesta di
 * </p>
 * <p>
 * Accertamento pericolosità sociale al MDS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Ambrosino
 * @version 1.0
 */
public class ActLoadConfermaTrasmissioneRichiestaAccertaPericoloSociale extends ActionSiap implements
		ICostantiMisuraSicurezza {

	public String processRequest() throws F3BException {

		EventoNotificaModel EveNotMod = new EventoNotificaModel();

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		String lTipoUff = getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_TIPO_UFFICIO_UDS);
		String lSedeUff = getRequestStringParameter(ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS_NOTIFICA);

		String lCodiceUfficio = getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lSedeUff);

		UfficioModel lLocal = getUfficioByCodUfficio(lCodiceUfficio);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		// lEveMod = lCtrl.ExRicercaEventoIstanzaByKey(lEveId);
		EveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lEveId);

		// Prepara la Notifica
		NotificaModel lNot = new NotificaModel();
		lNot.setCodTipoNotifica("N");
		lNot.setDataInvio(DateUtils.getSysDate());
		lNot.setCodEsito("-");
		lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNot.setDataInserimento(DateUtils.getSysDate());
		lNot.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lNot.setUffCodUfficio(lCodiceUfficio);
		lNot.setEveIdEvento(lEveId);
		lNot.setUfficio(lLocal);

		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		/*NotificaModel lRetModel = */lCtrlNot.ExInserisciNotifica(lNot);

		setRequestAttribute("eventonotifica", EveNotMod);
		setRequestAttribute("UfficioDestinatario", lLocal);

		return PG_LOAD_CONFERMA_TRASFERISCI_RICH_ACCERTA_PERICOLO_SOC;
	}

}