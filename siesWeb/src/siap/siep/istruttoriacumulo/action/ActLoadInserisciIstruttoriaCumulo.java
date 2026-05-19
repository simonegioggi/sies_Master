package siap.siep.istruttoriacumulo.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActLoadInserisciIstruttoriaCumulo - Classe Action per la load inserisci di IstruttoriaCumulo
 *
 * @version 1.0
 */
public class ActLoadInserisciIstruttoriaCumulo extends ActionSiap implements ICostantiIstruttoriaCumulo {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche di precaricare tutti i dati
	 * da visualizzare i tale pagina (es: combo)
	 *
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = ((FascicoloSiepModel) (getSessionAttribute("fascicolo")));

		// Verifico se il fascicolo è di competenza dell'ufficio
		isFascicoloSiepDiCompetenza();

		// Il fascicolo non può essere Archiviato
		// FIXME gestire in STEP2 gli Archiviati. Va richiesto all'utente se vuole dearchiviarlo
		// e farlo inn automatico.
		isFascicoloArchiviatoDefinito();

		// Verifico se il fascicolo è Validato
		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;
		// Verifico se esiste un precedente provvedimeno NON Validato
		isEventoNonValidato();

		// Verifico se è presente Magistrato assegnatario. Potrebbe essere un migrato che
		// nasce Validato ma senza Magistrato
		MagistratoCompetenteMagistratoModel lMagMod = new MagistratoCompetenteMagistratoModel();
		IMagistratoCompetente lCtrlMagCom = SICOLookupRemote.getMagistratoCompetenteRemote();
		lMagMod = lCtrlMagCom.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod == null || lMagMod.getMagistratoCompetente() == null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun Magistrato assegnato al Fascicolo");
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setAction("siap.sico.magistratocompetente.action.ActLoadInserisciMagistratoCompetente&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		// MEV_2025-48: spostato questo controllo in dati finali
		// Verifico se è presente almeno un Avvocato assegnatario. Potrebbe essere un migrato che
		// nasce Validato ma senza Avvocato
		// Controllo esistenza almeno un avvocato per fascicolo.
		// IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		// try {
		// /* lAvvocati = */lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		// } catch (SIEPException e) {
		// RedirectTo lRedirigi = new RedirectTo();
		// lRedirigi.setPage(IWebConstants.PG_MAIN);
		// setRequestAttribute(IWebConstants.MESSAGE_TEXT,
		// e.getMessage() + " Impossibile aprire una istruttoria cumulo.");
		// lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&"
		// + ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		// return IWebConstants.PG_MESSAGE;
		// }

		// ==========================================================================
		// Verifica se Già presente una istruttoria Cumulo aperta, in questo caso la
		// passa alla finestra
		// ==========================================================================
		IstruttoriaCumuloModel lIstruttoriaModel = new IstruttoriaCumuloModel();

		lIstruttoriaModel.setFlagStato(FLAG_STATO_APERTA); // A = Aperte
		lIstruttoriaModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		Vector lListaIstruttorie = new Vector();

		IIstruttoriaCumulo lIstrCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		lListaIstruttorie = lIstrCtrl.ExRicercaIstruttoriaCumulo(lIstruttoriaModel);

		if (lListaIstruttorie.size() > 0) {
			lIstruttoriaModel = (IstruttoriaCumuloModel) lListaIstruttorie.elementAt(0);
			setRequestAttribute("IstruttoriaCumulo", lIstruttoriaModel);
		}

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCI_ISTRUTTORIA_CUMULO;
	}

}