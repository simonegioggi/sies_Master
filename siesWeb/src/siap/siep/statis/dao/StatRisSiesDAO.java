package siap.siep.statis.dao;

/**
* <p>Title: StatRisSiesDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella StatRisSies</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.statis.model.StatRisSiesModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

public class StatRisSiesDAO extends TableDAO 
{
	public StatRisSiesDAO (Connection con) 
	{
			 super(con);
			 setTable("ISP_STAT_RIS_SIES");

			 //Settare la Sequence e i campi chiave

			 setField("CHIAVE_ANNO", INTEGER);
			 setField("CHIAVE_PROGR", BIG_DECIMAL);
			 setField("COD_UFFICIO", STRING);
			 setField("DATA_ESTRAZIONE", DATE);
			 setField("COD_POSIZIONE_GIURIDICA", STRING);
			 setField("COD_STATO_PROCEDIMENTO", STRING);
			 setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
	}


  //
  // METODI GET()
  //

			public Integer 		 getChiaveAnno() 		throws DAOException	 { return getInteger("CHIAVE_ANNO"); } 
			public BigDecimal 		 getChiaveProgr() 		throws DAOException	 { return getBigDecimal("CHIAVE_PROGR"); } 
			public String 				 getCodUfficio() 		throws DAOException	 { return getString("COD_UFFICIO"); } 
			public Date 					 getDataEstrazione() 		throws DAOException	 { return getDate("DATA_ESTRAZIONE"); } 
			public String 				 getCodPosizioneGiuridica() 		throws DAOException	 { return getString("COD_POSIZIONE_GIURIDICA"); } 
			public String 				 getCodStatoProcedimento() 		throws DAOException	 { return getString("COD_STATO_PROCEDIMENTO"); } 
			public BigDecimal 		 getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); } 
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); } 
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); } 
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); } 


  //
  // METODI SET()
  //

			public void  	 setChiaveAnno(Integer aValore ) 			 { setInteger("CHIAVE_ANNO", aValore); } 
			public void  	 setChiaveProgr(BigDecimal aValore ) 			 { setBigDecimal("CHIAVE_PROGR", aValore); } 
			public void  	 setCodUfficio(String aValore ) 			 { setString("COD_UFFICIO", aValore); } 
			public void  	 setDataEstrazione(Date aValore ) 			 { setDate("DATA_ESTRAZIONE", aValore); } 
			public void  	 setCodPosizioneGiuridica(String aValore ) 			 { setString("COD_POSIZIONE_GIURIDICA", aValore); } 
			public void  	 setCodStatoProcedimento(String aValore ) 			 { setString("COD_STATO_PROCEDIMENTO", aValore); } 
			public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); } 
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); } 
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); } 
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); } 


	public GenericModel getModel() throws DAOException
  			 { 
 				 return new StatRisSiesModel(  
								 getChiaveAnno() , 
								 getChiaveProgr() , 
								 getCodUfficio() , 
								 "",
								 getDataEstrazione() , 
								 getCodPosizioneGiuridica() , 
								 "",
								 getCodStatoProcedimento() , 
								 "",
								 getFasSieIdFascicoloSiep() , 
								 getCodOperatoreInserimento() , 
								 getCodUfficioInserimento() , 
								 "",
								 getDataInserimento()  
								);
		}


	 public void 	 setDAOFromModel(StatRisSiesModel aModel) throws DAOException
  		{
				 setChiaveAnno( aModel.getChiaveAnno() );  
				 setChiaveProgr( aModel.getChiaveProgr() );  
				 setCodUfficio( aModel.getCodUfficio() );  
				 setDataEstrazione( aModel.getDataEstrazione() );  
				 setCodPosizioneGiuridica( aModel.getCodPosizioneGiuridica() );  
				 setCodStatoProcedimento( aModel.getCodStatoProcedimento() );  
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );  
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
				 setDataInserimento( aModel.getDataInserimento() );  
		}


	public void selCondizione(StatRisSiesModel aModel)
		 {
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
		 }


	public void selCondizioneUpdate(BigDecimal key)
 			 {}
}
