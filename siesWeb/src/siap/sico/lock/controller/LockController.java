package siap.sico.lock.controller;

import java.util.Hashtable;

import javax.servlet.ServletContext;

import siap.sico.lock.model.LockModel;

public class LockController {

	public LockController() {
	}

	/*
	 * Metodo che locka un entità presa in carico da un utente, inserendola nella Hash lockTable se non ancora
	 * ancora lockata.
	 *
	 * @sessionID = key della hashtable L'entità coinvolta è descritta tramite
	 *
	 * @Entity = descrizione testuale dell'entità
	 *
	 * @IdEntity = Id dell'occorrenza
	 *
	 * @Cod_utente = Codice ID Utente che ha lockato l'entità Torna un LockModel valorizzato se l'entita è
	 * lockata contenente le informazione del lock null otherwise.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static LockModel lockIfNotLocked(ServletContext ctx, String Entity, String idEntity,
			String Cod_utente, String SessionID) {

		Hashtable lockTable = (Hashtable) ctx.getAttribute("lockTable");
		// Object lckModels[] = lockTable.values().toArray();
		Object lckKeys[] = lockTable.keySet().toArray();
		for (int i = 0; i < lckKeys.length; i++) {
			LockModel lck = (LockModel) lockTable.get(lckKeys[i]);
			if (!lck.getCodOperatore().equals(Cod_utente)) {
				if (lck.getEntity().equals(Entity) && lck.getIdEntity().equals(idEntity)) {
					return lck;
				}
			}
		}

		LockModel lck = new LockModel();
		lck.setCodOperatore(Cod_utente);
		lck.setIdEntity(idEntity);
		lck.setEntity(Entity);
		lockTable.put(SessionID, lck);
		return null;
	}

}