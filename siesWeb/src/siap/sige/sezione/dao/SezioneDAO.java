package siap.sige.sezione.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sige.sezione.model.SezioneModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: SezioneDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Sezione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class SezioneDAO extends SIAPTableDAO
{
  public SezioneDAO (Connection con)
  {
    super(con);
    setTable("SEZIONE");

    //Settare la Sequence e i campi chiave
    setSequenceField("ID_SEZIONE", "SEZ_SEQ");
    setFieldKey("ID_SEZIONE", BIG_DECIMAL);

    setField("ID_SEZIONE", BIG_DECIMAL);
    setField("CODICE", STRING);
    setField("DESCRIZIONE", STRING);
    setField("COD_UFFICIO_APPARTENENZA", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
  }


  //
  // METODI GET()
  //

  public BigDecimal getIdSezione()                  throws DAOException	 { return getBigDecimal("ID_SEZIONE"); }
  public String 	  getCodice() 	                  throws DAOException	 { return getString("CODICE"); }
  public String 	  getDescrizione() 		            throws DAOException	 { return getString("DESCRIZIONE"); }
  public String 	  getCodUfficioAppartenenza()     throws DAOException	 { return getString("COD_UFFICIO_APPARTENENZA"); }
  public String 	  getCodOperatoreInserimento()    throws DAOException	 { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date 		  getDataInserimento() 		        throws DAOException	 { return getDate("DATA_INSERIMENTO"); }
  public String 	  getCodUfficioInserimento() 	    throws DAOException	 { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String   	getCodOperatoreAggiornamento() 	throws DAOException	 { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 		  getDataAggiornamento() 		      throws DAOException	 { return getDate("DATA_AGGIORNAMENTO"); }
  public String 	  getCodUfficioAggiornamento() 	  throws DAOException	 { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public String 	  getCodiceFiscale() 		          throws DAOException	 { return getString("CODICE_FISCALE"); }

  //
  // METODI SET()
  //

  public void setIdSezione(BigDecimal aValore)              { setBigDecimal("ID_SEZIONE", aValore); }
  public void setCodice(String aValore) 		                { setString("CODICE", aValore); }
  public void setDescrizione(String aValore) 		            { setString("DESCRIZIONE", aValore); }
  public void setCodUfficioAppartenenza(String aValore)     { setString("COD_UFFICIO_APPARTENENZA", aValore); }
  public void setCodOperatoreInserimento(String aValore) 	  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore) 		          { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore) 	    { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore) 	{ setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore) 		        { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore) 	  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setCodiceFiscale(String aValore) 		          { setString("CODICE_FISCALE", aValore); }

  public GenericModel getModel()
  throws DAOException
  {
    return new SezioneModel(
              getIdSezione() ,
              getCodice() ,
              getDescrizione() ,
              getCodUfficioAppartenenza() ,
              "",
              getCodOperatoreInserimento() ,
              getDataInserimento() ,
              getCodUfficioInserimento() ,
              "",
              getCodOperatoreAggiornamento() ,
              getDataAggiornamento() ,
              getCodUfficioAggiornamento(),
              "" );
  }

  public void setDAOFromModel(SezioneModel aModel)
  throws DAOException
  {
    setIdSezione( aModel.getIdSezione() );
    setCodice( aModel.getCodice() );
    setDescrizione( aModel.getDescrizione() );
    setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
  }

  public void setDAOFromModelForUpdate(SezioneModel aModel)
  throws DAOException
  {
    //setIdCuratore( aModel.getIdCuratore() );
    setCodice( aModel.getCodice() );
    setDescrizione( aModel.getDescrizione() );
    //setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCondizioneUpdate(aModel.getIdSezione());
  }

  public void setCondizione(SezioneModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_SEZIONE = " + key );
  }
}