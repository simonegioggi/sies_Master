package siap.siep.certificatostatoesecuzione.dao;

/**
* <p>Title: CertificatoStatoEsecSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella CertificatoStatoEsec</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;

import siap.siep.certificatostatoesecuzione.model.CertificatoStatoEsecModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class CertificatoStatoEsecSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public CertificatoStatoEsecSqlDAO (Connection con) {
    super(con);
  }


  /***************************************************************************** 
   * Restituisce il numero di record dell'operazione di ricerca costruendo 
   * la clausola where con lo stesso model utilizzato per la ricerca 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void getCountCertificatoStatoEsec(CertificatoStatoEsecModel aModel) throws DAOException {
    // Costruisce lo statement da eseguire 
    String lStatement ="SELECT COUNT(*) HowManyRecords FROM CERTIFICATO_STATO_ESEC ";

    // Recupero la where condition in base al model 
    String lCondizioni = this.setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lStatement+=" WHERE " + lCondizioni;

    // Imposta lo statement da eseguire 
    setStatement(lStatement);
  }

  /***************************************************************************** 
   * Effettua la ricerca e restituisce solo i risultati nel range di record che 
   * vanno inseriti nella pagfina passata in input 
   * @param aModel 
   * @param aPage 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaCertificatoStatoEsecPaged(CertificatoStatoEsecModel aModel, int aPage) throws DAOException { 
    String lStatement = new String(""); 

    lStatement += getSqlQuery(); 

    // Recupero la where condition in base al model 
    String lCondizioni = this.setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lStatement+=" WHERE " + lCondizioni;

    lStatement += " "+getOrderBy()+" "; 

    String lPaginedStatement = ""; 
    lPaginedStatement = "SELECT * FROM (SELECT INNER.* , Rownum rn FROM (" + lStatement + "  ) INNER ) WHERE rn between  " + ( (aPage - 1) * IWebConstants.RESULT_PER_PAGE + 1) + 
          " AND " + (aPage) * IWebConstants.RESULT_PER_PAGE; 

    setStatement(lPaginedStatement); 
  } 


  /***************************************************************************** 
   * Effettua la generica ricerca in base ai dati specificati nel model 
   * @param aModel 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaCertificatoStatoEsec( CertificatoStatoEsecModel  aModel)  throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Recupero la where condition in base al model 
    String lCondizioni = setCondizioni(aModel);

    if (!lCondizioni.trim().equals("")) 
      lSql+=" WHERE " + lCondizioni;

    lSql += " "+getOrderBy()+" "; 

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }


  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaCertificatoStatoEsecByKey( BigDecimal aIdCertificatoStatoEsec) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " WHERE " + setCondizioniByKey( aIdCertificatoStatoEsec);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }


  /***************************************************************************** 
   * Metodo per la costruzione della sql query 
   * @return 
   ****************************************************************************/ 
  protected String getSqlQuery() {
    String lStatement = new String("");

    lStatement += " SELECT " +
                  "ID_CERTIFICATO_STATO_ESEC, "+  
                  "ANNOTAZIONI, "+  
                  "FLAG_UPLOAD, "+  
                  "COD_OPERATORE_INSERIMENTO, "+  
                  "DATA_INSERIMENTO, "+  
                  "COD_UFFICIO_INSERIMENTO, "+  
                  "COD_OPERATORE_AGGIORNAMENTO, "+  
                  "DATA_AGGIORNAMENTO, "+  
                  "COD_UFFICIO_AGGIORNAMENTO, "+  
                  "FAS_SIE_ID_FASCICOLO_SIEP "; 
    // aggiungere qui gli eventuali campi descrizioni 

    // Aggiungo la from condition (completarla con eventuali altre tabelle per recuperare le descrizioni)
    lStatement += " FROM CERTIFICATO_STATO_ESEC";

    /*
    lStatement += " (     nvl(CERTIFICATO_STATO_ESEC.COD_OPERATORE_INSERIMENTO,'-') = CODOPERATOREINSERIMENTO.RV_LOW_VALUE AND CODOPERATOREINSERIMENTO.RV_DOMAIN = 'OPERATORE_INSERIMENTO' ) "; 
    lStatement += " (     nvl(CERTIFICATO_STATO_ESEC.COD_UFFICIO_INSERIMENTO,'-') = CODUFFICIOINSERIMENTO.RV_LOW_VALUE AND CODUFFICIOINSERIMENTO.RV_DOMAIN = 'UFFICIO_INSERIMENTO' ) " ;
    lStatement += " (     nvl(CERTIFICATO_STATO_ESEC.COD_OPERATORE_AGGIORNAMENTO,'-') = CODOPERATOREAGGIORNAMENTO.RV_LOW_VALUE AND CODOPERATOREAGGIORNAMENTO.RV_DOMAIN = 'OPERATORE_AGGIORNAMENTO' ) "; 
    lStatement += " (     nvl(CERTIFICATO_STATO_ESEC.COD_UFFICIO_AGGIORNAMENTO,'-') = CODUFFICIOAGGIORNAMENTO.RV_LOW_VALUE AND CODUFFICIOAGGIORNAMENTO.RV_DOMAIN = 'UFFICIO_AGGIORNAMENTO' ) "; 
	*/
    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
     CertificatoStatoEsecModel aModel = new  CertificatoStatoEsecModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setIdCertificatoStatoEsec    ( getBigDecimal ("ID_CERTIFICATO_STATO_ESEC"  ) ); 
    aModel.setAnnotazioni               ( getString     ("ANNOTAZIONI"                ) ); 
    aModel.setFlagUpload                ( getString     ("FLAG_UPLOAD"                ) ); 
    aModel.setCodOperatoreInserimento   ( getString     ("COD_OPERATORE_INSERIMENTO"  ) ); 
    aModel.setDataInserimento           ( getDate       ("DATA_INSERIMENTO"           ) ); 
    aModel.setCodUfficioInserimento     ( getString     ("COD_UFFICIO_INSERIMENTO"    ) ); 
// aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento ( getString     ("COD_OPERATORE_AGGIORNAMENTO") ); 
    aModel.setDataAggiornamento         ( getDate       ("DATA_AGGIORNAMENTO"         ) ); 
    aModel.setCodUfficioAggiornamento   ( getString     ("COD_UFFICIO_AGGIORNAMENTO"  ) ); 
// aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setFasSieIdFascicoloSiep     ( getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"  ) ); 

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(CertificatoStatoEsecModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getIdCertificatoStatoEsec() != null ) { 
      lCondizioni += " and ID_CERTIFICATO_STATO_ESEC = " + aModel.getIdCertificatoStatoEsec() + ""; 
    } 
    if (aModel.getAnnotazioni() != null && aModel.getAnnotazioni().length() > 0) { 
      lCondizioni += " and ANNOTAZIONI = '" + aModel.getAnnotazioni() + "' "; 
    } 
    if (aModel.getFlagUpload() != null && aModel.getFlagUpload().length() > 0) { 
      lCondizioni += " and FLAG_UPLOAD = '" + aModel.getFlagUpload() + "' "; 
    } 
    if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() > 0) { 
      lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() + "' "; 
    } 
    if (aModel.getDataInserimento() != null ) { 
      lCondizioni += " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataInserimento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' "; 
    } 
    if (aModel.getCodOperatoreAggiornamento() != null && aModel.getCodOperatoreAggiornamento().length() > 0) { 
      lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento() + "' "; 
    } 
    if (aModel.getDataAggiornamento() != null ) { 
      lCondizioni += " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '" + DateUtils.getDateToString(aModel.getDataAggiornamento(),"dd/MM/yyyy") + "' "; 
    } 
    if (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) { 
      lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' "; 
    } 
    if (aModel.getFasSieIdFascicoloSiep() != null ) { 
      lCondizioni += " and FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep() + ""; 
    } 
    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    return lCondizioni; 
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di select per chiave 
   * @param aKey 
   * @return 
   ****************************************************************************/ 
  public String setCondizioniByKey( BigDecimal aIdCertificatoStatoEsec  ) {
    String lCondizioni = new String();

    lCondizioni += " and ID_CERTIFICATO_STATO_ESEC = " + aIdCertificatoStatoEsec;

    // Elimino il primo and 
    if (lCondizioni.length() > 0) { 
      lCondizioni = lCondizioni.substring(4); 
    } 

    return lCondizioni;
  }


  /***************************************************************************** 
   * Metodo per la costruzione della sezione order by 
   * @return 
   ****************************************************************************/ 
  protected String getOrderBy() { 
    String orderBy = new String(""); 
    //orderBy = " ORDER BY "; 
    return orderBy; 
  }
  
  /***************************************************************************** 
   * Metodo per l'ordinamento dei Certificati di Esecuzione per data di inserimento   
   * @return stringa ORDER BY
   ****************************************************************************/
	public String setOrderCert()
	{
		return " ORDER BY DATA_INSERIMENTO ";
	}
  
	/***************************************************************************** 
 	* Metodo per ricercare solo i file salvati dall'utente 
	* @return stringa AND FLAG_UPLOAD = '1'
	****************************************************************************/
	public String setCondUpl(){
		return " AND FLAG_UPLOAD = '1' ";
	}
  
	/**
	 * RIcerca la notizia di reato dall'id del fascicolo
	 * @param aIdFascicolo
	 * @throws DAOException
	 */
	public void ricercaCertificatoStatoEsecByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException
	{
		String lSql = getSqlQuery();

		lSql += "  WHERE  FAS_SIE_ID_FASCICOLO_SIEP="+ aIdFascicolo;
		lSql += setCondUpl();
		lSql += setOrderCert();
		setStatement(lSql);
	}
  
  
  
}
