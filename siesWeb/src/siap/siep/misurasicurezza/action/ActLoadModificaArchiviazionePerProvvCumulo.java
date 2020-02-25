package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.Utils;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaArchiviazionePerProvvCumulo
 * </p>
 * <p>
 * Description: classe per Load Modifica di Archiviazione per provvedimento di cumulo
 * </p>
 */
public class ActLoadModificaArchiviazionePerProvvCumulo extends ActionSiap implements
		ICostantiMisuraSicurezza, ICostantiOrdineEsecuzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		BigDecimal idFascicoloSiep = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();
		
		UtenteModel lUtenteMod = getUtenteConnesso();

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

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByIdEvento(idEvento);
		if (pgldacm == null || (pgldacm != null && pgldacm.getPosizioneGiuridica() == null)) {
			siesLogger.info("Posizione Giuridica nulla!");
			pgldacm = ipg
					.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(idFascicoloSiep);
		}
		setRequestAttribute("posizioneluogoaltra", pgldacm);

		// Pena Complessiva
		IPenaComplessiva ipc = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel pcm = ipc.ExRicercaPenaComplessivaByIdFascicolo(idFascicoloSiep);
		if (pcm == null) {
			siesLogger.info("Pena Complessiva non presente.");
			// throw new SIEPException(SIEPException.USER_MESSAGE,
			// "Pena Complessiva non presente. Impossibile eseguire la richiesta.");
		} else {
			// ERGASTOLO
			String flagErgastolo = "N";
			// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
			if (pcm.getCodTipoPenaDetentiva() != null && pcm.getCodTipoPenaDetentiva() != "") {
				if (pcm.getCodTipoPenaDetentiva().equals("03"))
					flagErgastolo = "S";
				else if (pcm.getCodTipoPenaDetentiva().equals("04"))
					flagErgastolo = "D";
			}
			setRequestAttribute("flagergastolo", flagErgastolo);
		}

		IPenaResidua ipr = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel prm = ipr.ExRicercaPenaResiduaByIdEvento(idEvento);
		if (prm == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Sono nel caso della prima validazione!");
			prm = ipr.ExRicercaPenaResiduaCorrenteByFascicoloSiep(idFascicoloSiep);
		}
		setRequestAttribute("penaresidua", prm);

		// MAGISTRATO
		MagistratoModel mm = enm.getMagistrato();
		setRequestAttribute("magistrato", mm);

		String codSedeUffRecCred = "", descSedeUffRecCred = "", codTipoUffRecCred = "";
		String codSedeAutPol = "", descSedeAutPol = "", indAutPol = "", codTipoUffAutPol = "";
		String codSedeTDS = "", descSedeTDS = "";
		String codSedeUDS = "", descSedeUDS = "";
		String codSedeAltraAut = "", descSedeAltraAut = "", indAltraAut = "", codTipoUffAltraAut = "";
		String descAltraAut = "";

		NotificaModel[] nmArray = enm.getNotifiche();
		if (Utils.isPresent(nmArray)) {
			for (int i = 0; i < nmArray.length; i++) {
				NotificaModel nm = (NotificaModel) nmArray[i];
				if ("E".equals(nm.getCodTipoNotifica())) {
					codSedeUffRecCred = nm.getUffCodUfficio();
					descSedeUffRecCred = nm.getUfficio().getDescrComune();
					setRequestAttribute("codSedeUffRecCred", codSedeUffRecCred);
					setRequestAttribute("descSedeUffRecCred", descSedeUffRecCred);
					if (nm.getUfficio() != null)
						codTipoUffRecCred = nm.getUfficio().getCodTipoUfficio();
					else
						codTipoUffRecCred = nm.getUffCodUfficio();
					setRequestAttribute("codTipoUffRecCred", codTipoUffRecCred);
				} else if ("N".equals(nm.getCodTipoNotifica())) {
					if (nm.getAutoritaEsterna() != null) {
						descSedeAutPol = nm.getAutoritaEsterna().getDescrSede();
						codSedeAutPol = nm.getAutoritaEsterna().getCodSede();
						codTipoUffAutPol = nm.getAutoritaEsterna().getCodTipoAutorita();
					} else
						codTipoUffAutPol = nm.getUffCodUfficio();
					indAutPol = nm.getNote();
					setRequestAttribute("codSedeAutPol", codSedeAutPol);
					setRequestAttribute("descSedeAutPol", descSedeAutPol);
					setRequestAttribute("indAutPol", indAutPol);
					setRequestAttribute("codTipoUffAutPol", codTipoUffAutPol);
				} else if ("C".equals(nm.getCodTipoNotifica()) && nm.getUfficio() != null
						&& "TDS".equals(nm.getUfficio().getCodTipoUfficio())) {
					codSedeTDS = nm.getUffCodUfficio();
					descSedeTDS = nm.getUfficio().getDescrComune();
					setRequestAttribute("codSedeTDS", codSedeTDS);
					setRequestAttribute("descSedeTDS", descSedeTDS);
				} else if ("C".equals(nm.getCodTipoNotifica()) && nm.getUfficio() != null
						&& "UDS".equals(nm.getUfficio().getCodTipoUfficio())) {
					codSedeUDS = nm.getUffCodUfficio();
					descSedeUDS = nm.getUfficio().getDescrComune();
					setRequestAttribute("codSedeUDS", codSedeUDS);
					setRequestAttribute("descSedeUDS", descSedeUDS);
				} else if ("C".equals(nm.getCodTipoNotifica()) && nm.getAutoritaEsterna() != null) {
					codSedeAltraAut = nm.getAutoritaEsterna().getCodSede();
					descSedeAltraAut = nm.getAutoritaEsterna().getDescrSede();
					indAltraAut = nm.getNote();
					codTipoUffAltraAut = Utils.isPresent(nm.getUffCodUfficio()) ? nm.getUffCodUfficio() : nm
							.getAutoritaEsterna().getCodTipoAutorita();
					setRequestAttribute("codSedeAltraAut", codSedeAltraAut);
					setRequestAttribute("descSedeAltraAut", descSedeAltraAut);
					setRequestAttribute("indAltraAut", indAltraAut);
					setRequestAttribute("codTipoUffAltraAut", codTipoUffAltraAut);
				} else if ("C".equals(nm.getCodTipoNotifica()) && nm.getNote() != null
						&& nm.getAutoritaEsterna() == null) {
					descAltraAut = nm.getNote();
					setRequestAttribute("descAltraAut", descAltraAut);
				}
			}
		}

		Option tipoAutoritaPolizia = new Option(DecodificheManager.getInstance().getTipoAutorita(),
				codTipoUffAutPol);
		setRequestAttribute("tipoAutoritaPolizia", "" + tipoAutoritaPolizia);

		Option tipoAltraAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita(),
				codTipoUffAltraAut);
		setRequestAttribute("tipoAltraAutorita", "" + tipoAltraAutorita);

		Option tipoUfficio = new Option(DecodificheManager.getInstance().getTipoUfficio(), codTipoUffRecCred);
		tipoUfficio.setFilter(new String[] { "-", "DIB", "CAP", "TRIBSD" });
		setRequestAttribute("tipoUfficio", "" + tipoUfficio);
        
		setRequestAttribute("codUfficioUtente", lUtenteMod.getUfficioUtente().getCodUfficio());	
		
		// valore di ritorno
		return ICostantiMisuraSicurezza.PG_LOAD_MODIFICA_ARCHIVIAZIONE_PER_PROVV_CUMULO;
	} // Chiude processRequest
} // Chiude Classe