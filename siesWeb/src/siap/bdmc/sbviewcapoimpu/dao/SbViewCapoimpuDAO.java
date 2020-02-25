package siap.bdmc.sbviewcapoimpu.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel;
import siap.dao.SIAPTableDAO;
import siap.sico.ufficio.controller.UfficioUtils;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: SbViewCapoimpuDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella SbViewCapoimpu
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
public class SbViewCapoimpuDAO extends SIAPTableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public SbViewCapoimpuDAO(Connection con) {
		super(con);
		setTable("SB_VIEW_CAPOIMPU@SIES_BDMC_LINK");

		setField("FLAG_ARTI_0056", STRING);
		setField("FLAG_ARTI_0061", STRING);
		setField("ARTI_0061_COMM", STRING);
		setField("FLAG_ARTI_0081", STRING);
		setField("ARTI_0081_COMM", STRING);
		setField("FLAG_ARTI_0110", STRING);
		setField("FLAG_ARTI_0112", STRING);
		setField("ARTI_0112_COMM", STRING);
		setField("FLAG_ARTI_0113", STRING);
		setField("FLAG_ARTI_0114", STRING);
		setField("FLAG_ARTI_0116", STRING);
		setField("FLAG_ARTI_0117", STRING);
		setField("LUOG_REAT", STRING);
		setField("FLAG_PERI_TEMP", STRING);
		setField("DATA_REAT_0101", DATE);
		setField("DATA_REAT_0202", DATE);
		setField("DESC_PERI_TEMP", STRING);
		setField("NUME_PROG_CAPO_IMPU", BIG_DECIMAL);
		setField("ID_PREN", BIG_DECIMAL);
		setField("ANNO_FASC_BDMC", BIG_DECIMAL);
		setField("NUME_FASC_BDMC", BIG_DECIMAL);
		setField("CODI_SEDE_INST", STRING);
	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public String getFlagArti0056() throws DAOException {
		return getString("FLAG_ARTI_0056");
	}

	public String getFlagArti0061() throws DAOException {
		return getString("FLAG_ARTI_0061");
	}

	public String getArti0061Comm() throws DAOException {
		return getString("ARTI_0061_COMM");
	}

	public String getFlagArti0081() throws DAOException {
		return getString("FLAG_ARTI_0081");
	}

	public String getArti0081Comm() throws DAOException {
		return getString("ARTI_0081_COMM");
	}

	public String getFlagArt0110() throws DAOException {
		return getString("FLAG_ARTI_0110");
	}

	public String getFlagArti0112() throws DAOException {
		return getString("FLAG_ARTI_0112");
	}

	public String getArti0112Commi() throws DAOException {
		return getString("ARTI_0112_COMM");
	}

	public String getFlagArti0113() throws DAOException {
		return getString("FLAG_ARTI_0113");
	}

	public String getFlagArti0114() throws DAOException {
		return getString("FLAG_ARTI_0114");
	}

	public String getFlagArti0116() throws DAOException {
		return getString("FLAG_ARTI_0116");
	}

	public String getFlagArti0117() throws DAOException {
		return getString("FLAG_ARTI_0117");
	}

	public String getLuogReat() throws DAOException {
		return getString("LUOG_REAT");
	}

	public String getFlagPeriTemp() throws DAOException {
		return getString("FLAG_PERI_TEMP");
	}

	public Date getDataReat0101() throws DAOException {
		return getDate("DATA_REAT_0101");
	}

	public Date getDataReat0202() throws DAOException {
		return getDate("DATA_REAT_0202");
	}

	public String getDescPeriTemp() throws DAOException {
		return getString("DESC_PERI_TEMP");
	}

	public BigDecimal getNumeProgCapoImpu() throws DAOException {
		return getBigDecimal("NUME_PROG_CAPO_IMPU");
	}

	public BigDecimal getIdPren() throws DAOException {
		return getBigDecimal("ID_PREN");
	}

	public BigDecimal getAnnoFascBdmc() throws DAOException {
		return getBigDecimal("ANNO_FASC_BDMC");
	}

	public BigDecimal getNumeFascBdmc() throws DAOException {
		return getBigDecimal("NUME_FASC_BDMC");
	}

	public String getCodiSedeInst() throws DAOException {
		return getString("CODI_SEDE_INST");
	}

