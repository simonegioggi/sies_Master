package siap.sico.utente.dao;

import java.sql.Connection;

import siap.sico.utente.model.UtenteModel;
import siap.sico.utente.model.UtenteViewModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;

public class UfficioUtenteDao extends SqlDAO
{
  public UfficioUtenteDao (Connection con)
  {
    super(con);
  }

  /**
   * 
   * @param codUfficio
   * @param cognome
   * @param nome
   * @param codUt
   */
  public void ricercaUtentiPerUfficio(String codUfficio, String aDistretto, String cognome, String nome, String codUt)
  {
    String appo = getSqlQuery();

    if (!codUfficio.equals(""))
    {
      appo += " AND UTENTE_UFFICIO.UFF_COD_UFFICIO = '"+ codUfficio +"'";
    }
    else if (aDistretto!=null && !aDistretto.equals("")){
      appo += " AND UFFICIO.COD_DISTRETTO = '"+ aDistretto +"'";
    }

    if (!cognome.equals(""))
    {
      cognome = cognome.toUpperCase();
      appo += " AND upper(COGNOME) = '"+ StringUtils.convertSqlString(cognome) +"'";
    }

    if (!nome.equals(""))
    {
      nome = nome.toUpperCase();
      appo += " AND upper(NOME) = '"+ StringUtils.convertSqlString(nome) +"'";
    }

    if (!codUt.equals(""))
    {
      appo += " AND COD_UTENTE = '"+ StringUtils.convertSqlString(codUt) +"'";
    }

    appo += " ORDER BY COGNOME, NOME ";

    this.setStatement(appo);
  }

  /**
   * Ricerca uno specifico utente su un determinato ufficio. Tutti i campi sono
   * obbligatori.
   * La ricerca è non case sensitive.
   * 
   * @param codUfficio
   * @param cognome
   * @param nome
   */
  public void ricercaUtentePerUfficioCognomeNome(String codUfficio, String cognome, String nome)
  {
    if (cognome!=null){
      cognome = cognome.toUpperCase();
    }
    
    if (nome!=null){
      nome = nome.toUpperCase();
    }
    
    String appo2 = getSqlQueryFromView() + " COD_UFFICIO = '"+ codUfficio +"' AND upper(COGNOME) = '"+ StringUtils.convertSqlString(cognome) +"' AND upper(NOME) = '"+ StringUtils.convertSqlString(nome) +"'";

    this.setStatement(appo2);
  }

  
  /**
   * 
   * @return
   */
  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "UTENTE.COD_UTENTE COD_UTENTE, "+
                  "UTENTE.COGNOME, "+
                  "UTENTE.NOME, "+
                  "UTENTE.PWD, "+
                  "UTENTE.TELEFONO, "+
                  "UTENTE.FAX, "+
                  "UTENTE.E_MAIL, "+
                  "UTENTE.DATA_FINE_VALIDITA DATA_FINE_VALIDITA, "+
                  "UTENTE.DATA_ORA_CONNESSIONE DATA_ORA_CONNESSIONE, "+
                  "UTENTE.COD_OPERATORE_INSERIMENTO COD_OPERATORE_INSERIMENTO, "+
                  "UTENTE.DATA_INSERIMENTO DATA_INSERIMENTO, "+
                  "UTENTE.COD_OPERATORE_AGGIORNAMENTO COD_OPERATORE_AGGIORNAMENTO, "+
                  "UTENTE.DATA_AGGIORNAMENTO DATA_AGGIORNAMENTO, "+
                  "UTENTE.DATA_ULTIMA_MODIFICA_PWD DATA_ULTIMA_MODIFICA_PWD, "+
                  "UTENTE.IP ";
    lStatement += " FROM UTENTE, UTENTE_UFFICIO, UFFICIO ";
    lStatement += " WHERE UTENTE.COD_UTENTE = UTENTE_UFFICIO.UTE_COD_UTENTE ";
    lStatement += "   AND UTENTE_UFFICIO.UFF_COD_UFFICIO = UFFICIO.COD_UFFICIO ";

