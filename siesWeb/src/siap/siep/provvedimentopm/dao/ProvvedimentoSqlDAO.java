package siap.siep.provvedimentopm.dao;

/**
* <p>Title: ProvvedimentoSqlDAO</p>
* <p>Description: Classe SqlDAO che rappresenta la tabella Provvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.model.GenericModel;
import siap.dao.SIAPSqlDAO;
import siap.siep.provvedimentopm.model.ProvvedimentoModel;

public class ProvvedimentoSqlDAO extends SIAPSqlDAO {

	public ProvvedimentoSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaProvvedimento(ProvvedimentoModel aModel) throws DAOException {
		String lStatement = new String("");

		lStatement += "SELECT ";
		lStatement += "ID_PROVVEDIMENTO, ";
		lStatement += "COD_TIPO, ";
		lStatement += "COD_MOTIVO, ";
		lStatement += "DATA, ";
		lStatement += "COD_ESITO, ";
		lStatement += "FLAG_PIU_MENO, ";
		lStatement += "DATA_TRASMISSIONE_ATTI, ";
		lStatement += "DATA_SCADENZA, ";
		lStatement += "ANNO_PROTOCOLLO, ";
		lStatement += "PROGR_PROTOCOLLO, ";
		lStatement += "COD_OPERATORE_INSERIMENTO, ";
		lStatement += "DATA_INSERIMENTO, ";
		lStatement += "COD_OPERATORE_AGGIORNAMENTO, ";
		lStatement += "DATA_AGGIORNAMENTO, ";
		lStatement += "FAS_SIE_ID_FASCICOLO_SIEP, ";
		lStatement += "MAG_COD_MAGISTRATO, ";
		lStatement += "CODTIPO.RV_MEANING DESCR_TIPO , ";
		lStatement += "CODMOTIVO.RV_MEANING DESCR_MOTIVO , ";
		lStatement += "CODESITO.RV_MEANING DESCR_ESITO  ";
		lStatement += " FROM PROVVEDIMENTO,CG_REF_CODES CODTIPO, CG_REF_CODES CODMOTIVO, CG_REF_CODES CODESITO ";
		lStatement += " WHERE ";
		lStatement += " PROVVEDIMENTO.COD_TIPO=CODTIPO.RV_LOW_VALUE AND CODTIPO.RV_DOMAIN='TIPO_PROVVEDIMENTO' AND ";
		lStatement += " PROVVEDIMENTO.COD_MOTIVO=CODMOTIVO.RV_LOW_VALUE AND CODMOTIVO.RV_DOMAIN='MOTIVO_PROVVEDIMENTO' AND ";
		lStatement += " PROVVEDIMENTO.COD_ESITO=CODESITO.RV_LOW_VALUE AND CODESITO.RV_DOMAIN='ESITO_PROVVEDIMENTO' ";
		lStatement += " " + setCondizioni(aModel);
		lStatement += " ORDER BY DATA";

		setStatement(lStatement);
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		ProvvedimentoModel aModel = new ProvvedimentoModel();

		// Inserire le opportune set delle descrizioni!
		aModel.setIdProvvedimento(getBigDecimal("ID_PROVVEDIMENTO"));
		aModel.setCodTipo(getString("COD_TIPO"));
		aModel.setDescrTipo(getString("DESCR_TIPO"));
		aModel.setCodMotivo(getString("COD_MOTIVO"));
		aModel.setDescrMotivo(getString("DESCR_MOTIVO"));
		aModel.setData(getDate("DATA"));
		aModel.setCodEsito(getString("COD_ESITO"));
		aModel.setDescrEsito(getString("DESCR_ESITO"));
		aModel.setFlagPiuMeno(getString("FLAG_PIU_MENO"));
		aModel.setDataTrasmissioneAtti(getDate("DATA_TRASMISSIONE_ATTI"));
		aModel.setDataScadenza(getDate("DATA_SCADENZA"));
		aModel.setAnnoProtocollo(getBigDecimal("ANNO_PROTOCOLLO"));
		aModel.setProgrProtocollo(getBigDecimal("PROGR_PROTOCOLLO"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setMagCodMagistrato(getString("MAG_COD_MAGISTRATO"));

		return aModel;
	}

	public void selCondizione(ProvvedimentoModel aModel) {
		// String lCondizioni = new String();
		// boolean lInserito = false;
	}

	public String setCondizioni(ProvvedimentoModel aModel) {
		String lCondizioni = new String();
		// boolean lInserito = false;
		if (aModel.getFasSieIdFascicoloSiep() != null) {
			lCondizioni += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aModel.getFasSieIdFascicoloSiep();
			// lInserito=true;
		}
		return lCondizioni;
	}

}