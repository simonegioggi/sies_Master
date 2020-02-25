package siap.sius.udienzaprocedimento.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.udienza.action.ICostantiUdienza;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaUdienzaProcedimento
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Procedimenti per Udienza
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
public class ActRicercaUdienzaProcedimento extends ActionSiap
		implements ICostantiUdienzaProcedimento, ICostantiUdienza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String mReturnPage = "";
	private BigDecimal mIdUdienza = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		setLinkRitorno();

		String lOrderBy = null;
		lOrderBy = getRequestStringParameter("tipo");

		String tiporicerca = getRequestStringParameter("tiporicerca");

		// 11/04/2007 Nuovi Parametri x Ricerca Procedimenti x Udienza.
		// 22/05/2007 e 05/06/2007 Impostato il nuovo attributo "Stato Procedimento".
		String tipoProc = "TUTTI";
		if (!isRequestParameterNullObj("tipoProc"))
			tipoProc = getRequestStringParameter("tipoProc");

		String lStatoProcedimento = "";
		if (isRequestChecked("CheckUnificazione"))
			lStatoProcedimento = "U";

		setRequestAttribute("tipoProc", tipoProc);
		setRequestAttribute("lStatoProcedimento", lStatoProcedimento);

		setRequestAttribute("tipo", lOrderBy);
		setRequestAttribute("tiporicerca", tiporicerca);

		// lettura ID Udienza
		mIdUdienza = getRequestBigDecimalParameter(CAMPO_UDI_ID_UDIENZA);
		Vector lVect = new Vector();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> tipoRicerca : " + tiporicerca);

		// Ricerca Lista procedimenti
		IUdienzaProcedimento lCtrlUdPr = SIUSLookupRemote.getUdienzaProcedimentoRemote();

		if (tiporicerca.compareTo("xdata") == 0) {
			if (this.ricercaUdienza()) {
				// 12/04/2007 Ricerca Procedimenti con aggiunti parametri tipoProc e lStatoProcedimento
				// lVect = lCtrlUdPr.ExRicercaProcedimentixUdienza(lIdUdienza,lOrderBy);
				lVect = lCtrlUdPr.ExRicercaProcedimentixUdienza(mIdUdienza, lOrderBy, lStatoProcedimento,
						tipoProc);
				TemplateModel lTempRic = new TemplateModel();
				lTempRic.setCodTipoProvvedimento("90");
				lTempRic.setFlagTemplate(getFlagTemplatePerCodTipoUfficio());// Imposta il flag template per
																				// tipo ufficio
				setListaCbxTemplate(lTempRic);

				mReturnPage = PG_LISTAPROCEDIMENTIXUDIENZA;
			}
		} else if (tiporicerca.compareTo("xmagistrato") == 0) {
			if (this.ricercaUdienza()) {
				// 12/04/2007 Ricerca Procedimenti con aggiunti parametri tipoProc e lStatoProcedimento
				// lVect = lCtrlUdPr.ExRicercaProcedimentixUdienza(lIdUdienza,"M"+lOrderBy);
				lVect = lCtrlUdPr.ExRicercaProcedimentixUdienza(mIdUdienza, "M" + lOrderBy,
						lStatoProcedimento, tipoProc);
				TemplateModel lTempRic = new TemplateModel();
				lTempRic.setCodTipoProvvedimento("91");
				setListaCbxTemplate(lTempRic);

				mReturnPage = PG_LISTAPROCEDIMENTIXUDIENZAMAG;
			}
		} else if (tiporicerca.compareTo("xdatacollegi") == 0) {
			Date lDataUdienza = getRequestDateParameter(ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA,
					ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA, ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA);

			lVect = lCtrlUdPr.ExRicercaProcedimentixDataUdienza(lDataUdienza, lOrderBy, lStatoProcedimento,
					tipoProc, this.getCodUfficioUtenteConnesso());

			TemplateModel lTempRic = new TemplateModel();
			lTempRic.setCodTipoProvvedimento("93");
			setListaCbxTemplate(lTempRic);

			setRequestAttribute("data_udi", lDataUdienza);
			mReturnPage = PG_LISTAPROCEDIMENTIXUDIENZATUTTICOLLEGI;
		}

		setRequestAttribute("procedimenti", lVect);

		return mReturnPage;
	}

	/**
	 * Metodo di ricercaUdienza
	 * <p>
	 * 
	 * @param aIdUdienza
	 */
	protected boolean ricercaUdienza() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".ricercaUdienza(): inizio");

		boolean lFlag = false;

		// Se è valorizzato l'id dell'udienza, si provvede a recuperare
		// i dati udienza corrispondenti. In caso contrario si esegue
		// la ricerca per data, ove in caso di pù udienze, s'imposta la
		// ReturnPage con la view di lista delle udienze selezionabili.
		if (mIdUdienza != null && !mIdUdienza.toString().equals("0")) {
			// Ricerca dettaglio Udienza.
			IUdienza lCtrlUd = SIUSLookupRemote.getUdienzaRemote();
			UdienzaModel lUdiMod = lCtrlUd.ExRicercaUdienzaByKey(mIdUdienza);
			setRequestAttribute("udienza", lUdiMod);

			lFlag = true;
		} else {
			// Ricerca Udienza
			Date lCampoDate = getRequestDateParameter(ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA,
					ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA, ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA);

			UdienzaModel lUdiModCond = new UdienzaModel();
			lUdiModCond.setDataUdienza(lCampoDate);
			lUdiModCond.setDataUdienzaFine(lCampoDate);
			lUdiModCond.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso()); // La ricerca è sempre
																					// filtrata per ufficio

			IUdienza lCtrl = SIUSLookupRemote.getUdienzaRemote();
			Vector lUdienze = lCtrl.ExRicercaUdienza(lUdiModCond);

			setRequestAttribute("campo_date", lCampoDate);

			if (lUdienze.size() == 1) {
				lFlag = true;
				// Unica Udienza.
				UdienzaModel lUdiModel = new UdienzaModel((UdienzaModel) lUdienze.firstElement());
				// Valorizza l'id udienza.
				mIdUdienza = lUdiModel.getIdUdienza();
				// Inserisce il model Udienza nella request.
				setRequestAttribute("udienza", lUdiModel);
			} else {
				// Lista di udienze
				// Inserisce il Vector Udienza nella request
				setRequestAttribute("udienze", lUdienze);
				mReturnPage = PG_LISTASELEZIONEPROCEDIMENTIXUDIENZA;
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> mReturnPage : " + mReturnPage);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".ricercaUdienza(): fine");

		return lFlag;
	}

	/**
	 * 
	 * @param aTempRic
	 * @throws Exception
	 */
	protected void setListaCbxTemplate(TemplateModel aTempRic) throws Exception {
		// Template
		ITemplate lTemCtrl = SICOLookupRemote.getTemplateRemote();
		Vector lTemplate = lTemCtrl.ExListaCbxTemplate(aTempRic);
		Option lOptTemplate = new Option(lTemplate);

		setRequestAttribute("ElencoTemplate", "" + lOptTemplate);
		setRequestAttribute("AutoTemplate", null);
		setRequestAttribute("Stampabile", "SI");
	}

	/**
	 * Gestione della scelta del template per ufficio TDS = T; UDS = U. N.B.: Per altri uffici diversi da TDS
	 * e UDS, il flag template è stato intenzionalmente posto a "", tale da non ritornare nessun modello di
	 * stampa nella combo box, poichè al momento non esistono altri template al di fuori di T e U.
	 * <p>
	 * 
	 * @return Stringa del FlagTemplate
	 * @throws Exception
	 *             propage errore di eccezione
	 */
	private String getFlagTemplatePerCodTipoUfficio() throws Exception {

		String lFlagTemplate = new String();

		if (getUfficioUtenteConnesso().getCodTipoUfficio().equalsIgnoreCase("TDS"))
			lFlagTemplate = "T"; // TDS
		else if (getUfficioUtenteConnesso().getCodTipoUfficio().equalsIgnoreCase("UDS"))
			lFlagTemplate = "U"; // UDS
		else
			lFlagTemplate = ""; // Non definito

		return lFlagTemplate;
	}

}