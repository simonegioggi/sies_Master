package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
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
 * MEV_2023-13: aggiunta classe per il caricamento dell'inserimento dell'ordine di ingiunzione
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActLoadInserisciOrdineIngiunzione extends ActionSiap implements ICostantiSanzioneSostitutiva {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// Controlli preliminari all'inserimento di un nuovo evento
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " non è stato Validato. Impossibile inserire un ordine di Ingiunzione!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		isFascicoloSiepDiCompetenza();

		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " Il fascicolo risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidato();
		// =======================================

		// Ricerca i pagamenti per id Fascicolo
		Vector<RateizzazionePPModel> listaRateizzazioni = new Vector<>();
		IRateizzazionePP lRateCTRL = SIEPLookupRemote.getRateizzazionePPRemote();
		listaRateizzazioni = lRateCTRL.exRicercaRateizzazioniByIdFasc(lFascMod.getIdFascicoloSiep());

		if (listaRateizzazioni.size() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Non e' stato inserito un metodo di pagamento: unica rata o rateizzazione. Impossibile procedere");
		}
		
        IEvento lCtrl = SICOLookupRemote.getEventoRemote();
        Hashtable <BigDecimal, EventoNotificaModel> listaOrdiniIngiunzione = new Hashtable <BigDecimal, EventoNotificaModel>();
        for (RateizzazionePPModel rata : listaRateizzazioni) {
            if (rata.getEveIdEvento()!=null)  {
                if (listaOrdiniIngiunzione.get(rata.getEveIdEvento())!=null) {
                    rata.setOrdineIngiunzione(listaOrdiniIngiunzione.get(rata.getEveIdEvento()));
                }
                else {                                                
                    EventoNotificaModel lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey (rata.getEveIdEvento());
                    rata.setOrdineIngiunzione(lEveNotMod);
                    listaOrdiniIngiunzione.put(lEveNotMod.getEvento().getIdEvento(), lEveNotMod);
                }
            }
        }
            
		setRequestAttribute("listaRateizzazioni", listaRateizzazioni);

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());
		if (lPos == null || lPos.getPosizioneGiuridica() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}
		setRequestAttribute("posizioneluogoaltra", lPos);

		// Ricerco il civilmente Obbligato se esiste
		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		Vector<CivilmenteObbligatoModel> coms = ico
				.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("civilmenteObbligati", coms);

		// Magistrato
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// Avvocati
		try {
	        IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		    Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
	        setRequestAttribute("avvocati", lAvvocati);
		}
		catch (SIEPException e) {
		    // nessun avvocato trovato
            RedirectTo lRedirigi = new RedirectTo();
            lRedirigi.setPage(IWebConstants.PG_MAIN);
            setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
                    + "/" + lFascMod.getChiaveProgr() + " non è stato associato alcun avvocato.");
            lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&"
                    + ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
            setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

            return IWebConstants.PG_MESSAGE;		    
		}

		// Autorità esterna
		Option lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		// Verifico se sovrescrivere l'auturità esterna
		if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {
			// modifica relativa al tipo istituto
			if (lPos.getAltraCausa() != null && (lPos.getAltraCausa().getCodTipoPosGiuridica().equals("23")
					|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("78")
					|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("79")
					|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("80")
					|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("81"))) {
				lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
			} else {
				if (lPos.getAltraCausa() != null && lPos.getAltraCausa().getIstitutoDetenzione() != null)
					lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							lPos.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
			}
		} else {
			if (lPos.getPosizioneGiuridica().isLibero()
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")) {
				lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
			} else {
				if (lPos.getLuogoDetenzione() != null
						&& lPos.getLuogoDetenzione().getIstitutoDetenzione() != null)
					lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							lPos.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
			}
		}
		lOptionAutoritaEsternaE.setSelected("-");
		setRequestAttribute("autoritaEsternaE", "" + lOptionAutoritaEsternaE);

		// Autorita Notifica Avvocato
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "C0");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		// Autorita Notifica Civilmente Obbligati
		Option lOptCivilObb = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaCivilObb", "" + lOptCivilObb);

		// info per il log
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		setRequestAttribute("modalita", "I");
		return PG_LOAD_INSERISCI_ORDINE_INGIUNZIONE;
	}

}