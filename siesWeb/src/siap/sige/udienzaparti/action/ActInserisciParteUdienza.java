package siap.sige.udienzaparti.action;

import java.math.BigDecimal;
import java.util.List;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.udienzaparti.model.UdienzaPartiModel;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * ActInserisciParteUdienza - Classe Action per l'inserimento della Parte (Offesa/Civile) di una Udienza
 *
 * @version 1.0
 */
public class ActInserisciParteUdienza extends ActionSige implements ICostantiPartiUdienza {

	protected FascicoloSigeEstesoModel mFascicoloEsteso = null;

	/**
	 * Effettua Inserimento della Parte (Offesa/Civile) di una Udienza.
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione.
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		// Fascicolo Sige Esteso in sessione.
		mFascicoloEsteso = getFascicoloSigeEstesoInSessione();

		BigDecimal lIdChiaveAnno = mFascicoloEsteso.getFascicoloSige().getChiaveAnno();
		BigDecimal lIdChiaveProgr = mFascicoloEsteso.getFascicoloSige().getChiaveProgr();

		AnagraficaPartiUdienzaModel lAnagraficaParteMod = letturaAnagraficaParte();

		UdienzaPartiModel udienzaParteMod = letturaUdienzaParte();

		String idEventoUdienza = getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA);
		String idUdienzaSige = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);

		IPartiUdienza lCtrl = SIGELookupRemote.getPartiUdienzaRemote();

		// Chiama il controller.
		AnagraficaPartiUdienzaModel lAnagraficaParteModRet = lCtrl
				.ExInserisciParteUdienza(lAnagraficaParteMod, udienzaParteMod);

		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);
		lRedir.setAction("siap.sige.udienzaparti.action.ActLoadModificaDifensore");
		lRedir.setParameter(CAMPO_COD_TIPO_PART, lAnagraficaParteMod.getCodTipoPart());
		lRedir.setParameter(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE,
				udienzaParteMod.getIdUdienzaProcedimentoSige().toString());
		lRedir.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE, idUdienzaSige);
		lRedir.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, idEventoUdienza);
		lRedir.setParameter(CAMPO_ID_SOGGETTO, lAnagraficaParteModRet.getIdSoggetto().toString());
		lRedir.setParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO, lIdChiaveAnno.toString());
		lRedir.setParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR, lIdChiaveProgr.toString());

		// Torna alla pagina di visualizzazione parti senza aggiungerla allo stack
		lRedir.setParameter(IWebConstants.LINK_RITORNO, "10");
		// lRedir.setParameter(IWebConstants.FLAG_RITORNO, "1");

		String lPage = lRedir.toString();

		return lPage;
	}

	@SuppressWarnings("rawtypes")
	protected AnagraficaPartiUdienzaModel letturaAnagraficaParte() throws Exception {

		AnagraficaPartiUdienzaModel lAnagraficaParteModel = new AnagraficaPartiUdienzaModel();
		ResidenzaModel residenzaMod = new ResidenzaModel();

		// Codice del tipo parte da inserire (O=Offesa, C=Civile)
		String codTipoParte = getRequestStringParameter(CAMPO_COD_TIPO_PART);
		lAnagraficaParteModel.setCodTipoPart(codTipoParte);

		// Parte interessata all'udienza (F=Fisica, G=Giuridica)
		String codParte = getRequestStringParameter(RADIO_COD_PARTE);
		lAnagraficaParteModel.setCodParte(codParte);

		// Persona Giuridica
		if (codParte != null && codParte.equals("G")) {

			// Società
			lAnagraficaParteModel
					.setDenominazione(getRequestStringParameter(CAMPO_DENOMINAZIONE).toUpperCase().trim());
			// Ragione Sociale
			if (!isRequestParameterNullObj(CAMPO_RAG_SOCIALE)) {
				lAnagraficaParteModel.setRagSociale(getRequestStringParameter(CAMPO_RAG_SOCIALE));
			}
			// Provincia
			if (!isRequestParameterNullObj(CAMPO_COD_PROVINCIA)) {
				lAnagraficaParteModel.setCodProvincia(getRequestStringParameter(CAMPO_COD_PROVINCIA));
			}
			// Partita IVA/Codice Fiscale
			if (!isRequestParameterNullObj(CAMPO_COD_FISCALE_RAP)) {
				lAnagraficaParteModel.setCodFiscaleRap(getRequestStringParameter(CAMPO_COD_FISCALE_RAP));
			}
			// Sede Legale
			if (!isRequestParameterNullObj(CAMPO_IND_SEDE_LEGALE)) {
				lAnagraficaParteModel.setIndSedeLegale(
						getRequestStringParameter(CAMPO_IND_SEDE_LEGALE).toUpperCase().trim());
			}
			// Sede Operativa
			if (!isRequestParameterNullObj(CAMPO_IND_SEDE_OPERATIVA)) {
				lAnagraficaParteModel.setIndSedeOperativa(
						getRequestStringParameter(CAMPO_IND_SEDE_OPERATIVA).toUpperCase().trim());
			}
		}

		// Cognome
		lAnagraficaParteModel.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase().trim());
		// Nome
		lAnagraficaParteModel.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase().trim());
		// Sesso
		lAnagraficaParteModel.setSesso(getRequestStringParameter(CAMPO_SESSO));
		// Data Nascita
		lAnagraficaParteModel.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA,
				CAMPO_MESE_DATA_NASCITA, CAMPO_GIORNO_DATA_NASCITA));

		// Recupero dati del Comune di nascita
		lAnagraficaParteModel.setCodComuneNascita("-");
		lAnagraficaParteModel.setCodProvinciaNascita("-");
		ComuneModel lComMod;
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			// se presente dal codice comune (e descrizione)
			// 20210524 MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni senza flag validità.
			lComMod = new ComuneModel(
					getDatiComuneByCodDescr(getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
							getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
		} else {
			// altrimenti dalla sola descrizione (rischio omonimi)
			lComMod = new ComuneModel(
					// 20210524 MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni senza flag
					// validità.
					getDatiComuneByDescrOmonimia(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
		}

		lAnagraficaParteModel.setCodComuneNascita(lComMod.getCodComune());
		lAnagraficaParteModel.setCodProvinciaNascita(lComMod.getCodProvincia());

		// Stato Nascita
		if (!isRequestParameterNullObj(CAMPO_COD_STATO_NASCITA)) {
			lAnagraficaParteModel.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));
		}
		// Comune Nascita Estero
		if (!isRequestParameterNullObj(CAMPO_DESC_COMUNE_NASCITA_ESTERO)) {
			lAnagraficaParteModel.setDescComuneNascitaEstero(
					getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_ESTERO).toUpperCase().trim());
		}

		// Codice Fiscale/Partita IVA
		if (!isRequestParameterNullObj(CAMPO_COD_FISCALE)) {
			lAnagraficaParteModel
					.setCodFiscale(getRequestStringParameter(CAMPO_COD_FISCALE).toUpperCase().trim());
		}

		// Imposto la descrizione della Nazione di Nascita
		DecodificheModel lDecMod = new DecodificheModel();
		lDecMod.setContesto("NAZIONE");
		lDecMod.setCode(lAnagraficaParteModel.getCodStatoNascita());
		List lNazioni = (List) DecodificheManager.getInstance().getNazioni();
		int lIndModel = lNazioni.indexOf(lDecMod);
		String lDescri = ((DecodificheModel) lNazioni.get(lIndModel)).getDescription();
		lAnagraficaParteModel.setDescrStatoNascita(lDescri);

		lAnagraficaParteModel.setDescComuneNascita("-");
		if (lComMod != null)
			lAnagraficaParteModel.setDescComuneNascita(lComMod.getDescrizione());

		// Imposto la descrizione della Provincia di Nascita
		lDecMod = new DecodificheModel();
		lDecMod.setContesto("PROVINCIA");
		lDecMod.setCode(lAnagraficaParteModel.getCodProvinciaNascita());
		List lProvincie = (List) DecodificheManager.getInstance().getProvincie();
		lIndModel = lProvincie.indexOf(lDecMod);
		if (lIndModel != -1) {
			lDescri = ((DecodificheModel) lProvincie.get(lIndModel)).getDescription();
			lAnagraficaParteModel.setDescrProvinciaNascita(lDescri);
		} else
			lAnagraficaParteModel.setDescrProvinciaNascita("-");

		// 20260415 [SG]: aggiunto controllo su CF che deve essere obbligatorio e conforme
		// SoggettoUtil.controllaCF(lSogMod);

		// Residenza/Domicilio
		// Tipo Residenza
		residenzaMod.setCodTipoResidenza("R");

		// Flag Domicilio Avvocato
		residenzaMod.setFlgDomAvv("N");

		// Flag Domicilio Difensore
		residenzaMod.setFlgDomicilioDifensore("N");

		// Indirizzo
		if (!isRequestParameterNullObj(CAMPO_INDIRIZZO)) {
			residenzaMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase().trim());
		}

		// Recupero dati del Comune di Residenza
		ComuneModel lComuneMod = null;
		if (!isRequestParameterNullObj(CAMPO_COD_COMUNE_RESIDENZA)
				&& getRequestStringParameter(CAMPO_COD_COMUNE_RESIDENZA).length() > 0) {
			// se presente dal codice comune (e descrizione)
			lComuneMod = getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_COMUNE_RESIDENZA));
			residenzaMod.setCodComune(lComuneMod.getCodComune());
			residenzaMod.setCodProvincia(lComuneMod.getCodProvincia());
		} else {
			residenzaMod.setCodComune("-");
			residenzaMod.setCodProvincia("-");
		}

		// CAP
		if (!isRequestParameterNullObj(CAMPO_CAP_RESIDENZA)) {
			residenzaMod.setCap(getRequestStringParameter(CAMPO_CAP_RESIDENZA));
		}

		// Comune Estero
		if (!isRequestParameterNullObj(CAMPO_DESC_COMUNE_ESTERO_RESIDENZA)) {
			residenzaMod.setDescComuneEstero(
					getRequestStringParameter(CAMPO_DESC_COMUNE_ESTERO_RESIDENZA).toUpperCase().trim());
		}

		// Stato
		if (!isRequestParameterNullObj(CAMPO_COD_STATO_RESIDENZA)) {
			residenzaMod.setCodStato(getRequestStringParameter(CAMPO_COD_STATO_RESIDENZA));
		}

		// Imposto la descrizione della Nazione di Residenza
		DecodificheModel lDecModRes = new DecodificheModel();
		lDecModRes.setContesto("NAZIONE");
		lDecModRes.setCode(residenzaMod.getCodStato());
		// List lNazioniRes = (List) DecodificheManager.getInstance().getNazioni();
		int lIndModelRes = lNazioni.indexOf(lDecModRes);
		String lDescriRes = ((DecodificheModel) lNazioni.get(lIndModelRes)).getDescription();
		residenzaMod.setDescrStato(lDescriRes);

		if (lComuneMod != null) {
			residenzaMod.setDescrComune(lComuneMod.getDescrizione());
		} else {
			residenzaMod.setDescrComune("-");
		}

		// Imposto la descrizione della Provincia di Residenza
		lDecModRes = new DecodificheModel();
		lDecModRes.setContesto("PROVINCIA");
		lDecModRes.setCode(residenzaMod.getCodProvincia());
		List lProvincieRes = (List) DecodificheManager.getInstance().getProvincie();
		lIndModelRes = lProvincieRes.indexOf(lDecMod);
		if (lIndModelRes != -1) {
			lDescriRes = ((DecodificheModel) lProvincieRes.get(lIndModelRes)).getDescription();
			residenzaMod.setDescrProvincia(lDescriRes);
		} else {
			residenzaMod.setDescrProvincia("-");
		}

		residenzaMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		residenzaMod.setDataInserimento(DateUtils.getSysDate());
		residenzaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		lAnagraficaParteModel.setResidenza(residenzaMod);

		lAnagraficaParteModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAnagraficaParteModel.setDataInserimento(DateUtils.getSysDate());
		lAnagraficaParteModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		return lAnagraficaParteModel;
	}

	protected UdienzaPartiModel letturaUdienzaParte() throws Exception {

		UdienzaPartiModel lUdienzaParteModel = new UdienzaPartiModel();

		// Ticket#20200528012 - SIGE - inserimento parte civile / offesa su quadro "emissione ordinanza"
		// gestito numberformatexception poichè significa che l'udienza del procedimento non esiste ancora
		try {
			lUdienzaParteModel.setIdUdienzaProcedimentoSige(getRequestBigDecimalParameter(
					ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE));
		} catch (Exception e) {
			throw new SIGEException(SIGEException.USER_MESSAGE,
					// "Attenzione! L'inserimento delle parti civili o delle parti offese va fatto dopo aver "
					// + "fissato l'udienza da apposita funzione (Udienze/Fissazione/Rinvio/Ruolo >>
					// Fissazione Udienza).");
					"Attenzione! L'inserimento delle parti civili o delle parti offese è previsto dopo"
							+ " l'emissione del Decreto di Fissazione Udienza.");
		}
		// FINE Ticket#20200528012

		lUdienzaParteModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		lUdienzaParteModel.setDataInserimento(DateUtils.getSysDate());
		lUdienzaParteModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		return lUdienzaParteModel;
	}

}