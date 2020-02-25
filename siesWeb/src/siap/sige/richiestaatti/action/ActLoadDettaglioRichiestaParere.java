package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioRichiestaParere
 * </p>
 * <p>
 * Description: Classe Responsabile della visualizzazione dati di dettaglio di un evento generato dalla
 * funzionalità della richiesta parere.
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
public class ActLoadDettaglioRichiestaParere extends ActSIESDettaglioProvvedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");

		BigDecimal lIdProvvedimento = null;
		lIdProvvedimento = getRequestBigDecimalParameter(
				ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> Il Provvedimento è : " + lIdProvvedimento);

		BigDecimal lIdFascicolo = null;

		// Fascicolo SIGE Esteso.

		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE)) {
			lIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE);
			IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
			FascicoloSigeEstesoModel lFasEsteso = lCtrl.ExRicercaEstesaFascicoloSigeByKey(
					this.getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE));

			setSessionAttribute("FascicoloSigeEsteso", lFasEsteso);
		}

		ProvvedimentoSigeModel lProvSige = new ProvvedimentoSigeModel();
		lProvSige.setFasIdFascicoloSige(lIdFascicolo);

		// Ricerca Provvedimento dalla chiave
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvEvento = lCtrlProv.ExRicercaProvvedimentoById(lIdProvvedimento);
		lProvEvento.getProvvedimento().setFasIdFascicoloSige(lIdFascicolo);

		setRequestAttribute("ProvvedimentoEvento", lProvEvento);

		BigDecimal lId = lProvEvento.getEventoNotifica().getEvento().getIdEvento();

		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		// chiama il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		// AAAAAAAAAAAAAAAAAA
		// Chiama il controller.
		// S* IEvento lCtrlR = SICOLookupRemote.getEventoRemote();
		// S* EventoNotificaModel lEveNot = lCtrlR.ExRicercaEventoNotificaByKeyForRichiestaAtti( lId );
		// S* if(lEveNot == null)
		// S* throw new SIUSException( SIUSException.USER_MESSAGE, "Dato Assente !" );

		setLinkRitorno();
		// Chiama il controller delle Motivazioni Decreto
		// IMotivazioneDecreto lMotivCtrl = SIUSLookupRemote.getMotivazioneDecretoRemote();
		// Vector lVectMotiv = lMotivCtrl.ExRicercaMotivazioniDecretoInammissibilitaByEve(lId);
		// setRequestAttribute( "motivazioniDecreto", lVectMotiv );
		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.info( ">>> MOTIVAZIONI AA : "+lVectMotiv );

		// setRequestAttribute( "eventoNotifica", lEveNot );

		Collection lColMot = (DecodificheManager.getInstance()).getMotivoProvvedimento();
		setRequestAttribute("codMotivo",
				DecodificheUtils.getDescbyCode(lColMot, lEveMod.getEvento().getCodMotivo()));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(">>> CODMOT : "
				+ DecodificheUtils.getDescbyCode(lColMot, lEveMod.getEvento().getCodMotivo()));

		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		Vector lNots = lCtrlNot
				.ExRicercaNotificaByKeyEvento(lProvEvento.getEventoNotifica().getEvento().getIdEvento());

		lEveMod.setNotifiche((NotificaModel[]) lNots.toArray(new NotificaModel[0]));
		setRequestAttribute("eventonotifica", lEveMod);

		String lUfficio = null;
		if (lEveMod.getNotifiche().length > 0) {
			lUfficio = lEveMod.getNotifiche()[0].getUffCodUfficio();
		}

		UfficioModel lUffMod = new UfficioModel();

		if (lUfficio != null) {
			IUfficio lUff = SICOLookupRemote.getUfficioRemote();
			lUffMod = lUff.getUfficioByKey(lEveMod.getNotifiche()[0].getUffCodUfficio().toUpperCase());
		}

		// ANGELA
		// Modificabilità
		String lModificabile = "NO";
		String lStampabile = "NO";

		// Stampabilità
		if (lEveMod.getEvento().getFlagDocumentoRegistrato() == null
				|| lEveMod.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
			// Modificabilità e Stampabilità coincidono
			lStampabile = "SI";
			lModificabile = "SI";
		}
		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Stampabile", lStampabile);
		setRequestAttribute("ufficio", lUffMod);

		// ANGELA
		// Creazione della combobox, nell'evento non sia già impostato uno
		// specifico template. Si è adottata tale soluzione che impatta
		// su tutte le funzionalità di stampa, al fine di evitare che si debba
		// legarsi a condizioni di if sui determinati codici motivo.
		if (lEveMod.getEvento().getTemIdTemplate() == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>> Il CodMotivo nell'evento è : " + lEveMod.getEvento().getCodMotivo());

			ITemplate lCtrlTemplate = SICOLookupRemote.getTemplateRemote();
			TemplateModel lTemplateMod = new TemplateModel();

			lTemplateMod.setCodMotivo(lEveMod.getEvento().getCodMotivo());
			lTemplateMod.setCodTipoProvvedimento("11");

			Collection lTemplates = lCtrlTemplate.ExListaCbxTemplate(lTemplateMod);
			// Prepara la ComboBox
			Option lOptionTemp = new Option(lTemplates);

			setRequestAttribute(ICostantiTemplate.CAMPO_COMBO_TEMPLATE, lOptionTemp.toString());

		}

		return ICostantiRichiestaAtti.PG_DETTAGLIO_RICHIESTAPARERE;
	}

}