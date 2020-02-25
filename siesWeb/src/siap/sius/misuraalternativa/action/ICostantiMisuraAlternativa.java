package siap.sius.misuraalternativa.action;

import f3b.web.IWebConstants;

/**
 * <p>Title: ICostantiMisuraAlternativa</p>
 * <p>Description: Classe di costanti di MisuraAlternativa</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public interface ICostantiMisuraAlternativa
{
  public static final String COD_ESITO_CONCEDE = "0001";
  public static final String COD_ESITO_CONCEDE_SOSPENSIONE_PENA_DETENZIONE_CASA ="0040";
  public static final String PG_LOADINSERISCIDATAINIZIOMISURAALTERNATIVA = IWebConstants.ROOT_DIR + "files/siap/sius/misuraalternativa/LoadInserisciDataInizioMisuraAlternativa.jsp";
  public static final String PG_LOADDETTAGLIODATAINIZIOMISURAALTERNATIVA = IWebConstants.ROOT_DIR + "files/siap/sius/misuraalternativa/LoadDettaglioDataInizioMisuraAlternativa.jsp";

}
