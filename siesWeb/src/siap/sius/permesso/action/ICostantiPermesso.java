package siap.sius.permesso.action;

/**
* <p>Title: ICostantiFascicoloSius</p>
* <p>Description: Classe di costanti di FascicoloSius</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import f3b.web.IWebConstants;

public interface ICostantiPermesso
{
  public static final String CAMPO_COD_PERMESSO                     = "CodPermesso";
  public static final String CAMPO_COD_LICENZA                      = "CodLicenza";
  public static final String CAMPO_INCLUDE_RIGETTATI                = "CampoIncludeRigettati";

  public static final String PG_LOAD_RICERCASOGGETTICONPERMESSO     = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/LoadRicercaSoggettiConPermesso.jsp";
  
  public static final String PG_RICERCA_SOGGETTICONPERMESSO	        = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/RicercaSoggettiConPermesso.jsp";
  
  public static final String PG_BUTTONS_PERMESSI	                  = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/ButtonsPermessiDelSoggetto.jsp";
  
  public static final String PG_BUTTONS_LICENZE	                    = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/ButtonsLicenzeDelSoggetto.jsp";
  
  public static final String PG_RICERCA_PERMESSIDELSOGGETTO         = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/RicercaPermessiDelSoggetto.jsp";
  
  public static final String PG_LOAD_RICERCASOGGETTICONLICENZA      = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/LoadRicercaSoggettiConLicenza.jsp";  // STUB 28/04/2005.
  
  public static final String PG_RICERCA_SOGGETTICONLICENZA	        = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/RicercaSoggettiConLicenza.jsp";
  
  public static final String PG_RICERCA_LICENZEDELSOGGETTO          = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/RicercaLicenzeDelSoggetto.jsp";
  
  // Esecuzione Permesso Licenza
  public static final String PG_DETTAGLIO_ESECUZIONE_PERMESSOLICENZA = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/DettaglioEsecuzionePermessoLicenza.jsp";
  
  public static final String PG_LOAD_MODIFICA_ESITO_PERMESSOLICENZA = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/LoadModificaEsitoPermessoLicenza.jsp";
  
  // Sintesi dati Permesso Licenza
  public static final String PG_SINTESI_DATI_PERMESSOLICENZA = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/SintesiDatiPermessoLicenza.jsp";
  
  // Relazione Trimestrale Permessi Licenza
  public static final String PG_LOAD_RELAZIONE_TRIMESTRALE_PERMESSOLICENZA = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/LoadRelazioneTrimestralePermessoLicenza.jsp";
  
  public static final String PG_RICERCA_PROVVEDIMENTI_PERMESSOLICENZA = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/RicercaProvvedimentiPermessoLicenza.jsp";
  
  // Pulsante di stampa per elenco provvedimenti permessi/licenze
  public static final String PG_BUTTON_STAMPA_PROVV_PERMESSI_LICENZE = 
    IWebConstants.ROOT_DIR + "files/siap/sius/permesso/ButtonsStampaProvvPermessiLicenze.jsp";
}