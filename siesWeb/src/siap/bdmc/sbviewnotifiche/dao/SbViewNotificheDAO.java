package siap.bdmc.sbviewnotifiche.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.bdmc.sbviewnotifiche.model.SbViewNotificheModel;
import siap.dao.SIAPTableDAO;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: SbViewNotificheDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella SbViewNotifiche
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
public class SbViewNotificheDAO extends SIAPTableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public SbViewNotificheDAO(Connection con) {
		super(con);
		setTable("SB_VIEW_NOTIFICHE@SIES_BDMC_LINK");

		setField("PROG_NOTI", BIG_DECIMAL);
		setField("CODI_NOTI", STRING);
		setField("DESCRIZIONE", STRING);
		setField("DATA_INVI_NOTI", DATE);
		setField("DATA_REGI_NOTI", DATE);
		setField("DATA_VALI_NOTI", DATE);
		setField("NOTE", STRING);
		setField("CODI_UFFI_SIES", STRING);
		setField("CODI_UFFI", STRING);
		setField("FLAG_STAT_NOTI", STRING);
		setField("DATA_CHIU_NOTI", DATE);
		setField("FLAG_TRAS", STRING);
		setField("STOP_ANNO_FASC_BDMC", BIG_DECIMAL);
		setField("STOP_NUME_FASC_BDMC", BIG_DECIMAL);
		setField("UTEN_SIES", STRING);
		setField("ID_PREN", BIG_DECIMAL);
		setField("PROG_PERI", BIG_DECIMAL);
		setField("MODI_ANNO_FASC_BDMC", BIG_DECIMAL);
		setField("MODI_NUME_FASC_BDMC", BIG_DECIMAL);
		setField("FLAG_MODI", STRING);
		setField("DATA_INIZ", DATE);
		setField("DATA_FINE", DATE);
		setField("DATA_INIZ_PREC", DATE);
		setField("DATA_FINE_PREC", DATE);
		setField("ID_PROV_SIES", BIG_DECIMAL);
	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getProgNoti() throws DAOException {
		return getBigDecimal("PROG_NOTI");
	}

	public BigDecimal getIdProvSies() throws DAOException {
		return getBigDecimal("ID_PROV_SIES");
	}

	public String getCodiNoti() throws DAOException {
		return getString("CODI_NOTI");
	}

	public String getDescrizione() throws DAOException {
		return getString("DESCRIZIONE");
	}

	public Date getDataInviNoti() throws DAOException {
		return getDate("DATA_INVI_NOTI");
	}

	public Date getDataRegiNoti() throws DAOException {
		return getDate("DATA_REGI_NOTI");
	}

	public Date getDataValiNoti() throws DAOException {
		return getDate("DATA_VALI_NOTI");
	}

	public String getNote() throws DAOException {
		return getString("NOTE");
	}

	public String getCodiUffiSies() throws DAOException {
		return getString("CODI_UFFI_SIES");
	}

	public String getCodiUffi() throws DAOException {
		return getString("CODI_UFFI");
	}

	public String getFlagStatNoti() throws DAOException {
		return getString("FLAG_STAT_NOTI");
	}

	public Date getDataChiuNoti() throws DAOException {
		return getDate("DATA_CHIU_NOTI");
	}

	public String getFlagTras() throws DAOException {
		return getString("FLAG_TRAS");
	}

	public BigDecimal getStopAnnoFascBdmc() throws DAOException {
		return getBigDecimal("STOP_ANNO_FASC_BDMC");
	}

	public BigDecimal getStopNumeFascBdmc() throws DAOException {
		return getBigDecimal("STOP_NUME_FASC_BDMC");
	}

	public String getUtenSies() throws DAOException {
		return getString("UTEN_SIES");
	}

	public BigDecimal getIdPren() throws DAOException {
		return getBigDecimal("ID_PREN");
	}

	public BigDecimal getProgPeri() throws DAOException {
		return getBigDecimal("PROG_PERI");
	}

	public BigDecimal getModiAnnoFascBdmc() throws DAOException {
		return getBigDecimal("MODI_ANNO_FASC_BDMC");
	}

	public BigDecimal getModiNumeFascBdmc() throws DAOException {
		return getBigDecimal("MODI_NUME_FASC_BDMC");
	}

	public String getFlagModi() throws DAOException {
		return getString("FLAG_MODI");
	}

	public Date getDataIniz() throws DAOException {
		return getDate("DATA_INIZ");
	}

	public Date getDataFine() throws DAOException {
		return getDate("DATA_FINE");
	}

	public Date getDataInizPrec() throws DAOException {
		return getDate("DATA_INIZ_PREC");
	}

