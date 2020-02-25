package siap.bdmc.sbviewreat.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.bdmc.sbviewreat.model.SbViewReatModel;
import siap.dao.SIAPTableDAO;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: SbViewReatDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella SbViewReat
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
public class SbViewReatDAO extends SIAPTableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public SbViewReatDAO(Connection con) {
		super(con);
		setTable("SB_VIEW_REAT@SIES_BDMC_LINK");

		setField("NUME_PROG_CAPO_IMPU", BIG_DECIMAL);
		setField("NUME_PROG_REAT", BIG_DECIMAL);
		setField("CODI_FONT_GIUR", STRING);
		setField("ANNO_FONT_GIUR", BIG_DECIMAL);
		setField("NUME_FONT_GIUR", BIG_DECIMAL);
		setField("ARTI_FONT_GIUR", BIG_DECIMAL);
		setField("COMM_ARTI_FONT", STRING);
		setField("LETT_ARTI_FONT", STRING);
		setField("NUME_ARTI_FONT", STRING);
		setField("ARTI_QUAL_FONT", STRING);
		setField("ID_PREN", BIG_DECIMAL);
		setField("ANNO_FASC_BDMC", BIG_DECIMAL);
		setField("NUME_FASC_BDMC", BIG_DECIMAL);
		setField("CODI_SEDE_INST", STRING);
	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public BigDecimal getNumeProgCapoImpu() throws DAOException {
		return getBigDecimal("NUME_PROG_CAPO_IMPU");
	}

	public BigDecimal getNumeProgReat() throws DAOException {
		return getBigDecimal("NUME_PROG_REAT");
	}

	public String getCodiFontGiur() throws DAOException {
		return getString("CODI_FONT_GIUR");
	}

	public BigDecimal getAnnoFontGiur() throws DAOException {
		return getBigDecimal("ANNO_FONT_GIUR");
	}

	public BigDecimal getNumeFontGiur() throws DAOException {
		return getBigDecimal("NUME_FONT_GIUR");
	}

	public BigDecimal getArtiFontGiur() throws DAOException {
		return getBigDecimal("ARTI_FONT_GIUR");
	}

	public String getCommiArtiFont() throws DAOException {
		return getString("COMM_ARTI_FONT");
	}

	public String getLettArtiFont() throws DAOException {
		return getString("LETT_ARTI_FONT");
	}

	public String getNumeArtiFont() throws DAOException {
		return getString("NUME_ARTI_FONT");
	}

	public String getArtiQualFont() throws DAOException {
		return getString("ARTI_QUAL_FONT");
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

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setNumeProgCapoImpu(BigDecimal aValore) {
		setBigDecimal("NUME_PROG_CAPO_IMPU", aValore);
	}

	public void setNumeProgReat(BigDecimal aValore) {
		setBigDecimal("NUME_PROG_REAT", aValore);
	}

	public void setCodiFontGiur(String aValore) {
		setString("CODI_FONT_GIUR", aValore);
	}

	public void setAnnoFontGiur(BigDecimal aValore) {
		setBigDecimal("ANNO_FONT_GIUR", aValore);
	}

	public void setNumeFontGiur(BigDecimal aValore) {
		setBigDecimal("NUME_FONT_GIUR", aValore);
	}

	public void setArtiFontGiur(BigDecimal aValore) {
		setBigDecimal("ARTI_FONT_GIUR", aValore);
	}

	public void setCommiArtiFont(String aValore) {
		setString("COMM_ARTI_FONT", aValore);
	}

	public void setLettArtiFont(String aValore) {
		setString("LETT_ARTI_FONT", aValore);
	}

	public void setNumeArtiFont(String aValore) {
		setString("NUME_ARTI_FONT", aValore);
	}

	public void setArtiQualFont(String aValore) {
		setString("ARTI_QUAL_FONT", aValore);
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
		return new SbViewReatModel(getNumeProgCapoImpu(), getNumeProgReat(), getCodiFontGiur(), "",
				getAnnoFontGiur(), getNumeFontGiur(), getArtiFontGiur(), getCommiArtiFont(),
				getLettArtiFont(), getNumeArtiFont(), getArtiQualFont(), getIdPren(), getAnnoFascBdmc(),
				getNumeFascBdmc(), getCodiSedeInst());
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(SbViewReatModel aModel) throws DAOException {
		setNumeProgCapoImpu(aModel.getNumeProgCapoImpu());
		setNumeProgReat(aModel.getNumeProgReat());
		setCodiFontGiur(aModel.getCodiFontGiur());
		setAnnoFontGiur(aModel.getAnnoFontGiur());
		setNumeFontGiur(aModel.getNumeFontGiur());
		setArtiFontGiur(aModel.getArtiFontGiur());
		setCommiArtiFont(aModel.getCommiArtiFont());
		setLettArtiFont(aModel.getLettArtiFont());
		setNumeArtiFont(aModel.getNumeArtiFont());
		setArtiQualFont(aModel.getArtiQualFont());
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
	public void setCondizioni(SbViewReatModel aModel) {
		String lCondizioni = new String();

		if (aModel.getNumeProgCapoImpu() != null) {
			lCondizioni += " and NUME_PROG_CAPO_IMPU = " + aModel.getNumeProgCapoImpu() + "";
		}
		if (aModel.getNumeProgReat() != null) {
			lCondizioni += " and NUME_PROG_REAT = " + aModel.getNumeProgReat() + "";
		}
		if (aModel.getCodiFontGiur() != null && aModel.getCodiFontGiur().length() > 0) {
			lCondizioni += " and CODI_FONT_GIUR = '" + aModel.getCodiFontGiur() + "' ";
		}
		if (aModel.getAnnoFontGiur() != null) {
			lCondizioni += " and ANNO_FONT_GIUR = " + aModel.getAnnoFontGiur() + "";
		}
		if (aModel.getNumeFontGiur() != null) {
			lCondizioni += " and NUME_FONT_GIUR = " + aModel.getNumeFontGiur() + "";
		}
		if (aModel.getArtiFontGiur() != null) {
			lCondizioni += " and ARTI_FONT_GIUR = " + aModel.getArtiFontGiur() + "";
		}
		if (aModel.getCommiArtiFont() != null && aModel.getCommiArtiFont().length() > 0) {
			lCondizioni += " and COMM_ARTI_FONT = '" + aModel.getCommiArtiFont() + "' ";
		}
		if (aModel.getLettArtiFont() != null && aModel.getLettArtiFont().length() > 0) {
			lCondizioni += " and LETT_ARTI_FONT = '" + aModel.getLettArtiFont() + "' ";
		}
		if (aModel.getNumeArtiFont() != null && aModel.getNumeArtiFont().length() > 0) {
			lCondizioni += " and NUME_ARTI_FONT = '" + aModel.getNumeArtiFont() + "' ";
		}
		if (aModel.getArtiQualFont() != null && aModel.getArtiQualFont().length() > 0) {
			lCondizioni += " and ARTI_QUAL_FONT = '" + aModel.getArtiQualFont() + "' ";
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
	public void selCondizioneUpdate(BigDecimal aIdPren, BigDecimal aNumeProgCapoImpu, BigDecimal aNumeProgReat) {
		String lCondizioni = new String();

		lCondizioni += " and ID_PREN = " + aIdPren;
		lCondizioni += " and NUME_PROG_CAPO_IMPU = " + aNumeProgCapoImpu;
		lCondizioni += " and NUME_PROG_REAT = " + aNumeProgReat;
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