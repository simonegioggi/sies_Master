package siap.sico.utente.action;

import siap.util.ICostanti;
import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiUtente</p>
* <p>Description: Classe di costanti di Utente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiUtente extends ICostanti
{
   public static final String CAMPO_COD_UTENTE = "CodUtente";
   public static final String CAMPO_COGNOME = "Cognome";
   public static final String CAMPO_NOME = "Nome";
   public static final String CAMPO_PWD = "Pwd";
   public static final String CAMPO_TELEFONO = "Telefono";
   public static final String CAMPO_FAX = "Fax";
   public static final String CAMPO_E_MAIL = "Email";
   public static final String CAMPO_GIORNO_DATA_FINE_VALIDITA = "GiornoDataFineValidita";
   public static final String CAMPO_MESE_DATA_FINE_VALIDITA = "MeseDataFineValidita";
   public static final String CAMPO_ANNO_DATA_FINE_VALIDITA = "AnnoDataFineValidita";
   public static final String CAMPO_GIORNO_DATA_ORA_CONNESSIONE = "GiornoDataOraConnessione";
   public static final String CAMPO_MESE_DATA_ORA_CONNESSIONE = "MeseDataOraConnessione";
   public static final String CAMPO_ANNO_DATA_ORA_CONNESSIONE = "AnnoDataOraConnessione";
   public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
   public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
   public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
   public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
   public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
   public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
   public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
   public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
   public static final String CAMPO_GIORNO_DATA_ULTIMA_MODIFICA_PWD = "GiornoDataUltimaModificaPwd";
   public static final String CAMPO_MESE_DATA_ULTIMA_MODIFICA_PWD = "MeseDataUltimaModificaPwd";
   public static final String CAMPO_ANNO_DATA_ULTIMA_MODIFICA_PWD = "AnnoDataUltimaModificaPwd";
   public static final String CAMPO_USERID_NSC = "UseridNSC";
   public static final String CAMPO_PWD_NSC = "PwdNSC";
   
   public static final String PG_LOAD_RICERCAUTENTE	= IWebConstants.ROOT_DIR + "files/siap/sico/utente/LoadRicercaUtente.jsp";
   public static final String PG_LOAD_DETTAGLIOUTENTE	= IWebConstants.ROOT_DIR + "files/siap/sico/utente/LoadDettaglioUtente.jsp";
   public static final String PG_RICERCAUTENTE	= IWebConstants.ROOT_DIR + "files/siap/sico/utente/RicercaUtente.jsp";
   public static final String PG_LISTAUTENTI	= IWebConstants.ROOT_DIR + "files/siap/sico/utente/ListaUtenti.jsp";
   public static final String PG_LOAD_INSERISCIUTENTE	= IWebConstants.ROOT_DIR + "files/siap/sico/utente/LoadInserisciUtente.jsp";
   public static final String PG_LOAD_MODIFICAUTENTE	= IWebConstants.ROOT_DIR + "files/siap/sico/utente/LoadModificaUtente.jsp";
   public static final String PG_LOAD_MODIFICAPWD	= IWebConstants.ROOT_DIR + "files/siap/sico/utente/LoadModificaPassword.jsp";
   public static final String PG_LOAD_RICERCA_UTENTE_ATTIVO = IWebConstants.ROOT_DIR + "files/siap/sico/utente/LoadRicercaUtenteAttivo.jsp";
}