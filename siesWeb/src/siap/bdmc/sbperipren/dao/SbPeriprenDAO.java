package siap.bdmc.sbperipren.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.bdmc.sbperipren.model.SbPeriprenModel;
import siap.dao.SIAPTableDAO;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: SbPeriprenDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella SbPeripren
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
public class SbPeriprenDAO extends SIAPTableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public SbPeriprenDAO(Connection con) {
		super(con);
		setTable("SB_PERIPREN@SIES_BDMC_LINK");

		// Settare la Sequence e i campi chiave e commentare il setField del campo chiave
		setSequenceField("PROG_PERI_PREN", "SEQ_SB_PERIPREN");

		setField("DATA_INIZ_PERI", DATE);
		setField("DATA_FINE_PERI", DATE);
		// setField("PROG_PERI_PRES", BIG_DECIMAL);
		setField("ID_PREN", BIG_DECIMAL);
		setField("CODI_UFFI_SIES", STRING);
		setField("ANNO_FASC_SIEP", BIG_DECIMAL);
		setField("NUME_FASC_SIEP", BIG_DECIMAL);
		setField("CODI_SEDE_INST", STRING);
		setField("ANNO_FASC_BDMC", BIG_DECIMAL);
		setField("NUME_FASC_BDMC", BIG_DECIMAL);
		setField("CODI_STAT_PREN_PERI", STRING);
		// in disuso setField("COD_TIPO_PERI" , STRING);
		setField("DATA_PREN_PERI", DATE);
		setField("DESC_PERI", STRING);
		setField("DATA_MODI_PREN_PERI", DATE);
	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public Date getDataInizPeri() throws DAOException {
		return getDate("DATA_INIZ_PERI");
	}

	public Date getDataFinePeri() throws DAOException { // return getDate ("DATA_FINE_PERI" ); }
		Date lDataFinePeri = getDate("DATA_FINE_PERI");
		if (lDataFinePeri == null) {
			lDataFinePeri = DateUtils.getSysDate();
		}
		return lDataFinePeri;
	}

	public BigDecimal getProgPeriPres() throws DAOException {
		return getBigDecimal("PROG_PERI_PREN");
	}

	public BigDecimal getIdPren() throws DAOException {
		return getBigDecimal("ID_PREN");
	}

	public String getCodiUffiSies() throws DAOException {
		return getString("CODI_UFFI_SIES");
	}

	public BigDecimal getAnnoFascSiep() throws DAOException {
		return getBigDecimal("ANNO_FASC_SIEP");
	}

	public BigDecimal getNumeFascSiep() throws DAOException {
		return getBigDecimal("NUME_FASC_SIEP");
	}

	public String getCodiSedeInst() throws DAOException {
		return getString("CODI_SEDE_INST");
	}

	public BigDecimal getAnnoFascBdmc() throws DAOException {
		return getBigDecimal("ANNO_FASC_BDMC");
	}

	public BigDecimal getNumeFascBdmc() throws DAOException {
		return getBigDecimal("NUME_FASC_BDMC");
	}

	public String getCodStatPrenPeri() throws DAOException {
		return getString("CODI_STAT_PREN_PERI");
	}

	// public String getCodTipoPeri() throws DAOException { return getString ("COD_TIPO_PERI" ); }
	public Date getDataPrenPeri() throws DAOException {
		return getDate("DATA_PREN_PERI");
	}

	public String getDescPeri() throws DAOException {
		return getString("DESC_PERI");
	}

