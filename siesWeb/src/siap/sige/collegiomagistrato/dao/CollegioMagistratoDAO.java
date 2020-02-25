package siap.sige.collegiomagistrato.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: CollegioMagistratoDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella CollegioMagistrato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 */
public class CollegioMagistratoDAO extends SIAPTableDAO {

	public CollegioMagistratoDAO(Connection con) {
		super(con);
		setTable("COLLEGIO_MAGISTRATO");

		setField("COL_ID_COLLEGIO", BIG_DECIMAL);
		setField("MAG_COD_MAGISTRATO", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_UFFICIO_APPARTENENZA", STRING);
		// 20171013: [SG] aggiunta variabile di collegamento all'udienza sige
		setField("UDI_ID_UDIENZA_SIGE", BIG_DECIMAL);
	}

	//
	// METODI GET()
	//
	public BigDecimal getColIdCollegio() throws DAOException {
		return getBigDecimal("COL_ID_COLLEGIO");
	}

	public String getMagCodMagistrato() throws DAOException {
		return getString("MAG_COD_MAGISTRATO");
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

	public String getCodUfficioAppartenenza() throws DAOException {
		return getString("COD_UFFICIO_APPARTENENZA");
	}

	public BigDecimal getUdiIdUdienzaSige() throws DAOException {
		return getBigDecimal("UDI_ID_UDIENZA_SIGE");
	}

	//
	// METODI SET()
	//
	public void setColIdCollegio(BigDecimal aValore) {
		setBigDecimal("COL_ID_COLLEGIO", aValore);
	}

	public void setMagCodMagistrato(String aValore) {
		setString("MAG_COD_MAGISTRATO", aValore);
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

	public void setCodUfficioAppartenenza(String aValore) {
		setString("COD_UFFICIO_APPARTENENZA", aValore);
	}

	public void setUdiIdUdienzaSige(BigDecimal aValore) {
		setBigDecimal("UDI_ID_UDIENZA_SIGE", aValore);
	}

	public GenericModel getModel() throws DAOException {
		CollegioMagistratoModel lModel = new CollegioMagistratoModel();
		lModel.setColIdCollegio(getColIdCollegio());
		lModel.setMagCodMagistrato(getMagCodMagistrato());
		lModel.setCodOperatoreInserimento(getCodOperatoreInserimento());
		lModel.setDataInserimento(getDataInserimento());
		lModel.setCodUfficioInserimento(getCodUfficioInserimento());
		lModel.setCodUfficioAppartenenza(getCodUfficioAppartenenza());
		lModel.setUdiIdUdienzaSige(getUdiIdUdienzaSige());
		return lModel;
	}

	public void setDAOFromModel(CollegioMagistratoModel aModel) throws DAOException {
		setColIdCollegio(aModel.getColIdCollegio());
		setMagCodMagistrato(aModel.getMagCodMagistrato());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodUfficioAppartenenza(aModel.getCodUfficioAppartenenza());
		setUdiIdUdienzaSige(aModel.getUdiIdUdienzaSige());
	}

	public void setCondizione(CollegioMagistratoModel aModel) {
		String lCondizioni = new String();

		boolean lInserito = false;

		if (lInserito)
			setCondition(lCondizioni);
	}

	public void setCondizioneByCodMag(String key) {
		setCondition(" MAG_COD_MAGISTRATO = '" + key + "'");
	}

	public void setCondizioneByIdCol(BigDecimal aId) {
		setCondition(" COL_ID_COLLEGIO = " + aId.toString());
	}

}