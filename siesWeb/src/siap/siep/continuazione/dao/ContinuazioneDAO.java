package siap.siep.continuazione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.continuazione.model.ContinuazioneModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: ContinuazioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Continuazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ContinuazioneDAO extends TableDAO
{
	public ContinuazioneDAO (Connection con)
	{
    super(con);

    setTable("CONTINUAZIONE");

    setSequenceField("ID_CONTINUAZIONE", "CON_SEQ");

    setFieldKey("ID_CONTINUAZIONE", BIG_DECIMAL);

    setField("ID_CONTINUAZIONE", BIG_DECIMAL);
    setField("PROGR_CONTINUAZIONE", BIG_DECIMAL);
    setField("COD_TIPO_CONTINUAZIONE", STRING);
    setField("COD_TIPO_AUTORITA", STRING);
    setField("COD_LUOGO_AUTORITA", STRING);
    setField("DATA_SENTENZA", DATE);
    setField("ANNO_SENTENZA", BIG_DECIMAL);
    setField("NUM_SENTENZA", STRING);
    setField("ANNO_REGE_PM", BIG_DECIMAL);
    setField("NUM_REGE_PM", STRING);
    setField("ANNO_REGE_GIP", BIG_DECIMAL);
    setField("NUM_REGE_GIP", STRING);
    setField("ANNO_REGE_DIB", BIG_DECIMAL);
    setField("NUM_REGE_DIB", STRING);
    setField("ANNO_REGE_CAS", BIG_DECIMAL);
    setField("NUM_REGE_CAS", STRING);
    setField("ANNO_REGE_CAP", BIG_DECIMAL);
    setField("NUM_REGE_CAP", STRING);
    setField("ANNO_REGE_CASAP", BIG_DECIMAL);
    setField("NUM_REGE_CASAP", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("PEN_COM_ID_PENA_COMPLESSIVA", BIG_DECIMAL);
	}

  //
  // METODI GET()
  //

  public BigDecimal getIdContinuazione() 		throws DAOException	            { return getBigDecimal("ID_CONTINUAZIONE"); }
  public BigDecimal getProgrContinuazione() 		throws DAOException	        { return getBigDecimal("PROGR_CONTINUAZIONE"); }
  public String 		getCodTipoContinuazione() 		throws DAOException	      { return getString("COD_TIPO_CONTINUAZIONE"); }
  public String 		getCodTipoAutorita() 		throws DAOException	            { return getString("COD_TIPO_AUTORITA"); }
  public String 		getCodLuogoAutorita() 		throws DAOException	          { return getString("COD_LUOGO_AUTORITA"); }
  public Date 			getDataSentenza() 		throws DAOException	              { return getDate("DATA_SENTENZA"); }
  public BigDecimal getAnnoSentenza() 		throws DAOException	              { return getBigDecimal("ANNO_SENTENZA"); }
  public String 		getNumSentenza() 		throws DAOException	                { return getString("NUM_SENTENZA"); }
  public BigDecimal getAnnoRegePm() 		throws DAOException	                { return getBigDecimal("ANNO_REGE_PM"); }
  public String 		getNumRegePm() 		throws DAOException	                  { return getString("NUM_REGE_PM"); }
  public BigDecimal getAnnoRegeGip() 		throws DAOException	                { return getBigDecimal("ANNO_REGE_GIP"); }
  public String 		getNumRegeGip() 		throws DAOException	                { return getString("NUM_REGE_GIP"); }
  public BigDecimal getAnnoRegeDib() 		throws DAOException	                { return getBigDecimal("ANNO_REGE_DIB"); }
  public String 		getNumRegeDib() 		throws DAOException	                { return getString("NUM_REGE_DIB"); }
  public BigDecimal getAnnoRegeCas() 		throws DAOException	                { return getBigDecimal("ANNO_REGE_CAS"); }
  public String 		getNumRegeCas() 		throws DAOException	                { return getString("NUM_REGE_CAS"); }
  public BigDecimal getAnnoRegeCap() 		throws DAOException	                { return getBigDecimal("ANNO_REGE_CAP"); }
  public String 		getNumRegeCap() 		throws DAOException	                { return getString("NUM_REGE_CAP"); }
  public BigDecimal getAnnoRegeCasap() 		throws DAOException	              { return getBigDecimal("ANNO_REGE_CASAP"); }
  public String 		getNumRegeCasap() 		throws DAOException	              { return getString("NUM_REGE_CASAP"); }
  public String 		getCodOperatoreInserimento() 		throws DAOException	    { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 			getDataInserimento() 		throws DAOException	            { return getDate("DATA_INSERIMENTO"); }
  public String 		getCodUfficioInserimento() 		throws DAOException	      { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 		getCodOperatoreAggiornamento() 		throws DAOException	  { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 			getDataAggiornamento() 		throws DAOException	          { return getDate("DATA_AGGIORNAMENTO"); }
  public String 		getCodUfficioAggiornamento() 		throws DAOException	    { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal getPenComIdPenaComplessiva() 		throws DAOException	    { return getBigDecimal("PEN_COM_ID_PENA_COMPLESSIVA"); }


  //
  // METODI SET()
  //

  public void setIdContinuazione(BigDecimal aValore ) 			      { setBigDecimal("ID_CONTINUAZIONE", aValore); }
  public void setProgrContinuazione(BigDecimal aValore ) 			    { setBigDecimal("PROGR_CONTINUAZIONE", aValore); }
  public void setCodTipoContinuazione(String aValore ) 			      { setString("COD_TIPO_CONTINUAZIONE", aValore); }
  public void setCodTipoAutorita(String aValore ) 			          { setString("COD_TIPO_AUTORITA", aValore); }
  public void setCodLuogoAutorita(String aValore ) 			          { setString("COD_LUOGO_AUTORITA", aValore); }
  public void setDataSentenza(Date aValore ) 			                { setDate("DATA_SENTENZA", aValore); }
  public void setAnnoSentenza(BigDecimal aValore ) 			          { setBigDecimal("ANNO_SENTENZA", aValore); }
  public void setNumSentenza(String aValore ) 			              { setString("NUM_SENTENZA", aValore); }
  public void setAnnoRegePm(BigDecimal aValore ) 			            { setBigDecimal("ANNO_REGE_PM", aValore); }
  public void setNumRegePm(String aValore ) 			                { setString("NUM_REGE_PM", aValore); }
  public void setAnnoRegeGip(BigDecimal aValore ) 			          { setBigDecimal("ANNO_REGE_GIP", aValore); }
  public void setNumRegeGip(String aValore ) 			                { setString("NUM_REGE_GIP", aValore); }
  public void setAnnoRegeDib(BigDecimal aValore ) 			          { setBigDecimal("ANNO_REGE_DIB", aValore); }
  public void setNumRegeDib(String aValore ) 			                { setString("NUM_REGE_DIB", aValore); }
  public void setAnnoRegeCas(BigDecimal aValore ) 			          { setBigDecimal("ANNO_REGE_CAS", aValore); }
  public void setNumRegeCas(String aValore ) 			                { setString("NUM_REGE_CAS", aValore); }
  public void setAnnoRegeCap(BigDecimal aValore ) 			          { setBigDecimal("ANNO_REGE_CAP", aValore); }
  public void setNumRegeCap(String aValore ) 			                { setString("NUM_REGE_CAP", aValore); }
  public void setAnnoRegeCasap(BigDecimal aValore ) 			        { setBigDecimal("ANNO_REGE_CASAP", aValore); }
  public void setNumRegeCasap(String aValore ) 			              { setString("NUM_REGE_CASAP", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 			  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 			            { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 			    { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 			{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 			          { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 			  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setPenComIdPenaComplessiva(BigDecimal aValore ) 		{ setBigDecimal("PEN_COM_ID_PENA_COMPLESSIVA", aValore); }

	public GenericModel getModel() throws DAOException
  {
    return new ContinuazioneModel(
                                   getIdContinuazione() ,
                                   getProgrContinuazione() ,
                                   getCodTipoContinuazione() ,
                                   "",
                                   getCodTipoAutorita() ,
                                   "",
                                   getCodLuogoAutorita() ,
                                   "",
                                   getDataSentenza() ,
                                   getAnnoSentenza() ,
                                   getNumSentenza() ,
                                   getAnnoRegePm() ,
                                   getNumRegePm() ,
                                   getAnnoRegeGip() ,
                                   getNumRegeGip() ,
                                   getAnnoRegeDib() ,
                                   getNumRegeDib() ,
                                   getAnnoRegeCas() ,
                                   getNumRegeCas() ,
                                   getAnnoRegeCap() ,
                                   getNumRegeCap() ,
                                   getAnnoRegeCasap() ,
                                   getNumRegeCasap() ,
                                   getCodOperatoreInserimento() ,
                                   getDataInserimento() ,
                                   getCodUfficioInserimento() ,
                                   "",
                                   getCodOperatoreAggiornamento() ,
                                   getDataAggiornamento() ,
                                   getCodUfficioAggiornamento() ,
                                   "",
                                   getPenComIdPenaComplessiva()
                                );
  }

  public void setDAOFromModel(ContinuazioneModel aModel) throws DAOException
  {
    setIdContinuazione( aModel.getIdContinuazione() );
    setProgrContinuazione( aModel.getProgrContinuazione() );
    setCodTipoContinuazione( aModel.getCodTipoContinuazione() );
    setCodTipoAutorita( aModel.getCodTipoAutorita() );
    setCodLuogoAutorita( aModel.getCodLuogoAutorita() );
    setDataSentenza( aModel.getDataSentenza() );
    setAnnoSentenza( aModel.getAnnoSentenza() );
    setNumSentenza( aModel.getNumSentenza() );
    setAnnoRegePm( aModel.getAnnoRegePm() );
    setNumRegePm( aModel.getNumRegePm() );
    setAnnoRegeGip( aModel.getAnnoRegeGip() );
    setNumRegeGip( aModel.getNumRegeGip() );
    setAnnoRegeDib( aModel.getAnnoRegeDib() );
    setNumRegeDib( aModel.getNumRegeDib() );
    setAnnoRegeCas( aModel.getAnnoRegeCas() );
    setNumRegeCas( aModel.getNumRegeCas() );
    setAnnoRegeCap( aModel.getAnnoRegeCap() );
    setNumRegeCap( aModel.getNumRegeCap() );
    setAnnoRegeCasap( aModel.getAnnoRegeCasap() );
    setNumRegeCasap( aModel.getNumRegeCasap() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    //setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    //setDataAggiornamento( aModel.getDataAggiornamento() );
    //setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setPenComIdPenaComplessiva( aModel.getPenComIdPenaComplessiva() );
  }

  public void setDAOFromModelForUpdate(ContinuazioneModel aModel) throws DAOException
  {
    //setIdContinuazione( aModel.getIdContinuazione() );
    //setProgrContinuazione( aModel.getProgrContinuazione() );
    setCodTipoContinuazione( aModel.getCodTipoContinuazione() );
    setCodTipoAutorita( aModel.getCodTipoAutorita() );
    setCodLuogoAutorita( aModel.getCodLuogoAutorita() );
    setDataSentenza( aModel.getDataSentenza() );
    setAnnoSentenza( aModel.getAnnoSentenza() );
    setNumSentenza( aModel.getNumSentenza() );
    setAnnoRegePm( aModel.getAnnoRegePm() );
    setNumRegePm( aModel.getNumRegePm() );
    setAnnoRegeGip( aModel.getAnnoRegeGip() );
    setNumRegeGip( aModel.getNumRegeGip() );
    setAnnoRegeDib( aModel.getAnnoRegeDib() );
    setNumRegeDib( aModel.getNumRegeDib() );
    setAnnoRegeCas( aModel.getAnnoRegeCas() );
    setNumRegeCas( aModel.getNumRegeCas() );
    setAnnoRegeCap( aModel.getAnnoRegeCap() );
    setNumRegeCap( aModel.getNumRegeCap() );
    setAnnoRegeCasap( aModel.getAnnoRegeCasap() );
    setNumRegeCasap( aModel.getNumRegeCasap() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    //setPenComIdPenaComplessiva( aModel.getPenComIdPenaComplessiva() );

    setCondizioneUpdate(aModel.getIdContinuazione());
  }

	public void setCondizione(ContinuazioneModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

	public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_CONTINUAZIONE = " + key );
  }

  public void setCondizioneByIdPenaComplessiva(BigDecimal aIdPenaComplessiva)
  {
    setCondition(" PEN_COM_ID_PENA_COMPLESSIVA = " + aIdPenaComplessiva);
  }
}
