package siap.sige.provvInterlocutori.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.template.util.UtilTemplate;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.tenore.util.TenoriSigeUtil;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActDettaglioCitazioneTesti
 * </p>
 * <p>
 * Description: Classe Action per il Dettaglio Citazione Testi
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
public class ActDettaglioOrdinanzaConflittoCompetenza extends ActionSige implements
		ICostantiProvvedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActDettaglioOrdinanzaConflittoCompetenza: inizio");
		BigDecimal lIdProvvedimento = null;

		// Attivazione punto di Ritorno
		setLinkRitorno();

		// Rimozione dell'elenco Tenori dalla sessione
		removeSessionAttribute("tenori");

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		// Ricerca Provvedimento dalla chiave (passaggio per Parametro).
		lIdProvvedimento = getRequestBigDecimalParameter(CAMPO_ID_PROVVEDIMENTO_SIGE);

		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvEvento = lCtrlProv.ExRicercaProvvedimentoById(lIdProvvedimento);

		// preleva i dati della udienza sige
		if (lProvEvento.getProvvedimento() != null
				&& lProvEvento.getProvvedimento().getUdiIdUdienzaSige() != null) {
			IUdienzaSige ctrlUdiSige = SIGELookupRemote.getUdienzaSigeRemote();
			UdienzaSigeModel udienza = ctrlUdiSige.ExRicercaUdienzaSigeById(lProvEvento.getProvvedimento()
					.getUdiIdUdienzaSige());
			lProvEvento.getProvvedimento().setUdienzaSige(udienza);
		}

		setRequestAttribute("ProvvedimentoEvento", lProvEvento);
		super.setRequestAttribute("idProvvedimentoTitoliEsecutivi", lIdProvvedimento);

		// Ricerca tenori legati al provvedimento
		TenoreSigeModel lTenore = new TenoreSigeModel();
		lTenore.setProvIdProvvedimentoSige(lIdProvvedimento);

		// ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
		// Vector lTenori = lTenCtrl.ExRicercaTenori(lTenore);

		// TenoreSigeModel lTenore = new TenoreSigeModel();
		// lTenore.setProvIdProvvedimentoSige(mProvEvento.getProvvedimento().getProvvIdProvvedimentoSige());
		lTenore.setProvIdProvvedimentoSige(lIdProvvedimento);
		ITenoreSige ctrTS = SIGELookupRemote.getTenoreSigeRemote();
		Vector<TenoreSigeEstesoModel> lTenoriEstesi = ctrTS.ExRicercaTenoriEstesiAttivi(lTenore);
		TenoriSigeUtil tsu = new TenoriSigeUtil();
		List<TenoreSigeModel> lTenori = tsu.listaTenoriDaListaTenoriEstesi(lTenoriEstesi);

		setRequestAttribute("tenori", lTenori);
		setRequestAttribute("tenoriEstesi", lTenoriEstesi);

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
		TemplateModel lTempRic = new TemplateModel();
		lTempRic.setCodTipoProvvedimentoSige(ICostantiProvvedimentoSige.COD_ORDINANZA_CONFLITTO_COMPETENZA);

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
			ricercaCollegio(lProvEvento.getProvvedimento().getColIdCollegio(), getUfficioUtenteConnesso()
					.getCodTipoUfficio());

		// Lettura delle notifiche.
		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		Vector lVect = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento(lProvEvento.getEventoNotifica()
				.getEvento().getIdEvento());
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.info(">>>>> Numero notifiche = "+lVect.size());
		setRequestAttribute("notifiche", lVect);

		// Carica Combo x TipoGiudizio.
		// setComboTipoGiudizio();
		if (lFasEsteso != null && lFasEsteso.getFascicoloSige() != null
				&& lFasEsteso.getFascicoloSige().getCodTipoGiudizio() != null) {
			String lTipoGiudizio = (lFasEsteso.getFascicoloSige().getCodTipoGiudizio() == null ? "-"
					: lFasEsteso.getFascicoloSige().getCodTipoGiudizio().trim());
			String lCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
			Option lOptionGiudizio = getComboTipoGiudizio(lTipoGiudizio, lCodTipoUfficio);
			lOptionGiudizio.setSelected(lFasEsteso.getFascicoloSige().getCodTipoGiudizio());
			setRequestAttribute("tipoGiudizio", lOptionGiudizio.toString());
		}

		return ICostantiProvvInterlocutoriSige.PG_DETTAGLIO_ORDINANZA_CONFLITTO_COMPETENZA;
	}

	private void ModificabileStampabile(FascicoloSigeEstesoModel lFasEsteso,
			ProvvedimentoSigeEventoModel lProvEvento, List<TenoreSigeModel> lTenori) throws Exception {

		String lStampabile = "NO";
		String lModificabile = "NO";
		String lCancellabile = "NO";

		// Fascicolo Modificabile e Provvedimento non validato
		if (getCodUfficioUtenteConnesso().equalsIgnoreCase(lFasEsteso.getFascicoloSige().getChiaveUfficio())
				&& (lProvEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() == null || lProvEvento
						.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0)) {
			lStampabile = "SI";
			lModificabile = "SI";
			lCancellabile = "SI";

			// Solo se tutti gli oggetti sono definiti la stampa è possibile.
			for (TenoreSigeModel lTenore : lTenori) {
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
		super.setRequestAttribute("Cancellabile", lCancellabile);
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