package siap.sige.udienzaprocedimento.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiUdienzaProcedimentoSige</p>
* <p>Description: Classe di costanti di UdienzaProcedimentoSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public interface ICostantiUdienzaProcedimentoSige {
  public static final String CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE   = "IdUdienzaProcedimentoSige";
  public static final String CAMPO_FLAG_RINVIATA                  = "FlagRinviata";
  public static final String CAMPO_COD_OPERATORE_INSERIMENTO      = "CodOperatoreInserimento";
  public static final String CAMPO_GIORNO_DATA_INSERIMENTO        = "GiornoDataInserimento";
  public static final String CAMPO_MESE_DATA_INSERIMENTO          = "MeseDataInserimento";
  public static final String CAMPO_ANNO_DATA_INSERIMENTO          = "AnnoDataInserimento";
  public static final String CAMPO_COD_UFFICIO_INSERIMENTO        = "CodUfficioInserimento";
  public static final String CAMPO_COD_OPERATORE_AGGIORNAMENTO    = "CodOperatoreAggiornamento";
  public static final String CAMPO_GIORNO_DATA_AGGIORNAMENTO      = "GiornoDataAggiornamento";
  public static final String CAMPO_MESE_DATA_AGGIORNAMENTO        = "MeseDataAggiornamento";
  public static final String CAMPO_ANNO_DATA_AGGIORNAMENTO        = "AnnoDataAggiornamento";
  public static final String CAMPO_COD_UFFICIO_AGGIORNAMENTO      = "CodUfficioAggiornamento";
  public static final String CAMPO_FAS_ID_FASCICOLO_SIGE 		  = "FasIdFascicoloSige";
  public static final String CAMPO_UDI_ID_UDIENZA_SIGE            = "UdiIdUdienzaSige";
  public static final String CAMPO_COD_MAGISTRATO                 = "CodMagistrato";
  public static final String CAMPO_ID_ESPERTO                     = "IdEsperto";
  public static final String CAMPO_EEVE_ID_EVENTO                 = "IdEvento";


  // Dettaglio Ruolo
  public static final String PG_LOAD_DETTAGLIOUDIENZAPROCEDIMENTO	= 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/LoadRicercaUdienzaProcedimento.jsp";
  public static final String PG_RICERCAUDIENZAPROCEDIMENTO = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/RicercaUdienzaProcedimento.jsp";
  public static final String PG_LOAD_INSERISCIUDIENZAPROCEDIMENTO	= 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/LoadInserisciUdienzaProcedimento.jsp";

  public static final String PG_LOAD_RICERCAUDIENZAPROCEDIMENTO = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/dettaglioruolo/LoadRicercaUdienzaProcedimento.jsp";
  public static final String PG_LOAD_RICERCAUDIENZEMAGPROC  = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/dettaglioruolo/LoadRicercaUdienzeMagistratiProcedimenti.jsp";
  public static final String PG_LISTA_UDIENZE_MAGISTRATI_NUMPROC  = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/dettaglioruolo/ListaUdienzeMagistratiProcedimenti.jsp";  
  public static final String PG_LISTAPROCEDIMENTIXUDIENZA	 = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/dettaglioruolo/ListaProcedimentixUdienza.jsp";
  public static final String PG_LISTAPROCEDIMENTIXUDIENZAMAG = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/dettaglioruolo/ListaProcedimentixUdienzaMag.jsp";
  public static final String PG_LISTAPROCEDIMENTIXUDIENZAMAGCHILD =
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/dettaglioruolo/ListaProcedimentixUdienzaMagChild.jsp";

  
  
  public static final String PG_LISTASELEZIONEPROCEDIMENTIXUDIENZA  = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/dettaglioruolo/ListaSelezioneProcedimentixUdienza.jsp";
  public static final String PG_LISTAPROCEDIMENTIXUDIENZATUTTICOLLEGI = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/dettaglioruolo/ListaProcedimentixUdienzaTuttiCollegi.jsp";
  public static final String PG_SINTESIDETTAGLIOUDIENZA = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/dettaglioruolo/SintesiDettaglioUdienza.jsp";
  
  
  
  // Stati della fissazione udienza
  public static final String UDIENZA_FISSATA          = "F";
  public static final String UDIENZA_PREFISSATA       = "P";
  public static final String UDIENZA_MODIFICATA       = "M";
  public static final String UDIENZA_ANNULLATA        = "A";
  public static final String UDIENZA_RINVIATA         = "R";
  public static final String UDIENZA_SEGUITO_RINVIO   = "S";
  public static final String NUOVO_RUOLO              = "N";
  
  public static final String CAMPO_CHECK_NUOVO_RUOLO  = "CheckNuovoRuolo";
  
  // Pagine di PRE-FISSAZIONE UDIENZA
  public static final String PG_LOAD_INSERISCIPREFISSAZIONEUDIENZA = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/LoadInserisciPreFissazioneUdienza.jsp";
  public static final String PG_LOAD_DETTAGLIOPREFISSAZIONEUDIENZA = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/DettaglioPreFissazioneUdienza.jsp";
  
  // Pagina di Movimento Udienze per Fascicolo
  public static final String PG_LISTAUDIENZEXPROCEDIMENTO	= 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/ElencoUdienzeProcedimento.jsp";
  public static final String PG_LOAD_INSERISCIORDINANZARINVIOUDIENZA = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/LoadInserisciOrdinanzaRinvioUdienza.jsp";
  public static final String PG_LOAD_DETTAGLIOORDINANZARINVIOUDIENZA = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/DettaglioOrdinanzaRinvioUdienza.jsp";
  public static final String PG_LOAD_INSERISCIVERBALERINVIOUDIENZA = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/LoadInserisciVerbaleRinvioUdienza.jsp";
  public static final String PG_LOAD_DETTAGLIOVERBALERINVIOUDIENZA =
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/DettaglioVerbaleRinvioUdienza.jsp";
  public static final String PG_LOAD_MODIFICAORDINANZARINVIOUDIENZA = 
		    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/LoadModificaOrdinanzaRinvioUdienza.jsp";
  
  public static final String PG_LOAD_MODIFICAORDINANZVERBALERINVIOUDIENZA = 
		    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/LoadModificaOrdinanzaVerbaleRinvioUdienza.jsp";
  
  // Dettaglio ruolo
  public static final String PG_COMBOTEMPLATE_RUOLOUDIENZA = 
    IWebConstants.ROOT_DIR + "files/siap/sige/udienzaprocedimento/dettaglioruolo/ComboTemplateRuoloUdienza.jsp";
  
  // Template di stampa per il dettaglio ruolo
  public static final String ID_TEMPLATE_VERBALE_UDIENZA = "SIGE_VE_002";
  
 
}