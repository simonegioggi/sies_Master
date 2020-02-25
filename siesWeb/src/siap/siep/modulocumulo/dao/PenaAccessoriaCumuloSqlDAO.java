package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;

/**
 * <p>
 * Title: PenaAccessoriaCumuloSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella PenaAccessoriaCumulo
 * </p>
 * 
 * @version 1.0
 */
public class PenaAccessoriaCumuloSqlDAO extends SIAPSqlDAO {

	public PenaAccessoriaCumuloSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaPenaAccessoriaCumulo(PenaAccessoriaCumuloModel aModel) throws DAOException {
		String lStatement = new String(getSqlPenaAccessoriaCumulo());

		lStatement += " " + setCondizioni(aModel);
		lStatement += " " + setOrder();

		setStatement(lStatement);
	}

	public void ricercaPeneAccessorieCumulo_Valide(PenaAccessoriaCumuloModel aModel) throws DAOException {
		String lStatement = new String(getSqlPenaAccessoriaCumulo());

		lStatement += " " + setCondizioni(aModel);
		lStatement += " AND FLAG_STATO != 'C'";
		lStatement += " " + setOrder();

		setStatement(lStatement);
	}

	public void ricercaPenaAccessoriaCumuloByKey(BigDecimal aKey) throws DAOException {
		String lStatement = new String(getSqlPenaAccessoriaCumulo());

		lStatement += " AND ID_PENA_ACCESSORIA_CUMULO =" + aKey;

		setStatement(lStatement);
	}

	public void ricercaPenaAccessoriaCumuloByKeyPAOrigine(BigDecimal aKeyPA, BigDecimal aKeyTit)
			throws DAOException {
		String lStatement = new String(getSqlPenaAccessoriaCumulo());

		lStatement += " AND ID_PENA_ACCESSORIA_ORIGINE = " + aKeyPA;
		lStatement += " AND TIT_ID_TITOLO_CUMULATO = " + aKeyTit;

		setStatement(lStatement);
	}

	public void ricercaPenaAccessoriaCumuloByTitoloCum(BigDecimal aKey) throws DAOException {
		String lStatement = new String(getSqlPenaAccessoriaCumulo());

		lStatement += " AND TIT_ID_TITOLO_CUMULATO =" + aKey;

		setStatement(lStatement);
	}

	// Ricerca per Tit_Id_Titolo_Cumulato in Join con RICHPM_PENACC_CUM
	public void ricercaPenaAccessoriaCumuloByTitoloCumRichGE(BigDecimal aKeyTit, BigDecimal aKeyRich)
			throws DAOException {
		String lStatement = new String(getSqlPenaAccJoinRichiestaGE());

		lStatement += " AND TIT_ID_TITOLO_CUMULATO = " + aKeyTit;

		lStatement += " AND RICHPM_PENACC_CUM.RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aKeyRich;
		lStatement += " AND RICHPM_PENACC_CUM.PEN_ID_PENACC_CUMULO = ID_PENA_ACCESSORIA_CUMULO ";

		setStatement(lStatement);
	}

	protected String getSqlPenaAccessoriaCumulo() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PENA_ACCESSORIA_CUMULO, " + "COD_TIPO_PENA_ACCESSORIA, "
				+ "DESCR_ALTRE_PA, " + "COD_TIPO_DURATA, " + "NUM_ANNI, " + "NUM_MESI, " + "NUM_GIORNI, "
				+ "NOTE, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "FLAG_STATO, " + "MOTIVO_MODIFICA, "
				+ "TIT_ID_TITOLO_CUMULATO, " + "ID_PENA_ACCESSORIA_ORIGINE, " + " BEN_ID_BENEFICIO_CUMULO, "
				+ " BEN_ID_BENEFICIO_ORIG, " + " FLAG_DATI_FINALI, "
				+ "TIPOPENA.RV_MEANING DESCRPENA, TIPODURATAPENA.RV_MEANING DESCRDURATA " + "";
		lStatement += "FROM PENA_ACCESSORIA_CUMULO, CG_REF_CODES TIPOPENA, CG_REF_CODES TIPODURATAPENA ";

		lStatement += "WHERE ";

		lStatement += "TIPOPENA.RV_DOMAIN='TIPO_PENA_ACCESSORIA' AND ";
		lStatement += "TIPOPENA.RV_LOW_VALUE=PENA_ACCESSORIA_CUMULO.COD_TIPO_PENA_ACCESSORIA AND ";
		lStatement += "TIPODURATAPENA.RV_DOMAIN='TIPO_DURATA' AND ";
		lStatement += "TIPODURATAPENA.RV_LOW_VALUE=PENA_ACCESSORIA_CUMULO.COD_TIPO_DURATA";

