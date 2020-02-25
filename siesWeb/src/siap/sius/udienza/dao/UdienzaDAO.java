package siap.sius.udienza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sius.udienza.model.UdienzaModel;
import f3b.dao.DAOException;
//import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: UdienzaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Udienza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class UdienzaDAO extends SIAPTableDAO
{
  /**
   * Costruttore con parametro Connection
   * <p> 
   * @param con Connection connessione al dbase
   */
  public UdienzaDAO (Connection con)
  {
    super(con);
    setTable("UDIENZA");
    //Settare la Sequence e i campi chiave

    setSequenceField("ID_UDIENZA", "UDI_SEQ");
    setField("ID_UDIENZA", BIG_DECIMAL);
    setField("DATA_UDIENZA", DATE);
    setField("COD_PRESIDENTE", STRING);
    setField("COD_GIUDICE_1", STRING);
    setField("COD_GIUDICE_2", STRING);
    setField("COD_PG", STRING);
    setField("COD_ID_ESPERTO_1", BIG_DECIMAL);
    setField("COD_ID_ESPERTO_2", BIG_DECIMAL);
    setField("COD_ID_ASSISTENTE", BIG_DECIMAL);
    //setField("FLAG_RINVIATA", STRING);
    setField("NUMERO_MAX_FASCICOLI", BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("LUOGO_UDIENZA", STRING);
    setField("COD_UFFICIO_APPARTENENZA", STRING);
    setField("NUM_COLLEGIO", BIG_DECIMAL);
    setField("ORA_INIZIO", STRING);
    setField("MIN_INIZIO", STRING);
    setField("ORA_FINE", STRING);
    setField("MIN_FINE", STRING);
    setField("ORA_FINE_CC", STRING);
    setField("MIN_FINE_CC", STRING);
  }


  //
  // METODI GET()
  //

  public BigDecimal   getIdUdienza() 		              throws DAOException	 { return getBigDecimal("ID_UDIENZA"); }
  public Date 	      getDataUdienza() 		            throws DAOException	 { return getDate("DATA_UDIENZA"); }
  public String       getCodPresidente() 	            throws DAOException	 { return getString("COD_PRESIDENTE"); }
  public String       getCodGiudice1() 		            throws DAOException	 { return getString("COD_GIUDICE_1"); }
  public String       getCodGiudice2() 		            throws DAOException	 { return getString("COD_GIUDICE_2"); }
  public String       getCodPg() 		                  throws DAOException	 { return getString("COD_PG"); }
  public BigDecimal   getCodIdEsperto1() 	            throws DAOException	 { return getBigDecimal("COD_ID_ESPERTO_1"); }
  public BigDecimal   getCodIdEsperto2() 	            throws DAOException	 { return getBigDecimal("COD_ID_ESPERTO_2"); }
  public BigDecimal   getCodIdAssistente() 	          throws DAOException	 { return getBigDecimal("COD_ID_ASSISTENTE"); }
  //public String       getFlagRinviata() 	throws DAOException	 { return getString("FLAG_RINVIATA"); }
  public BigDecimal   getNumeroMaxFascicoli() 	      throws DAOException	 { return getBigDecimal("NUMERO_MAX_FASCICOLI"); }
  public String       getCodOperatoreInserimento() 	  throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 	      getDataInserimento() 		        throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String       getCodUfficioInserimento() 	    throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String       getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 	      getDataAggiornamento() 		      throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String       getCodUfficioAggiornamento() 	  throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public String       getLuogoUdienza() 		          throws DAOException	 { return getString("LUOGO_UDIENZA"); }
  public String       getCodUfficioAppartenenza() 	  throws DAOException	 { return getString("COD_UFFICIO_APPARTENENZA"); }
  public BigDecimal   getNumCollegio() 		            throws DAOException	 { return getBigDecimal("NUM_COLLEGIO"); }
  public String       getOraInizio()                  throws DAOException { return getString("ORA_INIZIO");}
  public String       getMinInizio()                  throws DAOException { return getString("MIN_INIZIO");}
  public String       getOraFine()                    throws DAOException { return getString("ORA_FINE");}
  public String       getMinFine()                    throws DAOException { return getString("MIN_FINE");}
  public String       getOraFineCC()                  throws DAOException { return getString("ORA_FINE_CC");}
  public String       getMinFineCC()                  throws DAOException { return getString("MIN_FINE_CC");}

  
  //
  // METODI SET()
  //

  public void  	 setIdUdienza(BigDecimal aValore ) 			        { setBigDecimal("ID_UDIENZA", aValore); }
  public void  	 setDataUdienza(Date aValore ) 			            { setDate("DATA_UDIENZA", aValore); }
  public void  	 setCodPresidente(String aValore ) 			        { setString("COD_PRESIDENTE", aValore); }
  public void  	 setCodGiudice1(String aValore ) 			          { setString("COD_GIUDICE_1", aValore); }
  public void  	 setCodGiudice2(String aValore ) 			          { setString("COD_GIUDICE_2", aValore); }
  public void  	 setCodPg(String aValore ) 			                { setString("COD_PG", aValore); }
  public void  	 setCodIdEsperto1(BigDecimal aValore ) 			    { setBigDecimal("COD_ID_ESPERTO_1", aValore); }
  public void  	 setCodIdEsperto2(BigDecimal aValore ) 			    { setBigDecimal("COD_ID_ESPERTO_2", aValore); }
  public void  	 setCodIdAssistente(BigDecimal aValore ) 			  { setBigDecimal("COD_ID_ASSISTENTE", aValore); }
  //public void  	 setFlagRinviata(String aValore ) 			 { setString("FLAG_RINVIATA", aValore); }
  public void  	 setNumeroMaxFascicoli(BigDecimal aValore )     { setBigDecimal("NUMERO_MAX_FASCICOLI", aValore); }
  public void  	 setCodOperatoreInserimento(String aValore )    { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void  	 setDataInserimento(Date aValore ) 			        { setDate("DATA_INSERIMENTO", aValore); }
  public void  	 setCodUfficioInserimento(String aValore ) 		  { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void  	 setCodOperatoreAggiornamento(String aValore )  { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void  	 setDataAggiornamento(Date aValore ) 			      { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void  	 setCodUfficioAggiornamento(String aValore ) 	  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void  	 setLuogoUdienza(String aValore ) 			        { setString("LUOGO_UDIENZA", aValore); }
  public void  	 setCodUfficioAppartenenza(String aValore ) 	  { setString("COD_UFFICIO_APPARTENENZA", aValore); }
  public void  	 setNumCollegio(BigDecimal aValore ) 			      { setBigDecimal("NUM_COLLEGIO", aValore); }
  public void    setOraInizio(String aValore )                  { setString("ORA_INIZIO", aValore); }
  public void    setMinInizio(String aValore )                  { setString("MIN_INIZIO", aValore); }
  public void    setOraFine(String aValore )                    { setString("ORA_FINE", aValore); }
  public void    setMinFine(String aValore )                    { setString("MIN_FINE", aValore); }
  public void    setOraFineCC(String aValore )                  { setString("ORA_FINE_CC", aValore); }
  public void    setMinFineCC(String aValore )                  { setString("MIN_FINE_CC", aValore); }



  public GenericModel getModel() throws DAOException
  {
    return new UdienzaModel(
             getIdUdienza() ,
             getDataUdienza() ,
             getCodPresidente() ,
             "",
             getCodGiudice1() ,
             "",
             getCodGiudice2() ,
             "",
             getCodPg() ,
             "",
             getCodIdEsperto1() ,
             "",
             getCodIdEsperto2() ,
             "",
             getCodIdAssistente() ,
             "",
             //getFlagRinviata() ,
             getNumeroMaxFascicoli() ,
             getCodOperatoreInserimento() ,
             getDataInserimento() ,
             getCodUfficioInserimento() ,
             "",
             getCodOperatoreAggiornamento() ,
             getDataAggiornamento() ,
             getCodUfficioAggiornamento(),
             "",
             getLuogoUdienza() ,
             getCodUfficioAppartenenza(),
             "",
             getNumCollegio(),
             getOraInizio(),
             getMinInizio(),
             getOraFine(),
             getMinFine(),
             getOraFineCC(),
             getMinFineCC()
             );
  }


  public void setDAOFromModel(UdienzaModel aModel) throws DAOException
  {
     setIdUdienza( aModel.getIdUdienza() );
     setDataUdienza( aModel.getDataUdienza() );
     setCodPresidente( aModel.getCodPresidente() );
     setCodGiudice1( aModel.getCodGiudice1() );
     setCodGiudice2( aModel.getCodGiudice2() );
     setCodPg( aModel.getCodPg() );
     setCodIdEsperto1( aModel.getCodIdEsperto1() );
     setCodIdEsperto2( aModel.getCodIdEsperto2() );
     setCodIdAssistente( aModel.getCodIdAssistente() );
     //setFlagRinviata( aModel.getFlagRinviata() );
     setNumeroMaxFascicoli( aModel.getNumeroMaxFascicoli() );
     setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
     setDataInserimento( aModel.getDataInserimento() );
     setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setLuogoUdienza( aModel.getLuogoUdienza() );
     setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
     setNumCollegio( aModel.getNumCollegio() );
     
     setOraInizio( aModel.getOraInizio() );
     setMinInizio( aModel.getMinInizio() );
     setOraFine( aModel.getOraFine() );
     setMinFine( aModel.getMinFine() );
     setOraFineCC( aModel.getOraFineCC() );
     setMinFineCC( aModel.getMinFineCC() );

  }

  public void 	 setDAOFromModelUDS(UdienzaModel aModel) throws DAOException
  {
     setIdUdienza( aModel.getIdUdienza() );
     setDataUdienza( aModel.getDataUdienza() );
     setCodPresidente( aModel.getCodPresidente() );
  //  setCodGiudice1( aModel.getCodGiudice1() );
  //   setCodGiudice2( aModel.getCodGiudice2() );
     setCodPg( aModel.getCodPg() );
  //   setCodIdEsperto1( aModel.getCodIdEsperto1() );
  //   setCodIdEsperto2( aModel.getCodIdEsperto2() );
     setCodIdAssistente( aModel.getCodIdAssistente() );
     //setFlagRinviata( aModel.getFlagRinviata() );
     setNumeroMaxFascicoli( aModel.getNumeroMaxFascicoli() );
     setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
     setDataInserimento( aModel.getDataInserimento() );
     setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setLuogoUdienza( aModel.getLuogoUdienza() );
     setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
     setNumCollegio( aModel.getNumCollegio() );
     
     setOraInizio( aModel.getOraInizio() );
     setMinInizio( aModel.getMinInizio() );
     setOraFine( aModel.getOraFine() );
     setMinFine( aModel.getMinFine() );
     
  }


  public void setDAOFromModelForUpdate(UdienzaModel aModel) throws DAOException
  {
     setIdUdienza( aModel.getIdUdienza() );
    //setDataUdienza( aModel.getDataUdienza() ); non modificabile
     setCodPresidente( aModel.getCodPresidente() );
     setCodGiudice1( aModel.getCodGiudice1() );
     setCodGiudice2( aModel.getCodGiudice2() );
     setCodPg( aModel.getCodPg() );
     setCodIdEsperto1( aModel.getCodIdEsperto1() );
     setCodIdEsperto2( aModel.getCodIdEsperto2() );
     setCodIdAssistente( aModel.getCodIdAssistente() );
     //setFlagRinviata( aModel.getFlagRinviata() );
     setNumeroMaxFascicoli( aModel.getNumeroMaxFascicoli() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setCondizioneUpdate(aModel.getIdUdienza());
     setLuogoUdienza( aModel.getLuogoUdienza() );
     //setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
     setNumCollegio( aModel.getNumCollegio() );
     
     setOraInizio( aModel.getOraInizio() );
     setMinInizio( aModel.getMinInizio() );
     setOraFine( aModel.getOraFine() );
     setMinFine( aModel.getMinFine() );
     setOraFineCC( aModel.getOraFineCC() );
     setMinFineCC( aModel.getMinFineCC() );
  }


  public void 	 setDAOFromModelForUpdateUDS(UdienzaModel aModel) throws DAOException
  {
     setIdUdienza( aModel.getIdUdienza() );
    //setDataUdienza( aModel.getDataUdienza() ); non modificabile
     setCodPresidente( aModel.getCodPresidente() );
  //   setCodGiudice1( aModel.getCodGiudice1() );
  //   setCodGiudice2( aModel.getCodGiudice2() );
     setCodPg( aModel.getCodPg() );
  //   setCodIdEsperto1( aModel.getCodIdEsperto1() );
  //   setCodIdEsperto2( aModel.getCodIdEsperto2() );
     setCodIdAssistente( aModel.getCodIdAssistente() );
     //setFlagRinviata( aModel.getFlagRinviata() );
     setNumeroMaxFascicoli( aModel.getNumeroMaxFascicoli() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setCondizioneUpdate(aModel.getIdUdienza());
     setLuogoUdienza( aModel.getLuogoUdienza() );
     //setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
     setNumCollegio( aModel.getNumCollegio() );
     
     setOraInizio( aModel.getOraInizio() );
     setMinInizio( aModel.getMinInizio() );
     setOraFine( aModel.getOraFine() );
     setMinFine( aModel.getMinFine() );

  }


  public void setCondizione(UdienzaModel aModel)
    {
      String lCondizioni = new String();
      boolean lInserito = false;
      if ( lInserito ) setCondition(lCondizioni);
    }


  public void setCondizioneUpdate(BigDecimal key)
  {
      setCondition(" ID_UDIENZA = " + key );
  }

}