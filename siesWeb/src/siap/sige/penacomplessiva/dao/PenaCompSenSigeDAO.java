package siap.sige.penacomplessiva.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sige.penacomplessiva.model.PenaCompSigeModel;
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

public class PenaCompSenSigeDAO extends TableDAO 
{
	public PenaCompSenSigeDAO (Connection con) 
	{
			 super(con);
			 setTable("PENA_COMPLESSIVA_SENTENZA_SIGE");

			 //Settare la Sequence e i campi chiave

			setField("PNC_ID_PENA_COMPLESSIVA", BIG_DECIMAL);
			setField("FAS_SIGE_SEN_ID", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //
	public BigDecimal 		 getIdPenaComplessiva() throws DAOException	 { return getBigDecimal("PNC_ID_PENA_COMPLESSIVA"); } 
	public BigDecimal 		 getFasSigeSenId() 		throws DAOException	 { return getBigDecimal("FAS_SIGE_SEN_ID"); } 


  //
  // METODI SET()
  //
	public void  	 setIdPenaComplessiva(BigDecimal aValore ) 	{ setBigDecimal("PNC_ID_PENA_COMPLESSIVA", aValore); } 
	public void  	 setFasSigeSenId(BigDecimal aValore ) 		{ setBigDecimal("FAS_SIGE_SEN_ID", aValore); } 


	public GenericModel getModel() throws DAOException
  	{ 
		return new PenaCompSigeModel(  
				getIdPenaComplessiva() , 
					getFasSigeSenId()  
				);
	}

	public void setDAOFromModel(PenaCompSigeModel aModel) throws DAOException
  	{
		setIdPenaComplessiva( aModel.getIdPenaComplessiva());  
		setFasSigeSenId( aModel.getFasSigeSenId() );  
	}


	public void setCondizione(PenaCompSigeModel aModel)
	{
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
	}

	public void setCondizioneDeletePenaComplessiva(BigDecimal aIPenaComplessiva)
 	{
		setCondition(" PNC_ID_PENA_COMPLESSIVA = " + aIPenaComplessiva ); 
	}
	public void setCondizioneIdFasSigeSen(BigDecimal aIdFasSigeSen)
 	{
		setCondition(" FAS_SIGE_SEN_ID = " + aIdFasSigeSen ); 
	}

}
