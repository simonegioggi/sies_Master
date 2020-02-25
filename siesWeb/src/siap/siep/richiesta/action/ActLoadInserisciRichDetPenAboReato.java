package siap.siep.richiesta.action;

/**
 * <p>Title: ActLoadInserisciRichDetPenAboReato</p>
 * <p>Description: Azione Load della Richiesta Depenalizzazione/Incostituzionalità
 *    nel caso di Cumulo</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadInserisciRichDetPenAboReato extends ActionSiap implements ICostantiRichiesta {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di caricamento della form d'inserimento del provvedimento
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		this.isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();
		SentenzaModel lSentMod = lFascMod.getSentenza();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("sentenza--->" + lSentMod);

		Vector reati = new Vector();
		AnnotazioneManualeModel lAnnModel = new AnnotazioneManualeModel();

		// Controllo Validazione Fascicolo
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Controllo Fascicolo definito
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidato();

		// Posizione Giuridica
		IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosMod = lCtrlPos
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);
		this.setRequestAttribute("posizioneGiuridica", lPosMod);

		// Codice del Motivo
		String codice = "0210";

		// Ricerca evento legato all'anotazione manuale
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		BigDecimal lIdAnnotazioneManuale = null;
		Vector lAnnMod = new Vector();

		// ==========================================================================
		// Se non è stata passata l'annotazione, ricerco l'ultimo evento di Depenalizzazione
		// o Incostituzionalità inserito per poter recuperare l'annotazione
		// ==========================================================================
		if (isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE)
				|| (getRequestStringParameter(
						ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE) != null
						&& getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE)
								.equals(""))) {
			EventoModel lEveMod = new EventoModel();
			EventoModel lEveModRic = new EventoModel();
			EventoModel lEveModDep = null;
			EventoModel lEveModIncost = null;
			// STUB 29/09/2005 REWORK STATO ESECUZIONE
			// Vector evento = null;
			lEveModRic.setFasSieIdFascicoloSiep(lIdFascicolo);
			lEveModRic.setCodTipoProvvedimento("04");
			lEveModRic.setCodTipoEvento("01");
			// depenalizzazione
			lEveModRic.setCodMotivo("0210");

			// Ricerco prima la Depenalizzazione
			try {
				// STUB 11/10/2005 REWORK STATO ESECUZIONE
				/*
				 * String lFlagDocReg = null; evento =
				 * lCtrlEve.ExRicercaEventoTipoEveTipoProvMot(lEveModRic,lFlagDocReg); if(evento.size()>0)
				 * lEveModDep = (EventoModel)evento.get(0);
				 */
				String[] codMot = { "0210" };
				String[] tipoProvv = { "04", "26" };
				lEveModDep = lCtrlEve.ExRicercaEventoPerMotivoPerProvv(codMot, tipoProvv, lEveModRic);
			} catch (Exception e) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("eccezione" + e);
			}

			// Se non trovata provo con incostituzionalità
			if (lEveModDep == null) {
				// incostituzionalità
				codice = "0211";

				lEveModRic.setCodMotivo("0211");
				// STUB 11/10/2005 REWORK STATO ESECUZIONE
				/*
				 * Vector eventoIncost = lCtrlEve.ExRicercaEvento(lEveModRic); if(eventoIncost.size()>0)
				 * lEveModIncost = (EventoModel)eventoIncost.get(0);
				 */
				String[] codMot = { "0211" };
				String[] tipoProvv = { "04", "26" };
				lEveModIncost = lCtrlEve.ExRicercaEventoPerMotivoPerProvv(codMot, tipoProvv, lEveModRic);

				if (lEveModIncost != null) {
					lEveMod = lEveModIncost;
					this.setRequestAttribute("tipo", "I");
				} else {
					throw new SIEPException(SIEPException.USER_MESSAGE,
							"Non esiste la Richiesta di determinazione della pena revoca sentenza per abolizione del reato ex.artt.671 cp e 673 cpp");
				}
			} else {
				lEveMod = lEveModDep;
				this.setRequestAttribute("tipo", "D");
			}

			if (lEveMod != null) {
				this.setRequestAttribute("evento", lEveMod);

				IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
				lAnnMod = lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lEveMod.getIdEvento());

				this.setRequestAttribute("annotazioneManuale", lAnnMod);
			}
		} else {
			lIdAnnotazioneManuale = getRequestBigDecimalParameter(
					ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE);

			IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
			AnnotazioneManualeModel aAnnManMod = lCtrlAnn
					.ExRicercaAnnotazioneManualeByKey(lIdAnnotazioneManuale);

			if (aAnnManMod != null) {
				lAnnMod.add(aAnnManMod);
			}

			this.setRequestAttribute("annotazioneManuale", lAnnMod);
		}

		// Ricerca reati
		IReato lCtrlReato = SIEPLookupRemote.getReatoRemote();

		for (int i = 0; i < lAnnMod.size(); i++) {
			lAnnModel = (AnnotazioneManualeModel) lAnnMod.get(i);
			if (lAnnModel.getReaIdReato() != null) {
				ReatoModel lReatoModel = lCtrlReato.ExRicercaReatoByKey(lAnnModel.getReaIdReato());
				reati.add(lReatoModel);

				this.setRequestAttribute("reati", reati);
			}
		}

		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(),
				lSentMod.getCodTipoAutoritaEmittente());
		setRequestAttribute("tipoUfficio", "" + lOption);

		this.setRequestAttribute("codice", codice);
		this.setRequestAttribute("sentenza", lSentMod);

		return PG_LOAD_RICHIESTA_DET_PEN_ABO_REATO;
	}
}