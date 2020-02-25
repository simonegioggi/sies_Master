package siap.sius.depositosentenza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.dao.SIAPTableDAO;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.GenericModel;

/**
* <p>Title: DepositoSentenzaDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella DepositoSentenza</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class DepositoSentenzaDAO extends SIAPTableDAO
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public DepositoSentenzaDAO (Connection con)
  {
    super(con);

    setTable("DEPOSITO_SENTENZA");
    setFieldKey("ID_DEPOSITO_SENTENZA",BIG_DECIMAL);
    setSequenceField("ID_DEPOSITO_SENTENZA","DEP_SEN_SEQ");

    setField("ID_DEPOSITO_SENTENZA", BIG_DECIMAL);
    setField("ANNO_SENTENZA", BIG_DECIMAL);
    setField("NUM_SENTENZA", BIG_DECIMAL);
    setField("COD_TIPO_SENTENZA", STRING);
    setField("DATA_EMISSIONE", DATE);
    setField("DATA_DEPOSITO", DATE);
    setField("COD_MAGISTRATO", STRING);
    setField("ALTRI_DESTINATARI", STRING);
    setField("DATA_PARERE_PG", DATE);
    setField("COD_TIPO_PARERE_PG", STRING);
    setField("DATA_RICORSO_IMPUGNAZIONE", DATE);
    setField("DATA_INVIO_ATTI_IMPUGNAZIONE", DATE);
    setField("DATA_SENTENZA_IMPUGNAZIONE", DATE);
    setField("TENORE_SENTENZA_IMPUGNAZIONE", STRING);
    setField("NOTE", STRING);
    setField("SENTENZE_RIFERIMENTO", STRING);
    setField("COD_PROCURA_ESECUZIONE", STRING);
    setField("COD_UFFICIO_COMP", STRING);
    setField("ID_EVENTO_GENERATO", BIG_DECIMAL);
    setField("COD_OPERATORE_INSERIMENTO", STRING);
    setField("DATA_INSERIMENTO", DATE);
    setField("COD_UFFICIO_INSERIMENTO", STRING);
    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
    setField("DATA_AGGIORNAMENTO", DATE);
    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
    setField("GEN_PRID_GENERALE_PROCEDIMENTO", BIG_DECIMAL);
    setField("ULTERIORE_DESCRIZIONE", STRING);
    setField("COD_NATURA_PROVVEDIMENTO", STRING);
    setField("OGGETTO_PROCEDIMENTO", STRING);
    setField("DATA_UDIENZA", DATE);
  }

  //
  // METODI GET()
  //
  public BigDecimal	getIdDepositoSentenza() 	  	  throws DAOException  { return  getBigDecimal("ID_DEPOSITO_SENTENZA"); }
  public BigDecimal	getAnnoSentenza()             	  throws DAOException  { return  getBigDecimal("ANNO_SENTENZA"); }
  public BigDecimal	getNumSentenza() 			  	  throws DAOException  { return  getBigDecimal("NUM_SENTENZA"); }
  public String	    getCodTipoSentenza()          	  throws DAOException  { return  getString("COD_TIPO_SENTENZA"); }
  public Date	    getDataEmissione() 	  		  	  throws DAOException  { return  getDate("DATA_EMISSIONE"); }
  public Date	    getDataDeposito()  	  		  	  throws DAOException  { return  getDate("DATA_DEPOSITO"); }
  public String	    getCodMagistrato()  	  	  	  throws DAOException  { return  getString("COD_MAGISTRATO"); }
  public String	    getAltriDestinatari() 	  	  	  throws DAOException  { return  getString("ALTRI_DESTINATARI"); }
  public Date	    getDataParerePg()  	  		  	  throws DAOException  { return  getDate("DATA_PARERE_PG"); }
  public String	    getCodTipoParerePg() 	  	  	  throws DAOException  { return  getString("COD_TIPO_PARERE_PG"); }
  public Date	    getDataRicorsoImpugnazione()   	  throws DAOException  { return  getDate("DATA_RICORSO_IMPUGNAZIONE"); }
  public Date	    getDataInvioAttiImpugnazione() 	  throws DAOException  { return  getDate("DATA_INVIO_ATTI_IMPUGNAZIONE"); }
  public Date	    getDataSentenzaImpugnazione()  	  throws DAOException  { return  getDate("DATA_SENTENZA_IMPUGNAZIONE"); }
  public String	    getTenoreSentenzaImpugnazione()   throws DAOException  { return  getString("TENORE_SENTENZA_IMPUGNAZIONE"); }
  public String	    getNote()  	  					  throws DAOException  { return  getString("NOTE"); }
  public String	    getSentenzeRiferimento()  	  	  throws DAOException  { return  getString("SENTENZE_RIFERIMENTO"); }
  public String	    getCodProcuraEsecuzione()  	  	  throws DAOException  { return  getString("COD_PROCURA_ESECUZIONE"); }
  public String	    getCodUfficioComp() 	  		  throws DAOException  { return  getString("COD_UFFICIO_COMP"); }
  public BigDecimal	getIdEventoGenerato()  	  	      throws DAOException  { return  getBigDecimal("ID_EVENTO_GENERATO"); }
  public String	    getCodOperatoreInserimento()  	  throws DAOException  { return  getString("COD_OPERATORE_INSERIMENTO"); }
  public Date	    getDataInserimento()  	          throws DAOException  { return  getDate("DATA_INSERIMENTO"); }
  public String	    getCodUfficioInserimento()  	  throws DAOException  { return  getString("COD_UFFICIO_INSERIMENTO"); }
  public String	    getCodOperatoreAggiornamento()    throws DAOException  { return  getString("COD_OPERATORE_AGGIORNAMENTO"); }
  public String	    getCodUfficioAggiornamento()  	  throws DAOException  { return  getString("COD_UFFICIO_AGGIORNAMENTO"); }
  public Date	    getDataAggiornamento()   	      throws DAOException  { return  getDate("DATA_AGGIORNAMENTO"); } 
  public BigDecimal	getGenPridGeneraleProcedimento()  throws DAOException  { return  getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO"); }
  public String	    getUlterioreDescrizione()  	      throws DAOException  { return  getString("ULTERIORE_DESCRIZIONE"); }
  public String	    getCodNaturaProvvedimento()  	  throws DAOException  { return  getString("COD_NATURA_PROVVEDIMENTO"); }
  public String	    getOggettoProcedimento()  	  	  throws DAOException  { return  getString("OGGETTO_PROCEDIMENTO"); }
  public Date	    getDataUdienza()   	      		  throws DAOException  { return  getDate("DATA_UDIENZA"); }
  
  //
  // METODI SET()
  //
  public void    setIdDepositoSentenza(BigDecimal aValore ) 	  	  throws DAOException  { setBigDecimal("ID_DEPOSITO_SENTENZA", aValore); }
  public void    setAnnoSentenza(BigDecimal aValore )             	  throws DAOException  { setBigDecimal("ANNO_SENTENZA", aValore); }
  public void    setNumSentenza(BigDecimal aValore ) 			  	  throws DAOException  { setBigDecimal("NUM_SENTENZA", aValore); }
  public void    setCodTipoSentenza(String aValore )          	      throws DAOException  { setString("COD_TIPO_SENTENZA", aValore); }
  public void    setDataEmissione(Date aValore ) 	  		  	      throws DAOException  { setDate("DATA_EMISSIONE", aValore); }
  public void    setDataDeposito(Date aValore )  	  		  	      throws DAOException  { setDate("DATA_DEPOSITO", aValore); }
  public void    setCodMagistrato(String aValore )  	  	  	      throws DAOException  { setString("COD_MAGISTRATO", aValore); }
  public void    setAltriDestinatari(String aValore ) 	  	  	      throws DAOException  { setString("ALTRI_DESTINATARI", aValore); }
  public void    setDataParerePg(Date aValore )  	  		  	      throws DAOException  { setDate("DATA_PARERE_PG", aValore); }
  public void    setCodTipoParerePg(String aValore ) 	  	  	      throws DAOException  { setString("COD_TIPO_PARERE_PG", aValore); }
  public void    setDataRicorsoImpugnazione(Date aValore )   	      throws DAOException  { setDate("DATA_RICORSO_IMPUGNAZIONE", aValore); }
  public void    setDataInvioAttiImpugnazione(Date aValore ) 	      throws DAOException  { setDate("DATA_INVIO_ATTI_IMPUGNAZIONE", aValore); }
  public void    setDataSentenzaImpugnazione(Date aValore )  	      throws DAOException  { setDate("DATA_SENTENZA_IMPUGNAZIONE", aValore); }
  public void    setTenoreSentenzaImpugnazione(String aValore )   	  throws DAOException  { setString("TENORE_SENTENZA_IMPUGNAZIONE", aValore); }
  public void    setNote(String aValore )  	  					  	  throws DAOException  { setString("NOTE", aValore); }
  public void    setSentenzeRiferimento(String aValore ) 	  	  	  throws DAOException  { setString("SENTENZE_RIFERIMENTO", aValore); }
  public void    setCodProcuraEsecuzione(String aValore )  	  	  	  throws DAOException  { setString("COD_PROCURA_ESECUZIONE", aValore); }
  public void    setCodUfficioComp(String aValore )	  		  	  	  throws DAOException  { setString("COD_UFFICIO_COMP", aValore); }
  public void    setIdEventoGenerato(BigDecimal aValore )  	  	      throws DAOException  { setBigDecimal("ID_EVENTO_GENERATO", aValore); }
  public void    setCodOperatoreInserimento(String aValore )  	  	  throws DAOException  { setString("COD_OPERATORE_INSERIMENTO", aValore); }
  public void    setDataInserimento(Date aValore )  	          	  throws DAOException  { setDate("DATA_INSERIMENTO", aValore); }
  public void    setCodUfficioInserimento(String aValore )  	  	  throws DAOException  { setString("COD_UFFICIO_INSERIMENTO", aValore); }
  public void    setCodOperatoreAggiornamento(String aValore )    	  throws DAOException  { setString("COD_OPERATORE_AGGIORNAMENTO", aValore); }
  public void    setCodUfficioAggiornamento(String aValore )  	  	  throws DAOException  { setString("COD_UFFICIO_AGGIORNAMENTO", aValore); }
  public void    setDataAggiornamento(Date aValore )   	      		  throws DAOException  { setDate("DATA_AGGIORNAMENTO", aValore); }
  public void    setGenPridGeneraleProcedimento(BigDecimal aValore )  throws DAOException  { setBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO", aValore); }
  public void    setUlterioreDescrizione(String aValore )  	  	  	  throws DAOException  { setString("ULTERIORE_DESCRIZIONE", aValore); }
  public void    setCodNaturaProvvedimento(String aValore )  	  	  throws DAOException  { setString("COD_NATURA_PROVVEDIMENTO", aValore); }
  public void    setOggettoProcedimento(String aValore )  	  	  	  throws DAOException  { setString("OGGETTO_PROCEDIMENTO", aValore); }
  public void    setDataUdienza(Date aValore )   	      		  	  throws DAOException  { setDate("DATA_UDIENZA", aValore); }
  
  public GenericModel getModel() throws DAOException
  {
    return new DepositoSentenzaModel(
       getIdDepositoSentenza() ,
       getAnnoSentenza() ,
       getNumSentenza() ,
       getCodTipoSentenza() ,
       getDataEmissione() ,
       getDataDeposito() ,
       getCodMagistrato() ,
       getAltriDestinatari(),
       getDataParerePg() ,
       getCodTipoParerePg(),
       getDataRicorsoImpugnazione() ,
       getDataInvioAttiImpugnazione(),
       getDataSentenzaImpugnazione() ,
       getTenoreSentenzaImpugnazione() ,
       getNote() ,
       getSentenzeRiferimento() ,
       getCodProcuraEsecuzione() ,
       getCodUfficioComp() ,
       getIdEventoGenerato() ,
       getCodOperatoreInserimento() ,
       getDataInserimento() ,
       getCodUfficioInserimento() ,
       "",
       getCodOperatoreAggiornamento() ,
       getCodUfficioAggiornamento() ,
       "",
       getDataAggiornamento() ,
       getGenPridGeneraleProcedimento(),
       getUlterioreDescrizione(),
       getCodNaturaProvvedimento(),
       getOggettoProcedimento(),
       getDataUdienza()
     );
 }

  public void setDAOFromModel(DepositoSentenzaModel aModel) throws DAOException
  {
	  setIdDepositoSentenza( aModel.getIdDepositoSentenza() );
	  setAnnoSentenza( aModel.getAnnoSentenza() );
	  setNumSentenza( aModel.getNumSentenza() );
	  setCodTipoSentenza( aModel.getCodTipoSentenza() );
	  setDataEmissione( aModel.getDataEmissione() );
	  setDataDeposito( aModel.getDataDeposito() );
	  setCodMagistrato( aModel.getCodMagistrato() );
	  setAltriDestinatari( aModel.getAltriDestinatari() );
	  setDataParerePg ( aModel.getDataParerePg() );
	  setCodTipoParerePg( aModel.getCodTipoParerePg() );
	  setDataRicorsoImpugnazione( aModel.getDataRicorsoImpugnazione() );
	  setDataInvioAttiImpugnazione( aModel.getDataInvioAttiImpugnazione() );
	  setDataSentenzaImpugnazione( aModel.getDataSentenzaImpugnazione() );
	  setTenoreSentenzaImpugnazione( aModel.getTenoreSentenzaImpugnazione() );
	  setNote( aModel.getNote() );
	  setSentenzeRiferimento( aModel.getSentenzeRiferimento() );
	  setCodProcuraEsecuzione( aModel.getCodProcuraEsecuzione() );
	  setCodUfficioComp( aModel.getCodUfficioComp() );
	  setIdEventoGenerato( aModel.getIdEventoGenerato());
	  setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
	  setDataInserimento( aModel.getDataInserimento() );
	  setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
	  setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
	  setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
	  setDataAggiornamento( aModel.getDataAggiornamento() );
	  setGenPridGeneraleProcedimento( aModel.getGenPridGeneraleProcedimento() );
	  setCodMagistrato( aModel.getCodMagistrato() );
	  setUlterioreDescrizione( aModel.getUlterioreDescrizione() );
	  setCodNaturaProvvedimento( aModel.getCodNaturaProvvedimento() );
	  setOggettoProcedimento( aModel.getOggettoProcedimento() );
	  setDataUdienza(aModel.getDataUdienza());
  }

  public void setCondizioneUpdate(BigDecimal aKey)
  {
    setCondition(" ID_DEPOSITO_SENTENZA = " + aKey );
  }

  public void setDAOFromModelForUpdate(DepositoSentenzaModel aModel) throws DAOException
  {
	  setIdDepositoSentenza( aModel.getIdDepositoSentenza() );
	  setAnnoSentenza( aModel.getAnnoSentenza() );
	  setNumSentenza( aModel.getNumSentenza() );
	  setCodTipoSentenza( aModel.getCodTipoSentenza() );
	  setDataEmissione( aModel.getDataEmissione() );
	  setDataDeposito( aModel.getDataDeposito() );
	  setCodMagistrato( aModel.getCodMagistrato() );
	  setAltriDestinatari( aModel.getAltriDestinatari() );
	  setDataParerePg ( aModel.getDataParerePg() );
	  setCodTipoParerePg( aModel.getCodTipoParerePg() );
	  setDataRicorsoImpugnazione( aModel.getDataRicorsoImpugnazione() );
	  setDataInvioAttiImpugnazione( aModel.getDataInvioAttiImpugnazione() );
	  setDataSentenzaImpugnazione( aModel.getDataSentenzaImpugnazione() );
	  setTenoreSentenzaImpugnazione( aModel.getTenoreSentenzaImpugnazione() );
	  setNote( aModel.getNote() );
	  setSentenzeRiferimento( aModel.getSentenzeRiferimento() );
	  setCodProcuraEsecuzione( aModel.getCodProcuraEsecuzione() );
	  setCodUfficioComp( aModel.getCodUfficioComp() );
	  setIdEventoGenerato( aModel.getIdEventoGenerato());
	  setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
	  setDataInserimento( aModel.getDataInserimento() );
	  setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
	  setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
	  setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
	  setDataAggiornamento( aModel.getDataAggiornamento() );
	  setGenPridGeneraleProcedimento( aModel.getGenPridGeneraleProcedimento() );
	  setCodMagistrato( aModel.getCodMagistrato() );
	  setUlterioreDescrizione( aModel.getUlterioreDescrizione() );
	  setCodNaturaProvvedimento( aModel.getCodNaturaProvvedimento() );
	  setOggettoProcedimento( aModel.getOggettoProcedimento() );
	  setDataUdienza(aModel.getDataUdienza());
    
	  setCondizioneUpdate(aModel.getIdDepositoSentenza());
  }
  
  /**
   * Valorizza le condizioni di filtro in base al contenuto 
   * del model DepositoSentenzaModel passato.
   * @param aModel
   */
  public void setCondizione(DepositoSentenzaModel aModel)
  {
    String lCondizioni = new String("");
    String lAppoggio = new String("");
    lInserito = false;
    
    if (aModel != null)
    {
      if (aModel.getAnnoSentenza() != null )
        lAppoggio =" ANNO_SENTENZA = " + aModel.getAnnoSentenza();
      lCondizioni += setAND (lAppoggio);
      
      if (aModel.getNumSentenza() != null )
        lAppoggio =" NUM_SENTENZA = " + aModel.getNumSentenza();
      lCondizioni += setAND (lAppoggio);
      
      if (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().trim().length() > 0 )
        lAppoggio =" COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "'";
      lCondizioni += setAND (lAppoggio);
       // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
       siesLogger.debug("Condizione -> " + lCondizioni);
       
       setCondition(lCondizioni);
    }
  }
  
  boolean lInserito = false;
  private String setAND(String aCondizioni)
  {
    if (lInserito)
      aCondizioni = " AND " + aCondizioni;
    
    lInserito = true;
    
    return aCondizioni;
  }
  
}