package siap.siep.certificatostatoesecuzione.action;

/**
* <p>Title: ICostantiCertificatoStatoEsec</p>
* <p>Description: Classe di costanti di CertificatoStatoEsec</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiCertificatoStatoEsec {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_CERTIFICATO_STATO_ESEC   = "IdCertificatoStatoEsec"; 
    public static final String CAMPO_ANNOTAZIONI                 = "Annotazioni"; 
    public static final String CAMPO_FLAG_UPLOAD                 = "FlagUpload"; 
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
    public static final String CAMPO_FAS_SIE_ID_FASCICOLO_SIEP   = "FasSieIdFascicoloSiep";
    //----
    public static final String CAMPO_BLOB   = "CampoBlob";
    public static final String CAMPO_AZIONE_DETTAGLIO = "AzioneDettaglio";

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCACERTIFICATOSTATOESEC  	= IWebConstants.ROOT_DIR + "files/siap/siep/certificatostatoesecuzione/LoadRicercaCertificatoStatoEsec.jsp";
    public static final String PG_LOAD_DETTAGLIOCERTIFICATOSTATOESEC	= IWebConstants.ROOT_DIR + "files/siap/siep/certificatostatoesecuzione/DettaglioCertificatoStatoEsec.jsp";
    public static final String PG_RICERCACERTIFICATOSTATOESEC       	= IWebConstants.ROOT_DIR + "files/siap/siep/certificatostatoesecuzione/RicercaCertificatoStatoEsec.jsp";
    public static final String PG_LOAD_INSERISCICERTIFICATOSTATOESEC	= IWebConstants.ROOT_DIR + "files/siap/siep/certificatostatoesecuzione/LoadInserisciCertificatoStatoEsec.jsp";
    public static final String PG_LOAD_CANCELLACERTIFICATOSTATOESEC 	= IWebConstants.ROOT_DIR + "files/siap/siep/certificatostatoesecuzione/DettaglioCertificatoStatoEsec.jsp";
    public static final String PG_LOAD_STAMPA_CERTIFICATO 				= IWebConstants.ROOT_DIR + "files/siap/siep/certificatostatoesecuzione/LoadCertificatoEsec.jsp";
}