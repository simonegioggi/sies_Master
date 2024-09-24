package siap.siep.statoprocedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;

public class MaxStatoProcedimentoDAO extends TableDAO {
  public MaxStatoProcedimentoDAO (Connection con)
  {
    super(con);

    setTable("MAX_STATO_PROCEDIMENTO");

    //Settare la Sequence e i campi chiave

    setField("STA_PRO_PROGRESSIVO", BIG_DECIMAL);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
  }
  
  // Metodi GET
  public BigDecimal getProgressivo()           throws DAOException { return getBigDecimal("STA_PRO_PROGRESSIVO"); }
  public BigDecimal getFasSieIdFascicoloSiep() throws DAOException { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  
  // Metodi SET
  public void setProgressivo           (BigDecimal aValore ) { setBigDecimal("STA_PRO_PROGRESSIVO", aValore); }
  public void getFasSieIdFascicoloSiep (BigDecimal aValore ) { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  
  public void setCondizioneByIdFascicolo(BigDecimal aIdFascicolo)
  {
    setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = "+ aIdFascicolo);
  }
}
