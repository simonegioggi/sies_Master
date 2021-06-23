package siap.sico.decodifiche.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.ComuneProvinciaModel;

public class ComuneSqlDAO extends SIAPSqlDAO {

	public ComuneSqlDAO(Connection aCon) {
		super(aCon);
	}

	public void listaProvince() throws DAOException {
		String lStatement = new String();
		lStatement += " SELECT DISTINCT RV_LOW_VALUE COD_PROVINCIA,RV_MEANING DESC_PROVINCIA";
		lStatement += " FROM CG_REF_CODES";
		lStatement += " WHERE RV_DOMAIN='PROVINCIA'";
		lStatement += " ORDER BY DESC_PROVINCIA";

		setStatement(lStatement);

	}

	/**
	 * La funzione restituisce la lista delle province per codTipoUfficio PM, PMM, PGCAP, ...
	 */
	public void listaProvincePerTipoUfficio(String aCodTipoUfficio) throws DAOException {

		// BigDecimal lCount;
		// int retNum = 0;

		String lStatement = new String();
		lStatement += " SELECT DISTINCT RV_LOW_VALUE COD_PROVINCIA,RV_MEANING DESC_PROVINCIA ";
		lStatement += " FROM CG_REF_CODES, UFFICIO ";
		lStatement += " WHERE CG_REF_CODES.RV_DOMAIN='PROVINCIA' AND CG_REF_CODES.RV_LOW_VALUE=UFFICIO.COD_PROVINCIA AND  ";
		lStatement += " UFFICIO.COD_TIPO_UFFICIO = '" + aCodTipoUfficio + "'";
		lStatement += " ORDER BY DESC_PROVINCIA ";

		setStatement(lStatement);

	}

	public void ricercaComuneTdsm() throws DAOException {
		String lStatement = new String();
		lStatement += " SELECT COMUNE.COD_COMUNE, COMUNE.DESCRIZIONE";
		lStatement += " FROM COMUNE, UFFICIO";
		lStatement += " WHERE UFFICIO.COD_TIPO_UFFICIO = 'TDSM' AND COMUNE.COD_COMUNE = UFFICIO.COD_COMUNE ";
		lStatement += " ORDER BY COMUNE.DESCRIZIONE";

		setStatement(lStatement);
	}

	public void ricercaComuneTds() throws DAOException {
		String lStatement = new String();
		lStatement += " SELECT COMUNE.COD_COMUNE, COMUNE.DESCRIZIONE";
		lStatement += " FROM COMUNE, UFFICIO";
		lStatement += " WHERE UFFICIO.COD_TIPO_UFFICIO = 'TDS' AND COMUNE.COD_COMUNE = UFFICIO.COD_COMUNE ";
		lStatement += " ORDER BY COMUNE.DESCRIZIONE";

		setStatement(lStatement);
	}

	public GenericModel getModelComuneTds() throws DAOException {
		ComuneModel lCPr = new ComuneModel();

		lCPr.setCodComune(getString("COD_COMUNE"));
		lCPr.setDescrizione(getString("DESCRIZIONE"));

		return lCPr;
	}

	public GenericModel getModel() throws DAOException {
		ComuneProvinciaModel lCPr = new ComuneProvinciaModel();

		lCPr.setCodProvincia(getString("COD_PROVINCIA"));
		lCPr.setProvincia(getString("DESC_PROVINCIA"));

		return lCPr;
	}

	// MEV_21 Recupero Comune, Descrizione, CAP e CodProvincia dal COMUNE puntato dal ccc.
	public GenericModel getModelComuneCcc() throws DAOException {
		ComuneModel lCcc = new ComuneModel();

		lCcc.setCodComune(getString("COD_COMUNE"));
		lCcc.setDescrizione(getString("DESCRIZIONE"));
		lCcc.setCap(getString("CAP"));
		lCcc.setCodProvincia(getString("COD_PROVINCIA"));

		return lCcc;
	}
	
	/**
	 * La funzione prepara la Select per effettuare la ricerca dei Comuni di uno specifico Distretto che siano
	 * sedi UNEP. Sono possibili sedi UNEP quei Comuni del Distretto che risultino anche sede di uno dei
	 * seguenti uffici: CAP : Corte d'Appello; DIB : Tribunale Ordinario; TRIBSD : Sezione distaccata di
	 * Tribunale.
	 *
	 * @param aCodDis
	 *            : codice Distretto
	 * @throws DAOException
	 */
	public void ricercaSediUNEPperDistretto(String aCodDis) throws DAOException {
		String lStatement = new String();
		lStatement += " SELECT DISTINCT COMUNE.COD_COMUNE, COMUNE.DESCRIZIONE";
		lStatement += " FROM COMUNE";
		lStatement += " WHERE COMUNE.COD_COMUNE IN (SELECT  COD_COMUNE FROM UFFICIO WHERE COD_DISTRETTO = '"
				+ aCodDis + "'";
		lStatement += " AND COD_TIPO_UFFICIO IN ('CAP', 'DIB', 'TRIBSD'))";
		lStatement += " ORDER BY COMUNE.DESCRIZIONE";

		setStatement(lStatement);
	}

	/**
	 * La funzione restituisce il numero di uffici del Comune cui può essere associato un ufficio UNEP sedi
	 * UNEP in un determinato Comune.
	 */
	public int getNumSediUNEPperCodComune(String aCodComune) throws DAOException {
		BigDecimal lCount;
		int retNum = 0;

		String lStatement = new String();
		lStatement += " SELECT COUNT(*) N FROM UFFICIO";
		lStatement += " WHERE COD_COMUNE = '" + aCodComune + "'";
		lStatement += " AND COD_TIPO_UFFICIO IN ('CAP', 'DIB', 'TRIBSD')";
		setStatement(lStatement);

		start();
		if (next()) {
			lCount = getBigDecimal("N");
			retNum = lCount.intValue();
		}

		return retNum;
	}

	// MEV_21: ricerco il comune dato il codice catastale
	public void ricercaComuneByCodCatastale(String ccc) {

		String lStatement = new String();
		lStatement += " SELECT COD_COMUNE, COD_PROVINCIA, DESCRIZIONE, CAP, ";
		lStatement += " FROM COMUNE";
		lStatement += " WHERE COD_CATASTALE_COMUNE = '" + ccc + "'";

		setStatement(lStatement);
	}

	// MEV_21: ricerco la nazione collegata al codice catastale estratto dal C.F. soggetto nato all'Estero.
	/**
	 * La funzione restituisce la lista delle province per codTipoUfficio PM, PMM, PGCAP, ...
	 */
	public void ricercaNazionePerCcc(String aCcc) throws DAOException {

		// BigDecimal lCount;
		// int retNum = 0;

		String lStatement = new String();
		lStatement += " SELECT '-' COD_COMUNE, RV_HIGH_VALUE COD_PROVINCIA, RV_MEANING DESCRIZIONE, RV_ABBREVIATION CAP ";
		lStatement += " FROM CG_REF_CODES";
		lStatement += " WHERE CG_REF_CODES.RV_DOMAIN='NAZIONE' AND CG_REF_CODES.RV_ALT2_VALUE='"+aCcc+"' ";

		setStatement(lStatement);

	}
	
}