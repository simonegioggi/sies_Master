package siap.sius.avvocatura.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: AvvisiAvvocatoDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella Avvisi_Avvocato
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class AvvisiAvvocatoDAO extends TableDAO {

	public AvvisiAvvocatoDAO(Connection con) {
		super(con);
		setTable("AVVISI_AVVOCATO");
		setSequenceField("ID_AVVISO", "SEQ_AVVISI_AVVOCATO");
		setField("ID_AVVISO", BIG_DECIMAL);
		setField("ID_AVVOCATO", BIG_DECIMAL);
		setField("COGNOME_SOGGETTO", STRING);
		setField("NOME_SOGGETTO", STRING);
		// setField("ID_PROVVEDIMENTO", BIG_DECIMAL);
		setField("ID_EVENTO", BIG_DECIMAL);
		setField("DESC_PROVVEDIMENTO", STRING);
		setField("UFFICIO_EMITTENTE", STRING);
		setField("TESTO_AVVISO", STRING);
		setField("FLAG_VISUALIZZAZIONE", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdAvviso() throws DAOException {
		return getBigDecimal("ID_AVVISO");
	}
	public BigDecimal getIdAvvocato() throws DAOException {
		return getBigDecimal("ID_AVVOCATO");
	}
	public String getCognomeSoggetto() throws DAOException {
		return getString("COGNOME_SOGGETTO");
	}
	public String getNomeSoggetto() throws DAOException {
		return getString("NOME_SOGGETTO");
	}
	// public BigDecimal getIdProvvedimento() throws DAOException { return getBigDecimal("ID_PROVVEDIMENTO");
	// }
	public BigDecimal getIdEvento() throws DAOException {
		return getBigDecimal("ID_EVENTO");
	}
	public String getDescProvvedimento() throws DAOException {
		return getString("DESC_PROVVEDIMENTO");
	}
	public String getUfficioEmittente() throws DAOException {
		return getString("UFFICIO_EMITTENTE");
	}
	public String getTestoAvviso() throws DAOException {
		return getString("TESTO_AVVISO");
	}
	public String getFlagVisualizzazione() throws DAOException {
		return getString("FLAG_VISUALIZZAZIONE");
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

	//
	// METODI SET()
	//
	public void setIdAvviso(BigDecimal aValore) {
		setBigDecimal("ID_AVVISO", aValore);
	}
	public void setIdAvvocato(BigDecimal aValore) {
		setBigDecimal("ID_AVVOCATO", aValore);
	}
	public void setCognomeSoggetto(String aValore) {
		setString("COGNOME_SOGGETTO", aValore);
	}
	public void setNomeSoggetto(String aValore) {
		setString("NOME_SOGGETTO", aValore);
	}
	// public void setIdProvvedimento(BigDecimal aValore) {setBigDecimal("ID_PROVVEDIMENTO", aValore);}
	public void setIdEvento(BigDecimal aValore) {
		setBigDecimal("ID_EVENTO", aValore);
	}
	public void setDescProvvedimento(String aValore) {
		setString("DESC_PROVVEDIMENTO", aValore);
	}
	public void setUfficioEmittente(String aValore) {
		setString("UFFICIO_EMITTENTE", aValore);
	}
	public void setTestoAvviso(String aValore) {
		setString("TESTO_AVVISO", aValore);
	}
	public void setFlagVisualizzazione(String aValore) {
		setString("FLAG_VISUALIZZAZIONE", aValore);
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

	public GenericModel getModel() throws DAOException {
		return new AvvisiAvvocatoModel(getIdAvviso(), getIdAvvocato(),
				getCognomeSoggetto(),
				getNomeSoggetto(),
				// getIdProvvedimento(),
				getIdEvento(), getDescProvvedimento(), getUfficioEmittente(), getTestoAvviso(),
				getFlagVisualizzazione(), getCodOperatoreInserimento(), getDataInserimento(),
				getCodUfficioInserimento());
	}

	public void setDAOFromModel(AvvisiAvvocatoModel aModel) throws DAOException {
		setIdAvvocato(aModel.getIdAvvocato());
		setCognomeSoggetto(aModel.getCognomeSoggetto());
		setNomeSoggetto(aModel.getNomeSoggetto());
		// setIdProvvedimento( aModel.getIdProvvedimento() );
		setIdEvento(aModel.getIdEvento());
		setDescProvvedimento(aModel.getDescProvvedimento());
		setUfficioEmittente(aModel.getUfficioEmittente());
		setTestoAvviso(aModel.getTestoAvviso());
		setFlagVisualizzazione(aModel.getFlagVisualizzazione());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
	}

}