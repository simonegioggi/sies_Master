package siap.sius.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.xml.TreeModel;
import f3b.web.html.Option;
import siap.jms.util.ParserMessageRec;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.stampa.action.ICostantiStampaSius;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioRichiestaAtti
 * </p>
 * <p>
 * Description: Classe Responsabile della visualizzazione dati di dettaglio di un evento generato dalla
 * funzionalità della richiesta atti.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActLoadDettaglioRichiestaAtti extends ActionSius implements ICostantiRichiestaAtti {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di: recupero dati in request, lettura evento/notifiche e creazione combobox con elenco dei
	 * template.
	 * <p>
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione.
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		setLinkRitorno();

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Chiama il controller.
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNot = lCtrl.ExRicercaEventoNotificaByKeyForRichiestaAtti(lIdEvento);
		if (lEveNot == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Dato Assente !");

		setRequestAttribute("eventoNotifica", lEveNot);

		// MEV_2019-09 Se provengo dalle statistiche devo caricare in sessione il fascicolo legato all'evento
		//
		IFascicoloSius lFascicoloCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		FascicoloGPModel fgpm = lFascicoloCtrl.ExRicercaFascicoloByKey(lEveNot.getEvento().getFasSiuIdFascicoloSius());
		setSessionAttribute("fascicoloSiusGP", fgpm);
		
		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
//		int[] aTipoDati = { ICostantiStampaSius.TREE_SOGGETTO, ICostantiStampaSius.TREE_FASCICOLOSIEP,
//				ICostantiStampaSius.TREE_SENTENZA, ICostantiStampaSius.TREE_AVVOCATO,
//				ICostantiStampaSius.TREE_LUOGODET, ICostantiStampaSius.TREE_MAGISTRATO,
//				ICostantiStampaSius.TREE_UDIENZA };
		int[] aTipoDati = { ICostantiStampaSius.TREE_SOGGETTO, ICostantiStampaSius.TREE_FASCICOLOSIEP,
				ICostantiStampaSius.TREE_SENTENZA};
		TreeModel lTreeDati = lCtrlSta.ExPrelevaDatiVideo(lEveNot.getEvento().getFasSiuIdFascicoloSius(), aTipoDati);
		ParserMessageRec lParser = new ParserMessageRec(lTreeDati);
		setSessionAttribute("fascicolo", lParser.getFascicolo());
		// MEV_2019-09 - FINE
		
		// si ricava l'eventuale Action di ritorno dalla jsp
		if (!isRequestParameterNullObj("CAMPO_ACTION_RET"))
			setRequestAttribute("actRet", getRequestStringParameter("CAMPO_ACTION_RET"));

		// Modificabilità
		String lModificabile = "NO";
		String lStampabile = "NO";

		if (IsFascicoloSiusModificabile()) {
			// Stampabilità
			if (lEveNot.getEvento().getFlagDocumentoRegistrato() == null
					|| lEveNot.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
				// Modificabilità e Stampabilità coincidono
				lStampabile = "SI";
				lModificabile = "SI";
			}
		}

		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Stampabile", lStampabile);

		// Creazione della combobox, nell'evento non sia già impostato uno
		// specifico template. Si è adottata tale soluzione che impatta
		// su tutte le funzionalità di stampa, al fine di evitare che si debba
		// legarsi a condizioni di if sui determinati codici motivo.
		if (lEveNot.getEvento().getTemIdTemplate() == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>> Il CodMotivo nell'evento è : " + lEveNot.getEvento().getCodMotivo());

			ITemplate lCtrlTemplate = SICOLookupRemote.getTemplateRemote();
			TemplateModel lTemplateMod = new TemplateModel();
			lTemplateMod.setCodMotivo(lEveNot.getEvento().getCodMotivo());
			Collection lTemplates = lCtrlTemplate.ExListaCbxTemplate(lTemplateMod);
			// Prepara la ComboBox
			Option lOptionTemp = new Option(lTemplates);

			setRequestAttribute(ICostantiTemplate.CAMPO_COMBO_TEMPLATE, lOptionTemp.toString());
		}

		return PG_DETTAGLIO_RICHIESTAATTI;
	}

}