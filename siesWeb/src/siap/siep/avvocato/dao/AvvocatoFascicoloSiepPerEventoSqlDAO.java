package siap.siep.avvocato.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>Title: AvvocatoFascicoloSiepPerEventoSqlDAO</p>
 * <p>Description: Classe per la gestione degli avvocati referenziati dagli eventi</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Bull Italia S.p.A.</p>
 *  not attributable
 *  1.0
 */
public class AvvocatoFascicoloSiepPerEventoSqlDAO extends SIAPSqlDAO
{
  public AvvocatoFascicoloSiepPerEventoSqlDAO(Connection con)
  {
    super(con);
  }

  private String getSqlQuery()
  {
    String lStatement = new String("");

    lStatement += "SELECT " +
      "ID_AVVOCATO, " +
      "COGNOME, " +
      "NOME, " +
      "FORO, " +
      "INDIRIZZO, " +
      "TELEFONO, " +
      "FAX, " +
      "E_MAIL, " +
      "COD_FISCALE, " +
      "AVVOCATO.COD_OPERATORE_INSERIMENTO, " +
      "AVVOCATO.DATA_INSERIMENTO, " +
      "AVVOCATO.COD_UFFICIO_INSERIMENTO, " +
      "AVVOCATO.COD_UFFICIO_AGGIORNAMENTO, " +
      "AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, " +
      "AVVOCATO.DATA_AGGIORNAMENTO, " +
      "AVVOCATO.DATA_NASCITA, " +
      "AVVOCATO.COD_LUOGO_NASCITA, " +
      "COMNA.DESCRIZIONE COMUNE_NASCITA, " +
      "AVVOCATO.COD_COMUNE_RESIDENZA, " +
      "COMRES.DESCRIZIONE COMUNE_RESIDENZA, " +
      "AVVTIPODESC.RV_MEANING DESCRTIPO, " +
      " AVVOCATO_FASCICOLO_SIEP.ID_AVVOCATO_FASCICOLO_SIEP ID_AVV_FASC_SIEP," +
      " AVVOCATO_FASCICOLO_SIEP.COD_OPERATORE_INSERIMENTO AVV_FASC_SIEP_COD_OP_INS," +
      " AVVOCATO_FASCICOLO_SIEP.DATA_INSERIMENTO AVV_FASC_SIEP_DATA_INSERIMENTO," +
      " AVVOCATO_FASCICOLO_SIEP.DATA_AGGIORNAMENTO AVV_FASC_SIEP_DATA_AGG," +
      " AVVOCATO_FASCICOLO_SIEP.COD_OPERATORE_AGGIORNAMENTO AVV_FASC_SIEP_COD_OP_AGG," +
      " AVVOCATO_FASCICOLO_SIEP.COD_TIPO_AVVOCATO AVV_FASC_SIEP_COD_TIPO_AVV," +
      " AVVOCATO_FASCICOLO_SIEP.COD_UFFICIO_INSERIMENTO AVV_FASC_SIEP_COD_UFF_INS," +
      " DATA_INIZIO_VALIDITA, DATA_FINE_VALIDITA, AVV_ID_AVVOCATO, FAS_SIE_ID_FASCICOLO_SIEP," +
      " AVVOCATO_FASCICOLO_SIEP.COD_UFFICIO_AGGIORNAMENTO AVV_FASC_SIEP_COD_UFF_AGG, " +
      " AVVOCATO_FASCICOLO_SIEP.COD_MOTIVO_DESIGNAZIONE , " +
      " AVVTIPODESCMOTIVO.RV_MEANING DESCR_MOTIVO_DESIGNAZIONE, " +
      " AVVOCATO_FASCICOLO_SIEP.NOTE,  " +
      " AVVOCATO_FASCICOLO_SIEP.COD_TIPO_AUTORITA, " +
      " AVVTIPODESCAUTORITA.RV_MEANING DESCR_TIPO_AUTORITA, " +
      " AVVOCATO_FASCICOLO_SIEP.SEDE_TIPO_AUTORITA, " +
      "COMSEDE.DESCRIZIONE COMUNE_TIPO_AUTORITA, " +
      " AVVOCATO_FASCICOLO_SIEP.INDIRIZZO_TIPO_AUTORITA, " +
      " AVVOCATO_FASCICOLO_SIEP.IST_DET_ID_ISTITUTO_DETENZIONE, " +
      " AVVOCATO_FASCICOLO_SIEP.COD_TIPO_AUTORITA_DIF, " +
      " AVVTIPODESCAUTORITADIF.RV_MEANING DESCR_TIPO_AUTORITA_DIF, " +
      "COMSEDEDIF.DESCRIZIONE COMUNE_TIPO_AUTORITA_DIF, " +
      " AVVOCATO_FASCICOLO_SIEP.SEDE_TIPO_AUTORITA_DIF ";

    lStatement += " FROM AVVOCATO,AVVOCATO_FASCICOLO_SIEP,CG_REF_CODES AVVTIPODESC,CG_REF_CODES AVVTIPODESCMOTIVO, CG_REF_CODES AVVTIPODESCAUTORITA, CG_REF_CODES AVVTIPODESCAUTORITADIF,COMUNE COMNA,COMUNE COMRES, COMUNE COMSEDE, COMUNE COMSEDEDIF ";

    lStatement += " WHERE ";
   //--- Questo per noi non vale lStatement += " DATA_FINE_VALIDITA IS NULL AND ";
    lStatement += " AVVOCATO.ID_AVVOCATO=AVVOCATO_FASCICOLO_SIEP.AVV_ID_AVVOCATO AND ";
    lStatement += " AVVTIPODESC.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIEP.COD_TIPO_AVVOCATO AND ";
    lStatement += " AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO' AND ";
    lStatement += " AVVTIPODESCMOTIVO.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIEP.COD_MOTIVO_DESIGNAZIONE AND ";
    lStatement += " AVVTIPODESCMOTIVO.RV_DOMAIN='MOTIVO_DESIGNAZIONE' AND";
    lStatement += " AVVTIPODESCAUTORITA.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIEP.COD_TIPO_AUTORITA AND ";
    lStatement += " AVVTIPODESCAUTORITA.RV_DOMAIN='TIPO_AUTORITA' AND  ";
    lStatement += " AVVTIPODESCAUTORITADIF.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIEP.COD_TIPO_AUTORITA_DIF AND ";
    lStatement += " AVVTIPODESCAUTORITADIF.RV_DOMAIN='TIPO_AUTORITA' AND ";

    lStatement += "AVVOCATO.COD_LUOGO_NASCITA = COMNA.COD_COMUNE AND ";
    lStatement += "AVVOCATO.COD_COMUNE_RESIDENZA = COMRES.COD_COMUNE AND ";

    lStatement += "AVVOCATO_FASCICOLO_SIEP.SEDE_TIPO_AUTORITA = COMSEDE.COD_COMUNE AND ";
    lStatement += "AVVOCATO_FASCICOLO_SIEP.SEDE_TIPO_AUTORITA_DIF = COMSEDEDIF.COD_COMUNE ";

    return lStatement;
  }