	public String getDescSedeInst() throws DAOException {
		try {
			return UfficioUtils.getDescTipoUffByCodUfficio(getString("CODI_SEDE_INST"));
		} catch (F3BException e) {
			e.printStackTrace();
		}
		return "";
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setFlagArti0056(String aValore) {
		setString("FLAG_ARTI_0056", aValore);
	}

	public void setFlagArti0061(String aValore) {
		setString("FLAG_ARTI_0061", aValore);
	}

	public void setArti0061Comm(String aValore) {
		setString("ARTI_0061_COMM", aValore);
	}

	public void setFlagArti0081(String aValore) {
		setString("FLAG_ARTI_0081", aValore);
	}

	public void setArti0081Comm(String aValore) {
		setString("ARTI_0081_COMM", aValore);
	}

	public void setFlagArt0110(String aValore) {
		setString("FLAG_ARTI_0110", aValore);
	}

	public void setFlagArti0112(String aValore) {
		setString("FLAG_ARTI_0112", aValore);
	}

	public void setArti0112Commi(String aValore) {
		setString("ARTI_0112_COMM", aValore);
	}

	public void setFlagArti0113(String aValore) {
		setString("FLAG_ARTI_0113", aValore);
	}

	public void setFlagArti0114(String aValore) {
		setString("FLAG_ARTI_0114", aValore);
	}

	public void setFlagArti0116(String aValore) {
		setString("FLAG_ARTI_0116", aValore);
	}

	public void setFlagArti0117(String aValore) {
		setString("FLAG_ARTI_0117", aValore);
	}

	public void setLuogReat(String aValore) {
		setString("LUOG_REAT", aValore);
	}

	public void setFlagPeriTemp(String aValore) {
		setString("FLAG_PERI_TEMP", aValore);
	}

	public void setDataReat0101(Date aValore) {
		setDate("DATA_REAT_0101", aValore);
	}

	public void setDataReat0202(Date aValore) {
		setDate("DATA_REAT_0202", aValore);
	}

	public void setDescPeriTemp(String aValore) {
		setString("DESC_PERI_TEMP", aValore);
	}

	public void setNumeProgCapoImpu(BigDecimal aValore) {
		setBigDecimal("NUME_PROG_CAPO_IMPU", aValore);
	}

	public void setIdPren(BigDecimal aValore) {
		setBigDecimal("ID_PREN", aValore);
	}

	public void setAnnoFascBdmc(BigDecimal aValore) {
		setBigDecimal("ANNO_FASC_BDMC", aValore);
	}

	public void setNumeFascBdmc(BigDecimal aValore) {
		setBigDecimal("NUME_FASC_BDMC", aValore);
	}

	public void setCodiSedeInst(String aValore) {
		setString("CODI_SEDE_INST", aValore);
	}

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new SbViewCapoimpuModel(getFlagArti0056(), getFlagArti0061(), getArti0061Comm(),
				getFlagArti0081(), getArti0081Comm(), getFlagArt0110(), getFlagArti0112(),
				getArti0112Commi(), getFlagArti0113(), getFlagArti0114(), getFlagArti0116(),
				getFlagArti0117(), getLuogReat(), getFlagPeriTemp(), getDataReat0101(), getDataReat0202(),
				getDescPeriTemp(), getNumeProgCapoImpu(), getIdPren(), getAnnoFascBdmc(), getNumeFascBdmc(),
				getCodiSedeInst(), getDescSedeInst());
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(SbViewCapoimpuModel aModel) throws DAOException {
		setFlagArti0056(aModel.getFlagArti0056());
		setFlagArti0061(aModel.getFlagArti0061());
		setArti0061Comm(aModel.getArti0061Comm());
		setFlagArti0081(aModel.getFlagArti0081());
		setArti0081Comm(aModel.getArti0081Comm());
		setFlagArt0110(aModel.getFlagArt0110());
		setFlagArti0112(aModel.getFlagArti0112());
		setArti0112Commi(aModel.getArti0112Commi());
		setFlagArti0113(aModel.getFlagArti0113());
		setFlagArti0114(aModel.getFlagArti0114());
		setFlagArti0116(aModel.getFlagArti0116());
		setFlagArti0117(aModel.getFlagArti0117());
		setLuogReat(aModel.getLuogReat());
		setFlagPeriTemp(aModel.getFlagPeriTemp());
		setDataReat0101(aModel.getDataReat0101());
		setDataReat0202(aModel.getDataReat0202());
		setDescPeriTemp(aModel.getDescPeriTemp());
		setNumeProgCapoImpu(aModel.getNumeProgCapoImpu());
		setIdPren(aModel.getIdPren());
		setAnnoFascBdmc(aModel.getAnnoFascBdmc());
		setNumeFascBdmc(aModel.getNumeFascBdmc());
		setCodiSedeInst(aModel.getCodiSedeInst());
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(SbViewCapoimpuModel aModel) {
		String lCondizioni = new String();

		if (aModel.getFlagArti0056() != null && aModel.getFlagArti0056().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0056 = '" + aModel.getFlagArti0056() + "' ";
		}
		if (aModel.getFlagArti0061() != null && aModel.getFlagArti0061().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0061 = '" + aModel.getFlagArti0061() + "' ";
		}
		if (aModel.getArti0061Comm() != null && aModel.getArti0061Comm().length() > 0) {
			lCondizioni += " and ARTI_0061_COMM = '" + aModel.getArti0061Comm() + "' ";
		}
		if (aModel.getFlagArti0081() != null && aModel.getFlagArti0081().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0081 = '" + aModel.getFlagArti0081() + "' ";
		}
		if (aModel.getArti0081Comm() != null && aModel.getArti0081Comm().length() > 0) {
			lCondizioni += " and ARTI_0081_COMM = '" + aModel.getArti0081Comm() + "' ";
		}
		if (aModel.getFlagArt0110() != null && aModel.getFlagArt0110().length() > 0) {
			lCondizioni += " and FLAG_ART_0110 = '" + aModel.getFlagArt0110() + "' ";
		}
		if (aModel.getFlagArti0112() != null && aModel.getFlagArti0112().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0112 = '" + aModel.getFlagArti0112() + "' ";
		}
		if (aModel.getArti0112Commi() != null && aModel.getArti0112Commi().length() > 0) {
			lCondizioni += " and ARTI_0112_COMMI = '" + aModel.getArti0112Commi() + "' ";
		}
		if (aModel.getFlagArti0113() != null && aModel.getFlagArti0113().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0113 = '" + aModel.getFlagArti0113() + "' ";
		}
		if (aModel.getFlagArti0114() != null && aModel.getFlagArti0114().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0114 = '" + aModel.getFlagArti0114() + "' ";
		}
		if (aModel.getFlagArti0116() != null && aModel.getFlagArti0116().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0116 = '" + aModel.getFlagArti0116() + "' ";
		}
		if (aModel.getFlagArti0117() != null && aModel.getFlagArti0117().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0117 = '" + aModel.getFlagArti0117() + "' ";
		}
		if (aModel.getLuogReat() != null && aModel.getLuogReat().length() > 0) {
			lCondizioni += " and LUOG_REAT = '" + aModel.getLuogReat() + "' ";
		}
		if (aModel.getFlagPeriTemp() != null && aModel.getFlagPeriTemp().length() > 0) {
			lCondizioni += " and FLAG_PERI_TEMP = '" + aModel.getFlagPeriTemp() + "' ";
		}
		if (aModel.getDataReat0101() != null) {
			lCondizioni += " and to_char(DATA_REAT_0101,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataReat0101(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDataReat0202() != null) {
			lCondizioni += " and to_char(DATA_REAT_0202,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataReat0202(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getDescPeriTemp() != null && aModel.getDescPeriTemp().length() > 0) {
			lCondizioni += " and DESC_PERI_TEMP = '" + aModel.getDescPeriTemp() + "' ";
		}
		if (aModel.getNumeProgCapoImpu() != null) {
			lCondizioni += " and NUME_PROG_CAPO_IMPU = " + aModel.getNumeProgCapoImpu() + "";
		}
		if (aModel.getIdPren() != null) {
			lCondizioni += " and ID_PREN = " + aModel.getIdPren() + "";
		}
		if (aModel.getAnnoFascBdmc() != null) {
			lCondizioni += " and ANNO_FASC_BDMC = " + aModel.getAnnoFascBdmc() + "";
		}
		if (aModel.getNumeFascBdmc() != null) {
			lCondizioni += " and NUME_FASC_BDMC = " + aModel.getNumeFascBdmc() + "";
		}
		if (aModel.getCodiSedeInst() != null && aModel.getCodiSedeInst().length() > 0) {
			lCondizioni += " and CODI_SEDE_INST = '" + aModel.getCodiSedeInst() + "' ";
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
	public void selCondizioneUpdate(BigDecimal aIdPren, BigDecimal aNumeProgCapoImpu) {
		String lCondizioni = new String();

		lCondizioni += " and ID_PREN = " + aIdPren;
		lCondizioni += " and NUME_PROG_CAPO_IMPU = " + aNumeProgCapoImpu;
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