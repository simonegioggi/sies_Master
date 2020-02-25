package f3b.dao;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;

import f3b.model.GenericModel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public interface ITableDAO
{
   public BigDecimal getNextSeq(String aSequenceName)  throws DAOException;
   public void delete()                                throws DAOException;
   public BigDecimal insert()                          throws DAOException;
   public void insertWithSelect()                      throws DAOException;
   public void selByKey()                              throws DAOException;
   public void start(int aFirstRec, int aLastRec)      throws DAOException;
   public void update()                                throws DAOException;
   public void setBigDecimal(String aFieldName, BigDecimal aValue);
   public void setBinaryStream(String aFieldName, InputStream aValue);
   public void setBlob(String aFieldName, Blob aValue);
   public void getBlob(String aFieldName);
   public GenericModel getModel()                      throws DAOException;
}