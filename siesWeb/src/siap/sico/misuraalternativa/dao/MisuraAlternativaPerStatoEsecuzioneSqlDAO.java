package siap.sico.misuraalternativa.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>Title: MisuraAlternativaSqlDAO</p>
 * <p>Description: Classe SqlDAO che rappresenta la tabella MisuraAlternativa</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class MisuraAlternativaPerStatoEsecuzioneSqlDAO
    extends SqlDAO
{
  public MisuraAlternativaPerStatoEsecuzioneSqlDAO(Connection con)
  {
    super(con);
  }

//
// METODO RICERCA()
//
  public void ricercaMisuraAlternativaPerStatoEsecuzione(BigDecimal aKey) throws DAOException
  {
    String lSql = getSqlQuery() +
    " and MISURA_ALTERNATIVA.FAS_SIE_ID_FASCICOLO_SIEP = " +  aKey +
    " ORDER BY EVENTO.DATA_EMISSIONE DESC ,EVENTO.DATA_INSERIMENTO DESC ";

    setStatement(lSql);
  }


  /**
   * Metodo sempre per la stampa della MA legata alla MA corrente
   * @param aKey
   * @throws DAOException
   */
  public void ricercaMisuraAlternativaPrecedenteSosp(BigDecimal aKey, String[] aCodici) throws DAOException
  {
// AMBROSINO 29/09/2010 : Per Stampa di "Variazione Data Misura" (Eventi 5414 e 5415) 
//	      		         è stato aggiunto il codice '2005' nella query	  
     String lSql = getSqlQuery();
     lSql += " and MISURA_ALTERNATIVA.FAS_SIE_ID_FASCICOLO_SIEP = " +  aKey;     
   // " and COD_TIPO_MISURA in  ('2145','2146','2147','2148','2149','2150','2151','2153','2005') " +
     
     // 07/2015 MEV 29 aggiunta gestione codici Sospensione
     if (aCodici!=null){
       String sqlCodici = "";
       for(int i=0;i<aCodici.length;i++)
       {
         sqlCodici += "'" +aCodici[i]+ "'";
         if(aCodici.length>1 && i<aCodici.length-1)
           sqlCodici += ",";
       }

       lSql += " and COD_TIPO_MISURA in ("+sqlCodici+")";
     }
     lSql += " ORDER BY MISURA_ALTERNATIVA.DATA_INSERIMENTO DESC ";


    setStatement(lSql);
  }


  protected String getSqlQuery()
  {
    String lStatement = new String("");
    lStatement += "SELECT ID_MISURA_ALTERNATIVA, COD_TIPO_DECISIONE, PROV.RV_MEANING TIPODECISIONE, " +
        " COD_NATURA_DECISIONE, NAT.RV_MEANING NATURA, u1.RV_MEANING DESCRUFFSORV,COD_TIPO_MISURA, MPROV.RV_MEANING MISURA, " +
        " DATA_DECISIONE,  MISURA_ALTERNATIVA.COD_MAGISTRATO, COD_UFFICIO_SORVEGLIANZA, CSS_ID_CSSA, DESCR_LUOGO_PROVA," +
        " NUM_ANNI_MISURA, NUM_MESI_MISURA, NUM_GIORNI_MISURA, DATA_INIZIO_MISURA, DATA_FINE_MISURA, CHIAVE_ANNO_FASCICOLO_SIUS," +
        " CHIAVE_UFFICIO_FASCICOLO_SIUS, UFFSOR.RV_MEANING DESCUFSORV, COM.DESCRIZIONE DESCCOM, " +
        " CHIAVE_PROGR_FASCICOLO_SIUS, ANNO_REGISTRO, NUMERO_REGISTRO,  MISURA_ALTERNATIVA.COD_OPERATORE_INSERIMENTO," +
        " MISURA_ALTERNATIVA.DATA_INSERIMENTO,  MISURA_ALTERNATIVA.COD_UFFICIO_INSERIMENTO, " +
        " MISURA_ALTERNATIVA.COD_OPERATORE_AGGIORNAMENTO,  MISURA_ALTERNATIVA.DATA_AGGIORNAMENTO," +
        " MISURA_ALTERNATIVA.COD_UFFICIO_AGGIORNAMENTO,  MISURA_ALTERNATIVA.FAS_SIE_ID_FASCICOLO_SIEP, " +
        " MISURA_ALTERNATIVA.EVE_ID_EVENTO,  MISURA_ALTERNATIVA.NOTE, DATA_SCARCERAZIONE, DATA_INGRESSO_ISTITUTO," +
        " COD_TIPO_UFFICIO_SCARCERAZIONE, FLAG_UFFICIO_INSERIMENTO, DATA_INIZIO_REVOCA, NUM_ANNI_REVOCA_RECLUSIONE," +
        " NUM_MESI_REVOCA_RECLUSIONE, NUM_GIORNI_REVOCA_RECLUSIONE, NUM_ANNI_REVOCA_ARRESTO, NUM_MESI_REVOCA_ARRESTO," +
        " DATA_SCADENZA_PROROGA, FLAG_DECISIONE_TRIBUNALE, COD_TDS_COMPETENTE, FLAG_SITUAZIONE, "+
        " TDSCOMP.RV_MEANING DESCUFFTDS, COMTDSCOMP.DESCRIZIONE DESCCOMTDS, " +
        " NUM_GIORNI_REVOCA_ARRESTO, ID_EVENTO  " +
        " FROM EVENTO,MISURA_ALTERNATIVA, CG_REF_CODES PROV, CG_REF_CODES NAT,CG_REF_CODES UFFSCA, " +
        " CG_REF_CODES MPROV,  CG_REF_CODES UFFSOR, UFFICIO SORU, COMUNE COM,   " +
        " CG_REF_CODES TDSCOMP, UFFICIO TDSCOMUFF, COMUNE COMTDSCOMP, "+
        " (SELECT U.COD_UFFICIO,UFF.RV_MEANING FROM CG_REF_CODES UFF, " +
        " UFFICIO U WHERE UFF.RV_DOMAIN = 'TIPO_UFFICIO' AND UFF.RV_LOW_VALUE = U.COD_TIPO_UFFICIO) u1 " +
        " WHERE PROV.RV_DOMAIN = 'TIPO_PROVVEDIMENTO'  AND NAT.RV_DOMAIN = 'NATURA_DECISIONE' " +
        " AND MPROV.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO'  AND PROV.RV_LOW_VALUE = COD_TIPO_DECISIONE " +
        " AND NAT.RV_LOW_VALUE = COD_NATURA_DECISIONE  AND MPROV.RV_LOW_VALUE = COD_TIPO_MISURA " +
        " AND UFFSCA.RV_DOMAIN = 'TIPO_UFFICIO_SCARCERAZIONE'    AND UFFSCA.RV_LOW_VALUE = COD_TIPO_UFFICIO_SCARCERAZIONE  " +
        " AND u1.COD_UFFICIO(+) = MISURA_ALTERNATIVA.COD_UFFICIO_SORVEGLIANZA " +
        " AND SORU.COD_UFFICIO = MISURA_ALTERNATIVA.CHIAVE_UFFICIO_FASCICOLO_SIUS  " +
        " AND COM.COD_COMUNE = SORU.COD_COMUNE  AND UFFSOR.RV_DOMAIN = 'TIPO_UFFICIO' " +
        " AND UFFSOR.RV_LOW_VALUE = SORU.COD_TIPO_UFFICIO AND" +
        " EVENTO.FLAG_STAMPA_SIEP = 'S'" +
        " AND EVENTO.FLAG_DOCUMENTO_REGISTRATO = 'S'  AND MISURA_ALTERNATIVA.EVE_ID_EVENTO = EVENTO.ID_EVENTO "+
        " AND TDSCOMUFF.COD_UFFICIO =  NVL(MISURA_ALTERNATIVA.COD_TDS_COMPETENTE , '-') "+
        " AND COMTDSCOMP.COD_COMUNE = TDSCOMUFF.COD_COMUNE "+
        " AND TDSCOMP.RV_DOMAIN = 'TIPO_UFFICIO'   "+
        " AND TDSCOMP.RV_LOW_VALUE = TDSCOMUFF.COD_TIPO_UFFICIO  ";

        return lStatement;
  }

//
// METODO GETMODEL()
//
  public GenericModel getModel() throws DAOException
  {
    MisuraAlternativaModel aModel = new MisuraAlternativaModel();

//Inserire le opportune set delle descrizioni!
    aModel.setIdMisuraAlternativa(getBigDecimal("ID_MISURA_ALTERNATIVA"));
    aModel.setCodTipoDecisione(getString("COD_TIPO_DECISIONE"));
    aModel.setDescrTipoDecisione(getString("TIPODECISIONE"));
    aModel.setCodNaturaDecisione(getString("COD_NATURA_DECISIONE"));
    aModel.setDescrNaturaDecisione(getString("NATURA"));
    aModel.setCodTipoMisura(getString("COD_TIPO_MISURA"));
    aModel.setDescrTipoMisura(getString("MISURA"));
    aModel.setDataDecisione(getDate("DATA_DECISIONE"));
    aModel.setCodMagistrato(getString("COD_MAGISTRATO"));
// aModel.setDescrMagistrato(getString("") );
    aModel.setCodUfficioSorveglianza(getString("COD_UFFICIO_SORVEGLIANZA"));
    aModel.setDescrUfficioSorveglianza(getString("DESCRUFFSORV"));
//aModel.setDescrUfficioSorveglianza(getString("SEDEUFFSORV") );
    aModel.setCssIdCssa(getBigDecimal("CSS_ID_CSSA"));
    aModel.setDescrLuogoProva(getString("DESCR_LUOGO_PROVA"));
    aModel.setNumAnniMisura(getBigDecimal("NUM_ANNI_MISURA"));
    aModel.setNumMesiMisura(getBigDecimal("NUM_MESI_MISURA"));
    aModel.setNumGiorniMisura(getBigDecimal("NUM_GIORNI_MISURA"));
    aModel.setDataInizioMisura(getDate("DATA_INIZIO_MISURA"));
    aModel.setDataFineMisura(getDate("DATA_FINE_MISURA"));
    aModel.setChiaveAnnoFascicoloSius(getBigDecimal("CHIAVE_ANNO_FASCICOLO_SIUS"));
    aModel.setChiaveUfficioFascicoloSius(getString("CHIAVE_UFFICIO_FASCICOLO_SIUS"));
    aModel.setChiaveProgrFascicoloSius(getBigDecimal("CHIAVE_PROGR_FASCICOLO_SIUS"));
    aModel.setAnnoRegistro(getBigDecimal("ANNO_REGISTRO"));
    aModel.setNumeroRegistro(getBigDecimal("NUMERO_REGISTRO"));
    aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
//aModel.setDescrUfficioInserimento(getString("") );
    aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
    aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
    aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
// aModel.setDescrUfficioAggiornamento(getString("") );
    aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
    aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
    aModel.setNote(getString("NOTE"));
    aModel.setDataScarcerazione(getDate("DATA_SCARCERAZIONE"));
    aModel.setDataIngressoIstituto(getDate("DATA_INGRESSO_ISTITUTO"));
    aModel.setCodTipoUfficioScarcerazione(getString("COD_TIPO_UFFICIO_SCARCERAZIONE"));
    aModel.setFlagUfficioInserimento(getString("FLAG_UFFICIO_INSERIMENTO"));
    //dario
    aModel.setDataInizioRevoca(getDate("DATA_INIZIO_REVOCA"));
    aModel.setNumAnniRevocaReclusione(getBigDecimal("NUM_ANNI_REVOCA_RECLUSIONE"));
    aModel.setNumMesiRevocaReclusione(getBigDecimal("NUM_MESI_REVOCA_RECLUSIONE"));
    aModel.setNumGiorniRevocaReclusione(getBigDecimal("NUM_GIORNI_REVOCA_RECLUSIONE"));
    aModel.setNumAnniRevocaArresto(getBigDecimal("NUM_ANNI_REVOCA_ARRESTO"));
    aModel.setNumMesiRevocaArresto(getBigDecimal("NUM_MESI_REVOCA_ARRESTO"));
    aModel.setNumGiorniRevocaArresto(getBigDecimal("NUM_GIORNI_REVOCA_ARRESTO"));

    //DARIO
    aModel.setDescrChiaveUfficioFascicoloSius(getString("DESCUFSORV")+  " di "+ getString("DESCCOM"));

    aModel.setDataScadenzaProroga(getDate("DATA_SCADENZA_PROROGA"));
    aModel.setFlagDecisioneTribunale(getString("FLAG_DECISIONE_TRIBUNALE"));
    aModel.setCodTdsCompetente(getString("COD_TDS_COMPETENTE"));
    aModel.setFlagSituazione(getString("FLAG_SITUAZIONE"));

    aModel.setDescTdsCompetente(getString("DESCUFFTDS"));
    aModel.setDescSedeTdsCompetente(getString("DESCCOMTDS"));

    return aModel;
  }

}
