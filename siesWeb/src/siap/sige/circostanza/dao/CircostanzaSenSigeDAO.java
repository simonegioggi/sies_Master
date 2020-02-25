package siap.sige.circostanza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sige.circostanza.model.CircostanzaSigeModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: CircostanzaSenSigeDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella CIRCOSTANZA_SENTENZA_SIGE atta
*  a memorizzare la relazione tra CIRCOSTANZA e FAS_SIGE_SENTENZA.</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class CircostanzaSenSigeDAO extends TableDAO 
{
	public CircostanzaSenSigeDAO (Connection con) 
	{
			 super(con);
			 setTable("CIRCOSTANZA_SENTENZA_SIGE");

			 //Settare la Sequence e i campi chiave
			setField("CIR_ID_CIRCOSTANZA", BIG_DECIMAL);
			setField("FAS_SIGE_SEN_ID", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //
	public BigDecimal 		 getIdCircostanza() throws DAOException	 { return getBigDecimal("CIR_ID_CIRCOSTANZA"); } 
	public BigDecimal 		 getFasSigeSenId() 		throws DAOException	 { return getBigDecimal("FAS_SIGE_SEN_ID"); } 


  //
  // METODI SET()
  //
	public void  	 setIdCircostanza(BigDecimal aValore ) 	{ setBigDecimal("CIR_ID_CIRCOSTANZA", aValore); } 
	public void  	 setFasSigeSenId(BigDecimal aValore ) 		{ setBigDecimal("FAS_SIGE_SEN_ID", aValore); } 


	public GenericModel getModel() throws DAOException
  	{ 
		return new CircostanzaSigeModel(  
				getIdCircostanza() , 
					getFasSigeSenId()  
				);
	}

	public void setDAOFromModel(CircostanzaSigeModel aModel) throws DAOException
  	{
		setIdCircostanza( aModel.getIdCircostanza());  
		setFasSigeSenId( aModel.getFasSigeSenId() );  
	}


	public void setCondizione(CircostanzaSigeModel aModel)
	{
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
	}

	public void setCondizioneDeleteCircostanza(BigDecimal aICircostanza)
 	{
		setCondition(" CIR_ID_CIRCOSTANZA = " + aICircostanza ); 
	}
	
	public void setCondizioneIdFasSigeSen(BigDecimal aIdFasSigeSen)
 	{
		setCondition(" FAS_SIGE_SEN_ID = " + aIdFasSigeSen ); 
	}

}
