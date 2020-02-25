package siap.siep.penasospesa.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.penasospesa.model.AnnmanReatoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: AnnmanReatoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella AnnmanReato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class AnnmanReatoDAO extends TableDAO 
{
	public AnnmanReatoDAO (Connection con) 
	{
			 super(con);
			 setTable("ANNMAN_REATO");

			 //Settare la Sequence e i campi chiave
			setSequenceField("ID_ANNMAN_REATO", "ANNMAN_REATO_SEQ");
			setFieldKey("ID_ANNMAN_REATO", BIG_DECIMAL);

			 setField("ID_ANNMAN_REATO", BIG_DECIMAL);
			 setField("ANNOTAZIONEMANUALE_ID", BIG_DECIMAL);
			 setField("REATO_ID", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdAnnmanReato() 		throws DAOException	 { return getBigDecimal("ID_ANNMAN_REATO"); } 
			public BigDecimal 		 getAnnotazionemanualeId() 		throws DAOException	 { return getBigDecimal("ANNOTAZIONEMANUALE_ID"); } 
			public BigDecimal 		 getReatoId() 		throws DAOException	 { return getBigDecimal("REATO_ID"); } 


  //
  // METODI SET()
  //

			public void  	 setIdAnnmanReato(BigDecimal aValore ) 			 { setBigDecimal("ID_ANNMAN_REATO", aValore); } 
			public void  	 setAnnotazionemanualeId(BigDecimal aValore ) 			 { setBigDecimal("ANNOTAZIONEMANUALE_ID", aValore); } 
			public void  	 setReatoId(BigDecimal aValore ) 			 { setBigDecimal("REATO_ID", aValore); } 


	public GenericModel getModel() throws DAOException
  			 { 
 				 return new AnnmanReatoModel(  
								 getIdAnnmanReato() , 
								 getAnnotazionemanualeId() , 
								 getReatoId()  
								);
		}


	 public void 	 setDAOFromModel(AnnmanReatoModel aModel) throws DAOException
  		{
				 setIdAnnmanReato( aModel.getIdAnnmanReato() );  
				 setAnnotazionemanualeId( aModel.getAnnotazionemanualeId() );  
				 setReatoId( aModel.getReatoId() );  
		}


	 public void 	 setDAOFromModelForUpdate(AnnmanReatoModel aModel) throws DAOException
  		{
				 setIdAnnmanReato( aModel.getIdAnnmanReato() );  
				 setAnnotazionemanualeId( aModel.getAnnotazionemanualeId() );  
				 setReatoId( aModel.getReatoId() );  
		 setCondizioneUpdate(aModel.getIdAnnmanReato());
		}


	public void setCondizione(AnnmanReatoModel aModel)
		 {
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_ANNMAN_REATO = " + key ); 
		 }

}
