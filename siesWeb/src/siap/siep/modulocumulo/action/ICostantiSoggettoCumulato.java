package siap.siep.modulocumulo.action;

/**
* <p>Title: ICostantiSoggettoCumulato</p>
* <p>Description: Classe di costanti di SoggettoCumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;

public interface ICostantiSoggettoCumulato {
    //========================================================================== 
    // Costanti utilizzate nelle jsp e che rappresentano i campi della tabella  
    //========================================================================== 
    public static final String CAMPO_ID_SOGGETTO_CUMULATO        = "IdSoggettoCumulato"; 
    public static final String CAMPO_COGNOME                     = "Cognome"; 
    public static final String CAMPO_NOME                        = "Nome"; 
    public static final String CAMPO_SESSO                       = "Sesso"; 
    public static final String CAMPO_GIORNO_DATA_NASCITA         = "GiornoDataNascita"; 
    public static final String CAMPO_MESE_DATA_NASCITA           = "MeseDataNascita"; 
    public static final String CAMPO_ANNO_DATA_NASCITA           = "AnnoDataNascita"; 
    public static final String CAMPO_DATA_NASCITA_PRESUNTA       = "DataNascitaPresunta"; 
    public static final String CAMPO_ANNO_NASCITA                = "AnnoNascita"; 
    public static final String CAMPO_MESE_NASCITA                = "MeseNascita"; 
    public static final String CAMPO_COD_COMUNE_NASCITA          = "CodComuneNascita"; 
    public static final String CAMPO_COD_PROVINCIA_NASCITA       = "CodProvinciaNascita"; 
    public static final String CAMPO_COD_STATO_NASCITA           = "CodStatoNascita"; 
    public static final String CAMPO_DESC_COMUNE_NASCITA_ESTERO  = "DescComuneNascitaEstero"; 
    public static final String CAMPO_NAZIONALITA                 = "Nazionalita"; 
    public static final String CAMPO_PATERNITA                   = "Paternita"; 
    public static final String CAMPO_COGNOME_MADRE               = "CognomeMadre"; 
    public static final String CAMPO_NOME_MADRE                  = "NomeMadre"; 
    public static final String CAMPO_COD_FISCALE                 = "CodFiscale"; 
    public static final String CAMPO_ATTO_NASCITA                = "AttoNascita"; 
    public static final String CAMPO_COD_AFIS                    = "CodAfis"; 
    public static final String CAMPO_COD_COMUNE_CASELLARIO       = "CodComuneCasellario"; 
    public static final String CAMPO_NOTE                        = "Note"; 
    public static final String CAMPO_KEY_SOGG_NSC                = "KeySoggNsc"; 
    public static final String CAMPO_TIT_ID_TITOLO_CUMULATO      = "TitIdTitoloCumulato"; 
    public static final String CAMPO_FLAG_STATO                  = "FlagStato"; 
    public static final String CAMPO_MOTIVO_MODIFICA             = "MotivoModifica"; 
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

    //================================================================= 
    // Aggiungere qui eventuali altre costanti utilizzate nelle jsp e 
    // nelle classi del progetto 
    //================================================================= 
    public static final String FLAG_OMONIMI = "flag_omonimi";
    
    //========================================== 
    // Costanti che rappresentano le pagine jsp  
    //========================================== 
    public static final String PG_LOAD_RICERCASOGGETTOCUMULATO  	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadRicercaSoggettoCumulato.jsp";
    public static final String PG_LOAD_DETTAGLIOSOGGETTOCUMULATO	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/DettaglioSoggettoCumulato.jsp";
    public static final String PG_RICERCASOGGETTOCUMULATO       	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/RicercaSoggettoCumulato.jsp";
    public static final String PG_LOAD_INSERISCISOGGETTOCUMULATO	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/LoadInserisciSoggettoCumulato.jsp";
    public static final String PG_LOAD_CANCELLASOGGETTOCUMULATO 	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/DettaglioSoggettoCumulato.jsp";
    
    public static final String PG_LOAD_INSERISCIOMONIMI_CUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/InserimentoOmonimiSoggettoCumulato.jsp";
    public static final String PG_LOAD_INSERISCICUI_CUMULO	= IWebConstants.ROOT_DIR + "files/siap/siep/modulocumulo/InserimentoSoggettoCodAfisCumulato.jsp"; 
    
}
