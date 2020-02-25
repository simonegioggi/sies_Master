
package siap.siep.statoesecuzione.controller;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.siep.statoesecuzione.model.EventoModel;
import siap.siep.verbale.dao.VerbaleSqlDAO;
import siap.siep.verbale.model.VerbaleModel;
import f3b.log.LogF3B;

/**
 * StatoEsecuzioneVerbale - Realizza il pezzo di esecuzione del Verbale 
 * @author Giselda De Vita
 *
 */
public class StatoEsecuzioneVerbale extends StatoEsecuzioneElement 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecuzioneVerbale(StatoEsecuzioneElement aStat){	
		super(aStat);
	}

	/**
	 * elabora EventoModel
	 */
	public void elabora(siap.sico.evento.model.EventoModel aEvento)
	{
		try{
			EventoModel lEvento = new EventoModel(aEvento);
			String lTipoProvv = lEvento.getCodTipoProvvedimento();
			
			lEvento.setFamiglia("VERB");
			//lEvento.setDescrizioneData("redatto in data");
			lEvento.setDescrizioneData(mCostanti.getProperty("VERBALE_REDATTO_IN_DATA"));
			lEvento.setDescrLuogoEmittente(null);
			lEvento.setDescrUfficioEmittente(null);


	/*		if (   lTipoProvv.equals("16")
				|| lTipoProvv.equals("17")
				|| lTipoProvv.equals("18")
				|| (lTipoProvv.equals("26")&&(lEvento.getCodMotivo().equals("0244"))))
			{*/
				VerbaleModel lVerb = getVerbale(lEvento.getIdEvento());
				if(lVerb!=null)
				{
					lEvento.setDescrUfficioEmittente(lVerb.getDescrTipoUfficioFirmatario());
					lEvento.setDescrLuogoEmittente(lVerb.getDescrLuogoUfficioFirmatario());
					
					if (!lTipoProvv.equals("17"))
					{
						lEvento.setDataArresto(lVerb.getDataEmissione());
						if(lTipoProvv.equals("16"))
							lEvento.setStringDataArresto(mCostanti.getProperty("DATA_ARRESTO"));
						else
							lEvento.setStringDataArresto(mCostanti.getProperty("DATA_SOTTOSCRIZIONE"));
						
					}
				}
			//}


			//if(lEvento.getCodMotivo().equals("0244"))
	    if(lEvento.getCodTipoProvvedimento().equals("26"))
				lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F"));

			lEvento.setData(aEvento.getDataEmissione());
			lEvento.setLegge(aEvento.getLegge()); // Paolo Cherubini 28/06/2011
			
			        
			this.mEventoStatoEsecuzione = lEvento;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("* * * --> Settato Evento Verbale" + lEvento);	
		}
		catch(Exception ex)
		{
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore in StampaProperties",ex);
			ex.printStackTrace();
		}	
	}



	/**
	 * get Verbale
	 * @param 
	 */
	VerbaleModel getVerbale(BigDecimal aIdEveSorv)
	{
		Connection lConn = null;
		VerbaleSqlDAO lVerbSqlDao = null;
		VerbaleModel lVerb = null;

		try{
			lConn = getDBConnection();
			lVerbSqlDao = new VerbaleSqlDAO(lConn);
			lVerbSqlDao.ricercaVerbaleByIdEvento(aIdEveSorv);
			lVerb = (VerbaleModel) lVerbSqlDao.getModelByKey();
		}
		catch(Exception ex)
		{
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore in getVerbale");
			ex.printStackTrace();
		}
		finally
		{
			try{
				cleanup(lVerbSqlDao);
				cleanup(lConn);
			}catch(Exception eee ){}

		}
		return lVerb;
	}

}