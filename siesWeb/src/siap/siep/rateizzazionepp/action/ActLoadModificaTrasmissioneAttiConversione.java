package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.residenza.controller.IResidenza;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per caricare la modifica della Trasmissione Atti Conversione
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadModificaTrasmissioneAttiConversione extends ActionSiap
		implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enm = ie.ExRicercaEventoNotificaByKey(idEvento);
		setRequestAttribute("eventonotifica", enm);

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Ricerca i pagamenti per id Fascicolo
		Vector<RateizzazionePPModel> listaRateizzazioni = new Vector<>();
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		// listaRateizzazioni = irpp.exRicercaRateizzazioniByIdFasc(fsm.getIdFascicoloSiep());
		listaRateizzazioni = irpp.exRicercaRateizzazioniByIdEvento(idEvento);
		setRequestAttribute("listaRateizzazioni", listaRateizzazioni);

		// Sezione con l'importo da pagare
		BigDecimal importoDaPagare = listaRateizzazioni.firstElement().getImportoDaPagare();
		setRequestAttribute("importoDaPagare", importoDaPagare);

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				fsm.getIdFascicoloSiep());

		// imposto valore nella request
		setRequestAttribute("posizioneluogoaltra", pgldacm);

		// residenza
		IResidenza ir = SICOLookupRemote.getResidenzaRemote();
		Vector<ResidenzaAssociataModel> residenze = ir
				.ExRicercaResidenzeByIdFascicolo(fsm.getIdFascicoloSiep());
		ResidenzaAssociataModel ram = new ResidenzaAssociataModel();
		if (residenze != null && !residenze.isEmpty())
			ram = residenze.get(0);

		// domicilio
		IFascicoloSiep ifs = SIEPLookupRemote.getFascicoloSiepRemote();
		ResidenzaAssociataModel dram = ifs.ExRicercaDomicilioFascicoloSiepCorrente(fsm.getIdFascicoloSiep());

		String domicilio = new String("");
		if (ram.getResidenza() != null || dram.getResidenza() != null) {
			if (pgldacm.getPosizioneGiuridica().isLibero() && ram != null && ram.getResidenza() != null) {
				domicilio += ram.getResidenza().getDescrComune() + " (" + ram.getResidenza().getCodProvincia()
						+ ") - " + ram.getResidenza().getIndirizzo();
				if (pgldacm.getPosizioneGiuridica().isLibero() && dram != null
						&& dram.getResidenza() != null) {
					domicilio += dram.getResidenza().getIndirizzo() + " "
							+ dram.getResidenza().getDescrComune();
				}
			}
		}
		setRequestAttribute("domicilio", domicilio);

		// Magistrato
		MagistratoModel mm = enm.getMagistrato();
		MagistratoCompetenteMagistratoModel mcmm = new MagistratoCompetenteMagistratoModel();
		mcmm.setMagistrato(mm);
		setRequestAttribute("magistrato", mcmm);

		// destinatari
		Option o = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice());
		o.setFilter(new String[] { "-", "UDS", "UDSM" });
		o.setSelected(enm.getNotifiche()[0].getCodUffUdsUdsm());
		setRequestAttribute("tipoUDS", "" + o);

		setRequestAttribute("comuneUDS", enm.getNotifiche()[0].getUfficio().getDescrComune());

		setRequestAttribute("modalita", "M");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_LOAD_INSERISCI_TRASMISSIONE_ATTI_CONVERSIONE;
	}

}