package siap.siep.penaresidua.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.penaresidua.model.PenaResiduaModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: PenaResiduaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella PenaResidua</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class PenaResiduaDAO extends TableDAO
{
  public PenaResiduaDAO (Connection con)
  {
    super(con);
    setTable("PENA_RESIDUA");

    //Settare la Sequence e i campi chiave
    setSequenceField("ID_PENA_RESIDUA","PEN_RES_SEQ");
    setField("ID_PENA_RESIDUA", BIG_DECIMAL);
    setField("DATA_INIZIO", DATE);
    setField("DATA_FINE", DATE);
    setField("NUM_ANNI_RECLUSIONE", BIG_DECIMAL);
    setField("NUM_MESI_RECLUSIONE", BIG_DECIMAL);
    setField("NUM_GIORNI_RECLUSIONE", BIG_DECIMAL);
    setField("IMPORTO_MULTA", BIG_DECIMAL);
    setField("NUM_ANNI_ARRESTO", BIG_DECIMAL);
    setField("NUM_MESI_ARRESTO", BIG_DECIMAL);
    setField("NUM_GIORNI_ARRESTO", BIG_DECIMAL);
    setField("IMPORTO_AMMENDA", BIG_DECIMAL);
    setField("DIES_A_QUO", STRING);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("EVE_ID_EVENTO", BIG_DECIMAL);
    setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);

    setField("FLAG_VALIDATO", STRING);
    setField("DATA_FINE_PRESUNTA", DATE);
    setField("DATA_FINE_RECLUSIONE", DATE);
    setField("DATA_INIZIO_ARRESTO", DATE);
    setField("FLAG_ERGASTOLO", STRING);
    setField("DATA_INIZIO_ISOLAMENTO_DIURNO", DATE);
    setField("DATA_FINE_ISOLAMENTO_DIURNO", DATE);
    setField("NUM_ANNI_ISOLAMENTO_DIURNO", BIG_DECIMAL);
    setField("NUM_MESI_ISOLAMENTO_DIURNO", BIG_DECIMAL);
    setField("NUM_GIORNI_ISOLAMENTO_DIURNO", BIG_DECIMAL);
    setField("MIS_ALT_ID_MISURA_ALTERNATIVA", BIG_DECIMAL);
    setField("FLAG_PENA_SOSPESA", STRING);
  }

        
  //
  // METODI GET()
  //
  public BigDecimal getIdPenaResidua()              throws DAOException  { return getBigDecimal("ID_PENA_RESIDUA"); }
  public Date       getDataInizio()                 throws DAOException  { return getDate("DATA_INIZIO"); }
  public Date       getDataFine()                   throws DAOException  { return getDate("DATA_FINE"); }
  public BigDecimal getNumAnniReclusione()          throws DAOException  { return getBigDecimal("NUM_ANNI_RECLUSIONE"); }
  public BigDecimal getNumMesiReclusione()          throws DAOException  { return getBigDecimal("NUM_MESI_RECLUSIONE"); }
  public BigDecimal getNumGiorniReclusione()        throws DAOException  { return getBigDecimal("NUM_GIORNI_RECLUSIONE"); }
  public BigDecimal getImportoMulta()               throws DAOException  { return getBigDecimal("IMPORTO_MULTA"); }
  public BigDecimal getNumAnniArresto()             throws DAOException  { return getBigDecimal("NUM_ANNI_ARRESTO"); }
  public BigDecimal getNumMesiArresto()             throws DAOException  { return getBigDecimal("NUM_MESI_ARRESTO"); }
  public BigDecimal getNumGiorniArresto()           throws DAOException  { return getBigDecimal("NUM_GIORNI_ARRESTO"); }
  public BigDecimal getImportoAmmenda()             throws DAOException  { return getBigDecimal("IMPORTO_AMMENDA"); }
  public String     getDiesAQuo()                   throws DAOException  { return getString("DIES_A_QUO"); }
  public String     getCodOperatoreInserimento()    throws DAOException  { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date       getDataInserimento()            throws DAOException  { return getDate("DATA_INSERIMENTO"); }
  public String     getCodUfficioInserimento()      throws DAOException  { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String     getCodOperatoreAggiornamento()  throws DAOException  { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date       getDataAggiornamento()          throws DAOException  { return getDate("DATA_AGGIORNAMENTO"); }
  public String     getCodUfficioAggiornamento()    throws DAOException  { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal getEveIdEvento()                throws DAOException  { return getBigDecimal("EVE_ID_EVENTO"); }
  public BigDecimal getFasSieIdFascicoloSiep()      throws DAOException  { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  public String     getFlagValidato()               throws DAOException  { return getString("FLAG_VALIDATO"); }
  public Date       getDataFinePresunta()           throws DAOException  { return getDate("DATA_FINE_PRESUNTA"); }
  public Date       getDataFineReclusione()         throws DAOException  { return getDate("DATA_FINE_RECLUSIONE"); }
  public Date       getDataInizioArresto()          throws DAOException  { return getDate("DATA_INIZIO_ARRESTO"); }
  public String     getFlagErgastolo()              throws DAOException  { return getString("FLAG_ERGASTOLO"); }
  public Date       getDataInizioIsolamentoDiurno() throws DAOException  { return getDate("DATA_INIZIO_ISOLAMENTO_DIURNO"); }
  public Date       getDataFineIsolamentoDiurno()   throws DAOException  { return getDate("DATA_FINE_ISOLAMENTO_DIURNO"); }
  public BigDecimal getNumAnniIsolamentoDiurno()    throws DAOException  { return getBigDecimal("NUM_ANNI_ISOLAMENTO_DIURNO"); }
  public BigDecimal getNumMesiIsolamentoDiurno()    throws DAOException  { return getBigDecimal("NUM_MESI_ISOLAMENTO_DIURNO"); }
  public BigDecimal getNumGiorniIsolamentoDiurno()  throws DAOException  { return getBigDecimal("NUM_GIORNI_ISOLAMENTO_DIURNO"); }
  public BigDecimal getMisAltIdMisuraAlternativa()  throws DAOException  { return getBigDecimal("MIS_ALT_ID_MISURA_ALTERNATIVA"); }
  public String     getFlagPenaSospesa()            throws DAOException  { return getString("FLAG_PENA_SOSPESA"); }


  //
  // METODI SET()
  //
  public void setIdPenaResidua(BigDecimal aValore )             { setBigDecimal("ID_PENA_RESIDUA", aValore); }
  public void setDataInizio(Date aValore )                      { setDate("DATA_INIZIO", aValore); }
  public void setDataFine(Date aValore )                        { setDate("DATA_FINE", aValore); }
  public void setNumAnniReclusione(BigDecimal aValore )         { setBigDecimal("NUM_ANNI_RECLUSIONE", aValore); }
  public void setNumMesiReclusione(BigDecimal aValore )         { setBigDecimal("NUM_MESI_RECLUSIONE", aValore); }
  public void setNumGiorniReclusione(BigDecimal aValore )       { setBigDecimal("NUM_GIORNI_RECLUSIONE", aValore); }
  public void setImportoMulta(BigDecimal aValore )              { setBigDecimal("IMPORTO_MULTA", aValore); }
  public void setNumAnniArresto(BigDecimal aValore )            { setBigDecimal("NUM_ANNI_ARRESTO", aValore); }
  public void setNumMesiArresto(BigDecimal aValore )            { setBigDecimal("NUM_MESI_ARRESTO", aValore); }
  public void setNumGiorniArresto(BigDecimal aValore )          { setBigDecimal("NUM_GIORNI_ARRESTO", aValore); }
  public void setImportoAmmenda(BigDecimal aValore )            { setBigDecimal("IMPORTO_AMMENDA", aValore); }
  public void setDiesAQuo(String aValore )                      { setString("DIES_A_QUO", aValore); }
  public void setCodOperatoreInserimento(String aValore )       { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void setDataInserimento(Date aValore )                 { setDate("DATA_INSERIMENTO", aValore); }
  public void setCodUfficioInserimento(String aValore )         { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void setCodOperatoreAggiornamento(String aValore )     { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void setDataAggiornamento(Date aValore )               { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void setCodUfficioAggiornamento(String aValore )       { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void setEveIdEvento(BigDecimal aValore )               { setBigDecimal("EVE_ID_EVENTO", aValore); }
  public void setFasSieIdFascicoloSiep(BigDecimal aValore )     { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void setFlagValidato(String aValore)                   { setString("FLAG_VALIDATO" ,aValore); }
  public void setDataFinePresunta(Date aValore)                 { setDate("DATA_FINE_PRESUNTA",aValore); }
  public void setDataFineReclusione(Date aValore)               { setDate("DATA_FINE_RECLUSIONE",aValore); }
  public void setDataInizioArresto(Date aValore)                { setDate("DATA_INIZIO_ARRESTO",aValore); }
  public void setFlagErgastolo(String aValore)                  { setString("FLAG_ERGASTOLO",aValore); }
  public void setDataInizioIsolamentoDiurno(Date aValore)       { setDate("DATA_INIZIO_ISOLAMENTO_DIURNO",aValore); }
  public void setDataFineIsolamentoDiurno(Date aValore)         { setDate("DATA_FINE_ISOLAMENTO_DIURNO",aValore); }
  public void setNumAnniIsolamentoDiurno(BigDecimal aValore)    { setBigDecimal("NUM_ANNI_ISOLAMENTO_DIURNO",aValore); }
  public void setNumMesiIsolamentoDiurno(BigDecimal aValore)    { setBigDecimal("NUM_MESI_ISOLAMENTO_DIURNO",aValore); }
  public void setNumGiorniIsolamentoDiurno(BigDecimal aValore)  { setBigDecimal("NUM_GIORNI_ISOLAMENTO_DIURNO",aValore); }
  public void setMisAltIdMisuraAlternativa(BigDecimal aValore)  { setBigDecimal("MIS_ALT_ID_MISURA_ALTERNATIVA",aValore); }
  public void setFlagPenaSospesa(String aValore )               { setString("FLAG_PENA_SOSPESA", aValore); }

  
  public GenericModel getModel() throws DAOException
  {
    return new PenaResiduaModel(
                                 getIdPenaResidua() ,
                                 getDataInizio() ,
                                 getDataFine() ,
                                 getNumAnniReclusione() ,
                                 getNumMesiReclusione() ,
                                 getNumGiorniReclusione() ,
                                 getImportoMulta() ,
                                 getNumAnniArresto() ,
                                 getNumMesiArresto() ,
                                 getNumGiorniArresto() ,
                                 getImportoAmmenda() ,
                                 getDiesAQuo() ,
                                 getCodOperatoreInserimento() ,
                                 getDataInserimento() ,
                                 getCodUfficioInserimento() ,
                                 "",
                                 getCodOperatoreAggiornamento() ,
                                 getDataAggiornamento() ,
                                 getCodUfficioAggiornamento() ,
                                 "",
                                 getEveIdEvento() ,
                                 getFasSieIdFascicoloSiep(),
                                 getFlagValidato(),
                                 getDataFinePresunta(),
                                 getDataFineReclusione(),
                                 getDataInizioArresto(),
                                 getFlagErgastolo(),
                                 getDataInizioIsolamentoDiurno(),
                                 getDataFineIsolamentoDiurno(),
                                 getNumAnniIsolamentoDiurno(),
                                 getNumMesiIsolamentoDiurno(),
                                 getNumGiorniIsolamentoDiurno(),
                                 getMisAltIdMisuraAlternativa() ,
                                 getFlagPenaSospesa()
                               );
    }

  /**
   * 
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModel(PenaResiduaModel aModel) throws DAOException
  {
    setIdPenaResidua( aModel.getIdPenaResidua() );
    setDataInizio( aModel.getDataInizio() );
    setDataFine( aModel.getDataFine() );
    setNumAnniReclusione( aModel.getNumAnniReclusione() );
    setNumMesiReclusione( aModel.getNumMesiReclusione() );
    setNumGiorniReclusione( aModel.getNumGiorniReclusione() );
    setImportoMulta( aModel.getImportoMulta() );
    setNumAnniArresto( aModel.getNumAnniArresto() );
    setNumMesiArresto( aModel.getNumMesiArresto() );
    setNumGiorniArresto( aModel.getNumGiorniArresto() );
    setImportoAmmenda( aModel.getImportoAmmenda() );
    setDiesAQuo( aModel.getDiesAQuo() );
    setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
    setDataInserimento( aModel.getDataInserimento() );
    setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
    setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
    setEveIdEvento( aModel.getEveIdEvento() );
    setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
    setFlagValidato(aModel.getFlagValidato());
    setDataFinePresunta(aModel.getDataFinePresunta());
    setDataFineReclusione(aModel.getDataFineReclusione());
    setDataInizioArresto(aModel.getDataInizioArresto());
    setFlagErgastolo(aModel.getFlagErgastolo());
    setDataInizioIsolamentoDiurno(aModel.getDataInizioIsolamentoDiurno());
    setDataFineIsolamentoDiurno(aModel.getDataFineIsolamentoDiurno());
    setNumAnniIsolamentoDiurno(aModel.getNumAnniIsolamentoDiurno());
    setNumMesiIsolamentoDiurno(aModel.getNumMesiIsolamentoDiurno());
    setNumGiorniIsolamentoDiurno(aModel.getNumGiorniIsolamentoDiurno());
    setMisAltIdMisuraAlternativa(aModel.getMisAltIdMisuraAlternativa());
    setFlagPenaSospesa( aModel.getFlagPenaSospesa() );
  }

  /**
   * 
   * @param aModel
   * @throws DAOException
   */
  public void setDAOFromModelForUpdate(PenaResiduaModel aModel) throws DAOException
  {
    //setIdPenaResidua( aModel.getIdPenaResidua() );
    setFasSieIdFascicoloSiep ( aModel.getFasSieIdFascicoloSiep() );
    setEveIdEvento           ( aModel.getEveIdEvento() );

    setDataInizio          ( aModel.getDataInizio() );
    setDataFine            ( aModel.getDataFine() );
    setDataFinePresunta    ( aModel.getDataFinePresunta());
    setDataFineReclusione  ( aModel.getDataFineReclusione());
    setDataInizioArresto   ( aModel.getDataInizioArresto());
    
    setNumAnniReclusione   ( aModel.getNumAnniReclusione() );
    setNumMesiReclusione   ( aModel.getNumMesiReclusione() );
    setNumGiorniReclusione ( aModel.getNumGiorniReclusione() );
    setImportoMulta        ( aModel.getImportoMulta() );
    
    setNumAnniArresto      ( aModel.getNumAnniArresto() );
    setNumMesiArresto      ( aModel.getNumMesiArresto() );
    setNumGiorniArresto    ( aModel.getNumGiorniArresto() );
    setImportoAmmenda      ( aModel.getImportoAmmenda() );
    
    setDiesAQuo( aModel.getDiesAQuo() );
    
    // Dati Ergastolo 
    setFlagErgastolo(aModel.getFlagErgastolo());
    
    setDataInizioIsolamentoDiurno ( aModel.getDataInizioIsolamentoDiurno());
    setDataFineIsolamentoDiurno   ( aModel.getDataFineIsolamentoDiurno());
    setNumAnniIsolamentoDiurno    ( aModel.getNumAnniIsolamentoDiurno());
    setNumMesiIsolamentoDiurno    ( aModel.getNumMesiIsolamentoDiurno());
    setNumGiorniIsolamentoDiurno  ( aModel.getNumGiorniIsolamentoDiurno());

    setMisAltIdMisuraAlternativa(aModel.getMisAltIdMisuraAlternativa());
    setFlagValidato    ( aModel.getFlagValidato());
    setFlagPenaSospesa ( aModel.getFlagPenaSospesa() );

    setCodOperatoreAggiornamento ( aModel.getCodOperatoreAggiornamento() );
    setDataAggiornamento         ( aModel.getDataAggiornamento() );
    setCodUfficioAggiornamento   ( aModel.getCodUfficioAggiornamento() );
    
    // Update per chiave record
    setCondizioneUpdate(aModel.getIdPenaResidua());
  }

/*
  public void setCondizione(PenaResiduaModel aModel)
  {
    String lCondizioni = new String();

    boolean lInserito = false;
    if ( lInserito ) setCondition(lCondizioni);
  }
*/
  
  public void setCondizioneUpdate(BigDecimal key)
  {
    setCondition(" ID_PENA_RESIDUA = " + key );
  }

  public void setCondizioneUpdateNonValidato(BigDecimal key)
  {
    setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + key + " AND (FLAG_VALIDATO='N' OR FLAG_VALIDATO IS NULL)");
  }

  public void setCondizioneUpdateByEventoNonValidato(BigDecimal key)
  {
    setCondition(" EVE_ID_EVENTO = " + key + " AND (FLAG_VALIDATO='N' OR FLAG_VALIDATO IS NULL)");
  }

  public void setCondizioneByIdEvento(BigDecimal aIdEvento)
  {
    setCondition(" EVE_ID_EVENTO = " + aIdEvento );
  }

}
