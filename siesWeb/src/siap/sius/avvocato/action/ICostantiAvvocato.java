package siap.sius.avvocato.action;

/**
* <p>Title: ICostantiAvvocato</p>
* <p>Description: Classe di costanti di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;
public interface ICostantiAvvocato
{
		 public static final String CAMPO_ID_AVVOCATO = "IdAvvocato";
		 public static final String CAMPO_COGNOME = "Cognome";
		 public static final String CAMPO_NOME = "Nome";
		 public static final String CAMPO_COD_TIPO = "CodTipo";
		 public static final String CAMPO_FORO = "Foro";
		 public static final String CAMPO_INDIRIZZO = "Indirizzo";
		 public static final String CAMPO_TELEFONO = "Telefono";
		 public static final String CAMPO_FAX = "Fax";
		 public static final String CAMPO_E_MAIL = "EMail";
		 public static final String CAMPO_CODICE_FISCALE= "CodiceFIscale";
		 public static final String CAMPO_COD_OPERATORE_INSERIMENTO = "CodOperatoreInserimento";
		 public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento";
		 public static final String CAMPO_MESE_DATA_INSERIMENTO = "MeseDataInserimento";
		 public static final String CAMPO_ANNO_DATA_INSERIMENTO = "AnnoDataInserimento";
		 public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento";
		 public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO = "GiornoDataAggiornamento";
		 public static final String CAMPO_MESE_DATA_AGGIORNAMENTO = "MeseDataAggiornamento";
		 public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO = "AnnoDataAggiornamento";
     public static final String CAMPO_COD_LUOGO_NASCITA = "CodLuogoNascita";
     public static final String CAMPO_GIORNO_DATA_NASCITA = "GiornoDataNascita";
     public static final String CAMPO_MESE_DATA_NASCITA = "MeseDataNascita";
     public static final String CAMPO_ANNO_DATA_NASCITA = "AnnoDataNascita";
     public static final String CAMPO_COD_COMUNE_RESIDENZA = "CodComuneResidenza";

		 public static final String PG_LOAD_RICERCAAVVOCATO	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/LoadRicercaAvvocato.jsp";
		 public static final String PG_RICERCAAVVOCATO	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/RicercaAvvocato.jsp";
		 public static final String PG_LOAD_INSERISCIAVVOCATO	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/LoadInserisciAvvocato.jsp";
     public static final String PG_DETTAGLIO_AVVOCATO_SENTENZA = IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/DettaglioAvvocatoSentenza.jsp";
     public static final String PG_RICERCAAVVOCATOBREVE	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/RicercaAvvocatoBreve.jsp";
     public static final String PG_LOAD_RICERCAAVVOCATOBREVE	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/LoadRicercaAvvocatoBreve.jsp";

     public static final String PG_LOAD_RICERCAAVVOCATOSIEP	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/LoadRicercaAvvocatoSiep.jsp";
     public static final String PG_RICERCAAVVOCATOBREVESIEP	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/RicercaAvvocatoBreveSiep.jsp";
     public static final String PG_LOAD_RICERCAAVVOCATOBREVESIEP	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/LoadRicercaAvvocatoBreveSiep.jsp";
     public static final String PG_RICERCAAVVOCATOSIEP	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/RicercaAvvocatoSiep.jsp";

      public static final String PG_SOSTITUZIONE_AVVOCATO = IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/SostituzioneDifensore.jsp";
      public static final String PG_ASSEGNA_INSERISCI_AVVOCATO	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/AssegnazioneElencoAvvocati.jsp";
      public static final String PG_RICERCAAVVOCATOPROVVEDIMENTO	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/RicercaAvvocatoProvvedimento.jsp";
      public static final String PG_LOAD_RICERCAAVVOCATOGENERICO	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/LoadRicercaAvvocatoGenerico.jsp";
      public static final String PG_LOAD_RICERCAAVVOCATOBREVEGENERICO	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/LoadRicercaAvvocatoBreveGenerico.jsp";
      public static final String PG_RICERCAAVVOCATOGENERICO	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/RicercaAvvocatoGenerico.jsp";
      public static final String PG_DETTAGLIO_AVVOCATO_AVVOCATO_FASCICOLO_SIUS = IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/DettaglioAvvocatoAvvocatoFascicoloSius.jsp";
      public static final String PG_RICERCAAVVOCATOBREVEGENERICO	= IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/RicercaAvvocatoBreveGenerico.jsp";
      public static final String PG_FILTRAAVV = IWebConstants.ROOT_DIR + "files/siap/sius/avvocato/FiltraAvv.jsp";

}