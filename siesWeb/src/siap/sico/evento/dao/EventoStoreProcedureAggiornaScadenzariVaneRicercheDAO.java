package siap.sico.evento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.StoreProcedureDAO;

/**
 * <p>Title: EventoStoreProcedureAggiornaScadenzariVaneRicercheDAO</p>
 * <p>Description: Store Procedure AGGIORNA_SCADENZARIO_TIPO03.update_scadenzario_03</p>
 * <p>Copyright: Copyright (c) 2011</p>
 */
public class EventoStoreProcedureAggiornaScadenzariVaneRicercheDAO
  extends StoreProcedureDAO
{
  public EventoStoreProcedureAggiornaScadenzariVaneRicercheDAO(Connection lConn)
  {	  								
    super(lConn);
    setStoreProcedure("AGGIORNA_SCADENZARIO_TIPO03.update_scadenzario_03");

    //Settare  i campi chiave di INput e di output della Store Procedure
    this.setArgInput("par_anno", BIG_DECIMAL);
    this.setArgInput("par_bdi", STRING);
    this.setArgInput("par_ufficio", STRING);
    this.setArgInput("par_id_fascicolo", BIG_DECIMAL);
    this.setArgInputPosition("par_anno", 1);
    this.setArgInputPosition("par_bdi", 2);
    this.setArgInputPosition("par_ufficio", 3);
    this.setArgInputPosition("par_id_fascicolo", 4);
   // this.setArgOutput("RETURN", java.sql.Types.VARCHAR);    
  }

	public void  	 setpar_anno(BigDecimal aValore ) 	   		{ setBigDecimal("par_anno", aValore); }
	public void  	 setpar_bdi(String aValore ) 	   			{ setString("par_bdi", aValore); }
	public void  	 setpar_ufficio(String aValore ) 	   		{ setString("par_ufficio", aValore); }
	public void  	 setpar_id_fascicolo(BigDecimal aValore ) 	{ setBigDecimal("par_id_fascicolo", aValore); }

 // public String  getReturn() 		throws DAOException		 { return getOutString("RETURN"); }
}