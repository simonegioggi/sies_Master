package siap.sius.magistratorelatore.action;

import java.util.Vector;

import f3b.util.F3BException;
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
		String lCodUfficio = this.getCodUfficioUtenteConnesso();
		String lStato[] = { "02", "03", "10" };

		Vector lListaProcedimenti = null;
		Vector lListaSoggetti = new Vector();

		IFascicoloSius lFascSiusCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		lListaProcedimenti = lFascSiusCtrl.ExRicercaFascicoliByMagistratoSorvAssegnatario(lCodMagistrato,
				lCodUfficio, lStato);

		setRequestAttribute("aListaProcedimenti", lListaProcedimenti);

		if (lListaProcedimenti != null) {
			ISoggetto lSoggettoCtrl = SICOLookupRemote.getSoggettoRemote();
			for (int i = 0; i < lListaProcedimenti.size(); i++) {
				FascicoloSiusModel lFascicolo = (FascicoloSiusModel) lListaProcedimenti.elementAt(i);
				SoggettoModel lSoggetto = lSoggettoCtrl.ExRicercaSoggettoByKey(lFascicolo.getSogIdSoggetto());
				lListaSoggetti.addElement(lSoggetto);
			}
			setRequestAttribute("aListaSoggetti", lListaSoggetti);
		}

		return PG_ESITO_RICERCAPROCEDIMENTI_SORVEGLIANZA;
	}

}