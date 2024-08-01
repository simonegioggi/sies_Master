package siap.siep.agdgfascicolosiep.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.agdgfascicolosiep.model.AgdgFascicoloSiepModel;
import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: AgdgFascicoloSiepDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella AgdgFascicoloSiep</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class AgdgFascicoloSiepDAO extends TableDAO 
{
	public AgdgFascicoloSiepDAO (Connection con) 
	{
			 super(con);
			 setTable("AGDG_FASCICOLO_SIEP");

			 //Settare la Sequence e i campi chiave

			 setSequenceField("ID_AGDG_FASCICOLO_SIEP","AGDG_FAS_SIE_SEQ");
			 setFieldKey("ID_AGDG_FASCICOLO_SIEP", BIG_DECIMAL);
			 
			 
			 setField("ID_AGDG_FASCICOLO_SIEP", BIG_DECIMAL);
			 setField("AGDG_ID_ALTRIGRADIGIUDIZIO", BIG_DECIMAL);
			 setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdAgdgFascicoloSiep() 		throws DAOException	 { return getBigDecimal("ID_AGDG_FASCICOLO_SIEP"); } 
			public BigDecimal 		 getAgdgIdAltrigradigiudizio() 		throws DAOException	 { return getBigDecimal("AGDG_ID_ALTRIGRADIGIUDIZIO"); } 
			public BigDecimal 		 getFasSieIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); } 
			public AltriGradiGiudizioModel getAltriGradiGiudizioModel()		throws DAOException	  { return getAltriGradiGiudizioModel(); }

  //
  // METODI SET()
  //

			public void  	 setIdAgdgFascicoloSiep(BigDecimal aValore ) 			 { setBigDecimal("ID_AGDG_FASCICOLO_SIEP", aValore); } 
			public void  	 setAgdgIdAltrigradigiudizio(BigDecimal aValore ) 			 { setBigDecimal("AGDG_ID_ALTRIGRADIGIUDIZIO", aValore); } 
			public void  	 setFasSieIdFascicoloSiep(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); } 
			public void  	 setAltriGradiGiudizioModel(AltriGradiGiudizioModel aValore ) 			 { setAltriGradiGiudizioModel (aValore); }

	public GenericModel getModel() throws DAOException
  			 { 
 				 return new AgdgFascicoloSiepModel(  
								 getIdAgdgFascicoloSiep() , 
								 getAgdgIdAltrigradigiudizio() , 
								 getFasSieIdFascicoloSiep(),
								 getAltriGradiGiudizioModel()
								);
		}


	 public void 	 setDAOFromModel(AgdgFascicoloSiepModel aModel) throws DAOException
  		{
				 setIdAgdgFascicoloSiep( aModel.getIdAgdgFascicoloSiep() );  
				 setAgdgIdAltrigradigiudizio( aModel.getAgdgIdAltrigradigiudizio() );  
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() ); 
		}


	 public void 	 setDAOFromModelForUpdate(AgdgFascicoloSiepModel aModel) throws DAOException
  		{
				 setIdAgdgFascicoloSiep( aModel.getIdAgdgFascicoloSiep() );  
				 setAgdgIdAltrigradigiudizio( aModel.getAgdgIdAltrigradigiudizio() );  
				 setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );  
		 setCondizioneUpdate(aModel.getIdAgdgFascicoloSiep());
		}


	public void setCondizione(AgdgFascicoloSiepModel aModel)
		 {
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_AGDG_FASCICOLO_SIEP = " + key ); 
		 }
	
	 // MEV_2024-DNA
	 public void setCondizioneByIdFascicolo (BigDecimal aIdFascicoloSiep)
   {
	     setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascicoloSiep ); 
   }
	

}
