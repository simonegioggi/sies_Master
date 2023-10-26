package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
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
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe action per il caricamento dell'inserimento della Trasmissione Atti Conversione
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadInserisciTrasmissioneAttiConversione extends ActionSiap
		implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Controlli preliminari all'inserimento di un nuovo evento
		if (fsm.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr()
					+ " non è stato Validato. Impossibile inserire una Rideterminzaione Pena Pecuniaria!");
			rt.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

			return IWebConstants.PG_MESSAGE;
		}

		isFascicoloSiepDiCompetenza();

		if (fsm.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr() + " Il fascicolo risulta Definito. Impossibile procedere!");
			rt.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			return IWebConstants.PG_MESSAGE;
		}

		// controllo se evento non sia validato
		isEventoNonValidato();

		// Ricerca i pagamenti per id Fascicolo
		Vector<RateizzazionePPModel> listaRateizzazioni = new Vector<>();
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		listaRateizzazioni = irpp.exRicercaMancatiPagamentiUnicaSoluzione(fsm.getIdFascicoloSiep());
		if (listaRateizzazioni.size() == 0) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Non e' stato trovato alcun pagamento in unica rata. " + "Impossibile procedere! "
							+ "Si reindirizza alla pagina di Gestione Modalita' Pagamento.");
			rt.setAction("siap.siep.rateizzazionepp.action.ActLoadDettagloRateizzazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			return IWebConstants.PG_MESSAGE;
		}

		// Sezione con l'importo da pagare
		BigDecimal importoDaPagare = listaRateizzazioni.firstElement().getImportoDaPagare();
		setRequestAttribute("importoDaPagare", importoDaPagare);

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		pgldacm = ipg.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				fsm.getIdFascicoloSiep());
		if (pgldacm == null || pgldacm.getPosizioneGiuridica() == null) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			rt.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

			return IWebConstants.PG_MESSAGE;
		}
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
		IMagistratoCompetente imc = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel mcmm = imc
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(fsm.getIdFascicoloSiep());
		setRequestAttribute("magistrato", mcmm);

		// destinatari
		Option o = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice());
		o.setFilter(new String[] { "-", "UDS", "UDSM" });
		setRequestAttribute("tipoUDS", "" + o);

		// modalita'
		setRequestAttribute("modalita", "I");

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_LOAD_INSERISCI_TRASMISSIONE_ATTI_CONVERSIONE;
	}

}