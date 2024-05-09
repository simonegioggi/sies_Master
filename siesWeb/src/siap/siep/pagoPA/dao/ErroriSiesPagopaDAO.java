package siap.siep.pagoPA.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.siep.pagoPA.model.ErroriSiesPagopaModel;

public class ErroriSiesPagopaDAO extends SIAPTableDAO {
  public ErroriSiesPagopaDAO (Connection con) {

    super(con);
    setTable("ERRORI_SIES_PAGOPA");

    // Settare la Sequence e i campi chiave
    setSequenceField("ID_ERRORI_SIES_PAGOPA", "ERRORI_SIES_PAGOPA_SEQ");
    setFieldKey("ID_ERRORI_SIES_PAGOPA", BIG_DECIMAL);

    setField("ID_ERRORI_SIES_PAGOPA", BIG_DECIMAL);
    setField("ID_FASCICOLO_SIEP", BIG_DECIMAL);
    setField("ID_EVENTO", BIG_DECIMAL);
    setField("AZIONE_CONTESTO_JAVA", STRING);
    setField("DESCRIZIONE_FUNZIONE", STRING);
    setField("COD_UTENTE", STRING);
    setField("COD_UFFICIO", STRING);
    setField("ERRORE_ESECUZIONE", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("DATA_VISUALIZZAZIONE", DATE);
    setField("COD_UTENTE_VISUALIZZAZIONE", STRING);
  }
  
  //
  // METODI GET()
  //
  public BigDecimal getIdErroriSiesPagopa() throws DAOException {
    return getBigDecimal("ID_ERRORI_SIES_PAGOPA");
  }
  public BigDecimal getIdFascicoloSiep() throws DAOException {
    return getBigDecimal("ID_FASCICOLO_SIEP");
  }
  public BigDecimal getIdEvento() throws DAOException {
    return getBigDecimal("ID_EVENTO");
  }
  public String getAzioneContestoJava() throws DAOException {
    return getString("AZIONE_CONTESTO_JAVA");
  }
  public String getDescrizioneFunzione() throws DAOException {
    return getString("DESCRIZIONE_FUNZIONE");
  }  
  public String getCodUtente() throws DAOException {
    return getString("COD_UTENTE");
  }
  public String getCodUfficio() throws DAOException {
    return getString("COD_UFFICIO");
  }
  public String getErroreEsecuzione() throws DAOException {
    return getString("ERRORE_ESECUZIONE");
  }
  public Date getDataInserimento() throws DAOException {
    return getDate("DATA_INSERIMENTO");
  }
  public Date getDataVisualizzazione() throws DAOException {
    return getDate("DATA_VISUALIZZAZIONE");
  }
  public String getCodUtenteVisualizzazione() throws DAOException {
    return getString("COD_UTENTE_VISUALIZZAZIONE");
  }
  
  // Metodi SET()
  public void setIdErroriSiesPagopa(BigDecimal aValore) {
    setBigDecimal("ID_ERRORI_SIES_PAGOPA", aValore);
  }
  public void setIdFascicoloSiep(BigDecimal aValore) {
    setBigDecimal("ID_FASCICOLO_SIEP", aValore);
  }
  public void setIdEvento(BigDecimal aValore) {
    setBigDecimal("ID_EVENTO", aValore);
  }
  public void setAzioneContestoJava(String aValore) {
    setString("AZIONE_CONTESTO_JAVA", aValore);
  }
  public void setDescrizioneFunzione(String aValore) {
    setString("DESCRIZIONE_FUNZIONE", aValore);
  }
  public void setCodUtente(String aValore) {
    setString("COD_UTENTE", aValore);
  }
  public void setCodUfficio(String aValore) {
    setString("COD_UFFICIO", aValore);
  }
  public void setErroreEsecuzione(String aValore) {
    setString("ERRORE_ESECUZIONE", aValore);
  }
  public void setDataInserimento(Date aValore) {
    setDate("DATA_INSERIMENTO", aValore);
  }
  public void setDataVisualizzazione(Date aValore) {
    setDate("DATA_VISUALIZZAZIONE", aValore);
  }
  public void setCodUtenteVisualizzazione(String aValore) {
    setString("COD_UTENTE_VISUALIZZAZIONE", aValore);
  }
  
  public GenericModel getModel() throws DAOException {
    ErroriSiesPagopaModel lErroriModel = new ErroriSiesPagopaModel();
    
    lErroriModel.setIdErroriSiesPagopa (getIdErroriSiesPagopa());
    lErroriModel.setIdFascicoloSiep (getIdFascicoloSiep());
    lErroriModel.setIdEvento (getIdEvento());
    lErroriModel.setAzioneContestoJava (getAzioneContestoJava());
    lErroriModel.setDescrizioneFunzione (getDescrizioneFunzione());
    lErroriModel.setCodUtente (getCodUtente());
    lErroriModel.setCodUfficio (getCodUfficio());
    lErroriModel.setErroreEsecuzione (getErroreEsecuzione());
    lErroriModel.setDataInserimento (getDataInserimento());
    lErroriModel.setDataVisualizzazione (getDataVisualizzazione());
    lErroriModel.setCodUtenteVisualizzazione (getCodUtenteVisualizzazione());      
    
    return lErroriModel;
  }
 
  
  public void setDAOFromModel (ErroriSiesPagopaModel aModel) throws DAOException {
    setIdErroriSiesPagopa (aModel.getIdErroriSiesPagopa());
    setIdFascicoloSiep (aModel.getIdFascicoloSiep());
    setIdEvento (aModel.getIdEvento());
    setAzioneContestoJava (aModel.getAzioneContestoJava());
    setDescrizioneFunzione (aModel.getDescrizioneFunzione());
    setCodUtente (aModel.getCodUtente());
    setCodUfficio (aModel.getCodUfficio());
    setErroreEsecuzione (aModel.getErroreEsecuzione());
    setDataInserimento (aModel.getDataInserimento());
    setDataVisualizzazione (aModel.getDataVisualizzazione());
    setCodUtenteVisualizzazione (aModel.getCodUtenteVisualizzazione());  
  }
  
  public void setDAOFromModelForUpdate (ErroriSiesPagopaModel aModel) throws DAOException {
    //setIdErroriSiesPagopa (aModel.getIdErroriSiesPagopa());
    setIdFascicoloSiep (aModel.getIdFascicoloSiep());
    setIdEvento (aModel.getIdEvento());
    setAzioneContestoJava (aModel.getAzioneContestoJava());
    setDescrizioneFunzione (aModel.getDescrizioneFunzione());
    setCodUtente (aModel.getCodUtente());
    setCodUfficio (aModel.getCodUfficio());
    setErroreEsecuzione (aModel.getErroreEsecuzione());
    setDataInserimento (aModel.getDataInserimento());
    setDataVisualizzazione (aModel.getDataVisualizzazione());
    setCodUtenteVisualizzazione (aModel.getCodUtenteVisualizzazione());  
  }
  
  public void selCondizioneByKey(BigDecimal IdErroriSiesPagopa) {
    setCondition(" ID_ERRORI_SIES_PAGOPA = " + IdErroriSiesPagopa);
  }

}
