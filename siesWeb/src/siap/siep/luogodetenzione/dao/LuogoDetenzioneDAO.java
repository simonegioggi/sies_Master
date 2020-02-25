package siap.siep.luogodetenzione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: LuogoDetenzioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella LuogoDetenzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class LuogoDetenzioneDAO extends SIAPTableDAO
{
  public LuogoDetenzioneDAO (Connection con)
  {
    super(con);

    setTable("LUOGO_DETENZIONE");

    setSequenceField("ID_LUOGO_DETENZIONE", "LUO_DET_SEQ");
    setFieldKey("ID_LUOGO_DETENZIONE", BIG_DECIMAL);

    setField("ID_LUOGO_DETENZIONE", BIG_DECIMAL);
//modifica relativa al tipo istituto
    setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
   // setField("COD_TIPO_ISTITUTO", STRING);
   // setField("COD_LUOGO", STRING);
   // setField("INDIRIZZO", STRING);
    //setField("DESCR", STRING);
    setField("NOTE", STRING);
    setField("DATA_INIZIO_DETENZIONE", DATE);
    setField("DATA_FINE_DETENZIONE", DATE);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
    setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
    setField("POS_GIU_ID_POSIZIONE_GIURIDICA", BIG_DECIMAL);
  //modifica relativa al tipo istituto
    setField("ALTRO_LUOGO", STRING);
  }

  //
  // METODI GET()
  //

  public BigDecimal getIdLuogoDetenzione() 		throws DAOException	            { return getBigDecimal("ID_LUOGO_DETENZIONE"); }
//modifica relativa al tipo istituto
  public String getIstDetIdIstitutoDetenzione() 		throws DAOException	            { return getString("IST_DET_ID_ISTITUTO_DETENZIONE"); }
//  public String getCodTipoIstituto() 		        throws DAOException	          { return getString("COD_TIPO_ISTITUTO"); }
//  public String getCodLuogo() 		                throws DAOException	        { return getString("COD_LUOGO"); }
//  public String getIndirizzo() 		                throws DAOException	        { return getString("INDIRIZZO"); }
  public String getDescr() 		                throws DAOException	            { return getString("DESCR"); }
  public String getNote() 		                throws DAOException	            { return getString("NOTE"); }
  public Date 	getDataInizioDetenzione() 		throws DAOException	            { return getDate("DATA_INIZIO_DETENZIONE"); }
  public Date 	getDataFineDetenzione() 		throws DAOException	              { return getDate("DATA_FINE_DETENZIONE"); }
  public String getCodOperatoreInserimento() 		throws DAOException	          { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 	getDataInserimento() 		        throws DAOException	          { return getDate("DATA_INSERIMENTO"); }
  public String getCodUfficioInserimento() 		throws DAOException	            { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String getCodOperatoreAggiornamento() 		throws DAOException	        { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 	getDataAggiornamento() 		        throws DAOException	        { return getDate("DATA_AGGIORNAMENTO"); }
  public String getCodUfficioAggiornamento() 		throws DAOException	          { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal getFasSieIdFascicoloSiep() 		throws DAOException	        { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  public BigDecimal getFasSiuIdFascicoloSius() 		throws DAOException	        { return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"); }
  public BigDecimal getPosGiuIdPosizioneGiuridica() 		throws DAOException	  { return getBigDecimal("POS_GIU_ID_POSIZIONE_GIURIDICA"); }
//modifica relativa al tipo istituto
  public String    getAltroLuogo()		throws DAOException	          { return getString("ALTRO_LUOGO"); }


  //
  // METODI SET()
  //

  public void setIdLuogoDetenzione(BigDecimal aValore ) 	          { setBigDecimal("ID_LUOGO_DETENZIONE", aValore); }
//modifica relativa al tipo istituto
  public void setIstDetIdIstitutoDetenzione(String aValore ) 		  { setString("IST_DET_ID_ISTITUTO_DETENZIONE", aValore); }
//  public void setCodTipoIstituto(String aValore ) 		              { setString("COD_TIPO_ISTITUTO", aValore); }
//  public void setCodLuogo(String aValore ) 			                    { setString("COD_LUOGO", aValore); }
//  public void setIndirizzo(String aValore ) 			                  { setString("INDIRIZZO", aValore); }
  public void setDescr(String aValore ) 			                      { setString("DESCR", aValore); }
  public void setNote(String aValore ) 			                        { setString("NOTE", aValore); }
  public void setDataInizioDetenzione(Date aValore ) 		            { setDate("DATA_INIZIO_DETENZIONE", aValore); }
  public void setDataFineDetenzione(Date aValore ) 		              { setDate("DATA_FINE_DETENZIONE", aValore); }
  public void setCodOperatoreInserimento(String aValore ) 	        { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 		                { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore ) 	          { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore ) 	      { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 		              { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore ) 	        { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setFasSieIdFascicoloSiep(BigDecimal aValore ) 	      { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void setFasSiuIdFascicoloSius(BigDecimal aValore ) 	      { setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore); }
  public void setPosGiuIdPosizioneGiuridica(BigDecimal aValore ) 		{ setBigDecimal("POS_GIU_ID_POSIZIONE_GIURIDICA", aValore); }
//modifica relativa al tipo istituto
  public void setAltroLuogo(String aValore)		throws DAOException	          { setString("ALTRO_LUOGO", aValore); }

  public GenericModel getModel() throws DAOException
  {
    return new LuogoDetenzioneModel(
                                   getIdLuogoDetenzione() ,
                                //modifica relativa al tipo istituto
                                   getIstDetIdIstitutoDetenzione(),
                                  // getCodTipoIstituto() ,
                                  // "",
                                  // getCodLuogo() ,
                                  // "",
                                  // getIndirizzo() ,
                                   getDescr() ,
                                   getNote() ,
                                   getDataInizioDetenzione() ,
                                   getDataFineDetenzione() ,
                                   getCodOperatoreInserimento() ,
                                   getDataInserimento() ,
                                   getCodUfficioInserimento() ,
                                   "",
                                   getCodOperatoreAggiornamento() ,
                                   getDataAggiornamento() ,
                                   getCodUfficioAggiornamento() ,
                                   "",
                                   getFasSieIdFascicoloSiep() ,
                                   getFasSiuIdFascicoloSius() ,
								                   getPosGiuIdPosizioneGiuridica(),
                                   //modifica relativa al tipo istituto
                                   getAltroLuogo(),
                                   null

                                  );
  }

  public void setDAOFromModel(LuogoDetenzioneModel aModel) throws DAOException
  {
    setIdLuogoDetenzione( aModel.getIdLuogoDetenzione() );
 //modifica relativa al tipo istituto
    setIstDetIdIstitutoDetenzione( aModel.getIstDetIdIstitutoDetenzione());
    //setCodTipoIstituto( aModel.getCodTipoIstituto() );
    //setCodLuogo( aModel.getCodLuogo() );
    //setIndirizzo( aModel.getIndirizzo() );
    //setDescr( aModel.getDescr() );
    setNote( aModel.getNote() );
    setDataInizioDetenzione( aModel.getDataInizioDetenzione() );
    setDataFineDetenzione( aModel.getDataFineDetenzione() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
    setPosGiuIdPosizioneGiuridica( aModel.getPosGiuIdPosizioneGiuridica() );
  //modifica relativa al tipo istituto
    setAltroLuogo(aModel.getAltroLuogo());

  }

  public void setDAOFromModelForUpdate(LuogoDetenzioneModel aModel) throws DAOException
  {
    //setIdLuogoDetenzione( aModel.getIdLuogoDetenzione() );
    //modifica relativa al tipo istituto
    setIdLuogoDetenzione( aModel.getIdLuogoDetenzione() );
//    setCodTipoIstituto( aModel.getCodTipoIstituto() );
//    setCodLuogo( aModel.getCodLuogo() );
//    setIndirizzo( aModel.getIndirizzo() );
    //setDescr( aModel.getDescr() );
    setNote( aModel.getNote() );
    setDataInizioDetenzione( aModel.getDataInizioDetenzione() );
    setDataFineDetenzione( aModel.getDataFineDetenzione() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setFasSiuIdFascicoloSius( aModel.getFasSiuIdFascicoloSius() );
  	setPosGiuIdPosizioneGiuridica( aModel.getPosGiuIdPosizioneGiuridica() );
  //modifica relativa al tipo istituto
    setAltroLuogo(aModel.getAltroLuogo());
    setCondizioneUpdate(aModel.getIdLuogoDetenzione());
  }

  public void setDAOFromModelForUpdateDataFine(LuogoDetenzioneModel aModel) throws DAOException
{
  setDataFineDetenzione( aModel.getDataFineDetenzione() );
  setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
  setDataAggiornamento( aModel.getDataAggiornamento() );
  setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
  setCondizioneUpdate(aModel.getIdLuogoDetenzione());
}




  public void setCondizione(LuogoDetenzioneModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_LUOGO_DETENZIONE = " + key );
  }

  public void setCondizioneIdPosizioneGiuridica(BigDecimal key)
  {
    setCondition(" POS_GIU_ID_POSIZIONE_GIURIDICA = " + key );
  }
}
