package siap.siep.dettaglioprovvedimento.dao;

import java.sql.Connection;

import siap.siep.dettaglioprovvedimento.model.DettaglioProvvedimentoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: DettaglioProvvedimentoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella DettaglioProvvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class DettaglioProvvedimentoDAO extends TableDAO
{
	public DettaglioProvvedimentoDAO (Connection con)
	{
			 super(con);
			 setTable("dettaglio_provvedimento");

			 //Settare la Sequence e i campi chiave

			 setField("COD_TIPO_EVENTO", STRING);
			 setField("COD_TIPO_PROVVEDIMENTO", STRING);
			 setField("COD_MOTIVO", STRING);
			 setField("ACTION_DETTAGLIO", STRING);
			 setField("ACTION_UPLOAD", STRING);
	}

  //
  // METODI GET()
  //

			public String 		getCodTipoEvento() 		throws DAOException	 { return getString("COD_TIPO_EVENTO"); }
			public String 		getCodTipoProvvedimento() 		throws DAOException	 { return getString("COD_TIPO_PROVVEDIMENTO"); }
			public String 		getCodMotivo() 		throws DAOException	 { return getString("COD_MOTIVO"); }
			public String 		getActionDettaglio() 		throws DAOException	 { return getString("ACTION_DETTAGLIO"); }
			public String 		getActionUpload() 		throws DAOException	 { return getString("ACTION_UPLOAD"); }

  //
  // METODI SET()
  //

			public void  	 setCodTipoEvento(String aValore ) 			 { setString("COD_TIPO_EVENTO", aValore); }
			public void  	 setCodTipoProvvedimento(String aValore ) 			 { setString("COD_TIPO_PROVVEDIMENTO", aValore); }
			public void  	 setCodMotivo(String aValore ) 			 { setString("COD_MOTIVO", aValore); }
			public void  	 setActionDettaglio(String aValore ) 			 { setString("ACTION_DETTAGLIO", aValore); }
			public void  	 setActionUpload(String aValore ) 			 { setString("ACTION_UPLOAD", aValore); }

	public GenericModel getModel() throws DAOException
  			 {
 				 return new DettaglioProvvedimentoModel(
								 getCodTipoEvento() ,
								 getCodTipoProvvedimento() ,
								 getCodMotivo() ,
								 getActionDettaglio(),
                  getCodTipoEvento() +
                  //getCodTipoProvvedimento() +
                  getCodMotivo(),
                  getActionUpload()
								);
		}


	 public void 	 setDAOFromModel(DettaglioProvvedimentoModel aModel) throws DAOException
  		{
				 setCodTipoEvento( aModel.getCodTipoEvento() );
				 setCodTipoProvvedimento( aModel.getCodTipoProvvedimento() );
				 setCodMotivo( aModel.getCodMotivo() );
				 setActionDettaglio( aModel.getActionDettaglio() );
				 setActionUpload( aModel.getActionUpload() );
		}




	public void setCondizione(DettaglioProvvedimentoModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }

}
