package siap.sige.beneficio.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sige.beneficio.model.BeneficioSigeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: BeneficioSenSigeDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella BENEFICIO_SENTENZA_SIGE atta
*  a memorizzare la relazione tra BENEFICIO e FAS_SIGE_SENTENZA.</p>
* <p>Copyright: Copyright (c) 2010</p>
* <p>Company: Agile</p>
* @version 1.0
*/

public class BeneficioSenSigeDAO extends TableDAO 
{
	public BeneficioSenSigeDAO (Connection con) 
	{
			 super(con);
			 setTable("BENEFICIO_SENTENZA_SIGE");

			 //Settare la Sequence e i campi chiave
			setField("BEN_ID_BENEFICIO", BIG_DECIMAL);
			setField("FAS_SIGE_SEN_ID", BIG_DECIMAL);
	}

  //
  // METODI GET()
  //
	public BigDecimal 		 getBeneficioId() throws DAOException	 { return getBigDecimal("BEN_ID_BENEFICIO"); } 
	public BigDecimal 		 getFasSigeSenId() 		throws DAOException	 { return getBigDecimal("FAS_SIGE_SEN_ID"); } 


  //
  // METODI SET()
  //
	public void  	 setBeneficioId(BigDecimal aValore ) 	{ setBigDecimal("BEN_ID_BENEFICIO", aValore); } 
	public void  	 setFasSigeSenId(BigDecimal aValore ) 		{ setBigDecimal("FAS_SIGE_SEN_ID", aValore); } 


	public GenericModel getModel() throws DAOException
  	{ 
		return new BeneficioSigeModel(  
				getBeneficioId() , 
				getFasSigeSenId()  
				);
	}

	public void setDAOFromModel(BeneficioSigeModel aModel) throws DAOException
  	{
		setBeneficioId( aModel.getIdBeneficio());  
		setFasSigeSenId( aModel.getFasSigeSenId() );  
	}


	public void setCondizione(BeneficioSigeModel aModel)
	{
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
	}

	public void setCondizioneDeleteBeneficio(BigDecimal aBeneficioId)
 	{
		setCondition(" BEN_ID_BENEFICIO = " + aBeneficioId ); 
	}
	
	public void setCondizioneIdFasSigeSen(BigDecimal aFasSigeSenId)
 	{
		setCondition(" FAS_SIGE_SEN_ID = " + aFasSigeSenId ); 
	}

}
