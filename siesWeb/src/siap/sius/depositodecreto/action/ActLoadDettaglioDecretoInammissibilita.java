package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sius.ActionSius;
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
public class ActLoadDettaglioDecretoInammissibilita extends ActionSius implements ICostantiDepositoDecreto {
	DepositoDecretoEventoMotivazioniModel lDepDecrMotMod;

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		this.setLinkRitorno();

		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva id evento dalla request.
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Preleva attraverso l'id evento generato, il decreto in deposito decreto.
		IDepositoDecreto lCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
		lDepDecrMotMod = lCtrl.ExRicercaDecretoEventoMotivazioniInammissibilitaByIdEvento(lIdEvento);

		// Inserisce l'id evento nel model.
		lDepDecrMotMod.getEvento().setIdEvento(lIdEvento);

		// Preleva i tenori, per il generale procedimento.
		ITenore lTenCtrl = SIUSLookupRemote.getTenoreRemote();
		Vector lTenori = lTenCtrl
				.ExRicercaTenoreByDecreto(lDepDecrMotMod.getDepositoDecreto().getIdDepositoDecreto());

		// Gestione del carattere € da passare alla jsp (al momento solo per Remissione Debito)
		if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U011") == 0) {
			if ((lDepDecrMotMod.getMotivazioniDecreto() != null)
					&& (lDepDecrMotMod.getMotivazioniDecreto().length > 0)) {
				for (int j = 0; j < lDepDecrMotMod.getMotivazioniDecreto().length; j++) {
					lDepDecrMotMod.getMotivazioniDecreto()[j]
							.setDescrMotivazione(lDepDecrMotMod.getMotivazioniDecreto()[j]
									.getDescrMotivazione().replace("€", "&#8364;"));
				}
			}
		}
		//

		// Imposta gli oggetti nella request.
		setRequestAttribute("tenori", lTenori);
		setRequestAttribute("depositoDecretoMotivazioni", lDepDecrMotMod);

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl
				.ExRicercaEstesaMagRelByFascicolo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("magistratorelatore", lMagRel);

		modificabile();

		return PG_LOAD_DETTAGLIO_DECRETO_INAMMISSIBILITA;
	}

	private void modificabile() throws Exception {

		// Modificabilità
		String lModificabile = "NO";
		String lStampabile = "NO";
		if (IsFascicoloSiusModificabile()) {
			// Stampabilità
			if (lDepDecrMotMod.getEvento().getFlagDocumentoRegistrato() == null
					|| lDepDecrMotMod.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
				lStampabile = "SI";
				// Se depositato non può essere cancellato
				if (lDepDecrMotMod.getDepositoDecreto().getAnnoS72() == null
						&& lDepDecrMotMod.getDepositoDecreto().getNumS72() == null)
					lModificabile = "SI";
				else
					lModificabile = "NO";
			}

		}
		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Stampabile", lStampabile);
	}

}