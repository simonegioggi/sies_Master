package siap.siep.refertoscarcerazione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.refertoscarcerazione.model.RefertoScarcerazioneModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: RefertoScarcerazioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RefertoScarcerazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class RefertoScarcerazioneDAO extends TableDAO
{
	public RefertoScarcerazioneDAO (Connection con)
	{
			 super(con);
			 setTable("REFERTO_SCARCERAZIONE");


       //Settare la Sequence e i campi chiave
       this.setSequenceField("ID_REFERTO_SCARCERAZIONE","REF_SCA_SEQ");
       this.setFieldKey("ID_REFERTO_SCARCERAZIONE", BIG_DECIMAL);


			 setField("ID_REFERTO_SCARCERAZIONE", BIG_DECIMAL);
			 setField("ANNO_NOTA", BIG_DECIMAL);
			 setField("NUM_NOTA", STRING);
			 setField("DATA_NOTA", DATE);
			 setField("DATA_SCARCERAZIONE", DATE);
			 setField("NOTE", STRING);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
			 setField("DATA_AGGIORNAMENTO", DATE);
			 setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
			 setField("EVE_ID_EVENTO", BIG_DECIMAL);
			 setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdRefertoScarcerazione() 		throws DAOException	 { return getBigDecimal("ID_REFERTO_SCARCERAZIONE"); }
			public BigDecimal 		 getAnnoNota() 		throws DAOException	 { return getBigDecimal("ANNO_NOTA"); }
			public String 				 getNumNota() 		throws DAOException	 { return getString("NUM_NOTA"); }
			public Date 					 getDataNota() 		throws DAOException	 { return getDate("DATA_NOTA"); }
			public Date 					 getDataScarcerazione() 		throws DAOException	 { return getDate("DATA_SCARCERAZIONE"); }
			public String 				 getNote() 		throws DAOException	 { return getString("NOTE"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
			public BigDecimal 		 getEveIdEvento() 		throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }
			public String 				 getIstDetIdIstitutoDetenzione() 		throws DAOException	 { return getString("IST_DET_ID_ISTITUTO_DETENZIONE"); }


  //
  // METODI SET()
  //

			public void  	 setIdRefertoScarcerazione(BigDecimal aValore ) 			 { setBigDecimal("ID_REFERTO_SCARCERAZIONE", aValore); }
			public void  	 setAnnoNota(BigDecimal aValore ) 			 { setBigDecimal("ANNO_NOTA", aValore); }
			public void  	 setNumNota(String aValore ) 			 { setString("NUM_NOTA", aValore); }
			public void  	 setDataNota(Date aValore ) 			 { setDate("DATA_NOTA", aValore); }
			public void  	 setDataScarcerazione(Date aValore ) 			 { setDate("DATA_SCARCERAZIONE", aValore); }
			public void  	 setNote(String aValore ) 			 { setString("NOTE", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
			public void  	 setEveIdEvento(BigDecimal aValore ) 			 { setBigDecimal("EVE_ID_EVENTO", aValore); }
			public void  	 setIstDetIdIstitutoDetenzione(String aValore ) 			 { setString("IST_DET_ID_ISTITUTO_DETENZIONE", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new RefertoScarcerazioneModel(
								 getIdRefertoScarcerazione() ,
								 getAnnoNota() ,
								 getNumNota() ,
								 getDataNota() ,
								 getDataScarcerazione() ,
								 getNote() ,
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								 "",
								 getEveIdEvento() ,
								 getIstDetIdIstitutoDetenzione(),
                 null
								);
		}


	 public void 	 setDAOFromModel(RefertoScarcerazioneModel aModel) throws DAOException
  		{
				 setIdRefertoScarcerazione( aModel.getIdRefertoScarcerazione() );
				 setAnnoNota( aModel.getAnnoNota() );
				 setNumNota( aModel.getNumNota() );
				 setDataNota( aModel.getDataNota() );
				 setDataScarcerazione( aModel.getDataScarcerazione() );
				 setNote( aModel.getNote() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setEveIdEvento( aModel.getEveIdEvento() );
				 setIstDetIdIstitutoDetenzione( aModel.getIstDetIdIstitutoDetenzione() );
		}


	 public void 	 setDAOFromModelForUpdate(RefertoScarcerazioneModel aModel) throws DAOException
  		{
				 setIdRefertoScarcerazione( aModel.getIdRefertoScarcerazione() );
				 setAnnoNota( aModel.getAnnoNota() );
				 setNumNota( aModel.getNumNota() );
				 setDataNota( aModel.getDataNota() );
				 setDataScarcerazione( aModel.getDataScarcerazione() );
				 setNote( aModel.getNote() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
				 setEveIdEvento( aModel.getEveIdEvento() );
				 setIstDetIdIstitutoDetenzione( aModel.getIstDetIdIstitutoDetenzione() );
		 setCondizioneUpdate(aModel.getIdRefertoScarcerazione());
		}


	public void setCondizione(RefertoScarcerazioneModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_REFERTO_SCARCERAZIONE = " + key );
		 }

}
