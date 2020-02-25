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
public class ActLoadDettaglioDecretoIncompetenza extends ActionSiap implements ICostantiDepositoDecreto {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Gestione ritorno
		this.setLinkRitorno();

		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		// Preleva id evento dalla request.
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Preleva attraverso l'id evento generato, il decreto in deposito decreto.
		IDepositoDecreto lCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
		DepositoDecretoEventoMotivazioniModel lDepDecrMotMod = lCtrl
				.ExRicercaDecretoEventoMotivazioniIncompetenzaByIdEvento(lIdEvento);

		// Inserisce l'id evento nel model. Perchè mai ????????????
		lDepDecrMotMod.getEvento().setIdEvento(lIdEvento);

		// Preleva i tenori, per il generale procedimento.
		ITenore lTenCtrl = SIUSLookupRemote.getTenoreRemote();
		Vector lTenori = lTenCtrl
				.ExRicercaTenoreByDecreto(lDepDecrMotMod.getDepositoDecreto().getIdDepositoDecreto());

		// Imposta gli oggetti nella request.
		setRequestAttribute("tenori", lTenori);
		setRequestAttribute("depositoDecretoMotivazioni", lDepDecrMotMod);

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl
				.ExRicercaEstesaMagRelByFascicolo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("magistratorelatore", lMagRel);

		return PG_LOAD_DETTAGLIO_DECRETO_INCOMPETENZA;
	}

}