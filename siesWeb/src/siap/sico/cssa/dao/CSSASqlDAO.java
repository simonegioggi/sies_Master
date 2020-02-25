package siap.sico.cssa.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.sico.cssa.model.CSSAModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.StringUtils;

public class CSSASqlDAO extends SIAPSqlDAO {
	public CSSASqlDAO(Connection aCon) {
		super(aCon);
	}

	public GenericModel getModel() throws DAOException {
		CSSAModel lCSSA = new CSSAModel();

		lCSSA.setIdCSSA(getBigDecimal("ID_CSSA"));
		lCSSA.setTipo(getString("TIPO"));
		lCSSA.setComune(getString("COMUNE"));
		lCSSA.setIndirizzo(getString("INDIRIZZO"));
		lCSSA.setEMail(getString("E_MAIL"));
		lCSSA.setFax(getString("FAX"));
		lCSSA.setTel(getString("TEL"));
		lCSSA.setIncarico(getString("INCARICO"));
		lCSSA.setTitolo(getString("TITOLO"));
		lCSSA.setNome(getString("NOME"));
		lCSSA.setCognome(getString("COGNOME"));
		lCSSA.setDataCaricamento(getDate("DATA_CARICAMENTO"));
		// lCSSA.setCodComune(getString("COD_COMUNE") );

		return lCSSA;
	}

	public void listaCSSA() throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO,";
		lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO";
		lStatement += " FROM CSSA";
		lStatement += " WHERE ";
		lStatement += " TIPO LIKE 'UEPE%' ";
		lStatement += " ORDER BY ID_CSSA";

