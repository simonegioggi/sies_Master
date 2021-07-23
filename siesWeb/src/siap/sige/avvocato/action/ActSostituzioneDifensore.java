package siap.sige.avvocato.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione;

public class ActSostituzioneDifensore extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Sostituzione dell' Avvocato assegnato al Fascicolo SIGE
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		Vector lVectPrec = null;
		Vector lVectRic = null;

		// Recupero i dati del Nuovo avvocato se selezionato, altrimenti l'id coincide
		// con il vecchio avvocato
		BigDecimal id = getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO);
		AvvocatoModel lAvvModRic = new AvvocatoModel();
		lAvvModRic.setIdAvvocato(id);
		lVectRic = lCtrl.ExRicercaAvvocato(lAvvModRic);
		lAvvModRic = (AvvocatoModel) lVectRic.get(0);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("id = " + id);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lAvvModRic = " + lAvvModRic);

		AvvocatoModel lAvvModPrec = new AvvocatoModel();
		AvvocatoSigeModel lAvvSigeMod = new AvvocatoSigeModel();
		AvvocatoFascicoloSigeModel lAvvFascModPrec = new AvvocatoFascicoloSigeModel();
		lAvvFascModPrec.setFasSigeIdFascicoloSige(
				((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso")).getFascicoloSige()
						.getIdFascicoloSige());

		BigDecimal idVecchio = getRequestBigDecimalParameter("idAvvVecchio");

		try {
			lVectPrec = new Vector();
			lVectPrec = lCtrl.ExRicercaDifensoreAttualiFascicolo(lAvvModPrec, lAvvFascModPrec);
		} catch (Exception e) {
		}

		if (lVectPrec.size() > 1) {
			lAvvSigeMod = (AvvocatoSigeModel) lVectPrec.get(0);
			String lDescrTipo = lAvvSigeMod.getAvvocato().getDescrTipo();
			if (lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
					&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02")) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi sono di fiducia!");
			}
		}

		AvvocatoFascicoloSigeModel lAvvFascMod = new AvvocatoFascicoloSigeModel();

		lAvvFascMod.setCodTipoAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO));
		if (lAvvFascMod.getCodTipoAvvocato().equals("01")) {
			lAvvFascMod.setDataInizioValidita(
					getRequestDateParameter(ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_DESIGNAZIONE,
							ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_DESIGNAZIONE,
							ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_DESIGNAZIONE));
		}
		if (lAvvFascMod.getCodTipoAvvocato().equals("02")) {
			lAvvFascMod.setDataInizioValidita(
					getRequestDateParameter(ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_NOMINA,
							ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_NOMINA,
							ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_NOMINA));
		}
		if (lAvvFascMod.getCodTipoAvvocato().equals("03")) {
			lAvvFascMod.setDataInizioValidita(DateUtils.getSysDate());
		}
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE)) {
			lAvvFascMod.setCodMotivoDesignazione(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE));
		} else {
			lAvvFascMod.setCodMotivoDesignazione("-");
		}
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA))
			lAvvFascMod.setCodTipoAutorita(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA));
		else
			lAvvFascMod.setCodTipoAutorita("-");

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)
				&& (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA)
						&& !getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA)
								.equals("-"))) {
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)));
			lAvvFascMod.setSedeAutorita(lComMod.getCodComune());
		} else {
			lAvvFascMod.setSedeAutorita("-");
		}

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA))
			lAvvFascMod.setIndirizzoTipoAutorita(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA));
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lAvvFascMod.setIstDetIdIstitutoDetenzione(
					getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF))
			lAvvFascMod.setCodTipoAutoritaDif(
					getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF));
		else
			lAvvFascMod.setCodTipoAutoritaDif("-");

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF)
				&& (!this
						.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF)
						&& !getRequestStringParameter(
								ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF).equals("-"))) {
			String lComneAutoritaDif = getRequestStringParameter(
					ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF);
			ComuneModel lComModDif = new ComuneModel(getCodComuneByDescr(lComneAutoritaDif));

			lAvvFascMod.setSedeAutoritaDif(lComModDif.getCodComune());
		} else {
			lAvvFascMod.setSedeAutoritaDif("-");
		}

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE)) {
			lAvvFascMod.setNote(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE));
		}

		if (!lAvvModRic.getCodUffAppartenenza().equals(this.getCodUfficioUtenteConnesso())) {
			// Vado in duplicazione aggiornando i dati selezionabili in form
			lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
			lAvvModRic.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());

			lAvvModRic.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());

			ComuneModel lComModRes = new ComuneModel(this.getCodComuneByDescr(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());

			lAvvModRic.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lAvvModRic.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lAvvModRic.setDataInserimento(DateUtils.getSysDate());

			lAvvModRic = lCtrl.ExInserisciAvvocato(lAvvModRic);
		} else {
			// Ho selezionato un avvocato dell'ufficio e ne sto modificando eventualmente
			// i dati.
			// Storicizzando i dati, quindi vado in update.
			StoricoAvvocatoModel lStoricoModel = new StoricoAvvocatoModel();

			lStoricoModel.setCognome(lAvvModRic.getCognome());
			lStoricoModel.setNome(lAvvModRic.getNome());
			lStoricoModel.setForo(lAvvModRic.getForo());
			lStoricoModel.setIndirizzo(lAvvModRic.getIndirizzo());
			lStoricoModel.setTelefono(lAvvModRic.getTelefono());
			lStoricoModel.setFax(lAvvModRic.getFax());
			lStoricoModel.setEMail(lAvvModRic.getEMail());
			lStoricoModel.setCodComuneResidenza(lAvvModRic.getCodComuneResidenza());
			lStoricoModel.setCodLuogoNascita(lAvvModRic.getCodLuogoNascita());
			lStoricoModel.setDataNascita(lAvvModRic.getDataNascita());
			lStoricoModel.setDataSospesoFinoAl(lAvvModRic.getDataSospensione());
			lStoricoModel.setDataRadiatoDal(lAvvModRic.getDataRadiazione());
			lStoricoModel.setCodNonAttivita(lAvvModRic.getCodNonAttivita());
			// mancano le note
			lStoricoModel.setCodUfficioAppartenenza(lAvvModRic.getCodUffAppartenenza());

			lStoricoModel.setAvvIdAvvocato(lAvvModRic.getIdAvvocato());

			// manca flag cancellato
			lStoricoModel.setCodiceFiscale(lAvvModRic.getCodiceFiscale());
			lStoricoModel.setProvincia(lAvvModRic.getProvincia());
			lStoricoModel.setCap(lAvvModRic.getCap());

			// manca id_avvocato_Standard
			lStoricoModel.setIdAvvocatoStandard(lAvvModRic.getIdAvvocatoStandard());

			lStoricoModel.setCodUfficioInserimento(lAvvModRic.getCodUfficioAggiornamento());
			lStoricoModel.setCodOperatoreInserimento(lAvvModRic.getCodOperatoreAggiornamento());
			lStoricoModel.setDataInserimento(lAvvModRic.getDataAggiornamento());

			// Aggiorno i dati con quanto prelevabile/modificabile della form
			lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
			lAvvModRic.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());
			ComuneModel lComModRes = new ComuneModel(this.getCodComuneByDescr(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());

			lAvvModRic.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
			lAvvModRic.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			lAvvModRic.setDataAggiornamento(DateUtils.getSysDate());

			lAvvModRic = lCtrl.ExModificaStoricizzaAvvocato(lAvvModRic, lStoricoModel);
		}

		lAvvFascMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvFascMod.setDataInserimento(DateUtils.getSysDate());
		lAvvFascMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		lAvvFascMod.setFasSigeIdFascicoloSige(
				((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso")).getFascicoloSige()
						.getIdFascicoloSige());
		lAvvFascMod.setAvvIdAvvocato(lAvvModRic.getIdAvvocato());

		// update
		AvvocatoFascicoloSigeModel lAvvFascUp = new AvvocatoFascicoloSigeModel();
		lAvvFascUp.setAvvIdAvvocato(idVecchio);
		lAvvFascUp.setFasSigeIdFascicoloSige(
				((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso")).getFascicoloSige()
						.getIdFascicoloSige());
		lAvvFascUp.setDataFineValidita(DateUtils.getSysDate());

		lAvvFascUp.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAvvFascUp.setDataAggiornamento(DateUtils.getSysDate());
		lAvvFascUp.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

		AvvocatoFascicoloSigeModel lAvvMod = new AvvocatoFascicoloSigeModel();
		lAvvMod = lCtrl.ExSostituzioneAvvocato(lAvvFascUp, lAvvFascMod);

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.avvocato.action.ActLoadDettaglioAvvocatoFascicolo&" + CAMPO_ID_AVVOCATO + "="
				+ lAvvMod.getAvvIdAvvocato().toString();
	}

}