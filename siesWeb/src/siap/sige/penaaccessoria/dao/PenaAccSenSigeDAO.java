package siap.sige.penaaccessoria.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sige.penaaccessoria.model.PenaAccSigeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: PenaAccSenSigeDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella PENA_ACCESSORIA_SENTENZA_SIGE</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class PenaAccSenSigeDAO extends TableDAO 
{
	public PenaAccSenSigeDAO (Connection con) 
	{
			 super(con);
			 setTable("PENA_ACCESSORIA_SENTENZA_SIGE");

			 //Settare la Sequence e i campi chiave

			setField("PNA_ID_PENA_ACCESSORIA", BIG_DECIMAL);
			setField("FAS_SIGE_SEN_ID", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //
	public BigDecimal 		 getIdPenaAccessoria() throws DAOException	 { return getBigDecimal("PNA_ID_PENA_ACCESSORIA"); } 
	public BigDecimal 		 getFasSigeSenId() 		throws DAOException	 { return getBigDecimal("FAS_SIGE_SEN_ID"); } 


  //
  // METODI SET()
  //
	public void  	 setIdPenaAccessoria(BigDecimal aValore ) 	{ setBigDecimal("PNA_ID_PENA_ACCESSORIA", aValore); } 
	public void  	 setFasSigeSenId(BigDecimal aValore ) 		{ setBigDecimal("FAS_SIGE_SEN_ID", aValore); } 


	public GenericModel getModel() throws DAOException
  	{ 
		return new PenaAccSigeModel(  
				getIdPenaAccessoria() , 
					getFasSigeSenId()  
				);
	}

	public void setDAOFromModel(PenaAccSigeModel aModel) throws DAOException
  	{
		setIdPenaAccessoria( aModel.getIdPenaAccessoria());  
		setFasSigeSenId( aModel.getFasSigeSenId() );  
	}


	public void setCondizione(PenaAccSigeModel aModel)
	{
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
	}

	public void setCondizioneDeletePenaAccessoria(BigDecimal aIPenaAccessoria)
 	{
		setCondition(" PNA_ID_PENA_ACCESSORIA = " + aIPenaAccessoria ); 
	}
	
	public void setCondizioneIdFasSigeSen(BigDecimal aIdFasSigeSen)
 	{
		setCondition(" FAS_SIGE_SEN_ID = " + aIdFasSigeSen ); 
	}

}
