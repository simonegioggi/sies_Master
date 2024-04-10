package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.lock.model.LockModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.residenza.controller.IResidenza;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il caricamento della modifica della trasmissione atti per l'esecuzione
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadModificaTrasmissioneAttiEsecuzione extends ActionSiap
		implements ICostantiSanzioneSostitutiva {

	// info per il log
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di caricamento della form d'inserimento della trasmissione atti esecuzione
	 *
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// recupero l'ID Evento dalla form
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lm = lockIfNotLocked("evento", "" + idEvento, getCodUtenteConnesso());
		if (lm != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"La " + lm.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		// EVENTO
		EventoNotificaModel enm = new EventoNotificaModel();
		IEvento ie = SICOLookupRemote.getEventoRemote();
		enm = ie.ExRicercaEventoNotificaByKey(idEvento);
		setRequestAttribute("eventonotifica", enm);

		// recupero il fascicolo dalla sessione
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// residenza
		IResidenza ir = SICOLookupRemote.getResidenzaRemote();
		Vector residenze = ir.ExRicercaResidenzeByIdFascicolo(fsm.getIdFascicoloSiep());
		ResidenzaAssociataModel ram = new ResidenzaAssociataModel();
		if (residenze != null && !residenze.isEmpty())
			ram = (ResidenzaAssociataModel) residenze.get(0);
		setRequestAttribute("residenzaassociata", ram);

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				fsm.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", pgldacm);

		// Controllo Esistenza pena residua per quel fascicolo
		PenaResiduaModel prm = new PenaResiduaModel();
		IPenaResidua ipr = SIEPLookupRemote.getPenaResiduaRemote();
		prm = ipr.ExRicercaPenaResiduaCorrenteByFascicoloSiep(fsm.getIdFascicoloSiep());
		// ricerco le sanzione sostitutive
		ISanzioneSostitutiva iss = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		SanzioneSostResiduaModel ssrm = iss.getUltimaSSResidua(fsm.getIdFascicoloSiep(), "S");
		// Inserisco la SS residua nel model della PR
		if (ssrm == null || ssrm.getIdSanzioneSostResidua() == null)
			ssrm = iss.getUltimaSSResidua(fsm.getIdFascicoloSiep(), "N");
		prm.setSanzSostResidua(ssrm);
		setRequestAttribute("penaresidua", prm);

		// solo se provengo da annotazione provvedimento
		if (!isRequestParameterNullObj("lAnnotazione")) {
			setRequestAttribute("lAnnotazione", getRequestStringParameter("lAnnotazione"));
			setRequestAttribute("lSedeUfficio", getRequestStringParameter("lSedeUfficio"));
		}

		// AVVOCATO
		IAvvocato ia = SIEPLookupRemote.getAvvocatoRemote();
		Vector avvocati = ia.ExRicercaAvvocatiByFascicolo(fsm.getIdFascicoloSiep());
		setRequestAttribute("avvocati", avvocati);
		// DESTINATARI
		// Forze di polizia per notifica condannato
		Option option = new Option(DecodificheManager.getInstance().getTipoAutoritaPolizia(), "-");
		setRequestAttribute("tipoAutoritaPolizia", "" + option);
		// Autorita Notifica Avvocato
		option = new Option(DecodificheManager.getInstance().getTipoAutorita(), "C0");
		setRequestAttribute("tipoAutoritaC0", "" + option);
		// Altro destinatario: tutti i records TIPO_AUTORITA
		option = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("tipoAutoritaAll", "" + option);

		NotificaModel[] nmArray = enm.getNotifiche();
		for (int i = 0; i < nmArray.length; i++) {
			NotificaModel nm = nmArray[i];
			if ("MS".equals(nm.getCodTipoNotifica()) || "MM".equals(nm.getCodTipoNotifica())) {
				UfficioModel um = getUfficioByCodUfficio(nm.getUffCodUfficio());
				setRequestAttribute("lSedeUfficio", um.getDescrComune());
				break;
			}
		}

		// ricerca penacomplessiva e sanzione sostitutiva
		IPenaComplessiva ipc = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaSanzioneSostitutivaModel pcssm = ipc
				.ExRicercaPenaComplessivaPenaSostitutivaByIdFascicoloSiep(fsm.getIdFascicoloSiep(),
						"('G', 'H')"); // (Semiliberta', Detenzione Domiciliare)
		// if (Utils.isNullObj(pcssm) || Utils.isNullObj(pcssm.getSanzioneSostitutiva())
		// || Utils.isNullObj(pcssm.getSanzioneSostitutiva().getIdSanzioneSostitutiva())) {
		// RedirectTo rt = new RedirectTo();
		// rt.setPage(IWebConstants.PG_MAIN);
		// setRequestAttribute(IWebConstants.MESSAGE_TEXT,
		// "Procedimento N." + fsm.getChiaveAnno() + "/" + fsm.getChiaveProgr()
		// + " privo di Semilibert&agrave; o Detenzione Domiciliare Sostitutiva!");
		// rt.setAction("siap.siep.sanzionesostitutiva.action.ActGestioneAltreSanzioni&"
		// + ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
		// return IWebConstants.PG_MESSAGE;
		// }
		setRequestAttribute("penaCompPenaSost", pcssm);

		// misure cautelari
		IMisuraCautelare imc = SIEPLookupRemote.getMisuraCautelareRemote();
		Vector misureCautelari = imc.ExRicercaMisureCautelariByIdFascicolo(fsm.getIdFascicoloSiep());
		setRequestAttribute("misurecautelari", misureCautelari);

		// ricerca magistrato competente
		IMagistratoCompetente imagc = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel mcmm = imagc
				.ExRicercaMagistratoCompetenteByFascicolo(fsm.getIdFascicoloSiep());
		if (mcmm != null)
			setRequestAttribute("magistratocompetente", mcmm);

		// impostazione di modifica
		setRequestAttribute("modalita", "M");

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_LOAD_TRASMISSIONE_ATTI_ESECUZIONE;
	}

}