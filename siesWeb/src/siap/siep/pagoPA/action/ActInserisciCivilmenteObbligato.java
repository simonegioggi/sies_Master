package siap.siep.pagoPA.action;

import java.util.List;

import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-13 
 * Title: ActInserisciCivilmenteObbligato 
 * Description: Classe Action per la insert del Civilmente Obbligato
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActInserisciCivilmenteObbligato extends ActionSiap implements ICostantiPagoPA {

	public String processRequest() throws Exception {

		// Fascicolo siep in sessione
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		CivilmenteObbligatoModel com = riempiDatiCivilmenteObbligato("");
		com.setFasSieIdFascicolSiep(fsm.getIdFascicoloSiep());
		// tutore
		String codTutore = getRequestStringParameter(CAMPO_TIPO_TUTORE);
		com.setCodTutore(codTutore);

		// Chiama il controller.
		/* CivilmenteObbligatoModel comRet = */ico.ExInserisciCivilmenteObbligato(com);

		if (!"-".equals(codTutore) && !"G".equals(com.getCodPersona())) {
			CivilmenteObbligatoModel com_ST = riempiDatiCivilmenteObbligato("_ST");
			if (Utils.isPresent(com_ST.getCognome()) && Utils.isPresent(com_ST.getNome())) {
				com_ST.setFasSieIdFascicolSiep(fsm.getIdFascicoloSiep());
				com_ST.setCodTutore(codTutore);
				ico.ExInserisciCivilmenteObbligato(com_ST);
			}
		}

		RedirectTo rt = new RedirectTo();
		rt.setPage(IWebConstants.PG_MAIN);
		rt.setAction("siap.siep.pagoPA.action.ActDettaglioCivilmenteObbligato");
		// rt.setParameter(CAMPO_ID_CIVILMENTE_OBBLIGATO, comRet.getIdCivilmenteObbligato().toString());
		rt.setParameter(CAMPO_ID_FASCICOLO_SIEP, fsm.getIdFascicoloSiep().toString());
		return rt.toString();
	}

	@SuppressWarnings("rawtypes")
	protected CivilmenteObbligatoModel riempiDatiCivilmenteObbligato(String st) throws Exception {

		CivilmenteObbligatoModel com = new CivilmenteObbligatoModel();

		// Parte interessata all'udienza (F=Fisica, G=Giuridica)
		String codPersona = getRequestStringParameter(RADIO_COD_PERSONA);
		com.setCodPersona(codPersona);

		// Persona Giuridica
		if (codPersona != null && codPersona.equals("G")) {
			// Società
			com.setDenominazione(getRequestStringParameter(CAMPO_DENOMINAZIONE).toUpperCase().trim());
			// Ragione Sociale
			if (!isRequestParameterNullObj(CAMPO_RAG_SOCIALE)) {
				com.setRagSociale(getRequestStringParameter(CAMPO_RAG_SOCIALE));
			}
			// Provincia
			if (!isRequestParameterNullObj(CAMPO_COD_PROVINCIA)) {
				com.setCodProvincia(getRequestStringParameter(CAMPO_COD_PROVINCIA));
			}
			// Partita IVA/Codice Fiscale
			if (!isRequestParameterNullObj(CAMPO_COD_FISCALE_RAP)) {
				com.setCodFiscaleRap(getRequestStringParameter(CAMPO_COD_FISCALE_RAP));
			}
			// Sede Legale
			if (!isRequestParameterNullObj(CAMPO_IND_SEDE_LEGALE)) {
				com.setIndSedeLegale(getRequestStringParameter(CAMPO_IND_SEDE_LEGALE).toUpperCase().trim());
			}
			// Sede Operativa
			if (!isRequestParameterNullObj(CAMPO_IND_SEDE_OPERATIVA)) {
				com.setIndSedeOperativa(
						getRequestStringParameter(CAMPO_IND_SEDE_OPERATIVA).toUpperCase().trim());
			}
		}

		// Cognome
		com.setCognome((getRequestStringParameter(CAMPO_COGNOME + st)).toUpperCase().trim());
		// Nome
		com.setNome((getRequestStringParameter(CAMPO_NOME + st)).toUpperCase().trim());
		// Sesso
		com.setSesso(getRequestStringParameter(CAMPO_SESSO + st));
		// Data Nascita
		com.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA + st, CAMPO_MESE_DATA_NASCITA + st,
				CAMPO_GIORNO_DATA_NASCITA + st));

		// Recupero dati del Comune di nascita
		ComuneModel lComMod = null;
		if (!isRequestParameterNullObj(CAMPO_COD_COMUNE_NASCITA + st)
				&& getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA + st).length() > 0) {
			// se presente dal codice comune (e descrizione)
			lComMod = getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA + st));
			// Comune Nascita
			com.setCodComuneNascita(lComMod.getCodComune());
			com.setCodProvinciaNascita(lComMod.getCodProvincia());
		} else {
			com.setCodComuneNascita("-");
			com.setCodProvinciaNascita("-");
		}

		// Stato Nascita
		// && getRequestStringParameter(CAMPO_COD_STATO_NASCITA).equals("039")
		if (!isRequestParameterNullObj(CAMPO_COD_STATO_NASCITA + st)) {
			com.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA + st));
		}
		// Comune Nascita Estero
		if (!isRequestParameterNullObj(CAMPO_DESC_COMUNE_NASCITA_ESTERO + st)) {
			com.setDescComuneNascitaEstero(
					getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_ESTERO + st).toUpperCase().trim());
		}

		// Codice Fiscale/Partita IVA
		if (!isRequestParameterNullObj(CAMPO_COD_FISCALE + st)) {
			com.setCodFiscale(getRequestStringParameter(CAMPO_COD_FISCALE + st).toUpperCase().trim());
		}

		// Pec
		if (!isRequestParameterNullObj(CAMPO_PEC + st)) {
			com.setPec(getRequestStringParameter(CAMPO_PEC + st));
		}
		// Email
		if (!isRequestParameterNullObj(CAMPO_EMAIL + st)) {
			com.setEmail(getRequestStringParameter(CAMPO_EMAIL + st));
		}

		// Imposto la descrizione della Nazione di Nascita
		DecodificheModel lDecMod = new DecodificheModel();
		lDecMod.setContesto("NAZIONE");
		lDecMod.setCode(com.getCodStatoNascita());
		List lNazioni = (List) DecodificheManager.getInstance().getNazioni();
		int lIndModel = lNazioni.indexOf(lDecMod);
		String lDescri = ((DecodificheModel) lNazioni.get(lIndModel)).getDescription();
		com.setDescrStatoNascita(lDescri);

		if (lComMod != null) {
			com.setDescComuneNascita(lComMod.getDescrizione());
		} else {
			com.setDescComuneNascita("-");
		}

		// Imposto la descrizione della Provincia di Nascita
		lDecMod = new DecodificheModel();
		lDecMod.setContesto("PROVINCIA");
		lDecMod.setCode(com.getCodProvinciaNascita());
		List lProvincie = (List) DecodificheManager.getInstance().getProvincie();
		lIndModel = lProvincie.indexOf(lDecMod);
		if (lIndModel != -1) {
			lDescri = ((DecodificheModel) lProvincie.get(lIndModel)).getDescription();
			com.setDescrProvinciaNascita(lDescri);
		} else {
			com.setDescrProvinciaNascita("-");
		}

		// inserimento RESIDENZA solo se sono valorizzati indirizzo e comune o comune estero
		boolean indirizzoResidenza, comuneResidenza, comuneEsteroResidenza = false;
		indirizzoResidenza = !isRequestParameterNullObj(CAMPO_INDIRIZZO + st)
				&& getRequestStringParameter(CAMPO_INDIRIZZO + st).length() > 0;
		comuneResidenza = !isRequestParameterNullObj(CAMPO_COD_COMUNE_RESIDENZA + st)
				&& getRequestStringParameter(CAMPO_COD_COMUNE_RESIDENZA + st).length() > 0;
		comuneEsteroResidenza = !isRequestParameterNullObj(CAMPO_DESC_COMUNE_ESTERO_RESIDENZA + st)
				&& getRequestStringParameter(CAMPO_DESC_COMUNE_ESTERO_RESIDENZA + st).length() > 0;
		if (indirizzoResidenza && (comuneResidenza || comuneEsteroResidenza)) {
			// Residenza/Domicilio
			ResidenzaModel rm = new ResidenzaModel();

			// Tipo Residenza
			rm.setCodTipoResidenza("R");

			// Flag Domicilio Avvocato
			rm.setFlgDomAvv("N");

			// Flag Domicilio Difensore
			rm.setFlgDomicilioDifensore("N");

			// Indirizzo
			if (!isRequestParameterNullObj(CAMPO_INDIRIZZO + st)) {
				rm.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO + st).toUpperCase().trim());
			}

			// Recupero dati del Comune di Residenza
			ComuneModel lComuneMod = null;
			if (!isRequestParameterNullObj(CAMPO_COD_COMUNE_RESIDENZA + st)
					&& getRequestStringParameter(CAMPO_COD_COMUNE_RESIDENZA + st).length() > 0) {
				// se presente dal codice comune (e descrizione)
				lComuneMod = getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_COMUNE_RESIDENZA + st));
				rm.setCodComune(lComuneMod.getCodComune());
				rm.setCodProvincia(lComuneMod.getCodProvincia());
			} else {
				rm.setCodComune("-");
				rm.setCodProvincia("-");
			}

			// CAP
			if (!isRequestParameterNullObj(CAMPO_CAP_RESIDENZA + st)) {
				rm.setCap(getRequestStringParameter(CAMPO_CAP_RESIDENZA + st));
			}

			// Comune Estero
			if (!isRequestParameterNullObj(CAMPO_DESC_COMUNE_ESTERO_RESIDENZA + st)) {
				rm.setDescComuneEstero(getRequestStringParameter(CAMPO_DESC_COMUNE_ESTERO_RESIDENZA + st)
						.toUpperCase().trim());
			}

			// Stato
			if (!isRequestParameterNullObj(CAMPO_COD_STATO_RESIDENZA + st)) {
				rm.setCodStato(getRequestStringParameter(CAMPO_COD_STATO_RESIDENZA + st));
			}

			// Imposto la descrizione della Nazione di Residenza
			DecodificheModel lDecModRes = new DecodificheModel();
			lDecModRes.setContesto("NAZIONE");
			lDecModRes.setCode(rm.getCodStato());
			// List lNazioniRes = (List) DecodificheManager.getInstance().getNazioni();
			int lIndModelRes = lNazioni.indexOf(lDecModRes);
			String lDescriRes = ((DecodificheModel) lNazioni.get(lIndModelRes)).getDescription();
			rm.setDescrStato(lDescriRes);

			if (lComuneMod != null) {
				rm.setDescrComune(lComuneMod.getDescrizione());
			} else {
				rm.setDescrComune("-");
			}

			// Imposto la descrizione della Provincia di Residenza
			lDecModRes = new DecodificheModel();
			lDecModRes.setContesto("PROVINCIA");
			lDecModRes.setCode(rm.getCodProvincia());
			List lProvincieRes = (List) DecodificheManager.getInstance().getProvincie();
			lIndModelRes = lProvincieRes.indexOf(lDecMod);
			if (lIndModelRes != -1) {
				lDescriRes = ((DecodificheModel) lProvincieRes.get(lIndModelRes)).getDescription();
				rm.setDescrProvincia(lDescriRes);
			} else {
				rm.setDescrProvincia("-");
			}

			rm.setCodOperatoreInserimento(getCodUtenteConnesso());
			rm.setDataInserimento(DateUtils.getSysDate());
			rm.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			com.setResidenza(rm);
		}

		com.setCodOperatoreInserimento(getCodUtenteConnesso());
		com.setDataInserimento(DateUtils.getSysDate());
		com.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// valore di ritorno
		return com;
	}

}