package f3b.controller;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;

//import oracle.jdbc.pool.OracleConnectionCacheImpl;
import oracle.jdbc.pool.OracleDataSource;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class F3bOracleDataSourceFactory implements ObjectFactory {

	public F3bOracleDataSourceFactory() {
	}

	public Object getObjectInstance(Object obj, Name name, Context context, Hashtable hashtable)
			throws Exception {
		Reference reference = (Reference) obj;
		Object obj1 = null;
		String s = reference.getClassName();

		// ======================================================================
		// Verifica il tipo di oggetto che gli viene passato, quello che deve
		// istanziare. Gli oggetti sono stati ridotti a 2.
		// - OracleDataSource (non gestisce i pool)
		// - OracleConnectionCacheImpl (per la gestione dei pool)
		// ======================================================================
		if (s.equals("oracle.jdbc.pool.OracleDataSource")) {
			obj1 = new OracleDataSource();
			// }else if(s.equals("oracle.jdbc.pool.OracleConnectionCacheImpl")){
			// obj1 = new OracleConnectionCacheImpl();
		} else {
			throw new F3BException("F3bOracleDataSourceFactory: impossibile istanziare l'oggetto " + s);
		}

		// ======================================================================
		// Dopo aver istanziato l'oggetto vengono inizializzati i parametri
		// di connessione (comuni in quanto legati alla classe OracleDataSource
		// estesa da entrambi gli oggetti)
		// ======================================================================
		if (obj1 != null) {
			StringRefAddr stringrefaddr1 = null;
			if ((stringrefaddr1 = (StringRefAddr) reference.get("url")) != null)
				((OracleDataSource) (obj1)).setURL((String) stringrefaddr1.getContent());
			if ((stringrefaddr1 = (StringRefAddr) reference.get("userName")) != null
					|| (stringrefaddr1 = (StringRefAddr) reference.get("u")) != null
					|| (stringrefaddr1 = (StringRefAddr) reference.get("user")) != null)
				((OracleDataSource) (obj1)).setUser((String) stringrefaddr1.getContent());
			if ((stringrefaddr1 = (StringRefAddr) reference.get("passWord")) != null
					|| (stringrefaddr1 = (StringRefAddr) reference.get("password")) != null)
				((OracleDataSource) (obj1)).setPassword((String) stringrefaddr1.getContent());
			if ((stringrefaddr1 = (StringRefAddr) reference.get("description")) != null
					|| (stringrefaddr1 = (StringRefAddr) reference.get("describe")) != null)
				((OracleDataSource) (obj1)).setDescription((String) stringrefaddr1.getContent());
			if ((stringrefaddr1 = (StringRefAddr) reference.get("driverType")) != null
					|| (stringrefaddr1 = (StringRefAddr) reference.get("driver")) != null)
				((OracleDataSource) (obj1)).setDriverType((String) stringrefaddr1.getContent());
			if ((stringrefaddr1 = (StringRefAddr) reference.get("serverName")) != null
					|| (stringrefaddr1 = (StringRefAddr) reference.get("host")) != null)
				((OracleDataSource) (obj1)).setServerName((String) stringrefaddr1.getContent());
			if ((stringrefaddr1 = (StringRefAddr) reference.get("databaseName")) != null
					|| (stringrefaddr1 = (StringRefAddr) reference.get("sid")) != null)
				((OracleDataSource) (obj1)).setDatabaseName((String) stringrefaddr1.getContent());
			if ((stringrefaddr1 = (StringRefAddr) reference.get("networkProtocol")) != null
					|| (stringrefaddr1 = (StringRefAddr) reference.get("protocol")) != null)
				((OracleDataSource) (obj1)).setNetworkProtocol((String) stringrefaddr1.getContent());
			if ((stringrefaddr1 = (StringRefAddr) reference.get("portNumber")) != null
					|| (stringrefaddr1 = (StringRefAddr) reference.get("port")) != null) {
				String s6 = (String) stringrefaddr1.getContent();
				((OracleDataSource) (obj1)).setPortNumber(Integer.parseInt(s6));
			}
			if ((stringrefaddr1 = (StringRefAddr) reference.get("tnsentryname")) != null
					|| (stringrefaddr1 = (StringRefAddr) reference.get("tns")) != null)
				((OracleDataSource) (obj1)).setTNSEntryName((String) stringrefaddr1.getContent());

		}

		// ======================================================================
		// Se ho istanziato l'oggetto che gestisce il pool, setto i relativi
		// parametri (se passati in input)
		// ======================================================================
		/*
		 * if(obj1 instanceof OracleConnectionCacheImpl) { StringRefAddr stringrefaddr = null; String minLimit
		 * = null; String maxLimit = null; String cacheScheme = null;
		 * 
		 * if((stringrefaddr = (StringRefAddr)reference.get("maxLimit")) != null) { maxLimit =
		 * (String)stringrefaddr.getContent(); } if((stringrefaddr = (StringRefAddr)reference.get("minLimit"))
		 * != null) { minLimit = (String)stringrefaddr.getContent(); } if((stringrefaddr =
		 * (StringRefAddr)reference.get("cacheScheme")) != null) { cacheScheme =
		 * (String)stringrefaddr.getContent(); }
		 * 
		 * //================================================================== // Controllo che i parametri
		 * in input siano coerenti //================================================================== if (
		 * minLimit!=null && Integer.parseInt(minLimit)<0 ){ throw new
		 * Exception("F3bOracleDataSourceFactory: parametri di inizializzazione non validi: minLimit deve essere > 0 "
		 * ); } if ( maxLimit!=null && Integer.parseInt(maxLimit)<=0 ){ throw new
		 * Exception("F3bOracleDataSourceFactory: parametri di inizializzazione non validi: maxLimit deve essere > 0 "
		 * ); } if ( minLimit!=null && maxLimit!=null ){ if (
		 * Integer.parseInt(minLimit)>Integer.parseInt(maxLimit) ){ throw new
		 * Exception("F3bOracleDataSourceFactory: parametri di inizializzazione non coerenti minLimit>maxLimit"
		 * ); } }
		 * 
		 * //================================================================== // Setto i parametri
		 * coerentemente con i valori di default per // evitare problemi sui controlli dei metodi setMinLimit
		 * e setMaxLimit //================================================================== if (
		 * maxLimit!=null ){ ((OracleConnectionCacheImpl)obj1).setMaxLimit(Integer.parseInt(maxLimit)); }
		 * 
		 * if (minLimit!=null) { if (
		 * Integer.parseInt(minLimit)>((OracleConnectionCacheImpl)obj1).getMaxLimit() ){ // minLimit >
		 * defMaxLimit ((OracleConnectionCacheImpl)obj1).setMaxLimit(Integer.parseInt(minLimit));
		 * ((OracleConnectionCacheImpl)obj1).setMinLimit(Integer.parseInt(minLimit)); } else {
		 * ((OracleConnectionCacheImpl)obj1).setMinLimit(Integer.parseInt(minLimit)); } }
		 * 
		 * if (cacheScheme!=null && Integer.parseInt(cacheScheme)>0 && Integer.parseInt(cacheScheme)<4){
		 * ((OracleConnectionCacheImpl)obj1).setCacheScheme(Integer.parseInt(cacheScheme)); } else throw new
		 * Exception("F3bOracleDataSourceFactory: parametri di inizializzazione non coerenti: cacheScheme ("
		 * +cacheScheme+") non valido, valori ammessi (1,2,3) ");
		 * 
		 * }
		 * 
		 */
		return obj1;
	}

}