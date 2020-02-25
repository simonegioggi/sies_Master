package siap.siep.tipoeventibdmc.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPTableDAO;
import siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: TipoEventiBdmcDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella TipoEventiBdmc
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
public class TipoEventiBdmcDAO extends SIAPTableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public TipoEventiBdmcDAO(Connection con) {
		super(con);
		setTable("TIPO_EVENTI_BDMC");

		// Settare la Sequence e i campi chiave e commentare il setField del campo chiave
		// setSequenceField("ID_TIPO_EVENTI_BDMC","SEQ_TIPO_EVENTI_BDMC");

		setField("ID_TIPO_EVENTI_BDMC", BIG_DECIMAL);
		setField("COD_TIPO_EVENTO", STRING);
		setField("COD_PROVVEDIMENTO", STRING);
		setField("COD_MOTIVO", STRING);
	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getIdTipoEventiBdmc() throws DAOException {
		return getBigDecimal("ID_TIPO_EVENTI_BDMC");
	}

	public String getCodTipoEvento() throws DAOException {
		return getString("COD_TIPO_EVENTO");
	}

	public String getCodProvvedimento() throws DAOException {
		return getString("COD_PROVVEDIMENTO");
	}

	public String getCodMotivo() throws DAOException {
		return getString("COD_MOTIVO");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setIdTipoEventiBdmc(BigDecimal aValore) {
		setBigDecimal("ID_TIPO_EVENTI_BDMC", aValore);
	}

	public void setCodTipoEvento(String aValore) {
		setString("COD_TIPO_EVENTO", aValore);
	}

	public void setCodProvvedimento(String aValore) {
		setString("COD_PROVVEDIMENTO", aValore);
	}

	public void setCodMotivo(String aValore) {
		setString("COD_MOTIVO", aValore);
	}

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new TipoEventiBdmcModel(getIdTipoEventiBdmc(), getCodTipoEvento(), "", getCodProvvedimento(),
				"", getCodMotivo());
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(TipoEventiBdmcModel aModel) throws DAOException {
		setIdTipoEventiBdmc(aModel.getIdTipoEventiBdmc());
		setCodTipoEvento(aModel.getCodTipoEvento());
		setCodProvvedimento(aModel.getCodProvvedimento());
		setCodMotivo(aModel.getCodMotivo());
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(TipoEventiBdmcModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdTipoEventiBdmc() != null) {
			lCondizioni += " and ID_TIPO_EVENTI_BDMC = " + aModel.getIdTipoEventiBdmc() + "";
		}
		if (aModel.getCodTipoEvento() != null && aModel.getCodTipoEvento().length() > 0) {
			lCondizioni += " and COD_TIPO_EVENTO = '" + aModel.getCodTipoEvento() + "' ";
		}
		if (aModel.getCodProvvedimento() != null && aModel.getCodProvvedimento().length() > 0) {
			lCondizioni += " and COD_PROVVEDIMENTO = '" + aModel.getCodProvvedimento() + "' ";
		}
		if (aModel.getCodMotivo() != null && aModel.getCodMotivo().length() > 0) {
			lCondizioni += " and COD_MOTIVO = '" + aModel.getCodMotivo() + "' ";
		}
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		setCondition(lCondizioni);
	}

	/*****************************************************************************
	 * Imposta la condizione di where per l'operazione di update puntuale si entra sempre in chiave
	 * 
	 * @param key
	 ****************************************************************************/
	public void selCondizioneUpdate(BigDecimal aIdTipoEventiBdmc) {
		String lCondizioni = new String();

		lCondizioni += " and ID_TIPO_EVENTI_BDMC = " + aIdTipoEventiBdmc;
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