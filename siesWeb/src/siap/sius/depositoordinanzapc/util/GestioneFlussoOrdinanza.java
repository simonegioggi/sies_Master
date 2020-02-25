package siap.sius.depositoordinanzapc.util;

import siap.sius.tenore.model.TenoreModel;

/**
 * <p>
 * Title: GestioneFlussoOrdinanza.
 * </p>
 * <p>
 * Description: Gestisce in funzione del cod oggetto tenore e il relativo esito l'assegnazione di un template
 * di stampa. Questo avviene passando l'elenco dei tenori opportunamente sortati per il peso.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class GestioneFlussoOrdinanza {

	private TenoreModel[] mTenori;
	private String mPosizioneGiuridica;
	private String mTemplate;

	private static final String TEMPLATE_DEFAULT = "SIUS_OR_018";

	/**
	 * Costruttore di classe.
	 */
	public GestioneFlussoOrdinanza() {
		mTenori = new TenoreModel[0];
		mTemplate = TEMPLATE_DEFAULT;
	}

	/**
	 * Costruttore di classe con parametri.
	 * <p>
	 * 
	 * @param aTenori
	 *            elenco dei tenori.
	 */
	public GestioneFlussoOrdinanza(TenoreModel[] aTenori) {
		mTenori = aTenori;
		mTemplate = TEMPLATE_DEFAULT;
	}

	/**
	 * Costruttore di calsse con parametri.
	 * <p>
	 * 
	 * @param aTenori
	 *            elenco dei tenori.
	 * @param aPosizioneGiuridica
	 *            valore della posizione giuridic.
	 */
	public GestioneFlussoOrdinanza(TenoreModel[] aTenori, String aPosizioneGiuridica) {
		mTenori = aTenori;
		mPosizioneGiuridica = aPosizioneGiuridica;
	}

	/**
	 * Ritorna il template associato.
	 * <p>
	 * 
	 * @return nome del template associato.
	 */
	public String getTemplate() {
		return this.mTemplate;
	}

	/**
	 * Esegue la definizione del template da associare, prelevabile con il metodo getTemplate().
	 */
	public void start() {
		for (int x = 0; x < mTenori.length; x++) {
			TenoreModel lModel = mTenori[x];

			// K1
			if (lModel.getCodOggettoTenore().equals("0002") && lModel.getCodEsitoTenore().equals("0001")) {
				mTemplate = "SIUS_OR_001";
				break;
			}

			// K2
			if (lModel.getCodOggettoTenore().equals("0001") && lModel.getCodEsitoTenore().equals("0001")) {
				mTemplate = "SIUS_OR_002";
				break;
			}

			// K5
			if (mTenori.length == 1
					&& (lModel.getCodOggettoTenore().equals("0005")
							|| lModel.getCodOggettoTenore().equals("0006")
							|| lModel.getCodOggettoTenore().equals("0007")
							|| lModel.getCodOggettoTenore().equals("0008")
							|| lModel.getCodOggettoTenore().equals("0009"))
					&& lModel.getCodEsitoTenore().equals("0001")) {
				mTemplate = "SIUS_OR_004";
				break;
			}

			// K5bis
			if (mTenori.length > 1
					&& (lModel.getCodOggettoTenore().equals("0005")
							|| lModel.getCodOggettoTenore().equals("0006")
							|| lModel.getCodOggettoTenore().equals("0007")
							|| lModel.getCodOggettoTenore().equals("0008")
							|| lModel.getCodOggettoTenore().equals("0009"))
					&& lModel.getCodEsitoTenore().equals("0001")) {
				mTemplate = "SIUS_OR_005";
				break;
			}

			// K6
			if (lModel.getCodOggettoTenore().equals("0010") && lModel.getCodEsitoTenore().equals("0001")) {
				mTemplate = "SIUS_OR_006";
				break;
			}

			// K7
			if (lModel.getCodOggettoTenore().equals("0011") && lModel.getCodEsitoTenore().equals("0001")) {
				for (int y = x; y < mTenori.length; y++) {
					lModel = mTenori[y];

					if ((lModel.getCodOggettoTenore().equals("0030")
							|| lModel.getCodOggettoTenore().equals("0031")
							|| lModel.getCodOggettoTenore().equals("0032"))
							&& !lModel.getCodEsitoTenore().equals("0001")) {
						mTemplate = "SIUS_OR_007";
						break;
					}
				}

				break;
			}

			// K8
			if (mTenori.length == 1 && lModel.getCodOggettoTenore().equals("0004")
					&& lModel.getCodEsitoTenore().equals("0001")) {
				mTemplate = "SIUS_OR_008";
				break;
			}

			// K8bis
			if (mTenori.length > 1 && lModel.getCodOggettoTenore().equals("0004")
					&& lModel.getCodEsitoTenore().equals("0001")) {
				for (int y = x; y < mTenori.length; y++) {
					lModel = mTenori[y];

					if (!lModel.getCodEsitoTenore().equals("0001")) {
						mTemplate = "SIUS_OR_009";
						break;
					}
				}
				break;
			}

			// K9(NA)
			if ((lModel.getCodOggettoTenore().equals("0014") || lModel.getCodOggettoTenore().equals("0015")
					|| lModel.getCodOggettoTenore().equals("0016"))
					&& lModel.getCodEsitoTenore().equals("0006")) {
				mTemplate = "SIUS_OR_010";
				break;
			}

			// K10
			if (lModel.getCodOggettoTenore().equals("0020") && lModel.getCodEsitoTenore().equals("0009")) {
				mTemplate = "SIUS_OR_011";
				break;
			}

			// K11
			if (lModel.getCodOggettoTenore().equals("0022") && lModel.getCodEsitoTenore().equals("0012")) // &&
			// mPosizioneGiuridica != null && mPosizioneGiuridica.equals("001") )
			{
				mTemplate = "SIUS_OR_012";
				break;
			}

			// K14
			if (lModel.getCodOggettoTenore().equals("0027") && lModel.getCodEsitoTenore().equals("0012"))// &&
			// mPosizioneGiuridica != null && mPosizioneGiuridica.equals("001") )
			{
				mTemplate = "SIUS_OR_013";
				break;
			}

			// K16
			if ((lModel.getCodOggettoTenore().equals("0030") || lModel.getCodOggettoTenore().equals("0031")
					|| lModel.getCodOggettoTenore().equals("0032")
					|| lModel.getCodOggettoTenore().equals("0033"))
					&& lModel.getCodEsitoTenore().equals("0002"))// &&
			// mPosizioneGiuridica != null && mPosizioneGiuridica.equals("001") )
			{
				mTemplate = "SIUS_OR_014";
				break;
			}

			// K17
			if (lModel.getCodOggettoTenore().equals("0034")) {
				mTemplate = "SIUS_OR_015";
				break;
			}

			// K18
			if (lModel.getCodOggettoTenore().equals("0036") && lModel.getCodEsitoTenore().equals("0028")) {
				mTemplate = "SIUS_OR_016";
				break;
			}

			// K19
			if (lModel.getCodOggettoTenore().equals("0039") && lModel.getCodEsitoTenore().equals("0029")) {
				mTemplate = "SIUS_OR_017";
				break;
			}

		} // End for
	}

	public String getPosizioneGiuridica() {
		return mPosizioneGiuridica;
	}

	public void setPosizioneGiuridica(String mPosizioneGiuridica) {
		this.mPosizioneGiuridica = mPosizioneGiuridica;
	}

}