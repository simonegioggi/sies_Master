package siap.sius.depositosentenza.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: DepositoSentenzaSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella DepositoSentenza</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class DepositoSentenzaSqlDAO extends SIAPSqlDAO
{
  public DepositoSentenzaSqlDAO (Connection con)
  {
    super(con);
  }

  public void ricercaDepositoSentenzaByIdEveGenerato( BigDecimal aKey) throws DAOException
  {
    String lSql = getSqlQuery0();

    lSql += " WHERE ID_EVENTO_GENERATO = " + aKey;
    setStatement(lSql);
  }

  protected String getSqlQuery0( )
  {
      String lStatement = new String("");

      lStatement += "SELECT " ;
      lStatement +=	"ID_DEPOSITO_SENTENZA, ";
      lStatement +=	"ANNO_SENTENZA, ";
      lStatement +=	"NUM_SENTENZA, ";
      lStatement +=	"COD_TIPO_SENTENZA, ";
      lStatement +=	"DATA_EMISSIONE, ";
      lStatement +=	"DATA_DEPOSITO, ";
      lStatement +=	"COD_MAGISTRATO, ";
      lStatement +=	"ALTRI_DESTINATARI, ";
      lStatement +=	"DATA_PARERE_PG, ";
      lStatement +=	"COD_TIPO_PARERE_PG, ";
      lStatement +=	"DATA_RICORSO_IMPUGNAZIONE, ";
      lStatement +=	"DATA_INVIO_ATTI_IMPUGNAZIONE, ";
      lStatement +=	"DATA_SENTENZA_IMPUGNAZIONE, ";
      lStatement +=	"TENORE_SENTENZA_IMPUGNAZIONE, ";
      lStatement +=	"NOTE, ";
      lStatement +=	"SENTENZE_RIFERIMENTO, ";
      lStatement +=	"COD_PROCURA_ESECUZIONE, ";
      lStatement +=	"COD_UFFICIO_COMP, ";
      lStatement +=	"ID_EVENTO_GENERATO, ";
      lStatement +=	"COD_OPERATORE_INSERIMENTO, ";
      lStatement +=	"DATA_INSERIMENTO, ";
      lStatement +=	"COD_UFFICIO_INSERIMENTO, ";
      lStatement +=	"COD_OPERATORE_AGGIORNAMENTO, ";
      lStatement +=	"DATA_AGGIORNAMENTO, ";
      lStatement +=	"COD_UFFICIO_AGGIORNAMENTO, ";
      lStatement +=	"GEN_PRID_GENERALE_PROCEDIMENTO, ";
      lStatement +=	"ULTERIORE_DESCRIZIONE, ";
      lStatement +=	"COD_NATURA_PROVVEDIMENTO, ";
      lStatement +=	"OGGETTO_PROCEDIMENTO, ";
      lStatement +=	"DATA_UDIENZA ";
      
      lStatement += "FROM DEPOSITO_SENTENZA ";
     
      return lStatement;
  }
  
  //
  // METODO GETMODEL()
  //
  public GenericModel getModel() throws DAOException
  {
    DepositoSentenzaModel aModel = new  DepositoSentenzaModel();

    aModel.setIdDepositoSentenza( getBigDecimal("ID_DEPOSITO_SENTENZA") );
    aModel.setAnnoSentenza( getBigDecimal("ANNO_SENTENZA") );
    aModel.setNumSentenza( getBigDecimal("NUM_SENTENZA") );
    aModel.setCodTipoSentenza( getString("COD_TIPO_SENTENZA") );
    aModel.setDataEmissione( getDate("DATA_EMISSIONE") );
    aModel.setDataDeposito( getDate("DATA_DEPOSITO") );
    aModel.setCodMagistrato( getString("COD_MAGISTRATO") );
    aModel.setAltriDestinatari( getString("ALTRI_DESTINATARI") );
    aModel.setDataParerePg( getDate("DATA_PARERE_PG") );
    aModel.setCodTipoParerePg( getString("COD_TIPO_PARERE_PG") );
    aModel.setDataRicorsoImpugnazione( getDate("DATA_RICORSO_IMPUGNAZIONE") );
    aModel.setDataInvioAttiImpugnazione( getDate("DATA_INVIO_ATTI_IMPUGNAZIONE") );
    aModel.setDataSentenzaImpugnazione( getDate("DATA_SENTENZA_IMPUGNAZIONE") );
    aModel.setTenoreSentenzaImpugnazione( getString("TENORE_SENTENZA_IMPUGNAZIONE") );
    aModel.setNote( getString("NOTE") );
    aModel.setSentenzeRiferimento( getString("SENTENZE_RIFERIMENTO") );
    aModel.setCodProcuraEsecuzione( getString("COD_PROCURA_ESECUZIONE") );
    aModel.setCodUfficioComp( getString("COD_UFFICIO_COMP") );
    aModel.setIdEventoGenerato( getBigDecimal("ID_EVENTO_GENERATO") );
    aModel.setCodOperatoreInserimento( getString("COD_OPERATORE_INSERIMENTO") );
    aModel.setDataInserimento( getDate("DATA_INSERIMENTO") );
    aModel.setCodUfficioInserimento( getString("COD_UFFICIO_INSERIMENTO") );
    aModel.setCodOperatoreAggiornamento( getString("COD_OPERATORE_AGGIORNAMENTO") );
    aModel.setCodUfficioAggiornamento( getString("COD_UFFICIO_AGGIORNAMENTO") );
    aModel.setDataAggiornamento( getDate("DATA_AGGIORNAMENTO") );
    aModel.setGenPridGeneraleProcedimento( getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO") );
    aModel.setUlterioreDescrizione( getString("ULTERIORE_DESCRIZIONE") );
    aModel.setCodNaturaProvvedimento( getString("COD_NATURA_PROVVEDIMENTO") );
    aModel.setOggettoProcedimento( getString("OGGETTO_PROCEDIMENTO") );
    aModel.setDataUdienza( getDate("DATA_UDIENZA") );

    return aModel;
  }

  /**
   * Calcola il Max NUM_SENTENZA relativo ad un certo ufficio e all'anno in corso.
   * Il massimo NUM_SENTENZA rappresenta l'ultimo NUM_SENTENZA inserito all'interno dell'ufficio trattato.
   * @param aModel
   * @throws DAOException
   */
   public void  getProgressivoNumSentenza(DepositoSentenzaModel aModel)
     throws DAOException
   {
     String lStatement = new String();

     lStatement +=    " SELECT MAX(NUM_SENTENZA) aMAX";
     lStatement +=    " FROM DEPOSITO_SENTENZA ";
     lStatement +=    " WHERE ANNO_SENTENZA = " + aModel.getAnnoSentenza();
     lStatement +=    " AND COD_UFFICIO_INSERIMENTO = " + aModel.getCodUfficioInserimento();

     setStatement( lStatement );
   }

   /**
    * <p>Description: metodo di ricerca, restituisce il numero
    * di record in DEPOSITO_SENTENZA relativi ad un generale procedimento specificato
    * dalla sua chiave passata come primo argomento e di  tipo non presente tra quelli 
    * passati nella lista secondo argomento della funzione.
    * </p>
    * @param  BigDecimal aKey : Identificativo Generale Procedimento
    * @param aTipi : String[] elenco dei tipi Sentenza esclusi dalla ricerca.
    * @return int : numero di record trovati
    * @throws DAOException
    */
    public int getNumDepoSentenzaByGenProcEccettoTipi(BigDecimal aKey, String[] aTipi)
 		 throws DAOException
    {
      // Numero di tipi sentenza da escludere dalla ricerca
      int lNumTipi = (aTipi != null) ? aTipi.length : 0;
      // numero di record trovati
      BigDecimal lCount = null;
      int retNum = -1;

      String lStatement = "select count(*) as COUNT from DEPOSITO_SENTENZA D join EVENTO E ON  (D.ID_EVENTO_GENERATO = E.ID_EVENTO  AND (E.FLAG_DOCUMENTO_REGISTRATO IS NULL OR E.FLAG_DOCUMENTO_REGISTRATO <> 'A'))";
      lStatement += " WHERE D.GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;
      
      if (lNumTipi > 0)
      {
        lStatement += " AND D.COD_TIPO_SENTENZA NOT IN ('" + aTipi[0] + "'";
        for (int i = 1; i < lNumTipi; i++)
        {
           lStatement += ", '" + aTipi[i] + "'";
        }
        lStatement += ")";
      }

      lStatement += " AND D.COD_TIPO_SENTENZA IS NOT NULL ";

      setStatement( lStatement );

      start();
      if( next() )
      {
     	 lCount = getBigDecimal("COUNT");
     	 retNum = lCount.intValue();
      }
      
      return retNum;
   }

   public void ricercaSentenzaRimessioneAttiByKeyPerUpdate( BigDecimal aKey) throws DAOException
   {
      String lSql = getSqlQuery0();

      lSql += " " + setCondizioniByKey(aKey);
      setStatement(lSql);
   }

   /**
    * Imposta condizione di filtro su la chiave id
    * del deposito sentenza.
    * <p>
    * @param aKey chiave deposito sentenza.
    * @return la codizione sql.
    */
   public String setCondizioniByKey(BigDecimal aKey)
   {
     return " WHERE ID_DEPOSITO_SENTENZA = " + aKey;
   }

   public void ricercaDepositoSentenzaByIdGenProcedimento( BigDecimal aKey) throws DAOException
   {
     String lSql = getSqlQuery0();

     lSql += " WHERE GEN_PRID_GENERALE_PROCEDIMENTO = " + aKey;
     setStatement(lSql);
   }

}
