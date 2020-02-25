package siap.sius.avvocato.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

public class AvvocatoFascicoloSiusSqlDAO	extends SIAPSqlDAO
{
	public AvvocatoFascicoloSiusSqlDAO(Connection con)
   {
		super(con);
   }

	public void ricercaAvvocatiByFascicolo(BigDecimal aKey) throws DAOException
   {
		String lStatement = getSqlQuery();
                lStatement += " AND DATA_FINE_VALIDITA IS NULL ";
		lStatement += " AND FAS_SIU_ID_FASCICOLO_SIUS=" + aKey;

		//Gli avvocati sono ordinati in ordine crescente di ID_AVVOCATO
		lStatement += " ORDER BY ID_AVVOCATO ASC";

		setStatement(lStatement);
    }

	public void ricercaAvvocatoByKeyAvvocatoFasSius(BigDecimal aKey) throws DAOException
		{
		 String lStatement = getSqlQuery();

		 lStatement += " AND AVVOCATO_FASCICOLO_SIUS.ID_AVVOCATO_FASCICOLO_SIUS=" + aKey;

		 setStatement(lStatement);
	  }

  public void ricercaAvvocatoFascicoloSiusByKeyAvvocato(BigDecimal aKey) throws DAOException
    {
      String lStatement = getSqlQuery();
      lStatement += " AND DATA_FINE_VALIDITA IS NULL ";
      lStatement += " AND AVVOCATO_FASCICOLO_SIUS.AVV_ID_AVVOCATO=" + aKey;

      setStatement(lStatement);
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
      "COD_FISCALE," +
      "PROVINCIA," +
      "AVVOCATO.CAP," +
      "FLAG_VISUALIZZA," +
      "FLAG_CANCELLATO," +
      "ID_AVVOCATO_STANDARD," +   
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
      "AVVOCATO.COD_NON_ATTIVITA, " +
      "AVVOCATO.DATA_SOSPESO_FINO_AL, " +
      "AVVOCATO.DATA_RADIATO_DAL, " +
      "AVVOCATO.COD_UFFICIO_APPARTENENZA, " +
      "AVVTIPODESC.RV_MEANING DESCRTIPO, " +
      "AVVOCATO_FASCICOLO_SIUS.COD_OPERATORE_INSERIMENTO AVV_FASC_SIUS_COD_OP_INS, " +
      "AVVOCATO_FASCICOLO_SIUS.DATA_INSERIMENTO AVV_FASC_SIUS_DATA_INSERIMENTO, " +
      "AVVOCATO_FASCICOLO_SIUS.DATA_AGGIORNAMENTO AVV_FASC_SIUS_DATA_AGG, " +
      "AVVOCATO_FASCICOLO_SIUS.COD_OPERATORE_AGGIORNAMENTO AVV_FASC_SIUS_COD_OP_AGG, " +
      "AVVOCATO_FASCICOLO_SIUS.ID_AVVOCATO_FASCICOLO_SIUS ID_AVV_FASC_SIUS, " +
      "AVVOCATO_FASCICOLO_SIUS.AVV_ID_AVVOCATO, " +
      "AVVOCATO_FASCICOLO_SIUS.COD_TIPO_AVVOCATO, " +
      "AVVOCATO_FASCICOLO_SIUS.DATA_INIZIO_VALIDITA, " +
      "AVVOCATO_FASCICOLO_SIUS.DATA_FINE_VALIDITA, " +
      "AVVOCATO_FASCICOLO_SIUS.FAS_SIU_ID_FASCICOLO_SIUS, "+
      "AVVOCATO_FASCICOLO_SIUS.COD_UFFICIO_INSERIMENTO AVV_FASC_SIUS_COD_UFF_INS, " +
      "AVVOCATO_FASCICOLO_SIUS.COD_UFFICIO_AGGIORNAMENTO AVV_FASC_SIUS_COD_UFF_AGG, "+
      "AVVOCATO_FASCICOLO_SIUS.COD_MOTIVO_DESIGNAZIONE, "+
      "AVVTIPODESCMOTIVO.RV_MEANING DESCR_MOTIVO_DESIGNAZIONE, "+
      "AVVOCATO_FASCICOLO_SIUS.NOTE,  "+
      "AVVOCATO_FASCICOLO_SIUS.COD_TIPO_AUTORITA, "+
      "AVVTIPODESCAUTORITA.RV_MEANING DESCR_TIPO_AUTORITA, "+
      "AVVOCATO_FASCICOLO_SIUS.SEDE_TIPO_AUTORITA, "+
      "COMSEDE.DESCRIZIONE COMUNE_TIPO_AUTORITA, " +
      "AVVOCATO_FASCICOLO_SIUS.INDIRIZZO_TIPO_AUTORITA, "+
      "AVVOCATO_FASCICOLO_SIUS.IST_DET_ID_ISTITUTO_DETENZIONE, "+
      "AVVOCATO_FASCICOLO_SIUS.COD_TIPO_AUTORITA_DIF, "+
      "AVVTIPODESCAUTORITADIF.RV_MEANING DESCR_TIPO_AUTORITA_DIF, "+
      "COMSEDEDIF.DESCRIZIONE COMUNE_TIPO_AUTORITA_DIF, "+
      "AVVOCATO_FASCICOLO_SIUS.SEDE_TIPO_AUTORITA_DIF ";

    lStatement += " FROM AVVOCATO, AVVOCATO_FASCICOLO_SIUS, CG_REF_CODES AVVTIPODESC, CG_REF_CODES AVVTIPODESCMOTIVO, CG_REF_CODES AVVTIPODESCAUTORITA, CG_REF_CODES AVVTIPODESCAUTORITADIF, COMUNE COMNA, COMUNE COMRES, COMUNE COMSEDE, COMUNE COMSEDEDIF";

    lStatement += " WHERE ";
   // lStatement += " DATA_FINE_VALIDITA IS NULL AND ";
    lStatement += " AVVOCATO.ID_AVVOCATO=AVVOCATO_FASCICOLO_SIUS.AVV_ID_AVVOCATO AND ";
    lStatement += " AVVTIPODESC.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIUS.COD_TIPO_AVVOCATO AND ";
    lStatement += " AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO' AND ";
    lStatement += " AVVTIPODESCMOTIVO.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIUS.COD_MOTIVO_DESIGNAZIONE AND ";
    lStatement += " AVVTIPODESCMOTIVO.RV_DOMAIN='MOTIVO_DESIGNAZIONE' AND";
    lStatement += " AVVTIPODESCAUTORITA.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIUS.COD_TIPO_AUTORITA AND ";
    lStatement += " AVVTIPODESCAUTORITA.RV_DOMAIN='TIPO_AUTORITA' AND  ";
    lStatement += " AVVTIPODESCAUTORITADIF.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIUS.COD_TIPO_AUTORITA_DIF AND ";
    lStatement += " AVVTIPODESCAUTORITADIF.RV_DOMAIN='TIPO_AUTORITA' AND ";
    lStatement += " AVVOCATO.COD_LUOGO_NASCITA = COMNA.COD_COMUNE AND ";
    lStatement += " AVVOCATO.COD_COMUNE_RESIDENZA = COMRES.COD_COMUNE AND ";
    lStatement += " AVVOCATO_FASCICOLO_SIUS.SEDE_TIPO_AUTORITA = COMSEDE.COD_COMUNE AND ";
    lStatement += " AVVOCATO_FASCICOLO_SIUS.SEDE_TIPO_AUTORITA_DIF = COMSEDEDIF.COD_COMUNE ";

    return lStatement;

  }


