package siap.siep.ripristino.action;

import siap.siep.web.ISIEPCostantiWeb;
/**
* <p>Title: ICostantiRipristino</p>
* <p>Description: Classe di costanti di Rispristino Esecuzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiRipristino extends ISIEPCostantiWeb
{
  public static final String PG_LOAD_INSERISCI_RIPRISTINO = ROOT_DIR + "/files/siap/siep/ripristino/LoadInserisciRipristino.jsp";
  public static final String PG_LOAD_DETTAGLIO_RIPRISTINO = ROOT_DIR + "/files/siap/siep/ripristino/DettaglioRipristino.jsp";

  public static final String PG_LOAD_EMISSIONE_PROVVEDIMENTO_RIPRISTINO = ROOT_DIR + "files/siap/siep/ripristino/LoadInserisciProvvedimentoRipristino.jsp";
  public static final String PG_LOAD_DETTAGLIO_PROVVEDIMENTO_RIPRISTINO = ROOT_DIR + "files/siap/siep/ripristino/DettaglioProvvedimentoRipristino.jsp";
}