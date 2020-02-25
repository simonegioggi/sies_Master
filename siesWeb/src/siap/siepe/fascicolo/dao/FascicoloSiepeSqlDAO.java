package siap.siepe.fascicolo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: FascicoloSiepeSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella FascicoloSiepe
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
public class FascicoloSiepeSqlDAO extends SqlDAO {

	public FascicoloSiepeSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaFascicoloSiepe(FascicoloSiepeModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaFascicoloSiepeByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaFascicoloByAnnoProgrCodUfficio(FascicoloSiepeModel aFasSiepe) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByAnnoProgrCodUfficio(aFasSiepe);
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_FASCICOLO_SIEPE, " + "CHIAVE_ANNO, " + "CHIAVE_PROGR, "
				+ "CHIAVE_UFFICIO, " + "NUM_UEPE, " + "ANNO_UEPE, " + "PROGR_UEPE, " + "COD_STATO_FASCICOLO, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_ISCRIZIONE, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "SOG_ID_SOGGETTO, " + "FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "FAS_SIU_ID_FASCICOLO_SIUS, " + "COD_INCARICO, " + "NOTE, " + "COD_UFFICIO_MITTENTE, "
				+ "EVE_ID_EVENTO, " + "TIPO_DEFINIZIONE, " + "DATA_DEFINIZIONE, " + "DESCR_DEFINIZIONE ";
		lStatement += " FROM FASCICOLO_SIEPE";
		// lStatement += " WHERE ";
		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		FascicoloSiepeModel aModel = new FascicoloSiepeModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdFascicoloSiepe(getBigDecimal("ID_FASCICOLO_SIEPE"));
		aModel.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));
		aModel.setChiaveProgr(getBigDecimal("CHIAVE_PROGR"));
		aModel.setChiaveUfficio(getString("CHIAVE_UFFICIO"));
		aModel.setNumUepe(getBigDecimal("NUM_UEPE"));
		aModel.setAnnoUepe(getBigDecimal("ANNO_UEPE"));
		aModel.setProgrUepe(getBigDecimal("PROGR_UEPE"));
		aModel.setCodStatoFascicolo(getString("COD_STATO_FASCICOLO"));
		try {
			aModel.setDescrStatoFascicolo(DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getStatoFascicolo(), aModel.getCodStatoFascicolo()));
		} catch (Exception e) {
			throw new DAOException(e.toString());
		}

		// aModel.setDescrStatoFascicolo(getString("") );
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataIscrizione(getDate("DATA_ISCRIZIONE"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setSogIdSoggetto(getBigDecimal("SOG_ID_SOGGETTO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setFasSiuIdFascicoloSius(getBigDecimal("FAS_SIU_ID_FASCICOLO_SIUS"));
		aModel.setCodIncarico(getString("COD_INCARICO"));
		try {
			aModel.setDescrIncarico(DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getTipoIncaricoSiepe(), aModel.getCodIncarico()));
		} catch (Exception e) {
			throw new DAOException(e.toString());
		}
		aModel.setNote(getString("NOTE"));
		aModel.setCodUfficioMittente(getString("COD_UFFICIO_MITTENTE"));
		// aModel.setDescrUfficioMittente(getString("") );
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setTipoDefinizione(getString("TIPO_DEFINIZIONE"));
		aModel.setDataDefinizione(getDate("DATA_DEFINIZIONE"));
		aModel.setDescrDefinizione(getString("DESCR_DEFINIZIONE"));

		return aModel;
	}

	public String setCondizione(FascicoloSiepeModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " WHERE ID_FASCICOLO_SIEPE = " + aKey;
	}

	public String setCondizioniByAnnoProgrCodUfficio(FascicoloSiepeModel lFasMod) {
		String lCondizioni = new String();
		lCondizioni += " WHERE CHIAVE_ANNO = " + lFasMod.getChiaveAnno();
		lCondizioni += " AND CHIAVE_PROGR = " + lFasMod.getChiaveProgr();
		lCondizioni += " AND CHIAVE_UFFICIO = " + lFasMod.getChiaveUfficio();

		// boolean lInserito = false;
		return lCondizioni;
	}

	/**
	 * Calcola il Massimo Progressivo relativo ad un certo ufficio e all'anno in corso. Il massimo progressivo
	 * rappresenta anche l'ultimo progressivo inserito all'intenro dell'ufficio trattato.
	 * <p>
	 * 
	 * @return BigDecimal
	 * @param FascicoloSiepeModel
	 *            model del fascicolo SIEPE.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public BigDecimal getProgressivoFascicoloSiepe(FascicoloSiepeModel aModel) throws DAOException {
		BigDecimal lProgr = new BigDecimal(0);

		String lStatement = "";

		lStatement += " SELECT MAX(CHIAVE_PROGR) aMAX";
		lStatement += " FROM FASCICOLO_SIEPE FS";
		lStatement += " WHERE FS.CHIAVE_ANNO = " + aModel.getChiaveAnno();
		lStatement += " AND FS.CHIAVE_UFFICIO = '" + aModel.getChiaveUfficio() + "'";

		setStatement(lStatement);
		start();
		// if (next())
		if (this.next() && (this.getBigDecimal("aMAX") != null))
			lProgr = getBigDecimal("aMAX");
		stop();
		if (lProgr == null)
			lProgr = new BigDecimal(0);
		return lProgr;

	}

	/**
	 * Query che Individua i Fascicoli SIEPE afferenti ad un Fascicolo SIUS.
	 * 
	 * @param Bigdecimal
	 */
	public void ricercaFascicoliSiepePerIdFasSius(BigDecimal aIdFasSius) {
		String strQuery = new String();

		strQuery += "SELECT ID_FASCICOLO_SIEPE, " + "CHIAVE_ANNO, " + "CHIAVE_PROGR, " + "CHIAVE_UFFICIO, "
				+ "UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO, "
				+ "DESCR_TIPO_UFF.RV_MEANING DESCRIZIONE_TIPO_UFFICIO, "
				+ "DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, " + "NUM_UEPE, " + "ANNO_UEPE, "
				+ "PROGR_UEPE, " + "COD_STATO_FASCICOLO, " + "FASC.COD_OPERATORE_INSERIMENTO, "
				+ "DATA_ISCRIZIONE, " + "FASC.DATA_INSERIMENTO, " + "FASC.COD_UFFICIO_INSERIMENTO, "
				+ "FASC.COD_OPERATORE_AGGIORNAMENTO," + "FASC.DATA_AGGIORNAMENTO, "
				+ "FASC.COD_UFFICIO_AGGIORNAMENTO, " + "SOG_ID_SOGGETTO, " + "FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "FAS_SIU_ID_FASCICOLO_SIUS," + "COD_INCARICO, " + "FASC.NOTE, "
				+ "FASC.COD_UFFICIO_MITTENTE, " + "FASC.EVE_ID_EVENTO, " + "SOGG.ID_SOGGETTO, "
				+ "SOGG.COGNOME COGNOME, " + "SOGG.NOME NOME, " + "SOGG.DATA_NASCITA DATA_NASCITA,"
				+ "SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO, "
				+ "SOGG.COD_COMUNE_NASCITA COD_COMUNE_NASCITA, "
				+ "DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, "
				+ "SOGG.COD_PROVINCIA_NASCITA COD_PROVINCIA_NASCITA, " + "TIPO_DEFINIZIONE, "
				+ "DATA_DEFINIZIONE, " + "DESCR_DEFINIZIONE " + "FROM FASCICOLO_SIEPE FASC, "
				+ "UFFICIO UFF, " + "CG_REF_CODES DESCR_TIPO_UFF, " + "COMUNE DESCR_COM_UFF, "
				+ "SOGGETTO SOGG, " + "COMUNE DESCR_COM_NASCITA " + "WHERE FASC.FAS_SIU_ID_FASCICOLO_SIUS = '"
				+ aIdFasSius + "'"
				+ "AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO "
				+ "AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE "
				+ "AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE "
				+ "AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' "
				+ "AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE "
				+ "AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE ";

		setStatement(strQuery);
	}

	/**
	 * Query che Individua i Fascicoli SIEPE afferenti ad un Fascicolo SIEP.
	 * 
	 * @param Bigdecimal
	 * @return String
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void ricercaFascicoliSiepePerIdFasSiep(BigDecimal aIdFasSiep) {
		String strQuery = new String();

		strQuery += "SELECT ID_FASCICOLO_SIEPE, " + "CHIAVE_ANNO, CHIAVE_PROGR, " + "CHIAVE_UFFICIO, "
				+ "UFF.COD_TIPO_UFFICIO DESCR_TIPO_UFFICIO, "
				+ "DESCR_TIPO_UFF.RV_MEANING DESCRIZIONE_TIPO_UFFICIO, "
				+ "DESCR_COM_UFF.DESCRIZIONE DESCR_COMUNE_UFFICIO, " + "NUM_UEPE, ANNO_UEPE, "
				+ "PROGR_UEPE, " + "COD_STATO_FASCICOLO, " + "FASC.COD_OPERATORE_INSERIMENTO, "
				+ "DATA_ISCRIZIONE, " + "FASC.DATA_INSERIMENTO, " + "FASC.COD_UFFICIO_INSERIMENTO, "
				+ "FASC.COD_OPERATORE_AGGIORNAMENTO," + "FASC.DATA_AGGIORNAMENTO, "
				+ "FASC.COD_UFFICIO_AGGIORNAMENTO, " + "SOG_ID_SOGGETTO, " + "FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "FAS_SIU_ID_FASCICOLO_SIUS," + "COD_INCARICO, FASC.NOTE, " + "FASC.COD_UFFICIO_MITTENTE, "
				+ "FASC.EVE_ID_EVENTO, " + "SOGG.ID_SOGGETTO, " + "SOGG.COGNOME COGNOME, "
				+ "SOGG.NOME NOME, " + "SOGG.DATA_NASCITA DATA_NASCITA,"
				+ "SOGG.DESC_COMUNE_NASCITA_ESTERO DESC_COMUNE_NASCITA_ESTERO, "
				+ "SOGG.COD_COMUNE_NASCITA COD_COMUNE_NASCITA, "
				+ "DESCR_COM_NASCITA.DESCRIZIONE DESCR_COMUNE_NASCITA, "
				+ "SOGG.COD_PROVINCIA_NASCITA COD_PROVINCIA_NASCITA, " + "TIPO_DEFINIZIONE, "
				+ "DATA_DEFINIZIONE, " + "DESCR_DEFINIZIONE " + "FROM FASCICOLO_SIEPE FASC, "
				+ "UFFICIO UFF, " + "CG_REF_CODES DESCR_TIPO_UFF, " + "COMUNE DESCR_COM_UFF, "
				+ "SOGGETTO SOGG, " + "COMUNE DESCR_COM_NASCITA " + "WHERE FASC.FAS_SIE_ID_FASCICOLO_SIEP = '"
				+ aIdFasSiep + "'" + "AND UFF.COD_UFFICIO = FASC.CHIAVE_UFFICIO "
				+ "AND FASC.SOG_ID_SOGGETTO = SOGG.ID_SOGGETTO "
				+ "AND UFF.COD_COMUNE = DESCR_COM_UFF.COD_COMUNE "
				+ "AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE "
				+ "AND DESCR_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' "
				+ "AND UFF.COD_TIPO_UFFICIO = DESCR_TIPO_UFF.RV_LOW_VALUE "
				+ "AND SOGG.COD_COMUNE_NASCITA = DESCR_COM_NASCITA.COD_COMUNE";

		setStatement(strQuery);
	}

}