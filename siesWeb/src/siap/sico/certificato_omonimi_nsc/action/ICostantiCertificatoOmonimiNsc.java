package siap.sico.certificato_omonimi_nsc.action;

/**
* <p>Title: ICostantiCertificatoOmonimiNsc</p>
* <p>Description: Classe di costanti di CertificatoOmonimiNsc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiCertificatoOmonimiNsc {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_CERTIFICATO_OMONIMI = "IdCertificatoOmonimi"; 
    public static final String CAMPO_GIORNO_DATA_INSERIMENTO = "GiornoDataInserimento"; 
    public static final String CAMPO_MESE_DATA_INSERIMENTO  = "MeseDataInserimento"; 
    public static final String CAMPO_ANNO_DATA_INSERIMENTO  = "AnnoDataInserimento"; 

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 

    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCACERTIFICATOOMONIMINSC  	= IWebConstants.ROOT_DIR + "files/siap/sico/certificato_omonimi_nsc/LoadRicercaCertificatoOmonimiNsc.jsp";
    public static final String PG_LOAD_DETTAGLIOCERTIFICATOOMONIMINSC	= IWebConstants.ROOT_DIR + "files/siap/sico/certificato_omonimi_nsc/DettaglioCertificatoOmonimiNsc.jsp";
    public static final String PG_RICERCACERTIFICATOOMONIMINSC       	= IWebConstants.ROOT_DIR + "files/siap/sico/certificato_omonimi_nsc/RicercaCertificatoOmonimiNsc.jsp";
    public static final String PG_LOAD_INSERISCICERTIFICATOOMONIMINSC	= IWebConstants.ROOT_DIR + "files/siap/sico/certificato_omonimi_nsc/LoadInserisciCertificatoOmonimiNsc.jsp";
    public static final String PG_LOAD_CANCELLACERTIFICATOOMONIMINSC 	= IWebConstants.ROOT_DIR + "files/siap/sico/certificato_omonimi_nsc/DettaglioCertificatoOmonimiNsc.jsp";
}