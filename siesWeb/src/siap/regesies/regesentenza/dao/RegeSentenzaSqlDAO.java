package siap.regesies.regesentenza.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.regesies.regesentenza.model.RegeSentenzaModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>Title: RegeSentenzaSqlDAO</p>
 * <p>Description: Classe SqlDAO che rappresenta la tabella RegeSentenza</p>
 */
public class RegeSentenzaSqlDAO extends SIAPSqlDAO
{

  public void elencoProvvedimenti(String aCodComune) throws DAOException
  {
    String lSql = getSqlQuery();
    lSql += setOrder();
    setStatement(lSql);
  }

  public void getCountProvvedimenti()
  {
    String lStatement = "SELECT COUNT(*) HowManyRecords ";
    lStatement += " FROM REGE_SENTENZA";

    setStatement(lStatement);
  }

  public RegeSentenzaSqlDAO(Connection con)
  {
    super(con);
  }

  //
  // METODO RICERCA()
  //
  public void ricercaRegeSentenza(RegeSentenzaModel aModel) throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizione(aModel);
    setStatement(lSql);
  }

  public void ricercaRegeSentenzaByKey(String aKey) throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizioniByKey(aKey);
    setStatement(lSql);
  }

  protected String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement = "SELECT ID_FILE," +
      " COD_TIPO_PROVVEDIMENTO," +
      " TIPO_PROVVEDIMENTO.RV_MEANING DESCR_TIPO_PROVVEDIMENTO," +
      " ANNO_REGE_PM," +
      " NUMERO_REGE_PM," +
      " DATA_ARRIVO_ATTO," +
      " DATA_PROVVEDIMENTO, " +
      " COD_TIPO_AUTORITA_EMITTENTE," +
      " TIPO_AUTORITA_EMITTENTE.RV_MEANING DESCR_TIPO_AUTORITA_EMITTENTE," +
      "  COD_LUOGO_EMITTENTE," +
      "  LUOGO_EMITTENTE.DESCRIZIONE DESCR_LUOGO_EMITTENTE," +
      "  NUM_SEZIONE_AUTORITA_EMITTENTE, " +
      "  ANNO_SENTENZA," +
      " NUMERO_SENTENZA," +
      " DATA_IRREVOCABILITA," +
      " FLAG_SENTENZA_APPLICAZ_PENA, " +
      "  COD_TIPO_PROVV_RIF," +
      " DATA_PROVV_RIF," +
      " COD_TIPO_AUTORITA_PROVV_RIF," +
      " ANNO_PROVV_RIF," +
      " NUMERO_PROVV_RIF," +
      " COD_LUOGO_PROVV_RIF," +
      "   NUM_SEZIONE_AUTORITA_PROVV_RIF," +
      " COD_TIPO_DECISIONE_CASSAZIONE," +
      " ANNO_SENTENZA_CASSAZIONE," +
      " NUMERO_SENTENZA_CASSAZIONE," +
      " ANNO_RACCOLTA_GENERALE, " +
      "   NUMERO_RACCOLTA_GENERALE," +
      " ANNO_REGISTRO_35," +
      " NUM_REGISTRO_35," +
      " NOTE," +
      " DESCR_NUM_CAMPIONE_PENALE," +
      " ANNO_REGE_GIP," +
      " NUMERO_REGE_GIP," +
      " ANNO_REGE_DIB, " +
      "   NUMERO_REGE_DIB," +
      " ANNO_REGE_CAS," +
      " NUMERO_REGE_CAS," +
      " ANNO_REGE_CAP," +
      " NUMERO_REGE_CAP," +
      " ANNO_REGE_CASAP," +
      " NUMERO_REGE_CASAP," +
      " NOTA_DISPOSITIVO, " +
      "   COD_TIPO_RITO," +
      " COD_OPERATORE_INSERIMENTO," +
      " DATA_INSERIMENTO," +
      " COD_UFFICIO_INSERIMENTO," +
      " COD_OPERATORE_AGGIORNAMENTO," +
      " DATA_AGGIORNAMENTO," +
      "    COD_UFFICIO_AGGIORNAMENTO," +
      " FLAG_GIUDIZIO_ABBREVIATO " +
      "	 FROM rege_sentenza," +
      "	 CG_REF_CODES TIPO_PROVVEDIMENTO," +
      "	 CG_REF_CODES TIPO_AUTORITA_EMITTENTE," +
      "     COMUNE LUOGO_EMITTENTE" +
      " WHERE (TIPO_PROVVEDIMENTO.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' "+
      " AND TIPO_PROVVEDIMENTO.RV_LOW_VALUE = COD_TIPO_PROVVEDIMENTO)" +
      " AND (TIPO_AUTORITA_EMITTENTE.RV_DOMAIN = 'TIPO_UFFICIO' "+
      " AND TIPO_AUTORITA_EMITTENTE.RV_LOW_VALUE = COD_TIPO_AUTORITA_EMITTENTE)" +
      " AND (LUOGO_EMITTENTE.COD_COMUNE = COD_LUOGO_EMITTENTE)";

      return lStatement;

      //
      // METODO GETMODEL()
      //

  }

  public GenericModel getModel() throws DAOException
  {
    RegeSentenzaModel aModel = new RegeSentenzaModel();

    aModel.setIdFile(getString("ID_FILE"));
    aModel.setCodTipoProvvedimento(getString("COD_TIPO_PROVVEDIMENTO"));
    aModel.setDescrTipoProvvedimento(getString("DESCR_TIPO_PROVVEDIMENTO"));
    aModel.setAnnoRegePm(getInt("ANNO_REGE_PM"));
    aModel.setNumeroRegePm(getString("NUMERO_REGE_PM"));
    aModel.setDataArrivoAtto(getDate("DATA_ARRIVO_ATTO"));
    aModel.setDataProvvedimento(getDate("DATA_PROVVEDIMENTO"));
    aModel.setCodTipoAutoritaEmittente(getString("COD_TIPO_AUTORITA_EMITTENTE"));
    aModel.setDescrTipoAutoritaEmittente(getString("DESCR_TIPO_AUTORITA_EMITTENTE"));
    aModel.setCodLuogoEmittente(getString("COD_LUOGO_EMITTENTE"));
    aModel.setDescrLuogoEmittente(getString("DESCR_LUOGO_EMITTENTE"));
    aModel.setNumSezioneAutoritaEmittente(getString("NUM_SEZIONE_AUTORITA_EMITTENTE"));
    aModel.setAnnoSentenza(getInt("ANNO_SENTENZA"));
    aModel.setNumeroSentenza(getString("NUMERO_SENTENZA"));
    aModel.setDataIrrevocabilita(getDate("DATA_IRREVOCABILITA"));
    aModel.setFlagSentenzaApplicazPena(getString("FLAG_SENTENZA_APPLICAZ_PENA"));
    aModel.setCodTipoProvvRif(getString("COD_TIPO_PROVV_RIF"));
    aModel.setDataProvvRif(getDate("DATA_PROVV_RIF"));
    aModel.setCodTipoAutoritaProvvRif(getString("COD_TIPO_AUTORITA_PROVV_RIF"));
    aModel.setAnnoProvvRif(getInt("ANNO_PROVV_RIF"));
    aModel.setNumeroProvvRif(getString("NUMERO_PROVV_RIF"));
    aModel.setCodLuogoProvvRif(getString("COD_LUOGO_PROVV_RIF"));
    aModel.setNumSezioneAutoritaProvvRif(getString("NUM_SEZIONE_AUTORITA_PROVV_RIF"));
    aModel.setCodTipoDecisioneCassazione(getString("COD_TIPO_DECISIONE_CASSAZIONE"));
    aModel.setAnnoSentenzaCassazione(getInt("ANNO_SENTENZA_CASSAZIONE"));
    aModel.setNumeroSentenzaCassazione(getString("NUMERO_SENTENZA_CASSAZIONE"));
    aModel.setAnnoRaccoltaGenerale(getInt("ANNO_RACCOLTA_GENERALE"));
    aModel.setNumeroRaccoltaGenerale(getString("NUMERO_RACCOLTA_GENERALE"));
    aModel.setAnnoRegistro35(getInt("ANNO_REGISTRO_35"));
    aModel.setNumRegistro35(getString("NUM_REGISTRO_35"));
    aModel.setNote(getString("NOTE"));
    aModel.setDescrNumCampionePenale(getString("DESCR_NUM_CAMPIONE_PENALE"));
    aModel.setAnnoRegeGip(getInt("ANNO_REGE_GIP"));
    aModel.setNumeroRegeGip(getString("NUMERO_REGE_GIP"));
    aModel.setAnnoRegeDib(getInt("ANNO_REGE_DIB"));
    aModel.setNumeroRegeDib(getString("NUMERO_REGE_DIB"));
    aModel.setAnnoRegeCas(getInt("ANNO_REGE_CAS"));
    aModel.setNumeroRegeCas(getString("NUMERO_REGE_CAS"));
    aModel.setAnnoRegeCap(getInt("ANNO_REGE_CAP"));
    aModel.setNumeroRegeCap(getString("NUMERO_REGE_CAP"));
    aModel.setAnnoRegeCasap(getInt("ANNO_REGE_CASAP"));
    aModel.setNumeroRegeCasap(getString("NUMERO_REGE_CASAP"));
    aModel.setNotaDispositivo(getString("NOTA_DISPOSITIVO"));
    aModel.setCodTipoRito(getString("COD_TIPO_RITO"));
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
    aModel.setFlagGiudizioAbbreviato(getString("FLAG_GIUDIZIO_ABBREVIATO"));

    if (aModel.getCodTipoRito() == null || aModel.getCodTipoRito().equals(""))
    {
    	aModel.setDescrTipoRito("-");
    }
    else
    {
     	aModel.setDescrTipoRito(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoRitoSentenza(), aModel.getCodTipoRito()));
    }

    return aModel;
  }

public String setCondizione(RegeSentenzaModel aModel)
  {
    String lCondizioni = new String();

    lCondizioni += " AND ANNO_SENTENZA = "+aModel.getAnnoSentenza() +
    " AND NUMERO_SENTENZA = '" +aModel.getNumeroSentenza() +"'" +
    " AND COD_LUOGO_EMITTENTE = '" +aModel.getCodLuogoEmittente() +"'" +
    " AND COD_TIPO_AUTORITA_EMITTENTE = '" +aModel.getCodTipoAutoritaEmittente() +"'";


    return lCondizioni;
  }

public String setCondizioniByKey(String aKey)
  {
    return " AND ID_FILE = '" + aKey + "'";
  }
public String setOrder()
  {
    return " ORDER by DATA_PROVVEDIMENTO";
  }

}