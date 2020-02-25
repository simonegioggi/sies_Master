package siap.sico.log.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sico.log.model.LogAttivitaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: LogAttivitaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella LogAttivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class LogAttivitaDAO extends TableDAO
{
  public LogAttivitaDAO (Connection con)
  {
       super(con);
       setTable("LOG_ATTIVITA");

       //Settare la Sequence e i campi chiave

       setField("RECORD", STRING);
       setField("COD_OPERATORE", STRING);
       setField("DATA", DATE);
       setField("IP_UTENTE", STRING);
       setField("AZIONE_CONTESTO_JAVA", STRING);
  }


  //
  // METODI GET()
  //

  public String    getRecord()             throws DAOException  { return getString("RECORD"); }
  public String    getCodOperatore()       throws DAOException  { return getString("COD_OPERATORE"); }
  public Date      getData()               throws DAOException  { return getDate  ("DATA"); }
  public String    getIpUtente()           throws DAOException  { return getString("IP_UTENTE"); }
  public String    getAzioneContestoJava() throws DAOException  { return getString("AZIONE_CONTESTO_JAVA"); }


  //
  // METODI SET()
  //
  public void    setRecord(String aValore )             { setString("RECORD", aValore); }
  public void    setCodOperatore(String aValore )       { setString("COD_OPERATORE", aValore); }
  public void    setData(Date aValore )                 { setDate  ("DATA", aValore); }
  public void    setIpUtente(String aValore )           { setString("IP_UTENTE", aValore); }
  public void    setAzioneContestoJava(String aValore ) { setString("AZIONE_CONTESTO_JAVA", aValore); }

  /**
   * 
   */
  public GenericModel getModel() throws DAOException
  {
    return new LogAttivitaModel(getRecord() ,
                                getCodOperatore() ,
                                getData() ,
                                getIpUtente() ,
                                getAzioneContestoJava(),
                                "",
                                ""
                               );
  }


  /**
   * 
   * @param aModel
   * @throws DAOException
   */
  public void   setDAOFromModel(LogAttivitaModel aModel) throws DAOException
  {
     setRecord             ( aModel.getRecord() );
     setCodOperatore       ( aModel.getCodOperatore() );
     setData               ( aModel.getData() );
     setIpUtente           ( aModel.getIpUtente() );
     setAzioneContestoJava ( aModel.getAzioneContestoJava() );
  }

  /**
   * 
   * @param aModel
   * @throws DAOException
   */
  public void   setDAOFromModelForUpdate(LogAttivitaModel aModel) throws DAOException
  {
    setRecord             ( aModel.getRecord() );
    setCodOperatore       ( aModel.getCodOperatore() );
    setData               ( aModel.getData() );
    setIpUtente           ( aModel.getIpUtente() );
    setAzioneContestoJava ( aModel.getAzioneContestoJava() );
    //setCondizioneUpdate(aModel.getIdLogAttivita());
  }

  /**
   * 
   * @param aModel
   */
  public void setCondizione(LogAttivitaModel aModel)
  {
     String lCondizioni = new String();

     boolean lInserito = false;
     if ( lInserito ) setCondition(lCondizioni);
  }

  /**
   * 
   * @param id del record
   */
  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_LOG_ATTIVITA = " + key );
  }

}
