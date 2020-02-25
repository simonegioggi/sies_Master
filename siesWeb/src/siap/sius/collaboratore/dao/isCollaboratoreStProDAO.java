package siap.sius.collaboratore.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.StoreProcedureDAO;

/**
 * <p>Title: isCollaboratoreStProcDAO</p>
 * <p>Description:</p>
 * <p> La classe costituisce l'interfaccia alla funzione PlSql isCollaboratore():
 * <p> Schema : COLLA
 * <p>Nome del package : COLL
 * <p>function : isCollaboratore ( par_ufficio IN VARCHAR2, par_data IN VARCHAR2, par_ID IN number) return boolean.
 * 
 * <p>Copyright: Copyright (c) 2007</p>
 */
public class isCollaboratoreStProDAO extends StoreProcedureDAO
{
  public isCollaboratoreStProDAO(Connection lConn)
  {
    super(lConn);
    // Nome della Stored Procedure 
    setStoreProcedure("COLLA.COLL.isCollaboratore");
   
    //Settare  i campi chiave di INput e di output della Store Procedure
	this.setArgInput("COD_UFFICIO", STRING);
	this.setArgInputPosition("COD_UFFICIO",1);

	this.setArgInput("ID", BIG_DECIMAL);
	this.setArgInputPosition("ID",2);
	
    this.setArgOutput("CONT", java.sql.Types.INTEGER);
  }
  
  public void setCodUfficio(String aValore ) 	 
  {
	setString("COD_UFFICIO", aValore); 
  }

  public void  setID(BigDecimal aValore ) 	   
  { 
  	setBigDecimal("ID", aValore); 
  }

  public int  getReturn() 		throws DAOException		 { return getOutInteger("CONT"); }


  /**
   * La funzione viene utilizzata per controllare l'esistenza del package di interfaccia alla gestione del collaboratore di giustizia.
   * La funzione effettua una ricerca del package e ritorna false se questo non viene rilevato. 
   * @throws Exception
   */
  public boolean esistePackage() throws Exception
  {
	boolean retValue = true;
	  setStatement(" SELECT * FROM ALL_OBJECTS WHERE OBJECT_NAME = 'COLL' ");
	  start();
	  if (next())
		  retValue = true;
	  else
		  retValue = false;
	  stop();

    return retValue;
  }

}