package siap.sico.versione.dao;

import java.sql.Connection;
import java.util.Date;

import siap.sico.versione.model.VersioneModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: VersioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Versione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class VersioneDAO extends TableDAO
{
	public VersioneDAO (Connection con)
	{
			 super(con);
			 setTable("VERSIONE");

			 //Settare la Sequence e i campi chiave

			 setField("COD_VERSIONE", STRING);
			 setField("DATA", DATE);
			 setField("DESCRIZIONE", STRING);
	}


  //
  // METODI GET()
  //

			public String 				 getCodVersione() 		throws DAOException	 { return getString("COD_VERSIONE"); }
			public Date 					 getData() 		throws DAOException	 { return getDate("DATA"); }
			public String 				 getDescrizione() 		throws DAOException	 { return getString("DESCRIZIONE"); }


  //
  // METODI SET()
  //

			public void  	 setCodVersione(String aValore ) 			 { setString("COD_VERSIONE", aValore); }
			public void  	 setData(Date aValore ) 			 { setDate("DATA", aValore); }
			public void  	 setDescrizione(String aValore ) 			 { setString("DESCRIZIONE", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new VersioneModel(
								 getCodVersione() ,
								 getData() ,
								 getDescrizione()
								);
		}
}
