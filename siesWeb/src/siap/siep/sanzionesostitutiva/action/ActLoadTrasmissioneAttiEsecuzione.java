package siap.siep.sanzionesostitutiva.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.residenza.controller.IResidenza;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
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
 * Classe Action per il caricamento della trasmissione atti per l'esecuzione
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadTrasmissioneAttiEsecuzione extends ActionSiap implements ICostantiSanzioneSostitutiva {

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

		if (isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		isFascicoloSiepDiCompetenza();

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		isEventoNonValidato();

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

		if (notEsistePosizioneGiuridica(pgldacm))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", pgldacm);

		// Controllo Esistenza pena residua per quel fascicolo
		PenaResiduaModel prm = new PenaResiduaModel();
		IPenaResidua ipr = SIEPLookupRemote.getPenaResiduaRemote();
		prm = ipr.ExRicercaPenaResiduaCorrenteByFascicoloSiep(fsm.getIdFascicoloSiep());

		if (notEsistePenaResiduaCorrenteByFascicoloSiep(prm))
			return IWebConstants.PG_MESSAGE;

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

		// DESTINATARI
		Option option = new Option(DecodificheManager.getInstance().getTipoAutoritaPolizia(), "-");
		setRequestAttribute("tipoAutoritaPolizia", "" + option);
		// AVVOCATO
		IAvvocato ia = SIEPLookupRemote.getAvvocatoRemote();
		Vector avvocati = ia.ExRicercaAvvocatiByFascicolo(fsm.getIdFascicoloSiep());
		setRequestAttribute("avvocati", avvocati);
		// Autorita Notifica Avvocato
		option = new Option(DecodificheManager.getInstance().getTipoAutorita(), "C0");
		setRequestAttribute("tipoAutoritaC0", "" + option);
		// Altro destinatario: tutti i records TIPO_AUTORITA
		option = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("tipoAutoritaAll", "" + option);

		// ricerca penacomplessiva e sanzione sostitutiva
		IPenaComplessiva ipc = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaSanzioneSostitutivaModel pcssm = ipc
				.ExRicercaPenaComplessivaPenaSostitutivaByIdFascicoloSiep(fsm.getIdFascicoloSiep(),
						"('G', 'H')"); // (Semiliberta', Detenzione Domiciliare)
		if (Utils.isNullObj(pcssm) || Utils.isNullObj(pcssm.getSanzioneSostitutiva())
				|| Utils.isNullObj(pcssm.getSanzioneSostitutiva().getIdSanzioneSostitutiva())) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Procedimento N." + fsm.getChiaveAnno() + "/" + fsm.getChiaveProgr()
							+ " privo di Semilibert&agrave; o Detenzione Domiciliare Sostitutiva!");
			rt.setAction("siap.siep.sanzionesostitutiva.action.ActGestioneAltreSanzioni&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			return IWebConstants.PG_MESSAGE;
		}
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

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_LOAD_TRASMISSIONE_ATTI_ESECUZIONE;
	}

}