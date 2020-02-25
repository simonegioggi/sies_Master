package siap.siep.nuovaistanza.action;

/**
* <p>Title: ICostantiNuovaIstanza</p>
* <p>Description: Classe di costanti di NuovaIstanza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile S.r.l.</p>
* @version 5.0
*/

import f3b.web.IWebConstants;

public interface ICostantiNuovaIstanza {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_NUOVA_ISTANZA              = "IdNuovaIstanza"; 
    public static final String CAMPO_COD_CONTENUTO                 = "CodContenuto"; 
    public static final String CAMPO_GIORNO_DATA_ISTANZA           = "GiornoDataIstanza"; 
    public static final String CAMPO_MESE_DATA_ISTANZA             = "MeseDataIstanza"; 
    public static final String CAMPO_ANNO_DATA_ISTANZA             = "AnnoDataIstanza"; 
    public static final String CAMPO_GIORNO_DATA_DEPOSITO          = "GiornoDataDeposito"; 
    public static final String CAMPO_MESE_DATA_DEPOSITO            = "MeseDataDeposito"; 
    public static final String CAMPO_ANNO_DATA_DEPOSITO            = "AnnoDataDeposito";     
    public static final String CAMPO_NOTE                          = "NoteNuovaIstanza"; 
    public static final String CAMPO_FLAG_PRESDEP                  = "FlagPresdep"; 
    public static final String CAMPO_SOGG_PRESENTANTE              = "SoggPresentante"; 
    public static final String CAMPO_SOGG_PRESENTANTE_IDENTIFICATO = "SoggPresentanteIdentificato"; 
    public static final String CAMPO_AVV_ID_AVVOCATO_PRESENTANTE   = "AvvIdAvvocatoPresentante"; 
    public static final String CAMPO_COD_AUTORITA_MITTENTE         = "CodAutoritaMittente"; 
    public static final String CAMPO_COD_SEDE_MITTENTE             = "CodSedeMittente"; 
    public static final String CAMPO_DESCR_MITTENTE             	 = "DescrMittente";	// 27/04/2010 
    public static final String CAMPO_AVV_ID_AVVOCATO               = "AvvIdAvvocato"; 
    public static final String CAMPO_COD_ESITO                     = "CodEsito"; 
    public static final String CAMPO_ANNO_REGISTRO                 = "AnnoRegistro"; 
    public static final String CAMPO_PROGR_REGISTRO                = "ProgrRegistro"; 
    public static final String CAMPO_COD_TIPO_UFFICIO_DESTINATARIO = "CodTipoUfficioDestinatario"; 
    public static final String CAMPO_COD_LUOGO_DESTINATARIO        = "CodLuogoDestinatario"; 
    public static final String CAMPO_COD_UFFICIO_DESTINATARIO      = "CodUfficioDestinatario"; 
    public static final String CAMPO_COD_STATO_ISTANZA             = "CodStatoIstanza"; 
    public static final String CAMPO_COD_OPERATORE_INSERIMENTO     = "CodOperatoreInserimento"; 
    public static final String CAMPO_GIORNO_DATA_INSERIMENTO       = "GiornoDataInserimento"; 
    public static final String CAMPO_MESE_DATA_INSERIMENTO         = "MeseDataInserimento"; 
    public static final String CAMPO_ANNO_DATA_INSERIMENTO         = "AnnoDataInserimento"; 
    public static final String CAMPO_COD_UFFICIO_INSERIMENTO       = "CodUfficioInserimento"; 
    public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO   = "CodOperatoreAggiornamento"; 
    public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO     = "GiornoDataAggiornamento"; 
    public static final String CAMPO_MESE_DATA_AGGIORNAMENTO       = "MeseDataAggiornamento"; 
    public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO       = "AnnoDataAggiornamento"; 
    public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO     = "CodUfficioAggiornamento"; 
    public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP     = "FasSieIdFascicoloSiep"; 
    public static final String CAMPO_EVE_ID_EVENTO                 = "EveIdEvento"; 
    public static final String CAMPO_GIORNO_DATA_INOLTRO_PM        = "GiornoDataInoltroPM"; 
    public static final String CAMPO_MESE_DATA_INOLTRO_PM          = "MeseDataInoltroPMo"; 
    public static final String CAMPO_ANNO_DATA_INOLTRO_PM          = "AnnoDataInoltroPM"; 
    public static final String CAMPO_TIPO_AVVOCATO                 = "TipoAvvocato"; 
    public static final String CAMPO_NOME_COGNOME_AVVOCATO         = "NomeCognomeAvvocato"; 
    public static final String CAMPO_GIORNO_DATA_IRREVOCABILITA    = "GiornoDataIrrevocabilita"; 
    public static final String CAMPO_MESE_DATA_IRREVOCABILITA      = "MeseDataIrrevocabilita"; 
    public static final String CAMPO_ANNO_DATA_IRREVOCABILITA      = "AnnoDataIrrevocabilita"; 


