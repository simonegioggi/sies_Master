package siap.sius.decretounificazione.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiDecretoUnificazione</p>
* <p>Description: Classe di costanti di DecretoUnificazione</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiDecretoUnificazione
{
  public static final String CAMPO_ANNO_DA_UNIF = "AnnoDaUnif";
  public static final String CAMPO_NUMERO_DA_UNIF = "NumeroDaUnif";
  public static final String CAMPO_ANNO_UNIFICANTE = "AnnoUnificante";
  public static final String CAMPO_NUMERO_UNIFICANTE = "NumeroUnificante";
  public static final String CAMPO_DATA_UNIFICAZIONE = "DataUnificazione";
  public static final String CAMPO_DATA_GG_UNIFICAZIONE = "DataGGUnificazione";
  public static final String CAMPO_DATA_MM_UNIFICAZIONE = "DataMMUnificazione";
  public static final String CAMPO_DATA_AAAA_UNIFICAZIONE = "DataAAAAUnificazione";
  public static final String CAMPO_ID_FASCICOLO_UNIFICANTE = "IdFascicoloUnificante";
  public static final String CAMPO_ID_FASCICOLO_UNIFICATO = "IdFascicoloUnificato";
  public static final String CAMPO_ID_EVENTO_UNIFICAZIONE = "IdEvento";
  public static final String ACTION_DOPO_CANCELLAZIONE    = "ActDopoCanc";  // 30/04/2004

  public static final String PG_LOAD_VERIFICADECRETOUNIFICAZIONE	= IWebConstants.ROOT_DIR + "files/siap/sius/decretounificazione/LoadVerificaDecretoUnificazione.jsp";
  public static final String PG_LOAD_INSERISCIDECRETOUNIFICAZIONE	= IWebConstants.ROOT_DIR + "files/siap/sius/decretounificazione/LoadInserisciDecretoUnificazione.jsp";
  public static final String PG_INSERISCIDECRETOUNIFICAZIONE	= IWebConstants.ROOT_DIR + "files/siap/sius/decretounificazione/InserisciDecretoUnificazione.jsp";
  public static final String PG_DETTAGLIODECRETOUNIFICAZIONE	= IWebConstants.ROOT_DIR + "files/siap/sius/decretounificazione/DettaglioDecretoUnificazione.jsp";

/*
  public static final String TEMPLATE_ORDINANZA_GENERICO = "SIUS_OR_018";
  public static final String TEMPLATE_ORDINANZA_NLP_GENERICO = "SIUS_OR_019";
  public static final String TEMPLATE_ORDINANZA_RIGETTO_GENERICO = "SIUS_OR_003";
*/
}