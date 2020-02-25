package siap.sico.magistratocompetente.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.dao.SIAPTableDAO;
import siap.sico.magistratocompetente.model.MagistratoCompetenteModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;

/**
* <p>Title: MagistratoCompetenteDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella MagistratoCompetente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class MagistratoCompetenteDAO extends SIAPTableDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public MagistratoCompetenteDAO (Connection con)
  {
       super(con);
       setTable("MAGISTRATO_COMPETENTE");

       //Settare la Sequence e i campi chiave

       setField("DATA_INIZIO", DATE);
       setField("DATA_FINE", DATE);
       setField("COD_RUOLO_MAGISTRATO", STRING);
       setField("COD_OPERATORE_INSERIMENTO", STRING);
       setField("DATA_INSERIMENTO", DATE);
       setField("COD_UFFICIO_INSERIMENTO", STRING);
       setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
       setField("DATA_AGGIORNAMENTO", DATE);
       setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
       setField("MAG_COD_MAGISTRATO", STRING);
       setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
  }


  //=================================
  // METODI GET()
  //=================================
  public Date        getDataInizio()                throws DAOException  { return getDate("DATA_INIZIO"); }
  public Date        getDataFine()                  throws DAOException  { return getDate("DATA_FINE"); }
  public String      getCodRuoloMagistrato()        throws DAOException  { return getString("COD_RUOLO_MAGISTRATO"); }
  public String      getCodOperatoreInserimento()   throws DAOException  { return getString("COD_OPERATORE_INSERIMENTO"); }
  public Date        getDataInserimento()           throws DAOException  { return getDate("DATA_INSERIMENTO"); }
  public String      getCodUfficioInserimento()     throws DAOException  { return getString("COD_UFFICIO_INSERIMENTO"); }
  public String      getCodOperatoreAggiornamento() throws DAOException  { return getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public Date        getDataAggiornamento()         throws DAOException  { return getDate("DATA_AGGIORNAMENTO"); }
  public String      getCodUfficioAggiornamento()   throws DAOException  { return getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public String      getMagCodMagistrato()          throws DAOException  { return getString("MAG_COD_MAGISTRATO"); }
  public BigDecimal  getFasSieIdFascicoloSiep()     throws DAOException  { return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"); }


  //=================================
  // METODI SET()
  //=================================
  public void    setDataInizio(Date aValore )                   { setDate   ("DATA_INIZIO", aValore); }
  public void    setDataFine(Date aValore )                     { setDate   ("DATA_FINE", aValore); }
  public void    setCodRuoloMagistrato(String aValore )         { setString ("COD_RUOLO_MAGISTRATO", aValore); }
  public void    setCodOperatoreInserimento(String aValore )    { setString ("COD_OPERATORE_INSERIMENTO", aValore); }
  public void    setDataInserimento(Date aValore )              { setDate   ("DATA_INSERIMENTO", aValore); }
  public void    setCodUfficioInserimento(String aValore )      { setString ("COD_UFFICIO_INSERIMENTO", aValore); }
  public void    setCodOperatoreAggiornamento(String aValore )  { setString ("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void    setDataAggiornamento(Date aValore )            { setDate   ("DATA_AGGIORNAMENTO", aValore); }
  public void    setCodUfficioAggiornamento(String aValore )    { setString ("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void    setMagCodMagistrato(String aValore )           { setString ("MAG_COD_MAGISTRATO", aValore); }
  public void    setFasSieIdFascicoloSiep(BigDecimal aValore )  { setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore); }


  public GenericModel getModel() throws DAOException
  {
    return new MagistratoCompetenteModel(getDataInizio() ,
                                         getDataFine() ,
                                         getCodRuoloMagistrato() ,
                                         "",
                                         getCodOperatoreInserimento() ,
                                         getDataInserimento() ,
                                         getCodUfficioInserimento() ,
                                         "",
                                         getCodOperatoreAggiornamento() ,
                                         getDataAggiornamento() ,
                                         getCodUfficioAggiornamento() ,
                                         "",
                                         getMagCodMagistrato() ,
                                         getFasSieIdFascicoloSiep()
                                        );
  }


  public void   setDAOFromModel(MagistratoCompetenteModel aModel) throws DAOException
  {
     setMagCodMagistrato( aModel.getMagCodMagistrato() );
     setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
     setCodRuoloMagistrato( aModel.getCodRuoloMagistrato() );
     setDataInizio( aModel.getDataInizio() );
     setDataFine( aModel.getDataFine() );

     setCodOperatoreInserimento ( aModel.getCodOperatoreInserimento() );
     setDataInserimento         ( aModel.getDataInserimento() );
     setCodUfficioInserimento   ( aModel.getCodUfficioInserimento() );
     
     setCodOperatoreAggiornamento ( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento         ( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento   ( aModel.getCodUfficioAggiornamento() );
  }

  /**
   * 
   * @param aModel
   * @throws DAOException
   */
  public void   setDAOFromModelForUpdate(MagistratoCompetenteModel aModel) throws DAOException
  {
     setDataInizio( aModel.getDataInizio() );
     setDataFine( aModel.getDataFine() );
     setCodRuoloMagistrato( aModel.getCodRuoloMagistrato() );
     setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
     setDataAggiornamento( aModel.getDataAggiornamento() );
     setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
     setMagCodMagistrato( aModel.getMagCodMagistrato() );
     setFasSieIdFascicoloSiep( aModel.getFasSieIdFascicoloSiep() );
     setCondizioneUpdate(aModel.getFasSieIdFascicoloSiep(),aModel.getMagCodMagistrato());
  }

  /**
   * 
   * @param aModel
   */
  public void setCondizione(MagistratoCompetenteModel aModel)
  {
     String lCondizioni = new String();

     boolean lInserito = false;
     if ( lInserito ) setCondition(lCondizioni);
  }


  /* public void setCondizioneUpdate(BigDecimal key)
       {
   setCondition(" ID_MAGISTRATO_COMPETENTE = " + key );
     }*/

  public void setCondizioneUpdate(BigDecimal key, String lKey )
  {
    String lCondizioni = null;

    lCondizioni = " MAG_COD_MAGISTRATO = " + "'" + lKey + "'" ;
    lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP = " + key ;

    setCondition(lCondizioni);
  }

  /**
   * Imposta la condizione di Update sul magistrato correntemente assegnatario
   * del fascicolo passato in input.
   * n.b. il magistrato correntemente competente sul fascicolo è quello senza data
   *      fine
   * @param idFascicoloSiep - id del fascicolo
   */
  public void setCondizioneUpdateMagistratoCorrente(BigDecimal idFascicoloSiep )
  {
    String lCondizioni = null;

    lCondizioni = " FAS_SIE_ID_FASCICOLO_SIEP = " + idFascicoloSiep ;
    lCondizioni += " AND DATA_FINE is null ";
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("lCondizioni = "+lCondizioni);
    
    setCondition(lCondizioni);
  }

}