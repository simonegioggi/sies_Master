package siap.sige.statistiche.dao;

import java.sql.Connection;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.DateUtils;
import siap.dao.SIAPSqlDAO;
import siap.sige.statistiche.model.RicercaFogliCompModel;
import siap.sige.statistiche.model.RiepilogoStatisticheFogliComplementari;
import siap.sige.statistiche.model.StatisticheFogliComplementariModel;

public class StatisticheFogliComplementariSqlDAO extends SIAPSqlDAO {

	public StatisticheFogliComplementariSqlDAO(Connection con) {

		super(con);
	}

	public void ricercaStatisticheFogliComplementari(RicercaFogliCompModel filtroModel) {

		String sql = "";
		if (filtroModel.isFcTrasmessi()) {
			sql = getQueryFcTrasmessi(filtroModel);
		}

		if (filtroModel.isFcIscrittiManualmente()) {
			if (!sql.equals(""))
				sql += " UNION ";
			// Ticket#20230202011 - si elimina la UNION ALL che duplica i record degli iscritti manualmente
			// sql += " UNION ALL ";
			sql += this.getQueryFcTrasmessiManualmente(filtroModel);
		}

		if (filtroModel.isProvvedimentiPriviFC()) {
			if (!sql.equals(""))
				sql += " UNION ";
			// Ticket#20230202011 - si elimina la UNION ALL che duplica i record degli iscritti manualmente
			// sql += " UNION ALL ";
			sql += this.getQueryProvvedimentiPriviFC(filtroModel);
		}

		if (filtroModel.isFcAnnullati()) {
			if (!sql.equals(""))
				sql += " UNION ";
			// Ticket#20230202011 - si elimina la UNION ALL che duplica i record degli iscritti manualmente
			// sql += " UNION ALL ";
			sql += this.getQueryProvvedimentiFcAnnullati(filtroModel);
		}
		sql += " order by data_emissione_provv, PRG ";
		// Ticket#20230202011 - ulteriori campi per order by
		sql += " , annoFC,  numFC ";
		// Ticket#20230202011 - FINE
		super.setStatement(sql);
	}

	public void ricercaFcAnnullati(RicercaFogliCompModel filtroModel) {

		String sql = this.getQueryProvvedimentiFcAnnullati(filtroModel);
		sql += " order by data_emissione_provv ";
		// Ticket#20230202011 - ulteriori campi per order by
		sql += " , prg, annoFC,  numFC  ";
		// Ticket#20230202011 - FINE
		super.setStatement(sql);
	}

	public void ricercaConteggioFcAnnullati(RicercaFogliCompModel filtroModel) {

		String sql = this.getQueryConteggioProvvedimentiFcAnnullati(filtroModel);
		super.setStatement(sql);
	}

	public void ricercaIscrittiManualmente(RicercaFogliCompModel filtroModel) {

		String sql = this.getQueryFcTrasmessiManualmente(filtroModel);
		sql += " order by data_emissione_provv ";
		// Ticket#20230202011 - ulteriori campi per order by
		sql += " , prg, annoFC,  numFC  ";
		// Ticket#20230202011 - FINE
		super.setStatement(sql);
	}

	public void ricercaConteggioFcIscrittiManualmente(RicercaFogliCompModel filtroModel) {

		String sql = this.getQueryConteggioFcTrasmessiManualmente(filtroModel);
		super.setStatement(sql);
	}

	public void ricercaProvvedimentiPriviFC(RicercaFogliCompModel filtroModel) {

		String sql = this.getQueryProvvedimentiPriviFC(filtroModel);
		sql += " order by data_emissione_provv ";
		// Ticket#20230202011 - ulteriori campi per order by
		sql += " , prg, annoFC,  numFC  ";
		// Ticket#20230202011 - FINE
		super.setStatement(sql);
	}

	public void ricercaConteggioProvvedimentiPriviFC(RicercaFogliCompModel filtroModel) {

		String sql = this.getQueryConteggioProvvedimentiPriviFC(filtroModel);
		super.setStatement(sql);
	}

	public void ricercaConteggioProvvedimentiConFC(RicercaFogliCompModel filtroModel) {

		String sql = this.getQueryConteggioProvvedimentiConFC(filtroModel);
		super.setStatement(sql);
	}

