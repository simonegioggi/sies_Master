package siap.sius.produzioneatti.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sius.produzioneatti.model.ParereModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
* <p>Title: ParereSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta l'accesso alle informazioni
* connesse alla "Richiesta Parere su Procedimento SIUS".</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull </p>
* @version 1.0
*/

public class ParereSqlDAO extends SIAPSqlDAO
{
  public ParereSqlDAO (Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //
  public void ricerca( ParereModel  aModel) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += "  " + setCondizione(aModel);
    lSql += "  " + setOrdinamento(aModel);
    setStatement(lSql);
  }

  /**
   * La query effettua una ricerca dei dati concernenti una "richiesta Parere"
   * accedendo a 4 tabelle :
   * EVENTO, FASCICOLO_SIUS, GENERALE_PROCEDIMENTO, SOGGETTO.
   */
  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += " SELECT " +
        "FAS.ID_FASCICOLO_SIUS, FAS.CHIAVE_ANNO AS ANNO, FAS.CHIAVE_PROGR AS PROGR," +
        " EVE.ID_EVENTO, EVE.DATA_EMISSIONE, EVE.COD_MOTIVO, EVE.COD_ESITO, EVE.DATA_RICEZIONE_ATTI, EVE.DATA_TRASMISSIONE_ATTI, EVE.COD_TIPO_EVENTO, EVE.COD_UFFICIO_EMITTENTE," +
        " GP.ID_GENERALE_PROCEDIMENTO, GP.COD_OGGETTO_PROCEDIMENTO, "+
        " SOG.ID_SOGGETTO, SOG.COGNOME, SOG.NOME, SOG.DATA_NASCITA " +
        " FROM EVENTO EVE INNER JOIN FASCICOLO_SIUS FAS ON (FAS.ID_FASCICOLO_SIUS = EVE.FAS_SIU_ID_FASCICOLO_SIUS AND EVE.COD_TIPO_EVENTO= '08')" +
        " INNER JOIN GENERALE_PROCEDIMENTO GP ON FAS.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS" +
        " INNER JOIN SOGGETTO SOG ON FAS.SOG_ID_SOGGETTO = SOG.ID_SOGGETTO";
    return lStatement;
  }


  //
  // METODO GETMODEL()
  //
  public GenericModel  getModel() throws DAOException
  {
    ParereModel aModel = new  ParereModel();

    aModel.setIdFascicoloSius(getBigDecimal("ID_FASCICOLO_SIUS"));
    aModel.setAnnoFascicoloSius(getBigDecimal("ANNO"));
    aModel.setProgrFascicoloSius(getBigDecimal("PROGR"));

    aModel.setIdEvento(getBigDecimal("ID_EVENTO"));
    aModel.setCodEsito(getString("COD_ESITO") );

    try
    {
    aModel.setDescrEsito(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getEsitoProvvedimento(),aModel.getCodEsito()));
    }
    catch(Exception e)
    {
      throw new DAOException(e.getMessage());
    }

    aModel.setCodMotivo(getString("COD_MOTIVO"));
    aModel.setDescrMotivo(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getMotivoProvvedimento(),aModel.getCodMotivo()));
    aModel.setCodTipoEvento(getString("COD_TIPO_EVENTO"));
    aModel.setDataEmissione(getDate("DATA_EMISSIONE"));
    aModel.setDataRicezioneAtti(getDate("DATA_RICEZIONE_ATTI"));
    aModel.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI"));
    aModel.setCodUfficioEmittente(getString("COD_UFFICIO_EMITTENTE"));

    aModel.setIdGeneraleProcedimento(getBigDecimal("ID_GENERALE_PROCEDIMENTO"));
    aModel.setCodOggettoProcedimento(getString("COD_OGGETTO_PROCEDIMENTO"));
    aModel.setDescrOggettoProcedimento(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getOggettoProcedimento(),aModel.getCodOggettoProcedimento()));
    aModel.setIdSoggetto(getBigDecimal("ID_SOGGETTO"));
    aModel.setNome(getString("NOME"));
    aModel.setCognome(getString("COGNOME"));
    aModel.setDataNascita(getDate("DATA_NASCITA"));

    return aModel;
  }

  public String  setCondizione(ParereModel aModel)
  {
    String lCondizioni = new String();
    boolean lInserito = false;

    if(aModel.getCodUfficioEmittente().length() != 0)
    {
      lCondizioni = " EVE.COD_UFFICIO_EMITTENTE = '" + aModel.getCodUfficioEmittente() + "'";
      lInserito = true;
    }

    if( aModel.getCodMotivo().length() > 1)
    {
      if(lInserito)
        lCondizioni += " AND";
      lCondizioni += " COD_MOTIVO = '" + aModel.getCodMotivo() + "'";
      lInserito = true;
    }

    if( aModel.getCodOggettoProcedimento().length() > 1)
    {
      if(lInserito)
        lCondizioni += " AND";
      lCondizioni += " COD_OGGETTO_PROCEDIMENTO = '" + aModel.getCodOggettoProcedimento() + "'";
      lInserito = true;
    }

    if( aModel.getDataEmissione() != null)
    {
      if(lInserito)
        lCondizioni += " AND";
      lCondizioni += " DATA_EMISSIONE >= TO_DATE('" + DateUtils.getDateToString(aModel.getDataEmissione(),"dd/MM/yyyy")+ "','DD/MM/YYYY')";
      lInserito = true;
    }

    if( aModel.getDataEmissione2() != null)
    {
      if(lInserito)
        lCondizioni += " AND";
      lCondizioni += " DATA_EMISSIONE <= TO_DATE('" + DateUtils.getDateToString(aModel.getDataEmissione2(),"dd/MM/yyyy")+ "','DD/MM/YYYY')";
      lInserito = true;
    }

    // 27/03/2007 Aggiunta filtro x Codice Utente.
    if( aModel.getCodiceUtente().length() > 1)
    {
      if(lInserito)
        lCondizioni += " AND";
      lCondizioni += " EVE.COD_OPERATORE_INSERIMENTO = '" + aModel.getCodiceUtente() + "'";
      lInserito = true;
    }

    if(lInserito)
      lCondizioni = " WHERE " + lCondizioni;
    return lCondizioni;
  }


  /**
   * Metodo che ritorna il tipo di ordinamanto desiderato ossia
   * orderby per ANNO-PROCEDIMENTO ( Condizione di default ) oppure
   * COGNOME-NOME. Il Tipo di ordinamento viene impostato, utilizzando
   * il metodo <code> getMessage() </code> ereditato da GenericModel.
   * Quindi se il valore di getMessage non è valorizzato imposta la
   * condizione di ordinamanto di default (ANNO-PROCEDIMENTO)
   * <p>
   * Mappatura dei codici :
   * -> A-P ( Ordinamento per Anno e Procedimento ) default
   * -> C-N ( Ordinamento per Cognome e Nome )
   * <p>
   * @param aModel ParereModel Istanza dell' oggetto ParereModel
   * @return String ritorna la stringa SQL ORDER BY desiderata.
   */
  protected String setOrdinamento( ParereModel aModel )
  {
    String lSql = new String();

    // Condizione di default è l'orderby per ANNO - PROCEDIMENTO
    // Quindi, al fine di ridurre l'impatto sulle altre funzionalità
    // che utilizzano tale condizione, si considera che, se il Message
    // non è valorizzato applica l'ordinamento di default.
    // Mappatura codici :
    // -> A-P ( Ordinamento per Anno e Procedimento ) default
    // -> C-N ( Ordinamento per Cognome e Nome )
    if( aModel.getMessage() == null || aModel.getMessage().equalsIgnoreCase("A-P") )
      lSql += " ORDER BY FAS.CHIAVE_ANNO, FAS.CHIAVE_PROGR"; // Default
    else if( aModel.getMessage().equalsIgnoreCase("C-N") )
      lSql += " ORDER BY SOG.COGNOME, SOG.NOME";

    return lSql;
  }
}
