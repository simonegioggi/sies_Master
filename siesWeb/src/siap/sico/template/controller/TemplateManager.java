package siap.sico.template.controller;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.template.model.TemplateModel;
import siap.sico.util.SICOLookupRemote;

/**
 * TemplateManager - Classe singleton per il caricamento dei templates da stampare
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TemplateManager {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private static TemplateManager mTemplateManager;
	private static Hashtable mTemplates;

	protected TemplateManager() {

		mTemplates = new Hashtable();
	}

	public static TemplateManager getInstance() {

		if (mTemplateManager == null) {
			mTemplateManager = new TemplateManager();

			mTemplateManager.init();
		}
		return mTemplateManager;
	}

	/**
	 * Inizializzazione degli attributi del Singleton
	 */
	private void init() {

		try {
			ITemplate lTemplate = SICOLookupRemote.getTemplateRemote();
			Vector lVector = lTemplate.ExRicercaAllTemplate();
			Iterator lItx = lVector.iterator();
			while (lItx.hasNext()) {
				TemplateModel lTemp = new TemplateModel((TemplateModel) lItx.next());
				mTemplates.put(lTemp.getIdTemplate(), lTemp.getPathRicerca() + lTemp.getNomeTemplate());
				// FIXME: commentare - vale solo per LOCALHOST
				// String pathRicerca = lTemp.getPathRicerca().replace("/", "\\").replace("\\var\\SIES",
				// "C:");
				// mTemplates.put(lTemp.getIdTemplate(), pathRicerca + lTemp.getNomeTemplate());
				// TODO: commentare - vale solo per LOCALHOST
			}
		} catch (F3BException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(this.getClass().getName(), ex);
		}
	}

	public String getTemplateName(String Key) {

		String lName = null;

		if (mTemplates != null) {
			if (!mTemplates.isEmpty()) {
				if (Key != null)
					lName = (String) mTemplates.get(Key);
			}
		}
		return lName;
	}

}