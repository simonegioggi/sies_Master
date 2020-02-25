package siap.sige.collegio.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sige.collegio.model.CollegioModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: CollegioDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella Collegio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class CollegioDAO extends SIAPTableDAO {

	public CollegioDAO(Connection con) {

		super(con);
		setTable("COLLEGIO");

		// Settare la Sequence e i campi chiave
		setSequenceField("ID_COLLEGIO", "COL_SEQ");
		setFieldKey("ID_COLLEGIO", BIG_DECIMAL);

		setField("ID_COLLEGIO", BIG_DECIMAL);
		setField("COD_COLLEGIO", STRING);
		setField("SEZ_ID_SEZIONE", BIG_DECIMAL);
		setField("DATA_INIZIO_VALIDITA", DATE);
		setField("DATA_FINE_VALIDITA", DATE);
		setField("COD_UFFICIO_APPARTENENZA", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		// intervento per 11.2.1
		setField("MAG_COD_MAGISTRATO", STRING);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdCollegio() throws DAOException {
		return getBigDecimal("ID_COLLEGIO");
	}

	public String getCodCollegio() throws DAOException {
		return getString("COD_COLLEGIO");
	}

	public BigDecimal getSezIdSezione() throws DAOException {
		return getBigDecimal("SEZ_ID_SEZIONE");
	}

	public String getCodUfficioAppartenenza() throws DAOException {
		return getString("COD_UFFICIO_APPARTENENZA");
	}

	public Date getDataInizioValidita() throws DAOException {
		return getDate("DATA_INIZIO_VALIDITA");
	}

	public Date getDataFineValidita() throws DAOException {
		return getDate("DATA_FINE_VALIDITA");
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

	public String getCodiceFiscale() throws DAOException {
		return getString("CODICE_FISCALE");
	}

	// intervento per 11.2.1
	public String getCodMagistrato() throws DAOException {
		return getString("MAG_COD_MAGISTRATO");
	}

	//
	// METODI SET()
	//
	public void setIdCollegio(BigDecimal aValore) {
		setBigDecimal("ID_COLLEGIO", aValore);
	}

	public void setCodCollegio(String aValore) {
		setString("COD_COLLEGIO", aValore);
	}

	public void setSezIdSezione(BigDecimal aValore) {
		setBigDecimal("SEZ_ID_SEZIONE", aValore);
	}

	public void setCodUfficioAppartenenza(String aValore) {
		setString("COD_UFFICIO_APPARTENENZA", aValore);
	}

	public void setDataInizioValidita(Date aValore) {
		setDate("DATA_INIZIO_VALIDITA", aValore);
	}

	public void setDataFineValidita(Date aValore) {
		setDate("DATA_FINE_VALIDITA", aValore);
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

	public void setCodiceFiscale(String aValore) {
		setString("CODICE_FISCALE", aValore);
	}

	// intervento per 11.2.1
	public void setCodMagistrato(String aValore) {
		setString("MAG_COD_MAGISTRATO", aValore);
	}

	public GenericModel getModel() throws DAOException {

		return new CollegioModel(getIdCollegio(), getCodCollegio(), getSezIdSezione(),
				getCodUfficioAppartenenza(), "", getDataInizioValidita(), getDataFineValidita(),
				getCodOperatoreInserimento(), getDataInserimento(), getCodUfficioInserimento(), "",
				getCodOperatoreAggiornamento(), getDataAggiornamento(), getCodUfficioAggiornamento(), "",
				getCodMagistrato(), "");
	}

	public void setDAOFromModel(CollegioModel aModel) throws DAOException {

		setIdCollegio(aModel.getIdCollegio());
		setCodCollegio(aModel.getCodCollegio());
		setSezIdSezione(aModel.getSezIdSezione());
		setCodUfficioAppartenenza(aModel.getCodUfficioAppartenenza());
		setDataInizioValidita(aModel.getDataInizioValidita());
		setDataFineValidita(aModel.getDataFineValidita());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		// aggiunto per intervento 11.2.1
		setCodMagistrato(aModel.getMagCodMagistrato());
	}

	public void setDAOFromModelForUpdate(CollegioModel aModel) throws DAOException {

		// setIdCuratore( aModel.getIdCuratore() );
		setCodCollegio(aModel.getCodCollegio());
		setSezIdSezione(aModel.getSezIdSezione());
		// setCodUfficioAppartenenza( aModel.getCodUfficioAppartenenza() );
		setDataInizioValidita(aModel.getDataInizioValidita());
		setDataFineValidita(aModel.getDataFineValidita());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		if(aModel.getMagCodMagistrato()!=null && !"".equals(aModel.getMagCodMagistrato()))
			setCodMagistrato(aModel.getMagCodMagistrato());
		setCondizioneUpdate(aModel.getIdCollegio());
	}

	public void setCondizione(CollegioModel aModel) {

		String lCondizioni = new String();
		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	public void setCondizioneUpdate(BigDecimal key) {
		setCondition(" ID_COLLEGIO = " + key);
	}

}