package siap.siep.statoesecuzione.controller;

import org.apache.log4j.Logger;

import siap.siep.statoesecuzione.model.EventoModel;
import f3b.log.LogF3B;

public class StatoEsecuzioneRich extends StatoEsecuzioneElement {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecuzioneRich() {
		// TODO Auto-generated constructor stub
	}

	public StatoEsecuzioneRich(StatoEsecuzioneElement aCopy) {
		super(aCopy);
		// TODO Auto-generated constructor stub
	}
	/**
	 * elabora EventoModel
	 */
	public void elabora(siap.sico.evento.model.EventoModel aEvento)
	{
	try{
		if(!aEvento.getCodMotivo().equals("0341"))
		{
			EventoModel lEvento = new EventoModel(aEvento);
			lEvento.setFamiglia("RICH");
			//lEvento.setDescrizioneData("redatto in data");
			lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));
			lEvento.setDescrLuogoEmittente(null);
			lEvento.setDescrUfficioEmittente(null);
			
			
			lEvento.setData(aEvento.getDataEmissione());
			
			this.mEventoStatoEsecuzione = lEvento;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("* * * --> Settato Evento" + lEvento);	
		}
		}
		catch(Exception ex)
		{
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore in StampaProperties",ex);
			ex.printStackTrace();
		}	
	}
}