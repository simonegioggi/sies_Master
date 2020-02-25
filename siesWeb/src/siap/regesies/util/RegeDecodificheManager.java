package siap.regesies.util;

import java.util.Collection;

import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RegeDecodificheManager
 * </p>
 * <p>
 * Description: Singleton per la gestione delle Combo da caricare all'interno delle Form HTML
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
@SuppressWarnings("rawtypes")
public class RegeDecodificheManager {

	private static RegeDecodificheManager mRegeDecodificheManager = null;

	private Collection mUfficiRege = null;

	protected RegeDecodificheManager() {
	}

	public static RegeDecodificheManager getInstance() {
		if (mRegeDecodificheManager == null) {
			mRegeDecodificheManager = new RegeDecodificheManager();
			mRegeDecodificheManager.init();
		}

		return mRegeDecodificheManager;
	}

	/**
	 * Inizializzazione degli attributi del Singleton
	 */
	private void init() {
		DecodificheModel lModel = new DecodificheModel();

		try {
			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
			lModel.setContesto("TIPO_UFFICIO_REGE");
			mUfficiRege = lDecodifiche.ExRicercaDecodifiche(lModel);
		} catch (F3BException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Collection getUfficiRege() {
		return mUfficiRege;
	}

}