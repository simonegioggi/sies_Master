package siap.siep.fascicolo.penasostitutiva;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import siap.controller.SiapController;

// 20261124-MEV_2026-124_SG - ricava dinamicamente il fascicolo SIUS (procedimento EPS) collegato all'evento
// dell'esecuzione della pena sostitutiva
public class FascicoloSiusPenaSostitutivaController extends SiapController {

	public BigDecimal getIdFascicoloSius(BigDecimal aIdEvento) {
		if (aIdEvento == null)
			return null;
		Connection lConn = null;
		PreparedStatement lStm = null;
		ResultSet lRs = null;
		try {
			lConn = getDBConnection();
			// L'evento puo' essere sia l'evento SIUS sia l'annotazione SIEP che lo richiama (EVE_ID_EVENTO)
			lStm = lConn.prepareStatement("SELECT MAX(e.fas_siu_id_fascicolo_sius) FROM evento e "
					+ "WHERE e.fas_siu_id_fascicolo_sius IS NOT NULL AND (e.id_evento = ? "
					+ "OR e.id_evento = (SELECT a.eve_id_evento FROM evento a WHERE a.id_evento = ?))");
			lStm.setBigDecimal(1, aIdEvento);
			lStm.setBigDecimal(2, aIdEvento);
			lRs = lStm.executeQuery();
			return lRs.next() ? lRs.getBigDecimal(1) : null;
		} catch (Exception e) {
			return null;
		} finally {
			try { if (lRs != null) lRs.close(); } catch (Exception e) { }
			try { if (lStm != null) lStm.close(); } catch (Exception e) { }
			try { cleanup(lConn); } catch (Exception e) { }
		}
	}
}
