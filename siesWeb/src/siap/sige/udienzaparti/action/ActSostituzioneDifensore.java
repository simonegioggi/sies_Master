package siap.sige.udienzaparti.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.security.action.ICostantiSecurity;
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
 * Title: ActSostituzioneDifensore
 * </p>
 * <p>
 * Description: Classe Action per la Sostituzione di un Difensore
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActSostituzioneDifensore extends ActionSiap implements ICostantiPartiUdienza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		String idSoggetto = "";
		if (!this.isRequestParameterNullObj(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO)) {
			idSoggetto = this.getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO);
		}

		// Identificativo evento udienza
		String lIdEventoUdienza = getRequestStringParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV);

		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		Vector lVectPrec = null;
		Vector lVectRic = null;
		BigDecimal id = getRequestBigDecimalParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
		AvvocatoModel lAvvModRic = new AvvocatoModel();
		lAvvModRic.setIdAvvocato(id);
		BigDecimal idVecchio = getRequestBigDecimalParameter("idAvvVecchio");
		lVectRic = lCtrl.ExRicercaAvvocato(lAvvModRic);
		lAvvModRic = (AvvocatoModel) lVectRic.get(0);
		AvvocatoModel lAvvModPrec = new AvvocatoModel();

		AvvocatoParteModel lAvvParteMod = new AvvocatoParteModel();

		PartiUdienzaDifensoreModel lAvvParteModPrec = new PartiUdienzaDifensoreModel();
		lAvvParteModPrec.setSoggIdSoggetto(new BigDecimal(idSoggetto));

		try {
			lVectPrec = new Vector();
			lVectPrec = lCtrl.ExRicercaDifensoreAttualiParteUdienza(lAvvModPrec, lAvvParteModPrec);
		} catch (Exception e) {
		}
		if (lVectPrec.size() > 1) {
			lAvvParteMod = (AvvocatoParteModel) lVectPrec.get(0);
			String lDescrTipo = lAvvParteMod.getAvvocato().getDescrTipo();
			if (lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
					&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02")) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi sono di fiducia!");
			}
		} else {
			lAvvParteMod = (AvvocatoParteModel) lVectPrec.get(0);
		}

		PartiUdienzaDifensoreModel lDifParteMod = new PartiUdienzaDifensoreModel();

		lDifParteMod.setCodTipoAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO));
		if (lDifParteMod.getCodTipoAvvocato().equals("01")) {
			lDifParteMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_DESIGNAZIONE,
					ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_DESIGNAZIONE,
					ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_DESIGNAZIONE));
		}
		if (lDifParteMod.getCodTipoAvvocato().equals("02")) {
			lDifParteMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_NOMINA,
					ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_NOMINA,
					ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_NOMINA));
		}
		if (lDifParteMod.getCodTipoAvvocato().equals("03")) {
			lDifParteMod.setDataInizioValidita(DateUtils.getSysDate());
		}
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE)) {
			lDifParteMod
					.setCodMotivoDesignazione(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE));
		} else {
			lDifParteMod.setCodMotivoDesignazione("-");
		}
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA))
			lDifParteMod
					.setCodTipoAutorita(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA));
		else
			lDifParteMod.setCodTipoAutorita("-");

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)
				&& (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA) && !getRequestStringParameter(
						ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA).equals("-"))) {
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA)));
			lDifParteMod.setSedeTipoAutorita(lComMod.getCodComune());
		} else {
			lDifParteMod.setSedeTipoAutorita("-");
		}

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA))
			lDifParteMod
					.setIndirizzoTipoAutorita(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA));
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lDifParteMod
					.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF))
			lDifParteMod
					.setCodTipoAutoritaDif(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF));
		else
			lDifParteMod.setCodTipoAutoritaDif("-");

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF)
				&& (!this
						.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF) && !getRequestStringParameter(
						ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF).equals("-"))) {
			String lComneAutoritaDif = getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF);
			ComuneModel lComModDif = new ComuneModel(getCodComuneByDescr(lComneAutoritaDif));

			lDifParteMod.setSedeAutoritaDif(lComModDif.getCodComune());
		} else {
			lDifParteMod.setSedeAutoritaDif("-");
		}

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE)) {
			lDifParteMod.setNote(getRequestStringParameter(ICostantiAvvocatoFascicoloSige.CAMPO_NOTE));
		}
		if (!lAvvModRic.getCodUffAppartenenza().equals(this.getCodUfficioUtenteConnesso())) {
			lAvvModRic.setForo(getRequestStringParameter(ICostantiAvvocato.CAMPO_FORO).toUpperCase());
			lAvvModRic.setIndirizzo(getRequestStringParameter(ICostantiAvvocato.CAMPO_INDIRIZZO)
					.toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(ICostantiAvvocato.CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(ICostantiAvvocato.CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(ICostantiAvvocato.CAMPO_E_MAIL).toUpperCase());
			lAvvModRic.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());
			ComuneModel lComModRes = new ComuneModel(
					this.getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());
			lAvvModRic.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lAvvModRic.setCodOperatoreInserimento(this.getCodUtenteConnesso());
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
			lAvvModRic.setForo(getRequestStringParameter(ICostantiAvvocato.CAMPO_FORO).toUpperCase());
			lAvvModRic.setIndirizzo(getRequestStringParameter(ICostantiAvvocato.CAMPO_INDIRIZZO)
					.toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(ICostantiAvvocato.CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(ICostantiAvvocato.CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(ICostantiAvvocato.CAMPO_E_MAIL).toUpperCase());
			ComuneModel lComModRes = new ComuneModel(
					this.getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());
			lAvvModRic = lCtrl.ExModificaStoricizzaAvvocato(lAvvModRic, lStoricoModel);
		}

		lDifParteMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lDifParteMod.setDataInserimento(DateUtils.getSysDate());
		lDifParteMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lDifParteMod.setSoggIdSoggetto(new BigDecimal(idSoggetto));
		lDifParteMod.setAvvIdAvvocato(lAvvModRic.getIdAvvocato());

		// update
		PartiUdienzaDifensoreModel lParteUdienzaUp = new PartiUdienzaDifensoreModel();
		lParteUdienzaUp.setAvvIdAvvocato(idVecchio);
		lParteUdienzaUp.setSoggIdSoggetto(new BigDecimal(idSoggetto));
		lParteUdienzaUp.setDataFineValidita(DateUtils.getSysDate());
		lParteUdienzaUp.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lParteUdienzaUp.setDataAggiornamento(DateUtils.getSysDate());
		PartiUdienzaDifensoreModel lAvvMod = new PartiUdienzaDifensoreModel();
		lAvvMod = lCtrl.ExSostituzioneAvvocato(lParteUdienzaUp, lDifParteMod);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.udienzaparti.action.ActLoadDettaglioAvvocatoParte&"
				+ ICostantiAvvocato.CAMPO_ID_AVVOCATO + "=" + lAvvMod.getAvvIdAvvocato() + "&"
				+ ICostantiPartiUdienza.CAMPO_ID_AVVOCATO_PARTE_UDIENZA + "="
				+ lAvvParteMod.getAvvocatoParteUdienzaModel().getIdAvvocatoParteUdienza() + "&"
				+ ICostantiPartiUdienza.CAMPO_ID_SOGGETTO + "=" + idSoggetto + "&"
				// MERGE v10 COLLAUDO: aggiunto parametro di passaggio in query string
				+ ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA + "=" + lIdEventoUdienza;

		return lPage;
	}

}