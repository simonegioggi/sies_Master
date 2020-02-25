package siap.sige.udienzaprocedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.aula.controller.IAula;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadModificaOrdinanzaVerbaleRinvioUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load della form di inserimento Ordinanza Rinvio Udienza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadModificaOrdinanzaVerbaleRinvioUdienza extends ActLoadInserisciVerbaleRinvioUdienza
		implements ICostantiUdienzaProcedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// protected FascicoloSigeEstesoModel mFasEsteso ;
	// protected String mRetPage = PG_LOAD_INSERISCIORDINANZARINVIOUDIENZA;

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest(): inizio");
		BigDecimal lIdUdienza = null;
		super.processRequest();
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
		setRequestAttribute("dataEmissione", lEveMod.getDataEmissione());

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
			setRequestAttribute("idUdienzaProcedimentoSige",
					lUdienzaProcedimentoSige.getIdUdienzaProcedimentoSige().toString());

			// recupero anche il flag_rinvio
			String flagRinvio = lUdienzaProcedimentoSige.getFlagRinviata();
			super.setRequestAttribute("flagRinvio", flagRinvio != null ? flagRinvio : "");
		}
		// Modifica del 24/02/2016 Nuova Infrastruttura - INIZIO ******
		BigDecimal idAula = null;
		String idSezione = null;
		UdienzaSigeModel lUdienzaSige = null;

		if (lIdUdienza != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(">>>>> ID Udienza = " + lIdUdienza);
			IUdienzaSige lUdi = SIGELookupRemote.getUdienzaSigeRemote();
			lUdienzaSige = lUdi.ExRicercaUdienzaSigeById(lIdUdienza);
			super.setRequestAttribute("idUdienzaSige", lUdienzaSige.getIdUdienzaSige().toString());
			String oraInizio = (lUdienzaSige.getOraInizio() == null
					|| lUdienzaSige.getOraInizio().equalsIgnoreCase("NULL") ? ""
							: lUdienzaSige.getOraInizio());
			String minInizio = (lUdienzaSige.getMinInizio() == null
					|| lUdienzaSige.getMinInizio().equalsIgnoreCase("NULL") ? ""
							: lUdienzaSige.getMinInizio());
			String oraFine = (lUdienzaSige.getOraFine() == null
					|| lUdienzaSige.getOraFine().equalsIgnoreCase("NULL") ? "" : lUdienzaSige.getOraFine());
			String minFine = (lUdienzaSige.getMinFine() == null
					|| lUdienzaSige.getMinFine().equalsIgnoreCase("NULL") ? "" : lUdienzaSige.getMinFine());
			super.setRequestAttribute("oraInizio", oraInizio);
			super.setRequestAttribute("minInizio", minInizio);
			super.setRequestAttribute("oraFine", oraFine);
			super.setRequestAttribute("minFine", minFine);

			idAula = lUdienzaSige.getCodIdAulaUdienza();
		}
		// Modifica del 24/02/2016 Nuova Infrastruttura - FINE ******

		if (idAula != null) {
			IAula ctrlAula = SIGELookupRemote.getAulaRemote();
			AulaUdienzaModel aula = ctrlAula.ExRicercaAulaByIdAula(idAula);
			// Modifica del 24/02/2016 Nuova Infrastruttura - INIZIO ******
			if (lUdienzaSige != null) {
				lUdienzaSige.setAulaUdienzaModel(aula);
			}
			// Modifica del 24/02/2016 Nuova Infrastruttura - FINE ******
			// [EC] 20171017 controllo per valori a null
			super.setRequestAttribute("descrizioneAula",
					aula.getDescrizioneAula() != null ? aula.getDescrizioneAula() : "");
			super.setRequestAttribute("ingressoAula",
					aula.getDescrizioneIngresso() != null ? aula.getDescrizioneIngresso() : "");
			super.setRequestAttribute("pianoAula",
					aula.getNumeroPiano() != null ? aula.getNumeroPiano().toString() : "");
			super.setRequestAttribute("idAula", aula.getIdAula() != null ? aula.getIdAula().toString() : "");
			idSezione = aula.getIdSezione().toString();
		}

		if (idSezione == null) {
			// // [EC] - 20171019 provo a recuperare la sezione dall'oggetto lUdienzaSige e non dall'aula
			if (lUdienzaSige != null && lUdienzaSige.getCodIdSezioneUdienza() != null)
				idSezione = lUdienzaSige.getCodIdSezioneUdienza().toString();
			else
				idSezione = this.getIdSezione();
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

		super.getRequest().setAttribute("luogoSvolgimento",
				lProvEveMod.getProvvedimento().getLuogoSvolgimento());

		TenoreSigeModel lTenore = new TenoreSigeModel();
		lTenore.setProvIdProvvedimentoSige(lProvEveMod.getProvvedimento().getIdProvvedimentoSige());

		// Modifica del 24/02/2016 Nuova Infrastruttura - INIZIO ******
		if (lUdienzaSige != null) {
			super.setRequestAttribute("gg", DateUtils.getDateToString(lUdienzaSige.getDataUdienza(), "dd"));
			super.setRequestAttribute("mm", DateUtils.getDateToString(lUdienzaSige.getDataUdienza(), "MM"));
			super.setRequestAttribute("aaaa",
					DateUtils.getDateToString(lUdienzaSige.getDataUdienza(), "yyyy"));
		}
		// Modifica del 24/02/2016 Nuova Infrastruttura - FINE ******

		// Solo la prima volta vengono messi i Tenori in sessione
		if (isRequestParameterNullObj(IWebConstants.FLAG_RITORNO) ||
		// 20190520 [SG]: aggiunta or condition
				isSessionAttributeNullObj("tenori")) {
			ITenoreSige lTenCtrl = SIGELookupRemote.getTenoreSigeRemote();
			Vector lTenori = lTenCtrl.ExRicercaTenori(lTenore);
			setRequestAttribute("tenori", lTenori);
			setSessionAttribute("tenori", lTenori);
		} else
			setRequestAttribute("tenori", getSessionAttribute("tenori"));

		Option lOptionSezioniUdienza = new Option(
				SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), "-", Option.BLANK_ITEM);
		lOptionSezioniUdienza.setValueBlankItem("-");
		lOptionSezioniUdienza.setSelected(idSezione);
		setRequestAttribute("elencoSezioniUdienza", lOptionSezioniUdienza.toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest(): fine");

		return PG_LOAD_MODIFICAORDINANZVERBALERINVIOUDIENZA;
	}

	private String getIdSezione() throws F3BException {

		String lSezioneUdienza = "-";
		mFasEsteso = getFascicoloSigeEstesoInSessione();
		IMagistratoAssegnatario lMagCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
		MagistratoAssegnatarioModel lMagAss = lMagCtrl
				.ExRicercaEstesaMagAssCorrenteXFascicolo(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
		if (lMagAss != null) {
			IMagistrato lCtrl = SIGELookupRemote.getMagistratoRemote();
			// 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un
			// ufficio differente da quello in cui ha delle udienze poichè trasferito
			MagistratoModel lMagMod = lCtrl.ExRicercaMagistratoByCod(lMagAss.getMagCodMagistrato(),
					getCodUfficioUtenteConnesso());
			if (lMagMod.getMagistratoSezioni().length > 0) {
				BigDecimal idSezMag = lMagMod.getMagistratoSezioni()[0].getSezIdSezione();
				if (idSezMag != null) {
					lSezioneUdienza = idSezMag.toString();
				}
			}
		}
		return lSezioneUdienza;
	}

}