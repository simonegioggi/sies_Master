package siap.sico.evento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.StoreProcedureDAO;

/**
 * <p>Title: EventoStoreProcedurePulisciDAO</p>
 * <p>Description: Store Procedure PULISCI.pulisci_evento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 */
public class EventoStoreProcedurePulisciDAO
  extends StoreProcedureDAO
{
  public EventoStoreProcedurePulisciDAO(Connection lConn)
  {
    super(lConn);
    setStoreProcedure("PULISCI.Pulisci_Evento");

    //Settare  i campi chiave di INput e di output della Store Procedure
    this.setArgInput("ID_EVENTO", BIG_DECIMAL);
    this.setArgOutput("RETURN", java.sql.Types.VARCHAR);
  }

	public void  	 setIdEvento(BigDecimal aValore ) 	   { setBigDecimal("ID_EVENTO", aValore); }

  public String  getReturn() 		throws DAOException		 { return getOutString("RETURN"); }
}