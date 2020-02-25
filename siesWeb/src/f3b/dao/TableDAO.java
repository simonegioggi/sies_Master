package f3b.dao;

import java.sql.Connection;

/**
 * <p>Title: TableDAO</p>
 * <p>Description: Classe per la gestione dati al DBase
 * rifeririti da una singola tabella di un tipo DB specificato dal padre.</p>
 * <p>Company: Bull ITALIA S.p.A.</p>
 */
public class TableDAO extends TableOracleDAO
{
  /**
   *
   * <p>
   * @param aCon
   */
  public TableDAO(Connection aCon)
  {
    super(aCon);
  }
}
