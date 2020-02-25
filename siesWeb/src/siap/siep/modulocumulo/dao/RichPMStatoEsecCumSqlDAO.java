package siap.siep.modulocumulo.dao;

/**
* <p>Title: RichPMStatoEsecCumSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella RichPM_Stato_Esec_Cum</p>
* <p>Company: Intersistemi Itali S.p.A.</p>
*/

import java.math.BigDecimal;
import java.lang.String;
import java.sql.Connection;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

import siap.siep.modulocumulo.model.RichPMStatoEsecCumModel;


public class RichPMStatoEsecCumSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public RichPMStatoEsecCumSqlDAO (Connection con) {
    super(con);
  }

  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per Id_Richiesta_PM_In_Cumulo 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaRichPmStatoEsecCumByRichIdRich( BigDecimal aIdRichiestePmInCumulo) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " WHERE " + setCondizioniByRichIdRich( aIdRichiestePmInCumulo);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }

  public void ricercaRichPmStatoEsecCumByIdStatEsec( BigDecimal aIdStatoEsecTitCum) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " WHERE STAT_ID_STATO_ESEC_TITOLO_CUM = " + aIdStatoEsecTitCum;

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
                  "RIC_ID_RICHIESTE_PM_IN_CUMULO, "+  
                  "STAT_ID_STATO_ESEC_TITOLO_CUM ";  
    lStatement += " FROM RICHPM_STATO_ESEC_CUM";

    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
	  RichPMStatoEsecCumModel aModel = new RichPMStatoEsecCumModel(); 

    aModel.setRicIdRichiestePmInCumulo  ( getBigDecimal ("RIC_ID_RICHIESTE_PM_IN_CUMULO"    ) ); 
    aModel.setStatIdStatoEsecCumulo		( getBigDecimal ("STAT_ID_STATO_ESEC_TITOLO_CUM"       ) ); 

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(RichPMStatoEsecCumModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getRicIdRichiestePmInCumulo() != null ) { 
      lCondizioni += " and RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aModel.getRicIdRichiestePmInCumulo() + ""; 
    } 

    if (aModel.getStatIdStatoEsecCumulo() != null ) { 
      lCondizioni += " and STAT_ID_STATO_ESEC_TITOLO_CUM = " + aModel.getStatIdStatoEsecCumulo() + ""; 
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
  public String setCondizioniByRichIdRich( BigDecimal aIdRichiestePmInCumulo  ) {
    String lCondizioni = new String();

    lCondizioni += " and RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aIdRichiestePmInCumulo;

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
}
