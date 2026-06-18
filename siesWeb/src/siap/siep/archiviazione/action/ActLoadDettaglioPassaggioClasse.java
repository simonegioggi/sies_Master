package siap.siep.archiviazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.util.Utils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * ActLoadDettaglioPassaggioClasse - Classe Action per la load dettaglio di Passaggio di Classe
 *
 * @version 1.0
 */
public class ActLoadDettaglioPassaggioClasse extends ActSIESDettaglioProvvedimento {

	@SuppressWarnings("unchecked")
	public String processRequest() throws F3BException {

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();

		// EVENTO NOTIFICA
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enm = ie.ExRicercaEventoNotificaByKey(idEvento);
		setRequestAttribute("eventonotifica", enm);

		// ARCHIVIAZIONE
		IArchiviazione ia = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel am = new ArchiviazioneModel();
		am = ia.ExRicercaArchiviazioneCssaIstitutoByIdEvento(idEvento);
		setRequestAttribute("archiviazione", am);

		// NOTIFICHE
		if (enm != null && enm.getNotifiche() != null) {
			NotificaModel[] nmArray = enm.getNotifiche();
			for (int i = 0; i < nmArray.length; i++) {
				// Cassellario
				if (nmArray[i].getAutEstIdAutoritaEsterna() != null
						&& "E".equals(nmArray[i].getCodTipoNotifica())) {
					setRequestAttribute("autorita", nmArray[i]);
				}

				// istituto
				if (nmArray[i].getIstDetIdIstitutoDetenzione() != null
						&& !nmArray[i].getIstDetIdIstitutoDetenzione().equals("")) {
					setRequestAttribute("istituto", nmArray[i]);
				}

				// ufficio recupero crediti
				if (nmArray[i].getUffCodUfficio() != null) {
					setRequestAttribute("ufficio", nmArray[i]);
				}

				// altra autorita
				if (nmArray[i].getAutEstIdAutoritaEsterna() != null
						&& "C".equals(nmArray[i].getCodTipoNotifica())) {
					setRequestAttribute("altraautorita", nmArray[i]);
				}
			}
		}

		// POSIZIONE GIURIDICA
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = getPosizioneGiuridicaLuogoDetenzioneAltraCausa(
				idEvento, idFascicolo);
		setRequestAttribute("posizioneluogoaltra", pgldacm);

		// MAGISTRATO
		MagistratoModel mm = enm.getMagistrato();
		setRequestAttribute("magistrato", mm);

		// PENA RESIDUA
		PenaResiduaModel prm = this.getPenaResidua(idEvento, idFascicolo);
		setRequestAttribute("penaresidua", prm);

		// NUOVO FASCICOLO per PASSAGGIO di CLASSE
		BigDecimal idNuovoFascicolo = null;
		IFascicoloSiep ifs = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel fsmNew = new FascicoloSiepModel();
		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP)) {
			idNuovoFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
			fsmNew = ifs.ExRicercaFascicoloByKeyNoError(idNuovoFascicolo);
		} else {
			// Se vengo da elenco PM o dettaglio fascicolo il fascicolo è stato già iscritto
			Vector<FascicoloSiepModel> fascicoliCollegati = new Vector<>();
			FascicoloSiepModel fsmColl = new FascicoloSiepModel();
			fsmColl.setFasSieIdFascicoloSiep(idFascicolo);
			fsmColl.setChiaveUfficio(fsm.getChiaveUfficio());
			fsmColl.setSenIdSentenza(fsm.getSenIdSentenza());
			fsmColl.setDataIrrevocabilita(fsm.getDataIrrevocabilita());
			fascicoliCollegati = ifs.ExRicercaFascicoloSiep(fsmColl);
			if (!Utils.isNullObj(fascicoliCollegati) && !fascicoliCollegati.isEmpty())
				fsmNew = fascicoliCollegati.firstElement();
		}
		
		setRequestAttribute("fascicoloSiepModel", fsmNew);

		// pagina di ritorno
		return ICostantiArchiviazione.PG_LOAD_DETTAGLIO_PASSAGGIO_CLASSE;
	}

}