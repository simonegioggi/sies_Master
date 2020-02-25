package siap.sige.avvocato.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

public class AvvocatoFascicoloSigeSqlDAO	extends SIAPSqlDAO
{
	public AvvocatoFascicoloSigeSqlDAO(Connection con)
   {
		super(con);
   }

	public void ricercaAvvocatiByFascicolo(BigDecimal aKey) throws DAOException
   {
		String lStatement = getSqlQuery();
                lStatement += " AND DATA_FINE_VALIDITA IS NULL ";
		lStatement += " AND FAS_SIGE_ID_FASCICOLO_SIGE=" + aKey;

		//Gli avvocati sono ordinati in ordine crescente di ID_AVVOCATO
		lStatement += " ORDER BY ID_AVVOCATO ASC";

		setStatement(lStatement);
    }

	public void ricercaAvvocatoByKeyAvvocatoFasSige(BigDecimal aKey) throws DAOException
		{
		 String lStatement = getSqlQuery();

		 lStatement += " AND AVVOCATO_FASCICOLO_SIGE.ID_AVVOCATO_FASCICOLO_SIGE=" + aKey;

		 setStatement(lStatement);
	  }

  public void ricercaAvvocatoFascicoloSigeByKeyAvvocato(BigDecimal aKey) throws DAOException
    {
      String lStatement = getSqlQuery();
      lStatement += " AND DATA_FINE_VALIDITA IS NULL ";
      lStatement += " AND AVVOCATO_FASCICOLO_SIGE.AVV_ID_AVVOCATO=" + aKey;

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
      "AVVOCATO_FASCICOLO_SIGE.COD_OPERATORE_INSERIMENTO AVV_FASC_SIGE_COD_OP_INS, " +
      "AVVOCATO_FASCICOLO_SIGE.DATA_INSERIMENTO AVV_FASC_SIGE_DATA_INSERIMENTO, " +
      "AVVOCATO_FASCICOLO_SIGE.DATA_AGGIORNAMENTO AVV_FASC_SIGE_DATA_AGG, " +
      "AVVOCATO_FASCICOLO_SIGE.COD_OPERATORE_AGGIORNAMENTO AVV_FASC_SIGE_COD_OP_AGG, " +
      "AVVOCATO_FASCICOLO_SIGE.ID_AVVOCATO_FASCICOLO_SIGE ID_AVV_FASC_SIGE, " +
      "AVVOCATO_FASCICOLO_SIGE.AVV_ID_AVVOCATO, " +
      "AVVOCATO_FASCICOLO_SIGE.COD_TIPO_AVVOCATO, " +
      "AVVOCATO_FASCICOLO_SIGE.DATA_INIZIO_VALIDITA, " +
      "AVVOCATO_FASCICOLO_SIGE.DATA_FINE_VALIDITA, " +
      "AVVOCATO_FASCICOLO_SIGE.FAS_SIGE_ID_FASCICOLO_SIGE, "+
      "AVVOCATO_FASCICOLO_SIGE.COD_UFFICIO_INSERIMENTO AVV_FASC_SIGE_COD_UFF_INS, " +
      "AVVOCATO_FASCICOLO_SIGE.COD_UFFICIO_AGGIORNAMENTO AVV_FASC_SIGE_COD_UFF_AGG, "+
      "AVVOCATO_FASCICOLO_SIGE.COD_MOTIVO_DESIGNAZIONE, "+
      "AVVTIPODESCMOTIVO.RV_MEANING DESCR_MOTIVO_DESIGNAZIONE, "+
      "AVVOCATO_FASCICOLO_SIGE.NOTE,  "+
      "AVVOCATO_FASCICOLO_SIGE.COD_TIPO_AUTORITA, "+
      "AVVTIPODESCAUTORITA.RV_MEANING DESCR_TIPO_AUTORITA, "+
      "AVVOCATO_FASCICOLO_SIGE.SEDE_TIPO_AUTORITA, "+
      "COMSEDE.DESCRIZIONE COMUNE_TIPO_AUTORITA, " +
      "AVVOCATO_FASCICOLO_SIGE.INDIRIZZO_TIPO_AUTORITA, "+
      "AVVOCATO_FASCICOLO_SIGE.IST_DET_ID_ISTITUTO_DETENZIONE, "+
      "AVVOCATO_FASCICOLO_SIGE.COD_TIPO_AUTORITA_DIF, "+
      "AVVTIPODESCAUTORITADIF.RV_MEANING DESCR_TIPO_AUTORITA_DIF, "+
      "COMSEDEDIF.DESCRIZIONE COMUNE_TIPO_AUTORITA_DIF, "+
      "AVVOCATO_FASCICOLO_SIGE.SEDE_TIPO_AUTORITA_DIF ";

    lStatement += " FROM AVVOCATO, AVVOCATO_FASCICOLO_SIGE, CG_REF_CODES AVVTIPODESC, CG_REF_CODES AVVTIPODESCMOTIVO, CG_REF_CODES AVVTIPODESCAUTORITA, CG_REF_CODES AVVTIPODESCAUTORITADIF, COMUNE COMNA, COMUNE COMRES, COMUNE COMSEDE, COMUNE COMSEDEDIF";

    lStatement += " WHERE ";
   // lStatement += " DATA_FINE_VALIDITA IS NULL AND ";
    lStatement += " AVVOCATO.ID_AVVOCATO=AVVOCATO_FASCICOLO_SIGE.AVV_ID_AVVOCATO AND ";
    lStatement += " AVVTIPODESC.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIGE.COD_TIPO_AVVOCATO AND ";
    lStatement += " AVVTIPODESC.RV_DOMAIN='TIPO_AVVOCATO' AND ";
    lStatement += " AVVTIPODESCMOTIVO.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIGE.COD_MOTIVO_DESIGNAZIONE AND ";
    lStatement += " AVVTIPODESCMOTIVO.RV_DOMAIN='MOTIVO_DESIGNAZIONE' AND";
    lStatement += " AVVTIPODESCAUTORITA.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIGE.COD_TIPO_AUTORITA AND ";
    lStatement += " AVVTIPODESCAUTORITA.RV_DOMAIN='TIPO_AUTORITA' AND  ";
    lStatement += " AVVTIPODESCAUTORITADIF.RV_LOW_VALUE=AVVOCATO_FASCICOLO_SIGE.COD_TIPO_AUTORITA_DIF AND ";
    lStatement += " AVVTIPODESCAUTORITADIF.RV_DOMAIN='TIPO_AUTORITA' AND ";
    lStatement += " AVVOCATO.COD_LUOGO_NASCITA = COMNA.COD_COMUNE AND ";
    lStatement += " AVVOCATO.COD_COMUNE_RESIDENZA = COMRES.COD_COMUNE AND ";
    lStatement += " AVVOCATO_FASCICOLO_SIGE.SEDE_TIPO_AUTORITA = COMSEDE.COD_COMUNE AND ";
    lStatement += " AVVOCATO_FASCICOLO_SIGE.SEDE_TIPO_AUTORITA_DIF = COMSEDEDIF.COD_COMUNE ";

    return lStatement;

  }


