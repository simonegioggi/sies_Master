package siap.sico.soggettodattilo.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sico.soggettodattilo.model.SoggettoDattiloModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: SoggettoDattiloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella SoggettoDattilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class SoggettoDattiloDAO extends TableDAO
{
	public SoggettoDattiloDAO (Connection con)
	{
			 super(con);
			 setTable("SOGGETTO_DATTILO");

			 //Settare la Sequence e i campi chiave
			 setSequenceField("ID_DATTILO","SEQ_SOG_DAT");
			 setFieldKey("ID_DATTILO", BIG_DECIMAL);

			 setField("ID_DATTILO", BIG_DECIMAL);
			 setField("COD_SOGGETTO", BIG_DECIMAL);
			 setField("DOC_TIPO", STRING);
			 setField("DOC_NOME", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_OPERATORE_INSERIMENTO", STRING);
			 setField("COD_UFFICIO_INSERIMENTO", STRING);
			 setField("DOC_BLOB", TBLOB);
	}


  //
  // METODI GET()
  //

			public BigDecimal 		 getIdDattilo() 		throws DAOException	 { return getBigDecimal("ID_DATTILO"); }
			public BigDecimal 		 getCodSoggetto() 		throws DAOException	 { return getBigDecimal("COD_SOGGETTO"); }
			public String 				 getDocTipo() 		throws DAOException	 { return getString("DOC_TIPO"); }
			public String 				 getDocNome() 		throws DAOException	 { return getString("DOC_NOME"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodOperatoreInserimento() 		throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
			public String 				 getCodUfficioInserimento() 		throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
			public ByteArrayOutputStream 		 getDocBlob() 		throws DAOException	 { return getBlob("DOC_BLOB"); }


  //
  // METODI SET()
  //

			public void  	 setIdDattilo(BigDecimal aValore ) 			 { setBigDecimal("ID_DATTILO", aValore); }
			public void  	 setCodSoggetto(BigDecimal aValore ) 			 { setBigDecimal("COD_SOGGETTO", aValore); }
			public void  	 setDocTipo(String aValore ) 			 { setString("DOC_TIPO", aValore); }
			public void  	 setDocNome(String aValore ) 			 { setString("DOC_NOME", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodOperatoreInserimento(String aValore ) 			 { setString("COD_OPERATORE_INSERIMENTO", aValore); }
			public void  	 setCodUfficioInserimento(String aValore ) 			 { setString("COD_UFFICIO_INSERIMENTO", aValore); }
			public void  	 setDocBlob(ByteArrayInputStream aValore ) 			 { setBlob("DOC_BLOB", aValore); }


	public GenericModel getModel() throws DAOException
		{
			return new SoggettoDattiloModel(
								 getIdDattilo() ,
								 getCodSoggetto() ,
								 getDocTipo() ,
								 getDocNome() ,
								 getDataInserimento() ,
								 getCodOperatoreInserimento() ,
								 getCodUfficioInserimento()
								);
		}


	public void 	 setDAOFromModel(SoggettoDattiloModel aModel) throws DAOException
		{
				 setIdDattilo( aModel.getIdDattilo() );
				 setCodSoggetto( aModel.getCodSoggetto() );
				 setDocTipo( aModel.getDocTipo() );
				 setDocNome( aModel.getDocNome() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
				 //setDocBlob( aModel.getDocBlob() );
		}


	public void 	 setDAOFromModelForUpdate(SoggettoDattiloModel aModel) throws DAOException
		{
				 setIdDattilo( aModel.getIdDattilo() );
				 setCodSoggetto( aModel.getCodSoggetto() );
				 setDocTipo( aModel.getDocTipo() );
				 setDocNome( aModel.getDocNome() );
				 //setDocBlob( aModel.getDocBlob() );
				 setCondizioneUpdate(aModel.getIdDattilo());
		}

	public void 	 setDAOFromModelForUpdateBlob(SoggettoDattiloModel aModel) throws DAOException
	{
			 setIdDattilo( aModel.getIdDattilo() );
			 setDocBlob( aModel.getDocBlobIn() );
			 setCondizioneUpdate(aModel.getIdDattilo());
	}

	public void setCondizione(SoggettoDattiloModel aModel)
		{
			String lCondizioni = new String();
	
			boolean lInserito = false;
			if ( lInserito ) setCondition(lCondizioni);
		}


	public void setCondizioneUpdate(BigDecimal key)
		{
			setCondition(" ID_DATTILO = " + key );
		}

}