	public Date getDataFinePrec() throws DAOException {
		return getDate("DATA_FINE_PREC");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setProgNoti(BigDecimal aValore) {
		setBigDecimal("PROG_NOTI", aValore);
	}

	public void setIdProvSies(BigDecimal aValore) {
		setBigDecimal("ID_PROV_SIES", aValore);
	}

	public void setCodiNoti(String aValore) {
		setString("CODI_NOTI", aValore);
	}

	public void setDescrizione(String aValore) {
		setString("DESCRIZIONE", aValore);
	}

	public void setDataInviNoti(Date aValore) {
		setDate("DATA_INVI_NOTI", aValore);
	}

	public void setDataRegiNoti(Date aValore) {
		setDate("DATA_REGI_NOTI", aValore);
	}

	public void setDataValiNoti(Date aValore) {
		setDate("DATA_VALI_NOTI", aValore);
	}

	public void setNote(String aValore) {
		setString("NOTE", aValore);
	}

	public void setCodiUffiSies(String aValore) {
		setString("CODI_UFFI_SIES", aValore);
	}

	public void setCodiUffi(String aValore) {
		setString("CODI_UFFI", aValore);
	}

	public void setFlagStatNoti(String aValore) {
		setString("FLAG_STAT_NOTI", aValore);
	}

	public void setDataChiuNoti(Date aValore) {
		setDate("DATA_CHIU_NOTI", aValore);
	}

	public void setFlagTras(String aValore) {
		setString("FLAG_TRAS", aValore);
	}

	public void setStopAnnoFascBdmc(BigDecimal aValore) {
		setBigDecimal("STOP_ANNO_FASC_BDMC", aValore);
	}

	public void setStopNumeFascBdmc(BigDecimal aValore) {
		setBigDecimal("STOP_NUME_FASC_BDMC", aValore);
	}

	public void setUtenSies(String aValore) {
		setString("UTEN_SIES", aValore);
	}

	public void setIdPren(BigDecimal aValore) {
		setBigDecimal("ID_PREN", aValore);
	}

	public void setProgPeri(BigDecimal aValore) {
		setBigDecimal("PROG_PERI", aValore);
	}

	public void setModiAnnoFascBdmc(BigDecimal aValore) {
		setBigDecimal("MODI_ANNO_FASC_BDMC", aValore);
	}

	public void setModiNumeFascBdmc(BigDecimal aValore) {
		setBigDecimal("MODI_NUME_FASC_BDMC", aValore);
	}

	public void setFlagModi(String aValore) {
		setString("FLAG_MODI", aValore);
	}

	public void setDataIniz(Date aValore) {
		setDate("DATA_INIZ", aValore);
	}

	public void setDataFine(Date aValore) {
		setDate("DATA_FINE", aValore);
	}

	public void setDataInizPrec(Date aValore) {
		setDate("DATA_INIZ_PREC", aValore);
	}

	public void setDataFinePrec(Date aValore) {
		setDate("DATA_FINE_PREC", aValore);
	}

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new SbViewNotificheModel(getProgNoti(), getCodiNoti(), "", getDescrizione(),
				getDataInviNoti(), getDataRegiNoti(), getDataValiNoti(), getNote(), getCodiUffiSies(), "",
				getCodiUffi(), "", getFlagStatNoti(), getDataChiuNoti(), getFlagTras(),
				getStopAnnoFascBdmc(), getStopNumeFascBdmc(), getUtenSies(), getIdPren(), getProgPeri(),
				getModiAnnoFascBdmc(), getModiNumeFascBdmc(), getFlagModi(), getDataIniz(), getDataFine(),
				getDataInizPrec(), getDataFinePrec(), getIdProvSies());
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(SbViewNotificheModel aModel) throws DAOException {
		setProgNoti(aModel.getProgNoti());
		setIdProvSies(aModel.getIdProvSies());
		setCodiNoti(aModel.getCodiNoti());
		setDescrizione(aModel.getDescrizione());
		setDataInviNoti(aModel.getDataInviNoti());
		setDataRegiNoti(aModel.getDataRegiNoti());
		setDataValiNoti(aModel.getDataValiNoti());
		setNote(aModel.getNote());
		setCodiUffiSies(aModel.getCodiUffiSies());
		setCodiUffi(aModel.getCodiUffi());
		setFlagStatNoti(aModel.getFlagStatNoti());
		setDataChiuNoti(aModel.getDataChiuNoti());
		setFlagTras(aModel.getFlagTras());
		setStopAnnoFascBdmc(aModel.getStopAnnoFascBdmc());
		setStopNumeFascBdmc(aModel.getStopNumeFascBdmc());
		setUtenSies(aModel.getUtenSies());
		setIdPren(aModel.getIdPren());
		setProgPeri(aModel.getProgPeri());
		setModiAnnoFascBdmc(aModel.getModiAnnoFascBdmc());
		setModiNumeFascBdmc(aModel.getModiNumeFascBdmc());
		setFlagModi(aModel.getFlagModi());
		setDataIniz(aModel.getDataIniz());
		setDataFine(aModel.getDataFine());
		setDataInizPrec(aModel.getDataInizPrec());
		setDataFinePrec(aModel.getDataFinePrec());
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(SbViewNotificheModel aModel) {
		String lCondizioni = new String();

		if (aModel.getProgNoti() != null) {
			lCondizioni += " and PROG_NOTI = " + aModel.getProgNoti() + "";
		}
		if (aModel.getCodiNoti() != null && aModel.getCodiNoti().length() > 0) {
			lCondizioni += " and CODI_NOTI = '" + aModel.getCodiNoti() + "' ";
		}
		if (aModel.getDescrizione() != null && aModel.getDescrizione().length() > 0) {
			lCondizioni += " and DESCRIZIONE = '" + aModel.getDescrizione() + "' ";
		}
		if (aModel.getDataInviNoti() != null) {
			lCondizioni += " and to_char(DATA_INVI_NOTI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInviNoti(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataRegiNoti() != null) {
			lCondizioni += " and to_char(DATA_REGI_NOTI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataRegiNoti(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataValiNoti() != null) {
			lCondizioni += " and to_char(DATA_VALI_NOTI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataValiNoti(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getNote() != null && aModel.getNote().length() > 0) {
			lCondizioni += " and NOTE = '" + aModel.getNote() + "' ";
		}
		if (aModel.getCodiUffiSies() != null && aModel.getCodiUffiSies().length() > 0) {
			lCondizioni += " and (CODI_UFFI_SIES = '" + aModel.getCodiUffiSies()
					+ "'or CODI_UFFI_SIES = 'ALL' )";
		}
		if (aModel.getCodiUffi() != null && aModel.getCodiUffi().length() > 0) {
			lCondizioni += " and CODI_UFFI = '" + aModel.getCodiUffi() + "' ";
		}
		if (aModel.getFlagStatNoti() != null && aModel.getFlagStatNoti().length() > 0) {
			lCondizioni += " and FLAG_STAT_NOTI = '" + aModel.getFlagStatNoti() + "' ";
		}
		if (aModel.getDataChiuNoti() != null) {
			lCondizioni += " and to_char(DATA_CHIU_NOTI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataChiuNoti(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getFlagTras() != null) {
			lCondizioni += " and FLAG_TRAS = '" + aModel.getFlagTras() + "'";
		}
		if (aModel.getStopAnnoFascBdmc() != null) {
			lCondizioni += " and STOP_ANNO_FASC_BDMC = " + aModel.getStopAnnoFascBdmc() + "";
		}
		if (aModel.getStopNumeFascBdmc() != null) {
			lCondizioni += " and STOP_NUME_FASC_BDMC = " + aModel.getStopNumeFascBdmc() + "";
		}
		if (aModel.getUtenSies() != null && aModel.getUtenSies().length() > 0) {
			lCondizioni += " and UTEN_SIES = '" + aModel.getUtenSies() + "' ";
		}
		if (aModel.getIdPren() != null) {
			lCondizioni += " and ID_PREN = " + aModel.getIdPren() + "";
		}
		if (aModel.getProgPeri() != null) {
			lCondizioni += " and PROG_PERI = " + aModel.getProgPeri() + "";
		}
		if (aModel.getModiAnnoFascBdmc() != null) {
			lCondizioni += " and MODI_ANNO_FASC_BDMC = " + aModel.getModiAnnoFascBdmc() + "";
		}
		if (aModel.getModiNumeFascBdmc() != null) {
			lCondizioni += " and MODI_NUME_FASC_BDMC = " + aModel.getModiNumeFascBdmc() + "";
		}
		if (aModel.getFlagModi() != null && aModel.getFlagModi().length() > 0) {
			lCondizioni += " and FLAG_MODI = '" + aModel.getFlagModi() + "' ";
		}
		if (aModel.getDataIniz() != null) {
			lCondizioni += " and to_char(DATA_INIZ,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataIniz(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataFine() != null) {
			lCondizioni += " and to_char(DATA_FINE,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataFine(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataInizPrec() != null) {
			lCondizioni += " and to_char(DATA_INIZ_PREC,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInizPrec(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataFinePrec() != null) {
			lCondizioni += " and to_char(DATA_FINE_PREC,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataFinePrec(), "dd/MM/yyyy") + "' ";
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
	public void selCondizioneUpdate(BigDecimal aProgNoti) {
		String lCondizioni = new String();

		lCondizioni += " and PROG_NOTI = " + aProgNoti;
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