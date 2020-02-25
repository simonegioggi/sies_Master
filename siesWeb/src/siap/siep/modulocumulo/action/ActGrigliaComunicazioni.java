package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Carica la griglia con le comunicazioi
 *
 * @author d.fiorletta
 *
 */
public class ActGrigliaComunicazioni extends ActionModuloCumulo implements ICostantiModuloCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		if (isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
				|| getRequestBigDecimalParameter(
						ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) == null) {
			siesLogger.debug("Istruttoria non selezionata, provo a recuperare l'unica chiusa se presente");
			FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

			IstruttoriaCumuloModel lIstMod = new IstruttoriaCumuloModel();
			lIstMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			// lIstMod.setFlagStato("")

			IIstruttoriaCumulo lCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
			Vector<IstruttoriaCumuloModel> lVect = lCtrl.ExRicercaIstruttoriaCumulo(lIstMod);

			// Se presente una sola istruttoria chiusa la preseleziono.
			BigDecimal lIdIstruttoria = null;
			int contaChiuse = 0;
			int contaAperte = 0;

			for (IstruttoriaCumuloModel lIstruttoria : lVect) {
				if (ICostantiIstruttoriaCumulo.FLAG_STATO_CHIUSA.equals(lIstruttoria.getFlagStato())) {
					lIdIstruttoria = lIstruttoria.getIdIstruttoriaCumulo();
					contaChiuse++;
				} else if (ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstruttoria.getFlagStato())) {
					contaAperte++;
				}
			}

			if (contaChiuse == 1 && contaAperte == 0) {
				siesLogger.debug(
						"Presente una sola istruttoria chiusa [id: " + lIdIstruttoria + "], la precarico.");
				super.getDatiIstruttoria(lIdIstruttoria);
				super.getDatiFinaliCumuloAggregato(lIdIstruttoria);
			} else if ((contaChiuse + contaAperte) > 1) {
				// presente almeno una chiusa ed eventualmente chuise + aperte, l'utente
				// deve scegliere
				RedirectTo lRedirigi = new RedirectTo();

				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessuna istruttoria selezionata");
				lRedirigi.setAction("siap.siep.istruttoriacumulo.action.ActRicercaIstruttoriaCumulo");
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				throw new F3BException(F3BException.USER_MESSAGE, "Nessuna istruttoria selezionata");
			} else {
				// non ho istruttorie chiuse da selezionare
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Nessuna istruttoria presente su cui emettere comunicazione");
				return IWebConstants.PG_MESSAGE;
			}
		} else {
			super.getDatiIstruttoria();
			super.getDatiFinaliCumuloAggregato();
		}

    siesLogger.debug("--XXX-- Esecuzione ActGrigliaComunicazioni punto 2 ");

		// ==========================================================================
		// Recupero le comunicazioni già inviate per il provvedimento di cumulo/istruttoria
		// ==========================================================================

		setRequestAttribute("ListaComunicazioni", new Vector());

		return PG_GRIGLIA_COMUNICAZIONI;
	}

}