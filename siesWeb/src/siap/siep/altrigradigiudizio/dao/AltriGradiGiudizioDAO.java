package siap.siep.altrigradigiudizio.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: AltriGradiGiudizioDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella AltriGradiGiudizio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class AltriGradiGiudizioDAO extends TableDAO 
{
	public AltriGradiGiudizioDAO (Connection con) 
	{
			 super(con);
			 setTable("ALTRI_GRADI_GIUDIZIO");

			 //Settare la Sequence e i campi chiave

			 setSequenceField("ID_ALTRIGRADIGIUDIZIO","AGDG_SEQ");
			 setFieldKey("ID_ALTRIGRADIGIUDIZIO", BIG_DECIMAL);
			 
			 setField("ID_ALTRIGRADIGIUDIZIO", BIG_DECIMAL);
			 setField("DATA_SENTENZA_I_GRADO", DATE);
			 setField("ANNO_SENTENZA_I_GRADO", BIG_DECIMAL);
			 setField("NUMERO_SENTENZA_I_GRADO", STRING);
			 setField("COD_AUT_EMITT_SENT_I_GRADO", STRING);
			 setField("COD_LUO_EMITT_SENT_I_GRADO", STRING);
			 setField("NUM_SEZ_EMITT_SENT_I_GRADO", STRING);
			 setField("COD_TIPO_SENTENZA_II_GRADO", STRING);
			 setField("DATA_SENTENZA_II_GRADO", DATE);
			 setField("ANNO_SENTENZA_II_GRADO", BIG_DECIMAL);
			 setField("NUMERO_SENTENZA_II_GRADO", STRING);
			 setField("COD_AUT_EMITT_SENT_II_GRADO", STRING);
			 setField("COD_LUO_EMITT_SENT_II_GRADO", STRING);
			 setField("NUM_SEZ_EMITT_SENT_II_GRADO", STRING);
			 setField("ANNO_REG_GEN_CASSAZ", BIG_DECIMAL);
			 setField("NUMERO_REG_GEN_CASSAZ", STRING);
			 setField("ANNO_SENTENZA_CASSAZ", BIG_DECIMAL);
			 setField("NUMERO_SENTENZA_CASSAZ", STRING);
			 setField("ANNO_RACC_GENEALE_II_GRADO", BIG_DECIMAL);
			 setField("NUMERO_RACC_GENEALE_II_GRADO", STRING);
			 setField("COD_TIPO_DECISIONE_CASSAZIONE", STRING);
			 setField("SEN_ID_SENTENZA", BIG_DECIMAL);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 setField("COD_TIPO_RITO", STRING);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdAltrigradigiudizio() 		throws DAOException	 { return getBigDecimal("ID_ALTRIGRADIGIUDIZIO"); } 
			public Date 					 getDataSentenzaIGrado() 		throws DAOException	 { return getDate("DATA_SENTENZA_I_GRADO"); } 
			public BigDecimal 		 getAnnoSentenzaIGrado() 		throws DAOException	 { return getBigDecimal("ANNO_SENTENZA_I_GRADO"); } 
			public String 				 getNumeroSentenzaIGrado() 		throws DAOException	 { return getString("NUMERO_SENTENZA_I_GRADO"); } 
			public String 				 getCodAutEmittSentIGrado() 		throws DAOException	 { return getString("COD_AUT_EMITT_SENT_I_GRADO"); } 
			public String 				 getCodLuoEmittSentIGrado() 		throws DAOException	 { return getString("COD_LUO_EMITT_SENT_I_GRADO"); } 
			public String 				 getNumSezEmittSentIGrado() 		throws DAOException	 { return getString("NUM_SEZ_EMITT_SENT_I_GRADO"); } 
			public String 				 getCodTipoSentenzaIiGrado() 		throws DAOException	 { return getString("COD_TIPO_SENTENZA_II_GRADO"); } 
			public Date 					 getDataSentenzaIiGrado() 		throws DAOException	 { return getDate("DATA_SENTENZA_II_GRADO"); } 
			public BigDecimal 		 getAnnoSentenzaIiGrado() 		throws DAOException	 { return getBigDecimal("ANNO_SENTENZA_II_GRADO"); } 
			public String 				 getNumeroSentenzaIiGrado() 		throws DAOException	 { return getString("NUMERO_SENTENZA_II_GRADO"); } 
			public String 				 getCodAutEmittSentIiGrado() 		throws DAOException	 { return getString("COD_AUT_EMITT_SENT_II_GRADO"); } 
			public String 				 getCodLuoEmittSentIiGrado() 		throws DAOException	 { return getString("COD_LUO_EMITT_SENT_II_GRADO"); } 
			public String 				 getNumSezEmittSentIiGrado() 		throws DAOException	 { return getString("NUM_SEZ_EMITT_SENT_II_GRADO"); } 
			public BigDecimal 		 getAnnoRegGenCassaz() 		throws DAOException	 { return getBigDecimal("ANNO_REG_GEN_CASSAZ"); } 
			public String 				 getNumeroRegGenCassaz() 		throws DAOException	 { return getString("NUMERO_REG_GEN_CASSAZ"); } 
			public BigDecimal 		 getAnnoSentenzaCassaz() 		throws DAOException	 { return getBigDecimal("ANNO_SENTENZA_CASSAZ"); } 
			public String 				 getNumeroSentenzaCassaz() 		throws DAOException	 { return getString("NUMERO_SENTENZA_CASSAZ"); } 
			public BigDecimal 		 getAnnoRaccGenealeIiGrado() 		throws DAOException	 { return getBigDecimal("ANNO_RACC_GENEALE_II_GRADO"); } 
			public String 				 getNumeroRaccGenealeIiGrado() 		throws DAOException	 { return getString("NUMERO_RACC_GENEALE_II_GRADO"); } 
			public String 				 getCodTipoDecisioneCassazione() 		throws DAOException	 { return getString("COD_TIPO_DECISIONE_CASSAZIONE"); } 
			public BigDecimal 		 getSenIdSentenza() 		throws DAOException	 { return getBigDecimal("SEN_ID_SENTENZA"); } 
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); } 
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); } 
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); } 
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); } 
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); } 
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); } 
			public String 				 getCodTipoRito() 		throws DAOException	 { return getString("COD_TIPO_RITO"); }


  //
  // METODI SET()
  //

			public void  	 setIdAltrigradigiudizio(BigDecimal aValore ) 			 { setBigDecimal("ID_ALTRIGRADIGIUDIZIO", aValore); } 
			public void  	 setDataSentenzaIGrado(Date aValore ) 			 { setDate("DATA_SENTENZA_I_GRADO", aValore); } 
			public void  	 setAnnoSentenzaIGrado(BigDecimal aValore ) 			 { setBigDecimal("ANNO_SENTENZA_I_GRADO", aValore); } 
			public void  	 setNumeroSentenzaIGrado(String aValore ) 			 { setString("NUMERO_SENTENZA_I_GRADO", aValore); } 
			public void  	 setCodAutEmittSentIGrado(String aValore ) 			 { setString("COD_AUT_EMITT_SENT_I_GRADO", aValore); } 
			public void  	 setCodLuoEmittSentIGrado(String aValore ) 			 { setString("COD_LUO_EMITT_SENT_I_GRADO", aValore); } 
			public void  	 setNumSezEmittSentIGrado(String aValore ) 			 { setString("NUM_SEZ_EMITT_SENT_I_GRADO", aValore); } 
			public void  	 setCodTipoSentenzaIiGrado(String aValore ) 			 { setString("COD_TIPO_SENTENZA_II_GRADO", aValore); } 
			public void  	 setDataSentenzaIiGrado(Date aValore ) 			 { setDate("DATA_SENTENZA_II_GRADO", aValore); } 
			public void  	 setAnnoSentenzaIiGrado(BigDecimal aValore ) 			 { setBigDecimal("ANNO_SENTENZA_II_GRADO", aValore); } 
			public void  	 setNumeroSentenzaIiGrado(String aValore ) 			 { setString("NUMERO_SENTENZA_II_GRADO", aValore); } 
			public void  	 setCodAutEmittSentIiGrado(String aValore ) 			 { setString("COD_AUT_EMITT_SENT_II_GRADO", aValore); } 
			public void  	 setCodLuoEmittSentIiGrado(String aValore ) 			 { setString("COD_LUO_EMITT_SENT_II_GRADO", aValore); } 
			public void  	 setNumSezEmittSentIiGrado(String aValore ) 			 { setString("NUM_SEZ_EMITT_SENT_II_GRADO", aValore); } 
			public void  	 setAnnoRegGenCassaz(BigDecimal aValore ) 			 { setBigDecimal("ANNO_REG_GEN_CASSAZ", aValore); } 
			public void  	 setNumeroRegGenCassaz(String aValore ) 			 { setString("NUMERO_REG_GEN_CASSAZ", aValore); } 
			public void  	 setAnnoSentenzaCassaz(BigDecimal aValore ) 			 { setBigDecimal("ANNO_SENTENZA_CASSAZ", aValore); } 
			public void  	 setNumeroSentenzaCassaz(String aValore ) 			 { setString("NUMERO_SENTENZA_CASSAZ", aValore); } 
			public void  	 setAnnoRaccGenealeIiGrado(BigDecimal aValore ) 			 { setBigDecimal("ANNO_RACC_GENEALE_II_GRADO", aValore); } 
			public void  	 setNumeroRaccGenealeIiGrado(String aValore ) 			 { setString("NUMERO_RACC_GENEALE_II_GRADO", aValore); } 
			public void  	 setCodTipoDecisioneCassazione(String aValore ) 			 { setString("COD_TIPO_DECISIONE_CASSAZIONE", aValore); } 
			public void  	 setSenIdSentenza(BigDecimal aValore ) 			 { setBigDecimal("SEN_ID_SENTENZA", aValore); } 
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); } 
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); } 
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); } 
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); } 
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); } 
			public void  	 setCodTipoRito(String aValore ) 			 { setString("COD_TIPO_RITO", aValore); }


	public GenericModel getModel() throws DAOException
  			 { 
 				 return new AltriGradiGiudizioModel(  
								 getIdAltrigradigiudizio() , 
								 getDataSentenzaIGrado() , 
								 getAnnoSentenzaIGrado() , 
								 getNumeroSentenzaIGrado() , 
								 getCodAutEmittSentIGrado() , 
								 "",
								 getCodLuoEmittSentIGrado() , 
								 "",
								 getNumSezEmittSentIGrado() , 
								 getCodTipoSentenzaIiGrado() , 
								 "",
								 getDataSentenzaIiGrado() , 
								 getAnnoSentenzaIiGrado() , 
								 getNumeroSentenzaIiGrado() , 
								 getCodAutEmittSentIiGrado() , 
								 "",
								 getCodLuoEmittSentIiGrado() , 
								 "",
								 getNumSezEmittSentIiGrado() , 
								 getAnnoRegGenCassaz() , 
								 getNumeroRegGenCassaz() , 
								 getAnnoSentenzaCassaz() , 
								 getNumeroSentenzaCassaz() , 
								 getAnnoRaccGenealeIiGrado() , 
								 getNumeroRaccGenealeIiGrado() , 
								 getCodTipoDecisioneCassazione() , 
								 "",
								 getSenIdSentenza() , 
								 getCodOperatoreInserimento() , 
								 getDataInserimento() , 
								 getCodUfficioInserimento() , 
								 "",
								 getCodOperatoreAggiornamento() , 
								 getDataAggiornamento() , 
								 getCodUfficioAggiornamento(),
								 "",
								 getCodTipoRito(),
								 ""
								);
		}


	 public void 	 setDAOFromModel(AltriGradiGiudizioModel aModel) throws DAOException
  		{
				 setIdAltrigradigiudizio( aModel.getIdAltrigradigiudizio() );  
				 setDataSentenzaIGrado( aModel.getDataSentenzaIGrado() );  
				 setAnnoSentenzaIGrado( aModel.getAnnoSentenzaIGrado() );  
				 setNumeroSentenzaIGrado( aModel.getNumeroSentenzaIGrado() );  
				 setCodAutEmittSentIGrado( aModel.getCodAutEmittSentIGrado() );  
				 setCodLuoEmittSentIGrado( aModel.getCodLuoEmittSentIGrado() );  
				 setNumSezEmittSentIGrado( aModel.getNumSezEmittSentIGrado() );  
				 setCodTipoSentenzaIiGrado( aModel.getCodTipoSentenzaIiGrado() );  
				 setDataSentenzaIiGrado( aModel.getDataSentenzaIiGrado() );  
				 setAnnoSentenzaIiGrado( aModel.getAnnoSentenzaIiGrado() );  
				 setNumeroSentenzaIiGrado( aModel.getNumeroSentenzaIiGrado() );  
				 setCodAutEmittSentIiGrado( aModel.getCodAutEmittSentIiGrado() );  
				 setCodLuoEmittSentIiGrado( aModel.getCodLuoEmittSentIiGrado() );  
				 setNumSezEmittSentIiGrado( aModel.getNumSezEmittSentIiGrado() );  
				 setAnnoRegGenCassaz( aModel.getAnnoRegGenCassaz() );  
				 setNumeroRegGenCassaz( aModel.getNumeroRegGenCassaz() );  
				 setAnnoSentenzaCassaz( aModel.getAnnoSentenzaCassaz() );  
				 setNumeroSentenzaCassaz( aModel.getNumeroSentenzaCassaz() );  
				 setAnnoRaccGenealeIiGrado( aModel.getAnnoRaccGenealeIiGrado() );  
				 setNumeroRaccGenealeIiGrado( aModel.getNumeroRaccGenealeIiGrado() );  
				 setCodTipoDecisioneCassazione( aModel.getCodTipoDecisioneCassazione() );  
				 setSenIdSentenza( aModel.getSenIdSentenza() );  
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
				 setDataInserimento( aModel.getDataInserimento() );  
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
				 setDataAggiornamento( aModel.getDataAggiornamento() );  
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setCodTipoRito( aModel.getCodTipoRito());
		}


	 public void 	 setDAOFromModelForUpdate(AltriGradiGiudizioModel aModel) throws DAOException
  		{
				 setIdAltrigradigiudizio( aModel.getIdAltrigradigiudizio() );  
				 setDataSentenzaIGrado( aModel.getDataSentenzaIGrado() );  
				 setAnnoSentenzaIGrado( aModel.getAnnoSentenzaIGrado() );  
				 setNumeroSentenzaIGrado( aModel.getNumeroSentenzaIGrado() );  
				 setCodAutEmittSentIGrado( aModel.getCodAutEmittSentIGrado() );  
				 setCodLuoEmittSentIGrado( aModel.getCodLuoEmittSentIGrado() );  
				 setNumSezEmittSentIGrado( aModel.getNumSezEmittSentIGrado() );  
				 setCodTipoSentenzaIiGrado( aModel.getCodTipoSentenzaIiGrado() );  
				 setDataSentenzaIiGrado( aModel.getDataSentenzaIiGrado() );  
				 setAnnoSentenzaIiGrado( aModel.getAnnoSentenzaIiGrado() );  
				 setNumeroSentenzaIiGrado( aModel.getNumeroSentenzaIiGrado() );  
				 setCodAutEmittSentIiGrado( aModel.getCodAutEmittSentIiGrado() );  
				 setCodLuoEmittSentIiGrado( aModel.getCodLuoEmittSentIiGrado() );  
				 setNumSezEmittSentIiGrado( aModel.getNumSezEmittSentIiGrado() );  
				 setAnnoRegGenCassaz( aModel.getAnnoRegGenCassaz() );  
				 setNumeroRegGenCassaz( aModel.getNumeroRegGenCassaz() );  
				 setAnnoSentenzaCassaz( aModel.getAnnoSentenzaCassaz() );  
				 setNumeroSentenzaCassaz( aModel.getNumeroSentenzaCassaz() );  
				 setAnnoRaccGenealeIiGrado( aModel.getAnnoRaccGenealeIiGrado() );  
				 setNumeroRaccGenealeIiGrado( aModel.getNumeroRaccGenealeIiGrado() );  
				 setCodTipoDecisioneCassazione( aModel.getCodTipoDecisioneCassazione() );  
				 setSenIdSentenza( aModel.getSenIdSentenza() );  
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
				 setDataAggiornamento( aModel.getDataAggiornamento() );  
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setCodTipoRito( aModel.getCodTipoRito());
		 setCondizioneUpdate(aModel.getIdAltrigradigiudizio());
		}


	public void setCondizione(AltriGradiGiudizioModel aModel)
		 {
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_ALTRIGRADIGIUDIZIO = " + key ); 
		 }

}
