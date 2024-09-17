package siap.siep.sanzionesostitutiva.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;

/**
 * Classe SqlDAO che rappresenta la tabella SanzioneSostitutiva
 *
 * @version 1.0
 */
public class SanzioneSostitutivaSqlDAO extends SIAPSqlDAO {

	public SanzioneSostitutivaSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	// public void ricercaSanzioneSostitutiva(SanzioneSostitutivaModel aModel) throws DAOException {
	// String lSql = getSqlQuery();
	// lSql += " " + setCondizione(aModel);
	// setStatement(lSql);
	// }

	public void ricercaSanzioneSostitutivaByKey(BigDecimal aKey) throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaSanzioneSostitutivaByIdPenaComplessiva(BigDecimal aIdPenaComplessiva)
			throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdPenaComplessiva(aIdPenaComplessiva);

		setStatement(lSql);
	}

	protected String getSqlQuery() {

		String lStatement = new String("");
		lStatement += " SELECT " + "ID_SANZIONE_SOSTITUTIVA, TIPO_SANZIONE.RV_MEANING DESCR_TIPO_SANZIONE,"
				+ "COD_TIPO_SANZIONE, " + "NUM_ANNI, " + "NUM_MESI, " + "NUM_GIORNI, "
				+ "SANZIONE_PECUNIARIA_MULTA, " + "ANNO_REGISTRO, " + "NUM_REGISTRO, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "PEN_COM_ID_PENA_COMPLESSIVA, " + "SANZIONE_PECUNIARIA_AMMENDA ";
		// MEV_2023-13
		lStatement += " , TIPO_SANZIONE.RV_ABBREVIATION DESCR_CATEGORIA_SANZIONE";
		// MEV_2023-13 - FINE
		lStatement += " FROM SANZIONE_SOSTITUTIVA, CG_REF_CODES TIPO_SANZIONE";
		lStatement += " WHERE (TIPO_SANZIONE.RV_DOMAIN = 'TIPO_SANZIONE_SOSTITUTIVA' AND SANZIONE_SOSTITUTIVA.COD_TIPO_SANZIONE = TIPO_SANZIONE.RV_LOW_VALUE)";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {

		SanzioneSostitutivaModel aModel = new SanzioneSostitutivaModel();

		aModel.setIdSanzioneSostitutiva(getBigDecimal("ID_SANZIONE_SOSTITUTIVA"));
		aModel.setCodTipoSanzione(getString("COD_TIPO_SANZIONE"));
		aModel.setDescrTipoSanzione(getString("DESCR_TIPO_SANZIONE"));
		aModel.setNumAnni(getBigDecimal("NUM_ANNI"));
		aModel.setNumMesi(getBigDecimal("NUM_MESI"));
		aModel.setNumGiorni(getBigDecimal("NUM_GIORNI"));
		aModel.setSanzionePecuniariaMulta(getBigDecimal("SANZIONE_PECUNIARIA_MULTA"));
		aModel.setAnnoRegistro(getBigDecimal("ANNO_REGISTRO"));
		aModel.setNumRegistro(getBigDecimal("NUM_REGISTRO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setPenComIdPenaComplessiva(getBigDecimal("PEN_COM_ID_PENA_COMPLESSIVA"));
		aModel.setSanzionePecuniariaAmmenda(getBigDecimal("SANZIONE_PECUNIARIA_AMMENDA"));

		// MEV_2023-13
		aModel.setDescrCategoriaSanzione(getString("DESCR_CATEGORIA_SANZIONE"));
		// MEV_2023-13 - FINE

		return aModel;
	}

	// public String setCondizione(SanzioneSostitutivaModel aModel) {
	// String lCondizioni = new String();
	// return lCondizioni;
	// }

	public String setCondizioniByKey(BigDecimal aKey) {

		return " AND ID_SANZIONE_SOSTITUTIVA = " + aKey;
	}

	public String setCondizioniByIdPenaComplessiva(BigDecimal aIdPenaComplessiva) {

		return " AND PEN_COM_ID_PENA_COMPLESSIVA = " + aIdPenaComplessiva;
	}

	/**
	 * Aggiunto metodo di ricerca pena sostituiva per tipologia (highValue della
	 * cg_ref_codes.TIPO_SANZIONE_SOSTITUTIVA)
	 *
	 * @author sgioggi
	 * @since MEV_2023-33
	 *
	 * @param idPenaComplessiva
	 * @param highValue
	 * @throws DAOException
	 */
	public void ricercaPenaSostitutivaByIdPenaComplessiva(BigDecimal idPenaComplessiva, String highValue)
			throws DAOException {

		String lSql = getSqlQuery();
		lSql += " " + setCondizioniByIdPenaComplessiva(idPenaComplessiva);
		lSql += " AND TIPO_SANZIONE.RV_HIGH_VALUE in " + highValue;

		setStatement(lSql);
	}

}