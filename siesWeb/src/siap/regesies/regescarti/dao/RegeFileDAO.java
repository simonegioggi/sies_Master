package siap.regesies.regescarti.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.regesies.regescarti.model.RegeFileModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: RegeFileDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella RegeFile</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class RegeFileDAO extends TableDAO
{
	public RegeFileDAO (Connection con)
	{
			 super(con);
			 setTable("REGE_FILE");

			 //Settare la Sequence e i campi chiave

			 setField("ID_FILE", STRING);
			 setField("DATA_INSERIMENTO", DATE);
			 setField("COD_COMUNE", STRING);
			 //setField("FILE_BLOB", BLOB);
			 setField("AF10FASC", STRING);
			 setField("AF10PROG", STRING);
			 setField("AF10TIPOR", STRING);
			 setField("COD_STATO", BIG_DECIMAL);
			 setField("DESC_ERR", STRING);
	}


  //
  // METODI GET()
  //

			public String 				 getIdFile() 		throws DAOException	 { return getString("ID_FILE"); }
			public Date 					 getDataInserimento() 		throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
			public String 				 getCodComune() 		throws DAOException	 { return getString("COD_COMUNE"); }
			//public Blob 		 getFileBlob() 		throws DAOException	 { return getBlob("FILE_BLOB"); }
			public String 				 getAf10fasc() 		throws DAOException	 { return getString("AF10FASC"); }
			public String 				 getAf10prog() 		throws DAOException	 { return getString("AF10PROG"); }
			public String 				 getAf10tipor() 		throws DAOException	 { return getString("AF10TIPOR"); }
			public BigDecimal 		 getCodStato() 		throws DAOException	 { return getBigDecimal("COD_STATO"); }
			public String 				 getDescErr() 		throws DAOException	 { return getString("DESC_ERR"); }


  //
  // METODI SET()
  //

			public void  	 setIdFile(String aValore ) 			 { setString("ID_FILE", aValore); }
			public void  	 setDataInserimento(Date aValore ) 			 { setDate("DATA_INSERIMENTO", aValore); }
			public void  	 setCodComune(String aValore ) 			 { setString("COD_COMUNE", aValore); }
			//public void  	 setFileBlob(Blob aValore ) 			 { setBlob("FILE_BLOB", aValore); }
			public void  	 setAf10fasc(String aValore ) 			 { setString("AF10FASC", aValore); }
			public void  	 setAf10prog(String aValore ) 			 { setString("AF10PROG", aValore); }
			public void  	 setAf10tipor(String aValore ) 			 { setString("AF10TIPOR", aValore); }
			public void  	 setCodStato(BigDecimal aValore ) 			 { setBigDecimal("COD_STATO", aValore); }
			public void  	 setDescErr(String aValore ) 			 { setString("DESC_ERR", aValore); }


	public GenericModel getModel() throws DAOException
  			 {
 				 return new RegeFileModel(
								 getIdFile() ,
								 getDataInserimento() ,
								 getCodComune() ,
								 "",
								 //getFileBlob() ,
								 getAf10fasc() ,
								 getAf10prog() ,
								 getAf10tipor() ,
								 getCodStato() ,
								 //"",
								 getDescErr()
								);
		}


	 public void 	 setDAOFromModel(RegeFileModel aModel) throws DAOException
  		{
				 setIdFile( aModel.getIdFile() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodComune( aModel.getCodComune() );
				 //setFileBlob( aModel.getFileBlob() );
				 setAf10fasc( aModel.getAf10fasc() );
				 setAf10prog( aModel.getAf10prog() );
				 setAf10tipor( aModel.getAf10tipor() );
				 setCodStato( aModel.getCodStato() );
				 setDescErr( aModel.getDescErr() );
		}


	 public void 	 setDAOFromModelForUpdate(RegeFileModel aModel) throws DAOException
  		{
				 //setIdFile( aModel.getIdFile() );
				 setCodComune( aModel.getCodComune() );
				 //setFileBlob( aModel.getFileBlob() );
				 setAf10fasc( aModel.getAf10fasc() );
				 setAf10prog( aModel.getAf10prog() );
				 setAf10tipor( aModel.getAf10tipor() );
				 setCodStato( aModel.getCodStato() );
				 setDescErr( aModel.getDescErr() );
         setCondizioneUpdate(aModel.getIdFile());
		}


	public void setCondizione(RegeFileModel aModel)
		 {
		 String lCondizioni = new String();

		 boolean lInserito = false;
 		 if ( lInserito ) setCondition(lCondizioni);
		 }


	public void setCondizioneUpdate(String key)
 			 {
	 setCondition(" ID_FILE = '"+ key +"'" );
		 }

}
