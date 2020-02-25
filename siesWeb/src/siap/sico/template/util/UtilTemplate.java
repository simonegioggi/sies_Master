package siap.sico.template.util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.util.SICOLookupRemote;
import f3b.log.LogF3B;
import f3b.model.DecodeModel;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActUtilTemplate
 * </p>
 * <p>
 * Description: Classe Action contenente funzioni di utilità per la gestione dei template
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class UtilTemplate {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * La funzione effettua una ricerca dei template dall'Evento. Viene passata alla request una Combo Box
	 * contenente la Lista dei Template associati ad un Provvedimento e il template automatico se previsto.
	 * con la lista dei template associati ad un tipo di Provvedimento.
	 */
	public static Option listaCbxTemplate(BigDecimal alIdEvento) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UtilTemplate.listaCbxTemplate : inizio");

		// Opzione restituita
		Option lOptTemplate = new Option();

		// Ricerca dell'evento
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEvento = lEveCtrl.ExRicercaEventoByKey(alIdEvento);

		// Individuazione del Codice Oggetto Procedimento (contenuto) dal Cod Motivo
		Collection lOggetti = DecodificheManager.getInstance().getMotivoProvvedimento();
		String lCodOggProc = DecodificheUtils.getCodAltebyCode(lOggetti, lEvento.getCodMotivo());
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Cod Oggetto Proc ->" +lCodOggProc);

		// I criteri di ricerca vengono inseriti in un Template Model
		TemplateModel lTempRic = new TemplateModel();
		lTempRic.setCodOggettoProcedimento(lCodOggProc);
		lTempRic.setCodTipoEvento(lEvento.getCodTipoEvento());
		lTempRic.setCodTipoProvvedimento(lEvento.getCodTipoProvvedimento());
		// Il template può essere filtrato anche per magistrato
		if (lEvento.getCodMagistrato() != null && lEvento.getCodMagistrato().length() > 1)
			lTempRic.setCodMagistrato(lEvento.getCodMagistrato());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ricerca lista template -> " + lTempRic);

		ITemplate lTemCtrl = SICOLookupRemote.getTemplateRemote();
		Vector lTemplate = lTemCtrl.ExListaCbxTemplate(lTempRic);

		if (lTemplate.size() > 0) {

			lTempRic.setCodEsito(lEvento.getCodEsito());
			lTempRic.setCodMotivo(lEvento.getCodMotivo());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricerca template automatico -> " + lTempRic);
			Vector lTemplateAuto = lTemCtrl.ExListaCbxTemplate(lTempRic);

			if (lTemplateAuto.size() > 0) {
				// template automatico
				DecodeModel lAutoTemp = (DecodeModel) lTemplateAuto.firstElement();

				// setRequestAttribute( "AutoTemplate", "" + lAutoTemp.getDescription() );
				lOptTemplate = new Option(lTemplate, lAutoTemp.getCode());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("template automatico -> " + lAutoTemp.getDescription());

			} else
				lOptTemplate = new Option(lTemplate);
			// setRequestAttribute( "elencoTemplate", "" + lOptTemplate );
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Nessun Template trovato");
			// throw new F3BException(F3BException.EX_NOT_FOUND,"Nessun Template trovato");
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UtilTemplate.listaCbxTemplate : fine");

		return lOptTemplate;
	}

	/**
	 * La funzione effettua una ricerca dei template dal Codice Provvedimento. Viene passata alla request una
	 * Combo Box contenente la Lista dei Template associati ad un Provvedimento con la lista dei template
	 * associati ad un tipo di Provvedimento.
	 */
	public static Option listaTemplateByCodProvv(String aCodTipoProvvedimento, String aFlagTemplate)
			throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UtilTemplate.listaTemplateByCodProvv : inizio");

		// Opzione restituita
		Option lOptTemplate = new Option();

		// I criteri di ricerca vengono inseriti in un Template Model
		TemplateModel lTempRic = new TemplateModel();
		lTempRic.setCodTipoProvvedimento(aCodTipoProvvedimento);
		// Il template può essere filtrato anche per magistrato
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ricerca lista template per provvedimento -> " + lTempRic);

		if (aFlagTemplate != null)
			lTempRic.setFlagTemplate(aFlagTemplate);

		ITemplate lTemCtrl = SICOLookupRemote.getTemplateRemote();
		Vector lTemplate = lTemCtrl.ExListaCbxTemplate(lTempRic);

		if (lTemplate.size() > 0) {
			lOptTemplate = new Option(lTemplate);
			// setRequestAttribute( "elencoTemplate", "" + lOptTemplate );
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Nessun Template trovato");
			// throw new F3BException(F3BException.EX_NOT_FOUND,"Nessun Template trovato");
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UtilTemplate.listaTemplateByCodProvv : fine");

		return lOptTemplate;
	}

	/**
	 * 21-05-2009
	 * 
	 * La funzione effettua una ricerca dei template dal Codice Provvedimento Sige. Viene passata alla request
	 * una Combo Box contenente la Lista dei Template associati ad un Tipo Provvedimento Sige
	 */
	public static Option listaTemplateByCodProvvSige(String aCodTipoProvvedimentoSige) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UtilTemplate.listaTemplateByCodProvvSige : inizio");

		// Opzione restituita
		Option lOptTemplate = new Option();

		// I criteri di ricerca vengono inseriti in un Template Model
		TemplateModel lTempRic = new TemplateModel();
		lTempRic.setCodTipoProvvedimentoSige(aCodTipoProvvedimentoSige);
		// Il template può essere filtrato anche per magistrato
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Ricerca lista template per provvedimento Sige -> "+ lTempRic);

		ITemplate lTemCtrl = SICOLookupRemote.getTemplateRemote();
		Vector lTemplate = lTemCtrl.ExListaCbxTemplate(lTempRic);

		if (lTemplate.size() > 0) {
			lOptTemplate = new Option(lTemplate);
			// setRequestAttribute( "elencoTemplate", "" + lOptTemplate );
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Nessun Template trovato");
			// throw new F3BException(F3BException.EX_NOT_FOUND,"Nessun Template trovato");
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UtilTemplate.listaTemplateByCodProvv : fine");

		return lOptTemplate;
	}

	/**
	 * La funzione ricerca una lista di Template in base al Codice Attività ed il Codice Incarico. I codici
	 * utilizzati per filtrare la ricerca sono quelli memorizzati nei campi COD_MOTIVO (Codice Attività) e
	 * COD_OGGETTO_PROCEDIMENTO (Codice Incarico).
	 * 
	 * @param acodIncarico
	 * @param aCodAttivita
	 * @return
	 * @throws Exception
	 */
	public static Option listaTemplateByIncaricoAttivita(String acodIncarico, String aCodAttivita)
			throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UtilTemplate.listaTemplateByIncaricoAttivita : inizio");
		// Opzione restituita
		Option lOptTemplate = new Option();

		if ((acodIncarico != null && acodIncarico.trim().length() > 0)
				|| (aCodAttivita != null && aCodAttivita.trim().length() > 0)) {

			// I criteri di ricerca vengono inseriti in un Template Model
			TemplateModel lTempRic = new TemplateModel();
			lTempRic.setCodOggettoProcedimento(acodIncarico);
			lTempRic.setCodMotivo(aCodAttivita);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricerca lista template per Incarico ed Attività -> " + lTempRic);

			ITemplate lTemCtrl = SICOLookupRemote.getTemplateRemote();
			Vector lTemplate = lTemCtrl.ExListaCbxTemplate(lTempRic);

			if (lTemplate.size() > 0) {
				lOptTemplate = new Option(lTemplate);
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nessun Template trovato");
			}
		} else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("UtilTemplate.listaTemplateByIncaricoAttivita : parametri di ricerca nulli");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UtilTemplate.listaTemplateByIncaricoAttivita : fine");

		return lOptTemplate;
	}

	/**
	 * La funzione ricerca una lista di Template in base al Codice Richiesta. Il codice utilizzato per
	 * filtrare la ricerca viene memorizzato nel campo COD_MOTIVO.
	 * 
	 * @param acodRichiesta
	 * @return
	 * @throws Exception
	 */
	public static Option listaTemplateByRichiesta(String acodRichiesta) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UtilTemplate.listaTemplateByRichiesta : inizio");
		// Opzione restituita
		Option lOptTemplate = new Option();

		if ((acodRichiesta != null && acodRichiesta.trim().length() > 0)) {

			// I criteri di ricerca vengono inseriti in un Template Model
			TemplateModel lTempRic = new TemplateModel();
			lTempRic.setCodMotivo(acodRichiesta);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Ricerca lista template per Richiesta -> " + lTempRic);

			ITemplate lTemCtrl = SICOLookupRemote.getTemplateRemote();
			Vector lTemplate = lTemCtrl.ExListaCbxTemplate(lTempRic);

			if (lTemplate.size() > 0) {
				lOptTemplate = new Option(lTemplate);
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Nessun Template trovato");
			}
		} else
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("UtilTemplate.listaTemplateByRichiesta :manca il parametro di ricerca ");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UtilTemplate.listaTemplateByRichiesta : fine");

		return lOptTemplate;
	}

	/**
	 * La funzione effettua una ricerca nella tabella TEMPLATE in base alle opzioni di filtro selezionate nel
	 * TemplateModel argomento della funzione. Restituisce un elenco dei templates trovati in forma di Option
	 * utile per costruire una combo-box. Se la ricerca non da alcun risultato la Option restituita sarà
	 * vuota.
	 * 
	 * @param lTempRic
	 * @return Option
	 * @throws Exception
	 */

	public static Option listaCbxTemplate(TemplateModel aTempRic) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UtilTemplate.listaCbxTemplate Ricerca generica: inizio");

		// Opzione restituita
		Option lOptTemplate = new Option();

		ITemplate lTemCtrl = SICOLookupRemote.getTemplateRemote();
		Vector lTemplate = lTemCtrl.ExListaCbxTemplate(aTempRic);

		if (lTemplate.size() > 0) {
			lOptTemplate = new Option(lTemplate);
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Nessun Template trovato");
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("UtilTemplate.listaCbxTemplate Ricerca generica: fine");

		return lOptTemplate;
	}

}