	public Date getDataModiPrenPeri() throws DAOException {
		return getDate("DATA_MODI_PREN_PERI");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setDataInizPeri(Date aValore) {
		setDate("DATA_INIZ_PERI", aValore);
	}

	public void setDataFinePeri(Date aValore) {
		setDate("DATA_FINE_PERI", aValore);
	}

	public void setProgPeriPres(BigDecimal aValore) {
		setBigDecimal("PROG_PERI_PREN", aValore);
	}

	public void setIdPren(BigDecimal aValore) {
		setBigDecimal("ID_PREN", aValore);
	}

	public void setCodiUffiSies(String aValore) {
		setString("CODI_UFFI_SIES", aValore);
	}

	public void setAnnoFascSiep(BigDecimal aValore) {
		setBigDecimal("ANNO_FASC_SIEP", aValore);
	}

	public void setNumeFascSiep(BigDecimal aValore) {
		setBigDecimal("NUME_FASC_SIEP", aValore);
	}

	public void setCodiSedeInst(String aValore) {
		setString("CODI_SEDE_INST", aValore);
	}

	public void setAnnoFascBdmc(BigDecimal aValore) {
		setBigDecimal("ANNO_FASC_BDMC", aValore);
	}

	public void setNumeFascBdmc(BigDecimal aValore) {
		setBigDecimal("NUME_FASC_BDMC", aValore);
	}

	public void setCodStatPrenPeri(String aValore) {
		setString("CODI_STAT_PREN_PERI", aValore);
	}

	// public void setCodTipoPeri (String aValore ) { setString ("COD_TIPO_PERI" , aValore); }
	public void setDataPrenPeri(Date aValore) {
		setDate("DATA_PREN_PERI", aValore);
	}

	public void setDescPeri(String aValore) {
		setString("DESC_PERI", aValore);
	}

	public void setDataModiPrenPeri(Date aValore) {
		setDate("DATA_MODI_PREN_PERI", aValore);
	}

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new SbPeriprenModel(getDataInizPeri(), getDataFinePeri(), getProgPeriPres(), getIdPren(),
				getCodiUffiSies(), "", getAnnoFascSiep(), getNumeFascSiep(), getCodiSedeInst(), "",
				getAnnoFascBdmc(), getNumeFascBdmc(), getCodStatPrenPeri(), "",
				// getCodTipoPeri() ,
				"", getDataPrenPeri(), getDescPeri(), getDataModiPrenPeri());
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(SbPeriprenModel aModel) throws DAOException {
		setDataInizPeri(aModel.getDataInizPeri());
		setDataFinePeri(aModel.getDataFinePeri());
		setProgPeriPres(aModel.getProgPeriPres());
		setIdPren(aModel.getIdPren());
		setCodiUffiSies(aModel.getCodiUffiSies());
		setAnnoFascSiep(aModel.getAnnoFascSiep());
		setNumeFascSiep(aModel.getNumeFascSiep());
		setCodiSedeInst(aModel.getCodiSedeInst());
		setAnnoFascBdmc(aModel.getAnnoFascBdmc());
		setNumeFascBdmc(aModel.getNumeFascBdmc());
		setCodStatPrenPeri(aModel.getCodStatPrenPeri());
		// setCodTipoPeri ( aModel.getCodTipoPeri() );
		setDataPrenPeri(aModel.getDataPrenPeri());
		setDescPeri(aModel.getDescPeri());
		setDataModiPrenPeri(aModel.getDataModiPrenPeri());
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(SbPeriprenModel aModel) {
		String lCondizioni = new String();

		if (aModel.getDataInizPeri() != null) {
			lCondizioni += " and to_char(DATA_INIZ_PERI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataInizPeri(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataFinePeri() != null) {
			lCondizioni += " and to_char(DATA_FINE_PERI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataFinePeri(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getProgPeriPres() != null) {
			lCondizioni += " and PROG_PERI_PREN = " + aModel.getProgPeriPres() + "";
		}
		if (aModel.getIdPren() != null) {
			lCondizioni += " and ID_PREN = " + aModel.getIdPren() + "";
		}
		if (aModel.getCodiUffiSies() != null && aModel.getCodiUffiSies().length() > 0) {
			lCondizioni += " and CODI_UFFI_SIES = '" + aModel.getCodiUffiSies() + "' ";
		}
		if (aModel.getAnnoFascSiep() != null) {
			lCondizioni += " and ANNO_FASC_SIEP = " + aModel.getAnnoFascSiep() + "";
		}
		if (aModel.getNumeFascSiep() != null) {
			lCondizioni += " and NUME_FASC_SIEP = " + aModel.getNumeFascSiep() + "";
		}
		if (aModel.getCodiSedeInst() != null && aModel.getCodiSedeInst().length() > 0) {
			lCondizioni += " and CODI_SEDE_INST = '" + aModel.getCodiSedeInst() + "' ";
		}
		if (aModel.getAnnoFascBdmc() != null) {
			lCondizioni += " and ANNO_FASC_BDMC = " + aModel.getAnnoFascBdmc() + "";
		}
		if (aModel.getNumeFascBdmc() != null) {
			lCondizioni += " and NUME_FASC_BDMC = " + aModel.getNumeFascBdmc() + "";
		}
		if (aModel.getCodStatPrenPeri() != null && aModel.getCodStatPrenPeri().length() > 0) {
			lCondizioni += " and CODI_STAT_PREN_PERI = '" + aModel.getCodStatPrenPeri() + "' ";
		}
		/*
		 * if (aModel.getCodTipoPeri() != null && aModel.getCodTipoPeri().length() > 0) { lCondizioni +=
		 * " and COD_TIPO_PERI = '" + aModel.getCodTipoPeri() + "' "; }
		 */
		if (aModel.getDataPrenPeri() != null) {
			lCondizioni += " and to_char(DATA_PREN_PERI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataPrenPeri(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDescPeri() != null && aModel.getDescPeri().length() > 0) {
			lCondizioni += " and DESC_PERI = '" + aModel.getDescPeri() + "' ";
		}
		if (aModel.getDataModiPrenPeri() != null) {
			lCondizioni += " and to_char(DATA_MODI_PREN_PERI,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataModiPrenPeri(), "dd/MM/yyyy") + "' ";
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
	public void selCondizioneUpdate(BigDecimal aIdPren) {
		String lCondizioni = new String();

		lCondizioni += " and ID_PREN = " + aIdPren;
		// Elimino il primo and
		if (lCondizioni.length() > 0) {
			lCondizioni = lCondizioni.substring(4);
		}

		setCondition(lCondizioni);
	}

	/*****************************************************************************
	 * Imposta la condizione di where per l'operazione di delete puntuale si entra sempre in chiave
	 * 
	 * @param key
	 ****************************************************************************/
	public void selCondizioneDelete(BigDecimal aIdPren) {
		setCondition(" ID_PREN = '" + aIdPren + "'");
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