package siap.sico.magistratocompetente.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActModificaMultiplaMagistratoAssegnatario extends ActionSiap
		implements ICostantiMagistratoCompetente {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Effettua la modifica del magistrato assegnatario su n procedimenti contemporaneamente
	 */
	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del nuovo magistrato competente
		// ==========================================================================
		MagistratoCompetenteModel lMagCompModel = new MagistratoCompetenteModel();
		lMagCompModel
				.setMagCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		// lMagCompModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lMagCompModel
				.setDataInizio(getRequestDateParameter(ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO,
						ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO,
						ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO));
		lMagCompModel.setCodRuoloMagistrato("01");
		lMagCompModel.setDataInserimento(DateUtils.getSysDate());
		lMagCompModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		lMagCompModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lMagCompModel = " + lMagCompModel);

		IMagistratoCompetente lMagCompCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();

		// ==========================================================================
		// idFascicoloDaAggiornare = idFascicolo;anno/numero
		// ==========================================================================
		// 20251010 [SG]: paginata la ricerca
		Vector lListaAnnoNumero = new Vector();
		if (!isRequestParameterNullObj("selezionaAll") && isRequestChecked("selezionaAll")) {
			String lStato[] = { "02", "03" };
			Vector lListaProcedimenti = null;
			IFascicoloSiep ifs = SIEPLookupRemote.getFascicoloSiepRemote();
			lListaProcedimenti = ifs.ExRicercaFascicoliByMagistratoAssegnatario(
					getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO + "_OLD"),
					getCodUfficioUtenteConnesso(), lStato);
			String[] lListaIdFascicoli = new String[lListaProcedimenti.size()];
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Numero Fascicoli trasferiti = " + lListaProcedimenti.size());
			for (int i = 0; i < lListaProcedimenti.size(); i++) {
				String idFascicolo = ""
						+ ((FascicoloSiepModel) lListaProcedimenti.get(i)).getIdFascicoloSiep();
				String lAnnoNumero = "" + ((FascicoloSiepModel) lListaProcedimenti.get(i)).getChiaveAnno()
						+ "/" + ((FascicoloSiepModel) lListaProcedimenti.get(i)).getChiaveProgr();
				lListaIdFascicoli[i] = idFascicolo;
				lListaAnnoNumero.add(lAnnoNumero);
			}
			lMagCompCtrl.ExModificaMultiplaMagistratoCompetente(lMagCompModel, lListaIdFascicoli);
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
			lMagCompCtrl.ExModificaMultiplaMagistratoCompetente(lMagCompModel, lListaIdFascicoli);
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
				DateUtils.getDateToString(lMagCompModel.getDataInizio(), "dd/MM/yyyy"));

		return PG_ESITO_MODIFICAPROCEDIMENTI;
	}

}