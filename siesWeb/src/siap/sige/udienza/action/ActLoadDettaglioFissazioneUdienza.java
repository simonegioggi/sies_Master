package siap.sige.udienza.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.template.util.UtilTemplate;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActLoadDettaglioFissazioneUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load della jsp di dettaglio Fissazione Udienza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioFissazioneUdienza extends ActionSige implements ICostantiUdienzaSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".ActLoadDettaglioFissazioneUdienza: inizio");
		BigDecimal lIdUdienza = null;
		BigDecimal lIdUdienzaProcedimentoSige = null;

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = this.getFascicoloSigeEstesoInSessione();

		this.setLinkRitorno();
		// I parametri passati all'azione con il metodo get.
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// passo il tipo giudizio
		setRequestAttribute("tipoGiudizioVal", lFasEsteso.getFascicoloSige().getCodTipoGiudizio());

		// Chiama il controller Evento per ricerca di un evento
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("###### Invoca la ricerca EventoNotifica : ");
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("eventonotifica", lEveMod);

		// Chiama il controller Udienza_Procedimento per risalire all'Udienza
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("###### Invoca la ricerca UDIENZA_PROCEDIMENTO_SIGE : ");
		IUdienzaProcedimentoSige lUdiProCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		UdienzaProcedimentoSigeModel lUdienzaProcedimentoSige = lUdiProCtrl
				.ExRicercaUdienzaProcedimentoByEve(lIdEvento);
		if (lUdienzaProcedimentoSige != null) {
			// Se trovato UDIENZA_PROCEDIMENTO_SIGE si ricava l'ID Udienza e
			// l'ID_UDIENZA_PROCEDIMENTO_SIGE viene passato nella request.
			lIdUdienzaProcedimentoSige = lUdienzaProcedimentoSige.getIdUdienzaProcedimentoSige();
			lIdUdienza = lUdienzaProcedimentoSige.getUdiIdUdienzaSige();
			setRequestAttribute(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE,
					(lIdUdienzaProcedimentoSige != null) ? lIdUdienzaProcedimentoSige.toString() : "");
			setRequestAttribute("IdUdienzaSige", (lIdUdienza != null) ? lIdUdienza.toString() : "");
			setRequestAttribute("IdEvento", (lIdEvento != null) ? lIdEvento.toString() : "");

			// chiama il controller
			IPartiUdienza lCtrlParti = SIGELookupRemote.getPartiUdienzaRemote();
			Vector<AnagraficaPartiUdienzaModel> lPartiC = lCtrlParti
					.ExRicercaPartiUdienzaByIdUdienza(lIdUdienzaProcedimentoSige, "C");
			setRequestAttribute("udienzaPartiC", lPartiC);

			Vector<AnagraficaPartiUdienzaModel> lPartiO = lCtrlParti
					.ExRicercaPartiUdienzaByIdUdienza(lIdUdienzaProcedimentoSige, "O");
			setRequestAttribute("udienzaPartiO", lPartiO);
		} else {
			// occorre segnalare l'assenza di udienzaProcedimento
			siesLogger.info("Assenza di udienza x Procedimento");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("##### ID Udienza = " + lIdUdienza);
		IUdienzaSige lUdi = SIGELookupRemote.getUdienzaSigeRemote();
		UdienzaSigeModel lUdienzaSige = lUdi.ExRicercaUdienzaSigeById(lIdUdienza);
		setRequestAttribute("udienza", lUdienzaSige);

		// introduco questa ricerca per 11.2.1
		if ("C".equals(lFasEsteso.getFascicoloSige().getCodTipoGiudizio())) {
			// 20190513 [SG]: l'udienza non e' obbligatoria
			if (lUdienzaSige != null && lUdienzaSige.getCollegio() != null
					&& lUdienzaSige.getCollegio().getIdCollegio() != null) {
				// PER LE COLLEGIALI DEVO RECUPERARE IL CODICE DEL MAGISTRATO PRESIDENTE
				// DALLA TABELLA COLLEGIO
				BigDecimal IdCol = lUdienzaSige.getCollegio().getIdCollegio();
				ICollegio lCtrlColl = SIGELookupRemote.getCollegioRemote();
				CollegioModel lColMod = lCtrlColl.ExRicercaCollegioByKey(IdCol);
				String codPres = lColMod.getMagCodMagistrato();
				// setto in request il codice del magistrato Presidente del collegio
				setRequestAttribute("codMagPresidente", codPres);
				IMagistrato lCtrlM = SIGELookupRemote.getMagistratoRemote();
				if (codPres != null) {
					String lCodUfficio = getCodUfficioUtenteConnesso();
					MagistratoModel lMagistrato = lCtrlM.ExRicercaMagistratoByCod(codPres, lCodUfficio);
					if (lMagistrato != null) {
						setRequestAttribute("descrMagPresidente",
								lMagistrato.getCognome() + " " + lMagistrato.getNome());
					}
				}
			}
		}

		// Avvocati attuali assegnati al fascicolo SIGE.
		FascicoloSigeUtils lFasSigeUtils = new FascicoloSigeUtils();
		Vector lAvvocati = lFasSigeUtils.ricercaAvvocati(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		if (lAvvocati.size() > 0)
			setRequestAttribute("avvocato", lAvvocati);

		// Magistrato Assegnatario
		// MagistratoAssegnatarioModel lMagAss =
		// lFasEsteso.getMagAssegnatario();
		// setRequestAttribute("magistratoassegnatario", lMagAss);
		IMagistratoAssegnatario lMagCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
		MagistratoAssegnatarioModel lMagAss = lMagCtrl
				.ExRicercaEstesaMagAssCorrenteXFascicolo(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		setRequestAttribute("magistratoassegnatario", lMagAss);

		// Lettura delle notifiche.
		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		Vector<NotificaModel> lVect = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento(lIdEvento);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>>>> Numero notifiche = " + lVect.size());
		setRequestAttribute("notifiche", lVect);

		// Rimozione dell'elenco Tenori dalla sessione
		removeSessionAttribute("tenori");
		// 20171011: [SG] rimozione variabile statoTenori (un provvedimento lo si può modificare o meno solo
		// in base al fatto che sia validato!
		// boolean statoTenori = true;

		// Lettura del Decreto.
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvEveMod = lCtrlProv
				.ExRicercaProvvedimentoByIdEvento(lEveMod.getEvento().getIdEvento());
		if (lProvEveMod != null && lProvEveMod.getProvvedimento() != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>>> IdProvvedimento Sige = "
					+ lProvEveMod.getProvvedimento().getIdProvvedimentoSige());
			setRequestAttribute("provvedimentoSige", lProvEveMod);

			// Ricerca tenori legati al provvedimento
			TenoreSigeModel lTenore = new TenoreSigeModel();
			lTenore.setProvIdProvvedimentoSige(lProvEveMod.getProvvedimento().getIdProvvedimentoSige());

			ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
			// 20190510 [SG]: aggiungo tenori legati al procedimento
			Vector<TenoreSigeModel> lTenori = lTenCtrl.ExRicercaTenori(lTenore);
			// Vector<TenoreSigeEstesoModel> v = lTenCtrl
			// .ExRicercaTenoriEstesiByIdProvvedimento(lTenore.getProvIdProvvedimentoSige());
			// Vector<TenoreSigeModel> lTenori = new Vector<TenoreSigeModel>();
			// for (TenoreSigeEstesoModel tsem : v)
			// lTenori.add(tsem.getTenoreSige());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>>> Numero Tenori = " + lTenori.size());

			// if (lTenori.size() > 0)
			// statoTenori = (((TenoreSigeModel) lTenori.firstElement()).getDataFine() == null);

			// setSessionAttribute("tenori", lTenori);
			setRequestAttribute("tenori", lTenori);
			setSessionAttribute("tenori", lTenori);
		}

		// Modificabilita'
		String lInseribile = "NO";
		String lModificabile = "NO";
		String lCancellabile = "NO";
		String lStampabile = "NO";

		if (IsFascicoloSigeIscrittoCompetenza()) {
			// 21/07/2009 Udienza Cancellabile solo se non validata e nessun provvedimento è stato emesso.
			if (lProvEveMod != null && lProvEveMod.getProvvedimento() != null) {
				// 20170919: [SG] aggiunta gestione data emissione e data udienza
				// Date dataEmissione = lProvEveMod.getProvvedimento().getDataEmissione();
				// boolean isDataEmissioneFittizia = false;
				// if (dataEmissione != null)
				// isDataEmissioneFittizia = "31/12/9999".equals(DateUtils.getDateToString(dataEmissione,
				// "dd/MM/yyyy"));
				// boolean isDataUdienzaFittizia = false;
				// if (lUdienzaSige != null) {
				// Date dataUdienza = lUdienzaSige.getDataUdienza();
				// if (dataUdienza != null)
				// isDataUdienzaFittizia = "31/12/9999".equals(DateUtils.getDateToString(dataUdienza,
				// "dd/MM/yyyy"));
				// }
				lEveMod = lProvEveMod.getEventoNotifica();
				if ((lEveMod.getEvento().getFlagDocumentoRegistrato() == null
						|| lEveMod.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0)
				/* && (statoTenori || isDataEmissioneFittizia || isDataUdienzaFittizia) */) {
					lCancellabile = "SI";
					lModificabile = "SI";
					lStampabile = "SI";
				}
			} else {
				lCancellabile = "SI";
				lModificabile = "SI";
				lStampabile = "SI";
			}
		}

		// 29/07/2009 Se si sta riprovando la fissazione udienza, si recupera il relativo parametro di
		// cancellazione.
		if (!(isRequestAttributeNullObj("Cancellabile"))
				&& getRequestStringParameter("Cancellabile").length() == 2)
			lCancellabile = getRequestStringParameter("Cancellabile");

		// Imposta in request ulteriori parametri.
		setRequestAttribute("Inseribile", lInseribile);
		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Cancellabile", lCancellabile);
		setRequestAttribute("Stampabile", lStampabile);

		if (!isRequestParameterNullObj("modalita"))
			setRequestAttribute("modalita", getRequestStringParameter("modalita"));

		// 29/07/2009 Predisposizione Alert presenza rinvio.
		if (!isRequestParameterNullObj("presenzaRinvio"))
			setRequestAttribute("rinvio", getRequestStringParameter("presenzaRinvio"));

		gestioneTemplate(lFasEsteso.getFascicoloSige().getCodTipoGiudizio().trim(), lEveMod.getEvento());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".ActLoadDettaglioFissazioneUdienza: fine");
		return PG_LOAD_DETTAGLIOFISSAZIONEUDIENZA;
	}

	// Costruzione della Combo con i template di stampa
	private void gestioneTemplate(String lFlagTemplate, EventoModel aEvento) throws Exception {
		Option lOptTemplate = null;

		// Si valorizza il filtro di ricerca sui Template
		TemplateModel lTempRic = new TemplateModel();
		lTempRic.setCodTipoProvvedimento(aEvento.getCodTipoProvvedimento());
		lTempRic.setCodTipoEvento(aEvento.getCodTipoEvento());
		lTempRic.setCodEsito(aEvento.getCodEsito());
		lTempRic.setFlagTemplate(lFlagTemplate);

		// Ricerca dei Template e creazione della Combo
		lOptTemplate = UtilTemplate.listaCbxTemplate(lTempRic);

		setRequestAttribute(ICostantiTemplate.CAMPO_COMBO_TEMPLATE, "" + lOptTemplate);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ElencoTemplate nella Combo -> " + lOptTemplate);
	}

}