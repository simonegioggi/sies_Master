package siap.bdmc.sbpren.action;

import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;

import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;

import siap.bdmc.config.BdmcProperties;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadRicercaSbPren
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca di SbPren
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
public class ActCollegamentoBdmcSsl extends ActionSiap implements ICostantiSbPren {

	private BdmcProperties mProperties = BdmcProperties.getInstance();

	/*****************************************************************************
	 * Azione di caricamento della pagina di ricerca. Si occupa di precaricare tutti i dati da visualizzare i
	 * tale pagina (es: combo)
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// =============================================================
		// Aggiungere qui eventuali caricamento di combo o altri dati
		// da passare alla finestra di ricerca, e relative setRequest
		// =============================================================
		// es:
		// Option lOption = new Option(codice per caricare la option);
		// setRequestAttribute("nome_attributo", "" + lOption);

		byte[] autenticazioneB64 = null;
		String autenticazione = this.getCodUtenteConnesso() + ":" + this.getCodUfficioUtenteConnesso();
		try {
			KeyStore keystore = KeyStore.getInstance("JKS");
			String lPathProp = System.getProperty("path.properties");
			String fileName = lPathProp + System.getProperty("file.separator") + "bdmc_sies.ks";
			char[] passPhrase = "carlino".toCharArray();
			File certificateFile = new File(fileName);
			keystore.load(new FileInputStream(certificateFile), passPhrase);
			String alias = "bdmc_sies";

			Key key = keystore.getKey(alias, "carlino".toCharArray());
			RSAPrivateKey privateKey = null;
			RSAEngine rsaEn = null;
			RSAKeyParameters rs = null;

			if (key instanceof RSAPrivateKey) {
				privateKey = (RSAPrivateKey) key;
				rsaEn = new RSAEngine();
				// false indirica che la chiave è pubblica
				rs = new RSAKeyParameters(true, privateKey.getModulus(), privateKey.getPrivateExponent());
				// false indica che si tratta di decifratura
				rsaEn.init(true, rs);
				byte[] autenticazioneByteFormat = autenticazione.getBytes();
				autenticazioneB64 = org.bouncycastle.util.encoders.Base64.encode(rsaEn.processBlock(
						autenticazioneByteFormat, 0, autenticazioneByteFormat.length));
			}
			/*
			 * PublicKey publicKey = keystore.getCertificate(alias).getPublicKey(); RSAPublicKey rsaPublicKey
			 * = null;
			 * 
			 * if(publicKey instanceof RSAPublicKey) {
			 * 
			 * rsaPublicKey = (RSAPublicKey)publicKey; // false indica che la chiave è pubblica rs = new
			 * RSAKeyParameters(false, rsaPublicKey.getModulus(), rsaPublicKey.getPublicExponent());
			 * rsaEn.init(false, rs); byte[] cBis =
			 * org.bouncycastle.util.encoders.Base64.decode(autenticazioneB64); byte[] b =
			 * rsaEn.processBlock(cBis, 0, cBis.length); }
			 */
		} catch (Exception ex) {
		}

		setRequestAttribute("autentica", new String(autenticazioneB64));

		// ============================================================
		// carico dal file properties di Bdmc indirizzo del server
		// per effettuare la prenotazione dei dati Bdmc
		// ============================================================
		setRequestAttribute("indirizzoBdmc", mProperties.getProperty("BDMCSERVER"));

		// restituisce la jsp di visualizzazione della pagina di ricerca
		return PG_COLLEGAMENTO_BDMC;
	}

}