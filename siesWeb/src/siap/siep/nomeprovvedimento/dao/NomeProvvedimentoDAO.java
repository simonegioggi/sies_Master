package siap.siep.nomeprovvedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.nomeprovvedimento.model.NomeProvvedimentoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: NomeProvvedimentoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella NomeProvvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class NomeProvvedimentoDAO extends TableDAO
{
	public NomeProvvedimentoDAO (Connection con)
	{
			 super(con);
			 setTable("NOME_PROVVEDIMENTO");

			 //Settare la Sequence e i campi chiave

			 setField("COD_NOME_PROVVEDIMENTO", STRING);
			 setField("EVE_ID_EVENTO", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

			public String 				 getCodNomeProvvedimento() 		throws DAOException	 { return getString("COD_NOME_PROVVEDIMENTO"); }
			public BigDecimal 		 getEveIdEvento() 		throws DAOException	 { return getBigDecimal("EVE_ID_EVENTO"); }


  //
  // METODI SET()
  //

			public void  	 setCodNomeProvvedimento(String aValore ) 			 { setString("COD_NOME_PROVVEDIMENTO", aValore); }
			public void  	 setEveIdEvento(BigDecimal aValore ) 			 { setBigDecimal("EVE_ID_EVENTO", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new NomeProvvedimentoModel(
								 getCodNomeProvvedimento() ,
								 "",
								 getEveIdEvento()
								);
		}


	 public void 	 setDAOFromModel(NomeProvvedimentoModel aModel) throws DAOException
  		{
				 setCodNomeProvvedimento( aModel.getCodNomeProvvedimento() );
				 setEveIdEvento( aModel.getEveIdEvento() );
		}


	 public void 	 setDAOFromModelForUpdate(NomeProvvedimentoModel aModel) throws DAOException
  		{
				 setCodNomeProvvedimento( aModel.getCodNomeProvvedimento() );
				 setEveIdEvento( aModel.getEveIdEvento() );
		     setCondizioneUpdate(aModel.getCodNomeProvvedimento());
		}


	public void setCondizione(NomeProvvedimentoModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(String  key)
 			 {
	 setCondition(" COD_NOME_PROVVEDIMENTO = " + key );
		 }

}
