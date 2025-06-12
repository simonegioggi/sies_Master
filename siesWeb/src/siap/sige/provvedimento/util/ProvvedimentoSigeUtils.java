package siap.sige.provvedimento.util;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.template.util.UtilTemplate;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
import siap.sige.decretounificazione.action.ICostantiDecretoUnificazioneSige;
import siap.sige.impugnazione.action.ICostantiImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * FascicoloSigeUtils - Classe di utilita' per il package provvedimento di Sige
 *
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class ProvvedimentoSigeUtils extends ActionSige
		implements ICostantiProvvedimentoSige, ICostantiImpugnazioneSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Confronta i Magistrati componenti del Collegio con il Magistrato Assegnatario per valutare se questi fa
	 * parte del Collegio stesso.
	 *
	 * @param aIdCollegio
	 * @return
	 * @throws Exception
	 */
	public boolean isMagistratoAssInCollegio(BigDecimal aIdCollegio, String lCodMagAssegnatario)
			throws Exception {

		boolean lRet = false;

		// Ricerca del Collegio
		ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
		CollegioModel lColMod = lCtrl.ExRicercaCollegioByKey(aIdCollegio);
		if (lColMod != null && lColMod.getCollegioMagistrati() != null) {
			// Magistrati del Collegio
			CollegioMagistratoModel[] lElencoMag = lColMod.getCollegioMagistrati();

			for (int i = 0; i < lElencoMag.length; i++) {
				if (lElencoMag[i].getMagCodMagistrato().equalsIgnoreCase(lCodMagAssegnatario)) {
					lRet = true;
					break;
				}
			}
		}
		return lRet;
	}

	// Funzione per la costruzione della combo con i template di stampa previsti.
	public void gestioneTemplate(TemplateModel lTempRic) throws Exception {

		Option lOptTemplate = null;

		lOptTemplate = UtilTemplate.listaTemplateByCodProvvSige(lTempRic.getCodTipoProvvedimentoSige());
		setRequestAttribute(ICostantiTemplate.CAMPO_COMBO_TEMPLATE, "" + lOptTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ElencoTemplate nella Combo -> " + lOptTemplate);

		// template di default
		String[] lSelected = lOptTemplate.getSelecteds();
		if (lSelected != null && lSelected.length > 0) {
			setRequestAttribute(ICostantiTemplate.CAMPO_DEFAULT_TEMPLATE, lSelected[0]);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("TemplateDiDefault -> " + lSelected[0]);
		}
		return;
	}

	public void ricercaCollegio(BigDecimal aIdCollegio, String aCodTipoUfficioConnesso) throws Exception {

		// Chiama il controller.
		ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
		CollegioModel lColMod = lCtrl.ExRicercaCollegioByKey(aIdCollegio);

		//
		// Decisione del ruolo magistrato in virtù del tipo ufficio.
		//
		String lRuoloMagistrato = "Giudice";

		if (aCodTipoUfficioConnesso.equals("CAP") || aCodTipoUfficioConnesso.equals("CASAP")
				|| aCodTipoUfficioConnesso.equals("CAPSM") || aCodTipoUfficioConnesso.equals("DIBM"))
			lRuoloMagistrato = "Consigliere";

		// Passaggio alla request.
		if (lColMod != null)
			setRequestAttribute("collegio", lColMod);
		setRequestAttribute("ruoloMagistrato", lRuoloMagistrato);

	}

	public String getOggettiProvvedimento(ProvvedimentoSigeEventoModel lProvEve) {

		Vector<TenoreSigeModel> lTenori = null;

		if (lProvEve.getProvvedimento().getCodTipoProvvedimentoSige() != null
				&& lProvEve.getProvvedimento().getCodTipoProvvedimentoSige()
						.compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_SOSPENSIONE) == 0) {
			// Per l'Ordinanza di Sospensione il riferimento non è più ai tenori del provvedimento sospeso
			lTenori = lProvEve.getTenoriEstesi();
		} else {
			lTenori = lProvEve.getTenoriEstesi();
		}

		String lDescrTenorePrecedente = "";
		String lIdTenore = "";
		StringBuffer sb = new StringBuffer();
		for (TenoreSigeModel lTenore : lTenori) {
			if (lTenore != null && !lTenore.getIdTenoreSige().toString().equalsIgnoreCase(lIdTenore)
					&& lTenore.getDescrOggettoSige().compareTo(lDescrTenorePrecedente) != 0) {
				lDescrTenorePrecedente = lTenore.getDescrOggettoSige();
				sb.append("<font class=\"label\">");
				sb.append("-&nbsp" + lTenore.getDescrOggettoSige() + "<br />");
				sb.append("</font>");
			}
		}

		return sb.toString();
	}

	public String getDescrizioneTipoProvvedimento(ProvvedimentoSigeEventoModel lProvEve) {

		String descrizione = "";
		if (lProvEve.getProvvedimento().getCodTipoProvvedimentoSige() != null) {
			descrizione = StringUtils.toStringJSP(lProvEve.getProvvedimento().getDescrTipoProvvedimentoSige(),
					"-");
			if (lProvEve.getProvvedimento().getCodTipoProvvedimentoSige().equals("03")) {
				descrizione = "<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN + "?"
						+ IWebConstants.ACTION_FIELD
						+ "=siap.sige.provvedimento.action.ActDettaglioOrdinanza&"
						+ CAMPO_ID_PROVVEDIMENTO_SIGE + "="
						+ lProvEve.getProvvedimento().getIdProvvedimentoSige()
						+ "&TornaQui=0\">Ordinanza</a>";
			}

			if (lProvEve.getProvvedimento().getCodTipoProvvedimentoSige().equals("18")) {
				descrizione = "<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN + "?"
						+ IWebConstants.ACTION_FIELD
						+ "=siap.sige.provvInterlocutori.action.ActDettaglioOrdinanzaConflittoCompetenza&"
						+ CAMPO_ID_PROVVEDIMENTO_SIGE + "="
						+ lProvEve.getProvvedimento().getIdProvvedimentoSige() + "&TornaQui=0\">"
						+ StringUtils.toStringJSP(descrizione, "-") + "</a>";
			}

			if (lProvEve.getProvvedimento().getCodTipoProvvedimentoSige().equals("01")) {
				descrizione = "<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN + "?"
						+ IWebConstants.ACTION_FIELD
						+ "=siap.sige.udienza.action.ActLoadDettaglioFissazioneUdienza&"
						+ CAMPO_ID_PROVVEDIMENTO_SIGE + "="
						+ lProvEve.getProvvedimento().getIdProvvedimentoSige() + "&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "="
						+ lProvEve.getEventoNotifica().getEvento().getIdEvento() + "&TornaQui=0\">"
						+ StringUtils.toStringJSP(descrizione, "-") + "</a>";
			}

			if (lProvEve.getProvvedimento().getCodTipoProvvedimentoSige().equals("05")) {
				descrizione = "<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN + "?"
						+ IWebConstants.ACTION_FIELD
						+ "=siap.sige.provvedimento.action.ActDettaglioDecretoInammissibilita&"
						+ CAMPO_ID_PROVVEDIMENTO_SIGE + "="
						+ lProvEve.getProvvedimento().getIdProvvedimentoSige() + "&TornaQui=0\">"
						+ StringUtils.toStringJSP(descrizione, "-") + "</a>";
			}

			if (lProvEve.getProvvedimento().getCodTipoProvvedimentoSige().equals("06")) {
				descrizione = "<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN + "?"
						+ IWebConstants.ACTION_FIELD
						+ "=siap.sige.provvedimento.action.ActDettaglioOrdinanza&"
						+ CAMPO_ID_PROVVEDIMENTO_SIGE + "="
						+ lProvEve.getProvvedimento().getIdProvvedimentoSige() + "&TornaQui=0\">"
						+ StringUtils.toStringJSP(descrizione, "-") + "</a>";
			}

			if (lProvEve.getProvvedimento().getCodTipoProvvedimentoSige().equals("07")) {
				descrizione = "<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN + "?"
						+ IWebConstants.ACTION_FIELD
						+ "=siap.sige.provvedimento.action.ActDettaglioOrdinanza&"
						+ CAMPO_ID_PROVVEDIMENTO_SIGE + "="
						+ lProvEve.getProvvedimento().getIdProvvedimentoSige() + "&TornaQui=0\">"
						+ StringUtils.toStringJSP(descrizione, "-") + "</a>";
			}

			// @emma 20/08/2018: deve essere linkabile anche Ordinanza di Rinvio Udienza
			if (lProvEve.getProvvedimento().getCodTipoProvvedimentoSige().equals("04")) {
				descrizione = "<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN + "?"
						+ IWebConstants.ACTION_FIELD
						+ "=siap.sige.udienzaprocedimento.action.ActLoadDettaglioOrdinanzaRinvioUdienza&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "="
						+ lProvEve.getEventoNotifica().getEvento().getIdEvento() + "&TornaQui=0\">"
						+ StringUtils.toStringJSP(descrizione, "-") + "</a>";
			}

			// @emma 04/12/2018: deve essere linkabile anche il Decreto di Unificazione
			if (lProvEve.getProvvedimento().getCodTipoProvvedimentoSige().equals("55")) {
				descrizione = "<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN + "?"
						+ IWebConstants.ACTION_FIELD
						+ "=siap.sige.decretounificazione.action.ActLoadDettaglioDecretoUnificazioneSige&"
						+ ICostantiDecretoUnificazioneSige.CAMPO_ID_DECRETO_UNIFICAZIONE + "="
						+ lProvEve.getProvvedimento().getIdProvvedimentoSige() + "&TornaQui=0\">"
						+ StringUtils.toStringJSP(descrizione, "-") + "</a>";
			}

			// @emma 04/12/2018: deve essere linkabile anche l' Ordinanza di Sospensione
			if (lProvEve.getProvvedimento().getCodTipoProvvedimentoSige().equals("10")) {
				descrizione = "<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN + "?"
						+ IWebConstants.ACTION_FIELD
						+ "=siap.sige.provvedimento.action.ActDettaglioOrdinanzaSospensione&"
						+ CAMPO_ID_PROVVEDIMENTO_SIGE + "="
						+ lProvEve.getProvvedimento().getIdProvvedimentoSige() + "&TornaQui=0\">"
						+ StringUtils.toStringJSP(descrizione, "-") + "</a>";
			}

			// @emma 04/12/2018: deve essere linkabile anche Ordinanza di Rinvio Udienza da Verbale
			if (lProvEve.getProvvedimento().getCodTipoProvvedimentoSige().equals("50")) {
				descrizione = "<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN + "?"
						+ IWebConstants.ACTION_FIELD
						+ "=siap.sige.udienzaprocedimento.action.ActLoadDettaglioVerbaleRinvioUdienza&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "="
						+ lProvEve.getEventoNotifica().getEvento().getIdEvento() + "&TornaQui=0\">"
						+ StringUtils.toStringJSP(descrizione, "-") + "</a>";
			}
		} else {
			descrizione = StringUtils.toStringJSP(lProvEve.getProvvedimento().getDescrTipoProvvedimento(),
					"-");
			if (Utils.isNullObj(lProvEve.getEventoNotifica().getEvento())
					|| Utils.isNullObj(lProvEve.getEventoNotifica().getEvento().getCodEsito())) {
				descrizione = "&nbsp;";
			} else {
				descrizione = lProvEve.getEventoNotifica().getEvento().getDescrEsito();
			}
		}

		if (descrizione.equalsIgnoreCase("Ordinanza Generica")) {
			descrizione = "Ordinanza";
		}
		return descrizione;
	}

	public String getProvvedimentoValidato(ProvvedimentoSigeEventoModel lProvEve, String flagValida,
			String lFunAnnullaValidaProvvedimento, boolean modificabile) {

		if (flagValida.equalsIgnoreCase("NO"))
			return "";

		StringBuffer tag = new StringBuffer("<td class=\"c\">");

		if ((lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != null) && (lProvEve
				.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0)) {
			// SVALIDAZIONE. Il decreto di Unificazione è sempre svalidabile !
			if (lProvEve.getEventoNotifica().getEvento().getNumAllValidati() < 1
					&& lFunAnnullaValidaProvvedimento.length() > 1
					&& (modificabile || (lProvEve.getEventoNotifica().getEvento().getCodEsito()
							.compareToIgnoreCase("0600") == 0))) {
				// <a href="Javascript:annulla('Vuoi annullare la validazione del provvedimento?
				// ','<%=lFunAnnullaValidaProvvedimento%>'
				// ,'<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=lProvEve.getEventoNotifica().getEvento().getIdEvento()%>');">
				tag.append(
						"<a href=\"Javascript:annulla('Vuoi annullare la validazione del provvedimento? ','"
								+ lFunAnnullaValidaProvvedimento + "','" + ICostantiEvento.CAMPO_ID_EVENTO
								+ "','" + lProvEve.getEventoNotifica().getEvento().getIdEvento() + "');\">");
				tag.append(
						"<img src=\"/images/TickRed.gif\" alt = \"Annulla validazione provvedimento\"  border=\"0\">");
				tag.append("</a>");
			} else {
				tag.append("<img src=\"/images/TickRed.gif\">");
			}
			// ANNULLATO
		} else if (lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != null && lProvEve
				.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("A") == 0) {
			tag.append("<a class=\"cliccabile\" href=\"javascript:cancella('"
					+ lProvEve.getEventoNotifica().getEvento().getIdEvento()
					+ "');\" title=\"ANNULLAMENTO\">");
			tag.append("<font class=\"cRosso\">ANNULLATO</font></a>");
		} else {
			tag.append("-");
		}
		tag.append("</td>");
		return tag.toString();
	}

	public String getDepositoValidato(ProvvedimentoSigeEventoModel lProvEve, String flagValida,
			String lFunAnnullaValidaAllegato) {

		if (flagValida.equalsIgnoreCase("NO"))
			return "";

		StringBuffer tag = new StringBuffer("<td class=\"c\">");

		if (lProvEve.getEventoNotifica().getEvento().getNumAllValidati() > 0) {
			if (lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != null && lProvEve
					.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0) {
				if (lFunAnnullaValidaAllegato.length() > 1) {
					tag.append("<a href=\"Javascript:annulla('Vuoi annullare la validazione del deposito? ','"
							+ lFunAnnullaValidaAllegato + "','+" + ICostantiEvento.CAMPO_ID_EVENTO + "','"
							+ lProvEve.getEventoNotifica().getEvento().getIdEvento() + "');\">");
					tag.append(
							"<img src=\"/images/TickRed.gif\" alt = \"Annulla validazione deposito\"  border=\"0\">");
					tag.append("</a>");
				} else {
					tag.append("<img src=\"/images/TickRed.gif\">");
				}
			} else if (lProvEve.getProvvedimento().getDataDeposito() != null) {
				tag.append("<img src=\"/images/TickRed.gif\">");
			}
		} else {
			tag.append("-");
		}
		tag.append("</td>");
		return tag.toString();
	}

	public String getDataRicorso(Vector<ImpugnazioneSigeModel> impugnazioni) {

		StringBuffer tag = new StringBuffer("<font class=\"campo\">");
		String tag1 = "-";
		String action = "siap.sige.impugnazione.action.ActLoadDettaglioImpugnazioneSige";
		if (impugnazioni.size() > 0) {

			ImpugnazioneSigeModel ricorso = impugnazioni.get(0);
			String idProvvedimento = ricorso.getProvvIdProvvedimentoSige().toString();
			String dataPrimoRicorso = DateUtils.getDateToString(ricorso.getDataRicorso(), "dd-MM-yyyy");
			if (impugnazioni.size() > 1)
				action = "siap.sige.impugnazione.action.ActRicercaImpugnazioniDelProvvedimentoSige";

			tag1 = "<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN + "?"
					+ IWebConstants.ACTION_FIELD + "=" + action + "&" + CAMPO_ID_IMPUGNAZIONE + "="
					+ ricorso.getIdImpugnazioneSige() + "&" + CAMPO_ID_PROVVEDIMENTO_SIGE + "="
					+ idProvvedimento + "&" + CAMPO_COD_TIPO_IMPUGNAZIONE + "=" + COD_TIPO_RICORSO + "&"
					+ CAMPO_SHOW_COMBO_TEMPLATE + "=true&TornaQui=0\">"
					+ StringUtils.toStringJSP(dataPrimoRicorso, "-") + "</a>";
		}
		tag.append(tag1);
		tag.append("</font>");

		return tag.toString();
	}

	public String getDataOpposizione(Vector<ImpugnazioneSigeModel> impugnazioni) {

		StringBuffer tag = new StringBuffer("<font class=\"campo\">");
		String tag1 = "-";
		String action = "siap.sige.impugnazione.action.ActLoadDettaglioImpugnazioneSige";
		if (impugnazioni.size() > 0) {
			ImpugnazioneSigeModel opposizione = impugnazioni.get(0);
			String idProvvedimento = opposizione.getProvvIdProvvedimentoSige().toString();
			String dataPrimoRicorso = DateUtils.getDateToString(opposizione.getDataRicorso(), "dd-MM-yyyy");
			if (impugnazioni.size() > 1)
				action = "siap.sige.impugnazione.action.ActRicercaImpugnazioniDelProvvedimentoSige";

			tag1 = "<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN + "?"
					+ IWebConstants.ACTION_FIELD + "=" + action + "&"
					+ ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE + "="
					+ opposizione.getIdImpugnazioneSige() + "&"
					+ ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE + "=" + idProvvedimento + "&"
					+ CAMPO_COD_TIPO_IMPUGNAZIONE + "=" + COD_TIPO_OPPOSIZIONE + "&"
					+ CAMPO_SHOW_COMBO_TEMPLATE + "=true&TornaQui=0\">"
					+ StringUtils.toStringJSP(dataPrimoRicorso, "-") + "</a>";
		}
		tag.append(tag1);
		tag.append("</font>");

		return tag.toString();
	}

	public String getLinkDettaglioOrdinanzaUdienza(ProvvedimentoSigeEventoModel lProvEve) {

		UdienzaSigeModel udienzaModel = lProvEve.getProvvedimento().getUdienzaSige();
		if (udienzaModel == null)
			return "&nbsp;";

		String codTipoProvv = lProvEve.getProvvedimento().getCodTipoProvvedimentoSige();
		String action = "siap.sige.udienza.action.ActLoadDettaglioFissazioneUdienza";

		StringBuffer tag = new StringBuffer();
		if (codTipoProvv.equals("04"))
			action = "siap.sige.udienzaprocedimento.action.ActLoadDettaglioOrdinanzaRinvioUdienza";
		// 20250610 [SG]: aggiunta distinzione action di reindirizzamento
		else if (codTipoProvv.equals("50"))
			action = "siap.sige.udienzaprocedimento.action.ActLoadDettaglioVerbaleRinvioUdienza";

		// @emma 26072018 intervento post COLLAUDO 11.2 (la data udienza deve essere visibile per tipo
		// provvedimetno sige 'Decreto Fissazione Udienza')
		/*
		 * ISSUE MAC : Aggiunta condizione alla if: la data udienza deve essere visibile anche per tipo
		 * provvedimento 'Rinvio udienza da verbale' ('50') Numero MAC : 20191115013 Autore : monica Data :
		 * 18/nov/2019 Branch : MAC_20191115013
		 */
		if (codTipoProvv.equals("01") || codTipoProvv.equals("04") || codTipoProvv.equals("50")) {
			tag.append("<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN + "?"
					+ IWebConstants.ACTION_FIELD + "=" + action + "&TornaQui=0&IdEvento="
					+ lProvEve.getEventoNotifica().getEvento().getIdEvento().toString() + "\">"
					+ StringUtils.toStringJSP(
							DateUtils.getDateToString(udienzaModel.getDataUdienza(), "dd-MM-yyyy"), "-")
					+ "</a>");
		} else {
			tag.append("-");
		}
		// ***** FINE INTERVENTO MAC_20191115013 *****//

		// valore di ritorno
		return tag.toString();
	}

	public String getLinkDettaglioDeposito(ProvvedimentoSigeEventoModel lProvEve) {

		String descr = StringUtils.toStringJSP(
				DateUtils.getDateToString(lProvEve.getProvvedimento().getDataDeposito(), "dd-MM-yyyy"), "-");
		String action = "siap.sige.provvedimento.action.ActLoadDettaglioDataDeposito";
		StringBuffer tag = new StringBuffer();
		// Modifica del 02/03/2016 Nuova Infrastruttura - INIZIO ******
		// nel caso di provvedimento "Definizione Manuale" la Data Deposito
		// non è cliccabile, poichè non esiste un Documento Allegato e la classe
		// ActLoadDettaglioDataDeposito solleverebbe un eccezione
		String provvDefManuale = "N";
		if (lProvEve != null && lProvEve.getEventoNotifica() != null
				&& lProvEve.getEventoNotifica().getEvento() != null
				&& lProvEve.getEventoNotifica().getEvento().getCodTipoProvvedimento() != null
				&& lProvEve.getEventoNotifica().getEvento().getCodTipoProvvedimento().equals("62")) {
			provvDefManuale = "S";
		}
		// Modifica del 02/03/2016 Nuova Infrastruttura - FINE ******

		if (!descr.equals("-") && provvDefManuale.equals("N"))
			descr = "<a class=\"cliccabile\" href=\"" + IWebConstants.PG_MAIN + "?"
					+ IWebConstants.ACTION_FIELD + "=" + action + "&TornaQui=0&"
					+ ICostantiProvvedimentoSige.CAMPO_ID_EVENTO_GENERATO + "="
					+ lProvEve.getEventoNotifica().getEvento().getIdEvento().toString() + "\">" + descr
					+ "</a>";

		tag.append(descr);
		return tag.toString();
	}

}