package siap.sige.giudicepopolare.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiGiudicePopolare</p>
* <p>Description: Classe di costanti di GiudicePopolare</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public interface ICostantiGiudicePopolare
{
	 public static final String CAMPO_ID_GIUDICE_POPOLARE = "IdGiudicePopolare";
	 public static final String CAMPO_COGNOME = "Cognome";
	 public static final String CAMPO_NOME = "Nome";
	 public static final String CAMPO_INDIRIZZO = "Indirizzo";
	 public static final String CAMPO_COD_UFFICIO_APPARTENENZA = "CodUfficioAppartenenza";
	 public static final String CAMPO_GIORNO_DATA_INIZIO_VALIDITA = "GiornoDataInizioValidita";
	 public static final String CAMPO_MESE_DATA_INIZIO_VALIDITA = "MeseDataInizioValidita";
	 public static final String CAMPO_ANNO_DATA_INIZIO_VALIDITA = "AnnoDataInizioValidita";
	 public static final String CAMPO_GIORNO_DATA_FINE_VALIDITA = "GiornoDataFineValidita";
	 public static final String CAMPO_MESE_DATA_FINE_VALIDITA = "MeseDataFineValidita";
	 public static final String CAMPO_ANNO_DATA_FINE_VALIDITA = "AnnoDataFineValidita";
	 public static final String CAMPO_COD_RUOLO = "CodRuolo";
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
	 public static final String CAMPO_CODICE_FISCALE = "CodiceFiscale";
	 public static final String CAMPO_GIORNO_DATA_NASCITA = "GiornoDataNascita";
	 public static final String CAMPO_MESE_DATA_NASCITA = "MeseDataNascita";
	 public static final String CAMPO_ANNO_DATA_NASCITA = "AnnoDataNascita";
	 public static final String CAMPO_COD_COMUNE_NASCITA = "CodComuneNascita";
	 public static final String CAMPO_COD_STATO_NASCITA = "CodStatoNascita";
	 public static final String CAMPO_COMUNE_ESTERO_NASCITA = "ComuneEsteroNascita";
	 public static final String CAMPO_COD_SESSO = "CodSesso";
	 public static final String CAMPO_SEZ_ID_SEZIONE = "SezIdSezione"; 
	 
	 // Si è reso necessario creare doppioni di costanti, per problemi di omonimia con
	 // quelle del magistrato, pertanto le seguenti costanti sono obbligatorie nella
	 // fase di lista popup dei giudici popolari ( Vedi Inserimento Collegi ).
   public static final String CAMPO_COGNOME_GIU_POP = "CognomeGiuPop";
   public static final String CAMPO_NOME_GIU_POP = "NomeGiuPop";
	 
	 	 
   public static final String PG_LOAD_RICERCAGIUDICE_POPOLARE	= 
     IWebConstants.ROOT_DIR + "files/siap/sige/giudicepopolare/LoadRicercaGiudicePopolare.jsp";
	 public static final String PG_LOAD_DETTAGLIOGIUDICE_POPOLARE	= 
     IWebConstants.ROOT_DIR + "files/siap/sige/giudicepopolare/DettaglioGiudicePopolare.jsp";
	 public static final String PG_RICERCAGIUDICE_POPOLARE = 
     IWebConstants.ROOT_DIR + "files/siap/sige/giudicepopolare/RicercaGiudicePopolare.jsp";
	 public static final String PG_LOAD_INSERISCIGIUDICE_POPOLARE	= 
     IWebConstants.ROOT_DIR + "files/siap/sige/giudicepopolare/LoadInserisciGiudicePopolare.jsp";
   public static final String PG_LOAD_RICERCA_GIUDICE_POPOLARE_LISTA = 
     IWebConstants.ROOT_DIR + "files/siap/sige/giudicepopolare/LoadRicercaGiudicePopolareLista.jsp";
	 public static final String PG_RICERCA_GIUDICE_POPOLARE_LISTA	= 
     IWebConstants.ROOT_DIR + "files/siap/sige/giudicepopolare/RicercaGiudicePopolareLista.jsp";
	 public static final String PG_FILTRA_GIUPOP_ASSEGNAZIONE_LISTA = 
     IWebConstants.ROOT_DIR + "files/siap/sige/giudicepopolare/FiltraGiuPopLista.jsp";

}