  public void ricercaAvvocatoByKeyAvvocatoFasSiep(BigDecimal aKey) throws DAOException
  {
    String lStatement = getSqlQuery();

    lStatement += " AND AVVOCATO_FASCICOLO_SIEP.ID_AVVOCATO_FASCICOLO_SIEP=" + aKey;

    setStatement(lStatement);
  }


public GenericModel getModel() throws DAOException
  {
    AvvocatoSiepModel aModel = new AvvocatoSiepModel();

//Inserire le opportune set delle descrizioni!
    aModel.getAvvocato().setIdAvvocato(getBigDecimal("ID_AVVOCATO"));
    aModel.getAvvocato().setCognome(getString("COGNOME"));
    aModel.getAvvocato().setNome(getString("NOME"));
    aModel.getAvvocato().setForo(getString("FORO"));
    aModel.getAvvocato().setIndirizzo(getString("INDIRIZZO"));
    aModel.getAvvocato().setTelefono(getString("TELEFONO"));
    aModel.getAvvocato().setFax(getString("FAX"));
    aModel.getAvvocato().setEMail(getString("E_MAIL"));
    aModel.getAvvocato().setCodiceFiscale(getString("COD_FISCALE"));
    aModel.getAvvocato().setDescLuogoNascita(getString("COMUNE_NASCITA"));
    aModel.getAvvocato().setDescComuneResidenza(getString("COMUNE_RESIDENZA"));
    aModel.getAvvocato().setDataNascita(getDate("DATA_NASCITA"));
    aModel.getAvvocato().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.getAvvocato().setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.getAvvocato().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
    aModel.getAvvocato().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
    aModel.getAvvocato().setDescrTipo(getString("DESCRTIPO"));
    aModel.getAvvocato().setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
    aModel.getAvvocato().setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    aModel.getAvvocatoFascicoloSiepModel().setIdAvvocatoFascicoloSiep(getBigDecimal("ID_AVV_FASC_SIEP"));
    aModel.getAvvocatoFascicoloSiepModel().setCodOperatoreAggiornamento(getString("AVV_FASC_SIEP_COD_OP_AGG"));
    aModel.getAvvocatoFascicoloSiepModel().setCodOperatoreInserimento(getString("AVV_FASC_SIEP_COD_OP_INS"));
    aModel.getAvvocatoFascicoloSiepModel().setCodTipoAvvocato(getString("AVV_FASC_SIEP_COD_TIPO_AVV"));
    aModel.getAvvocatoFascicoloSiepModel().setCodUfficioAggiornamento(getString("AVV_FASC_SIEP_COD_UFF_AGG"));
    aModel.getAvvocatoFascicoloSiepModel().setCodUfficioInserimento(getString("AVV_FASC_SIEP_COD_UFF_INS"));
    aModel.getAvvocatoFascicoloSiepModel().setDataAggiornamento(getDate("AVV_FASC_SIEP_DATA_AGG"));
    aModel.getAvvocatoFascicoloSiepModel().setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
    aModel.getAvvocatoFascicoloSiepModel().setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
    aModel.getAvvocatoFascicoloSiepModel().setDataInserimento(getDate("AVV_FASC_SIEP_DATA_INSERIMENTO"));
    aModel.getAvvocatoFascicoloSiepModel().setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
    aModel.getAvvocatoFascicoloSiepModel().setCodMotivoDesignazione(getString("COD_MOTIVO_DESIGNAZIONE"));
    aModel.getAvvocatoFascicoloSiepModel().setDescrMotivoDesignazione(getString("DESCR_MOTIVO_DESIGNAZIONE"));
    aModel.getAvvocatoFascicoloSiepModel().setNote(getString("NOTE"));
    aModel.getAvvocatoFascicoloSiepModel().setCodTipoAutorita(getString("COD_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSiepModel().setDescrTipoAutorita(getString("DESCR_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSiepModel().setSedeAutorita(getString("SEDE_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSiepModel().setIndirizzoTipoAutorita(getString("INDIRIZZO_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSiepModel().setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
    aModel.getAvvocatoFascicoloSiepModel().setCodTipoAutoritaDif(getString("COD_TIPO_AUTORITA_DIF"));
    aModel.getAvvocatoFascicoloSiepModel().setDescrTipoAutoritaDif(getString("DESCR_TIPO_AUTORITA_DIF"));
    aModel.getAvvocatoFascicoloSiepModel().setSedeAutoritaDif(getString("SEDE_TIPO_AUTORITA_DIF"));
    aModel.getAvvocatoFascicoloSiepModel().setComuneTipoAutorita(getString("COMUNE_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSiepModel().setComuneTipoAutoritaDif(getString("COMUNE_TIPO_AUTORITA_DIF"));
    // STUB 06/04/2005 Aggiunta la seguente valorizzazione:
    aModel.getAvvocatoFascicoloSiepModel().setAvvIdAvvocato(getBigDecimal("AVV_ID_AVVOCATO"));
   //aModel.getAvvocatoFascicoloSiepModel().

    return aModel;
  }

}