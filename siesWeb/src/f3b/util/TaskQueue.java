package f3b.util;

import java.util.Hashtable;
import java.util.Timer;

import f3b.controller.GenericTask;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TaskQueue extends Hashtable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2308161984800436981L;

	private static TaskQueue mTaskQueue = null;

	private TaskQueue() {
		super();
	}

	/**
	 * Ritorna l'istanza come risorsa statica, della classe <code>PropertiesMgr</code>.
	 * <p>
	 * 
	 * @return l'istanza di <code>PropertiesMgr</code>.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	public static synchronized TaskQueue getInstance() /* throws F3BException */ {
		if (mTaskQueue == null) {
			mTaskQueue = new TaskQueue();
		}
		return mTaskQueue;
	}

	/**
	 * 
	 * <p>
	 * 
	 * @param aKey
	 * @param aTask
	 */
	public void putTask(String aKey, GenericTask aTask) {
		super.put(aKey, aTask);
	}

	/**
	 * 
	 * <p>
	 * 
	 * @param aKey
	 * @return
	 */
	public GenericTask getTask(String aKey) {
		return (GenericTask) super.get(aKey);
	}

	/**
	 * 
	 * <p>
	 * 
	 * @param aKey
	 * @return
	 */
	public long getScheduledExecutionTime(String aKey) {
		return getTask(aKey).scheduledExecutionTime();
	}

	/**
	 * 
	 * <p>
	 * 
	 * @param aKey
	 */
	public void schedule(String aKey, long aDelay, long aPeriod) {
		Timer lTimer = new Timer();
		lTimer.schedule(getTask(aKey), aDelay, aPeriod);
	}

	/**
	 * 
	 * <p>
	 * 
	 * @param aKey
	 * @return
	 */
	public boolean cancel(String aKey) {
		return getTask(aKey).cancel();
	}

	/**
	 * 
	 * <p>
	 * 
	 * @param aKey
	 */
	public void remove(String aKey) {
		super.remove(aKey);
	}

	/*
	 * private void init() { mTaskQueue = new(super()); } (
	 */

}