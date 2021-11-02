package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.calcolopena.action.ActCalcoloPenaMain;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.model.VerbaleModel;

@SuppressWarnings("rawtypes")
public class ActCalcolaAvvenutaEspulsione extends ActionSiap implements ICostantiSospensione {

	/**
	 * Azione di Calcolo dell'Avvenuta Espulsione
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		// Preparo L'evento Verbale
		EventoModel lEveVer = new EventoModel();

		lEveVer.setCodTipoEvento("07");
		lEveVer.setCodTipoProvvedimento("27");
		lEveVer.setCodMotivo("2141");
		lEveVer.setFlagStampaSiep("S");
		lEveVer.setFlagVideoSiep("S");
		lEveVer.setFlagDocumentoRegistrato("N");
		lEveVer.setCodUfficioEmittente(lCodiceUfficio);

		lEveVer.setFasSieIdFascicoloSiep(lIdFascicolo);
		Date lDataEmissione = getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE, ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE);
		lEveVer.setDataEmissione(lDataEmissione);
		lEveVer.setCodOperatoreInserimento(lCodiceOperatore);
		lEveVer.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEveVer.setDataInserimento(DateUtils.getSysDate());
		lEveVer.setCodUfficioInserimento(lCodiceUfficio);
		lEveVer.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveVer.setCodEsito("-");
		lEveVer.setCodLuogoDestinatario("-");
		lEveVer.setCodTipoUfficioDestinatario("-");
		lEveVer.setCodMagistrato("-");

		// Verbale
		VerbaleModel lVerMod = new VerbaleModel();
		lVerMod.setCodTipoVerbale("05");
		lVerMod.setDataPervenimento(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO,
				ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO,
				ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO));

		lVerMod.setDataEmissione(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE, ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE));

		lVerMod.setIstDetIdIstitutoDetenzione("-");

		String lDescr = null;

		if (!this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO)
				&& !this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO)) {
			ComuneModel lComMod = this.getCodComuneByDescr(
					getRequestStringParameter(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO));

			lVerMod.setCodLuogoUfficioFirmatario(lComMod.getCodComune());
			lVerMod.setCodTipoUfficioFirmatario(
					getRequestStringParameter(ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO));

			lDescr = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoAutorita(),
					lVerMod.getCodTipoUfficioFirmatario());
			lDescr += " di " + getRequestStringParameter(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO);
		} else {
			lVerMod.setCodTipoUfficioFirmatario("-");
			lVerMod.setCodLuogoUfficioFirmatario("-");
		}

		lVerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lVerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lVerMod.setDataInserimento(DateUtils.getSysDate());

		if (!this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO))
			lVerMod.setNumeroProtocollo(getRequestStringParameter(ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO));

		if (!this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_NOTE))
			lVerMod.setNote(getRequestStringParameter(ICostantiVerbale.CAMPO_NOTE));

		// ricerca misura alternativa per trovare la durata dell'espulsione altrimenti si considerano
		// 10 anni come valore di default
		String[] lNatura = { "CO" };
		String[] lTipoMisura = { "2140" };
		String[] lTipoDecisione = { "02" };
		IMisuraAlternativaIndultino lCtrMis = SICOLookupRemote.getMisuraAlternativaRemoteIndultino();
		MisuraAlternativaModel lMisMod = lCtrMis
				.ExRicercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisione(
						lFascMod.getIdFascicoloSiep(), lTipoDecisione, lNatura, lTipoMisura);

		int lAnni = 0;
		int lMesi = 0;
		int lGiorni = 0;

		if (lMisMod != null && lMisMod.getIdMisuraAlternativa() != null) {
			if (lMisMod.getNumAnniMisura() != null
					&& lMisMod.getNumAnniMisura().compareTo(new BigDecimal(0)) != 0)
				lAnni = lMisMod.getNumAnniMisura().intValue();

			if (lMisMod.getNumMesiMisura() != null
					&& lMisMod.getNumMesiMisura().compareTo(new BigDecimal(0)) != 0)
				lMesi = lMisMod.getNumMesiMisura().intValue();

			if (lMisMod.getNumGiorniMisura() != null
					&& lMisMod.getNumGiorniMisura().compareTo(new BigDecimal(0)) != 0)
				lGiorni = lMisMod.getNumGiorniMisura().intValue();
		}

		if (lAnni == 0 && lMesi == 0 && lGiorni == 0) {
			lAnni = 10;
			lMesi = 0;
			lGiorni = 0;
		}

		lVerMod.setNumAnniEspulsione(new BigDecimal(lAnni));
		lVerMod.setNumMesiEspulsione(new BigDecimal(lMesi));
		lVerMod.setNumGiorniEspulsione(new BigDecimal(lGiorni));

		// ==========================================================================
		// Effettua il ricalcolo della pena residua e già espiata
		// ==========================================================================
		ActCalcoloPenaMain lCalcoloPenaMain = new ActCalcoloPenaMain();
		CalcoloPenaModel lCalcoloPenaModel = lCalcoloPenaMain.calcoloPena(lIdFascicolo, null);

		IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lUltimaPenaResidua = IPenRes
				.ExRicercaPenaResiduaUltimaValidata(lFascMod.getIdFascicoloSiep());

		// pena complessiva
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);
		String lFlagErgastolo = "N";

		// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		if (lPenMod.getCodTipoPenaDetentiva() != null && lPenMod.getCodTipoPenaDetentiva() != ""
				&& (lPenMod.getCodTipoPenaDetentiva().equals("03")
						|| lPenMod.getCodTipoPenaDetentiva().equals("04"))) {
			lFlagErgastolo = "S";
		}

		PenaResiduaModel lPenaResiduaNuova = new PenaResiduaModel();
		CalendarModel lPenaEspiataSosp = new CalendarModel();

		if (lFlagErgastolo.equals("N")) {
			try {
				PenaResiduaModel lPenaResiduaIniziale = lCalcoloPenaModel
						.getPenaDaEspiare(lUltimaPenaResidua.getDataInizio(), null, "all");
				lCalcoloPenaModel.calcolaPenaDaSospensione(lPenaResiduaIniziale,
						getRequestDateParameter(ICostantiSospensione.CAMPO_ANNO_DATA_ESPULSIONE,
								ICostantiSospensione.CAMPO_MESE_DATA_ESPULSIONE,
								ICostantiSospensione.CAMPO_GIORNO_DATA_ESPULSIONE));
				lPenaResiduaNuova = lCalcoloPenaModel.getPenaResiduaRicalcolata();
				lPenaEspiataSosp = lCalcoloPenaModel.getPenaEspiata();
			} catch (Exception e) {
			}

			// Vengono settati quei parametri
			// che non vengono gestiti nel CalcoloPenaModel
			lPenaResiduaNuova.setFasSieIdFascicoloSiep(lIdFascicolo);
			lPenaResiduaNuova.setDiesAQuo(lUltimaPenaResidua.getDiesAQuo());
			lPenaResiduaNuova.setFlagErgastolo(lUltimaPenaResidua.getFlagErgastolo());
			lPenaResiduaNuova.setFlagValidato("N");
			lPenaResiduaNuova.setFlagPenaSospesa("E");
		} else {
			// Se Ergastolo non ricalcolo la pena, ma copio i dati dell'ultimo record
			lPenaResiduaNuova = lUltimaPenaResidua;
			lPenaResiduaNuova.setMisAltIdMisuraAlternativa(null);
			lPenaResiduaNuova.setFlagPenaSospesa("E");
			lPenaResiduaNuova.setFlagValidato("N");
		}

		// Gestione della Data Inserimento
		lPenaResiduaNuova.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPenaResiduaNuova.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPenaResiduaNuova.setDataInserimento(DateUtils.getSysDate());

		lPenaResiduaNuova.setCodOperatoreAggiornamento(null);
		lPenaResiduaNuova.setDataAggiornamento(null);
		lPenaResiduaNuova.setCodUfficioAggiornamento(null);

		if (lFlagErgastolo.equals("S")) {
			lPenaResiduaNuova.setDataFine(DateUtils.getDate(9999, 12, 31));
			if (lPenMod.getCodTipoPenaDetentiva().equals("03")) {
				lPenaResiduaNuova.setFlagErgastolo("S");
			} else if (lPenMod.getCodTipoPenaDetentiva().equals("04")) {
				lPenaResiduaNuova.setFlagErgastolo("D");
			}
		}

		// preparo il model di sospensione
		SospensioneModel lSospensione = new SospensioneModel();
		lSospensione.setDataInizio(getRequestDateParameter(ICostantiSospensione.CAMPO_ANNO_DATA_ESPULSIONE,
				ICostantiSospensione.CAMPO_MESE_DATA_ESPULSIONE,
				ICostantiSospensione.CAMPO_GIORNO_DATA_ESPULSIONE));

		if (lFlagErgastolo.equals("N")) {

			lSospensione.setAmmendaResidua(lPenaResiduaNuova.getImportoAmmenda());
			lSospensione.setNumAnniPenaResiduaReclus(lPenaResiduaNuova.getNumAnniReclusione());
			lSospensione.setNumMesiPenaResiduaReclus(lPenaResiduaNuova.getNumMesiReclusione());
			lSospensione.setNumGiorniPenaResiduaReclus(lPenaResiduaNuova.getNumGiorniReclusione());
			lSospensione.setNumAnniPenaResiduaArres(lPenaResiduaNuova.getNumAnniArresto());
			lSospensione.setNumMesiPenaResiduaArres(lPenaResiduaNuova.getNumMesiArresto());
			lSospensione.setNumGiorniPenaResiduaArres(lPenaResiduaNuova.getNumGiorniArresto());
			lSospensione.setMultaResidua(lPenaResiduaNuova.getImportoMulta());

			lSospensione.setNumAnniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumAnni()));
			lSospensione.setNumMesiPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumMesi()));
			lSospensione.setNumGiorniPenaEspiata(new BigDecimal(lPenaEspiataSosp.getNumGiorni()));
		} else {
			// Calcolo la pena espiata come intervallo tra la data inizio e la data
			// di sospensione (considerato come giorno espiato)
			CalendarModel lCalPenaEspiataCalcoloErg = new CalendarModel();
			CalendarUtil lCalUtil = new CalendarUtil();

			lCalPenaEspiataCalcoloErg.setDataInizio(lUltimaPenaResidua.getDataInizio());
			lCalPenaEspiataCalcoloErg.setDataFine(lSospensione.getDataInizio());

			lCalPenaEspiataCalcoloErg = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiataCalcoloErg);
			lCalPenaEspiataCalcoloErg = lCalUtil.ricalcolaGAM(lCalPenaEspiataCalcoloErg);

			lSospensione.setNumAnniPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumAnni()));
			lSospensione.setNumMesiPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumMesi()));
			lSospensione.setNumGiorniPenaEspiata(new BigDecimal(lCalPenaEspiataCalcoloErg.getNumGiorni()));
		}

		lSospensione.setFasSieIdFascicoloSiep(lIdFascicolo);
		lSospensione.setNumGiorniLibanticipata(new BigDecimal(lCalcoloPenaModel.getLiberazioneAnticipata()));

		// calcolo la data fine espulsione
		Date lSommaAnni = null;
		Date lSommaMesi = null;
		Date lFineScadenza = null;

		lSommaAnni = DateUtils.moveDateTo(lSospensione.getDataInizio(), java.util.Calendar.YEAR, lAnni);
		lSommaMesi = DateUtils.moveDateTo(lSommaAnni, java.util.Calendar.MONTH, lMesi);
		lFineScadenza = DateUtils.moveDateTo(lSommaMesi, java.util.Calendar.DAY_OF_MONTH, lGiorni);

		lSospensione.setDataFine(lFineScadenza);

		lSospensione.setCodOperatoreInserimento(getCodUtenteConnesso());
		lSospensione.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lSospensione.setDataInserimento(DateUtils.getSysDate());

		// passo i model in request per inserirli contemporanemente alla comunicazione e alle notifiche!!!
		setRequestAttribute("sospensione", lSospensione);
		setRequestAttribute("penaresiduanuova", lPenaResiduaNuova);
		setRequestAttribute("verbale", lVerMod);
		setRequestAttribute("evento", lEveVer);
		setRequestAttribute("lDescr", lDescr);

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// Controllo Esistenza pena residua per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		setRequestAttribute("penaresidua", lPenaResMod);

		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// Avvocato
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		Option lOptionAvv = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOptionAvv);

		// Autorità E
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaE", "" + lOption);

		// Ufficio recupero crediti
		Option lOptionUffRecCrediti = new Option(DecodificheManager.getInstance().getTipoUfficio());
		// Ticket#20210922015 - Si aggiunge DIBM
		// lOptionUffRecCrediti.setFilter(new String[] { "-", "DIB", "CAP" });
		lOptionUffRecCrediti.setFilter(new String[] { "-", "DIB", "CAP", "DIBM" });
		// Ticket#20210922015 - FINE
		setRequestAttribute("uffrecrediti", "" + lOptionUffRecCrediti);

		// restituisce la jsp di VIEW
		return PG_LOAD_INSERISCI_COMUNICAZIONE_ESPULSIONE;
	}

}