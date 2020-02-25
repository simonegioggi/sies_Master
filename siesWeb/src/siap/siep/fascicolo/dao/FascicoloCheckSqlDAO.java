package siap.siep.fascicolo.dao;

import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.fascicolo.model.FascicoloCheckModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: FascicoloSiepSqlDAO
 * </p>
 * <p>
 * Description: Realizza Sql DAo del Fascicolo Siep
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class FascicoloCheckSqlDAO extends SIAPSqlDAO {

	public FascicoloCheckSqlDAO(Connection aCon) {
		super(aCon);
	}

	public void ricercaFascicoliIscrittiTot(String aChiaveUfficio) throws DAOException {

		String lStatement = new String();
		lStatement += " SELECT count(*) NUM_FASCICOLI, CHIAVE_ANNO ";
		lStatement += "   FROM FASCICOLO_SIEP ";
		lStatement += "  WHERE COD_UFFICIO_INSERIMENTO = '" + aChiaveUfficio + "' ";
		lStatement += "    AND FLAG_VALIDATO = 'S' ";
		lStatement += "    AND COD_STATO_FASCICOLO = '03' "; // validato non archiviato
		lStatement += " GROUP BY CHIAVE_ANNO ";
		lStatement += " ORDER BY CHIAVE_ANNO DESC ";

		setStatement(lStatement);
	}

	public void ricercaFascicoliIscrittiRES(String aChiaveUfficio) throws DAOException {

		String lStatement = new String();

		lStatement += " SELECT count(*) NUM_FASCICOLI, CHIAVE_ANNO ";
		lStatement += "   FROM FASCICOLO_SIEP ";
		lStatement += "  WHERE COD_UFFICIO_INSERIMENTO = '" + aChiaveUfficio + "' ";
		lStatement += "    AND FLAG_VALIDATO = 'S' ";
		lStatement += "    AND COD_STATO_FASCICOLO = '03' "; // validato non archiviato
		lStatement += "    AND COD_OPERATORE_INSERIMENTO like 'res-%' "; // Migrati
		lStatement += " GROUP BY CHIAVE_ANNO ";
		lStatement += " ORDER BY CHIAVE_ANNO DESC ";

		setStatement(lStatement);
	}

	public void ricercaFascicoliIscrittiSIEP(String aChiaveUfficio) throws DAOException {

		String lStatement = new String();
		lStatement += " SELECT count(*) NUM_FASCICOLI, CHIAVE_ANNO ";
		lStatement += "   FROM FASCICOLO_SIEP ";
		lStatement += "  WHERE COD_UFFICIO_INSERIMENTO = '" + aChiaveUfficio + "' ";
		lStatement += "    AND FLAG_VALIDATO = 'S' ";
		lStatement += "    AND COD_STATO_FASCICOLO = '03' "; // validato non archiviato
		lStatement += "    AND COD_OPERATORE_INSERIMENTO not like 'res-%' "; // Iscritti SIEP
		lStatement += " GROUP BY CHIAVE_ANNO ";
		lStatement += " ORDER BY CHIAVE_ANNO DESC ";

		setStatement(lStatement);
	}

	/**
	 *
	 * @return Il Model dei dati selezionati
	 * @throws DAOException
	 */
	public GenericModel getModel() throws DAOException {

		FascicoloCheckModel lCheckFascicolo = new FascicoloCheckModel();

		lCheckFascicolo.setNumFascicoli(getBigDecimal("NUM_FASCICOLI"));
		lCheckFascicolo.setChiaveAnno(getBigDecimal("CHIAVE_ANNO"));

		return lCheckFascicolo;
	}

}