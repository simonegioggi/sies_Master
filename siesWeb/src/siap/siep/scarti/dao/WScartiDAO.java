package siap.siep.scarti.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.scarti.model.WScartiModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: WScartiDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella WScarti</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class WScartiDAO extends TableDAO
{
	public WScartiDAO (Connection con)
	{
			 super(con);
			 setTable("W_SCARTI");

			 //Settare la Sequence e i campi chiave

			 setField("ID_SCARTI", BIG_DECIMAL);
			 setField("TABELLA", STRING);
			 setField("ANN_RES", BIG_DECIMAL);
			 setField("NUM_RES", STRING);
			 setField("LET_RES", STRING);
			 setField("CHIAVE_ALTERNATIVA", STRING);
			 setField("NOTE_SCARTO", STRING);
			 setField("CAUSA_SCARTO", STRING);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdScarti() 		throws DAOException	 { return getBigDecimal("ID_SCARTI"); }
			public String 				 getTabella() 		throws DAOException	 { return getString("TABELLA"); }
			public BigDecimal 		 getAnnRes() 		throws DAOException	 { return getBigDecimal("ANN_RES"); }
			public String 				 getNumRes() 		throws DAOException	 { return getString("NUM_RES"); }
			public String 				 getLetRes() 		throws DAOException	 { return getString("LET_RES"); }
			public String 				 getChiaveAlternativa() 		throws DAOException	 { return getString("CHIAVE_ALTERNATIVA"); }
			public String 				 getNoteScarto() 		throws DAOException	 { return getString("NOTE_SCARTO"); }
			public String 				 getCausaScarto() 		throws DAOException	 { return getString("CAUSA_SCARTO"); }


  //
  // METODI SET()
  //

			public void  	 setIdScarti(BigDecimal aValore ) 			 { setBigDecimal("ID_SCARTI", aValore); }
			public void  	 setTabella(String aValore ) 			 { setString("TABELLA", aValore); }
			public void  	 setAnnRes(BigDecimal aValore ) 			 { setBigDecimal("ANN_RES", aValore); }
			public void  	 setNumRes(String aValore ) 			 { setString("NUM_RES", aValore); }
			public void  	 setLetRes(String aValore ) 			 { setString("LET_RES", aValore); }
			public void  	 setChiaveAlternativa(String aValore ) 			 { setString("CHIAVE_ALTERNATIVA", aValore); }
			public void  	 setNoteScarto(String aValore ) 			 { setString("NOTE_SCARTO", aValore); }
			public void  	 setCausaScarto(String aValore ) 			 { setString("CAUSA_SCARTO", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new WScartiModel(
								 getIdScarti() ,
								 getTabella() ,
								 getAnnRes() ,
								 getNumRes() ,
								 getLetRes() ,
								 getChiaveAlternativa() ,
								 getNoteScarto() ,
								 getCausaScarto()
								);
		}


	 public void 	 setDAOFromModel(WScartiModel aModel) throws DAOException
  		{
				 setIdScarti( aModel.getIdScarti() );
				 setTabella( aModel.getTabella() );
				 setAnnRes( aModel.getAnnRes() );
				 setNumRes( aModel.getNumRes() );
				 setLetRes( aModel.getLetRes() );
				 setChiaveAlternativa( aModel.getChiaveAlternativa() );
				 setNoteScarto( aModel.getNoteScarto() );
				 setCausaScarto( aModel.getCausaScarto() );
		}


	 public void 	 setDAOFromModelForUpdate(WScartiModel aModel) throws DAOException
  		{
				 setIdScarti( aModel.getIdScarti() );
				 setTabella( aModel.getTabella() );
				 setAnnRes( aModel.getAnnRes() );
				 setNumRes( aModel.getNumRes() );
				 setLetRes( aModel.getLetRes() );
				 setChiaveAlternativa( aModel.getChiaveAlternativa() );
				 setNoteScarto( aModel.getNoteScarto() );
				 setCausaScarto( aModel.getCausaScarto() );
		 setCondizioneUpdate(aModel.getIdScarti());
		}


	public void setCondizione(WScartiModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_W_SCARTI = " + key );
		 }

}
