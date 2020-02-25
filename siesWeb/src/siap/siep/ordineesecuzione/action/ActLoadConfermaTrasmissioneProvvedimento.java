package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.nuovaistanza.action.ICostantiNuovaIstanza;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadConfermaTrasmissioneProvvedimento
 * </p>
 * <p>
 * Description: Classe Action per il trasferimento dei provvedimenti (Legge 199/2010) Libero
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadConfermaTrasmissioneProvvedimento extends ActSIESDettaglioProvvedimento
		implements ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		// BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		EventoModel lEveMod = new EventoModel();

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		String lTipoUff = getRequestStringParameter(
				ICostantiNuovaIstanza.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
		String lSedeUff = getRequestStringParameter(ICostantiNuovaIstanza.CAMPO_COD_LUOGO_DESTINATARIO);

		String lCodiceUfficio = getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lSedeUff);

		UfficioModel lLocal = getUfficioByCodUfficio(lCodiceUfficio);

		// A.S. 18/05/2015 su richiesta di Michele/Nunzia
		// Per SIEP la descrizione UDSM cambia da "Ufficio di Sorveglianza presso il Tribunale per minorenni"
		// in "Magistrato di Sorveglianza per i minorenni"
		if (lLocal.getCodTipoUfficio().equalsIgnoreCase("UDSM")) {
			lLocal.setDescrTipoUfficio("Magistrato di Sorveglianza per i minorenni");
		}

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoIstanzaByKey(lEveId);

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
		/* NotificaModel lRetModel = */lCtrlNot.ExInserisciNotifica(lNot);

		setRequestAttribute("evento", lEveMod);
		setRequestAttribute("UfficioDestinatario", lLocal);

		return PG_LOAD_CONFERMA_TRASMISSIONE_PROVVEDIMENTO;
	}
}