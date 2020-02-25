package f3b.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p>Title: SqlOracleDAO </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class SqlOracleDAO extends SqlDAO
{
  public SqlOracleDAO(Connection aCon)
  {
    super(aCon);
  }


 /**
   * Imposta il campo di riferimento con un valore del tipo <code>Blob</code>.
   * <p>
   * @param aFieldName nome del campo di riferimeno.
   * @param aValue valore da impostare.
   */
public ByteArrayOutputStream getBlob(String aFieldName) throws DAOException
  {
     ByteArrayOutputStream lStream = null;
    try
    {
      Blob blob = super.mRs.getBlob(aFieldName);

      //modifica dario 23-02-05
      if(blob != null)
      {
        BigDecimal lLen = new BigDecimal(blob.length());
        byte[] lBuffer = new byte[lLen.intValue()];
        lStream = new ByteArrayOutputStream();
        lBuffer = blob.getBytes(1, lLen.intValue());
        lStream.write(lBuffer);
        lStream.flush();
        lStream.close();
      }
      else
      {
         lStream = new ByteArrayOutputStream();
      }
    }
    catch (SQLException sqlEx)
    {
      sqlEx.printStackTrace();
      throw new DAOException(sqlEx);
    }
    catch (IOException ioEx)
    {
      ioEx.printStackTrace();
      throw new DAOException("Errore nell'apertura dello Stream!");
    }
    return lStream;
  }





}