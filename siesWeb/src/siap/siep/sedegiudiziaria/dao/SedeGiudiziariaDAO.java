package siap.siep.sedegiudiziaria.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.sedegiudiziaria.model.SedeGiudiziariaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;



/**
* <p>Title: SedeGiudiziariaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella SedeGiudiziaria</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class SedeGiudiziariaDAO extends TableDAO
{
	public SedeGiudiziariaDAO (Connection con)
	{
			 super(con);
			 setTable("SEDE_GIUDIZIARIA");

			 //Settare la Sequence e i campi chiave

			 setField("COD_SEDE_GIUDIZIARIA", STRING);
			 setField("DESCRIZIONE", STRING);
			 setField("DATA_CARICAMENTO_REGE", DATE);
			 setField("COD_COMUNE", STRING);
	}


  //
  // METODI GET()
  //

			public String 				 getCodSedeGiudiziaria() 		throws DAOException	 { return getString("COD_SEDE_GIUDIZIARIA"); }
			public String 				 getDescrizione() 		throws DAOException	 { return getString("DESCRIZIONE"); }
			public Date 					 getDataCaricamentoRege() 		throws DAOException	 { return getDate("DATA_CARICAMENTO_REGE"); }
			public String 				 getCodComune() 		throws DAOException	 { return getString("COD_COMUNE"); }


  //
  // METODI SET()
  //

			public void  	 setCodSedeGiudiziaria(String aValore ) 			 { setString("COD_SEDE_GIUDIZIARIA", aValore); }
			public void  	 setDescrizione(String aValore ) 			 { setString("DESCRIZIONE", aValore); }
			public void  	 setDataCaricamentoRege(Date aValore ) 			 { setDate("DATA_CARICAMENTO_REGE", aValore); }
			public void  	 setCodComune(String aValore ) 			 { setString("COD_COMUNE", aValore); }


	/*public GenericModel getModel() throws DAOException
  			 {
 				 return new SedeGiudiziariaModel(
								 getCodSedeGiudiziaria() ,
								 "",
								 getDescrizione() ,
								 getDataCaricamentoRege() ,
								 getCodComune()
								);
		}*/


	 public void 	 setDAOFromModel(SedeGiudiziariaModel aModel) throws DAOException
  		{
				 setCodSedeGiudiziaria( aModel.getCodSedeGiudiziaria() );
				 setDescrizione( aModel.getDescrizione() );
				 setDataCaricamentoRege( aModel.getDataCaricamentoRege() );
				 setCodComune( aModel.getCodComune() );
		}


/*	 public void 	 setDAOFromModelForUpdate(SedeGiudiziariaModel aModel) throws DAOException
  		{
				 setCodSedeGiudiziaria( aModel.getCodSedeGiudiziaria() );
				 setDescrizione( aModel.getDescrizione() );
				 setDataCaricamentoRege( aModel.getDataCaricamentoRege() );
				 setCodComune( aModel.getCodComune() );
		     setCondizioneUpdate(aModel.getIdSedeGiudiziaria());
		}*/


	public void setCondizione(SedeGiudiziariaModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_SEDE_GIUDIZIARIA = '" + key + "'" );
		 }

}
