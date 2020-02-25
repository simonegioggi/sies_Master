package siap.siep.modulocumulo.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;

import siap.dao.SIAPSqlDAO;
import siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel;

/**
 * <p>
 * Title: SanzioneSostitutivaCumuloSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella SanzioneSostitutiva
 * </p>
 * <p>
 * in ambito Cumulo (SanzioneSostitutiva_Cumulo)
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
public class SanzioneSostitutivaCumuloSqlDAO extends SIAPSqlDAO {

	public SanzioneSostitutivaCumuloSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaSanzioneSostitutiva(SanzioneSostitutivaCumuloModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaSanzioneSostitutivaByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaSanzioneSostitutivaByIdPenaComplessivaCum(BigDecimal aIdPenaComplessiva)
			throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByIdPenaComplessivaCum(aIdPenaComplessiva);

		setStatement(lSql);
	}

	public void ricercaSanzioneSostitutivaCumByTitoloCum(BigDecimal aIdTitoloCum) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioneByIdTitoloCum(aIdTitoloCum);

		setStatement(lSql);
	}

  public void ricercaSanzioneSostitutivaByIdPenaComplessivaCumTitoloCum(BigDecimal aIdPenaComplessiva, BigDecimal aIdTitoloCum)  throws DAOException
  {
    String lSql = getSqlQuery();

    lSql += " " + setCondizioniByIdPenaComplessivaCum(aIdPenaComplessiva);
    lSql += " " + setCondizioneByIdTitoloCum(aIdTitoloCum);

    setStatement(lSql);
  }
  
	// Ricerca per Tit_Id_Titolo_Cumulato in Join con RICHPM_SANZIONE_SOST_CUM
	public void ricercaSanzioneSostitutivaCumByTitoloCumRichGE(BigDecimal aIdTitoloCum, BigDecimal aIdRichGe)
			throws DAOException {
		String lSql = getSqlQueryjoinRicPMSSCum();

		lSql += " " + setCondizioneByIdTitoloCum(aIdTitoloCum);

		lSql += " AND RICHPM_SANZIONE_SOST_CUM.RIC_ID_RICHIESTE_PM_IN_CUMULO = " + aIdRichGe;
		lSql += " AND RICHPM_SANZIONE_SOST_CUM.SS_ID_SANZIONE_SOST_CUM = ID_SANZIONE_SOST_CUM ";

		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT ID_SANZIONE_SOST_CUM, "
				+ "COD_TIPO_SANZIONE, TIPO_SANZIONE.RV_MEANING DESCR_TIPO_SANZIONE," + "NUM_ANNI, "
				+ "NUM_MESI, " + "NUM_GIORNI, " + "SANZIONE_PECUNIARIA_MULTA, "
				+ "SANZIONE_PECUNIARIA_AMMENDA, " + "PC_ID_PENA_COMPLESSIVA_CUM, " + "FLAG_STATO, "
				+ "MOTIVO_MODIFICA, " + "TIT_ID_TITOLO_CUMULATO, " + "ID_SANZIONE_SOST_ORIGINE, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO ";
		lStatement += " FROM SANZIONE_SOST_CUM, CG_REF_CODES TIPO_SANZIONE";
		lStatement += " WHERE ( nvl(SANZIONE_SOST_CUM.COD_TIPO_SANZIONE,'-') = TIPO_SANZIONE.RV_LOW_VALUE AND TIPO_SANZIONE.RV_DOMAIN = 'TIPO_SANZIONE_SOSTITUTIVA' )";

		return lStatement;
	}

	protected String getSqlQueryjoinRicPMSSCum() {
		String lStatement = new String("");

		lStatement += " SELECT ID_SANZIONE_SOST_CUM, "
				+ "COD_TIPO_SANZIONE, TIPO_SANZIONE.RV_MEANING DESCR_TIPO_SANZIONE," + "NUM_ANNI, "
				+ "NUM_MESI, " + "NUM_GIORNI, " + "SANZIONE_PECUNIARIA_MULTA, "
				+ "SANZIONE_PECUNIARIA_AMMENDA, " + "PC_ID_PENA_COMPLESSIVA_CUM, " + "FLAG_STATO, "
				+ "MOTIVO_MODIFICA, " + "TIT_ID_TITOLO_CUMULATO, " + "ID_SANZIONE_SOST_ORIGINE, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO ";
		lStatement += " FROM SANZIONE_SOST_CUM, CG_REF_CODES TIPO_SANZIONE";
		lStatement += " ,RICHPM_SANZIONE_SOST_CUM";
		lStatement += " WHERE ( nvl(SANZIONE_SOST_CUM.COD_TIPO_SANZIONE,'-') = TIPO_SANZIONE.RV_LOW_VALUE AND TIPO_SANZIONE.RV_DOMAIN = 'TIPO_SANZIONE_SOSTITUTIVA' )";

		return lStatement;
	}

	public GenericModel getModel() throws DAOException {
		SanzioneSostitutivaCumuloModel aModel = new SanzioneSostitutivaCumuloModel();

		aModel.setIdSanzioneSostitutivaCum(getBigDecimal("ID_SANZIONE_SOST_CUM"));
		aModel.setCodTipoSanzione(getString("COD_TIPO_SANZIONE"));
		aModel.setDescrTipoSanzione(getString("DESCR_TIPO_SANZIONE"));
		aModel.setNumAnni(getBigDecimal("NUM_ANNI"));
		aModel.setNumMesi(getBigDecimal("NUM_MESI"));
		aModel.setNumGiorni(getBigDecimal("NUM_GIORNI"));
		aModel.setSanzionePecuniariaMulta(getBigDecimal("SANZIONE_PECUNIARIA_MULTA"));
		aModel.setSanzionePecuniariaAmmenda(getBigDecimal("SANZIONE_PECUNIARIA_AMMENDA"));

		aModel.setPcIdPenaComplessivaCum(getBigDecimal("PC_ID_PENA_COMPLESSIVA_CUM"));

		aModel.setFlagStato(getString("FLAG_STATO"));
		aModel.setMotivoModifica(getString("MOTIVO_MODIFICA"));
		aModel.setTitIdTitoloCumulato(getBigDecimal("TIT_ID_TITOLO_CUMULATO"));
		aModel.setIdSanzioneSostOrigine(getBigDecimal("ID_SANZIONE_SOST_ORIGINE"));

		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));

		return aModel;
	}

	public String setCondizione(SanzioneSostitutivaCumuloModel aModel) {
		String lCondizioni = new String();

		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_SANZIONE_SOST_CUM = " + aKey;
	}

	public String setCondizioniByIdPenaComplessivaCum(BigDecimal aIdPenaComplessiva) {
		return " AND PC_ID_PENA_COMPLESSIVA_CUM = " + aIdPenaComplessiva;
	}

	public String setCondizioneByIdTitoloCum(BigDecimal aIdTitoloCum) {
		return " AND TIT_ID_TITOLO_CUMULATO = " + aIdTitoloCum;
	}

}