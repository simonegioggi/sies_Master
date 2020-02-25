package siap.sico.soggetto.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sico.soggetto.model.SoggettoModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.StringUtils;

/**
 * <p>
 * Title: SoggettoDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella Soggetto
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
public class SoggettoDAO extends SIAPTableDAO {

	public SoggettoDAO(Connection con) {
		super(con);

		setTable("SOGGETTO");

		// Settare la Sequence e i campi chiave

		setField("ID_SOGGETTO", BIG_DECIMAL);

		setSequenceField("ID_SOGGETTO", "SOG_SEQ");

		setFieldKey("ID_SOGGETTO", BIG_DECIMAL);

		setField("COD_FISCALE", STRING);
		setField("COD_CS", STRING);
		setField("COD_AFIS", STRING);
		setField("COGNOME", STRING);
		setField("NOME", STRING);
		setField("ANNO_NASCITA", BIG_DECIMAL);
		setField("DATA_NASCITA", DATE);
		setField("DATA_REATO_SIUS", DATE);
		setField("DATA_NASCITA_PRESUNTA", STRING);
		setField("DATA_NASCITA_PRESUNTA_CALC", DATE);
		setField("COD_COMUNE_NASCITA", STRING);
		setField("COD_PROVINCIA_NASCITA", STRING);
		setField("COD_STATO_NASCITA", STRING);
		setField("DESC_COMUNE_NASCITA_ESTERO", STRING);
		setField("NAZIONALITA", STRING);
		setField("PATERNITA", STRING);
		setField("COGNOME_MADRE", STRING);
		setField("NOME_MADRE", STRING);
		setField("SESSO", STRING);
		setField("ATTO_NASCITA", STRING);
		setField("NOTE", STRING);
		setField("COD_COMUNE_CASELLARIO", STRING);
		setField("FLAG_PRESENZA_FASCICOLO", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("MESE_NASCITA", BIG_DECIMAL);
		setField("KEY_SOGG_NSC", BIG_DECIMAL);
		setField("ETA_PRESUNTA_ANNI", BIG_DECIMAL);
		setField("ETA_PRESUNTA_MESI", BIG_DECIMAL);
	}

	//
	// METODI GET()
	//

	public BigDecimal getIdSoggetto() throws DAOException {
		return getBigDecimal("ID_SOGGETTO");
	}

	public String getCodFiscale() throws DAOException {
		return getString("COD_FISCALE");
	}

	public String getCodCs() throws DAOException {
		return getString("COD_CS");
	}

	public String getCodAfis() throws DAOException {
		return getString("COD_AFIS");
	}

	public String getCognome() throws DAOException {
		return getString("COGNOME");
	}

	public String getNome() throws DAOException {
		return getString("NOME");
	}

	public BigDecimal getAnnoNascita() throws DAOException {
		return getBigDecimal("ANNO_NASCITA");
	}

	public Date getDataNascita() throws DAOException {
		return getDate("DATA_NASCITA");
	}

	public Date getDataReatoSius() throws DAOException {
		return getDate("DATA_REATO_SIUS");
	}

	public String getDataNascitaPresunta() throws DAOException {
		return getString("DATA_NASCITA_PRESUNTA");
	}

	public Date getDataNascitaPresuntaCalc() throws DAOException {
		return getDate("DATA_NASCITA_PRESUNTA_CALC");
	}

	public String getCodComuneNascita() throws DAOException {
		return getString("COD_COMUNE_NASCITA");
	}

	public String getCodProvinciaNascita() throws DAOException {
		return getString("COD_PROVINCIA_NASCITA");
	}

	public String getCodStatoNascita() throws DAOException {
		return getString("COD_STATO_NASCITA");
	}

	public String getDescComuneNascitaEstero() throws DAOException {
		return getString("DESC_COMUNE_NASCITA_ESTERO");
	}

	public String getNazionalita() throws DAOException {
		return getString("NAZIONALITA");
	}

	public String getPaternita() throws DAOException {
		return getString("PATERNITA");
	}

	public String getCognomeMadre() throws DAOException {
		return getString("COGNOME_MADRE");
	}

	public String getNomeMadre() throws DAOException {
		return getString("NOME_MADRE");
	}

	public String getSesso() throws DAOException {
		return getString("SESSO");
	}

	public String getAttoNascita() throws DAOException {
		return getString("ATTO_NASCITA");
	}

	public String getNote() throws DAOException {
		return getString("NOTE");
	}

	public String getCodComuneCasellario() throws DAOException {
		return getString("COD_COMUNE_CASELLARIO");
	}

	public String getFlagPresenzaFascicolo() throws DAOException {
		return getString("FLAG_PRESENZA_FASCICOLO");
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

	public BigDecimal getMeseNascita() throws DAOException {
		return getBigDecimal("MESE_NASCITA");
	}

	public BigDecimal getKeySoggNsc() throws DAOException {
		return getBigDecimal("KEY_SOGG_NSC");
	}

	public BigDecimal getEtaPresuntaAnni() throws DAOException {
		return getBigDecimal("ETA_PRESUNTA_ANNI");
	}

	public BigDecimal getEtaPresuntaMesi() throws DAOException {
		return getBigDecimal("ETA_PRESUNTA_MESI");
	}

	//
	// METODI SET()
	//

	public void setIdSoggetto(BigDecimal aValore) {
		setBigDecimal("ID_SOGGETTO", aValore);
	}

	public void setCodFiscale(String aValore) {
		setString("COD_FISCALE", aValore);
	}

	public void setCodCs(String aValore) {
		setString("COD_CS", aValore);
	}

	public void setCodAfis(String aValore) {
		setString("COD_AFIS", aValore);
	}

	public void setCognome(String aValore) {
		setString("COGNOME", aValore);
	}

	public void setNome(String aValore) {
		setString("NOME", aValore);
	}

	public void setAnnoNascita(BigDecimal aValore) {
		setBigDecimal("ANNO_NASCITA", aValore);
	}

	public void setDataNascita(Date aValore) {
		setDate("DATA_NASCITA", aValore);
	}

	public void setDataReatoSius(Date aValore) {
		setDate("DATA_REATO_SIUS", aValore);
	}

	public void setDataNascitaPresunta(String aValore) {
		setString("DATA_NASCITA_PRESUNTA", aValore);
	}

	public void setDataNascitaPresuntaCalc(Date aValore) {
		setDate("DATA_NASCITA_PRESUNTA_CALC", aValore);
	}

	public void setCodComuneNascita(String aValore) {
		setString("COD_COMUNE_NASCITA", aValore);
	}

	public void setCodProvinciaNascita(String aValore) {
		setString("COD_PROVINCIA_NASCITA", aValore);
	}

	public void setCodStatoNascita(String aValore) {
		setString("COD_STATO_NASCITA", aValore);
	}

	public void setDescComuneNascitaEstero(String aValore) {
		setString("DESC_COMUNE_NASCITA_ESTERO", aValore);
	}

	public void setNazionalita(String aValore) {
		setString("NAZIONALITA", aValore);
	}

	public void setPaternita(String aValore) {
		setString("PATERNITA", aValore);
	}

	public void setCognomeMadre(String aValore) {
		setString("COGNOME_MADRE", aValore);
	}

	public void setNomeMadre(String aValore) {
		setString("NOME_MADRE", aValore);
	}

	public void setSesso(String aValore) {
		setString("SESSO", aValore);
	}

	public void setAttoNascita(String aValore) {
		setString("ATTO_NASCITA", aValore);
	}

	public void setNote(String aValore) {
		setString("NOTE", aValore);
	}

	public void setCodComuneCasellario(String aValore) {
		setString("COD_COMUNE_CASELLARIO", aValore);
	}

	public void setFlagPresenzaFascicolo(String aValore) {
		setString("FLAG_PRESENZA_FASCICOLO", aValore);
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

	public void setMeseNascita(BigDecimal aValore) {
		setBigDecimal("MESE_NASCITA", aValore);
	}

	public void setKeySoggNsc(BigDecimal aValore) {
		setBigDecimal("KEY_SOGG_NSC", aValore);
	}

	public void setEtaPresuntaAnni(BigDecimal aValore) {
		setBigDecimal("ETA_PRESUNTA_ANNI", aValore);
	}

	public void setEtaPresuntaMesi(BigDecimal aValore) {
		setBigDecimal("ETA_PRESUNTA_MESI", aValore);
	}

	public GenericModel getModel() throws DAOException {
		return new SoggettoModel(getIdSoggetto(), getCodFiscale(), getCodCs(), getCodAfis(), getCognome(),
				getNome(), getAnnoNascita(), getDataNascita(), getDataReatoSius(), getDataNascitaPresunta(),
				getDataNascitaPresuntaCalc(), getCodComuneNascita(), "", getCodProvinciaNascita(), "",
				getCodStatoNascita(), "", getDescComuneNascitaEstero(), getNazionalita(), "", getPaternita(),
				getCognomeMadre(), getNomeMadre(), getSesso(), getAttoNascita(), getNote(),
				getCodComuneCasellario(), "", getFlagPresenzaFascicolo(), getCodOperatoreInserimento(),
				getDataInserimento(), getCodUfficioInserimento(), "", getCodOperatoreAggiornamento(),
				getDataAggiornamento(), getCodUfficioAggiornamento(), getMeseNascita(), null, "",
				getKeySoggNsc(), getEtaPresuntaAnni(), getEtaPresuntaMesi());
	}

	public void setDAOFromModel(SoggettoModel aModel) throws DAOException {
		setIdSoggetto(aModel.getIdSoggetto());
		setCodFiscale(aModel.getCodFiscale());
		setCodCs(aModel.getCodCs());
		setCodAfis(aModel.getCodAfis());
		setCognome(aModel.getCognome().toUpperCase());
		setNome(aModel.getNome().toUpperCase());
		setAnnoNascita(aModel.getAnnoNascita());
		setDataNascita(aModel.getDataNascita());
		setDataReatoSius(aModel.getDataReatoSius());
		setDataNascitaPresunta(aModel.getDataNascitaPresunta());
		setDataNascitaPresuntaCalc(aModel.getDataNascitaPresuntaCalc());
		setCodComuneNascita(aModel.getCodComuneNascita());
		setCodProvinciaNascita(aModel.getCodProvinciaNascita());
		setCodStatoNascita(aModel.getCodStatoNascita());
		setDescComuneNascitaEstero(aModel.getDescComuneNascitaEstero().toUpperCase());
		setNazionalita(aModel.getNazionalita());
		setPaternita(aModel.getPaternita().toUpperCase());
		setCognomeMadre(aModel.getCognomeMadre().toUpperCase());
		setNomeMadre(aModel.getNomeMadre().toUpperCase());
		setSesso(aModel.getSesso());
		setAttoNascita(aModel.getAttoNascita());
		setNote(aModel.getNote());
		setCodComuneCasellario(aModel.getCodComuneCasellario());
		setFlagPresenzaFascicolo(aModel.getFlagPresenzaFascicolo());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setMeseNascita(aModel.getMeseNascita());
		setKeySoggNsc(aModel.getKeySoggNsc());
		setEtaPresuntaAnni(aModel.getEtaPresuntaAnni());
		setEtaPresuntaMesi(aModel.getEtaPresuntaMesi());
	}

	public void setDAOFromModelForUpdate(SoggettoModel aModel) throws DAOException {
		// setIdSoggetto( aModel.getIdSoggetto() );
		setCodFiscale(aModel.getCodFiscale());
		setCodCs(aModel.getCodCs());
		setCodAfis(aModel.getCodAfis());
		setCognome(aModel.getCognome());
		setNome(aModel.getNome());
		setDataNascita(aModel.getDataNascita());
		setDataNascitaPresunta(aModel.getDataNascitaPresunta());
		setDataNascitaPresuntaCalc(aModel.getDataNascitaPresuntaCalc());
		setDataReatoSius(aModel.getDataReatoSius());
		setAnnoNascita(aModel.getAnnoNascita());
		setCodComuneNascita(aModel.getCodComuneNascita());
		setCodProvinciaNascita(aModel.getCodProvinciaNascita());
		setCodStatoNascita(aModel.getCodStatoNascita());
		setDescComuneNascitaEstero(aModel.getDescComuneNascitaEstero());
		setNazionalita(aModel.getNazionalita());
		setPaternita(aModel.getPaternita());
		setCognomeMadre(aModel.getCognomeMadre());
		setNomeMadre(aModel.getNomeMadre());
		setSesso(aModel.getSesso());
		setAttoNascita(aModel.getAttoNascita());
		setNote(aModel.getNote());
		setCodComuneCasellario(aModel.getCodComuneCasellario());
		setFlagPresenzaFascicolo(aModel.getFlagPresenzaFascicolo());
		// setCodOperatoreInserimento( aModel.getCodOperatoreInserimento() );
		// setDataInserimento( aModel.getDataInserimento() );
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		// setCodUfficioInserimento( aModel.getCodUfficioInserimento() );
		setMeseNascita(aModel.getMeseNascita());
		setKeySoggNsc(aModel.getKeySoggNsc());
		setEtaPresuntaAnni(aModel.getEtaPresuntaAnni());
		setEtaPresuntaMesi(aModel.getEtaPresuntaMesi());
	}

	public void setDAOFromModelForUpdateKeyNsc(SoggettoModel aModel) throws DAOException {
		setKeySoggNsc(aModel.getKeySoggNsc());
	}

	/**
	 * Setta la condizione per la ricerca....?
	 * 
	 * @param aSm
	 */
	public void selCondizione(SoggettoModel aSm) {
		String lCondizioni = new String();
		boolean inserito = false;

		if (!(aSm.getCognome().equals(""))) {
			if (inserito)
				lCondizioni += " AND COGNOME LIKE '" + StringUtils.convertSqlString(aSm.getCognome()) + "%'";
			else {
				lCondizioni = " COGNOME LIKE '" + StringUtils.convertSqlString(aSm.getCognome()) + "%'";
				inserito = true;
			}
		}

		if (!(aSm.getNome().equals(""))) {
			if (inserito)
				lCondizioni += " AND NOME LIKE '" + StringUtils.convertSqlString(aSm.getNome()) + "%'";
			else {
				lCondizioni = " NOME LIKE '" + StringUtils.convertSqlString(aSm.getNome()) + "%'";
				inserito = true;
			}
		}

		if (aSm.getIdSoggetto().doubleValue() != 0) {
			if (inserito)
				lCondizioni += " AND ID_SOGGETTO=" + aSm.getIdSoggetto();
			else {
				lCondizioni = " ID_SOGGETTO=" + aSm.getIdSoggetto();
				inserito = true;
			}
		}

		if (!(aSm.getCodComuneNascita().equals(""))) {
			if (inserito)
				lCondizioni += " AND COD_COMUNE_NASCITA = " + aSm.getCodComuneNascita();
			else {
				lCondizioni = " COD_COMUNE_NASCITA = " + aSm.getCodComuneNascita();
				inserito = true;
			}
		}

		if (aSm.getDataNascita() != null) {
			if (inserito)
				// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
				lCondizioni += " AND trunc(DATA_NASCITA) = TO_DATE("
						+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + ", 'DDMMYYYY') ";
			else {
				// 20180110: [SG] aggiunta trunc sulla data nascita per gestire la presenza di ore min sec
				lCondizioni = " trunc(DATA_NASCITA) = TO_DATE("
						+ DateUtils.getDateToString(aSm.getDataNascita(), "ddMMyyyy") + ", 'DDMMYYYY')";
				inserito = true;
			}
		}

		if (inserito) {
			setCondition(lCondizioni);
			setOrder("COGNOME");
		}

	}

	/**
	 * Imposta la selezione della condizione di aggiornamento.
	 * <p>
	 * 
	 * @param key
	 *            id del soggetto, da modificare.
	 */
	public void selCondizioneUpdate(BigDecimal key) {
		String lCondizioni = " ID_SOGGETTO = " + key;

		setCondition(lCondizioni);
	}

	public void setCondizioneByCodiceCUI$AFIS(String aCodice) {
		String lCondizioni = " COD_AFIS = '" + aCodice + "'";

		setCondition(lCondizioni);
	}

	public void setCondizioneByCodiceCS(String aCodice) {
		String lCondizioni = " COD_CS = '" + aCodice + "'";

		setCondition(lCondizioni);
	}

	public void setCondizioneByAttoNascita(String aAtto) {
		String lCondizioni = " ATTO_NASCITA = '" + aAtto + "'";

		setCondition(lCondizioni);
	}

}