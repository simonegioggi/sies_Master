package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.modulocumulo.model.PenaComplessivaCumuloModel;

/**
 * <p>
 * Title: PenaComplessivaCumuloSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella PenaComplessiva
 * </p>
 * <p>
 * in ambito Cumulo (Pena_complessiva_Cumulo)
 * </p>
 */

public class PenaComplessivaCumuloSqlDAO extends SIAPSqlDAO {
	public PenaComplessivaCumuloSqlDAO(Connection con) {
		super(con);
	}

	public void ricercaPenaComplessivaCumulo(PenaComplessivaCumuloModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaPenaComplessivaCumuloByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaPenaComplessivaCumuloByIdTitolo(BigDecimal aIdTitolo) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " AND TIT_ID_TITOLO_CUMULATO = " + aIdTitolo;

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += "SELECT " + "ID_PENA_COMPLESSIVA_CUM, "
				+ "COD_TIPO_PENA_DETENTIVA, PENA_DETENTIVA.RV_MEANING DESCR_PENA_DETENTIVA,"
				+ "NUM_ANNI_RECLUSIONE, " + "NUM_MESI_RECLUSIONE, " + "NUM_GIORNI_RECLUSIONE, "
				+ "IMPORTO_MULTA, " + "NUM_ANNI_ARRESTO, " + "NUM_MESI_ARRESTO, " + "NUM_GIORNI_ARRESTO, "
				+ "IMPORTO_AMMENDA, " + "NUM_ANNI_ISOLAMENTO_DIURNO, " + "NUM_MESI_ISOLAMENTO_DIURNO, "
				+ "NUM_GIORNI_ISOLAMENTO_DIURNO, " + "DATA_PRESCRIZIONE, " + "FLAG_PENA_IN_CONTINUAZIONE, "
				+ "FLAG_STATO, " + "MOTIVO_MODIFICA, " + "TIT_ID_TITOLO_CUMULATO, "
				+ "ID_PENA_COMPLESSIVA_ORIGINE, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO ";
		lStatement += " FROM PENA_COMPLESSIVA_CUMULO, CG_REF_CODES PENA_DETENTIVA ";
		lStatement += " WHERE 1=1 ";
		lStatement += " AND ( nvl(PENA_COMPLESSIVA_CUMULO.COD_TIPO_PENA_DETENTIVA,'-') = PENA_DETENTIVA.RV_LOW_VALUE AND PENA_DETENTIVA.RV_DOMAIN = 'TIPO_PENA_DETENTIVA' )";

		return lStatement;
	}

	/**
	 * 
	 */
	public GenericModel getModel() throws DAOException {
		PenaComplessivaCumuloModel aModel = new PenaComplessivaCumuloModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdPenaComplessivaCum(getBigDecimal("ID_PENA_COMPLESSIVA_CUM"));
		aModel.setCodTipoPenaDetentiva(getString("COD_TIPO_PENA_DETENTIVA"));
		aModel.setDescrTipoPenaDetentiva(getString("DESCR_PENA_DETENTIVA"));

		aModel.setNumAnniReclusione(getBigDecimal("NUM_ANNI_RECLUSIONE"));
		aModel.setNumMesiReclusione(getBigDecimal("NUM_MESI_RECLUSIONE"));
		aModel.setNumGiorniReclusione(getBigDecimal("NUM_GIORNI_RECLUSIONE"));
		aModel.setImportoMulta(getBigDecimal("IMPORTO_MULTA"));

		aModel.setNumAnniArresto(getBigDecimal("NUM_ANNI_ARRESTO"));
		aModel.setNumMesiArresto(getBigDecimal("NUM_MESI_ARRESTO"));
		aModel.setNumGiorniArresto(getBigDecimal("NUM_GIORNI_ARRESTO"));
		aModel.setImportoAmmenda(getBigDecimal("IMPORTO_AMMENDA"));

		aModel.setNumAnniIsolamentoDiurno(getBigDecimal("NUM_ANNI_ISOLAMENTO_DIURNO"));
		aModel.setNumMesiIsolamentoDiurno(getBigDecimal("NUM_MESI_ISOLAMENTO_DIURNO"));
		aModel.setNumGiorniIsolamentoDiurno(getBigDecimal("NUM_GIORNI_ISOLAMENTO_DIURNO"));

		aModel.setDataPrescrizione(getDate("DATA_PRESCRIZIONE"));

		aModel.setFlagPenaInContinuazione(getString("FLAG_PENA_IN_CONTINUAZIONE"));

		aModel.setFlagStato(getString("FLAG_STATO"));
		aModel.setMotivoModifica(getString("MOTIVO_MODIFICA"));
		aModel.setTitIdTitoloCumulato(getBigDecimal("TIT_ID_TITOLO_CUMULATO"));
		aModel.setIdPenaComplessivaOrigine(getBigDecimal("ID_PENA_COMPLESSIVA_ORIGINE"));

		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));

		return aModel;
	}

	public String setCondizione(PenaComplessivaCumuloModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		// if (aModel.getFasSieIdFascicoloSiep()!=null)
		// {
		// lInserito=true;
		// lCondizioni+=" AND FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
		// }
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_PENA_COMPLESSIVA_CUM = " + aKey;
	}

}