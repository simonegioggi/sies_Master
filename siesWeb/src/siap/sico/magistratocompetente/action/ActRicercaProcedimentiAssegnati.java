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
public class ActRicercaProcedimentiAssegnati extends ActionSiap implements ICostantiMagistratoCompetente {

	/**
	 * Azione di Ricerca dei Procedimenti assegnati a un determinato Magistrato
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		String lCodMagistrato = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);

		// ==========================================================================
		// Recupero il magistrato da passare alla form esito ricerca
		// ==========================================================================
		IMagistrato lMagistratoCtrl = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagModel = lMagistratoCtrl.ExRicercaMagistratoByCod(lCodMagistrato);
		setRequestAttribute("aMagistrato", lMagModel);

		// ==========================================================================
		// Effettuo la ricerca dei fascicoli (Iscritti/Validati) dell'ufficio dell'utente
		// connesso, assegnati attualmente al Magistrato specificato
		// ==========================================================================
		String lCodUfficio = getCodUfficioUtenteConnesso();
		String lStato[] = { "02", "03" };

		// MEV_2025-48: aggiunta nuova funzionalita': paginata la ricerca
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		Vector lListaProcedimenti = null;
		IFascicoloSiep ifs = SIEPLookupRemote.getFascicoloSiepRemote();
		lListaProcedimenti = ifs.ExRicercaFascicoliByMagistratoAssegnatarioPaged(lCodMagistrato, lCodUfficio,
				lStato, Integer.parseInt(lPagina));
		setRequestAttribute("aListaProcedimenti", lListaProcedimenti);

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = ifs.ExGetCountProcedimenti(lCodMagistrato, lCodUfficio, lStato);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute("totaleProcedimenti", "" + CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		// pagina di ritorno
		return PG_ESITO_RICERCAPROCEDIMENTI;
	}

}