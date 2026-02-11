package siap.siep.modulocumulo.dao;

/**
* <p>Title: RichPMBeneficioCumSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella RichPM_Beneficio_Cum</p>
* <p>Company: Intersistemi Itali S.p.A.</p>
*/

import java.math.BigDecimal;
import java.lang.String;
import java.sql.Connection;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

import siap.siep.modulocumulo.model.RichPMBeneficioCumModel;

public class RichPMBeneficioCumSqlDAO extends SqlDAO {
  /***************************************************************************** 
   * Costruttore  
   * @param con 
   ****************************************************************************/ 
  public RichPMBeneficioCumSqlDAO (Connection con) {
    super(con);
  }

  /***************************************************************************** 
   * Metodo che imposta la statement di ricerca per chiave 
   * @param aKey 
   * @throws DAOException 
   ****************************************************************************/ 
  public void ricercaRichPmBeneficioCumByRichIdRich( BigDecimal aIdRichiestePmInCumulo) throws DAOException {
    // Recupera la select...from 
    String lSql = getSqlQuery();

    // Aggiunge le where condition per chiave 
    lSql += " WHERE " + setCondizioniByRichIdRich( aIdRichiestePmInCumulo);

    // Imposta lo statement da eseguire 
    setStatement(lSql);
  }

  // 2025.11.18 Aggiunto metodo ricerca per id_beneficio
  public void ricercaRichPmBeneficioCumByIdBen( BigDecimal aIdBeneficio) throws DAOException {
      // Recupera la select...from 
      String lSql = getSqlQuery();

      // Aggiunge le where condition per chiave 
      lSql += " WHERE BEN_ID_BENEFICIO_CUM = " + aIdBeneficio;

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
                  "BEN_ID_BENEFICIO_CUM  ";  
    lStatement += " FROM RICHPM_BENEFICIO_CUM";

    return lStatement;
  }


  /***************************************************************************** 
   * Metodo che carica il record del result set nel model 
   * @return 
   * @throws DAOException 
   ****************************************************************************/ 
  public GenericModel  getModel() throws DAOException {
	  RichPMBeneficioCumModel aModel = new  RichPMBeneficioCumModel(); 

    //Inserire le opportune set delle descrizioni!
    aModel.setRicIdRichiestePmInCumulo  ( getBigDecimal ("RIC_ID_RICHIESTE_PM_IN_CUMULO"    ) ); 
    aModel.setBenIdBeneficioCum         ( getBigDecimal ("BEN_ID_BENEFICIO_CUM"       ) ); 

    return aModel;
  }


  /***************************************************************************** 
   * Metodo che imposta le condizioni di where per la ricerca 
   * @param aModel 
   * @return 
   ****************************************************************************/ 
  public String setCondizioni(RichPMBeneficioCumModel aModel) {
    String lCondizioni = new String(); 

    if (aModel.getRicIdRichiestePmInCumulo() != null ) { 
      lCondizioni += " and RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aModel.getRicIdRichiestePmInCumulo() + ""; 
    } 


    if (aModel.getBenIdBeneficioCum() != null ) { 
      lCondizioni += " and BEN_ID_BENEFICIO_CUM = " + aModel.getBenIdBeneficioCum() + ""; 
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
