package siap.bdmc.sbviewnotifiche.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.StoreProcedureDAO;

/**
 * <p>
 * Title: VariazioneStatoNotificaStoreProcedureDAO
 * </p>
 * <p>
 * Description: Store Procedure VARIAZIONE_STATO.variazione_stato_notifica
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 */
public class VariazioneStatoNotificaStoreProcedureDAO extends StoreProcedureDAO {

	public VariazioneStatoNotificaStoreProcedureDAO(Connection lConn) {
		super(lConn);
		setStoreProcedure("isies.var_stato_notifica@SIES_BDMC_LINK");

		// Settare i campi chiave di Input e di output della Store Procedure
		this.setArgInput("PROG_NOTI", BIG_DECIMAL);
		this.setArgInputPosition("PROG_NOTI", 1);
		this.setArgInput("FLAG_STAT_NOTI", STRING);
		this.setArgInputPosition("FLAG_STAT_NOTI", 2);
		this.setArgInput("DATA_CHIU_NOTI", DATE);
		this.setArgInputPosition("DATA_CHIU_NOTI", 3);

		this.setArgOutput("ESITO", BIG_DECIMAL);
		// this.setArgOutputPosition("ESITO", 4);
		this.setArgOutput("ESITO_MSG", STRING);
	}

	public void setProgNoti(BigDecimal aValore) {
		setBigDecimal("PROG_NOTI", aValore);
	}

	public void setFlagStato(String aValore) {
		setString("FLAG_STAT_NOTI", aValore);
	}

	public void setDataChiuNoti(java.sql.Date aValore) {
		setDate("DATA_CHIU_NOTI", aValore);
	}

	public BigDecimal getEsito() throws DAOException {
		return this.getOutBigDecimal("ESITO");
	}

	public String getEsitoMsg() throws DAOException {
		return getOutString("ESITO_MSG");
	}

	/**
	 * Imposta il campo di riferimento con il valore <code>Date</code>.
	 * <p>
	 * 
	 * @param aFieldName
	 *            nome del campo di riferimento.
	 * @param aValue
	 *            valore da impostare.
	 */
	@SuppressWarnings("unchecked")
	protected void setDate(String aFieldName, java.util.Date aValue) {
		if (aValue != null)
			mFieldsValues.put(aFieldName, aValue);
		else
			mFieldsNullValues.put(aFieldName, new Boolean(true));
	}

}