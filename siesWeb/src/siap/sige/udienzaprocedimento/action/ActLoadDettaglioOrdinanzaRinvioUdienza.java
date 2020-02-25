package siap.sige.udienzaprocedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.template.util.UtilTemplate;
import siap.sico.util.SICOLookupRemote;
import siap.sige.aula.controller.IAula;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActLoadDettaglioOrdinazaRinvioUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load della jsp di dettaglio Ordinanza Rinvio Udienza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioOrdinanzaRinvioUdienza extends ActionSige
		implements ICostantiUdienzaProcedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest(): inizio");
		BigDecimal lIdUdienza = null;

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = this.getFascicoloSigeEstesoInSessione();

		this.setLinkRitorno();
		// I parametri passati all'azione con il metodo get.
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Chiama il controller Evento per ricerca di un evento
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("###### Invoca la ricerca Evento ######");

		EventoModel lEveMod = new EventoModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);
		setRequestAttribute("evento", lEveMod);

		// Chiama il controller Udienza_Procedimento per risalire all'Udienza
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("###### Invoca la ricerca UDIENZA_PROCEDIMENTO_SIGE ######");
		UdienzaProcedimentoSigeModel lUdienzaProcedimentoSige = null;
		IUdienzaProcedimentoSige lUdiProCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		lUdienzaProcedimentoSige = lUdiProCtrl.ExRicercaUdienzaProcedimentoByEve(lIdEvento);

		if (lUdienzaProcedimentoSige != null) {
			// Se trovato UDIENZA_PROCEDIMENTO_SIGE si ricava l'ID Udienza e
			// l'ID_UDIENZA_PROCEDIMENTO_SIGE viene passato nella request.
			lIdUdienza = lUdienzaProcedimentoSige.getUdiIdUdienzaSige();
			setRequestAttribute(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE,
					lUdienzaProcedimentoSige.getIdUdienzaProcedimentoSige().toString());
			setRequestAttribute("udienzaProcedimentoSige", lUdienzaProcedimentoSige);

			IPartiUdienza lCtrlParti = SIGELookupRemote.getPartiUdienzaRemote();
			Vector<?> lPartiC = lCtrlParti.ExRicercaPartiUdienzaByIdUdienza(
					lUdienzaProcedimentoSige.getIdUdienzaProcedimentoSige(), "C");
			setRequestAttribute("udienzaPartiC", lPartiC);
			Vector<?> lPartiO = lCtrlParti.ExRicercaPartiUdienzaByIdUdienza(
					lUdienzaProcedimentoSige.getIdUdienzaProcedimentoSige(), "O");
			setRequestAttribute("udienzaPartiO", lPartiO);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>>>> ID Udienza = " + lIdUdienza);
		UdienzaSigeModel lUdienzaSige = new UdienzaSigeModel();
		IUdienzaSige lUdi = SIGELookupRemote.getUdienzaSigeRemote();
		lUdienzaSige = lUdi.ExRicercaUdienzaSigeById(lIdUdienza);

		BigDecimal idAula = null;
		try {
			idAula = lUdienzaSige.getCodIdAulaUdienza();
		} catch (Exception e) {
		}
		if (idAula != null) {
			IAula ctrlAula = SIGELookupRemote.getAulaRemote();
			AulaUdienzaModel aula = ctrlAula.ExRicercaAulaByIdAula(idAula);
			lUdienzaSige.setAulaUdienzaModel(aula);
		}

		BigDecimal idSezione = null;
		try {
			idSezione = lUdienzaSige.getCodIdSezioneUdienza();
		} catch (Exception e) {
		}

		if (idSezione != null) {
			ISezione ctrlSez = SIGELookupRemote.getSezioneRemote();
			SezioneModel sezioneModel = ctrlSez.ExRicercaSezioneByKey(idSezione);
			lUdienzaSige.setSezioneModel(sezioneModel);
		}

		// [EC] - 20171018 passo il giudice sulla pagina
		if (lUdienzaSige != null && lUdienzaSige.getColIdCollegio() != null
				&& !"0".equals(lUdienzaSige.getColIdCollegio().toString())
				&& lUdienzaSige.getCollegio().getCollegioMagistrati() != null
				&& lUdienzaSige.getCollegio().getCollegioMagistrati().length > 0) {
			// in questo caso sto trattando un rito collegiate, quindi il codice giudice lo devo recuperare
			// dall'oggetto magistrato collegio
			lUdienzaSige.setCodGiudice(
					lUdienzaSige.getCollegio().getCollegioMagistrati()[0].getMagCodMagistrato());
			lUdienzaSige.setDescrGiudice((lUdienzaSige.getCollegio().getCollegioMagistrati()[0]
					.getMagistrato().getCognome() + " "
					+ lUdienzaSige.getCollegio().getCollegioMagistrati()[0].getMagistrato().getNome()));
		}

		setRequestAttribute("udienza", lUdienzaSige);

		// Avvocati attuali assegnati al fascicolo SIGE.
		FascicoloSigeUtils lFasSigeUtils = new FascicoloSigeUtils();
		Vector lAvvocati = lFasSigeUtils.ricercaAvvocati(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		if (lAvvocati.size() > 0)
			setRequestAttribute("avvocato", lAvvocati);

		// Magistrato Assegnatario
		MagistratoAssegnatarioModel lMagAss = lFasEsteso.getMagAssegnatario();
		setRequestAttribute("magistratoassegnatario", lMagAss);

		// Lettura del Decreto.
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();

		ProvvedimentoSigeEventoModel lProvEveMod = lCtrlProv
				.ExRicercaProvvedimentoByIdEvento(lEveMod.getIdEvento());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(
				">>>>> IdProvvedimento Sige = " + lProvEveMod.getProvvedimento().getIdProvvedimentoSige());
		setRequestAttribute("ProvvedimentoEvento", lProvEveMod);

		// 20190520 [SG]: gestione tenori
		// Rimozione dell'elenco Tenori dalla sessione
		// removeSessionAttribute("tenori");
		if (isSessionAttributeNullObj("tenori")) {
			// Ricerca tenori legati al provvedimento.
			TenoreSigeModel lTenore = new TenoreSigeModel();
			lTenore.setProvIdProvvedimentoSige(lProvEveMod.getProvvedimento().getIdProvvedimentoSige());
			ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
			Vector lTenori = lTenCtrl.ExRicercaTenori(lTenore);
			setRequestAttribute("tenori", lTenori);
			setSessionAttribute("tenori", lTenori);
		} else
			setRequestAttribute("tenori", getSessionAttribute("tenori"));

		// Modificabilità.
		String lModificabile = "NO";
		String lCancellabile = "NO";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>>>> Iscritto competenza : " + IsFascicoloSigeIscrittoCompetenza());

		// if (IsFascicoloSigeIscrittoCompetenza()) {
		if (IsFascicoloSigeModificabile()) {
			// 20171011: [SG] il fascicolo anche se modificabile bisogna controllare se validato è l'evento
			if (lEveMod.getFlagDocumentoRegistrato() == null
					|| !lEveMod.getFlagDocumentoRegistrato().equalsIgnoreCase("S"))
				lModificabile = "SI";
			// 22/07/2009 Ordinanza Rinvio Udienza Cancellabile solo se non validata e nessun provvedimento
			// successivo è stato emesso.
			if ((lEveMod.getFlagDocumentoRegistrato() == null
					|| lEveMod.getFlagDocumentoRegistrato().compareTo("N") == 0)
			// 20171011: [SG] rimozione variabile statoTenori (un provvedimento lo si può modificare o meno
			// solo in base al fatto che sia validato!
			/* && (((TenoreSigeModel) lTenori.firstElement()).getDataFine() == null) */)
				lCancellabile = "SI";
		}

		// 20171011: [SG] il fascicolo anche se modificabile bisogna controllare se validato è l'evento
		if (lEveMod.getFlagDocumentoRegistrato() == null
				|| lEveMod.getFlagDocumentoRegistrato().compareTo("S") == 0)
			setRequestAttribute("Inseribile", "NO");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>>>> Cancellabile : " + lCancellabile);

		// Imposta in request ulteriori parametri.
		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Cancellabile", lCancellabile);

		if (!isRequestParameterNullObj("modalita"))
			setRequestAttribute("modalita", getRequestStringParameter("modalita"));

		gestioneTemplate(lFasEsteso.getFascicoloSige().getCodTipoGiudizio().trim(), lEveMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest(): fine");

		// [EC] - 20171017 passo il tipo giudizio alla pagina di dettaglio
		if (lFasEsteso.getFascicoloSige().getCodTipoGiudizio() != null)
			setRequestAttribute("tipoGiudizioVal", lFasEsteso.getFascicoloSige().getCodTipoGiudizio());

		return PG_LOAD_DETTAGLIOORDINANZARINVIOUDIENZA;
	}

	// Costruzione della Combo con i template di stampa.
	private void gestioneTemplate(String lFlagTemplate, EventoModel aEvento) throws Exception {

		Option lOptTemplate = null;

		// Si valorizza il filtro di ricerca sui Template
		TemplateModel lTempRic = new TemplateModel();
		lTempRic.setCodTipoProvvedimento(aEvento.getCodTipoProvvedimento());
		lTempRic.setCodTipoEvento(aEvento.getCodTipoEvento());
		lTempRic.setCodEsito(aEvento.getCodEsito());
		lTempRic.setFlagTemplate(lFlagTemplate);

		// Ricerca dei Template e creazione della Combo.
		lOptTemplate = UtilTemplate.listaCbxTemplate(lTempRic);

		setRequestAttribute(ICostantiTemplate.CAMPO_COMBO_TEMPLATE, "" + lOptTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(">>>>> ElencoTemplate nella Combo -> " + lOptTemplate);

		return;
	}

}