		return lStatement;
	}

	protected String getSqlQueryJoinTitoloCumulato() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PENA_ACCESSORIA_CUMULO, " + "COD_TIPO_PENA_ACCESSORIA, "
				+ "DESCR_ALTRE_PA, " + "COD_TIPO_DURATA, " + "NUM_ANNI, " + "NUM_MESI, " + "NUM_GIORNI, "
				+ "PENA_ACCESSORIA_CUMULO.NOTE, " + "PENA_ACCESSORIA_CUMULO.COD_OPERATORE_INSERIMENTO, "
				+ "PENA_ACCESSORIA_CUMULO.DATA_INSERIMENTO, "
				+ "PENA_ACCESSORIA_CUMULO.COD_UFFICIO_INSERIMENTO, "
				+ "PENA_ACCESSORIA_CUMULO.COD_OPERATORE_AGGIORNAMENTO, "
				+ "PENA_ACCESSORIA_CUMULO.DATA_AGGIORNAMENTO, "
				+ "PENA_ACCESSORIA_CUMULO.COD_UFFICIO_AGGIORNAMENTO, " + "PENA_ACCESSORIA_CUMULO.FLAG_STATO, "
				+ "PENA_ACCESSORIA_CUMULO.MOTIVO_MODIFICA, " + "TIT_ID_TITOLO_CUMULATO, "
				+ "ID_PENA_ACCESSORIA_ORIGINE, " + " BEN_ID_BENEFICIO_CUMULO, " + " BEN_ID_BENEFICIO_ORIG, "
				+ " FLAG_DATI_FINALI, "
				+ "TIPOPENA.RV_MEANING DESCRPENA, TIPODURATAPENA.RV_MEANING DESCRDURATA " + "";
		lStatement += "FROM PENA_ACCESSORIA_CUMULO, CG_REF_CODES TIPOPENA, CG_REF_CODES TIPODURATAPENA ";
		lStatement += "    ,TITOLO_CUMULATO ";

		lStatement += "WHERE ";

		lStatement += "TIPOPENA.RV_DOMAIN='TIPO_PENA_ACCESSORIA' AND ";
		lStatement += "TIPOPENA.RV_LOW_VALUE=PENA_ACCESSORIA_CUMULO.COD_TIPO_PENA_ACCESSORIA AND ";
		lStatement += "TIPODURATAPENA.RV_DOMAIN='TIPO_DURATA' AND ";
		lStatement += "TIPODURATAPENA.RV_LOW_VALUE=PENA_ACCESSORIA_CUMULO.COD_TIPO_DURATA";

		lStatement += " AND (TIT_ID_TITOLO_CUMULATO = TITOLO_CUMULATO.ID_TITOLO_CUMULATO) ";

		return lStatement;
	}

	// Stringa della QUERY in Join con RICHPM_PENACC_CUM
	protected String getSqlPenaAccJoinRichiestaGE() {
		String lStatement = new String("");

		lStatement += " SELECT " + "ID_PENA_ACCESSORIA_CUMULO, " + "COD_TIPO_PENA_ACCESSORIA, "
				+ "DESCR_ALTRE_PA, " + "COD_TIPO_DURATA, " + "NUM_ANNI, " + "NUM_MESI, " + "NUM_GIORNI, "
				+ "NOTE, " + "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, " + "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, " + "FLAG_STATO, " + "MOTIVO_MODIFICA, "
				+ "TIT_ID_TITOLO_CUMULATO, " + "ID_PENA_ACCESSORIA_ORIGINE, " + " BEN_ID_BENEFICIO_CUMULO, "
				+ " BEN_ID_BENEFICIO_ORIG, " + " FLAG_DATI_FINALI, "
				+ "TIPOPENA.RV_MEANING DESCRPENA, TIPODURATAPENA.RV_MEANING DESCRDURATA " + "";
		lStatement += "FROM PENA_ACCESSORIA_CUMULO, RICHPM_PENACC_CUM, ";
		lStatement += " CG_REF_CODES TIPOPENA, CG_REF_CODES TIPODURATAPENA ";

		lStatement += "WHERE ";

		lStatement += "TIPOPENA.RV_DOMAIN='TIPO_PENA_ACCESSORIA' AND ";
		lStatement += "TIPOPENA.RV_LOW_VALUE=PENA_ACCESSORIA_CUMULO.COD_TIPO_PENA_ACCESSORIA AND ";
		lStatement += "TIPODURATAPENA.RV_DOMAIN='TIPO_DURATA' AND ";
		lStatement += "TIPODURATAPENA.RV_LOW_VALUE=PENA_ACCESSORIA_CUMULO.COD_TIPO_DURATA";

		return lStatement;
	}

	public void ricercaPenaAccessoriaCumuloByIdIstruttoria(BigDecimal aIdIstrittoriaCumulo,
			boolean aFlagDatiFinali) throws DAOException {
		// Recupera la select...from
		String lSql = getSqlQueryJoinTitoloCumulato();

		// Aggiunge le where condition per chiave
		lSql += " AND TITOLO_CUMULATO.ISTR_ID_ISTRUTTORIA_CUMULO = " + aIdIstrittoriaCumulo;

		if (aFlagDatiFinali) {
			lSql += " AND FLAG_DATI_FINALI= 'S' ";
		}

		// Ordinate per Data Irrevocabilità del Titolo, titolo e...
		lSql += " ORDER BY TITOLO_CUMULATO.DATA_IRREVOCABILITA ASC, TITOLO_CUMULATO.ID_TITOLO_CUMULATO ASC ";
		// Imposta lo statement da eseguire
		setStatement(lSql);
	}
	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		PenaAccessoriaCumuloModel aModel = new PenaAccessoriaCumuloModel();

		aModel.setIdPenaAccessoriaCumulo(getBigDecimal("ID_PENA_ACCESSORIA_CUMULO"));
		aModel.setCodTipoPenaAccessoria(getString("COD_TIPO_PENA_ACCESSORIA"));
		aModel.setDescrTipoPenaAccessoria(getString("DESCRPENA"));
		aModel.setDescrAltrePA(getString("DESCR_ALTRE_PA"));
		aModel.setDurata(getString("COD_TIPO_DURATA"));
		aModel.setDescrDurata(getString("DESCRDURATA"));
		aModel.setNumAnni(getBigDecimal("NUM_ANNI"));
		aModel.setNumMesi(getBigDecimal("NUM_MESI"));
		aModel.setNumGiorni(getBigDecimal("NUM_GIORNI"));
		aModel.setNote(getString("NOTE"));
		aModel.setBenIdBeneficioCumulo(getBigDecimal("BEN_ID_BENEFICIO_CUMULO"));
		aModel.setBenIdBeneficioOrig(getBigDecimal("BEN_ID_BENEFICIO_ORIG"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		/*
		 * aModel.setAnnoOrdinanzaGE(getBigDecimal("ANNO_ORDINANZA_GE") );
		 * aModel.setNumeroOrdinanzaGE(getBigDecimal("NUMERO_ORDINANZA_GE") );
		 * aModel.setDataOrdinanzaGE(getDate("DATA_ORDINANZA_GE") );
		 * aModel.setCodTipoUfficioOrdinanzaGE(getString("COD_TIPO_UFFICIO_ORDINANZA_GE") );
		 * aModel.setDescrTipoUfficioOrdinanzaGE(getString("DESCRTIPOUFFICIO_OR_GE") );
		 * aModel.setCodLuogoUfficioOrdinanzaGE(getString("COD_LUOGO_UFFICIO_ORDINANZA_GE") );
		 * aModel.setDescrLuogoUfficioOrdinanzaGE(getString("DESCRLUOGOUFFICIO_OR_GE") );
		 * aModel.setIdPenaAccessoriaOrigine(getBigDecimal("ID_PENA_ACCESSORIA_ORIGINE") );
		 */
		// aModel.setNumeroEventiCorrelati(this.getNumeroEventiCorrelati(getBigDecimal("ID_PENA_ACCESSORIA"))
		// );

		aModel.setMotivoModifica(getString("MOTIVO_MODIFICA"));
		aModel.setFlagStato(getString("FLAG_STATO"));
		aModel.setTitIdTitoloCumulato(getBigDecimal("TIT_ID_TITOLO_CUMULATO"));
		aModel.setIdPenaAccessoriaOrigine(getBigDecimal("ID_PENA_ACCESSORIA_ORIGINE"));
		aModel.setFlagDatiFinali(getString("FLAG_DATI_FINALI"));

		return aModel;
	}

	public void selCondizione(PenaAccessoriaCumuloModel aModel) {
		// String lCondizioni = new String();
		// boolean lInserito = false;
	}

	public String setCondizioni(PenaAccessoriaCumuloModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		if (aModel.getIdPenaAccessoriaCumulo() != null) {
			lCondizioni += " AND ID_PENA_ACCESSORIA_CUMULO = " + aModel.getIdPenaAccessoriaCumulo();
			// lInserito = true;
		}

		if (aModel.getBenIdBeneficioCumulo() != null) {
			lCondizioni += " AND BEN_ID_BENEFICIO_CUMULO = " + aModel.getBenIdBeneficioCumulo();
			// lInserito = true;
		}

		if (aModel.getTitIdTitoloCumulato() != null) {
			lCondizioni += " AND TIT_ID_TITOLO_CUMULATO = " + aModel.getTitIdTitoloCumulato();
			// lInserito = true;
		}

		return lCondizioni;
	}

	private String setOrder() {
		String lOrder = new String();
		lOrder = " ORDER BY DATA_INSERIMENTO, ID_PENA_ACCESSORIA_CUMULO DESC";
		return lOrder;
	}

}