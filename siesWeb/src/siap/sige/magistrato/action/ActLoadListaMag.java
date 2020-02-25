package siap.sige.magistrato.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.web.ActionSige;

public class ActLoadListaMag extends ActionSige implements ICostantiMagistrato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");
		// MagistratoAssegnatarioModel lMagAss = null;
		// try {
		// lMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario();
		// } catch (Exception e) {
		//
		// }
		// IMagistrato lMagCtrl = SIGELookupRemote.getMagistratoRemote();
		Option lOption = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()));
		lOption.setSelected("-");

		// intervento per richiesta nunzia per 11.2.1 NON BISOGNA PRENDERE QUELLA DEL MAGISTRATO!!!
		// if (lMagAss != null) {
		// // 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un
		// // ufficio differente da quello in cui ha delle udienze poichè trasferito
		// MagistratoModel lMagMod = lMagCtrl.ExRicercaMagistratoByCod(lMagAss.getMagCodMagistrato(),
		// getCodUfficioUtenteConnesso());
		// if (lMagMod.getMagistratoSezioni().length > 0) {
		// BigDecimal idSezMag = lMagMod.getMagistratoSezioni()[0].getSezIdSezione();
		// if (idSezMag != null) {
		// lOption.setSelected(idSezMag.toString());
		// }
		// }
		// }
		setRequestAttribute("elencoSezioni", lOption.toString());

		// ISezione lSezCtrl = SIGELookupRemote.getSezioneRemote();
		// Vector lSezioni = lSezCtrl.ExRicercaSezioneByCodUfficio(getCodUfficioUtenteConnesso());

		// imposta la risposta la risposta nella request
		// setRequestAttribute("SezioniList",lSezioni);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		// restituisce la jsp di VIEW
		return PG_FILTRA_MAG_ASSEGNAZIONE_LISTA;
	}

}