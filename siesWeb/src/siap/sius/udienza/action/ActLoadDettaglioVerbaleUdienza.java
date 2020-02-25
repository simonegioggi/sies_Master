package siap.sius.udienza.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.esperto.controller.IEsperto;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioVerbaleUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load della jsp di dettaglio.
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
public class ActLoadDettaglioVerbaleUdienza extends ActionSiap implements ICostantiUdienza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// I parametri passati all'azione con il metodo get.
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		MagistratoRelatoreModel lMagRel = null;
		MagistratoModel lMagistrato = null;
		EspertoModel lEsperto = null;

		// Fascicolo Sius
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		BigDecimal lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		// Chiama il controller Evento per ricerca di un evento
		EventoModel lEveMod = new EventoModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

		// Magistrato relatore
		IMagistratoRelatore lMagRelCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		lMagRel = lMagRelCtrl.ExRicercaMagRelByFascicolo(lIdFasSius);

		if (lMagRel != null) {
			// Magistrato
			if (lMagRel.getMagCodMagistrato() != null) {
				IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
				lMagistrato = lMagCtrl.ExRicercaMagistratoByCod(lMagRel.getMagCodMagistrato());
			}

			// Esperto
			if (lMagRel.getEspIdEsperto() != null) {
				IEsperto lEspCtrl = SIUSLookupRemote.getEspertoRemote();
				lEsperto = lEspCtrl.ExRicercaEspertoByKey(lMagRel.getEspIdEsperto());
			}
		}

		// Avvocati assegnati al fascicolo con data fine = null
		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSiusModel lAvvFascMod = new AvvocatoFascicoloSiusModel();

		lAvvFascMod.setFasSiuIdFascicoloSius((lFasGPMod.getFascicoloSiusModel()).getIdFascicoloSius());

		IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector lAvvocato = lAvvCtrl.ExRicercaAvvocatiAttualiFascicolo(lAvvMod, lAvvFascMod);

		setRequestAttribute("evento", lEveMod);
		setRequestAttribute("magistrato", lMagistrato);
		setRequestAttribute("avvocato", lAvvocato);
		setRequestAttribute("esperto", lEsperto);

		return PG_LOAD_DETTAGLIOVERBALEUDIENZA;
	}

}