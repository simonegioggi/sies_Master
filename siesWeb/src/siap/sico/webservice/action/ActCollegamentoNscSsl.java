package siap.sico.webservice.action;

import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.sico.saml.model.SamlModel;
import siap.sico.saml.util.SamlMaker;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.webservice.config.NscProperties;

/**
 * <p>
 * Title: ActCollegamentoNscSsl
 * </p>
 * <p>
 * Description: Classe Action per l'accesso al Casellario (Nsc)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 *
 * @version 1.0
 */
public class ActCollegamentoNscSsl extends ActWsBase implements ICostantiNsc {

	private NscProperties mProperties = NscProperties.getInstance();

	public String processRequest() throws Exception {

		UtenteModel lUteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		String lCodiceCentrSedeUfficio = "", lCodiceCentrTipoUfficio = "", lUtenteConnesso = "",
				lHostAddress = "", lCognomeUtente = "", lNomeUtente = "", lDistretto = "", lSistema = "";
		CodiciSiesNscModel lCodiciSiesNscModel;

		// DECODIFICA CODI_SEDE_UFFICIO
		if (lUteMod.getUfficioUtente().getCodComune() != null) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("COMUNE");
			lCodiciSiesNscModel.setCoSies(lUteMod.getUfficioUtente().getCodComune());
			// lCodiceCentrSedeUfficio = DecodificaCodiceSies(lCodiciSiesNscModel);
			CodiciSiesNscModel aCodiciSIESNSCModel = DecodificaCodiceSies(lCodiciSiesNscModel);
			lCodiceCentrSedeUfficio = aCodiciSIESNSCModel.getCoCodcentr();
		}

		// DECODIFICA TIPO_UFFICIO
		if (lUteMod.getUfficioUtente().getCodTipoUfficio() != null) {
			lCodiciSiesNscModel = new CodiciSiesNscModel();
			lCodiciSiesNscModel.setCoDomain("TIPO_UFFICIO");
			lCodiciSiesNscModel.setCoSies(lUteMod.getUfficioUtente().getCodTipoUfficio());
			// lCodiceCentrTipoUfficio = DecodificaCodiceSies(lCodiciSiesNscModel);
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

		// ============================================================
		// carico dal file properties di Nsc indirizzo del server
		// per effettuare la prenotazione dei dati Nsc
		// ============================================================

		setRequestAttribute("IndirizzoNsc", mProperties.getProperty("NscServer"));
		setRequestAttribute("CodiceSedeUfficio", lCodiceCentrSedeUfficio);
		setRequestAttribute("CodiceTipoUfficio", lCodiceCentrTipoUfficio);
		setRequestAttribute("Utente", lUtenteConnesso);
		setRequestAttribute("HostAddress", lHostAddress);
		setRequestAttribute("CognomeUtente", lCognomeUtente);
		setRequestAttribute("NomeUtente", lNomeUtente);
		setRequestAttribute("Distretto", lDistretto);
		setRequestAttribute("Sistema", lSistema);

		/* Togliere commenti quando sarà pronto SAML */
		String lSamkCriptata = "";
		String lUseSAML = mProperties.getProperty("UseSAML");

		if (lUseSAML.equals("SI")) {
			SamlModel lModel = new SamlModel(lCodiceCentrSedeUfficio, lCodiceCentrTipoUfficio,
					lUtenteConnesso, lHostAddress, lCognomeUtente, lNomeUtente, lDistretto, lSistema,
					lUserAdn);
			SamlMaker lmaker = new SamlMaker();
			lSamkCriptata = lmaker.createSamlAssertion(lModel);
		}

		setRequestAttribute("UseSAML", lUseSAML);
		setRequestAttribute("TokenSAML", lSamkCriptata);

		// restituisce la jsp di visualizzazione della pagina di ricerca
		return PG_COLLEGAMENTO_NSC;
	}

}