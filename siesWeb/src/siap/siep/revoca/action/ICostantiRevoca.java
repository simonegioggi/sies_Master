package siap.siep.revoca.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiRevoca</p>
* <p>Description: Classe di costanti di AnnotazioneManuale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiRevoca
{
  //REVOCA
  public static final String PG_LOAD_INSERISCI_REVOCA = IWebConstants.ROOT_DIR + "/files/siap/siep/revoca/LoadInserisciRevoca.jsp";
  public static final String PG_LOAD_DETTAGLIOREVOCA = IWebConstants.ROOT_DIR + "/files/siap/siep/revoca/DettaglioRevoca.jsp";

  public static final String PG_LOAD_INSERISCI_ORDINE_ESECUZIONE_REVOCA = IWebConstants.ROOT_DIR + "/files/siap/siep/revoca/LoadInserisciOrdineEsecuzioneRevoca.jsp";
  public static final String PG_LOAD_DETTAGLIO_ORDINE_ESECUZIONE_REVOCA = IWebConstants.ROOT_DIR + "/files/siap/siep/revoca/DettaglioOrdineEsecuzioneRevoca.jsp";
}