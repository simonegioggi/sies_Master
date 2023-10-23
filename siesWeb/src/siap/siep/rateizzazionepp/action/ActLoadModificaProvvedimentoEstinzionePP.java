package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
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
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per caricare la modifica del Provvedimento Estinzione Pena Pecuniaria
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
public class ActLoadModificaProvvedimentoEstinzionePP extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lId);
		setRequestAttribute("eventonotifica", lEveNotMod);

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

		// =======================================
		// Gli OI o Assimilabile e relative rate e bollettini
		// Attenzione serve per controllare che tutte le rate siano state pagate o meglio che l'importo dovuto
		// si stato pagato.
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		String[] listaCodici = new String[] { "0622", "1307", "1308" };
		Vector<EventoRateizzazionePPModel> listaOrdiniIngiunzione = irpp
				.exRicercaEventiRateizzazionePP(lFascMod.getIdFascicoloSiep(), listaCodici, false);

		IBollettinoPagopa lBollCtrl = SIEPLookupRemote.getBollettinoPagopaRemote();
		for (EventoRateizzazionePPModel evento : listaOrdiniIngiunzione) {
			Vector <RateizzazionePPModel> listaRateizzazioni = evento.getListaRateizzazioniPP();
			
			for (RateizzazionePPModel rata : listaRateizzazioni) {
				Vector <BollettinoPagopaModel> listaBollettini = lBollCtrl.ExRicercaBollettiniPagopaByIdRateizzazione(rata.getIdRateizzazionePP());
				rata.setListaBollettini(listaBollettini);
			}
		}
		
		setRequestAttribute("listaOrdiniIngiunzione", listaOrdiniIngiunzione);
		/*
		// Recupera l'ultimo OI o Assimilabile e relative rate 
		// Attenzione serve per controllare che tutte le rate siano state pagate o meglio che l'importo dovuto
		// si stato pagato.
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		String[] listaCodici = new String[] { "0622", "1307", "1308" };
		Vector<EventoRateizzazionePPModel> listaOrdiniIngiunzione = irpp
				.exRicercaEventiRateizzazionePP(lFascMod.getIdFascicoloSiep(), listaCodici, false);

		Vector <RateizzazionePPModel> listaRateizzazioni = null;
		if (listaOrdiniIngiunzione.size()>0) {
			listaRateizzazioni = listaOrdiniIngiunzione.elementAt(0).getListaRateizzazioniPP();
			setRequestAttribute("listaRateizzazioni", listaRateizzazioni);
		}
		*/

		
		
		
		
		
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
		MagistratoModel lMag = lEveNotMod.getMagistrato();
		MagistratoCompetenteMagistratoModel lMagModel = new MagistratoCompetenteMagistratoModel();
		lMagModel.setMagistrato(lMag);
		setRequestAttribute("magistrato", lMagModel);

		// Avvocati
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

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

		setRequestAttribute("modalita", "M");

		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_LOAD_INSERISCI_PROVVEDIMENTO_ESTINZIONE_PP;
	}

}