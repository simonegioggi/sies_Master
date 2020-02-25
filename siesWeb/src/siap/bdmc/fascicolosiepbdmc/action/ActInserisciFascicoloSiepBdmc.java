package siap.bdmc.fascicolosiepbdmc.action;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.fascicolosiepbdmc.controller.IFascicoloSiepBdmc;
import siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel;
import siap.bdmc.notifichesies.controller.INotificheSies;
import siap.bdmc.notifichesies.model.NotificheSiesModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActInserisciFascicoloSiepBdmc
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di FascicoloSiepBdmc
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
public class ActInserisciFascicoloSiepBdmc extends ActionSiap implements ICostantiFascicoloSiepBdmc {

	/*****************************************************************************
	 * Azione di Inserimento del FascicoloSiepBdmc
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 ****************************************************************************/
	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati presenti in maschera
		// n.b. eliminare o commentare i campi non presenti in maschera
		// es: chiave della tabella, date_ins, foreignkey...
		// ==========================================================================

		// Controlla esistenza Fascicolo SIES
		// Istanzio il Model
		FascicoloSiepModel lFasModSies = new FascicoloSiepModel();

		lFasModSies.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_SIEP));
		lFasModSies.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_SIEP));
		lFasModSies.setChiaveUfficio(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO_SIEP));

		/***********************************************************/
		// ICostantiSiepJMS.CAMPO_SEDE_UFFICIO
		// FascicoloSiepController lCtrl = new FascicoloSiepController();
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		lFasModSies = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasModSies);

		if (lFasModSies == null) {
			throw new SIEPException(F3BException.USER_MESSAGE, "Fascicolo siep non trovato");
		}

		FascicoloSiepBdmcModel lFasMod = new FascicoloSiepBdmcModel();

		lFasMod.setChiaveAnnoBdmc(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_BDMC));
		lFasMod.setChiaveUfficioBdmc(getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
				getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
		lFasMod.setChiaveProgrBdmc(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_BDMC));
		lFasMod.setChiaveAnnoSiep(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_SIEP));
		lFasMod.setChiaveUfficioSiep(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO_SIEP));
		lFasMod.setChiaveProgrSiep(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_SIEP));
		lFasMod.setFlagTrasmissione("S");
		lFasMod.setCodOperatoreInserimento(this.getUtenteConnesso().getUserId());
		lFasMod.setCodUfficioInserimento(this.getUfficioUtenteConnesso().getCodUfficio());

		lFasMod.setDataInserimento(DateUtils.getSysDate());
		lFasMod.setDataTrasmissione(DateUtils.getSysDate());
		lFasMod.setTipoMisura("NC");

		// ===================================================
		// Recupera il controller ed effettua l'inserimento
		// ===================================================
		IFascicoloSiepBdmc lCtrl1 = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
		FascicoloSiepBdmcModel lFasRetMod = new FascicoloSiepBdmcModel();
		lFasRetMod = lCtrl1.ExInserisciFascicoloSiepBdmc(lFasMod);

		// ===================================================
		// Inserimento della notifica verso BDMC
		// ===================================================
		NotificheSiesModel lNotMod = new NotificheSiesModel();

		lNotMod.setAnnoSiep(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_SIEP));
		lNotMod.setUfficioSiep(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO_SIEP));
		lNotMod.setProgSiep(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_SIEP));

		lNotMod.setAnnoFascBdmc(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_BDMC));
		lNotMod.setUfficioFascBdmc(getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
				getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
		lNotMod.setNumeroFascBdmc(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_BDMC));

		lNotMod.setIdFascicoloBdmc(lFasRetMod.getIdFascicoloBdmc());

		lNotMod.setTipoNotifica("A");
		lNotMod.setDataNotifica(DateUtils.getSysDate());
		lNotMod.setStatoTrasmissione("N");
		lNotMod.setCodOperatoreInserimento(this.getUtenteConnesso().getUserId());
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(this.getUfficioUtenteConnesso().getCodUfficio());

		INotificheSies lCtrl2 = BDMCLookupRemote.getNotificheSiesRemote();
//		NotificheSiesModel lNotRetMod = new NotificheSiesModel();
		/*lNotRetMod = */lCtrl2.ExInserisciNotificheSies(lNotMod);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		String lPage = "";
		lPage = ISIAPCostantiWeb.PG_MAIN + "?" + ISIAPCostantiWeb.ACTION_FIELD
				+ "=siap.bdmc.fascicolosiepbdmc.action.ActLoadDettaglioFascicoloSiepBdmc";
		lPage += "&" + CAMPO_ID_FASCICOLO_BDMC + "=" + lFasRetMod.getIdFascicoloBdmc().toString();
		lPage += "&" + CAMPO_COD_TIPO_AUTORITA_EMITTENTE + "="
				+ getRequestStringParameter(CAMPO_DESCR_AUTEMI_BDMC).toString() + " ("
				+ getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE).toString() + ")";

		return lPage;
	}

}