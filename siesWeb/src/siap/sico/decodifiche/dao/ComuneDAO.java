package siap.sico.decodifiche.dao;

import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sico.decodifiche.model.ComuneModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;

public class ComuneDAO extends SIAPTableDAO {

	public ComuneDAO(Connection con) {
		super(con);

		setTable("COMUNE");

		setFieldKey("COD_COMUNE", STRING);

		setField("COD_COMUNE", STRING);
		setField("COD_PROVINCIA", STRING);
		setField("DESCRIZIONE", STRING);
		setField("CAP", STRING);
		setField("DATA_CARICAMENTO_REGE", DATE);
		setField("COD_SEDE_GIUDIZIARIA", STRING);
		setField("FLAG_VALIDITA", STRING);				// 20210517 MEV_21
		setField("COD_CATASTALE_COMUNE", STRING);		// 20210517 MEV_21
		setField("DATA_AGGIORNAMENTO_COMUNE", DATE);	// 20210517 MEV_21
		setField("DATA_FINE_VALIDITA_COMUNE", DATE);	// 20210517 MEV_21
	}

	//
	// METODI GET()
	//

	public String getCodComune() throws DAOException {
		return getString("COD_COMUNE");
	}

	public String getCodProvincia() throws DAOException {
		return getString("COD_PROVINCIA");
	}

	public String getDescrizione() throws DAOException {
		return getString("DESCRIZIONE");
	}

	public String getCap() throws DAOException {
		return getString("CAP");
	}

	public Date getDataCaricamentoRege() throws DAOException {
		return getDate("DATA_CARICAMENTO_REGE");
	}

	public String getCodSedeGiudiziaria() throws DAOException {
		return getString("COD_SEDE_GIUDIZIARIA");
	}

	public String getFlagValidita() throws DAOException {
		return getString("FLAG_VALIDITA");
	}

	public String getCodCatastaleComune() throws DAOException {
		return getString("COD_CATASTALE_COMUNE");
	}

	public Date getDataAggiornamentoComune() throws DAOException {
		return getDate("DATA_AGGIORNAMENTO_COMUNE");
	}

	public Date getDataFineValiditaComune() throws DAOException {
		return getDate("DATA_FINE_VALIDITA_COMUNE");
	}

	//
	// METODI SET()
	//

	public void setCodComune(String valore) {
		setString("COD_COMUNE", valore);
	}

	public void setCodProvincia(String valore) {
		setString("COD_PROVINCIA", valore);
	}

	public void setDescrizione(String valore) {
		setString("DESCRIZIONE", valore);
	}

	public void setCap(String valore) {
		setString("CAP", valore);
	}

	public void setDataCaricamentoRege(Date valore) {
		setDate("DATA_CARICAMENTO_REGE", valore);
	}

	public void setCodSedeGiudiziaria(String aValore) {
		setString("COD_SEDE_GIUDIZIARIA", aValore);
	}

	public void setFlagValidita(String aValore) {
		setString("FLAG_VALIDITA", aValore);
	}

	public void setCodCatastaleComune(String aValore) {
		setString("COD_CATASTALE_COMUNE", aValore);
	}

	public void setDataAggiornamentoComune(Date valore) {
		setDate("DATA_AGGIORNAMENTO_COMUNE", valore);
	}

	public void setDataFineValiditaComune(Date valore) {
		setDate("DATA_FINE_VALIDITA_COMUNE", valore);
	}

	//
	// GET MODEL
	//

	public GenericModel getModel() throws DAOException {

		return new ComuneModel(getCodComune(), getCodProvincia(), getDescrizione(), getCap(),
				getDataCaricamentoRege(), getCodSedeGiudiziaria(), "", false, getFlagValidita(), 
				getCodCatastaleComune(), getDataAggiornamentoComune(), getDataFineValiditaComune());
	}

