package siap.siep.scadenzario.penasostitutiva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import f3b.util.F3BException;
import siap.controller.SiapController;

// 20261124-MEV_2026-124_SG - scadenzario delle pene sostitutive (T = semilibertà, V = detenzione domiciliare sostitutive)
public class ScadenzarioPenaSostitutivaController extends SiapController {

	public static final String TIPO_TUTTI = "Tutti";
	public static final String TIPO_ENTRO = "sette";
	public static final String TIPO_OGGI = "oggi";
	public static final String TIPO_SCADUTI = "scaduto";

	private static final int MAX_RIGHE = 500;

	// Fine pena = DATA_FINE_PRESUNTA, altrimenti DATA_INIZIO + durata residua. Considera l'ultima sanzione sostitutiva
	// del fascicolo, non terminata, con pena non sospesa (PENA_RESIDUA.FLAG_PENA_SOSPESA) e fascicolo non archiviato.
	private static final String SQL_BASE =
		"SELECT * FROM (SELECT f.id_fascicolo_siep, f.chiave_anno, f.chiave_progr, so.cognome, so.nome, "
		+ "so.data_nascita, c.descrizione descr_comune, s.cod_tipo_sanzione, s.data_inizio, "
		+ "COALESCE(s.data_fine_presunta, ADD_MONTHS(s.data_inizio, NVL(s.num_anni,0)*12 + NVL(s.num_mesi,0)) + NVL(s.num_giorni,0)) data_fine_calc "
		+ "FROM sanzione_sost_residua s "
		+ "JOIN fascicolo_siep f ON f.id_fascicolo_siep = s.fas_sie_id_fascicolo_siep "
		+ "JOIN soggetto so ON so.id_soggetto = f.sog_id_soggetto "
		+ "LEFT JOIN comune c ON c.cod_comune = so.cod_comune_nascita "
		+ "LEFT JOIN pena_residua p ON p.id_pena_residua = s.pen_res_id_pena_residua "
		+ "WHERE s.cod_tipo_sanzione IN ('T','V') AND s.data_fine IS NULL "
		+ "AND f.data_archiviazione IS NULL AND f.chiave_ufficio = ? "
		+ "AND NVL(p.flag_pena_sospesa,'N') <> 'S' "
		+ "AND s.data_inserimento = (SELECT MAX(s2.data_inserimento) FROM sanzione_sost_residua s2 "
		+ "WHERE s2.fas_sie_id_fascicolo_siep = s.fas_sie_id_fascicolo_siep)) "
		+ "WHERE data_fine_calc IS NOT NULL ";

	public List<ScadenzarioPenaSostitutivaRiga> exRicerca(String aUfficio, String aTipo, int aAnni, int aMesi, int aGiorni)
			throws F3BException {
		StringBuffer lSql = new StringBuffer(SQL_BASE);
		Date lOggi = trunc(new Date());
		Date lLimite = null;
		if (TIPO_OGGI.equals(aTipo)) {
			lSql.append("AND data_fine_calc >= ? AND data_fine_calc < ? + 1 ");
		} else if (TIPO_SCADUTI.equals(aTipo)) {
			lSql.append("AND data_fine_calc < ? ");
		} else if (TIPO_ENTRO.equals(aTipo)) {
			Calendar lCal = Calendar.getInstance();
			lCal.setTime(lOggi);
			lCal.add(Calendar.YEAR, aAnni);
			lCal.add(Calendar.MONTH, aMesi);
			lCal.add(Calendar.DAY_OF_MONTH, aGiorni);
			lLimite = lCal.getTime();
			lSql.append("AND data_fine_calc >= ? AND data_fine_calc < ? + 1 ");
		}
		lSql.append("ORDER BY data_fine_calc, cognome, nome");

		Connection lConn = null;
		PreparedStatement lStm = null;
		ResultSet lRs = null;
		List<ScadenzarioPenaSostitutivaRiga> lRighe = new ArrayList<ScadenzarioPenaSostitutivaRiga>();
		try {
			lConn = getDBConnection();
			lStm = lConn.prepareStatement(lSql.toString());
			int i = 1;
			lStm.setString(i++, aUfficio);
			if (TIPO_OGGI.equals(aTipo)) {
				lStm.setTimestamp(i++, new Timestamp(lOggi.getTime()));
				lStm.setTimestamp(i++, new Timestamp(lOggi.getTime()));
			} else if (TIPO_SCADUTI.equals(aTipo)) {
				lStm.setTimestamp(i++, new Timestamp(lOggi.getTime()));
			} else if (TIPO_ENTRO.equals(aTipo)) {
				lStm.setTimestamp(i++, new Timestamp(lOggi.getTime()));
				lStm.setTimestamp(i++, new Timestamp(lLimite.getTime()));
			}
			lRs = lStm.executeQuery();
			while (lRs.next() && lRighe.size() < MAX_RIGHE) {
				ScadenzarioPenaSostitutivaRiga r = new ScadenzarioPenaSostitutivaRiga();
				r.setIdFascicoloSiep(lRs.getString("id_fascicolo_siep"));
				r.setChiaveAnno(lRs.getString("chiave_anno"));
				r.setChiaveProgr(lRs.getString("chiave_progr"));
				r.setCognome(lRs.getString("cognome"));
				r.setNome(lRs.getString("nome"));
				r.setLuogoNascita(lRs.getString("descr_comune"));
				r.setDataNascita(lRs.getTimestamp("data_nascita"));
				r.setCodTipoSanzione(lRs.getString("cod_tipo_sanzione"));
				r.setDataInizio(lRs.getTimestamp("data_inizio"));
				Date lFine = lRs.getTimestamp("data_fine_calc");
				r.setDataFine(lFine);
				r.setGiorniResidui((trunc(lFine).getTime() - lOggi.getTime()) / 86400000L);
				lRighe.add(r);
			}
		} catch (java.sql.SQLException e) {
			throw new F3BException("ScadenzarioPenaSostitutivaController.exRicerca: " + e);
		} finally {
			try { if (lRs != null) lRs.close(); } catch (Exception e) { }
			try { if (lStm != null) lStm.close(); } catch (Exception e) { }
			cleanup(lConn);
		}
		return lRighe;
	}

	private static Date trunc(Date aDate) {
		Calendar lCal = Calendar.getInstance();
		lCal.setTime(aDate);
		lCal.set(Calendar.HOUR_OF_DAY, 0);
		lCal.set(Calendar.MINUTE, 0);
		lCal.set(Calendar.SECOND, 0);
		lCal.set(Calendar.MILLISECOND, 0);
		return lCal.getTime();
	}
}
