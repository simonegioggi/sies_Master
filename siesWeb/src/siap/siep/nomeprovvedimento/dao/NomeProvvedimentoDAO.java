package siap.siep.nomeprovvedimento.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import siap.siep.nomeprovvedimento.model.NomeProvvedimentoModel;

/**
 * NomeProvvedimentoDAO - Description: Classe DAO che rappresenta la tabella NomeProvvedimento
 *
 * @version 1.0
 */
public class NomeProvvedimentoDAO extends TableDAO {

	public NomeProvvedimentoDAO(Connection con) {

		super(con);
		setTable("NOME_PROVVEDIMENTO");

		// Settare la Sequence e i campi chiave
		setField("COD_NOME_PROVVEDIMENTO", STRING);
		setField("EVE_ID_EVENTO", BIG_DECIMAL);
	}

	//
	// METODI GET()
	//

	public String getCodNomeProvvedimento() throws DAOException {
		return getString("COD_NOME_PROVVEDIMENTO");
	}

	public BigDecimal getEveIdEvento() throws DAOException {
		return getBigDecimal("EVE_ID_EVENTO");
	}

	//
	// METODI SET()
	//

	public void setCodNomeProvvedimento(String aValore) {
		setString("COD_NOME_PROVVEDIMENTO", aValore);
	}

	public void setEveIdEvento(BigDecimal aValore) {
		setBigDecimal("EVE_ID_EVENTO", aValore);
	}

	@Override
	public GenericModel getModel() throws DAOException {
		return new NomeProvvedimentoModel(getCodNomeProvvedimento(), "", getEveIdEvento());
	}

	public void setDAOFromModel(NomeProvvedimentoModel aModel) throws DAOException {
		setCodNomeProvvedimento(aModel.getCodNomeProvvedimento());
		setEveIdEvento(aModel.getEveIdEvento());
	}

	public void setDAOFromModelForUpdate(NomeProvvedimentoModel aModel) throws DAOException {
		setCodNomeProvvedimento(aModel.getCodNomeProvvedimento());
		setEveIdEvento(aModel.getEveIdEvento());
		setCondizioneUpdate(aModel.getCodNomeProvvedimento());
	}

	public void setCondizione(NomeProvvedimentoModel aModel) {
		String lCondizioni = new String();

		boolean lInserito = false;
		if (lInserito) {
			setCondition(lCondizioni);
		}
	}

	public void setCondizioneUpdate(String key) {
		setCondition(" COD_NOME_PROVVEDIMENTO = " + key);
	}

	// [FT] - 13/07/2026 - Ticket #20260713011 - Aggiunta condizione per la cancellazione del
	// NOME_PROVVEDIMENTO per EVE_ID_EVENTO, necessaria a rendere idempotente la insert in fase di
	// (ri)validazione del provvedimento (vedi OrdineScarcerazioneController.ExUpdateValidaOSLibAnt).
	public void setCondizioneByEveIdEvento(BigDecimal aEveIdEvento) {
		setCondition(" EVE_ID_EVENTO = " + aEveIdEvento);
	}

}