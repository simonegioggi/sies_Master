package siap.sius.tenore.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 *
 * <p>Title: TenoreEventoSqlDAO</p>
 * <p>Description: Classe per recuperare il tenore dal solo id dell'evento dell'ordinanza</p>
 * <p> </p>
 * <p>Company: Bull Italia S.p.A.</p>
 *  not attributable
 */
public class TenoreEventoSqlDAO extends SIAPSqlDAO
{
  public TenoreEventoSqlDAO(Connection con)
  {
    super(con);
  }

  public void ricercaTenorebyIdEvento(BigDecimal aIdEvento)
  {

    String lStatement = "SELECT ID_TENORE, COD_ESITO_TENORE, ESITO.RV_MEANING DESCR_ESITO, DATA," +
      "       NVL (TEN.COD_MAGISTRATO, '') COD_MAGISTRATO, TEN.NOTE NOTE," +
      "       COD_OGGETTO_TENORE, OGGETTO_TENORE.RV_MEANING DESC_OGGETTO_TENORE," +
      "       TEN.COD_OPERATORE_INSERIMENTO COD_OPERATORE_INSERIMENTO," +
      "       TEN.DATA_INSERIMENTO DATA_INSERIMENTO," +
      "       TEN.COD_UFFICIO_INSERIMENTO COD_UFFICIO_INSERIMENTO," +
      "       DESCR_COM_UFF.DESCRIZIONE DESC_UFFICIO_INSERIMENTO," +
      "       TEN.COD_OPERATORE_AGGIORNAMENTO COD_OPERATORE_AGGIORNAMENTO," +
      "       TEN.DATA_AGGIORNAMENTO DATA_AGGIORNAMENTO, TEN.DATA_FINE DATA_FINE," +
      "       TEN.COD_UFFICIO_AGGIORNAMENTO COD_UFFICIO_AGGIORNAMENTO," +
      "       DEP_OPID_DEPOSITO_ORDINANZA_PC," +
      "       IMP_ID_IMPUGNAZIONE, PROGR_TENORE, DEP_DEC_ID_DEPOSITO_DECRETO," +
      "       COD_DETTAGLIO_OGGETTO," +
      "       DETTAGLIO_OGGETTO.RV_MEANING DESC_DETTAGLIO_OGGETTO," +
      "       OGGETTO_TENORE.RV_ABBREVIATION ABBR_OGGETTO_TENORE" +
      "  FROM TENORE TEN," +
      "       DEPOSITO_ORDINANZA_PC," +
      "       CG_REF_CODES OGGETTO_TENORE," +
      "       CG_REF_CODES DETTAGLIO_OGGETTO," +
      "       CG_REF_CODES ESITO," +
      "       UFFICIO UFF," +
      "       COMUNE DESCR_COM_UFF" +
      " WHERE DEPOSITO_ORDINANZA_PC.ID_EVENTO_GENERATO = " + aIdEvento +
      "   AND DEPOSITO_ORDINANZA_PC.ID_DEPOSITO_ORDINANZA_PC =  TEN.DEP_OPID_DEPOSITO_ORDINANZA_PC" +
      "   AND (OGGETTO_TENORE.RV_LOW_VALUE = COD_OGGETTO_TENORE)" +
      "   AND (OGGETTO_TENORE.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO')" +
      "   AND (DETTAGLIO_OGGETTO.RV_LOW_VALUE = COD_DETTAGLIO_OGGETTO)" +
      "   AND (DETTAGLIO_OGGETTO.RV_DOMAIN = 'DETTAGLIO_MOTIVO')" +
      "   AND (UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE" +
      "        AND TEN.COD_UFFICIO_INSERIMENTO = UFF.COD_UFFICIO" +
      "       )" +
      "   AND (ESITO.RV_LOW_VALUE = COD_ESITO_TENORE)" +
      "   AND (ESITO.RV_DOMAIN = 'ESITO_PROVVEDIMENTO')";

    setStatement(lStatement);

  }

  //
  // METODO GETMODEL()
  //
  public GenericModel getModel() throws DAOException
  {
    TenoreModel aModel = new TenoreModel();

    aModel.setIdTenore(getBigDecimal("ID_TENORE"));
    aModel.setCodEsitoTenore(getString("COD_ESITO_TENORE"));
    aModel.setDescrEsitoTenore(getString("DESCR_ESITO"));
    aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
    aModel.setData(getDate("DATA"));
    aModel.setNote(getString("NOTE"));
    aModel.setCodOggettoTenore(getString("COD_OGGETTO_TENORE"));
    aModel.setDescrOggettoTenore(getString("DESC_OGGETTO_TENORE"));
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    aModel.setDescrUfficioInserimento(getString("DESC_UFFICIO_INSERIMENTO"));
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
    //aModel.setGenPridGeneraleProcedimento(getBigDecimal("GEN_PRID_GENERALE_PROCEDIMENTO"));
    aModel.setDepOpidDepositoOrdinanzaPc(getBigDecimal("DEP_OPID_DEPOSITO_ORDINANZA_PC"));
    aModel.setImpIdImpugnazione(getBigDecimal("IMP_ID_IMPUGNAZIONE"));
    aModel.setProgrTenore(getBigDecimal("PROGR_TENORE"));
    aModel.setDepDecIdDepositoDecreto(getBigDecimal("DEP_DEC_ID_DEPOSITO_DECRETO"));
    aModel.setCodDettaglioOggetto(getString("COD_DETTAGLIO_OGGETTO"));
    aModel.setDescrDettaglioOggetto(getString("DESC_DETTAGLIO_OGGETTO"));
    aModel.setAbbrOggettoTenore(getString("ABBR_OGGETTO_TENORE"));
    aModel.setDataFine(getDate("DATA_FINE"));
    return aModel;
  }

}