package siap.sius.avvocato.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActSostituzioneDifensore extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Avvocato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 *         <p>
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
//		Vector lVect = null;
		Vector lVectPrec = null;
		Vector lVectRic = null;
		BigDecimal id = getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO);
		AvvocatoModel lAvvModRic = new AvvocatoModel();
		lAvvModRic.setIdAvvocato(id);
		BigDecimal idVecchio = getRequestBigDecimalParameter("idAvvVecchio");
		lVectRic = lCtrl.ExRicercaAvvocato(lAvvModRic);
		lAvvModRic = (AvvocatoModel) lVectRic.get(0);
		AvvocatoModel lAvvModPrec = new AvvocatoModel();
		AvvocatoSiusModel lAvvSiusMod = new AvvocatoSiusModel();
		AvvocatoFascicoloSiusModel lAvvFascModPrec = new AvvocatoFascicoloSiusModel();
		lAvvFascModPrec.setFasSiuIdFascicoloSius(((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());

		try {
			lVectPrec = new Vector();
			lVectPrec = lCtrl.ExRicercaDifensoreAttualiFascicolo(lAvvModPrec, lAvvFascModPrec);
		} catch (Exception e) {
		}

		if (lVectPrec.size() > 1) {
			lAvvSiusMod = (AvvocatoSiusModel) lVectPrec.get(0);
			String lDescrTipo = lAvvSiusMod.getAvvocato().getDescrTipo();
			if (lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
					&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02")) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi sono di fiducia!");
			}
		}

		AvvocatoFascicoloSiusModel lAvvFascMod = new AvvocatoFascicoloSiusModel();

		lAvvFascMod.setCodTipoAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO));
		if (lAvvFascMod.getCodTipoAvvocato().equals("01")) {
			lAvvFascMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocatoFascicoloSius.CAMPO_ANNO_DATA_DESIGNAZIONE,
					ICostantiAvvocatoFascicoloSius.CAMPO_MESE_DATA_DESIGNAZIONE,
					ICostantiAvvocatoFascicoloSius.CAMPO_GIORNO_DATA_DESIGNAZIONE));
		}
		if (lAvvFascMod.getCodTipoAvvocato().equals("02")) {
			lAvvFascMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocatoFascicoloSius.CAMPO_ANNO_DATA_NOMINA,
					ICostantiAvvocatoFascicoloSius.CAMPO_MESE_DATA_NOMINA,
					ICostantiAvvocatoFascicoloSius.CAMPO_GIORNO_DATA_NOMINA));
		}
		if (lAvvFascMod.getCodTipoAvvocato().equals("03")) {
			lAvvFascMod.setDataInizioValidita(DateUtils.getSysDate());
		}
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_MOTIVO_DESIGNAZIONE)) {
			lAvvFascMod
					.setCodMotivoDesignazione(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_MOTIVO_DESIGNAZIONE));
		} else {
			lAvvFascMod.setCodMotivoDesignazione("-");
		}
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA))
			lAvvFascMod
					.setCodTipoAutorita(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA));
		else
			lAvvFascMod.setCodTipoAutorita("-");

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA)
				&& (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA) && !getRequestStringParameter(
						ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA).equals("-"))) {
//			String lComuneAutorita = getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA);
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA)));
			lAvvFascMod.setSedeAutorita(lComMod.getCodComune());
		} else {
			lAvvFascMod.setSedeAutorita("-");
		}

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_INDIRIZZO_TIPO_AUTORITA))
			lAvvFascMod
					.setIndirizzoTipoAutorita(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_INDIRIZZO_TIPO_AUTORITA));
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lAvvFascMod
					.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA_DIF))
			lAvvFascMod
					.setCodTipoAutoritaDif(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA_DIF));
		else
			lAvvFascMod.setCodTipoAutoritaDif("-");

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA_DIF)
				&& (!this
						.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA_DIF) && !getRequestStringParameter(
						ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA_DIF).equals("-"))) {
			String lComneAutoritaDif = getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA_DIF);
			ComuneModel lComModDif = new ComuneModel(getCodComuneByDescr(lComneAutoritaDif));

			lAvvFascMod.setSedeAutoritaDif(lComModDif.getCodComune());
		} else {
			lAvvFascMod.setSedeAutoritaDif("-");
		}

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_NOTE)) {
			lAvvFascMod.setNote(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_NOTE));
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO appar = " + lAvvModRic.getCodUffAppartenenza());

		if (!lAvvModRic.getCodUffAppartenenza().equals(this.getCodUfficioUtenteConnesso())) {
			lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
			lAvvModRic.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());
			lAvvModRic.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());
			ComuneModel lComModRes = new ComuneModel(
					this.getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());

			lAvvModRic.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lAvvModRic.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lAvvModRic.setDataInserimento(DateUtils.getSysDate());

			lAvvModRic = lCtrl.ExInserisciAvvocato(lAvvModRic);
		} else {
			StoricoAvvocatoModel lStoricoModel = new StoricoAvvocatoModel();

			lStoricoModel.setCognome(lAvvModRic.getCognome());
			lStoricoModel.setNome(lAvvModRic.getNome());
			lStoricoModel.setAvvIdAvvocato(lAvvModRic.getIdAvvocato());
			lStoricoModel.setCodLuogoNascita(lAvvModRic.getCodLuogoNascita());
			lStoricoModel.setDataNascita(lAvvModRic.getDataNascita());
			lStoricoModel.setForo(lAvvModRic.getForo());
			lStoricoModel.setIndirizzo(lAvvModRic.getIndirizzo());
			lStoricoModel.setCodComuneResidenza(lAvvModRic.getCodComuneResidenza());
			lStoricoModel.setTelefono(lAvvModRic.getTelefono());
			lStoricoModel.setFax(lAvvModRic.getFax());
			lStoricoModel.setEMail(lAvvModRic.getEMail());
			lStoricoModel.setCodiceFiscale(lAvvModRic.getCodiceFiscale());
			lStoricoModel.setProvincia(lAvvModRic.getProvincia());
			lStoricoModel.setCap(lAvvModRic.getCap());
			lStoricoModel.setDataSospesoFinoAl(lAvvModRic.getDataSospensione());
			lStoricoModel.setDataRadiatoDal(lAvvModRic.getDataRadiazione());
			lStoricoModel.setCodNonAttivita(lAvvModRic.getCodNonAttivita());
			lStoricoModel.setCodUfficioAppartenenza(lAvvModRic.getCodUffAppartenenza());
			lStoricoModel.setAvvIdAvvocato(lAvvModRic.getIdAvvocato());

			lStoricoModel.setCodUfficioInserimento(lAvvModRic.getCodUfficioAggiornamento());
			lStoricoModel.setCodOperatoreInserimento(lAvvModRic.getCodOperatoreAggiornamento());
			lStoricoModel.setDataInserimento(lAvvModRic.getDataAggiornamento());

			lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
			lAvvModRic.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());

			lAvvModRic.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
			lAvvModRic.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			lAvvModRic.setDataAggiornamento(DateUtils.getSysDate());

			ComuneModel lComModRes = new ComuneModel(
					this.getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());

			lAvvModRic = lCtrl.ExModificaStoricizzaAvvocato(lAvvModRic, lStoricoModel);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO CONTR0 = " + lAvvModRic.getIdAvvocato());

		lAvvFascMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvFascMod.setDataInserimento(DateUtils.getSysDate());
		lAvvFascMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lAvvFascMod.setFasSiuIdFascicoloSius(((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());
		lAvvFascMod.setAvvIdAvvocato(lAvvModRic.getIdAvvocato());

		// update
		AvvocatoFascicoloSiusModel lAvvFascUp = new AvvocatoFascicoloSiusModel();
		lAvvFascUp.setAvvIdAvvocato(idVecchio);
		lAvvFascUp.setFasSiuIdFascicoloSius(((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());
		lAvvFascUp.setDataFineValidita(DateUtils.getSysDate());

		lAvvFascUp.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lAvvFascUp.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lAvvFascUp.setDataAggiornamento(DateUtils.getSysDate());

		AvvocatoFascicoloSiusModel lAvvMod = new AvvocatoFascicoloSiusModel();
		lAvvMod = lCtrl.ExSostituzioneAvvocato(lAvvFascUp, lAvvFascMod);

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.avvocato.action.ActLoadDettaglioAvvocatoFascicolo&" + CAMPO_ID_AVVOCATO + "="
				+ lAvvMod.getAvvIdAvvocato().toString();
	}

}