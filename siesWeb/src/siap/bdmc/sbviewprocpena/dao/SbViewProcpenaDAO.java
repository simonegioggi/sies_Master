package siap.bdmc.sbviewprocpena.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel;
import siap.dao.SIAPTableDAO;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: SbViewProcpenaDAO
 * </p>
 * <p>
 * Description: Classe DAO che rappresenta la tabella SbViewProcpena
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
public class SbViewProcpenaDAO extends SIAPTableDAO {

	/*****************************************************************************
	 * Costruttore della classe: imposta il nome della tabella e i campi
	 * 
	 * @param con
	 *            connessione
	 ****************************************************************************/
	public SbViewProcpenaDAO(Connection con) {
		super(con);
		setTable("SB_VIEW_PROCPENA@SIES_BDMC_LINK");

		// Settare la Sequence e i campi chiave e commentare il setField del campo chiave
		setSequenceField("ID_PREN", "SOGPROCPENA_SEQ");

		setField("CODI_UFFI_PMPM", STRING);
		setField("ANNO_REGI_PMPM", BIG_DECIMAL);
		setField("NUME_REGI_PMPM", BIG_DECIMAL);
		setField("CODI_UFFI_GIPP", STRING);
		setField("ANNO_REGI_GIPP", BIG_DECIMAL);
		setField("NUME_REGI_GIPP", BIG_DECIMAL);
		setField("CODI_UFFI_DIBB", STRING);
		setField("ANNO_REGI_DIBB", BIG_DECIMAL);
		setField("NUME_REGI_DIBB", BIG_DECIMAL);
		setField("CODI_UFFI_COAP", STRING);
		setField("NUME_REGI_COAP", BIG_DECIMAL);
		setField("ANNO_REGI_COAP", BIG_DECIMAL);
		setField("DATA_PASS_GIUD", DATE);
		setField("FLAG_RECL_ARRE_GIGU", STRING);
		setField("ANNI_PENA_GIGU", BIG_DECIMAL);
		setField("MESI_PENA_GIGU", BIG_DECIMAL);
		setField("GIOR_PENA_GIGU", BIG_DECIMAL);
		setField("DATA_SENT_1GRA", DATE);
		setField("ANNO_SENT_1GRA", BIG_DECIMAL);
		setField("NUME_SENT_1GRA", BIG_DECIMAL);
		setField("DATA_SENT_2GRA", DATE);
		setField("ANNO_SENT_2GRA", BIG_DECIMAL);
		setField("NUME_SENT_2GRA", BIG_DECIMAL);
		setField("DATA_SENT_GIPP_GUPP", DATE);
		setField("NUME_SENT_GIPP_GUPP", BIG_DECIMAL);
		setField("ANNO_SENT_GIPP_GUPP", BIG_DECIMAL);
		setField("ANNI_PENA_DIBA", BIG_DECIMAL);
		setField("MESI_PENA_DIBA", BIG_DECIMAL);
		setField("GIOR_PENA_DIBA", BIG_DECIMAL);
		setField("ANNI_PENA_APPE", BIG_DECIMAL);
		setField("MESI_PENA_APPE", BIG_DECIMAL);
		setField("GIOR_PENA_APPE", BIG_DECIMAL);
		setField("FLAG_RECL_ARRE_DIBA", STRING);
		setField("FLAG_RECL_ARRE_APPE", STRING);
		setField("FLAG_ARTI_0089", STRING);
		setField("FLAG_ARTI_0090", STRING);
		setField("FLAG_ARTI_0091", STRING);
		setField("FLAG_ARTI_0092", STRING);
		setField("FLAG_ARTI_0093", STRING);
		setField("FLAG_ARTI_0094", STRING);
		setField("FLAG_ARTI_0095", STRING);
		setField("FLAG_ARTI_0096", STRING);
		setField("FLAG_ARTI_0097", STRING);
		setField("FLAG_ARTI_0098", STRING);
		setField("FLAG_ARTI_0099", STRING);
		setField("FLAG_ARTI_0062", STRING);
		setField("ARTI_0062_COMM", STRING);
		setField("FLAG_ARTI_62BI", STRING);
		setField("CODI_MISU_CUST", STRING);
		setField("CODI_ISTI_PENA", STRING);
		setField("DESC_LUOG", STRING);
		// setField("ID_PREN", BIG_DECIMAL);
		setField("CODI_SEDE_INST", STRING);
		setField("NUME_FASC_BDMC", BIG_DECIMAL);
		setField("ANNO_FASC_BDMC", BIG_DECIMAL);
		setField("FLAG_INFO_SELE", STRING);
		setField("DATA_DECI_CASS", DATE);
		setField("ANNO_DECI_CASS", BIG_DECIMAL);
		setField("NUME_DECI_CASS", BIG_DECIMAL);

	}

