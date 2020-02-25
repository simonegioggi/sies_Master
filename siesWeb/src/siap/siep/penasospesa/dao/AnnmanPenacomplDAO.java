package siap.siep.penasospesa.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.penasospesa.model.AnnmanPenacomplModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: AnnmanPenacomplDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella AnnmanPenacompl</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class AnnmanPenacomplDAO extends TableDAO 
{
	public AnnmanPenacomplDAO (Connection con) 
	{
			 super(con);
			 setTable("ANNMAN_PENACOMPL");

			 //Settare la Sequence e i campi chiave
				setSequenceField("ID_ANNMAN_PENACOMPL", "ANNMAN_PCOMPL_SEQ");
				setFieldKey("ID_ANNMAN_PENACOMPL", BIG_DECIMAL);

			 setField("ID_ANNMAN_PENACOMPL", BIG_DECIMAL);
			 setField("ANNOTAZIONEMANUALE_ID", BIG_DECIMAL);
			 setField("PENACOMPLESSIVA_ID", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdAnnmanPenacompl() 		throws DAOException	 { return getBigDecimal("ID_ANNMAN_PENACOMPL"); } 
			public BigDecimal 		 getAnnotazionemanualeId() 		throws DAOException	 { return getBigDecimal("ANNOTAZIONEMANUALE_ID"); } 
			public BigDecimal 		 getPenacomplessivaId() 		throws DAOException	 { return getBigDecimal("PENACOMPLESSIVA_ID"); } 


  //
  // METODI SET()
  //

			public void  	 setIdAnnmanPenacompl(BigDecimal aValore ) 			 { setBigDecimal("ID_ANNMAN_PENACOMPL", aValore); } 
			public void  	 setAnnotazionemanualeId(BigDecimal aValore ) 			 { setBigDecimal("ANNOTAZIONEMANUALE_ID", aValore); } 
			public void  	 setPenacomplessivaId(BigDecimal aValore ) 			 { setBigDecimal("PENACOMPLESSIVA_ID", aValore); } 


	public GenericModel getModel() throws DAOException
  			 { 
 				 return new AnnmanPenacomplModel(  
								 getIdAnnmanPenacompl() , 
								 getAnnotazionemanualeId() , 
								 getPenacomplessivaId()  
								);
		}


	 public void 	 setDAOFromModel(AnnmanPenacomplModel aModel) throws DAOException
  		{
				 setIdAnnmanPenacompl( aModel.getIdAnnmanPenacompl() );  
				 setAnnotazionemanualeId( aModel.getAnnotazionemanualeId() );  
				 setPenacomplessivaId( aModel.getPenacomplessivaId() );  
		}


	 public void 	 setDAOFromModelForUpdate(AnnmanPenacomplModel aModel) throws DAOException
  		{
				 setIdAnnmanPenacompl( aModel.getIdAnnmanPenacompl() );  
				 setAnnotazionemanualeId( aModel.getAnnotazionemanualeId() );  
				 setPenacomplessivaId( aModel.getPenacomplessivaId() );  
		 setCondizioneUpdate(aModel.getIdAnnmanPenacompl());
		}


	public void setCondizione(AnnmanPenacomplModel aModel)
		 {
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_ANNMAN_PENACOMPL = " + key ); 
		 }

}