    //DIsposizioni PM
    public static final String CAMPO_GIORNO_DISPOSIZIONE           = "GiornoDataIstanza"; 
    public static final String CAMPO_MESE_DISPOSIZIONE             = "MeseDataIstanza"; 
    public static final String CAMPO_ANNO_DISPOSIZIONE             = "AnnoDataIstanza"; 
    
    public static final String CAMPO_ATTIVITA_ISTANZA              = "AttivitaIstanza"; 
    public static final String CAMPO_TIPOLOGIA_ISTANZA             = "TipologiaIstanza"; 
    public static final String CAMPO_CHIAVE_ANNO_INIZIALE          = "CampoChiaveAnnoIniziale"; 
    public static final String CAMPO_CHIAVE_PROGR_INIZIALE         = "CampoChiaveProgrIniziale"; 
    public static final String CAMPO_CHIAVE_ANNO_FINALE            = "CampoChiaveAnnoFinale"; 
    public static final String CAMPO_CHIAVE_PROGR_FINALE           = "CampoChiaveProgrFinale"; 
    
    public static final String TIPO_RICERCA_SINGOLA_ISTANZA        = "TipoRicercaSingolaIstanza";
    public static final String TIPO_RICERCA_INTERVALLO_ISTANZE     = "TipoRicercaIntervalloIstanze";
    public static final String TIPO_RICERCA_SOGGETTO_ISTANZA       = "TipoRicercaSoggettoIstanza";
    public static final String TIPO_RICERCA_ATTIVITA_ISTANZA       = "TipoRicercaAttivitaIstanza";
    
    public static final int    REGISTRO_ISTANZA_MIN		   		       = 90001;
    public static final int    REGISTRO_ISTANZA_MAX		   		       = 99999;


    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCANUOVAISTANZA  	  = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadRicercaNuovaIstanza.jsp";
    public static final String PG_LOAD_DETTAGLIONUOVAISTANZA	  = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/DettaglioNuovaIstanza.jsp";
    public static final String PG_RICERCANUOVAISTANZA       	  = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/RicercaNuovaIstanza.jsp";
    public static final String PG_ELENCONUOVAISTANZA       	  = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/ElencoNuovaIstanza.jsp";
    public static final String PG_LOAD_INSERISCINUOVAISTANZA	  = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadInserisciNuovaIstanza.jsp";
    public static final String PG_LOAD_CANCELLANUOVAISTANZA 	  = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/DettaglioNuovaIstanza.jsp";

    // Trasmissione
    public static final String PG_LOAD_TRASFERISCI_NUOVAISTANZA      = 
      IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadTrasferisciNuovaIstanza.jsp";
    public static final String PG_DETTAGLIO_TRASFERISCI_NUOVAISTANZA = 
      IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/DettaglioTrasferisciNuovaIstanza.jsp";
    public static final String PG_DETTAGLIO_MESSAGGIO_TRASMESSO = 
      IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/DettaglioNuovaIstanzaSpedita.jsp";

    public static final String PG_DETTAGLIO_MESSAGGIO_RICEVUTO = 
      IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/DettaglioNuovaIstanzaRicevuta.jsp";
    
    // Da eliminare ?
    public static final String TEMPLATE_TRASFERISCI_ISTANZA = "STXISBDI";
    