	// ============================================================================
	// Metodi get utilizzati per recuperare i dati dal result set
	// ============================================================================
	public String getCodiUffiPmpm() throws DAOException {
		return getString("CODI_UFFI_PMPM");
	}

	public BigDecimal getAnnoRegiPmpm() throws DAOException {
		return getBigDecimal("ANNO_REGI_PMPM");
	}

	public BigDecimal getNumeRegiPmpm() throws DAOException {
		return getBigDecimal("NUME_REGI_PMPM");
	}

	public String getCodiUffiGipp() throws DAOException {
		return getString("CODI_UFFI_GIPP");
	}

	public BigDecimal getAnnoRegiGipp() throws DAOException {
		return getBigDecimal("ANNO_REGI_GIPP");
	}

	public BigDecimal getNumeRegiGipp() throws DAOException {
		return getBigDecimal("NUME_REGI_GIPP");
	}

	public String getCodiUffiDibb() throws DAOException {
		return getString("CODI_UFFI_DIBB");
	}

	public BigDecimal getAnnoRegiDibb() throws DAOException {
		return getBigDecimal("ANNO_REGI_DIBB");
	}

	public BigDecimal getNumeRegiDibb() throws DAOException {
		return getBigDecimal("NUME_REGI_DIBB");
	}

	public String getCodiUffiCoap() throws DAOException {
		return getString("CODI_UFFI_COAP");
	}

	public BigDecimal getNumeRegiCoap() throws DAOException {
		return getBigDecimal("NUME_REGI_COAP");
	}

	public BigDecimal getAnnoRegiCoap() throws DAOException {
		return getBigDecimal("ANNO_REGI_COAP");
	}

	public Date getDataPassGiud() throws DAOException {
		return getDate("DATA_PASS_GIUD");
	}

	public String getFlagReclArreGigu() throws DAOException {
		return getString("FLAG_RECL_ARRE_GIGU");
	}

	public BigDecimal getAnniPenaGigu() throws DAOException {
		return getBigDecimal("ANNI_PENA_GIGU");
	}

	public BigDecimal getMesiPenaGigu() throws DAOException {
		return getBigDecimal("MESI_PENA_GIGU");
	}

	public BigDecimal getGiorPenaGigu() throws DAOException {
		return getBigDecimal("GIOR_PENA_GIGU");
	}

	public Date getDataSent1gra() throws DAOException {
		return getDate("DATA_SENT_1GRA");
	}

	public BigDecimal getAnnoSent1gra() throws DAOException {
		return getBigDecimal("ANNO_SENT_1GRA");
	}

	public BigDecimal getNumeSent1gra() throws DAOException {
		return getBigDecimal("NUME_SENT_1GRA");
	}

	public Date getDataSent2gra() throws DAOException {
		return getDate("DATA_SENT_2GRA");
	}

	public BigDecimal getAnnoSent2gra() throws DAOException {
		return getBigDecimal("ANNO_SENT_2GRA");
	}

	public BigDecimal getNumeSent2gra() throws DAOException {
		return getBigDecimal("NUME_SENT_2GRA");
	}

	public Date getDataSentGippGupp() throws DAOException {
		return getDate("DATA_SENT_GIPP_GUPP");
	}

	public BigDecimal getNumeSentGippGupp() throws DAOException {
		return getBigDecimal("NUME_SENT_GIPP_GUPP");
	}

	public BigDecimal getAnnoSentGippGupp() throws DAOException {
		return getBigDecimal("ANNO_SENT_GIPP_GUPP");
	}

	public BigDecimal getAnniPenaDiba() throws DAOException {
		return getBigDecimal("ANNI_PENA_DIBA");
	}

	public BigDecimal getMesiPenaDiba() throws DAOException {
		return getBigDecimal("MESI_PENA_DIBA");
	}

	public BigDecimal getGiorPenaDiba() throws DAOException {
		return getBigDecimal("GIOR_PENA_DIBA");
	}

	public BigDecimal getAnniPenaAppe() throws DAOException {
		return getBigDecimal("ANNI_PENA_APPE");
	}

	public BigDecimal getMesiPenaAppe() throws DAOException {
		return getBigDecimal("MESI_PENA_APPE");
	}

	public BigDecimal getGiorPenaAppe() throws DAOException {
		return getBigDecimal("GIOR_PENA_APPE");
	}

	public String getFlagReclArreDiba() throws DAOException {
		return getString("FLAG_RECL_ARRE_DIBA");
	}

