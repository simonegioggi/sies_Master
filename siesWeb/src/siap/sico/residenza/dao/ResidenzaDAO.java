package siap.sico.residenza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.sico.residenza.model.ResidenzaModel;

/**
 * Title: ResidenzaDAO
 * Description: Classe DAO che rappresenta la tabella Residenza
 *
 * @version 1.0
 */
public class ResidenzaDAO extends SIAPTableDAO {

	public ResidenzaDAO(Connection con) {

		super(con);

		setTable("RESIDENZA");

		setSequenceField("ID_RESIDENZA", "RES_SEQ");

		setFieldKey("ID_RESIDENZA", BIG_DECIMAL);

		setField("ID_RESIDENZA", BIG_DECIMAL);
		setField("COD_STATO", STRING);
		setField("COD_PROVINCIA", STRING);
		setField("COD_COMUNE", STRING);
		setField("CAP", STRING);
		setField("INDIRIZZO", STRING);
		setField("COD_TIPO_RESIDENZA", STRING);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("SOG_ID_SOGGETTO", BIG_DECIMAL);
		setField("DESC_COMUNE_ESTERO", STRING);
		setField("FLG_DOM_AVV", STRING);
		setField("ID_PARTE_UDIENZA", BIG_DECIMAL);
		setField("FLG_DOMICILIO_DIFENSORE", STRING);
		// MEV_2023-13: aggiunto campo in estrazione e gestito ovunque
		setField("ID_CIVILMENTE_OBBLIGATO", BIG_DECIMAL);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdResidenza() throws DAOException {
		return getBigDecimal("ID_RESIDENZA");
	}

	public String getCodStato() throws DAOException {
		return getString("COD_STATO");
	}

	public String getCodProvincia() throws DAOException {
		return getString("COD_PROVINCIA");
	}

	public String getCodComune() throws DAOException {
		return getString("COD_COMUNE");
	}

	public String getCap() throws DAOException {
		return getString("CAP");
	}

	public String getIndirizzo() throws DAOException {
		return getString("INDIRIZZO");
	}

	public String getCodTipoResidenza() throws DAOException {
		return getString("COD_TIPO_RESIDENZA");
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

	public BigDecimal getSogIdSoggetto() throws DAOException {
		return getBigDecimal("SOG_ID_SOGGETTO");
	}

	public String getDescComuneEstero() throws DAOException {
		return getString("DESC_COMUNE_ESTERO");
	}

	public String getFlgDomAvv() throws DAOException {
		return getString("FLG_DOM_AVV");
	}

	public BigDecimal getIdParteUdienza() throws DAOException {
		return getBigDecimal("ID_PARTE_UDIENZA");
	}

	public String getFlgDomicilioDifensore() throws DAOException {
		return getString("FLG_DOMICILIO_DIFENSORE");
	}

	public BigDecimal getIdCivilmenteObbligato() throws DAOException {
		return getBigDecimal("ID_CIVILMENTE_OBBLIGATO");
	}

	//
	// METODI SET()
	//
	public void setIdResidenza(BigDecimal aValore) {
		setBigDecimal("ID_RESIDENZA", aValore);
	}

	public void setCodStato(String aValore) {
		setString("COD_STATO", aValore);
	}

	public void setCodProvincia(String aValore) {
		setString("COD_PROVINCIA", aValore);
	}

	public void setCodComune(String aValore) {
		setString("COD_COMUNE", aValore);
	}

	public void setCap(String aValore) {
		setString("CAP", aValore);
	}

	public void setIndirizzo(String aValore) {
		setString("INDIRIZZO", aValore);
	}

	public void setCodTipoResidenza(String aValore) {
		setString("COD_TIPO_RESIDENZA", aValore);
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

	public void setDescComuneEstero(String aValore) {
		setString("DESC_COMUNE_ESTERO", aValore);
	}

	public void setSogIdSoggetto(BigDecimal aValore) {
		setBigDecimal("SOG_ID_SOGGETTO", aValore);
	}

	public void setFlgDomAvv(String aValore) {
		setString("FLG_DOM_AVV", aValore);
	}

	public void setIdParteUdienza(BigDecimal aValore) {
		setBigDecimal("ID_PARTE_UDIENZA", aValore);
	}

	public void setFlgDomicilioDifensore(String aValore) {
		setString("FLG_DOMICILIO_DIFENSORE", aValore);
	}

	public void setIdCivilmenteObbligato(BigDecimal aValore) {
		setBigDecimal("ID_CIVILMENTE_OBBLIGATO", aValore);
	}

	public GenericModel getModel() throws DAOException {

		return new ResidenzaModel(getIdResidenza(), getCodStato(), "", getCodProvincia(), "", getCodComune(),
				"", getCap(), getIndirizzo(), getCodTipoResidenza(), "", getCodOperatoreInserimento(),
				getDataInserimento(), getCodUfficioInserimento(), "", getCodOperatoreAggiornamento(),
				getDataAggiornamento(), getCodUfficioAggiornamento(), "", getSogIdSoggetto(), getFlgDomAvv(),
				getIdParteUdienza(), getFlgDomicilioDifensore(), getIdCivilmenteObbligato());
	}

	public void setDAOFromModel(ResidenzaModel aModel) throws DAOException {

		setIdResidenza(aModel.getIdResidenza());
		setCodStato(aModel.getCodStato());
		setCodProvincia(aModel.getCodProvincia());
		setCodComune(aModel.getCodComune());
		setCap(aModel.getCap());
		setIndirizzo(aModel.getIndirizzo());
		setCodTipoResidenza(aModel.getCodTipoResidenza());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setSogIdSoggetto(aModel.getSogIdSoggetto());
		setDescComuneEstero(aModel.getDescComuneEstero());
		setFlgDomAvv(aModel.getFlgDomAvv());
		setIdParteUdienza(aModel.getIdParteUdienza());
		setFlgDomicilioDifensore(aModel.getFlgDomicilioDifensore());
		setIdCivilmenteObbligato(getIdCivilmenteObbligato());
	}

	/**
	 * Setta il DAO dal Model passato per l'update
	 *
	 * @param aModel
	 * @throws DAOException
	 */
	public void setDAOFromModelForUpdate(ResidenzaModel aModel) throws DAOException {

		setCodStato(aModel.getCodStato());
		setCodProvincia(aModel.getCodProvincia());
		setCodComune(aModel.getCodComune());
		setCap(aModel.getCap());
		setIndirizzo(aModel.getIndirizzo());
		setCodTipoResidenza(aModel.getCodTipoResidenza());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setSogIdSoggetto(aModel.getSogIdSoggetto());
		setDescComuneEstero(aModel.getDescComuneEstero());
		setFlgDomAvv(aModel.getFlgDomAvv());
		setIdParteUdienza(aModel.getIdParteUdienza());
		setFlgDomicilioDifensore(aModel.getFlgDomicilioDifensore());
		setIdCivilmenteObbligato(aModel.getIdCivilmenteObbligato());
		setCondizioneUpdate(aModel.getIdResidenza());
	}

	public void selCondizione(ResidenzaModel aModel) {

		String lCondizioni = new String();
		if ((aModel.getSogIdSoggetto() != null) && (aModel.getSogIdSoggetto().intValue() > 0)) {
			lCondizioni = " SOG_ID_SOGGETTO = " + aModel.getSogIdSoggetto();
		}
		setCondition(lCondizioni);
	}

	public void selPerIdSoggetto(BigDecimal aIdSoggetto) {

		String lCondizioni = " SOG_ID_SOGGETTO = " + aIdSoggetto;
		setCondition(lCondizioni);
	}

	public void setCondizioneUpdate(BigDecimal key) {

		setCondition(" ID_RESIDENZA = " + key);
	}

	/**
	 * Condizione di ricerca per selezionare tutte le Residenze distinte collegate allo stesso Fascicolo SIGE
	 */
	public void setCondizioneIdFasSige(BigDecimal aIdFasSige) {

		setCondition(
				" ID_RESIDENZA IN ( select distinct RES_ID_RESIDENZA from  RESIDENZA_FASCICOLO_SIGE where FAS_SIGE_ID_FASCICOLO_SIGE = "
						+ aIdFasSige + " )");
	}

	public void selPerIdParteUdienza(BigDecimal aIdParteUdienza) {

		String lCondizioni = " ID_PARTE_UDIENZA = " + aIdParteUdienza;
		setCondition(lCondizioni);
	}

	// MEV_2023-13: aggiunto metodo di selezione
	public void selPerIdCivilmenteObbligato(BigDecimal idCivilmenteObbligato) {

		String lCondizioni = " ID_CIVILMENTE_OBBLIGATO = " + idCivilmenteObbligato;
		setCondition(lCondizioni);
	}

}