	public void ricercaProvvedimentiConFC(RicercaFogliCompModel filtroModel) {

		String sql = this.getQueryFcTrasmessi(filtroModel);
		// Ticket#20230202011 - ulteriori campi per order by
		sql += " order by data_emissione_provv ";
		sql += " , prg, annoFC,  numFC  ";
		// Ticket#20230202011 - FINE
		super.setStatement(sql);
	}

	private String getQueryFcTrasmessi(RicercaFogliCompModel filtroModel) {

		String sql = "SELECT D.ID_FASCICOLO_SIGE PRG, " + "D.chiave_anno||'/'||D.chiave_progr num_fasc_SIGE, "
				+ "A.data_emissione data_emissione_provv, " +
				// Ticket#20210514016 - recuparato descrizione oggetto dal Tenore invece che da Evento
				// "E.RV_MEANING motivo, " +
				"TipoP.RV_MEANING||' - '||ET.RV_MEANING motivo, " +
				// Ticket#20210514016 - FINE
				"to_char(B.DATA_EMISSIONE,'DD-MM-YYYY') DATA_EMISSIONE_FOGLIO, "
				+ "decode(b.data_ins_man,null,'Trasmesso','Iscritto Manualmente') esito" +
				// Ticket#20230202011 - Aggiunti ulteriori campi alla select
				", A.CHIAVE_ANNO||'/'||A.CHIAVE_PROGR as annoNumeroProvvedimento"
				+ ", B.ANNO_FOGLIO_COMPLEMENTARE||'/'||B.PROGR_FOGLIO_COMPLEMENTARE as annoNumeroFoglioComplementare"
				// solo per order by
				+ ", B.ANNO_FOGLIO_COMPLEMENTARE as annoFC, B.PROGR_FOGLIO_COMPLEMENTARE as numFC" + 
				// Ticket#20230202011 - FINE
				" FROM PROVVEDIMENTO_SIGE A, DOCUMENTO_ALLEGATO B, EVENTO C, "
				+ "FASCICOLO_SIGE D, CG_REF_CODES E " +
				// Ticket#20210514016 - recuparato descrizione oggetto dal Tenore invece che da Evento
				" , CG_REF_CODES ET, TENORE_SIGE T, CG_REF_CODES TipoP" +
				// Ticket#20210514016 - FINE
				" WHERE A.FAS_ID_FASCICOLO_SIGE = D.ID_FASCICOLO_SIGE AND "
				+ "C.COD_MOTIVO = E.RV_LOW_VALUE AND E.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND "
				+ "A.ID_EVENTO_GENERATO = C.ID_EVENTO AND " + "C.COD_TIPO_EVENTO = '01' AND "
				+ "A.COD_TIPO_PROVVEDIMENTO_SIGE IN ('02','03') AND "
				+ "A.COD_TIPO_PROVVEDIMENTO IN ('02','03') AND " + "B.EVE_ID_EVENTO = C.ID_EVENTO AND "
				+ "B.COD_TIPO_DOCUMENTO = '06' AND " + "B.DATA_ANNULLAMENTO IS NULL AND " +
				// Ticket#20210514016 - recuparato descrizione oggetto dal Tenore invece che da Evento
				" T.PROV_ID_PROVVEDIMENTO_SIGE = A.ID_PROVVEDIMENTO_SIGE AND "
				+ " ET.RV_DOMAIN = 'OGGETTO_SIGE' AND " + " T.COD_OGGETTO_SIGE = ET.RV_LOW_VALUE AND "
				+ " TIPOP.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND "
				+ " TIPOP.RV_LOW_VALUE = C.COD_TIPO_PROVVEDIMENTO AND " +
				// Ticket#20210514016 - FINE
				" D.COD_UFFICIO_INSERIMENTO='" + filtroModel.getCodUfficioInserimento() + "'";

		if (filtroModel.getAnnoIniziale() != null) {
			if (filtroModel.getAnnoFinale() == null)
				sql += " and B.ANNO_FOGLIO_COMPLEMENTARE=" + filtroModel.getAnnoIniziale();
			if (filtroModel.getAnnoFinale() != null)
				sql += " and B.ANNO_FOGLIO_COMPLEMENTARE between " + filtroModel.getAnnoIniziale() + " and "
						+ filtroModel.getAnnoFinale();
		}

		if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and b.data_emissione = TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			} else {
				sql += " and b.data_emissione between TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' ) and TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneFinale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			}
		}

