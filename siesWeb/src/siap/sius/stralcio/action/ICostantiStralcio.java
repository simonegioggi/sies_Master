package siap.sius.stralcio.action;

/**
* <p>Title: ICostantiStralcio</p>
* <p>Description: Classe di costanti di Stralcio</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import f3b.web.IWebConstants;
public interface ICostantiStralcio
{
  public static final String CAMPO_GIORNO_DATA_STRALCIO = "GiornoDataStralcio";
  public static final String CAMPO_MESE_DATA_STRALCIO = "MeseDataStralcio";
  public static final String CAMPO_ANNO_DATA_STRALCIO = "AnnoDataStralcio";
  public static final String CAMPO_CHECKBOX = "Checkbox";
  public static final String CAMPO_TIPO_STRALCIO = "tipoStralcio";
  public static final String CAMPO_ID_EVENTO_STRALCIO = "IdEvento";
  public static final String CAMPO_CHIAVE_ANNO = "ChiaveAnno";
  public static final String CAMPO_CHIAVE_PROGR = "ChiaveProgr";
  public static final String COD_EVENTO_PROVVEDIMENTO = "48";

  public static final String ACTION_DOPO_CANCELLAZIONE    = "ActDopoCanc";

  public static final String PG_LOAD_INSERISCISTRALCIO  = IWebConstants.ROOT_DIR + "files/siap/sius/stralcio/LoadInserisciStralcio.jsp";
  public static final String PG_DETTAGLIOSTRALCIO       = IWebConstants.ROOT_DIR + "files/siap/sius/stralcio/DettaglioStralcio.jsp";
}
