package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;

/**
* <p>Title: ActRicercaIstrCumuloEstesa</p>
* <p>Description: Classe Action per la ricerca Estesa di IstruttoriaCumulo </p>
* @version 1.0
*/

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.jms.action.ICostantiSiepJMS;
import siap.siep.modulocumulo.action.ICostantiTitoloCumulato;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaIstrCumuloEstesa extends ActionSiap implements ICostantiIstruttoriaCumulo {

	/***********************************************************************************
	 * Azione che ricerca l'elenco delle istruttorie in funzione dei criteri selezionati
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 ***********************************************************************************/
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// prende dalla sessione il codice dell'ufficio dell'utente connesso.
		String ufficio = this.getCodUfficioUtenteConnesso();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// =========================================================================================
		// Istanzio il Model e imposto le Condizioni per il Tipo di ricerca e i filtri selezionati.
		// =========================================================================================

		IstruttoriaCumuloModel lIstCumMod = new IstruttoriaCumuloModel();
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		lIstCumMod.setChiaveUfficio(ufficio);

		String lvaloreRadio = "";
		if (!isRequestParameterNullObj("valoreRadio"))
			lvaloreRadio = this.getRequestStringParameter("valoreRadio").trim();

		int lRadio = Integer.parseInt(lvaloreRadio);
		switch (lRadio) {
		case 0: {
			// Condizione per intervallo Anno/Numero Istruttoria.
			if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE))
				lIstCumMod.setAnnoProtocolloIniziale(getRequestBigDecimalParameter(
						ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE));
			if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_INIZIALE))
				lIstCumMod.setNumProtocolloIniziale(getRequestBigDecimalParameter(
						ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_INIZIALE));

			if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE))
				lIstCumMod.setAnnoProtocolloFinale(getRequestBigDecimalParameter(
						ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE));
			if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_FINALE))
				lIstCumMod.setNumProtocolloFinale(getRequestBigDecimalParameter(
						ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_FINALE));

			if (!isRequestParameterNullObj("statoI") && !getRequestStringParameter("statoI").equals("-"))
				lIstCumMod.setFlagStato(getRequestStringParameter("statoI"));

			// Recupero parametri di ricerca per Intervallo Istruttoria.
			String AnnoIniIst = "";
			String NumeroIniIst = "";
			String AnnoFineIst = "";
			String NumeroFineIst = "";
			String IntestaIstruttoria = "";
			if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE))
				AnnoIniIst = getRequestStringParameter(
						ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE);
			if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_INIZIALE))
				NumeroIniIst = getRequestStringParameter(
						ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_INIZIALE);
			if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE))
				AnnoFineIst = getRequestStringParameter(
						ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE);
			if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_FINALE))
				NumeroFineIst = getRequestStringParameter(
						ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_FINALE);
			if (!AnnoIniIst.equals(""))
				IntestaIstruttoria += "Istruttorie dal " + AnnoIniIst + "/" + NumeroIniIst + " ";

			if (!AnnoFineIst.equals(""))
				IntestaIstruttoria += " al " + AnnoFineIst + "/" + NumeroFineIst + " ";

			if (!isRequestParameterNullObj("statoI") && !getRequestStringParameter("statoI").equals("-"))
				IntestaIstruttoria += "       Stato : " + DecodificheUtils.getDescbyCode(
						DecodificheManager.getInstance().getStatoIstruttoriaCumulo(),
						getRequestStringParameter("statoI"));
			else
				IntestaIstruttoria += "       Stato : Tutte ";

			if (!IntestaIstruttoria.equals("")) {
				setRequestAttribute("CriteriRicerca", IntestaIstruttoria);
			}

			break;
		}
		case 1: {
			// Condizione per Intervallo data iscrizione.
			if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE)
					&& !isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE)
					&& !isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE)) {
				lIstCumMod.setDataIscrizioneIniziale(
						getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE,
								ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE,
								ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE));
			}
			if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE)
					&& !isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE)
					&& !isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE)) {
				lIstCumMod.setDataIscrizioneFinale(
						getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE,
								ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE,
								ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE));
			}
			if (!isRequestParameterNullObj("statoD") && !getRequestStringParameter("statoD").equals("-"))
				lIstCumMod.setFlagStato(getRequestStringParameter("statoD"));

			// Recupero parametri di ricerca per Intervallo data iscrizione.
			String DataIniIsc = "";
			String DataFineIsc = "";
			String IntestaDate = "";

			if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE)
					&& getRequestStringParameter(
							ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE) != null
					&& !getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE)
							.equals("")) {
				String gg = getRequestStringParameter(
						ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE);
				String mm = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE);
				String aaaa = getRequestStringParameter(
						ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE);
				DataIniIsc = gg + "/" + mm + "/" + aaaa;
			}
			if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE)
					&& !getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE)
							.equals("")
					&& getRequestStringParameter(
							ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE) != null) {
				String ggF = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE);
				String mmF = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE);
				String aaaaF = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE);
				DataFineIsc = ggF + "/" + mmF + "/" + aaaaF;
			}

			if (!DataIniIsc.equals(""))
				IntestaDate += "Istruttorie iscritte dal " + DataIniIsc + " ";

			if (!DataFineIsc.equals(""))
				IntestaDate += " al " + DataFineIsc + "    ";

			if (!isRequestParameterNullObj("statoD") && !getRequestStringParameter("statoD").equals("-"))
				IntestaDate += "       Stato : " + DecodificheUtils.getDescbyCode(
						DecodificheManager.getInstance().getStatoIstruttoriaCumulo(),
						getRequestStringParameter("statoD"));
			else
				IntestaDate += "       Stato : Tutte ";

			if (!IntestaDate.equals("")) {
				setRequestAttribute("CriteriRicerca", IntestaDate);
			}

			break;
		}
		case 2: {
			// Titolo Coinvolto in Cumulo
			EventoModel lProvvCumMod = new EventoModel();

			if (!isRequestParameterNullObj(ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO)
					&& !isRequestParameterNullObj(ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO)
					&& !isRequestParameterNullObj(ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO)) {
				lProvvCumMod.setDataEmissione(
						getRequestDateParameter(ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO,
								ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO,
								ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO));
			}

			if (!isRequestParameterNullObj(ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO)
					&& !getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO)
							.equals("-"))
				lProvvCumMod.setCodTipoProvvedimento(
						getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO));

			if (!isRequestParameterNullObj(ICostantiTitoloCumulato.CAMPO_ANNO_PROVV_RIF))
				lProvvCumMod.setAnnoProtocollo(
						getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ANNO_PROVV_RIF));
			if (!isRequestParameterNullObj(ICostantiTitoloCumulato.CAMPO_NUMERO_PROVV_RIF))
				lProvvCumMod.setProgrProtocollo(
						getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_NUMERO_PROVV_RIF));

			// Si carica l'ufficio emittente
			if (!isRequestParameterNullObj("CodTipoAutoritaEmittente")
					&& !getRequestStringParameter("CodTipoAutoritaEmittente").equals("-")
					&& !isRequestParameterNullObj("CodLuogoEmittente")
					&& !getRequestStringParameter("CodLuogoEmittente").equals("")) {
				lProvvCumMod
						.setCodTipoUfficioEmittente(getRequestStringParameter("CodTipoAutoritaEmittente"));
				lProvvCumMod.setCodLuogoEmittente(
						getCodComuneByDescr(getRequestStringParameter("CodLuogoEmittente")).getCodComune());
			}

			lIstCumMod.setProvvedimentoCumulo(lProvvCumMod);

			// Recupero parametri di ricerca per Titolo Coinvolto in Cumulo
			String DataEmi = "";
			String AnnoTitolo = "";
			String NumeroTitolo = "";
			String IntestazionePerTitolo = "";

			if (!isRequestParameterNullObj(ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO)
					&& !getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO)
							.equals("-"))
				IntestazionePerTitolo += DecodificheUtils.getDescbyCode(
						DecodificheManager.getInstance().getTipoProvvedimenti(),
						getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO))
						+ " ";
			else
				IntestazionePerTitolo += "Titoli ";

			if (!isRequestParameterNullObj(ICostantiTitoloCumulato.CAMPO_ANNO_PROVV_RIF))
				AnnoTitolo = getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_ANNO_PROVV_RIF);
			if (!isRequestParameterNullObj(ICostantiTitoloCumulato.CAMPO_NUMERO_PROVV_RIF))
				NumeroTitolo = getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_NUMERO_PROVV_RIF);
			if (!AnnoTitolo.equals(""))
				IntestazionePerTitolo += " N. " + AnnoTitolo + "/" + NumeroTitolo;

			if (!isRequestParameterNullObj(ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO)
					&& getRequestStringParameter(
							ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO) != null
					&& !getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO)
							.equals("")) {
				String gg = getRequestStringParameter(
						ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO);
				String mm = getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO);
				String aaaa = getRequestStringParameter(
						ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO);
				DataEmi = gg + "/" + mm + "/" + aaaa;
				IntestazionePerTitolo += " con data emissione : " + DataEmi;
			}

			if (!isRequestParameterNullObj("CodTipoAutoritaEmittente")
					&& !getRequestStringParameter("CodTipoAutoritaEmittente").equals("-")
					&& !isRequestParameterNullObj("CodLuogoEmittente")
					&& !getRequestStringParameter("CodLuogoEmittente").equals(""))
				IntestazionePerTitolo += " da "
						+ DecodificheUtils.getDescbyCode(
								DecodificheManager.getInstance().getTipoAutoritaEmittente(),
								getRequestStringParameter("CodTipoAutoritaEmittente"))
						+ " di " + (getRequestStringParameter("CodLuogoEmittente")).toUpperCase();

			if (!IntestazionePerTitolo.equals("")) {
				IntestazionePerTitolo = "Istruttorie riferite a : " + IntestazionePerTitolo;
				setRequestAttribute("CriteriRicerca", IntestazionePerTitolo);
			}

			break;
		}
		case 3: {
			// Fascicolo SIEP
			lFasMod = new FascicoloSiepModel();

			if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO))
				lFasMod.setChiaveAnno(
						getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO));

			if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR))
				lFasMod.setChiaveProgr(
						getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR));

			IUfficio lCtrlUfficio = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUfficio = null;
			if (!this.isRequestParameterNullObj(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO)) {
				lUfficio = lCtrlUfficio.getUfficioByCodTipoUffDescrComune(
						this.getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO),
						this.getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO).toUpperCase());
				lFasMod.setChiaveUfficio(lUfficio.getCodUfficio());
				siesLogger.debug("ufficio----->" + lUfficio.getCodUfficio());
			}
			if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO)) {
				// Sono nella form Avanzata - Intervallo Procedimenti
				String ufficioAccorpato = getRequestStringParameter(
						ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO);
				String[] parts = ufficioAccorpato.split("-");
				if (parts.length > 1 && parts[1] != null && !parts[1].equals("")) {
					lFasMod.setCodUfficioInserimento(parts[1]);
				} else {
					lFasMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				}
			}
			if (!isRequestParameterNullObj("tipoClasse")) {
				String[] lClassiFascicolo = this.getRequestStringParameters("tipoClasse");
				lFasMod.setClassiFascicolo(lClassiFascicolo);
			}

			// Recupero parametri di ricerca per Fascicolo SIEP.
			String IntestazionePerFasSiep = "";
			if ((!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO))
					&& (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR)))
				IntestazionePerFasSiep = "Istruttorie riferite al Fascicolo "
						+ getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO).toString()
						+ "/"
						+ getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR).toString();

			if (!this.isRequestParameterNullObj(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO))
				IntestazionePerFasSiep += " della " + lUfficio.getDescrTipoUfficio() + " di "
						+ lUfficio.getDescrComune();

			setRequestAttribute("CriteriRicerca", IntestazionePerFasSiep);

			break;
		}
		}

		// ==========================================================================
		// Istanzio il controller ed effettuo la ricerca paginata.
		// ==========================================================================
		IIstruttoriaCumulo lCtrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		Vector<IstruttoriaCumuloModel> lVect = new Vector<IstruttoriaCumuloModel>();

		BigDecimal lCountRisultati = null;
		if (isRequestParameterNullObj("CountRisultati")) {
			if (lRadio == 3)
				lCountRisultati = lCtrl.ExCountIstruttoriaPerFasSIEP(lFasMod, ufficio);
			else if (lRadio == 2)
				lCountRisultati = lCtrl.ExCountIstruttoriaPerTitoloCumulato(lIstCumMod);
			else
				lCountRisultati = lCtrl.ExCountIstruttoriaCumuloPaged(lIstCumMod);
		} else
			lCountRisultati = getRequestBigDecimalParameter("CountRisultati");

		if (lRadio == 3) {
			lVect = lCtrl.ExRicercaIstruttoriaPerFasSIEPpaged(lFasMod, ufficio, Integer.parseInt(lPagina));
		} else if (lRadio == 2) {
			lVect = lCtrl.ExRicercaIstruttoriaPerTitoloCumulatoPaged(lIstCumMod, Integer.parseInt(lPagina));
		} else {
			lVect = lCtrl.ExRicercaIstruttoriaCumuloPaged(lIstCumMod, Integer.parseInt(lPagina));
		}

		if (lVect.size() == 0) {
			this.setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun dato presente");
			siesLogger.debug("Nessun dato presente");
			return IWebConstants.PG_MESSAGE;
		}

		siesLogger.debug("Size del vettore di Istruttorie : " + lVect.size());

		// ==========================================
		// Recupero del vettore dei Titoli Esecutivi
		// ==========================================
		IFascicoloSiep lFasSIEPCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		Vector<FascicoloSiepModel> lVectFasSIEP = new Vector<FascicoloSiepModel>();
		for (int i = 0; i < lVect.size(); i++) {
			IstruttoriaCumuloModel lIstruttoria = lVect.elementAt(i);

			FascicoloSiepModel lFasSIEP = lFasSIEPCtrl
					.ExRicercaFascicoloByKey(lIstruttoria.getFasSieIdFascicoloSiep());
			lVectFasSIEP.add(lFasSIEP);
		}

		setRequestAttribute("ListaIstruttorieCumulo", lVect);
		setRequestAttribute("ListaFascicoliSIEP", lVectFasSIEP);

		setRequestAttribute("CountRisultati", lCountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		return PG_ELENCO_ESTESO_ISTRUTTORIE_CUMULO;

	}

}