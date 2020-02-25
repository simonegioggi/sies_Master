package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioArchiviazionePerProvvGiudiceCassazione
 * </p>
 * <p>
 * Description: Load Dettaglio del Archiviazione
 * </p>
 * <p>
 * (Tipo_Eve = 01 e Tipo_Provv = 25 (Annotazione) )
 * </p>
 * <p>
 * per Provvedimento emesso da Giuduce/Cassazione
 * </p>
 * <p>
 * (Definizione. MIS. SIC. Provvisorie o Fuori Sentenza)
 */
public class ActLoadDettaglioArchiviazionePerProvvGiudiceCassazione extends ActSIESDettaglioProvvedimento {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

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

		// Misure Sicurezza

		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		List lListMis = new ArrayList();
		lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("listaMisureSic", lListMis);

		// POSIZIONE GIURIDICA
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lIdFascicolo);
		setRequestAttribute("posizioneluogoaltra", lPos);

		// MAGISTRATO
		MagistratoModel lMag = lEveNotMod.getMagistrato();
		setRequestAttribute("magistrato", lMag);

		// PENA RESIDUA
		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento, lIdFascicolo);
		setRequestAttribute("penaresidua", llPenMod);

		return ICostantiMisuraSicurezza.PG_LOAD_DETTAGLIO_ARCHIVIAZIONE_PER_PROVV_GIUDICE_CASSAZIONE;
	}

}