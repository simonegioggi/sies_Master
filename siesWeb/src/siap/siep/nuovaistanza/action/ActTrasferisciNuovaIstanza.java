package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import siap.jms.ICostantiJMS;
//import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
//import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title : ActTrasferisciIstanza
 * </p>
 * <p>
 * Description : Trasferisce l'Istanza
 * </p>
 * <p>
 * Copyright : Copyright (c) 2002
 * </p>
 * <p>
 * Company :
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActTrasferisciNuovaIstanza extends ActionSiap implements ICostantiNuovaIstanza, ICostantiJMS {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("<------------ inizio ------------>");

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		String lTipoUff = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_DESTINATARIO);
		String lSedeUff = getRequestStringParameter(CAMPO_COD_LUOGO_DESTINATARIO);

		// ComuneModel lComuneSedeUff = this.getCodComuneByDescr(lSedeUff);

		String lCodiceUfficio = getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lSedeUff);

		UfficioModel lLocal = getUfficioByCodUfficio(lCodiceUfficio);
		// UfficioModel lBDI = this.getUfficioByCodUfficio(lLocal.getCodDistretto());

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrl.ExRicercaEventoIstanzaByKey(lEveId);

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
		// setRequestAttribute("BDIDestinataria", lSedeUff);

		/*
		 * // Prepara l'Evento per la modifica EventoModel lEveMod = new EventoModel();
		 * lEveMod.setIdEvento(lEveId);
		 * 
		 * lEveMod.setDataTrasmissioneAtti(DateUtils.getSysDate());
		 * lEveMod.setCodUfficioDestinatario(lCodiceUfficio);
		 * lEveMod.setCodLuogoDestinatario(lComuneSedeUff.getCodComune());
		 * 
		 * 
		 * // Prepara la Notifica NotificaModel lNot = new NotificaModel(); lNot.setCodTipoNotifica("N");
		 * lNot.setDataInvio(DateUtils.getSysDate()); lNot.setCodEsito("-");
		 * lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
		 * lNot.setDataInserimento(DateUtils.getSysDate());
		 * lNot.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		 * lNot.setUffCodUfficio(lCodiceUfficio); lNot.setEveIdEvento(lEveId); lNot.setUfficio(lLocal);
		 * 
		 * IEvento lCtrl = SICOLookupRemote.getEventoRemote(); EventoNotificaModel lEveNotMod =
		 * lCtrl.ExConfermaTrasferisciIstanza(lEveMod, lNot);
		 * 
		 * setRequestAttribute("eventonotifica", lEveNotMod);
		 * 
		 */
		/*
		 * --------- 21 - 08 - 2003 ---- Il Messaggio viene spedito dopo la stampa della Trasmissione Istanza
		 * ITrasmissioneJMS lCtrlMess = SIEPLookupRemote.getTrasmissioneJMS(); MessaggioModel lMessage =
		 * lCtrlMess.getMessageForIstanza(lEveId,lFascicoloModel.getIdFascicoloSiep()); //-------->>>>>>>>>>>
		 * Inserire un meccanismo di reperimento della BDI a partire //da un ufficio qualsiasi
		 * >>>>>>>>>>>>>>>>>>>>-----------------------------------------
		 * lMessage.setDescrBdiDestinataria(lBDI.getDescrComune());
		 * lMessage.setCodBdiDestinataria(lBDI.getCodUfficio()); //>>>>>>>>> Destinazxione fissa e non da
		 * maschera....... lMessage.setCodBdiMittente(lLocal.getCodDistretto());
		 * lMessage.setCodUfficioDestinatario(lCodiceUfficio);
		 * lMessage.setCodUfficioMittente(this.getCodUfficioUtenteConnesso());
		 * lMessage.setTipoMessaggio(this.RICHIESTA); lMessage.setTipoOperazione("TRASFERIMENTO ISTANZA");
		 * lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
		 * lMessage.setDataInvio(DateUtils.getSysDate()); SIAPSender lSender = new SIAPSender();
		 * lSender.send(lMessage); setRequestAttribute("IDEvento",lEveId.toString());
		 * setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Istanza sottomessa al Sistema!");
		 * 
		 * return IWebConstants.PG_MESSAGE;
		 */

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("<------------ fine ------------>");

		return PG_DETTAGLIO_TRASFERISCI_NUOVAISTANZA;
	}
}