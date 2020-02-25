package siap.dao;

import java.sql.Connection;

public class SIAPTableDAO extends f3b.dao.TableOracleDAO
{
  public SIAPTableDAO(Connection aCon)
  {
    super(aCon);
  }

  /**
   * Metodo sovrascritto per limitare l'elenco di occorrenze
   * a max 200 elementi.
   * <p>
   * @throws DAOException propaga errore di eccezione.
   */
 /* public void start() throws f3b.dao.DAOException
  {
    start(1, 200);
  }*/

  /**
   * Metodo sovrascritto, ritorna l'insieme di model, per tutte le occorrenze.
   * Max 200
   * <p>
   * @return l'insieme di models.
   * @throws DAOException propaga l'errore di ecceione.
   */
 /* public java.util.Collection getModels() throws f3b.dao.DAOException
  {
    return getModels(1,200);
  }*/

}