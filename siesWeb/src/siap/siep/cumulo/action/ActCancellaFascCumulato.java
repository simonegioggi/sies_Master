package siap.siep.cumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacumulo.controller.IPenaCumulo;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.ulterioresanzionecumulo.controller.IUlterioreSanzioneCumulo;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActCancellaFascCumulato
 * </p>
 * <p>
 * Description: Azione che consente di Eliminare dal Cumulo il Fascicolo cumulato selezionato.
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActCancellaFascCumulato extends ActionSiap implements ICostantiSentenza {

	public String processRequest() throws Exception {

		BigDecimal lFascID = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();

		CumuloModel lCumMod = new CumuloModel();

		lCumMod.setSenIdSentenza(getRequestBigDecimalParameter(CAMPO_ID_SENTENZA));

		// chiama il controller
		ICumulo lCtrlCumulo = SIEPLookupRemote.getCumuloRemote();

		String flagValidato = "N";
		lCumMod = lCtrlCumulo.ExRicercaCumuloByIdSentenza(lCumMod.getSenIdSentenza(), flagValidato);

		// se il Fascicolo Cumulato è stato validato non può essere cancellato
		if (lCumMod == null) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Non si può procedere con la cancellazione, Provvedimento validato!");
		}

		// se il Fascicolo Cumulato è "Primo Cumulo" e quest'ultimo ha una
		// pena cumulo associata, prima di eliminare il record recupero il cumulo
		// successivo (per data inserimento) non validato, imposto il flag "Primo Cumulo"
		// uguale a 'P' e associo a quest'ultimo la pena cumulo.

		// se il fascicolo Cumulato che viene cancellato è unico, procedo con la cancellazione
		// della pena cumulo, se esiste, imposto a null il campo FLAG_CUMULANTE sulla tabella
		// FASCICOLO_SIEP, elimino l'evento ad esso collegato, ed infine cancello il cumulo.

		if (lCumMod != null && lCumMod.getPrimoCumulo() != null && lCumMod.getPrimoCumulo().equals("P")) {

			// ============================================================================
			// Recupero il cumulo non validato (meno recente), quest'ultimo deve diventare
			// "Primo Cumulo" e ad esso devo associare la Pena Cumulo, se esiste
			// ============================================================================
			CumuloModel lNextPrimoCum = new CumuloModel();
			Vector cumuli = lCtrlCumulo.ExRicercaFascicoliCumulobyIdFascicoloSiepFlagValidato(
					lCumMod.getFasSieIdFascicoloSiep());
			if (cumuli.size() > 0) {
				lNextPrimoCum = ((CumuloModel) (cumuli).get(0));

				lNextPrimoCum.setCodOperatoreAggiornamento(getCodUtenteConnesso());
				lNextPrimoCum.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
				lNextPrimoCum.setDataAggiornamento(DateUtils.getSysDate());

				// ============================================================================
				// Recupero la PENA_CUMULO associata al cumulo
				// ============================================================================
				PenaCumuloModel lPenCumMod = new PenaCumuloModel();
				IPenaCumulo lCtrlPena = SIEPLookupRemote.getPenaCumuloRemote();
				lPenCumMod = lCtrlPena.ExRicercaPenaCumuloByIdCumulo(lCumMod.getIdCumulo());

				// ============================================================================
				// Recupero le Ulteriori Sanzioni associate al Cumulo
				// ============================================================================
				IUlterioreSanzioneCumulo lUlt = SIEPLookupRemote.getUlterioreSanzioneCumuloRemote();
				Vector lUltSanzCumulo = lUlt.ExRicercaUlterioreSanzioneCumuloByFascicoloIDCumul0(lFascID,
						lCumMod.getIdCumulo());

				lCtrlCumulo.ExCancellaFascCumulatoPrimoCumulo(lCumMod, lNextPrimoCum, lPenCumMod,
						lUltSanzCumulo);
			} else {
				// non sono presenti altri Fascicoli Cumulati
				lCtrlCumulo.ExCancellaFascCumulatoUnico(lCumMod, getCodUtenteConnesso(),
						getCodUfficioUtenteConnesso());
			}
		} // lCumMod.getPrimoCumulo().equals("P")
		else {
			lCtrlCumulo.ExCancellaFascCumulato(lCumMod);
		}

		// setta la risposta nella request
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Cancellazione Avvenuta Correttamente!");

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.siep.cumulo.action.ActLoadElencoFascicoliCoinvoltiCumulo");
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW
	}

}