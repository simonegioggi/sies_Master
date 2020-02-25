package siap.bdmc.sbpren.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.bdmc.sbpren.model.SbPrenModel;
import siap.dao.SIAPTableDAO;
import siap.sico.ufficio.controller.UfficioUtils;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: SbPrenDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella SbPren
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
public class SbPrenDAO extends SIAPTableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public SbPrenDAO(Connection con) {
		super(con);
		setTable("SB_PREN@SIES_BDMC_LINK");

		// Settare la Sequence e i campi chiave e commentare il setField del campo chiave
		// setSequenceField("ID_PREN","PREN_SEQ");

		setField("DATA_PREN", DATE);
		setField("ID_PREN", BIG_DECIMAL);
		setField("UTEN_SIES", STRING);
		setField("NOTE", STRING);
		setField("DATA_ANNU", DATE);
		setField("MOTI_ANNU", STRING);
		// flag_pren diventa codi_stat_pren
		setField("CODI_STAT_PREN", BIG_DECIMAL);
		setField("FLAG_SELE_CAPO_IMPU", STRING);
		setField("FLAG_SELE_PROC_PENA", STRING);
		// Int. bdmc non c'è setField("FLAG_SELE_SENT" , BIG_DECIMAL);
		setField("FLAG_PREN_PRES", STRING);
		setField("CODI_UFFI_SIES", STRING);
		setField("FLAG_SELE_PERI_COMP", STRING);
		setField("NUME_FASC_BDMC", BIG_DECIMAL);
		setField("ANNO_FASC_BDMC", BIG_DECIMAL);
		setField("CODI_SEDE_INST", STRING);
		setField("COGN_SOGG", STRING);
		setField("NOME_SOGG", STRING);
		setField("FLAG_SESS", STRING);
		setField("CODI_STAT", STRING);
		setField("LUOG_NASC", STRING);
		setField("CODI_IDEN_AFIS", STRING);
		setField("DATA_NASC", DATE);
		setField("FLAG_SELE_CIRC_SOGG", STRING);
	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public Date getDataPren() throws DAOException {
		return getDate("DATA_PREN");
	}

	public BigDecimal getIdPren() throws DAOException {
		return getBigDecimal("ID_PREN");
	}

	public String getUtenSies() throws DAOException {
		return getString("UTEN_SIES");
	}

	public String getNote() throws DAOException {
		return getString("NOTE");
	}

	public Date getDataAnnu() throws DAOException {
		return getDate("DATA_ANNU");
	}

	public String getMotiAnnu() throws DAOException {
		return getString("MOTI_ANNU");
	}

	public BigDecimal getCodiStatPren() throws DAOException {
		return getBigDecimal("CODI_STAT_PREN");
	}

	public String getFlagSeleCapoImpu() throws DAOException {
		return getString("FLAG_SELE_CAPO_IMPU");
	}

	public String getFlagSeleProcPena() throws DAOException {
		return getString("FLAG_SELE_PROC_PENA");
	}

	// int. bdmc public BigDecimal getFlagSeleSent() throws DAOException { return getBigDecimal
	// ("FLAG_SELE_SENT" ); }
	public String getFlagPrenPres() throws DAOException {
		return getString("FLAG_PREN_PRES");
	}

	public String getCodiUffiSies() throws DAOException {
		return getString("CODI_UFFI_SIES");
	}

	public String getFlagSelePeriComp() throws DAOException {
		return getString("FLAG_SELE_PERI_COMP");
	}

	public BigDecimal getNumeFascBdmc() throws DAOException {
		return getBigDecimal("NUME_FASC_BDMC");
	}

