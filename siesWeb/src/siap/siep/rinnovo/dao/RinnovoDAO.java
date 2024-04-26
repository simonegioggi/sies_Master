package siap.siep.rinnovo.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.rinnovo.model.RinnovoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;



/**
* <p>Title: RinnovoDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Rinnovo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class RinnovoDAO extends TableDAO
{
	public RinnovoDAO (Connection con)
	{
		super(con);
		setTable("RINNOVO");

		//Settare la Sequence e i campi chiave
		this.setSequenceField("ID_RINNOVO","RIN_SEQ");
		this.setFieldKey("ID_RINNOVO", BIG_DECIMAL);

		setField("ID_RINNOVO", BIG_DECIMAL);
		setField("COD_TIPO_RINNOVO", STRING);
		setField("DATA_RINNOVO", DATE);
		setField("COD_TIPO_AUTORITA_RINNOVO", STRING);
		setField("COD_LUOGO_RINNOVO", STRING);
		setField("NOTE", STRING);
		setField("DOC_BLOB", TBLOB);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("NOT_ID_NOTIFICA", BIG_DECIMAL);
		setField("VER_ID_VERBALE", BIG_DECIMAL);
		setField("FLAG_DOCUMENTO_REGISTRATO", STRING);
		setField("TEM_ID_TEMPLATE", STRING);
		setField("NUOVO_LUOGO_NOTIFICA", STRING);

		// MEV_2023-33
		setField("ESITO", STRING);
	}


  //
  // METODI GET()
  //

	public BigDecimal      getIdRinnovo()     throws DAOException  { return getBigDecimal("ID_RINNOVO"); }
	public String          getCodTipoRinnovo()    throws DAOException  { return getString("COD_TIPO_RINNOVO"); }
	public Date            getDataRinnovo()     throws DAOException  { return getDate("DATA_RINNOVO"); }
	public String          getCodTipoAutoritaRinnovo()    throws DAOException  { return getString("COD_TIPO_AUTORITA_RINNOVO"); }
	public String          getCodLuogoRinnovo()     throws DAOException  { return getString("COD_LUOGO_RINNOVO"); }
	public String          getNote()    throws DAOException  { return getString("NOTE"); }
	public ByteArrayOutputStream    getDocBlob()        throws DAOException   { return getBlob("DOC_BLOB"); }
	public String          getCodOperatoreInserimento()     throws DAOException  { return getString("COD_OPERATORE_INSERIMENTO"); }
	public Date            getDataInserimento()     throws DAOException  { return getDate("DATA_INSERIMENTO"); }
	public String          getCodUfficioInserimento()     throws DAOException  { return getString("COD_UFFICIO_INSERIMENTO"); }
	public String          getCodOperatoreAggiornamento()     throws DAOException  { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
	public Date            getDataAggiornamento()     throws DAOException  { return getDate("DATA_AGGIORNAMENTO"); }
	public String          getCodUfficioAggiornamento()     throws DAOException  { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
	public BigDecimal      getNotIdNotifica()     throws DAOException  { return getBigDecimal("NOT_ID_NOTIFICA"); }
	public BigDecimal      getVerIdVerbale()    throws DAOException  { return getBigDecimal("VER_ID_VERBALE"); }
	public String          getFlagDocumentoRegistrato() throws DAOException  { return getString("FLAG_DOCUMENTO_REGISTRATO"); }
	public String          getTemIdTemplate() throws DAOException  { return getString("TEM_ID_TEMPLATE"); }
	public String          getNuovoLuogoNotifica() throws DAOException   { return getString("NUOVO_LUOGO_NOTIFICA"); }
	// MEV_2023-33
	public String          getEsito() throws DAOException   { return getString("ESITO"); }
	
   //
   // METODI SET()
   //
	public void    setIdRinnovo(BigDecimal aValore )       { setBigDecimal("ID_RINNOVO", aValore); }
	public void    setCodTipoRinnovo(String aValore )        { setString("COD_TIPO_RINNOVO", aValore); }
	public void    setDataRinnovo(Date aValore )       { setDate("DATA_RINNOVO", aValore); }
	public void    setCodTipoAutoritaRinnovo(String aValore )        { setString("COD_TIPO_AUTORITA_RINNOVO", aValore); }
	public void    setCodLuogoRinnovo(String aValore )       { setString("COD_LUOGO_RINNOVO", aValore); }
	public void    setNote(String aValore )        { setString("NOTE", aValore); }
	public void    setDocBlob( ByteArrayInputStream aValore )      { setBlob("DOC_BLOB", aValore); }
	public void    setCodOperatoreInserimento(String aValore )       { setString("COD_OPERATORE_INSERIMENTO", aValore); }
	public void    setDataInserimento(Date aValore )       { setDate("DATA_INSERIMENTO", aValore); }
	public void    setCodUfficioInserimento(String aValore )       { setString("COD_UFFICIO_INSERIMENTO", aValore); }
	public void    setCodOperatoreAggiornamento(String aValore )       { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
	public void    setDataAggiornamento(Date aValore )       { setDate("DATA_AGGIORNAMENTO", aValore); }
	public void    setCodUfficioAggiornamento(String aValore )       { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
	public void    setNotIdNotifica(BigDecimal aValore )       { setBigDecimal("NOT_ID_NOTIFICA", aValore); }
	public void    setVerIdVerbale(BigDecimal aValore )        { setBigDecimal("VER_ID_VERBALE", aValore); }
	public void    setFlagDocumentoRegistrato(String aValore )       { setString("FLAG_DOCUMENTO_REGISTRATO", aValore); }
	public void    setTemIdTemplate(String aValore )               { setString("TEM_ID_TEMPLATE", aValore); }
	public void    setNuovoLuogoNotifica(String aValore)       {setString("NUOVO_LUOGO_NOTIFICA", aValore); }
	// MEV_2023-33
	public void    setEsito(String aValore)       {setString("ESITO", aValore); }


	/*public GenericModel getModel() throws DAOException
  			 {
 				 return new RinnovoModel(
								 getIdRinnovo() ,
								 getCodTipoRinnovo() ,
								 //"",
								 getDataRinnovo() ,
								 getCodTipoAutoritaRinnovo() ,
								 "",
								 getCodLuogoRinnovo() ,
								 "",
								 getNote() ,
								 getDocBlob() ,
								 getCodOperatoreInserimento() ,
								 getDataInserimento() ,
								 getCodUfficioInserimento() ,
								// "",
								 getCodOperatoreAggiornamento() ,
								 getDataAggiornamento() ,
								 getCodUfficioAggiornamento() ,
								// "",
								 getNotIdNotifica() ,
								 getVerIdVerbale(),
                 getTemIdTemplate(),
                 getFlagDocumentoRegistrato()
								);
		}*/


	public void setDAOFromModel(RinnovoModel aModel) throws DAOException
	{
	  setIdRinnovo( aModel.getIdRinnovo() );
	  setCodTipoRinnovo( aModel.getCodTipoRinnovo() );
	  setDataRinnovo( aModel.getDataRinnovo() );
	  setCodTipoAutoritaRinnovo( aModel.getCodTipoAutoritaRinnovo() );
	  setCodLuogoRinnovo( aModel.getCodLuogoRinnovo() );
	  setNote( aModel.getNote() );
	  //setDocBlob( aModel.getDocBlob() );
	  setDocBlob( aModel.getDocBlobIn() );
	  setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
	  setDataInserimento( aModel.getDataInserimento() );
	  setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
	  setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
	  setDataAggiornamento( aModel.getDataAggiornamento() );
	  setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
	  setNotIdNotifica( aModel.getNotIdNotifica() );
	  setVerIdVerbale( aModel.getVerIdVerbale() );
	  setFlagDocumentoRegistrato(aModel.getFlagDocumentoRegistrato() );
	  setTemIdTemplate(aModel.getTemIdTemplate() );
	  setNuovoLuogoNotifica(aModel.getNuovoLuogoNotifica());
	  // MEV_2023-33
	  setEsito(aModel.getEsito());
	}


	public void setDAOFromModelForUpdate(RinnovoModel aModel) throws DAOException
	{
	  //setIdRinnovo( aModel.getIdRinnovo() );
	  setCodTipoRinnovo( aModel.getCodTipoRinnovo() );
	  setDataRinnovo( aModel.getDataRinnovo() );
	  setCodTipoAutoritaRinnovo( aModel.getCodTipoAutoritaRinnovo() );
	  setCodLuogoRinnovo( aModel.getCodLuogoRinnovo() );
	  setNote( aModel.getNote() );
	  //setDocBlob( aModel.getDocBlob() );
	  setDocBlob( aModel.getDocBlobIn() );
	  setNotIdNotifica( aModel.getNotIdNotifica() );
	  setVerIdVerbale( aModel.getVerIdVerbale() );
	  setFlagDocumentoRegistrato(aModel.getFlagDocumentoRegistrato() );
	  setTemIdTemplate(aModel.getTemIdTemplate() );
	  setNuovoLuogoNotifica(aModel.getNuovoLuogoNotifica());
	  // MEV_2023-33
	  setEsito(aModel.getEsito());

	  setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
	  setDataAggiornamento( aModel.getDataAggiornamento() );
	  setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );

	  setCondizioneUpdate(aModel.getIdRinnovo());
	}

	public void setDAOFromModelForUpdateBlob(RinnovoModel aModel) throws DAOException
	{
	  setDocBlob( aModel.getDocBlobIn() );
	  setDataAggiornamento( aModel.getDataAggiornamento() );
	  setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
	  setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
	  setFlagDocumentoRegistrato( aModel.getFlagDocumentoRegistrato() );
	}

	public void setCondizione(RinnovoModel aModel)
	{
	  String lCondizioni = new String();

	  boolean lInserito = false;
	  if ( lInserito ) setCondition(lCondizioni);
	}


	public void setCondizioneUpdate(BigDecimal key)
	{
	  setCondition(" ID_RINNOVO = " + key );
	}

}
