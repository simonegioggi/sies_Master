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
 * <p>
 * Title: ActLoadDettaglioDecretoinammissibilita
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Decreto Inammissibilita
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioDecretoNDPNLP extends ActionSiap implements ICostantiDepositoDecreto {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Gestione ritorno
		setLinkRitorno();

		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		// Preleva id evento dalla request.
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Preleva attraverso l'id evento generato, il decreto in deposito decreto.
		IDepositoDecreto lCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
		// Si utilizza il metodo ExRicercaDecretoEventoMotivazioniInammissibilitaByIdEvento, poichè
		// anche se il naming non è contestualizzato, è il metodo è già utilizzato anche dalle parti comuni
		// es.: ActLoadDettaglioDecretoDeposito pertanto è un pseudo centralizzato.
		DepositoDecretoEventoMotivazioniModel lDepDecrEveMotMod = lCtrl
				.ExRicercaDecretoEventoMotivazioniInammissibilitaByIdEvento(lIdEvento);

		// Inserisce l'id evento nel model. Perchè mai ????????????
		lDepDecrEveMotMod.getEvento().setIdEvento(lIdEvento);

		// Preleva i tenori, per il generale procedimento.
		ITenore lTenCtrl = SIUSLookupRemote.getTenoreRemote();
		Vector lTenori = lTenCtrl.ExRicercaTenoreByDecreto(lDepDecrEveMotMod.getDepositoDecreto()
				.getIdDepositoDecreto());

		// Imposta gli oggetti nella request.
		setRequestAttribute("tenori", lTenori);
		setRequestAttribute("depositoDecretoMotivazioni", lDepDecrEveMotMod);

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl.ExRicercaEstesaMagRelByFascicolo(lFasGPMod
				.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("magistratorelatore", lMagRel);

		return PG_LOAD_DETTAGLIO_DECRETO_NDP_NLP;
	}

}