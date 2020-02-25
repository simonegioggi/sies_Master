package siap.siep.annotazionemanuale.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel;
import siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaSigeModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioAnnotazioneManuale
 * </p>
 * <p>
 * Description: Classe padre delle classi di caricamento del dettaglio delle annotazioni manuali presenti nel
 * package siap.siep.calcolopena:<br>
 * <br>
 * 
 * Decisioni del GE:<br>
 * siap.siep.calcolopena.ActLoadDettaglioAnnotazioniAmnistiaIndulto<br>
 * siap.siep.calcolopena.ActLoadDettaglioAnnotazioniDepenalizzazione<br>
 * siap.siep.calcolopena.ActLoadDettaglioAnnotazioniIncostituzionalita<br>
 * <br>
 * 
 * Rideterminazione Pena:<br>
 * siap.siep.calcolopena.ActLoadDettaglioAnnotazioniSenzaTitolo<br>
 * siap.siep.calcolopena.ActLoadDettaglioAnnotazioniStessoTitolo<br>
 * siap.siep.calcolopena.ActLoadDettaglioAnnotazioniAltroTitolo<br>
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
public class ActLoadDettaglioAnnotazioneManuale extends ActionSiap implements ICostantiAnnotazioneManuale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * TODO questa classe andrebbe spostata nel package del clacolo pena per coerenza visto che le classi
	 * figlie si trovano tutte in quel package (o vice versa)
	 */
	/*****************************************************************************
	 * Metodo che carica l'elenco delle annotazioni manuali del tipo specificato in input (lTipoAnnotazione) e
	 * non ancora validate.
	 *
	 * Viene chiamata in fase di insert per verificare se esistono annotazioni già inserite. In questo caso
	 * viene richiamata la form di dettaglio, in caso contrario la form di inserimento.
	 * 
	 * Recupera l'ultima annotazione (la più recente) e la mette sulla request.
	 * 
	 * Recupera l'ultimo evento di tipo ordinanza e relativa annotazione e la mette sulla request. (serve solo
	 * nel caso di decisioni del GE)
	 * 
	 * 
	 * @param lMotivoProvvedimento
	 *            - utilizzato nella ricerca dell'evento associato all'annotazione manuale
	 * @param lTipoAnnotazione
	 *            -
	 * @param lFlagPage
	 *            - parametro passato alle jsp (settato sulla request)
	 * @return
	 * @throws F3BException
	 *             - SIEPException.EX_NOT_FOUND - se l'annotazione non esiste
	 ************************************************************************** */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected String loadDettaglio(String lMotivoProvvedimento, String lTipoAnnotazione, String lFlagPage)
			throws F3BException {

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		setRequestAttribute("MotivoProvvedimento", lMotivoProvvedimento);

		// ==========================================================================
		// Carico l'elenco delle annotazioni manuali del lTipoAnnotazione in input e
		// ancora NON validate (n.b. ne può esistere più di una tasto 'Aggiungi' delle
		// form di dettaglio e inoltre potrebbero essere presenti quelle caricate
		// da Rideterminazione Pena/Richieste al GE e ferme a sistema in attesa
		// di essere prese in considerazione)
		// ==========================================================================
		IAnnotazioneManuale lCtrlAnnMan = SIEPLookupRemote.getAnnotazioneManualeRemote();

		Vector lListAnnMan = new Vector();
		Vector lListAnnManTemp = null;

		lListAnnManTemp = lCtrlAnnMan.ExRicercaAnnotazioniManualiByIdFascicoloNonRichiesteTipoAnn(
				lIdFascicolo, lTipoAnnotazione, "N");

		// Luigi 16-6-2009. Eliminazione dalla lista le Annotazioni Manuali inserite da altro Ufficio
		if (lListAnnManTemp != null && !lListAnnManTemp.isEmpty()) {
			for (Iterator lIter = lListAnnManTemp.iterator(); lIter.hasNext();) {
				AnnotazioneManualeModel lAnnMan = (AnnotazioneManualeModel) lIter.next();
				if (lAnnMan.getCodUfficioInserimento().equalsIgnoreCase(getCodUfficioUtenteConnesso()))
					lListAnnMan.add(lAnnMan);
			}
		}

		// ==========================================================================
		// Nel caso delle decisioni del GE recupero l'eventuale Ordinanza del GE
		// e annotazione associata
		// ==========================================================================
		EventoModel lEveMod = new EventoModel();

		lEveMod.setCodTipoEvento("01");
		lEveMod.setCodTipoProvvedimento("03"); // ordinanza
		lEveMod.setCodMotivo(lMotivoProvvedimento);
		lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		// Luigi 15-06-2009
		lEveMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// Nel caso di AMINISTIA_INDULTO prima Ricerca Ordinanza GE emessa da Ufficio SIGE
		AnnotazioneOrdinanzaSigeModel lAnnOrdSigeMod = null;

		if (lTipoAnnotazione.equalsIgnoreCase(AMINISTIA_INDULTO)) {
			lEveMod.setFlagDocumentoRegistrato("S");
			lAnnOrdSigeMod = lCtrlAnnMan.ExRicercannotazioneManualeOrdinanzaSigeByIdFascicolo(lEveMod);
			lEveMod.setFlagDocumentoRegistrato(null);
		}

		if (lAnnOrdSigeMod != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lAnnOrdSigeMod NON NULL ");

			lAnnOrdSigeMod.setAnnoNumGeAnnotazione();
			setRequestAttribute("AnnotazioneOrdinanza", lAnnOrdSigeMod);
			// lListAnnMan.add(lAnnOrdSigeMod.getAnnotazioneManuale());
		} else {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lAnnOrdSigeMod  NULL ");

			// Recupera l'ultimo evento di tipo ordinanza e relativa Annotazione
			AnnotazioneOrdinanzaModel lAnnOrdMod = lCtrlAnnMan
					.ExRicercaUltimaAnnotazioneManualeOrdinanzaByIdFascicolo(lEveMod);
			setRequestAttribute("AnnotazioneOrdinanza", lAnnOrdMod);
		}

		if (lListAnnMan == null || lListAnnMan.isEmpty())
			throw new SIEPException(SIEPException.EX_NOT_FOUND, "Nessun elememento trovato.");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ListaAnnotazioni non vuota! ");

		// Passo alla form la lista delle annotazione ancora da elaborare
		setRequestAttribute("ListaAnnotazioni", lListAnnMan);

		// AnnotazioneManualeModel lAnnManIns = lCtrlAnnMan.ExRicercaAnnotazioneManualeByKey(lIdAnnMan);

		// Considera l'ultima inserita
		AnnotazioneManualeModel lAnnManIns = (AnnotazioneManualeModel) lListAnnMan
				.get((lListAnnMan.size() - 1));
		setRequestAttribute("AnnotazioneManualeInserita", lAnnManIns);

		/******************************************************************************/
		setRequestAttribute("lFlagPage", lFlagPage);
		setRequestAttribute("lPageGE", lFlagPage);
		// per vedere se arrivo da computo altro titolo stesso o senza

		// Nel caso ritorna null, tutti i controlli sono passati
		return null;
	}

}