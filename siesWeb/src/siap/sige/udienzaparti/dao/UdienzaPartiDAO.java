package siap.sige.udienzaparti.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sige.udienzaparti.model.UdienzaPartiModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: UdienzaPartiDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella UDIENZA_PARTI
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class UdienzaPartiDAO extends SIAPTableDAO {

	public UdienzaPartiDAO(Connection con) {

		super(con);
		setTable("UDIENZA_PARTI");

		// Settare i campi chiave
		setFieldKey("ID_SOGGETTO", BIG_DECIMAL);
		setFieldKey("ID_UDIENZA_PROCEDIMENTO_SIGE", BIG_DECIMAL);

		setField("ID_SOGGETTO", BIG_DECIMAL);
		setField("ID_UDIENZA_PROCEDIMENTO_SIGE", BIG_DECIMAL);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdSoggetto() throws DAOException {
		return getBigDecimal("ID_SOGGETTO");
	}

	public BigDecimal getIdUdienzaProcedimentoSige() throws DAOException {
		return getBigDecimal("ID_UDIENZA_PROCEDIMENTO_SIGE");
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

	//
	// METODI SET()
	//
	public void setIdSoggetto(BigDecimal aValore) {
		setBigDecimal("ID_SOGGETTO", aValore);
	}

	public void setIdUdienzaProcedimentoSige(BigDecimal aValore) {
		setBigDecimal("ID_UDIENZA_PROCEDIMENTO_SIGE", aValore);
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

	public GenericModel getModel() throws DAOException {
		return new UdienzaPartiModel(getIdSoggetto(), getIdUdienzaProcedimentoSige(),
				getCodOperatoreInserimento(), getDataInserimento(), getCodUfficioInserimento(),
				getCodOperatoreAggiornamento(), getDataAggiornamento(), getCodUfficioAggiornamento());
	}

	public void setDAOFromModel(UdienzaPartiModel aModel) throws DAOException {
		setIdSoggetto(aModel.getIdSoggetto());
		setIdUdienzaProcedimentoSige(aModel.getIdUdienzaProcedimentoSige());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
	}

	public void selCondizioneDeleteByIdParteUdienza(BigDecimal IdParte) {
		setCondition(" ID_SOGGETTO = " + IdParte);
	}

	/**
	 * MERGE v10: condizione per la cancellazione
	 * 
	 * @param idProvvedimentoSige
	 */
	public void setCondizioneByIdUdienzaProcedimentoSige(BigDecimal idProvvedimentoSige) {
		setCondition(" ID_UDIENZA_PROCEDIMENTO_SIGE = " + idProvvedimentoSige);
	}

}