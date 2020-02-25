package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadDettaglioNuovaIstanza
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di NuovaIstanza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Agile S.r.l.
 * </p>
 * 
 * @version 5.0
 */
public class ActLoadDettaglioNuovaIstanza extends ActionSiap implements ICostantiNuovaIstanza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEveMod = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);

		INuovaIstanza lCtrl = SIEPLookupRemote.getNuovaIstanzaRemote();
		NuovaIstanzaModel lNuoMod = null;
		if (this.isRequestParameterNullObj("TipoVis"))
			lNuoMod = lCtrl.ExRicercaNuovaIstanzaByEveIdEvento(lEveMod.getIdEvento());
		else
			lNuoMod = lCtrl.ExRicercaNuovaIstanzaByEveIdEvento(lEveMod.getEveIdEvento());

		IFascicoloSiep lCtrlFs = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel fascSiepMod = null;
		// SG: prevenzione null pointer
		if (lNuoMod != null)
			fascSiepMod = lCtrlFs.ExRicercaFascicoloByKey(lNuoMod.getFasSieIdFascicoloSiep());
		else
			fascSiepMod = lCtrlFs.ExRicercaFascicoloByKey(lEveMod.getFasSieIdFascicoloSiep());

		// 14/03/2011 Lettura dell'Avvocato.
		if (lNuoMod != null && lNuoMod.getAvvIdAvvocato() != null) {
			AvvocatoModel lAvvMod = new AvvocatoModel();
			lAvvMod.setIdAvvocato(lNuoMod.getAvvIdAvvocato());

			IAvvocato lCtrlAvv = SIEPLookupRemote.getAvvocatoRemote();
			AvvocatoModel lAvv = lCtrlAvv.ExRicercaAvvocatoByKey(lNuoMod.getAvvIdAvvocato());
			// 29/06/2011 Il Tipo Avvocato va recuperato da NuovaIstanza
			if (lNuoMod.getTipoAvvocato() != null)
				lAvv.setDescrTipo(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
						.getTipoAvvocato(), lNuoMod.getTipoAvvocato()));
			lNuoMod.setAvvocato(lAvv);
		}

		if (lNuoMod != null && lNuoMod.getAvvIdAvvocatoPresentante() != null) {
			AvvocatoModel lAvvModel = new AvvocatoModel();
			lAvvModel.setIdAvvocato(lNuoMod.getAvvIdAvvocatoPresentante());

			IAvvocato lCtrlAvv = SIEPLookupRemote.getAvvocatoRemote();
			Vector lAvvocati = lCtrlAvv.ExRicercaAvvocatoPerInserimento(lAvvModel);
			AvvocatoModel lAvvocato = new AvvocatoModel((AvvocatoModel) lAvvocati.get(0));
			// 29/06/2011 Il Tipo Avvocato va recuperato da NuovaIstanza
			if (lNuoMod.getTipoAvvocato() != null)
				lAvvocato.setDescrTipo(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
						.getTipoAvvocato(), lNuoMod.getTipoAvvocato()));

			lNuoMod.setAvvocatoPresentante(lAvvocato);
		}

		// ============================================
		// ANNA per RicercaMagistrato in caso di inoltro e quindi solo
		// solo se il dato c'è
		// ============================================
		// if (!lEveMod.getCodMagistrato().equals(null)){
		if (lEveMod.getCodMagistrato() != null) {
			IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
			MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getCodMagistrato());
			setRequestAttribute("magistrato", lMagi);
		}

		try {
			Option lOptionStato = new Option(DecodificheManager.getInstance().getStatoNuovaIstanza());
			lOptionStato.setFilter(lNuoMod != null ? lNuoMod.getCodStatoIstanza() : "-");
			setRequestAttribute("statoIstanza", lOptionStato.toString());
		} catch (Exception ex) {
			throw new F3BException("ActLoadDettaglioNuovaIstanza: " + ex);
		}

		setRequestAttribute("evento", lEveMod);
		setRequestAttribute("nuovaistanza", lNuoMod);
		setSessionAttribute("fascicolo", fascSiepMod);

		// 06/06/2011 Competenza.
		String lCompetenza = "SI";
		try {
			isFascicoloSiepDiCompetenza();
		} catch (Exception ex) {
			lCompetenza = "NO";
		}
		setRequestAttribute("competenza", lCompetenza);

		if (this.isRequestParameterNullObj("TipoVis")) {
			return PG_LOAD_DETTAGLIONUOVAISTANZA;
		} else {
			if (this.getRequestStringParameter("TipoVis").equals("Inoltro")) {
				return PG_LOAD_DETTAGLIO_INOLTRO_PM;
			}
			if (this.getRequestStringParameter("TipoVis").equals("Disposizione")) {
				EventoNotificaModel lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
				setRequestAttribute("eventonotifica", lEveNotMod);
				return PG_LOAD_DETTAGLIO_DISPOSIZIONI_PM;
			}
			return PG_LOAD_DETTAGLIONUOVAISTANZA;
		}
	}

}