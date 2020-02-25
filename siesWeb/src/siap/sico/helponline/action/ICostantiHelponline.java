package siap.sico.helponline.action;

/**
* <p>Title: ICostantiHelponline</p>
* <p>Description: Classe di costanti di Helponline</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiHelponline {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_HELPONLINE   = "IdHelponline"; 
    public static final String CAMPO_FUN_ID_FUNZIONE = "FunIdFunzione"; 
    public static final String CAMPO_NOME_PAGINA     = "NomePagina"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCAHELPONLINE  	= IWebConstants.ROOT_DIR + "files/siap/sico/helponline/LoadRicercaHelponline.jsp";
    public static final String PG_LOAD_DETTAGLIOHELPONLINE	= IWebConstants.ROOT_DIR + "files/siap/sico/helponline/DettaglioHelponline.jsp";
    public static final String PG_RICERCAHELPONLINE       	= IWebConstants.ROOT_DIR + "files/siap/sico/helponline/RicercaHelponline.jsp";
    public static final String PG_LOAD_INSERISCIHELPONLINE	= IWebConstants.ROOT_DIR + "files/siap/sico/helponline/LoadInserisciHelponline.jsp";
    public static final String PG_LOAD_CANCELLAHELPONLINE 	= IWebConstants.ROOT_DIR + "files/siap/sico/helponline/DettaglioHelponline.jsp";
}