package siap.sico.jms.dao;

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sico.jms.model.PresaInCaricoModel;

public class PresaInCaricoSqlDAO extends SIAPSqlDAO {
  
  public PresaInCaricoSqlDAO (Connection con) {
    super(con);
  }
  
  protected String getSqlQuery() {
    String lStatement = new String("");

    lStatement += "SELECT ID_PRESA_IN_CARICO, FAS_SIE_ID_FASCICOLO_SIEP "
               + "      , DATA_PRESA_IN_CARICO, COD_OPERATORE_PRESA_IN_CARICO, COD_UFFICIO_PRESA_IN_CARICO ";

    lStatement += " FROM PRESA_IN_CARICO ";

    lStatement += " WHERE 1=1 ";

    return lStatement;
  } 
  
  public GenericModel getModel() throws DAOException {
    PresaInCaricoModel aModel = new PresaInCaricoModel();

    aModel.setIdPresaInCarico           (getBigDecimal ("ID_PRESA_IN_CARICO"));
    aModel.setFasSieIdFascicoloSiep     (getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"));
    aModel.setDataPresaInCarico         (getDate       ("DATA_PRESA_IN_CARICO"));
    aModel.setCodUfficioPresaInCarico   (getString     ("COD_UFFICIO_PRESA_IN_CARICO"));
    aModel.setCodOperatorePresaInCarico (getString     ("COD_OPERATORE_PRESA_IN_CARICO")); 

    return aModel;
  }
  
  public void ricercaPresaIncaricoByCodUfficio (String aCodUfficio) throws DAOException {
    String lSql = getSqlQuery();

    lSql += " AND COD_UFFICIO_PRESA_IN_CARICO = '" + aCodUfficio + "' ";

    setStatement(lSql);
  }
  
  
  public void ricercaPresaInCaricoDNA (String aCodUfficio) throws DAOException {
    String lStatement = new String("");
    
    lStatement += " SELECT PRESA_IN_CARICO.* ";
    lStatement += "   FROM PRESA_IN_CARICO ";
    lStatement += "      , (select DISTINCT FAS_SIE_ID_FASCICOLO_SIEP ";
    lStatement += "           FROM PRESA_IN_CARICO ";
    lStatement += "          WHERE COD_UFFICIO_PRESA_IN_CARICO = '"+aCodUfficio+"' "; // DNA
    lStatement += "        ) dna ";
    lStatement += "  WHERE 1=1 ";
    lStatement += "    AND DNA.FAS_SIE_ID_FASCICOLO_SIEP = PRESA_IN_CARICO.FAS_SIE_ID_FASCICOLO_SIEP ";
    lStatement += "  ORDER BY PRESA_IN_CARICO.FAS_SIE_ID_FASCICOLO_SIEP, PRESA_IN_CARICO.DATA_PRESA_IN_CARICO ";
    
    setStatement(lStatement);
  }
}
