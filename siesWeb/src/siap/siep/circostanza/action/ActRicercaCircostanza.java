package siap.siep.circostanza.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaCircostanza
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Circostanza
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

public class ActRicercaCircostanza extends ActionSiap implements ICostantiCircostanza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		if (!this.isRequestAttributeNullObj("lTipoFunzione")) // paramentro passato solo nel caso di
																// iscrizione guidata
		{
			this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
		}

		// paramentro passato solo nel caso di iscrizione guidata E VENGO DA DETTAGLIO SOGGETTO
		if (!this.isRequestParameterNullObj("lTipoFunzione")) {
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		CircostanzaModel lCirMod = new CircostanzaModel();

		/*
		 * lCirMod.setIdCircostanza( getRequestBigDecimalParameter( CAMPO_ID_CIRCOSTANZA) );
		 * lCirMod.setCodTipoCircostanza( getRequestStringParameter( CAMPO_COD_TIPO_CIRCOSTANZA) );
		 * lCirMod.setCodFonte( getRequestStringParameter( CAMPO_COD_FONTE) ); lCirMod.setAnnoFonte(
		 * getRequestBigDecimalParameter( CAMPO_ANNO_FONTE) ); lCirMod.setNumeroFonte(
		 * getRequestStringParameter( CAMPO_NUMERO_FONTE) ); lCirMod.setCodSottonumerazione(
		 * getRequestStringParameter( CAMPO_COD_SOTTONUMERAZIONE) ); lCirMod.setComma(
		 * getRequestStringParameter( CAMPO_COMMA) ); lCirMod.setLettera( getRequestStringParameter(
		 * CAMPO_LETTERA) ); lCirMod.setNumero( getRequestStringParameter( CAMPO_NUMERO) );
		 * lCirMod.setArticolo( getRequestStringParameter( CAMPO_ARTICOLO) ); lCirMod.setNote(
		 * getRequestStringParameter( CAMPO_NOTE) );
		 */

		FascicoloSiepModel lFasMod = ((FascicoloSiepModel) getSessionAttribute("fascicolo"));
		lCirMod.setFasSieIdFascicoloSiep(lFasMod.getIdFascicoloSiep());
		Vector lVect = null;
		ICircostanza lCtrl = SIEPLookupRemote.getCircostanzaRemote();

		try {
			lVect = lCtrl.ExRicercaCircostanza(lCirMod);
		} catch (Exception e) {
			// Nessun Elemento Trovato
			throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento Trovato");
		}

		for (int i = 0; i < lVect.size(); i++) {
			CircostanzaModel lCirMod1 = (CircostanzaModel) lVect.get(i);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lCirMod1 = " + i + " " + lCirMod1);
		}

		// ********************************************************************************************
		// Federica - a9-rr-078
		/*
		 * Vector lNewVectCirc = CircostanzaUtil.creaVectorCircostanze(lVect);
		 * 
		 * if(lVect != null && lVect.size()>0) { for (int i = 0; i < lNewVectCirc.size(); i++) {
		 * CircostanzaModel CiReato = (CircostanzaModel) lNewVectCirc.get(i); } }
		 * 
		 * // setRequestAttribute("circostanza", lNewVectCirc);
		 * 
		 * if(lVect != null && lVect.size()>0) { for (int y = 0; y < lVect.size(); y++) { CircostanzaModel
		 * CiReatoS = (CircostanzaModel) lVect.get(y); } Vector lNewVectCirc =
		 * CircostanzaUtil.creaVectorCircostanze(lVect);
		 * 
		 * for (int i = 0; i < lNewVectCirc.size(); i++) { CircostanzaModel CiReato = (CircostanzaModel)
		 * lNewVectCirc.get(i); }
		 * 
		 * setRequestAttribute("circostanza", lNewVectCirc); } else { throw new
		 * SIEPException(SIEPException.USER_MESSAGE,"Nessun Elemento Trovato"); }
		 * 
		 * //Poichè alcuni campi editabili nella pagina LoadInserimentoCircostanza.jsp //sono in realtà campi
		 * della tabella SENTENZA //nel dettaglio viene ricaricato il fascicolo (e quindi sentenza e soggetto)
		 * //e messo in sessione: //in questo modo il model del fascicolo già in sessione resta allineato con
		 * //le eventuali modifiche fatte inserendo/modificando le circostanze IFascicoloSiep lCtrlFasSie =
		 * SIEPLookupRemote.getFascicoloSiepRemote(); lFasMod =
		 * lCtrlFasSie.ExRicercaFascicoloByKey(lFasMod.getIdFascicoloSiep());
		 * 
		 * setSessionAttribute("fascicolo", lFasMod);
		 * //******************************************************************************************
		 */
		setRequestAttribute("circostanza", lVect);
		return PG_RICERCACIRCOSTANZA;
	}
}