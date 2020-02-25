package siap.siep.autoritaesterna.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiAutoritaEsterna</p>
* <p>Description: Classe di costanti di AutoritaEsterna</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiAutoritaEsterna
{
     public static final String CAMPO_ID_AUTORITA_ESTERNA        = "IdAutoritaEsterna";
     public static final String CAMPO_COD_TIPO_AUTORITA          = "CodTipoAutorita";
     public static final String CAMPO_DESCRIZIONE                = "Descrizione";
     public static final String CAMPO_COD_SEDE                   = "CodSede";

//modifica relativa al tipo istituto
    public static final String CAMPO_COD_TIPO_AUTORITA_E         = "CodTipoAutoritaE";
    public static final String CAMPO_COD_SEDE_E                  = "CodSedeE";
    public static final String CAMPO_NOTE_E                      = "NoteE";

    public static final String CAMPO_COD_TIPO_AUTORITA_FUNGI     = "CodTipoAutoritaFungibilita";
    public static final String CAMPO_COD_SEDE_FUNGI              =  "CodSedeFungibilita";

    public static final String CAMPO_COD_TIPO_AUTORITA_C         = "CodTipoAutoritaC";
    public static final String CAMPO_COD_SEDE_C                  = "CodSedeC";
    public static final String CAMPO_NOTE_C                      = "NoteC";

    public static final String CAMPO_COD_SEDE_CAS                = "CodSedeCasellarioGiudiziale";

    public static final String CAMPO_DESC_SEDE                   = "DescSede";
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

    public static final String PG_LOAD_RICERCAAUTORITAESTERNA	    = IWebConstants.ROOT_DIR + "files/siap/siep/autoritaesterna/LoadRicercaAutoritaEsterna.jsp";
    public static final String PG_LOAD_DETTAGLIOAUTORITAESTERNA	  = IWebConstants.ROOT_DIR + "files/siap/siep/autoritaesterna/LoadRicercaAutoritaEsterna.jsp";
    public static final String PG_RICERCAAUTORITAESTERNA	        = IWebConstants.ROOT_DIR + "files/siap/siep/autoritaesterna/RicercaAutoritaEsterna.jsp";
    public static final String PG_LOAD_INSERISCIAUTORITAESTERNA	  = IWebConstants.ROOT_DIR + "files/siap/siep/autoritaesterna/LoadInserisciAutoritaEsterna.jsp";
}
