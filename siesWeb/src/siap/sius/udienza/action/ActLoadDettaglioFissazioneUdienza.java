package siap.sius.udienza.action;

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
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.curatore.controller.ICuratore;
import siap.sige.curatore.model.CuratoreModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.ActionSius;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.curatore.controller.ICuratoreSius;
import siap.sius.curatore.model.CuratoreSiusModel;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.tenore.controller.ITenore;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioFissazioneUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load della jsp di dettaglio.
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
public class ActLoadDettaglioFissazioneUdienza extends ActionSius implements ICostantiUdienza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".ActLoadDettaglioDecretoDeposito: inizio");
		BigDecimal lIdUdienza = null;

		this.setLinkRitorno();
		// I parametri passati all'azione con il metodo get.
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Chiama il controller Evento per ricerca di un evento
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("###### Invoca la ricerca EventoNotifica : ");
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		// lEveMod = lCtrl.ExRicercaEventoNotificaByKeyForUdienza(lIdEvento);
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);

		// Chiama il controller Udienza_Procedimento per risalire all'Udienza
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("###### Invoca la ricerca UDIENZA_PROCEDIMENTO : ");
		UdienzaProcedimentoModel lUdienzaProcedimento = null;
		IUdienzaProcedimento lUdiProCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
		lUdienzaProcedimento = lUdiProCtrl.ExRicercaUdienzaProcedimentoByEve(lIdEvento);
		if (lUdienzaProcedimento != null) {
			// Se trovato UDIENZA_PROCEDIMENTO si ricava l'ID Udienza e
			// l'ID UDIENZA_PROCEDIMENTO viene passato nella request.
			lIdUdienza = lUdienzaProcedimento.getUdiIdUdienza();
			setRequestAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO,
					lUdienzaProcedimento.getIdUdienzaProcedimento().toString());
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("###### Invoca la ricerca FascicoloByKey : ");
		BigDecimal lIdFascicolo = lEveMod.getEvento().getFasSiuIdFascicoloSius();
		IFascicoloSius lFas = SIUSLookupRemote.getFascicoloSiusRemote();
		FascicoloGPModel lFascicoloGPModel = lFas.ExRicercaFascicoloByKey(lIdFascicolo);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lFascicoloGPModel = " + lFascicoloGPModel);

		// ID Udienza viene ricavata dal fascicolo
		if (lIdUdienza == null) {
			lIdUdienza = lFascicoloGPModel.getGeneraleProcedimentoModel().getUdiIdUdienza();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("ID Udienza viene ricavato dal fascicolo !");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("##### ID Udienza = " + lIdUdienza);
		UdienzaModel lUdienza = new UdienzaModel();
		IUdienza lUdi = SIUSLookupRemote.getUdienzaRemote();
		lUdienza = lUdi.ExRicercaUdienzaByKey(lIdUdienza);

		// Avvocati assegnati al fascicolo con data fine = null
		AvvocatoFascicoloSiusModel lAvvFascMod = new AvvocatoFascicoloSiusModel();
		lAvvFascMod
				.setFasSiuIdFascicoloSius((lFascicoloGPModel.getFascicoloSiusModel()).getIdFascicoloSius());

		IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector<?> lAvvocato = lAvvCtrl.ExRicercaAvvocatiAttualiFascicolo(null, lAvvFascMod);

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl.ExRicercaEstesaMagRelByFascicolo(
				lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());

		// Lettura delle notifiche.
		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		Vector lVect = lCtrlNot.ExRicercaEstesaNotificaByKeyEvento(lIdEvento);

		// Lettura del Deposito Decreto.
		IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
		DepositoDecretoModel llDecMod = lCtrlDD
				.ExRicercaDepositoDecretoByIdEvento(lEveMod.getEvento().getIdEvento());

		// Preleva i tenori, per il generale procedimento.
		ITenore lTenCtrl = SIUSLookupRemote.getTenoreRemote();
		Vector lTenori = lTenCtrl.ExRicercaTenoreByDecreto(llDecMod.getIdDepositoDecreto());

		// Eventuale Curatore Sius 18/05/2011
		UfficioModel lUff = getUfficioUtenteConnesso();
		CuratoreSiusModel lCuratore = null;
		if (lUff.getCodTipoUfficio().equals("UDS")) {
			ICuratoreSius lCurSiusCtrl = SIUSLookupRemote.getCuratoreSiusRemote();
			lCuratore = lCurSiusCtrl.ExRicercaCurSiusByFascicolo(
					lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius());
			if (lCuratore != null) {
				// Curatore
				if (lCuratore.getCurIdCuratore() != null) {
					ICuratore lCurCtrl = SIGELookupRemote.getCuratoreRemote();
					CuratoreModel lCurMod = lCurCtrl.ExRicercaCuratoreByKey(lCuratore.getCurIdCuratore());
					lCuratore.setCuratore(lCurMod);
				}
			}
			setRequestAttribute("curatore", lCuratore);
		}

		// Imposta gli oggetti nella request.
		setRequestAttribute("tenori", lTenori); // Tenori
		String lDescOggetti = new String();
		if (lFascicoloGPModel.getTenori() != null) {
			for (int i = 0; i < lFascicoloGPModel.getTenori().length; i++) {
				lDescOggetti += lFascicoloGPModel.getTenori()[i].getDescrOggettoTenore() + "\n";
			}
		}

		// Modificabilità
		String lModificabile = "NO";
		String lCancellabile = "NO";
		if (IsFascicoloSiusModificabile()) {
			lModificabile = "SI";
			// Stampabilità
			if (lEveMod.getEvento().getFlagDocumentoRegistrato() == null
					|| lEveMod.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0)
				lCancellabile = "SI";
		}

		// Imposta in request i parametri necessari
		setRequestAttribute("fascicoloGP", lFascicoloGPModel);
		setRequestAttribute("eventonotifica", lEveMod);
		setRequestAttribute("udienza", lUdienza);
		setRequestAttribute("avvocato", lAvvocato);
		setRequestAttribute("magistratorelatore", lMagRel);
		setRequestAttribute("notifiche", lVect);
		setRequestAttribute("descOggetti", lDescOggetti);
		setRequestAttribute("depositoDecreto", llDecMod);
		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Cancellabile", lCancellabile);
		if (!isRequestParameterNullObj("modalita"))
			setRequestAttribute("modalita", getRequestStringParameter("modalita"));

		gestioneTemplate(lEveMod.getEvento());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".ActLoadDettaglioDecretoDeposito: fine");
		return PG_LOAD_DETTAGLIOFISSAZIONEUDIENZA;
	}

	// Costruzione della Combo con i template di stampa
	private void gestioneTemplate(EventoModel aEvento) throws Exception {
		Option lOptTemplate = null;

		// Ufficio Utente connesso serve per ricavare il FLAG TEMPLATE
		UfficioModel lUff = getUfficioUtenteConnesso();

		// Si valorizza il filtro di ricerca sui Template
		TemplateModel lTempRic = new TemplateModel();
		lTempRic.setCodTipoProvvedimento(aEvento.getCodTipoProvvedimento());
		lTempRic.setCodTipoEvento(aEvento.getCodTipoEvento());
		lTempRic.setCodEsito(aEvento.getCodEsito());
		lTempRic.setFlagTemplate(lUff.getCodTipoUfficio().toString().substring(0, 1));

		// Ricerca dei Template e creazione della Combo
		lOptTemplate = UtilTemplate.listaCbxTemplate(lTempRic);

		setRequestAttribute(ICostantiTemplate.CAMPO_COMBO_TEMPLATE, "" + lOptTemplate);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ElencoTemplate nella Combo -> " + lOptTemplate);

		// template di default
		/*
		 * String[] lSelected = lOptTemplate.getSelecteds(); if (lSelected != null && lSelected.length > 0) {
		 * setRequestAttribute(ICostantiTemplate.CAMPO_DEFAULT_TEMPLATE, lSelected[0]); // [FT] - 03/08/2016 -
		 * MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug("TemplateDiDefault -> " + lSelected[0]); }
		 */
		return;
	}

}