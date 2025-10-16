package siap.siep.sospensione.action;

/**
 * <p>Title: ActInserisciRigettoOpEspulsione</p>
 * <p>Description: Classe Action per l'inserimento Rigetto Opposizione di Espulsione</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.misuraalternativa.action.ActMisuraAlternativa;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

public class ActInserisciRigettoOpEspulsione extends ActMisuraAlternativa {

	/**
	 * Azione di Inserimento del Sospensione Espulsione
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
		BigDecimal lIdEvento = null;

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisModEsp = null;
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lMisModEsp = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		// non esiste il decreto di espulsione
		if (lMisModEsp == null) {
			// INSERISCO EVENTO E NOTIFICA DEL TDS
			EventoNotificaModel lEveMod = new EventoNotificaModel();
			lEveMod.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			String lCodiceUffEmi = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));
			ComuneModel lComModAutEmi = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));
			Date lDataEmisTras = getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);

			lEveMod.setEvento(setEventoOrdinanzaDecretoMisuraAlternativa(lEveMod.getEvento(), "03",
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// setto l'ordinanza
			DepositoOrdinanzaPcModel lDepOrMod = this.setDepositoOrdinanzaPc(lCodiceUffEmi);

			// setto il tenore
			TenoreModel lTenMod = setTenore(new BigDecimal(1), "0025");

			// misura aletrantiva
			lMisMod = setMisuraAlternativa("03", "RG", lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), "-");

			BigDecimal lAnniEspulso = null;
			BigDecimal lGiorniEspulso = null;
			BigDecimal lMesiEspulso = null;

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA)
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA)
							.equals(""))
				lAnniEspulso = new BigDecimal(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA));

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA)
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA)
							.equals(""))
				lGiorniEspulso = new BigDecimal(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA));

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA)
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA)
							.equals(""))
				lMesiEspulso = new BigDecimal(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA));

			lMisMod.setNumAnniMisura(lAnniEspulso);
			lMisMod.setNumGiorniMisura(lGiorniEspulso);
			lMisMod.setNumMesiMisura(lMesiEspulso);

			// inserimento
			lMisModEsp = lMisAltCtrl.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrMod, lTenMod,
					lMisMod);
		}
		lIdEvento = lMisModEsp.getEveIdEvento();

		// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.getEvento().setCodTipoProvvedimento("09");
		lEveNot.getEvento().setCodMotivo("0395");

		lEveNot.setEvento(setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));
		lEveNot.getEvento().setEveIdEvento(lMisModEsp.getEveIdEvento());

		NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();
		lEveNot.setNotifiche(lNotificheMod);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		IMisuraAlternativa lCtrlMisuraAlt = SICOLookupRemote.getMisuraAlternativaRemote();
		EventoNotificaModel lEveNotModel = lCtrlMisuraAlt.ExInserisciOModificaMANotifica(lEveNot, lPenaRes,
				null, null);
		lIdEvento = lEveNotModel.getEvento().getIdEvento();

		// Prepara la pagina di destinazione
		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sospensione.action.ActDettaglioRigettoOpEspulsione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEvento.toString();
		return lPage;
	}

}