    return lStatement;
  }

  /**
   * 
   * @return
   */
  protected String getSqlQueryFromView()
  {
     String lStatement = new String("");

     lStatement += " SELECT "+
                   "COD_UFFICIO, "+
                   "COD_UTENTE, "+
                   "NOME, "+
                   "COGNOME, "+
                   "UTENTE_TEL, "+
                   "UTENTE_FAX, "+
                   "UTENTE_E_MAIL, "+
                   "DESCR_COMUNE, "+
                   "DESCR_TIPO_UFFICIO, "+
                   "DESCRIZIONE, "+
                   "DATA_FINE_VALIDITA, "+
                   "DATA_ORA_CONNESSIONE, "+
                   "DATA_INSERIMENTO, "+
                   "DATA_ULTIMA_MODIFICA_PWD, " +
                   "IP ";
     lStatement += " FROM V_UTENTI_COMPLETA ";
     lStatement += " WHERE ";

     return lStatement;
  }

  public GenericModel getModel() throws DAOException
  {
    UtenteModel aModel = new  UtenteModel();

//Inserire le opportune set delle descrizioni!

    aModel.setUserId(getString("COD_UTENTE") );
    aModel.setCognome(getString("COGNOME") );
    aModel.setNome(getString("NOME") );
    aModel.setTelefono(getString("TELEFONO") );
    aModel.setFax(getString("FAX") );
    aModel.setEmail(getString("E_MAIL") );
    aModel.setDataFineValidita(getDate("DATA_FINE_VALIDITA") );
    aModel.setDataOraConnessione(getDate("DATA_ORA_CONNESSIONE") );
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    aModel.setDataUltimaModifcaPwd(getDate("DATA_ULTIMA_MODIFICA_PWD") );
    aModel.setIP(getString("IP"));

    return aModel;
  }

  public UtenteViewModel getModelFromView() throws DAOException
  {
    UtenteViewModel lUt=new UtenteViewModel();

    lUt.setUserId(getString("COD_UTENTE"));
    lUt.setNome(getString("NOME"));
    lUt.setCognome(getString("COGNOME") );
    lUt.setUtTelefono(getString("UTENTE_TEL") );
    lUt.setUtFax(getString("UTENTE_FAX") );
    lUt.setUtEmail(getString("UTENTE_E_MAIL") );
    lUt.setComuneUfficio(getString("DESCR_COMUNE"));
    lUt.setDescTipoUfficio(getString("DESCR_TIPO_UFFICIO"));
    lUt.setDescProfilo(getString("DESCRIZIONE"));
    lUt.setDataFineValidita(getDate("DATA_FINE_VALIDITA") );
    lUt.setDataOraConnessione(getDate("DATA_ORA_CONNESSIONE") );
    lUt.setDataInserimento(getDate("DATA_INSERIMENTO") );
    lUt.setDataUltimaModifcaPwd(getDate("DATA_ULTIMA_MODIFICA_PWD") );
    lUt.setIP(getString("IP"));

    return lUt;
  }
  
  /**
   * Metodo per la costruzione della select di ricerca degli utenti
   * attivi per l'ufficio passato
   * 
   * <p>
   * @param codUfficio String codice ufficio 
   * @return String select
   */
  public void ricercaUtentiAttiviPerUfficio(String codUfficio, String aCognome)
  {
    String appo = getSqlQuery();
    appo += " AND (UTENTE.DATA_FINE_VALIDITA>=TO_DATE('"+DateUtils.getDateToString(DateUtils.getSysDate(),"dd/MM/yyyy HH:mm:ss")+"','DD/MM/YYYY HH24:MI:SS')  OR UTENTE.DATA_FINE_VALIDITA IS NULL)";

    if (!codUfficio.equals(""))
    {
      appo += " AND UTENTE_UFFICIO.UFF_COD_UFFICIO = '"+ codUfficio +"'";
    }
    
    if(!aCognome.equals("")){
    	appo+= " AND upper(UTENTE.COGNOME) LIKE '%"+ StringUtils.convertSqlString(aCognome.toUpperCase()) +"%' ";    	
    }

    appo += " ORDER BY COGNOME, NOME ";

    this.setStatement(appo);
  }
  
}