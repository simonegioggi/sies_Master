package siap.sige.udienzaparti.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.udienzaparti.model.AvvocatoParteModel;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;
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
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciAvvocato extends ActionSiap implements ICostantiAvvocato {

	/**
	 * Azione di Inserimento dell' Avvocato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 *         <p>
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		String idSoggetto = "";
		String idEventoUdienza = "";
		if (!this.isRequestParameterNullObj(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO)) {
			idSoggetto = this.getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO);
		}
		setRequestAttribute("idSoggetto", "" + idSoggetto);

		if (!this.isRequestParameterNullObj(ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA)) {
			idEventoUdienza = this.getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA);
		}
		setRequestAttribute("idEventoUdienza", "" + idEventoUdienza);

		// Passa la action di destinazione
		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();

		AvvocatoModel lAvvMod = new AvvocatoModel();
		Vector lVectRic = new Vector();
		Vector lVectPrec = null;
		AvvocatoModel lAvvModRic = new AvvocatoModel();

		AvvocatoModel lAvvModPrec = new AvvocatoModel();

		try {
			lVectPrec = new Vector();
			lVectPrec = lCtrl.ExRicercaAvvocatiByParteUdienza(new BigDecimal(idSoggetto));
		} catch (Exception e) {
		}

		if (lVectPrec.size() > 0) {
			lAvvModPrec = ((PartiUdienzaDifensoreModel) lVectPrec.get(0)).getAvvocato();
			String lDescrTipo = lAvvModPrec.getDescrTipo();
			if (lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
					&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02")) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi sono di fiducia!");
			}

			if (lAvvModPrec.getIdAvvocato().compareTo(this.getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO)) == 0) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione: il difensore risulta già inserito!");
			}
		}

		PartiUdienzaDifensoreModel lAvvParteMod = new PartiUdienzaDifensoreModel();
		lAvvParteMod.setSoggIdSoggetto(new BigDecimal(idSoggetto));
		lAvvParteMod.setAvvIdAvvocato(getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO));

		lAvvMod.setIdAvvocato(getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO));

		lVectRic = lCtrl.ExRicercaAvvocato(lAvvMod);
		lAvvModRic = (AvvocatoModel) lVectRic.get(0);

		// da qui sul secondo model
		lAvvParteMod.setCodTipoAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO));
		if (lAvvParteMod.getCodTipoAvvocato().equals("01")) {
			lAvvParteMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_DESIGNAZIONE,
					ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_DESIGNAZIONE,
					ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_DESIGNAZIONE));

		}
		if (lAvvParteMod.getCodTipoAvvocato().equals("02")) {
			lAvvParteMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_NOMINA,
					ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_NOMINA,
					ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_NOMINA));

		}
		if (lAvvParteMod.getCodTipoAvvocato().equals("03")) {
			lAvvParteMod.setDataInizioValidita(DateUtils.getSysDate());

		}
		lAvvParteMod.setDataFineValidita(null);
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE)) {
			lAvvParteMod
					.setCodMotivoDesignazione(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE));
		} else {
			lAvvParteMod.setCodMotivoDesignazione("-");
		}
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA))
			lAvvParteMod
					.setCodTipoAutorita(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA));
		else
			lAvvParteMod.setCodTipoAutorita("-");

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)
				&& (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA) && !getRequestStringParameter(
						ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA).equals("-"))) {
//			String lComuneAutorita = getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA);
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)));
			lAvvParteMod.setSedeTipoAutorita(lComMod.getCodComune());
		} else {

			lAvvParteMod.setSedeTipoAutorita("-");
		}

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA))
			lAvvParteMod
					.setIndirizzoTipoAutorita(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA));
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lAvvParteMod
					.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF))
			lAvvParteMod
					.setCodTipoAutoritaDif(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF));
		else
			lAvvParteMod.setCodTipoAutoritaDif("-");

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF)
				&& (!this
						.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF) && !getRequestStringParameter(
						ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF).equals("-"))) {
			String lComneAutoritaDif = getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF);
			ComuneModel lComModDif = new ComuneModel(getCodComuneByDescr(lComneAutoritaDif));
			lAvvParteMod.setSedeAutoritaDif(lComModDif.getCodComune());
		} else {
			lAvvParteMod.setSedeAutoritaDif("-");
		}

		lAvvParteMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvParteMod.setDataInserimento(DateUtils.getSysDate());
		lAvvParteMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lAvvParteMod.setSoggIdSoggetto(new BigDecimal(idSoggetto));
		lAvvParteMod.setAvvIdAvvocato(getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO));
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE)) {
			lAvvParteMod.setNote(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE));
		}

		if (!lAvvModRic.getCodUffAppartenenza().equals(this.getCodUfficioUtenteConnesso())) {
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
			lAvvModRic.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());
			ComuneModel lComModRes = new ComuneModel(
					this.getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());
			lAvvMod = lCtrl.ExModificaStoricizzaAvvocato(lAvvModRic, lStoricoModel);
		}

		AvvocatoParteModel avvParteMod = new AvvocatoParteModel();
		BigDecimal idAvv = null;
		BigDecimal idAvvParteUdienza = null;
		avvParteMod = lCtrl.ExInserisciAvvocato(lAvvMod, lAvvParteMod); // setta la risposta nella request
		// Identificativo avvvocato

		idAvv = avvParteMod.getAvvocato().getIdAvvocato();
		// chiave tabella PARTI_UDIENZA_DIFENSORE
		idAvvParteUdienza = avvParteMod.getAvvocatoParteUdienzaModel().getIdAvvocatoParteUdienza();

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.udienzaparti.action.ActLoadDettaglioAvvocatoParte&" + CAMPO_ID_AVVOCATO + "="
				+ idAvv + "&" + ICostantiPartiUdienza.CAMPO_ID_AVVOCATO_PARTE_UDIENZA + "="
				+ idAvvParteUdienza + "&" + ICostantiPartiUdienza.CAMPO_ID_SOGGETTO + "=" + idSoggetto + "&"
				+ ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA + "=" + idEventoUdienza;

		return lPage;
	}

}