		return sql;
	}

	private String getQueryFcTrasmessiManualmente(RicercaFogliCompModel filtroModel) {

		String sql = "SELECT D.ID_FASCICOLO_SIGE prg, " + "D.chiave_anno||'/'||D.chiave_progr num_fasc_sige, "
				+ "A.data_emissione data_emissione_provv, " +
				// Ticket#20210514016 - recuparato descrizione oggetto dal Tenore invece che da Evento
				// "E.RV_MEANING motivo, " +
				"TipoP.RV_MEANING||' - '||ET.RV_MEANING motivo, " +
				// Ticket#20210514016 - FINE
				"to_char (B.DATA_EMISSIONE,'dd-MM-yyyy') as DATA_EMISSIONE_FOGLIO, "
				+ "'Iscritto Manualmente' esito " +
				// Ticket#20230202011 - Aggiunti ulteriori campi alla select
				", A.CHIAVE_ANNO||'/'||A.CHIAVE_PROGR as annoNumeroProvvedimento"
				+ ", B.ANNO_FOGLIO_COMPLEMENTARE||'/'||B.PROGR_FOGLIO_COMPLEMENTARE as annoNumeroFoglioComplementare"
				// solo per order by
				+ ", B.ANNO_FOGLIO_COMPLEMENTARE as annoFC, B.PROGR_FOGLIO_COMPLEMENTARE as numFC " +
				// Ticket#20230202011 - FINE
				"FROM PROVVEDIMENTO_SIGE A, DOCUMENTO_ALLEGATO B, EVENTO C, "
				+ "FASCICOLO_SIGE D, CG_REF_CODES E" +
				// Ticket#20210514016 - recuparato descrizione oggetto dal Tenore invece che da Evento
				", CG_REF_CODES ET, TENORE_SIGE T, CG_REF_CODES TipoP " +
				// Ticket#20210514016 - FINE
				"WHERE A.FAS_ID_FASCICOLO_SIGE = D.ID_FASCICOLO_SIGE AND " + "D.COD_UFFICIO_INSERIMENTO='"
				+ filtroModel.getUfficioConnesso().getCodUfficio() + "' AND "
				+ "C.COD_MOTIVO = E.RV_LOW_VALUE AND E.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND "
				+ "A.ID_EVENTO_GENERATO = C.ID_EVENTO AND " + "C.COD_TIPO_EVENTO = '01'  AND "
				+ "A.COD_TIPO_PROVVEDIMENTO_SIGE IN ('02','03') AND "
				+ "A.COD_TIPO_PROVVEDIMENTO IN ('02','03') AND " + "B.EVE_ID_EVENTO = C.ID_EVENTO AND "
				+ "B.COD_TIPO_DOCUMENTO = '06' AND " +
				// Ticket#20210514016 - recuparato descrizione oggetto dal Tenore invece che da Evento
				" T.PROV_ID_PROVVEDIMENTO_SIGE = A.ID_PROVVEDIMENTO_SIGE AND "
				+ " ET.RV_DOMAIN = 'OGGETTO_SIGE' AND " + " T.COD_OGGETTO_SIGE = ET.RV_LOW_VALUE AND "
				+ " TIPOP.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND "
				+ " TIPOP.RV_LOW_VALUE = C.COD_TIPO_PROVVEDIMENTO AND " +
				// Ticket#20210514016 - FINE
				"B.DATA_ANNULLAMENTO IS NULL AND B.DATA_INS_MAN is not NULL ";

		if (filtroModel.getAnnoIniziale() != null) {
			if (filtroModel.getAnnoFinale() == null)
				sql += " and B.ANNO_FOGLIO_COMPLEMENTARE=" + filtroModel.getAnnoIniziale();
			if (filtroModel.getAnnoFinale() != null)
				sql += " and B.ANNO_FOGLIO_COMPLEMENTARE between " + filtroModel.getAnnoIniziale() + " and "
						+ filtroModel.getAnnoFinale();
		}

		if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and b.data_emissione = TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			} else {
				sql += " and b.data_emissione between TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' ) and TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneFinale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			}
		}

		return sql;
	}

	private String getQueryProvvedimentiPriviFC(RicercaFogliCompModel filtroModel) {

		String sql = "SELECT DISTINCT(D.ID_FASCICOLO_SIGE) prg , "
				+ "D.chiave_anno||'/'||D.chiave_progr num_fasc_SIGE, "
				+ "A.data_emissione data_emissione_provv, " +
				// Ticket#20210514016 - recuparato descrizione oggetto dal Tenore invece che da Evento
				// "E.RV_MEANING motivo, " +
				"TipoP.RV_MEANING||' - '||ET.RV_MEANING motivo, " +
				// Ticket#20210514016 - FINE
				"'-' DATA_EMISSIONE_FOGLIO, " + "'Privo di Foglio Complementare' esito" +
				// Ticket#20230202011 - Aggiunti ulteriori campi alla select
				", A.CHIAVE_ANNO||'/'||A.CHIAVE_PROGR as annoNumeroProvvedimento"
				// solo per order by
				+ ", null as annoNumeroFoglioComplementare, null as annoFC, null as numFC " +
				// Ticket#20230202011 - FINE
				"FROM PROVVEDIMENTO_SIGE A, EVENTO C, FASCICOLO_SIGE D, CG_REF_CODES E" +
				// Ticket#20210514016 - recuparato descrizione oggetto dal Tenore invece che da Evento
				", CG_REF_CODES ET, TENORE_SIGE T, CG_REF_CODES TipoP " +
				// Ticket#20210514016 - FINE
				"WHERE A.FAS_ID_FASCICOLO_SIGE = D.ID_FASCICOLO_SIGE AND " +
				// Ticket#20210514016 - recuparato descrizione oggetto dal Tenore invece che da Evento
				" T.PROV_ID_PROVVEDIMENTO_SIGE = A.ID_PROVVEDIMENTO_SIGE AND "
				+ " ET.RV_DOMAIN = 'OGGETTO_SIGE' AND " + " T.COD_OGGETTO_SIGE = ET.RV_LOW_VALUE AND "
				+ " TIPOP.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND "
				+ " TIPOP.RV_LOW_VALUE = C.COD_TIPO_PROVVEDIMENTO AND " +
				// Ticket#20210514016 - FINE
				"D.COD_UFFICIO_INSERIMENTO='" + filtroModel.getCodUfficioInserimento() + "' and "
				+ "c.cod_motivo = e.RV_LOW_VALUE AND e.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' "
				+ "AND A.ID_EVENTO_GENERATO = C.ID_EVENTO AND "
				+ "(C.FLAG_DOCUMENTO_REGISTRATO IS NULL OR C.FLAG_DOCUMENTO_REGISTRATO <> 'A')  AND "
				+ "C.COD_TIPO_EVENTO = '01'  AND " + "A.COD_TIPO_PROVVEDIMENTO_SIGE IN ('02','03') AND "
				+ "A.COD_TIPO_PROVVEDIMENTO IN ('02','03') AND "
				+ "C.ID_EVENTO NOT IN ( SELECT B.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO B, "
				+ "EVENTO G WHERE G.ID_EVENTO = B.EVE_ID_EVENTO AND B.COD_TIPO_DOCUMENTO  = '06') ";

		if (filtroModel.getAnnoIniziale() != null) {
			if (filtroModel.getAnnoFinale() == null)
				sql += " and A.CHIAVE_ANNO=" + filtroModel.getAnnoIniziale();
			if (filtroModel.getAnnoFinale() != null)
				sql += " and A.CHIAVE_ANNO between " + filtroModel.getAnnoIniziale() + " and "
						+ filtroModel.getAnnoFinale();
		}

		if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and a.data_emissione = TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			} else {
				sql += " and a.data_emissione between TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' ) and TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneFinale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			}
		}

		return sql;
	}

	private String getQueryProvvedimentiFcAnnullati(RicercaFogliCompModel filtroModel) {

		String sql = "SELECT D.ID_FASCICOLO_SIGE prg, " + "D.chiave_anno||'/'||D.chiave_progr num_fasc_sige, "
				+ "A.data_emissione data_emissione_provv, " +
				// Ticket#20210514016 - recuparato descrizione oggetto dal Tenore invece che da Evento
				// "E.RV_MEANING motivo, " +
				" TipoP.RV_MEANING||' - '||ET.RV_MEANING motivo, " +
				// Ticket#20210514016 - FINE
				"to_char (B.DATA_EMISSIONE,'dd-MM-yyyy') as DATA_EMISSIONE_FOGLIO, " + "'Annullato' esito " +
				// Ticket#20230202011 - Aggiunti ulteriori campi alla select
				", A.CHIAVE_ANNO||'/'||A.CHIAVE_PROGR as annoNumeroProvvedimento "
				+ ", B.ANNO_FOGLIO_COMPLEMENTARE||'/'||B.PROGR_FOGLIO_COMPLEMENTARE as annoNumeroFoglioComplementare "
				// solo per order by
				+ ", B.ANNO_FOGLIO_COMPLEMENTARE as annoFC, B.PROGR_FOGLIO_COMPLEMENTARE as numFC " +
				// Ticket#20230202011 - FINE
				" FROM PROVVEDIMENTO_SIGE A, DOCUMENTO_ALLEGATO B, EVENTO C, "
				+ "FASCICOLO_SIGE D, CG_REF_CODES E " +
				// Ticket#20210514016 - recuparato descrizione oggetto dal Tenore invece che da Evento
				" , CG_REF_CODES ET, TENORE_SIGE T " + " , CG_REF_CODES TipoP " +
				// Ticket#20210514016 - FINE
				" WHERE A.FAS_ID_FASCICOLO_SIGE = D.ID_FASCICOLO_SIGE AND " + "D.COD_UFFICIO_INSERIMENTO='"
				+ filtroModel.getUfficioConnesso().getCodUfficio() + "' AND "
				+ "C.COD_MOTIVO = E.RV_LOW_VALUE AND E.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' AND "
				+ "A.ID_EVENTO_GENERATO = C.ID_EVENTO AND " + "C.COD_TIPO_EVENTO = '01'  AND "
				+ "A.COD_TIPO_PROVVEDIMENTO_SIGE IN ('02','03') AND "
				+ "A.COD_TIPO_PROVVEDIMENTO IN ('02','03') AND " + "B.EVE_ID_EVENTO = C.ID_EVENTO AND "
				+ "B.COD_TIPO_DOCUMENTO = '06' AND " +
				// Ticket#20210514016 - recuparato descrizione oggetto dal Tenore invece che da Evento
				" T.PROV_ID_PROVVEDIMENTO_SIGE = A.ID_PROVVEDIMENTO_SIGE AND "
				+ " ET.RV_DOMAIN = 'OGGETTO_SIGE' AND " + " T.COD_OGGETTO_SIGE = ET.RV_LOW_VALUE AND "
				+ " TIPOP.RV_DOMAIN = 'TIPO_PROVVEDIMENTO' AND "
				+ " TIPOP.RV_LOW_VALUE = C.COD_TIPO_PROVVEDIMENTO AND " +
				// Ticket#20210514016 - FINE
				"B.DATA_ANNULLAMENTO IS NOT NULL ";

		if (filtroModel.getAnnoIniziale() != null) {
			if (filtroModel.getAnnoFinale() == null)
				sql += " and B.ANNO_FOGLIO_COMPLEMENTARE=" + filtroModel.getAnnoIniziale();
			if (filtroModel.getAnnoFinale() != null)
				sql += " and B.ANNO_FOGLIO_COMPLEMENTARE between " + filtroModel.getAnnoIniziale() + " and "
						+ filtroModel.getAnnoFinale();
		}

		if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and b.data_emissione = TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			} else {
				sql += " and b.data_emissione between TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' ) and TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneFinale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			}
		}

		return sql;
	}

	private String getQueryConteggioProvvedimentiFcAnnullati(RicercaFogliCompModel filtroModel) {

		String sql = "select a.chiave_anno as anno, count (b.chiave_anno) as conteggio, 'Fogli Complementari Annullati' as descrizione from provvedimento_sige A, "
				+ "(" + "SELECT A.ID_PROVVEDIMENTO_SIGE, A.CHIAVE_ANNO, B.data_emissione as data_emissione " +
				// Ticket#20230202011 - Aggiunti ANNO_FOGLIO_COMPLEMENTARE x la where condition cambiata
				", B.ANNO_FOGLIO_COMPLEMENTARE " +
				// Ticket#20230202011 - FINE
				"FROM PROVVEDIMENTO_SIGE A, DOCUMENTO_ALLEGATO B, FASCICOLO_SIGE D WHERE "
				+ "A.FAS_ID_FASCICOLO_SIGE = D.ID_FASCICOLO_SIGE AND " + "D.COD_UFFICIO_INSERIMENTO='"
				+ filtroModel.getCodUfficioInserimento() + "' and "
				+ "A.ID_EVENTO_GENERATO = B.EVE_ID_EVENTO AND "
				+ "A.COD_TIPO_PROVVEDIMENTO_SIGE IN ('02','03') AND "
				+ "A.COD_TIPO_PROVVEDIMENTO IN ('02','03') AND " + "B.COD_TIPO_DOCUMENTO = '06' AND "
				+ "B.DATA_ANNULLAMENTO IS NOT NULL " + " ) B where "
				+ " A.ID_PROVVEDIMENTO_SIGE=B.ID_PROVVEDIMENTO_SIGE(+) and " + " A.chiave_anno is not NULL ";

		// Ticket#20230202011 - Si sposta il controllo su ANNO_FOGLIO_COMPLEMENTARE come per il dettaglio
		if (filtroModel.getAnnoIniziale() != null) {
			if (filtroModel.getAnnoFinale() == null)
				sql += " and B.ANNO_FOGLIO_COMPLEMENTARE=" + filtroModel.getAnnoIniziale();
			if (filtroModel.getAnnoFinale() != null) {
				sql += " and B.ANNO_FOGLIO_COMPLEMENTARE between " + filtroModel.getAnnoIniziale() + " and "
						+ filtroModel.getAnnoFinale();
			}
		}
		/*
		 * if (filtroModel.getAnnoIniziale() != null) { if (filtroModel.getAnnoFinale() == null) sql +=
		 * " and A.chiave_anno=" + filtroModel.getAnnoIniziale(); if (filtroModel.getAnnoFinale() != null) sql
		 * += " and a.chiave_anno between " + filtroModel.getAnnoIniziale() +" and " +
		 * filtroModel.getAnnoFinale(); }
		 */
		// Ticket#20230202011 - FINE
		if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and b.data_emissione = TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			} else {
				sql += " and b.data_emissione between TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' ) and TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneFinale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			}
		}
		sql += " group by a.chiave_anno ";
		sql += " order by a.chiave_anno ";
		return sql;
	}

	private String getQueryConteggioFcTrasmessiManualmente(RicercaFogliCompModel filtroModel) {

		String sql = "select a.chiave_anno as anno, count (b.chiave_anno) as conteggio, 'Fogli Complementari Iscritti Manualmente' as descrizione from provvedimento_sige A, "
				+ "(" + "SELECT A.ID_PROVVEDIMENTO_SIGE, A.CHIAVE_ANNO, b.data_emissione as data_emissione " +
				// Ticket#20230202011 - Aggiunti ANNO_FOGLIO_COMPLEMENTARE x la where condition cambiata
				", B.ANNO_FOGLIO_COMPLEMENTARE " +
				// Ticket#20230202011 - FINE
				"FROM PROVVEDIMENTO_SIGE A, DOCUMENTO_ALLEGATO B, FASCICOLO_SIGE D WHERE "
				+ "A.FAS_ID_FASCICOLO_SIGE = D.ID_FASCICOLO_SIGE AND " + "D.COD_UFFICIO_INSERIMENTO='"
				+ filtroModel.getCodUfficioInserimento() + "' and "
				+ "A.ID_EVENTO_GENERATO = B.EVE_ID_EVENTO AND "
				+ "A.COD_TIPO_PROVVEDIMENTO_SIGE IN ('02','03') AND "
				+ "A.COD_TIPO_PROVVEDIMENTO IN ('02','03') AND " + "B.COD_TIPO_DOCUMENTO = '06' AND "
				+ "B.DATA_ANNULLAMENTO IS NULL and B.DATA_INS_MAN is NOT NULL" + " ) B where "
				+ " A.ID_PROVVEDIMENTO_SIGE=B.ID_PROVVEDIMENTO_SIGE(+) and " + " A.chiave_anno is not NULL ";

		// Ticket#20230202011 - Si sposta il controllo su ANNO_FOGLIO_COMPLEMENTARE come per il dettaglio
		if (filtroModel.getAnnoIniziale() != null) {
			if (filtroModel.getAnnoFinale() == null)
				sql += " and B.ANNO_FOGLIO_COMPLEMENTARE=" + filtroModel.getAnnoIniziale();
			if (filtroModel.getAnnoFinale() != null) {
				sql += " and B.ANNO_FOGLIO_COMPLEMENTARE between " + filtroModel.getAnnoIniziale() + " and "
						+ filtroModel.getAnnoFinale();
			}
		}
		/*
		 * if (filtroModel.getAnnoIniziale() != null) { if (filtroModel.getAnnoFinale() == null) sql +=
		 * " and A.chiave_anno=" + filtroModel.getAnnoIniziale(); if (filtroModel.getAnnoFinale() != null) sql
		 * += " and a.chiave_anno between " + filtroModel.getAnnoIniziale() +" and " +
		 * filtroModel.getAnnoFinale(); }
		 */
		// Ticket#20230202011 - FINE
		if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and b.data_emissione = TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			} else {
				sql += " and b.data_emissione between TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' ) and TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneFinale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			}
		}
		sql += " group by a.chiave_anno ";
		sql += " order by a.chiave_anno ";
		return sql;
	}

	private String getQueryConteggioProvvedimentiPriviFC(RicercaFogliCompModel filtroModel) {

		String sql = "select a.chiave_anno as anno, count (b.chiave_anno) as conteggio, 'Provvedimenti Privi di Fogli Complementari' as descrizione from provvedimento_sige A, "
				+ "(" + "SELECT DISTINCT(D.ID_FASCICOLO_SIGE) prg , "
				+ "A.ID_PROVVEDIMENTO_SIGE, A.CHIAVE_ANNO "
				+ "FROM PROVVEDIMENTO_SIGE A, EVENTO C, FASCICOLO_SIGE D, CG_REF_CODES E "
				+ "WHERE A.FAS_ID_FASCICOLO_SIGE = D.ID_FASCICOLO_SIGE AND " + "D.COD_UFFICIO_INSERIMENTO='"
				+ filtroModel.getCodUfficioInserimento() + "' and "
				+ "c.cod_motivo = e.RV_LOW_VALUE AND e.RV_DOMAIN = 'MOTIVO_PROVVEDIMENTO' "
				+ "AND A.ID_EVENTO_GENERATO = C.ID_EVENTO AND "
				+ "(C.FLAG_DOCUMENTO_REGISTRATO IS NULL OR C.FLAG_DOCUMENTO_REGISTRATO <> 'A')  AND "
				+ "C.COD_TIPO_EVENTO = '01'  AND " + "A.COD_TIPO_PROVVEDIMENTO_SIGE IN ('02','03') AND "
				+ "A.COD_TIPO_PROVVEDIMENTO IN ('02','03') AND "
				+ "C.ID_EVENTO NOT IN ( SELECT B.EVE_ID_EVENTO FROM DOCUMENTO_ALLEGATO B, "
				+ "EVENTO G WHERE G.ID_EVENTO = B.EVE_ID_EVENTO AND B.COD_TIPO_DOCUMENTO  = '06') "
				+ " ) B where " + " A.ID_PROVVEDIMENTO_SIGE=B.ID_PROVVEDIMENTO_SIGE(+) and "
				+ " A.chiave_anno is not NULL ";

		if (filtroModel.getAnnoIniziale() != null) {
			if (filtroModel.getAnnoFinale() == null)
				sql += " and A.CHIAVE_ANNO=" + filtroModel.getAnnoIniziale();
			if (filtroModel.getAnnoFinale() != null)
				sql += " and A.CHIAVE_ANNO between " + filtroModel.getAnnoIniziale() + " and "
						+ filtroModel.getAnnoFinale();
		}

		// Ticket#20230202011 - Si aggiunge la condizione sul periodo
		if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and a.data_emissione = TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			} else {
				sql += " and a.data_emissione between TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' ) and TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneFinale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			}
		}
		// Ticket#20230202011 - FINE

		sql += " group by a.chiave_anno ";
		sql += " order by a.chiave_anno ";
		return sql;
	}

	private String getQueryConteggioProvvedimentiConFC(RicercaFogliCompModel filtroModel) {

		String sql = "select a.chiave_anno as anno, count (b.chiave_anno) as conteggio, 'Provvedimenti con Fogli Complementari' as descrizione from provvedimento_sige A, "
				+ "( "
				+ "SELECT A.ID_PROVVEDIMENTO_SIGE, A.CHIAVE_ANNO, b.data_emissione as data_emissione, B.ANNO_FOGLIO_COMPLEMENTARE "
				+ "FROM PROVVEDIMENTO_SIGE A, DOCUMENTO_ALLEGATO B, FASCICOLO_SIGE D WHERE "
				+ "A.FAS_ID_FASCICOLO_SIGE = D.ID_FASCICOLO_SIGE AND  " + "D.COD_UFFICIO_INSERIMENTO='"
				+ filtroModel.getCodUfficioInserimento() + "' and "
				+ "A.ID_EVENTO_GENERATO = B.EVE_ID_EVENTO AND "
				+ "A.COD_TIPO_PROVVEDIMENTO_SIGE IN ('02','03') AND "
				+ "A.COD_TIPO_PROVVEDIMENTO IN ('02','03') AND  " + "B.COD_TIPO_DOCUMENTO = '06' AND "
				+ "B.DATA_ANNULLAMENTO IS NULL  " +

				" ) B where " + " A.ID_PROVVEDIMENTO_SIGE=B.ID_PROVVEDIMENTO_SIGE(+) and "
				+ " A.chiave_anno is not NULL ";

		if (filtroModel.getAnnoIniziale() != null) {
			if (filtroModel.getAnnoFinale() == null)
				// Modifica del 30/11/2016 MEV_15_S4
				// Commento la riga seguente e sostituisco con la successiva
				// per incongruenza con la query "getQueryFcTrasmessi"
				// sql += " and A.chiave_anno=" + filtroModel.getAnnoIniziale();
				sql += " and B.ANNO_FOGLIO_COMPLEMENTARE=" + filtroModel.getAnnoIniziale();
			if (filtroModel.getAnnoFinale() != null) {
				// Modifica del 30/11/2016 MEV_15_S4
				// Commento la riga seguente e sostituisco con la successiva
				// per incongruenza con la query "getQueryFcTrasmessi"
				// sql += " and a.chiave_anno between " + filtroModel.getAnnoIniziale() +" and " +
				// filtroModel.getAnnoFinale();
				sql += " and B.ANNO_FOGLIO_COMPLEMENTARE between " + filtroModel.getAnnoIniziale() + " and "
						+ filtroModel.getAnnoFinale();
			}
		}

		if (filtroModel.getDataEmissioneIniziale() != null) {
			if (filtroModel.getDataEmissioneFinale() == null) {
				sql += " and b.data_emissione = TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			} else {
				sql += " and b.data_emissione between TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneIniziale(), "yyyyMMdd")
						+ ",'YYYYMMDD' ) and TO_DATE("
						+ DateUtils.getDateToString(filtroModel.getDataEmissioneFinale(), "yyyyMMdd")
						+ ",'YYYYMMDD' )";
			}
		}
		sql += " group by a.chiave_anno " + " order by a.chiave_anno";

		return sql;
	}

	public StatisticheFogliComplementariModel getModel() throws DAOException {

		StatisticheFogliComplementariModel model = new StatisticheFogliComplementariModel();
		model.setIdFascicolo(super.getBigDecimal("PRG"));
		model.setDescrFascicolo(super.getString("num_fasc_SIGE"));
		model.setDataProvvedimento(super.getDate("data_emissione_provv"));
		model.setDataFoglioComplementare(super.getString("DATA_EMISSIONE_FOGLIO"));
		model.setDescrEsito(super.getString("esito"));
		model.setDescrProvvedimento(super.getString("motivo"));

		// Ticket#20230202011 - Aggiunti campi per visualizzazione X TEST
		model.setAnnoNumeroProvvedimento(super.getString("annoNumeroProvvedimento"));
		model.setAnnoNumeroFoglioComplementare(super.getString("annoNumeroFoglioComplementare"));
		// Ticket#20230202011 - FINE
		return model;
	}

	public RiepilogoStatisticheFogliComplementari getRiepilogoModel() throws DAOException {

		RiepilogoStatisticheFogliComplementari model = new RiepilogoStatisticheFogliComplementari();
		model.setAnno(super.getBigDecimal("anno"));
		model.setConteggio(super.getBigDecimal("conteggio"));
		model.setDescrizione(super.getString("descrizione"));
		return model;
	}

	public Vector<StatisticheFogliComplementariModel> getLista() throws DAOException {

		Vector<StatisticheFogliComplementariModel> lElencoFC = new Vector<>();
		start();
		while (next()) {
			lElencoFC.add(getModel());
		}
		stop();
		return lElencoFC;
	}

	public Vector<RiepilogoStatisticheFogliComplementari> getListaRiepilogo() throws DAOException {

		Vector<RiepilogoStatisticheFogliComplementari> lista = new Vector<>();
		start();
		while (next()) {
			lista.add(this.getRiepilogoModel());
		}
		stop();
		return lista;
	}

}