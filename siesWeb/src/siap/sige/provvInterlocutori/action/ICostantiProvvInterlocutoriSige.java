package siap.sige.provvInterlocutori.action;

/**
* <p>Title: ICostantiProvvedimentoSige</p>
* <p>Description: Classe di costanti di ProvvedimentoSige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/

import f3b.web.IWebConstants;
public interface ICostantiProvvInterlocutoriSige
{
  // Costante Esito x Conflitto di Competenza
  public static final String COD_ESITO_CONFLITTO_COMPETENZA = "0502";
  public static final String COD_ESITO_DICHIARA_LATITANZA = "0503";
  public static final String COD_ESITO_DICHIARA_IRREPERIBILITA = "0504";
  public static final String COD_ESITO_NOMINA_PERITI = "0505";
  public static final String COD_ESITO_CITAZIONE_TESTI= "0506";
  
  public static final String PG_LOAD_DECRETO_LATITANZA =  IWebConstants.ROOT_DIR + "files/siap/sige/provvInterlocutori/LoadInserisciDecretoLatitanza.jsp";
  public static final String PG_DETTAGLIO_DECRETO_LATITANZA =  IWebConstants.ROOT_DIR + "files/siap/sige/provvInterlocutori/DettaglioDecretoLatitanza.jsp";
  
  public static final String PG_LOAD_DECRETO_IRREPERIBILITA =  IWebConstants.ROOT_DIR + "files/siap/sige/provvInterlocutori/LoadInserisciDecretoIrreperibilita.jsp";
  public static final String PG_DETTAGLIO_DECRETO_IRREPERIBILITA =  IWebConstants.ROOT_DIR + "files/siap/sige/provvInterlocutori/DettaglioDecretoIrreperibilita.jsp";
  
  public static final String PG_LOAD_NOMINA_PERITI =  IWebConstants.ROOT_DIR + "files/siap/sige/provvInterlocutori/LoadInserisciNominaPeriti.jsp";
  public static final String PG_DETTAGLIO_NOMINA_PERITI =  IWebConstants.ROOT_DIR + "files/siap/sige/provvInterlocutori/DettaglioNominaPeriti.jsp";
  public static final String PG_LOAD_MODIFICA_PERITI =  IWebConstants.ROOT_DIR + "files/siap/sige/provvInterlocutori/ModificaNominaPeriti.jsp";
  public static final String PG_MODIFICA_CITAZIONE_TESTI =  IWebConstants.ROOT_DIR + "files/siap/sige/provvInterlocutori/ModificaCitazioneTesti.jsp";
  public static final String PG_MODIFICA_DECRETO_LATITANZA =  IWebConstants.ROOT_DIR + "files/siap/sige/provvInterlocutori/ModificaDecretoLatitanza.jsp";
  public static final String PG_MODIFICA_DECRETO_IRREPERIBILITA =  IWebConstants.ROOT_DIR + "files/siap/sige/provvInterlocutori/ModificaDecretoIrreperibilita.jsp";

  public static final String PG_LOAD_CITAZIONE_TESTI =  IWebConstants.ROOT_DIR + "files/siap/sige/provvInterlocutori/LoadInserisciCitazioneTesti.jsp";
  public static final String PG_DETTAGLIO_CITAZIONE_TESTI =  IWebConstants.ROOT_DIR + "files/siap/sige/provvInterlocutori/DettaglioCitazioneTesti.jsp";
  public static final String PG_LOAD_ORDINANZA_CONFLITTO_COMPETENZA =  IWebConstants.ROOT_DIR + "files/siap/sige/provvInterlocutori/LoadInserisciOrdinanzaConflittoCompetenza.jsp";
  public static final String PG_DETTAGLIO_ORDINANZA_CONFLITTO_COMPETENZA =  IWebConstants.ROOT_DIR + "files/siap/sige/provvInterlocutori/DettaglioOrdinanzaConflittoCompetenza.jsp";
}