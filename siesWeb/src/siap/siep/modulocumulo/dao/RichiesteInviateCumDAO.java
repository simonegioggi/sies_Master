package siap.siep.modulocumulo.dao;

/**
* <p>Title: RichiesteInviateCumDAO</p>
* <p>Description: Classe DAO che rappresenta la tabella Richieste_Inviate_Cum</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: InterSistemi Italia S.p.A.</p>
* @version 1.0
*/

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import f3b.dao.DAOException;
import f3b.dao.TableDAO;
import f3b.model.GenericModel;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;

public class RichiesteInviateCumDAO extends TableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public RichiesteInviateCumDAO(Connection con) {
		super(con);
		setTable("RICHIESTE_INVIATE_CUM");

		// Settare la Sequence e i campi chiave e commentare il setField del campo chiave
		setSequenceField("ID_RICHIESTE_INVIATE_CUM", "RICHIESTE_INVIATE_CUM_SEQ");

		// setField("ID_RICHIESTE_INVIATE_CUM", BIG_DECIMAL);
		setField("DATA_EMISSIONE", DATE);
		setField("DATA_TRASMISSIONE", DATE);
		setField("COD_MAGISTRATO", STRING);
		setField("CONTENUTO", STRING);
		setField("COD_UFFICIO_DEST", STRING);
		setField("COD_LUOGO_DEST", STRING);
		setField("ISTR_ID_ISTRUTTORIA_CUMULO", BIG_DECIMAL);

		setField("DOC_BLOB", TBLOB);
		setField("FLAG_VALIDATO", STRING);

		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getIdRichiesteInviateCum() throws DAOException {
		return getBigDecimal("ID_RICHIESTE_INVIATE_CUM");
	}

	public Date getDataEmissione() throws DAOException {
		return getDate("DATA_EMISSIONE");
	}

	public Date getDataTrasmissione() throws DAOException {
		return getDate("DATA_TRASMISSIONE");
	}

	public String getCodMagistrato() throws DAOException {
		return getString("COD_MAGISTRATO");
	}

	public String getContenuto() throws DAOException {
		return getString("CONTENUTIO");
	}

	public String getCodUfficioDest() throws DAOException {
		return getString("COD_UFFICIO_DEST");
	}

	public String getCodLuogoDest() throws DAOException {
		return getString("COD_LUOGO_DEST");
	}

	public BigDecimal getIstrIdIstruttoriaCumulo() throws DAOException {
		return getBigDecimal("ISTR_ID_ISTRUTTORIA_CUMULO");
	}

	public ByteArrayOutputStream getDocBlob() throws DAOException {
		return getBlob("DOC_BLOB");
	}

	public String getFlagDocValidato() throws DAOException {
		return getString("FLAG_VALIDATO");
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

	public BigDecimal getRicIdRichiesteInviateCum() throws DAOException {
		return getBigDecimal("RIC_ID_RICHIESTE_INVIATE_CUM");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setIdRichiesteInviateCum(BigDecimal aValore) {
		setBigDecimal("ID_RICHIESTE_INVIATE_CUM", aValore);
	}

	public void setDataEmissione(Date aValore) {
		setDate("DATA_EMISSIONE", aValore);
	}

	public void setDataTrasmissione(Date aValore) {
		setDate("DATA_TRASMISSIONE", aValore);
	}

	public void setCodMagistrato(String aValore) {
		setString("COD_MAGISTRATO", aValore);
	}

	public void setContenuto(String aValore) {
		setString("CONTENUTO", aValore);
	}

	public void setCodUfficioDest(String aValore) {
		setString("COD_UFFICIO_DEST", aValore);
	}

	public void setCodLuogoDest(String aValore) {
		setString("COD_LUOGO_DEST", aValore);
	}

	public void setIstrIdIstruttoriaCumulo(BigDecimal aValore) {
		setBigDecimal("ISTR_ID_ISTRUTTORIA_CUMULO", aValore);
	}

	public void setDocBlob(ByteArrayInputStream aValore) {
		setBlob("DOC_BLOB", aValore);
	}

	public void setFlagDocValidato(String aValore) {
		setString("FLAG_VALIDATO", aValore);
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

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new RichiesteInviateCumModel(getIdRichiesteInviateCum(), getDataEmissione(),
				getDataTrasmissione(), getCodMagistrato(), getContenuto(), getCodUfficioDest(), "",
				getCodLuogoDest(), "", getIstrIdIstruttoriaCumulo(),
				// getDocBlob(),
				getFlagDocValidato(),

				getCodOperatoreInserimento(), getDataInserimento(), getCodUfficioInserimento(),
				getCodOperatoreAggiornamento(), getDataAggiornamento(), getCodUfficioAggiornamento());
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(RichiesteInviateCumModel aModel) throws DAOException {
		// setIdRichiesteInviateCum ( aModel.getIdRichiesteInviateCum() );
		setDataEmissione(aModel.getDataEmissione());
		setDataTrasmissione(aModel.getDataTrasmissione());
		setCodMagistrato(aModel.getCodMagistrato());
		setContenuto(aModel.getContenuto());
		setCodUfficioDest(aModel.getCodUfficioDest());
		setCodLuogoDest(aModel.getCodLuogoDest());
		setIstrIdIstruttoriaCumulo(aModel.getIstrIdIstruttoriaCumulo());

		setFlagDocValidato(aModel.getFlagDocValidato());
		setDocBlob(aModel.getDocBlobIn());

		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
	}

	/************************************************************************************************
	 * Metodo che imposta i campi Per l'operazione di Validazione Richiesta del PM Inviata (Update)
	 ************************************************************************************************/
	public void setDAOFromModelForUploadBlob(RichiesteInviateCumModel aModel) throws DAOException {
		setDocBlob(aModel.getDocBlobIn());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setFlagDocValidato(aModel.getFlagDocValidato());
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(RichiesteInviateCumModel aModel) {
	}

	/*****************************************************************************
	 * Imposta la condizione di where per l'operazione di update puntuale si entra sempre in chiave
	 * 
	 * @param key
	 ****************************************************************************/
	public void selCondizioneUpdate(BigDecimal aIdRichiesteInviateCum) {
		String lCondizioni = new String();

		lCondizioni += " and ID_RICHIESTE_INVIATE_CUM = " + aIdRichiesteInviateCum;
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		setCondition(lCondizioni);
	}

	/*****************************************************************************
	 * Imposta la condizione di order by per la ricerca
	 * 
	 *****************************************************************************/
	public void setOrderBy() {
		String orderBy = "";
		// =======================================================================
		// Lasciare orderBy="" se non si vuole scegliere un ordinamento,
		// altrimenti elencare i campi separati da virgola
		// n.b. non inserire la clausola ORDER BY, viene aggiunta automaticamente
		// =======================================================================

		setOrder(orderBy);
	}

}