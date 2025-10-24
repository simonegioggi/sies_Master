package siap.sico.magistratocompetente.action;

import java.util.Vector;

import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

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
		String lCodUfficio = this.getCodUfficioUtenteConnesso();
		String lStato[] = { "02", "03" };

		Vector lListaProcedimenti = null;
		IFascicoloSiep lFascSiesCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		lListaProcedimenti = lFascSiesCtrl.ExRicercaFascicoliByMagistratoAssegnatario(lCodMagistrato,
				lCodUfficio, lStato);

		setRequestAttribute("aListaProcedimenti", lListaProcedimenti);

		return PG_ESITO_RICERCAPROCEDIMENTI;
	}

}