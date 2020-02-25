package siap.sico.decodifiche.controller;

/**
 * <p>Title: DecodificheManager</p>
 * <p>Description: Singleton per la gestione delle Combo da caricare all'interno delle Form HTML</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
public class DecodificheManager extends DecodificheManagerModel {

	private static DecodificheManager mDecodificheManager = null;

	protected DecodificheManager() {
	}

	public static DecodificheManager getInstance() {
		if (mDecodificheManager == null) {
			mDecodificheManager = new DecodificheManager();
			mDecodificheManager.init();
		}

		return mDecodificheManager;
	}

}