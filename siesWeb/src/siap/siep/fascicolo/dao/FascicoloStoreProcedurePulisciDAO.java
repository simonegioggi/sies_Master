package siap.siep.fascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.StoreProcedureDAO;

/**
 * <p>Title: FascicoloStoreProcedurePulisciDAO</p>
 * <p>Description: Store Procedure PULISCI_ALTRE_BDI.pulisci_fascicolo</p>
 * <p>Copyright: Copyright (c) 2002</p>
 */
public class FascicoloStoreProcedurePulisciDAO
  extends StoreProcedureDAO
{
  public FascicoloStoreProcedurePulisciDAO(Connection lConn)
  {
    	 super(lConn);
			 setStoreProcedure("PULISCI_ALTRE_BDI.pulisci_fascicolo");

			 //Settare  i campi chiave di INput e di output della Store Procedure
			 this.setArgInput("ID_FASCICOLO_SIEP", BIG_DECIMAL);
       this.setArgOutput("RETURN", java.sql.Types.VARCHAR);

		}


	public void  	 setIdFascicolo(BigDecimal aValore ) 	 { setBigDecimal("ID_FASCICOLO_SIEP", aValore); }
  public String  getReturn() 		throws DAOException		 { return getOutString("RETURN"); }

}