	public String getFlagReclArreAppe() throws DAOException {
		return getString("FLAG_RECL_ARRE_APPE");
	}

	public String getFlagArti0089() throws DAOException {
		return getString("FLAG_ARTI_0089");
	}

	public String getFlagArti0090() throws DAOException {
		return getString("FLAG_ARTI_0090");
	}

	public String getFlagArti0091() throws DAOException {
		return getString("FLAG_ARTI_0091");
	}

	public String getFlagArti0092() throws DAOException {
		return getString("FLAG_ARTI_0092");
	}

	public String getFlagArti0093() throws DAOException {
		return getString("FLAG_ARTI_0093");
	}

	public String getFlagArti0094() throws DAOException {
		return getString("FLAG_ARTI_0094");
	}

	public String getFlagArti0095() throws DAOException {
		return getString("FLAG_ARTI_0095");
	}

	public String getFlagArti0096() throws DAOException {
		return getString("FLAG_ARTI_0096");
	}

	public String getFlagArti0097() throws DAOException {
		return getString("FLAG_ARTI_0097");
	}

	public String getFlagArti0098() throws DAOException {
		return getString("FLAG_ARTI_0098");
	}

	public String getFlagArti0099() throws DAOException {
		return getString("FLAG_ARTI_0099");
	}

	public String getFlagArti62() throws DAOException {
		return getString("FLAG_ARTI_0062");
	}

	public String getArti0062Comm() throws DAOException {
		return getString("ARTI_0062_COMM");
	}

	public String getFlagArt62bi() throws DAOException {
		return getString("FLAG_ARTI_62BI");
	}

	public String getCodiMisuCust() throws DAOException {
		return getString("CODI_MISU_CUST");
	}

	public String getCodiIstiPena() throws DAOException {
		return getString("CODI_ISTI_PENA");
	}

	public String getDescLuog() throws DAOException {
		return getString("DESC_LUOG");
	}

	public BigDecimal getIdPren() throws DAOException {
		return getBigDecimal("ID_PREN");
	}

	public String getCodiSedeInst() throws DAOException {
		return getString("CODI_SEDE_INST");
	}

	public BigDecimal getNumeFascBdmc() throws DAOException {
		return getBigDecimal("NUME_FASC_BDMC");
	}

	public BigDecimal getAnnoFascBdmc() throws DAOException {
		return getBigDecimal("ANNO_FASC_BDMC");
	}

	public String getFlagInfoSele() throws DAOException {
		return getString("FLAG_INFO_SELE");
	}

	public Date getDataDeciCass() throws DAOException {
		return getDate("DATA_DECI_CASS");
	}

	public BigDecimal getAnnoDeciCass() throws DAOException {
		return getBigDecimal("ANNO_DECI_CASS");
	}

	public BigDecimal getNumeDeciCass() throws DAOException {
		return getBigDecimal("NUME_DECI_CASS");
	}

	// ============================================================================
	// Metodi set utilizzati per impostare i campi delle query
	// ============================================================================
	public void setCodiUffiPmpm(String aValore) {
		setString("CODI_UFFI_PMPM", aValore);
	}

	public void setAnnoRegiPmpm(BigDecimal aValore) {
		setBigDecimal("ANNO_REGI_PMPM", aValore);
	}

	public void setNumeRegiPmpm(BigDecimal aValore) {
		setBigDecimal("NUME_REGI_PMPM", aValore);
	}

	public void setCodiUffiGipp(String aValore) {
		setString("CODI_UFFI_GIPP", aValore);
	}

	public void setAnnoRegiGipp(BigDecimal aValore) {
		setBigDecimal("ANNO_REGI_GIPP", aValore);
	}

	public void setNumeRegiGipp(BigDecimal aValore) {
		setBigDecimal("NUME_REGI_GIPP", aValore);
	}

	public void setCodiUffiDibb(String aValore) {
		setString("CODI_UFFI_DIBB", aValore);
	}

	public void setAnnoRegiDibb(BigDecimal aValore) {
		setBigDecimal("ANNO_REGI_DIBB", aValore);
	}

	public void setNumeRegiDibb(BigDecimal aValore) {
		setBigDecimal("NUME_REGI_DIBB", aValore);
	}

	public void setCodiUffiCoap(String aValore) {
		setString("CODI_UFFI_COAP", aValore);
	}

	public void setNumeRegiCoap(BigDecimal aValore) {
		setBigDecimal("NUME_REGI_COAP", aValore);
	}

	public void setAnnoRegiCoap(BigDecimal aValore) {
		setBigDecimal("ANNO_REGI_COAP", aValore);
	}

