package siap.sige.udienzaparti.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;

/**
 * PartiUdienzaDifensoreDAO - Classe DAO che rappresenta la tabella PARTI_UDIENZA_DIFENSORE
 *
* @version 1.0
*/
public class PartiUdienzaDifensoreDAO extends SIAPTableDAO {

	public PartiUdienzaDifensoreDAO(Connection con) {

	    super(con);
	    setTable("PARTI_UDIENZA_DIFENSORE");
	
	    //Settare la Sequence e i campi chiave
	    setSequenceField("ID_AVVOCATO_PARTE_UDIENZA", "SEQ_PARTI_UDIENZA_DIFENSORE");
	    setFieldKey("ID_AVVOCATO_PARTE_UDIENZA", BIG_DECIMAL);
	
	    setField("ID_AVVOCATO_PARTE_UDIENZA", BIG_DECIMAL);
	    setField("COD_TIPO_AVVOCATO", STRING);
	    setField("DATA_INIZIO_VALIDITA", DATE);
	    setField("DATA_FINE_VALIDITA", DATE);
	    setField("COD_MOTIVO_DESIGNAZIONE", STRING);
	    setField("COD_TIPO_AUTORITA", STRING);
	    setField("SEDE_TIPO_AUTORITA", STRING);
	    setField("INDIRIZZO_TIPO_AUTORITA", STRING);
	    setField("COD_OPERATORE_INSERIMENTO", STRING);
	    setField("DATA_INSERIMENTO", DATE);
	    setField("COD_UFFICIO_INSERIMENTO", STRING);
	    setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
	    setField("DATA_AGGIORNAMENTO", DATE);
	    setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
	    setField("AVV_ID_AVVOCATO", BIG_DECIMAL);
	    setField("SOGG_ID_SOGGETTO", BIG_DECIMAL);
	    setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
	    setField("COD_TIPO_AUTORITA_DIF", STRING);
	    setField("SEDE_TIPO_AUTORITA_DIF", STRING);
	    setField("NOTE", STRING);
  }
	
	  //
	  // METODI GET()
	  //
	public BigDecimal getIdAvvocatoParteUdienza() throws DAOException {
		return getBigDecimal("ID_AVVOCATO_PARTE_UDIENZA");
	}

	public String getCodTipoAvvocato() throws DAOException {
		return getString("COD_TIPO_AVVOCATO");
	}

	public Date getDataInizioValidita() throws DAOException {
		return getDate("DATA_INIZIO_VALIDITA");
	}

	public Date getDataFineValidita() throws DAOException {
		return getDate("DATA_FINE_VALIDITA");
	}

	public String getCodMotivoDesignazione() throws DAOException {
		return getString("COD_MOTIVO_DESIGNAZIONE");
	}

	public String getCodTipoAutorita() throws DAOException {
		return getString("COD_TIPO_AUTORITA");
	}

	public String getSedeTipoAutorita() throws DAOException {
		return getString("SEDE_TIPO_AUTORITA");
	}

	public String getIndirizzoTipoAutorita() throws DAOException {
		return getString("INDIRIZZO_TIPO_AUTORITA");
	}

	public String getCodOperatoreInserimento() throws DAOException {
		return getString("COD_OPERATORE_INSERIMENTO");
	}

	public Date getDataInserimento() throws DAOException {
		return getDate("DATA_INSERIMENTO");
	}

	public String getCodUfficioInserimento() throws DAOException {
		return getString("COD_UFFICIO_INSERIMENTO");
	}

	public String getCodOperatoreAggiornamento() throws DAOException {
		return getString("COD_OPERATORE_AGGIORNAMENTO");
	}

	public Date getDataAggiornamento() throws DAOException {
		return getDate("DATA_AGGIORNAMENTO");
	}

	public String getCodUfficioAggiornamento() throws DAOException {
		return getString("COD_UFFICIO_AGGIORNAMENTO");
	}

	public BigDecimal getAvvIdAvvocato() throws DAOException {
		return getBigDecimal("AVV_ID_AVVOCATO");
	}

