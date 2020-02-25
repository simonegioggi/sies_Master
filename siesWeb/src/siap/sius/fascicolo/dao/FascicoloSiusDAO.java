package siap.sius.fascicolo.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sius.fascicolo.model.FascicoloSiusCertBlobModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: FascicoloSiusDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella FascicoloSius
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class FascicoloSiusDAO extends SIAPTableDAO {

	public FascicoloSiusDAO(Connection con) {

		super(con);

		setTable("FASCICOLO_SIUS");
		setSequenceField("ID_FASCICOLO_SIUS", "FAS_SIU_SEQ");
		setFieldKey("ID_FASCICOLO_SIUS", BIG_DECIMAL);

		setField("ID_FASCICOLO_SIUS", BIG_DECIMAL);
		setField("CHIAVE_ANNO", BIG_DECIMAL);
		setField("CHIAVE_UFFICIO", STRING);
		setField("CHIAVE_PROGR", BIG_DECIMAL);
		setField("COD_STATO_FASCICOLO", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("SOG_ID_SOGGETTO", BIG_DECIMAL);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
		setField("DATA_ISCRIZIONE", DATE);
		setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
		setField("DATA_DEFINIZIONE", DATE);
		setField("ID_FASCICOLO_SIUS_ORIGINE", BIG_DECIMAL); // 15/01/2004
		setField("NUMERO_FASCICOLI_UNIFICATI", BIG_DECIMAL); // 29/04/2004
		// Modifica MEV 12 (Richiesta Certificato Penale)
		setField("CERTIFICATO_PENALE", TBLOB);
		// MEV10-s3: aggiunto campo per gestire maggiore/minore età
		setField("VISIBILITA_EX_MINORENNE", STRING);
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdFascicoloSius() throws DAOException {
		return getBigDecimal("ID_FASCICOLO_SIUS");
	}

	public BigDecimal getChiaveAnno() throws DAOException {
		return getBigDecimal("CHIAVE_ANNO");
	}

	public String getChiaveUfficio() throws DAOException {
		return getString("CHIAVE_UFFICIO");
	}

	public BigDecimal getChiaveProgr() throws DAOException {
		return getBigDecimal("CHIAVE_PROGR");
	}

	public String getCodStatoFascicolo() throws DAOException {
		return getString("COD_STATO_FASCICOLO");
	}

	public String getCodOperatoreInserimento() throws DAOException {
		return getString("COD_OPERATORE_INSERIMENTO");
	}

	public String getCodUfficioInserimento() throws DAOException {
		return getString("COD_UFFICIO_INSERIMENTO");
	}

	public Date getDataInserimento() throws DAOException {
		return getDate("DATA_INSERIMENTO");
	}

	public Date getDataIscrizione() throws DAOException {
		return getDate("DATA_ISCRIZIONE");
	}

	public String getCodOperatoreAggiornamento() throws DAOException {
		return getString("COD_OPERATORE_AGGIORNAMENTO");
	}

	public String getCodUfficioAggiornamento() throws DAOException {
		return getString("COD_UFFICIO_AGGIORNAMENTO");
	}

	public Date getDataAggiornamento() throws DAOException {
		return getDate("DATA_AGGIORNAMENTO");
	}

	public BigDecimal getSogIdSoggetto() throws DAOException {
		return getBigDecimal("SOG_ID_SOGGETTO");
	}

	public BigDecimal getFasSieIdFascicoloSiep() throws DAOException {
		return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP");
	}

	public BigDecimal getFasSiuIdFascicoloSius() throws DAOException {
		return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS");
	}

	public BigDecimal getIdFascicoloSiusOrigine() throws DAOException {
		return getBigDecimal("ID_FASCICOLO_SIUS_ORIGINE");
	}

	public Date getDataDefinizione() throws DAOException {
		return getDate("DATA_DEFINIZIONE");
	}

	public BigDecimal getNumeroFascicoliUnificati() throws DAOException {
		return getBigDecimal("NUMERO_FASCICOLI_UNIFICATI");
	} // 29/04/2004

	public ByteArrayOutputStream getCertPenaleBlob() throws DAOException {
		return getBlob("CERTIFICATO_PENALE");
	}

	public String getVisibilitaMinorenne() throws DAOException {
		return getString("VISIBILITA_EX_MINORENNE");
	}

	//
	// METODI SET()
	//

	public void setIdFascicoloSius(BigDecimal aValore) {
		setBigDecimal("ID_FASCICOLO_SIUS", aValore);
	}

	public void setChiaveAnno(BigDecimal aValore) {
		setBigDecimal("CHIAVE_ANNO", aValore);
	}

	public void setChiaveUfficio(String aValore) {
		setString("CHIAVE_UFFICIO", aValore);
	}

	public void setChiaveProgr(BigDecimal aValore) {
		setBigDecimal("CHIAVE_PROGR", aValore);
	}

	public void setCodStatoFascicolo(String aValore) {
		setString("COD_STATO_FASCICOLO", aValore);
	}

	public void setCodOperatoreInserimento(String aValore) {
		setString("COD_OPERATORE_INSERIMENTO", aValore);
	}

	public void setCodUfficioInserimento(String aValore) {
		setString("COD_UFFICIO_INSERIMENTO", aValore);
	}

	public void setDataInserimento(Date aValore) {
		setDate("DATA_INSERIMENTO", aValore);
	}

	public void setDataIscrizione(Date aValore) {
		setDate("DATA_ISCRIZIONE", aValore);
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		setString("COD_OPERATORE_AGGIORNAMENTO", aValore);
	}

	public void setCodUfficioAggiornamento(String aValore) {
		setString("COD_UFFICIO_AGGIORNAMENTO", aValore);
	}

	public void setDataAggiornamento(Date aValore) {
		setDate("DATA_AGGIORNAMENTO", aValore);
	}

	public void setSogIdSoggetto(BigDecimal aValore) {
		setBigDecimal("SOG_ID_SOGGETTO", aValore);
	}

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore);
	}

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore);
	}

	public void setIdFascicoloSiusOrigine(BigDecimal aValore) {
		setBigDecimal("ID_FASCICOLO_SIUS_ORIGINE", aValore);
	}

	public void setDataDefinizione(Date aValore) {
		setDate("DATA_DEFINIZIONE", aValore);
	}

	public void setNumeroFascicoliUnificati(BigDecimal aValore) {
		setBigDecimal("NUMERO_FASCICOLI_UNIFICATI", aValore);
	} // 29/04/2004

	public void setCertPenaleBlob(ByteArrayInputStream aValore) {
		setBlob("CERTIFICATO_PENALE", aValore);
	}

	public void setVisibilitaMinorenne(String aValore) {
		setString("VISIBILITA_EX_MINORENNE", aValore);
	}

	public GenericModel getModel() throws DAOException {

		return new FascicoloSiusModel(getIdFascicoloSius(), getChiaveAnno(), null, null, null,
				getChiaveUfficio(), "", "", "", getChiaveProgr(), null, null, null, getCodStatoFascicolo(),
				"", getCodOperatoreInserimento(), getCodUfficioInserimento(), getDataInserimento(),
				getDataIscrizione(), getCodOperatoreAggiornamento(), getCodUfficioAggiornamento(),
				getDataAggiornamento(), getSogIdSoggetto(), getFasSieIdFascicoloSiep(), null, null, "",
				getFasSiuIdFascicoloSius(), getIdFascicoloSiusOrigine(), getDataDefinizione(), null, null,
				getNumeroFascicoliUnificati(), // 29/04/2004
				getVisibilitaMinorenne(), "" // MEV_65: codCancelleria
		);
	}

	public void setDAOFromModelForUpdate(FascicoloSiusModel aModel) throws DAOException {
		setIdFascicoloSius(aModel.getIdFascicoloSius());
		setChiaveAnno(aModel.getChiaveAnno());
		setChiaveUfficio(aModel.getChiaveUfficio());
		setChiaveProgr(aModel.getChiaveProgr());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());

		setCondizioneUpdate(aModel.getIdFascicoloSius());
	}

	public void setDAOFromModelForAssegnaTitoloEsecutivo(FascicoloSiusModel aModel) throws DAOException {
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setSogIdSoggetto(aModel.getSogIdSoggetto());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());

		setCondizioneUpdate(aModel.getIdFascicoloSius());
	}

	public void setDAOFromModelForAggiornaIdFascicoloOrigine(FascicoloSiusModel aModel) throws DAOException {
		setIdFascicoloSiusOrigine(aModel.getIdFascicoloSiusOrigine());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());

		setCondizioneUpdate(aModel.getIdFascicoloSius());
	}

	public void setDAOFromModelForUnificazioneSoggetti(FascicoloSiusModel aFascicoloUnificante,
			BigDecimal aIdFascicoloSiusUnificato) throws DAOException {
		setSogIdSoggetto(aFascicoloUnificante.getSogIdSoggetto());
		setCodOperatoreAggiornamento(aFascicoloUnificante.getCodOperatoreAggiornamento());
		setCodUfficioAggiornamento(aFascicoloUnificante.getCodUfficioAggiornamento());
		setDataAggiornamento(aFascicoloUnificante.getDataAggiornamento());

		setCondizioneUpdate(aIdFascicoloSiusUnificato);
	}

	public void setDAOFromModel(FascicoloSiusModel aModel) throws DAOException {
		if (!(aModel.getIdFascicoloSius() == null))
			setIdFascicoloSius(aModel.getIdFascicoloSius());
		if (!(aModel.getChiaveAnno() == null))
			setChiaveAnno(aModel.getChiaveAnno());
		if (!(aModel.getChiaveUfficio() == null))
			setChiaveUfficio(aModel.getChiaveUfficio());
		if (!(aModel.getChiaveProgr() == null))
			setChiaveProgr(aModel.getChiaveProgr());
		if (!(aModel.getCodStatoFascicolo() == null))
			setCodStatoFascicolo(aModel.getCodStatoFascicolo());
		if (!(aModel.getCodOperatoreInserimento() == null))
			setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		if (!(aModel.getCodUfficioInserimento() == null))
			setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		if (!(aModel.getDataInserimento() == null))
			setDataInserimento(aModel.getDataInserimento());
		if (!(aModel.getDataIscrizione() == null))
			setDataIscrizione(aModel.getDataIscrizione());
		if (!(aModel.getCodOperatoreAggiornamento() == null))
			setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		if (!(aModel.getCodUfficioAggiornamento() == null))
			setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		if (!(aModel.getDataAggiornamento() == null))
			setDataAggiornamento(aModel.getDataAggiornamento());
		if (!(aModel.getSogIdSoggetto() == null))
			setSogIdSoggetto(aModel.getSogIdSoggetto());
		if (!(aModel.getFasSieIdFascicoloSiep() == null))
			setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		if (!(aModel.getFasSiuIdFascicoloSius() == null))
			setFasSiuIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
		if (!(aModel.getIdFascicoloSiusOrigine() == null))
			setIdFascicoloSiusOrigine(aModel.getIdFascicoloSiusOrigine());
		if (!(aModel.getDataDefinizione() == null))
			setDataDefinizione(aModel.getDataDefinizione());
		if (!(aModel.getNumeroFascicoliUnificati() == null))
			setNumeroFascicoliUnificati(aModel.getNumeroFascicoliUnificati()); // 29/04/2004
		if (!(aModel.getVisibilitaMinorenne() == null))
			setVisibilitaMinorenne(aModel.getVisibilitaMinorenne());
	}

	public void selCondizione(FascicoloSiusModel aModel) {
		String lCondizioni = new String();

		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	public void setCondizioneUpdate(BigDecimal key) {
		setCondition(" ID_FASCICOLO_SIUS = " + key);
	}

	public void setCondizioneUpdateStatoFascicolo(BigDecimal key, String aCodStato) {
		setCondition(" ID_FASCICOLO_SIUS = " + key + " AND COD_STATO_FASCICOLO = '" + aCodStato + "'");
	}

	public void setDAOFromModelForUpdateBlob(FascicoloSiusCertBlobModel aModel) throws DAOException {
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setCertPenaleBlob(aModel.ritornaCertPenaleBlobIn());
	}

}