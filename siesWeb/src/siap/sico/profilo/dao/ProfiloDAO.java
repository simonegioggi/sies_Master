package siap.sico.profilo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sico.profilo.model.ProfiloModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: ProfiloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Profilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ProfiloDAO extends TableDAO
{
	public ProfiloDAO (Connection con)
	{
			 super(con);
			 setTable("PROFILO");

			 //Settare la Sequence e i campi chiave

			 setField("COD_PROFILO", BIG_DECIMAL);
			 setField("DESCRIZIONE", STRING);
			 setField("DATA_FINE_VALIDITA", DATE);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getCodProfilo() 		throws DAOException	 { return getBigDecimal("COD_PROFILO"); }
			public String 				 getDescrizione() 		throws DAOException	 { return getString("DESCRIZIONE"); }
			public Date 					 getDataFineValidita() 		throws DAOException	 { return getDate("DATA_FINE_VALIDITA"); }


  //
  // METODI SET()
  //

			public void  	 setCodProfilo(BigDecimal aValore ) 			 { setBigDecimal("COD_PROFILO", aValore); }
			public void  	 setDescrizione(String aValore ) 			 { setString("DESCRIZIONE", aValore); }
			public void  	 setDataFineValidita(Date aValore ) 			 { setDate("DATA_FINE_VALIDITA", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new ProfiloModel(
								 getCodProfilo() ,
								 getDescrizione() ,
								 getDataFineValidita()
								);
		}


	 public void 	 setDAOFromModel(ProfiloModel aModel) throws DAOException
  		{
				 setCodProfilo( aModel.getCodProfilo() );
				 setDescrizione( aModel.getDescrizione() );
				 setDataFineValidita( aModel.getDataFineValidita() );
		}


	 public void 	 setDAOFromModelForUpdate(ProfiloModel aModel) throws DAOException
  		{
				// setCodProfilo( aModel.getCodProfilo() );
				 setDescrizione( aModel.getDescrizione() );
				 setDataFineValidita( aModel.getDataFineValidita() );
		 setCondizioneUpdate(aModel.getCodProfilo());
		}


	public void setCondizione(ProfiloModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition("COD_PROFILO = " + key );
		 }

}
