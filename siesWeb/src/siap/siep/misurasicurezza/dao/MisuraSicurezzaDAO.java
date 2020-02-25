package siap.siep.misurasicurezza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: MisuraSicurezzaDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella MisuraSicurezza
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

public class MisuraSicurezzaDAO extends SIAPTableDAO {
	public MisuraSicurezzaDAO(Connection con) {
		super(con);
		setTable("MISURA_SICUREZZA");

		setSequenceField("ID_MISURA_SICUREZZA", "MIS_SIC_SEQ");

		setFieldKey("ID_MISURA_SICUREZZA", BIG_DECIMAL);

		setField("ID_MISURA_SICUREZZA", BIG_DECIMAL);
		setField("COD_NATURA", STRING);
		setField("COD_TIPO", STRING);
		setField("NUM_ANNI", BIG_DECIMAL);
		setField("NUM_MESI", BIG_DECIMAL);
		setField("NUM_GIORNI", BIG_DECIMAL);
		setField("ANNO_REG_38", BIG_DECIMAL);
		setField("NUM_REG_38", BIG_DECIMAL);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
		setField("FAS_SIU_ID_FASCICOLO_SIUS", BIG_DECIMAL);
		setField("EVE_ID_EVENTO", BIG_DECIMAL);
		setField("FAS_SIE_ID_FASCICOLO_SIEP_RIF", BIG_DECIMAL);
		setField("SEN_ID_SENTENZA", BIG_DECIMAL);
		setField("DATA_DECORRENZA", DATE);
		setField("FL_FORMA_MISURA", BIG_DECIMAL);
		setField("DESCRIZIONE_COMUNITA", STRING);
		// 12-12-2014
		setField("FLAG_ANNULLA_MISURA", STRING);
		setField("DATA_FINE_VALIDITA", DATE);
		setField("MIS_ID_MISURA_SICUREZZA", BIG_DECIMAL);
		// 18-02-2015
		setField("IST_DET_ID_ISTITUTO_DETENZIONE", STRING);
		setField("LUOGO_ESECUZIONE_MISURA", STRING);
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdMisuraSicurezza() throws DAOException {
		return getBigDecimal("ID_MISURA_SICUREZZA");
	}

	public String getCodNatura() throws DAOException {
		return getString("COD_NATURA");
	}

	public String getCodTipo() throws DAOException {
		return getString("COD_TIPO");
	}

	public BigDecimal getNumAnni() throws DAOException {
		return getBigDecimal("NUM_ANNI");
	}

	public BigDecimal getNumMesi() throws DAOException {
		return getBigDecimal("NUM_MESI");
	}

	public BigDecimal getNumGiorni() throws DAOException {
		return getBigDecimal("NUM_GIORNI");
	}

	public BigDecimal getAnnoReg38() throws DAOException {
		return getBigDecimal("ANNO_REG_38");
	}

	public BigDecimal getNumReg38() throws DAOException {
		return getBigDecimal("NUM_REG_38");
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

	public BigDecimal getFasSieIdFascicoloSiep() throws DAOException {
		return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP");
	}

	public BigDecimal getFasSiuIdFascicoloSius() throws DAOException {
		return getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS");
	}

	public BigDecimal getEveIdEvento() throws DAOException {
		return getBigDecimal("EVE_ID_EVENTO");
	}

	public BigDecimal getFasSieIdFascicoloSiepRif() throws DAOException {
		return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP_RIF");
	}

	public BigDecimal getSenIdSentenza() throws DAOException {
		return getBigDecimal("SEN_ID_SENTENZA");
	}

	public Date getDataDecorrenza() throws DAOException {
		return getDate("DATA_DECORRENZA");
	}

	public BigDecimal getFlFormaMisura() throws DAOException {
		return getBigDecimal("FL_FORMA_MISURA");
	}

	public String getDescrizioneComunita() throws DAOException {
		return getString("DESCRIZIONE_COMUNITA");
	}

	public String getFlagAnnullaMisura() throws DAOException {
		return getString("FLAG_ANNULLA_MISURA");
	}

	public Date getDataFineValidita() throws DAOException {
		return getDate("DATA_FINE_VALIDITA");
	}

	public BigDecimal getMisIdMisuraSicurezza() throws DAOException {
		return getBigDecimal("MIS_ID_MISURA_SICUREZZA");
	}

	public String getIstDetIdIstitutoDetenzione() throws DAOException {
		return getString("IST_DET_ID_ISTITUTO_DETENZIONE");
	}

	public String getLuogoEsecuzioneMisura() throws DAOException {
		return getString("LUOGO_ESECUZIONE_MISURA");
	}

	//
	// METODI SET()
	//

	public void setIdMisuraSicurezza(BigDecimal aValore) {
		setBigDecimal("ID_MISURA_SICUREZZA", aValore);
	}

	public void setCodNatura(String aValore) {
		setString("COD_NATURA", aValore);
	}

	public void setCodTipo(String aValore) {
		setString("COD_TIPO", aValore);
	}

	public void setNumAnni(BigDecimal aValore) {
		setBigDecimal("NUM_ANNI", aValore);
	}

	public void setNumMesi(BigDecimal aValore) {
		setBigDecimal("NUM_MESI", aValore);
	}

	public void setNumGiorni(BigDecimal aValore) {
		setBigDecimal("NUM_GIORNI", aValore);
	}

	public void setAnnoReg38(BigDecimal aValore) {
		setBigDecimal("ANNO_REG_38", aValore);
	}

	public void setNumReg38(BigDecimal aValore) {
		setBigDecimal("NUM_REG_38", aValore);
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

	public void setFasSieIdFascicoloSiep(BigDecimal aValore) {
		setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore);
	}

	public void setFasSiuIdFascicoloSius(BigDecimal aValore) {
		setBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS", aValore);
	}

	public void setEveIdEvento(BigDecimal aValore) {
		setBigDecimal("EVE_ID_EVENTO", aValore);
	}

	public void setFasSieIdFascicoloSiepRif(BigDecimal aValore) {
		setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP_RIF", aValore);
	}

	public void setSenIdSentenza(BigDecimal aValore) {
		setBigDecimal("SEN_ID_SENTENZA", aValore);
	}

	public void setDataDecorrenza(Date aValore) {
		setDate("DATA_DECORRENZA", aValore);
	}

	public void setFlFormaMisura(BigDecimal aValore) {
		setBigDecimal("FL_FORMA_MISURA", aValore);
	}

	public void setDescrizioneComunita(String aValore) {
		setString("DESCRIZIONE_COMUNITA", aValore);
	}

	public void setFlagAnnullaMisura(String aValore) {
		setString("FLAG_ANNULLA_MISURA", aValore);
	}

	public void setDataFineValidita(Date aValore) {
		setDate("DATA_FINE_VALIDITA", aValore);
	}

	public void setMisIdMisuraSicurezza(BigDecimal aValore) {
		setBigDecimal("MIS_ID_MISURA_SICUREZZA", aValore);
	}

	public void setIstDetIdIstitutoDetenzione(String aValore) {
		setString("IST_DET_ID_ISTITUTO_DETENZIONE", aValore);
	}

	public void setLuogoEsecuzioneMisura(String aValore) {
		setString("LUOGO_ESECUZIONE_MISURA", aValore);
	}

	public GenericModel getModel() throws DAOException {
		return new MisuraSicurezzaModel(getIdMisuraSicurezza(), getCodNatura(), "", getCodTipo(), "", "",
				getNumAnni(), getNumMesi(), getNumGiorni(), getAnnoReg38(), getNumReg38(),
				getCodOperatoreInserimento(), getDataInserimento(), getCodUfficioInserimento(), "",
				getCodOperatoreAggiornamento(), getDataAggiornamento(), getCodUfficioAggiornamento(), "",
				getFasSieIdFascicoloSiep(), getFasSiuIdFascicoloSius(), getEveIdEvento(),
				getFasSieIdFascicoloSiepRif(), getSenIdSentenza(), getDataDecorrenza(), getFlFormaMisura(),
				getDescrizioneComunita(), getFlagAnnullaMisura(), getDataFineValidita(),
				getMisIdMisuraSicurezza(), getIstDetIdIstitutoDetenzione(), getLuogoEsecuzioneMisura());
	}

	public void setDAOFromModel(MisuraSicurezzaModel aModel) throws DAOException {
		setIdMisuraSicurezza(aModel.getIdMisuraSicurezza());
		setCodNatura(aModel.getCodNatura());
		setCodTipo(aModel.getCodTipo());
		setNumAnni(aModel.getNumAnni());
		setNumMesi(aModel.getNumMesi());
		setNumGiorni(aModel.getNumGiorni());
		setAnnoReg38(aModel.getAnnoReg38());
		setNumReg38(aModel.getNumReg38());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		// setCodOperatoreAggiornamento( aModel.getCodOperatoreAggiornamento() );
		// setDataAggiornamento( aModel.getDataAggiornamento() );
		// setCodUfficioAggiornamento( aModel.getCodUfficioAggiornamento() );
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setFasSiuIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
		setEveIdEvento(aModel.getEveIdEvento());
		setFasSieIdFascicoloSiepRif(aModel.getFasSieIdFascicoloSiepRif());
		setSenIdSentenza(aModel.getSenIdSentenza());
		setDataDecorrenza(aModel.getDataDecorrenza());
		setFlFormaMisura(aModel.getFlFormaMisura());
		setDescrizioneComunita(aModel.getDescrizioneComunita());
		setFlagAnnullaMisura(aModel.getFlagAnnullaMisura());
		setDataFineValidita(aModel.getDataFineValidita());
		setMisIdMisuraSicurezza(aModel.getMisIdMisuraSicurezza());
		setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
		setLuogoEsecuzioneMisura(aModel.getLuogoEsecuzioneMisura());
	}

	public void setDAOFromModelForUpdate(MisuraSicurezzaModel aModel) throws DAOException {
		// setIdMisuraSicurezza( aModel.getIdMisuraSicurezza() );
		setCodNatura(aModel.getCodNatura());
		setCodTipo(aModel.getCodTipo());
		setNumAnni(aModel.getNumAnni());
		setNumMesi(aModel.getNumMesi());
		setNumGiorni(aModel.getNumGiorni());
		// setAnnoReg38( aModel.getAnnoReg38() );
		// setNumReg38( aModel.getNumReg38() );
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setFasSieIdFascicoloSiep(aModel.getFasSieIdFascicoloSiep());
		setFasSiuIdFascicoloSius(aModel.getFasSiuIdFascicoloSius());
		setEveIdEvento(aModel.getEveIdEvento());
		setFasSieIdFascicoloSiepRif(aModel.getFasSieIdFascicoloSiepRif());
		setSenIdSentenza(aModel.getSenIdSentenza());
		setCondizioneUpdate(aModel.getIdMisuraSicurezza());
		setDataDecorrenza(aModel.getDataDecorrenza());
		setFlFormaMisura(aModel.getFlFormaMisura());
		setDescrizioneComunita(aModel.getDescrizioneComunita());
		setFlagAnnullaMisura(aModel.getFlagAnnullaMisura());
		setDataFineValidita(aModel.getDataFineValidita());
		setMisIdMisuraSicurezza(aModel.getMisIdMisuraSicurezza());
		setIstDetIdIstitutoDetenzione(aModel.getIstDetIdIstitutoDetenzione());
		setLuogoEsecuzioneMisura(aModel.getLuogoEsecuzioneMisura());
	}

	public void setCondizione(MisuraSicurezzaModel aModel) {
		String lCondizioni = new String();

		boolean lInserito = false;
		if (lInserito)
			setCondition(lCondizioni);
	}

	public void setCondizioneUpdate(BigDecimal key) {
		setCondition(" ID_MISURA_SICUREZZA = " + key);
	}

	public void setCondizioneByDepOrdPC(BigDecimal aKey) {
		setCondition(" EVE_ID_EVENTO = " + aKey);
	}

}