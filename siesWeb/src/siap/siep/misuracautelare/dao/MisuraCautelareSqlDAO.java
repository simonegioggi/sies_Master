package siap.siep.misuracautelare.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import siap.dao.SIAPSqlDAO;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import f3b.dao.DAOException;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: MisuraCautelareSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la tabella MisuraCautelare
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
public class MisuraCautelareSqlDAO extends SIAPSqlDAO {
	public MisuraCautelareSqlDAO(Connection con) {
		super(con);
	}

	//
	// METODO RICERCA()
	//
	public void ricercaMisuraCautelare(MisuraCautelareModel aModel) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizione(aModel);
		setStatement(lSql);
	}

	public void ricercaMisuraCautelareByKey(BigDecimal aKey) throws DAOException {
		String lSql = getSqlQuery();

		lSql += " " + setCondizioniByKey(aKey);

		setStatement(lSql);
	}

	public void ricercaMisuraCautelareByFascicolo(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicolo;
		lStatement += " ORDER BY DATA_INIZIO ,DATA_FINE";

		setStatement(lStatement);
	}

	public void ricercaMisuraCautelareByPosizioneGiuridica(BigDecimal aIdPosizioneGiuridica)
			throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND POS_GIU_ID_POSIZIONE_GIURIDICA=" + aIdPosizioneGiuridica;
		lStatement += " ORDER BY DATA_INIZIO ,DATA_FINE";

		setStatement(lStatement);
	}

	public void ricercaMisuraCautelareByFascicoloNoDataNull(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = getSqlQueryNoDataNull();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicolo;
		lStatement += " ORDER BY DATA_INIZIO ,DATA_FINE";

		setStatement(lStatement);
	}

	public void ricercaMisuraCautelareByFascicoloSiDataNull(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = getSqlQuerySiDataNull();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicolo;
		lStatement += " ORDER BY DATA_INIZIO ,DATA_FINE";

		setStatement(lStatement);
	}

	public void ricercaMisuraCautelareByFascicoloSoloDataInizio(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = getSqlQuerySoloDataInizio();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicolo;
		lStatement += " ORDER BY DATA_INIZIO ,DATA_FINE";

		setStatement(lStatement);
	}

	public void ricercaMisuraCautelareByFascicoloForStampa(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = getSqlQuery();

		// lStatement += " AND FLAG_COMPUTABILE='S' AND FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicolo
		// +" AND DATA_FINE IS NOT NULL ";
		// MEV 10 S3
		// Vengono visualizzate nell'elenco anche le Misure Cautelari NON COMPUTABILI
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicolo + " AND DATA_FINE IS NOT NULL ";
		lStatement += " ORDER BY FLAG_COMPUTABILE DESC, DATA_INIZIO";

		setStatement(lStatement);
	}

	public void ricercaMisuraCautelareSenzaDataFineByKey(BigDecimal aIdFascicolo) throws DAOException {
		String lStatement = getSqlQuery();

		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicolo;
		lStatement += " AND DATA_FINE is null ";
		lStatement += " AND FLAG_COMPUTABILE = 'S'";

		setStatement(lStatement);
	}

	private String getSqlQuery() {

		String lStatement = new String("");

		lStatement += " SELECT "
				+ "ID_MISURA_CAUTELARE, "
				+ "COD_TIPO_MISURA, MIS.RV_MEANING TIPO_MIS,"
				+ "DATA_INIZIO, "
				+ "DATA_FINE, "
				+ "DATA_EMISSIONE_ORDINANZA, "
				+ "NUM_ANNI, "
				+ "NUM_MESI, "
				+ "NUM_GIORNI, "
				+ "GIORNI, "
				+ "FLAG_MODIFICA_MANUALE, "
				+ "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "EVE_ID_EVENTO,FLAG_COMPUTABILE, "
				+ "COD_MOTIVO_NON_COMPUTABILE,MOTNON.RV_MEANING DESC_MOTNON, "
				+
				// modifica relativa al tipo istituto
				"IST_DET_ID_ISTITUTO_DETENZIONE, "
				+

				// "COD_TIPO_ISTITUTO_DETENZIONE, ISTDET.RV_MEANING DESC_ISTDET, "+
				"ALTRO_LUOGO_DETENZIONE, "
				+
				// "COD_LUOGO_DETENZIONE, LUOGODET.DESCRIZIONE DESC_LUOGO, "+
				"NUM_RIFER, " + "COD_TIPO_UFFICIO_RIFER, TIPUFFRIF.RV_MEANING  DESC_UFF_RIF, "
				+ "COD_LUOGO_UFFICIO_RIFER,LUOGOUFFRIF.DESCRIZIONE DESC_LUOGO_RIF, " + "DATA_FUNGIBILITA, "
				+ "NOTE, " +

				"ANNO_FASC_BDMC, " + "NUME_FASC_BDMC, " + "ANNO_RGNR, " + "NUMERO_RGNR, " + "ANNO_REG_GEN, "
				+ "NUMERO_REG_GEN, " + "TIPO_UFFICIO_REG_GEN, " + "AUTORITA_EMITTENTE, "
				+ "AUTORITA_EMITTENTE_LUOGO, LUOGOEMITT.DESCRIZIONE  AUTORITA_EMITTENTE_LUOGO_DESC, "
				+ "AUTORITA_COMPETENTE, "
				+ "AUTORITA_COMPETENTE_SEDE, LUOGOCOMPE.DESCRIZIONE  AUTORITA_COMPETENTE_SEDE_DESC, "
				+ "AUTORITA_COMPETENTE_INDIRIZZO, " + "ANNO_RIFER, " + "CODICE_UFFICIO_PM_SEDE, "
				+ "POS_GIU_ID_POSIZIONE_GIURIDICA, AUTCOMPETENTE.RV_MEANING AUTORITA_COMPETENTE_DESC";

		// modifica relativa al tipo istituto
		lStatement += " FROM MISURA_CAUTELARE,CG_REF_CODES MIS,CG_REF_CODES MOTNON,";// CG_REF_CODES ISTDET ,
																						// COMUNE LUOGODET ,";
		lStatement += " CG_REF_CODES TIPUFFRIF, CG_REF_CODES AUTCOMPETENTE, COMUNE LUOGOUFFRIF , COMUNE LUOGOEMITT , COMUNE LUOGOCOMPE ";
		lStatement += " WHERE ";
		lStatement += " MIS.RV_DOMAIN = 'TIPO_MISURA_CAUTELARE' ";
		lStatement += " AND MIS.RV_LOW_VALUE = COD_TIPO_MISURA ";
		lStatement += " AND MOTNON.RV_DOMAIN = 'MOTIVO_NON_COMPUTABILE' ";
		lStatement += " AND MOTNON.RV_LOW_VALUE = COD_MOTIVO_NON_COMPUTABILE ";

		// modifica relativa al tipo istituto
		// lStatement += " AND ISTDET.RV_DOMAIN = 'TIPO_ISTITUTO' ";
		// lStatement += " AND ISTDET.RV_LOW_VALUE = COD_TIPO_ISTITUTO_DETENZIONE ";
		// lStatement += " AND LUOGODET.COD_COMUNE = COD_LUOGO_DETENZIONE ";
		lStatement += " AND TIPUFFRIF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND TIPUFFRIF.RV_LOW_VALUE = COD_TIPO_UFFICIO_RIFER ";
		lStatement += " AND LUOGOUFFRIF.COD_COMUNE = COD_LUOGO_UFFICIO_RIFER ";
		lStatement += " AND LUOGOEMITT.COD_COMUNE (+) = AUTORITA_EMITTENTE_LUOGO ";
		lStatement += " AND LUOGOCOMPE.COD_COMUNE (+) = AUTORITA_COMPETENTE_SEDE ";

		// modifica relativa autorita componente
		lStatement += " AND AUTCOMPETENTE.RV_DOMAIN (+) = 'TIPO_AUTORITA' ";
		lStatement += " AND AUTCOMPETENTE.RV_LOW_VALUE (+) = MISURA_CAUTELARE.AUTORITA_COMPETENTE ";

		return lStatement;
	}

	private String getSqlQueryNoDataNull() {

		String lStatement = new String("");
		lStatement += "SELECT "
				+ "ID_MISURA_CAUTELARE, COD_TIPO_MISURA, MIS.RV_MEANING TIPO_MIS, "
				+ "DATA_INIZIO, "
				+ "DATA_FINE, "
				+ "DATA_EMISSIONE_ORDINANZA, "
				+ "NUM_ANNI, "
				+ "NUM_MESI, "
				+ "NUM_GIORNI, "
				+ "GIORNI, "
				+ "FLAG_MODIFICA_MANUALE, "
				+ "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "EVE_ID_EVENTO,FLAG_COMPUTABILE, "
				+ "COD_MOTIVO_NON_COMPUTABILE,MOTNON.RV_MEANING DESC_MOTNON, "
				+
				// modifica relativa al tipo istituto
				"IST_DET_ID_ISTITUTO_DETENZIONE, "
				+
				// "COD_TIPO_ISTITUTO_DETENZIONE, ISTDET.RV_MEANING DESC_ISTDET, "+
				"ALTRO_LUOGO_DETENZIONE, "
				+
				// "COD_LUOGO_DETENZIONE, LUOGODET.DESCRIZIONE DESC_LUOGO, "+
				"NUM_RIFER, " + "COD_TIPO_UFFICIO_RIFER, TIPUFFRIF.RV_MEANING  DESC_UFF_RIF, "
				+ "COD_LUOGO_UFFICIO_RIFER,LUOGOUFFRIF.DESCRIZIONE DESC_LUOGO_RIF, " + "DATA_FUNGIBILITA, "
				+ "NOTE, " + "ANNO_FASC_BDMC, " + "NUME_FASC_BDMC, " + "ANNO_RGNR, " + "NUMERO_RGNR, " + "ANNO_REG_GEN, "
				+ "NUMERO_REG_GEN, " + "TIPO_UFFICIO_REG_GEN, " + "AUTORITA_EMITTENTE, "
				+ "AUTORITA_EMITTENTE_LUOGO, LUOGOEMITT.DESCRIZIONE  AUTORITA_EMITTENTE_LUOGO_DESC, "
				+ "AUTORITA_COMPETENTE, "
				+ "AUTORITA_COMPETENTE_SEDE, LUOGOCOMPE.DESCRIZIONE  AUTORITA_COMPETENTE_SEDE_DESC, "
				+ "AUTORITA_COMPETENTE_INDIRIZZO, " + "ANNO_RIFER, " + "CODICE_UFFICIO_PM_SEDE, "
				+ "POS_GIU_ID_POSIZIONE_GIURIDICA, AUTCOMPETENTE.RV_MEANING AUTORITA_COMPETENTE_DESC ";

		// modifica relativa al tipo istituto
		lStatement += "FROM MISURA_CAUTELARE,CG_REF_CODES MIS,CG_REF_CODES MOTNON,";// CG_REF_CODES ISTDET ,
																						// COMUNE LUOGODET ,";
		lStatement += " CG_REF_CODES TIPUFFRIF, CG_REF_CODES AUTCOMPETENTE, COMUNE LUOGOUFFRIF, COMUNE LUOGOEMITT, COMUNE LUOGOCOMPE ";
		lStatement += "WHERE ";
		lStatement += "MIS.RV_DOMAIN = 'TIPO_MISURA_CAUTELARE' ";
		lStatement += "AND MIS.RV_LOW_VALUE = COD_TIPO_MISURA ";
		lStatement += "AND MOTNON.RV_DOMAIN = 'MOTIVO_NON_COMPUTABILE' ";
		lStatement += "AND MOTNON.RV_LOW_VALUE = COD_MOTIVO_NON_COMPUTABILE ";

		// modifica relativa al tipo istituto
		// lStatement += "AND ISTDET.RV_DOMAIN = 'TIPO_ISTITUTO' ";
		// lStatement += "AND ISTDET.RV_LOW_VALUE = COD_TIPO_ISTITUTO_DETENZIONE ";
		// lStatement += "AND LUOGODET.COD_COMUNE = COD_LUOGO_DETENZIONE ";
		lStatement += "AND TIPUFFRIF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += "AND TIPUFFRIF.RV_LOW_VALUE = COD_TIPO_UFFICIO_RIFER ";
		lStatement += "AND LUOGOUFFRIF.COD_COMUNE = COD_LUOGO_UFFICIO_RIFER ";
		lStatement += "AND LUOGOEMITT.COD_COMUNE (+) = AUTORITA_EMITTENTE_LUOGO ";
		lStatement += "AND LUOGOCOMPE.COD_COMUNE (+) = AUTORITA_COMPETENTE_SEDE ";
		lStatement += "AND MISURA_CAUTELARE.DATA_INIZIO is not null ";
		lStatement += "AND MISURA_CAUTELARE.DATA_FINE is not null ";

		// modifica relativa autorita componente
		lStatement += "AND AUTCOMPETENTE.RV_DOMAIN (+) = 'TIPO_AUTORITA' ";
		lStatement += "AND AUTCOMPETENTE.RV_LOW_VALUE (+) = MISURA_CAUTELARE.AUTORITA_COMPETENTE ";

		return lStatement;
	}

	private String getSqlQuerySiDataNull() {

		String lStatement = new String("");
		lStatement += "SELECT "
				+ "ID_MISURA_CAUTELARE, COD_TIPO_MISURA, MIS.RV_MEANING TIPO_MIS, "
				+ "DATA_INIZIO, "
				+ "DATA_FINE, "
				+ "DATA_EMISSIONE_ORDINANZA, "
				+ "NUM_ANNI, "
				+ "NUM_MESI, "
				+ "NUM_GIORNI, "
				+ "GIORNI, "
				+ "FLAG_MODIFICA_MANUALE, "
				+ "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "EVE_ID_EVENTO,FLAG_COMPUTABILE, "
				+ "COD_MOTIVO_NON_COMPUTABILE,MOTNON.RV_MEANING DESC_MOTNON, "
				// modifica relativa al tipo istituto
				+ "IST_DET_ID_ISTITUTO_DETENZIONE, "
				// "COD_TIPO_ISTITUTO_DETENZIONE, ISTDET.RV_MEANING DESC_ISTDET, "+
				+ "ALTRO_LUOGO_DETENZIONE, "
				// "COD_LUOGO_DETENZIONE, LUOGODET.DESCRIZIONE DESC_LUOGO, "+
				+ "NUM_RIFER, " + "COD_TIPO_UFFICIO_RIFER, TIPUFFRIF.RV_MEANING  DESC_UFF_RIF, "
				+ "COD_LUOGO_UFFICIO_RIFER,LUOGOUFFRIF.DESCRIZIONE DESC_LUOGO_RIF, " + "DATA_FUNGIBILITA, "
				+ "NOTE, " + "ANNO_FASC_BDMC, " + "NUME_FASC_BDMC, " + "ANNO_RGNR, " + "NUMERO_RGNR, " + "ANNO_REG_GEN, "
				+ "NUMERO_REG_GEN, " + "TIPO_UFFICIO_REG_GEN, " + "AUTORITA_EMITTENTE, "
				+ "AUTORITA_EMITTENTE_LUOGO, LUOGOEMITT.DESCRIZIONE  AUTORITA_EMITTENTE_LUOGO_DESC, "
				+ "AUTORITA_COMPETENTE, "
				+ "AUTORITA_COMPETENTE_SEDE, LUOGOCOMPE.DESCRIZIONE  AUTORITA_COMPETENTE_SEDE_DESC, "
				+ "AUTORITA_COMPETENTE_INDIRIZZO, " + "ANNO_RIFER, " + "CODICE_UFFICIO_PM_SEDE, "
				+ "POS_GIU_ID_POSIZIONE_GIURIDICA, AUTCOMPETENTE.RV_MEANING AUTORITA_COMPETENTE_DESC ";
		// modifica relativa al tipo istituto
		lStatement += "FROM MISURA_CAUTELARE, CG_REF_CODES MIS, CG_REF_CODES MOTNON, ";// CG_REF_CODES ISTDET ,
																						// COMUNE LUOGODET ,";
		lStatement += "CG_REF_CODES TIPUFFRIF, CG_REF_CODES AUTCOMPETENTE, COMUNE LUOGOUFFRIF, COMUNE LUOGOEMITT, COMUNE LUOGOCOMPE ";
		lStatement += "WHERE ";
		lStatement += "MIS.RV_DOMAIN = 'TIPO_MISURA_CAUTELARE' ";
		lStatement += "AND MIS.RV_LOW_VALUE = COD_TIPO_MISURA ";
		lStatement += "AND MOTNON.RV_DOMAIN = 'MOTIVO_NON_COMPUTABILE' ";
		lStatement += "AND MOTNON.RV_LOW_VALUE = COD_MOTIVO_NON_COMPUTABILE ";

		// modifica relativa al tipo istituto
		// lStatement += "AND ISTDET.RV_DOMAIN = 'TIPO_ISTITUTO' ";
		// lStatement += "AND ISTDET.RV_LOW_VALUE = COD_TIPO_ISTITUTO_DETENZIONE ";
		// lStatement += "AND LUOGODET.COD_COMUNE = COD_LUOGO_DETENZIONE ";
		lStatement += "AND TIPUFFRIF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += "AND TIPUFFRIF.RV_LOW_VALUE = COD_TIPO_UFFICIO_RIFER ";
		lStatement += "AND LUOGOUFFRIF.COD_COMUNE = COD_LUOGO_UFFICIO_RIFER ";
		lStatement += "AND LUOGOEMITT.COD_COMUNE (+) = AUTORITA_EMITTENTE_LUOGO ";
		lStatement += "AND LUOGOCOMPE.COD_COMUNE (+) = AUTORITA_COMPETENTE_SEDE ";
		lStatement += "AND MISURA_CAUTELARE.DATA_INIZIO is null ";
		lStatement += "AND MISURA_CAUTELARE.DATA_FINE is null ";

		// modifica relativa autorita componente
		lStatement += "AND AUTCOMPETENTE.RV_DOMAIN (+) = 'TIPO_AUTORITA' ";
		lStatement += "AND AUTCOMPETENTE.RV_LOW_VALUE (+) = MISURA_CAUTELARE.AUTORITA_COMPETENTE ";

		return lStatement;
	}

	private String getSqlQuerySoloDataInizio() {

		String lStatement = new String("");
		lStatement += "SELECT "
				+ "ID_MISURA_CAUTELARE, COD_TIPO_MISURA, MIS.RV_MEANING TIPO_MIS, "
				+ "DATA_INIZIO, "
				+ "DATA_FINE, "
				+ "DATA_EMISSIONE_ORDINANZA, "
				+ "NUM_ANNI, "
				+ "NUM_MESI, "
				+ "NUM_GIORNI, "
				+ "GIORNI, "
				+ "FLAG_MODIFICA_MANUALE, "
				+ "COD_OPERATORE_INSERIMENTO, "
				+ "DATA_INSERIMENTO, "
				+ "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, "
				+ "DATA_AGGIORNAMENTO, "
				+ "COD_UFFICIO_AGGIORNAMENTO, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, "
				+ "EVE_ID_EVENTO,FLAG_COMPUTABILE, "
				+ "COD_MOTIVO_NON_COMPUTABILE,MOTNON.RV_MEANING DESC_MOTNON, "
				// modifica relativa al tipo istituto
				+ "IST_DET_ID_ISTITUTO_DETENZIONE, "
				// "COD_TIPO_ISTITUTO_DETENZIONE, ISTDET.RV_MEANING DESC_ISTDET, "+
				+ "ALTRO_LUOGO_DETENZIONE, "
				// "COD_LUOGO_DETENZIONE, LUOGODET.DESCRIZIONE DESC_LUOGO, "+
				+ "NUM_RIFER, " + "COD_TIPO_UFFICIO_RIFER, TIPUFFRIF.RV_MEANING  DESC_UFF_RIF, "
				+ "COD_LUOGO_UFFICIO_RIFER,LUOGOUFFRIF.DESCRIZIONE DESC_LUOGO_RIF, " + "DATA_FUNGIBILITA, "
				+ "NOTE, " + "ANNO_FASC_BDMC, " + "NUME_FASC_BDMC, " + "ANNO_RGNR, " + "NUMERO_RGNR, " + "ANNO_REG_GEN, "
				+ "NUMERO_REG_GEN, " + "TIPO_UFFICIO_REG_GEN, " + "AUTORITA_EMITTENTE, "
				+ "AUTORITA_EMITTENTE_LUOGO, LUOGOEMITT.DESCRIZIONE  AUTORITA_EMITTENTE_LUOGO_DESC, "
				+ "AUTORITA_COMPETENTE, "
				+ "AUTORITA_COMPETENTE_SEDE, LUOGOCOMPE.DESCRIZIONE  AUTORITA_COMPETENTE_SEDE_DESC, "
				+ "AUTORITA_COMPETENTE_INDIRIZZO, " + "ANNO_RIFER, " + "CODICE_UFFICIO_PM_SEDE, "
				+ "POS_GIU_ID_POSIZIONE_GIURIDICA, AUTCOMPETENTE.RV_MEANING AUTORITA_COMPETENTE_DESC ";
		// modifica relativa al tipo istituto
		lStatement += "FROM MISURA_CAUTELARE, CG_REF_CODES MIS, CG_REF_CODES MOTNON, ";// CG_REF_CODES ISTDET ,
																						// COMUNE LUOGODET ,";
		lStatement += "CG_REF_CODES TIPUFFRIF, CG_REF_CODES AUTCOMPETENTE, COMUNE LUOGOUFFRIF, COMUNE LUOGOEMITT, COMUNE LUOGOCOMPE ";
		lStatement += "WHERE ";
		lStatement += "MIS.RV_DOMAIN = 'TIPO_MISURA_CAUTELARE' ";
		lStatement += "AND MIS.RV_LOW_VALUE = COD_TIPO_MISURA ";
		lStatement += "AND MOTNON.RV_DOMAIN = 'MOTIVO_NON_COMPUTABILE' ";
		lStatement += "AND MOTNON.RV_LOW_VALUE = COD_MOTIVO_NON_COMPUTABILE ";

		// modifica relativa al tipo istituto
		// lStatement += "AND ISTDET.RV_DOMAIN = 'TIPO_ISTITUTO' ";
		// lStatement += "AND ISTDET.RV_LOW_VALUE = COD_TIPO_ISTITUTO_DETENZIONE ";
		// lStatement += "AND LUOGODET.COD_COMUNE = COD_LUOGO_DETENZIONE ";
		lStatement += "AND TIPUFFRIF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += "AND TIPUFFRIF.RV_LOW_VALUE = COD_TIPO_UFFICIO_RIFER ";
		lStatement += "AND LUOGOUFFRIF.COD_COMUNE = COD_LUOGO_UFFICIO_RIFER ";
		lStatement += "AND LUOGOEMITT.COD_COMUNE (+) = AUTORITA_EMITTENTE_LUOGO ";
		lStatement += "AND LUOGOCOMPE.COD_COMUNE (+) = AUTORITA_COMPETENTE_SEDE ";
		lStatement += "AND MISURA_CAUTELARE.DATA_INIZIO is not null ";
		lStatement += "AND MISURA_CAUTELARE.DATA_FINE is null ";

		// modifica relativa autorita componente
		lStatement += "AND AUTCOMPETENTE.RV_DOMAIN (+) = 'TIPO_AUTORITA' ";
		lStatement += "AND AUTCOMPETENTE.RV_LOW_VALUE (+) = MISURA_CAUTELARE.AUTORITA_COMPETENTE ";

		return lStatement;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModel() throws DAOException {
		MisuraCautelareModel aModel = new MisuraCautelareModel();

		aModel.setIdMisuraCautelare(getBigDecimal("ID_MISURA_CAUTELARE"));
		aModel.setCodTipoMisura(getString("COD_TIPO_MISURA"));
		aModel.setDescrTipoMisura(getString("TIPO_MIS"));
		aModel.setDataInizio(getDate("DATA_INIZIO"));
		aModel.setDataFine(getDate("DATA_FINE"));
		aModel.setDataEmissioneOrdinanza(getDate("DATA_EMISSIONE_ORDINANZA"));
		aModel.setNumAnni(getBigDecimal("NUM_ANNI"));
		aModel.setNumMesi(getBigDecimal("NUM_MESI"));
		aModel.setNumGiorni(getBigDecimal("NUM_GIORNI"));
		aModel.setGiorni(getBigDecimal("GIORNI"));
		aModel.setCodOperatoreInserimento(getString("COD_OPERATORE_INSERIMENTO"));
		aModel.setDataInserimento(getDate("DATA_INSERIMENTO"));
		aModel.setCodUfficioInserimento(getString("COD_UFFICIO_INSERIMENTO"));
		// aModel.setDescrUfficioInserimento(getString("") );
		aModel.setCodOperatoreAggiornamento(getString("COD_OPERATORE_AGGIORNAMENTO"));
		aModel.setDataAggiornamento(getDate("DATA_AGGIORNAMENTO"));
		aModel.setCodUfficioAggiornamento(getString("COD_UFFICIO_AGGIORNAMENTO"));
		// aModel.setDescrUfficioAggiornamento(getString("") );
		aModel.setFasSieIdFascicoloSiep(getBigDecimal("FAS_SIE_ID_FASCICOLO_SIEP"));
		aModel.setEveIdEvento(getBigDecimal("EVE_ID_EVENTO"));
		aModel.setFlagComputabile(getString("FLAG_COMPUTABILE"));
		aModel.setFlagModificaManuale(getString("FLAG_MODIFICA_MANUALE"));
		aModel.setCodMotivoNonComputabile(getString("COD_MOTIVO_NON_COMPUTABILE"));
		aModel.setDescrMotivoNonComputabile(getString("DESC_MOTNON"));
		// modifica relativa al tipo istituto
		aModel.setIstDetIdIstitutoDetenzione(getString("IST_DET_ID_ISTITUTO_DETENZIONE"));
		// aModel.setCodTipoIstitutoDetenzione(getString("COD_TIPO_ISTITUTO_DETENZIONE") );
		// aModel.setDescrTipoIstitutoDetenzione(getString("DESC_ISTDET") );
		aModel.setAltroLuogoDetenzione(getString("ALTRO_LUOGO_DETENZIONE"));
		// aModel.setCodLuogoDetenzione(getString("COD_LUOGO_DETENZIONE") );
		// aModel.setDescrLuogoDetenzione(getString("DESC_LUOGO") );
		aModel.setNumRifer(getString("NUM_RIFER"));
		aModel.setCodTipoUfficioRifer(getString("COD_TIPO_UFFICIO_RIFER"));
		aModel.setDescrTipoUfficioRifer(getString("DESC_UFF_RIF"));
		aModel.setCodLuogoUfficioRifer(getString("COD_LUOGO_UFFICIO_RIFER"));
		aModel.setDescrLuogoUfficioRifer(getString("DESC_LUOGO_RIF"));
		aModel.setDataFungibilita(getDate("DATA_FUNGIBILITA"));
		aModel.setNote(getString("NOTE"));

		aModel.setAnnoFascBdmc(getBigDecimal("ANNO_FASC_BDMC"));
		aModel.setNumeFascBdmc(getBigDecimal("NUME_FASC_BDMC"));
		aModel.setAnnoRgnr(getBigDecimal("ANNO_RGNR"));
		aModel.setNumeroRgnr(getBigDecimal("NUMERO_RGNR"));
		aModel.setAnnoRegGen(getBigDecimal("ANNO_REG_GEN"));
		aModel.setNumeroRegGen(getBigDecimal("NUMERO_REG_GEN"));
		aModel.setTipoUfficioRegGen(getString("TIPO_UFFICIO_REG_GEN"));
		aModel.setAutoritaEmittente(getString("AUTORITA_EMITTENTE"));
		aModel.setAutoritaEmittenteLuogo(getString("AUTORITA_EMITTENTE_LUOGO"));
		aModel.setAutoritaEmittenteLuogoDesc(getString("AUTORITA_EMITTENTE_LUOGO_DESC"));
		aModel.setAutoritaCompetente(getString("AUTORITA_COMPETENTE"));
		aModel.setAutoritaCompetenteDesc(getString("AUTORITA_COMPETENTE_DESC"));
		aModel.setAutoritaCompetenteSede(getString("AUTORITA_COMPETENTE_SEDE"));
		aModel.setAutoritaCompetenteSedeDesc(getString("AUTORITA_COMPETENTE_SEDE_DESC"));
		aModel.setAutoritaCompetenteIndirizzo(getString("AUTORITA_COMPETENTE_INDIRIZZO"));
		aModel.setAnnoRifer(getBigDecimal("ANNO_RIFER"));
		aModel.setCodiceUfficioPmSede(getString("CODICE_UFFICIO_PM_SEDE"));
		aModel.setPosGiuIdPosizioneGiuridica(getBigDecimal("POS_GIU_ID_POSIZIONE_GIURIDICA"));

		return aModel;
	}

	public String setCondizione(MisuraCautelareModel aModel) {
		String lCondizioni = "";

		if (aModel.getFasSieIdFascicoloSiep() != null)
			lCondizioni = " AND FAS_SIE_ID_FASCICOLO_SIEP = " + aModel.getFasSieIdFascicoloSiep();
		if (aModel.getCodTipoMisura() != null && !(aModel.getCodTipoMisura()).equals(""))
			lCondizioni += " AND COD_TIPO_MISURA = '" + aModel.getCodTipoMisura() + "'";
		if (aModel.getFlagComputabile() != null && !(aModel.getFlagComputabile()).equals(""))
			lCondizioni += " AND FLAG_COMPUTABILE = '" + aModel.getFlagComputabile() + "'";
		if (aModel.getPosGiuIdPosizioneGiuridica() != null)
			lCondizioni += " AND POS_GIU_ID_POSIZIONE_GIURIDICA = " + aModel.getPosGiuIdPosizioneGiuridica();
		return lCondizioni;
	}

	public String setCondizioniByKey(BigDecimal aKey) {
		return " AND ID_MISURA_CAUTELARE = " + aKey;
	}

	/**
	 * MERGE v10: aggiunta funzione di ricerca
	 * 
	 * @param aIdFascicolo
	 */
	public void newRicercaMisuraCautelareByFascicolo(BigDecimal aIdFascicolo) {

		String lStatement = new String("");

		lStatement += "SELECT "
				+ "distinct COD_TIPO_MISURA, null as ID_MISURA_CAUTELARE, MIS.RV_MEANING TIPO_MIS,"
				+ "DATA_INIZIO, " + "DATA_FINE, " + "DATA_EMISSIONE_ORDINANZA, " + "NUM_ANNI, "
				+ "NUM_MESI, " + "NUM_GIORNI, " + "GIORNI, " + "FLAG_MODIFICA_MANUALE, "
				+ "COD_OPERATORE_INSERIMENTO, " + "DATA_INSERIMENTO, " + "COD_UFFICIO_INSERIMENTO, "
				+ "COD_OPERATORE_AGGIORNAMENTO, " + "DATA_AGGIORNAMENTO, " + "COD_UFFICIO_AGGIORNAMENTO, "
				+ "FAS_SIE_ID_FASCICOLO_SIEP, " + "EVE_ID_EVENTO,FLAG_COMPUTABILE, "
				+ "COD_MOTIVO_NON_COMPUTABILE,MOTNON.RV_MEANING DESC_MOTNON, "
				+ "IST_DET_ID_ISTITUTO_DETENZIONE, " + "ALTRO_LUOGO_DETENZIONE, " + "NUM_RIFER, "
				+ "COD_TIPO_UFFICIO_RIFER, TIPUFFRIF.RV_MEANING  DESC_UFF_RIF, "
				+ "COD_LUOGO_UFFICIO_RIFER,LUOGOUFFRIF.DESCRIZIONE DESC_LUOGO_RIF, " + "DATA_FUNGIBILITA, "
				+ "NOTE, " + "ANNO_FASC_BDMC, " + "NUME_FASC_BDMC, " + "ANNO_RGNR, " + "NUMERO_RGNR, "
				+ "ANNO_REG_GEN, " + "NUMERO_REG_GEN, " + "TIPO_UFFICIO_REG_GEN, " + "AUTORITA_EMITTENTE, "
				+ "AUTORITA_EMITTENTE_LUOGO, LUOGOEMITT.DESCRIZIONE  AUTORITA_EMITTENTE_LUOGO_DESC, "
				+ "AUTORITA_COMPETENTE, "
				+ "AUTORITA_COMPETENTE_SEDE, LUOGOCOMPE.DESCRIZIONE  AUTORITA_COMPETENTE_SEDE_DESC, "
				+ "AUTORITA_COMPETENTE_INDIRIZZO, " + "ANNO_RIFER, " + "CODICE_UFFICIO_PM_SEDE, "
				+ "null as POS_GIU_ID_POSIZIONE_GIURIDICA, "
				+ "AUTCOMPETENTE.RV_MEANING AUTORITA_COMPETENTE_DESC";
		lStatement += " FROM MISURA_CAUTELARE,CG_REF_CODES MIS,CG_REF_CODES MOTNON,";
		lStatement += " CG_REF_CODES TIPUFFRIF, CG_REF_CODES AUTCOMPETENTE, COMUNE LUOGOUFFRIF , COMUNE LUOGOEMITT , COMUNE LUOGOCOMPE ";
		lStatement += " WHERE ";
		lStatement += " MIS.RV_DOMAIN = 'TIPO_MISURA_CAUTELARE' ";
		lStatement += " AND MIS.RV_LOW_VALUE = COD_TIPO_MISURA ";
		lStatement += " AND MOTNON.RV_DOMAIN = 'MOTIVO_NON_COMPUTABILE' ";
		lStatement += " AND MOTNON.RV_LOW_VALUE = COD_MOTIVO_NON_COMPUTABILE ";
		lStatement += " AND TIPUFFRIF.RV_DOMAIN = 'TIPO_UFFICIO' ";
		lStatement += " AND TIPUFFRIF.RV_LOW_VALUE = COD_TIPO_UFFICIO_RIFER ";
		lStatement += " AND LUOGOUFFRIF.COD_COMUNE = COD_LUOGO_UFFICIO_RIFER ";
		lStatement += " AND LUOGOEMITT.COD_COMUNE (+) = AUTORITA_EMITTENTE_LUOGO ";
		lStatement += " AND LUOGOCOMPE.COD_COMUNE (+) = AUTORITA_COMPETENTE_SEDE ";
		lStatement += " AND AUTCOMPETENTE.RV_DOMAIN (+) = 'TIPO_AUTORITA' ";
		lStatement += " AND AUTCOMPETENTE.RV_LOW_VALUE (+) = MISURA_CAUTELARE.AUTORITA_COMPETENTE ";
		lStatement += " AND FAS_SIE_ID_FASCICOLO_SIEP=" + aIdFascicolo;
		lStatement += " ORDER BY DATA_INIZIO ,DATA_FINE";

		setStatement(lStatement);
	}

}