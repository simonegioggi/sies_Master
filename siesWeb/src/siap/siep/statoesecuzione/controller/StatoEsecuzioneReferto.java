/**
 * 
 */
package siap.siep.statoesecuzione.controller;

import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.siep.refertoscarcerazione.dao.RefertoScarcerazioneSqlDAO;
import siap.siep.refertoscarcerazione.model.RefertoScarcerazioneModel;
import siap.siep.statoesecuzione.model.EventoModel;
import f3b.log.LogF3B;
import f3b.util.StringUtils;


/**
 * @author Giselda De Vita
 *
 */
public class StatoEsecuzioneReferto extends StatoEsecuzioneElement {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * 
	 */
	public StatoEsecuzioneReferto() {

	}

	/**
	 * @param aCopy
	 */
	public StatoEsecuzioneReferto(StatoEsecuzioneElement aCopy) {
		super(aCopy);

	}

	/**
	 * elabora EventoModel
	 */
	public void elabora(siap.sico.evento.model.EventoModel aEvento)
	{
		Connection lConn = null;
		RefertoScarcerazioneSqlDAO lRefDao = null;

		try{
			EventoModel lEvento = new EventoModel(aEvento);
			lEvento.setFamiglia("REFERTO");
			//lEvento.setDescrizioneData("redatto in data");
			//lEvento.setDescrizioneData(mCostanti.getProperty("VERBALE_REDATTO_IN_DATA"));
			//lEvento.setData(aEvento.getDataEmissione());

			lEvento.setDescrTipoProvvedimento(aEvento.getDescrTipoProvvedimento() + " " + aEvento.getDescrMotivo());
			lEvento.setDescrMotivo(null);
			lEvento.setData(null);
			lEvento.setDataEmissione(null);

			/*
			 * Il referto proviene sempre da un'autorità esterna che in questo momento
			 * non so dove andare a ricercare all'interno del DB....
			 * la query dovrebbe essere la seguente:
			 * select * from notifica where eve_id_evento=(select id_evento 
			 * from evento where eve_id_evento=aEvento.getIdEvento()) and cod_tipo_notifica ='E'
			 * Il ritorno potrebbe essere o un istituto di detenzione o un'autorità
			 * esterna. 
			 */
			RefertoScarcerazioneModel lRef = new RefertoScarcerazioneModel();
			lConn = getDBConnection();
			lRefDao = new RefertoScarcerazioneSqlDAO(lConn);
			lRefDao.ricercaRefertoScarcerazioneByEveIdEvento(lEvento.getIdEvento());
			lRef = (RefertoScarcerazioneModel)lRefDao.getModelByKey();

			if(lRef.getIstitutoDetenzione()!=null)
			{
				lEvento.setDescrLuogoEmittente(StringUtils.capitalize(lRef.getIstitutoDetenzione().getDescrComune()));
				lEvento.setDescrUfficioEmittente(lRef.getIstitutoDetenzione().getDescrTipoIstituto());
				lEvento.setFraseUfficio(mCostanti.getProperty("PREPOSIZIONE_DA"));
			}

			this.mEventoStatoEsecuzione = lEvento;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("* * * --> Settato Evento" + lEvento);	
		}
		catch(Exception ex)
		{
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore in StampaProperties",ex);
			ex.printStackTrace();
		}	
		finally
		{
			try{
				cleanup(lConn);
				cleanup(lRefDao);
			}catch(Exception ex)
			{
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Errore in "+this.getClass().getName()+ " elabora --->",ex);
			}
		}
	}

}