package siap.sico.w_magistrato.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sico.w_magistrato.model.WMagistratoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: WMagistratoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella WMagistrato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class WMagistratoDAO extends SIAPTableDAO
{
	public WMagistratoDAO (Connection con)
	{
			 super(con);
			 setTable("W_MAGISTRATO");

			 //Settare la Sequence e i campi chiave

			 setField("COD_MAGISTRATO", STRING);
			 setField("COGNOME", STRING);
			 setField("NOME", STRING);
			 setField("DATA_NASCITA", DATE);
			 setField("DESC_LUOGO_NASCITA", STRING);
			 setField("DATA_CARICAMENTO", DATE);
	}


  //
  // METODI GET()
  //

			public String 				 getCodMagistrato() 		throws DAOException	 { return getString("COD_MAGISTRATO"); }
			public String 				 getCognome() 		throws DAOException	 { return getString("COGNOME"); }
			public String 				 getNome() 		throws DAOException	 { return getString("NOME"); }
			public Date 					 getDataNascita() 		throws DAOException	 { return getDate("DATA_NASCITA"); }
			public String 				 getDescLuogoNascita() 		throws DAOException	 { return getString("DESC_LUOGO_NASCITA"); }
			public Date 					 getDataCaricamento() 		throws DAOException	 { return getDate("DATA_CARICAMENTO"); }


  //
  // METODI SET()
  //

			public void  	 setCodMagistrato(String aValore ) 			 { setString("COD_MAGISTRATO", aValore); }
			public void  	 setCognome(String aValore ) 			 { setString("COGNOME", aValore); }
			public void  	 setNome(String aValore ) 			 { setString("NOME", aValore); }
			public void  	 setDataNascita(Date aValore ) 			 { setDate("DATA_NASCITA", aValore); }
			public void  	 setDescLuogoNascita(String aValore ) 			 { setString("DESC_LUOGO_NASCITA", aValore); }
			public void  	 setDataCaricamento(Date aValore ) 			 { setDate("DATA_CARICAMENTO", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new WMagistratoModel(
								 getCodMagistrato() ,
								 "",
								 getCognome() ,
								 getNome() ,
								 getDataNascita() ,
								 getDescLuogoNascita() ,
								 getDataCaricamento()
								);
		}


	 public void 	 setDAOFromModel(WMagistratoModel aModel) throws DAOException
  		{
				 setCodMagistrato( aModel.getCodMagistrato() );
				 setCognome( aModel.getCognome() );
				 setNome( aModel.getNome() );
				 setDataNascita( aModel.getDataNascita() );
				 setDescLuogoNascita( aModel.getDescLuogoNascita() );
				 setDataCaricamento( aModel.getDataCaricamento() );
		}


	 public void 	 setDAOFromModelForUpdate(WMagistratoModel aModel) throws DAOException
  		{
				 setCodMagistrato( aModel.getCodMagistrato() );
				 setCognome( aModel.getCognome() );
				 setNome( aModel.getNome() );
				 setDataNascita( aModel.getDataNascita() );
				 setDescLuogoNascita( aModel.getDescLuogoNascita() );
				 setDataCaricamento( aModel.getDataCaricamento() );
		 //setCondizioneUpdate(aModel.getIdWMagistrato()); commento Luigi: 8-5-03
                 		}


	public void setCondizione(WMagistratoModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_W_MAGISTRATO = " + key );
		 }

}
