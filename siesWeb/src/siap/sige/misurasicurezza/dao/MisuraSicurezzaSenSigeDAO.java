package siap.sige.misurasicurezza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sige.misurasicurezza.model.MisuraSicurezzaSigeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: MisuraSicurezzaSenSigeDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella MISURA_SICUREZZA_SENTENZA_SIGE atta
*  a memorizzare la relazione tra MISURA_SICUREZZA e FAS_SIGE_SENTENZA.</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile</p>
* @version 1.0
*/

public class MisuraSicurezzaSenSigeDAO extends TableDAO 
{
	public MisuraSicurezzaSenSigeDAO (Connection con) 
	{
			 super(con);
			 setTable("MISURA_SICUREZZA_SENTENZA_SIGE");

			 //Settare la Sequence e i campi chiave
			setField("MIS_ID_MISURA_SICUREZZA", BIG_DECIMAL);
			setField("FAS_SIGE_SEN_ID", BIG_DECIMAL);
	}

  //
  // METODI GET()
  //
	public BigDecimal 		 getIdMisuraSicurezza() throws DAOException	 { return getBigDecimal("MIS_ID_MISURA_SICUREZZA"); } 
	public BigDecimal 		 getFasSigeSenId() 		throws DAOException	 { return getBigDecimal("FAS_SIGE_SEN_ID"); } 


  //
  // METODI SET()
  //
	public void  	 setIdMisuraSicurezza(BigDecimal aValore ) 	{ setBigDecimal("MIS_ID_MISURA_SICUREZZA", aValore); } 
	public void  	 setFasSigeSenId(BigDecimal aValore ) 		{ setBigDecimal("FAS_SIGE_SEN_ID", aValore); } 


	public GenericModel getModel() throws DAOException
  	{ 
		return new MisuraSicurezzaSigeModel(  
				getIdMisuraSicurezza() , 
					getFasSigeSenId()  
				);
	}

	public void setDAOFromModel(MisuraSicurezzaSigeModel aModel) throws DAOException
  	{
		setIdMisuraSicurezza( aModel.getIdMisuraSicurezza());  
		setFasSigeSenId( aModel.getFasSigeSenId() );  
	}


	public void setCondizione(MisuraSicurezzaSigeModel aModel)
	{
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
	}

	public void setCondizioneDeleteMisuraSicurezza(BigDecimal aIMisuraSicurezza)
 	{
		setCondition(" MIS_ID_MISURA_SICUREZZA = " + aIMisuraSicurezza ); 
	}
	
	public void setCondizioneIdFasSigeSen(BigDecimal aIdFasSigeSen)
 	{
		setCondition(" FAS_SIGE_SEN_ID = " + aIdFasSigeSen ); 
	}

}
