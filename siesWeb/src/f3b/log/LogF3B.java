package f3b.log;

/**
 * L'interfaccia definisce le costanti identificati i logger definiti in log4j.properties
 * 
 * @author [FT]
 */
public interface LogF3B {

	// [FT] - 04/08/2016 - MAC_LOG - Convertita da classe a interfaccia che espone solo
	// costanti con i nomi dei logger definiti in log4j.properties. Le classi che definiscono
	// il logger (log4j) per identificare il logger desiderato, utilizzano queste costanti
	// anziché scolpire il nome internamente alle singole classi.
	public final static String SIES_LOG = "SIESLog";
	public final static String STAMPA_LOG = "StampaLog";
	// MEV AVVOCATURA: aggiunto log x ws
	public final static String AVVOCATURA_LOG = "AvvocaturaLog";
	// mev problema code per sies 11.3: aggiunto un log per le code jms
	public final static String JMS_LOG = "CodeJmsLog";


}