	public BigDecimal getSoggIdSoggetto() throws DAOException {
		return getBigDecimal("SOGG_ID_SOGGETTO");
	}

	public String getCodTipoAutoritaDif() throws DAOException {
		return getString("COD_TIPO_AUTORITA_DIF");
	}

	public String getSedeTipoAutoritaDif() throws DAOException {
		return getString("SEDE_TIPO_AUTORITA_DIF");
	}

	public String getIstDetIdIstitutoDetenzione() throws DAOException {
		return getString("IST_DET_ID_ISTITUTO_DETENZIONE");
	}

	public String getNote() throws DAOException {
		return getString("NOTE");
	}

	  //
	  // METODI SET()
	  //
	public void setIdAvvocatoParteUdienza(BigDecimal aValore) {
		setBigDecimal("ID_AVVOCATO_PARTE_UDIENZA", aValore);
	}

	public void setCodTipoAvvocato(String aValore) {
		setString("COD_TIPO_AVVOCATO", aValore);
	}

	public void setDataInizioValidita(Date aValore) {
		setDate("DATA_INIZIO_VALIDITA", aValore);
	}

	public void setDataFineValidita(Date aValore) {
		setDate("DATA_FINE_VALIDITA", aValore);
	}

	public void setCodMotivoDesignazione(String aValore) {
		setString("COD_MOTIVO_DESIGNAZIONE", aValore);
	}

	public void setCodTipoAutorita(String aValore) {
		setString("COD_TIPO_AUTORITA", aValore);
	}

	public void setSedeTipoAutorita(String aValore) {
		setString("SEDE_TIPO_AUTORITA", aValore);
	}

	public void setIndirizzoTipoAutorita(String aValore) {
		setString("INDIRIZZO_TIPO_AUTORITA", aValore);
	}

	public void setCodOperatoreInserimento(String aValore) {
		setString("COD_OPERATORE_INSERIMENTO", aValore);
	}

	public void setDataInserimento(Date aValore) {
		setDate("DATA_INSERIMENTO", aValore);
	}

	public void setCodUfficioInserimento(String aValore) {
		setString("COD_UFFICIO_INSERIMENTO", aValore);
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		setString("COD_OPERATORE_AGGIORNAMENTO", aValore);
	}

	public void setDataAggiornamento(Date aValore) {
		setDate("DATA_AGGIORNAMENTO", aValore);
	}

	public void setCodUfficioAggiornamento(String aValore) {
		setString("COD_UFFICIO_AGGIORNAMENTO", aValore);
	}

	public void setAvvIdAvvocato(BigDecimal aValore) {
		setBigDecimal("AVV_ID_AVVOCATO", aValore);
	}

	public void setSoggIdSoggetto(BigDecimal aValore) {
		setBigDecimal("SOGG_ID_SOGGETTO", aValore);
	}

	public void setCodTipoAutoritaDif(String aValore) {
		setString("COD_TIPO_AUTORITA_DIF", aValore);
	}

	public void setSedeTipoAutoritaDif(String aValore) {
		setString("SEDE_TIPO_AUTORITA_DIF", aValore);
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		setString("IST_DET_ID_ISTITUTO_DETENZIONE", aValore);
	}

	public void setNote(String aValore) {
		setString("NOTE", aValore);
	}

	public GenericModel getModel() throws DAOException {
	  
		return new PartiUdienzaDifensoreModel(getIdAvvocatoParteUdienza(), getCodTipoAvvocato(),
				getDataInizioValidita(), getDataFineValidita(), getCodMotivoDesignazione(),
				getCodTipoAutorita(), getSedeTipoAutorita(), getIndirizzoTipoAutorita(),
				getCodOperatoreInserimento(), getDataInserimento(), getCodUfficioInserimento(),
				getCodOperatoreAggiornamento(), getDataAggiornamento(), getCodUfficioAggiornamento(),
				getAvvIdAvvocato(), getSoggIdSoggetto(), getIstDetIdIstitutoDetenzione(), getNote(),
				getCodTipoAutoritaDif(), getSedeTipoAutoritaDif(), "", "", "", "", "");
	  }

