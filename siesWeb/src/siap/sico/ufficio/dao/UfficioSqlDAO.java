package siap.sico.ufficio.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import f3b.util.StringUtils;
import siap.dao.SIAPSqlDAO;
import siap.sico.ufficio.model.UfficiProvvedimentoModel;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.ufficio.model.UfficioModel;

public class UfficioSqlDAO extends SIAPSqlDAO {

	public UfficioSqlDAO(Connection aCon) {
		super(aCon);
	}

	public void listaUfficiAccorpati(String tipoUfficio, String ufficioCompetente) throws DAOException {

		String lStatement = new String();

		lStatement += " SELECT A.COD_UFFICIO, A.COD_UFFICIO_NEW, A.COD_UFFICIO_COMPETENTE, A.COD_TIPO_UFFICIO, T.COD_TIPO_UFFICIO as COD_TIPO_UFFICIO_NEW, A.DESCRIZIONE, A.DESCRIZIONE_NEW_UFFICIO, A.COD_COMUNE, A.COD_PROVINCIA, A.COD_DISTRETTO_OLD, A.COD_DISTRETTO_NEW, A.INCR_PROGRESSIVO ";
		lStatement += " FROM UFFICIO_ACCORPATO A , UFFICIO T ";
		lStatement += " WHERE A.INCR_PROGRESSIVO is not null";
		lStatement += " AND A.COD_UFFICIO_NEW = T.COD_UFFICIO ";
		if (tipoUfficio != null && !tipoUfficio.equals("")) {
			lStatement += " AND A.COD_TIPO_UFFICIO = '" + tipoUfficio + "' ";
		}
		if (ufficioCompetente != null && !ufficioCompetente.equals("")) {
			lStatement += " AND A.COD_UFFICIO_NEW = '" + ufficioCompetente + "' ";
		}
		lStatement += " ORDER BY A.DESCRIZIONE";

		setStatement(lStatement);
	}

	/**
	 * Ricerca Ufficio Accorpato a partire dal Cod Accorpante e incremento Progressivo
	 *
	 * @param aCodUfficioAccorpante
	 * @param aIncremento
	 * @throws DAOException
	 */
	public void getUfficioAccorpatoByCodUffAccorpanteIncrement(String aCodUfficioAccorpante,
			String aIncremento) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT U.COD_UFFICIO, U.COD_TIPO_UFFICIO, U.COD_PROVINCIA, U.COD_COMUNE, U.COD_DISTRETTO ";
		lStatement += ", U.DATA_CARICAMENTO_REGE, U.COD_UFFICIO_COMPETENTE ";
		lStatement += ", U.INDIRIZZO, U.CAP CAP_UFFICIO, U.TELEFONO, U.FAX, U.E_MAIL ";
		lStatement += ", U.COD_OPERATORE_AGG, U.DATA_AGG, U.COD_UFFICIO_AGG ";
		lStatement += ", U.FLAG_ACCORP ";
		lStatement += ", CG_PROVICIA.RV_MEANING DESC_PROVINCIA ";
		lStatement += ", C.DESCRIZIONE DESC_COMUNE ";
		lStatement += ", TIPO_UFF.RV_MEANING DESC_TIPO_UFFICIO ";
		lStatement += " FROM UFFICIO U, UFFICIO_ACCORPATO UA, COMUNE C, CG_REF_CODES TIPO_UFF, CG_REF_CODES CG_PROVICIA ";
		lStatement += " WHERE U.COD_UFFICIO = UA.COD_UFFICIO ";
		// COMUNE
		lStatement += " AND U.COD_COMUNE = C.COD_COMUNE ";
		// PROVINCIA
		lStatement += " AND CG_PROVICIA.RV_DOMAIN = 'PROVINCIA' ";
		lStatement += " AND CG_PROVICIA.RV_LOW_VALUE = C.COD_PROVINCIA ";
		// TIPO UFFICIO
		lStatement += " AND TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND TIPO_UFF.RV_LOW_VALUE = U.COD_TIPO_UFFICIO ";
		//
		lStatement += " AND UA.INCR_PROGRESSIVO = '" + aIncremento + "' ";
		lStatement += " AND UA.COD_UFFICIO_NEW = '" + aCodUfficioAccorpante + "' ";

