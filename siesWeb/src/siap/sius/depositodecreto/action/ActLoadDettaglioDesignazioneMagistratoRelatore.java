package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Stack;
import java.util.Vector;

import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sius.ActionSius;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.tenore.controller.ITenore;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_9: aggiunta action di caricamento dati
 *
 * @author Gioggi
 */
public class ActLoadDettaglioDesignazioneMagistratoRelatore extends ActionSius
		implements ICostantiDepositoDecreto {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloGPModel fgpm = new FascicoloGPModel();
		fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		setLinkRitorno();
		Stack<String> lRetStack = (Stack<String>) getSessionAttribute("StackDiRitorno");
		String retURL = lRetStack.peek().toString();
		if (Utils.isPresent(retURL)) {
			lRetStack.pop(); // Rimuovo il vecchio valore
			retURL = "siap.sius.fascicolo.action.ActLoadDettaglioFascicolo";
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			rt.setAction("siap.sius.fascicolo.action.ActLoadDettaglioFascicolo");
			rt.setParameter("IdFascicoloSius", fgpm.getFascicoloSiusModel().getIdFascicoloSius().toString());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			// fgpm.getFascicoloSiusModel().getIdFascicoloSius().toString());
			lRetStack.push(rt.toString()); // lo sostituisco con il nuovo
			// Rimetto in sessione lo Stack
			setSessionAttribute("StackDiRitorno", lRetStack);
		}

		// Preleva id evento dalla request
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Preleva attraverso l'id evento generato, il decreto in deposito decreto.
		IDepositoDecreto idd = SIUSLookupRemote.getDepositoDecretoRemote();
		DepositoDecretoEventoMotivazioniModel ddemm = idd
				.ExRicercaDecretoEventoMotivazioniIncompetenzaByIdEvento(idEvento);

		// Inserisce l'id evento nel model
		ddemm.getEvento().setIdEvento(idEvento);

		// Preleva i tenori, per il generale procedimento.
		ITenore it = SIUSLookupRemote.getTenoreRemote();
		Vector tenori = it.ExRicercaTenoreByDecreto(ddemm.getDepositoDecreto().getIdDepositoDecreto());

		// Imposta gli oggetti nella request
		setRequestAttribute("tenori", tenori);
		setRequestAttribute("depositoDecretoMotivazioni", ddemm);

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore imr = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel mrm = imr
				.ExRicercaEstesaMagRelByFascicolo(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("magistratorelatore", mrm);

		// Modificabilità
		String isModificabile = "NO";
		if (IsFascicoloSiusModificabile()) {
			// Stampabilità
			if (ddemm.getEvento().getFlagDocumentoRegistrato() == null
					|| ddemm.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
				// Se depositato non può essere cancellato
				if (ddemm.getDepositoDecreto().getAnnoS72() == null
						&& ddemm.getDepositoDecreto().getNumS72() == null)
					isModificabile = "SI";
			}
		}
		setRequestAttribute("Modificabile", isModificabile);

		// Ricerca avvocati assegnati al fascicolo
		IAvvocato ia = SIUSLookupRemote.getAvvocatoRemote();
		Vector lAvvocato = ia
				.ExRicercaAvvocatiByFascicoloNoError(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("avvocato", lAvvocato);

		// Pagina di ritorno
		return PG_LOAD_DETTAGLIO_DESIGNAZIONE_MAGISTRATO_RELATORE;
	}

}