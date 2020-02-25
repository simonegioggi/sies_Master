package siap.sige.udienzaprocedimento.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.html.Option;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSigeRuolo;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActRicercaUdienzaProcedimento
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Procedimenti per Udienza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class ActRicercaUdienzaProcedimento extends ActionSiap
		implements ICostantiUdienzaProcedimentoSige, ICostantiUdienzaSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String mReturnPage = "";
	private BigDecimal mIdUdienza = null;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		setLinkRitorno();

		String lOrderBy = null;
		lOrderBy = getRequestStringParameter("tipo");

		String tiporicerca = getRequestStringParameter("tiporicerca");

		String tipoProc = "TUTTI";

		String cod_magis = null;
		if (Utils.isPresent(getRequestStringParameter("codMagis"))) {
			cod_magis = getRequestStringParameter("codMagis");
		}

		String lStatoProcedimento = "";
		if (isRequestChecked("CheckUnificazione"))
			lStatoProcedimento = "U";
		// 20170908: [SG] aggiunto controllo
		else if (!isRequestParameterNullObj("statoProc"))
			lStatoProcedimento = getRequestStringParameter("statoProc");

		setRequestAttribute("tipoProc", tipoProc);
		setRequestAttribute("lStatoProcedimento", lStatoProcedimento);

		setRequestAttribute("tipo", lOrderBy);
		setRequestAttribute("tiporicerca", tiporicerca);

		String listaIdUdienze = "";
		if (this.getParameter("listaIdUdienze") != null) {
			listaIdUdienze = getRequestStringParameter("listaIdUdienze");
			setRequestAttribute("listaIdUdienze", listaIdUdienze);
		}

		// lettura ID Udienza
		mIdUdienza = getRequestBigDecimalParameter(CAMPO_UDI_ID_UDIENZA_SIGE);
		// 20170913: [SG] aggiunta query
		Vector<Object> collegi = new Vector<>();
		if (mIdUdienza != null && "0".equals(mIdUdienza.toString())) {
			// Chiama la RemoteInterface del controller udienza.
			IUdienzaSigeRuolo lCtrlUdienza = SIGELookupRemote.getUdienzaSigeRuoloRemote();
			if (!isRequestParameterNullObj("dataUdienza")) {
				Date dataUdienza = null;
				String du = getRequestStringParameter("dataUdienza");
				if (Utils.isPresent(du)) {
					dataUdienza = getRequestDateParameter("dataUdienza", "dd/MM/yyyy");
				} else {
					dataUdienza = getRequestDateParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
							ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA,
							ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA);
				}
				String idSez = getRequestStringParameter("idSezione");
				BigDecimal idSezione = null;
				if (Utils.isPresent(idSez))
					idSezione = new BigDecimal(idSez);
				String tipoRito = null;
				if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO))
					tipoRito = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO);
				else
					tipoRito = getRequestStringParameter("tipoRito");

				// 20171122: [EC] la ricerca dei collegi va fatta solo se è presente il parametro codMagis
				// questo perchè, facendola sempre ci perdevamo la maschera intermedia dei magistrati per
				// udienze in pari data
				// String idCollegio = this.getParameter("IdCollegio") != null
				// ? getRequestStringParameter("IdCollegio")
				// : "";

				// if (idCollegio != null) {
				// collegi = lCtrlUdienza.cercaCollegi(dataUdienza, getCodUfficioUtenteConnesso(),
				// getRequestStringParameter("codMagis"), idSezione, tipoRito, idCollegio);
				// }

				if (Utils.isPresent(getRequestStringParameter("codMagis"))
						&& !getRequestStringParameter("codMagis").equals("null")) {
					collegi = lCtrlUdienza.cercaCollegi(dataUdienza, getCodUfficioUtenteConnesso(),
							getRequestStringParameter("codMagis"), idSezione, tipoRito, null);
				}
			}
		}

		Collection<ProcedimentixUdienzaModel> lVect = new Vector<>();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>> tipoRicerca : " + tiporicerca);

		// Ricerca Lista procedimenti
		IUdienzaProcedimentoSige lCtrlUdPr = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		IUdienzaSige lCtrlUd = SIGELookupRemote.getUdienzaSigeRemote();

		UdienzaSigeModel lUdienza = new UdienzaSigeModel();

		if (tiporicerca.compareTo("xdata") == 0) {
			if (!collegi.isEmpty()) {
				for (int i = 0; i < collegi.size(); i++) {
					UdienzaSigeModel usm = (UdienzaSigeModel) collegi.get(i);
					mIdUdienza = usm.getIdUdienzaSige();
					lVect.addAll(lCtrlUdPr.ExRicercaProcedimentixUdienza(mIdUdienza, lOrderBy,
							lStatoProcedimento, tipoProc, null, cod_magis));
					// Preleva dati dell'udienza di riferimento.
					lUdienza = lCtrlUd.ExRicercaUdienzaSigeById(mIdUdienza);
					ProcedimentixUdienzaModel proc = ((Vector<ProcedimentixUdienzaModel>) lVect)
							.firstElement();
					if (proc != null)
						impostaDatiAssegnatario(proc, lUdienza);
				}
				TemplateModel lTempRic = new TemplateModel();
				lTempRic.setCodTipoProvvedimentoSige("90");
				setListaCbxTemplate(lTempRic);
				mReturnPage = PG_LISTAPROCEDIMENTIXUDIENZA;
			} else {
				if (ricercaUdienza() || (listaIdUdienze != null && !"".equals(listaIdUdienze))) {
					if ("".equals(listaIdUdienze) && this.getRequestAttribute("listaIdUdienze") != null) {
						listaIdUdienze = (String) getRequestAttribute("listaIdUdienze");
					}
					if (!listaIdUdienze.equals("")) {
						String[] udi = listaIdUdienze.split(";");

						for (int i = 0; i < udi.length; i++) {
							String id = udi[i].trim();
							lVect.addAll(lCtrlUdPr.ExRicercaProcedimentixUdienza(new BigDecimal(id), lOrderBy,
									lStatoProcedimento, tipoProc, null, cod_magis));
							mIdUdienza = new BigDecimal(id);
						}
					}
					// Preleva dati dell'udienza di riferimento.
					lUdienza = lCtrlUd.ExRicercaUdienzaSigeById(mIdUdienza);
					ProcedimentixUdienzaModel proc = ((Vector<ProcedimentixUdienzaModel>) lVect)
							.firstElement();
					if (proc != null)
						impostaDatiAssegnatario(proc, lUdienza);
					// [EC]: se la size di lVect è = 1 allora recupero un idProcedimento
					TemplateModel lTempRic = new TemplateModel();
					lTempRic.setCodTipoProvvedimentoSige("90");
					setListaCbxTemplate(lTempRic);

					mReturnPage = PG_LISTAPROCEDIMENTIXUDIENZA;
				}
			}
		} else if (tiporicerca.compareTo("xmagistrato") == 0) {
			// 20171004: [SG] aggiunta ricerca udienza x collegi
			if (!collegi.isEmpty()) {
				for (int i = 0; i < collegi.size(); i++) {
					UdienzaSigeModel usm = (UdienzaSigeModel) collegi.get(i);
					mIdUdienza = usm.getIdUdienzaSige();
					lVect.addAll(lCtrlUdPr.ExRicercaProcedimentixUdienza(mIdUdienza, lOrderBy,
							lStatoProcedimento, tipoProc, null, cod_magis));
					// Preleva dati dell'udienza di riferimento.
					lUdienza = lCtrlUd.ExRicercaUdienzaSigeById(mIdUdienza);
				}
				TemplateModel lTempRic = new TemplateModel();
				lTempRic.setCodTipoProvvedimentoSige("90");
				setListaCbxTemplate(lTempRic);
				mReturnPage = PG_LISTAPROCEDIMENTIXUDIENZA;
			} else {
				if (ricercaUdienza() || (listaIdUdienze != null && !"".equals(listaIdUdienze))) {

					// lVect = lCtrlUdPr.ExRicercaProcedimentixUdienza(mIdUdienza, "M" + lOrderBy,
					// lStatoProcedimento, tipoProc, null, cod_magis);
					// INTERVENTO PER 11.2.1
					if ("".equals(listaIdUdienze) && this.getRequestAttribute("listaIdUdienze") != null) {
						listaIdUdienze = (String) getRequestAttribute("listaIdUdienze");
					}
					if (!listaIdUdienze.equals("")) {
						String[] udi = listaIdUdienze.split(";");

						for (int i = 0; i < udi.length; i++) {
							String id = udi[i].trim();
							lVect.addAll(lCtrlUdPr.ExRicercaProcedimentixUdienza(new BigDecimal(id),
									"M" + lOrderBy, lStatoProcedimento, tipoProc, null, cod_magis));
							mIdUdienza = new BigDecimal(id);
						}
					}

					// Preleva dati dell'udienza di riferimento.
					lUdienza = lCtrlUd.ExRicercaUdienzaSigeById(mIdUdienza);
					ProcedimentixUdienzaModel proc = ((Vector<ProcedimentixUdienzaModel>) lVect)
							.firstElement();
					if (proc != null)
						impostaDatiAssegnatario(proc, lUdienza);

					TemplateModel lTempRic = new TemplateModel();
					lTempRic.setCodTipoProvvedimentoSige("91");
					setListaCbxTemplate(lTempRic);

					mReturnPage = PG_LISTAPROCEDIMENTIXUDIENZAMAG;
				}
			}
		} else if (tiporicerca.compareTo("xdatacollegi") == 0) {
			// 20171004: [SG] aggiunta ricerca udienza x collegi
			if (!collegi.isEmpty()) {
				for (int i = 0; i < collegi.size(); i++) {
					UdienzaSigeModel usm = (UdienzaSigeModel) collegi.get(i);
					mIdUdienza = usm.getIdUdienzaSige();
					lVect.addAll(lCtrlUdPr.ExRicercaProcedimentixUdienza(mIdUdienza, lOrderBy,
							lStatoProcedimento, tipoProc, null, cod_magis));
					// Preleva dati dell'udienza di riferimento.
					lUdienza = lCtrlUd.ExRicercaUdienzaSigeById(mIdUdienza);
				}
				TemplateModel lTempRic = new TemplateModel();
				lTempRic.setCodTipoProvvedimentoSige("90");
				setListaCbxTemplate(lTempRic);
				mReturnPage = PG_LISTAPROCEDIMENTIXUDIENZA;
			} else {
				Date lDataUdienza = getRequestDateParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
						ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA,
						ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA);
				lVect = lCtrlUdPr.ExRicercaProcedimentixDataUdienza(lDataUdienza, lOrderBy,
						lStatoProcedimento, tipoProc, getCodUfficioUtenteConnesso());
				TemplateModel lTempRic = new TemplateModel();
				lTempRic.setCodTipoProvvedimentoSige("93");
				setListaCbxTemplate(lTempRic);

				setRequestAttribute("data_udi", lDataUdienza);
				mReturnPage = PG_LISTAPROCEDIMENTIXUDIENZATUTTICOLLEGI;
			}
		}

		//
		// Decisione del ruolo magistrato in virtù del tipo ufficio.
		//
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Decisione Ruolo Magistrato : INIT ");

		String lRuoloMagistrato = "Giudice";
		String lTipoProcuratore = "Procuratore Generale";
		if (getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAP")
				|| getUfficioUtenteConnesso().getCodTipoUfficio().equals("CASAP")
				|| getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAPSM")
				|| getUfficioUtenteConnesso().getCodTipoUfficio().equals("DIBM")) {

			lRuoloMagistrato = "Consigliere";
			lTipoProcuratore = "Procuratore Repubblica";
		}

		setRequestAttribute("ruoloMagistrato", lRuoloMagistrato);
		setRequestAttribute("tipoProcuratore", lTipoProcuratore);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Decisione Ruolo Magistrato : OK ");

		setRequestAttribute("udienza", lUdienza);
		setRequestAttribute("procedimenti", lVect);
		// 20171002: [SG] aggiunta impostazione di attributo nella request = Tipo rito
		String tipoRito = null;
		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO))
			tipoRito = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO);
		else
			tipoRito = getRequestStringParameter("tipoRito");
		setRequestAttribute("tipoRito", tipoRito);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");

		return mReturnPage;
	}

	private void impostaDatiAssegnatario(ProcedimentixUdienzaModel proc, UdienzaSigeModel lUdienza) {

		if (lUdienza != null) {
			lUdienza.setDescrProcuratore(proc.getDescrProcuratore());
			lUdienza.setDescrIdAssistente(proc.getDescrIdAssistente());
			lUdienza.setDescrMagistratoAss(proc.getCognomeMagistrato() + " " + proc.getNomeMagistrato());
			lUdienza.setCodMagistratoAss(proc.getCodMagistrato());
		}

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
		siesLogger.info("inizio");

		boolean lFlag = false;

		// Se è valorizzato l'id dell'udienza, si provvede a recuperare
		// i dati udienza corrispondenti. In caso contrario si esegue
		// la ricerca per data, ove in caso di pù udienze, s'imposta la
		// ReturnPage con la view di lista delle udienze selezionabili.
		if (mIdUdienza != null && !mIdUdienza.toString().equals("0")) {
			// Ricerca dettaglio Udienza.
			IUdienzaSige lCtrlUd = SIGELookupRemote.getUdienzaSigeRemote();
			UdienzaSigeModel lUdiMod = lCtrlUd.ExRicercaUdienzaSigeById(mIdUdienza);
			setRequestAttribute("udienza", lUdiMod);

			lFlag = true;
		} else {
			// Chiama la RemoteInterface del controller udienza.
			IUdienzaSigeRuolo lCtrlUdienza = SIGELookupRemote.getUdienzaSigeRuoloRemote();

			// 20171002: [SG] aggiunta impostazione di attributo nella request = Tipo rito
			String tipoRito = null;
			// introdotto per vers 11.2.1
			String cod_magis = null;
			if (Utils.isPresent(getRequestStringParameter("codMagis"))) {
				cod_magis = getRequestStringParameter("codMagis");
			}

			if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO))
				tipoRito = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO);
			else
				tipoRito = getRequestStringParameter("tipoRito");

			// Ricerca Udienza
			// 20171002: [SG] aggiunta impostazione di attributo nella request = Data Udienza
			Date dataUdienza = null;
			if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA)) {
				dataUdienza = getRequestDateParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
						ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA,
						ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA);
			} else
				dataUdienza = DateUtils.getDate(getRequestStringParameter("dataUdienza"), "dd/MM/yyyy");
			setRequestAttribute("campo_date", dataUdienza);

			UdienzaSigeModel lUdiModCond = new UdienzaSigeModel();
			lUdiModCond.setDataUdienza(dataUdienza);
			lUdiModCond.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

			// intervnto per 11.2.1
			if (cod_magis != null) {
				lUdiModCond.setCodMagistratoAss(cod_magis);
			}
			// La ricerca è sempre filtrata per ufficio

			Vector<Object> lUdienze = new Vector<>();
			lUdienze = lCtrlUdienza.ExRicercaUdienzaSigePerRuolo(lUdiModCond, tipoRito);
			if (lUdienze.size() == 1) {
				lFlag = true;
				// Unica Udienza.
				UdienzaSigeModel lUdiModel = new UdienzaSigeModel((UdienzaSigeModel) lUdienze.firstElement());
				// Valorizza l'id udienza.

				mIdUdienza = lUdiModel.getIdUdienzaSige();
				if ((mIdUdienza == null || "0".equals(mIdUdienza.toString()))
						&& lUdiModel.getNumeroUdienze().intValue() == 1) {
					String listaUdi = lUdiModel.getListaIdUdienze().trim();
					mIdUdienza = new BigDecimal(listaUdi);
					// Inserisce il model Udienza nella request.
					setRequestAttribute("listaIdUdienze", listaUdi);

				}
				// 20170913: [SG] aggiunta query
				Vector<Object> collegi = new Vector<>();
				String codColl = null;
				if (mIdUdienza != null && "0".equals(mIdUdienza.toString())) {
					// Chiama la RemoteInterface del controller udienza
					BigDecimal idSezione = null;
					if (lUdiModel.getCollegio() != null && lUdiModel.getCollegio().getSezione() != null)
						idSezione = lUdiModel.getCollegio().getSezione().getIdSezione();
					if (lUdiModel.getCollegio() != null && lUdiModel.getCollegio().getCodCollegio() != null) {
						codColl = lUdiModel.getCollegio().getCodCollegio();
					}
					collegi = lCtrlUdienza.cercaCollegi(lUdiModel.getDataUdienza(),
							getCodUfficioUtenteConnesso(), lUdiModel.getCodGiudice(), idSezione, tipoRito,
							codColl);
					if (!collegi.isEmpty()) {
						for (int i = 0; i < collegi.size(); i++) {
							UdienzaSigeModel usm = (UdienzaSigeModel) collegi.get(i);
							mIdUdienza = usm.getIdUdienzaSige();
							// Preleva dati dell'udienza di riferimento.
							IUdienzaSige lCtrlUd = SIGELookupRemote.getUdienzaSigeRemote();
							lUdiModel = lCtrlUd.ExRicercaUdienzaSigeById(mIdUdienza);
						}
					}
				}
				// Inserisce il model Udienza nella request.
				if (lUdiModel.getListaIdUdienze() == null || "".equals(lUdiModel.getListaIdUdienze())) {
					lUdiModel.setListaIdUdienze(lUdiModel.getIdUdienzaSige().toString());
					setRequestAttribute("listaIdUdienze", lUdiModel.getListaIdUdienze().trim());
				}

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
		siesLogger.info("fine");

		return lFlag;
	}

	/**
	 *
	 * <p>
	 *
	 * @param aTempRic
	 * @throws Exception
	 */
	protected void setListaCbxTemplate(TemplateModel aTempRic) throws Exception {

		// Template
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");
		ITemplate lTemCtrl = SICOLookupRemote.getTemplateRemote();
		Vector<TemplateModel> lTemplate = lTemCtrl.ExListaCbxTemplate(aTempRic);
		Option lOptTemplate = new Option(lTemplate);

		setRequestAttribute("ElencoTemplate", "" + lOptTemplate);
		setRequestAttribute("AutoTemplate", null);
		setRequestAttribute("Stampabile", "SI");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");
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
	/*
	 * private String getFlagTemplatePerCodTipoUfficio() throws Exception { String lFlagTemplate = new
	 * String();
	 *
	 * if( getUfficioUtenteConnesso().getCodTipoUfficio().equalsIgnoreCase("TDS") ) lFlagTemplate="T"; // TDS
	 * else if( getUfficioUtenteConnesso().getCodTipoUfficio().equalsIgnoreCase("UDS") ) lFlagTemplate="U"; //
	 * UDS else lFlagTemplate=""; // Non definito
	 *
	 * return lFlagTemplate;
	 *
	 * }
	 */

}