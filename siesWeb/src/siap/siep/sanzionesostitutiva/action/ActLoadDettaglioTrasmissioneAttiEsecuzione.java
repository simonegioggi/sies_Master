package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.Utils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.residenza.controller.IResidenza;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * Classe Action per il caricamento del dettaglio trasmissione atti per l'esecuzione
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadDettaglioTrasmissioneAttiEsecuzione extends ActSIESDettaglioProvvedimento
		implements ICostantiSanzioneSostitutiva {

	// info per il log
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Evento
		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enm = ie.ExRicercaEventoNotificaByKey(idEvento);
		setRequestAttribute("evento", enm);

		NotificaModel nm = new NotificaModel();
		if (!Utils.isNullObj(enm) && !Utils.isNullObj(enm.getNotifiche())) {
			for (int i = 0; i < enm.getNotifiche().length; i++) {
				NotificaModel nmfor = enm.getNotifiche()[i];
				if (Utils.isPresent(nmfor.getUffCodUfficio())) {
					nm = nmfor;
					break;
				}
			}
		}
		setRequestAttribute("notificaUDS", nm);

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = getPosizioneGiuridicaLuogoDetenzioneAltraCausa(
				idEvento, fsm.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", pgldacm);

		// residenza
		IResidenza ir = SICOLookupRemote.getResidenzaRemote();
		Vector residenze = ir.ExRicercaResidenzeByIdFascicolo(fsm.getIdFascicoloSiep());
		ResidenzaAssociataModel ram = new ResidenzaAssociataModel();
		if (residenze != null && !residenze.isEmpty())
			ram = (ResidenzaAssociataModel) residenze.get(0);
		setRequestAttribute("residenzaassociata", ram);

		// Pena residua
		PenaResiduaModel prm = getPenaResidua(idEvento, fsm.getIdFascicoloSiep());

		// ricerco le sanzione sostitutive
		ISanzioneSostitutiva iss = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		SanzioneSostResiduaModel ssrm = iss.getUltimaSSResidua(fsm.getIdFascicoloSiep(), "S");
		// Inserisco la SS residua nel model della PR
		if (ssrm == null || ssrm.getIdSanzioneSostResidua() == null)
			ssrm = iss.getUltimaSSResidua(fsm.getIdFascicoloSiep(), "N");
		prm.setSanzSostResidua(ssrm);
		setRequestAttribute("penaresidua", prm);

		// ricerca penacomplessiva e sanzione sostitutiva
		IPenaComplessiva ipc = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaSanzioneSostitutivaModel pcssm = ipc
				.ExRicercaPenaComplessivaPenaSostitutivaByIdFascicoloSiep(fsm.getIdFascicoloSiep(),
						"('G', 'H')"); // (Semiliberta', Detenzione Domiciliare)
		setRequestAttribute("penaCompPenaSost", pcssm);

		// misure cautelari
		IMisuraCautelare imc = SIEPLookupRemote.getMisuraCautelareRemote();
		Vector misureCautelari = imc.ExRicercaMisureCautelariByIdFascicolo(fsm.getIdFascicoloSiep());
		setRequestAttribute("misurecautelari", misureCautelari);

		// Ricerca Magistrato
		IMagistrato im = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel mm = im.ExRicercaMagistratoByCod(enm.getEvento().getCodMagistrato());
		setRequestAttribute("magistrato", mm);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_LOAD_DETTAGLIO_TRASMISSIONE_ATTI_ESECUZIONE;
	}

}