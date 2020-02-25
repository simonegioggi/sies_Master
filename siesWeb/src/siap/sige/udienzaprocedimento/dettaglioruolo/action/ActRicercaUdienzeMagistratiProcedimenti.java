package siap.sige.udienzaprocedimento.dettaglioruolo.action;

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

//import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
//import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
//import siap.sius.ActionSius;
import siap.sico.util.SICOLookupRemote;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
//import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
//import siap.sius.util.SIUSLookupRemote;
import siap.sius.udienza.action.ICostantiUdienza;
import siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento;

/**
 * <p>
 * Title: ActRicercaUdienzeMagistratiProcedimenti
 * </p>
 * <p>
 * Description: Classe Action per la ricerca dei Procedimenti raggruppati per Magistrati Relatori e per
 * Udienze in un arco di date selezionato.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2020
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * 
 * @version 1.0
 */

public class ActRicercaUdienzeMagistratiProcedimenti extends ActionSige implements ICostantiUdienza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		String lRetPage = ICostantiUdienzaProcedimento.PG_LISTA_UDIENZE_MAGISTRATI_NUMPROC;

		setLinkRitorno();

		// Switch fra Ricerca x Udienze e Magistrati e Ricerca x Magistrati, Udienze
		if (!isRequestParameterNullObj("tiporicerca") && getRequestStringParameter("tiporicerca")
				.equalsIgnoreCase(ICostantiUdienzaProcedimento.CARICO_MAGISTRATI)) {
			lRetPage = ICostantiUdienzaProcedimento.PG_LISTA_MAGISTRATI_UDIENZE_NUMPROC;
			String lVisualizzaUdienza = "SI";

			if (!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
				lVisualizzaUdienza = (String) getSessionAttribute("VisualizzaUdienza");

			if (lVisualizzaUdienza.equalsIgnoreCase("SI"))
				setSessionAttribute("VisualizzaUdienza", "NO");
			else
				setSessionAttribute("VisualizzaUdienza", "SI");
		}

		// Popola il model UdienzaModel con le date prelevata dalla request.
		UdienzaSigeModel lUdienzaMod = new UdienzaSigeModel();

		/*
		 * OLD
		 * lUdienzaMod.setDataUdienza(getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA,CAMPO_MESE_DATA_UDIENZA,
		 * CAMPO_GIORNO_DATA_UDIENZA));
		 * lUdienzaMod.setDataUdienzaFine(getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA_FINE,
		 * CAMPO_MESE_DATA_UDIENZA_FINE,CAMPO_GIORNO_DATA_UDIENZA_FINE));
		 */

		Date[] lRangeDate = {
				getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA, CAMPO_MESE_DATA_UDIENZA,
						CAMPO_GIORNO_DATA_UDIENZA),
				getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA_FINE, CAMPO_MESE_DATA_UDIENZA_FINE,
						CAMPO_GIORNO_DATA_UDIENZA_FINE) };

		lUdienzaMod.setDateUdienze(lRangeDate);

		// Periodo di date udienze passato alla request
		/*
		 * OLD String lDataIniziale = " " +
		 * StringUtils.toStringJSP(DateUtils.getDateToString(lUdienzaMod.getDataUdienza(),"dd-MM-yyyy"), "-");
		 * String lDataFinale = " " +
		 * StringUtils.toStringJSP(DateUtils.getDateToString(lUdienzaMod.getDataUdienzaFine(),"dd-MM-yyyy"),
		 * "-");
		 */

		// Range di date per filtro date.
		String lDataIniziale = StringUtils
				.toStringJSP(DateUtils.getDateToString(lUdienzaMod.getDateUdienze()[0], "dd-MM-yyyy"), "-");
		String lDataFinale = StringUtils
				.toStringJSP(DateUtils.getDateToString(lUdienzaMod.getDateUdienze()[1], "dd-MM-yyyy"), "-");

		String lPeriodo = " Udienze tra  " + lDataIniziale + " e   " + lDataFinale;

		setRequestAttribute("periodo", lPeriodo);

		// La ricerca è sempre filtrata per ufficio
		lUdienzaMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		// L'Udienza viene passata alla jsp perchè i dati servono alla funzione di stampa
		setRequestAttribute("udienza", lUdienzaMod);

		// Chiama la RemoteInterfacce del controller udienza per attivare la ricerca.
		IUdienzaProcedimentoSige lCtrlUdienza = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();

		Collection<Object> lLista = lCtrlUdienza.ExRicercaUdienzeMagistratiProcedimentiByDate(lUdienzaMod);

		setRequestAttribute("procedimenti", lLista);

		// Carichi X Magistrato non prevede la funzione di stampa
		if (isRequestParameterNullObj("tiporicerca") || !getRequestStringParameter("tiporicerca")
				.equalsIgnoreCase(ICostantiUdienzaProcedimento.CARICO_MAGISTRATI))
			gestioneTemplate();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lRetPage; // restituisce la jsp di VIEW
	}

	/**
	 * Funzione per la generazione dei possibili template da utilizzare nella stampa.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void gestioneTemplate() throws Exception {

		// Template
		TemplateModel lTempRic = new TemplateModel();
		// Solo di prova
		lTempRic.setCodTipoProvvedimento("92");

		ITemplate lTemCtrl = SICOLookupRemote.getTemplateRemote();
		Collection lTemplate = lTemCtrl.ExListaCbxTemplate(lTempRic);
		Option lOptTemplate = new Option(lTemplate);
		setRequestAttribute("ElencoTemplate", "" + lOptTemplate);
		setRequestAttribute("AutoTemplate", null);
		setRequestAttribute("Stampabile", "SI");
	}

}