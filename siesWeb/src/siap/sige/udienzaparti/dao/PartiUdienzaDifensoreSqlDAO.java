package siap.sige.udienzaparti.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
* <p>Title: PartiUdienzaDifensoreSqlDAO</p>
* <p>Description: Classe SqlDAO che consente l'accesso ai Difensori </p>
*    delle pari di una Udienza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/

public class PartiUdienzaDifensoreSqlDAO extends SIAPSqlDAO {
  
  public PartiUdienzaDifensoreSqlDAO (Connection con) {
    super(con);
  }

  /**
   * Slq Query di base.
   * <p>
   * @param aValue
   * @return
   * @throws DAOException 
   */
  protected String getSqlQuery()throws DAOException {
	  String lStatement = new String("");
    
     lStatement += "SELECT PUD.ID_AVVOCATO_PARTE_UDIENZA, " + 
			        " PUD.COD_TIPO_AVVOCATO, " + 
			        " PUD.DATA_INIZIO_VALIDITA, " +
			        " PUD.DATA_FINE_VALIDITA, " +
			        " PUD.COD_MOTIVO_DESIGNAZIONE," + 
			        " PUD.COD_TIPO_AUTORITA, " + 
			        " PUD.SEDE_TIPO_AUTORITA, " + 
			        " PUD.INDIRIZZO_TIPO_AUTORITA, " + 
			        " PUD.COD_OPERATORE_INSERIMENTO, " + 
			        " PUD.DATA_INSERIMENTO, " +  
			        " PUD.COD_UFFICIO_INSERIMENTO, " + 
			        " PUD.COD_OPERATORE_AGGIORNAMENTO, " + 
			        " PUD.DATA_AGGIORNAMENTO, " + 
			        " PUD.COD_UFFICIO_AGGIORNAMENTO, " +
			        " PUD.AVV_ID_AVVOCATO, "+
			        " PUD.SOGG_ID_SOGGETTO, " + 
			        " AVVOCATO.ID_AVVOCATO, "+
			        " AVVOCATO.COGNOME, "+
			        " AVVOCATO.NOME, "+
			        " AVVOCATO.FORO, "+
			        " AVVOCATO.INDIRIZZO, "+
			        " AVVOCATO.TELEFONO, "+
			        " AVVOCATO.FAX, "+
			        " AVVOCATO.E_MAIL, "+
			        " AVVOCATO.COD_FISCALE," +
			        " AVVOCATO.PROVINCIA," +
			        " AVVOCATO.CAP," +
			        " AVVOCATO.FLAG_VISUALIZZA," +
			        " AVVOCATO.ID_AVVOCATO_STANDARD," +
			        " AVVOCATO.NOTE NOTEAVV, "+
			        " AVVOCATO.COD_OPERATORE_INSERIMENTO, "+
			        " AVVOCATO.DATA_INSERIMENTO, "+
			        " AVVOCATO.COD_OPERATORE_AGGIORNAMENTO, "+
			        " AVVOCATO.COD_UFFICIO_INSERIMENTO, "+
			        " AVVOCATO.DATA_AGGIORNAMENTO, "+
			        " AVVTIPODESC.RV_MEANING DESCRTIPO, "+
			        " AVVOCATO.DATA_SOSPESO_FINO_AL, "+
			        " AVVOCATO.DATA_RADIATO_DAL, "+
			        " AVVOCATO.COD_NON_ATTIVITA, "+
			        " AVVOCATO.COD_LUOGO_NASCITA, "+
			        " AVVOCATO.COD_COMUNE_RESIDENZA, "+
			        " AVVOCATO.FLAG_CANCELLATO, "+
			        " DESCR.DESCRIZIONE DESCR_COMUNE_RESIDENZA, "+
			        " DESNASCITA.DESCRIZIONE DESCR_LUOGO_NASCITA, "+
			        " AVVOCATO.COD_UFFICIO_APPARTENENZA, "+
			        " AVVOCATO.DATA_NASCITA, "+
			        " CG.RV_MEANING DESCR_NON_ATTIVITA ";
     lStatement += " FROM AVVOCATO, PARTI_UDIENZA_DIFENSORE PUD, CG_REF_CODES AVVTIPODESC, CG_REF_CODES CG, COMUNE DESNASCITA, COMUNE DESCR";
     lStatement += " WHERE ";
     lStatement += " DATA_FINE_VALIDITA IS NULL";
     lStatement += " AND AVVOCATO.ID_AVVOCATO = PUD.AVV_ID_AVVOCATO";
     lStatement += " AND AVVTIPODESC.RV_LOW_VALUE = PUD.COD_TIPO_AVVOCATO ";
     lStatement += " AND AVVTIPODESC.RV_DOMAIN = 'TIPO_AVVOCATO'";
     lStatement += " AND DESCR.COD_COMUNE = COD_COMUNE_RESIDENZA ";
     lStatement += " AND DESNASCITA.COD_COMUNE = COD_LUOGO_NASCITA ";
     lStatement += " AND CG.RV_DOMAIN  = 'NON_ATTIVITA' ";
     lStatement += " AND CG.RV_LOW_VALUE = COD_NON_ATTIVITA ";
	
	 return lStatement;
  }
  
