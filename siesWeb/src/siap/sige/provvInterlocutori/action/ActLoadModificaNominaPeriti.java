package siap.sige.provvInterlocutori.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.detenzione.controller.IFasSigeDetenzione;
import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.richiestaatti.action.ICostantiRichiestaAtti;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadModificaNominaPeriti
 * </p>
 * <p>
 * Description: Classe Action per la Modifica Nomina Periti
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
@SuppressWarnings("rawtypes")
public class ActLoadModificaNominaPeriti extends ActDettaglioNominaPeriti
		implements ICostantiProvvedimentoSige, ICostantiRichiestaAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		String lRetPage = null;

		// Attivazione punto di Ritorno
		setLinkRitorno();

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");

		// Ricerca Provvedimento dalla chiave (passaggio per Parametro).
		BigDecimal lIdProvvedimento = getRequestBigDecimalParameter(CAMPO_ID_PROVVEDIMENTO_SIGE);

		// ricerca provvedimento
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvEvento = lCtrlProv.ExRicercaProvvedimentoById(lIdProvvedimento);
		// MERGE v10: nuova gestione ritorno
		if (!Utils.isPresent(lProvEvento)) {
			lProvEvento = lCtrlProv.ExRicercaProvvedimentoDefinitorioByIdFascicolo(
					lFasEsteso.getFascicoloSige().getIdFascicoloSige());
			lRetPage = ICostantiProvvInterlocutoriSige.PG_LOAD_DECRETO_LATITANZA;
			// 20170907: [SG] prevenzione nullpointer
			if (Utils.isNullObj(lProvEvento))
				return lRetPage;
		}

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

		// 20190519 [SG]: gestione tenori
		// Rimozione dell'elenco Tenori dalla sessione
		// removeSessionAttribute("tenori");
		if (isRequestParameterNullObj(IWebConstants.FLAG_RITORNO) || isSessionAttributeNullObj("tenori")) {
			// Ricerca tenori attivi
			TenoreSigeModel lTenore = new TenoreSigeModel();
			lTenore.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
			ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
			Vector lTenori = lTenCtrl.ExRicercaTenoriEstesiAttivi(lTenore);
			setSessionAttribute("tenori", lTenori);
		}

		// Combo per la definizione del tipo Giudizio.
		setComboTipoGiudizio();

		// Avvocati attuali assegnati al fascicolo SIGE.
		FascicoloSigeUtils lFasSigeUtils = new FascicoloSigeUtils();
		Vector lAvvocati = lFasSigeUtils.ricercaAvvocati(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		if (lAvvocati.size() > 0)
			setRequestAttribute("avvocato", lAvvocati);

		// Magistrato Assegnatario
		MagistratoAssegnatarioModel lMagAss = lFasEsteso.getMagAssegnatario();
		setRequestAttribute("magistratoassegnatario", lMagAss);

		// Imposta gli oggetti nella request.
		if (lProvEvento != null && lProvEvento.getEventoNotifica() != null
				&& lProvEvento.getEventoNotifica().getEvento() != null
				&& lProvEvento.getEventoNotifica().getEvento().getIdEvento() != null) {
			setRequestAttribute("IdEvento", lProvEvento.getEventoNotifica().getEvento().getIdEvento());
		}

		// Valorizzazione eventuale bottone di ritorno.
		if (!this.isRequestParameterNullObj("acdest")) {
			setRequestAttribute("acdest", getRequestStringParameter("acdest"));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(getRequestStringParameter("acdest"));
		}

		// Collegio
		if (lProvEvento != null && lProvEvento.getProvvedimento() != null
				&& lProvEvento.getProvvedimento().getColIdCollegio() != null)
			ricercaCollegio(lProvEvento.getProvvedimento().getColIdCollegio(),
					getUfficioUtenteConnesso().getCodTipoUfficio());

		// Lettura delle notifiche.
		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		Vector lVect = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento(
				lProvEvento.getEventoNotifica().getEvento().getIdEvento());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(">>>>> Numero notifiche = " + lVect.size());
		setRequestAttribute("notifiche", lVect);

		if (lProvEvento.getProvvedimento().getCodTipoProvvedimentoSige().equalsIgnoreCase("13")
				|| lProvEvento.getProvvedimento().getCodTipoProvvedimentoSige().equalsIgnoreCase("17")) {
			// Carica Tipo Destinatario in base al Tipo Ufficio.
			String strTipoDest = lFasSigeUtils
					.leggiTipoDestinatario(this.getUfficioUtenteConnesso().getCodTipoUfficio());
			setRequestAttribute("TipoDest", strTipoDest);

			// Preparazione delle COMBO per i Destinatari
			// Preleva elenco degli altri destinatari.
			Option lOptionAut = new Option();
			lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(), 75);

			// Ricerca LUOGO DETENZIONE
			IFasSigeDetenzione lDetenzioneCtrl = SIGELookupRemote.getFasSigeDetenzioneRemote();
			FasSigeDetenzioneModel lDetenzione = lDetenzioneCtrl
					.ExRicercaUltimaDetenzioneFascicolo(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
			setRequestAttribute("detenzione", lDetenzione);

			// MERGE v10: oggetto non usato!!!
			// LISTA UFFICI SOGGETTO
			// Option lOptionSog = new Option();
			// if (lDetenzione != null
			// && lDetenzione.getLuogoDetenzione() != null
			// && lDetenzione.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto().length() > 0)
			// lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), lDetenzione
			// .getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto(), 75);
			// else
			// lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), 75);

			// LISTA UFFICI
			Collection<DecodificheModel> lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
			String[] lStringFilter = { "-", "22" };
			Option lOptionAvv = new Option(lTipoIstituto, "22", 75);
			lOptionAvv.setFilter(lStringFilter);

			setRequestAttribute("luogodet", lDetenzione);
			setRequestAttribute("TipiIstituti1", "" + lOptionAvv);
			setRequestAttribute("tipoAutorita", lOptionAut.toString());
		}

		lockApplicativo("Emissione_Provvedimento");

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

		if (lProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(ICostantiProvvedimentoSige.COD_NOMINA_PERITI))
			lRetPage = ICostantiProvvInterlocutoriSige.PG_LOAD_MODIFICA_PERITI;
		else if (lProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(ICostantiProvvedimentoSige.COD_CITAZIONE_TESTI))
			lRetPage = ICostantiProvvInterlocutoriSige.PG_MODIFICA_CITAZIONE_TESTI;
		else if (lProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(ICostantiProvvedimentoSige.COD_DECRETO_LATITANZA))
			lRetPage = ICostantiProvvInterlocutoriSige.PG_MODIFICA_DECRETO_LATITANZA;
		else if (lProvEvento.getProvvedimento().getCodTipoProvvedimentoSige()
				.equalsIgnoreCase(ICostantiProvvedimentoSige.COD_DECRETO_IRREPERIBILITA))
			lRetPage = ICostantiProvvInterlocutoriSige.PG_MODIFICA_DECRETO_IRREPERIBILITA;

		return lRetPage;
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