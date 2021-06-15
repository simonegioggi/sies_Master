package siap.sico.utenzaAdn.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.sico.utenzaAdn.model.UtenzaAdnModel;

/**
 * MEV INTEGRAZIONE SIES ADN
 *
 * @author sgioggi
 *
 */
public class UtenzaAdnDAO extends SIAPTableDAO {

	public UtenzaAdnDAO(Connection con) {

		super(con);

		setTable("UTENZA_ADN");

		// Settare la Sequence e i campi chiave
		setSequenceField("ID", "UTENZA_ADN_SEQ");
		setFieldKey("ID", BIG_DECIMAL);
		setField("SAMACCOUNTNAME", STRING);
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

	public String getSamAccountName() throws DAOException {
		return getString("SAMACCOUNTNAME");
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

	public void setSamAccountName(String aValore) {
		setString("SAMACCOUNTNAME", aValore);
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

		return new UtenzaAdnModel(getId(), getSamAccountName(), getCodOperatoreInserimento(),
				getDataInserimento(), getCodOperatoreAggiornamento(), getDataAggiornamento());
	}

	public void setDAOFromModel(UtenzaAdnModel aModel) throws DAOException {

		setId(aModel.getId());
		setSamAccountName(aModel.getSamAccountName());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
	}

	public void setDAOFromModelForUpdate(UtenzaAdnModel aModel) throws DAOException {

		setId(aModel.getId());
		setSamAccountName(aModel.getSamAccountName());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
	}

	public void setCondizioneUpdate(BigDecimal key) {

		setCondition(" ID = " + key);
	}

}