package siap.siep.provvedimentogenerico.action;

import f3b.web.IWebConstants;

/**
 * <p>Title: ICostantiProvvedimentoGenerico</p>
 * <p>Description: Classe di costanti di Provvedimento Generico</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public interface ICostantiProvvedimentoGenerico
{
  public static final String CAMPO_ANNO_REGISTRO = "AnnoRegistro";
  public static final String CAMPO_NUMERO_REGISTRO = "NumeroRegistro";
  public static final String CAMPO_COD_REGISTRO = "CodRegistro";
  public static final String CAMPO_GIORNO_DATA_EMISSIONE = "GiornoDataEmissione";
  public static final String CAMPO_MESE_DATA_EMISSIONE = "MeseDataEmissione";
  public static final String CAMPO_ANNO_DATA_EMISSIONE = "AnnoDataEmissione";
  public static final String CAMPO_ANNO_PROVVEDIMENTO = "AnnoProvvedimento";
  public static final String CAMPO_NUMERO_PROVVEDIMENTO = "NumeroProvvedimento";
  public static final String CAMPO_COD_TIPO_PROVVEDIMENTO = "CodTipoProvvedimento";
  public static final String CAMPO_COD_AUTORITA = "CodAutorita";
  public static final String CAMPO_SEDE_AUTORITA = "SedeAutorita";
  public static final String CAMPO_CONTENUTO = "Contenuto";
  public static final String CAMPO_OGGETTO = "Oggetto";
  public static final String CAMPO_ESITO = "Esito";
  public static final String CAMPO_NOTE = "Note";

  public static final String PG_LOAD_INSERICI_PROVVEDIMENTO_GENERICO = IWebConstants.ROOT_DIR + "files/siap/siep/provvedimentogenerico/LoadInserisciProvvedimentoGenerico.jsp";
  public static final String PG_LOAD_DETTAGLIO_PROVVEDIMENTO_GENERICO = IWebConstants.ROOT_DIR + "files/siap/siep/provvedimentogenerico/LoadDetteglioProvvedimentoGenerico.jsp";

}