	public void selCondizioni(ComuneModel aModel, String FlagVal) {

		String lCondizioni = new String();
		boolean inserito = false;

		if ((aModel.getCodComune() != null) && !(aModel.getCodComune().equals(""))) {
			lCondizioni = " COD_COMUNE = '" + aModel.getCodComune() + "'";
			inserito = true;
		}

		if ((aModel.getCodProvincia() != null) && !(aModel.getCodProvincia().equals(""))) {
			if (inserito)
				lCondizioni += " AND COD_PROVINCIA = '" + aModel.getCodProvincia() + "'";
			else {
				lCondizioni = " COD_PROVINCIA = '" + aModel.getCodProvincia() + "'";
				inserito = true;
			}
		}

		// Ticket#202510210160 - SIES: Anomalia nomi comuni - PROV. BOLZANO
		// uppercase di JAVA trasforma VILLNÖß in VILLNOSS e non lo trova
		// allora uso UPPER lato SQL
		if ((aModel.getDescrizione() != null) && !(aModel.getDescrizione().equals(""))) {
			if (inserito)
				lCondizioni += " AND DESCRIZIONE = UPPER('" + StringUtils.convertSqlString(aModel.getDescrizione())
						+ "')";
			else {
				lCondizioni = " DESCRIZIONE = UPPER('" + StringUtils.convertSqlString(aModel.getDescrizione())
						+ "')";
				inserito = true;
			}
		} else {
			if (inserito)
				lCondizioni += " AND DESCRIZIONE = '-'";
			else {
				lCondizioni = " DESCRIZIONE = '-'";
				inserito = true;
			}
		}

		if ((aModel.getCap() != null) && !(aModel.getCap().equals(""))) {
			if (inserito)
				lCondizioni += " AND CAP = '" + aModel.getCap() + "'";
			else {
				lCondizioni = " CAP = '" + aModel.getCap() + "'";
				inserito = true;
			}
		}

		if (aModel.getDataCaricamentoRege() != null) {
			if (inserito)
				lCondizioni += " AND DATA_CARICAMENTO_REGE = TO_DATE("
						+ DateUtils.getDateToString(aModel.getDataCaricamentoRege(), "ddMMyyyy")
						+ ", 'DDMMYYYY') ";
			else {
				lCondizioni = " DATA_CARICAMENTO_REGE = TO_DATE("
						+ DateUtils.getDateToString(aModel.getDataCaricamentoRege(), "ddMMyyyy")
						+ ", 'DDMMYYYY')";
				inserito = true;
			}
		}

		// AMBROS 09/2013 - NAPOLI NORD

		if (FlagVal.equals("S")) {
			if (inserito)
				lCondizioni += " AND FLAG_VALIDITA = 'S'";
			else {
				lCondizioni = " FLAG_VALIDITA = 'S'";
				inserito = true;
			}
		}
		// END

		setCondition(lCondizioni);
	}

	public void selCondizioniLike(ComuneModel aModel) {

		String lCondizioni = new String();
		lCondizioni += "";

		// Ticket#202510210160 - SIES: Anomalia nomi comuni - PROV. BOLZANO
		// uppercase di JAVA trasforma VILLNÖß in VILLNOSS e non lo trova
		// allora uso UPPER lato SQL
		if ((aModel.getDescrizione() != null) && !(aModel.getDescrizione().equals("")))
			lCondizioni += " DESCRIZIONE LIKE UPPER('" + StringUtils.convertSqlString(aModel.getDescrizione())
					+ "%') AND FLAG_VALIDITA = 'S'";
		else if (aModel.getCodProvincia() != null)
			lCondizioni += " COD_PROVINCIA = '" + aModel.getCodProvincia() + "' AND FLAG_VALIDITA = 'S'";

		lCondizioni += " ORDER BY DESCRIZIONE";
		setCondition(lCondizioni);
	}

	// 20210517	MEV_21
	public void selCondizioniNascita(ComuneModel aModel) {

		String lCondizioni = new String();
		lCondizioni += "";

		// Ticket#202510210160 - SIES: Anomalia nomi comuni - PROV. BOLZANO
		// uppercase di JAVA trasforma VILLNÖß in VILLNOSS e non lo trova
		// allora uso UPPER lato SQL
		if ((aModel.getDescrizione() != null) && !(aModel.getDescrizione().equals("")))
			lCondizioni += " DESCRIZIONE LIKE UPPER('" + StringUtils.convertSqlString(aModel.getDescrizione())
					+ "%')";
		else if (aModel.getCodProvincia() != null)
			lCondizioni += " COD_PROVINCIA = '" + aModel.getCodProvincia() + "'";

		// 20210721	Condizione di esclusione NAPOLI NORD
		lCondizioni += " AND NOT (DATA_FINE_VALIDITA_COMUNE is NULL AND FLAG_VALIDITA = 'N')";

		lCondizioni += " ORDER BY DESCRIZIONE ASC, DATA_FINE_VALIDITA_COMUNE DESC";
		setCondition(lCondizioni);
	}
	
}