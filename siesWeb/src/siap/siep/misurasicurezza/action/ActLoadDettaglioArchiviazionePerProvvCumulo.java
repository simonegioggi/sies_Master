package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadDettaglioArchiviazionePerProvvCumulo
 * </p>
 * <p>
 * Description: Load Dettaglio dell'Archiviazione per provvedimento di cumulo
 * </p>
 */
public class ActLoadDettaglioArchiviazionePerProvvCumulo extends ActSIESDettaglioProvvedimento {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		BigDecimal idFascicoloSiep = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		// FASCICOLO
		IFascicoloSiep ifs = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel fsm = ifs.ExRicercaFascicoloByKey(idFascicoloSiep);
		setRequestAttribute("fascicolosiep", fsm);

		// EVENTO NOTIFICA
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		IEvento iEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enm = iEvento.ExRicercaEventoNotificaByKey(idEvento);
		setRequestAttribute("eventonotifica", enm);

		// ARCHIVIAZIONE
		IArchiviazione iArchiviazione = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel am = new ArchiviazioneModel();
		am = iArchiviazione.ExRicercaArchiviazioneCssaIstitutoByIdEvento(idEvento);
		setRequestAttribute("archiviazione", am);

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = getPosizioneGiuridicaLuogoDetenzioneAltraCausa(
				idEvento, idFascicoloSiep);
		setRequestAttribute("posizioneluogoaltra", pgldacm);

		PenaResiduaModel prm = getPenaResidua(idEvento, idFascicoloSiep);
		setRequestAttribute("penaresidua", prm);

		// MAGISTRATO
		MagistratoModel mm = enm.getMagistrato();
		setRequestAttribute("magistrato", mm);

		// 20190604 [SG]: aggiunta sezione mancante!
		List listaMisure = new ArrayList();
		IMisuraSicurezza ims = SIEPLookupRemote.getMisuraSicurezzaRemote();
		listaMisure = ims.ExRicercaMisuraSicurezzaByIdFascicoloOrd(idFascicoloSiep);
		setRequestAttribute("listaMisure", listaMisure);

		// valore di ritorno
		return ICostantiMisuraSicurezza.PG_LOAD_DETTAGLIO_ARCHIVIAZIONE_PER_PROVV_CUMULO;
	}

}