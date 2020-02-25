package siap.siep.archiviazione.action;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;

public class ActLoadInsArchSemplificataRES extends ActionSiap implements ICostantiArchiviazione {

	/**
	 * L'Archiviazione semplificata procederà
	 */
	public String processRequest() throws Exception {

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// Di competenza
		this.isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// Controllo Fascicolo definito
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " risulta già Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// =================================================================
		// Fascicolo Migrato RES !!!!!!!!!!!!
		// n.b. la funzione è disponibili solo per i fascicoli migrati RES
		// =================================================================
		if (lFascMod.getCodOperatoreInserimento().indexOf("res") < 0) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Funzione disponibile SOLO per i procedimenti migrati RES. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Assenza di eventi non validati
		this.isEventoNonValidato();

		// ==========================================================================
		// Non sono necessari ulteriori controlli sul fascicolo in quanto trattasi di
		// Archiviazione semplificata che è di fatto una forzatura utile per
		// archiviare anche i fascicoli incompleti
		// ==========================================================================

		/******************************* Posizione Giuridica **********************************/
		// IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		//
		// PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltra =
		// lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.getIdFascicoloSiep());
		//
		// if (lPosLuoAltra == null || lPosLuoAltra.getPosizioneGiuridica() == null)
		// {
		// RedirectTo lRedirigi = new RedirectTo();
		// lRedirigi.setPage(IWebConstants.PG_MAIN);
		// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno() +
		// "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
		// lRedirigi.setAction( "siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&" +
		// ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		//
		// return IWebConstants.PG_MESSAGE;
		// }
		//
		// setRequestAttribute("posizioneluogoaltra", lPosLuoAltra);
		//
		/******************************* Pena Complessiva *****************************/
		// IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		// PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);
		//
		// if (lPenComMod == null)
		// throw new SIEPException(SIEPException.USER_MESSAGE, "Pena Complessiva non presente. Impossibile
		// eseguire la richiesta.");
		//
		// String lFlagErgastolo = "N";
		// // se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		// if( lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "" )
		// {
		// if(lPenComMod.getCodTipoPenaDetentiva().equals("03"))
		// {
		// lFlagErgastolo = "S";
		// }
		// else if(lPenComMod.getCodTipoPenaDetentiva().equals("04"))
		// {
		// lFlagErgastolo = "D";
		// }
		// }
		//
		// setRequestAttribute("flagergastolo", lFlagErgastolo);
		/******************************* Fine Pena Complessiva ************************/

		/*********************************** Pena Residua ***************************/
		// IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		//
		// PenaResiduaModel lPenaResidua =
		// lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		// if (lPenaResidua == null)
		// {
		// RedirectTo lRedirigi = new RedirectTo();
		// lRedirigi.setPage(IWebConstants.PG_MAIN);
		// setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Residua da Espiare Inesistente. Eseguire
		// Calcolo della pena?");
		// lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&" +
		// ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
		// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
		//
		// return IWebConstants.PG_MESSAGE;
		// }
		//
		// setRequestAttribute("penaresidua", lPenaResidua);

		/******************************************************************************/
		// IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		// MagistratoCompetenteMagistratoModel lMagMod =
		// lMagComp.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		// if (lMagMod != null)
		// setRequestAttribute("magistratocompetente", lMagMod);
		//

		return PG_LOAD_INS_ARCH_SEMPL_RES;
	}

}