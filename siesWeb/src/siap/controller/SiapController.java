package siap.controller;

import f3b.controller.GenericController;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class SiapController extends GenericController
//extends f3b.
{

/**
   * Ritorna la connessione dal POOL impostando l'autocommit a <code>false</code>.
   * <p>
   * @return la connessione al DBase prelevata dal pool.
   * @throws F3BException propaga l'errore di eccezione.

  protected static synchronized Connection getDBConnectionForListner() throws F3BException
  {
    try
    {

      Properties lProps = new Properties();
      lProps.setProperty("java.naming.factory.initial","org.apache.naming.java.javaURLContextFactory");
      lProps.setProperty("java.naming.factory.url.pkgs","org.apache.naming");

      Context lInitialCtx = new InitialContext(lProps);

      //Context envCtx = (Context)initCtx.lookup("java:comp/env");
      Context lEnvCtx = (Context)lInitialCtx.lookup(F3BProperties.getProperty("ctx.env"));

      //DataSource ds = (DataSource)envCtx.lookup("jdbc/siap");
      DataSource lDataSource = (DataSource)lEnvCtx.lookup(F3BProperties.getProperty("datasource.ctx"));

      Connection lConn = lDataSource.getConnection();
      lConn.setAutoCommit( false );

      return lConn;
    }
    catch(SQLException sqlex)
    {
      sqlex.printStackTrace();
      throw new F3BException(sqlex.getMessage());
    }
    catch(NamingException ex)
    {
      ex.printStackTrace();
      throw new F3BException(ex.getMessage());
    }
  }
*/
}