package siap.sius.magistratorelatore.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.util.SIUSLookupRemote;

public class ActRicercaProcedimentiSorveglianzaAssegnati extends ActionSiap
		implements ICostantiMagistratoRelatore {

	/**
	 * Azione di Ricerca dei Procedimenti assegnati a un determinato Magistrato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		String lStato[] = { "02", "03", "10" };

		// MEV_2025-48: aggiunta nuova funzionalita': paginata la ricerca
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		Vector lListaProcedimenti = null;
		Vector lListaSoggetti = new Vector();

		IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
		lListaProcedimenti = ifs.ExRicercaFascicoliByMagistratoSorvAssegnatarioPaged(lCodMagistrato,
				lCodUfficio, lStato, Integer.parseInt(lPagina));
		setRequestAttribute("aListaProcedimenti", lListaProcedimenti);

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = ifs.ExGetCountProcedimenti(lCodMagistrato, lCodUfficio, lStato);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		if (lListaProcedimenti != null) {
			ISoggetto is = SICOLookupRemote.getSoggettoRemote();
			for (int i = 0; i < lListaProcedimenti.size(); i++) {
				FascicoloSiusModel lFascicolo = (FascicoloSiusModel) lListaProcedimenti.elementAt(i);
				SoggettoModel lSoggetto = is.ExRicercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
				lListaSoggetti.addElement(lSoggetto);
			}
			setRequestAttribute("aListaSoggetti", lListaSoggetti);
		}

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute("totaleProcedimenti", "" + CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		// pagina di ritorno
		return PG_ESITO_RICERCAPROCEDIMENTI_SORVEGLIANZA;
	}

}