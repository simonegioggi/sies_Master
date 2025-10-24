package siap.sico.magistratocompetente.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

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

		lMagCompModel.setMagCodMagistrato(
				this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		// lMagCompModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lMagCompModel.setDataInizio(
				this.getRequestDateParameter(ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO,
						ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO,
						ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO));
		lMagCompModel.setCodRuoloMagistrato("01");

		lMagCompModel.setDataInserimento(DateUtils.getSysDate());
		lMagCompModel.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lMagCompModel.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lMagCompModel = " + lMagCompModel);

		// ==========================================================================
		// idFascicoloDaAggiornare = idFascicolo;anno/numero
		// ==========================================================================
		String[] lListaFascicoli = this.getRequestStringParameters("idFascicoloDaAggiornare");
		String[] lListaIdFascicoli = new String[lListaFascicoli.length];

		Vector lListaAnnoNumero = new Vector();

		for (int i = 0; i < lListaFascicoli.length; i++) {
			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			//// LogF3B.getLogger()
			// siesLogger.debug("id_fascicolo = "+lListaFascicoli[i]);
			String idFascicolo = null;
			String lAnnoNumero = null;
			idFascicolo = lListaFascicoli[i].substring(0, lListaFascicoli[i].indexOf(';'));
			lAnnoNumero = lListaFascicoli[i].substring(lListaFascicoli[i].indexOf(';') + 1,
					lListaFascicoli[i].length());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lListaFascicoli = " + lListaFascicoli[i]);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("idFascicolo = " + idFascicolo);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lAnnoNumero = " + lAnnoNumero);

			lListaIdFascicoli[i] = idFascicolo;
			lListaAnnoNumero.add(lAnnoNumero);
		}

		// if (1==1) return "";

		IMagistratoCompetente lMagCompCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		lMagCompCtrl.ExModificaMultiplaMagistratoCompetente(lMagCompModel, lListaIdFascicoli);

		// ==========================================================================
		//
		// ==========================================================================
		// String[] lListaFascicoli = this.getRequestStringParameters("AnnoNumeroFascicolo");

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