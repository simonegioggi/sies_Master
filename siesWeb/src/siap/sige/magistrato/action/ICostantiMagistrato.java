package siap.sige.magistrato.action;

import f3b.web.IWebConstants;

/**
* <p>Title: ICostantiMagistrato</p>
* <p>Description: Classe di costanti di Magistrato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public interface ICostantiMagistrato extends siap.sico.magistrato.action.ICostantiMagistrato
{  
  public static final String CAMPO_SEZIONE                      = "Sezione";
  //public static final String CAMPO_DESCR_FLAG_STATO             = "DescrFlagStato";
  
  
  public static final String PG_LOAD_RICERCAMAGISTRATO = 
    IWebConstants.ROOT_DIR + "files/siap/sige/magistrato/LoadRicercaMagistrato.jsp";
  public static final String PG_LOAD_DETTAGLIOMAGISTRATO = 
    IWebConstants.ROOT_DIR + "files/siap/sige/magistrato/DettaglioMagistrato.jsp";
  public static final String PG_RICERCAMAGISTRATO	= 
    IWebConstants.ROOT_DIR + "files/siap/sige/magistrato/RicercaMagistrato.jsp";
  public static final String PG_LOAD_INSERISCIMAGISTRATO = 
    IWebConstants.ROOT_DIR + "files/siap/sige/magistrato/LoadInserisciMagistrato.jsp";

  public static final String PG_LOAD_RICERCA_MAGISTRATO_ASSEGNAZIONE_LISTA = 
    IWebConstants.ROOT_DIR + "files/siap/sige/magistrato/LoadRicercaMagistratoAssegnazioneLista.jsp";
  public static final String PG_RICERCA_MAGISTRATO_ASSEGNAZIONE_LISTA = 
    IWebConstants.ROOT_DIR + "files/siap/sige/magistrato/RicercaMagistratoAssegnazioneLista.jsp";
  public static final String PG_FILTRA_MAG_ASSEGNAZIONE_LISTA = 
	    IWebConstants.ROOT_DIR + "files/siap/sige/magistrato/FiltraMagAssegnazioneLista.jsp";

  //public static final String PG_ELENCOMAGISTRATI = 
  //  IWebConstants.ROOT_DIR + "files/siap/sige/magistrato/ElencoMagistrati.jsp";
  //public static final String PG_LOAD_RICERCA_MAGISTRATO_LISTA  = 
  //  IWebConstants.ROOT_DIR + "files/siap/sige/magistrato/LoadRicercaMagistratoLista.jsp";
  //public static final String PG_RICERCA_MAGISTRATO_LISTA  = 
  //  IWebConstants.ROOT_DIR + "files/siap/sige/magistrato/RicercaMagistratoLista.jsp";
  //public static final String PG_LOAD_RICERCA_MAG = 
  //  IWebConstants.ROOT_DIR + "files/siap/sige/magistrato/LoadRicercaMag.jsp";
  //public static final String PG_RICERCA_MAG  = 
  //  IWebConstants.ROOT_DIR + "files/siap/sige/magistrato/RicercaMag.jsp";
}