  //
  // METODO GETMODEL()
  //

  public GenericModel getModel() throws DAOException
  {
    AvvocatoSiusModel aModel = new AvvocatoSiusModel();

    aModel.getAvvocato().setIdAvvocato(getBigDecimal("ID_AVVOCATO"));
    aModel.getAvvocato().setCognome(getString("COGNOME"));
    aModel.getAvvocato().setNome(getString("NOME"));
    aModel.getAvvocato().setForo(getString("FORO"));
    aModel.getAvvocato().setIndirizzo(getString("INDIRIZZO"));
    aModel.getAvvocato().setTelefono(getString("TELEFONO"));
    aModel.getAvvocato().setFax(getString("FAX"));
    aModel.getAvvocato().setEMail(getString("E_MAIL"));
    aModel.getAvvocato().setCodLuogoNascita(getString("COD_LUOGO_NASCITA"));  // STUB 11/04/2005
    aModel.getAvvocato().setDescLuogoNascita(getString("COMUNE_NASCITA"));
    aModel.getAvvocato().setCodComuneResidenza(getString("COD_COMUNE_RESIDENZA"));  // STUB 11/04/2005
    aModel.getAvvocato().setDescComuneResidenza(getString("COMUNE_RESIDENZA"));
    aModel.getAvvocato().setDataNascita(getDate("DATA_NASCITA"));

    aModel.getAvvocato().setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
    aModel.getAvvocato().setDataInserimento(getDate("DATA_INSERIMENTO"));
    aModel.getAvvocato().setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
    aModel.getAvvocato().setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
    aModel.getAvvocato().setDescrTipo(getString("DESCRTIPO"));
    aModel.getAvvocato().setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
    aModel.getAvvocato().setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
    aModel.getAvvocato().setCodNonAttivita(getString("COD_NON_ATTIVITA"));  // STUB 11/04/2005
    aModel.getAvvocato().setDataSospensione(getDate("DATA_SOSPESO_FINO_AL"));  // STUB 11/04/2005
    aModel.getAvvocato().setDataRadiazione(getDate("DATA_RADIATO_DAL"));  // STUB 11/04/2005
    aModel.getAvvocato().setCodUffAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));  // STUB 11/04/2005
    aModel.getAvvocato().setCodiceFiscale(getString("COD_FISCALE"));
    aModel.getAvvocato().setProvincia(getString("PROVINCIA"));
    aModel.getAvvocato().setCap(getString("CAP"));
    aModel.getAvvocato().setFlagVisualizza(getBigDecimal("FLAG_VISUALIZZA"));
    aModel.getAvvocato().setIdAvvocatoStandard(getBigDecimal("ID_AVVOCATO_STANDARD"));


    aModel.getAvvocatoFascicoloSiusModel().setIdAvvocatoFascicoloSius(getBigDecimal("ID_AVV_FASC_SIUS"));
    aModel.getAvvocatoFascicoloSiusModel().setCodOperatoreAggiornamento(getString("AVV_FASC_SIUS_COD_OP_AGG"));
    aModel.getAvvocatoFascicoloSiusModel().setCodOperatoreInserimento(getString("AVV_FASC_SIUS_COD_OP_INS"));
    aModel.getAvvocatoFascicoloSiusModel().setCodTipoAvvocato(getString("COD_TIPO_AVVOCATO"));
    aModel.getAvvocatoFascicoloSiusModel().setCodUfficioAggiornamento(getString("AVV_FASC_SIUS_COD_UFF_AGG"));
    aModel.getAvvocatoFascicoloSiusModel().setCodUfficioInserimento(getString("AVV_FASC_SIUS_COD_UFF_INS"));
    aModel.getAvvocatoFascicoloSiusModel().setDataAggiornamento(getDate("AVV_FASC_SIUS_DATA_AGG"));
    aModel.getAvvocatoFascicoloSiusModel().setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
    aModel.getAvvocatoFascicoloSiusModel().setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
    aModel.getAvvocatoFascicoloSiusModel().setAvvIdAvvocato(getBigDecimal("AVV_ID_AVVOCATO"));
    aModel.getAvvocatoFascicoloSiusModel().setDataInserimento(getDate("AVV_FASC_SIUS_DATA_INSERIMENTO"));
    aModel.getAvvocatoFascicoloSiusModel().setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
    aModel.getAvvocatoFascicoloSiusModel().setCodMotivoDesignazione(getString("COD_MOTIVO_DESIGNAZIONE"));
    aModel.getAvvocatoFascicoloSiusModel().setDescrMotivoDesignazione(getString("DESCR_MOTIVO_DESIGNAZIONE"));
    aModel.getAvvocatoFascicoloSiusModel().setNote(getString("NOTE"));
    aModel.getAvvocatoFascicoloSiusModel().setCodTipoAutorita(getString("COD_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSiusModel().setDescrTipoAutorita(getString("DESCR_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSiusModel().setSedeAutorita(getString("SEDE_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSiusModel().setIndirizzoTipoAutorita(getString("INDIRIZZO_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSiusModel().setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
    aModel.getAvvocatoFascicoloSiusModel().setCodTipoAutoritaDif(getString("COD_TIPO_AUTORITA_DIF"));
    aModel.getAvvocatoFascicoloSiusModel().setDescrTipoAutoritaDif(getString("DESCR_TIPO_AUTORITA_DIF"));
    aModel.getAvvocatoFascicoloSiusModel().setSedeAutoritaDif(getString("SEDE_TIPO_AUTORITA_DIF"));
    aModel.getAvvocatoFascicoloSiusModel().setComuneTipoAutorita(getString("COMUNE_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSiusModel().setComuneTipoAutoritaDif(getString("COMUNE_TIPO_AUTORITA_DIF"));

    return aModel;
  }

}