package siap.siep.pagoPA.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;

/**
 * MEV_2023-13 
 * Title: CivilmenteObbligatoDAO 
 * Description: Classe DAO per la gestione del Civilmente Obbligato
 *
 * @author sgioggi
 * @version 1.0
 */
public class CivilmenteObbligatoDAO extends SIAPTableDAO {

	public CivilmenteObbligatoDAO(Connection con) {

		super(con);
		setTable("CIVILMENTE_OBBLIGATO");

		// Settare la Sequence e i campi chiave
		setSequenceField("ID_CIVILMENTE_OBBLIGATO", "SEQ_CIVILMENTE_OBBLIGATO");
		setFieldKey("ID_CIVILMENTE_OBBLIGATO", BIG_DECIMAL);

		setField("ID_CIVILMENTE_OBBLIGATO", BIG_DECIMAL);
		setField("COD_TUTORE", STRING);
		setField("COD_PERSONA", STRING);
		setField("COD_FISCALE", STRING);
		setField("COGNOME", STRING);
		setField("NOME", STRING);
		setField("DENOMINAZIONE", STRING);
		setField("DATA_NASCITA", DATE);
		setField("COD_COMUNE_NASCITA", STRING);
		setField("COD_PROVINCIA_NASCITA", STRING);
		setField("COD_STATO_NASCITA", STRING);
		setField("DESC_COMUNE_NASCITA_ESTERO", STRING);
		setField("SESSO", STRING);
		setField("RAG_SOCIALE", STRING);
		setField("COD_PROVINCIA", STRING);
		setField("IND_SEDE_LEGALE", STRING);
		setField("IND_SEDE_OPERATIVA", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("COD_FISCALE_RAP", STRING);
		setField("PEC", STRING);
		setField("E_MAIL", STRING);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdCivilmenteObbligato() throws DAOException {
		return getBigDecimal("ID_CIVILMENTE_OBBLIGATO");
	}

	public String getCodTutore() throws DAOException {
		return getString("COD_TUTORE");
	}

	public String getCodPersona() throws DAOException {
		return getString("COD_PERSONA");
	}

	public String getCodFiscale() throws DAOException {
		return getString("COD_FISCALE");
	}

	public String getCognome() throws DAOException {
		return getString("COGNOME");
	}

	public String getNome() throws DAOException {
		return getString("NOME");
	}

	public String getDenominazione() throws DAOException {
		return getString("DENOMINAZIONE");
	}

	public Date getDataNascita() throws DAOException {
		return getDate("DATA_NASCITA");
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

	public String getSesso() throws DAOException {
		return getString("SESSO");
	}

	public String getRagSociale() throws DAOException {
		return getString("RAG_SOCIALE");
	}

	public String getCodProvincia() throws DAOException {
		return getString("COD_PROVINCIA");
	}

	public String getIndSedeLegale() throws DAOException {
		return getString("IND_SEDE_LEGALE");
	}

	public String getIndSedeOperativa() throws DAOException {
		return getString("IND_SEDE_OPERATIVA");
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

	public String getCodFiscaleRap() throws DAOException {
		return getString("COD_FISCALE_RAP");
	}

	public String getPec() throws DAOException {
		return getString("PEC");
	}

	public String getEmail() throws DAOException {
		return getString("E_MAIL");
	}

	public BigDecimal getFasSieIdFascicolSiep() throws DAOException {
		return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP");
	}

	//
	// METODI SET()
	//
	public void setIdCivilmenteObbligato(BigDecimal aValore) {
		setBigDecimal("ID_CIVILMENTE_OBBLIGATO", aValore);
	}

	public void setCodTutore(String aValore) {
		setString("COD_TUTORE", aValore);
	}

	public void setCodPersona(String aValore) {
		setString("COD_PERSONA", aValore);
	}

	public void setCodFiscale(String aValore) {
		setString("COD_FISCALE", aValore);
	}

	public void setCognome(String aValore) {
		setString("COGNOME", aValore);
	}

	public void setNome(String aValore) {
		setString("NOME", aValore);
	}

	public void setDenominazione(String aValore) {
		setString("DENOMINAZIONE", aValore);
	}

	public void setDataNascita(Date aValore) {
		setDate("DATA_NASCITA", aValore);
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

	public void setSesso(String aValore) {
		setString("SESSO", aValore);
	}

	public void setRagSociale(String aValore) {
		setString("RAG_SOCIALE", aValore);
	}

	public void setCodProvincia(String aValore) {
		setString("COD_PROVINCIA", aValore);
	}

	public void setIndSedeLegale(String aValore) {
		setString("IND_SEDE_LEGALE", aValore);
	}

	public void setIndSedeOperativa(String aValore) {
		setString("IND_SEDE_OPERATIVA", aValore);
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

	public void setCodFiscaleRap(String aValore) {
		setString("COD_FISCALE_RAP", aValore);
	}

	public void setPec(String aValore) {
		setString("PEC", aValore);
	}

	public void setEmail(String aValore) {
		setString("E_MAIL", aValore);
	}

	public void setFasSieIdFascicolSiep(BigDecimal aValore) {
		setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore);
	}

	public GenericModel getModel() throws DAOException {

		return new CivilmenteObbligatoModel(getIdCivilmenteObbligato(), getCodTutore(), getCodPersona(),
				getCodFiscale(), getCognome(), getNome(), getDenominazione(), getDataNascita(),
				getCodComuneNascita(), getCodStatoNascita(), "", getDescComuneNascitaEstero(),
				getCodProvinciaNascita(), "", getSesso(), getRagSociale(), getCodProvincia(),
				getIndSedeLegale(), getIndSedeOperativa(), getCodOperatoreInserimento(), getDataInserimento(),
				getCodUfficioInserimento(), getCodOperatoreAggiornamento(), getDataAggiornamento(),
				getCodUfficioAggiornamento(), getCodFiscaleRap(), getPec(), getEmail(),
				getFasSieIdFascicolSiep());
	}

	public void setDAOFromModel(CivilmenteObbligatoModel aModel) throws DAOException {

		setIdCivilmenteObbligato(aModel.getIdCivilmenteObbligato());
		setCodTutore(aModel.getCodTutore());
		setCodPersona(aModel.getCodPersona());
		setCodFiscale(aModel.getCodFiscale());
		setCognome(aModel.getCognome());
		setNome(aModel.getNome());
		setDenominazione(aModel.getDenominazione());
		setDataNascita(aModel.getDataNascita());
		setCodComuneNascita(aModel.getCodComuneNascita());
		setCodProvinciaNascita(aModel.getCodProvinciaNascita());
		setCodStatoNascita(aModel.getCodStatoNascita());
		setDescComuneNascitaEstero(aModel.getDescComuneNascitaEstero());
		setSesso(aModel.getSesso());
		setRagSociale(aModel.getRagSociale());
		setCodProvincia(aModel.getCodProvincia());
		setIndSedeLegale(aModel.getIndSedeLegale());
		setIndSedeOperativa(aModel.getIndSedeOperativa());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodFiscaleRap(aModel.getCodFiscaleRap());
		setPec(aModel.getPec());
		setEmail(aModel.getEmail());
		setFasSieIdFascicolSiep(aModel.getFasSieIdFascicolSiep());
	}

	public void setDAOFromModelForUpdate(CivilmenteObbligatoModel aModel) throws DAOException {

		setCodFiscale(aModel.getCodFiscale());
		setCodPersona(aModel.getCodPersona());
		setCodTutore(aModel.getCodTutore());
		setCognome(aModel.getCognome());
		setNome(aModel.getNome());
		setDenominazione(aModel.getDenominazione());
		setDataNascita(aModel.getDataNascita());
		setCodComuneNascita(aModel.getCodComuneNascita());
		setCodProvinciaNascita(aModel.getCodProvinciaNascita());
		setCodStatoNascita(aModel.getCodStatoNascita());
		setDescComuneNascitaEstero(aModel.getDescComuneNascitaEstero());
		setSesso(aModel.getSesso());
		setRagSociale(aModel.getRagSociale());
		setCodProvincia(aModel.getCodProvincia());
		setIndSedeLegale(aModel.getIndSedeLegale());
		setIndSedeOperativa(aModel.getIndSedeOperativa());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setCodFiscaleRap(aModel.getCodFiscaleRap());
		setPec(aModel.getPec());
		setEmail(aModel.getEmail());
		setFasSieIdFascicolSiep(aModel.getFasSieIdFascicolSiep());

		setCondizioneUpdate(aModel.getIdCivilmenteObbligato());
	}

	public void setCondizioneUpdate(BigDecimal idCivilmenteObbligato) {
		setCondition(" ID_CIVILMENTE_OBBLIGATO = " + idCivilmenteObbligato);
	}

	public void selCondizioneDeleteByKey(BigDecimal idCivilmenteObbligato) {
		setCondition(" ID_CIVILMENTE_OBBLIGATO = " + idCivilmenteObbligato);
	}

}