    public static final String TEMPLATE_INOLTRO_ISTANZA = "SIEP_RG_01";
    public static final String TEMPLATE_PERCOMPETENZA_ISTANZA = "SIEP_RG_02";
    public static final String TEMPLATE_DISPONE_ISTANZA = "SIEP_RG_03";
    public static final String TEMPLATE_RICEVUTA_ISTANZA = "SIEP_RG_04";
    
    public static final String PG_LOAD_DETTAGLIO_FASCICOLO_ISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/DettaglioFascicoloRegistroIstanza";  
    public static final String PG_INSERICI_MOTIVAZIONI_ISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadCancellaNuovaIstanza.jsp";
    public static final String PG_LOAD_MODIFICA_NUOVA_ISTANZA = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadModificaNuovaIstanza.jsp";

    //titolo esecutivo per inserimento instanza
    public static final String PG_LOAD_RICERCA_TIT_ESEC_ISTANZA	  = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadRicercaTitEsecIstanza.jsp";
    public static final String PG_RICERCA_TIT_ESEC_ISTANZA	      = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/RicercaTitEsecIstanza.jsp";
    public static final String PG_LOAD_INSERISCI_TIT_ESEC_ISTANZA	= IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadInserisciTitEsecIstanza.jsp";      
    public static final String PG_BUTTONS_FASCICOLI_NUOVAISTANZA	= IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/ButtonsFascicoloPerNuovaIstanza.jsp";      
    public static final String PG_BUTTONS_SENTENZA_NUOVAISTANZA	  = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/ButtonsSentenzaPerNuovaIstanza.jsp";      
    public static final String PG_BUTTONS_SOGGETTO_NUOVAISTANZA	  = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/ButtonsSoggettoPerNuovaIstanza.jsp";      
    public static final String PG_BUTTONS_NUOVAISTANZA 						= IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/buttonsNuovaIstanza.jsp";
    
    //ricerca soggetto per inserimento instanza
    public static final String PG_LOAD_RICERCA_SOGGETTO_ISTANZA	= IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadRicercaSoggettoIstanza.jsp";
    public static final String PG_RICERCA_SOGGETTO_ISTANZA	    = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/RicercaSoggettoIstanza.jsp";

    //ricerca fascicoli per inserimento istanza
    public static final String PG_LOAD_RICERCA_FASCICOLO = IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadRicercaFascicoloSiepIstanza.jsp";
    public static final String PG_LOAD_INSERISCI_PROCEDIMENTO_ISTANZA	= IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadInserisciProcedimentoIstanza.jsp";      
    public static final String PG_LOAD_INSERISCI_SOGGETTO_ISTANZA	= IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadInserisciSoggettoIstanza.jsp";      
    
    //GESTIONE ISTANZA    
    public static final String PG_LOAD_INSERISCI_INOLTRO_PM	= IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadInoltroPM.jsp";
    public static final String PG_LOAD_DETTAGLIO_INOLTRO_PM	= IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/DettaglioInoltroPM.jsp";
    public static final String PG_LOAD_DETTAGLIO_DISPOSIZIONI_PM	= IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/DettaglioDisposizioniPM.jsp";

    public static final String PG_LOAD_ASSOCIA_ISTANZA	= IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadAssociaNuovaIstanza.jsp";
    public static final String PG_LOAD_CONVERTI_ISTANZA	= IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadConvertiNuovaIstanza.jsp";
    public static final String PG_LOAD_ANNULLA_ASSOCIA_ISTANZA	= IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadAnnullaAssociaNuovaIstanza.jsp";
    public static final String PG_ARCHIVIAZIONE_MANUALE	= IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/ArchiviazioneManuale.jsp";

    //==================================================================== 
    // Costanti utilizzate nelle jsp che rappresentano codifiche generiche  
    //==================================================================== 
    public static final String FUNZIONE_DETTAGLIO_FASCICOLO  = "25"; 
    public static final String FUNZIONE_DETTAGLIO_SENTENZA   = "24"; 
    public static final String FUNZIONE_DETTAGLIO_SOGGETTO   = "5"; 
    
}