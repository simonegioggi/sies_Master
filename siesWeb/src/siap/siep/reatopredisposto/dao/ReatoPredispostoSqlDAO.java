package siap.siep.reatopredisposto.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import f3b.util.StringUtils;
import siap.siep.reatopredisposto.model.ReatoPredispostoModel;

/**
 * <p>
 * Title: ReatoPredispostoSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella ReatoPredisposto
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
public class ReatoPredispostoSqlDAO extends SqlDAO {

	public ReatoPredispostoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaReatoPredisposto(ReatoPredispostoModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);

		lSql += " " + setOrdinamento();
		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		// siesLogger.error(lSql);

		setStatement(lSql);
	}

	public void ricercaReatoPredispostoByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	protected String getSqlQuery() {

		String lStatement = new String("");

		lStatement += " SELECT " + "ID_REATO_PREDISPOSTO, " + "PROGR_NORMA, " + "NOME_ELEMENTO, "
				+ "COD_FONTE, " + "ANNO_FONTE, " + "NUMERO_FONTE, " + "COD_SOTTONUMERAZIONE, " + "COMMA, "
				+ "LETTERA, " + "NUMERO, " + "ARTICOLO, " + "NOTE_ELEMENTO, " + "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, ";

		lStatement += "DECOSOTTONUM.RV_MEANING DESCSOTTONUM, ";
		lStatement += "DECOFONTE.RV_MEANING DESCFONTE ";

		lStatement += " FROM reato_predisposto, CG_REF_CODES DECOSOTTONUM, CG_REF_CODES DECOFONTE";
		lStatement += " WHERE ";
		lStatement += " DECOSOTTONUM.RV_DOMAIN='SOTTONUMERAZIONE' AND DECOSOTTONUM.RV_LOW_VALUE=reato_predisposto.COD_SOTTONUMERAZIONE ";
		lStatement += " AND DECOFONTE.RV_DOMAIN='FONTE' AND DECOFONTE.RV_LOW_VALUE=reato_predisposto.COD_FONTE ";

		return lStatement;

	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		ReatoPredispostoModel aModel = new ReatoPredispostoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdReatoPredisposto(getBigDecimal("ID_REATO_PREDISPOSTO"));
		aModel.setProgrNorma(getBigDecimal("PROGR_NORMA"));
		aModel.setNomeElemento(getString("NOME_ELEMENTO"));
		aModel.setCodFonte(getString("COD_FONTE"));
		aModel.setDescrFonte(getString("DESCFONTE"));
		aModel.setDescrSottonumerazione(getString("DESCSOTTONUM"));
		aModel.setAnnoFonte(getBigDecimal("ANNO_FONTE"));
		aModel.setNumeroFonte(getString("NUMERO_FONTE"));
		aModel.setCodSottonumerazione(getString("COD_SOTTONUMERAZIONE"));
		aModel.setComma(getString("COMMA"));
		aModel.setLettera(getString("LETTERA"));
		aModel.setNumero(getString("NUMERO"));
		aModel.setArticolo(getString("ARTICOLO"));
		aModel.setNoteElemento(getString("NOTE_ELEMENTO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));

		return aModel;
	}

	public String setCondizione(ReatoPredispostoModel aModel) {
		String lCondizioni = new String();

		if (aModel.getNomeElemento() != "")
			lCondizioni += " AND NOME_ELEMENTO= '" + StringUtils.convertSqlString(aModel.getNomeElemento())
					+ "'";

		lCondizioni += " AND COD_UFFICIO_INSERIMENTO='"
				+ StringUtils.convertSqlString(aModel.getCodUfficioInserimento()) + "'";

		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_reato_predisposto = " + aKey;
	}

	public BigDecimal getProgrNorma(String aNomeElemento, String aCodUfficio) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT MAX(PROGR_NORMA) aMAX";
		lStatement += " FROM REATO_PREDISPOSTO";
		lStatement += " WHERE Nome_Elemento = '" + aNomeElemento + "'";
		lStatement += " AND COD_UFFICIO_INSERIMENTO = '" + aCodUfficio + "'";

		setStatement(lStatement);

		this.start();

		BigDecimal lProgrNorma = null;
		if (this.next() && (this.getBigDecimal("aMAX") != null))
			lProgrNorma = this.getBigDecimal("aMAX");

		this.stop();

		if (lProgrNorma == null)
			lProgrNorma = new BigDecimal(0);

		return lProgrNorma;
	}

	public boolean isNomeElementoUsed(String aNomeElemento, String aCodUfficio) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT count(NOME_ELEMENTO) aCONT";
		lStatement += " FROM REATO_PREDISPOSTO";
		lStatement += " WHERE Nome_Elemento = '" + aNomeElemento + "'";
		lStatement += " AND COD_UFFICIO_INSERIMENTO = '" + aCodUfficio + "'";

		setStatement(lStatement);

		this.start();

		this.next();

		int lCont = this.getInt("aCONT");

		this.stop();

		if (lCont == 0)
			return false;
		else
			return true;
	}

	public String setOrdinamento() {
		String lOrd = new String();

		lOrd = " ORDER BY NOME_ELEMENTO, PROGR_NORMA";

		return lOrd;
	}
}