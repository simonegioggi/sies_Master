package siap.bdmc.fascicolosiepbdmc.action;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.fascicolosiepbdmc.controller.IFascicoloSiepBdmc;
import siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel;
import siap.bdmc.notifichesies.controller.INotificheSies;
import siap.bdmc.notifichesies.model.NotificheSiesModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActModificaFascicoloSiepBdmc
 * </p>
 * <p>
 * Description: Classe Action per la modifica di FascicoloSiepBdmc
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
public class ActModificaFascicoloSiepBdmc extends ActionSiap implements ICostantiFascicoloSiepBdmc {

	/*****************************************************************************
	 * Azione di Modifica del FascicoloSiepBdmc
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// =====================================================
		// Riempie il model con i campi recuperati dalla form
		// n.b. per i campi del model non valorizzati, i corrispondenti
		// campi della tabella verranno impostati a null
		// Il campo chiave è obbligatorio perchè utilizzato nelle clausola where
		// per individuare il record da aggiornare
		// =====================================================

		FascicoloSiepBdmcModel lFasModSession = (FascicoloSiepBdmcModel) this
				.getSessionAttribute("fascicolosiepbdmcSession");

		FascicoloSiepBdmcModel lFasMod = new FascicoloSiepBdmcModel();

		lFasMod.setIdFascicoloBdmc(lFasModSession.getIdFascicoloBdmc());
		lFasMod.setChiaveAnnoBdmc(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_BDMC));
		lFasMod.setChiaveUfficioBdmc(getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
				getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
		// lFasMod.setChiaveUfficioBdmc ( getRequestStringParameter ( CAMPO_CHIAVE_UFFICIO_BDMC) );
		lFasMod.setChiaveProgrBdmc(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_BDMC));
		lFasMod.setChiaveAnnoSiep(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_SIEP));
		lFasMod.setChiaveUfficioSiep(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO_SIEP));
		lFasMod.setChiaveProgrSiep(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_SIEP));
		lFasMod.setFlagTrasmissione(getRequestStringParameter(CAMPO_FLAG_TRASMISSIONE));
		lFasMod.setCodOperatoreAggiornamento(this.getUtenteConnesso().getUserId());
		lFasMod.setCodUfficioAggiornamento(this.getUfficioUtenteConnesso().getCodUfficio());

		lFasMod.setDataAggiornamento(DateUtils.getSysDate());
		lFasMod.setTipoMisura("NC");
		boolean flagModifica = false;
		if (lFasMod.getChiaveAnnoBdmc().compareTo(lFasModSession.getChiaveAnnoBdmc()) != 0)
			flagModifica = true;
		else if (lFasMod.getChiaveAnnoSiep().compareTo(lFasModSession.getChiaveAnnoSiep()) != 0)
			flagModifica = true;
		else if (lFasMod.getChiaveProgrBdmc().compareTo(lFasModSession.getChiaveProgrBdmc()) != 0)
			flagModifica = true;
		else if (lFasMod.getChiaveProgrSiep().compareTo(lFasModSession.getChiaveProgrSiep()) != 0)
			flagModifica = true;
		else if (lFasMod.getChiaveUfficioBdmc().compareTo(lFasModSession.getChiaveUfficioBdmc()) != 0)
			flagModifica = true;
		else if (lFasMod.getChiaveUfficioSiep().compareTo(lFasModSession.getChiaveUfficioSiep()) != 0)
			flagModifica = true;

		lFasMod.setDataTrasmissione(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE,
				CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE));
		lFasMod.setDataDisattivazione(getRequestDateParameter(CAMPO_ANNO_DATA_DISATTIVAZIONE,
				CAMPO_MESE_DATA_DISATTIVAZIONE, CAMPO_GIORNO_DATA_DISATTIVAZIONE));
		lFasMod.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		lFasMod.setCodOperatoreInserimento((getRequestStringParameter(CAMPO_COD_OPERATORE_INSERIMENTO)));
		lFasMod.setCodUfficioInserimento(getRequestStringParameter(CAMPO_COD_UFFICIO_INSERIMENTO));

		// ===================================================
		// Recupera il controller ed effettua la modifica
		// ===================================================
		IFascicoloSiepBdmc lCtrl = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
		lCtrl.ExModificaFascicoloSiepBdmc(lFasMod);

		// ===================================================
		// Ricerco la precedente notifica
		// ===================================================
		INotificheSies lCtrl2 = BDMCLookupRemote.getNotificheSiesRemote();
//		NotificheSiesModel lNotRetModRicerca = new NotificheSiesModel();

		if (flagModifica
				&& lFasMod.getFlagTrasmissione().compareTo(lFasModSession.getFlagTrasmissione()) == 0) {
			if (lFasMod.getFlagTrasmissione().compareTo("S") == 0) {
				// inserisco annullo la riga precedente

				NotificheSiesModel lNotMod = new NotificheSiesModel();

				lNotMod.setAnnoSiep(lFasModSession.getChiaveAnnoSiep());
				lNotMod.setUfficioSiep(lFasModSession.getChiaveUfficioSiep());
				lNotMod.setProgSiep(lFasModSession.getChiaveProgrSiep());
				lNotMod.setIdFascicoloBdmc(lFasModSession.getIdFascicoloBdmc());
				lNotMod.setAnnoFascBdmc(lFasModSession.getChiaveAnnoBdmc());
				lNotMod.setNumeroFascBdmc(lFasModSession.getChiaveProgrBdmc());
				lNotMod.setUfficioFascBdmc(lFasModSession.getChiaveUfficioBdmc());
				lNotMod.setTipoNotifica("C");
				lNotMod.setDataNotifica(DateUtils.getSysDate());
				lNotMod.setStatoTrasmissione("N");
				lNotMod.setCodOperatoreInserimento(this.getUtenteConnesso().getUserId());
				lNotMod.setDataInserimento(DateUtils.getSysDate());
				lNotMod.setCodUfficioInserimento(this.getUfficioUtenteConnesso().getCodUfficio());

//				NotificheSiesModel lNotRetMod = new NotificheSiesModel();
				/*lNotRetMod = */lCtrl2.ExInserisciNotificheSies(lNotMod);
			}

		}
		if (flagModifica
				&& lFasMod.getFlagTrasmissione().compareTo(lFasModSession.getFlagTrasmissione()) != 0) {
			if (lFasMod.getFlagTrasmissione().compareTo("S") != 0) {
				// inserisco notifica annullo la riga precedente
				NotificheSiesModel lNotMod = new NotificheSiesModel();

				lNotMod.setAnnoSiep(lFasModSession.getChiaveAnnoSiep());
				lNotMod.setUfficioSiep(lFasModSession.getChiaveUfficioSiep());
				lNotMod.setProgSiep(lFasModSession.getChiaveProgrSiep());
				lNotMod.setIdFascicoloBdmc(lFasModSession.getIdFascicoloBdmc());
				lNotMod.setAnnoFascBdmc(lFasModSession.getChiaveAnnoBdmc());
				lNotMod.setNumeroFascBdmc(lFasModSession.getChiaveProgrBdmc());
				lNotMod.setUfficioFascBdmc(lFasModSession.getChiaveUfficioBdmc());
				lNotMod.setTipoNotifica("C");
				lNotMod.setDataNotifica(DateUtils.getSysDate());
				lNotMod.setStatoTrasmissione("N");
				lNotMod.setCodOperatoreInserimento(this.getUtenteConnesso().getUserId());
				lNotMod.setDataInserimento(DateUtils.getSysDate());
				lNotMod.setCodUfficioInserimento(this.getUfficioUtenteConnesso().getCodUfficio());

//				NotificheSiesModel lNotRetMod = new NotificheSiesModel();
				/*lNotRetMod = */lCtrl2.ExInserisciNotificheSies(lNotMod);

				// ===================================================
				// Inserimento della nuova notifica verso BDMC
				// (La notifica prece)
				// ===================================================
				/*
				 * temporaneamente disattivata in attesa di un check con ois lNotMod = new
				 * NotificheSiesModel(); lNotMod.setAnnoSiep( getRequestBigDecimalParameter (
				 * CAMPO_CHIAVE_ANNO_SIEP) ); lNotMod.setUfficioSiep(getRequestStringParameter (
				 * CAMPO_CHIAVE_UFFICIO_SIEP) ); lNotMod.setProgSiep(getRequestBigDecimalParameter (
				 * CAMPO_CHIAVE_PROGR_SIEP) );
				 * lNotMod.setIdFascicoloBdmc(lFasModSession.getIdFascicoloBdmc());
				 * lNotMod.setAnnoFascBdmc(getRequestBigDecimalParameter ( CAMPO_CHIAVE_ANNO_BDMC));
				 * lNotMod.setUfficioFascBdmc
				 * (getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter
				 * (CAMPO_COD_TIPO_AUTORITA_EMITTENTE),getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
				 * lNotMod.setNumeroFascBdmc(getRequestBigDecimalParameter ( CAMPO_CHIAVE_PROGR_BDMC) );
				 * lNotMod.setTipoNotifica ("A"); lNotMod.setDataNotifica (DateUtils.getSysDate());
				 * lNotMod.setStatoTrasmissione("N");
				 * lNotMod.setCodOperatoreInserimento(this.getUtenteConnesso().getUserId());
				 * lNotMod.setDataInserimento(DateUtils.getSysDate());
				 * lNotMod.setCodUfficioInserimento(this.getUfficioUtenteConnesso().getCodUfficio());
				 * lNotRetMod = new NotificheSiesModel(); lNotRetMod=lCtrl2.ExInserisciNotificheSies(lNotMod);
				 */
				// inserisco associazione corrente
			}
		}

		// ===================================================
		// Inserimento della nuova notifica verso BDMC
		// ===================================================
		if (!flagModifica
				&& lFasMod.getFlagTrasmissione().compareTo(lFasModSession.getFlagTrasmissione()) == 0) {
		}

		else {

			NotificheSiesModel lNotMod = new NotificheSiesModel();

			lNotMod.setAnnoSiep(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_SIEP));
			lNotMod.setUfficioSiep(getRequestStringParameter(CAMPO_CHIAVE_UFFICIO_SIEP));
			lNotMod.setProgSiep(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_SIEP));
			lNotMod.setIdFascicoloBdmc(lFasModSession.getIdFascicoloBdmc());
			lNotMod.setAnnoFascBdmc(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_BDMC));
			lNotMod.setUfficioFascBdmc(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
					getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
			lNotMod.setNumeroFascBdmc(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_BDMC));

			if (lFasMod.getFlagTrasmissione().compareTo("S") == 0)
				lNotMod.setTipoNotifica("A");
			else
				lNotMod.setTipoNotifica("C");
			lNotMod.setDataNotifica(DateUtils.getSysDate());
			lNotMod.setStatoTrasmissione("N");
			lNotMod.setCodOperatoreInserimento(this.getUtenteConnesso().getUserId());
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(this.getUfficioUtenteConnesso().getCodUfficio());

//			NotificheSiesModel lNotRetMod = new NotificheSiesModel();
			/*lNotRetMod = */lCtrl2.ExInserisciNotificheSies(lNotMod);
		}

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		String lPage = "";

		lPage = ISIAPCostantiWeb.PG_MAIN + "?" + ISIAPCostantiWeb.ACTION_FIELD
				+ "=siap.bdmc.fascicolosiepbdmc.action.ActLoadDettaglioFascicoloSiepBdmc";
		lPage += "&" + CAMPO_ID_FASCICOLO_BDMC + "=" + lFasModSession.getIdFascicoloBdmc().toString();
		lPage += "&" + CAMPO_COD_TIPO_AUTORITA_EMITTENTE + "="
				+ getRequestStringParameter(CAMPO_DESCR_AUTEMI_BDMC).toString() + " ("
				+ getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE).toString() + ")";

		return lPage;
	}

}