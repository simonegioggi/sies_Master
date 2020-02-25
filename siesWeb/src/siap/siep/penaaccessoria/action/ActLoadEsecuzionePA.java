package siap.siep.penaaccessoria.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.ComboManager;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadEsecuzionePA
 * </p>
 * <p>
 * Description: Classe Action per la load Esecuzione PenaAccessoria
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadEsecuzionePA extends ActionSiap implements ICostantiPenaAccessoria {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// gestioneRitorno();
		Collection lTipoAutorita = null;

		// Data Fascicolo SIEP
		Date lDataInserimento = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Parametri per configurare l'Esecuzione della Pena Accessoria.
		String idTipoPenaAccessoria = getRequestStringParameter(
				ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA);
		String codTipoPenaAccessoria = getRequestStringParameter(
				ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA);
		String descrTipoPenaAccessoria = getRequestStringParameter(
				ICostantiPenaAccessoria.CAMPO_DESCR_TIPO_PENA_ACCESSORIA);
		setRequestAttribute(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA, "" + idTipoPenaAccessoria);
		setRequestAttribute(ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA,
				"" + codTipoPenaAccessoria);
		setRequestAttribute(ICostantiPenaAccessoria.CAMPO_DESCR_TIPO_PENA_ACCESSORIA,
				"" + descrTipoPenaAccessoria);
		setRequestAttribute("CodMotivo", "" + "5" + codTipoPenaAccessoria.trim());

		// Impostazione di EVENTO
		// EventoNotificaModel lEveNot = new EventoNotificaModel();
		// lEveNot.getEvento().setCodTipoEvento("16"); // Comunicazione Pena Accessoria
		// lEveNot.getEvento().setCodTipoProvvedimento("12"); // Comunicazione
		// lEveNot.getEvento().setCodMotivo("5"+codTipoPenaAccessoria.trim());
		/// ???lEveNot.getEvento().setCodEsito("0112");
		// setRequestAttribute("eventoEsecuzionePA", "" + lEveNot);

		// Gestione LISTA DESTINATARI.
		if (codTipoPenaAccessoria != null) {
			switch (Integer.parseInt(codTipoPenaAccessoria.trim())) {
			case 1: // Interdizione Dai Pubblici Uffici
			{
				// DESTINATARI : SINDACO, QUESTORE, GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita, "26");
				String[] lStringFilter = { "-", "26" }; // Comune
				lOption.setFilter(lStringFilter);
				setRequestAttribute("Destinatario1", "" + lOption);

				lOption = new Option(lTipoAutorita, "20");
				String[] lStringFilter2 = { "-", "20" }; // Questura
				lOption.setFilter(lStringFilter2);
				setRequestAttribute("Destinatario2", "" + lOption);

				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario3", "" + lOption);
				setRequestAttribute("Destinatario4", "" + lOption);

				break;
			}

			case 12: // Interdizione Legale
			{
				// DESTINATARI : GIUDICE TUTELARE, UFFICIO RECUPERO CREDITI, GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita, "82");
				String[] lStringFilter = { "-", "82" }; // Giudice Tutelare
				lOption.setFilter(lStringFilter);
				setRequestAttribute("Destinatario1", "" + lOption);

				lOption = new Option(lTipoAutorita, "36");
				String[] lStringFilter2 = { "-", "36", "37", "56", "57" }; // Questura
				lOption.setFilter(lStringFilter2);
				setRequestAttribute("Destinatario2", "" + lOption);

				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario3", "" + lOption);
				setRequestAttribute("Destinatario4", "" + lOption);

				break;
			}

			case 66: // Interdizione Dall'Esercizio di Tutela e Curatela(Legge Merlin)
			{
				// DESTINATARI : GIUDICE TUTELARE, UFFICIO RECUPERO CREDITI, GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita, "82");
				String[] lStringFilter = { "-", "82" }; // Giudice Tutelare
				lOption.setFilter(lStringFilter);
				setRequestAttribute("Destinatario1", "" + lOption);

				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario2", "" + lOption);
				setRequestAttribute("Destinatario3", "" + lOption);

				break;
			}

			case 15: // Inabilitazione All'Esercizio di una Impresa Commerciale
			{
				// DESTINATARI : PREFETTO, QUESTORE, CAMERA COMMERCIO REGISTRO IMPRESE, GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita, "30");
				String[] lStringFilter = { "-", "30" }; // Prefettura
				lOption.setFilter(lStringFilter);
				setRequestAttribute("Destinatario1", "" + lOption);

				lOption = new Option(lTipoAutorita, "20");
				String[] lStringFilter2 = { "-", "20" }; // Questura
				lOption.setFilter(lStringFilter2);
				setRequestAttribute("Destinatario2", "" + lOption);

				lOption = new Option(lTipoAutorita, "83");
				String[] lStringFilter3 = { "-", "83" }; // Camera Commercio Registro Imprese
				lOption.setFilter(lStringFilter3);
				setRequestAttribute("Destinatario3", "" + lOption);

				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario4", "" + lOption);
				setRequestAttribute("Destinatario5", "" + lOption);

				break;
			}

			case 88: // Interdizione Dalle Funzioni di Rappresentanza e Assistenza in Materia Tributaria
			case 86: // Interdizione Dall'Ufficio di Componente di Commissioni Tributarie
			{
				// DESTINATARI : MINISTERO DELLE FINANZE, DIREZIONE REGIONALE PER LE ENTRATE, COMMISSIONE
				// TRIBUTARIA PROVINCIALE,
				// COMMISSIONE TRIBUTARIA REGIONALE, GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita, "84");
				String[] lStringFilter = { "-", "84" }; // Ministero delle Finanze
				lOption.setFilter(lStringFilter);
				setRequestAttribute("Destinatario1", "" + lOption);

				lOption = new Option(lTipoAutorita, "39");
				String[] lStringFilter2 = { "-", "39" }; // Direzione Regionale Delle Entrate
				lOption.setFilter(lStringFilter2);
				setRequestAttribute("Destinatario2", "" + lOption);

				lOption = new Option(lTipoAutorita, "85");
				String[] lStringFilter3 = { "-", "85" }; // Commissione Tributaria Provinciale
				lOption.setFilter(lStringFilter3);
				setRequestAttribute("Destinatario3", "" + lOption);

				lOption = new Option(lTipoAutorita, "86");
				String[] lStringFilter5 = { "-", "86" }; // Commissione Tributaria Regionale
				lOption.setFilter(lStringFilter5);
				setRequestAttribute("Destinatario4", "" + lOption);

				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario5", "" + lOption);
				setRequestAttribute("Destinatario6", "" + lOption);

				break;
			}

			case 84: // Divieto di Emettere Assegni
			{
				// DESTINATARI : QUESTORE, CENTRO COMPARTIMENTALE SERVIZI BANCO POSTA,
				// ASSOCIAZIONE BANCARIA ITALIANA, ISTITUTO DI CREDITO, GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita, "20");
				String[] lStringFilter = { "-", "20" }; // Questura
				lOption.setFilter(lStringFilter);
				setRequestAttribute("Destinatario1", "" + lOption);

				lOption = new Option(lTipoAutorita, "87");
				String[] lStringFilter2 = { "-", "87" }; // Centro Compartimentale Servizi Banco Posta
				lOption.setFilter(lStringFilter2);
				setRequestAttribute("Destinatario2", "" + lOption);

				lOption = new Option(lTipoAutorita, "88");
				String[] lStringFilter3 = { "-", "88" }; // Associazione Bancaria Italiana
				lOption.setFilter(lStringFilter3);
				setRequestAttribute("Destinatario3", "" + lOption);

				lOption = new Option(lTipoAutorita, "89");
				String[] lStringFilter5 = { "-", "89" }; // Istituto di Credito
				lOption.setFilter(lStringFilter5);
				setRequestAttribute("Destinatario4", "" + lOption);

				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario5", "" + lOption);
				setRequestAttribute("Destinatario6", "" + lOption);

				break;
			}

			case 81: // Incapacita'Di Contrattare con la Pubblica Amministrazione
			{
				// DESTINATARI : SINDACO, PREFETTO, QUESTORE, GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita, "26");
				String[] lStringFilter = { "-", "26" }; // Sindaco
				lOption.setFilter(lStringFilter);
				setRequestAttribute("Destinatario1", "" + lOption);

				lOption = new Option(lTipoAutorita, "30");
				String[] lStringFilter2 = { "-", "30" }; // Prefettura
				lOption.setFilter(lStringFilter2);
				setRequestAttribute("Destinatario2", "" + lOption);

				lOption = new Option(lTipoAutorita, "20");
				String[] lStringFilter3 = { "-", "20" }; // Questura
				lOption.setFilter(lStringFilter3);
				setRequestAttribute("Destinatario3", "" + lOption);

				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario4", "" + lOption);
				setRequestAttribute("Destinatario5", "" + lOption);

				break;
			}

			case 90: // Esclusione Dalla Borsa
			{
				// DESTINATARIO : GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita);
				setRequestAttribute("Destinatario1", "" + lOption);
				setRequestAttribute("Destinatario2", "" + lOption);

				break;
			}

			case 80: // Interdizione Dagli Uffici Direttivi Delle Persone Giuridiche e Delle Imprese
			{
				// DESTINATARI : QUESTORE, CAMERA COMMERCIO REGISTRO IMPRESE, GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();

				Option lOption = new Option(lTipoAutorita, "20");
				String[] lStringFilter = { "-", "20" }; // Questura
				lOption.setFilter(lStringFilter);
				setRequestAttribute("Destinatario1", "" + lOption);

				lOption = new Option(lTipoAutorita, "83");
				String[] lStringFilter2 = { "-", "83" }; // Camera Commercio Registro Imprese
				lOption.setFilter(lStringFilter2);
				setRequestAttribute("Destinatario2", "" + lOption);

				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario3", "" + lOption);
				setRequestAttribute("Destinatario4", "" + lOption);

				break;
			}

			case 13: // Sospensione Dall'Esercizio di una Professione
			{
				// DESTINATARI : SINDACO, PREFETTO, QUESTORE, GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita, "26");
				String[] lStringFilter = { "-", "26" }; // Sindaco
				lOption.setFilter(lStringFilter);
				setRequestAttribute("Destinatario1", "" + lOption);

				lOption = new Option(lTipoAutorita, "30");
				String[] lStringFilter2 = { "-", "30" }; // Prefettura
				lOption.setFilter(lStringFilter2);
				setRequestAttribute("Destinatario2", "" + lOption);

				lOption = new Option(lTipoAutorita, "20");
				String[] lStringFilter3 = { "-", "20" }; // Questura
				lOption.setFilter(lStringFilter3);
				setRequestAttribute("Destinatario3", "" + lOption);

				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario4", "" + lOption);
				setRequestAttribute("Destinatario5", "" + lOption);

				break;
			}
			case 74: // Divieto di Espatrio;(Art. 79;L. 22.12.75;N. 685)
			case 14: // Sospensione Dalla Patente di Guida
			case 117: // Ritiro della Patente di Guida
			{
				// DESTINATARI : PREFETTO, QUESTORE, GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita, "30");
				String[] lStringFilter = { "-", "30" }; // Prefettura
				lOption.setFilter(lStringFilter);
				setRequestAttribute("Destinatario1", "" + lOption);

				lOption = new Option(lTipoAutorita, "20");
				String[] lStringFilter2 = { "-", "20" }; // Questura
				lOption.setFilter(lStringFilter2);
				setRequestAttribute("Destinatario2", "" + lOption);

				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario3", "" + lOption);
				setRequestAttribute("Destinatario4", "" + lOption);

				break;
			}

			case 17: // Sospensione Dall'Esercizio della Patria Potesta'
			case 82: // Decadenza della Potesta'Dei Genitori
			case 7: // Perdita del Diritto Agli Alimenti
			{
				// DESTINATARI : GIUDICE TUTELARE C/0 TRIBUNALE, TRIBUNALE DEI MINORI, GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita, "82");
				String[] lStringFilter = { "-", "82" }; // Giudice Tutelare
				lOption.setFilter(lStringFilter);
				setRequestAttribute("Destinatario1", "" + lOption);

				lOption = new Option(lTipoAutorita, "20");
				String[] lStringFilter2 = { "-", "20" }; // Questura
				lOption.setFilter(lStringFilter2);
				setRequestAttribute("Destinatario2", "" + lOption);

				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario3", "" + lOption);
				setRequestAttribute("Destinatario4", "" + lOption);

				break;
			}

			case 5: // Pubblicazione di Sentenza Penale di Condanna
			{
				// DESTINATARI : DIREZIONE SOCIETA', GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita, "90");
				String[] lStringFilter = { "-", "90" }; // Direzione Società
				lOption.setFilter(lStringFilter);
				setRequestAttribute("Destinatario1", "" + lOption);

				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario2", "" + lOption);
				setRequestAttribute("Destinatario3", "" + lOption);

				break;
			}

			case 119: // Affissione della Sentenza All'Albo Pretorio
			{
				// DESTINATARI : SINDACO, GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita, "26");
				String[] lStringFilter = { "-", "26" }; // Sindaco
				lOption.setFilter(lStringFilter);
				setRequestAttribute("Destinatario1", "" + lOption);

				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario2", "" + lOption);
				setRequestAttribute("Destinatario3", "" + lOption);

				break;
			}

			case 128: // Liquidazione spese per Pubblicazione sentenza
			{
				// DESTINATARI : GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario1", "" + lOption);
				setRequestAttribute("Destinatario2", "" + lOption);

				break;
			}

			default: // Tutti gli altri casi non classificati.
			{
				// DESTINATARI : GENERICO.
				lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				Option lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario1", "" + lOption);
				setRequestAttribute("Destinatario2", "" + lOption);

				break;
			}

			}
		}

		ComboManager lAutoritaCombo = new ComboManager(lTipoAutorita);
		String lListaForCombo = lAutoritaCombo.getListForAutocompleter();
		setRequestAttribute("ListaForCombo", "" + lListaForCombo);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(lListaForCombo);

		// Lettura Pena Accessoria;
		PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
		String idPenaAccessoria = getRequestStringParameter(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA);
		lPenMod.setIdPenaAccessoria(new BigDecimal(idPenaAccessoria.trim()));
		// Si Invoca il controller
		IPenaAccessoria lCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();
		lPenMod = lCtrl.ExRicercaPenaAccessoriaByKey(lPenMod);
		setRequestAttribute("penaaccessoria", lPenMod);

		return PG_LOAD_ESECUZIONEPA; // restituisce la jsp di VIEW
	}

}