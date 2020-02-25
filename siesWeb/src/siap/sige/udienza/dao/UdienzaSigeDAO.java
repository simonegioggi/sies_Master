package siap.sige.udienza.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.dao.SIAPTableDAO;
import siap.sige.udienza.model.UdienzaSigeModel;
import f3b.dao.DAOException;
//import f3b.dao.TableOracleDAO;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: UdienzaSigeDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella UdienzaSige
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
public class UdienzaSigeDAO extends SIAPTableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public UdienzaSigeDAO(Connection con) {
		super(con);
		setTable("UDIENZA_SIGE");

		// Settare la Sequence e i campi chiave
		setSequenceField("ID_UDIENZA_SIGE", "UDI_SIGE_SEQ");
		setFieldKey("ID_UDIENZA_SIGE", BIG_DECIMAL);

		setField("ID_UDIENZA_SIGE", BIG_DECIMAL);
		setField("DATA_UDIENZA", DATE);
		setField("COD_GIUDICE", STRING);
		setField("COL_ID_COLLEGIO", BIG_DECIMAL);
		setField("COD_PROCURATORE", STRING);
		setField("COD_ID_ASSISTENTE", BIG_DECIMAL);
		setField("NUMERO_MAX_FASCICOLI", BIG_DECIMAL);
		setField("COD_OPERATORE_INSERIMENTO", STRING);
		setField("DATA_INSERIMENTO", DATE);
		setField("COD_UFFICIO_INSERIMENTO", STRING);
		setField("COD_OPERATORE_AGGIORNAMENTO", STRING);
		setField("DATA_AGGIORNAMENTO", DATE);
		setField("COD_UFFICIO_AGGIORNAMENTO", STRING);
		setField("LUOGO_UDIENZA", STRING);
		setField("COD_UFFICIO_APPARTENENZA", STRING);
		setField("ORA_INIZIO", STRING);
		setField("MIN_INIZIO", STRING);
		setField("ORA_FINE", STRING);
		setField("MIN_FINE", STRING);

		setField("SEZIONE_UDIENZA", BIG_DECIMAL);
		setField("AULA_UDIENZA", BIG_DECIMAL);

	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getIdUdienzaSige() throws DAOException {
		return getBigDecimal("ID_UDIENZA_SIGE");
	}

	public Date getDataUdienza() throws DAOException {
		return getDate("DATA_UDIENZA");
	}

	public String getCodGiudice() throws DAOException {
		return getString("COD_GIUDICE");
	}

	public BigDecimal getColIdCollegio() throws DAOException {
		return getBigDecimal("COL_ID_COLLEGIO");
	}

	public String getCodProcuratore() throws DAOException {
		return getString("COD_PROCURATORE");
	}

	public BigDecimal getCodIdAssistente() throws DAOException {
		return getBigDecimal("COD_ID_ASSISTENTE");
	}