	public void setDataPassGiud(Date aValore) {
		setDate("DATA_PASS_GIUD", aValore);
	}

	public void setFlagReclArreGigu(String aValore) {
		setString("FLAG_RECL_ARRE_GIGU", aValore);
	}

	public void setAnniPenaGigu(BigDecimal aValore) {
		setBigDecimal("ANNI_PENA_GIGU", aValore);
	}

	public void setMesiPenaGigu(BigDecimal aValore) {
		setBigDecimal("MESI_PENA_GIGU", aValore);
	}

	public void setGiorPenaGigu(BigDecimal aValore) {
		setBigDecimal("GIOR_PENA_GIGU", aValore);
	}

	public void setDataSent1gra(Date aValore) {
		setDate("DATA_SENT_1GRA", aValore);
	}

	public void setAnnoSent1gra(BigDecimal aValore) {
		setBigDecimal("ANNO_SENT_1GRA", aValore);
	}

	public void setNumeSent1gra(BigDecimal aValore) {
		setBigDecimal("NUME_SENT_1GRA", aValore);
	}

	public void setDataSent2gra(Date aValore) {
		setDate("DATA_SENT_2GRA", aValore);
	}

	public void setAnnoSent2gra(BigDecimal aValore) {
		setBigDecimal("ANNO_SENT_2GRA", aValore);
	}

	public void setNumeSent2gra(BigDecimal aValore) {
		setBigDecimal("NUME_SENT_2GRA", aValore);
	}

	public void setDataSentGippGupp(Date aValore) {
		setDate("DATA_SENT_GIPP_GUPP", aValore);
	}

	public void setNumeSentGippGupp(BigDecimal aValore) {
		setBigDecimal("NUME_SENT_GIPP_GUPP", aValore);
	}

	public void setAnnoSentGippGupp(BigDecimal aValore) {
		setBigDecimal("ANNO_SENT_GIPP_GUPP", aValore);
	}

	public void setAnniPenaDiba(BigDecimal aValore) {
		setBigDecimal("ANNI_PENA_DIBA", aValore);
	}

	public void setMesiPenaDiba(BigDecimal aValore) {
		setBigDecimal("MESI_PENA_DIBA", aValore);
	}

	public void setGiorPenaDiba(BigDecimal aValore) {
		setBigDecimal("GIOR_PENA_DIBA", aValore);
	}

	public void setAnniPenaAppe(BigDecimal aValore) {
		setBigDecimal("ANNI_PENA_APPE", aValore);
	}

	public void setMesiPenaAppe(BigDecimal aValore) {
		setBigDecimal("MESI_PENA_APPE", aValore);
	}

	public void setGiorPenaAppe(BigDecimal aValore) {
		setBigDecimal("GIOR_PENA_APPE", aValore);
	}

	public void setFlagReclArreDiba(String aValore) {
		setString("FLAG_RECL_ARRE_DIBA", aValore);
	}

	public void setFlagReclArreAppe(String aValore) {
		setString("FLAG_RECL_ARRE_APPE", aValore);
	}

	public void setFlagArti0089(String aValore) {
		setString("FLAG_ARTI_0089", aValore);
	}

	public void setFlagArti0090(String aValore) {
		setString("FLAG_ARTI_0090", aValore);
	}

	public void setFlagArti0091(String aValore) {
		setString("FLAG_ARTI_0091", aValore);
	}

	public void setFlagArti0092(String aValore) {
		setString("FLAG_ARTI_0092", aValore);
	}

	public void setFlagArti0093(String aValore) {
		setString("FLAG_ARTI_0093", aValore);
	}

	public void setFlagArti0094(String aValore) {
		setString("FLAG_ARTI_0094", aValore);
	}

	public void setFlagArti0095(String aValore) {
		setString("FLAG_ARTI_0095", aValore);
	}

	public void setFlagArti0096(String aValore) {
		setString("FLAG_ARTI_0096", aValore);
	}

	public void setFlagArti0097(String aValore) {
		setString("FLAG_ARTI_0097", aValore);
	}

	public void setFlagArti0098(String aValore) {
		setString("FLAG_ARTI_0098", aValore);
	}

	public void setFlagArti0099(String aValore) {
		setString("FLAG_ARTI_0099", aValore);
	}

	public void setFlagArti62(String aValore) {
		setString("FLAG_ARTI_0062", aValore);
	}

	public void setArti0062Comm(String aValore) {
		setString("ARTI_0062_COMM", aValore);
	}

	public void setFlagArt62bi(String aValore) {
		setString("FLAG_ARTI_62BI", aValore);
	}

	public void setCodiMisuCust(String aValore) {
		setString("CODI_MISU_CUST", aValore);
	}

