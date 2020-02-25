package siap.siep.sentenzariunita.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.sentenzariunita.model.SentenzaRiunitaFascSiepModel;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: SentenzariunitaFascSiepDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella SentenzariunitaFascSiep</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class SentenzaRiunitaFascSiepDAO extends TableDAO 
{
	public SentenzaRiunitaFascSiepDAO (Connection con) 
	{
			 super(con);
			 setTable("SENTENZARIUNITA_FASC_SIEP");
			 setFieldKey("ID_SENTENZARIUNITA_FASC_SIEP", BIG_DECIMAL);
			 setSequenceField("ID_SENTENZARIUNITA_FASC_SIEP","SEN_RIU_FAS_SIE_SEQ");

			 //Settare la Sequence e i campi chiave

			 setField("ID_SENTENZARIUNITA_FASC_SIEP", BIG_DECIMAL);
			 setField("SEN_RIU_ID_SENTENZA_RIUNITA", BIG_DECIMAL);
			 setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdSentenzaRiunitaFascSiep() 		throws DAOException	 { return getBigDecimal("ID_SENTENZARIUNITA_FASC_SIEP"); } 
			public BigDecimal 		 getSenRiuIdSentenzaRiunita() 		throws DAOException	 { return getBigDecimal("SEN_RIU_ID_SENTENZA_RIUNITA"); } 
			public BigDecimal 		 getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); } 
			public SentenzaRiunitaModel getSentenzaRiunitaModel()		throws DAOException	  { return getSentenzaRiunitaModel(); }


  //
  // METODI SET()
  //

			public void  	 setIdSentenzaRiunitaFascSiep(BigDecimal aValore ) 			 { setBigDecimal("ID_SENTENZARIUNITA_FASC_SIEP", aValore); } 
			public void  	 setSenRiuIdSentenzaRiunita(BigDecimal aValore ) 			 { setBigDecimal("SEN_RIU_ID_SENTENZA_RIUNITA", aValore); } 
			public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); } 
			 public void  	 setSentenzaRiunitaModel(SentenzaRiunitaModel aValore ) 			 { setSentenzaRiunitaModel (aValore); } 


	public GenericModel getModel() throws DAOException
  			 { 
 				 return new SentenzaRiunitaFascSiepModel(  
								 getIdSentenzaRiunitaFascSiep() , 
								 getSenRiuIdSentenzaRiunita() , 
								 getFasSieIdFascicoloSiep(),
								 getSentenzaRiunitaModel());
		}


	 public void 	 setDAOFromModel(SentenzaRiunitaFascSiepModel aModel) throws DAOException
  		{
				 setIdSentenzaRiunitaFascSiep( aModel.getIdSentenzaRiunitaFascSiep() );  
				 setSenRiuIdSentenzaRiunita( aModel.getSenRiuIdSentenzaRiunita() );  
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() ); 
				 //setSentenzaRiunitaModel(aModel.getSentenzaRiunitaModel());
		}


	 public void 	 setDAOFromModelForUpdate(SentenzaRiunitaFascSiepModel aModel) throws DAOException
  		{
				 setIdSentenzaRiunitaFascSiep( aModel.getIdSentenzaRiunitaFascSiep() );  
				 setSenRiuIdSentenzaRiunita( aModel.getSenRiuIdSentenzaRiunita() );  
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );  
		 setCondizioneUpdate(aModel.getIdSentenzaRiunitaFascSiep());
		}


	public void setCondizione(SentenzaRiunitaFascSiepModel aModel)
		 {
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_SENTENZARIUNITA_FASC_SIEP = " + key ); 
		 }

}
