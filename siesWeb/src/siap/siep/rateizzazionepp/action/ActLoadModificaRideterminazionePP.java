package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per caricare la modifica della Rideterminazione Pena Pecuniaria
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadModificaRideterminazionePP extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enm = ie.ExRicercaEventoNotificaByKey(idEvento);
		setRequestAttribute("eventonotifica", enm);

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Controlli preliminari all'inserimento di un nuovo evento
		if (fsm.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr()
					+ " non è stato Validato. Impossibile inserire una Rideterminazione Pena Pecuniaria!");
			rt.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			// pagina di ritorno
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

		// Ricerca i pagamenti per id Fascicolo
		Vector<RateizzazionePPModel> listaRateizzazioni = new Vector<>();
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		listaRateizzazioni = irpp.exRicercaRateizzazioniByIdFasc(fsm.getIdFascicoloSiep());

		if (listaRateizzazioni.size() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Non e' stato inserito un metodo di pagamento: unica rata o rateizzazione. Impossibile procedere");
		}
		setRequestAttribute("listaRateizzazioni", listaRateizzazioni);

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
			// pagina di ritorno
			return IWebConstants.PG_MESSAGE;
		}

		// imposto valore nella request
		setRequestAttribute("posizioneluogoaltra", pgldacm);

		// Ricerco il civilmente Obbligato se esiste
		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		Vector<CivilmenteObbligatoModel> coms = ico
				.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		setRequestAttribute("civilmenteObbligati", coms);

		// Magistrato
		MagistratoModel mm = enm.getMagistrato();
		MagistratoCompetenteMagistratoModel mcmm = new MagistratoCompetenteMagistratoModel();
		mcmm.setMagistrato(mm);
		setRequestAttribute("magistrato", mcmm);

		// Avvocati
		IAvvocato ia = SIEPLookupRemote.getAvvocatoRemote();
		Vector avvocati = ia.ExRicercaAvvocatiByFascicolo(fsm.getIdFascicoloSiep());
		setRequestAttribute("avvocati", avvocati);

		// Autorità esterna
		Option tipoAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
		// Verifico se sovrescrivere l'auturità esterna
		if (fsm.getFlagAltraCausa() != null && fsm.getFlagAltraCausa().equals("S")) {
			// modifica relativa al tipo istituto
			if (pgldacm.getAltraCausa() != null
					&& (pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("23")
							|| pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("78")
							|| pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("79")
							|| pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("80")
							|| pgldacm.getAltraCausa().getCodTipoPosGiuridica().equals("81"))) {
				tipoAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
			} else {
				if (pgldacm.getAltraCausa() != null
						&& pgldacm.getAltraCausa().getIstitutoDetenzione() != null)
					tipoAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							pgldacm.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
			}
		} else {
			if (pgldacm.getPosizioneGiuridica().isLibero()
					|| pgldacm.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
					|| pgldacm.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")) {
				tipoAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
			} else {
				if (pgldacm.getLuogoDetenzione() != null
						&& pgldacm.getLuogoDetenzione().getIstitutoDetenzione() != null)
					tipoAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							pgldacm.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
			}
		}
		tipoAutorita.setSelected("-");
		setRequestAttribute("autoritaEsternaE", "" + tipoAutorita);

		// Autorita Notifica Avvocato
		Option autoritaEsternaN = new Option(DecodificheManager.getInstance().getTipoAutorita(), "C0");
		setRequestAttribute("autoritaEsternaN", "" + autoritaEsternaN);

		// Autorita Notifica Civilmente Obbligati
		Option autoritaEsternaCivilObb = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaCivilObb", "" + autoritaEsternaCivilObb);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		setRequestAttribute("modalita", "M");
		return PG_LOAD_INSERISCI_RIDETERMINAZIONE_PP;
	}

}