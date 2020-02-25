package siap.siep.autoritaesterna.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: AutoritaEsternaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella AutoritaEsterna</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class AutoritaEsternaDAO extends SIAPTableDAO
{
	public AutoritaEsternaDAO (Connection con)
	{
			 super(con);
			 setTable("AUTORITA_ESTERNA");

			 setSequenceField("ID_AUTORITA_ESTERNA","AUT_EST_SEQ");
       setFieldKey("ID_AUTORITA_ESTERNA", BIG_DECIMAL);


			 setField("ID_AUTORITA_ESTERNA", BIG_DECIMAL);
			 setField("COD_TIPO_AUTORITA", STRING);
			 setField("DESCRIZIONE", STRING);
			 setField("COD_SEDE", STRING);
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

			public BigDecimal 		 getIdAutoritaEsterna() 		throws DAOException	 { return getBigDecimal("ID_AUTORITA_ESTERNA"); }
			public String 				 getCodTipoAutorita() 		throws DAOException	 { return getString("COD_TIPO_AUTORITA"); }
			public String 				 getDescrizione() 		throws DAOException	 { return getString("DESCRIZIONE"); }
			public String 				 getCodSede() 		throws DAOException	 { return getString("COD_SEDE"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public String 				 getCodOperatoreAggiornamento() 		throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
			public Date 					 getDataAggiornamento() 		throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
			public String 				 getCodUfficioAggiornamento() 		throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }


  //
  // METODI SET()
  //

			public void  	 setIdAutoritaEsterna(BigDecimal aValore ) 			 { setBigDecimal("ID_AUTORITA_ESTERNA", aValore); }
			public void  	 setCodTipoAutorita(String aValore ) 			 { setString("COD_TIPO_AUTORITA", aValore); }
			public void  	 setDescrizione(String aValore ) 			 { setString("DESCRIZIONE", aValore); }
			public void  	 setCodSede(String aValore ) 			 { setString("COD_SEDE", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreAggiornamento(String aValore ) 			 { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
			public void  	 setDataAggiornamento(Date aValore ) 			 { setDate("DATA_AGGIORNAMENTO", aValore); }
			public void  	 setCodUfficioAggiornamento(String aValore ) 			 { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new AutoritaEsternaModel(
								 getIdAutoritaEsterna() ,
								 getCodTipoAutorita() ,
								 "",
								 getDescrizione() ,
								 getCodSede() ,
								 "",
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								 "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento()
								);
		}


	 public void 	 setDAOFromModel(AutoritaEsternaModel aModel) throws DAOException
  		{
				 setIdAutoritaEsterna( aModel.getIdAutoritaEsterna() );
				 setCodTipoAutorita( aModel.getCodTipoAutorita() );
				 setDescrizione( aModel.getDescrizione() );
				 setCodSede( aModel.getCodSede() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
		}


	 public void 	 setDAOFromModelForUpdate(AutoritaEsternaModel aModel) throws DAOException
  		{
				 setIdAutoritaEsterna( aModel.getIdAutoritaEsterna() );
				 setCodTipoAutorita( aModel.getCodTipoAutorita() );
				 setDescrizione( aModel.getDescrizione() );
				 setCodSede( aModel.getCodSede() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );

		     setCondizioneUpdate(aModel.getIdAutoritaEsterna());
		}


	public void setCondizione(AutoritaEsternaModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }

     public void setRicercaByAutSede(AutoritaEsternaModel aModel)
		 {
		   String lCondizioni = "";

       lCondizioni = " COD_TIPO_AUTORITA = '"+aModel.getCodTipoAutorita()+"' AND COD_SEDE = '" + aModel.getCodSede()+"'" ;

 		   setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_AUTORITA_ESTERNA = " + key );
		 }

}
