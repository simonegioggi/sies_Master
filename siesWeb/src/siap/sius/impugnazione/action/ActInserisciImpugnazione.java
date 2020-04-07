package siap.sius.impugnazione.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.avvocatura.action.ICostantiAvvisiAvvocato;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActInserisciImpugnazione
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Impugnazione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciImpugnazione extends ActionSiap implements ICostantiImpugnazione {

	/**
	 * Azione di Inserimento del Impugnazione
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		ImpugnazioneModel lImpMod = new ImpugnazioneModel();

		// Recupero il Fascicolo SIUS dalla sessione
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		// Preleva dalla request l'IdEvento.
		String lIdEvento = this.getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		lImpMod.setCodOperatoreInserimento(lCodiceOperatore);
		lImpMod.setCodUfficioInserimento(lCodiceUfficio);
		lImpMod.setDataInserimento(DateUtils.getSysDate());
		// Il campo Progr_S7 viene impostato nel controller.
		lImpMod.setAnnoS7(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lImpMod.setCodTipoImpugnazione(getRequestStringParameter(CAMPO_COD_TIPO_IMPUGNAZIONE));
		lImpMod.setSoggettoImpugnante(getRequestStringParameter(CAMPO_SOGGETTO_IMPUGNANTE));

		// Modifca del 16/11/2016 MEV_50
		lImpMod.setDescrizioneAltro(getStringParameter(CAMPO_DESCRIZIONE_ALTRO));

		lImpMod.setDataRicorso(getRequestDateParameter(CAMPO_ANNO_DATA_RICORSO, CAMPO_MESE_DATA_RICORSO,
				CAMPO_GIORNO_DATA_RICORSO));
		lImpMod.setDataArrivoCancelleria(getRequestDateParameter(CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA,
				CAMPO_MESE_DATA_ARRIVO_CANCELLERIA, CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA));
		lImpMod.setDataTrasmissioneAtti(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				CAMPO_MESE_DATA_TRASMISSIONE_ATTI, CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));
		lImpMod.setCodAutoritaDestinataria(getRequestStringParameter(CAMPO_COD_AUTORITA_DESTINATARIA));
		lImpMod.setDataDecisione(getRequestDateParameter(CAMPO_ANNO_DATA_DECISIONE,
				CAMPO_MESE_DATA_DECISIONE, CAMPO_GIORNO_DATA_DECISIONE));
		lImpMod.setCodTenoreDecisione(getRequestStringParameter(CAMPO_COD_TENORE_DECISIONE));
		lImpMod.setDataRestituzioneAtti(getRequestDateParameter(CAMPO_ANNO_DATA_RESTITUZIONE_ATTI,
				CAMPO_MESE_DATA_RESTITUZIONE_ATTI, CAMPO_GIORNO_DATA_RESTITUZIONE_ATTI));
		lImpMod.setAnnotazione(getRequestStringParameter(CAMPO_NOTE)); // Da inserire nella form
		// I campi depOpidDepositoOrdinanzaPc e depDecIdDepositoDecreto vengono impostati nel controller.
		// if( !isRequestAttributeNullObj(CAMPO_FLAG_SOSP_ESEC ) )
		lImpMod.setFlagSospEsec(getRequestStringParameter(CAMPO_FLAG_SOSP_ESEC)); // 08/05/2007 Sospensione
																					// Esecuzione
																					// Provvedimento

		// *********************************************
		// MEV_AVVOCATURA - INIZIO
		// **********************************************
		UtenteModel user = (UtenteModel) getSessionAttribute("UtenteConnesso");
		// Ufficio Emittente
		String ufficio = user.getUfficioUtente().getDescrTipoUfficio() + " di "
				+ user.getUfficioUtente().getDescrComune();

		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoModel eveMod = lCtrlEve.ExRicercaEventoByKey(new BigDecimal(lIdEvento));

		// Ricerca avvocati assegnati al fascicolo
		IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector<AvvocatoSiusModel> avvocati = null;
		avvocati = lAvvCtrl.ExRicercaAvvocatiByFascicoloNoError(lFasGPMod.getFascicoloSiusModel()
				.getIdFascicoloSius());

		// Recupero i dati del soggetto dalla sessione @emma 25/08/2016 - avvocatura
		String cognomeSoggetto = "";
		String nomeSoggetto = "";
		/* 
		 * ISSUE MEV : segnalazione Maffucci oggetto mail: SIUS Avvocati Di pre-esercizio - SIES MO di Roma:
		 * Eliminato recupero dalla session del soggetto che viene inserito nella tabella 
		 * degli avvisi_avvocato 
		 * Numero MEV : MEV_20
		 * Autore    : monica
		 * Data      : 16/mar/2020
		 * Branch    : MEV_20 
		 */
		/*if (!isSessionAttributeNullObj("soggetto")) {
			SoggettoModel datiSoggetto = (SoggettoModel) getSessionAttribute("soggetto");
			cognomeSoggetto = datiSoggetto.getCognome();
			nomeSoggetto = datiSoggetto.getNome();
		} else*/
		//***** FINE INTERVENTO MEV_20*****//
		if (lFasGPMod != null && lFasGPMod.getFascicoloSiusModel() != null) {
			// provo a verificare se è presente nell'oggetto FascicoloGPModel
			cognomeSoggetto = lFasGPMod.getFascicoloSiusModel().getSoggetto() != null ? lFasGPMod
					.getFascicoloSiusModel().getSoggetto().getCognome() : "";
			// EC@19/05/2017: correzione su nomeSoggetto. Inserivamo il nome sul cognome!
			nomeSoggetto = lFasGPMod.getFascicoloSiusModel().getSoggetto() != null ? lFasGPMod
					.getFascicoloSiusModel().getSoggetto().getNome() : "";
		} else {
			// devo procedere con una ricerca del soggetto per chiave soggetto
			BigDecimal idSoggetto = lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto();
			ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
			SoggettoModel s = lSogCtrl.ExRicercaSoggettoByKey(idSoggetto);
			cognomeSoggetto = s.getCognome();
			nomeSoggetto = s.getNome();
		}

		Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato = new Vector<AvvisiAvvocatoModel>();

		AvvisiAvvocatoModel lAvvisiAvvModel = null;

		Iterator itxAvv = avvocati.iterator();
		while (itxAvv.hasNext()) {
			lAvvisiAvvModel = new AvvisiAvvocatoModel();

			AvvocatoSiusModel lAvv = (AvvocatoSiusModel) itxAvv.next();

			lAvvisiAvvModel.setIdAvvocato(lAvv.getAvvocato().getIdAvvocato());
			lAvvisiAvvModel.setCognomeSoggeto(cognomeSoggetto);
			lAvvisiAvvModel.setNomeSoggetto(nomeSoggetto);
			// lAvvisiAvvModel.setIdProvvedimento(); // viene settato nel controller
			lAvvisiAvvModel.setDescProvvedimento(eveMod.getDescrTipoProvvedimento());
			lAvvisiAvvModel.setUfficioEmittente(ufficio);
			lAvvisiAvvModel.setTestoAvviso(ICostantiAvvisiAvvocato.CONTENUTO_INSERIMENTO_RICORSO);
			lAvvisiAvvModel.setFlagVisualizzazione("N");
			lAvvisiAvvModel.setCodOperatoreInserimento(user.getUserId());
			lAvvisiAvvModel.setCodUfficioInserimento(user.getUfficioUtente().getCodUfficio());
			lAvvisiAvvModel.setDataInserimento(DateUtils.getSysDate());

			lAvvvisiAvvocato.add(lAvvisiAvvModel);

		}

		// ***********************************************************************
		// MEV_AVVOCATURA aggiunto parametro lAvvvisiAvvocato
		// ***********************************************************************
		IImpugnazione lCtrl = SIUSLookupRemote.getImpugnazioneRemote();
		ImpugnazioneModel llImpModRet = lCtrl.ExInserisciImpugnazione(lImpMod, new BigDecimal(lIdEvento),
				lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(),
				ICostantiImpugnazione.COD_TIPO_SCADENZARIO, lAvvvisiAvvocato);
		// ImpugnazioneModel llImpModRet = lCtrl.ExInserisciImpugnazione(lImpMod, new BigDecimal(lIdEvento),
		// lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(),
		// ICostantiImpugnazione.COD_TIPO_SCADENZARIO);

		// *********************************************
		// MEV_AVVOCATURA - FINE
		// **********************************************

		// Setta la risposta nella request.
		setRequestAttribute("impugnazione", llImpModRet);
		setRequestAttribute("modalita", "M");

		// Imposta ComboBOX Tipo Ricorso.
		Option lOption = null;
		if (Utils.isNullObj(lImpMod))
			lOption = new Option(DecodificheManager.getInstance().getTipoRicorso());
		else
			lOption = new Option(DecodificheManager.getInstance().getTipoRicorso(),
					lImpMod.getCodTipoImpugnazione());
		setRequestAttribute("tipoRicorso", "" + lOption);

		// Imposta ComboBOX Soggetto Impugnante.
		if (Utils.isNullObj(lImpMod))
			lOption = new Option(DecodificheManager.getInstance().getSoggettoImpugnante());
		else
			lOption = new Option(DecodificheManager.getInstance().getSoggettoImpugnante(),
					lImpMod.getSoggettoImpugnante());
		setRequestAttribute("soggettoImpugnante", "" + lOption);

		// Imposta ComboBOX Tipo DecisioneCassazione.
		if (Utils.isNullObj(lImpMod))
			lOption = new Option(DecodificheManager.getInstance().getTenoreDecisioneRicorso());
		else
			lOption = new Option(DecodificheManager.getInstance().getTenoreDecisioneRicorso(),
					lImpMod.getCodTenoreDecisione());

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		if (strCodTipoUfficio.compareTo("TDS") == 0) {
			String[] lFilterTDR = { "01", "02", "03", "04", "05", "06" };
			lOption.setFilter(lFilterTDR);
		}
		setRequestAttribute("tenoreDecisioneRicorso", "" + lOption);

		// Imposta ComboBOX Autorita Destinataria
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		String[] lFilter = { "CSS" };
		lOption.setFilter(lFilter);
		setRequestAttribute("ListaUffici", "" + lOption);

		// Prepara la pagina di destinazione, puntando all'azione di dettaglio.
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sius.impugnazione.action.ActLoadDettaglioImpugnazione");
		lPage.setParameter(ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE, "" + llImpModRet.getIdImpugnazione());

		return "" + lPage;
	}

	private String getStringParameter(String paramName) throws F3BException {

		if (isRequestParameterNullObj(paramName))
			return null;
		String val = super.getRequestStringParameter(paramName);
		return val;
	}

}