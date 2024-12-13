package siap.sius.esecuzionesanzionesostitutiva.action;

/**
 * <p>Title: ActModificaEsecuzioneSS</p>
 * <p>Description: Classe Azione di modifica dell' Esecuzione Sanzione Sostitutiva
 * </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Bull</p>
 */
import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

public class ActModificaEsecuzioneSS extends ActionSiap implements ICostantiEsecuzioneSS {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Istanzio il Model e lo carico con quello posto in sessione.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Istanzio il Model di EsecuzioneSanzioneSostitutiva.
		EsecuzioneSanzioneSostitutivaModel lESSMod = new EsecuzioneSanzioneSostitutivaModel();

		// Caricamento Esecuzione Sanzione Sostitutiva (solo dati modificati)
		lESSMod.setIdEsecuzioneSanzioneSost(getRequestBigDecimalParameter(CAMPO_ID_ESECUZIONE_SS));

		// Rilettura Esecuzione Sanzione Sostitutiva.
		IEsecuzioneSS lESSCtrl = SIUSLookupRemote.getEsecuzioneSSRemote();
		lESSMod = lESSCtrl.ExRicercaEsecuzioneSanzioneSostitutivaByKey(lESSMod.getIdEsecuzioneSanzioneSost());

		if (lESSMod == null || lESSMod.getIdEsecuzioneSanzioneSost().equals(null))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! ESECUZIONE SANZIONE SOSTITUTIVA Assente!");
		else
			setRequestAttribute("sanzioneSostitutiva", lESSMod);

		CalendarModel lCalModDurata = new CalendarModel();
		if (!this.isRequestParameterNullObj(CAMPO_ANNO_TERMINE_ATTUALE))
			lESSMod.setNumAnniSanzione(getRequestBigDecimalParameter(CAMPO_ANNO_TERMINE_ATTUALE));
		lCalModDurata.setNumAnni(getRequestBigDecimalParameter(CAMPO_ANNO_TERMINE_ATTUALE));
		if (!this.isRequestParameterNullObj(CAMPO_MESE_TERMINE_ATTUALE))
			lESSMod.setNumMesiSanzione(getRequestBigDecimalParameter(CAMPO_MESE_TERMINE_ATTUALE));
		lCalModDurata.setNumMesi(getRequestBigDecimalParameter(CAMPO_MESE_TERMINE_ATTUALE));
		if (!this.isRequestParameterNullObj(CAMPO_GIORNO_TERMINE_ATTUALE))
			lESSMod.setNumGiorniSanzione(getRequestBigDecimalParameter(CAMPO_GIORNO_TERMINE_ATTUALE));
		lCalModDurata.setNumGiorni(getRequestBigDecimalParameter(CAMPO_GIORNO_TERMINE_ATTUALE));
		lESSMod.setLuogoEsecuzioneSanzione(getRequestStringParameter(CAMPO_LUOGO_ESECUZIONE_SANZIONE));

		// se ho modificato almeno un quantum ricalcolo la data fine
		if ((!this.isRequestParameterNullObj(CAMPO_ANNO_TERMINE_ATTUALE)
				|| !this.isRequestParameterNullObj(CAMPO_MESE_TERMINE_ATTUALE)
				|| !this.isRequestParameterNullObj(CAMPO_GIORNO_TERMINE_ATTUALE))
				&& lESSMod.getDataInizioSanzione() != null) {
			ICalcoloPena lCal = SIEPLookupRemote.getCalcoloPenaRemote();
			lESSMod.setDataTermineIniziale(
					lCal.exCalcolaNuovaDataFine(lESSMod.getDataInizioSanzione(), lCalModDurata, false));
			lESSMod.setDataTermineAttuale(
					lCal.exCalcolaNuovaDataFine(lESSMod.getDataInizioSanzione(), lCalModDurata, false));
		}
		//

		lESSMod.setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice dell'operatore che modifica
		lESSMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice dell'operatore che
																			// inserisce
		lESSMod.setDataAggiornamento(DateUtils.getSysDate());

		// Aggiornamento Esecuzione Sanzione Sostitutiva.
		lESSMod = lESSCtrl.ExModificaEsecuzioneSanzioneSostitutiva(lESSMod);
		
        // MEV_2023-35 Recupero GP per determinare il contenuto e passarlo al CTRL
        String lCodContenuto = "";
	    if (!lESSMod.getGenPridGeneraleProcedimento().equals(null)) {
            IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
            FascicoloGPModel lFasGPModel = lFasCtrl
                    .ExRicercaFascicoloByGenProc(lESSMod.getGenPridGeneraleProcedimento());
            setRequestAttribute("sanzioneUno", lFasGPModel);
            lCodContenuto = lFasGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
        }
	      
	    // MEV_2023-35 si aggiunge il parametro lCodContenuto alla chiamata
		Vector lVect = lESSCtrl.ExRicercaDettaglioEsecuzioneSS(
				getRequestBigDecimalParameter(CAMPO_ID_ESECUZIONE_SS),
				lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto(), this.getCodUfficioUtenteConnesso(), lCodContenuto);


		setRequestAttribute("sanzioni", lVect);

		// Leggo anche il fascicolo SIEP.
		BigDecimal lIdFascicoloSiep = null;
		if (!((getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP)) == null
				|| (getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP)).trim()
						.compareTo("null") == 0
				|| (getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP)).trim()
						.length() == 0)) {
			lIdFascicoloSiep = new BigDecimal(
					(getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP)));
			IFascicoloSiep lFSiepCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			DettaglioFascicoloModel lDettaglio = lFSiepCtrl.ExDettaglioFascicoloSiep(lIdFascicoloSiep);

			if (lDettaglio == null)
				throw new F3BException(SIUSException.USER_MESSAGE, "Fascicolo SIEP non individuato");

			setRequestAttribute("dettaglioFascSiep", lDettaglio);
		}

		// Bottone di ritorno
		// Nella gestione del bottone di ritorno non si passa per la modifica
		// In questo caso per ritornare sul dettaglio si posiziona a mano LINK_RITORNO
		// tecnica non sicura.
		// this.gestioneRitorno();
		// if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
		setRequestAttribute(IWebConstants.LINK_RITORNO, "20");
		String lReturnPage = "";
		lReturnPage = PG_DETTAGLIO_ESECUZIONE_SS;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}