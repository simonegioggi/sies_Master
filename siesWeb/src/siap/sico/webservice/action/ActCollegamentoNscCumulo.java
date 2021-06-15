package siap.sico.webservice.action;

import java.math.BigDecimal;
import java.util.Date;

import f3b.util.Utils;
import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.sico.saml.model.SamlModel;
import siap.sico.saml.util.SamlMaker;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.webservice.config.NscProperties;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Title: ActCollegamentoNscCumulo 
 * Description: Classe Action per l'accesso al Casellario (Nsc) dove poter
 * 				ricercare i Titoli Esecutivi, legati al Soggetto associato al procedimento SIEP Cumulante,
 * 			 	coinvolti nella gestione del cumulo 
 * Company: Engeneering S.p.A.
 *
 * @version 1.0
 */
public class ActCollegamentoNscCumulo extends ActWsBase implements ICostantiNsc {

	private NscProperties mProperties = NscProperties.getInstance();

	public String processRequest() throws Exception {

		UtenteModel lUteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		FascicoloSiepModel fascicoloSiepMod = ((FascicoloSiepModel) getSessionAttribute("fascicolo"));
		SoggettoModel soggettoMod = fascicoloSiepMod.getSoggetto();

		String cognomeSoggetto = soggettoMod.getCognome();
		String nomeSoggetto = soggettoMod.getNome();
		Date dn = soggettoMod.getDataNascita();
		String dataNascita = "";
		if (dn != null)
			dataNascita = dn.toString();
		String sesso = soggettoMod.getSesso();
		String luogoNascita = soggettoMod.getCodComuneNascita();
		String statoNascita = soggettoMod.getCodStatoNascita();
		String descLuogoNascita = soggettoMod.getDescrComuneNascita();
		String descStatoNascita = soggettoMod.getDescrStatoNascita();
		String paternita = soggettoMod.getPaternita();

		String progrFascSiep = fascicoloSiepMod.getChiaveProgr().toString();
		String annoFascSiep = fascicoloSiepMod.getChiaveAnno().toString();
		String idFascicoloSiep = fascicoloSiepMod.getIdFascicoloSiep().toString();
		String idSoggettoSiep = soggettoMod.getIdSoggetto().toString();

		String lCodiceCentrSedeUfficio = "", lCodiceCentrTipoUfficio = "", lUtenteConnesso = "",
				lHostAddress = "", lCognomeUtente = "", lNomeUtente = "", lDistretto = "", lSistema = "";
		CodiciSiesNscModel lCodiciSiesNscModel;

		// DECODIFICA CODI_SEDE_UFFICIO
		if (lUteMod.getUfficioUtente().getCodComune() != null) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("COMUNE");
			lCodiciSiesNscModel.setCoSies(lUteMod.getUfficioUtente().getCodComune());
			// lCodiceCentrSedeUfficio =
			// DecodificaCodiceSies(lCodiciSiesNscModel);
			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodiceCentrSedeUfficio = aCodiciSIESNSCModel.getCoCodcentr();
		}

