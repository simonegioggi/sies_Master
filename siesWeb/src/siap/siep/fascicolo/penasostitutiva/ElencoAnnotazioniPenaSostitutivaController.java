package siap.siep.fascicolo.penasostitutiva;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.statoesecuzione.controller.PenaSostitutivaStatoEsecuzioneDati;

// 20261124-MEV_2026-124_SG - eventi di annotazione dell'esecuzione della pena sostitutiva di un procedimento SIEP,
// per l'Elenco Procedimenti della Sorveglianza e del GE
public class ElencoAnnotazioniPenaSostitutivaController extends SiapController {

	public static class Riga {
		public BigDecimal idEvento;
		public String codTipoProvvedimento;
		public String codMotivo;
		public String descrMotivo;
		public Date dataEmissione;
		public Date dataInserimento;
		public String sanzioneResidua;
	}

	public List<Riga> exRicerca(BigDecimal aIdFascicoloSiep) throws F3BException {
		List<Riga> lRighe = new ArrayList<Riga>();
		Connection lConn = null;
		PreparedStatement lStm = null;
		ResultSet lRs = null;
		try {
			lConn = getDBConnection();
			lStm = lConn.prepareStatement("SELECT e.id_evento, e.cod_tipo_provvedimento, e.cod_motivo, "
					+ "(SELECT MAX(c.rv_meaning) FROM cg_ref_codes c WHERE c.rv_domain = 'MOTIVO_PROVVEDIMENTO' "
					+ "AND c.rv_low_value = e.cod_motivo) descr_motivo, e.data_emissione, e.data_inserimento "
					+ "FROM evento e WHERE e.fas_sie_id_fascicolo_siep = ? AND e.cod_tipo_provvedimento NOT IN ('02','03') "
					+ "AND e.cod_motivo IN ("
					+ PenaSostitutivaStatoEsecuzioneDati.getElencoMotiviSql()
					+ ") ORDER BY e.data_emissione DESC, e.data_inserimento DESC, e.id_evento DESC");
			lStm.setBigDecimal(1, aIdFascicoloSiep);
			lRs = lStm.executeQuery();
			PenaSostitutivaStatoEsecuzioneDati lDati = new PenaSostitutivaStatoEsecuzioneDati();
			while (lRs.next()) {
				Riga r = new Riga();
				r.idEvento = lRs.getBigDecimal(1);
				r.codTipoProvvedimento = lRs.getString(2);
				r.codMotivo = lRs.getString(3);
				r.descrMotivo = lRs.getString(4);
				r.dataEmissione = lRs.getTimestamp(5);
				r.dataInserimento = lRs.getTimestamp(6);
				lRighe.add(r);
			}
			for (Riga r : lRighe)
				r.sanzioneResidua = lDati.getDescrizioneSanzioneResidua(r.idEvento);
		} catch (java.sql.SQLException e) {
			throw new F3BException("ElencoAnnotazioniPenaSostitutivaController.exRicerca: " + e);
		} finally {
			try { if (lRs != null) lRs.close(); } catch (Exception e) { }
			try { if (lStm != null) lStm.close(); } catch (Exception e) { }
			try { cleanup(lConn); } catch (Exception e) { }
		}
		return lRighe;
	}
}
