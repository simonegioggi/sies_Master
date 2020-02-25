package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.template.util.UtilTemplate;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.richiestaatti.action.ICostantiRichiestaAtti;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActDettaglioOrdinanza
 * </p>
 * <p>
 * Description: Classe Action per il Dettaglio Richiesta Rogatoria MdS
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActDettaglioOrdineTraduzione extends ActLoadEmissioneOrdinanzaIncompetenza
		implements ICostantiProvvedimentoSige, ICostantiRichiestaAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActDettaglioOrdineTraduzione: inizio");
		BigDecimal lIdProvvedimento = null;

		// Attivazione punto di Ritorno
		setLinkRitorno();

		// Impostazione della jsp.

		// Rimozione dell'elenco Tenori dalla sessione
		removeSessionAttribute("tenori");

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		// Ricerca Provvedimento dalla chiave (passaggio per Parametro).
		lIdProvvedimento = getRequestBigDecimalParameter(CAMPO_ID_PROVVEDIMENTO_SIGE);

		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvEvento = lCtrlProv.ExRicercaProvvedimentoById(lIdProvvedimento);

		setRequestAttribute("ProvvedimentoEvento", lProvEvento);

		// Ricerca tenori legati al provvedimento
		TenoreSigeModel lTenore = new TenoreSigeModel();
		lTenore.setProvIdProvvedimentoSige(lIdProvvedimento);

		ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
		Vector lTenori = lTenCtrl.ExRicercaTenori(lTenore);

		setRequestAttribute("tenori", lTenori);

		// Avvocati attuali assegnati al fascicolo SIGE.
		FascicoloSigeUtils lFasSigeUtils = new FascicoloSigeUtils();
		Vector lAvvocati = lFasSigeUtils.ricercaAvvocati(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		if (lAvvocati.size() > 0)
			setRequestAttribute("avvocato", lAvvocati);

		// Magistrato Assegnatario
		MagistratoAssegnatarioModel lMagAss = lFasEsteso.getMagAssegnatario();
		setRequestAttribute("magistratoassegnatario", lMagAss);

		// Imposta gli oggetti nella request.
		setRequestAttribute("IdEvento", lProvEvento.getEventoNotifica().getEvento().getIdEvento());

		// Impostazione Combo Template
		// ProvvedimentoSigeUtils lProSigeUtils = new ProvvedimentoSigeUtils();
		TemplateModel lTempRic = new TemplateModel();
		// lTempRic.setCodTipoProvvedimentoSige(ICostantiProvvedimentoSige.COD_ORDINANZA_INCOMPETENZA);
		lTempRic.setCodTipoProvvedimentoSige("53");

		// lProSigeUtils.gestioneTemplate(lTempRic);
		this.gestioneTemplate(lTempRic);

		// Valorizzazione eventuale bottone di ritorno.
		if (!this.isRequestParameterNullObj("acdest")) {
			setRequestAttribute("acdest", getRequestStringParameter("acdest"));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(getRequestStringParameter("acdest"));
		}

		// Impostazione parametri di controllo gestione Modifica e Stampa.
		ModificabileStampabile(lFasEsteso, lProvEvento, lTenori);

		// Collegio
		if (lProvEvento != null && lProvEvento.getProvvedimento() != null
				&& lProvEvento.getProvvedimento().getColIdCollegio() != null)
			ricercaCollegio(lProvEvento.getProvvedimento().getColIdCollegio(),
					getUfficioUtenteConnesso().getCodTipoUfficio());

		// Leggo le notifiche
		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		// chiama il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl
				.ExRicercaEventoNotificaByKey(lProvEvento.getEventoNotifica().getEvento().getIdEvento());

		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		Vector lNots = lCtrlNot
				.ExRicercaNotificaByKeyEvento(lProvEvento.getEventoNotifica().getEvento().getIdEvento());
		lEveMod.setNotifiche((NotificaModel[]) lNots.toArray(new NotificaModel[0]));

		// return lRetPage; //restituisce la jsp di VIEW

		// Leggo il Destinatario
		String lUfficio = null;
		if (lEveMod.getNotifiche().length > 0) {
			lUfficio = lEveMod.getNotifiche()[0].getUffCodUfficio();
		}

		UfficioModel lUffMod = new UfficioModel();

		if (lUfficio != null) {
			IUfficio lUff = SICOLookupRemote.getUfficioRemote();
			lUffMod = lUff.getUfficioByKey(lEveMod.getNotifiche()[0].getUffCodUfficio().toUpperCase());
		}
		setRequestAttribute("ufficio", lUffMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(">>>TIPO UFFICIO : " + lUffMod.getDescrTipoUfficio());

		return ICostantiRichiestaAtti.PG_DETTAGLIO_ORDINE_TRADUZIONE;
	}

	private void ModificabileStampabile(FascicoloSigeEstesoModel lFasEsteso,
			ProvvedimentoSigeEventoModel lProvEvento, Vector lTenori) throws Exception {
		String lStampabile = "NO";
		String lModificabile = "NO";

		// Fascicolo Modificabile e Provvedimento non validato
		if (getCodUfficioUtenteConnesso().equalsIgnoreCase(lFasEsteso.getFascicoloSige().getChiaveUfficio())
				&& (lProvEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() == null
						|| lProvEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato()
								.compareTo("N") == 0)) {
			lStampabile = "SI";
			lModificabile = "SI";

			// Solo se tutti gli oggetti sono definiti la stampa è possibile.
			Iterator itx = lTenori.iterator();
			while (itx.hasNext()) {
				TenoreSigeModel lTenore = (TenoreSigeModel) itx.next();
				if (lTenore.getCodEsitoSige() == null) {
					// lStampabile = "NO";
					break;
				}
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Modificabile : " + lModificabile);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("Stampabile : " + lStampabile);

		super.setRequestAttribute("Modificabile", lModificabile);
		super.setRequestAttribute("Stampabile", lStampabile);
	}

	// Funzione per la costruzione della combo con i template di stampa previsti.
	private void gestioneTemplate(TemplateModel lTempRic) throws Exception {
		Option lOptTemplate = null;

		lOptTemplate = UtilTemplate.listaTemplateByCodProvvSige(lTempRic.getCodTipoProvvedimentoSige());
		setRequestAttribute(ICostantiTemplate.CAMPO_COMBO_TEMPLATE, "" + lOptTemplate.toString());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("ElencoTemplate nella Combo -> " + lOptTemplate);

		// template di default
		String[] lSelected = lOptTemplate.getSelecteds();
		if (lSelected != null && lSelected.length > 0) {
			setRequestAttribute(ICostantiTemplate.CAMPO_DEFAULT_TEMPLATE, lSelected[0]);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("TemplateDiDefault -> " + lSelected[0]);
		}
		return;
	}

	private void ricercaCollegio(BigDecimal aIdCollegio, String aCodTipoUfficioConnesso) throws Exception {
		// Chiama il controller.
		ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
		CollegioModel lColMod = lCtrl.ExRicercaCollegioByKey(aIdCollegio);

		//
		// Decisione del ruolo magistrato in virtù del tipo ufficio.
		//
		String lRuoloMagistrato = "Giudice";

		if (aCodTipoUfficioConnesso.equals("CAP") || aCodTipoUfficioConnesso.equals("CASAP")
				|| aCodTipoUfficioConnesso.equals("CAPSM") || aCodTipoUfficioConnesso.equals("DIBM"))
			lRuoloMagistrato = "Consigliere";

		// Passaggio alla request.
		if (lColMod != null)
			setRequestAttribute("collegio", lColMod);
		setRequestAttribute("ruoloMagistrato", lRuoloMagistrato);

	}

}