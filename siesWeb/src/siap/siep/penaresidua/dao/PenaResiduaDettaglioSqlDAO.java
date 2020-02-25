package siap.siep.penaresidua.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.penaresidua.model.PenaResiduaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>Title: PenaResiduaDettaglioSqlDAO</p>
 * <p>Description: Classe che realizza l'sql dao necessario alla ricerca della
 * pena precedente ad un certo evento</p>
 * <p> </p>
 * <p>Company: Bull Italia S.p.A.</p>
 */
public class PenaResiduaDettaglioSqlDAO extends SIAPSqlDAO
{
  public PenaResiduaDettaglioSqlDAO(Connection con)
  {
    super(con);
  }

  /**
   * ricerca la penultima pena da evento
   * @param lIdFascicolo
   * @param lIdEvento
   */
  public void ricercaPenultimaPenaDaEvento(BigDecimal lIdFascicolo, BigDecimal lIdEvento)
  {
    String lSql = getSqlQuery(lIdFascicolo, lIdEvento);

    setStatement(lSql);
  }

  protected String getSqlQuery(BigDecimal lIdFascicolo, BigDecimal lIdEvento)
  {
    String lStatement = new String("");
    lStatement = "select " +
      "d.ID_PENA_RESIDUA," +
      "d.DATA_INIZIO ," +
      "d.DATA_FINE ," +
      "d.NUM_ANNI_RECLUSIONE," +
      "d.NUM_MESI_RECLUSIONE," +
      "d.NUM_GIORNI_RECLUSIONE," +
      "d.IMPORTO_MULTA," +
      "d.NUM_ANNI_ARRESTO," +
      "d.NUM_MESI_ARRESTO," +
      "d.NUM_GIORNI_ARRESTO," +
      "d.IMPORTO_AMMENDA," +
      "d.DIES_A_QUO," +
      "d.COD_OPERATORE_INSERIMENTO," +
      "d.DATA_INSERIMENTO," +
      "d.COD_UFFICIO_INSERIMENTO," +
      "d.COD_OPERATORE_AGGIORNAMENTO," +
      "d.DATA_AGGIORNAMENTO," +
      "d.COD_UFFICIO_AGGIORNAMENTO," +
      "d.EVE_ID_EVENTO," +
      "d.FAS_SIE_ID_FASCICOLO_SIEP," +
      "d.FLAG_VALIDATO," +
      "d.DATA_FINE_PRESUNTA," +
      "d.DATA_FINE_RECLUSIONE," +
      "d.DATA_INIZIO_ARRESTO," +
      "d.FLAG_ERGASTOLO," +
      "d.DATA_INIZIO_ISOLAMENTO_DIURNO," +
      "d.DATA_FINE_ISOLAMENTO_DIURNO," +
      "d.NUM_ANNI_ISOLAMENTO_DIURNO," +
      "d.NUM_MESI_ISOLAMENTO_DIURNO," +
      "d.NUM_GIORNI_ISOLAMENTO_DIURNO," +
      "d.MIS_ALT_ID_MISURA_ALTERNATIVA," +
      "d.FLAG_PENA_SOSPESA  " +
      "from (select rownum rn,a.*,b.conta" +
      "       from ( select * " +
      "                from pena_residua" +
      "    	          where fas_sie_id_fascicolo_siep =" + lIdFascicolo +
      "     	        order by data_inserimento " +
      "            ) a , " +
      "            ( select count(*) conta " +
      "                from pena_residua v," +
      "            (select id_pena_residua from pena_residua where eve_id_evento= " + lIdEvento +
      "            ) h" +
      "       where v.fas_sie_id_fascicolo_siep =" + lIdFascicolo +
      "         and v.id_pena_residua <= h.id_pena_residua) b" +
      "	      order by data_inserimento" +
      "      ) d" +
      " where rn=conta-1";

    return lStatement;
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
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
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
    aModel.setFlagPenaSospesa(getString("FLAG_PENA_SOSPESA"));

    return aModel;
  }

}