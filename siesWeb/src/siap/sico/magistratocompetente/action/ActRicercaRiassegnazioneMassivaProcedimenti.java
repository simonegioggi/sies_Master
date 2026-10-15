package siap.sico.magistratocompetente.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings("rawtypes")
public class ActRicercaRiassegnazioneMassivaProcedimenti extends ActionSiap
		implements ICostantiMagistratoCompetente {

	/**
	 * Stima dei procedimenti assegnati a un Magistrato che rispondono al criterio scelto
	 * (lettere iniziali del cognome del soggetto oppure ultime cifre del progressivo)
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		String lCodMagistrato = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
		String lTipoCriterio = getRequestStringParameter(CAMPO_TIPO_CRITERIO_RIASSEGNAZIONE);
		String lValoreCriterio = getRequestStringParameter(CAMPO_VALORE_CRITERIO_RIASSEGNAZIONE);

		IMagistrato lMagistratoCtrl = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagModel = lMagistratoCtrl.ExRicercaMagistratoByCod(lCodMagistrato);
		setRequestAttribute("aMagistrato", lMagModel);
		setRequestAttribute(CAMPO_TIPO_CRITERIO_RIASSEGNAZIONE, lTipoCriterio);
		setRequestAttribute(CAMPO_VALORE_CRITERIO_RIASSEGNAZIONE, lValoreCriterio);

		String lCodUfficio = getCodUfficioUtenteConnesso();
		String lStato[] = { "02", "03" };

		IFascicoloSiep ifs = SIEPLookupRemote.getFascicoloSiepRemote();
		Vector lListaProcedimenti = ifs.ExRicercaFascicoliByMagistratoAssegnatario(lCodMagistrato,
				lCodUfficio, lStato, lTipoCriterio, lValoreCriterio);
		setRequestAttribute("aListaProcedimenti", lListaProcedimenti);

		BigDecimal lCount = ifs.ExGetCountProcedimenti(lCodMagistrato, lCodUfficio, lStato, lTipoCriterio,
				lValoreCriterio);
		setRequestAttribute("CountRisultati", lCount);
		setRequestAttribute("totaleProcedimenti", "" + lCount);

		return PG_ESITO_RIASSEGNAZIONEMASSIVA_PROCEDIMENTI;
	}
}
