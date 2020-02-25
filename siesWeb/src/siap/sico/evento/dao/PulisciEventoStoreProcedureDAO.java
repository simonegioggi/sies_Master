package siap.sico.evento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.StoreProcedureDAO;

/**
 * <p>Title: PulisciEventoStoreProcedureDAO</p>
 * <p>Description: Store Procedure PULISCI_ALTRE_BDI.pulisci_evento</p>
 * <p>Copyright: Copyright (c) 2005</p>
 */
public class PulisciEventoStoreProcedureDAO
extends StoreProcedureDAO
{
  public PulisciEventoStoreProcedureDAO(Connection lConn)
  {
		super(lConn);
		setStoreProcedure("PULISCI_ALTRE_BDI.pulisci_evento");

		//Settare  i campi chiave di Input e di output della Store Procedure
		this.setArgInput("ID_EVENTO", BIG_DECIMAL);
		this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
  }

	public void  	 setIdEvento(BigDecimal aValore ) 	 { setBigDecimal("ID_EVENTO", aValore); }

  public String  getReturn() 		throws DAOException	 { return getOutString("RETURN"); }
}