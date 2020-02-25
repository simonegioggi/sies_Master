package siap.siep.cumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.siep.cumulo.model.CumuloModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
* <p>Title: CumuloDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Cumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class CumuloDAO extends TableDAO
{
  public CumuloDAO (Connection con)
  {
       super(con);
       setTable("CUMULO");

       //Settare la Sequence e i campi chiave
       setSequenceField("ID_CUMULO", "CUM_SEQ");

       setField("ID_CUMULO", BIG_DECIMAL);
       setField("ID_FASCICOLO_SIEP_CUMULATO", BIG_DECIMAL);
       setField("CHIAVE_ANNO_FAS_CUMULATO", BIG_DECIMAL);
       setField("CHIAVE_PROGR_FAS_CUMULATO", BIG_DECIMAL);
       setField("COD_TIPO_UFFICIO_FAS_CUMULATO", STRING);
       setField("COD_LUOGO_UFFICIO_FAS_CUMULATO", STRING);
       setField("COD_UFFICIO_FAS_CUMULATO", STRING);
       setField("COD_TIPO_CUMULO", STRING);
       setField("DATA_RICHIESTA_FASCICOLO", DATE);
       setField("DATA_PERVENIMENTO_FASCICOLO", DATE);
       setField("DATA_CUMULO", DATE);
       setField("COD_MOTIVO_SOSPENSIONE_CUMULO", STRING);
       setField("DATA_SOSPENSIONE_CUMULO", DATE);
       setField("NOTE", STRING);
       setField("COD_OPERATORE_INSERIMENTO", STRING);
       setField("DATA_INSERIMENTO", DATE);
       setField("COD_UFFICIO_INSERIMENTO", STRING);
       setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
       setField("DATA_AGGIORNAMENTO", DATE);
       setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
       setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
       setField("FLAG_TIPO_STAMPA", STRING);
       setField("SEN_ID_SENTENZA", BIG_DECIMAL);
       setField("FLAG_VALIDATO", STRING);

       setField("EVE_ID_EVENTO", BIG_DECIMAL);
       setField("PRIMO_CUMULO", STRING);

       setField("ISTR_ID_ISTRUTTORIA_CUMULO", BIG_DECIMAL);
  }


  //
  // METODI GET()
  //

  public BigDecimal      getIdCumulo()                    throws DAOException  { return getBigDecimal("ID_CUMULO"); }
  public BigDecimal      getIdFascicoloSiepCumulato()     throws DAOException  { return getBigDecimal("ID_FASCICOLO_SIEP_CUMULATO"); }
  public BigDecimal      getChiaveAnnoFasCumulato()       throws DAOException  { return getBigDecimal("CHIAVE_ANNO_FAS_CUMULATO"); }
  public BigDecimal      getChiaveProgrFasCumulato()      throws DAOException  { return getBigDecimal("CHIAVE_PROGR_FAS_CUMULATO"); }
  public String          getCodTipoUfficioFasCumulato()   throws DAOException  { return getString("COD_TIPO_UFFICIO_FAS_CUMULATO"); }
  public String          getCodLuogoUfficioFasCumulato()  throws DAOException  { return getString("COD_LUOGO_UFFICIO_FAS_CUMULATO"); }
  public String          getCodUfficioFasCumulato()       throws DAOException  { return getString("COD_UFFICIO_FAS_CUMULATO"); }
  public String          getCodTipoCumulo()               throws DAOException  { return getString("COD_TIPO_CUMULO"); }
  public Date            getDataRichiestaFascicolo()      throws DAOException  { return getDate("DATA_RICHIESTA_FASCICOLO"); }
  public Date            getDataPervenimentoFascicolo()   throws DAOException  { return getDate("DATA_PERVENIMENTO_FASCICOLO"); }
  public Date            getDataCumulo()                  throws DAOException  { return getDate("DATA_CUMULO"); }
  public String          getCodMotivoSospensioneCumulo()  throws DAOException  { return getString("COD_MOTIVO_SOSPENSIONE_CUMULO"); }
  public Date            getDataSospensioneCumulo()       throws DAOException  { return getDate("DATA_SOSPENSIONE_CUMULO"); }
  public String          getNote()                        throws DAOException  { return getString("NOTE"); }
  public String          getCodOperatoreInserimento()     throws DAOException  { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date            getDataInserimento()             throws DAOException  { return getDate("DATA_INSERIMENTO"); }
  public String          getCodUfficioInserimento()       throws DAOException  { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String          getCodOperatoreAggiornamento()   throws DAOException  { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date            getDataAggiornamento()           throws DAOException  { return getDate("DATA_AGGIORNAMENTO"); }
  public String          getCodUfficioAggiornamento()     throws DAOException  { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public BigDecimal      getFasSieIdFascicoloSiep()       throws DAOException  { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }
  public String          getFlagTipoStampa()              throws DAOException  { return getString("FLAG_TIPO_STAMPA"); }
  public BigDecimal      getSenIdSentenza()               throws DAOException  { return getBigDecimal("SEN_ID_SENTENZA"); }
  public String          getFlagValidato()                throws DAOException  { return getString("FLAG_VALIDATO"); }

  public BigDecimal      getEveIdEvento()                 throws DAOException  { return getBigDecimal("EVE_ID_EVENTO"); }
  public String          getPrimoCumulo()                 throws DAOException  { return getString("PRIMO_CUMULO"); }

  public BigDecimal      getIstrIdIstruttoriaCumulo()     throws DAOException  { return getBigDecimal("ISTR_ID_ISTRUTTORIA_CUMULO"); }

  //============================================================================
  // METODI SET()
  //============================================================================
  public void    setIdCumulo                      (BigDecimal aValore )  { setBigDecimal ("ID_CUMULO", aValore); }
  public void    setIdFascicoloSiepCumulato       (BigDecimal aValore )  { setBigDecimal ("ID_FASCICOLO_SIEP_CUMULATO", aValore); }
  public void    setChiaveAnnoFasCumulato         (BigDecimal aValore )  { setBigDecimal ("CHIAVE_ANNO_FAS_CUMULATO", aValore); }
  public void    setChiaveProgrFasCumulato        (BigDecimal aValore )  { setBigDecimal ("CHIAVE_PROGR_FAS_CUMULATO", aValore); }
  public void    setCodTipoUfficioFasCumulato     (String     aValore )  { setString     ("COD_TIPO_UFFICIO_FAS_CUMULATO", aValore); }
  public void    setCodLuogoUfficioFasCumulato    (String     aValore )  { setString     ("COD_LUOGO_UFFICIO_FAS_CUMULATO", aValore); }
  public void    setCodUfficioFasCumulato         (String     aValore )  { setString     ("COD_UFFICIO_FAS_CUMULATO", aValore); }
  public void    setCodTipoCumulo                 (String     aValore )  { setString     ("COD_TIPO_CUMULO", aValore); }
  public void    setDataRichiestaFascicolo        (Date       aValore )  { setDate       ("DATA_RICHIESTA_FASCICOLO", aValore); }
  public void    setDataPervenimentoFascicolo     (Date       aValore )  { setDate       ("DATA_PERVENIMENTO_FASCICOLO", aValore); }
  public void    setDataCumulo                    (Date       aValore )  { setDate       ("DATA_CUMULO", aValore); }
  public void    setCodMotivoSospensioneCumulo    (String     aValore )  { setString     ("COD_MOTIVO_SOSPENSIONE_CUMULO", aValore); }
  public void    setDataSospensioneCumulo         (Date       aValore )  { setDate       ("DATA_SOSPENSIONE_CUMULO", aValore); }
  public void    setNote                          (String     aValore )  { setString     ("NOTE", aValore); }
  public void    setCodOperatoreInserimento       (String     aValore )  { setString     ("COD_OPERATORE_INSERIMENTO", aValore); }
  public void    setDataInserimento               (Date       aValore )  { setDate       ("DATA_INSERIMENTO", aValore); }
  public void    setCodUfficioInserimento         (String     aValore )  { setString     ("COD_UFFICIO_INSERIMENTO", aValore); }
  public void    setCodOperatoreAggiornamento     (String     aValore )  { setString     ("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void    setDataAggiornamento             (Date       aValore )  { setDate       ("DATA_AGGIORNAMENTO", aValore); }
  public void    setCodUfficioAggiornamento       (String     aValore )  { setString     ("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void    setFasSieIdFascicoloSiep         (BigDecimal aValore )  { setBigDecimal ("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }
  public void    setFlagTipoStampa                (String     aValore)   { setString     ("FLAG_TIPO_STAMPA", aValore); }
  public void    setSenIdSentenza                 (BigDecimal aValore )  { setBigDecimal ("SEN_ID_SENTENZA", aValore); }
  public void    setFlagValidato                  (String     aValore)   { setString     ("FLAG_VALIDATO", aValore); }

  public void    setEveIdEvento                   (BigDecimal aValore)   { setBigDecimal ("EVE_ID_EVENTO", aValore); }
  public void    setPrimoCumulo                   (String     aValore)   { setString     ("PRIMO_CUMULO", aValore); }

  public void    setIstrIdIstruttoriaCumulo       (BigDecimal aValore)   { setBigDecimal ("ISTR_ID_ISTRUTTORIA_CUMULO", aValore); }
      
  /**
   * 
   */
  public GenericModel getModel() throws DAOException
  {
    return new CumuloModel(getIdCumulo() ,
                           getIdFascicoloSiepCumulato() ,
                           getChiaveAnnoFasCumulato() ,
                           getChiaveProgrFasCumulato() ,
                           getCodTipoUfficioFasCumulato() ,
                           "",
                           getCodLuogoUfficioFasCumulato() ,
                           "",
                           getCodUfficioFasCumulato() ,
                           "",
                           getCodTipoCumulo() ,
                           "",
                           getDataRichiestaFascicolo() ,
                           getDataPervenimentoFascicolo() ,
                           getDataCumulo() ,
                           getCodMotivoSospensioneCumulo() ,
                           "",
                           getDataSospensioneCumulo() ,
                           getNote() ,
                           getCodOperatoreInserimento() ,
                           getDataInserimento() ,
                           getCodUfficioInserimento() ,
                           "",
                           getCodOperatoreAggiornamento() ,
                           getDataAggiornamento() ,
                           getCodUfficioAggiornamento() ,
                           "",
                           getFasSieIdFascicoloSiep(),
                           getFlagTipoStampa(),
                           "",
                           getSenIdSentenza(),
                           getFlagValidato(),
                           getEveIdEvento(),
                           getPrimoCumulo(),
                           getIstrIdIstruttoriaCumulo()
                          );
  }

  /**
   * 
   * @param aModel
   * @throws DAOException
   */
  public void   setDAOFromModel(CumuloModel aModel) throws DAOException
  {
         setIdCumulo( aModel.getIdCumulo() );
         setIdFascicoloSiepCumulato( aModel.getIdFascicoloSiepCumulato() );
         setChiaveAnnoFasCumulato( aModel.getChiaveAnnoFasCumulato() );
         setChiaveProgrFasCumulato( aModel.getChiaveProgrFasCumulato() );
         setCodTipoUfficioFasCumulato( aModel.getCodTipoUfficioFasCumulato() );
         setCodLuogoUfficioFasCumulato( aModel.getCodLuogoUfficioFasCumulato() );
         setCodUfficioFasCumulato( aModel.getCodUfficioFasCumulato() );
         setCodTipoCumulo( aModel.getCodTipoCumulo() );
         setDataRichiestaFascicolo( aModel.getDataRichiestaFascicolo() );
         setDataPervenimentoFascicolo( aModel.getDataPervenimentoFascicolo() );
         setDataCumulo( aModel.getDataCumulo() );
         setCodMotivoSospensioneCumulo( aModel.getCodMotivoSospensioneCumulo() );
         setDataSospensioneCumulo( aModel.getDataSospensioneCumulo() );
         setNote( aModel.getNote() );
         setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
         setDataInserimento( aModel.getDataInserimento() );
         setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
         setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
         setDataAggiornamento( aModel.getDataAggiornamento() );
         setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
         setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
        //if(aModel.getFlagTipoStampa() != null)
         setFlagTipoStampa(aModel.getFlagTipoStampa());
         setSenIdSentenza(aModel.getSenIdSentenza());
         setFlagValidato(aModel.getFlagValidato());

         setEveIdEvento(aModel.getEveIdEvento());
         setPrimoCumulo(aModel.getPrimoCumulo());
         
         setIstrIdIstruttoriaCumulo(aModel.getIstrIdIstruttoriaCumulo());
    }

   /**
    * 
    * @param aModel
    * @throws DAOException
    */
   public void setDAOFromModelForUpdate(CumuloModel aModel) throws DAOException
   {
        if(aModel.getIdCumulo()  != null)
         setIdCumulo( aModel.getIdCumulo() );
        if(aModel.getIdFascicoloSiepCumulato() != null)
         setIdFascicoloSiepCumulato( aModel.getIdFascicoloSiepCumulato() );

         setChiaveAnnoFasCumulato( aModel.getChiaveAnnoFasCumulato() );
         setChiaveProgrFasCumulato( aModel.getChiaveProgrFasCumulato() );
         setCodTipoUfficioFasCumulato( aModel.getCodTipoUfficioFasCumulato() );
         setCodLuogoUfficioFasCumulato( aModel.getCodLuogoUfficioFasCumulato() );
         setCodUfficioFasCumulato( aModel.getCodUfficioFasCumulato() );
         setCodTipoCumulo( aModel.getCodTipoCumulo() );
         setDataRichiestaFascicolo( aModel.getDataRichiestaFascicolo() );
         setDataPervenimentoFascicolo( aModel.getDataPervenimentoFascicolo() );
         setDataCumulo( aModel.getDataCumulo() );
         setCodMotivoSospensioneCumulo( aModel.getCodMotivoSospensioneCumulo() );
         setDataSospensioneCumulo( aModel.getDataSospensioneCumulo() );
         setNote( aModel.getNote() );
         setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
         setDataAggiornamento( aModel.getDataAggiornamento() );
         setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
         setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
         setFlagTipoStampa(aModel.getFlagTipoStampa());
         setSenIdSentenza( aModel.getSenIdSentenza() );
         setFlagValidato(aModel.getFlagValidato());

         setEveIdEvento(aModel.getEveIdEvento());
         setPrimoCumulo(aModel.getPrimoCumulo());
         
         setIstrIdIstruttoriaCumulo(aModel.getIstrIdIstruttoriaCumulo());

         //
         setCondizioneUpdate(aModel.getIdCumulo());
  }

  /**
   * 
   * @param aModel
   */
  public void setCondizione(CumuloModel aModel)
  {
       String lCondizioni = new String();

       boolean lInserito = false;
       if ( lInserito ) setCondition(lCondizioni);
  }

  /**
   * 
   * @param key
   */
  public void setCondizioneUpdate(BigDecimal key)
  {
     setCondition(" ID_CUMULO = " + key );
  }

  /**
   * 
   * @param key
   */
  public void setCondizioneUpdateByFasc(BigDecimal key)
  {
      setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + key );
  }

  /**
   * Imposta le condizioni di Update per Fascicoli ed Istruttoria Cumulo
   * @param key
   */
  public void setCondizioneUpdateByFascIstr(BigDecimal aIdFasc, BigDecimal aIdIstruttoria)
  {
      setCondition(" FAS_SIE_ID_FASCICOLO_SIEP = " + aIdFasc + " AND ISTR_ID_ISTRUTTORIA_CUMULO = "+aIdIstruttoria );
  }
  
  public void setCondizioneUpdateByIdSentenza(BigDecimal key)
  {
      setCondition(" SEN_ID_SENTENZA = " + key + "AND (FLAG_VALIDATO = 'N' OR FLAG_VALIDATO IS NULL)");
  }
  
}
