package siap.siep.avvocato.dao;

/**
 * <p>Title: AvvocatoDAO</p>
 * <p>Description: Classe DAO che rappresenta la tabella Avvocato_Fascicolo_Siep</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import f3b.dao.DAOException;

public class AvvocatoFascicoloSiepDAO
    extends SIAPTableDAO
{
  public AvvocatoFascicoloSiepDAO(Connection con)

  {
    super(con);

    setTable("AVVOCATO_FASCICOLO_SIEP");
    setSequenceField("ID_AVVOCATO_FASCICOLO_SIEP", "AVV_FAS_SIE_SEQ");

    setFieldKey("ID_AVVOCATO_FASCICOLO_SIEP", BIG_DECIMAL); 
    setField("ID_AVVOCATO_FASCICOLO_SIEP", BIG_DECIMAL);
    setField("COD_TIPO_AVVOCATO", STRING);
    setField("DATA_INIZIO_VALIDITA", DATE);
    setField("DATA_FINE_VALIDITA", DATE);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("AVV_ID_AVVOCATO", BIG_DECIMAL);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
    setField("COD_MOTIVO_DESIGNAZIONE", STRING);
    setField("COD_TIPO_AUTORITA", STRING);
    setField("SEDE_TIPO_AUTORITA", STRING);
    setField("INDIRIZZO_TIPO_AUTORITA", STRING);
    setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
    setField("COD_TIPO_AUTORITA_DIF", STRING);
    setField("SEDE_TIPO_AUTORITA_DIF", STRING);
    setField("NOTE", STRING);
    setField("EVE_ID_EVENTO", BIG_DECIMAL);
    setField("MOTIVO", STRING);
    setField("AVV_ID_AVVOCATO_FASCICOLO_SOST", BIG_DECIMAL);
  }

  //============================================================================
  // METODI GET()
  //============================================================================

  public String getCodTipoAvvocato() throws DAOException
  {
    return getString("COD_TIPO_AVVOCATO");
  }

  public Date getDataInizioValidita() throws DAOException
  {
    return getDate("DATA_INIZIO_VALIDITA");
  }

  public Date getDataFineValidita() throws DAOException
  {
    return getDate("DATA_FINE_VALIDITA");
  }

  public String getCodOperatoreInserimento() throws DAOException
  {
    return getString("COD_OPERATORE_INSERIMENTO");
  }

  public Date getDataInserimento() throws DAOException
  {
    return getDate("DATA_INSERIMENTO");
  }

  public String getCodUfficioInserimento() throws DAOException
   {
     return getString("COD_UFFICIO_INSERIMENTO");
   }

  public String getCodOperatoreAggiornamento() throws DAOException
  {
    return getString("COD_OPERATORE_AGGIORNAMENTO");
  }

  public Date getDataAggiornamento() throws DAOException
  {
    return getDate("DATA_AGGIORNAMENTO");
  }

  public String getCodUfficioAggiornamento() throws DAOException
  {
    return getString("COD_UFFICIO_AGGIORNAMENTO");
  }

  public BigDecimal getAvvIdAvvocato() throws DAOException
  {
    return getBigDecimal("AVV_ID_AVVOCATO");
  }

  public BigDecimal getFasSiepIdFascicolo() throws DAOException
  {
    return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP");
  }

  public BigDecimal getIdAvvocatoFascicoloSiep() throws DAOException
  {
    return getBigDecimal("ID_AVVOCATO_FASCICOLO_SIEP");
  }

  public String getCodMotivoDesignazione() throws DAOException
  {
    return getString("COD_MOTIVO_DESIGNAZIONE");
  }

  public String getCodTipoAutorita() throws DAOException
  {
    return getString("COD_TIPO_AUTORITA");
  }

  public String getSedeTipoAutorita() throws DAOException
  {
    return getString("SEDE_TIPO_AUTORITA");
  }

  public String getIndirizzoTipoAutorita() throws DAOException
  {
    return getString("INDIRIZZO_TIPO_AUTORITA");
  }

  public String getIstDetIdIstitutoDetenzione() throws DAOException
  {
    return getString("IST_DET_ID_ISTITUTO_DETENZIONE");
  }

  public String getCodTipoAutoritaDir() throws DAOException
  {
    return getString("COD_TIPO_AUTORITA_DIF");
  }

  public String getSedeTipoAutoritaDif() throws DAOException
  {
    return getString("SEDE_TIPO_AUTORITA_DIF");
  }

  public String getNote() throws DAOException
  {
    return getString("NOTE");
  }
  
  public BigDecimal getEveIdEvento() throws DAOException
  {
    return getBigDecimal("EVE_ID_EVENTO");
  }
  
  public String getMotivo() throws DAOException
  {
    return getString("MOTIVO");
  }
  
  public BigDecimal getAvvIdAvvocatoFascicoloSost() throws DAOException
  {
    return getBigDecimal("AVV_ID_AVVOCATO_FASCISCOLO_SOST");
  }
  
  //============================================================================
  // METODI  SET()
  //============================================================================

  public void setCodTipoAvvocato(String aValore)
  {
    setString("COD_TIPO_AVVOCATO", aValore);
  }

  public void setDataInizioValidita(Date aValore)
  {
    setDate("DATA_INIZIO_VALIDITA", aValore);
  }

  public void setDataFineValidita(Date aValore)
  {
    setDate("DATA_FINE_VALIDITA", aValore);
  }

  public void setCodOperatoreInserimento(String aValore)
  {
    setString("COD_OPERATORE_INSERIMENTO", aValore);
  }

  public void setDataInserimento(Date aValore)
  {
    setDate("DATA_INSERIMENTO", aValore);
  }

  public void setCodUfficoInserimento(String aValore)
  {
    setString("COD_UFFICIO_INSERIMENTO", aValore);
  }

  public void setCodOperatoreAggiornamento(String aValore)
  {
    setString("COD_OPERATORE_AGGIORNAMENTO", aValore);
  }

  public void setDataAggiornamento(Date aValore)
  {
    setDate("DATA_AGGIORNAMENTO", aValore);
  }

  public void setCodUfficoAggiornamento(String aValore)
  {
    setString("COD_UFFICIO_AGGIORNAMENTO", aValore);
  }

  public void setAvvIdAvvocato(BigDecimal aValore)
  {
    setBigDecimal("AVV_ID_AVVOCATO", aValore);
  }

  public void setFasSiepIdFascicolo(BigDecimal aValore)
  {
    setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore);
  }

  public void setIdAvvocatoFascicoloSiep(BigDecimal aValore) throws DAOException
  {
    setBigDecimal("ID_AVVOCATO_FASCICOLO_SIEP", aValore);

  }

  public void setCodMotivoDesignazione(String aValore) throws DAOException
  {
    setString("COD_MOTIVO_DESIGNAZIONE", aValore);
  }

  public void setCodTipoAutorita(String aValore) throws DAOException
  {
    setString("COD_TIPO_AUTORITA", aValore);
  }

  public void setSedeTipoAutorita(String aValore) throws DAOException
  {
    setString("SEDE_TIPO_AUTORITA", aValore);
  }

  public void setIndirizzoTipoAutorita(String aValore) throws DAOException
  {
    setString("INDIRIZZO_TIPO_AUTORITA", aValore);
  }

  public void setIstDetIdIstitutoDetenzione(String aValore) throws DAOException
  {
    setString("IST_DET_ID_ISTITUTO_DETENZIONE", aValore);
  }

  public void setCodTipoAutoritaDif(String aValore) throws DAOException
  {
    setString("COD_TIPO_AUTORITA_DIF", aValore);
  }

  public void setSedeTipoAutoritaDif(String aValore) throws DAOException
  {
    setString("SEDE_TIPO_AUTORITA_DIF", aValore);
  }

  public void setNote(String aValore) throws DAOException
  {
    setString("NOTE", aValore);
  }
  
  public void setEveIdEvento(BigDecimal aValore) throws DAOException
  {
    setBigDecimal("EVE_ID_EVENTO", aValore);
  }
  
  public void setMotivo(String aValore) throws DAOException
  {
    setString("MOTIVO", aValore);
  }
  
  public void setAvvIdAvvocatoFascicoloSost(BigDecimal aValore)
  {
    setBigDecimal("AVV_ID_AVVOCATO_FASCICOLO_SOST", aValore);
  }

  /*****************************************************************************
   * 
   * @param aModel
   * @throws DAOException
   ************************************************************************** */
  public void setDAOFromModel(AvvocatoFascicoloSiepModel aModel) throws DAOException
  {
    setIdAvvocatoFascicoloSiep (aModel.getIdAvvocatoFascicoloSiep());
    setFasSiepIdFascicolo      (aModel.getFasSieIdFascicoloSiep());
    setAvvIdAvvocato           (aModel.getAvvIdAvvocato());
    setCodTipoAvvocato         (aModel.getCodTipoAvvocato());
    setDataInizioValidita      (aModel.getDataInizioValidita());
    setDataFineValidita        (aModel.getDataFineValidita());
    
    // Per avvocato di ufficio 
    setCodMotivoDesignazione (aModel.getCodMotivoDesignazione());
    setCodTipoAutorita       (aModel.getCodTipoAutorita());
    setSedeTipoAutorita      (aModel.getSedeAutorita());
    setIndirizzoTipoAutorita (aModel.getIndirizzoTipoAutorita());
    setCodTipoAutoritaDif    (aModel.getCodTipoAutoritaDif());
    setSedeTipoAutoritaDif   (aModel.getSedeAutoritaDif());
    setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
    
    //===
    setNote(aModel.getNote());

    setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
    setDataInserimento(aModel.getDataInserimento());
    setCodUfficoInserimento(aModel.getCodUfficioInserimento());
    
    setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
    setDataAggiornamento(aModel.getDataAggiornamento());
    setCodUfficoAggiornamento(aModel.getCodUfficioAggiornamento());
    
    setEveIdEvento(aModel.getEveIdEvento());
    setMotivo(aModel.getMotivo());
    setAvvIdAvvocatoFascicoloSost(aModel.getAvvIdAvvocatoFascicoloSost());
    
  }

  /*****************************************************************************
   * Questo metodo imposta le condizioni di update per la tabella di relazione
   * AVVOCATO_FASCICOLO_SIEP aggiornando la data fine validità
   *  
   * @param aModel
   * @throws DAOException
   ************************************************************************** */
  public void setDAOFromModelForUpdateAvvIdAvvocato(AvvocatoFascicoloSiepModel aModel) throws DAOException
  {
    setDataFineValidita(aModel.getDataFineValidita());
    
    setCodOperatoreAggiornamento (aModel.getCodOperatoreAggiornamento());
    setDataAggiornamento         (aModel.getDataAggiornamento());
    setCodUfficoAggiornamento    (aModel.getCodUfficioAggiornamento());

    selCondizioneUpdateIdAvvFascSiep(aModel.getIdAvvocatoFascicoloSiep());
  }
  
  /*****************************************************************************
   * 
   * @param aModel
   * @throws DAOException
   ************************************************************************** */
 public void setDAOFromModelForUpdateAvvIdAvvocatoForDeassegnazione(AvvocatoFascicoloSiepModel aModel) throws DAOException
 {
   setDataFineValidita(aModel.getDataFineValidita());

   setCodOperatoreAggiornamento (aModel.getCodOperatoreAggiornamento());
   setDataAggiornamento         (aModel.getDataAggiornamento());
   setCodUfficoAggiornamento    (aModel.getCodUfficioAggiornamento());
   setMotivo					(aModel.getMotivo());
   
   selCondizioneUpdateIdAvvFascSiep(aModel.getIdAvvocatoFascicoloSiep());
 }


  public void selCondizione(AvvocatoFascicoloSiepModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if (lInserito)
      setCondition(lCondizioni);
  }

  public void selCondizioneUpdate(BigDecimal key)
  {
	  setCondition(" ID_AVVOCATO_FASCICOLO_SIEP = " + key );
  }
  
  public void selCondizioneUpdateAvvIdAvv(BigDecimal key)
  {
    setCondition(" AVV_ID_AVVOCATO = " + key );

  }
  public void selCondizioneUpdateIdAvvFascSiep(BigDecimal key)
   {
     setCondition(" ID_AVVOCATO_FASCICOLO_SIEP = " + key );

   }
  public void selCondizioneDeleteEveIdEvento(BigDecimal key)
  {
    setCondition(" EVE_ID_EVENTO = " + key );

  }
}