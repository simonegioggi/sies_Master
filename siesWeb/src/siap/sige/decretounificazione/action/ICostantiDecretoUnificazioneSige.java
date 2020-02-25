package siap.sige.decretounificazione.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiDecretoUnificazioneSige</p>
* <p>Description: Classe di costanti di DecretoUnificazione Sige</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiDecretoUnificazioneSige
{
  public static final String CAMPO_ANNO_DA_UNIF = "AnnoDaUnif";
  public static final String CAMPO_NUMERO_DA_UNIF = "NumeroDaUnif";
  public static final String CAMPO_NUMERO_DA_UNIF_ORIGIN = "NumeroDaUnifOrigin";
  public static final String CAMPO_ANNO_UNIFICANTE = "AnnoUnificante";
  public static final String CAMPO_NUMERO_UNIFICANTE = "NumeroUnificante";
  public static final String CAMPO_NUMERO_UNIFICANTE_ORIGIN = "NumeroUnificanteOrigin";
  public static final String CAMPO_DATA_UNIFICAZIONE = "DataUnificazione";
  public static final String CAMPO_DATA_GG_UNIFICAZIONE = "DataGGUnificazione";
  public static final String CAMPO_DATA_MM_UNIFICAZIONE = "DataMMUnificazione";
  public static final String CAMPO_DATA_AAAA_UNIFICAZIONE = "DataAAAAUnificazione";
  public static final String CAMPO_ID_FASCICOLO_UNIFICANTE = "IdFascicoloUnificante";
  public static final String CAMPO_ID_FASCICOLO_UNIFICATO = "IdFascicoloUnificato";
  public static final String CAMPO_ID_EVENTO_UNIFICAZIONE = "IdEvento";
  public static final String CAMPO_ID_DECRETO_UNIFICAZIONE = "IdDecretoUnificazione";
  public static final String ACTION_DOPO_CANCELLAZIONE    = "ActDopoCanc";  // 30/04/2004
  public static final String RUOLO_FASCICOLO_DA_UNIFICARE = "da Unificare";
  public static final String RUOLO_FASCICOLO_UNIFICANTE = "Unificante";

  public static final String PG_LOAD_VERIFICADECRETOUNIFICAZIONESIGE	= IWebConstants.ROOT_DIR + "files/siap/sige/decretounificazione/LoadVerificaDecretoUnificazioneSige.jsp";
  public static final String PG_LOAD_INSERISCIDECRETOUNIFICAZIONESIGE	= IWebConstants.ROOT_DIR + "files/siap/sige/decretounificazione/LoadInserisciDecretoUnificazioneSige.jsp";
  public static final String PG_LOAD_INSERISCIDECRETOUNIFICAZIONESIGE_FASCIOLO_UNIFICANTE	= IWebConstants.ROOT_DIR + "files/siap/sige/decretounificazione/LoadInserisciDecretoUnificazioneSigeFascicoloUnificante.jsp";
  public static final String PG_INSERISCIDECRETOUNIFICAZIONESIGE	    = IWebConstants.ROOT_DIR + "files/siap/sige/decretounificazione/InserisciDecretoUnificazioneSige.jsp";
  public static final String PG_DETTAGLIODECRETOUNIFICAZIONESIGE  	    = IWebConstants.ROOT_DIR + "files/siap/sige/decretounificazione/DettaglioDecretoUnificazioneSige.jsp";

}