package siap.sius.statistiche.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sius.statistiche.model.IspConteggioTempiModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: IspConteggioTempiDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella IspConteggioTempi</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class IspConteggioTempiDAO extends TableDAO 
{
	public IspConteggioTempiDAO (Connection con) 
	{
		super(con);
		setTable("ISP_CONTEGGIO_TEMPI");

			 //Settare la Sequence e i campi chiave

		setField("DESC_CONTENUTO_STATIS", STRING);
		setField("COD_OGGETTO", STRING);
		setField("DESC_OGGETTO", STRING);
		setField("COD_INTERVALLO", STRING);
		setField("DESC_INTERVALLO", STRING);
		setField("NUM_TEMPO1", BIG_DECIMAL);
		setField("NUM_TEMPO2", BIG_DECIMAL);
		setField("NUM_TEMPO3", BIG_DECIMAL);
		setField("NUM_TEMPO4", BIG_DECIMAL);
		setField("NUM_TEMPO5", BIG_DECIMAL);
		setField("NUM_TEMPO6", BIG_DECIMAL);
		setField("NUM_TOTALE", BIG_DECIMAL);
		setField("DURATA_MEDIA", BIG_DECIMAL);
		setField("FAS_SIU_CHIAVE_UFFICIO", STRING);  /* mod. michele 5/12/2008*/
	}


  //
  // METODI GET()
  //

		public String 				 getDescContenutoStatis() 		throws DAOException	 { return getString("DESC_CONTENUTO_STATIS"); } 
		public String 				 getCodOggetto() 		throws DAOException	 { return getString("COD_OGGETTO"); } 
		public String 				 getDescOggetto() 		throws DAOException	 { return getString("DESC_OGGETTO"); } 
		public String 				 getCodIntervallo() 		throws DAOException	 { return getString("COD_INTERVALLO"); } 
		public String 				 getDescIntervallo() 		throws DAOException	 { return getString("DESC_INTERVALLO"); } 
		public BigDecimal 		 getNumTempo1() 		throws DAOException	 { return getBigDecimal("NUM_TEMPO1"); } 
		public BigDecimal 		 getNumTempo2() 		throws DAOException	 { return getBigDecimal("NUM_TEMPO2"); } 
		public BigDecimal 		 getNumTempo3() 		throws DAOException	 { return getBigDecimal("NUM_TEMPO3"); } 
		public BigDecimal 		 getNumTempo4() 		throws DAOException	 { return getBigDecimal("NUM_TEMPO4"); } 
		public BigDecimal 		 getNumTempo5() 		throws DAOException	 { return getBigDecimal("NUM_TEMPO5"); } 
		public BigDecimal 		 getNumTempo6() 		throws DAOException	 { return getBigDecimal("NUM_TEMPO6"); } 
		public BigDecimal 		 getNumTotale() 		throws DAOException	 { return getBigDecimal("NUM_TOTALE"); } 
		public BigDecimal 		 getDurataMedia() 		throws DAOException	 { return getBigDecimal("DURATA_MEDIA"); } 
		public String 				 getFasSiuChiaveUfficio() 		throws DAOException	 { return getString("FAS_SIU_CHIAVE_UFFICIO"); }  /* mod. michele 5/12/2008*/ 
		

  //
  // METODI SET()
  //

		public void  	 setDescContenutoStatis(String aValore ) 			 { setString("DESC_CONTENUTO_STATIS", aValore); } 
		public void  	 setCodOggetto(String aValore ) 			 { setString("COD_OGGETTO", aValore); } 
		public void  	 setDescOggetto(String aValore ) 			 { setString("DESC_OGGETTO", aValore); } 
		public void  	 setCodIntervallo(String aValore ) 			 { setString("COD_INTERVALLO", aValore); } 
		public void  	 setDescIntervallo(String aValore ) 			 { setString("DESC_INTERVALLO", aValore); } 
		public void  	 setNumTempo1(BigDecimal aValore ) 			 { setBigDecimal("NUM_TEMPO1", aValore); } 
		public void  	 setNumTempo2(BigDecimal aValore ) 			 { setBigDecimal("NUM_TEMPO2", aValore); } 
		public void  	 setNumTempo3(BigDecimal aValore ) 			 { setBigDecimal("NUM_TEMPO3", aValore); } 
		public void  	 setNumTempo4(BigDecimal aValore ) 			 { setBigDecimal("NUM_TEMPO4", aValore); } 
		public void  	 setNumTempo5(BigDecimal aValore ) 			 { setBigDecimal("NUM_TEMPO5", aValore); } 
		public void  	 setNumTempo6(BigDecimal aValore ) 			 { setBigDecimal("NUM_TEMPO6", aValore); } 
		public void  	 setNumTotale(BigDecimal aValore ) 			 { setBigDecimal("NUM_TOTALE", aValore); } 
		public void  	 setDurataMedia(BigDecimal aValore ) 			 { setBigDecimal("DURATA_MEDIA", aValore); } 
		public void  	 setFasSiuChiaveUfficio(String aValore ) 		 { setString("FAS_SIU_CHIAVE_UFFICIO", aValore); } /* mod. michele 5/12/2008*/
		

	public GenericModel getModel() throws DAOException
  	{ 
 		return new IspConteggioTempiModel(  
								 getDescContenutoStatis() , 
								 getCodOggetto() , 
								 getDescOggetto() , 
								 getCodIntervallo() , 
								 getDescIntervallo() , 
								 getNumTempo1() , 
								 getNumTempo2() , 
								 getNumTempo3() , 
								 getNumTempo4() , 
								 getNumTempo5() , 
								 getNumTempo6() , 
								 getNumTotale() , 
								 getDurataMedia() ,
								 getFasSiuChiaveUfficio() /* mod. michele 5/12/2008*/
								);
  	}


	public void 	 setDAOFromModel(IspConteggioTempiModel aModel) throws DAOException
  	{
		setDescContenutoStatis( aModel.getDescContenutoStatis() );  
		setCodOggetto( aModel.getCodOggetto() );  
		setDescOggetto( aModel.getDescOggetto() );  
		setCodIntervallo( aModel.getCodIntervallo() );  
		setDescIntervallo( aModel.getDescIntervallo() );  
		setNumTempo1( aModel.getNumTempo1() );  
		setNumTempo2( aModel.getNumTempo2() );  
		setNumTempo3( aModel.getNumTempo3() );  
		setNumTempo4( aModel.getNumTempo4() );  
		setNumTempo5( aModel.getNumTempo5() );  
		setNumTempo6( aModel.getNumTempo6() );  
		setNumTotale( aModel.getNumTotale() );  
		setDurataMedia( aModel.getDurataMedia() );  
		setFasSiuChiaveUfficio( aModel.getFasSiuChiaveUfficio() ); /* mod. michele 5/12/2008*/
	}


	public void setCondizione(IspConteggioTempiModel aModel)
	{
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
	}

	/* mod michele 5/12/2008 */
	public void setCondizione(String lCondizioni)
	 {
	 	
	 	 if ( lCondizioni.compareTo("") != 0  ) setCondition(lCondizioni); 
	 }
	
	public void setOrdinamentoPerContenutoStatistico()
	{
		  setOrder("DESC_CONTENUTO_STATIS DESC, COD_OGGETTO ASC, COD_INTERVALLO ASC");
	}

}
