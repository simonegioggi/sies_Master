package siap.sius.collaboratore.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.StoreProcedureDAO;



/**
 * <p>Title: delCollabStProDAO</p>
 * <p>Description:</p>
 * <p> La classe costituisce l'interfaccia alla funzione PlSql delCollab():</p>
 * <p> Schema : COLLA</p>
 * <p>Nome del package : COLL</p>
 * <p>  procedure delCollab ( par_id IN   number)</p>
 * <p> La funzione effettua la cancellazione del record Collaboratore individuato dall'ID par_id.</p>
 * <p>Copyright: Copyright (c) 2008</p>
 */
public class delCollabStProDAO extends StoreProcedureDAO
{
  public delCollabStProDAO(Connection aConn, BigDecimal aIdCollaboratore)
  {
    super(aConn);
    
    // Nome della Stored Procedure 
    setStoreProcedure("COLLA.COLL.delCollab");
   
    // Definizione degli argomenti 
	setArgInput("ID", BIG_DECIMAL);
	setArgInputPosition("ID",1);
    
    //  Valorizzazione dei parametri di Input
	setBigDecimal("ID", aIdCollaboratore); 
  }
}