package siap.siep.penaaccessoria.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadComunicazione
 * </p>
 * <p>
 * Description: Classe Action per la load Comunicazione per Pena Accessoria
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

public class ActLoadComunicazione extends ActionSiap implements ICostantiPenaAccessoria {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {
		// gestioneRitorno();

		// Data Fascicolo SIEP
		Date lDataInserimento = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// Identificativi di Eventuale Pena accessoria di partenza.
		String idPenaAccessoria = getRequestStringParameter(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA);
		String codTipoPenaAccessoria = getRequestStringParameter(
				ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA);
		String descrTipoPenaAccessoria = getRequestStringParameter(
				ICostantiPenaAccessoria.CAMPO_DESCR_TIPO_PENA_ACCESSORIA);

		// Il parametro descrTipoPenaAccessoria potrebbe non essere impostato.
		if (descrTipoPenaAccessoria == null || descrTipoPenaAccessoria.length() < 2)
			descrTipoPenaAccessoria = DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getTipoPeneAccessorie(), codTipoPenaAccessoria);

		setRequestAttribute(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA, "" + idPenaAccessoria);
		setRequestAttribute(ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA,
				"" + codTipoPenaAccessoria);
		setRequestAttribute(ICostantiPenaAccessoria.CAMPO_DESCR_TIPO_PENA_ACCESSORIA,
				"" + descrTipoPenaAccessoria);

		setRequestAttribute("DataComunicazione_GG",
				getRequestStringParameter(CAMPO_GIORNO_DATA_COMUNICAZIONE));
		setRequestAttribute("DataComunicazione_MM", getRequestStringParameter(CAMPO_MESE_DATA_COMUNICAZIONE));
		setRequestAttribute("DataComunicazione_AA", getRequestStringParameter(CAMPO_ANNO_DATA_COMUNICAZIONE));

		// Configurazione combo TipoPenaAccessoria.
		// Option lOption2 = new Option( DecodificheManager.getInstance().getTipoPeneAccessorie());
		Option lOption2 = new Option(DecodificheUtils
				.getDecodesWithoutCode(DecodificheManager.getInstance().getTipoPeneAccessorie(), "999"));
		setRequestAttribute("TipoPenaAccessoria", "" + lOption2);

		// Descrizione Tipo Comunicazione.
		String codTipoComunicazione = getRequestStringParameter(
				ICostantiPenaAccessoria.CAMPO_COD_TIPO_COMUNICAZIONE);
		Collection lColTipoComunicazione = null;
		DecodificheModel lModel = new DecodificheModel();

		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TIPO_COMUNICAZIONE_PA");
		lColTipoComunicazione = lDecodifiche.ExRicercaDecodifiche(lModel);
		String descrTipoComunicazione = DecodificheUtils.getDescbyCode(lColTipoComunicazione,
				codTipoComunicazione);
		setRequestAttribute("CodTipoComunicazione", "" + codTipoComunicazione.trim());
		setRequestAttribute("DescrTipoComunicazione", "" + descrTipoComunicazione.trim());

		// Si Imposta il model lPenMod;
		PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
		lPenMod.setIdPenaAccessoria(new BigDecimal(idPenaAccessoria.trim()));

		// Si Invoca il controller
		IPenaAccessoria lCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();
		lPenMod = lCtrl.ExRicercaPenaAccessoriaByKey(lPenMod);
		setRequestAttribute("penaaccessoria", lPenMod);

		// Esclusione di "CSS" e "PM" da TipoUfficioS.
		Collection lColl = DecodificheUtils.getDecodesWithoutCodes(
				DecodificheManager.getInstance().getTipoUfficioS(), new String[] { "CSS", "PM" });
		Collection lColl2 = DecodificheUtils.getDecodesWithCodes(
				DecodificheManager.getInstance().getTipoUfficio(), new String[] { "GIPMI", "TMI" });
		/* boolean lBool = ( */lColl.addAll(lColl2)/* ) */;
		Option lOption = new Option(lColl, lPenMod.getCodTipoUfficioOrdinanzaPA());
		setRequestAttribute("autoritaOrdinanza", "" + lOption);

		// Costruzione combo "Tenore Ordinanza"
		Collection lColTenoreOrdinanza = null;
		lModel = new DecodificheModel();
		lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TENORE_ORDINANZA_PA");
		lColTenoreOrdinanza = lDecodifiche.ExRicercaDecodifiche(lModel);
		Option lOptionTenoreOrdinanza = new Option(lColTenoreOrdinanza, lPenMod.getFlagCondonata(), 50);

		// Costruzione combo "Tipo Durata Pena Accessoria".
		lOption = new Option(DecodificheManager.getInstance().getDurataPeneAccessorie());
		setRequestAttribute("DurataPeneAccessorie", "" + lOption);

		setRequestAttribute("tenoreOrdinanza", "" + lOptionTenoreOrdinanza);

		// Costruzione combo "Fonte Reato" e "Sottonumerazione"
		lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato(), lPenMod.getCodFonteGE());
		setRequestAttribute("TipiFontiReato", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getSottonumerazione(),
				lPenMod.getCodSottonumerazioneGE());
		setRequestAttribute("TipiSottonumerazione", "" + lOption);

		// Gestione LISTA DESTINATARI.
		if (codTipoPenaAccessoria != null) {
			switch (Integer.parseInt(codTipoPenaAccessoria.trim())) {
			case 1: // Interdizione Dai Pubblici Uffici
			{
				// DESTINATARI : SINDACO, QUESTORE, GENERICO.
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "26");
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
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "82");
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
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "82");
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
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "30");
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
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "84");
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
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "20");
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
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "26");
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
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita);
				setRequestAttribute("Destinatario1", "" + lOption);
				setRequestAttribute("Destinatario2", "" + lOption);

				break;
			}

			case 80: // Interdizione Dagli Uffici Direttivi Delle Persone Giuridiche e Delle Imprese
			{
				// DESTINATARI : QUESTORE, CAMERA COMMERCIO REGISTRO IMPRESE, GENERICO.
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();

				lOption = new Option(lTipoAutorita, "20");
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
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "26");
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
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "30");
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
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "82");
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
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "90");
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
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "26");
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
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario1", "" + lOption);
				setRequestAttribute("Destinatario2", "" + lOption);

				break;
			}

			default: // Tutti gli altri casi non classificati.
			{
				// DESTINATARI : GENERICO.
				Collection lTipoAutorita = DecodificheManager.getInstance().getTipoAutorita();
				lOption = new Option(lTipoAutorita, "-");
				setRequestAttribute("Destinatario1", "" + lOption);
				setRequestAttribute("Destinatario2", "" + lOption);

				break;
			}

			}
		}
		return PG_LOAD_COMUNICAZIONE; // restituisce la jsp di VIEW
	}
}
