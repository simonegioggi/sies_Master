package siap.siep.archiviazione.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadDettaglioAssorCumuloAltro
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di NonLuogoAProvvedere
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
public class ActLoadDettaglioAssorCumuloAltro extends ActSIESDettaglioProvvedimento {

	public String processRequest() throws F3BException {

		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		// FASCICOLO
		IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel lFascMod = lCtrlFas.ExRicercaFascicoloByKey(lIdFascicolo);
		setRequestAttribute("fascicolosiep", lFascMod);

		// EVENTO NOTIFICA
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("eventonotifica", lEveNotMod);

		// ARCHIVIAZIONE
		IArchiviazione lCtrlArc = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel lArcMod = new ArchiviazioneModel();
		lArcMod = lCtrlArc.ExRicercaArchiviazioneCssaIstitutoByIdEvento(lIdEvento);
		setRequestAttribute("archiviazione", lArcMod);

		// POSIZIONE GIURIDICA
		/*
		 * REWORK DETTAGLIO IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.
		 * getIdFascicoloSiep());
		 */

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = getPosizioneGiuridicaLuogoDetenzioneAltraCausa(
				lIdEvento, lIdFascicolo);

		setRequestAttribute("posizioneluogoaltra", lPos);

		// MAGISTRATO
		MagistratoModel lMag = lEveNotMod.getMagistrato();

		setRequestAttribute("magistrato", lMag);

		// PENA RESIDUA
		/*
		 * REWORK DETTAGLIO IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel
		 * llPenMod =
		 * lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveNotMod.getEvento().getFasSieIdFascicoloSiep(
		 * ));
		 */
		// MEV_66: modificato controllo consistenza per prevenzione nullpointer
		if (!isRequestParameterNullObj("modifica")
				&& !"N".equals(lEveNotMod.getEvento().getFlagDocumentoRegistrato())) {
			setRequestAttribute("modifica", "dettaglio");
		}

		PenaResiduaModel llPenMod = getPenaResidua(lIdEvento, lIdFascicolo);

		setRequestAttribute("penaresidua", llPenMod);

		Option lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutorita", "" + lOptionAutorita);

		return ICostantiArchiviazione.PG_LOAD_DETTAGLIO_ASSOR_CUMULO_ALTRO;
	}

}