  /**
   * Metodo che recupera i dati dal resulset e ne popola il model.
   * <p>
   * @return istanza model ProcedimentixUdienzaModel
   * @throws DAOException propaga errore di eccezione.
   */
  public GenericModel getModel() throws DAOException {
    PartiUdienzaDifensoreModel aModel = new  PartiUdienzaDifensoreModel();
    AvvocatoModel avvocatoModel = new AvvocatoModel();
    
    aModel.setIdAvvocatoParteUdienza( getBigDecimal("ID_AVVOCATO_PARTE_UDIENZA") );
    aModel.setCodTipoAvvocato( getString("COD_TIPO_AVVOCATO") );
    aModel.setDataInizioValidita( getDate("DATA_INIZIO_VALIDITA") );
    aModel.setDataFineValidita( getDate("DATA_FINE_VALIDITA") );
    aModel.setCodMotivoDesignazione( getString("COD_MOTIVO_DESIGNAZIONE") );
    aModel.setCodTipoAutorita( getString("COD_TIPO_AUTORITA") );
    aModel.setSedeTipoAutorita( getString("SEDE_TIPO_AUTORITA") );
    aModel.setIndirizzoTipoAutorita( getString("INDIRIZZO_TIPO_AUTORITA") );
	aModel.setCodOperatoreInserimento( getString("COD_OPERATORE_INSERIMENTO") );
	aModel.setDataInserimento( getDate("DATA_INSERIMENTO") );
	aModel.setCodUfficioInserimento( getString("COD_UFFICIO_INSERIMENTO") );
	aModel.setCodOperatoreAggiornamento( getString("COD_OPERATORE_AGGIORNAMENTO") );
	aModel.setDataAggiornamento( getDate("DATA_AGGIORNAMENTO") );
	aModel.setCodUfficioAggiornamento( getString("COD_UFFICIO_AGGIORNAMENTO") );
    aModel.setAvvIdAvvocato( getBigDecimal("AVV_ID_AVVOCATO") );
    aModel.setSoggIdSoggetto( getBigDecimal("SOGG_ID_SOGGETTO") );
    // Dati Avvocato
    avvocatoModel.setIdAvvocato(getBigDecimal("ID_AVVOCATO") );
    avvocatoModel.setCognome(getString("COGNOME") );
    avvocatoModel.setNome(getString("NOME") );
    avvocatoModel.setForo(getString("FORO") );
    avvocatoModel.setIndirizzo(getString("INDIRIZZO") );
    avvocatoModel.setTelefono(getString("TELEFONO") );
    avvocatoModel.setFax(getString("FAX") );
    avvocatoModel.setEMail(getString("E_MAIL") );
    avvocatoModel.setNote(getString("NOTEAVV") );
    avvocatoModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO") );
    avvocatoModel.setDataInserimento(getDate("DATA_INSERIMENTO") );
    avvocatoModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO") );
    avvocatoModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO") );
    avvocatoModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO") );
    avvocatoModel.setDescrTipo(getString("DESCRTIPO"));
    avvocatoModel.setCodLuogoNascita(getString("COD_LUOGO_NASCITA"));
    avvocatoModel.setDescLuogoNascita(getString("DESCR_LUOGO_NASCITA"));
    avvocatoModel.setDescrNonAttivita(getString("DESCR_NON_ATTIVITA"));
    avvocatoModel.setDescComuneResidenza(getString("DESCR_COMUNE_RESIDENZA"));
    avvocatoModel.setCodComuneResidenza(getString("COD_COMUNE_RESIDENZA"));
    avvocatoModel.setDataNascita(getDate("DATA_NASCITA") );
    avvocatoModel.setDataSospensione(getDate("DATA_SOSPESO_FINO_AL") );
    avvocatoModel.setDataRadiazione(getDate("DATA_RADIATO_DAL") );
    avvocatoModel.setCodNonAttivita(getString("COD_NON_ATTIVITA"));
    avvocatoModel.setCodUffAppartenenza(getString("COD_UFFICIO_APPARTENENZA"));
    avvocatoModel.setFlagCancellato(getString("FLAG_CANCELLATO"));
    avvocatoModel.setCodiceFiscale(getString("COD_FISCALE"));
    avvocatoModel.setProvincia(getString("PROVINCIA"));
    avvocatoModel.setCap(getString("CAP"));
    avvocatoModel.setFlagVisualizza(getBigDecimal("FLAG_VISUALIZZA"));
    avvocatoModel.setIdAvvocatoStandard(getBigDecimal("ID_AVVOCATO_STANDARD"));

    aModel.setAvvocato(avvocatoModel);
    
    return aModel;
  }

  /**
   * Metodo che imposta le condizioni di ricerca dei difensori
   * associati alle parti (Offese/Civile) di una udienza.
   * <p>
   * @param aIdSoggetto id della parte coinvolta.
    */
  public void ricercaDifensoreByIdSoggetto(BigDecimal aIdSoggetto)
		  throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += "  " + getCondizioneByIdSoggetto(aIdSoggetto);
    
    setStatement(lSql);
  }
  
  /**
   * Metodo che compone, imposta e ritorna la condizione di filtro 
   * per l'id della parte Offesa/Civile.<p>
   * @param aKey id della parte Offesa/Civile.
   * @return ritorna la condizione di filtro.
   */
  public String getCondizioneByIdSoggetto(BigDecimal aIdSoggetto) {
	  return " AND PUD.SOGG_ID_SOGGETTO = " + aIdSoggetto;
  }

  public void ricercaDifensoreParteByKeyAvvocato(BigDecimal aKey) throws DAOException
  {
    String lStatement = getSqlQuery();
    lStatement += " AND DATA_FINE_VALIDITA IS NULL ";
    lStatement += " AND PUD.AVV_ID_AVVOCATO=" + aKey;

    setStatement(lStatement);
  }

}