package siap.sige.giudicepopolare.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sige.giudicepopolare.model.GiudicePopolareModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: GiudicePopolareDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella GiudicePopolare</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class GiudicePopolareDAO extends SIAPTableDAO
{
  public GiudicePopolareDAO (Connection con)
  {
    super(con);
    setTable("GIUDICE_POPOLARE");

    //Settare la Sequence e i campi chiave
    setSequenceField("ID_GIUDICE_POPOLARE", "GIU_POP_SEQ");
    setFieldKey("ID_GIUDICE_POPOLARE", BIG_DECIMAL);

    setField("ID_GIUDICE_POPOLARE", BIG_DECIMAL);
    setField("COGNOME", STRING);
    setField("NOME", STRING);
    setField("INDIRIZZO", STRING);
    setField("COD_UFFICIO_APPARTENENZA", STRING);
    setField("DATA_INIZIO_VALIDITA", DATE);
    setField("DATA_FINE_VALIDITA", DATE);
    setField("COD_RUOLO", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("CODICE_FISCALE", STRING);    
    setField("DATA_NASCITA", DATE);
    setField("COD_STATO_NASCITA", STRING);
    setField("COD_COMUNE_NASCITA", STRING);
    setField("COMUNE_ESTERO_NASCITA", STRING);
    setField("SEZ_ID_SEZIONE", BIG_DECIMAL);
    setField("COD_SESSO", STRING);
  }


  //
  // METODI GET()
  //
  public BigDecimal getIdGiudicePopolare()          throws DAOException	 { return getBigDecimal("ID_GIUDICE_POPOLARE"); }
  public String 	  getCognome() 	                  throws DAOException	 { return getString("COGNOME"); }
  public String 	  getNome() 		                  throws DAOException	 { return getString("NOME"); }
  public String 	  getIndirizzo()                  throws DAOException	 { return getString("INDIRIZZO"); }
  public String 	  getCodUfficioAppartenenza()     throws DAOException	 { return getString("COD_UFFICIO_APPARTENENZA"); }
  public Date 		  getDataInizioValidita()         throws DAOException	 { return getDate("DATA_INIZIO_VALIDITA"); }
  public Date 		  getDataFineValidita() 	        throws DAOException	 { return getDate("DATA_FINE_VALIDITA"); }
  public String 	  getCodRuolo()                   throws DAOException	 { return getString("COD_RUOLO"); }
  public String 	  getCodOperatoreInserimento()    throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 		  getDataInserimento() 		        throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 	  getCodUfficioInserimento() 	    throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String   	getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 		  getDataAggiornamento() 		      throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 	  getCodUfficioAggiornamento() 	  throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public String 	  getCodiceFiscale() 		          throws DAOException	 { return getString("CODICE_FISCALE"); }
  public Date       getDataNascita()                throws DAOException  { return getDate("DATA_NASCITA"); }
  public String     getCodStatoNascita()            throws DAOException  { return getString("COD_STATO_NASCITA"); }
  public String     getCodComuneNascita()           throws DAOException  { return getString("COD_COMUNE_NASCITA"); }
  public String     getComuneEsteroNascita()        throws DAOException  { return getString("COMUNE_ESTERO_NASCITA"); }
  public BigDecimal getSezIdSezione()               throws DAOException  { return getBigDecimal("SEZ_ID_SEZIONE"); }
  public String     getCodSesso()                   throws DAOException  { return getString("COD_SESSO"); }

  //
  // METODI SET()
  //
  public void setIdGiudicePopolare(BigDecimal aValore)      { setBigDecimal("ID_GIUDICE_POPOLARE", aValore); }
  public void setCognome(String aValore) 		                { setString("COGNOME", aValore); }
  public void setNome(String aValore) 		                  { setString("NOME", aValore); }
  public void setIndirizzo(String aValore) 		              { setString("INDIRIZZO", aValore); }
  public void setCodUfficioAppartenenza(String aValore)     { setString("COD_UFFICIO_APPARTENENZA", aValore); }
  public void setDataInizioValidita(Date aValore) 	        { setDate("DATA_INIZIO_VALIDITA", aValore); }
  public void setDataFineValidita(Date aValore) 	          { setDate("DATA_FINE_VALIDITA", aValore); }
  public void setCodRuolo(String aValore) 		              { setString("COD_RUOLO", aValore); }
  public void setCodOperatoreInserimento(String aValore) 	  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore) 		          { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore) 	    { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore) 	{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore) 		        { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore) 	  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setCodiceFiscale(String aValore) 		          { setString("CODICE_FISCALE", aValore); }
  public void setDataNascita(Date aValore)                  { setDate("DATA_NASCITA", aValore); }
  public void setCodStatoNascita(String aValore)            { setString("COD_STATO_NASCITA", aValore); }
  public void setCodComuneNascita(String aValore)           { setString("COD_COMUNE_NASCITA", aValore); }
  public void setComuneEsteroNascita(String aValore)        { setString("COMUNE_ESTERO_NASCITA", aValore); }
  public void setSezIdSezione(BigDecimal aValore)           { setBigDecimal("SEZ_ID_SEZIONE", aValore); }
  public void setCodSesso(String aValore)                   { setString("COD_SESSO", aValore); }

  public GenericModel getModel()
  throws DAOException
  {
    return new GiudicePopolareModel(
              getIdGiudicePopolare() ,
              getCognome() ,
              getNome() ,
              getIndirizzo() ,
              getCodUfficioAppartenenza() ,
              "",
              getDataInizioValidita() ,
              getDataFineValidita() ,
              getCodRuolo() ,
              "",
              getCodOperatoreInserimento() ,
              getDataInserimento() ,
              getCodUfficioInserimento() ,
              "",
              getCodOperatoreAggiornamento() ,
              getDataAggiornamento() ,
              getCodUfficioAggiornamento(),
              "",
              getCodiceFiscale(),
              getDataNascita(),
              getCodStatoNascita(),
              "",
              getCodComuneNascita(),
              "",
              getComuneEsteroNascita(),
              getSezIdSezione(),
              "",
              getCodSesso());
  }


  public void setDAOFromModel(GiudicePopolareModel aModel)
  throws DAOException
  {
    setIdGiudicePopolare( aModel.getIdGiudicePopolare() );
    setCognome( aModel.getCognome() );
    setNome( aModel.getNome() );
    setIndirizzo( aModel.getIndirizzo() );
    setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
    setDataInizioValidita( aModel.getDataInizioValidita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setCodRuolo( aModel.getCodRuolo() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodiceFiscale( aModel.getCodiceFiscale() );
    setDataNascita( aModel.getDataNascita() );
    setCodStatoNascita( aModel.getCodStatoNascita() );
    setCodComuneNascita( aModel.getCodComuneNascita() );
    setComuneEsteroNascita( aModel.getComuneEsteroNascita() );
    setSezIdSezione( aModel.getSezIdSezione());
    setCodSesso( aModel.getCodSesso() );
  }

  public void setDAOFromModelForUpdate(GiudicePopolareModel aModel)
  throws DAOException
  {
    setCognome( aModel.getCognome() );
    setNome( aModel.getNome() );
    setIndirizzo( aModel.getIndirizzo() );
    setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
    setDataInizioValidita( aModel.getDataInizioValidita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setCodRuolo( aModel.getCodRuolo() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCodiceFiscale( aModel.getCodiceFiscale() );
    setDataNascita( aModel.getDataNascita() );
    setCodStatoNascita( aModel.getCodStatoNascita() );
    setCodComuneNascita( aModel.getCodComuneNascita() );
    setComuneEsteroNascita( aModel.getComuneEsteroNascita() );
    setSezIdSezione( aModel.getSezIdSezione());
    setCodSesso( aModel.getCodSesso() );
    
    setCondizioneUpdate(aModel.getIdGiudicePopolare());
  }

  public void setCondizione(GiudicePopolareModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_GIUDICE_POPOLARE = " + key );
  }
}