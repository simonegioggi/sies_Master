package siap.siep.pagoPA.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPTableDAO;
import siap.siep.pagoPA.model.BollettinoPagopaModel;

/**
 * MEV_2023-13 Title: BollettinoPagopaDAO Description: Classe DAO per la gestione del Bollettino PagoPA
 *
 * @author sgioggi
 * @version 1.0
 */
public class BollettinoPagopaDAO extends SIAPTableDAO {

	public BollettinoPagopaDAO(Connection con) {

		super(con);
		setTable("BOLLETTINO_PAGOPA");

		// Settare la Sequence e i campi chiave
		setSequenceField("ID_BOLLETTINO_PAGOPA", "BOLL_PAG_SEQ");
		setFieldKey("ID_BOLLETTINO_PAGOPA", BIG_DECIMAL);

		setField("ID_BOLLETTINO_PAGOPA", BIG_DECIMAL);
		setField("PROG_RATA", INT);
		setField("NUMERO_RATE", INT);
		setField("TIPO_RATEIZZAZIONE", STRING);
		setField("IUV", STRING);
		setField("IMPORTO_RATA", BIG_DECIMAL);
		setField("IMPORTO_PAGATO", BIG_DECIMAL);
		setField("DATA_AVV_PAGAMENTO", DATE);
		setField("DATA_SCADENZA", DATE);
		setField("DATA_SCADENZA_RICH", DATE);
		setField("STATO_PAGAMENTO", STRING);
		setField("DOC_BOLL_BLOB", TBLOB);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("FAS_SIE_ID_FASCICOLO_SIEP", BIG_DECIMAL);
		setField("RAT_ID_RATEIZZAZIONE_PP", BIG_DECIMAL);
	}

	//
	// METODI GET()
	//
	public BigDecimal getIdBollettinoPagopa() throws DAOException {
		return getBigDecimal("ID_BOLLETTINO_PAGOPA");
	}

	public int getProgRata() throws DAOException {
		return getInt("PROG_RATA");
	}

	public int getNumeroRate() throws DAOException {
		return getInt("NUMERO_RATE");
	}

	public String getTipoRateizzazione() throws DAOException {
		return getString("TIPO_RATEIZZAZIONE");
	}

	public String getIuv() throws DAOException {
		return getString("IUV");
	}

	public BigDecimal getImportoRata() throws DAOException {
		return getBigDecimal("IMPORTO_RATA");
	}

	public BigDecimal getImportoPagato() throws DAOException {
		return getBigDecimal("IMPORTO_PAGATO");
	}

	public Date getDataAvvPagamento() throws DAOException {
		return getDate("DATA_AVV_PAGAMENTO");
	}

	public Date getDataScadenza() throws DAOException {
		return getDate("DATA_SCADENZA");
	}

	public Date getDataScadenzaRich() throws DAOException {
		return getDate("DATA_SCADENZA_RICH");
	}

	public String getStatoPagamento() throws DAOException {
		return getString("STATO_PAGAMENTO");
	}

