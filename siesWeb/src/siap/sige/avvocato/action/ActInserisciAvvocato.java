package siap.sige.avvocato.action;

import java.math.BigDecimal;
import java.util.Vector;

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
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciAvvocato
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Avvocato
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
public class ActInserisciAvvocato extends ActionSiap implements ICostantiAvvocato {

	/**
	 * Azione di Inserimento del Avvocato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 *         <p>
	 * @throws F3BException
	 */
	/**
	 * <p>
	 * Title: ActDeassegnaDifensore
	 * </p>
	 * <p>
	 * Description: Classe Action per la modifica di Avvocato
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2004
	 * </p>
	 * <p>
	 * Company: Bull
	 * </p>
	 * 
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Passa la action di destinazione
		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();

		AvvocatoModel lAvvMod = new AvvocatoModel();
		Vector lVectRic = new Vector();
		Vector lVectPrec = null;
		AvvocatoModel lAvvModRic = new AvvocatoModel();

		AvvocatoModel lAvvModPrec = new AvvocatoModel();
		AvvocatoFascicoloSigeModel lAvvFascModPrec = new AvvocatoFascicoloSigeModel();
		lAvvFascModPrec
				.setFasSigeIdFascicoloSige(((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso"))
						.getFascicoloSige().getIdFascicoloSige());

		try {
			lVectPrec = new Vector();
			lVectPrec = lCtrl.ExRicercaDifensoreAttualiFascicolo(lAvvModPrec, lAvvFascModPrec);
		} catch (Exception e) {
		}

		if (lVectPrec.size() > 0) {
			lAvvModPrec = ((AvvocatoSigeModel) lVectPrec.get(0)).getAvvocato();
			String lDescrTipo = lAvvModPrec.getDescrTipo();
			if (lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
					&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02")) {

				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi sono di fiducia!");

			}

			if (lAvvModPrec.getIdAvvocato().compareTo(this.getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO)) == 0)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione: il difensore risulta già inserito!");

		}

		AvvocatoFascicoloSigeModel lAvvFascMod = new AvvocatoFascicoloSigeModel();
		lAvvMod.setIdAvvocato(getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO));

		lVectRic = lCtrl.ExRicercaAvvocato(lAvvMod);
		lAvvModRic = (AvvocatoModel) lVectRic.get(0);
		/*
		 * if(lAvvModRic.getDataSospensione()!= null && !lAvvModRic.getDataSospensione().equals("-")) throw
		 * new F3BException(F3BException.USER_MESSAGE,"Attenzione: il difensore risulta sospeso!");
		 * 
		 * if(lAvvModRic.getDataRadiazione()!= null && !lAvvModRic.getDataRadiazione().equals("-")) throw new
		 * F3BException(F3BException.USER_MESSAGE,"Attenzione: il difensore risulta radiato!");
		 * 
		 * if(lAvvModRic.getCodNonAttivita()!= null && !lAvvModRic.getCodNonAttivita().equals("-")) throw new
		 * F3BException
		 * (F3BException.USER_MESSAGE,"Attenzione: il difensore risulta non in attività per  "+lAvvModRic
		 * .getDescrNonAttivita()+"");
		 */

		// da qui sul secondo model
		lAvvFascMod.setCodTipoAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO));
		if (lAvvFascMod.getCodTipoAvvocato().equals("01")) {
			lAvvFascMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_DESIGNAZIONE,
					ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_DESIGNAZIONE,
					ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_DESIGNAZIONE));

		}
		if (lAvvFascMod.getCodTipoAvvocato().equals("02")) {
			lAvvFascMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_NOMINA,
					ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_NOMINA,
					ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_NOMINA));

		}
		if (lAvvFascMod.getCodTipoAvvocato().equals("03")) {
			lAvvFascMod.setDataInizioValidita(DateUtils.getSysDate());

		}
		lAvvFascMod.setDataFineValidita(null);
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE)) {
			lAvvFascMod
					.setCodMotivoDesignazione(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE));
		} else {
			lAvvFascMod.setCodMotivoDesignazione("-");
		}
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA))
			lAvvFascMod
					.setCodTipoAutorita(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA));
		else
			lAvvFascMod.setCodTipoAutorita("-");

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)
				&& (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA) && !getRequestStringParameter(
						ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA).equals("-"))) {
//			String lComneAutorita = getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA);
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)));
			lAvvFascMod.setSedeAutorita(lComMod.getCodComune());
		} else {

			lAvvFascMod.setSedeAutorita("-");

		}

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA))
			lAvvFascMod
					.setIndirizzoTipoAutorita(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA));
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lAvvFascMod
					.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF))
			lAvvFascMod
					.setCodTipoAutoritaDif(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF));
		else
			lAvvFascMod.setCodTipoAutoritaDif("-");

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF)
				&& (!this
						.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF) && !getRequestStringParameter(
						ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF).equals("-"))) {
			String lComneAutoritaDif = getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF);
			ComuneModel lComModDif = new ComuneModel(getCodComuneByDescr(lComneAutoritaDif));

			lAvvFascMod.setSedeAutoritaDif(lComModDif.getCodComune());
		} else {

			lAvvFascMod.setSedeAutoritaDif("-");

		}
		// CAMPO_COD_TIPO_AUTORITA_DIF
		lAvvFascMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvFascMod.setDataInserimento(DateUtils.getSysDate());
		lAvvFascMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lAvvFascMod
				.setFasSigeIdFascicoloSige(((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso"))
						.getFascicoloSige().getIdFascicoloSige());
		lAvvFascMod.setAvvIdAvvocato(getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO));
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE)) {
			lAvvFascMod.setNote(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE));
		}

		// AvvocatoController lCtrl = new AvvocatoController();

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

			lAvvMod = lCtrl.ExInserisciAvvocato(lAvvModRic);
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
			ComuneModel lComModRes = new ComuneModel(
					this.getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());

			lAvvModRic.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
			lAvvModRic.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			lAvvModRic.setDataAggiornamento(DateUtils.getSysDate());

			lAvvMod = lCtrl.ExModificaStoricizzaAvvocato(lAvvModRic, lStoricoModel);
		}

//		AvvocatoSigeModel avvSigeMod = new AvvocatoSigeModel();
		BigDecimal idAvv = null;
		/*avvSigeMod = */lCtrl.ExInserisciAvvocato(lAvvMod, lAvvFascMod); // setta la risposta nella request
																		// setRequestAttribute("avvocato",lVect);
		idAvv = lAvvFascMod.getAvvIdAvvocato();

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.avvocato.action.ActLoadDettaglioAvvocatoFascicolo&" + CAMPO_ID_AVVOCATO + "="
				+ idAvv;

		return lPage;
	}

}