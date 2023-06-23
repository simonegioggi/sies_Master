package siap.siep.statis.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;


import f3b.dao.DAOException;
import f3b.dao.SqlDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;
import siap.siep.statis.model.StatisticheMSModel;

/**
 * MEV_39
 *
 * <p>
 * Title: StatisticheMSSqlDAO
 * </p>
 * <p>
 * Description: Classe SqlDAO che rappresenta la vista VW_MS_APPL_SENT_COND
 * </p>
 *
 * @version 1.0
 */
public class StatisticheMSSqlDAO extends SqlDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatisticheMSSqlDAO(Connection con) {
		super(con);
	}

	public void ricercaRiepilogoIscrizioniProcedimentiMisura(String dataIniziale, String dataFinale,
			String numeroTrimestreSemestre, String ufficioConnesso, String range) throws DAOException {

		String s = new String("");

		s += "select * from (";
		// 1) VW_MS_APPL_SENT_COND;
		s += creaQueryRRIPM("VW_MS_APPL_SENT_COND", ufficioConnesso, numeroTrimestreSemestre, dataIniziale,
				dataFinale);
		s += " UNION ";
		// 2) VW_MS_APPL_SENT_ASS;
		s += creaQueryRRIPM("VW_MS_APPL_SENT_ASS", ufficioConnesso, numeroTrimestreSemestre, dataIniziale,
				dataFinale);
		s += " UNION ";
		// 3) VW_MS_APPL_MAG_SORV;
		s += creaQueryRRIPM("VW_MS_APPL_MAG_SORV", ufficioConnesso, numeroTrimestreSemestre, dataIniziale,
				dataFinale);
		s += " UNION ";
		// 4) VW_MS_APPL_MAG_SORV_RICH_SIEP;
		s += creaQueryRRIPM("VW_MS_APPL_MAG_SORV_RICH_SIEP", ufficioConnesso, numeroTrimestreSemestre,
				dataIniziale, dataFinale);
		s += " UNION ";
		// 5) VW_MS_APPL_GIU_COGNIZIONE;
		s += creaQueryRRIPM("VW_MS_APPL_GIU_COGNIZIONE", ufficioConnesso, numeroTrimestreSemestre,
				dataIniziale, dataFinale);
		s += " UNION ";
		// 6) VW_MS_APPL_TRIB_SORV_IMPUGN
		s += creaQueryRRIPM("VW_MS_APPL_TRIB_SORV_IMPUGN", ufficioConnesso, numeroTrimestreSemestre,
				dataIniziale, dataFinale);
		s += " UNION ";
		// 7) VW_MS_PROC_ISCR_PER_ERRORE
		s += creaQueryRRIPM("VW_MS_PROC_ISCR_PER_ERRORE", ufficioConnesso, numeroTrimestreSemestre,
				dataIniziale, dataFinale);
		s += " UNION ";
		// 8) VW_MS_COMPET_TERRIT_C1
		s += creaQueryRRIPM("VW_MS_COMPET_TERRIT_C1", ufficioConnesso, numeroTrimestreSemestre, dataIniziale,
				dataFinale);
		s += " UNION ";
		// 9) VW_MS_COMPET_TERRIT_C2
		s += creaQueryRRIPM("VW_MS_COMPET_TERRIT_C2", ufficioConnesso, numeroTrimestreSemestre, dataIniziale,
				dataFinale);
		s += " UNION ";
		// 10) VW_MS_CUMULO
		s += creaQueryRRIPM("VW_MS_CUMULO", ufficioConnesso, numeroTrimestreSemestre, dataIniziale,
				dataFinale);
		s += ") PIVOT(COUNT(FASC_SIEP) " + "FOR ANNO_ISC IN (" + range + ")) order by 1 desc";

		setStatement(s);
	}

	private String creaQueryRRIPM(String nomeVista, String ufficioConnesso, String numeroTrimestreSemestre,
			String dataIniziale, String dataFinale) {

		String s = new String();
		s += "SELECT * " + "FROM (SELECT DISTINCT V.Nome_Stat, V.ANNO_ISC, V.FASC_SIEP " + "FROM " + nomeVista
				+ " V " + "WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' ";
		if (!"".equals(numeroTrimestreSemestre))
			s += "AND V.trimestre IN (" + numeroTrimestreSemestre + ")) ";
		else if (!"".equals(dataIniziale) && !"".equals(dataFinale))
			s += "and v.Data_Iscr between TO_DATE('" + dataIniziale + "', 'dd/MM/yyyy') AND TO_DATE('"
					+ dataFinale + "', 'dd/MM/yyyy'))";
		return s;
	}

	//
	// METODO GETMODEL()
	//
	public GenericModel getModelIscrizioni(String range) throws DAOException {

		StatisticheMSModel smsm = new StatisticheMSModel();
		smsm.setTipoMS(getString("Nome_Stat"));
		String[] s = range.split(",");
		Vector<Integer> anni = new Vector<>();
		Vector<Integer> iscrittiParziali = new Vector<>();
		for (int i = 0; i < s.length; i++) {
			anni.add(Integer.parseInt(s[i]));
			iscrittiParziali.add(getInteger(s[i]));
		}
		smsm.setAnni(anni);
		smsm.setIscrittiParziali(iscrittiParziali);

		return smsm;
	}

	public GenericModel getModelDettaglio() throws DAOException {

		StatisticheMSModel smsm = new StatisticheMSModel();
		smsm.setTipoMS(getString("Nome_Stat"));
		String[] s = getString("Prog_Fasc").split("#");
		Vector<BigDecimal> pf = new Vector<>();
		for (int i = 0; i < s.length; i++) {
			pf.add(new BigDecimal(s[i]));
		}
		smsm.setProgFasc(pf);
		smsm.setTotFasc(getBigDecimal("Tot_Fasc"));
		smsm.setAnno(getBigDecimal("Anno"));

		return smsm;
	}

	// Ticket#20210531014] - eliminata la LISTAGG per limite dei 4000 caratteri (varchar2)
	// i dati verranno aggregati dal controller
	public GenericModel getModelDettaglio2() throws DAOException {
		
		StatisticheMSModel smsm = new StatisticheMSModel();
		smsm.setTipoMS(getString("Nome_Stat"));
		// String[] s = getString("Prog_Fasc").split("#");
		String Prog_Fasc = getString("Prog_Fasc");
		Vector<BigDecimal> pf = new Vector<>();
//		for (int i = 0; i < s.length; i++) {
//			pf.add(new BigDecimal(s[i]));
//		}
		pf.add(new BigDecimal(Prog_Fasc));
		
		smsm.setProgFasc(pf);
		smsm.setTotFasc(getBigDecimal("Tot_Fasc"));
		smsm.setAnno(getBigDecimal("Anno"));

		return smsm;
	}
	// Ticket#20210531014] - FINE
	
	
	public void ricercaRiepilogoIscrizioniTipologiaMisura(String dataIniziale, String dataFinale,
			String numeroTrimestreSemestre, String ufficioConnesso, String range) throws DAOException {

		String s = new String("");

		s += "SELECT * " + "FROM (SELECT DISTINCT V.DESC_MS Nome_Stat, V.ANNO_ISC, V.FASC_SIEP "
				+ "FROM VW_MS_TIPO_MISURA V " + "WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' ";
		if (!"".equals(numeroTrimestreSemestre))
			s += "AND V.trimestre IN (" + numeroTrimestreSemestre + ")) ";
		else if (!"".equals(dataIniziale) && !"".equals(dataFinale))
			s += "and v.Data_Iscr between TO_DATE('" + dataIniziale + "', 'dd/MM/yyyy') AND TO_DATE('"
					+ dataFinale + "', 'dd/MM/yyyy'))";
		s += "PIVOT(COUNT(FASC_SIEP) " + "FOR ANNO_ISC IN (" + range + ")) ORDER BY 1";

		setStatement(s);
	}

	public void ricercaDettaglioProcedimentiMSTipologiaIscrizione(String dataIniziale, String dataFinale,
			String numeroTrimestreSemestre, String ufficioConnesso, String range) throws DAOException {

		String s = new String("");
		s += "SELECT * FROM (";

		String[] array = range.split(",");
		String dataFinaleTemp = "";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger
				.debug("ricercaDettaglioProcedimentiMSTipologiaIscrizione - dataIniziale = " + dataIniziale);
		siesLogger.debug("ricercaDettaglioProcedimentiMSTipologiaIscrizione - dataFinale = " + dataFinale);

		for (int i = 0; i < array.length; i++) {
			if (array.length > 1) {
				if (i != 0) {
					s += " UNION ";
				}
				if (i != array.length - 1)
					dataFinaleTemp = "31/12/" + array[i];
				else
					dataFinaleTemp = dataFinale;
			}
			// [EC]: modifiche per mev39 post collaudo 11.3
			else if (array.length == 1)
				dataFinaleTemp = dataFinale;
			// 1) VW_MS_APPL_SENT_COND;
			s += creaQueryRDPMSTI("VW_MS_APPL_SENT_COND", ufficioConnesso, numeroTrimestreSemestre,
					dataIniziale, dataFinaleTemp, array[i]);
			s += " UNION ";
			// 2) VW_MS_APPL_SENT_ASS;
			s += creaQueryRDPMSTI("VW_MS_APPL_SENT_ASS", ufficioConnesso, numeroTrimestreSemestre,
					dataIniziale, dataFinaleTemp, array[i]);
			s += " UNION ";
			// 3) VW_MS_APPL_MAG_SORV;
			s += creaQueryRDPMSTI("VW_MS_APPL_MAG_SORV", ufficioConnesso, numeroTrimestreSemestre,
					dataIniziale, dataFinaleTemp, array[i]);
			s += " UNION ";
			// 4) VW_MS_APPL_MAG_SORV_RICH_SIEP;
			s += creaQueryRDPMSTI("VW_MS_APPL_MAG_SORV_RICH_SIEP", ufficioConnesso, numeroTrimestreSemestre,
					dataIniziale, dataFinaleTemp, array[i]);
			s += " UNION ";
			// 5) VW_MS_APPL_GIU_COGNIZIONE;
			s += creaQueryRDPMSTI("VW_MS_APPL_GIU_COGNIZIONE", ufficioConnesso, numeroTrimestreSemestre,
					dataIniziale, dataFinaleTemp, array[i]);
			s += " UNION ";
			// 6) VW_MS_APPL_TRIB_SORV_IMPUGN
			s += creaQueryRDPMSTI("VW_MS_APPL_TRIB_SORV_IMPUGN", ufficioConnesso, numeroTrimestreSemestre,
					dataIniziale, dataFinaleTemp, array[i]);
			s += " UNION ";
			// 7) VW_MS_PROC_ISCR_PER_ERRORE
			s += creaQueryRDPMSTI("VW_MS_PROC_ISCR_PER_ERRORE", ufficioConnesso, numeroTrimestreSemestre,
					dataIniziale, dataFinaleTemp, array[i]);
			s += " UNION ";
			// 8) VW_MS_COMPET_TERRIT_C1
			s += creaQueryRDPMSTI("VW_MS_COMPET_TERRIT_C1", ufficioConnesso, numeroTrimestreSemestre,
					dataIniziale, dataFinaleTemp, array[i]);
			s += " UNION ";
			// 9) VW_MS_COMPET_TERRIT_C2
			s += creaQueryRDPMSTI("VW_MS_COMPET_TERRIT_C2", ufficioConnesso, numeroTrimestreSemestre,
					dataIniziale, dataFinaleTemp, array[i]);
			s += " UNION ";
			// 10) VW_MS_CUMULO
			s += creaQueryRDPMSTI("VW_MS_CUMULO", ufficioConnesso, numeroTrimestreSemestre, dataIniziale,
					dataFinaleTemp, array[i]);
		}

		//s += ") order by 3, 4";
		s += ") order by 3, 4, 1"; // Ticket#20210531014 aggiunto order by per Numero Fascicolo
		setStatement(s);
	}

	private String creaQueryRDPMSTI(String nomeVista, String ufficioConnesso, String numeroTrimestreSemestre,
			String dataIniziale, String dataFinale, String anno) {

		siesLogger.debug("SQLDAO.creaQueryRDPMSTI...");
		
		// Ticket#20210531014] - eliminata la LISTAGG per limite dei 4000 caratteri (varchar2)
		// i dati verranno aggregati dal controller
		String s = new String();
//		s += "SELECT LISTAGG(V.NUMERO_FASC, '#') WITHIN GROUP(ORDER BY ANNO_ISC) Prog_Fasc, "
//				+ "COUNT(DISTINCT V.NUMERO_FASC) Tot_Fasc, "
//				+ "v.Nome_Stat || ' - ' || v.DESC_MS Nome_Stat, '" + anno + "' Anno FROM " + nomeVista + " V "
//				+ "WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' and V.ANNO_ISC = " + anno + " ";
//		if (!"".equals(numeroTrimestreSemestre))
//			s += "AND V.trimestre IN (" + numeroTrimestreSemestre + ") ";
//		else if (!"".equals(dataIniziale) && !"".equals(dataFinale))
//			s += "and v.Data_Iscr between TO_DATE('" + dataIniziale + "', 'dd/MM/yyyy') AND TO_DATE('"
//					+ dataFinale + "', 'dd/MM/yyyy') ";
//		s += " GROUP BY v.Nome_Stat || ' - ' || v.DESC_MS";
 
		s += "SELECT NUMERO_FASC Prog_Fasc, "
				+ "1 Tot_Fasc, "
				+ "v.Nome_Stat || ' - ' || v.DESC_MS Nome_Stat, '" + anno + "' Anno FROM " + nomeVista + " V "
				+ "WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' and V.ANNO_ISC = " + anno + " ";
		if (!"".equals(numeroTrimestreSemestre))
			s += "AND V.trimestre IN (" + numeroTrimestreSemestre + ") ";
		else if (!"".equals(dataIniziale) && !"".equals(dataFinale))
			s += "and v.Data_Iscr between TO_DATE('" + dataIniziale + "', 'dd/MM/yyyy') AND TO_DATE('"
					+ dataFinale + "', 'dd/MM/yyyy') ";
		//s += " GROUP BY v.Nome_Stat || ' - ' || v.DESC_MS";		

		// Ticket#20210531014] - FINE
		return s;
	}

	public void ricercaRiepilogoProcedimentiPendentiPeriodo(String dataIniziale, String dataFinale,
			String ufficioConnesso, String range, String codMagistrato) throws DAOException {

		siesLogger.debug("ricercaRiepilogoProcedimentiPendentiPeriodo...");
		
		String s = new String("");
		String[] anni = range.split(",");
		Date di = DateUtils.getDate(dataIniziale, "dd/MM/yyyy");
		String dataInizialePiuUno = DateUtils.getDateToString(DateUtils.getDayAfter(di), "dd/MM/yyyy");

		String from1 = "", from2 = "", from3 = "", from4 = "";
		String magCondition = "";
		if ("".equals(codMagistrato)) {
			from1 = "VW_MS_PROC_INIZIO_FINE_PERIODO";
			from2 = "VW_MS_PROCEDIMENTI";
			from3 = "VW_MS_PROC_ESAURITI";
			from4 = "VW_MS_PROC_RIAPERTI";
		} else {
			from1 = "VW_MS_PROC_PERIODO_MAG";
			from2 = "VW_MS_PROCEDIMENTI_MAG";
			from3 = "VW_MS_PROC_ESAURITI_MAG";
			from4 = "VW_MS_PROC_RIAPERTI_MAG";
			if (!"0".equals(codMagistrato))
				magCondition = "AND V.COD_MAGISTRATO = '" + codMagistrato + "' ";
			else
				magCondition = "AND V.COD_MAGISTRATO IS NOT NULL AND V.COD_MAGISTRATO != '-'";
		}

		s += "SELECT * FROM (";
		// PROCEDIMENTI PENDENTI INIZIO PERIODO
		String dataFinaleTemp = "";

		s += "SELECT 'Procedimenti pendenti inizio periodo' Nome_Stat, " + "0 ordinamento, ";
		if (anni.length == 1) {
			dataFinaleTemp = "31/12/" + anni[0];
			// [EC]: modifiche per mev39 post collaudo 11.3
			s += "count(distinct v.FASC_SIEP) \"" + anni[0] + "\" ";
		} else {
			for (int i = 0; i < anni.length; i++) {
				if (i == 0) {
					dataFinaleTemp = "31/12/" + anni[0];
					// [EC]: modifiche per mev39 post collaudo 11.3
					s += "count(distinct v.FASC_SIEP) \"" + anni[i] + "\", ";
				} else {
					dataFinaleTemp = "31/12/" + anni[0];
					if (i != anni.length - 1)
						s += "-1 \"" + anni[i] + "\", ";
					else
						s += "-1 \"" + anni[i] + "\" ";
				}
			}
		}
		s += "FROM " + from1 + " V " + "WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' ";
		if (!"".equals(magCondition))
			s += magCondition;
		s += "AND (v.Data_Iscr < TO_DATE('" + dataIniziale + "', 'dd/MM/yyyy')) "
				+ " and (V.Data_Arch is null  OR (V.COD_STATO_FASCICOLO <> '01' and  V.Data_Arch  is not null)"
				+ " or (V.cod_stato_fascicolo='01' and to_char(V.Data_Arch,'yyyy')>to_char('" + anni[0]
				+ "')))) ";

		s += "UNION ";
		// SOPRAVVENUTI NEL PERIODO
		s += "SELECT * FROM (SELECT 'Procedimenti sopravvenuti nel periodo' Nome_Stat, " + "1 ordinamento, "
				+ "V.ANNO_ISC, " + "V.FASC_SIEP " + "from " + from2 + " V " + "WHERE V.Cod_Uff_Ins = '"
				+ ufficioConnesso + "' "
				+ "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between " + "TO_DATE('"
				+ dataIniziale + "', 'dd/MM/yyyy') and " + "TO_DATE('" + dataFinale + "', 'dd/MM/yyyy') ";
		if (!"".equals(magCondition))
			s += magCondition;
		s += "UNION ";
		// ESAURITI NEL PERIODO
		// INVOCARE UN METODO DI GESTIONE ESAURITI
		// s += "SELECT 'Procedimenti esauriti nel periodo' Nome_Stat, " + "2 ordinamento, " + "V.ANNO_ISC, "
		// + "V.FASC_SIEP " + "FROM " + from3 + " V " + "WHERE V.Cod_Uff_Ins = '" + ufficioConnesso
		// + "' " + "and v.Data_Arch is not null " + "and v.mot_archiviazione is not null "
		// + "AND TO_DATE(TO_CHAR(v.Data_Arch, 'dd/MM/yyyy'), 'dd/MM/yyyy') between " + "TO_DATE('"
		// + dataIniziale + "', 'dd/MM/yyyy') and " + "TO_DATE('" + dataFinale + "', 'dd/MM/yyyy') "
		// + "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between " + "TO_DATE('"
		// + dataIniziale + "', 'dd/MM/yyyy') and " + "TO_DATE('" + dataFinale + "', 'dd/MM/yyyy') ";
		s += gestioneEsauriti(from3, ufficioConnesso, anni, dataIniziale, dataFinale, magCondition);
		// if (!"".equals(magCondition))
		// s += magCondition;
		// RIAPERTI NEL PERIODO
		s += "UNION ";
		// INVOCARE UN METODO DI GESTIONE RIAPERTI
		// s += "SELECT 'Procedimenti riaperti nel periodo' Nome_Stat, " + "3 ordinamento, "
		// + "NVL(V.ANNO_ISC, 0) ANNO_ISC, " + "NVL(V.FASC_SIEP, 0) FASC_SIEP " + "FROM " + from4 + " V "
		// + "WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' " + "AND v.Data_Iscr between TO_DATE('"
		// + dataInizialePiuUno + "', 'dd/MM/yyyy') and " + "TO_DATE('" + dataFinale
		// + "', 'dd/MM/yyyy') ";
		s += gestioneRiaperti(from4, ufficioConnesso, anni, dataIniziale, dataFinale, magCondition);
		// if (!"".equals(magCondition))
		// s += magCondition;
		s += "UNION ";
		// PENDENTI FINE PERIODO
		// INVOCARE UN METODO DI GESTIONE PENDENTI FINE
		// s += "SELECT 'Procedimenti pendenti fine periodo' Nome_Stat, " + "4 ordinamento, " + "V.ANNO_ISC, "
		// + "V.FASC_SIEP " + "FROM " + from1 + " V " + "WHERE V.Cod_Uff_Ins = '" + ufficioConnesso
		// + "' " + "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between "
		// + "TO_DATE('" + dataInizialePiuUno + "', 'dd/MM/yyyy') and " + "TO_DATE('" + dataFinale
		// + "', 'dd/MM/yyyy') ";
		s += gestionePendentiFine(from1, ufficioConnesso, anni, dataIniziale, dataFinale, magCondition);
		// if (!"".equals(magCondition))
		// s += magCondition;
		// FINE
		s += ") PIVOT(COUNT(FASC_SIEP) " + "FOR ANNO_ISC IN(" + range + ")) order by 2";

		siesLogger.info("Giorno dopo data iniziale: " + dataInizialePiuUno);
		siesLogger.info("Data finale temporanea: " + dataFinaleTemp);

		setStatement(s);
	}

	private String gestionePendentiFine(String nomeVista, String ufficioConnesso, String[] rangeAnni,
			String dataIniziale, String dataFinale, String magCondition) {

		String dataFinaleTemp = "";
		String dataInizialeTemp = "";
		String sql = "";

		if (rangeAnni.length == 1) {
			dataFinaleTemp = dataFinale;
			dataInizialeTemp = "01/01/" + rangeAnni[0];
			sql += " SELECT 'Procedimenti pendenti fine periodo' Nome_Stat, 4 ordinamento," + "'"
					+ rangeAnni[0] + "'" + " AS ANNO_ISC, " + " V.FASC_SIEP FROM  " + nomeVista + " V "
					+ " WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "'" + " AND (v.Data_Iscr <= TO_DATE('"
					+ dataFinaleTemp + "', 'dd/MM/yyyy'))";
			if (!"".equals(magCondition))
				sql += magCondition;
			sql += " and (V.Data_Arch is null  OR (V.COD_STATO_FASCICOLO <> '01' and  V.Data_Arch  is not null)"
					+ " or (V.cod_stato_fascicolo='01' and to_char(V.Data_Arch,'yyyy')>to_char('"
					+ rangeAnni[0] + "'))) ";
		} else {
			sql += "(";
			for (int i = 0; i < rangeAnni.length; i++) {
				// if (i != rangeAnni.length - 1){
				// dataInizialeTemp = "01/01/" + rangeAnni[i];
				// dataFinaleTemp = "31/12/" + rangeAnni[i];
				// }
				// else{
				// dataInizialeTemp = "01/01/" + rangeAnni[i];;
				// dataFinaleTemp = dataFinale;
				// }
				if (i == 0) {
					dataInizialeTemp = dataIniziale;
					dataFinaleTemp = "31/12/" + rangeAnni[i];
				} else if (i != rangeAnni.length - 1) {
					dataInizialeTemp = "01/01/" + rangeAnni[i];
					dataFinaleTemp = "31/12/" + rangeAnni[i];
				} else {
					dataInizialeTemp = "01/01/" + rangeAnni[i];
					dataFinaleTemp = dataFinale;
				}
				sql += " SELECT 'Procedimenti pendenti fine periodo' Nome_Stat, 4 ordinamento," + "'"
						+ rangeAnni[i] + "'" + " AS ANNO_ISC, " + " V.FASC_SIEP FROM  " + nomeVista + " V "
						+ " WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "'" + " AND (v.Data_Iscr <= TO_DATE('"
						+ dataFinaleTemp + "', 'dd/MM/yyyy'))";
				if (!"".equals(magCondition))
					sql += magCondition;
				sql += " and (V.Data_Arch is null  OR (V.COD_STATO_FASCICOLO <> '01' and  V.Data_Arch  is not null)"
						+ " or (V.cod_stato_fascicolo='01' and to_char(V.Data_Arch,'yyyy')>to_char('"
						+ rangeAnni[i] + "'))) ";

				if (i != rangeAnni.length - 1) {
					sql += " union ";
				}
			}
			sql += ")";
		}
		siesLogger.info("Data Iniziale temporanea: " + dataInizialeTemp);

		// valore di ritorno
		return sql;
	}

	private String gestioneEsauriti(String nomeVista, String ufficioConnesso, String[] rangeAnni,
			String dataIniziale, String dataFinale, String magCondition) {

		String dataFinaleTemp = "";
		String dataInizialeTemp = "";
		String sql = "";
		if (rangeAnni.length == 1) {
			sql += "SELECT 'Procedimenti esauriti nel periodo' Nome_Stat,    2 ordinamento," + "'"
					+ rangeAnni[0] + "'" + " AS ANNO_ISC, " + "  V.FASC_SIEP   FROM " + nomeVista + " V"
					+ " WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' " + " and v.Data_Arch is not null"
					+ " and v.mot_archiviazione is not null AND TO_DATE(TO_CHAR(v.Data_Arch, 'dd/MM/yyyy'), 'dd/MM/yyyy') between"
					+ "  TO_DATE('" + dataIniziale + "', 'dd/MM/yyyy') and   TO_DATE('" + dataFinale
					+ "', 'dd/MM/yyyy') ";
			if (!"".equals(magCondition))
				sql += magCondition;
		} else {
			sql += "(";
			for (int i = 0; i < rangeAnni.length; i++) {
				if (i == 0) {
					dataInizialeTemp = dataIniziale;
					dataFinaleTemp = "31/12/" + rangeAnni[i];
				} else if (i != rangeAnni.length - 1) {
					dataInizialeTemp = "01/01/" + rangeAnni[i];
					dataFinaleTemp = "31/12/" + rangeAnni[i];
				} else {
					dataInizialeTemp = "01/01/" + rangeAnni[i];
					dataFinaleTemp = dataFinale;
				}
				sql += "SELECT 'Procedimenti esauriti nel periodo' Nome_Stat,    2 ordinamento," + "'"
						+ rangeAnni[i] + "'" + " AS ANNO_ISC, " + "  V.FASC_SIEP   FROM " + nomeVista + " V"
						+ " WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' and v.Data_Arch is not null"
						+ " and v.mot_archiviazione is not null AND TO_DATE(TO_CHAR(v.Data_Arch, 'dd/MM/yyyy'), 'dd/MM/yyyy') between"
						+ "  TO_DATE('" + dataInizialeTemp + "', 'dd/MM/yyyy') and   TO_DATE('"
						+ dataFinaleTemp + "', 'dd/MM/yyyy') ";
				if (!"".equals(magCondition))
					sql += magCondition;

				if (i != rangeAnni.length - 1) {
					sql += " union ";
				}
			}
			sql += ")";
		}
		return sql;
	}

	private String gestioneRiaperti(String nomeVista, String ufficioConnesso, String[] rangeAnni,
			String dataIniziale, String dataFinale, String magCondition) {

		String dataFinaleTemp = "";
		String dataInizialeTemp = "";
		String sql = "";
		if (rangeAnni.length == 1) {
			sql = " SELECT 'Procedimenti riaperti nel periodo' Nome_Stat,  3 ordinamento," + "'"
					+ rangeAnni[0] + "'" + " AS ANNO_ISC, " + " NVL(V.FASC_SIEP, 0) FASC_SIEP" + " FROM   "
					+ nomeVista + " V " + " WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "'"
					+ "  AND v.Data_Arch between TO_DATE('" + dataIniziale + "', 'dd/MM/yyyy') and  TO_DATE('"
					+ dataFinale + "', 'dd/MM/yyyy')";
			if (!"".equals(magCondition))
				sql += magCondition;
		} else {
			sql = "(";
			for (int i = 0; i < rangeAnni.length; i++) {
				// if (i != rangeAnni.length - 1){
				// dataInizialeTemp = "01/01/" + rangeAnni[i];
				// dataFinaleTemp = "31/12/" + rangeAnni[i];
				// }
				// else{
				// dataInizialeTemp = dataIniziale;
				// dataFinaleTemp = dataFinale;
				// }
				if (i == 0) {
					dataInizialeTemp = dataIniziale;
					dataFinaleTemp = "31/12/" + rangeAnni[i];
				} else if (i != rangeAnni.length - 1) {
					dataInizialeTemp = "01/01/" + rangeAnni[i];
					dataFinaleTemp = "31/12/" + rangeAnni[i];
				} else {
					dataInizialeTemp = "01/01/" + rangeAnni[i];
					dataFinaleTemp = dataFinale;
				}
				sql += " SELECT 'Procedimenti riaperti nel periodo' Nome_Stat,  3 ordinamento," + "'"
						+ rangeAnni[i] + "'" + " AS ANNO_ISC, " + " NVL(V.FASC_SIEP, 0) FASC_SIEP" + " FROM  "
						+ nomeVista + " V " + "  WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "'"
						+ "  AND v.Data_Arch between TO_DATE('" + dataInizialeTemp
						+ "', 'dd/MM/yyyy') and  TO_DATE('" + dataFinaleTemp + "', 'dd/MM/yyyy')";
				if (!"".equals(magCondition))
					sql += magCondition;

				if (i != rangeAnni.length - 1) {
					sql += " union ";
				}
			}
			sql += ")";
		}
		return sql;
	}

	public void ricercaRiepilogoPPPTipologiaMisura(String dataIniziale, String dataFinale,
			String ufficioConnesso, String codMagistrato) throws DAOException {

		String s = new String("");

		Date di = DateUtils.getDate(dataIniziale, "dd/MM/yyyy");
		String dataInizialePiuUno = DateUtils.getDateToString(DateUtils.getDayAfter(di), "dd/MM/yyyy");

		String from = "";
		String magCondition = "";
		if ("".equals(codMagistrato)) {
			from = "VW_MS_TIPO_MISURA";
		} else {
			from = "VW_MS_TIPO_MISURA_MAG";
			if (!"0".equals(codMagistrato))
				magCondition = "AND V.COD_MAGISTRATO = '" + codMagistrato + "' ";
			else
				magCondition = "AND V.COD_MAGISTRATO IS NOT NULL AND V.COD_MAGISTRATO != '-'";
		}
		s += "SELECT * FROM (SELECT DISTINCT V.DESC_MS Nome_Stat, V.FASC_SIEP, 'Pendenti Inizio' tipo_statistica "
				+ "FROM " + from + " V WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' "
				// [EC]: modifiche per mev39 post collaudo 11.3
				// + "and v.Data_Iscr is null OR v.cod_stato_fascicolo <> '01' "
				+ "and (v.Data_Arch is null OR (v.cod_stato_fascicolo <> '01' and  v.Data_Arch  is not null))"
				+ "AND v.Data_Iscr < TO_DATE('" + dataIniziale + "', 'dd/MM/yyyy') ";
		if (!"".equals(magCondition))
			s += magCondition;
		s += "union ";
		s += "SELECT DISTINCT V.DESC_MS, V.FASC_SIEP, 'Sopravvenuti' tipo_statistica " + "FROM " + from
				+ " V WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' "
				+ "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between " + "TO_DATE('"
				+ dataIniziale + "', 'dd/MM/yyyy') and TO_DATE('" + dataFinale + "', 'dd/MM/yyyy') ";
		if (!"".equals(magCondition))
			s += magCondition;
		s += "union ";
		// FASCICOLI esauriti
		s += "SELECT DISTINCT V.DESC_MS, V.FASC_SIEP, 'Esauriti' tipo_statistica " + "FROM " + from
				+ " V WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' "
				+ "and v.Data_Arch is not null and v.cod_stato_fascicolo = '01' "
				+ "AND TO_DATE(TO_CHAR(v.Data_Arch, 'dd/MM/yyyy'), 'dd/MM/yyyy') between " + "TO_DATE('"
				+ dataIniziale + "', 'dd/MM/yyyy') and TO_DATE('" + dataFinale + "', 'dd/MM/yyyy') ";
		// + "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between " + "TO_DATE('"
		// + dataIniziale + "', 'dd/MM/yyyy') and TO_DATE('" + dataFinale + "', 'dd/MM/yyyy') "
		if (!"".equals(magCondition))
			s += magCondition;
		s += "union ";
		// FASCICOLI RIAPERTI
		s += "SELECT DISTINCT V.DESC_MS, V.FASC_SIEP, 'Riaperti' tipo_statistica " + "FROM " + from
				+ " V WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' "
				+ "and v.Data_Arch is not null and v.cod_stato_fascicolo <> '01' "
				+ "AND v.Data_Arch between TO_DATE('" + dataIniziale + "', 'dd/MM/yyyy') and " + "TO_DATE('"
				+ dataFinale + "', 'dd/MM/yyyy') ";
		if (!"".equals(magCondition))
			s += magCondition;
		s += "union ";
		// FASCICIOLI PENDENTI FINE
		s += "SELECT DISTINCT V.DESC_MS, V.FASC_SIEP, 'Pendenti Fine' tipo_statistica " + "FROM " + from
				+ " V WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' "
				// [EC]: modifiche per mev39 post collaudo 11.3
				// + "and v.Data_Arch is null OR v.cod_stato_fascicolo <> '01' "
				+ "and (v.Data_Arch is null OR (v.cod_stato_fascicolo <> '01' and  v.Data_Arch  is not null))"
				+ " AND v.Data_Iscr <= TO_DATE('" + dataFinale + "', 'dd/MM/yyyy') ";
		// + "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between " + "TO_DATE('"
		// + dataInizialePiuUno + "', 'dd/MM/yyyy') and TO_DATE('" + dataFinale + "', 'dd/MM/yyyy') "
		if (!"".equals(magCondition))
			s += magCondition;
		s += ") PIVOT(COUNT(FASC_SIEP) FOR tipo_statistica IN('Pendenti Inizio', 'Sopravvenuti', "
				+ "'Esauriti', 'Riaperti', 'Pendenti Fine'))";

		siesLogger.info("Giorno dopo Data Iniziale: " + dataInizialePiuUno);
		setStatement(s);
	}

	public StatisticheMSModel getModelRiepilogoPPPTipologiaMisura(String periodi) throws DAOException {

		StatisticheMSModel smsm = new StatisticheMSModel();
		smsm.setTipoMS(getString("Nome_Stat"));
		String[] s = periodi.split(",");
		Vector<String> p = new Vector<>();
		Vector<Integer> iscrittiParziali = new Vector<>();
		for (int i = 0; i < s.length; i++) {
			p.add(s[i]);
			iscrittiParziali.add(getInteger(s[i]));
		}
		smsm.setPeriodi(p);
		smsm.setIscrittiParziali(iscrittiParziali);

		return smsm;
	}

	/**
	 * 
	 * @param dataIniziale
	 * @param dataFinale
	 * @param ufficioConnesso
	 * @param range
	 * @param codMagistrato
	 * @param var
	 * @throws DAOException
	 * @deprecated Ticket#20210531014 sostituito dal metodo ricercaDettaglioProcedimentiPendentiPeriodo2. LISTAGG va in overflow se si superano i 4000 caratteri
	 */
	public void ricercaDettaglioProcedimentiPendentiPeriodo(String dataIniziale, String dataFinale,
			String ufficioConnesso, String range, String codMagistrato, String var) throws DAOException {
		siesLogger.debug("SQLDAO.ricercaDettaglioProcedimentiPendentiPeriodo...var = "+var);
		
		String s = new String("");
		s += "SELECT * FROM (";

		String[] array = range.split(",");
		String dataFinaleTemp = "";
		String dataInizialePiuUno = "";

		String dataInizialePendenti = "";
		String dataFinalePendenti = "";

		String from1 = "", from2 = "", from3 = "", from4 = "";
		String magCondition = "";
		if ("".equals(codMagistrato)) {
			from1 = "VW_MS_PROC_INIZIO_FINE_PERIODO";
			from2 = "VW_MS_PROCEDIMENTI";
			from3 = "VW_MS_PROC_ESAURITI";
			from4 = "VW_MS_PROC_RIAPERTI";
		} else {
			from1 = "VW_MS_PROC_PERIODO_MAG";
			from2 = "VW_MS_PROCEDIMENTI_MAG";
			from3 = "VW_MS_PROC_ESAURITI_MAG";
			from4 = "VW_MS_PROC_RIAPERTI_MAG";
			if (!"0".equals(codMagistrato))
				magCondition = "AND V.COD_MAGISTRATO = '" + codMagistrato + "' ";
			else
				magCondition = "AND V.COD_MAGISTRATO IS NOT NULL AND V.COD_MAGISTRATO != '-'";
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		//siesLogger.debug("ricercaDettaglioProcedimentiPendentiPeriodo - dataIniziale = " + dataIniziale);
		//siesLogger.debug("ricercaDettaglioProcedimentiPendentiPeriodo - dataFinale = " + dataFinale);

		for (int i = 0; i < array.length; i++) {
			if (array.length == 1) {
				dataFinaleTemp = dataFinale;
				dataInizialePendenti = dataIniziale;
				dataFinalePendenti = dataFinale;
			} else {
				if (i != 0) {
					s += " UNION ";
				}
				// if (i != array.length - 1){
				// dataInizialePendenti = "01/01/" + array[i];
				// dataFinaleTemp = "31/12/" + array[i];
				// dataFinalePendenti = "31/12/" + array[i];
				// }
				// else{
				// dataInizialePendenti = dataIniziale;
				// dataFinaleTemp = dataFinale;
				// dataFinalePendenti= dataFinale;
				// }
				if (i == 0) {
					dataInizialePendenti = dataIniziale;
					dataFinaleTemp = "31/12/" + array[i];
					dataFinalePendenti = "31/12/" + array[i];
				} else if (i != array.length - 1) {
					dataInizialePendenti = "01/01/" + array[i];
					dataFinaleTemp = "31/12/" + array[i];
					dataFinalePendenti = "31/12/" + array[i];
				} else {
					dataInizialePendenti = "01/01/" + array[i];
					dataFinaleTemp = dataFinale;
					dataFinalePendenti = dataFinale;
				}
			}
			if (i == 0) {
				Date di = DateUtils.getDate(dataIniziale, "dd/MM/yyyy");
				dataInizialePiuUno = DateUtils.getDateToString(DateUtils.getDayAfter(di), "dd/MM/yyyy");
			} else
				dataInizialePiuUno = dataIniziale;

			if ("A".equals(var)) {
				// 20191125 [25]: tolgo distinct poichè i fascicoli possono avere più MS dello stesso tipo al
				// loro
				// interno: COUNT(DISTINCT V.NUMERO_FASC) x 5 occorrenze
				s += "SELECT LISTAGG(V.NUMERO_FASC, '#') WITHIN GROUP(ORDER BY ANNO_ISC) Prog_Fasc, "
						+ "COUNT(V.NUMERO_FASC) Tot_Fasc, "
						+ "'Pendenti Inizio' || ' - ' || v.DESC_MS Nome_Stat, '" + array[i] + "' Anno "
						+ "FROM " + from1 + " V where V.Cod_Uff_Ins = '" + ufficioConnesso
						+ "' and (v.Data_Arch is null "
						+ "OR (v.cod_stato_fascicolo <> '01' and v.Data_Arch is not null)) "
						+ "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') < TO_DATE('"
						+ dataInizialePendenti + "', 'dd/MM/yyyy') ";
			
				// + "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between TO_DATE('"
				// + dataInizialePendenti + "', 'dd/MM/yyyy') and TO_DATE('" + dataFinalePendenti
				// + "', 'dd/MM/yyyy') ";
				if (!"".equals(magCondition))
					s += magCondition;
				s += "GROUP BY 'Pendenti Inizio' || ' - ' || v.DESC_MS";
			} else if ("B".equals(var)) {
				s += "SELECT LISTAGG(V.NUMERO_FASC, '#') WITHIN GROUP(ORDER BY ANNO_ISC) Prog_Fasc, "
						+ "COUNT(V.NUMERO_FASC) Tot_Fasc, "
						+ "'Sopravvenuti' || ' - ' || v.DESC_MS Nome_Stat, '" + array[i] + "' Anno " + "from "
						+ from2 + " V " + " WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' "
						+ "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between TO_DATE('"
						+ dataInizialePendenti + "', 'dd/MM/yyyy') and TO_DATE('" + dataFinalePendenti
						+ "', 'dd/MM/yyyy') ";

				
				if (!"".equals(magCondition))
					s += magCondition;
				s += "GROUP BY 'Sopravvenuti' || ' - ' || v.DESC_MS";
			} else if ("C".equals(var)) {
				s += "SELECT LISTAGG(V.NUMERO_FASC, '#') WITHIN GROUP(ORDER BY ANNO_ISC) Prog_Fasc, "
						+ "COUNT(V.NUMERO_FASC) Tot_Fasc, 'Esauriti' || ' - ' || v.DESC_MS Nome_Stat, '"
						+ array[i] + "' Anno " + "from " + from3 + " V WHERE V.Cod_Uff_Ins = '"
						+ ufficioConnesso + "' "
						+ "and v.Data_Arch is not null and v.mot_archiviazione is not null "
						+ "and v.cod_stato_fascicolo = '01' "
						+ "AND TO_DATE(TO_CHAR(v.Data_Arch, 'dd/MM/yyyy'), 'dd/MM/yyyy') between "
						+ "TO_DATE('" + dataInizialePendenti + "', 'dd/MM/yyyy') and TO_DATE('"
						+ dataFinalePendenti + "', 'dd/MM/yyyy') "
						// + "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between "
						// + "TO_DATE('" + dataInizialePendenti + "', 'dd/MM/yyyy') and TO_DATE('" +
						// dataFinalePendenti
						// + "', 'dd/MM/yyyy')"
						+ " ";
				
				if (!"".equals(magCondition))
					s += magCondition;
				s += "GROUP BY 'Esauriti' || ' - ' || v.DESC_MS";
			} else if ("D".equals(var)) {
				s += "SELECT LISTAGG(V.NUMERO_FASC, '#') WITHIN GROUP(ORDER BY ANNO_ISC) Prog_Fasc, "
						+ "COUNT(V.NUMERO_FASC) Tot_Fasc, " + "'Riaperti' || ' - ' || v.DESC_MS Nome_Stat, '"
						+ array[i] + "' Anno " + "FROM " + from4 + " V WHERE V.Cod_Uff_Ins = '"
						+ ufficioConnesso + "' "
						+ "and v.Data_Arch is not null and v.cod_stato_fascicolo <> '01' "
						+ "AND v.Data_Arch between TO_DATE('" + dataInizialePendenti + "', 'dd/MM/yyyy') and "
						+ "TO_DATE('" + dataFinalePendenti + "', 'dd/MM/yyyy') ";
				
				if (!"".equals(magCondition))
					s += magCondition;
				s += "GROUP BY 'Riaperti' || ' - ' || v.DESC_MS";
			} else {	
				s += "SELECT LISTAGG(V.NUMERO_FASC, '#') WITHIN GROUP(ORDER BY ANNO_ISC) Prog_Fasc, "
						+ "COUNT(V.NUMERO_FASC) Tot_Fasc, "
						+ "'Pendenti Fine' || ' - ' || v.DESC_MS Nome_Stat, '" + array[i] + "' Anno "
						+ "FROM " + from1 + " V WHERE V.Cod_Uff_Ins = '" + ufficioConnesso
						+ "' and (v.Data_Arch is null "
						+ "OR (v.cod_stato_fascicolo <> '01' and v.Data_Arch is not null)) "
						+ "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') <= TO_DATE('"
						+ dataFinalePendenti + "', 'dd/MM/yyyy') ";
				// + "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between "
				// + "TO_DATE('" + dataIniziale + "', 'dd/MM/yyyy') and TO_DATE('" + dataFinaleTemp + "',
				// 'dd/MM/yyyy') ";
		
				
				if (!"".equals(magCondition))
					s += magCondition;
				s += "GROUP BY 'Pendenti Fine' || ' - ' || v.DESC_MS";
			}
		}
		siesLogger.info("Data Finale temporanea: " + dataFinaleTemp);
		siesLogger.info("Giorno dopo Data Iniziale: " + dataInizialePiuUno);

		s += ") order by 3, 4";
		setStatement(s);
	}
	
	/**
	 * // Ticket#20210531014 metodo dulicato rispetto al 
	 * public void ricercaDettaglioProcedimentiPendentiPeriodo
	 * Per comodità si duplica il metodo lasciando il vecchio 
	 * @param dataIniziale
	 * @param dataFinale
	 * @param ufficioConnesso
	 * @param range
	 * @param codMagistrato
	 * @param var
	 * @throws DAOException
	 */
	public void ricercaDettaglioProcedimentiPendentiPeriodo2(String dataIniziale, String dataFinale,
			String ufficioConnesso, String range, String codMagistrato, String var) throws DAOException {
		siesLogger.debug("SQLDAO.ricercaDettaglioProcedimentiPendentiPeriodo...var = "+var);
		
		String s = new String("");
		s += "SELECT * FROM (";

		String[] array = range.split(",");
		String dataFinaleTemp = "";
		String dataInizialePiuUno = "";

		String dataInizialePendenti = "";
		String dataFinalePendenti = "";

		String from1 = "", from2 = "", from3 = "", from4 = "";
		String magCondition = "";
		if ("".equals(codMagistrato)) {
			from1 = "VW_MS_PROC_INIZIO_FINE_PERIODO";
			from2 = "VW_MS_PROCEDIMENTI";
			from3 = "VW_MS_PROC_ESAURITI";
			from4 = "VW_MS_PROC_RIAPERTI";
		} else {
			from1 = "VW_MS_PROC_PERIODO_MAG";
			from2 = "VW_MS_PROCEDIMENTI_MAG";
			from3 = "VW_MS_PROC_ESAURITI_MAG";
			from4 = "VW_MS_PROC_RIAPERTI_MAG";
			if (!"0".equals(codMagistrato))
				magCondition = "AND V.COD_MAGISTRATO = '" + codMagistrato + "' ";
			else
				magCondition = "AND V.COD_MAGISTRATO IS NOT NULL AND V.COD_MAGISTRATO != '-'";
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		//siesLogger.debug("ricercaDettaglioProcedimentiPendentiPeriodo - dataIniziale = " + dataIniziale);
		//siesLogger.debug("ricercaDettaglioProcedimentiPendentiPeriodo - dataFinale = " + dataFinale);

		for (int i = 0; i < array.length; i++) {
			if (array.length == 1) {
				dataFinaleTemp = dataFinale;
				dataInizialePendenti = dataIniziale;
				dataFinalePendenti = dataFinale;
			} else {
				if (i != 0) {
					s += " UNION ";
				}
				// if (i != array.length - 1){
				// dataInizialePendenti = "01/01/" + array[i];
				// dataFinaleTemp = "31/12/" + array[i];
				// dataFinalePendenti = "31/12/" + array[i];
				// }
				// else{
				// dataInizialePendenti = dataIniziale;
				// dataFinaleTemp = dataFinale;
				// dataFinalePendenti= dataFinale;
				// }
				if (i == 0) {
					dataInizialePendenti = dataIniziale;
					dataFinaleTemp = "31/12/" + array[i];
					dataFinalePendenti = "31/12/" + array[i];
				} else if (i != array.length - 1) {
					dataInizialePendenti = "01/01/" + array[i];
					dataFinaleTemp = "31/12/" + array[i];
					dataFinalePendenti = "31/12/" + array[i];
				} else {
					dataInizialePendenti = "01/01/" + array[i];
					dataFinaleTemp = dataFinale;
					dataFinalePendenti = dataFinale;
				}
			}
			if (i == 0) {
				Date di = DateUtils.getDate(dataIniziale, "dd/MM/yyyy");
				dataInizialePiuUno = DateUtils.getDateToString(DateUtils.getDayAfter(di), "dd/MM/yyyy");
			} else
				dataInizialePiuUno = dataIniziale;

			if ("A".equals(var)) {
				// 20191125 [25]: tolgo distinct poichè i fascicoli possono avere più MS dello stesso tipo al
				// loro
				// interno: COUNT(DISTINCT V.NUMERO_FASC) x 5 occorrenze
				// Ticket#20210531014] - eliminata la LISTAGG per limite dei 4000 caratteri (varchar2)
				// i dati verranno aggregati dal controller
//				s += "SELECT LISTAGG(V.NUMERO_FASC, '#') WITHIN GROUP(ORDER BY ANNO_ISC) Prog_Fasc, "
//						+ "COUNT(V.NUMERO_FASC) Tot_Fasc, "
//						+ "'Pendenti Inizio' || ' - ' || v.DESC_MS Nome_Stat, '" + array[i] + "' Anno "
//						+ "FROM " + from1 + " V where V.Cod_Uff_Ins = '" + ufficioConnesso
//						+ "' and (v.Data_Arch is null "
//						+ "OR (v.cod_stato_fascicolo <> '01' and v.Data_Arch is not null)) "
//						+ "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') < TO_DATE('"
//						+ dataInizialePendenti + "', 'dd/MM/yyyy') ";
				s += "SELECT V.NUMERO_FASC Prog_Fasc, "
						+ "1 Tot_Fasc, "
						+ "'Pendenti Inizio' || ' - ' || v.DESC_MS Nome_Stat, '" + array[i] + "' Anno "
						+ "FROM " + from1 + " V where V.Cod_Uff_Ins = '" + ufficioConnesso
						+ "' and (v.Data_Arch is null "
						+ "OR (v.cod_stato_fascicolo <> '01' and v.Data_Arch is not null)) "
						+ "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') < TO_DATE('"
						+ dataInizialePendenti + "', 'dd/MM/yyyy') ";				
				// + "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between TO_DATE('"
				// + dataInizialePendenti + "', 'dd/MM/yyyy') and TO_DATE('" + dataFinalePendenti
				// + "', 'dd/MM/yyyy') ";
				if (!"".equals(magCondition))
					s += magCondition;
//				s += "GROUP BY 'Pendenti Inizio' || ' - ' || v.DESC_MS";
			} else if ("B".equals(var)) {
				// Ticket#20210531014] - eliminata la LISTAGG per limite dei 4000 caratteri (varchar2)
				// i dati verranno aggregati dal controller
//				s += "SELECT LISTAGG(V.NUMERO_FASC, '#') WITHIN GROUP(ORDER BY ANNO_ISC) Prog_Fasc, "
//						+ "COUNT(V.NUMERO_FASC) Tot_Fasc, "
//						+ "'Sopravvenuti' || ' - ' || v.DESC_MS Nome_Stat, '" + array[i] + "' Anno " + "from "
//						+ from2 + " V " + " WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' "
//						+ "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between TO_DATE('"
//						+ dataInizialePendenti + "', 'dd/MM/yyyy') and TO_DATE('" + dataFinalePendenti
//						+ "', 'dd/MM/yyyy') ";
				s += "SELECT V.NUMERO_FASC Prog_Fasc, "
						+ "1 Tot_Fasc, "
						+ "'Sopravvenuti' || ' - ' || v.DESC_MS Nome_Stat, '" + array[i] + "' Anno " + "from "
						+ from2 + " V " + " WHERE V.Cod_Uff_Ins = '" + ufficioConnesso + "' "
						+ "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between TO_DATE('"
						+ dataInizialePendenti + "', 'dd/MM/yyyy') and TO_DATE('" + dataFinalePendenti
						+ "', 'dd/MM/yyyy') ";				
				
				if (!"".equals(magCondition))
					s += magCondition;
//				s += "GROUP BY 'Sopravvenuti' || ' - ' || v.DESC_MS";
			} else if ("C".equals(var)) {
				// Ticket#20210531014] - eliminata la LISTAGG per limite dei 4000 caratteri (varchar2)
				// i dati verranno aggregati dal controller
//				s += "SELECT LISTAGG(V.NUMERO_FASC, '#') WITHIN GROUP(ORDER BY ANNO_ISC) Prog_Fasc, "
//						+ "COUNT(V.NUMERO_FASC) Tot_Fasc, 'Esauriti' || ' - ' || v.DESC_MS Nome_Stat, '"
//						+ array[i] + "' Anno " + "from " + from3 + " V WHERE V.Cod_Uff_Ins = '"
//						+ ufficioConnesso + "' "
//						+ "and v.Data_Arch is not null and v.mot_archiviazione is not null "
//						+ "and v.cod_stato_fascicolo = '01' "
//						+ "AND TO_DATE(TO_CHAR(v.Data_Arch, 'dd/MM/yyyy'), 'dd/MM/yyyy') between "
//						+ "TO_DATE('" + dataInizialePendenti + "', 'dd/MM/yyyy') and TO_DATE('"
//						+ dataFinalePendenti + "', 'dd/MM/yyyy') "
//						// + "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between "
//						// + "TO_DATE('" + dataInizialePendenti + "', 'dd/MM/yyyy') and TO_DATE('" +
//						// dataFinalePendenti
//						// + "', 'dd/MM/yyyy')"
//						+ " ";
				s += "SELECT V.NUMERO_FASC Prog_Fasc, "
						+ "1 Tot_Fasc, 'Esauriti' || ' - ' || v.DESC_MS Nome_Stat, '"
						+ array[i] + "' Anno " + "from " + from3 + " V WHERE V.Cod_Uff_Ins = '"
						+ ufficioConnesso + "' "
						+ "and v.Data_Arch is not null and v.mot_archiviazione is not null "
						+ "and v.cod_stato_fascicolo = '01' "
						+ "AND TO_DATE(TO_CHAR(v.Data_Arch, 'dd/MM/yyyy'), 'dd/MM/yyyy') between "
						+ "TO_DATE('" + dataInizialePendenti + "', 'dd/MM/yyyy') and TO_DATE('"
						+ dataFinalePendenti + "', 'dd/MM/yyyy') "
						+ " ";
				
				if (!"".equals(magCondition))
					s += magCondition;
//				s += "GROUP BY 'Esauriti' || ' - ' || v.DESC_MS";
			} else if ("D".equals(var)) {
				// Ticket#20210531014] - eliminata la LISTAGG per limite dei 4000 caratteri (varchar2)
				// i dati verranno aggregati dal controller
//				s += "SELECT LISTAGG(V.NUMERO_FASC, '#') WITHIN GROUP(ORDER BY ANNO_ISC) Prog_Fasc, "
//						+ "COUNT(V.NUMERO_FASC) Tot_Fasc, " + "'Riaperti' || ' - ' || v.DESC_MS Nome_Stat, '"
//						+ array[i] + "' Anno " + "FROM " + from4 + " V WHERE V.Cod_Uff_Ins = '"
//						+ ufficioConnesso + "' "
//						+ "and v.Data_Arch is not null and v.cod_stato_fascicolo <> '01' "
//						+ "AND v.Data_Arch between TO_DATE('" + dataInizialePendenti + "', 'dd/MM/yyyy') and "
//						+ "TO_DATE('" + dataFinalePendenti + "', 'dd/MM/yyyy') ";
				s += "SELECT V.NUMERO_FASC Prog_Fasc, "
						+ "1 Tot_Fasc, " + "'Riaperti' || ' - ' || v.DESC_MS Nome_Stat, '"
						+ array[i] + "' Anno " + "FROM " + from4 + " V WHERE V.Cod_Uff_Ins = '"
						+ ufficioConnesso + "' "
						+ "and v.Data_Arch is not null and v.cod_stato_fascicolo <> '01' "
						+ "AND v.Data_Arch between TO_DATE('" + dataInizialePendenti + "', 'dd/MM/yyyy') and "
						+ "TO_DATE('" + dataFinalePendenti + "', 'dd/MM/yyyy') ";				
				if (!"".equals(magCondition))
					s += magCondition;
//				s += "GROUP BY 'Riaperti' || ' - ' || v.DESC_MS";
			} else {
				// Ticket#20210531014] - eliminata la LISTAGG per limite dei 4000 caratteri (varchar2)
				// i dati verranno aggregati dal controller				
//				s += "SELECT LISTAGG(V.NUMERO_FASC, '#') WITHIN GROUP(ORDER BY ANNO_ISC) Prog_Fasc, "
//						+ "COUNT(V.NUMERO_FASC) Tot_Fasc, "
//						+ "'Pendenti Fine' || ' - ' || v.DESC_MS Nome_Stat, '" + array[i] + "' Anno "
//						+ "FROM " + from1 + " V WHERE V.Cod_Uff_Ins = '" + ufficioConnesso
//						+ "' and (v.Data_Arch is null "
//						+ "OR (v.cod_stato_fascicolo <> '01' and v.Data_Arch is not null)) "
//						+ "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') <= TO_DATE('"
//						+ dataFinalePendenti + "', 'dd/MM/yyyy') ";
				// + "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') between "
				// + "TO_DATE('" + dataIniziale + "', 'dd/MM/yyyy') and TO_DATE('" + dataFinaleTemp + "',
				// 'dd/MM/yyyy') ";
				s += "SELECT V.NUMERO_FASC Prog_Fasc, "
						+ "1 Tot_Fasc, "
						+ "'Pendenti Fine' || ' - ' || v.DESC_MS Nome_Stat, '" + array[i] + "' Anno "
						+ "FROM " + from1 + " V WHERE V.Cod_Uff_Ins = '" + ufficioConnesso
						+ "' and (v.Data_Arch is null "
						+ "OR (v.cod_stato_fascicolo <> '01' and v.Data_Arch is not null)) "
						+ "AND TO_DATE(TO_CHAR(v.Data_Iscr, 'dd/MM/yyyy'), 'dd/MM/yyyy') <= TO_DATE('"
						+ dataFinalePendenti + "', 'dd/MM/yyyy') ";				
				
				if (!"".equals(magCondition))
					s += magCondition;
//				s += "GROUP BY 'Pendenti Fine' || ' - ' || v.DESC_MS";
			}
		}
		siesLogger.info("Data Finale temporanea: " + dataFinaleTemp);
		siesLogger.info("Giorno dopo Data Iniziale: " + dataInizialePiuUno);

		s += ") order by 3, 4, 1";
		setStatement(s);
	}	

	public void ricercaAttivitaMagistratiRiepilogo(int anno, String dataIniziale, String dataFinale)
			throws DAOException {

		String lStatement = new String("");
		lStatement += "select COUNT(A.TIPOLOGIA) CONTA, " + anno
				+ " ANNO, b.DESCRIZIONE_ATTIVITA, b.COD_ATTIVITA, a.TIPO_MS FROM " // , a.COD_MOTIVO
				+ "(select TIPOLOGIA, TIPO_MS from STATIS.ISP_ATTIVITA_MAGISTRATI_MS where " // , COD_MOTIVO
				+ "(DATA_EMISSIONE >= to_date('" + dataIniziale + "', 'ddmmyyyy') "
				+ "and DATA_EMISSIONE <= to_date('" + dataFinale + "', 'ddmmyyyy'))) a, "
				+ "ISP_TIPOLOGIA_ATTIVITA_MS b where a.TIPOLOGIA(+) = b.COD_ATTIVITA "
				// + "and a.COD_MOTIVO is not null "
				+ "group by b.COD_ATTIVITA, b.DESCRIZIONE_ATTIVITA, a.TIPO_MS " // , a.COD_MOTIVO
				+ "order by b.COD_ATTIVITA";
		setStatement(lStatement);
	}

	public void ricercaAttivitaMagistratiDettaglio(int anno, String dataIniziale, String dataFinale,
			String codMag) throws DAOException {

		String lStatement = new String("");
		lStatement += "select COUNT(A.TIPOLOGIA) CONTA, " + anno
				+ " ANNO, b.DESCRIZIONE_ATTIVITA, b.COD_ATTIVITA, a.TIPO_MS " // , a.COD_MOTIVO
				+ "FROM (select TIPOLOGIA, TIPO_MS from " // , COD_MOTIVO
				+ "STATIS.ISP_ATTIVITA_MAGISTRATI_MS where COD_MAGISTRATO ";
		if ((codMag == null) || (codMag != null && codMag.equals("null")))
			lStatement += "is null ";
		else if (codMag != null && "0".equals(codMag))
			lStatement += "is not null and COD_MAGISTRATO != '-' ";
		else
			lStatement += "= '" + codMag + "' ";
		lStatement += "and (DATA_EMISSIONE >= to_date('" + dataIniziale + "', 'ddmmyyyy') "
				+ "and DATA_EMISSIONE <= to_date('" + dataFinale + "', 'ddmmyyyy'))) a, "
				+ "ISP_TIPOLOGIA_ATTIVITA_MS b where a.TIPOLOGIA(+) = b.COD_ATTIVITA "
				// + "and a.COD_MOTIVO is not null "
				+ "group by b.COD_ATTIVITA, b.DESCRIZIONE_ATTIVITA, a.TIPO_MS " // , a.COD_MOTIVO
				+ "order by b.COD_ATTIVITA";

		setStatement(lStatement);
	}

	public GenericModel getModelAttivita() throws DAOException {

		StatisticheMSModel smsm = new StatisticheMSModel();
		smsm.setConta(getInteger("CONTA"));
		smsm.setAnno(getBigDecimal("ANNO"));
		smsm.setDescrMotivo(getString("DESCRIZIONE_ATTIVITA"));
		smsm.setCodAttivita(getString("COD_ATTIVITA"));
		smsm.setTipoMS(getString("TIPO_MS"));

		return smsm;
	}

	public void ricercaTotaleMotivoPerAnno(int anno) throws DAOException {

		String lStatement = new String("");

		lStatement += "select count(*) CONTA, TIPOLOGIA, COD_MOTIVO, to_char(DATA_EMISSIONE, 'YYYY')"
				+ " ANNO_FAS, TIPO_MS from STATIS.Isp_Attivita_Magistrati_Ms WHERE "
				+ "to_char(DATA_EMISSIONE, 'YYYY') = '" + anno
				+ "' GROUP BY TIPOLOGIA, COD_MOTIVO, to_char(DATA_EMISSIONE, 'YYYY'), TIPO_MS"
				+ " order by to_char(DATA_EMISSIONE, 'YYYY'), TIPOLOGIA, COD_MOTIVO";

		setStatement(lStatement);
	}

	public void ricercaTotaleMotivoPerMagistratoAnno(int anno, String codMagistrato) throws DAOException {

		String lStatement = new String("");

		lStatement += "select count(*) CONTA, TIPOLOGIA, COD_MOTIVO, to_char(DATA_EMISSIONE, 'YYYY') "
				+ "ANNO_FAS, TIPO_MS from STATIS.Isp_Attivita_Magistrati_Ms WHERE "
				+ "to_char(DATA_EMISSIONE, 'YYYY') = '" + anno + "' AND COD_MAGISTRATO ";
		if ((codMagistrato == null) || (codMagistrato != null && codMagistrato.equals("null")))
			lStatement += "is null ";
		else if (codMagistrato != null && "0".equals(codMagistrato))
			lStatement += "is not null and COD_MAGISTRATO != '-' ";
		else
			lStatement += "= '" + codMagistrato + "' ";
		lStatement += "GROUP BY TIPOLOGIA, COD_MOTIVO, to_char(DATA_EMISSIONE, 'YYYY'), TIPO_MS"
				+ " order by to_char(DATA_EMISSIONE, 'YYYY'), TIPOLOGIA, COD_MOTIVO";

		setStatement(lStatement);
	}

	public StatisticheMSModel getModelTotaliPerAnno(String tipo) throws DAOException {

		StatisticheMSModel smsm = new StatisticheMSModel();
		smsm.setConta(getInteger("CONTA"));
		smsm.setCodAttivita(getString("TIPOLOGIA"));
		smsm.setCodMotivo(getString("COD_MOTIVO"));
		smsm.setAnno(getBigDecimal("ANNO_FAS"));
		smsm.setTipoMS(getString("TIPO_MS"));
		// if (!"".equals(tipo))
		// smsm.setCodMagistrato(getString("COD_MAGISTRATO"));

		// valore di ritorno
		return smsm;
	}

	public void getNumTipologieAttivita() throws DAOException {

		String lStatement = new String("");
		lStatement += "select COUNT(*) contaTipologie FROM ISP_TIPOLOGIA_ATTIVITA_MS";
		setStatement(lStatement);
	}

	public void ricercaMotivoPerAttivita(String codAttivita) throws DAOException {

		String lStatement = new String("");
		lStatement += "select COD_MOTIVO, DESCRIZIONE_MOTIVO, COD_ATTIVITA, TIPO_MS "
				+ "FROM ISP_MOTIVO_ATTIVITA_MS where COD_ATTIVITA = '" + codAttivita
				+ "' order by COD_MOTIVO";
		setStatement(lStatement);
	}

	public StatisticheMSModel getModelMotivo() throws DAOException {

		StatisticheMSModel smsm = new StatisticheMSModel();
		smsm.setCodAttivita(getString("COD_ATTIVITA"));
		smsm.setCodMotivo(getString("COD_MOTIVO"));
		smsm.setDescrMotivo(getString("DESCRIZIONE_MOTIVO"));
		smsm.setTipoMS(getString("TIPO_MS"));

		return smsm;
	}

}