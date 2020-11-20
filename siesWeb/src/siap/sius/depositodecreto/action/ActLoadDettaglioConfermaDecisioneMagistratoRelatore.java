package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
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
public class ActLoadDettaglioConfermaDecisioneMagistratoRelatore extends ActionSiap
		implements ICostantiDepositoDecreto {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		setLinkRitorno();

		FascicoloGPModel fgpm = new FascicoloGPModel();
		fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

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

		// Pagina di ritorno
		return PG_LOAD_DETTAGLIO_CONFERMA_DECISIONE_MAGISTRATO_RELATORE;
	}

}