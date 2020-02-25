package siap.sico.utente.dao;

import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sico.utente.model.UtenteModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

public class UtenteDAO extends SIAPTableDAO
{
  public UtenteDAO (Connection con)
  {
    super(con);

    setTable("UTENTE");

    setFieldKey("COD_UTENTE", STRING);
    setField("COGNOME", STRING);
    setField("NOME", STRING);
    setField("PWD", STRING);
    setField("TELEFONO", STRING);
    setField("FAX", STRING);
    setField("E_MAIL", STRING);
    setField("DATA_FINE_VALIDITA", DATE);
    setField("DATA_ORA_CONNESSIONE", DATE);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("DATA_ULTIMA_MODIFICA_PWD", DATE);
    setField("IP",STRING);
    setField("USERID_NSC",STRING);
    setField("PWD_NSC",STRING);
  }

  //
  // METODI GET()
  //

  public String getCodUtente() throws DAOException                 { return getString("COD_UTENTE"); }
  public String getCognome()  throws DAOException                  { return getString("COGNOME"); }
  public String getNome() throws DAOException                      { return getString("NOME"); }
  public String getPwd() throws DAOException                       { return getString("PWD"); }
  public String getTelefono() throws DAOException                  { return getString("TELEFONO"); }
  public String getFax() throws DAOException                       { return getString("FAX"); }
  public String getEmail() throws DAOException                     { return getString("E_MAIL"); }
  public Date 	getDataFineValidita() throws DAOException          { return getDate("DATA_FINE_VALIDITA"); }
  public Date   getDataOraConnessione() throws DAOException        { return getDate("DATA_ORA_CONNESSIONE"); }
  public String getCodOperatoreInserimento() throws DAOException   { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date   getDataInserimento()  throws DAOException          { return getDate("DATA_INSERIMENTO"); }
  public String getCodOperatoreAggiornamento() throws DAOException { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date 	getDataAggiornamento()  throws DAOException        { return getDate("DATA_AGGIORNAMENTO"); }
  public Date 	getDataUltimaModificaPwd() throws DAOException     { return getDate("DATA_ULTIMA_MODIFICA_PWD"); }
  public String getIP()  throws DAOException                       { return getString("IP"); }
  public String getUseridNSC() throws DAOException                 { return getString("USERID_NSC"); }
  public String getPwdNSC() throws DAOException                    { return getString("PWD_NSC"); }
  //
  // METODI SET()
  //

  public void setCodUtente(String aValore )                        { setString("COD_UTENTE", aValore); }
  public void setCognome(String aValore )                          { setString("COGNOME", aValore); }
  public void setNome(String aValore ) 		                       { setString("NOME", aValore); }
  public void setPwd(String aValore ) 	                           { setString("PWD", aValore); }
  public void setTelefono(String aValore )                         { setString("TELEFONO", aValore); }
  public void setFax(String aValore ) 	                           { setString("FAX", aValore); }
  public void setEmail(String aValore ) 	                       { setString("E_MAIL", aValore); }
  public void setDataFineValidita(Date aValore )                   { setDate("DATA_FINE_VALIDITA", aValore); }
  public void setDataOraConnessione(Date aValore )                 { setDate("DATA_ORA_CONNESSIONE", aValore); }
  public void setCodOperatoreInserimento(String aValore )          { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore ) 	               { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore )        { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore ) 	               { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setDataUltimaModificaPwd(Date aValore )              { setDate("DATA_ULTIMA_MODIFICA_PWD", aValore); }
  public void setIP(String aValore )                               { setString("IP", aValore); }
  public void setUseridNSC(String aValore )                        { setString("USERID_NSC", aValore); }
  public void setPwdNSC(String aValore )                           { setString("PWD_NSC", aValore); }
  
  public GenericModel  	 getModel() throws DAOException
  {
    return new UtenteModel( getCodUtente() ,
                            getCognome() ,
                            getNome() ,
                            getPwd() ,
                            getTelefono(),
                            getFax(),
                            getEmail(),
                            getDataFineValidita() ,
                            getDataOraConnessione() ,
                            getCodOperatoreInserimento() ,
                            getDataInserimento() ,
                            getCodOperatoreAggiornamento() ,
                            getDataAggiornamento() ,
                            getDataUltimaModificaPwd(),
                            getIP(),
                            getUseridNSC(),
                            getPwdNSC()
                           );
  }

  public void selUtenteAttivoByCodice(String aCodUtente)
  {
    String lStrSysdate = DateUtils.getSysDate("dd/MM/yyyy");

    String lCondizione = "";
    lCondizione += "(COD_UTENTE = '" + aCodUtente + "')";
    lCondizione += " AND ( (DATA_FINE_VALIDITA is null)";
    lCondizione +=        " OR ( TO_DATE('" + lStrSysdate + "', 'DD/MM/YYYY') <= DATA_FINE_VALIDITA) )";

    setCondition( lCondizione );
  }

  public void 	 setDAOFromModel(UtenteModel aModel) throws DAOException
  		{
				 setCodUtente( aModel.getUserId() );
				 setCognome( aModel.getCognome() );
				 setNome( aModel.getNome() );
				 setPwd( aModel.getPwd() );
				 setTelefono( aModel.getTelefono() );
				 setFax( aModel.getFax() );
				 setEmail( aModel.getEmail() );
				 setDataFineValidita( aModel.getDataFineValidita() );
				 setDataOraConnessione( aModel.getDataOraConnessione() );
				 setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
				 setDataInserimento( aModel.getDataInserimento() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setDataUltimaModificaPwd( aModel.getDataUltimaModifcaPwd() );
		}
 
  public void 	 setDAOFromModelForUpdate(UtenteModel aModel) throws DAOException
  		{
				 setCodUtente( aModel.getUserId() );
				 setCognome( aModel.getCognome() );
				 setNome( aModel.getNome() );
				 setPwd( aModel.getPwd() );
				 setTelefono( aModel.getTelefono() );
				 setFax( aModel.getFax() );
				 setEmail( aModel.getEmail() );
				 setDataFineValidita( aModel.getDataFineValidita() );
				 setDataOraConnessione( aModel.getDataOraConnessione() );
				 setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
				 setDataAggiornamento( aModel.getDataAggiornamento() );
				 setDataUltimaModificaPwd( aModel.getDataUltimaModifcaPwd() );
				 setCondizioneUpdate(aModel.getUserId());
		}

 public void setCondizioneUpdate(String key)
 			 {
	 setCondition(" COD_UTENTE = '" + key +"'");
		 }

}
