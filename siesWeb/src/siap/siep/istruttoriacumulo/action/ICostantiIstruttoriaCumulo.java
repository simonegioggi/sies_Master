package siap.siep.istruttoriacumulo.action;

/**
* <p>Title: ICostantiIstruttoriaCumulo</p>
* <p>Description: Classe di costanti di IstruttoriaCumulo</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiIstruttoriaCumulo {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_ISTRUTTORIA_CUMULO       = "IdIstruttoriaCumulo"; 
    public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP   = "FasSieIdFascicoloSiep"; 
    public static final String CAMPO_EVE_ID_EVENTO_ISTR          = "EveIdEventoIstr"; 
    public static final String CAMPO_EVE_ID_EVENTO_PROV          = "EveIdEventoProv"; 
    public static final String CAMPO_ID_CUMULO_IN_ISTRU 		     = "IdCumuloInIstruttoria"; 
    public static final String CAMPO_GIORNO_DATA_APERTURA        = "GiornoDataApertura"; 
    public static final String CAMPO_MESE_DATA_APERTURA          = "MeseDataApertura"; 
    public static final String CAMPO_ANNO_DATA_APERTURA          = "AnnoDataApertura"; 
    public static final String CAMPO_GIORNO_DATA_CHIUSURA        = "GiornoDataChiusura"; 
    public static final String CAMPO_MESE_DATA_CHIUSURA          = "MeseDataChiusura"; 
    public static final String CAMPO_ANNO_DATA_CHIUSURA          = "AnnoDataChiusura"; 
    public static final String CAMPO_NOTE                        = "Note"; 
    public static final String CAMPO_FLAG_STATO                  = "FlagStato"; 
    public static final String CAMPO_ORDINAMENTO_TITOLI          = "OrdinamentoTitoli";     
    public static final String CAMPO_COD_OPERATORE_INSERIMENTO   = "CodOperatoreInserimento"; 
    public static final String CAMPO_GIORNO_DATA_INSERIMENTO     = "GiornoDataInserimento"; 
    public static final String CAMPO_MESE_DATA_INSERIMENTO       = "MeseDataInserimento"; 
    public static final String CAMPO_ANNO_DATA_INSERIMENTO       = "AnnoDataInserimento"; 
    public static final String CAMPO_COD_UFFICIO_INSERIMENTO     = "CodUfficioInserimento"; 
    public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO = "CodOperatoreAggiornamento"; 
    public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO   = "GiornoDataAggiornamento"; 
    public static final String CAMPO_MESE_DATA_AGGIORNAMENTO     = "MeseDataAggiornamento"; 
    public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO     = "AnnoDataAggiornamento"; 
    public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO   = "CodUfficioAggiornamento"; 

    // Order by della lista di ricerca Titoli Coinvolti memorizzata sulla Tabella
    // ISTRUTTORIA campo ORDINAMENTO    
    public static final String ORDER_BY_DATA_IRREVOCABILITA_ASC  = "DAT_IRR_ASC"; 
    public static final String ORDER_BY_DATA_IRREVOCABILITA_DESC = "DAT_IRR_DESC"; 
    public static final String ORDER_BY_DATA_PROVVEDIMENTO_ASC   = "DAT_PROV_ASC"; 
    public static final String ORDER_BY_DATA_PROVVEDIMENTO_DESC  = "DAT_PROV_DESC"; 
    //public static final String ORDER_BY_DATA_COMMESSO_REATO    = "5"; // da verificare se realizzabile

    // Ricerca Istruttoria Estesa
    public static final String CAMPO_ANNO_PROTOCOLLO_INIZIALE    = "AnnoProtocolloIniziale"; 
    public static final String CAMPO_NUMERO_PROTOCOLLO_INIZIALE  = "NumeroProtocolloIniziale"; 
    public static final String CAMPO_ANNO_PROTOCOLLO_FINALE      = "AnnoProtocolloFinale"; 
    public static final String CAMPO_NUMERO_PROTOCOLLO_FINALE    = "NumeroProtocolloFinale"; 
    
    public static final String FLAG_STATO_APERTA                  = "A"; 
    public static final String FLAG_STATO_ANNULLATA               = "N"; 
    public static final String FLAG_STATO_CHIUSA                  = "C"; 
    
    
    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //=========================================================================
    // Costanti che rappresentano le pagine jsp  
    //=========================================================================
    public static final String PG_LOAD_GRIGLIA_CUMULO               = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/GrigliaGestioneCumulo.jsp";
    public static final String PG_LOAD_INSERISCI_ISTRUTTORIA_CUMULO = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/LoadInserisciIstruttoriaCumulo.jsp";
    public static final String PG_LOAD_DETTAGLIO_ISTRUTTORIA_CUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/LoadDettaglioIstruttoriaCumulo.jsp";
    public static final String PG_LOAD_CANCELLA_ISTRUTTORIA_CUMULO  = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/LoadCancellaIstruttoriaCumulo.jsp";
    public static final String PG_LOAD_ELENCO_FASCICOLI_COINVOLTI   = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/ElencoFascicoliCoinvolti.jsp";

    public static final String PG_LOAD_ELENCO_FASCICOLI_UFFICIO   = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/RicercaProcedimentiUfficio.jsp";
    
    public static final String PG_ELENCO_ISTRUTTORIE_CUMULO       	= IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/ElencoIstruttorieCumulo.jsp";
    public static final String PG_ELENCO_ESTESO_ISTRUTTORIE_CUMULO  = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/ElencoEstesoIstruttorieCumulo.jsp";
    public static final String PG_LOAD_RICERCAISTRUTTORIACUMULO     = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/LoadRicercaIstruttoriaCumulo.jsp";

    public static final String PG_CARICA_FASCICOLI_TRASMESSI        = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/LoadFascicoliTrasmessi.jsp";
    public static final String PG_CARICAFASCICOLI_MANUALE           = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/LoadFascicoliCumuloManuale.jsp";
    public static final String PG_LOAD_RICERCA_ISTR_CUMULO_ESTESA   = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/LoadRicercaIstrCumuloEstesa.jsp";

    public static final String PG_MESSAGGIO_CONFERMA_ISCRIZIONE  = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/MessageConfermaIscrizione.jsp";
    
    public static final String PG_INCLUDE_DETTAGLIO_ISTRUTTORIA  = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp";
    
    public static final String PG_MESSAGGIO_CONFERMA_RESTITUZIONE  = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/MessageConfermaRestituzione.jsp";
    public static final String PG_RESTITUZIONE_FASCICOLO  = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/LoadRestituzioneFascicolo.jsp";
    
    public static final String PG_POPUP_CALCOLO_PENA  = IWebConstants.ROOT_DIR + "files/siap/siep/istruttoriacumulo/LoadCalcoloPenaCumulo.jsp";
}