	public BigDecimal getAnnoFascBdmc() throws DAOException {
		return getBigDecimal("ANNO_FASC_BDMC");
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

	public String getCognSogg() throws DAOException {
		return getString("COGN_SOGG");
	}

	public String getNomeSogg() throws DAOException {
		return getString("NOME_SOGG");
	}

	public String getFlagSess() throws DAOException {
		return getString("FLAG_SESS");
	}

	public String getCodiStat() throws DAOException {
		return getString("CODI_STAT");
	}

	public String getLuogNasc() throws DAOException {
		return getString("LUOG_NASC");
	}

	public String getCodiIdenAfis() throws DAOException {
		return getString("CODI_IDEN_AFIS");
	}

	public Date getDataNasc() throws DAOException {
		return getDate("DATA_NASC");
	}

	public String getFlagSeleCircSogg() throws DAOException {
		return getString("FLAG_SELE_CIRC_SOGG");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setDataPren(Date aValore) {
		setDate("DATA_PREN", aValore);
	}

	public void setIdPren(BigDecimal aValore) {
		setBigDecimal("ID_PREN", aValore);
	}

	public void setUtenSies(String aValore) {
		setString("UTEN_SIES", aValore);
	}

	public void setNote(String aValore) {
		setString("NOTE", aValore);
	}

	public void setDataAnnu(Date aValore) {
		setDate("DATA_ANNU", aValore);
	}

	public void setMotiAnnu(String aValore) {
		setString("MOTI_ANNU", aValore);
	}

	public void setCodiStatPren(BigDecimal aValore) {
		setBigDecimal("CODI_STAT_PRE", aValore);
	}

	public void setFlagSeleCapoImpu(String aValore) {
		setString("FLAG_SELE_CAPO_IMPU", aValore);
	}

	public void setFlagSeleProcPena(String aValore) {
		setString("FLAG_SELE_PROC_PENA", aValore);
	}

	// int. bdmc public void setFlagSeleSent (BigDecimal aValore ) { setBigDecimal ("FLAG_SELE_SENT" ,
	// aValore); }
	public void setFlagPrenPres(String aValore) {
		setString("FLAG_PREN_PRES", aValore);
	}

	public void setCodiUffiSies(String aValore) {
		setString("CODI_UFFI_SIES", aValore);
	}

	public void setFlagSelePeriComp(String aValore) {
		setString("FLAG_SELE_PERI_COMP", aValore);
	}

	public void setNumeFascBdmc(BigDecimal aValore) {
		setBigDecimal("NUME_FASC_BDMC", aValore);
	}

	public void setAnnoFascBdmc(BigDecimal aValore) {
		setBigDecimal("ANNO_FASC_BDMC", aValore);
	}

	public void setCodiSedeInst(String aValore) {
		setString("CODI_SEDE_INST", aValore);
	}

	public void setCognSogg(String aValore) {
		setString("COGN_SOGG", aValore);
	}

	public void setNomeSogg(String aValore) {
		setString("NOME_SOGG", aValore);
	}

	public void setFlagSess(String aValore) {
		setString("FLAG_SESS", aValore);
	}

	public void setCodiStat(String aValore) {
		setString("CODI_STAT", aValore);
	}

	public void setLuogNasc(String aValore) {
		setString("LUOG_NASC", aValore);
	}

	public void setCodiIdenAfis(String aValore) {
		setString("CODI_IDEN_AFIS", aValore);
	}

	public void setDataNasc(Date aValore) {
		setDate("DATA_NASC", aValore);
	}

	public void setFlagSeleCircSogg(String aValore) {
		setString("FLAG_SELE_CIRC_SOGG", aValore);
	}

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new SbPrenModel(getDataPren(), getIdPren(), getUtenSies(), getNote(), getDataAnnu(),
				getMotiAnnu(), getCodiStatPren(),
				getFlagSeleCapoImpu(),
				getFlagSeleProcPena(),
				// getFlagSeleSent() ,
				getFlagPrenPres(), getCodiUffiSies(), "", getFlagSelePeriComp(), getNumeFascBdmc(),
				getAnnoFascBdmc(), getCodiSedeInst(), getDescSedeInst(), getCognSogg(), getNomeSogg(),
				getFlagSess(), getCodiStat(), "", getLuogNasc(), getCodiIdenAfis(), "", getDataNasc(),
				getFlagSeleCircSogg());
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(SbPrenModel aModel) throws DAOException {
		setDataPren(aModel.getDataPren());
		setIdPren(aModel.getIdPren());
		setUtenSies(aModel.getUtenSies());
		setNote(aModel.getNote());
		setDataAnnu(aModel.getDataAnnu());
		setMotiAnnu(aModel.getMotiAnnu());
		setCodiStatPren(aModel.getCodiStatPren());
		setFlagSeleCapoImpu(aModel.getFlagSeleCapoImpu());
		setFlagSeleProcPena(aModel.getFlagSeleProcPena());
		// setFlagSeleSent ( aModel.getFlagSeleSent() );
		setFlagPrenPres(aModel.getFlagPrenPres());
		setCodiUffiSies(aModel.getCodiUffiSies());
		setFlagSelePeriComp(aModel.getFlagSelePeriComp());
		setNumeFascBdmc(aModel.getNumeFascBdmc());
		setAnnoFascBdmc(aModel.getAnnoFascBdmc());
		setCodiSedeInst(aModel.getCodiSedeInst());
		setCognSogg(aModel.getCognSogg());
		setNomeSogg(aModel.getNomeSogg());
		setFlagSess(aModel.getFlagSess());
		setCodiStat(aModel.getCodiStat());
		setLuogNasc(aModel.getLuogNasc());
		setCodiIdenAfis(aModel.getCodiIdenAfis());
		setDataNasc(aModel.getDataNasc());
		setFlagSeleCircSogg(aModel.getFlagSeleCircSogg());
	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(SbPrenModel aModel) {
		String lCondizioni = new String();

		if (aModel.getDataPren() != null) {
			lCondizioni += " and to_char(DATA_PREN,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataPren(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getIdPren() != null) {
			lCondizioni += " and ID_PREN = " + aModel.getIdPren() + "";
		}
		if (aModel.getUtenSies() != null && aModel.getUtenSies().length() > 0) {
			lCondizioni += " and UTEN_SIES = '" + aModel.getUtenSies() + "' ";
		}
		if (aModel.getNote() != null && aModel.getNote().length() > 0) {
			lCondizioni += " and NOTE = '" + aModel.getNote() + "' ";
		}
		if (aModel.getDataAnnu() != null) {
			lCondizioni += " and to_char(DATA_ANNU,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataAnnu(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getMotiAnnu() != null && aModel.getMotiAnnu().length() > 0) {
			lCondizioni += " and MOTI_ANNU = '" + aModel.getMotiAnnu() + "' ";
		}
		if (aModel.getCodiStatPren() != null) {
			lCondizioni += " and (CODI_STAT_PREN =0 or CODI_STAT_PREN =1) ";
		}
		if (aModel.getFlagSeleCapoImpu() != null && aModel.getFlagSeleCapoImpu().length() > 0) {
			lCondizioni += " and FLAG_SELE_CAPO_IMPU = '" + aModel.getFlagSeleCapoImpu() + "'";
		}
		if (aModel.getFlagSeleProcPena() != null && aModel.getFlagSeleProcPena().length() > 0) {
			lCondizioni += " and FLAG_SELE_PROC_PENA = '" + aModel.getFlagSeleProcPena() + "'";
		}
		/*
		 * if (aModel.getFlagSeleSent() != null ) { lCondizioni += " and FLAG_SELE_SENT = " +
		 * aModel.getFlagSeleSent() + ""; }
		 */
		if (aModel.getFlagPrenPres() != null && aModel.getFlagPrenPres().length() > 0) {
			lCondizioni += " and FLAG_PREN_PRES = '" + aModel.getFlagPrenPres() + "'";
		}
		if (aModel.getCodiUffiSies() != null && aModel.getCodiUffiSies().length() > 0) {
			lCondizioni += " and CODI_UFFI_SIES = '" + aModel.getCodiUffiSies() + "' ";
		}
		if (aModel.getFlagSelePeriComp() != null && aModel.getFlagSelePeriComp().length() > 0) {
			lCondizioni += " and FLAG_SELE_PERI_COMP = '" + aModel.getFlagSelePeriComp() + "'";
		}
		if (aModel.getNumeFascBdmc() != null) {
			lCondizioni += " and NUME_FASC_BDMC = " + aModel.getNumeFascBdmc() + "";
		}
		if (aModel.getAnnoFascBdmc() != null) {
			lCondizioni += " and ANNO_FASC_BDMC = " + aModel.getAnnoFascBdmc() + "";
		}
		if (aModel.getCodiSedeInst() != null && aModel.getCodiSedeInst().length() > 0) {
			lCondizioni += " and CODI_SEDE_INST = '" + aModel.getCodiSedeInst() + "' ";
		}
		if (aModel.getCognSogg() != null && aModel.getCognSogg().length() > 0) {
			lCondizioni += " and COGN_SOGG = '" + aModel.getCognSogg() + "' ";
		}
		if (aModel.getNomeSogg() != null && aModel.getNomeSogg().length() > 0) {
			lCondizioni += " and NOME_SOGG = '" + aModel.getNomeSogg() + "' ";
		}
		if (aModel.getFlagSess() != null && aModel.getFlagSess().length() > 0) {
			lCondizioni += " and FLAG_SESS = '" + aModel.getFlagSess() + "' ";
		}
		if (aModel.getCodiStat() != null && aModel.getCodiStat().length() > 0) {
			lCondizioni += " and CODI_STAT = '" + aModel.getCodiStat() + "' ";
		}
		if (aModel.getLuogNasc() != null && aModel.getLuogNasc().length() > 0) {
			lCondizioni += " and LUOG_NASC = '" + aModel.getLuogNasc() + "' ";
		}
		if (aModel.getCodiIdenAfis() != null && aModel.getCodiIdenAfis().length() > 0) {
			lCondizioni += " and CODI_IDEN_AFIS = '" + aModel.getCodiIdenAfis() + "' ";
		}
		if (aModel.getDataNasc() != null) {
			lCondizioni += " and to_char(DATA_NASC,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataNasc(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getFlagSeleCircSogg() != null && aModel.getFlagSeleCircSogg().length() > 0) {
			lCondizioni += " and FLAG_SELE_CIRC_SOGG = '" + aModel.getFlagSeleCircSogg() + "'";
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

		setCondition(" ID_PREN = '" + aIdPren + "'");

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