	public void setDAOFromModel(PartiUdienzaDifensoreModel aModel) throws DAOException {

		  setIdAvvocatoParteUdienza( aModel.getIdAvvocatoParteUdienza() );
		  setCodTipoAvvocato( aModel.getCodTipoAvvocato() );
		  setDataInizioValidita( aModel.getDataInizioValidita() );
		  setDataFineValidita( aModel.getDataFineValidita() );
		  setCodMotivoDesignazione( aModel.getCodMotivoDesignazione() );
		  setCodTipoAutorita( aModel.getCodTipoAutorita() );
		  setSedeTipoAutorita( aModel.getSedeTipoAutorita() );
		  setIndirizzoTipoAutorita( aModel.getIndirizzoTipoAutorita() );
		  setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
		  setDataInserimento( aModel.getDataInserimento() );
		  setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
		  setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
		  setDataAggiornamento( aModel.getDataAggiornamento() );
		  setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
		  setAvvIdAvvocato( aModel.getAvvIdAvvocato() );
		  setSoggIdSoggetto( aModel.getSoggIdSoggetto() );
	      setCodTipoAutoritaDif( aModel.getCodTipoAutoritaDif() );
	      setSedeTipoAutoritaDif( aModel.getSedeAutoritaDif() );
	      setIstDetIdIstitutoDetenzione( aModel.getIstDetIdIstitutoDetenzione() );
	      setNote( aModel.getNote() );	  
	  }

	public void setDAOFromModelForUpdate(PartiUdienzaDifensoreModel aModel) throws DAOException {

		  setAvvIdAvvocato( aModel.getAvvIdAvvocato() );
	      setCodTipoAvvocato( aModel.getCodTipoAvvocato() );
	      setDataInizioValidita( aModel.getDataInizioValidita() );
	      setDataFineValidita( aModel.getDataFineValidita() );
	      setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
	      setDataInserimento( aModel.getDataInserimento() );
	      setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
	      setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
	      setDataAggiornamento( aModel.getDataAggiornamento() );
	      setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
	      setSoggIdSoggetto( aModel.getSoggIdSoggetto() );
	      setAvvIdAvvocato( aModel.getAvvIdAvvocato() );
	      setCodTipoAutorita( aModel.getCodTipoAutorita() );
	      setSedeTipoAutorita( aModel.getSedeTipoAutorita() );
	      setCodTipoAutoritaDif( aModel.getCodTipoAutoritaDif() );
	      setSedeTipoAutoritaDif( aModel.getSedeAutoritaDif() );
	      setCodMotivoDesignazione( aModel.getCodMotivoDesignazione() );
	      setIstDetIdIstitutoDetenzione( aModel.getIstDetIdIstitutoDetenzione() );
	      setIndirizzoTipoAutorita( aModel.getIndirizzoTipoAutorita() );
		  setNote( aModel.getNote() );
      }	  

	  // Deassegnazione e Sostituzione Difensori Parte Udienza.
	public void setDAOFromModelForUpdateIdAvvocatoIdParte(PartiUdienzaDifensoreModel aModel)
			throws DAOException {

	    setDataFineValidita(aModel.getDataFineValidita());
	    setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
	    setDataAggiornamento(aModel.getDataAggiornamento());

	    selCondizioneUpdateAvvIdAvvIdParte(aModel.getAvvIdAvvocato(), aModel.getSoggIdSoggetto());
	  }

	public void selCondizioneUpdateAvvIdAvvIdParte(BigDecimal IdAvv, BigDecimal IdParte) {

	    setCondition(" AVV_ID_AVVOCATO = " + IdAvv +" AND SOGG_ID_SOGGETTO = " + IdParte );
	  }	  

	public void selCondizioneDeleteByIdParteUdienza(BigDecimal IdParte) {

	    setCondition(" SOGG_ID_SOGGETTO = " + IdParte );
	  }	  

}