package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActElencoSolleciti
 * </p>
 * <p>
 * Description: Azione specializzazione per la ricerca dei solleciti
 * <p>
 * Copyright: Bull Italia S.p.A.Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * 
 * @version 1.0
 */

public class ActElencoSolleciti extends ActionSiap implements ICostantiRichiestaAtti {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		BigDecimal lIdProvvedimento = null;

		lIdProvvedimento = getRequestBigDecimalParameter(
				ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE);

		// Ricerca Provvedimento dalla chiave
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvEvento = lCtrlProv.ExRicercaProvvedimentoById(lIdProvvedimento);

		// BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		BigDecimal lIdEvento = lProvEvento.getEventoNotifica().getEvento().getIdEvento();
		// Chiama il controller.
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNot = lCtrl.ExRicercaEventoNotificaByKeyForRichiestaAtti(lIdEvento);

		// Ricerca del documento allegato
		IDocumentoAllegato lCtrlDoc = SIUSLookupRemote.getDocumentoAllegatoRemote();
		Vector<DocumentoAllegatoModel> lDocs = lCtrlDoc.ExRicercaSollecitoByIdEvento(lIdEvento);

		setRequestAttribute("eventoNotifica", lEveNot);
		setRequestAttribute("allegati", lDocs);

		// ANGELA

		// Creazione della combobox, nell'evento non sia già impostato uno
		// specifico template. Si è adottata tale soluzione che impatta
		// su tutte le funzionalità di stampa, al fine di evitare che si debba
		// legarsi a condizioni di if sui determinati codici motivo.

		// if ( lEveNot.getEvento().getTemIdTemplate()==null)
		// {
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(">>> Il CodMotivo nell'evento è : " + lEveNot.getEvento().getCodMotivo());

		// ITemplate lCtrlTemplate = SICOLookupRemote.getTemplateRemote();
		// TemplateModel lTemplateMod = new TemplateModel();

		// lTemplateMod.setCodMotivo(lEveNot.getEvento().getCodMotivo());

		// Collection lTemplates = lCtrlTemplate.ExListaCbxTemplate(lTemplateMod);
		// Prepara la ComboBox
		// Option lOptionTemp = new Option( lTemplates );

		// setRequestAttribute(ICostantiTemplate.CAMPO_COMBO_TEMPLATE,lOptionTemp.toString());

		// }

		// si ricava l'eventuale Action di ritorno dalla jsp
		if (!isRequestParameterNullObj("CAMPO_ACTION_RET"))
			setRequestAttribute("actRet", getRequestStringParameter("CAMPO_ACTION_RET"));

		return PG_ELENCOSOLLECITI;
	}

}