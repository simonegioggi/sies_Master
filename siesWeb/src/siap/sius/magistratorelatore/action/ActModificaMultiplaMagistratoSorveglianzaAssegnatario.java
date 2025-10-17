package siap.sius.magistratorelatore.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.util.SIUSLookupRemote;

public class ActModificaMultiplaMagistratoSorveglianzaAssegnatario extends ActionSiap
		implements ICostantiMagistratoRelatore {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Effettua la modifica del magistrato assegnatario su n procedimenti contemporaneamente
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del nuovo magistrato relatore
		// ==========================================================================
		MagistratoRelatoreModel lMagRelModel = new MagistratoRelatoreModel();
		lMagRelModel.setMagCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lMagRelModel.setDataInizio(getRequestDateParameter(ICostantiMagistratoRelatore.CAMPO_ANNO_DATA_INIZIO,
						ICostantiMagistratoRelatore.CAMPO_MESE_DATA_INIZIO,
						ICostantiMagistratoRelatore.CAMPO_GIORNO_DATA_INIZIO));
		lMagRelModel.setCodRuoloMagistrato("02");

		lMagRelModel.setDataInserimento(DateUtils.getSysDate());
		lMagRelModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		lMagRelModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lMagRelModel = " + lMagRelModel);

		IMagistratoRelatore lMagRelCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();

		// ==========================================================================
		// idFascicoloDaAggiornare = idFascicolo;anno/numero
		// ==========================================================================
		// 20251010 [SG]: paginata la ricerca
		Vector lListaAnnoNumero = new Vector();
		if (!isRequestParameterNullObj("selezionaAll") && isRequestChecked("selezionaAll")) {
			String lStato[] = { "02", "03", "10" };
			Vector lListaProcedimenti = null;
			IFascicoloSius ifs = SIUSLookupRemote.getFascicoloSiusRemote();
			lListaProcedimenti = ifs.ExRicercaFascicoliByMagistratoSorvAssegnatario(
					getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO + "_OLD"),
					getCodUfficioUtenteConnesso(), lStato);
			String[] lListaIdFascicoli = new String[lListaProcedimenti.size()];
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Numero Fascicoli trasferiti = " + lListaProcedimenti.size());
			for (int i = 0; i < lListaProcedimenti.size(); i++) {
				String idFascicolo = ""
						+ ((FascicoloSiusModel) lListaProcedimenti.get(i)).getIdFascicoloSius();
				String lAnnoNumero = "" + ((FascicoloSiusModel) lListaProcedimenti.get(i)).getChiaveAnno()
						+ "/" + ((FascicoloSiusModel) lListaProcedimenti.get(i)).getChiaveProgr();
				lListaIdFascicoli[i] = idFascicolo;
				lListaAnnoNumero.add(lAnnoNumero);
			}
			lMagRelCtrl.ExModificaMultiplaMagistratoRelatore(lMagRelModel, lListaIdFascicoli);
		} else {
			String[] lListaFascicoli = getRequestStringParameters("idFascicoloDaAggiornare");
		String[] lListaIdFascicoli = new String[lListaFascicoli.length];
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Numero Fascicoli trasferiti = " + lListaFascicoli.length);
		for (int i = 0; i < lListaFascicoli.length; i++) {
			String idFascicolo = null;
			String lAnnoNumero = null;
			idFascicolo = lListaFascicoli[i].substring(0, lListaFascicoli[i].indexOf(';'));
			lAnnoNumero = lListaFascicoli[i].substring(lListaFascicoli[i].indexOf(';') + 1,
					lListaFascicoli[i].length());
			lListaIdFascicoli[i] = idFascicolo;
			lListaAnnoNumero.add(lAnnoNumero);
		}
		lMagRelCtrl.ExModificaMultiplaMagistratoRelatore(lMagRelModel, lListaIdFascicoli);
		}

		MagistratoModel lMagistratoOld = new MagistratoModel();
		lMagistratoOld.setCodMagistrato(
				getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO + "_OLD"));
		lMagistratoOld.setCognome(getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME + "_OLD"));
		lMagistratoOld.setNome(getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME + "_OLD"));

		MagistratoModel lMagistratoNew = new MagistratoModel();
		lMagistratoNew.setCognome(getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME));
		lMagistratoNew.setNome(getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME));

		setRequestAttribute("aVecchioMagistrato", lMagistratoOld);
		setRequestAttribute("aNuovoMagistrato", lMagistratoNew);
		setRequestAttribute("aListaProcedimenti", lListaAnnoNumero);
		setRequestAttribute("aDataCompetenza",
				DateUtils.getDateToString(lMagRelModel.getDataInizio(), "dd/MM/yyyy"));

		// pagina di ritorno
		return PG_ESITO_MODIFICAPROCEDIMENTI_SORVEGLIANZA;
	}

}