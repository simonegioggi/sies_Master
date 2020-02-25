package siap.sico.soggetto.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiSoggetto</p>
* <p>Description: Classe di costanti di Soggetto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public interface ICostantiSoggetto {

	public static final String CAMPO_ID_SOGGETTO = "IdSoggetto";
	public static final String CAMPO_COD_FISCALE = "CodFiscale";
	public static final String CAMPO_COD_CS = "CodCs";
	public static final String CAMPO_COD_AFIS = "CodAfis";
	public static final String CAMPO_COGNOME = "Cognome";
	public static final String CAMPO_NOME = "Nome";
	public static final String CAMPO_ANNO_NASCITA = "AnnoNascita";
	public static final String CAMPO_GIORNO_DATA_NASCITA = "GiornoDataNascita";
	public static final String CAMPO_MESE_DATA_NASCITA = "MeseDataNascita";
	public static final String CAMPO_ANNO_DATA_NASCITA = "AnnoDataNascita";
	public static final String CAMPO_GIORNO_DATA_COMMESSO_REATO = "GiornoDataCommessoReato";
	public static final String CAMPO_MESE_DATA_COMMESSO_REATO = "MeseDataCommessoReato";
	public static final String CAMPO_ANNO_DATA_COMMESSO_REATO = "AnnoDataCommessoReato";	
	public static final String CAMPO_DATA_NASCITA_PRESUNTA = "DataNascitaPresunta";
	public static final String CAMPO_COD_COMUNE_NASCITA = "CodComuneNascita";
	public static final String CAMPO_COD_PROVINCIA_NASCITA = "CodProvinciaNascita";
	public static final String CAMPO_COD_STATO_NASCITA = "CodStatoNascita";
	public static final String CAMPO_DESC_COMUNE_NASCITA_ESTERO = "DescComuneNascitaEstero";
	public static final String CAMPO_NAZIONALITA = "Nazionalita";
	public static final String CAMPO_PATERNITA = "Paternita";
	public static final String CAMPO_COGNOME_MADRE = "CognomeMadre";
	public static final String CAMPO_NOME_MADRE = "NomeMadre";
	public static final String CAMPO_SESSO = "Sesso";
	public static final String CAMPO_ATTO_NASCITA = "AttoNascita";
	public static final String CAMPO_NOTE = "Note";
	public static final String CAMPO_COD_COMUNE_CASELLARIO = "CodComuneCasellario";
	public static final String CAMPO_FLAG_PRESENZA_FASCICOLO = "FlagPresenzaFascicolo";
	public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
	public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
	public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
	public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
	public static final String CAMPO_COD_UFFICIO_INSERIMENTO = "CodUfficioInserimento";
	public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
	public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
	public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
	public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
	public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO = "CodUfficioAggiornamento";
	public static final String CAMPO_INCLUDE_UFFICIO = "IncludeUfficio";
	// STUB 27/09/2005 Nelle form come LoadRicercaFascicoloSiepPerSoggetto occorrono 2 diversi campi con Data di nascita.
	public static final String CAMPO_GIORNO_DATA_NASCITA2 = "GiornoDataNascita2";
	public static final String CAMPO_MESE_DATA_NASCITA2 = "MeseDataNascita2";
	public static final String CAMPO_ANNO_DATA_NASCITA2 = "AnnoDataNascita2";
	public static final String CAMPO_ETA_PRESUNTA_ANNI = "EtaPresuntaAnni";
	public static final String CAMPO_ETA_PRESUNTA_MESI = "EtaPresuntaMesi";
	public static final String CONDIZIONE_MAGGIORENNI = " nvl(vse.eta_ora, 18) >= 18 ";

	// Ambrosino SuperSoggetto 04/2010 -
	// Nella Form Sostituirà la combo 'Nazionalità'
	// public static final String CAMPO_STATO_CITTADINANZA = "StatoCittadinanza";

	public static final String PG_BUTTONS = IWebConstants.ROOT_DIR + "files/siap/sico/soggetto/buttonsSoggetto.jsp";

	public static final String PG_LOAD_RICERCASOGGETTO = IWebConstants.ROOT_DIR + "files/siap/sico/soggetto/LoadRicercaSoggetto.jsp";
	public static final String PG_LOAD_DETTAGLIOSOGGETTO = IWebConstants.ROOT_DIR + "files/siap/sico/soggetto/DettaglioSoggetto.jsp";
	public static final String PG_LOAD_DETTAGLIOSOGGETTO_COD_CUI = IWebConstants.ROOT_DIR + "files/siap/sico/soggetto/DettaglioSoggetto.jsp?ricerca=codcui";
	public static final String PG_RICERCASOGGETTO = IWebConstants.ROOT_DIR + "files/siap/sico/soggetto/RicercaSoggetto.jsp";
	public static final String PG_RICERCASOGGETTO_COD_CUI = IWebConstants.ROOT_DIR + "files/siap/sico/soggetto/RicercaSoggettoCodCui.jsp";
	public static final String PG_LOAD_INSERISCISOGGETTO = IWebConstants.ROOT_DIR + "files/siap/sico/soggetto/LoadInserisciSoggetto.jsp";
	public static final String PG_LOAD_DETTAGLIOSOGGETTO_MODIFICATO = IWebConstants.ROOT_DIR + "files/siap/sico/soggetto/DettaglioSoggettoModificato.jsp";
	public static final String PG_LOAD_ELENCO_STORICI_SOGGETTO = IWebConstants.ROOT_DIR + "files/siap/sico/storicosoggetto/ElencoStoricoSoggetto.jsp";

     public static final String PG_LOAD_ELENCO_STORICI_SOGGETTO_SIUS = IWebConstants.ROOT_DIR + "files/siap/sico/storicosoggetto/ElencoStoricoSoggettoSius.jsp";
     public static final String PG_LOAD_INSERISCIOMONIMI = IWebConstants.ROOT_DIR + "files/siap/sico/soggetto/InserimentoOmonimiSoggetto.jsp";
     public static final String BOTTONE_CONFERMA_OMONIMO = IWebConstants.ROOT_DIR + "files/siap/sico/soggetto/ConfermaOmonimo.jsp";
     public static final String PG_LOAD_INSERISCIOMONIMI_SIGE = IWebConstants.ROOT_DIR + "files/siap/sico/soggetto/InserimentoOmonimiSoggettoSige.jsp";
     
	public static final String FLAG_OMONIMI = "flag_omonimi";
	// Ambrosino 07/2009
	// Ambito di costituzione di "SuperSoggetto"
	public static final String PG_LOAD_INSERISCICUI = IWebConstants.ROOT_DIR + "files/siap/sico/soggetto/InserimentoSoggettoCodAfis.jsp";
	
}