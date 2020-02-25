package siap.sige.curatore.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sige.curatore.model.CuratoreModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: CuratoreDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Curatore</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class CuratoreDAO extends SIAPTableDAO
{
  public CuratoreDAO (Connection con)
  {
    super(con);
    setTable("CURATORE");

    //Settare la Sequence e i campi chiave
    setSequenceField("ID_CURATORE", "CUR_SEQ");
    setFieldKey("ID_CURATORE", BIG_DECIMAL);

    setField("ID_CURATORE", BIG_DECIMAL);
    setField("COGNOME", STRING);
    setField("NOME", STRING);
    setField("INDIRIZZO", STRING);
    setField("TELEFONO", STRING);
    setField("COD_UFFICIO_APPARTENENZA", STRING);
    setField("EMAIL", STRING);
    setField("FAX", STRING);
    setField("CELLULARE", STRING);
    setField("DATA_INIZIO_VALIDITA", DATE);
    setField("DATA_FINE_VALIDITA", DATE);
    setField("FLAG_STATO", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("CODICE_FISCALE", STRING);
  }


  //
  // METODI GET()
  //

  public BigDecimal getIdCuratore()                 throws DAOException	 { return getBigDecimal("ID_CURATORE"); }
  public String 	  getCognome() 	                  throws DAOException	 { return getString("COGNOME"); }
  public String 	  getNome() 		                  throws DAOException	 { return getString("NOME"); }
  public String 	  getIndirizzo()                  throws DAOException	 { return getString("INDIRIZZO"); }
  public String 	  getTelefono() 		              throws DAOException	 { return getString("TELEFONO"); }
  public String 	  getCodUfficioAppartenenza()     throws DAOException	 { return getString("COD_UFFICIO_APPARTENENZA"); }
  public String 	  getEmail()                      throws DAOException	 { return getString("EMAIL"); }
  public String 	  getFax() 		                    throws DAOException	 { return getString("FAX"); }
  public String 	  getCellulare() 		              throws DAOException	 { return getString("CELLULARE"); }
  public Date 		  getDataInizioValidita()         throws DAOException	 { return getDate("DATA_INIZIO_VALIDITA"); }
  public Date 		  getDataFineValidita() 	        throws DAOException	 { return getDate("DATA_FINE_VALIDITA"); }
  public String 	  getFlagStato()                  throws DAOException	 { return getString("FLAG_STATO"); }
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

  public void setIdCuratore(BigDecimal aValore)              { setBigDecimal("ID_CURATORE", aValore); }
  public void setCognome(String aValore) 		                { setString("COGNOME", aValore); }
  public void setNome(String aValore) 		                  { setString("NOME", aValore); }
  public void setIndirizzo(String aValore) 		              { setString("INDIRIZZO", aValore); }
  public void setTelefono(String aValore) 		              { setString("TELEFONO", aValore); }
  public void setCodUfficioAppartenenza(String aValore)     { setString("COD_UFFICIO_APPARTENENZA", aValore); }
  public void setEmail(String aValore) 		                  { setString("EMAIL", aValore); }
  public void setFax(String aValore) 		                    { setString("FAX", aValore); }
  public void setCellulare(String aValore) 		              { setString("CELLULARE", aValore); }
  public void setDataInizioValidita(Date aValore) 	        { setDate("DATA_INIZIO_VALIDITA", aValore); }
  public void setDataFineValidita(Date aValore) 	          { setDate("DATA_FINE_VALIDITA", aValore); }
  public void setFlagStato(String aValore) 		              { setString("FLAG_STATO", aValore); }
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
    return new CuratoreModel(
              getIdCuratore() ,
              getCognome() ,
              getNome() ,
              getIndirizzo() ,
              getTelefono() ,
              getCodUfficioAppartenenza() ,
              "",
              getEmail() ,
              getFax() ,
              getCellulare() ,
              getDataInizioValidita() ,
              getDataFineValidita() ,
              getFlagStato() ,
              getCodOperatoreInserimento() ,
              "",
              getDataInserimento() ,
              getCodUfficioInserimento() ,
              "",
              getCodOperatoreAggiornamento() ,
              getDataAggiornamento() ,
              getCodUfficioAggiornamento(),
              "",
              getCodiceFiscale());
  }

  public void setDAOFromModel(CuratoreModel aModel)
  throws DAOException
  {
    setIdCuratore( aModel.getIdCuratore() );
    setCognome( aModel.getCognome() );
    setNome( aModel.getNome() );
    setIndirizzo( aModel.getIndirizzo() );
    setTelefono( aModel.getTelefono() );
    setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
    setEmail( aModel.getEmail() );
    setFax( aModel.getFax() );
    setCellulare( aModel.getCellulare() );
    setDataInizioValidita( aModel.getDataInizioValidita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setFlagStato( aModel.getFlagStato() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCodiceFiscale( aModel.getCodiceFiscale() );
  }

  public void setDAOFromModelForUpdate(CuratoreModel aModel)
  throws DAOException
  {
    //setIdCuratore( aModel.getIdCuratore() );
    setCognome( aModel.getCognome() );
    setNome( aModel.getNome() );
    setIndirizzo( aModel.getIndirizzo() );
    setTelefono( aModel.getTelefono() );
    //setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
    setEmail( aModel.getEmail() );
    setFax( aModel.getFax() );
    setCellulare( aModel.getCellulare() );
    setDataInizioValidita( aModel.getDataInizioValidita() );
    setDataFineValidita( aModel.getDataFineValidita() );
    setFlagStato( aModel.getFlagStato() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setCodiceFiscale( aModel.getCodiceFiscale() );
    setCondizioneUpdate(aModel.getIdCuratore());
  }

  public void setCondizione(CuratoreModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }

  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_CURATORE = " + key );
  }
}