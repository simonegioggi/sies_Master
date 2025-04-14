package siap.siep.misuraalternativa.action;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;

/**
 * ActLoadInserisciMASemiliberta - Classe Action per la load inserisci di Concessione Semiliberta'
 *
 * @version 1.0
 */
public class ActLoadInserisciMASemiliberta extends ActConcessione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		// passo la posizione attuale per vedere se esiste il verbale o no
		String lRitorno = getConcessione("14");
		if (!lRitorno.equals(""))
			return lRitorno;

		// setto il campo codice motivo
		Collection lmotivo = DecodificheManager.getInstance().getMotivoProvvedimentoMASemiL();
		String lDesMotivo = "";
		String lCodiceMotivo = "";
		if (lmotivo != null && !lmotivo.isEmpty()) {
			Iterator lIter = lmotivo.iterator();
			if (lIter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) lIter.next();
				lDesMotivo = lDecMod.getDescription();
				lCodiceMotivo = lDecMod.getCode();
			}
		}

		setRequestAttribute("motivoProvv", lDesMotivo);
		setRequestAttribute("codicemotivo", lCodiceMotivo);

		setRequestAttribute("tipoMisura", "SEMILIBERTA");

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", getFiltroMinorenni());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// pagina di ritorno
		return PG_LOAD_INSERISCI_MA_CONCESSIONE;
	}

}