	public BigDecimal getNumeroMaxFascicoli() throws DAOException {
		return getBigDecimal("NUMERO_MAX_FASCICOLI");
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

	public String getLuogoUdienza() throws DAOException {
		return getString("LUOGO_UDIENZA");
	}

	public String getCodUfficioAppartenenza() throws DAOException {
		return getString("COD_UFFICIO_APPARTENENZA");
	}

	public String getOraInizio() throws DAOException {
		return getString("ORA_INIZIO");
	}

	public String getMinInizio() throws DAOException {
		return getString("MIN_INIZIO");
	}

	public String getOraFine() throws DAOException {
		return getString("ORA_FINE");
	}

	public String getMinFine() throws DAOException {
		return getString("MIN_FINE");
	}

	public BigDecimal getCodIdSezioneUdienza() throws DAOException {
		return getBigDecimal("SEZIONE_UDIENZA");
	}

	public BigDecimal getCodIdAulaUdienza() throws DAOException {
		return getBigDecimal("AULA_UDIENZA");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setIdUdienzaSige(BigDecimal aValore) {
		setBigDecimal("ID_UDIENZA_SIGE", aValore);
	}

	public void setDataUdienza(Date aValore) {
		setDate("DATA_UDIENZA", aValore);
	}

	public void setCodGiudice(String aValore) {
		setString("COD_GIUDICE", aValore);
	}

	public void setColIdCollegio(BigDecimal aValore) {
		setBigDecimal("COL_ID_COLLEGIO", aValore);
	}

	public void setCodProcuratore(String aValore) {
		setString("COD_PROCURATORE", aValore);
	}

	public void setCodIdAssistente(BigDecimal aValore) {
		setBigDecimal("COD_ID_ASSISTENTE", aValore);
	}

	public void setNumeroMaxFascicoli(BigDecimal aValore) {
		setBigDecimal("NUMERO_MAX_FASCICOLI", aValore);
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

	public void setLuogoUdienza(String aValore) {
		setString("LUOGO_UDIENZA", aValore);
	}

	public void setCodUfficioAppartenenza(String aValore) {
		setString("COD_UFFICIO_APPARTENENZA", aValore);
	}

	public void setOraInizio(String aValore) {
		setString("ORA_INIZIO", aValore);
	}

	public void setMinInizio(String aValore) {
		setString("MIN_INIZIO", aValore);
	}

	public void setOraFine(String aValore) {
		setString("ORA_FINE", aValore);
	}

	public void setMinFine(String aValore) {
		setString("MIN_FINE", aValore);
	}

	public void setCodIdSezioneUdienza(BigDecimal aValore) {
		setBigDecimal("SEZIONE_UDIENZA", aValore);
	}

	public void setCodIdAulaUdienza(BigDecimal aValore) {
		setBigDecimal("AULA_UDIENZA", aValore);
	}

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new UdienzaSigeModel(getIdUdienzaSige(), getDataUdienza(), getCodGiudice(), "",
				getColIdCollegio(), getCodProcuratore(), "", getCodIdAssistente(), "",
				getNumeroMaxFascicoli(), getCodOperatoreInserimento(), getDataInserimento(),
				getCodUfficioInserimento(), "", getCodOperatoreAggiornamento(), getDataAggiornamento(),
				getCodUfficioAggiornamento(), "", getLuogoUdienza(), getCodUfficioAppartenenza(), "",
				getOraInizio(), getMinInizio(), getOraFine(), getMinFine(), getCodIdSezioneUdienza(),
				getCodIdAulaUdienza());
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(UdienzaSigeModel aModel) throws DAOException {
		setIdUdienzaSige(aModel.getIdUdienzaSige());
		setDataUdienza(aModel.getDataUdienza());
		setCodGiudice(aModel.getCodGiudice());
		setColIdCollegio(aModel.getColIdCollegio());
		setCodProcuratore(aModel.getCodProcuratore());
		setCodIdAssistente(aModel.getCodIdAssistente());
		setNumeroMaxFascicoli(aModel.getNumeroMaxFascicoli());
		setCodOperatoreInserimento(aModel.getCodOperatoreInserimento());
		setDataInserimento(aModel.getDataInserimento());
		setCodUfficioInserimento(aModel.getCodUfficioInserimento());
		// setCodOperatoreAggiornamento ( aModel.getCodOperatoreAggiornamento() );
		// setDataAggiornamento ( aModel.getDataAggiornamento() );
		// setCodUfficioAggiornamento ( aModel.getCodUfficioAggiornamento() );
		setLuogoUdienza(aModel.getLuogoUdienza());
		setCodUfficioAppartenenza(aModel.getCodUfficioAppartenenza());
		setOraInizio(aModel.getOraInizio());
		setMinInizio(aModel.getMinInizio());
		setOraFine(aModel.getOraFine());
		setMinFine(aModel.getMinFine());
		setCodIdSezioneUdienza(aModel.getCodIdSezioneUdienza());
		setCodIdAulaUdienza(aModel.getCodIdAulaUdienza());
	}

	public void setDAOFromModelForUpdate(UdienzaSigeModel aModel) throws DAOException {
		// setIdUdienzaSige ( aModel.getIdUdienzaSige() );
		setDataUdienza(aModel.getDataUdienza());
		setCodGiudice(aModel.getCodGiudice());
		setColIdCollegio(aModel.getColIdCollegio());
		setCodProcuratore(aModel.getCodProcuratore());
		setCodIdAssistente(aModel.getCodIdAssistente());
		setNumeroMaxFascicoli(aModel.getNumeroMaxFascicoli());
		// setCodOperatoreInserimento ( aModel.getCodOperatoreInserimento() );
		// setDataInserimento ( aModel.getDataInserimento() );
		// setCodUfficioInserimento ( aModel.getCodUfficioInserimento() );
		setCodOperatoreAggiornamento(aModel.getCodOperatoreAggiornamento());
		setDataAggiornamento(aModel.getDataAggiornamento());
		setCodUfficioAggiornamento(aModel.getCodUfficioAggiornamento());
		setLuogoUdienza(aModel.getLuogoUdienza());
		// setCodUfficioAppartenenza ( aModel.getCodUfficioAppartenenza() );
		setOraInizio(aModel.getOraInizio());
		setMinInizio(aModel.getMinInizio());
		setOraFine(aModel.getOraFine());
		setMinFine(aModel.getMinFine());
		setCodIdSezioneUdienza(aModel.getCodIdSezioneUdienza());
		setCodIdAulaUdienza(aModel.getCodIdAulaUdienza());
		selCondizioneUpdate(aModel.getIdUdienzaSige());
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(UdienzaSigeModel aModel) {
		String lCondizioni = new String();

		if (aModel.getIdUdienzaSige() != null) {
			lCondizioni += " and ID_UDIENZA_SIGE = " + aModel.getIdUdienzaSige() + "";
		}
		if (aModel.getDataUdienza() != null) {
			lCondizioni += " and to_char(DATA_UDIENZA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataUdienza(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getCodGiudice() != null && aModel.getCodGiudice().length() > 0) {
			lCondizioni += " and COD_GIUDICE = '" + aModel.getCodGiudice() + "' ";
		}
		if (aModel.getColIdCollegio() != null) {
			lCondizioni += " and COL_ID_COLLEGIO = " + aModel.getColIdCollegio() + "";
		}
		if (aModel.getCodProcuratore() != null && aModel.getCodProcuratore().length() > 0) {
			lCondizioni += " and COD_PROCURATORE = '" + aModel.getCodProcuratore() + "' ";
		}
		if (aModel.getCodIdAssistente() != null) {
			lCondizioni += " and COD_ID_ASSISTENTE = " + aModel.getCodIdAssistente() + "";
		}
		if (aModel.getNumeroMaxFascicoli() != null) {
			lCondizioni += " and NUMERO_MAX_FASCICOLI = " + aModel.getNumeroMaxFascicoli() + "";
		}
		/*
		 * if (aModel.getCodOperatoreInserimento() != null && aModel.getCodOperatoreInserimento().length() >
		 * 0) { lCondizioni += " and COD_OPERATORE_INSERIMENTO = '" + aModel.getCodOperatoreInserimento() +
		 * "' "; } if (aModel.getDataInserimento() != null ) { lCondizioni +=
		 * " and to_char(DATA_INSERIMENTO,'dd/MM/yyyy') = '" +
		 * DateUtils.getDateToString(aModel.getDataInserimento(),"dd/MM/yyyy") + "' "; } if
		 * (aModel.getCodUfficioInserimento() != null && aModel.getCodUfficioInserimento().length() > 0) {
		 * lCondizioni += " and COD_UFFICIO_INSERIMENTO = '" + aModel.getCodUfficioInserimento() + "' "; } if
		 * (aModel.getCodOperatoreAggiornamento() != null && aModel.getCodOperatoreAggiornamento().length() >
		 * 0) { lCondizioni += " and COD_OPERATORE_AGGIORNAMENTO = '" + aModel.getCodOperatoreAggiornamento()
		 * + "' "; } if (aModel.getDataAggiornamento() != null ) { lCondizioni +=
		 * " and to_char(DATA_AGGIORNAMENTO,'dd/MM/yyyy') = '" +
		 * DateUtils.getDateToString(aModel.getDataAggiornamento(),"dd/MM/yyyy") + "' "; } if
		 * (aModel.getCodUfficioAggiornamento() != null && aModel.getCodUfficioAggiornamento().length() > 0) {
		 * lCondizioni += " and COD_UFFICIO_AGGIORNAMENTO = '" + aModel.getCodUfficioAggiornamento() + "' "; }
		 */
		if (aModel.getLuogoUdienza() != null && aModel.getLuogoUdienza().length() > 0) {
			lCondizioni += " and LUOGO_UDIENZA = '" + aModel.getLuogoUdienza() + "' ";
		}
		if (aModel.getCodUfficioAppartenenza() != null && aModel.getCodUfficioAppartenenza().length() > 0) {
			lCondizioni += " and COD_UFFICIO_APPARTENENZA = '" + aModel.getCodUfficioAppartenenza() + "' ";
		}
		if (aModel.getOraInizio() != null && aModel.getOraInizio().length() > 0) {
			lCondizioni += " and ORA_INIZIO = '" + aModel.getOraInizio() + "' ";
		}
		if (aModel.getMinInizio() != null && aModel.getMinInizio().length() > 0) {
			lCondizioni += " and MIN_INIZIO = '" + aModel.getMinInizio() + "' ";
		}
		if (aModel.getOraFine() != null && aModel.getOraFine().length() > 0) {
			lCondizioni += " and ORA_FINE = '" + aModel.getOraFine() + "' ";
		}
		if (aModel.getMinFine() != null && aModel.getMinFine().length() > 0) {
			lCondizioni += " and MIN_FINE = '" + aModel.getMinFine() + "' ";
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
	public void selCondizioneUpdate(BigDecimal aIdUdienzaSige) {
		String lCondizioni = new String();

		lCondizioni += " AND ID_UDIENZA_SIGE = " + aIdUdienzaSige;
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