package siap.sico.jms.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import siap.sico.jms.model.PresaInCaricoModel;

public class PresaInCaricoDAO extends TableDAO {
  public PresaInCaricoDAO (Connection con)
  {
    super(con);
    setTable("PRESA_IN_CARICO");

    setSequenceField("ID_PRESA_IN_CARICO", "PRE_IN_CAR_SEQ");
    //setFieldKey("ID_PRESA_IN_CARICO", BIG_DECIMAL);

    setField("ID_PRESA_IN_CARICO", BIG_DECIMAL);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);

    setField("DATA_PRESA_IN_CARICO", DATE);
    setField("COD_OPERATORE_PRESA_IN_CARICO", STRING);
    setField("COD_UFFICIO_PRESA_IN_CARICO", STRING);
  }
  
  // Metodi Get
  public BigDecimal getIdPresaInCarico()           throws DAOException { return getBigDecimal ("ID_PRESA_IN_CARICO"); }
  public BigDecimal getFasSieIdFascicoloSiep()     throws DAOException { return getBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP"); }   
  public Date       getDataPresaInCarico()         throws DAOException { return getDate   ("DATA_PRESA_IN_CARICO"); }
  public String     getCodOperatorePresaInCarico() throws DAOException { return getString ("COD_OPERATORE_PRESA_IN_CARICO"); }
  public String     getCodUfficioPresaInCarico()   throws DAOException { return getString ("COD_UFFICIO_PRESA_IN_CARICO"); }
  
  // Metodi Set
  public void setIdPresaInCarico           (BigDecimal aValore ) { setBigDecimal ("ID_PRESA_IN_CARICO", aValore); }
  public void setFasSieIdFascicoloSiep     (BigDecimal aValore ) { setBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void setDataPresaInCarico         (Date       aValore ) { setDate   ("DATA_PRESA_IN_CARICO", aValore); }
  public void setCodOperatorePresaInCarico (String     aValore ) { setString ("COD_OPERATORE_PRESA_IN_CARICO", aValore); }  
  public void setCodUfficioPresaInCarico   (String     aValore ) { setString ("COD_UFFICIO_PRESA_IN_CARICO", aValore); }
  
  public GenericModel getModel() throws DAOException
  {
    return new PresaInCaricoModel (getIdPresaInCarico() ,
                                   getFasSieIdFascicoloSiep() ,
                                   getDataPresaInCarico() ,
                                   getCodOperatorePresaInCarico() ,
                                   getCodUfficioPresaInCarico() 
                                  );
  }
  
  public void setDAOFromModel (PresaInCaricoModel aModel) throws DAOException
  {
    //setIdPresaInCarico           ( aModel.getIdPresaInCarico() );
    setFasSieIdFascicoloSiep     ( aModel.getFasSieIdFascicoloSiep() );
    setDataPresaInCarico         ( aModel.getDataPresaInCarico() );
    setCodUfficioPresaInCarico   ( aModel.getCodUfficioPresaInCarico() );
    setCodOperatorePresaInCarico ( aModel.getCodOperatorePresaInCarico() );
  }  
  
  public void setDAOFromModelForUpdate (PresaInCaricoModel aModel) throws DAOException
  {
    //setIdPresaInCarico           ( aModel.getIdPresaInCarico() );
    setFasSieIdFascicoloSiep     ( aModel.getFasSieIdFascicoloSiep() );
    setDataPresaInCarico         ( aModel.getDataPresaInCarico() );
    setCodUfficioPresaInCarico   ( aModel.getCodUfficioPresaInCarico() );
    setCodOperatorePresaInCarico ( aModel.getCodOperatorePresaInCarico() );
  }  
  
  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_PRESA_IN_CARICO = " + key );
  }
  
  public void setCondizioneDeleteDNA(BigDecimal aIdFascioloSiep, String aCodUfficioDNA)
  {
    String lCondition ="";
    lCondition += " FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFascioloSiep;
    lCondition += " AND COD_UFFICIO_PRESA_IN_CARICO = '" + aCodUfficioDNA+"' " ;
    setCondition(lCondition);
  }
}
