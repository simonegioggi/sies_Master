package siap.sius.udienzaprocedimento.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiUdienzaProcedimento</p>
* <p>Description: Classe di costanti di UdienzaProcedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiUdienzaProcedimento
{
  public static final String CAMPO_ID_UDIENZA_PROCEDIMENTO        = "IdUdienzaProcedimento";
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
  public static final String CAMPO_GEN_PRID_GENERALE_PROCEDIMENTO = "GenPridGeneraleProcedimento";
  public static final String CAMPO_UDI_ID_UDIENZA                 = "UdiIdUdienza";
  public static final String CAMPO_COD_MAGISTRATO                 = "CodMagistrato";
  public static final String CAMPO_ID_ESPERTO                     = "IdEsperto";
  public static final String CAMPO_EEVE_ID_EVENTO                 = "IdEvento";

  public static final String PG_LOAD_RICERCAUDIENZAPROCEDIMENTO	  = IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/LoadRicercaUdienzaProcedimento.jsp";
  public static final String PG_LOAD_RICERCAUDIENZEMAGPROC	      = IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/LoadRicercaUdienzeMagistratiProcedimenti.jsp";
 
  // TipoRicerca usato per differenziare ricerca x Udienze da ricerca x Magistrati
  public static final String CARICO_MAGISTRATI	= "CarMagistrati";
  public static final String PG_LISTA_UDIENZE_MAGISTRATI_NUMPROC	= IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/ListaUdienzeMagistratiProcedimenti.jsp";
  public static final String PG_LISTA_MAGISTRATI_UDIENZE_NUMPROC	= IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/ListaMagistratiUdienzeProcedimenti.jsp";

  public static final String PG_LOAD_DETTAGLIOUDIENZAPROCEDIMENTO	    = IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/LoadRicercaUdienzaProcedimento.jsp";
  public static final String PG_RICERCAUDIENZAPROCEDIMENTO	          = IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/RicercaUdienzaProcedimento.jsp";
  public static final String PG_LOAD_INSERISCIUDIENZAPROCEDIMENTO	    = IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/LoadInserisciUdienzaProcedimento.jsp";
  public static final String PG_LISTAPROCEDIMENTIXUDIENZA	            = IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/ListaProcedimentixUdienza.jsp";
  public static final String PG_LISTAPROCEDIMENTIXUDIENZAMAG	        = IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/ListaProcedimentixUdienzaMag.jsp";
  public static final String PG_LISTASELEZIONEPROCEDIMENTIXUDIENZA    = IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/ListaSelezioneProcedimentixUdienza.jsp";
  public static final String PG_LISTAPROCEDIMENTIXUDIENZATUTTICOLLEGI = IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/ListaProcedimentixUdienzaTuttiCollegi.jsp";

  // Stati della fissazione udienza
  public static final String UDIENZA_FISSATA        = "F";
  public static final String UDIENZA_PREFISSATA     = "P";
  public static final String UDIENZA_MODIFICATA     = "M";
  public static final String UDIENZA_ANNULLATA      = "A";
  public static final String UDIENZA_RINVIATA       = "R";
  public static final String UDIENZA_SEGUITO_RINVIO = "S";
  public static final String NUOVO_RUOLO            = "N";

  // Pagine di PRE-FISSAZIONE UDIENZA
  public static final String PG_LOAD_INSERISCIPREFISSAZIONEUDIENZA = IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/LoadInserisciPreFissazioneUdienza.jsp";
  public static final String PG_LOAD_DETTAGLIOPREFISSAZIONEUDIENZA = IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/DettaglioPreFissazioneUdienza.jsp";
  
  // Pagina di Movimento Udienze per Fascicolo
  public static final String PG_LISTAUDIENZEXPROCEDIMENTO	        = IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/ElencoUdienzeProcedimento.jsp";
  public static final String PG_COMBOTEMPLATE_RUOLOUDIENZA	      = IWebConstants.ROOT_DIR + "files/siap/sius/udienzaprocedimento/ComboTemplateRuoloUdienza.jsp";
}