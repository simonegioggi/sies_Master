package siap.siep.richiesta.action;

/**
 * <p>Title: ActLoadInserisciOrdineScarcerazioneProvv</p>
 * <p>Description: Azione Load inserimento Ordine Scarcerazione Provvisorio</p>
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
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadInserisciOrdineScarcerazioneProvv extends ActionSiap implements ICostantiRichiesta {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// ==========================================================================
		// n.b la chiamata può provenire dalla finestra delle stampe delle richieste
		// o dalla finestra delle Ricerche per Applicazioni Benefici
		// Nel primo caso viene passato l'id dell'annotazione manuale
		// Nel secondo caso viene passato l'id del fascicolo
		// ==========================================================================

		// Nel Caso la funzione venga richiamata per CAMPO_ID_FASCICOLO_SIEP
		// (es. da Ricerca Indulto sulla tabella RISULTATO_RICERCA)
		// cerca il fascicolo per Id e lo carica in sessione
		if (!this.isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP)) {
			BigDecimal lIdFascicoloSieop = getRequestBigDecimalParameter(
					ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);

			IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

			FascicoloSiepModel lFasMod = null;
			lFasMod = lCtrl.ExRicercaFascicoloByKey(lIdFascicoloSieop);

			if (lFasMod == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non presente");

			// Inserisce nella session il fascicolo (contenente Soggetto e Sentenza)
			setSessionAttribute("fascicolo", lFasMod);
			// Gianluca 01/03 ....inserisco anche Soggetto e Sentenza del fascicolo in sessione
			setSessionAttribute("soggetto", lFasMod.getSoggetto());
			setSessionAttribute("sentenza", lFasMod.getSentenza());
		}

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		this.isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// SentenzaModel lSentMod = lFascMod.getSentenza();
		Vector reati = new Vector();
		EventoModel lEveMod = null;
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

		/********* Posizione Giuridica ***************/
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosMod = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPosMod = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		setRequestAttribute("posizioneluogoaltra", lPosMod);

		/*************************************************************************/

		// ==========================================================================
		// Recupero l'annotazione se presente
		// ==========================================================================
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		BigDecimal lIdAnnotazioneManuale = null;
		Vector lAnnMod = new Vector();

		if (isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE)
				|| (getRequestStringParameter(
						ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE) != null
						&& getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE)
								.equals(""))) {
			// ******************************* Ricerca evento legato all'anotazione manuale
			// **********************************/
			// Manca l'id dell'annotazione ricerco l'ultimo evento a cui più essere
			// agganciato l'evento fittizio 0122
			EventoModel lEveModRic = new EventoModel();
			lEveModRic.setCodTipoEvento("01");
			lEveModRic.setCodMotivo("0122");
			lEveModRic.setFasSieIdFascicoloSiep(lIdFascicolo);

			try {
				// STUB 12-10-2005 REWORK STATO ESECUZIONE.
				/*
				 * String lFalgDocReg = null; Vector evento =
				 * lCtrlEve.ExRicercaEventoTipoEveTipoProvMot(lEveModRic,lFalgDocReg); if(evento.size()>0)
				 * lEveMod = (EventoModel)evento.get(0);
				 */
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Ricerca evento 0122");
				String[] lMotivi = { "0122" };
				String[] lProvv = { "04", "26" };
				lEveMod = lCtrlEve.ExRicercaEventoPerMotivoPerProvv(lMotivi, lProvv, lEveModRic);
			} catch (Exception e) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("eccezione" + e);
			}

			// Vector lAnnMod = new Vector();

			/*
			 * if (lEveMod == null) { throw new SIEPException(SIEPException.USER_MESSAGE,
			 * "Non esiste l' Ex artt. 174 c.p. e 672 comma 3° c.p.p."); }
			 */
			// Se esiste l'evento fittizio recupero le annotazioni legate all'evento
			if (lEveMod != null) {
				this.setRequestAttribute("evento", lEveMod);

				IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
				lAnnMod = lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lEveMod.getIdEvento());

				this.setRequestAttribute("annotazioneManuale", lAnnMod);
			}
		} else {
			// L'id dell'annotazione viene passato sulla chiamata, recupero direttamente
			// l'annotazione
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

		/**************************** ricerca reati *****************************************/
		for (int i = 0; i < lAnnMod.size(); i++) {
			lAnnModel = (AnnotazioneManualeModel) lAnnMod.get(i);
			if (lAnnModel.getReaIdReato() != null) {
				IReato lCtrlReato = SIEPLookupRemote.getReatoRemote();
				ReatoModel lReatoModel = lCtrlReato.ExRicercaReatoByKey(lAnnModel.getReaIdReato());
				reati.add(lReatoModel);
				this.setRequestAttribute("reati", reati);
			}
		}
		/***********************************************************************/

		/*
		 * *****************************Ricerca ordine esecuzione per codice 9
		 * ********************************************************** / EventoModel lEveOESet = new
		 * EventoModel(); lEveOESet.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 * lEveOESet.setCodTipoEvento("01"); lEveOESet.setCodTipoProvvedimento("06"); Vector eventi = new
		 * Vector(); try { eventi = lCtrlEve.ExRicercaEvento(lEveOESet); } catch (Exception ex) { // [FT] -
		 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.info("Eccezione " + ex); }
		 * 
		 * EventoModel lEveOE = new EventoModel(); if (eventi.size() > 0) { lEveOE = (EventoModel)
		 * eventi.get(0); if (lEveOE != null) { this.setRequestAttribute("lEveOE", lEveOE); } } /
		 ****************************************************************************************/

		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		/*
		 * Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(),
		 * lSentMod.getCodTipoAutoritaEmittente()); setRequestAttribute("tipoUfficio", "" + lOption);
		 * 
		 * lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		 * setRequestAttribute("autoritaEsterna", "" + lOption);
		 * 
		 * setRequestAttribute("testo", testo);
		 * 
		 * this.setRequestAttribute("codice", codice); this.setRequestAttribute("sentenza",lSentMod);
		 */

		// Autorità esterna
		Option lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutorita", "" + lOptionAutorita);

		if (!isRequestParameterNullObj("codice")) {
			String codiceMotivo = this.getRequestStringParameter("codice");
			this.setRequestAttribute("codicemotivo", codiceMotivo);
		}

		return PG_LOAD_ORDINE_SCARCERAZIONE_PROVV;
	}

}