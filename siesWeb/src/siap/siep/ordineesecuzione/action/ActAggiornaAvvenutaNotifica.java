package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActAggiornaAvvenutaNotifica
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Scadenzario
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
@SuppressWarnings("rawtypes")
public class ActAggiornaAvvenutaNotifica extends ActionSiap implements ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		BigDecimal lflag = this.getRequestBigDecimalParameter("flag");
		FascicoloSiepModel lFasc = (FascicoloSiepModel) this.getSessionAttribute("fascicolo");

		if (lflag.intValue() == 1) {
			String lArrayAbilitaUno = this
					.getRequestStringParameter(ICostantiOrdineEsecuzione.ABILITA_NOTIFICA);
//			String lIdSogUno = this.getRequestStringParameter(ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO);
			NotificaModel lNotModUno = new NotificaModel();

			String lGiornoNotUno = this
					.getRequestStringParameter(ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA);
			String lMeseNotUno = this
					.getRequestStringParameter(ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA);
			String lAnnoNotUno = this
					.getRequestStringParameter(ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA);

			lNotModUno.setDataAvvenutaNotifica(DateUtils.getDate(lAnnoNotUno, lMeseNotUno, lGiornoNotUno));
			lNotModUno.setIdNotifica(new BigDecimal(lArrayAbilitaUno));
			lNotModUno.setEveIdEvento(this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
			lNotModUno.setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
			lNotModUno.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			lNotModUno.setDataAggiornamento(DateUtils.getSysDate());
			lNotModUno.setCodEsito("01");

			NotificaModel[] lNotModArray = new NotificaModel[1];
			lNotModArray[0] = lNotModUno;

			/*
			 * if(!lIdSogUno.equals("null")) { ScadenzarioModel lScaMod = new ScadenzarioModel();
			 * lScaMod.setCodTipoScadenzario("01");
			 * lScaMod.setDataInizioScadenza(lNotModArray[0].getDataAvvenutaNotifica());
			 * 
			 * lScaMod.setDataFineScadenza(DateUtils.moveDateTo(lNotModArray[0].getDataAvvenutaNotifica(),java.
			 * util.Calendar.DAY_OF_MONTH,30));
			 * 
			 * lScaMod.setFlagVisto("N"); //lScaMod.setDataVisto( getRequestDateParameter(
			 * CAMPO_ANNO_DATA_VISTO,CAMPO_MESE_DATA_VISTO,CAMPO_GIORNO_DATA_VISTO) ); UtenteModel lUtenteMod
			 * = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
			 * lScaMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
			 * lScaMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());
			 * lScaMod.setDataInserimento(DateUtils.getSysDate()); // lScaMod.setFasSieIdFascicoloSiep(
			 * getRequestBigDecimalParameter( lScaMod.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );
			 * lScaMod.setFasSieIdFascicoloSiep(lFasc.getIdFascicoloSiep());
			 * 
			 * IScadenzario lCtrl = SIEPLookupRemote.getScadenzarioRemote(); ScadenzarioModel llScaModRet =
			 * lCtrl.ExInserisciScadenzario(lScaMod);
			 * 
			 * }
			 */

			IOrdineEsecuzione lCtrlUno = SIEPLookupRemote.getOrdineEsecuzioneRemote();
			Vector lEveVectUno = new Vector();
			lEveVectUno = lCtrlUno.ExAggiornaAvvenutaNotifica(lNotModArray, lFasc.getIdFascicoloSiep(), true);

			setRequestAttribute("notifiche", lEveVectUno);
			String lPage = "";
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordineesecuzione.action.ActDettaglioScadenzarioEvento&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "="
					+ ((NotificaModel) lEveVectUno.get(0)).getEveIdEvento().toString();
			return lPage;
		} else {
			// String[] lArrayNot =this.getRequestStringParameters(ICostantiNotifica.CAMPO_ID_NOTIFICA);
			String[] lArrayAbilita = this
					.getRequestStringParameters(ICostantiOrdineEsecuzione.ABILITA_NOTIFICA);
			NotificaModel lNotMod = null;
//			String[] lIdSog = this.getRequestStringParameters(ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO);
			// int lLungArray = lArrayNot.length;
			int lLungArrayAbilita = lArrayAbilita.length;

			String lIdNot = null;
			NotificaModel[] lNotModArray = new NotificaModel[lLungArrayAbilita];

			// chiama il controller
			String[] lGiornoNot = this
					.getRequestStringParameters(ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA);
			String[] lMeseNot = this
					.getRequestStringParameters(ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA);
			String[] lAnnoNot = this
					.getRequestStringParameters(ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA);

			// int lLungData = lGiornoNot.length;
			String lGG = null;
			String lMM = null;
			String lAA = null;

			for (int i = 0; i < lLungArrayAbilita; i++) {

				if (lGiornoNot[i] != null && !lGiornoNot[i].equals("")) {
					lNotMod = new NotificaModel();
					lGG = lGiornoNot[i];
					lMM = lMeseNot[i];
					lAA = lAnnoNot[i];
					lIdNot = lArrayAbilita[i];

					lNotMod.setDataAvvenutaNotifica(DateUtils.getDate(lAA, lMM, lGG));
					lNotMod.setIdNotifica(new BigDecimal(lIdNot));
					lNotMod.setEveIdEvento(this
							.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
					lNotMod.setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
					lNotMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					lNotMod.setDataAggiornamento(DateUtils.getSysDate());
					lNotMod.setCodEsito("01");
					lNotModArray[i] = lNotMod;

					/*
					 * if (!lIdSog[i].equals("null")) { FascicoloSiepModel lFasc =
					 * (FascicoloSiepModel)this.getSessionAttribute("fascicolo"); ScadenzarioModel lScaMod =
					 * new ScadenzarioModel();
					 * 
					 * lScaMod.setCodTipoScadenzario("01");
					 * lScaMod.setDataInizioScadenza(lNotModArray[i].getDataAvvenutaNotifica());
					 * 
					 * lScaMod.setDataFineScadenza(DateUtils.moveDateTo(lNotModArray[i].getDataAvvenutaNotifica
					 * (),java.util.Calendar.DAY_OF_MONTH, 30));
					 * 
					 * lScaMod.setFlagVisto("N"); //lScaMod.setDataVisto( getRequestDateParameter(
					 * CAMPO_ANNO_DATA_VISTO,CAMPO_MESE_DATA_VISTO,CAMPO_GIORNO_DATA_VISTO) ); UtenteModel
					 * lUtenteMod = new
					 * UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO
					 * )); lScaMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
					 * lScaMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());
					 * lScaMod.setDataInserimento(DateUtils.getSysDate()); //
					 * lScaMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter(
					 * lScaMod.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );
					 * lScaMod.setFasSieIdFascicoloSiep(lFasc.getIdFascicoloSiep()); //---Aggiungere in
					 * SIEPLookupRemote il metodo getScadenzarioRemote() IScadenzario lCtrl =
					 * SIEPLookupRemote.getScadenzarioRemote(); ScadenzarioModel llScaModRet =
					 * lCtrl.ExInserisciScadenzario(lScaMod); }
					 */

					// }
				}
			}

			IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
			Vector lEveVect = new Vector();
			lEveVect = lCtrl.ExAggiornaAvvenutaNotifica(lNotModArray, lFasc.getIdFascicoloSiep(), true);
			setRequestAttribute("notifiche", lEveVect);

			String lPage = "";
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.ordineesecuzione.action.ActDettaglioScadenzarioEvento&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "="
					+ ((NotificaModel) lEveVect.get(0)).getEveIdEvento().toString();
			return lPage;
		}
		// return PG_AGGIORNA_AVVENUTA_NOTIFICA;
	}

}