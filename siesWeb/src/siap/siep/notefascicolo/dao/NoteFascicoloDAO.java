package siap.siep.notefascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.notefascicolo.model.NoteFascicoloModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: NoteFascicoloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella NoteFascicolo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class NoteFascicoloDAO extends TableDAO
{
	public NoteFascicoloDAO (Connection con)
	{
			 super(con);
			 setTable("note_fascicolo");

			 //Settare la Sequence e i campi chiave

			 setField("NOTA_DISPOSITIVO", STRING);
			 setField("NOTA_AVVOCATI", STRING);
			 setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

			public String 				 getNotaDispositivo() 		throws DAOException	 { return getString("NOTA_DISPOSITIVO"); }
			public String 				 getNotaAvvocati() 		throws DAOException	 { return getString("NOTA_AVVOCATI"); }
			public BigDecimal 		 getFasIdFascicoloSiep() 		throws DAOException	 { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }


  //
  // METODI SET()
  //

			public void  	 setNotaDispositivo(String aValore ) 			 { setString("NOTA_DISPOSITIVO", aValore); }
			public void  	 setNotaAvvocati(String aValore ) 			 { setString("NOTA_AVVOCATI", aValore); }
			public void  	 setFasIdFascicoloSiep(BigDecimal aValore ) 			 { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new NoteFascicoloModel(
								 getNotaDispositivo() ,
								 getNotaAvvocati() ,
								 getFasIdFascicoloSiep()
								);
		}


	 public void 	 setDAOFromModel(NoteFascicoloModel aModel) throws DAOException
  		{
				 setNotaDispositivo( aModel.getNotaDispositivo() );
				 setNotaAvvocati( aModel.getNotaAvvocati() );
				 setFasIdFascicoloSiep( aModel.getFasIdFascicoloSiep() );
		}


	 public void 	 setDAOFromModelForUpdate(NoteFascicoloModel aModel) throws DAOException
  		{
				 setNotaDispositivo( aModel.getNotaDispositivo() );
				 setNotaAvvocati( aModel.getNotaAvvocati() );
				 setFasIdFascicoloSiep( aModel.getFasIdFascicoloSiep() );
		     //setCondizioneUpdate(aModel.getIdNoteFascicolo());
		}


	public void setCondizione(NoteFascicoloModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	    setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + key );
		 }

}
