package siap.siep.statoesecuzione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import siap.controller.SiapController;

// 20261124-MEV_2026-124_SG - dati della pena sostitutiva residua collegata a un evento, per il Certificato Stato Esecuzione
public class PenaSostitutivaStatoEsecuzioneDati extends SiapController {

	// Motivi degli eventi di annotazione delle pene sostitutive (provvedimenti, revoca/conversione, sospensione/ripresa)
	private static final String[] MOTIVI_PS = { "2900", "2901", "2910", "2911", "2912", "2913", "2914", "2935",
			"2936", "2937", "2938", "2940", "2941", "3190", "3200", "9300", "9301" };

	public static boolean isMotivoPenaSostitutiva(String aCodMotivo) {
		if (aCodMotivo == null)
			return false;
		for (int i = 0; i < MOTIVI_PS.length; i++)
			if (MOTIVI_PS[i].equals(aCodMotivo))
				return true;
		return false;
	}

	/**
	 * @return descrizione della sanzione sostitutiva residua dell'evento (con indicazione di esecuzione sospesa),
	 *         oppure null se l'evento non ha una pena sostitutiva residua
	 */
	public String getDescrizioneSanzioneResidua(BigDecimal aIdEvento) {
		Connection lConn = null;
		PreparedStatement lStm = null;
		ResultSet lRs = null;
		try {
			lConn = getDBConnection();
			lStm = lConn.prepareStatement("SELECT ssr.cod_tipo_sanzione, ssr.num_anni, ssr.num_mesi, ssr.num_giorni, "
					+ "pr.flag_pena_sospesa FROM sanzione_sost_residua ssr, pena_residua pr "
					+ "WHERE pr.id_pena_residua = ssr.pen_res_id_pena_residua AND pr.eve_id_evento = ? "
					+ "AND ssr.cod_tipo_sanzione IN ('T','V') ORDER BY ssr.id_sanzione_sost_residua");
			lStm.setBigDecimal(1, aIdEvento);
			lRs = lStm.executeQuery();
			if (!lRs.next())
				return null;
			StringBuffer lBuf = new StringBuffer();
			lBuf.append("T".equals(lRs.getString(1)) ? "Semilibert\u00e0 sostitutiva" : "Detenzione domiciliare sostitutiva");
			append(lBuf, "Anni", lRs.getBigDecimal(2));
			append(lBuf, "Mesi", lRs.getBigDecimal(3));
			append(lBuf, "Giorni", lRs.getBigDecimal(4));
			if ("S".equals(lRs.getString(5)))
				lBuf.append(" - Esecuzione sospesa");
			return lBuf.toString();
		} catch (Exception e) {
			return null;
		} finally {
			try { if (lRs != null) lRs.close(); } catch (Exception e) { }
			try { if (lStm != null) lStm.close(); } catch (Exception e) { }
			try { cleanup(lConn); } catch (Exception e) { }
		}
	}

	private void append(StringBuffer aBuf, String aEtichetta, BigDecimal aValore) {
		if (aValore != null && aValore.intValue() != 0)
			aBuf.append(" ").append(aEtichetta).append(" ").append(aValore);
	}
}
