package siap.siep.reatopredisposto.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.reatopredisposto.model.ReatoPredispostoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import f3b.util.StringUtils;

/**
* <p>Title: ReatoPredispostoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella ReatoPredisposto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ReatoPredispostoDAO extends TableDAO 
{
	public ReatoPredispostoDAO (Connection con) 
	{
			 super(con);
			 setTable("reato_predisposto");

			    setSequenceField("ID_REATO_PREDISPOSTO", "REA_PRE_SEQ");

			    setFieldKey("ID_REATO_PREDISPOSTO", BIG_DECIMAL);

			    setField("ID_REATO_PREDISPOSTO", BIG_DECIMAL);
			    
			 setField("PROGR_NORMA", BIG_DECIMAL);
			 
			 setField("NOME_ELEMENTO", STRING);
			 setField("COD_FONTE", STRING);
			 setField("ANNO_FONTE", BIG_DECIMAL);
			 setField("NUMERO_FONTE", STRING);
			 setField("COD_SOTTONUMERAZIONE", STRING);
			 setField("COMMA", STRING);
			 setField("LETTERA", STRING);
			 setField("NUMERO", STRING);
			 setField("ARTICOLO", STRING);
			 setField("NOTE_ELEMENTO", STRING);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
	}


  //
  // METODI GET()
  //
			public BigDecimal getIdReatoPredisposto() 		throws DAOException	                  { return getBigDecimal("ID_REATO_PREDISPOSTO"); }
			public BigDecimal getProgrNorma() 		throws DAOException	                  { return getBigDecimal("PROGR_NORMA"); }
			public String 				 getNomeElemento() 		throws DAOException	 { return getString("NOME_ELEMENTO"); } 
			public String 				 getCodFonte() 		throws DAOException	 { return getString("COD_FONTE"); } 
			public BigDecimal 		 getAnnoFonte() 		throws DAOException	 { return getBigDecimal("ANNO_FONTE"); } 
			public String 				 getNumeroFonte() 		throws DAOException	 { return getString("NUMERO_FONTE"); } 
			public String 				 getCodSottonumerazione() 		throws DAOException	 { return getString("COD_SOTTONUMERAZIONE"); } 
			public String 				 getComma() 		throws DAOException	 { return getString("COMMA"); } 
			public String 				 getLettera() 		throws DAOException	 { return getString("LETTERA"); } 
			public String 				 getNumero() 		throws DAOException	 { return getString("NUMERO"); } 
			public String 				 getArticolo() 		throws DAOException	 { return getString("ARTICOLO"); } 
			public String 				 getNoteElemento() 		throws DAOException	 { return getString("NOTE_ELEMENTO"); } 
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); } 
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); } 
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); } 
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); } 
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); } 
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); } 


  //
  // METODI SET()
  //
				
			public void setIdReatoPredisposto(BigDecimal aValore ) 			              { setBigDecimal("ID_REATO_PREDISPOSTO", aValore); }
			public void setProgrNorma(BigDecimal aValore ) 			              { setBigDecimal("PROGR_NORMA", aValore); }
			public void  	 setNomeElemento(String aValore ) 			 { setString("NOME_ELEMENTO", aValore); } 
			public void  	 setCodFonte(String aValore ) 			 { setString("COD_FONTE", aValore); } 
			public void  	 setAnnoFonte(BigDecimal aValore ) 			 { setBigDecimal("ANNO_FONTE", aValore); } 
			public void  	 setNumeroFonte(String aValore ) 			 { setString("NUMERO_FONTE", aValore); } 
			public void  	 setCodSottonumerazione(String aValore ) 			 { setString("COD_SOTTONUMERAZIONE", aValore); } 
			public void  	 setComma(String aValore ) 			 { setString("COMMA", aValore); } 
			public void  	 setLettera(String aValore ) 			 { setString("LETTERA", aValore); } 
			public void  	 setNumero(String aValore ) 			 { setString("NUMERO", aValore); } 
			public void  	 setArticolo(String aValore ) 			 { setString("ARTICOLO", aValore); } 
			public void  	 setNoteElemento(String aValore ) 			 { setString("NOTE_ELEMENTO", aValore); } 
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); } 
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); } 
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); } 
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); } 
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); } 
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); } 


	public GenericModel getModel() throws DAOException
  			 { 
 				 return new ReatoPredispostoModel(  
 						 		 getIdReatoPredisposto(),
 						 		 getProgrNorma(),
								 getNomeElemento() , 
								 getCodFonte() , 
								 "",
								 getAnnoFonte() , 
								 getNumeroFonte() , 
								 getCodSottonumerazione() , 
								 "",
								 getComma() , 
								 getLettera() , 
								 getNumero() , 
								 getArticolo() , 
								 getNoteElemento() , 
								 getCodOperatoreInserimento() , 
								 getDataInserimento() , 
								 getCodUfficioInserimento() , 
								 "",
								 getCodOperatoreAggiornamento() , 
								 getDataAggiornamento() , 
								 getCodUfficioAggiornamento(),
								 ""
								);
		}


	 public void 	 setDAOFromModel(ReatoPredispostoModel aModel) throws DAOException
  		{
		 		 setIdReatoPredisposto( aModel.getIdReatoPredisposto() );
		 		 setProgrNorma( aModel.getProgrNorma() );
				 setNomeElemento( aModel.getNomeElemento() );  
				 setCodFonte( aModel.getCodFonte() );  
				 setAnnoFonte( aModel.getAnnoFonte() );  
				 setNumeroFonte( aModel.getNumeroFonte() );  
				 setCodSottonumerazione( aModel.getCodSottonumerazione() );  
				 setComma( aModel.getComma() );  
				 setLettera( aModel.getLettera() );  
				 setNumero( aModel.getNumero() );  
				 setArticolo( aModel.getArticolo() );  
				 setNoteElemento( aModel.getNoteElemento() );  
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );  
				 setDataInserimento( aModel.getDataInserimento() );  
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );  
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
				 setDataAggiornamento( aModel.getDataAggiornamento() );  
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
		}


	 public void 	 setDAOFromModelForUpdate(ReatoPredispostoModel aModel) throws DAOException
  		{			
		 		 //setIdReatoPredisposto( aModel.getIdReatoPredisposto() );
		 		 //setProgrNorma( aModel.getProgrNorma() );
				 //setNomeElemento( aModel.getNomeElemento() );  
				 setCodFonte( aModel.getCodFonte() );  
				 setAnnoFonte( aModel.getAnnoFonte() );  
				 setNumeroFonte( aModel.getNumeroFonte() );  
				 setCodSottonumerazione( aModel.getCodSottonumerazione() );  
				 setComma( aModel.getComma() );  
				 setLettera( aModel.getLettera() );  
				 setNumero( aModel.getNumero() );  
				 setArticolo( aModel.getArticolo() );  
				 setNoteElemento( aModel.getNoteElemento() );  
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );  
				 setDataAggiornamento( aModel.getDataAggiornamento() );  
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );  
		 setCondizioneUpdate(aModel.getIdReatoPredisposto());
		}


	public void setCondizione(ReatoPredispostoModel aModel)
		 {
		 String lCondizioni = new String(); 
 		
		 boolean lInserito = false; 
 		 if ( lInserito ) setCondition(lCondizioni); 
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_REATO_PREDISPOSTO = " + key ); 
		 }

	public void setCondizione_CancellazioneACatena(ReatoPredispostoModel aModel)
  {
		 String lCondizioni = new String(); 
 		
		 lCondizioni+=" NOME_ELEMENTO= '"+ StringUtils.convertSqlString( aModel.getNomeElemento()) + "'";
		 
		 lCondizioni+=" AND COD_UFFICIO_INSERIMENTO='"+ StringUtils.convertSqlString( aModel.getCodUfficioInserimento()) + "'";
		  		 		
		 setCondition (lCondizioni);
  }

}
