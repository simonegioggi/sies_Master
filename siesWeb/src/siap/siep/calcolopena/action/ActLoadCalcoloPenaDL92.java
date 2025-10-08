package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.penacomplessiva.action.ICostantiPenaComplessiva;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action pa la load della jsp dei calcoli della pena virtuale del nuovo decreto Nordio
 *
 * @since MEV_2024-092
 */
public class ActLoadCalcoloPenaDL92 extends ActCalcoloPenaMain implements ICostantiCalcoloPena {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		if (!this.isRequestParameterNullEmptyObj(ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE)) {
			// tasto "indietro" dalla form di calcolo
			CalcoloPenaModel lCalcoloPenaMod = new CalcoloPenaModel();
			PenaComplessivaModel lPenComp = new PenaComplessivaModel();
			lPenComp.setNumAnniReclusione   (getRequestBigDecimalParameter(ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE));
			lPenComp.setNumMesiReclusione   (getRequestBigDecimalParameter(ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE));
			lPenComp.setNumGiorniReclusione (getRequestBigDecimalParameter(ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE));

			lPenComp.setNumAnniArresto   (getRequestBigDecimalParameter(ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO));
			lPenComp.setNumMesiArresto   (getRequestBigDecimalParameter(ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO));
			lPenComp.setNumGiorniArresto (getRequestBigDecimalParameter(ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO));

			lCalcoloPenaMod.setPenaInSentenza(lPenComp);
			
			MisuraCautelareModel lMisCaut = new MisuraCautelareModel();
			lMisCaut.setIdMisuraCautelare(new BigDecimal(999));
			lMisCaut.setNumAnni   (getRequestBigDecimalParameter(ICostantiCalcoloPena.CAMPO_NUM_ANNI_PRESOFFERTO));
			lMisCaut.setNumMesi   (getRequestBigDecimalParameter(ICostantiCalcoloPena.CAMPO_NUM_MESI_PRESOFFERTO));
			lMisCaut.setNumGiorni (getRequestBigDecimalParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_PRESOFFERTO));
			//lMisCaut.setCodTipoMisura("CA");
			//lMisCaut.setFlagComputabile("S");
			
			setRequestAttribute("lPresoffertoMod", lMisCaut);
			
			lCalcoloPenaMod.setTipoPenaIniziale(PENA_IN_SENTENZA);
			
			setRequestAttribute("lCalcoloPenaMod", lCalcoloPenaMod);
			
			// Posizione Giuridica
			Date lDataInizioPena = null;
			if ("D".equals(getRequestStringParameter(ICostantiCalcoloPena.CAMPO_POSIZIONE_GIURIDICA))) {
				lDataInizioPena = getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA,
														  ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA,
														  ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA);
			}
			setRequestAttribute("lDataInizioPena", lDataInizioPena);
		}
		else if (!this.isSessionAttributeNullObj("fascicolo")) {
		  // Verifico se ho il fascicolo in sessione in questo caso precarico i dati della pena
			BigDecimal lFascID = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
			BigDecimal lIdEvento = null;

			// ==========================================================================
			// Recupero la PENA COMPLESSIVA per verificare se trattasi di ergastolo
			// ==========================================================================
			PenaComplessivaModel lPenComplMod = new PenaComplessivaModel();
			lPenComplMod.setFasSieIdFascicoloSiep(lFascID);
			IPenaComplessiva lPCon = SIEPLookupRemote.getPenaComplessivaRemote();
			Vector lPComples = lPCon.ExRicercaPenaComplessivaNoError(lPenComplMod);

			if (lPComples.size() == 0) {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Complessiva mancante");
				lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
						+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
						+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return IWebConstants.PG_MESSAGE;
			}
			lPenComplMod = (PenaComplessivaModel) (lPComples.get(0));

			setRequestAttribute("lPenComplMod", lPenComplMod);

			// ==========================================================================
			// Recupero i dati della pena
			// ==========================================================================
			CalcoloPenaModel lCalcoloPenaMod = null;

			lCalcoloPenaMod = super.calcoloPena(lFascID, lIdEvento);

			Date lDataInizioPena = super.getDataDecorrenzaPena(lFascID, lIdEvento);
			setRequestAttribute("lCalcoloPenaMod", lCalcoloPenaMod);
			setRequestAttribute("lDataInizioPena", lDataInizioPena);
		}

		return PG_LOAD_CALCOLOPENA_DL92;
	}

}