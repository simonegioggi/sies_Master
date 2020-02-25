package siap.siep.altracausa.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.altracausa.model.AltraCausaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: AltraCausaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella AltraCausa</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class AltraCausaDAO extends TableDAO
{
	public AltraCausaDAO (Connection con)
	{
    super(con);
    setTable("ALTRA_CAUSA");

    setSequenceField("ID_ALTRA_CAUSA", "ALT_CAU_SEQ");
    setFieldKey("ID_ALTRA_CAUSA", BIG_DECIMAL);

    setField("ID_ALTRA_CAUSA", BIG_DECIMAL);
    setField("ANNO", BIG_DECIMAL);
    setField("NUMERO", STRING);
    setField("DATA", DATE);
    setField("COD_LUOGO", STRING);
    setField("COD_AUTORITA", STRING);
    setField("DATA_DECORRENZA", DATE);
    setField("DATA_SCADENZA", DATE);
    setField("COD_TIPO_POS_GIURIDICA", STRING);
    //modifica relativa al tipo istituto
    setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
    //setField("COD_TIPO_ISTITUTO", STRING);
    //setField("COD_LUOGO_ISTITUTO", STRING);
    setField("ALTRO_LUOGO", STRING);
    setField("NOTE", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
	}


  //
  // METODI GET()
  //

  public BigDecimal getIdAltraCausa() 		throws DAOException	              { return getBigDecimal("ID_ALTRA_CAUSA"); }
  public BigDecimal getAnno() 		throws DAOException	                      { return getBigDecimal("ANNO"); }
  public String 	  getNumero() 		throws DAOException	                    { return getString("NUMERO"); }
  public Date 			getData() 		throws DAOException	                      { return getDate("DATA"); }
  public String 		getCodLuogo() 		throws DAOException	                  { return getString("COD_LUOGO"); }
  public String 		getCodAutorita() 		throws DAOException	                { return getString("COD_AUTORITA"); }
  public Date 			getDataDecorrenza() 		throws DAOException	            { return getDate("DATA_DECORRENZA"); }
  public Date 			getDataScadenza() 		throws DAOException	              { return getDate("DATA_SCADENZA"); }
  public String 		getCodTipoPosGiuridica() 		throws DAOException	        { return getString("COD_TIPO_POS_GIURIDICA"); }
//modifica relativa al tipo istituto
  public String                 getIstDetIdIstitutoDetenzione()  throws DAOException                 {return getString("IST_DET_ID_ISTITUTO_DETENZIONE");}
 // public String 		getCodTipoIstituto() 		throws DAOException	            { return getString("COD_TIPO_ISTITUTO"); }
 // public String 		getCodLuogoIstituto() 		throws DAOException	          { return getString("COD_LUOGO_ISTITUTO"); }
  public String 		getAltroLuogo() 		throws DAOException	                { return getString("ALTRO_LUOGO"); }
  public String 		getNote() 		throws DAOException	                      { return getString("NOTE"); }
  public String 		getCodOperatoreInserimento() 		throws DAOException	    { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 			getDataInserimento() 		throws DAOException	            { return getDate("DATA_INSERIMENTO"); }
  public String 		getCodUfficioInserimento() 		throws DAOException	      { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String 		getCodOperatoreAggiornamento() 		throws DAOException	  { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 			getDataAggiornamento() 		throws DAOException	          { return getDate("DATA_AGGIORNAMENTO"); }
  public String 		getCodUfficioAggiornamento() 		throws DAOException	    { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal getFasSieIdFascicoloSiep() 		throws DAOException	      { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }


  //
  // METODI SET()
  //

  public void setIdAltraCausa(BigDecimal aValore ) 			        { setBigDecimal("ID_ALTRA_CAUSA", aValore); }
  public void setAnno(BigDecimal aValore ) 			                { setBigDecimal("ANNO", aValore); }
  public void setNumero(String aValore ) 			                  { setString("NUMERO", aValore); }
  public void setData(Date aValore ) 			                      { setDate("DATA", aValore); }
  public void setCodLuogo(String aValore ) 			                { setString("COD_LUOGO", aValore); }
  public void setCodAutorita(String aValore ) 			            { setString("COD_AUTORITA", aValore); }
  public void setDataDecorrenza(Date aValore ) 			            { setDate("DATA_DECORRENZA", aValore); }
  public void setDataScadenza(Date aValore ) 			              { setDate("DATA_SCADENZA", aValore); }
  public void setCodTipoPosGiuridica(String aValore ) 			    { setString("COD_TIPO_POS_GIURIDICA", aValore); }
  //modifica relativa al tipo istituto
   public void setIstDetIdIstitutoDetenzione(String aValore )                 { setString("IST_DET_ID_ISTITUTO_DETENZIONE", aValore); }
  //public void setCodTipoIstituto(String aValore ) 			        { setString("COD_TIPO_ISTITUTO", aValore); }
  //public void setCodLuogoIstituto(String aValore ) 			        { setString("COD_LUOGO_ISTITUTO", aValore); }
  public void setAltroLuogo(String aValore ) 			              { setString("ALTRO_LUOGO", aValore); }
  public void setNote(String aValore ) 			                    { setString("NOTE", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 			{ setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 			          { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 			  { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 		{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 			        { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 			{ setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setFasSieIdFascicoloSiep(BigDecimal aValore ) 		{ setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }


	public GenericModel getModel() throws DAOException
  {
    return new AltraCausaModel(
                               getIdAltraCausa() ,
                               getAnno() ,
                               getNumero() ,
                               getData() ,
                               getCodLuogo() ,
                               "",
                               getCodAutorita() ,
                               "",
                               getDataDecorrenza() ,
                               getDataScadenza() ,
                               getCodTipoPosGiuridica() ,
                               "",
                               //modifica relativa al tipo istituto
                               getIstDetIdIstitutoDetenzione() ,
                             //  getCodTipoIstituto() ,
                             //  "",
                             //  getCodLuogoIstituto() ,
                             //  "",
                               getAltroLuogo() ,
                               getNote() ,
                               getCodOperatoreInserimento() ,
                               getDataInserimento() ,
                               getCodUfficioInserimento() ,
                               "",
                               getCodOperatoreAggiornamento() ,
                               getDataAggiornamento() ,
                               getCodUfficioAggiornamento() ,
                               "",
                               getFasSieIdFascicoloSiep(),
                              //modifica relativa al tipo istituto
                               null
              								);
  }

  public void setDAOFromModel(AltraCausaModel aModel) throws DAOException
  {
    setIdAltraCausa( aModel.getIdAltraCausa() );
    setAnno( aModel.getAnno() );
    setNumero( aModel.getNumero() );
    setData( aModel.getData() );
    setCodLuogo( aModel.getCodLuogo() );
    setCodAutorita( aModel.getCodAutorita() );
    setDataDecorrenza( aModel.getDataDecorrenza() );
    setDataScadenza( aModel.getDataScadenza() );
    setCodTipoPosGiuridica( aModel.getCodTipoPosGiuridica() );
    //modifica relativa al tipo istituto
    setIstDetIdIstitutoDetenzione( aModel.getIstDetIdIstitutoDetenzione() );
   // setCodTipoIstituto( aModel.getCodTipoIstituto() );
   // setCodLuogoIstituto( aModel.getCodLuogoIstituto() );
    setAltroLuogo( aModel.getAltroLuogo() );
    setNote( aModel.getNote() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    //setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    //setDataAggiornamento( aModel.getDataAggiornamento() );
    //setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
  }

  public void setDAOFromModelForUpdate(AltraCausaModel aModel) throws DAOException
  {
    //setIdAltraCausa( aModel.getIdAltraCausa() );
    setAnno( aModel.getAnno() );
    setNumero( aModel.getNumero() );
    setData( aModel.getData() );
    setCodLuogo( aModel.getCodLuogo() );
    setCodAutorita( aModel.getCodAutorita() );
    setDataDecorrenza( aModel.getDataDecorrenza() );
    setDataScadenza( aModel.getDataScadenza() );
    setCodTipoPosGiuridica( aModel.getCodTipoPosGiuridica() );
  //modifica relativa al tipo istituto
    setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
   // setCodTipoIstituto( aModel.getCodTipoIstituto() );
   // setCodLuogoIstituto( aModel.getCodLuogoIstituto() );
    setAltroLuogo( aModel.getAltroLuogo() );
    setNote( aModel.getNote() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    //setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );

    setCondizioneUpdate(aModel.getIdAltraCausa());
  }

  public void setCondizione(AltraCausaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

	public void setCondizioneUpdate(BigDecimal key)
  {
	  setCondition(" ID_ALTRA_CAUSA = " + key );
  }
}
