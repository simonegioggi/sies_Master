package siap.siep.penaresidua.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>Title: PenaResiduaPerStatoEsecuzioneSqlDAO</p>
 * <p>Description: Classe SqlDAO che rappresenta la tabella PenaResiduaPerStatoEsecuzioneSqlDAO</p>
 */

public class PenaResiduaPerStatoEsecuzioneSqlDAO extends SIAPSqlDAO
{
  public PenaResiduaPerStatoEsecuzioneSqlDAO(Connection con)
  {
    super(con);
  }

  /**
   * Ricerca Pena Residua By Key Evento
   * @param aKey
   * @throws DAOException
   */
  public void ricercaPenaResiduaPerStatoEsecuzione(BigDecimal aKey) throws DAOException
  {
    String lSql = getSqlQuery(aKey);

    setStatement(lSql);

  }

  protected String getSqlQuery(BigDecimal aKey) throws DAOException
  {
    String lSql = "SELECT " +
      "ID_PENA_RESIDUA, " +
      "DATA_INIZIO, " +
      "DATA_FINE, " +
      "NUM_ANNI_RECLUSIONE, " +
      "NUM_MESI_RECLUSIONE, " +
      "NUM_GIORNI_RECLUSIONE, " +
      "IMPORTO_MULTA, " +
      "NUM_ANNI_ARRESTO, " +
      "NUM_MESI_ARRESTO, " +
      "NUM_GIORNI_ARRESTO, " +
      "IMPORTO_AMMENDA, " +
      "DIES_A_QUO, " +
      "PENA_RESIDUA.COD_OPERATORE_INSERIMENTO, " +
      "PENA_RESIDUA.DATA_INSERIMENTO, " +
      "PENA_RESIDUA.COD_UFFICIO_INSERIMENTO, " +
      "PENA_RESIDUA.COD_OPERATORE_AGGIORNAMENTO, " +
      "PENA_RESIDUA.DATA_AGGIORNAMENTO, " +
      "PENA_RESIDUA.COD_UFFICIO_AGGIORNAMENTO, " +
      "PENA_RESIDUA.EVE_ID_EVENTO, " +
      "PENA_RESIDUA.FAS_SIE_ID_FASCICOLO_SIEP, " +
      "FLAG_VALIDATO, " +
      "DATA_FINE_PRESUNTA, " +
      "DATA_FINE_RECLUSIONE," +
      "DATA_INIZIO_ARRESTO," +
      "FLAG_ERGASTOLO, " +
      "DATA_INIZIO_ISOLAMENTO_DIURNO, " +
      "DATA_FINE_ISOLAMENTO_DIURNO, " +
      "NUM_ANNI_ISOLAMENTO_DIURNO, " +
      "NUM_MESI_ISOLAMENTO_DIURNO, " +
      "NUM_GIORNI_ISOLAMENTO_DIURNO, " +
      "MIS_ALT_ID_MISURA_ALTERNATIVA " +
      "FROM PENA_RESIDUA,EVENTO " +
      "WHERE EVENTO.FAS_SIE_ID_FASCICOLO_SIEP = " + aKey +
      " AND EVENTO.FLAG_STAMPA_SIEP = 'S' " +
      " AND FLAG_DOCUMENTO_REGISTRATO = 'S' " +
      " AND PENA_RESIDUA.EVE_ID_EVENTO = EVENTO.ID_EVENTO " +
      " ORDER BY EVENTO.DATA_EMISSIONE DESC ,EVENTO.DATA_INSERIMENTO DESC  ";

    return lSql;
  }

  //
  // METODO GETMODEL()
  //
  public GenericModel getModel() throws DAOException
  {
    PenaResiduaModel aModel = new PenaResiduaModel();

//Inserire le opportune set delle descrizioni!
    aModel.setIdPenaResidua(getBigDecimal("ID_PENA_RESIDUA"));
    aModel.setDataInizio(getDate("DATA_INIZIO"));
    aModel.setDataFine(getDate("DATA_FINE"));
    aModel.setNumAnniReclusione(getBigDecimal("NUM_ANNI_RECLUSIONE"));
    aModel.setNumMesiReclusione(getBigDecimal("NUM_MESI_RECLUSIONE"));
    aModel.setNumGiorniReclusione(getBigDecimal("NUM_GIORNI_RECLUSIONE"));
    aModel.setImportoMulta(getBigDecimal("IMPORTO_MULTA"));
    aModel.setNumAnniArresto(getBigDecimal("NUM_ANNI_ARRESTO"));
    aModel.setNumMesiArresto(getBigDecimal("NUM_MESI_ARRESTO"));
    aModel.setNumGiorniArresto(getBigDecimal("NUM_GIORNI_ARRESTO"));
    aModel.setImportoAmmenda(getBigDecimal("IMPORTO_AMMENDA"));
    aModel.setDiesAQuo(getString("DIES_A_QUO"));
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    //aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
    //aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
    aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
    aModel.setFlagValidato(getString("FLAG_VALIDATO"));
    aModel.setDataFinePresunta(getDate("DATA_FINE_PRESUNTA"));
    aModel.setDataFineReclusione(getDate("DATA_FINE_RECLUSIONE"));
    aModel.setDataInizioArresto(getDate("DATA_INIZIO_ARRESTO"));
    aModel.setFlagErgastolo(getString("FLAG_ERGASTOLO"));
    aModel.setDataInizioIsolamentoDiurno(getDate("DATA_INIZIO_ISOLAMENTO_DIURNO"));
    aModel.setDataFineIsolamentoDiurno(getDate("DATA_FINE_ISOLAMENTO_DIURNO"));
    aModel.setNumAnniIsolamentoDiurno(getBigDecimal("NUM_ANNI_ISOLAMENTO_DIURNO"));
    aModel.setNumMesiIsolamentoDiurno(getBigDecimal("NUM_MESI_ISOLAMENTO_DIURNO"));
    aModel.setNumGiorniIsolamentoDiurno(getBigDecimal("NUM_GIORNI_ISOLAMENTO_DIURNO"));
    aModel.setMisAltIdMisuraAlternativa(getBigDecimal("MIS_ALT_ID_MISURA_ALTERNATIVA"));
    
    return aModel;
  }

  public String setOrderDataInserimentoDesc()
  {
    String lCondizioni = " ORDER BY DATA_INSERIMENTO DESC ";

    return lCondizioni;
  }
}