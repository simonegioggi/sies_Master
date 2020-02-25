package siap.siep.motivoevento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.motivoevento.model.MotivoEventoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: MotivoEventoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella MotivoEvento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class MotivoEventoDAO extends TableDAO
{
	public MotivoEventoDAO (Connection con)
	{
			 super(con);
			 setTable("MOTIVO_EVENTO");

			 //Settare la Sequence e i campi chiave
       setSequenceField("ID_MOTIVO_EVENTO", "MOT_EVE_SEQ");
       setFieldKey("ID_MOTIVO_EVENTO", BIG_DECIMAL);

			 setField("ID_MOTIVO_EVENTO", BIG_DECIMAL);
			 setField("COD_MOTIVO_REVOCA", STRING);
			 setField("COD_MOTIVO_REVOCA_PM", STRING);
			 setField("MOTIVAZIONI", STRING);
			 setField("EVE_ID_EVENTO", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdMotivoEvento() 		throws DAOException	 { return getBigDecimal("ID_MOTIVO_EVENTO"); }
			public String 				 getCodMotivoRevoca() 		throws DAOException	 { return getString("COD_MOTIVO_REVOCA"); }
			public String 				 getCodMotivoRevocaPm() 		throws DAOException	 { return getString("COD_MOTIVO_REVOCA_PM"); }
			public String 				 getMotivazioni() 		throws DAOException	 { return getString("MOTIVAZIONI"); }
			public BigDecimal 		 getEveIdEvento() 		throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }


  //
  // METODI SET()
  //

			public void  	 setIdMotivoEvento(BigDecimal aValore ) 			 { setBigDecimal("ID_MOTIVO_EVENTO", aValore); }
			public void  	 setCodMotivoRevoca(String aValore ) 			 { setString("COD_MOTIVO_REVOCA", aValore); }
			public void  	 setCodMotivoRevocaPm(String aValore ) 			 { setString("COD_MOTIVO_REVOCA_PM", aValore); }
			public void  	 setMotivazioni(String aValore ) 			 { setString("MOTIVAZIONI", aValore); }
			public void  	 setEveIdEvento(BigDecimal aValore ) 			 { setBigDecimal("EVE_ID_EVENTO", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new MotivoEventoModel(
								 getIdMotivoEvento() ,
								 getCodMotivoRevoca() ,
								 "",
								 getCodMotivoRevocaPm() ,
								 "",
								 getMotivazioni() ,
								 getEveIdEvento()
								);
		}


	 public void 	 setDAOFromModel(MotivoEventoModel aModel) throws DAOException
  		{
				 setIdMotivoEvento( aModel.getIdMotivoEvento() );
				 setCodMotivoRevoca( aModel.getCodMotivoRevoca() );
				 setCodMotivoRevocaPm( aModel.getCodMotivoRevocaPm() );
				 setMotivazioni( aModel.getMotivazioni() );
				 setEveIdEvento( aModel.getEveIdEvento() );
		}


	 public void 	 setDAOFromModelForUpdate(MotivoEventoModel aModel) throws DAOException
  		{
				 setIdMotivoEvento( aModel.getIdMotivoEvento() );
				 setCodMotivoRevoca( aModel.getCodMotivoRevoca() );
				 setCodMotivoRevocaPm( aModel.getCodMotivoRevocaPm() );
				 setMotivazioni( aModel.getMotivazioni() );
				 setEveIdEvento( aModel.getEveIdEvento() );
		 setCondizioneUpdate(aModel.getIdMotivoEvento());
		}


	public void setCondizione(MotivoEventoModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(BigDecimal key)
 			 {
	 setCondition(" ID_motivo_evento = " + key );
		 }

}
