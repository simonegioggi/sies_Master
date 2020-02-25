package f3b.dao;

import java.sql.Connection;

/**
 * <p>Title: SqlDAO </p>
 * <p>Description: Classe padre per la gestione delle query complesse, infatti
 *                 eredita tutte le proprietà di GenericDAO pertatnto dovrà essere
 *                 ereditata da tutte le classi che hanno la responsabilità di effettuare
 *                 interrogazioni complesse su più tabelle.</p>
 *
 * <p>Company: Bull Italia S.p.A.</p>
 */
public class SqlDAO extends GenericDAO
{
  /**
   * Costruttore di classe con la connessione al db come parametro.
   * <p>
   * @param aCon Connessione la Dbase.
   */
  public SqlDAO(Connection aCon)
  {
    super(aCon);
  }

}