		setStatement(lStatement);
	}

	public void listaUSSM() throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO,";
		lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO";
		lStatement += " FROM CSSA";
		lStatement += " WHERE ";
		lStatement += " TIPO LIKE 'USSM%' ";
		lStatement += " AND ID_CSSA < 9999 ";
		lStatement += " ORDER BY ID_CSSA";

		setStatement(lStatement);
	}

	public void selCSSAByDescrComune(String aDescrComune) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO,";
		// lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO, C.COD_COMUNE COD_COMUNE";
		lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO";
		// lStatement += " FROM CSSA, COMUNE C";
		lStatement += " FROM CSSA";
		// lStatement += " WHERE (C.DESCRIZIONE = COMUNE";
		// lStatement += " AND C.DESCRIZIONE = '" + StringUtils.convertSqlString(aDescrComune) + "')";
		// lStatement += " OR COMUNE = '" + StringUtils.convertSqlString(aDescrComune) + "'";
		lStatement += " WHERE COMUNE = '" + StringUtils.convertSqlString(aDescrComune) + "'";
		setStatement(lStatement);
	}

	public void selUSSMByDescrComune(String aDescrComune) throws DAOException {
		String lStatement = new String();
		lStatement += " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO,";
		lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO";
		lStatement += " FROM CSSA";
		lStatement += " WHERE COMUNE = '" + StringUtils.convertSqlString(aDescrComune) + "'";
		lStatement += " AND TIPO = 'USSM' ";
		setStatement(lStatement);
	}

	public void selCondizioniCssaLike(CSSAModel aModel) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO,";
		lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO";
		lStatement += " FROM CSSA";
		lStatement += " WHERE COMUNE LIKE '%" + StringUtils.convertSqlString(aModel.getComune()) + "%' ";
		lStatement += " AND TIPO LIKE 'UEPE%' ";
		lStatement += " AND ID_CSSA < 9999"; // l'ID_CSSA = 9999 corrisponde al record '-'
		lStatement += " ORDER BY COMUNE";

		setStatement(lStatement);
	}

	public void selCondizioniCssaMinorLike(CSSAModel aModel) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO,";
		lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO";
		lStatement += " FROM CSSA";
		lStatement += " WHERE COMUNE LIKE '%" + StringUtils.convertSqlString(aModel.getComune()) + "%' ";
		lStatement += " AND TIPO LIKE 'USSM%' ";
		lStatement += " AND ID_CSSA < 9999"; // l'ID_CSSA = 9999 corrisponde al record '-'
		lStatement += " ORDER BY COMUNE";

		setStatement(lStatement);
	}

	public void selModelCssabyKey(BigDecimal aKey) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO,";
		lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO";
		lStatement += " FROM CSSA";
		lStatement += " WHERE ID_CSSA = " + aKey;

		setStatement(lStatement);
	}

	/*
	 * public BigDecimal ricercaIdCSSAByCodComune(String aCodComune) throws DAOException { String lStatement = new
	 * String();
	 * 
	 * lStatement += " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO,"; // lStatement +=
	 * " TITOLO, NOME, COGNOME, DATA_CARICAMENTO, C.COD_COMUNE COD_COMUNE"; // lStatement += " FROM CSSA, COMUNE C"; //
	 * lStatement += " WHERE C.DESCRIZIONE = COMUNE"; // lStatement += " AND C.COD_COMUNE = '" +
	 * StringUtils.convertSqlString(aCodComune) + "'"; lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO";
	 * lStatement += " FROM CSSA"; lStatement += " WHERE COMUNE ='" + StringUtils.convertSqlString(aCodComune) + "'";
	 * 
	 * 
	 * 
	 * setStatement( lStatement );
	 * 
	 * this.start();
	 * 
	 * BigDecimal lIdCSSA = null;
	 * 
	 * if( this.next() ) lIdCSSA = this.getBigDecimal("ID_CSSA");
	 * 
	 * return lIdCSSA; }
	 */

	public void selCSSAByKey(BigDecimal aIdCSSA) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO,";
		lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO";
		lStatement += " FROM CSSA";
		lStatement += " WHERE ID_CSSA = " + aIdCSSA;

		setStatement(lStatement);
	}

	public void ricercaCSSAByIdFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO, ";
		// lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO , C.COD_COMUNE COD_COMUNE ";
		// lStatement += " FROM CSSA, EVENTO E ,NOTIFICA N,MISURA_ALTERNATIVA MA, COMUNE C ";
		lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO";
		lStatement += " FROM CSSA, EVENTO E ,NOTIFICA N,MISURA_ALTERNATIVA MA";
		// lStatement += " WHERE  C.DESCRIZIONE = COMUNE ";
		lStatement += " WHERE MA.EVE_ID_EVENTO=E.ID_EVENTO ";
		lStatement += " AND E.FAS_SIE_ID_FASCICOLO_SIEP= " + aIdFascicolo;
		lStatement += " AND E.COD_TIPO_PROVVEDIMENTO='03' ";
		lStatement += " AND ID_CSSA=N.CSS_ID_CSSA ";
		// lStatement += "AND MA.CSS_ID_CSSA=N.CSS_ID_CSSA ";
		lStatement += "AND N.COD_TIPO_NOTIFICA='C' ";
		lStatement += "AND N.EVE_ID_EVENTO=E.ID_EVENTO ";

		setStatement(lStatement);
	}

	public void ricercaCSSAByIdFascicoloVerbaleNonFirmato(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO,  ";
		lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO  ";
		lStatement += " FROM CSSA, EVENTO E ,NOTIFICA N  ";
		lStatement += " WHERE   ";
		lStatement += " E.FAS_SIE_ID_FASCICOLO_SIEP='2003013'   ";
		lStatement += " AND E.COD_TIPO_PROVVEDIMENTO='03'  ";
		lStatement += " AND ID_CSSA=N.CSS_ID_CSSA  ";
		lStatement += " AND N.COD_TIPO_NOTIFICA='N'  ";
		lStatement += " AND N.EVE_ID_EVENTO=E.ID_EVENTO  ";

		setStatement(lStatement);
	}

	public void ricercaCSSAByKey(BigDecimal aKey) {
		String lStatement = new String();

		lStatement += " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO,";
		// lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO, C.COD_COMUNE COD_COMUNE";
		// lStatement += " FROM CSSA, COMUNE C";
		// lStatement += " WHERE C.DESCRIZIONE = COMUNE";
		lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO";
		lStatement += " FROM CSSA";
		lStatement += " WHERE ID_CSSA = " + aKey;

		setStatement(lStatement);
	}

	/**
	 * MEV10-s3: aggiunto metodo per estrarre il CSSA
	 * 
	 * @param aDescrComune
	 * @param aTipo
	 * @throws DAOException
	 */
	public void selCSSAByDescrComuneETipo(String aDescrComune, String aTipo) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO,";
		lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO";
		lStatement += " FROM CSSA";
		lStatement += " WHERE COMUNE = '" + StringUtils.convertSqlString(aDescrComune) + "'";
		lStatement += "   AND TIPO like '" + StringUtils.convertSqlString(aTipo) + "%'";
		setStatement(lStatement);
	}

}