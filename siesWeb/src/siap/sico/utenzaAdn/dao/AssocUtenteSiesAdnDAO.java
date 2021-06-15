package siap.sico.utenzaAdn.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.sico.utenzaAdn.model.AssocUtenteSiesAdnModel;

/**
 * MEV INTEGRAZIONE SIES ADN
 *
 * @author sgioggi
 *
 */
public class AssocUtenteSiesAdnDAO extends SIAPTableDAO {

	public AssocUtenteSiesAdnDAO(Connection con) {

		super(con);

		setTable("ASSOC_UTENTE_SIES_ADN");

		// Settare la Sequence e i campi chiave
		setSequenceField("ID", "ASSOC_UTENTE_SIES_ADN_SEQ");
		setFieldKey("ID", BIG_DECIMAL);
		setField("UTE_COD_UTENTE", STRING);
		setField("ID_UTENTE_ADN", BIG_DECIMAL);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
	}

	//
	// METODI GET()
	//

	public BigDecimal getId() throws DAOException {
		return getBigDecimal("ID");
	}

	public String getUteCodUtente() throws DAOException {
		return getString("UTE_COD_UTENTE");
	}

	public BigDecimal getIdUtenteAdn() throws DAOException {
		return getBigDecimal("ID_UTENTE_ADN");
	}

	public String getCodOperatoreInserimento() throws DAOException {
		return getString("COD_OPERATORE_INSERIMENTO");
	}

	public Date getDataInserimento() throws DAOException {
		return getDate("DATA_INSERIMENTO");
	}

	public String getCodOperatoreAggiornamento() throws DAOException {
		return getString("COD_OPERATORE_AGGIORNAMENTO");
	}

	public Date getDataAggiornamento() throws DAOException {
		return getDate("DATA_AGGIORNAMENTO");
	}

	//
	// METODI SET()
	//

	public void setId(BigDecimal aValore) {
		setBigDecimal("ID", aValore);
	}

	public void setUteCodUtente(String aValore) {
		setString("UTE_COD_UTENTE", aValore);
	}

	public void setIdUtenteAdn(BigDecimal aValore) {
		setBigDecimal("ID_UTENTE_ADN", aValore);
	}

	public void setCodOperatoreInserimento(String aValore) {
		setString("COD_OPERATORE_INSERIMENTO", aValore);
	}

	public void setDataInserimento(Date aValore) {
		setDate("DATA_INSERIMENTO", aValore);
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		setString("COD_OPERATORE_AGGIORNAMENTO", aValore);
	}

	public void setDataAggiornamento(Date aValore) {
		setDate("DATA_AGGIORNAMENTO", aValore);
	}

	public GenericModel getModel() throws DAOException {

		return new AssocUtenteSiesAdnModel(getId(), getUteCodUtente(), getIdUtenteAdn(),
				getCodOperatoreInserimento(), getDataInserimento(), getCodOperatoreAggiornamento(),
				getDataAggiornamento());
	}

	public void setDAOFromModel(AssocUtenteSiesAdnModel aModel) throws DAOException {

		setId(aModel.getId());
		setUteCodUtente(aModel.getUteCodUtente());
		setIdUtenteAdn(aModel.getIdUtenteAdn());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
	}

	public void setCondizioneByUteCodUtente(String uteCodUtente) {
		setCondition(" UTE_COD_UTENTE = '" + uteCodUtente + "'");
	}

}