	public void setCodiIstiPena(String aValore) {
		setString("CODI_ISTI_PENA", aValore);
	}

	public void setDescLuog(String aValore) {
		setString("DESC_LUOG", aValore);
	}

	public void setIdPren(BigDecimal aValore) {
		setBigDecimal("ID_PREN", aValore);
	}

	public void setCodiSedeInst(String aValore) {
		setString("CODI_SEDE_INST", aValore);
	}

	public void setNumeFascBdmc(BigDecimal aValore) {
		setBigDecimal("NUME_FASC_BDMC", aValore);
	}

	public void setAnnoFascBdmc(BigDecimal aValore) {
		setBigDecimal("ANNO_FASC_BDMC", aValore);
	}

	public void setFlagInfoSele(String aValore) {
		setString("FLAG_INFO_SELE", aValore);
	}

	public void setDataDeciCass(Date aValore) {
		setDate("DATA_DECI_CASS", aValore);
	}

	public void setAnnoDeciCass(BigDecimal aValore) {
		setBigDecimal("ANNO_DECI_CASS", aValore);
	}

	public void setNumeDeciCass(BigDecimal aValore) {
		setBigDecimal("NUME_DECI_CASS", aValore);
	}

	/*****************************************************************************
	 * Metodo che recupera i dati della select e carica il model in output
	 * 
	 * @return il model
	 * @throws DAOException
	 ****************************************************************************/
	public GenericModel getModel() throws DAOException {
		return new SbViewProcpenaModel(getCodiUffiPmpm(), "", "", getAnnoRegiPmpm(), getNumeRegiPmpm(),
				getCodiUffiGipp(), "", "", getAnnoRegiGipp(), getNumeRegiGipp(), getCodiUffiDibb(), "", "",
				getAnnoRegiDibb(), getNumeRegiDibb(), getCodiUffiCoap(), "", "", getNumeRegiCoap(),
				getAnnoRegiCoap(), getDataPassGiud(), getFlagReclArreGigu(), getAnniPenaGigu(),
				getMesiPenaGigu(), getGiorPenaGigu(), getDataSent1gra(), getAnnoSent1gra(),
				getNumeSent1gra(), getDataSent2gra(), getAnnoSent2gra(), getNumeSent2gra(),
				getDataSentGippGupp(), getNumeSentGippGupp(), getAnnoSentGippGupp(), getAnniPenaDiba(),
				getMesiPenaDiba(), getGiorPenaDiba(), getAnniPenaAppe(), getMesiPenaAppe(),
				getGiorPenaAppe(), getFlagReclArreDiba(), getFlagReclArreAppe(), getFlagArti0089(),
				getFlagArti0090(), getFlagArti0091(), getFlagArti0092(), getFlagArti0093(),
				getFlagArti0094(), getFlagArti0095(), getFlagArti0096(), getFlagArti0097(),
				getFlagArti0098(), getFlagArti0099(), getFlagArti62(), getArti0062Comm(), getFlagArt62bi(),
				getCodiMisuCust(), "", getCodiIstiPena(), "", getDescLuog(), getIdPren(), getCodiSedeInst(),
				"", "", getNumeFascBdmc(), getAnnoFascBdmc(), getFlagInfoSele(), getDataDeciCass(),
				getAnnoDeciCass(), getNumeDeciCass()

		);
	}