  //
  // METODO GETMODEL()
  //

  public GenericModel getModel() throws DAOException
  {
    AvvocatoSigeModel aModel = new AvvocatoSigeModel();

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


    aModel.getAvvocatoFascicoloSigeModel().setIdAvvocatoFascicoloSige(getBigDecimal("ID_AVV_FASC_SIGE"));
    aModel.getAvvocatoFascicoloSigeModel().setCodOperatoreAggiornamento(getString("AVV_FASC_SIGE_COD_OP_AGG"));
    aModel.getAvvocatoFascicoloSigeModel().setCodOperatoreInserimento(getString("AVV_FASC_SIGE_COD_OP_INS"));
    aModel.getAvvocatoFascicoloSigeModel().setCodTipoAvvocato(getString("COD_TIPO_AVVOCATO"));
    aModel.getAvvocatoFascicoloSigeModel().setCodUfficioAggiornamento(getString("AVV_FASC_SIGE_COD_UFF_AGG"));
    aModel.getAvvocatoFascicoloSigeModel().setCodUfficioInserimento(getString("AVV_FASC_SIGE_COD_UFF_INS"));
    aModel.getAvvocatoFascicoloSigeModel().setDataAggiornamento(getDate("AVV_FASC_SIGE_DATA_AGG"));
    aModel.getAvvocatoFascicoloSigeModel().setDataInizioValidita(getDate("DATA_INIZIO_VALIDITA"));
    aModel.getAvvocatoFascicoloSigeModel().setDataFineValidita(getDate("DATA_FINE_VALIDITA"));
    aModel.getAvvocatoFascicoloSigeModel().setAvvIdAvvocato(getBigDecimal("AVV_ID_AVVOCATO"));
    aModel.getAvvocatoFascicoloSigeModel().setDataInserimento(getDate("AVV_FASC_SIGE_DATA_INSERIMENTO"));
    aModel.getAvvocatoFascicoloSigeModel().setFasSigeIdFascicoloSige(getBigDecimal("FAS_SIGE_ID_FASCICOLO_SIGE"));
    aModel.getAvvocatoFascicoloSigeModel().setCodMotivoDesignazione(getString("COD_MOTIVO_DESIGNAZIONE"));
    aModel.getAvvocatoFascicoloSigeModel().setDescrMotivoDesignazione(getString("DESCR_MOTIVO_DESIGNAZIONE"));
    aModel.getAvvocatoFascicoloSigeModel().setNote(getString("NOTE"));
    aModel.getAvvocatoFascicoloSigeModel().setCodTipoAutorita(getString("COD_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSigeModel().setDescrTipoAutorita(getString("DESCR_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSigeModel().setSedeAutorita(getString("SEDE_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSigeModel().setIndirizzoTipoAutorita(getString("INDIRIZZO_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSigeModel().setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
    aModel.getAvvocatoFascicoloSigeModel().setCodTipoAutoritaDif(getString("COD_TIPO_AUTORITA_DIF"));
    aModel.getAvvocatoFascicoloSigeModel().setDescrTipoAutoritaDif(getString("DESCR_TIPO_AUTORITA_DIF"));
    aModel.getAvvocatoFascicoloSigeModel().setSedeAutoritaDif(getString("SEDE_TIPO_AUTORITA_DIF"));
    aModel.getAvvocatoFascicoloSigeModel().setComuneTipoAutorita(getString("COMUNE_TIPO_AUTORITA"));
    aModel.getAvvocatoFascicoloSigeModel().setComuneTipoAutoritaDif(getString("COMUNE_TIPO_AUTORITA_DIF"));

    return aModel;
  }

}