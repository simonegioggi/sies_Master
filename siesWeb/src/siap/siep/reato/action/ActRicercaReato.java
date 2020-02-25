package siap.siep.reato.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.controller.ReatoContinuazioneController;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActRicercaReato
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Reato
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
public class ActRicercaReato extends ActionSiap implements ICostantiReato {

	@SuppressWarnings({ "rawtypes", "unchecked" })
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

		ReatoModel lReaMod = new ReatoModel();
		lReaMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());

		if (!isRequestParameterNullObj(CAMPO_PROGR_REATO)) {
			lReaMod.setProgrReato(getRequestBigDecimalParameter(CAMPO_PROGR_REATO));
		}

		Vector lVect = new Vector();
		Vector lVectCir = new Vector();
		String lReturnPage = "";

		// AMBROSINO 04/2011 --> vengo da CopiaReati - campo nuovo CAMPO_NUM_REATI_N in request

		Vector lVectSing = new Vector();
		Vector lVectCirSing = new Vector();

		String[] ReatoSingolo = null;
		String StrNumdeiReati = null;

		if (!isRequestParameterNullObj(CAMPO_NUM_REATI_N)) {
			String[] ArrNumdeiReati = getRequestStringParameters(CAMPO_NUM_REATI_N);
			StrNumdeiReati = ArrNumdeiReati[0];
			ReatoSingolo = StrNumdeiReati.split(",");
		}

		if (StrNumdeiReati != null && StrNumdeiReati != "") {
			String NRea = null;
			for (int i = 0; i < ReatoSingolo.length; i++) {
				NRea = ReatoSingolo[i];
				BigDecimal ProgRea = new BigDecimal(NRea);
				lReaMod.setProgrReato(ProgRea);

				lVectSing = null;
				IReato lCtrl = SIEPLookupRemote.getReatoRemote();
				lVectSing = lCtrl.ExRicercaReato(lReaMod);

				Iterator itx = lVectSing.iterator();
				while (itx.hasNext()) {
					lVect.add(itx.next());
				}

				CircostanzaModel lCirMod = new CircostanzaModel();
				lReturnPage = "";
				lCirMod.setFasSieIdFascicoloSiep(lReaMod.getFasSieIdFascicoloSiep());

				lVectCirSing = null;
				ICircostanza lCtrlCir = SIEPLookupRemote.getCircostanzaRemote();
				lVectCirSing = lCtrlCir.ExRicercaCircostanzaNoErr(lCirMod);

				Iterator itx1 = lVectCirSing.iterator();
				while (itx.hasNext()) {
					lVectCir.add(itx1.next());
				}

				/*
				 * int k=0; while (itx1.hasNext()) { lVectCir.addElement(lVectCirSing.get(k)); if(k <
				 * lVectCirSing.size()) k++; }
				 */
			}

		} else {
			// END AMBROSINO 04/2011 --> vengo da CopiaReati

			IReato lCtrl = SIEPLookupRemote.getReatoRemote();
			lVect = lCtrl.ExRicercaReato(lReaMod);
			CircostanzaModel lCirMod = new CircostanzaModel();
			lReturnPage = "";
			lCirMod.setFasSieIdFascicoloSiep(lReaMod.getFasSieIdFascicoloSiep());

			ICircostanza lCtrlCir = SIEPLookupRemote.getCircostanzaRemote();
			lVectCir = lCtrlCir.ExRicercaCircostanzaNoErr(lCirMod);
		}

		setRequestAttribute("circostanza", lVect);
		ReatoContinuazioneController lRCtrl = new ReatoContinuazioneController();
		setRequestAttribute("continuazioni", lRCtrl.getTableContinuazioni(lVect));
		setSessionAttribute("reato", lVect.get(0));
		setRequestAttribute("reati", lVect);
		setRequestAttribute("Circ_reati", lVectCir);

		lReturnPage = PG_RICERCAREATO;
		return lReturnPage;

	} // Chiude request

} // Chiude Classe
