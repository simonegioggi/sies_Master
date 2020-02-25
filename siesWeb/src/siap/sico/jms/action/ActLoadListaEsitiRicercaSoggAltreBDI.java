package siap.sico.jms.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadListaEsitiRicercaSoggAltreBDI</p>
* <p>Description: Classe Action per la load della Lista degli Esiti della Ricerca Soggetti su altre BDI</p>
* <p>Copyright: Copyright (c) 2005</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadListaEsitiRicercaSoggAltreBDI extends ActionSiap implements ICostantiSicoJMS
{
  public String processRequest() throws F3BException
  {
    return PG_LOAD_LISTA_ESITI_RICERCA_SOGG_ALTRE_BDI;  //restituisce la jsp di VIEW
  }
}