	/*****************************************************************************
	 * Metodo che imposta i campi delle operazioni di Inserimento e Modifica a partire dal contenuto del model
	 * passato in input.
	 * 
	 * @param aModel
	 * @throws DAOException
	 ****************************************************************************/
	public void setDAOFromModel(SbViewProcpenaModel aModel) throws DAOException {
		setCodiUffiPmpm(aModel.getCodiUffiPmpm());
		setAnnoRegiPmpm(aModel.getAnnoRegiPmpm());
		setNumeRegiPmpm(aModel.getNumeRegiPmpm());
		setCodiUffiGipp(aModel.getCodiUffiGipp());
		setAnnoRegiGipp(aModel.getAnnoRegiGipp());
		setNumeRegiGipp(aModel.getNumeRegiGipp());
		setCodiUffiDibb(aModel.getCodiUffiDibb());
		setAnnoRegiDibb(aModel.getAnnoRegiDibb());
		setNumeRegiDibb(aModel.getNumeRegiDibb());
		setCodiUffiCoap(aModel.getCodiUffiCoap());
		setNumeRegiCoap(aModel.getNumeRegiCoap());
		setAnnoRegiCoap(aModel.getAnnoRegiCoap());
		setDataPassGiud(aModel.getDataPassGiud());
		setFlagReclArreGigu(aModel.getFlagReclArreGigu());
		setAnniPenaGigu(aModel.getAnniPenaGigu());
		setMesiPenaGigu(aModel.getMesiPenaGigu());
		setGiorPenaGigu(aModel.getGiorPenaGigu());
		setDataSent1gra(aModel.getDataSent1gra());
		setAnnoSent1gra(aModel.getAnnoSent1gra());
		setNumeSent1gra(aModel.getNumeSent1gra());
		setDataSent2gra(aModel.getDataSent2gra());
		setAnnoSent2gra(aModel.getAnnoSent2gra());
		setNumeSent2gra(aModel.getNumeSent2gra());
		setDataSentGippGupp(aModel.getDataSentGippGupp());
		setNumeSentGippGupp(aModel.getNumeSentGippGupp());
		setAnnoSentGippGupp(aModel.getAnnoSentGippGupp());
		setAnniPenaDiba(aModel.getAnniPenaDiba());
		setMesiPenaDiba(aModel.getMesiPenaDiba());
		setGiorPenaDiba(aModel.getGiorPenaDiba());
		setAnniPenaAppe(aModel.getAnniPenaAppe());
		setMesiPenaAppe(aModel.getMesiPenaAppe());
		setGiorPenaAppe(aModel.getGiorPenaAppe());
		setFlagReclArreDiba(aModel.getFlagReclArreDiba());
		setFlagReclArreAppe(aModel.getFlagReclArreAppe());
		setFlagArti0089(aModel.getFlagArti0089());
		setFlagArti0090(aModel.getFlagArti0090());
		setFlagArti0091(aModel.getFlagArti0091());
		setFlagArti0092(aModel.getFlagArti0092());
		setFlagArti0093(aModel.getFlagArti0093());
		setFlagArti0094(aModel.getFlagArti0094());
		setFlagArti0095(aModel.getFlagArti0095());
		setFlagArti0096(aModel.getFlagArti0096());
		setFlagArti0097(aModel.getFlagArti0097());
		setFlagArti0098(aModel.getFlagArti0098());
		setFlagArti0099(aModel.getFlagArti0099());
		setFlagArti62(aModel.getFlagArti62());
		setArti0062Comm(aModel.getArti0062Comm());
		setFlagArt62bi(aModel.getFlagArt62bi());
		setCodiMisuCust(aModel.getCodiMisuCust());
		setCodiIstiPena(aModel.getCodiIstiPena());
		setDescLuog(aModel.getDescLuog());
		setIdPren(aModel.getIdPren());
		setCodiSedeInst(aModel.getCodiSedeInst());
		setNumeFascBdmc(aModel.getNumeFascBdmc());
		setAnnoFascBdmc(aModel.getAnnoFascBdmc());
		setFlagInfoSele(aModel.getFlagInfoSele());
		setDataDeciCass(aModel.getDataDeciCass());
		setAnnoDeciCass(aModel.getAnnoDeciCass());
		setNumeDeciCass(aModel.getNumeDeciCass());

	}