		// DECODIFICA TIPO_UFFICIO
		if (lUteMod.getUfficioUtente().getCodTipoUfficio() != null) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("TIPO_UFFICIO");
			lCodiciSiesNscModel.setCoSies(lUteMod.getUfficioUtente().getCodTipoUfficio());
			// lCodiceCentrTipoUfficio =
			// DecodificaCodiceSies(lCodiciSiesNscModel);
			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodiceCentrTipoUfficio = aCodiciSIESNSCModel.getCoCodcentr();
		}

		lUtenteConnesso = getCodUtenteConnesso();
		lHostAddress = getRequest().getServerName() + ":" + getRequest().getServerPort();
		lCognomeUtente = lUteMod.getCognome();
		lNomeUtente = lUteMod.getNome();
		lDistretto = lUteMod.getUfficioUtente().getCodDistretto().substring(0, 6);
		lSistema = "SIEP";
		// MEV INTEGRAZIONE SIES ADN: aggiunta impostazione di proprietà userAdn
		// ed aggiunto nel SamlModel
		String lUserAdn = lUteMod.getUserAdn();

		// DECODIFICA CODICE COMUNE NASCITA SOGGETTO
		if (luogoNascita != null) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("COMUNE");
			lCodiciSiesNscModel.setCoSies(luogoNascita);
			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			luogoNascita = aCodiciSIESNSCModel.getCoNsc();
			descLuogoNascita = aCodiciSIESNSCModel.getCoNscDes();
		}
		// DECODIFICA CODICE NAZIONE NASCITA SOGGETTO
		if (statoNascita != null && !"-".equals(statoNascita)) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("NAZIONE");
			lCodiciSiesNscModel.setCoSies(statoNascita);
			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			statoNascita = aCodiciSIESNSCModel.getCoNsc();
			descStatoNascita = aCodiciSIESNSCModel.getCoNscDes();
		}

		// ============================================================
		// carico dal file properties di Nsc indirizzo del server
		// per effettuare la prenotazione dei dati Nsc
		// ============================================================
		setRequestAttribute("IndirizzoNsc", mProperties.getProperty("NscServer"));
		// **************
		setRequestAttribute("ProgrFascSiep", progrFascSiep);
		setRequestAttribute("AnnoFascSiep", annoFascSiep);
		setRequestAttribute("IdFascicoloSiep", idFascicoloSiep);
		setRequestAttribute("IdSoggettoSiep", idSoggettoSiep);
		setRequestAttribute("CognomeSoggetto", cognomeSoggetto);
		setRequestAttribute("NomeSoggetto", nomeSoggetto);
		setRequestAttribute("DataNascita", dataNascita);
		setRequestAttribute("Sesso", sesso);
		setRequestAttribute("LuogoNascita", luogoNascita);
		setRequestAttribute("StatoNascita", statoNascita);
		setRequestAttribute("DescLuogoNascita", descLuogoNascita);
		setRequestAttribute("DescStatoNascita", descStatoNascita);
		setRequestAttribute("Paternita", paternita);
		// **************

		/* Togliere commenti quando sarà pronto SAML */
		String lSamkCriptata = "";
		String lUseSAML = mProperties.getProperty("UseSAML");

		if ("SI".equals(lUseSAML)) {
			SamlModel lModel = new SamlModel(lCodiceCentrSedeUfficio, lCodiceCentrTipoUfficio,
					lUtenteConnesso, lHostAddress, lCognomeUtente, lNomeUtente, lDistretto, lSistema,
					lUserAdn);
			SamlMaker lmaker = new SamlMaker();
			lSamkCriptata = lmaker.createSamlAssertion(lModel);
		}

		setRequestAttribute("UseSAML", lUseSAML);
		setRequestAttribute("TokenSAML", lSamkCriptata);
		// MEV 16: aggiunta nuova impostazione di proprietà e modificata l'intera gestione dei dati da
		// inviare.
		setRequestAttribute("SiepToNsc", "SI");
		// MEV 16 CUMULO: aggiunta impostazione attributo
		BigDecimal idIstruttoriaCumulo;
		try {
			idIstruttoriaCumulo = ((BigDecimal) getSessionAttribute("IdIstruttoriaCumulo"));
		} catch (Exception e) {
			IIstruttoriaCumulo iic = SIEPLookupRemote.getIstruttoriaCumuloRemote();
			IstruttoriaCumuloModel icm = iic
					.ExRicercaIstruttoriaCumuloApertaByIdFasSiep(fascicoloSiepMod.getIdFascicoloSiep());
			idIstruttoriaCumulo = icm.getIdIstruttoriaCumulo();
		}
		if (Utils.isPresent(idIstruttoriaCumulo)) {
			setRequestAttribute("IdIstruttoriaCumulo", idIstruttoriaCumulo.toString());
			removeSessionAttribute("IdIstruttoriaCumulo");
		}

		// restituisce la jsp di visualizzazione della pagina di ricerca
		return PG_COLLEGAMENTO_NSC_CUMULO;
	}

}