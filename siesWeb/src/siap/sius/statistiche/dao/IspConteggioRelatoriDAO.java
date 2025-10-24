package siap.sius.statistiche.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sius.statistiche.model.IspConteggioRelatoriModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: IspConteggioRelatoriDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella IspConteggioRelatori</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class IspConteggioRelatoriDAO extends TableDAO 
{
	public IspConteggioRelatoriDAO (Connection con) 
	{
		super(con);
		setTable("ISP_CONTEGGIO_RELATORI");

			 //Settare la Sequence e i campi chiave

		setField("COD_RELATORE", STRING);
		setField("FAS_SIU_CHIAVE_UFFICIO", STRING);
		setField("DESC_CONTENUTO_STATIS", STRING);
		setField("NUM_PENDENTI_INIZIO", BIG_DECIMAL);
		setField("NUM_SOPRAVVENUTI", BIG_DECIMAL);
		setField("NUM_DEF_ESITO1", BIG_DECIMAL);
		setField("NUM_DEF_ESITO2", BIG_DECIMAL);
		setField("NUM_DEF_ESITO3", BIG_DECIMAL);
		setField("NUM_DEF_ESITO4", BIG_DECIMAL);
		setField("NUM_DEF_ESITO5", BIG_DECIMAL);
		setField("NUM_DEF_ESITO6", BIG_DECIMAL);
		setField("NUM_APP_PROVV", BIG_DECIMAL); //MEV_2019-09
		setField("NUM_DEF_ISC_ERR", BIG_DECIMAL);
		setField("NUM_PENDENTI_FINE", BIG_DECIMAL);
		setField("NUM_CANCELLATI", BIG_DECIMAL);
		setField("NUM_UNIFICATI", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

	public String 			getCodRelatore() 		throws DAOException	 { return getString("COD_RELATORE"); } 
	public String 			getDescContenutoStatis() throws DAOException	 { return getString("DESC_CONTENUTO_STATIS"); } 
	public BigDecimal 		getNumPendentiInizio() 	throws DAOException	 { return getBigDecimal("NUM_PENDENTI_INIZIO"); } 
	public BigDecimal 		getNumSopravvenuti() 	throws DAOException	 { return getBigDecimal("NUM_SOPRAVVENUTI"); } 
	public BigDecimal 		getNumDefEsito1() 		throws DAOException	 { return getBigDecimal("NUM_DEF_ESITO1"); } 
	public BigDecimal 		getNumDefEsito2() 		throws DAOException	 { return getBigDecimal("NUM_DEF_ESITO2"); } 
	public BigDecimal 		getNumDefEsito3() 		throws DAOException	 { return getBigDecimal("NUM_DEF_ESITO3"); } 
	public BigDecimal 		getNumDefEsito4() 		throws DAOException	 { return getBigDecimal("NUM_DEF_ESITO4"); } 
	public BigDecimal 		getNumDefEsito5() 		throws DAOException	 { return getBigDecimal("NUM_DEF_ESITO5"); } 
	public BigDecimal 		getNumDefEsito6() 		throws DAOException	 { return getBigDecimal("NUM_DEF_ESITO6"); } 
	public BigDecimal 		getNumDefIscErr() 		throws DAOException	 { return getBigDecimal("NUM_DEF_ISC_ERR"); } 
	public BigDecimal 		getNumPendentiFine() 	throws DAOException	 { return getBigDecimal("NUM_PENDENTI_FINE"); } 
	public String 			getFasSiuChiaveUfficio() 		throws DAOException	 { return getString("FAS_SIU_CHIAVE_UFFICIO"); } 
	public BigDecimal 		getNumCancellati() 	throws DAOException	 { return getBigDecimal("NUM_CANCELLATI"); }
	public BigDecimal 		getNumUnificati() 	throws DAOException	 { return getBigDecimal("NUM_UNIFICATI"); } 
	// MEV_2019-09
	public BigDecimal 		getNumAppProvv() 		throws DAOException	 { return getBigDecimal("NUM_APP_PROVV"); } 

  //
  // METODI SET()
  //

	public void  	 setCodRelatore(String aValore ) 			 { setString("COD_RELATORE", aValore); } 
	public void  	 setNumPendentiInizio(BigDecimal aValore ) 			 { setBigDecimal("NUM_PENDENTI_INIZIO", aValore); } 
	public void  	 setNumSopravvenuti(BigDecimal aValore ) 			 { setBigDecimal("NUM_SOPRAVVENUTI", aValore); } 
	public void  	 setNumDefEsito1(BigDecimal aValore ) 			 { setBigDecimal("NUM_DEF_ESITO1", aValore); } 
	public void  	 setNumDefEsito2(BigDecimal aValore ) 			 { setBigDecimal("NUM_DEF_ESITO2", aValore); } 
	public void  	 setNumDefEsito3(BigDecimal aValore ) 			 { setBigDecimal("NUM_DEF_ESITO3", aValore); } 
	public void  	 setNumDefEsito4(BigDecimal aValore ) 			 { setBigDecimal("NUM_DEF_ESITO4", aValore); } 
	public void  	 setNumDefEsito5(BigDecimal aValore ) 			 { setBigDecimal("NUM_DEF_ESITO5", aValore); } 
	public void  	 setNumDefEsito6(BigDecimal aValore ) 			 { setBigDecimal("NUM_DEF_ESITO6", aValore); } 
	public void  	 setNumPendentiFine(BigDecimal aValore ) 		{ setBigDecimal("NUM_PENDENTI_FINE", aValore); } 
	public void  	 setDescContenutoStatis(String aValore ) 		{ setString("DESC_CONTENUTO_STATIS", aValore); } 
	public void  	 setNumDefIscErr(BigDecimal aValore ) 			 { setBigDecimal("NUM_DEF_ISC_ERR", aValore); }
	public void  	 setFasSiuChiaveUfficio(String aValore ) 			 { setString("FAS_SIU_CHIAVE_UFFICIO", aValore); }
	public void  	 setNumCancellati(BigDecimal aValore ) 			 { setBigDecimal("NUM_CANCELLATI", aValore); }
	public void  	 setNumUnificati(BigDecimal aValore ) 			 { setBigDecimal("NUM_UNIFICATI", aValore); }
	// MEV_2019-09
	public void  	 setNumAppProvv(BigDecimal aValore ) 			 { setBigDecimal("NUM_APP_PROVV", aValore); } 

	public GenericModel getModel() throws DAOException
 	{ 
		return new IspConteggioRelatoriModel(  
					getCodRelatore() , 
					getDescContenutoStatis(),
					getNumPendentiInizio() , 
					getNumSopravvenuti() , 
					getNumDefEsito1() , 
					getNumDefEsito2() , 
					getNumDefEsito3() , 
					getNumDefEsito4() , 
					getNumDefEsito5() , 
					getNumDefEsito6() , 
					getNumDefIscErr(),
					getNumPendentiFine(),
					getFasSiuChiaveUfficio(),
					getNumCancellati(),
					getNumUnificati(),
					getNumAppProvv()  // MEV_2019-09
				);
	}


	 public void 	 setDAOFromModel(IspConteggioRelatoriModel aModel) throws DAOException
  	{
		setCodRelatore( aModel.getCodRelatore() );  
		setNumPendentiInizio( aModel.getNumPendentiInizio() );  
		setNumSopravvenuti( aModel.getNumSopravvenuti() );  
		setNumDefEsito1( aModel.getNumDefEsito1() );  
		setNumDefEsito2( aModel.getNumDefEsito2() );  
		setNumDefEsito3( aModel.getNumDefEsito3() );  
		setNumDefEsito4( aModel.getNumDefEsito4() );  
		setNumDefEsito5( aModel.getNumDefEsito5() );  
		setNumDefEsito6( aModel.getNumDefEsito6() );  
		setNumDefIscErr(aModel.getNumDefIscErr());
		setNumPendentiFine( aModel.getNumPendentiFine() );  
		setDescContenutoStatis(aModel.getDescContenutoStatis());
		setFasSiuChiaveUfficio( aModel.getFasSiuChiaveUfficio() );
		setNumCancellati( aModel.getNumCancellati() );
		setNumUnificati( aModel.getNumUnificati() );
		
		// MEV_2019-09
		setNumAppProvv(aModel.getNumAppProvv() );
		
	}


	public void setCondizione(IspConteggioRelatoriModel aModel)
		 {
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
 		 }
	
	
	public void setCondizione(String lCondizioni)
	 {
	 	
	 	 if ( lCondizioni.compareTo("") != 0  ) setCondition(lCondizioni); 
	 }
	
	  public void setOrdinamentoPerContenutoStatistico()
	  {
		  setOrder("DESC_CONTENUTO_STATIS DESC");
	  }
	  
	  public void setOrdinamentoPerCodiceRelatore()
	  {
		  setOrder("COD_RELATORE");
	  }

}