	/*****************************************************************************
	 * Metodo che imposta le condizioni di where per la ricerca
	 * 
	 * @param aModel
	 * @return
	 ****************************************************************************/
	public void setCondizioni(SbViewProcpenaModel aModel) {
		String lCondizioni = new String();

		if (aModel.getCodiUffiPmpm() != null && aModel.getCodiUffiPmpm().length() > 0) {
			lCondizioni += " and CODI_UFFI_PMPM = '" + aModel.getCodiUffiPmpm() + "' ";
		}
		if (aModel.getAnnoRegiPmpm() != null) {
			lCondizioni += " and ANNO_REGI_PMPM = " + aModel.getAnnoRegiPmpm() + "";
		}
		if (aModel.getNumeRegiPmpm() != null) {
			lCondizioni += " and NUME_REGI_PMPM = " + aModel.getNumeRegiPmpm() + "";
		}
		if (aModel.getCodiUffiGipp() != null && aModel.getCodiUffiGipp().length() > 0) {
			lCondizioni += " and CODI_UFFI_GIPP = '" + aModel.getCodiUffiGipp() + "' ";
		}
		if (aModel.getAnnoRegiGipp() != null) {
			lCondizioni += " and ANNO_REGI_GIPP = " + aModel.getAnnoRegiGipp() + "";
		}
		if (aModel.getNumeRegiGipp() != null) {
			lCondizioni += " and NUME_REGI_GIPP = " + aModel.getNumeRegiGipp() + "";
		}
		if (aModel.getCodiUffiDibb() != null && aModel.getCodiUffiDibb().length() > 0) {
			lCondizioni += " and CODI_UFFI_DIBB = '" + aModel.getCodiUffiDibb() + "' ";
		}
		if (aModel.getAnnoRegiDibb() != null) {
			lCondizioni += " and ANNO_REGI_DIBB = " + aModel.getAnnoRegiDibb() + "";
		}
		if (aModel.getNumeRegiDibb() != null) {
			lCondizioni += " and NUME_REGI_DIBB = " + aModel.getNumeRegiDibb() + "";
		}
		if (aModel.getCodiUffiCoap() != null && aModel.getCodiUffiCoap().length() > 0) {
			lCondizioni += " and CODI_UFFI_COAP = '" + aModel.getCodiUffiCoap() + "' ";
		}
		if (aModel.getNumeRegiCoap() != null) {
			lCondizioni += " and NUME_REGI_COAP = " + aModel.getNumeRegiCoap() + "";
		}
		if (aModel.getAnnoRegiCoap() != null) {
			lCondizioni += " and ANNO_REGI_COAP = " + aModel.getAnnoRegiCoap() + "";
		}
		if (aModel.getDataPassGiud() != null) {
			lCondizioni += " and to_char(DATA_PASS_GIUD,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataPassGiud(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getFlagReclArreGigu() != null && aModel.getFlagReclArreGigu().length() > 0) {
			lCondizioni += " and FLAG_RECL_ARRE_GIGU = '" + aModel.getFlagReclArreGigu() + "' ";
		}
		if (aModel.getAnniPenaGigu() != null) {
			lCondizioni += " and ANNI_PENA_GIGU = " + aModel.getAnniPenaGigu() + "";
		}
		if (aModel.getMesiPenaGigu() != null) {
			lCondizioni += " and MESI_PENA_GIGU = " + aModel.getMesiPenaGigu() + "";
		}
		if (aModel.getGiorPenaGigu() != null) {
			lCondizioni += " and GIOR_PENA_GIGU = " + aModel.getGiorPenaGigu() + "";
		}
		if (aModel.getDataSent1gra() != null) {
			lCondizioni += " and to_char(DATA_SENT_1GRA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataSent1gra(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getAnnoSent1gra() != null) {
			lCondizioni += " and ANNO_SENT_1GRA = " + aModel.getAnnoSent1gra() + "";
		}
		if (aModel.getNumeSent1gra() != null) {
			lCondizioni += " and NUME_SENT_1GRA = " + aModel.getNumeSent1gra() + "";
		}
		if (aModel.getDataSent2gra() != null) {
			lCondizioni += " and to_char(DATA_SENT_2GRA,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataSent2gra(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getAnnoSent2gra() != null) {
			lCondizioni += " and ANNO_SENT_2GRA = " + aModel.getAnnoSent2gra() + "";
		}
		if (aModel.getNumeSent2gra() != null) {
			lCondizioni += " and NUME_SENT_2GRA = " + aModel.getNumeSent2gra() + "";
		}
		if (aModel.getDataSentGippGupp() != null) {
			lCondizioni += " and to_char(DATA_SENT_GIPP_GUPP,'dd/MM/yyyy') = '"
					+ DateUtils.getDateToString(aModel.getDataSentGippGupp(), "dd/MM/yyyy") + "' ";
		}
		if (aModel.getNumeSentGippGupp() != null) {
			lCondizioni += " and NUME_SENT_GIPP_GUPP = " + aModel.getNumeSentGippGupp() + "";
		}
		if (aModel.getAnnoSentGippGupp() != null) {
			lCondizioni += " and ANNO_SENT_GIPP_GUPP = " + aModel.getAnnoSentGippGupp() + "";
		}
		if (aModel.getAnniPenaDiba() != null) {
			lCondizioni += " and ANNI_PENA_DIBA = " + aModel.getAnniPenaDiba() + "";
		}
		if (aModel.getMesiPenaDiba() != null) {
			lCondizioni += " and MESI_PENA_DIBA = " + aModel.getMesiPenaDiba() + "";
		}
		if (aModel.getGiorPenaDiba() != null) {
			lCondizioni += " and GIOR_PENA_DIBA = " + aModel.getGiorPenaDiba() + "";
		}
		if (aModel.getAnniPenaAppe() != null) {
			lCondizioni += " and ANNI_PENA_APPE = " + aModel.getAnniPenaAppe() + "";
		}
		if (aModel.getMesiPenaAppe() != null) {
			lCondizioni += " and MESI_PENA_APPE = " + aModel.getMesiPenaAppe() + "";
		}
		if (aModel.getGiorPenaAppe() != null) {
			lCondizioni += " and GIOR_PENA_APPE = " + aModel.getGiorPenaAppe() + "";
		}
		if (aModel.getFlagReclArreDiba() != null && aModel.getFlagReclArreDiba().length() > 0) {
			lCondizioni += " and FLAG_RECL_ARRE_DIBA = '" + aModel.getFlagReclArreDiba() + "' ";
		}
		if (aModel.getFlagReclArreAppe() != null && aModel.getFlagReclArreAppe().length() > 0) {
			lCondizioni += " and FLAG_RECL_ARRE_APPE = '" + aModel.getFlagReclArreAppe() + "' ";
		}
		if (aModel.getFlagArti0089() != null && aModel.getFlagArti0089().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0089 = '" + aModel.getFlagArti0089() + "' ";
		}
		if (aModel.getFlagArti0090() != null && aModel.getFlagArti0090().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0090 = '" + aModel.getFlagArti0090() + "' ";
		}
		if (aModel.getFlagArti0091() != null && aModel.getFlagArti0091().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0091 = '" + aModel.getFlagArti0091() + "' ";
		}
		if (aModel.getFlagArti0092() != null && aModel.getFlagArti0092().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0092 = '" + aModel.getFlagArti0092() + "' ";
		}
		if (aModel.getFlagArti0093() != null && aModel.getFlagArti0093().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0093 = '" + aModel.getFlagArti0093() + "' ";
		}
		if (aModel.getFlagArti0094() != null && aModel.getFlagArti0094().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0094 = '" + aModel.getFlagArti0094() + "' ";
		}
		if (aModel.getFlagArti0095() != null && aModel.getFlagArti0095().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0095 = '" + aModel.getFlagArti0095() + "' ";
		}
		if (aModel.getFlagArti0096() != null && aModel.getFlagArti0096().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0096 = '" + aModel.getFlagArti0096() + "' ";
		}
		if (aModel.getFlagArti0097() != null && aModel.getFlagArti0097().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0097 = '" + aModel.getFlagArti0097() + "' ";
		}
		if (aModel.getFlagArti0098() != null && aModel.getFlagArti0098().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0098 = '" + aModel.getFlagArti0098() + "' ";
		}
		if (aModel.getFlagArti0099() != null && aModel.getFlagArti0099().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0099 = '" + aModel.getFlagArti0099() + "' ";
		}
		if (aModel.getFlagArti62() != null && aModel.getFlagArti62().length() > 0) {
			lCondizioni += " and FLAG_ARTI_0062 = '" + aModel.getFlagArti62() + "' ";
		}
		if (aModel.getArti0062Comm() != null && aModel.getArti0062Comm().length() > 0) {
			lCondizioni += " and ARTI_0062_COMM = '" + aModel.getArti0062Comm() + "' ";
		}
		if (aModel.getFlagArt62bi() != null && aModel.getFlagArt62bi().length() > 0) {
			lCondizioni += " and FLAG_ARTI_62BI = '" + aModel.getFlagArt62bi() + "' ";
		}
		if (aModel.getCodiMisuCust() != null && aModel.getCodiMisuCust().length() > 0) {
			lCondizioni += " and CODI_MISU_CUST = '" + aModel.getCodiMisuCust() + "' ";
		}
		if (aModel.getCodiIstiPena() != null && aModel.getCodiIstiPena().length() > 0) {
			lCondizioni += " and CODI_ISTI_PENA = '" + aModel.getCodiIstiPena() + "' ";
		}
		if (aModel.getDescLuog() != null && aModel.getDescLuog().length() > 0) {
			lCondizioni += " and DESC_LUOG = '" + aModel.getDescLuog() + "' ";
		}
		if (aModel.getIdPren() != null) {
			lCondizioni += " and ID_PREN = " + aModel.getIdPren() + "";
		}
		if (aModel.getCodiSedeInst() != null && aModel.getCodiSedeInst().length() > 0) {
			lCondizioni += " and CODI_SEDE_INST = '" + aModel.getCodiSedeInst() + "' ";
		}
		if (aModel.getNumeFascBdmc() != null) {
			lCondizioni += " and NUME_FASC_BDMC = " + aModel.getNumeFascBdmc() + "";
		}
		if (aModel.getAnnoFascBdmc() != null) {
			lCondizioni += " and ANNO_FASC_BDMC = " + aModel.getAnnoFascBdmc() + "";
		}
		if (aModel.getFlagInfoSele() != null && aModel.getFlagInfoSele().length() > 0) {
			lCondizioni += " and FLAG_INFO_SELE = '" + aModel.getFlagInfoSele() + "' ";
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
	 * Imposta la condizione di where per l'operazione di Delete puntuale si entra sempre in chiave
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