package siap.sico.versione.dao;

import java.sql.Connection;

import siap.sico.versione.model.VersioneModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
* <p>Title: VersioneSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Versione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class VersioneSqlDAO extends SqlDAO
{
	 public VersioneSqlDAO (Connection con)
			{
			 super(con);
			}

 //
  // METODO RICERCA()
  //

  public void ricercaVersione()	 throws DAOException
		{
			 String lSql = getSqlQuery();
			 setStatement(lSql);
		}


  protected String getSqlQuery()
		{			 String lStatement = new String("");

     lStatement += " SELECT " +
				 "COD_VERSIONE, "+
				 "DATA, "+
				 "DESCRIZIONE ";
     lStatement += " FROM (SELECT * FROM VERSIONE ORDER BY DATA DESC)";
     lStatement += " WHERE ROWNUM = 1";
			 return lStatement;

     }


 //
  // METODO GETMODEL()
  //

	 public GenericModel  	 getModel() throws DAOException
  		{
				 VersioneModel aModel = new  VersioneModel();

//Inserire le opportune set delle descrizioni!
				 aModel.setCodVersione(getString("COD_VERSIONE") );
				 aModel.setData(getDate("DATA") );
				 aModel.setDescrizione(getString("DESCRIZIONE") );
				 return aModel;
		}

}
