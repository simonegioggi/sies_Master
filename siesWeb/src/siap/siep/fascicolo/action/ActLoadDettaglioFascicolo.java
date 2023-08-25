package siap.siep.fascicolo.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import siap.jms.ICostantiJMS;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.annotazioneesitotrasmissione.controller.IAnnotazioneEsitoTrasmissione;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.penacumulo.controller.IPenaCumulo;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.reato.controller.ReatoContinuazioneController;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.scadenzario.util.ScadenzarioUtils;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.ulterioresanzionecumulo.controller.IUlterioreSanzioneCumulo;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.util.SIUSLookupRemote;

/**
 * Classe che effettua il caricamento del dettaglio del fascicolo SIEP
 *
 * @author
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadDettaglioFascicolo extends ActionSiap implements ICostantiFascicoloSiep {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	void impostaEtichetta(int anni, int anniUltimoReato, FascicoloSiepModel lFascicoloMod,
			SentenzaModel lSentenzaMod, boolean anni_18_Maggiorenne) throws F3BException {

		String codUfficioUtente = "";
		if (getUtenteConnesso().getUfficioUtente() != null
				&& getUtenteConnesso().getUfficioUtente().getCodUfficio() != null) {
			codUfficioUtente = getUtenteConnesso().getUfficioUtente().getCodUfficio();
		}

		String etichettaEta = "";
		String oscuraEta = "";

		// a) il soggetto iscritto dai seguenti uffici : PM-GIP-DIB-TDS-UDS-CAP
		// è sempre 'MAGGIORENNE';
		if (lFascicoloMod.getCodTipoUfficio() != null && !lFascicoloMod.getCodTipoUfficio().equals("")
				&& (lFascicoloMod.getCodTipoUfficio().equals("PM")
						|| lFascicoloMod.getCodTipoUfficio().equals("GIP")
						|| lFascicoloMod.getCodTipoUfficio().equals("DIB")
						|| lFascicoloMod.getCodTipoUfficio().equals("TDS")
						|| lFascicoloMod.getCodTipoUfficio().equals("UDS")
						|| lFascicoloMod.getCodTipoUfficio().equals("CAP"))) {
			// etichetta non visibile
			etichettaEta = "";
			// rimozione etichetta non visibile
			oscuraEta = "NO";
		}
		// b) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// e dagli uffici DIBM-GIPM-CAPSM-UDSM-TDSM
		// è 'MINORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta' alla
		// data di sistema ha meno di 18 anni (per precisione, meno di 18 anni ed un giorno);
		else if (lFascicoloMod.getCodTipoUfficio() != null && !lFascicoloMod.getCodTipoUfficio().equals("")
				&& (lFascicoloMod.getCodTipoUfficio().equals("PMM")
						|| lFascicoloMod.getCodTipoUfficio().equals("DIBM")
						|| lFascicoloMod.getCodTipoUfficio().equals("GIPM")
						|| lFascicoloMod.getCodTipoUfficio().equals("CAPSM")
						|| lFascicoloMod.getCodTipoUfficio().equals("UDSM")
						|| lFascicoloMod.getCodTipoUfficio().equals("TDSM"))
				&& anni <= 18 && !anni_18_Maggiorenne) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#FF0040'>&nbsp;Minorenne (Anni "
					+ anni + ")&nbsp;</span>";
			oscuraEta = "NO";
		}
		// c) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// e dagli uffici DIBM-GIPM-CAPSM-UDSM-TDSM
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo la
		// 'Età presunta' alla data di sistema ha più di 18 anni ma meno di 25 anni
		// ed avente campo VISIBILITA_EX_MINORENNE = 'N';
		// MERGE v10 COLLAUDO: sostituito 25 con 24
		else if (lFascicoloMod.getCodTipoUfficio() != null && !lFascicoloMod.getCodTipoUfficio().equals("")
				&& (lFascicoloMod.getCodTipoUfficio().equals("PMM")
						|| lFascicoloMod.getCodTipoUfficio().equals("DIBM")
						|| lFascicoloMod.getCodTipoUfficio().equals("GIPM")
						|| lFascicoloMod.getCodTipoUfficio().equals("CAPSM")
						|| lFascicoloMod.getCodTipoUfficio().equals("UDSM")
						|| lFascicoloMod.getCodTipoUfficio().equals("TDSM"))
				&& anni <= 24 && lFascicoloMod.getVisibilitaMinorenne() != null
				&& lFascicoloMod.getVisibilitaMinorenne().equals("N")) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
			// rimozione etichetta non visibile
			oscuraEta = "NO";
		}
		// d) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// e dagli uffici DIBM-GIPM-CAPSM-UDSM-TDSM
		// è 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta' alla data di
		// sistema ha più di 18 anni ma meno di 26 anni ed avente campo VISIBILITA_EX_MINORENNE = '';
		else if (lFascicoloMod.getCodTipoUfficio() != null && !lFascicoloMod.getCodTipoUfficio().equals("")
				&& (lFascicoloMod.getCodTipoUfficio().equals("PMM")
						|| lFascicoloMod.getCodTipoUfficio().equals("DIBM")
						|| lFascicoloMod.getCodTipoUfficio().equals("GIPM")
						|| lFascicoloMod.getCodTipoUfficio().equals("CAPSM")
						|| lFascicoloMod.getCodTipoUfficio().equals("UDSM")
						|| lFascicoloMod.getCodTipoUfficio().equals("TDSM"))
				&& anni <= 24 && (lFascicoloMod.getVisibilitaMinorenne() == null
						|| lFascicoloMod.getVisibilitaMinorenne().equals(""))) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
			if (lFascicoloMod.getChiaveUfficio().equals(codUfficioUtente)) {
				oscuraEta = "SI";
			} else {
				oscuraEta = "NO";
			}
		}
		// e) il soggetto iscritto dalla Procura della Repubblica presso il Tribunale per i minorenni (PMM)
		// e dagli uffici DIBM-GIPM-CAPSM-UDSM-TDSM
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo
		// la 'Età presunta' alla data di sistema ha più di 25 anni;
		// MERGE v10 COLLAUDO: sostituito 25 con 24
		else if (lFascicoloMod.getCodTipoUfficio() != null && !lFascicoloMod.getCodTipoUfficio().equals("")
				&& (lFascicoloMod.getCodTipoUfficio().equals("PMM")
						|| lFascicoloMod.getCodTipoUfficio().equals("DIBM")
						|| lFascicoloMod.getCodTipoUfficio().equals("GIPM")
						|| lFascicoloMod.getCodTipoUfficio().equals("CAPSM")
						|| lFascicoloMod.getCodTipoUfficio().equals("UDSM")
						|| lFascicoloMod.getCodTipoUfficio().equals("TDSM"))
				&& anni > 24) {
			// etichetta non visibile
			etichettaEta = "";
			// rimozione etichetta non visibile
			oscuraEta = "NO";
		}

		// f) il soggetto iscritto dalla Procura Generale presso la Corte di Appello è sempre 'MAGGIORENNE';
		// CONDIZIONE INSERITA NEL PUNTO a)

		// g) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è 'MINORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta' alla
		// data di sistema ha meno di 18 anni (per precisione, meno di 18 anni ed un giorno);
		else if (lFascicoloMod.getCodTipoUfficio() != null && !lFascicoloMod.getCodTipoUfficio().equals("")
				&& lFascicoloMod.getCodTipoUfficio().equals("PGCAP")
				&& (lSentenzaMod != null && lSentenzaMod.getCodTipoAutoritaEmittente() != null
						&& (lSentenzaMod.getCodTipoAutoritaEmittente().equals("CAPSM")
								|| lSentenzaMod.getCodTipoAutoritaEmittente().equals("DIBM")
								|| lSentenzaMod.getCodTipoAutoritaEmittente().equals("GIPM")))
				&& anni <= 18 && !anni_18_Maggiorenne) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#FF0040'>&nbsp;Minorenne (Anni "
					+ anni + ")&nbsp;</span>";
			oscuraEta = "NO";
		}
		// h) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo
		// la 'Età presunta' alla data di sistema ha più di 18 anni ma meno di 25 anni
		// ed avente campo VISIBILITA_EX_MINORENNE = 'N';
		// MERGE v10 COLLAUDO: sostituito 25 con 24
		else if (lFascicoloMod.getCodTipoUfficio() != null && !lFascicoloMod.getCodTipoUfficio().equals("")
				&& lFascicoloMod.getCodTipoUfficio().equals("PGCAP")
				&& (lSentenzaMod != null && lSentenzaMod.getCodTipoAutoritaEmittente() != null
						&& (lSentenzaMod.getCodTipoAutoritaEmittente().equals("CAPSM")
								|| lSentenzaMod.getCodTipoAutoritaEmittente().equals("DIBM")
								|| lSentenzaMod.getCodTipoAutoritaEmittente().equals("GIPM")))
				&& anni <= 24 && lFascicoloMod.getVisibilitaMinorenne() != null
				&& lFascicoloMod.getVisibilitaMinorenne().equals("N")) {

			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
			oscuraEta = "NO";
		}
		// i) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è 'MAGGIORENNE' se secondo la 'Data di nascita' oppure secondo la 'Età presunta'
		// alla data di sistema ha più di 18 anni ma meno di 26 anni ed avente
		// campo VISIBILITA_EX_MINORENNE = '';
		else if (lFascicoloMod.getCodTipoUfficio() != null && !lFascicoloMod.getCodTipoUfficio().equals("")
				&& lFascicoloMod.getCodTipoUfficio().equals("PGCAP")
				&& (lSentenzaMod != null && lSentenzaMod.getCodTipoAutoritaEmittente() != null
						&& (lSentenzaMod.getCodTipoAutoritaEmittente().equals("CAPSM")
								|| lSentenzaMod.getCodTipoAutoritaEmittente().equals("DIBM")
								|| lSentenzaMod.getCodTipoAutoritaEmittente().equals("GIPM")))
				&& anni <= 24 && (lFascicoloMod.getVisibilitaMinorenne() == null
						|| lFascicoloMod.getVisibilitaMinorenne().equals(""))) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
			if (lFascicoloMod.getChiaveUfficio().equals(codUfficioUtente)) {
				oscuraEta = "SI";
			} else {
				oscuraEta = "NO";
			}
		}
		// j) il soggetto iscritto dalla Procura Generale presso la Corte di Appello (PGCAP)
		// è da considerarsi come 'MAGGIORENNE' se secondo la 'Data di nascita' oppure
		// secondo la 'Età presunta' alla data di sistema ha più di 25 anni.
		// MERGE v10 COLLAUDO: sostituito 25 con 24
		else if (lFascicoloMod.getCodTipoUfficio() != null && !lFascicoloMod.getCodTipoUfficio().equals("")
				&& lFascicoloMod.getCodTipoUfficio().equals("PGCAP")
				&& (lSentenzaMod != null && lSentenzaMod.getCodTipoAutoritaEmittente() != null
						&& (lSentenzaMod.getCodTipoAutoritaEmittente().equals("CAPSM")
								|| lSentenzaMod.getCodTipoAutoritaEmittente().equals("DIBM")
								|| lSentenzaMod.getCodTipoAutoritaEmittente().equals("GIPM")))
				&& anni > 24) {
			etichettaEta = "<span style='color:#F2F2F2;background-color:#5882FA';>&nbsp;Maggiorenne (Anni "
					+ anni + ")&nbsp;</span>";
			oscuraEta = "NO";
		}

		setRequestAttribute("etichettaEta", etichettaEta);
		// setRequestAttribute("oscuraEta", (anniUltimoReato < 18 ? "NO" : "SI"));
		setRequestAttribute("oscuraEta", oscuraEta);

	}

	int deltaAnni(Date dataStart, Date dataEnd) {

		int anni = 0;
		if (dataStart != null && dataEnd != null) {
			long lStart = dataStart.getTime();
			long lEnd = dataEnd.getTime();
			long delta = lEnd - lStart;
			long days = Math.round((delta / (1000 * 60 * 60 * 24)));
			anni = Math.round(days / 365);
		}
		return anni;
	}

	Date elaboraDataReato(ReatoModel lReato) {

		Date ret = null;
		if (lReato.getDataInizio() != null) {
			ret = lReato.getDataInizio();
		} else if (lReato.getMeseInizio() != null && lReato.getAnnoInizio() != null) {
			ret = DateUtils.getDate(lReato.getAnnoInizio().intValue(), lReato.getMeseInizio().intValue(), 1);
		} else if (lReato.getAnnoInizio() != null) {
			ret = DateUtils.getDate(lReato.getAnnoInizio().intValue(), 1, 1);
		}
		return ret;
	}

	Date elaboraDataUltimoReato(Vector reatiVect) {

		// data del primo reato
		Date dataUltimoReato = null;

		Iterator itx = reatiVect.iterator();
		while (itx.hasNext()) {

			ReatoModel lReato = null;
			Object lObj = itx.next();
			if (lObj instanceof ReatoModel) {
				lReato = (ReatoModel) lObj;
			} else if (lObj instanceof ReatoCircostanzaModel) {
				ReatoCircostanzaModel lReatoCirc = (ReatoCircostanzaModel) lObj;
				lReato = lReatoCirc.getReato();
			}

			Date dataReato = elaboraDataReato(lReato);
			if (dataReato != null) {
				if (dataUltimoReato == null) {
					dataUltimoReato = dataReato;
				} else if (dataUltimoReato.compareTo(dataReato) < 0) {
					dataUltimoReato = dataReato;
				}
			}
		}

		return dataUltimoReato;
	}

	Date elaboraDataPrimoReato(Vector reatiVect) {

		// data del primo reato
		Date dataPrimoReato = null;

		Iterator itx = reatiVect.iterator();
		while (itx.hasNext()) {

			ReatoModel lReato = null;
			Object lObj = itx.next();
			if (lObj instanceof ReatoModel) {
				lReato = (ReatoModel) lObj;
			} else if (lObj instanceof ReatoCircostanzaModel) {
				ReatoCircostanzaModel lReatoCirc = (ReatoCircostanzaModel) lObj;
				lReato = lReatoCirc.getReato();
			}

			Date dataReato = elaboraDataReato(lReato);
			if (dataReato != null) {
				if (dataPrimoReato == null) {
					dataPrimoReato = dataReato;
				} else if (dataPrimoReato.compareTo(dataReato) > 0) {
					dataPrimoReato = dataReato;
				}
			}
		}

		return dataPrimoReato;
	}

	Date elaboraDataNascitaSoggetto(SoggettoModel soggMod) {

		Date ret = null;
		if (soggMod.getDataNascita() != null) {
			ret = soggMod.getDataNascita();
		} else if (soggMod.getMeseNascita() != null && soggMod.getAnnoNascita() != null) {
			ret = DateUtils.getDate(soggMod.getAnnoNascita().intValue(), soggMod.getMeseNascita().intValue(),
					1);
		} else if (soggMod.getAnnoNascita() != null) {
			ret = DateUtils.getDate(soggMod.getAnnoNascita().intValue(), 1, 1);
		}
		return ret;
	}

	/**
	 * Metodo di controllo per la minore età
	 *
	 * @param reatiVect
	 * @param soggMod
	 * @param lFascicoloMod
	 * @param lSentenzaMod
	 * @throws F3BException
	 */
	void checkMinorenne(Vector reatiVect, SoggettoModel soggMod, FascicoloSiepModel lFascicoloMod,
			SentenzaModel lSentenzaMod) throws F3BException {

		// int anni = 0;
		// String statoMinorMagg = "";

		// data di sistema
		Date dataSistema = DateUtils.getSysDate();

		// data di nascita
		Date dataNascitaSoggetto = elaboraDataNascitaSoggetto(soggMod);

		// data del primo reato e ultimo
		Date dataPrimoReato = elaboraDataPrimoReato(reatiVect);
		Date dataUltimoReato = elaboraDataUltimoReato(reatiVect);
		// uguale a false se il soggetto ha 18 anni ed è quindi minorenne
		// uguale a true se il soggetto ha 18 anni ed 1 giorno ed è quindi maggiorenne
		boolean anni_18_Maggiorenne = false;
		if (dataNascitaSoggetto != null) {

			// calcolo gli anni del soggetto
			int anniSoggetto = deltaAnni(dataNascitaSoggetto, dataSistema);
			int anniUltimoReato = deltaAnni(dataNascitaSoggetto, dataUltimoReato);
			impostaEtichetta(anniSoggetto, anniUltimoReato, lFascicoloMod, lSentenzaMod, anni_18_Maggiorenne);
			// anni = anniSoggetto;

		} else if (soggMod.getEtaPresuntaAnni() != null && dataPrimoReato != null) {

			// calcolo gli anni presunti del soggetto
			Date dataNascitaPresunta = DateUtils.moveDateTo(dataPrimoReato, Calendar.YEAR,
					-soggMod.getEtaPresuntaAnni().intValue());
			if (soggMod.getEtaPresuntaMesi() != null) {
				dataNascitaPresunta = DateUtils.moveDateTo(dataNascitaPresunta, Calendar.MONTH,
						-soggMod.getEtaPresuntaMesi().intValue());
			}
			int anniPresunti = deltaAnni(dataNascitaPresunta, dataSistema);
			int anniPresuntiUltimoReato = deltaAnni(dataNascitaPresunta, dataUltimoReato);

			// quando il calcolo degli anni presunti restituisce 18, bisogna
			// verificare se il giorno della data di sistema è maggiore
			// della data ultimo reato, in questo saco il soggetto è maggiorenne
			if (anniPresunti == 18) {
				int lGiornoDataSistema = Integer.parseInt(DateUtils.getDateToString(dataSistema, "dd"));
				int lGiornoDataUltimoReato = Integer
						.parseInt(DateUtils.getDateToString(dataUltimoReato, "dd"));
				if (lGiornoDataSistema > lGiornoDataUltimoReato) {
					anni_18_Maggiorenne = true;
				} else {
					anni_18_Maggiorenne = false;
				}
			}

			impostaEtichetta(anniPresunti, anniPresuntiUltimoReato, lFascicoloMod, lSentenzaMod,
					anni_18_Maggiorenne);
			// anni = anniPresunti;

		}

	}

	public String processRequest() throws Exception {
		setLinkRitorno();

		// paramentro passato solo nel caso di iscrizione guidata
		if (!isRequestAttributeNullObj("lTipoFunzione")) {
			setRequestAttribute("lTipoFunzione", getRequestAttribute("lTipoFunzione"));
		}

		// paramentro passato solo nel caso di iscrizione guidata E VENGO DA DETTAGLIO SOGGETTO
		if (!isRequestParameterNullObj("lTipoFunzione")) {
			setRequestAttribute("lTipoFunzione", getRequestStringParameter("lTipoFunzione"));
		}

		if (!isRequestParameterNullObj("NomeAzione")) {
			setRequestAttribute("NomeAzione", getRequestStringParameter("NomeAzione"));
		}

		// ==========================================================================
		// Verifico che il parametro CAMPO_ID_FASCICOLO_SIEP sia stato passato sulla
		// request.
		// Questo controllo è necessario (29/05/2006) in quanto questa funzione
		// può essere richiamata anche dal menù di scelta rapida
		// ==========================================================================

		BigDecimal aId = null;

		if (!isRequestParameterNullObj(CAMPO_ID_FASCICOLO_SIEP)) {
			aId = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIEP);
		} else if (!isSessionAttributeNullObj("fascicolo")) {
			// Se provengo del Menù Scelta Rapida devo recuparere i dati dalla sessione
			// Cerco in sessione il fascicolo per recuperare l'id
			aId = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
		} else {
			// Se non ho l'id fascicolo ne sulla request ne in sessione restituisco la
			// pagina di ricerca fascicolo
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// BigDecimal aId = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIEP);
		// String flagDettaglio=getRequestStringParameter("flagDettaglio");
		// FascicoloSiepController lCtrl = new FascicoloSiepController();
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

		// MERGE v10: modificata funzione di ricerca dettaglio
		DettaglioFascicoloModel lDettaglio = lCtrl.ExDettaglioFascicoloSiepNew(aId);

		// inizio gestione desc posizione giuridica
		// I) se la posizione giuridica !="07" ==> parto dalla tabella POSIZIONE_GIURIDICA tramite
		// CG_REF_CODES prendo la DescrPosizioneGiuridica
		// II) se la posizione giuridica ="07" && COD_MASCHERA="L" non è presente altra causa la
		// DescrPosizioneGiuridica="Libero"
		// else la posizione giuridica ="07" ==> è presente altra causa la DescrPosizioneGiuridica si
		// considera
		// la desc di CG_REF_CODES tramite la tabella ALTRA_CAUSA partendo dalla tabella
		// POSIZIONE_GIURIDICA.ALT_CAU_ID_ALTRA_CAUSA
		// II.1) per la vecchia gestione posizione giuridica ="07" &&
		// POSIZIONE_GIURIDICA.ALT_CAU_ID_ALTRA_CAUSA=null && COD_MASCHERA=NULL
		// trovo la descrizione sulla tabella ALTRA_CAUSA tramite FAS_ID_FASCICOLO_SIEP se trovo ALTRA_CAUSA
		// metto la descrizione
		// di altra causa altrimenti libero
		IAltraCausa lAcCtrl = SIEPLookupRemote.getAltraCausa();
		// if(lDettaglio.getPosizioneGiuridica()!=null &&
		// !lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica().equalsIgnoreCase("07")){
		// //è già presente la descrizione giusta
		// } else if(lDettaglio.getPosizioneGiuridica()!=null && lDettaglio.getAltraCausa()!=null &&
		// lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica().equalsIgnoreCase("07") &&
		// lDettaglio.getAltraCausa().getIdAltraCausa()==null){
		// AltraCausaModel altraCausa =
		// lAcCtrl.ExRicercaAltraCausaIstitutoByFascicolo(lDettaglio.getPosizioneGiuridica().getFasSieIdFascicoloSiep());
		// if(altraCausa!=null)
		// lDettaglio.getPosizioneGiuridica().setDescrPosizioneGiuridica(altraCausa.getDescrAutorita());
		// else
		// lDettaglio.getPosizioneGiuridica().setDescrPosizioneGiuridica("Libero");
		// } else if(lDettaglio.getPosizioneGiuridica()!=null && lDettaglio.getAltraCausa()!=null &&
		// lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica().equalsIgnoreCase("07") &&
		// lDettaglio.getAltraCausa().getIdAltraCausa()!=null){
		// // Ricerca Altra Causa
		// AltraCausaModel altraCausa =
		// lAcCtrl.ExRicercaAltraCausaIstitutoByKey(lDettaglio.getAltraCausa().getIdAltraCausa());
		// lDettaglio.getPosizioneGiuridica().setDescrPosizioneGiuridica(altraCausa.getDescrTipoPosGiuridica());
		// }

		if (lDettaglio.getPosizioneGiuridica() != null
				&& !lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica().equalsIgnoreCase("07")) {
			// è già presente la descrizione giusta
		} else if (lDettaglio.getPosizioneGiuridica() != null
				&& lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica().equalsIgnoreCase("07")
				&& lDettaglio.getPosizioneGiuridica().getAltCauIdAltraCausa() == null) {
			AltraCausaModel altraCausa = lAcCtrl.ExRicercaAltraCausaIstitutoByFascicolo(
					lDettaglio.getPosizioneGiuridica().getFasSieIdFascicoloSiep());
			if (altraCausa != null && lDettaglio.getPosizioneGiuridica().getCodMaschera() == null) {
				// vecchia gestione
				lDettaglio.getPosizioneGiuridica()
						.setDescrPosizioneGiuridica(altraCausa.getDescrTipoPosGiuridica());
			} else if (lDettaglio.getPosizioneGiuridica().getCodMaschera() == "L") {
				lDettaglio.getPosizioneGiuridica().setDescrPosizioneGiuridica("Libero");
			}
		} else if (lDettaglio.getPosizioneGiuridica() != null
				&& lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica().equalsIgnoreCase("07")
				&& lDettaglio.getPosizioneGiuridica().getAltCauIdAltraCausa() != null) {
			// Ricerca Altra Causa
			AltraCausaModel altraCausa = lAcCtrl.ExRicercaAltraCausaIstitutoByKey(
					lDettaglio.getPosizioneGiuridica().getAltCauIdAltraCausa());
			lDettaglio.getPosizioneGiuridica()
					.setDescrPosizioneGiuridica(altraCausa.getDescrTipoPosGiuridica());
		}

		FascicoloSiepModel lFasMod = lDettaglio.getFascicoloSiep();

		BigDecimal lengthCertPenale = lCtrl.ExGetLengthCertPenaleByIdFascicolo(lFasMod.getIdFascicoloSiep());

		int NumFasc = lFasMod.getChiaveProgr().intValue();

		// Dario - 10/08/2009 - se il fascicolo ha classe >= 90000
		// vuol dire che fà parte di registro istanze
		if (NumFasc > 90000 && NumFasc < 100000) {
			setRequestAttribute("lRegistroIstanze", "S");

			FascicoloSiepModel lFasModConv = new FascicoloSiepModel();

			lFasModConv = lCtrl.ExRicercaFascicoloByKeyNoError(lFasMod.getFasSieIdFascicoloSiep());
			if (lFasModConv != null)
				setRequestAttribute("fascicoloconversione", lFasModConv);

			// lFasModConv.setIdFascicoloSiep(aId);
			// Vector lFascConv = new Vector();
			//
			// try {
			// lFascConv = lCtrl.ExRicercaFascicoloSiep(lFasModConv);
			// if (lFascConv.size() == 1) {
			// lFasModConv = (FascicoloSiepModel) lFascConv.get(0);
			// setRequestAttribute("fascicoloconversione", lFasModConv);
			// }
			// } catch (F3BException e) {
			// if (e.getErrorCode() != F3BException.USER_MESSAGE) {
			// throw e;
			// }
			// }

		}

		// Ambros 25/03/2009
		// Se il proc è di classe VII , prendo Il FAS_SIE_ID_FASCICOLO_SIEP e
		// lo uso per cercare i fascicoli collegati

		// ANNA commento l'if per leggere comunque il FasSies nel caso ci sia
		// un Reg.Istanze collegato
		// if(NumFasc > 70000 && NumFasc < 80000)
		// {
		if (lDettaglio.getFascicoloSiep().getFasSieIdFascicoloSiep() != null) {
			BigDecimal aIdColl = lDettaglio.getFascicoloSiep().getFasSieIdFascicoloSiep();
			IFascicoloSiep lCtrlColl = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFasColl = null;
			try {
				lFasColl = lCtrlColl.ExRicercaFascicoloByKey(aIdColl);
				setRequestAttribute("fascicoloCollMod", lFasColl);
			} catch (F3BException e) {
				if (e.getErrorCode() != F3BException.USER_MESSAGE) {
					throw e;
				}
			}
		}

		// Il collegamento con i registri istanza (classe 90000) viene ora fatto con un vettore
		Vector lRegistroIstanza = new Vector();
		FascicoloSiepModel lFasModRicRegIst = new FascicoloSiepModel();
		lFasModRicRegIst.setFasSieIdFascicoloSiep(aId);
		try {
			lRegistroIstanza = lCtrl.ExRicercaFascicoloSiep(lFasModRicRegIst);
			if (lRegistroIstanza != null)
				setRequestAttribute("registroIstanzaCollMod", lRegistroIstanza);
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE) {
				throw e;
			}
		}

		if (NumFasc > 70000 && NumFasc < 80000) {
			IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();
			RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();

			// Paolo 26/04/2011
			// Vector lRichiestaConversioni = new Vector();
			// lRicMod.setFasSieIdFascicoloSiep(lFasMod.getIdFascicoloSiep());
			// lRichiestaConversioni = lCtrlRic.ExRicercaRichiestaConversione(lRicMod);
			// if (lRichiestaConversioni.size() > 0) {
			// lRicMod = (RichiestaConversioneModel) lRichiestaConversioni.get(0);
			// lRicMod = lCtrlRic.ExRicercaRichiestaConversioneById(lRicMod.getIdRichiestaConversione());
			// setRequestAttribute("richiestaconversione", lRicMod);
			// }

			lRicMod = lCtrlRic.ExRicercaRichiestaConversioneByIdFascicoloSiep(lFasMod.getIdFascicoloSiep());
			if (lRicMod != null && lRicMod.getIdRichiestaConversione() != null) {
				setRequestAttribute("richiestaconversione", lRicMod);
			} // fine paolo
		}

		// Se il proc è di classe I , Ricerco tutti i procedimenti che hanno l'Id di questo fascicolo
		// nel campo FAS_SIE_ID_FASCICOLO_SIEP ;
		// Se i procedimenti trovati risultano di Classe VII verranno visualizzati nel dettaglio

		if (NumFasc < 20000) {
			IFascicoloSiep lCtrlColl7 = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel lFasColl = new FascicoloSiepModel();
			Vector lRicFascVec = new Vector();
			lFasColl.setFasSieIdFascicoloSiep(lFasMod.getIdFascicoloSiep());
			try {
				lRicFascVec = lCtrlColl7.ExRicercaFascicoloSiep(lFasColl);
			} catch (F3BException e) {
				if (e.getErrorCode() != F3BException.USER_MESSAGE) {
					throw e;
				}
			}

			if (lRicFascVec.size() > 0) {
				setRequestAttribute("vectfasc", lRicFascVec);

				// 26/01/2015 controllo fascicoli collegati di classe VII
				// Se il fascicolo in dettaglio ha originato "un 70000", si carica anche il dettaglio della
				// Ric.Conv.P.P.
				for (int i = 0; i < lRicFascVec.size(); i++) {
					FascicoloSiepModel lFSApp = (FascicoloSiepModel) lRicFascVec.get(i);
					int Numero = lFSApp.getChiaveProgr().intValue();
					if (Numero > 70000 && Numero < 80000) {
						IRichiestaConversione lCtrlRC = SIEPLookupRemote.getRichiestaConversioneRemote();
						RichiestaConversioneModel lRCMod = new RichiestaConversioneModel();
						lRCMod = lCtrlRC
								.ExRicercaRichiestaConversioneByIdFascicoloSiep(lFSApp.getIdFascicoloSiep());
						if (lRCMod != null && lRCMod.getIdRichiestaConversione() != null) {
							setRequestAttribute("richiestaconversione", lRCMod);
						}
					}
				}
			}
		}

		// Fine Ambros

		// ANNA per Pene Sospese -

		// if (!(lDettaglio.getFascicoloSiep().getCodOperatoreInserimento().startsWith("res-")))
		// { //Paolo Cherubini 28-06-2011 aggiungo controllo per saltare questa sezione per i migrati

		// Paolo Cherubini 11-10-2011 modifico controllo altrimenti scarto tutti i migrati,
		// invece devo scartare solo quelli che hanno fas_sie_id_fascicolo_siep < 8 percè sicuramente non è
		// valido
		// essendo egli un vecchio numero res
		if ((lDettaglio.getFascicoloSiep().getCodOperatoreInserimento().startsWith("res-"))
				&& lDettaglio.getFascicoloSiep().getFasSieIdFascicoloSiep() != null
				&& lDettaglio.getFascicoloSiep().getFasSieIdFascicoloSiep().toString().length() < 9) {
		} else {
			if (NumFasc > 30000 && NumFasc < 40000) {
				if (lDettaglio.getFascicoloSiep().getCodStatoFascicolo().equals("01")) {

					java.util.List Eventi = lDettaglio.getEventi();

					// Paolo Cherubini per la seguente segnalazione
					// b2/rr/012 22-02-2012 SIEP - Pagina di errore durante la ricerca e il
					// procedimento per soggetto Pina Marchese
					// Se nel fascicolo siep di classe II dopo l'archiviazione della pena sospesa
					// viene reso non visibile dalla gestione stato esecuzione l'evento di annotazione
					// il dettaglio del fascicolo va in errore. aggiungo un controllo che se non trova eventi
					// va avanti lo stesso non si vede però il riferimento al fascicolo di classe I.
					if (Eventi != null && Eventi.size() > 0) { // fine modifica b2/rr/012
						EventoNotificaModel evemod = (EventoNotificaModel) Eventi.get(0);

						// 08/05/2019 MEV70 Per i fascicoli di classe III l'evento di riferimento ha
						// COD_TIPO_PROVVEDIMENTO='25', COD_MOTIVO='1100' e FLAG_DOCUMENTO_REGISTRATO = 'S'.
						for (int i = 0; i < Eventi.size(); i++) {
							EventoNotificaModel evemodcur = (EventoNotificaModel) Eventi.get(i);
							if ("25".equals(evemodcur.getEvento().getCodTipoProvvedimento())
									&& "1100".equals(evemodcur.getEvento().getCodMotivo())
									&& "S".equals(evemodcur.getEvento().getFlagDocumentoRegistrato())) {
								evemod = evemodcur;
								break;
							}
						}

						if (evemod.getEvento().getAnnIdAnnotazioneManuale() != null) {
							IAnnotazioneManuale lAnnCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
							Vector eve = lAnnCtrl
									.ExRicercaAnnotazioneManualeByIdEvento(evemod.getEvento().getIdEvento());
							AnnotazioneManualeModel lAnnManMod = (AnnotazioneManualeModel) eve.firstElement();
							setRequestAttribute("annotazioneMan", lAnnManMod);
						}
					}

					if (lDettaglio.getFascicoloSiep().getFasSieIdFascicoloSiep() != null) {
						IFascicoloSiep lCtrlNuofasc = SIEPLookupRemote.getFascicoloSiepRemote();
						FascicoloSiepModel nuofasc = lCtrlNuofasc.ExRicercaFascicoloByKey(
								lDettaglio.getFascicoloSiep().getFasSieIdFascicoloSiep());
						setRequestAttribute("NuoFascMod", nuofasc);
					}
				}
			} else {
				if (lDettaglio.getFascicoloSiep().getFasSieIdFascicoloSiep() != null) {
					IFascicoloSiep lCtrlfasIII = SIEPLookupRemote.getFascicoloSiepRemote();
					FascicoloSiepModel fascIII = null;
					// 18042018 [EC]. Modifica per correzione anomalia segnalata per problemi di
					// visualizzazione
					// di un titolo esecutivo impotato da altra DBI (VEDI email Alfieri/Maffucci del
					// 26/01/2018)
					try {
						fascIII = lCtrlfasIII.ExRicercaFascicoloByKey(
								lDettaglio.getFascicoloSiep().getFasSieIdFascicoloSiep());
					} catch (Exception e) {
						throw new SIEPException(F3BException.USER_MESSAGE,
								"Attenzione, pagina di dettaglio non visualizzabile! In caso di titolo esecutivo SIEP "
										+ "importato da altra DBI, per visualizzare il dettaglio occorre scaricare anche i collegati relativi al titolo di classe I!");
					}

					if (fascIII.getCodStatoFascicolo().equals("01")) {
						DettaglioFascicoloModel lDettaglio_fasIII = null;
						lDettaglio_fasIII = lCtrl.ExDettaglioFascicoloSiep(fascIII.getIdFascicoloSiep());

						java.util.List Eventi = lDettaglio_fasIII.getEventi();
						// Paolo Cherubini per la seguente segnalazione
						// b2/rr/012 22-02-2012 vedi sopra
						if (Eventi != null && Eventi.size() > 0) { // fine modifica b2/rr/012
							EventoNotificaModel evemod = (EventoNotificaModel) Eventi.get(0);

							// 08/05/2019 MEV70 Per i fascicoli generati da classe III l'evento di riferimento
							// ha COD_TIPO_PROVVEDIMENTO='25', COD_MOTIVO='1100' e FLAG_DOCUMENTO_REGISTRATO =
							// 'S'.
							for (int i = 0; i < Eventi.size(); i++) {
								EventoNotificaModel evemodcur = (EventoNotificaModel) Eventi.get(i);
								if ("25".equals(evemodcur.getEvento().getCodTipoProvvedimento())
										&& "1100".equals(evemodcur.getEvento().getCodMotivo())
										&& "S".equals(evemodcur.getEvento().getFlagDocumentoRegistrato())) {
									evemod = evemodcur;
									break;
								}
							}

							if (evemod.getEvento().getAnnIdAnnotazioneManuale() != null) {
								IAnnotazioneManuale lAnnCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
								Vector eve = lAnnCtrl.ExRicercaAnnotazioneManualeByIdEvento(
										evemod.getEvento().getIdEvento());
								AnnotazioneManualeModel lAnnManMod = (AnnotazioneManualeModel) eve
										.firstElement();
								setRequestAttribute("annotazioneMan", lAnnManMod);
								if (lAnnManMod != null)
									if (lAnnManMod.getCodTipoAnnotazione().equals("016"))
										setRequestAttribute("da_classe_III", "SI");
							}
						}
						setRequestAttribute("NuoFascMod", fascIII);
					}
				}
			}
		} // fine Paolo Cherubini 28-06-2011

		// Carico eventuali sanzioni sostitutive residue legate al ricalcolo
		ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getUltimaSSResidua(aId, "S");

		// Inserisco la SS residua nel model della PR (n.b. il model potrebbe non essere
		// presente se la pena non è validata
		if (lDettaglio.getPenaResidua() != null) {
			// Inserisco la SS residua nel model della PR
			if (lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null) {
				lSSResiduaModel = lSSCtrl.getUltimaSSResidua(aId, "N");
			}

			lDettaglio.getPenaResidua().setSanzSostResidua(lSSResiduaModel);
		}

		ReatoContinuazioneController lRCtrl = new ReatoContinuazioneController();
		Collection lReatiColl = lDettaglio.getReatiCircostanze();
		Vector lReatiVect = new Vector(lReatiColl);
		setRequestAttribute("continuazioni", lRCtrl.getTableContinuazioni(lReatiVect));

		setRequestAttribute("dettagliofascicolo", lDettaglio);
		// setRequestAttribute("flagDettaglio", flagDettaglio);

		// Inserisce nella session il fascicolo (contenente Soggetto e Sentenza)
		setSessionAttribute("fascicolo", lFasMod);
		// Gianluca 01/03 ....inserisco anche Soggetto e Sentenza del fascicolo in sessione
		setSessionAttribute("soggetto", lFasMod.getSoggetto());
		setSessionAttribute("sentenza", lFasMod.getSentenza());

		// Daniele 30/08/05 in sessione anche Pena Residua
		setSessionAttribute("penaresidua", lDettaglio.getPenaResidua());

		// ricerca del cumulo con flag validato a 'S'
		Vector lCumuli = new Vector();
		ICumulo lCtrlCum = SIEPLookupRemote.getCumuloRemote();
		lCumuli = lCtrlCum.ExRicercaFascicoliCumulobyIdFascicoloSiepValidatoDataCumuloNotNull(
				lFasMod.getIdFascicoloSiep());

		CumuloModel lCumMod = new CumuloModel();
		PenaCumuloModel lPenCumMod = new PenaCumuloModel();
		Vector lUltMod = new Vector();

		if (lCumuli.size() > 0) {
			lCumMod = ((CumuloModel) (lCumuli).get(0));

			if (lCumMod != null && lCumMod.getIdCumulo() != null) {
				IPenaCumulo lCtrlPen = SIEPLookupRemote.getPenaCumuloRemote();
				lPenCumMod = lCtrlPen.ExRicercaPenaCumuloByIdCumulo(lCumMod.getIdCumulo());

				IUlterioreSanzioneCumulo lUlt = SIEPLookupRemote.getUlterioreSanzioneCumuloRemote();
				lUltMod = lUlt.ExRicercaUlterioreSanzioneCumuloByFascicoloIDCumul0(
						lFasMod.getIdFascicoloSiep(), lCumMod.getIdCumulo());

			}
		}

		setRequestAttribute("penacumulo", lPenCumMod);
		setRequestAttribute("ulteriorisanzionicumulo", lUltMod);

		// ==========================================================================
		// Verifico se presente una istruttoria cumulo aperta
		// ==========================================================================
		IIstruttoriaCumulo lIstrCumCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		IstruttoriaCumuloModel lIstrCumMod = new IstruttoriaCumuloModel();
		lIstrCumMod.setFasSieIdFascicoloSiep(lFasMod.getIdFascicoloSiep());
		lIstrCumMod.setFlagStato(ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA);
		Vector lListaIstr = lIstrCumCtrl.ExRicercaIstruttoriaCumulo(lIstrCumMod);
		if (lListaIstr != null && lListaIstr.size() > 0) {
			setRequestAttribute("IstruttoriaCumuloAperta", lListaIstr.elementAt(0));
		}

		// ==========================================================================
		// MEV26 - Cumulo. Verifico se presenti eventi di trasmissione x competenza
		// e/o annotazioni esito per stabilire lo stato del
		// procedimento e visualizzarlo sul dettaglio
		// ==========================================================================
		EventoModel lEveModelRic = new EventoModel();

		lEveModelRic.setFasSieIdFascicoloSiep(lFasMod.getIdFascicoloSiep());
		lEveModelRic.setFlagDocumentoRegistrato("S");

		// 01-31-0340/5403 - Trasmissione per competenza
		// 01-25-5202 - Esito Trasmissione Atti per competenza (ex artt. 663 e 665 comma 4 c.p.p.)
		// Attenzione il codice 0340 è utilizzato anche per la richiesta (26) atti da Istruttoria cumulo
		// per cui la ricerca va fatta anche per tipo Provv 31 o 25
		String[] lCodTipoProvvedimento = { "31", "25" };
		String[] lCodMotivoProvvedimento = { "0340", "5403", "5202", "1040" };

		IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lLastEveTrasm = lEventoCtrl.ExRicercaEventoPerMotivoPerProvv(lCodMotivoProvvedimento,
				lCodTipoProvvedimento, lEveModelRic);

		if (lLastEveTrasm != null) {
			// ADD x MEV 42
			// CHG: Verifico comunque se presente una archiviazione SUCCESSIVA all'evento.
			// Se presente non passo nulla alla JSP nell'ipotesi che l'utente
			// abbia provveduto ad archiviare senza annotare prima l'esito.
			boolean isArchiviazioneSuccessiva = false;
			IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
			EventoModel lEventoRicerca = new EventoModel();
			lEventoRicerca.setCodTipoEvento("01");
			lEventoRicerca.setCodTipoProvvedimento("25"); // Annotazione
			lEventoRicerca.setFlagDocumentoRegistrato("S"); // Validata
			lEventoRicerca.setFasSieIdFascicoloSiep(lLastEveTrasm.getFasSieIdFascicoloSiep());

			// Archiviazioni per assorbimento in cumulo
			String[] lMotivo = new String[] { "0019", "0022", "0356", "0357" };
			EventoModel lUltimoEventoArcCumulo = lCtrlEvento.ExRicercaEventoPerMotivoOrderDesc(lMotivo,
					lEventoRicerca);

			if (lUltimoEventoArcCumulo != null && lUltimoEventoArcCumulo.getIdEvento() != null) {
				if (lLastEveTrasm.getIdEvento().compareTo(lUltimoEventoArcCumulo.getIdEvento()) < 0) {
					siesLogger.debug(
							"Annotazione assorbimento in cumulo inserita prima dell'archiviazione per cumulo. Non visualizzo i dati della trasmissione");
					isArchiviazioneSuccessiva = true;
				}
			}
			// FINE ADD
			// ================================================
			BigDecimal lChiaveAnnoCumulante = null;
			BigDecimal lChiavePogrCumulante = null;
			String lChiaveUfficioCumulante = null;

			if ("0340".equals(lLastEveTrasm.getCodMotivo()) || "5403".equals(lLastEveTrasm.getCodMotivo())) {
				// Trasmissione - Recupera il record COMPETENZA
				if (!isArchiviazioneSuccessiva) {
					ICompetenza lCtrlCompetenza = SIEPLookupRemote.getCompetenzaRemote();
					CompetenzaModel lCompModel = lCtrlCompetenza
							.ExRicercaCompetenzaByEveIdEvento(lLastEveTrasm.getIdEvento());
					setRequestAttribute("CompetenzaCumulo", lCompModel);
					// 20170914: [SG] aggiunto controllo di consistenza
					if (lCompModel != null) {
						lChiaveAnnoCumulante = lCompModel.getChiaveAnno();
						lChiavePogrCumulante = lCompModel.getChiaveProgr();
						lChiaveUfficioCumulante = lCompModel.getChiaveUfficio();
					}
				} else
					lLastEveTrasm = null;
			} else if ("5202".equals(lLastEveTrasm.getCodMotivo())
					|| "1040".equals(lLastEveTrasm.getCodMotivo())) {
				// Esito - Recupera il record ANNOTAZIONE_ESITO_TRASMISSIONE
				IAnnotazioneEsitoTrasmissione lCtrlAnnot = SIEPLookupRemote
						.getAnnotazioneEsitoTrasmissioneRemote();
				AnnotazioneEsitoTrasmissioneModel lAnnotaModel = lCtrlAnnot
						.ExRicercaAnnotazioneEsitoTrasmissioneByIdEvento(lLastEveTrasm.getIdEvento());

				// siesLogger.debug("lAnnotaModel = "+lAnnotaModel);
				if (lAnnotaModel != null) {
					setRequestAttribute("AnnotazioneEsitoCumulo", lAnnotaModel);

					lChiaveAnnoCumulante = lAnnotaModel.getChiaveAnno();
					lChiavePogrCumulante = lAnnotaModel.getChiaveProgr();
					lChiaveUfficioCumulante = lAnnotaModel.getChiaveUfficio();

					if (ICostantiJMS.RESTITUITO.equals(lAnnotaModel.getCodEsito())) {
						// Niente da visualizzare. La trasmissione è di fatto annullata
						lLastEveTrasm = null;
						// Ticket#202210130113 - Aggiunta gestione el codice di RIGETTO
					} else if (ICostantiJMS.RIGETTATO.equals(lAnnotaModel.getCodEsito())) {
						// Niente da visualizzare. La trasmissione è di fatto annullata
						lLastEveTrasm = null;
						// Ticket#202210130113 - FINE
					} else if ("1040".equals(lLastEveTrasm.getCodMotivo())
							&& ICostantiJMS.ASSORBITO_IN_CUMULO.equals(lAnnotaModel.getCodEsito())) {
						// Niente da visualizzare. Archiviazione automatica stesso ufficio
						lLastEveTrasm = null;
					} else if ("5202".equals(lLastEveTrasm.getCodMotivo())
							&& ICostantiJMS.ASSORBITO_IN_CUMULO.equals(lAnnotaModel.getCodEsito())) {
						// Visualizzo lo stato ASSORBITO solo se non è stato già archiviato
						if (isArchiviazioneSuccessiva)
							lLastEveTrasm = null;
					} else {
						setRequestAttribute("AnnotazioneEsitoCumulo", lAnnotaModel);

						lChiaveAnnoCumulante = lAnnotaModel.getChiaveAnno();
						lChiavePogrCumulante = lAnnotaModel.getChiaveProgr();
						lChiaveUfficioCumulante = lAnnotaModel.getChiaveUfficio();
					}
				}
			}

			if (lLastEveTrasm != null && lChiaveAnnoCumulante != null) {
				// Se devo visualizzare lo stato, recupero il ref la fascicolo competente
				// al cumulo, se possibile, per visualizzare il link sul dettaglio
				FascicoloSiepModel lFascRicerca = new FascicoloSiepModel();
				lFascRicerca.setChiaveAnno(lChiaveAnnoCumulante);
				lFascRicerca.setChiaveProgr(lChiavePogrCumulante);
				lFascRicerca.setChiaveUfficio(lChiaveUfficioCumulante);

				IFascicoloSiep lCtrlFascCum = SIEPLookupRemote.getFascicoloSiepRemote();
				FascicoloSiepModel lFascCompetenteCumulo = lCtrlFascCum
						.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFascRicerca);
				setRequestAttribute("FascCompetenteCumulo", lFascCompetenteCumulo);
			}

			setRequestAttribute("LastEveTrasm", lLastEveTrasm);
		}

		// =========================================================================
		// MEV 12 - Richiesta Certificato Penale
		// =========================================================================
		// se è presente il Certificato Casellario Giudiziale recupero la data di emissione dello stesso.
		if (lDettaglio.getFascicoloSiep() != null && lengthCertPenale.intValue() > 0) {
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			String lTipoEvento = "05";
			String lCodMotivo = "0050";

			Date dataEmissioneCertificato = lCtrlEve.ExRicercaDataEmissioneCertCasellario(
					lDettaglio.getFascicoloSiep().getIdFascicoloSiep(),
					lDettaglio.getFascicoloSiep().getChiaveUfficio(), lTipoEvento, lCodMotivo);
			setRequestAttribute("dataEmissioneCertificato", dataEmissioneCertificato);
			setRequestAttribute("certificatoPenale", "SI");
		} else {
			setRequestAttribute("certificatoPenale", "NO");
		}

		if (!"N".equals(lFasMod.getVisibilitaMinorenne())) {
			// checkMinorenne(lReatiVect, lFasMod.getSoggetto());
			checkMinorenne(lReatiVect, lFasMod.getSoggetto(), lFasMod, lFasMod.getSentenza());
		}

		// Misure Sicurezza SIEP - 02/2014
		// -----> controllo se fascicolo di classe I è legato a fascicolo di classe IV

		IMisuraSicurezza lCtrMis = SIEPLookupRemote.getMisuraSicurezzaRemote();
		// FascMsToFascSiepModel FasMSMod = new FascMsToFascSiepModel();
		Vector VectFasIV = new Vector();

		/*
		 * if(NumFasc > 0 && NumFasc < 19999) {
		 * FasMSMod.setChiaveAnnoSiep(lDettaglio.getFascicoloSiep().getChiaveAnno());
		 * FasMSMod.setChiaveProgrSiep(lDettaglio.getFascicoloSiep().getChiaveProgr());
		 * FasMSMod.setChiaveUfficioSiep(lDettaglio.getFascicoloSiep().getChiaveUfficio()); }
		 *
		 * if(NumFasc > 39999 && NumFasc < 49999) {
		 * FasMSMod.setChiaveAnnoSiep(lDettaglio.getFascicoloSiep().getChiaveAnno());
		 * FasMSMod.setChiaveProgrSiep(lDettaglio.getFascicoloSiep().getChiaveProgr());
		 * FasMSMod.setChiaveUfficioSiep(lDettaglio.getFascicoloSiep().getChiaveUfficio()); }
		 */

		VectFasIV = lCtrMis.ExRicercaFascicoliCollegati(aId);

		// FasMSMod.setFasSieIdFascicoloSiep(aId);
		// VectFasIV = lCtrMis.ExRicercaFascMsToFascSiepByFascSiep(FasMSMod);
		// if(VectFasIV.size() == 0)
		// {
		// FasMSMod = new FascMsToFascSiepModel();
		// FasMSMod.setFasSieIdFascicoloClasseIV(aId);
		// VectFasIV = lCtrMis.ExRicercaFascMsToFascSiepByFascSiep(FasMSMod);
		// }

		setRequestAttribute("vecFascicoloIV", VectFasIV);

		// Passa la action di destinazione : sostituita da gestioneRitorno()
		// if (!isRequestParameterNullObj("TornaQui")) {
		// setRequestAttribute("TornaQui", getRequestStringParameter("TornaQui"));
		// }
		// 30/07/2015 Occorre il dato "tipo ufficio che ha inserito lo SCAMBIO_SANZIONE".
		// Se è diverso da UDS TDS UDSM TDSM non deve essere cliccabile il FASCICOLO_SIUS collegato.
		String vediLinkFasSorv = "N";
		if (lDettaglio.getScambioSanzione() != null
				&& lDettaglio.getScambioSanzione().getCodUfficioInserimento() != null) {
			UfficioModel lUffScambioSanzione = getUfficioByCodUfficio(
					lDettaglio.getScambioSanzione().getCodUfficioInserimento());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("<<<<<<<<<<<<<<<<<<<  codTipoUfficio = "
					+ lUffScambioSanzione.getCodTipoUfficio().toString() + "  >>>>>>>>>>>>>>>>>>");
			if (lUffScambioSanzione.getCodTipoUfficio().startsWith("UDS")
					|| lUffScambioSanzione.getCodTipoUfficio().startsWith("TDS"))
				vediLinkFasSorv = "S";
		}
		setRequestAttribute("vediLinkSorv", vediLinkFasSorv);

		/*
		 * ISSUE MEV : inserimento data comunicazione scadenza per provv. classe IV 
		 * Numero MEV : 39 
		 * Autore : Gioggi 
		 * Data : 12/mag/2017 
		 * Branch : MEV_39
		 */
		if (NumFasc >= 40000 && NumFasc < 50000) {
			IScadenzario is = SIEPLookupRemote.getScadenzarioRemote();
			ScadenzarioModel sm = new ScadenzarioModel();
			// Imposto fisso 20=comunicazione scadenza MS
			sm.setCodTipoScadenzario("20");
			sm.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			sm.setFasSieIdFascicoloSiep(aId);
			Vector scadenzari = null;
			try {
				scadenzari = is.ExRicercaScadenzario(sm, "CSMS");
			} catch (Exception e) {
				siesLogger.info("Non esistono scadenzari per il fascicolo in questione: " + aId.toString());
			}
			if (scadenzari != null && scadenzari.size() != 0)
				sm = (ScadenzarioModel) scadenzari.firstElement();
			// ricerca del parametro
			ParametroModel pm = new ParametroModel();
			// Ricerca se esiste Periodo per quell'Ufficio
			pm.setCodUfficioValidita(getCodUfficioUtenteConnesso());
			pm.setNomeParametro("INIZIO MISURA");
			IParametro ip = SIEPLookupRemote.getParametroRemote();
			Vector parametri = ip.ExRicercaParametroUfficioConnesso(pm);
			BigDecimal anni = null, mesi = null, giorni = null;
			if (parametri != null && parametri.size() != 0) {
				ParametroModel pam = (ParametroModel) parametri.firstElement();
				anni = (pam.getAnni() != null) ? pam.getAnni() : new BigDecimal(0);
				mesi = (pam.getMesi() != null) ? pam.getMesi() : new BigDecimal(0);
				giorni = (pam.getGiorni() != null) ? pam.getGiorni() : new BigDecimal(0);
			} else {
				// default
				anni = new BigDecimal(0);
				mesi = new BigDecimal(6);
				giorni = new BigDecimal(0);
			}
			String dsc = "", gr = "";
			if (sm != null && sm.getDataFineScadenza() != null) {
				// ricalcolo la data scadenza comunicazione
				Calendar dataScadenzaComunicazione = Calendar.getInstance();
				dataScadenzaComunicazione.setTime(sm.getDataFineScadenza());
				dataScadenzaComunicazione.add(Calendar.YEAR, -anni.intValue());
				dataScadenzaComunicazione.add(Calendar.MONTH, -mesi.intValue());
				dataScadenzaComunicazione.add(Calendar.DATE, -giorni.intValue());
				dsc += DateUtils.getDateToString(dataScadenzaComunicazione.getTime(), "dd/MM/yyyy");
				// 20200125 [SG]: aggiunte 2 variabili data
				Date dataMaggiore, dataMinore;
				if (sm.getDataFineScadenza().compareTo(DateUtils.getSysDate()) >= 0) {
					gr = ScadenzarioUtils.getDifferenza(sm.getDataFineScadenza(), DateUtils.getSysDate());
					dataMaggiore = sm.getDataFineScadenza();
					dataMinore = DateUtils.getSysDate();
				} else {
					gr = ScadenzarioUtils.getDifferenza(DateUtils.getSysDate(), sm.getDataFineScadenza());
					dataMinore = sm.getDataFineScadenza();
					dataMaggiore = DateUtils.getSysDate();
				}
				// 20200125 [SG]: 7. comunicazione pag. 47 (quantum), capitolo 5.3.7, frase da visualizzare
				// solo quando i giorni residui sono al di sotto del quantum;
				int agr = 0, mgr = 0, ggr = 0;
				int ggMaggiore = Integer.parseInt(DateUtils.getDateToString(dataMaggiore, "dd"));
				int mmMaggiore = Integer.parseInt(DateUtils.getDateToString(dataMaggiore, "MM"));
				int aaMaggiore = Integer.parseInt(DateUtils.getDateToString(dataMaggiore, "yyyy"));
				int ggMinore = Integer.parseInt(DateUtils.getDateToString(dataMinore, "dd"));
				int mmMinore = Integer.parseInt(DateUtils.getDateToString(dataMinore, "MM"));
				int aaMinore = Integer.parseInt(DateUtils.getDateToString(dataMinore, "yyyy"));
				int ultimoGiorno = Integer.parseInt(
						DateUtils.getDateToString(DateUtils.getEndOfMonth(aaMaggiore, mmMaggiore), "dd"));
				if (ggMaggiore >= ggMinore) {
					ggr = ggMaggiore - ggMinore;
				} else {
					ggr = (ggMaggiore + ultimoGiorno) - ggMinore;
					mmMaggiore--;
				}
				if (mmMaggiore >= mmMinore) {
					mgr = mmMaggiore - mmMinore;
				} else {
					mgr = mmMaggiore - mmMinore + 12;
					aaMaggiore--;
				}
				agr = aaMaggiore - aaMinore;
				boolean visualizzaComunicazione = false;
				if (anni.intValue() > agr)
					visualizzaComunicazione = true;
				else if (mesi.intValue() > mgr)
					visualizzaComunicazione = true;
				else if (giorni.intValue() > ggr)
					visualizzaComunicazione = true;
				if (!visualizzaComunicazione)
					gr = "";
				// 20200125 [SG]: FINE
			}
			setRequestAttribute("dsc", dsc);
			setRequestAttribute("gr", gr);

			// ricerco le MS
			IPeriodoAltraMisura ipam = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
			List lListMis = ipam.ExRicercaMisuraSicurezzaByIdSiep(aId);
			setRequestAttribute("misuresicurezza", lListMis);
		}
		// ***** FINE INTERVENTO MEV_39 *****//

		/*
		 * ISSUE MEV : aggiunta ricerca del Civilmente Obbligato ed elenco stato pagamenti 
		 * Numero MEV : 2023-33 
		 * Autore : sgioggi 
		 * Data : 24 ago 2023 
		 * Branch : MEV_2023-33
		 */
		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		Vector<CivilmenteObbligatoModel> coms = ico.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(aId);
		setRequestAttribute("existCivilmenteObbligato", !coms.isEmpty());

		BigDecimal idEventoStatoPagamenti = new BigDecimal(0);
		EventoModel emRic = new EventoModel();
		emRic.setCodTipoEvento("01");
		emRic.setCodTipoProvvedimento("06");
		emRic.setCodMotivo("0622");
		emRic.setFasSieIdFascicoloSiep(aId);
		emRic.setFlagDocumentoRegistrato("S");
		EventoModel em = lEventoCtrl.ExRicercaUltimoTipoEventoByIdFascicolo(emRic);
		if (!Utils.isNullObj(em))
			idEventoStatoPagamenti = em.getIdEvento();

		setRequestAttribute("idEventoStatoPagamenti", idEventoStatoPagamenti);
		// ***** FINE INTERVENTO MEV_2023-33 *****//

		return PG_DETTAGLIO_FASCICOLO_SIEP;
	}

}