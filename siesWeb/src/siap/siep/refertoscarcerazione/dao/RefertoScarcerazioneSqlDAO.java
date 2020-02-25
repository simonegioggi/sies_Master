package siap.siep.refertoscarcerazione.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.model.GenericModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.refertoscarcerazione.model.RefertoScarcerazioneModel;

/**
 * <p>
 * Title: RefertoScarcerazioneSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella RefertoScarcerazione
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
public class RefertoScarcerazioneSqlDAO extends SqlDAO {

	public RefertoScarcerazioneSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//

	public void ricercaRefertoScarcerazione(RefertoScarcerazioneModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaRefertoScarcerazioneByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);
		setStatement(lSql);
	}

	public void ricercaUltimoRefertoScarcerazione() throws DAOException {
		String lSql = getSqlQuery();

		lSql += " ORDER BY REF.DATA_INSERIMENTO DESC";
		setStatement(lSql);
	}

	public void ricercaRefertoScarcerazioneByEveIdEvento(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();
		lSql += " AND EVE_ID_EVENTO =" + aKey;
		setStatement(lSql);
	}

	protected String getSqlQuery() {
		String lStatement = new String("");

		lStatement += " SELECT " + "REF.ID_REFERTO_SCARCERAZIONE, " + "REF.ANNO_NOTA, " + "REF.NUM_NOTA, "
				+ "REF.DATA_NOTA, " + "REF.DATA_SCARCERAZIONE, " + "REF.NOTE, "
				+ "REF.COD_OPERATORE_INSERIMENTO, " + "REF.DATA_INSERIMENTO, "
				+ "REF.COD_UFFICIO_INSERIMENTO, " + "REF.COD_OPERATORE_AGGIORNAMENTO, "
				+ "REF.DATA_AGGIORNAMENTO, " + "REF.COD_UFFICIO_AGGIORNAMENTO, " + "REF.EVE_ID_EVENTO, "
				+ "REF.IST_DET_ID_ISTITUTO_DETENZIONE, " + "IST.ID_ISTITUTO_DETENZIONE ,"
				+ "IST.COD_TIPO_ISTITUTO, ISTITUTO.RV_MEANING ISTITUTI, "
				+ "IST.COD_COMUNE, C.DESCRIZIONE COMUNI, " + "IST.COD_PROVINCIA,PRO.RV_MEANING PROVINCIE, "
				+ "IST.INDIRIZZO, " + "IST.DESCRIZIONE, " + "IST.NOTE ";
		lStatement += " FROM REFERTO_SCARCERAZIONE REF, ISTITUTO_DETENZIONE IST, COMUNE C, CG_REF_CODES ISTITUTO,CG_REF_CODES PRO";
		lStatement += " WHERE IST.ID_ISTITUTO_DETENZIONE = REF.IST_DET_ID_ISTITUTO_DETENZIONE";
		lStatement += " AND C.COD_COMUNE = IST.COD_COMUNE AND ISTITUTO.RV_DOMAIN = 'TIPO_ISTITUTO' AND ISTITUTO.RV_LOW_VALUE = IST.COD_TIPO_ISTITUTO";
		lStatement += " AND PRO.RV_DOMAIN = 'PROVINCIA' AND PRO.RV_LOW_VALUE = IST.COD_PROVINCIA";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//

	public GenericModel getModel() throws DAOException {
		RefertoScarcerazioneModel aModel = new RefertoScarcerazioneModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdRefertoScarcerazione(getBigDecimal("ID_REFERTO_SCARCERAZIONE"));
		aModel.setAnnoNota(getBigDecimal("ANNO_NOTA"));
		aModel.setNumNota(getString("NUM_NOTA"));
		aModel.setDataNota(getDate("DATA_NOTA"));
		aModel.setDataScarcerazione(getDate("DATA_SCARCERAZIONE"));
		aModel.setNote(getString("NOTE"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));

		IstitutoDetenzioneModel IstDetMod = new IstitutoDetenzioneModel();

		IstDetMod.setIdIstitutoDetenzione(getString("ID_ISTITUTO_DETENZIONE"));
		IstDetMod.setCodTipoIstituto(getString("COD_TIPO_ISTITUTO"));
		IstDetMod.setDescrTipoIstituto(getString("ISTITUTI"));
		IstDetMod.setCodComune(getString("COD_COMUNE"));
		IstDetMod.setDescrComune(getString("COMUNI"));
		IstDetMod.setCodProvincia(getString("COD_PROVINCIA"));
		IstDetMod.setDescrProvincia(getString("PROVINCIE"));
		IstDetMod.setIndirizzo(getString("INDIRIZZO"));
		IstDetMod.setDescrizione(getString("DESCRIZIONE"));
		IstDetMod.setNote(getString("NOTE"));

		aModel.setDescrTipoIstituto(getString("ISTITUTI"));
		aModel.setIndirizzo(getString("INDIRIZZO"));
		if (getString("COMUNI") != null)
			aModel.setDescrLuogo(getString("COMUNI") + " (" + getString("COD_PROVINCIA") + ") ");

		aModel.setIstitutoDetenzione(IstDetMod);

		return aModel;
	}

	public String setCondizione(RefertoScarcerazioneModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_REFERTO_SCARCERAZIONE = " + aKey;
	}

}