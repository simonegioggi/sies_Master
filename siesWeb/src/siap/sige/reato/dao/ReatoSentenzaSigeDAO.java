package siap.sige.reato.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sige.reato.model.ReatoSentenzaSigeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: ReatoSentenzaSigeDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella REATO_SENTENZA_SIGE</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class ReatoSentenzaSigeDAO extends TableDAO 
{
	public ReatoSentenzaSigeDAO (Connection con) 
	{
			 super(con);
			 setTable("REATO_SENTENZA_SIGE");

			 //Settare la Sequence e i campi chiave

			setField("REA_ID_REATO", BIG_DECIMAL);
			setField("FAS_SIGE_SEN_ID", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //
	public BigDecimal 		 getReaIdReato() 		throws DAOException	 { return getBigDecimal("REA_ID_REATO"); } 
	public BigDecimal 		 getFasSigeSenId() 		throws DAOException	 { return getBigDecimal("FAS_SIGE_SEN_ID"); } 


  //
  // METODI SET()
  //
	public void  	 setReaIdReato(BigDecimal aValore ) 			 { setBigDecimal("REA_ID_REATO", aValore); } 
	public void  	 setFasSigeSenId(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIGE_SEN_ID", aValore); } 


	public GenericModel getModel() throws DAOException
  	{ 
		return new ReatoSentenzaSigeModel(  
					getReaIdReato() , 
					getFasSigeSenId()  
				);
	}

	public void setDAOFromModel(ReatoSentenzaSigeModel aModel) throws DAOException
  	{
		setReaIdReato( aModel.getIdReato());  
		setFasSigeSenId( aModel.getFasSigeSenId() );  
	}


	public void setCondizione(ReatoSentenzaSigeModel aModel)
	{
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
	}

	public void setCondizioneDeleteReato(BigDecimal aIdReato)
 	{
		setCondition(" REA_ID_REATO = " + aIdReato ); 
	}
	
	public void setCondizioneIdFasSigeSen(BigDecimal aIdFasSigeSen)
 	{
		setCondition(" FAS_SIGE_SEN_ID = " + aIdFasSigeSen ); 
	}

}