		setStatement(lStatement);
	}

	public void getUfficioAccorpanteByCodUffAccorpato(String aCodUfficioAccorpanto) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT U.COD_UFFICIO, U.COD_TIPO_UFFICIO, U.COD_PROVINCIA, U.COD_COMUNE, U.COD_DISTRETTO ";
		lStatement += ", U.DATA_CARICAMENTO_REGE, U.COD_UFFICIO_COMPETENTE ";
		lStatement += ", U.INDIRIZZO, U.CAP CAP_UFFICIO, U.TELEFONO, U.FAX, U.E_MAIL ";
		lStatement += ", U.COD_OPERATORE_AGG, U.DATA_AGG, U.COD_UFFICIO_AGG ";
		lStatement += ", U.FLAG_ACCORP ";
		lStatement += ", CG_PROVICIA.RV_MEANING DESC_PROVINCIA ";
		lStatement += ", C.DESCRIZIONE DESC_COMUNE ";
		lStatement += ", TIPO_UFF.RV_MEANING DESC_TIPO_UFFICIO ";
		lStatement += " FROM UFFICIO U, UFFICIO_ACCORPATO UA, COMUNE C, CG_REF_CODES TIPO_UFF, CG_REF_CODES CG_PROVICIA ";
		lStatement += " WHERE U.COD_UFFICIO = UA.COD_UFFICIO_NEW ";
		// COMUNE
		lStatement += " AND U.COD_COMUNE = C.COD_COMUNE ";
		// PROVINCIA
		lStatement += " AND CG_PROVICIA.RV_DOMAIN = 'PROVINCIA' ";
		lStatement += " AND CG_PROVICIA.RV_LOW_VALUE = C.COD_PROVINCIA ";
		// TIPO UFFICIO
		lStatement += " AND TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND TIPO_UFF.RV_LOW_VALUE = U.COD_TIPO_UFFICIO ";
		//
		lStatement += " AND UA.COD_UFFICIO = '" + aCodUfficioAccorpanto + "' ";

		setStatement(lStatement);
	}

	public void listaDistretti() throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT COD_UFFICIO, U.COD_PROVINCIA COD_PROVINCIA, R.RV_MEANING DESC_PROVINCIA, COD_TIPO_UFFICIO, U.DATA_CARICAMENTO_REGE DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, U.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL,";
		lStatement += " U.COD_COMUNE COD_COMUNE, C.DESCRIZIONE DESC_COMUNE, U.COD_DISTRETTO COD_DISTRETTO, R2.RV_MEANING DESC_TIPO_UFFICIO";
		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES R, CG_REF_CODES R2";
		lStatement += " WHERE COD_TIPO_UFFICIO = 'CAP' and C.COD_COMUNE = U.COD_COMUNE";
		lStatement += " AND R.RV_DOMAIN = 'PROVINCIA' and R.RV_LOW_VALUE = C.COD_PROVINCIA";
		lStatement += " AND R2.RV_DOMAIN = 'TIPO_UFFICIO' and R2.RV_LOW_VALUE = U.COD_TIPO_UFFICIO and C.COD_PROVINCIA = U.COD_PROVINCIA";
		lStatement += " ORDER BY C.DESCRIZIONE";

		setStatement(lStatement);
	}

	public void listaTDSM() throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT COD_UFFICIO, U.COD_PROVINCIA COD_PROVINCIA, R.RV_MEANING DESC_PROVINCIA, COD_TIPO_UFFICIO, U.DATA_CARICAMENTO_REGE DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, U.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL,";
		lStatement += " U.COD_COMUNE COD_COMUNE, C.DESCRIZIONE DESC_COMUNE, U.COD_DISTRETTO COD_DISTRETTO, R2.RV_MEANING DESC_TIPO_UFFICIO";
		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES R, CG_REF_CODES R2";
		lStatement += " WHERE COD_TIPO_UFFICIO = 'TDSM' and C.COD_COMUNE = U.COD_COMUNE";
		lStatement += " AND R.RV_DOMAIN = 'PROVINCIA' and R.RV_LOW_VALUE = C.COD_PROVINCIA";
		lStatement += " AND R2.RV_DOMAIN = 'TIPO_UFFICIO' and R2.RV_LOW_VALUE = U.COD_TIPO_UFFICIO and C.COD_PROVINCIA = U.COD_PROVINCIA";
		lStatement += " ORDER BY C.DESCRIZIONE";

		setStatement(lStatement);
	}

	public void listaPMM() throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT COD_UFFICIO, U.COD_PROVINCIA COD_PROVINCIA, R.RV_MEANING DESC_PROVINCIA, COD_TIPO_UFFICIO, U.DATA_CARICAMENTO_REGE DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, U.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL,";
		lStatement += " U.COD_COMUNE COD_COMUNE, C.DESCRIZIONE DESC_COMUNE, U.COD_DISTRETTO COD_DISTRETTO, R2.RV_MEANING DESC_TIPO_UFFICIO";
		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES R, CG_REF_CODES R2";
		lStatement += " WHERE COD_TIPO_UFFICIO = 'PMM' and C.COD_COMUNE = U.COD_COMUNE";
		lStatement += " AND R.RV_DOMAIN = 'PROVINCIA' and R.RV_LOW_VALUE = C.COD_PROVINCIA";
		lStatement += " AND R2.RV_DOMAIN = 'TIPO_UFFICIO' and R2.RV_LOW_VALUE = U.COD_TIPO_UFFICIO and C.COD_PROVINCIA = U.COD_PROVINCIA";
		lStatement += " ORDER BY C.DESCRIZIONE";

		setStatement(lStatement);
	}

	// public void listaUSSM ()
	// throws DAOException
	// {
	// String lStatement = new String();
	//
	// // lStatement +=
	// " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO,";
	// // lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO";
	// // lStatement += " FROM CSSA";
	// // lStatement += " WHERE ";
	// // lStatement += " TIPO LIKE 'USSM%' ";
	// // lStatement += " AND ID_CSSA < 9999"; // l'ID_CSSA = 9999 corrisponde
	// al record '-'
	// // lStatement += " ORDER BY COMUNE";
	//
	// lStatement +=
	// " SELECT ID_CSSA, TIPO, COMUNE, INDIRIZZO, E_MAIL, FAX, TEL, INCARICO,";
	// lStatement += " TITOLO, NOME, COGNOME, DATA_CARICAMENTO";
	// lStatement += " FROM CSSA";
	// lStatement += " WHERE ";
	// lStatement += " TIPO = 'USSM' ";
	// lStatement += " ORDER BY ID_CSSA";
	//
	// setStatement( lStatement );
	// }

	public void listaTipoUfficiDescr() throws DAOException {
		String lStatement = new String();

		lStatement += "SELECT DISTINCT A.DESCR_COMUNE||' ('||B.COD_PROVINCIA||') - '|| DESCR_TIPO_UFFICIO DESCR, A.COD_UFFICIO ";
		lStatement += "FROM UFFICIO_DESCR A JOIN UFFICIO B ON A.COD_UFFICIO = B.COD_UFFICIO WHERE A.COD_UFFICIO != '-' ORDER BY 1";

		setStatement(lStatement);
	}

	public void ricercaTDSByFascicolo(BigDecimal aFascicolo) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT COD_UFFICIO, U.COD_PROVINCIA COD_PROVINCIA, R.RV_MEANING DESC_PROVINCIA, COD_TIPO_UFFICIO, U.DATA_CARICAMENTO_REGE DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, U.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL,";
		lStatement += " U.COD_COMUNE COD_COMUNE, C.DESCRIZIONE DESC_COMUNE, U.COD_DISTRETTO COD_DISTRETTO, R2.RV_MEANING DESC_TIPO_UFFICIO";
		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES R, CG_REF_CODES R2,NOTIFICA N, EVENTO E , MISURA_ALTERNATIVA MA ";
		lStatement += " WHERE ";
		lStatement += " R.RV_DOMAIN = 'PROVINCIA' and R.RV_LOW_VALUE = C.COD_PROVINCIA";
		lStatement += " AND R2.RV_DOMAIN = 'TIPO_UFFICIO' AND R2.RV_LOW_VALUE = U.COD_TIPO_UFFICIO AND C.COD_PROVINCIA = U.COD_PROVINCIA";
		lStatement += " AND E.FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicolo;
		lStatement += " AND COD_TIPO_PROVVEDIMENTO='03' ";
		lStatement += " AND E.ID_EVENTO = N.EVE_ID_EVENTO";
		lStatement += " AND N.COD_TIPO_NOTIFICA='C' ";
		lStatement += " AND N.UFF_COD_UFFICIO= U.COD_UFFICIO ";
		lStatement += " AND MA.EVE_ID_EVENTO = N.EVE_ID_EVENTO ";

		setStatement(lStatement);
	}

	public void ricercaSedeUfficioEmittenteByFascicolo(BigDecimal aFascicolo) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT COD_UFFICIO, U.COD_PROVINCIA COD_PROVINCIA, R.RV_MEANING DESC_PROVINCIA, COD_TIPO_UFFICIO, U.DATA_CARICAMENTO_REGE DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, U.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL,";
		lStatement += " U.COD_COMUNE COD_COMUNE, C.DESCRIZIONE DESC_COMUNE, U.COD_DISTRETTO COD_DISTRETTO, R2.RV_MEANING DESC_TIPO_UFFICIO";
		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES R, CG_REF_CODES R2, EVENTO E , MISURA_ALTERNATIVA MA ";
		lStatement += " WHERE ";
		lStatement += " R.RV_DOMAIN = 'PROVINCIA' and R.RV_LOW_VALUE = C.COD_PROVINCIA";
		lStatement += " AND R2.RV_DOMAIN = 'TIPO_UFFICIO' AND R2.RV_LOW_VALUE = U.COD_TIPO_UFFICIO AND C.COD_PROVINCIA = U.COD_PROVINCIA";
		lStatement += " AND E.FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicolo;
		lStatement += " AND MA.FAS_SIE_ID_FASCICOLO_SIEP = " + aFascicolo;
		lStatement += " AND COD_TIPO_PROVVEDIMENTO='03' ";
		lStatement += " AND E.ID_EVENTO = MA.EVE_ID_EVENTO ";
		lStatement += " AND MA.COD_UFFICIO_SORVEGLIANZA= U.COD_UFFICIO ";

		setStatement(lStatement);
	}

	public void listaUfficiDistretto(String codDistretto) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT COD_UFFICIO, U.COD_PROVINCIA COD_PROVINCIA, R.RV_MEANING DESC_PROVINCIA, COD_TIPO_UFFICIO, U.DATA_CARICAMENTO_REGE DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, U.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL,";
		lStatement += " U.COD_COMUNE COD_COMUNE, C.DESCRIZIONE DESC_COMUNE, U.COD_DISTRETTO COD_DISTRETTO, R2.RV_MEANING DESC_TIPO_UFFICIO";
		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES R, CG_REF_CODES R2";
		lStatement += " WHERE COD_TIPO_UFFICIO = 'PM' AND COD_DISTRETTO = '" + codDistretto + "'";
		lStatement += " AND C.COD_COMUNE = U.COD_COMUNE";
		lStatement += " AND R.RV_DOMAIN = 'PROVINCIA' and R.RV_LOW_VALUE = C.COD_PROVINCIA";
		lStatement += " AND R2.RV_DOMAIN = 'TIPO_UFFICIO' and R2.RV_LOW_VALUE = U.COD_TIPO_UFFICIO and C.COD_PROVINCIA = U.COD_PROVINCIA";
		lStatement += " AND nvl(U.FLAG_ACCORP,'N') != 'S'";
		lStatement += " ORDER BY C.DESCRIZIONE";

		setStatement(lStatement);
	}

	public void listaUfficiMinorDistretto(String codDistretto) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT COD_UFFICIO, U.COD_PROVINCIA COD_PROVINCIA, R.RV_MEANING DESC_PROVINCIA, COD_TIPO_UFFICIO, U.DATA_CARICAMENTO_REGE DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, U.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL,";
		lStatement += " U.COD_COMUNE COD_COMUNE, C.DESCRIZIONE DESC_COMUNE, U.COD_DISTRETTO COD_DISTRETTO, R2.RV_MEANING DESC_TIPO_UFFICIO";
		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES R, CG_REF_CODES R2";
		lStatement += " WHERE COD_TIPO_UFFICIO = 'PMM' AND COD_DISTRETTO = '" + codDistretto + "'";
		lStatement += " AND C.COD_COMUNE = U.COD_COMUNE";
		lStatement += " AND R.RV_DOMAIN = 'PROVINCIA' and R.RV_LOW_VALUE = C.COD_PROVINCIA";
		lStatement += " AND R2.RV_DOMAIN = 'TIPO_UFFICIO' and R2.RV_LOW_VALUE = U.COD_TIPO_UFFICIO and C.COD_PROVINCIA = U.COD_PROVINCIA";
		lStatement += " AND nvl(U.FLAG_ACCORP,'N') != 'S'";
		lStatement += " ORDER BY C.DESCRIZIONE";

		setStatement(lStatement);
	}

	public void listaUfficiCompletaDistretto(String codDistretto) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT COD_UFFICIO, U.COD_PROVINCIA COD_PROVINCIA, R.RV_MEANING DESC_PROVINCIA, COD_TIPO_UFFICIO, U.DATA_CARICAMENTO_REGE DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, U.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL,";
		lStatement += " U.COD_COMUNE COD_COMUNE, C.DESCRIZIONE DESC_COMUNE, U.COD_DISTRETTO COD_DISTRETTO, R2.RV_MEANING DESC_TIPO_UFFICIO";
		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES R, CG_REF_CODES R2";
		lStatement += " WHERE COD_DISTRETTO = '" + codDistretto + "'";
		lStatement += " AND C.COD_COMUNE = U.COD_COMUNE";
		lStatement += " AND R.RV_DOMAIN = 'PROVINCIA' and R.RV_LOW_VALUE = C.COD_PROVINCIA";
		lStatement += " AND R2.RV_DOMAIN = 'TIPO_UFFICIO' and R2.RV_LOW_VALUE = U.COD_TIPO_UFFICIO and C.COD_PROVINCIA = U.COD_PROVINCIA";
		lStatement += " ORDER BY R2.RV_MEANING";

		setStatement(lStatement);
	}

	public void listaUfficiCompletaDistrettoAbilitatiLogin(String codDistretto) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT COD_UFFICIO, U.COD_PROVINCIA COD_PROVINCIA, R.RV_MEANING DESC_PROVINCIA, COD_TIPO_UFFICIO, U.DATA_CARICAMENTO_REGE DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, U.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL,";
		lStatement += " U.COD_COMUNE COD_COMUNE, C.DESCRIZIONE DESC_COMUNE, U.COD_DISTRETTO COD_DISTRETTO, R2.RV_MEANING DESC_TIPO_UFFICIO";
		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES R, CG_REF_CODES R2";
		lStatement += " WHERE COD_DISTRETTO = '" + codDistretto + "'";
		lStatement += " AND C.COD_COMUNE = U.COD_COMUNE";
		lStatement += " AND R.RV_DOMAIN = 'PROVINCIA' and R.RV_LOW_VALUE = C.COD_PROVINCIA";
		lStatement += " AND R2.RV_DOMAIN = 'UFFICIO_LOGIN' and R2.RV_LOW_VALUE = U.COD_TIPO_UFFICIO and C.COD_PROVINCIA = U.COD_PROVINCIA";
		lStatement += " ORDER BY R2.RV_MEANING";

		setStatement(lStatement);
	}

	public GenericModel getModel() throws DAOException {
		UfficioModel lUff = new UfficioModel();

		lUff.setCodComune(getString("COD_COMUNE"));
		lUff.setCodDistretto(getString("COD_DISTRETTO"));
		lUff.setCodProvincia(getString("COD_PROVINCIA"));
		lUff.setCodTipoUfficio(getString("COD_TIPO_UFFICIO"));
		lUff.setCodUfficio(getString("COD_UFFICIO"));
		lUff.setDescrProvincia(getString("DESC_PROVINCIA"));
		lUff.setDescrComune(getString("DESC_COMUNE"));
		lUff.setDescrTipoUfficio(getString("DESC_TIPO_UFFICIO"));
		lUff.setDateCarimentoRege(getDate("DATA_CARICAMENTO_REGE"));
		lUff.setIndirizzo(getString("INDIRIZZO"));
		lUff.setCap(getString("CAP_UFFICIO"));
		lUff.setTelefono(getString("TELEFONO"));
		lUff.setFax(getString("FAX"));
		lUff.setEMail(getString("E_MAIL"));

		return lUff;
	}

	public GenericModel getUfficioAccorpatoModel() throws DAOException {
		UfficioAccorpatoModel lUff = new UfficioAccorpatoModel();

		lUff.setCodUfficio(getString("COD_UFFICIO"));
		lUff.setCodUfficioNew(getString("COD_UFFICIO_NEW"));
		lUff.setCodUfficioCompetente(getString("COD_UFFICIO_COMPETENTE"));
		lUff.setCodTipoUfficio(getString("COD_TIPO_UFFICIO"));
		lUff.setCodTipoUfficioNew(getString("COD_TIPO_UFFICIO_NEW"));
		lUff.setDescrizione(getString("DESCRIZIONE"));
		lUff.setDescrizioneNewUfficio(getString("DESCRIZIONE_NEW_UFFICIO"));
		lUff.setCodComune(getString("COD_COMUNE"));
		lUff.setCodProvincia(getString("COD_PROVINCIA"));
		lUff.setCodDistrettoOld(getString("COD_DISTRETTO_OLD"));
		lUff.setCodDistrettoNew(getString("COD_DISTRETTO_NEW"));
		lUff.setIncrProgressivo(getString("INCR_PROGRESSIVO"));

		return lUff;
	}

	public GenericModel getUfficiProvvedimentoModel() throws DAOException {
		UfficiProvvedimentoModel lUff = new UfficiProvvedimentoModel();

		lUff.setCodComune(getString("COD_COMUNE"));
		lUff.setCodDistretto(getString("COD_DISTRETTO"));
		lUff.setCodTipoUfficio(getString("COD_TIPO_UFFICIO"));
		lUff.setCodUfficio(getString("COD_UFFICIO"));
		lUff.setDescrComune(getString("DESC_COMUNE"));
		lUff.setDescrTipoUfficio(getString("DESC_TIPO_UFFICIO"));
		lUff.setChiaveAnnoSiep(getString("CHIAVE_ANNO"));
		lUff.setChiaveProgrSiep(getString("CHIAVE_PROGR"));

		// MEV_39 03/01/2018
		lUff.setFasSiepOrigine(getString("FAS_SIEP_ORIG"));

		return lUff;
	}

	public void listaUDS() throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT COD_UFFICIO, U.COD_PROVINCIA COD_PROVINCIA, R.RV_MEANING DESC_PROVINCIA, COD_TIPO_UFFICIO, U.DATA_CARICAMENTO_REGE DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, U.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL,";
		lStatement += " U.COD_COMUNE COD_COMUNE, C.DESCRIZIONE DESC_COMUNE, U.COD_DISTRETTO COD_DISTRETTO, R2.RV_MEANING DESC_TIPO_UFFICIO";
		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES R, CG_REF_CODES R2";
		lStatement += " WHERE COD_TIPO_UFFICIO = 'UDS' and C.COD_COMUNE = U.COD_COMUNE";
		lStatement += " AND R.RV_DOMAIN = 'PROVINCIA' and R.RV_LOW_VALUE = C.COD_PROVINCIA";
		lStatement += " AND R2.RV_DOMAIN = 'TIPO_UFFICIO' and R2.RV_LOW_VALUE = U.COD_TIPO_UFFICIO and C.COD_PROVINCIA = U.COD_PROVINCIA";
		lStatement += " ORDER BY C.DESCRIZIONE";

		setStatement(lStatement);
	}

	public void listaUDSM() throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT COD_UFFICIO, U.COD_PROVINCIA COD_PROVINCIA, R.RV_MEANING DESC_PROVINCIA, COD_TIPO_UFFICIO, U.DATA_CARICAMENTO_REGE DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, U.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL,";
		lStatement += " U.COD_COMUNE COD_COMUNE, C.DESCRIZIONE DESC_COMUNE, U.COD_DISTRETTO COD_DISTRETTO, R2.RV_MEANING DESC_TIPO_UFFICIO";
		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES R, CG_REF_CODES R2";
		lStatement += " WHERE COD_TIPO_UFFICIO = 'UDSM' and C.COD_COMUNE = U.COD_COMUNE";
		lStatement += " AND R.RV_DOMAIN = 'PROVINCIA' and R.RV_LOW_VALUE = C.COD_PROVINCIA";
		lStatement += " AND R2.RV_DOMAIN = 'TIPO_UFFICIO' and R2.RV_LOW_VALUE = U.COD_TIPO_UFFICIO and C.COD_PROVINCIA = U.COD_PROVINCIA";
		lStatement += " ORDER BY C.DESCRIZIONE";

		setStatement(lStatement);
	}

	public void getDescTipoUffByCodUfficio(String CodUfficio) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT DESCR_TIPO_UFFICIO || ' ' || DESCR_COMUNE || ' (' || COD_PROVINCIA || ')' DESC_UFFICIO ";
		lStatement += " FROM UFFICIO_DESCR ";
		lStatement += " WHERE COD_UFFICIO = '" + StringUtils.convertSqlString(CodUfficio) + "'";

		setStatement(lStatement);
	}

	public void getCodUfficioByDescrComune(String aDescrComune) throws DAOException {
		String lStatement = new String();

		lStatement += " SELECT COD_UFFICIO ";
		lStatement += " FROM UFFICIO U, COMUNE C ";
		lStatement += " WHERE COD_TIPO_UFFICIO = 'PM' AND C.DESCRIZIONE = '"
				+ StringUtils.convertSqlString(aDescrComune) + "'";
		lStatement += " AND C.COD_COMUNE = U.COD_COMUNE";

		setStatement(lStatement);
	}

	public void selUfficioByCodTipoUffDescrComune(String aCodTipoUfficio, String aDescrComune)
			throws DAOException {

		String lStatement = new String();
		// 11/05/2010 Sofisticazione per Tipo Ufficio UEPE/ UEPESS)
		String lCondizioneUfficio = (aCodTipoUfficio.indexOf("UEPE") >= 0 ? " like '" + aCodTipoUfficio + "%'"
				: " = '" + aCodTipoUfficio + "'");

		lStatement += "SELECT UFF.COD_COMUNE, COM.DESCRIZIONE DESC_COMUNE,";
		lStatement += " UFF.COD_DISTRETTO,";
		lStatement += " UFF.COD_PROVINCIA, DESC_PROVINCIA.RV_MEANING DESC_PROVINCIA,";
		lStatement += " UFF.COD_TIPO_UFFICIO, DESC_TIPO_UFFICIO.RV_MEANING DESC_TIPO_UFFICIO,";
		lStatement += " UFF.COD_UFFICIO, UFF.DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, UFF.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL";
		// lStatement +=
		// " FROM UFFICIO UFF, COMUNE COM, CG_REF_CODES DESC_PROVINCIA, CG_REF_CODES DESC_TIPO_UFFICIO";
		lStatement += " FROM CG_REF_CODES DESC_TIPO_UFFICIO, CG_REF_CODES DESC_PROVINCIA, UFFICIO UFF, COMUNE COM ";
		// lStatement +=
		// " WHERE (UFF.COD_TIPO_UFFICIO = '"+aCodTipoUfficio+"')";
		lStatement += " WHERE (UFF.COD_TIPO_UFFICIO " + lCondizioneUfficio + ")";
		lStatement += " AND (COM.DESCRIZIONE = '" + StringUtils.convertSqlString(aDescrComune) + "')";
		lStatement += " AND (COM.COD_COMUNE = UFF.COD_COMUNE)";
		lStatement += " AND (DESC_PROVINCIA.RV_DOMAIN = 'PROVINCIA' AND UFF.COD_PROVINCIA = DESC_PROVINCIA.RV_LOW_VALUE)";
		lStatement += " AND (DESC_TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO' AND UFF.COD_TIPO_UFFICIO= DESC_TIPO_UFFICIO.RV_LOW_VALUE)";

		setStatement(lStatement);
	}

	public void selUfficioByCodTipoUffCodComune(String aCodTipoUfficio, String aCodComune)
			throws DAOException {
		String lStatement = new String();

		String lCondizioneUfficio = (aCodTipoUfficio.indexOf("UEPE") >= 0 ? " like '" + aCodTipoUfficio + "%'"
				: " = '" + aCodTipoUfficio + "'");

		lStatement += "SELECT UFF.COD_COMUNE, COM.DESCRIZIONE DESC_COMUNE,";
		lStatement += " UFF.COD_DISTRETTO,";
		lStatement += " UFF.COD_PROVINCIA, DESC_PROVINCIA.RV_MEANING DESC_PROVINCIA,";
		lStatement += " UFF.COD_TIPO_UFFICIO, DESC_TIPO_UFFICIO.RV_MEANING DESC_TIPO_UFFICIO,";
		lStatement += " UFF.COD_UFFICIO, UFF.DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, UFF.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL";
		lStatement += " FROM CG_REF_CODES DESC_TIPO_UFFICIO, CG_REF_CODES DESC_PROVINCIA, UFFICIO UFF, COMUNE COM ";
		lStatement += " WHERE (UFF.COD_TIPO_UFFICIO " + lCondizioneUfficio + ")";
		lStatement += " AND (COM.COD_COMUNE = '" + aCodComune + "')";
		lStatement += " AND (COM.COD_COMUNE = UFF.COD_COMUNE)";
		lStatement += " AND (DESC_PROVINCIA.RV_DOMAIN = 'PROVINCIA' AND UFF.COD_PROVINCIA = DESC_PROVINCIA.RV_LOW_VALUE)";
		lStatement += " AND (DESC_TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO' AND UFF.COD_TIPO_UFFICIO= DESC_TIPO_UFFICIO.RV_LOW_VALUE)";

		setStatement(lStatement);
	}

	public void selUfficioByCod(String aCodUfficio) throws DAOException {

		String lStatement = new String();

		lStatement += "SELECT UFF.COD_COMUNE, COM.DESCRIZIONE DESC_COMUNE,";
		lStatement += " UFF.COD_DISTRETTO,";
		lStatement += " UFF.COD_PROVINCIA, DESC_PROVINCIA.RV_MEANING DESC_PROVINCIA,";
		lStatement += " UFF.COD_TIPO_UFFICIO, DESC_TIPO_UFFICIO.RV_MEANING DESC_TIPO_UFFICIO,";
		lStatement += " UFF.COD_UFFICIO, UFF.DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, UFF.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL";
		lStatement += " FROM UFFICIO UFF, COMUNE COM,";
		lStatement += " CG_REF_CODES DESC_PROVINCIA, CG_REF_CODES DESC_TIPO_UFFICIO";
		lStatement += " WHERE (UFF.COD_UFFICIO = '" + aCodUfficio + "')";
		lStatement += " AND (COM.COD_COMUNE = UFF.COD_COMUNE)";
		lStatement += " AND (DESC_PROVINCIA.RV_DOMAIN = 'PROVINCIA' AND UFF.COD_PROVINCIA = DESC_PROVINCIA.RV_LOW_VALUE)";
		lStatement += " AND (DESC_TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO' AND UFF.COD_TIPO_UFFICIO= DESC_TIPO_UFFICIO.RV_LOW_VALUE)";

		setStatement(lStatement);
	}

	public void listaUfficiByCodTipoUfficio(String aCodTipoUfficio, String aFlagAccorp) throws DAOException {
		String lStatement = new String();
		// 11/05/2010 Sofisticazione per Tipo Ufficio UEPE/ UEPESS)
		String lCondizioneUfficio = (aCodTipoUfficio.indexOf("UEPE") >= 0 ? " like '" + aCodTipoUfficio + "%'"
				: " = '" + aCodTipoUfficio + "'");

		lStatement += "SELECT UFF.COD_COMUNE, COM.DESCRIZIONE DESC_COMUNE,";
		lStatement += " UFF.COD_DISTRETTO,";
		lStatement += " UFF.COD_PROVINCIA, DESC_PROVINCIA.RV_MEANING DESC_PROVINCIA,";
		lStatement += " UFF.COD_TIPO_UFFICIO, DESC_TIPO_UFFICIO.RV_MEANING DESC_TIPO_UFFICIO,";
		lStatement += " UFF.COD_UFFICIO, UFF.DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, UFF.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL";
		lStatement += " FROM UFFICIO UFF, COMUNE COM,";
		lStatement += " CG_REF_CODES DESC_PROVINCIA, CG_REF_CODES DESC_TIPO_UFFICIO";
		// lStatement +=
		// " WHERE (UFF.COD_TIPO_UFFICIO = '"+aCodTipoUfficio+"')";
		lStatement += " WHERE (UFF.COD_TIPO_UFFICIO " + lCondizioneUfficio + ")";
		lStatement += " AND (COM.COD_COMUNE = UFF.COD_COMUNE)";
		lStatement += " AND (DESC_PROVINCIA.RV_DOMAIN = 'PROVINCIA' AND UFF.COD_PROVINCIA = DESC_PROVINCIA.RV_LOW_VALUE)";
		lStatement += " AND (DESC_TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO' AND UFF.COD_TIPO_UFFICIO= DESC_TIPO_UFFICIO.RV_LOW_VALUE)";
		if (!"S".equals(aFlagAccorp))
			lStatement += " AND nvl(UFF.FLAG_ACCORP,'N') != 'S'";
		lStatement += " ORDER BY DESC_COMUNE";

		setStatement(lStatement);
	}

	public void listaUfficiByCodTipoUfficioUfficiCumulo(String aCodTipoUfficio) throws DAOException {

		String lStatement = new String();

		lStatement += "SELECT UFF.COD_COMUNE, COM.DESCRIZIONE DESC_COMUNE,";
		lStatement += " UFF.COD_DISTRETTO,";
		lStatement += " UFF.COD_PROVINCIA, DESC_PROVINCIA.RV_MEANING DESC_PROVINCIA,";
		lStatement += " UFF.COD_TIPO_UFFICIO, DESC_TIPO_UFFICIO.RV_MEANING DESC_TIPO_UFFICIO,";
		lStatement += " UFF.COD_UFFICIO, UFF.DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, UFF.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL";
		lStatement += " FROM UFFICIO UFF, COMUNE COM,";
		lStatement += " CG_REF_CODES DESC_PROVINCIA, CG_REF_CODES DESC_TIPO_UFFICIO";
		lStatement += " WHERE (UFF.COD_TIPO_UFFICIO = '" + aCodTipoUfficio + "')";
		lStatement += " AND (COM.COD_COMUNE = UFF.COD_COMUNE)";
		lStatement += " AND (DESC_PROVINCIA.RV_DOMAIN = 'PROVINCIA' AND UFF.COD_PROVINCIA = DESC_PROVINCIA.RV_LOW_VALUE)";
		lStatement += " AND (DESC_TIPO_UFFICIO.RV_DOMAIN = 'TIPO_UFFICIO_CUMULO' AND UFF.COD_TIPO_UFFICIO= DESC_TIPO_UFFICIO.RV_LOW_VALUE)";
		lStatement += " ORDER BY DESC_COMUNE";

		setStatement(lStatement);
	}

	// Query per la ricerca del TDS a cui afferisce un UDS collegato oppure
	// dell'UDS relativo al TDS collegato.
	// I parametri passati sono relativi all'ufficio connesso, tranne
	// aCodTipoUfficio.
	public void getUfficioUDSTDS(String aCodDistretto, String aCodTipoUfficio, String aCodComune)
			throws DAOException {

		String lStatement = new String();

		lStatement += "SELECT COD_UFFICIO, U.COD_PROVINCIA COD_PROVINCIA, R.RV_MEANING DESC_PROVINCIA, COD_TIPO_UFFICIO,";
		lStatement += " U.DATA_CARICAMENTO_REGE,INDIRIZZO, U.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL,U.COD_COMUNE COD_COMUNE,";
		lStatement += " C.DESCRIZIONE DESC_COMUNE, U.COD_DISTRETTO COD_DISTRETTO, R.RV_MEANING DESC_TIPO_UFFICIO";
		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES R";
		lStatement += " WHERE (U.COD_DISTRETTO = '" + aCodDistretto + "')";
		lStatement += " AND (U.COD_TIPO_UFFICIO = '" + aCodTipoUfficio + "')";
		lStatement += " AND (R.RV_DOMAIN = 'TIPO_UFFICIO')";
		lStatement += " AND (R.RV_LOW_VALUE = U.COD_TIPO_UFFICIO)";
		lStatement += " AND (C.COD_PROVINCIA = U.COD_PROVINCIA)";
		lStatement += " AND (C.COD_COMUNE = U.COD_COMUNE)";
		// MERGE v10 COLLAUDO: aggiunta or condition per i minori
		if ("UDS".equals(aCodTipoUfficio) || "UDSM".equals(aCodTipoUfficio))
			lStatement += " AND (U.COD_COMUNE = '" + aCodComune + "')";

		setStatement(lStatement);
	}

	public void listaUfficiPerDistretto(String aDistretto, String aComune, String aCodUfficio)
			throws DAOException {

		String lStatement = new String();
		lStatement += " SELECT DISTINCT (c.cod_comune), c.descrizione DESC_COMUNE, f.cod_tipo_ufficio,f.COD_DISTRETTO, f.CAP CAP_UFFICIO, f.COD_PROVINCIA, ";
		lStatement += "cg.RV_MEANING DESC_PROVINCIA ,  f.COD_UFFICIO , f.DATA_CARICAMENTO_REGE , f.INDIRIZZO , f.TELEFONO , f.FAX , f.E_MAIL , cg.rv_meaning DESC_TIPO_UFFICIO ";
		lStatement += " FROM COMUNE c , UFFICIO f , CG_REF_CODES cg ";
		lStatement += "  WHERE ";
		if (!aComune.equals("")) {
			lStatement += " c.DESCRIZIONE LIKE '" + aComune + "%'";

		} else {
			lStatement += " F.COD_DISTRETTO = '" + aDistretto + "'";
		}
		lStatement += " AND C.COD_COMUNE = F.COD_COMUNE   ";
		lStatement += "  AND CG.RV_LOW_VALUE = F.COD_TIPO_UFFICIO ";
		lStatement += " AND CG.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += "  AND F.COD_TIPO_UFFICIO = '" + aCodUfficio + "'";

		setStatement(lStatement);

	}

	public void getPrefissoUtenteUfficioLogin(String aCodDistretto) throws DAOException {

		String lStatement = new String();
		lStatement = "SELECT RV_ABBREVIATION FROM CG_REF_CODES,UFFICIO WHERE ";
		lStatement += " UFFICIO.COD_TIPO_UFFICIO=CG_REF_CODES.RV_LOW_VALUE ";
		lStatement += " AND UFFICIO.COD_UFFICIO='" + aCodDistretto + "'";
		lStatement += " AND CG_REF_CODES.RV_DOMAIN='UFFICIO_LOGIN'";
		setStatement(lStatement);
	}

	// Query per la ricerca degli uffici a cui interessa la ricezione di un
	// provvedimento di ordinanza/decreto.
	//
	// 1) Attraverso il parametro FASC.aIdFascicoloSius, si selezionano tutti i
	// FASCICOLI_SIUS dell'ufficio (uguale CHIAVE_UFFICIO)
	// aventi i campi Anno_S1, Progr_S1, Cod_Tipo_Registro di
	// Generale_Procedimento
	// uguali a quelli di FASC e aventi FAS_SIE_ID_FASCICOLO_SIEP differenti dal
	// valore del campo equivalente di FASC.
	// 2) Il Fascicolo su cui ho emesso il provvedimento fa riferimento ad un
	// procedimento unificante (Numero_Fascicoli_Unificati > 0)
	// Attraverso il parametro FASC.aIdFascicoloSius, si selezionano tutti i
	// FASCICOLI_SIUS dell'ufficio (uguale CHIAVE_UFFICIO)
	// aventi il campo FAS_SIU_ID_FASCICOLO_SIUS uguale ID_FASCICOLO_SIUS di
	// FASC
	// e aventi FAS_SIE_ID_FASCICOLO_SIEP differente dal valore del campo
	// equivalente di FASC.
	//
	// Per ciascun Fascicolo SIUS selezionato si presentano i seguenti dati:
	// Anno e Numero SIEP, Cod_Tipo_Ufficio (PM, PGCAP, GP), Descrizione della
	// Sede.

	public void getUfficiInteressatiProvvedimento(BigDecimal aIdFascicoloSius, BigDecimal aNumFascUnificati)
			throws DAOException {

		String lStatement = new String();

		lStatement += "SELECT DISTINCT U.COD_UFFICIO, U.COD_DISTRETTO, U.COD_COMUNE, U.COD_TIPO_UFFICIO,C.DESCRIZIONE DESC_COMUNE, DES_TIPO_UFF.RV_MEANING DESC_TIPO_UFFICIO,";
		lStatement += " FSIEP.CHIAVE_ANNO, FSIEP.CHIAVE_PROGR";
		// MEV_39 03/01/2018
		lStatement += " ,FASC.FAS_SIE_ID_FASCICOLO_SIEP FAS_SIEP_ORIG ";

		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES DES_TIPO_UFF,";
		lStatement += " FASCICOLO_SIUS FASC,GENERALE_PROCEDIMENTO GP,";
		lStatement += " FASCICOLO_SIUS FASC2,GENERALE_PROCEDIMENTO GP2, FASCICOLO_SIEP FSIEP";

		lStatement += " WHERE FASC.ID_FASCICOLO_SIUS = '" + aIdFascicoloSius + "'";
		lStatement += " AND FASC.CHIAVE_UFFICIO = FASC2.CHIAVE_UFFICIO";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND FASC2.ID_FASCICOLO_SIUS = GP2.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND GP.ANNO_S1 = GP2.ANNO_S1";
		lStatement += " AND GP.PROGR_S1 = GP2.PROGR_S1";
		lStatement += " AND GP.COD_TIPO_REGISTRO = GP2.COD_TIPO_REGISTRO";
		lStatement += " AND (FASC.FAS_SIE_ID_FASCICOLO_SIEP = FSIEP.ID_FASCICOLO_SIEP";
		lStatement += "      OR (FASC2.FAS_SIE_ID_FASCICOLO_SIEP <> FASC.FAS_SIE_ID_FASCICOLO_SIEP";
		lStatement += "     AND FASC2.FAS_SIE_ID_FASCICOLO_SIEP = FSIEP.ID_FASCICOLO_SIEP))";
		lStatement += " AND FSIEP.CHIAVE_UFFICIO = U.COD_UFFICIO";
		lStatement += " AND C.COD_COMUNE = U.COD_COMUNE";
		lStatement += " AND DES_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' and DES_TIPO_UFF.RV_LOW_VALUE = U.COD_TIPO_UFFICIO";

		if (aNumFascUnificati != null && aNumFascUnificati.intValue() > 0) {
			lStatement += " Union ";
			lStatement += "(SELECT DISTINCT U.COD_UFFICIO, U.COD_DISTRETTO, U.COD_COMUNE, U.COD_TIPO_UFFICIO, C.DESCRIZIONE DESC_COMUNE, DES_TIPO_UFF.RV_MEANING DESC_TIPO_UFFICIO,";
			lStatement += " FSIEP.CHIAVE_ANNO, FSIEP.CHIAVE_PROGR";
			// MEV_39 03/01/2018
			lStatement += " , null FAS_SIEP_ORIG";

			lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES DES_TIPO_UFF,";
			lStatement += " FASCICOLO_SIUS FASC,GENERALE_PROCEDIMENTO GP, FASCICOLO_SIUS FASC2, FASCICOLO_SIEP FSIEP";
			lStatement += " WHERE FASC.ID_FASCICOLO_SIUS = '" + aIdFascicoloSius + "'";
			lStatement += " AND FASC.CHIAVE_UFFICIO = FASC2.CHIAVE_UFFICIO";
			lStatement += " AND FASC.ID_FASCICOLO_SIUS = GP.FAS_SIU_ID_FASCICOLO_SIUS";
			lStatement += " AND FASC.ID_FASCICOLO_SIUS = FASC2.FAS_SIU_ID_FASCICOLO_SIUS";
			lStatement += " AND FASC2.FAS_SIE_ID_FASCICOLO_SIEP = FSIEP.ID_FASCICOLO_SIEP";
			lStatement += " AND FASC.FAS_SIE_ID_FASCICOLO_SIEP <> FASC2.FAS_SIE_ID_FASCICOLO_SIEP";
			lStatement += " AND FSIEP.CHIAVE_UFFICIO = U.COD_UFFICIO";
			lStatement += " AND C.COD_COMUNE = U.COD_COMUNE";
			lStatement += " AND DES_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' and DES_TIPO_UFF.RV_LOW_VALUE = U.COD_TIPO_UFFICIO )";
		}
		// Aggiunti i destinatari legati ai "RIFERIMENTI FASCICOLO SIEP"
		lStatement += " Union ";
		lStatement += "(SELECT DISTINCT U.COD_UFFICIO, U.COD_DISTRETTO, U.COD_COMUNE, U.COD_TIPO_UFFICIO, C.DESCRIZIONE DESC_COMUNE, DES_TIPO_UFF.RV_MEANING DESC_TIPO_UFFICIO,";
		lStatement += " RFSIEP.ANNO_FASCICOLO_SIEP, RFSIEP.PROGR_FASCICOLO_SIEP";

		// MEV_39 03/01/2018
		lStatement += " , null FAS_SIEP_ORIG";

		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES DES_TIPO_UFF,";
		lStatement += " FASCICOLO_SIUS FASC, RIFERIMENTO_FASCICOLO_SIEP RFSIEP";
		lStatement += " WHERE FASC.ID_FASCICOLO_SIUS = '" + aIdFascicoloSius + "'";
		lStatement += " AND FASC.ID_FASCICOLO_SIUS = RFSIEP.FAS_SIU_ID_FASCICOLO_SIUS";
		lStatement += " AND RFSIEP.COD_UFF_FASCICOLO_SIEP = U.COD_UFFICIO";
		lStatement += " AND C.COD_COMUNE = U.COD_COMUNE";
		lStatement += " AND DES_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO' and DES_TIPO_UFF.RV_LOW_VALUE = U.COD_TIPO_UFFICIO )";
		lStatement += " ORDER BY DESC_COMUNE";

		setStatement(lStatement);
	}

	public void stessoDistretto(BigDecimal aChiaveUfficio1, BigDecimal aChiaveUfficio2) {

		String lStatement = new String();

		lStatement += "SELECT U.COD_UFFICIO ";
		lStatement += " FROM UFFICIO U, UFFICIO U2 ";
		lStatement += " WHERE U.COD_UFFICIO = '" + aChiaveUfficio1 + "'";
		lStatement += " AND  U2.COD_UFFICIO = '" + aChiaveUfficio2 + "'";
		lStatement += " AND   U.COD_DISTRETTO = U2.COD_DISTRETTO ";

		setStatement(lStatement);

	}

	public void stessoDistretto(String aChiaveUfficio, BigDecimal aIdFascicoloSiep) {

		String lStatement = new String();

		lStatement += "SELECT U.COD_UFFICIO ";
		lStatement += " FROM UFFICIO U, UFFICIO U2, FASCICOLO_SIEP FS ";
		lStatement += " WHERE U.COD_UFFICIO = '" + aChiaveUfficio + "'";
		lStatement += " AND  U2.COD_UFFICIO = FS.CHIAVE_UFFICIO";
		lStatement += " AND  FS.ID_FASCICOLO_SIEP = '" + aIdFascicoloSiep + "'";
		lStatement += " AND   U.COD_DISTRETTO = U2.COD_DISTRETTO ";

		setStatement(lStatement);

	}

	public void ricercaUfficioByCod(String aCod) throws DAOException {

		String lStatement = new String();

		lStatement += " SELECT COD_UFFICIO, U.COD_PROVINCIA COD_PROVINCIA, R.RV_MEANING DESC_PROVINCIA, COD_TIPO_UFFICIO, U.DATA_CARICAMENTO_REGE DATA_CARICAMENTO_REGE,";
		lStatement += " INDIRIZZO, U.CAP CAP_UFFICIO, TELEFONO, FAX, E_MAIL,";
		lStatement += " U.COD_COMUNE COD_COMUNE, C.DESCRIZIONE DESC_COMUNE, U.COD_DISTRETTO COD_DISTRETTO, R2.RV_MEANING DESC_TIPO_UFFICIO";
		lStatement += " FROM UFFICIO U, COMUNE C, CG_REF_CODES R, CG_REF_CODES R2";
		lStatement += " WHERE COD_UFFICIO = '" + aCod + "'";
		lStatement += " AND C.COD_COMUNE = U.COD_COMUNE";
		lStatement += " AND R.RV_DOMAIN = 'PROVINCIA' and R.RV_LOW_VALUE = C.COD_PROVINCIA";
		lStatement += " AND R2.RV_DOMAIN = 'UFFICIO_LOGIN' and R2.RV_LOW_VALUE = U.COD_TIPO_UFFICIO and C.COD_PROVINCIA = U.COD_PROVINCIA";
		lStatement += " ORDER BY R2.RV_MEANING";
		setStatement(lStatement);
	}

	/**
	 * Il metodo restituisce la tipologia di ufficio codice ufficio
	 * 
	 * @param aCodDistretto
	 * @throws DAOException
	 */
	public void getTipoUfficioUtente(String aCodDistretto) throws DAOException {
		// 13/03/2018 metodo introdotto per anomalia m_dg.DOG07.28-02-2018.0007015.U (parametro scadenziario
		// mancante)
		// in fase di associazioni di un utente ad un ufficio andiamo a controllare se è stato inserito in
		// tabella PARAMETRO
		// il record per singolo ufficio per lo scadenziario di 'TERMINE SOTTOSCRIZIONE VERBALE M.A.
		String lStatement = new String();
		lStatement = "SELECT RV_LOW_VALUE FROM CG_REF_CODES,UFFICIO WHERE ";
		lStatement += " UFFICIO.COD_TIPO_UFFICIO=CG_REF_CODES.RV_LOW_VALUE ";
		lStatement += " AND UFFICIO.COD_UFFICIO='" + aCodDistretto + "'";
		lStatement += " AND CG_REF_CODES.RV_DOMAIN='UFFICIO_LOGIN'";
		setStatement(lStatement);
	}

	// MEV_39: aggiunto metodo di ricerca
	public void getUfficioPMEsecDest(BigDecimal idFascicoloSiepOrigine, String codTipoUfficio,
			String chiaveUfficioSiepOrigine) {

		String lStatement = new String();
		lStatement += "SELECT U.COD_UFFICIO," + " U.COD_DISTRETTO," + " U.COD_COMUNE,"
				+ " U.COD_TIPO_UFFICIO," + " C.DESCRIZIONE DESC_COMUNE,"
				+ " DES_TIPO_UFF.RV_MEANING DESC_TIPO_UFFICIO," + " FASC.CHIAVE_ANNO," + " FASC.CHIAVE_PROGR"
				// MEV_39 03/01/2018
				+ " , null FAS_SIEP_ORIG"

				+ " FROM UFFICIO U, COMUNE C, CG_REF_CODES DES_TIPO_UFF, FASCICOLO_SIEP FASC"
				+ " WHERE FASC.ID_FASCICOLO_SIEP = '" + idFascicoloSiepOrigine + "'"
				+ " AND C.COD_COMUNE = U.COD_COMUNE" + "   AND DES_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO'"
				+ " AND DES_TIPO_UFF.RV_LOW_VALUE = U.COD_TIPO_UFFICIO" + " AND U.COD_TIPO_UFFICIO = '"
				+ codTipoUfficio + "'" + " AND U.COD_COMUNE = (SELECT Z.COD_COMUNE" + " FROM UFFICIO Z"
				+ " WHERE Z.COD_UFFICIO = '" + chiaveUfficioSiepOrigine + "')";
		setStatement(lStatement);
	}

	public void getUfficioPGCAPEsecDest(String codTipoUfficio, String codComuneUtenteConnesso) {

		String lStatement = new String();
		lStatement += "SELECT DISTINCT U.COD_UFFICIO," + " U.COD_DISTRETTO," + " U.COD_COMUNE,"
				+ " U.COD_TIPO_UFFICIO," + " C.DESCRIZIONE DESC_COMUNE,"
				+ " DES_TIPO_UFF.RV_MEANING DESC_TIPO_UFFICIO, NULL CHIAVE_ANNO, NULL CHIAVE_PROGR"
				// MEV_39 03/01/2018
				+ " , null FAS_SIEP_ORIG"

				+ " FROM UFFICIO U," + " COMUNE C," + " CG_REF_CODES DES_TIPO_UFF"
				+ " WHERE C.COD_COMUNE = U.COD_COMUNE" + " AND DES_TIPO_UFF.RV_DOMAIN = 'TIPO_UFFICIO'"
				+ " AND DES_TIPO_UFF.RV_LOW_VALUE = U.COD_TIPO_UFFICIO" + " AND U.COD_TIPO_UFFICIO = '"
				+ codTipoUfficio + "'" + " AND U.COD_COMUNE = '" + codComuneUtenteConnesso + "'";

		setStatement(lStatement);
	}

}