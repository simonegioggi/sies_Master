package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.template.util.UtilTemplate;
import siap.sico.util.SICOLookupRemote;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.magistrato.action.ICostantiMagistrato;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioMagistratoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActDettaglioOrdinanza
 * </p>
 * <p>
 * Description: Classe Action per il Dettaglio Decreto Inammissibilità
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadModificaDecretoInamissibilita extends ActModificaOrdinanza
		implements ICostantiProvvedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActDettaglioDecretoInammissibilita: inizio");

		// Attivazione punto di Ritorno
		setLinkRitorno();

		// Impostazione della jsp.

		// Rimozione dell'elenco Tenori dalla sessione
		removeSessionAttribute("tenori");

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		// Ricerca Provvedimento dalla chiave (passaggio per Parametro).
		BigDecimal lIdProvvedimento = getRequestBigDecimalParameter(CAMPO_ID_PROVVEDIMENTO_SIGE);

		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvEvento = lCtrlProv.ExRicercaProvvedimentoById(lIdProvvedimento);

		// Modifica del 08/03/2017 INIZIO *******************
		// preleva i dati della udienza sige
		if (lProvEvento.getProvvedimento() != null
				&& lProvEvento.getProvvedimento().getUdiIdUdienzaSige() != null) {
			IUdienzaSige ctrlUdiSige = SIGELookupRemote.getUdienzaSigeRemote();
			UdienzaSigeModel udienza = ctrlUdiSige
					.ExRicercaUdienzaSigeById(lProvEvento.getProvvedimento().getUdiIdUdienzaSige());
			lProvEvento.getProvvedimento().setUdienzaSige(udienza);
		}
		// Modifica del 08/03/2017 FINE *******************

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
		lTempRic.setCodTipoProvvedimentoSige(ICostantiProvvedimentoSige.COD_DECRETO_INAMMISSIBILITA);

		// lProSigeUtils.gestioneTemplate(lTempRic);
		gestioneTemplate(lTempRic);

		// Valorizzazione eventuale bottone di ritorno.
		if (!isRequestParameterNullObj("acdest")) {
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

		listaMotivi();
		setComboTipoGiudizio();

		// In caso di Udienza gia fissata si visualizzano i dati del collegio.
		// Eventuale lettura del collegio.
		CollegioModel lColMod = null;
		if (lFasEsteso.getUdienzaProcedimento() != null
				&& lFasEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige() != null) {
			ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
			lColMod = lCtrl.ExRicercaCollegioByIdUdienzaSige(
					lFasEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige());
		}
		setRequestAttribute("collegio", lColMod);
		loadTenoriProvvedimento();

		// Modifica del 08/03/2017 *** INIZIO ******
		// Si Imposta l'Ufficio Competente.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		// lOption.setFilter( new String[] {"-", "TDS", "UDS", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID",
		// "CSS", "GIPMI", "GIP", "GIPM", "GP", "GUP", "GUPM", "GUPMI", "PT", "PM", "PMM", "PMPT", "PGCAP",
		// "PGMI", "PGMID", "PMI", "TRIBSD", "CAPSM", "TMI", "DIB", "DIBM"} ); //solo le Autorità Emittenti.
		lOption.setFilter(new String[] { "-", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID", "CSS", "GIPMI", "GIP",
				"GIPM", "GP", "GUP", "GUPM", "GUPMI", "PT", "TRIBSD", "CAPSM", "TMI", "DIB", "DIBM", "PM",
				"PGCAP" }); // solo le Autorità Emittenti.
		setRequestAttribute("tipoUfficioCompetente", "" + lOption);

		String lTipoGiudizio = "-";
		// il tipo giudizio va definito quando si definisce l'udienza
		if (lTipoGiudizio == null || "".equals(lTipoGiudizio) || "-".equals(lTipoGiudizio)) {
			// altrimenti si verifica quello in precedenza selezionato nella definizione del procedimento
			lTipoGiudizio = (lFasEsteso.getFascicoloSige().getCodTipoGiudizio() == null ? "-"
					: lFasEsteso.getFascicoloSige().getCodTipoGiudizio().trim());
		}

		String lCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		// Carica Combo x TipoGiudizio.
		// setComboTipoGiudizio();
		Option lOptionGiudizio = getComboTipoGiudizio(lTipoGiudizio, lCodTipoUfficio);
		setRequestAttribute("tipoGiudizioVal", lTipoGiudizio);
		setRequestAttribute("tipoGiudizio", lOptionGiudizio.toString());

		// Combo per l'Ufficio Competente.
		setComboUfficioCompetente();

		IUdienzaSige ctrlUdiSige = SIGELookupRemote.getUdienzaSigeRemote();
		UdienzaSigeModel udienza = ctrlUdiSige
				.ExRicercaUdienzaSigeById(lProvEvento.getProvvedimento().getUdiIdUdienzaSige());
		super.setRequestAttribute("UdienzaSige", udienza);
		// Modifica del 08/03/2017 *** FINE ******

		// INTERVENTO PER 11.2.1
		// controllo e gestione dell'eventuale aggiornamento del magistrato assegnatario
		MagistratoAssegnatarioMagistratoModel lMagistrato = new MagistratoAssegnatarioMagistratoModel();
		// MagistratoAssegnatarioModel llMagModRet = null;
		// vado in else quando l'udienza associata a tale fascicolo, è utilizzata anche da altri fascicoli
		// in questo caso il record dell'udienza sulla tabella udienza_sige NON E' MODIFICABILE
		// quindi ogni cambio del magistrato, o procuratore oppure del cancelliere deveno essere inserite
		// sulla tabella magistrato_assegnatario per lo specifico idFascicolo
		String codMagPrecedente = "";
		if (getFascicoloSigeEstesoInSessione().getMagAssegnatario() != null
				&& getFascicoloSigeEstesoInSessione().getMagAssegnatario().getMagCodMagistrato() != null) {
			codMagPrecedente = getFascicoloSigeEstesoInSessione().getMagAssegnatario().getMagCodMagistrato();
		}
		String codMagNuovo = "";
		if (!isRequestParameterNullEmptyObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)) {
			codMagNuovo = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
			// 20190519 [SG]: aggiunto controllo di consistenza
		} else if (!isRequestParameterNullEmptyObj(ICostantiUdienzaSige.CAMPO_COD_GIUDICE)) {
			codMagNuovo = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_GIUDICE);
		}
		// se sono diversi lo sostituisce, altrimento no
		if (!codMagNuovo.equals(codMagPrecedente) && !codMagNuovo.equals("")) {
			lMagistrato = prepareModificaAssegnatarioModel(
					getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige());
			MagistratoModel magMod = new MagistratoModel();
			magMod.setCodMagistrato(codMagPrecedente);
			lMagistrato.setMagistrato(magMod);
			IMagistratoAssegnatario lCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
			/* llMagModRet = */lCtrl.ExInserisciAggiornaMagistratoAssegnatario(lMagistrato);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				"ActDettaglioDecretoInammissibilita: -> page: " + PG_LOAD_MODIFICA_DECRETO_INAMMISSIBILITA);
		return PG_LOAD_MODIFICA_DECRETO_INAMMISSIBILITA; // restituisce la jsp di VIEW
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
					lStampabile = "NO";
					break;
				}
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Modificabile : " + lModificabile);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Stampabile : " + lStampabile);

		super.setRequestAttribute("Modificabile", lModificabile);
		super.setRequestAttribute("Stampabile", lStampabile);
	}

	// Funzione per la costruzione della combo con i template di stampa previsti.
	private void gestioneTemplate(TemplateModel lTempRic) throws Exception {

		Option lOptTemplate = null;

		lOptTemplate = UtilTemplate.listaTemplateByCodProvvSige(lTempRic.getCodTipoProvvedimentoSige());
		setRequestAttribute(ICostantiTemplate.CAMPO_COMBO_TEMPLATE, "" + lOptTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ElencoTemplate nella Combo -> " + lOptTemplate);

		// template di default
		String[] lSelected = lOptTemplate.getSelecteds();
		if (lSelected != null && lSelected.length > 0) {
			setRequestAttribute(ICostantiTemplate.CAMPO_DEFAULT_TEMPLATE, lSelected[0]);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("TemplateDiDefault -> " + lSelected[0]);
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

	private void listaMotivi() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".listaMotivi: inizio");
		String lTipoUff = getUfficioUtenteConnesso().getCodTipoUfficio().toUpperCase();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("tipo ufficio ->" + lTipoUff);

		// Chiamata Controller per la ricerca delle Decodifiche.
		IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();
		Vector lVect = new Vector(lDecCtrl.ExListaMotiviInammissibilitaxSottoSistema(lTipoUff, "SIGE"));
		setRequestAttribute("motivi", lVect);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".listaMotivi: fine");
	}

	private void loadTenoriProvvedimento() throws F3BException {

		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");
		TenoreSigeModel lTenore = new TenoreSigeModel();
		lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
		Vector lTenori = lTenCtrl.ExRicercaTenoriEstesiAttivi(lTenore);

		// 25/05/2010 in caso di assenza di tenori attivi, si ripristinano quelli della richiesta SIGE.
		if (lTenori.size() == 0 && lFasEsteso.getRichiestaSige() != null)

			lTenori = lTenCtrl
					.ExRicercaTenoreEstesoByRichiesta(lFasEsteso.getRichiestaSige().getIdRichiestaSige());
		setSessionAttribute("tenori", lTenori);
	}

}