	public ByteArrayOutputStream getDocBollBlob() throws DAOException {
		return getBlob("DOC_BOLL_BLOB");
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

	public BigDecimal getFasSieIdFascicolSiep() throws DAOException {
		return getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP");
	}

	public BigDecimal getRatIdRateizzazionePP() throws DAOException {
		return getBigDecimal("RAT_ID_RATEIZZAZIONE_PP");
	}

	//
	// METODI SET()
	//
	public void setIdBollettinoPagopa(BigDecimal aValore) {
		setBigDecimal("ID_BOLLETTINO_PAGOPA", aValore);
	}

	public void setProgRata(int aValore) {
		setInt("PROG_RATA", aValore);
	}

	public void setNumeroRate(int aValore) {
		setInt("NUMERO_RATE", aValore);
	}

	public void setTipoRateizzazione(String aValore) {
		setString("TIPO_RATEIZZAZIONE", aValore);
	}

	public void setIuv(String aValore) {
		setString("IUV", aValore);
	}

	public void setImportoRata(BigDecimal aValore) {
		setBigDecimal("IMPORTO_RATA", aValore);
	}

	public void setImportoPagato(BigDecimal aValore) {
		setBigDecimal("IMPORTO_PAGATO", aValore);
	}

	public void setDataAvvPagamento(Date aValore) {
		setDate("DATA_AVV_PAGAMENTO", aValore);
	}

	public void setDataScadenza(Date aValore) {
		setDate("DATA_SCADENZA", aValore);
	}

	public void setDataScadenzaRich(Date aValore) {
		setDate("DATA_SCADENZA_RICH", aValore);
	}

	public void setStatoPagamento(String aValore) {
		setString("STATO_PAGAMENTO", aValore);
	}

	public void setDocBollBlob(ByteArrayInputStream aValore) {
		setBlob("DOC_BOLL_BLOB", aValore);
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

	public void setFasSieIdFascicolSiep(BigDecimal aValore) {
		setBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP", aValore);
	}

	public void setRatIdRateizzazionePP(BigDecimal aValore) {
		setBigDecimal("RAT_ID_RATEIZZAZIONE_PP", aValore);
	}

	public GenericModel getModel() throws DAOException {

		return new BollettinoPagopaModel(getIdBollettinoPagopa(), getProgRata(), getNumeroRate(),
				getTipoRateizzazione(), getIuv(), getImportoRata(), getImportoPagato(), getDataAvvPagamento(),
				getDataScadenza(), getDataScadenzaRich(), getStatoPagamento(), getCodOperatoreInserimento(),
				getDataInserimento(), getCodUfficioInserimento(), getCodOperatoreAggiornamento(),
				getDataAggiornamento(), getCodUfficioAggiornamento(), getFasSieIdFascicolSiep(),
				getRatIdRateizzazionePP(), "", ""); // + 2 descrizioni
	}

	public void setDAOFromModel(BollettinoPagopaModel aModel) throws DAOException {

		setIdBollettinoPagopa(aModel.getIdBollettinoPagopa());
		setProgRata(aModel.getProgRata());
		setNumeroRate(aModel.getNumeroRate());
		setTipoRateizzazione(aModel.getTipoRateizzazione());
		setIuv(aModel.getIuv());
		setImportoRata(aModel.getImportoRata());
		setImportoPagato(aModel.getImportoPagato());
		setDataAvvPagamento(aModel.getDataAvvPagamento());
		setDataScadenza(aModel.getDataScadenza());
		setDataScadenzaRich(aModel.getDataScadenzaRich());
		setStatoPagamento(aModel.getStatoPagamento());
		setDocBollBlob(aModel.getDocBollBlob());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setFasSieIdFascicolSiep(aModel.getFasSieIdFascicolSiep());
		setRatIdRateizzazionePP(aModel.getRatIdRateizzazionePP());
	}

	public void setDAOFromModelForUpdate(BollettinoPagopaModel aModel) throws DAOException {

		setTipoRateizzazione(aModel.getTipoRateizzazione());
		setNumeroRate(aModel.getNumeroRate());
		setProgRata(aModel.getProgRata());
		setIuv(aModel.getIuv());
		setImportoRata(aModel.getImportoRata());
		setImportoPagato(aModel.getImportoPagato());
		setDataAvvPagamento(aModel.getDataAvvPagamento());
		setDataScadenza(aModel.getDataScadenza());
		setDataScadenzaRich(aModel.getDataScadenzaRich());
		setStatoPagamento(aModel.getStatoPagamento());
		setDocBollBlob(aModel.getDocBollBlob());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setFasSieIdFascicolSiep(aModel.getFasSieIdFascicolSiep());
		setRatIdRateizzazionePP(aModel.getRatIdRateizzazionePP());

		setCondizioneUpdate(aModel.getIdBollettinoPagopa());
	}

	public void setCondizioneUpdate(BigDecimal IdBollettinoPagopa) {
		setCondition(" ID_BOLLETTINO_PAGOPA = " + IdBollettinoPagopa);
	}

	public void selCondizioneDeleteByKey(BigDecimal IdBollettinoPagopa) {
		setCondition(" ID_BOLLETTINO_PAGOPA = " + IdBollettinoPagopa);
	}

}