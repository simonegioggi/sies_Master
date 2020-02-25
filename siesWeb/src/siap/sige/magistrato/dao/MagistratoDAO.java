package siap.sige.magistrato.dao;

import java.sql.Connection;

import siap.sige.magistrato.model.MagistratoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: MagistratoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Magistrato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class MagistratoDAO extends siap.sico.magistrato.dao.MagistratoDAO
{
  public MagistratoDAO (Connection con)
  {
    super(con);
  }

  public GenericModel getModel() throws DAOException
  {    
    MagistratoModel lModel = new MagistratoModel((MagistratoModel)super.getModel());
    return lModel;
  }
}
