package siap.siep.statoesecuzione.controller;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.statoesecuzione.model.EventoModel;
import siap.siep.statoesecuzione.model.EventoSorveglianzaModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

/**
 * Realizza il blocco del differimento
 * @author Giselda De Vita
 *
 */
public class StatoEsecuzioneDifferimento extends StatoEsecuzioneElement 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecuzioneDifferimento()
	{
	}
	
	public StatoEsecuzioneDifferimento(StatoEsecuzioneElement aStat){	
		        super(aStat);
			}


	/**
	 * Elabora l'elemento dello Stato di Esecuzione 
	 */
	public void elabora(siap.sico.evento.model.EventoModel aEvento)	
	{
		try{
			EventoModel lEvento = new EventoModel(aEvento);		
			lEvento.setFamiglia("DIFF");
			
			if(aEvento.getCodTipoProvvedimento().equals("12")||
					aEvento.getCodTipoProvvedimento().equals("26")	)
				lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F"));
			else
				lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));


			lEvento.setDescrLuogoEmittente(null);
			lEvento.setDescrUfficioEmittente(null);
			
			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			//siesLogger.info("aEvento = " + aEvento);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("aEvento.getEveIdEvento() = " + aEvento.getEveIdEvento());

			if(aEvento.getEveIdEvento()!=null && mHashEventiRiferimento.containsKey(aEvento.getEveIdEvento()))
			{
				siap.sico.evento.model.EventoModel lEventoOrdinanza = 	(siap.sico.evento.model.EventoModel)mHashEventiRiferimento.get(aEvento.getEveIdEvento());

				EventoSorveglianzaModel lEveSorv = new EventoSorveglianzaModel(lEventoOrdinanza);

				if(lEveSorv.getCodTipoProvvedimento().equals("03"))
					lEveSorv.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F"));
				else
					lEveSorv.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));

				StatoEsecuzioneUtilController lUtil = new StatoEsecuzioneUtilController();
				lUtil.getAnnoNumeroSius(lEveSorv);
				
				lEvento.setEventoSorveglianza(lEveSorv);

				if(mHashMisure.containsKey(lEveSorv.getIdEvento()))
				{
					MisuraAlternativaModel lMisure = (MisuraAlternativaModel)mHashMisure.get(lEveSorv.getIdEvento());
					lEvento.setMisuraAlternativa(lMisure);	
					
					String lMisuraDate = "";
					//Valorizzo la Stringa MisuraAlternativaDate (mStringaMisuraDate)
					if(lMisure.getDataFineMisura()!=null){	
						lMisuraDate = mCostanti.getProperty("FINO_AL"); 
						//lMisuraDate += " " + mCostanti.getProperty("MISURA_FINE");
						lMisuraDate += " " + DateUtils.getDateToString(lMisure.getDataFineMisura(),"dd-MM-yyyy");
					}
					
					if(lMisure.getFlagDecisioneTribunale()!=null && lMisure.getFlagDecisioneTribunale().equals("S")){
						lMisuraDate = mCostanti.getProperty("FINO_ALLA_DEC_TRIB"); 
						//lMisuraDate += "la decisione del Tirbunale di Sorveglianza " + 
					}

					lEvento.setStringaMisuraDate(lMisuraDate);

				}
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("* * * --> Addizionato MA e SORV " + lEvento);

			}

			if(mHashPenaResidua.containsKey(aEvento.getIdEvento()))
			{
				PenaResiduaModel lPena = (PenaResiduaModel)mHashPenaResidua.get(aEvento.getIdEvento());
				lEvento.setPenaResidua(lPena);
				//flag ergastolo
				boolean flagErgastolo = false;

				//Valorizzo la Stringa Decorrenza pena
				if(lPena.getDataInizio()!=null){
					String lPenaDate = mCostanti.getProperty("PENA_DECORRENZA"); 
					lPenaDate += " " + DateUtils.getDateToString(lPena.getDataInizio(),"dd-MM-yyyy");
					//verifico che non si tratti di ergastolo
					if(lPena.getFlagErgastolo().equals("S")||lPena.getFlagErgastolo().equals("D")){
						lPenaDate += mCostanti.getProperty("PENA_SCADENZA_ERGASTOLO");				
					}else if(lPena.getDataFine()!=null){
						lPenaDate += " " + mCostanti.getProperty("PENA_SCADENZA");
						lPenaDate += " " + DateUtils.getDateToString(lPena.getDataFine(),"dd-MM-yyyy");
					}
					lEvento.setStringaDecorrenzaPenaResidua(lPenaDate);

					//Valorizzo la Stringa pena residua
					//solo se il flag per l'ergastolo = false
					if(!flagErgastolo){
						lPena.calcolaStringaReclusione();
						lPena.calcolaStringaArresto();
						BigDecimal tmpIndex = new BigDecimal(0);					
						String tempPenaRes = "";
						boolean flag_str = false;
						//RECLUSIONE - MULTA
						if(lPena.getStringaReclusione()!=null){
							flag_str = true;
							tempPenaRes += mCostanti.getProperty("PENA_DA_ESPIARE") + " ";
							tempPenaRes += mCostanti.getProperty("PENA_RECLUSIONE");
							tempPenaRes += " " + lPena.getStringaReclusione();
						}
						if(lPena.getImportoMulta().compareTo(tmpIndex)>0){						
							tempPenaRes += " " + mCostanti.getProperty("PENA_MULTA_EURO");
							tempPenaRes += " " + lPena.getImportoMulta();
						}					
						//ARRESTO - AMMENDA 
						if(lPena.getStringaArresto()!=null){
							if(!flag_str)tempPenaRes += " " + mCostanti.getProperty("PENA_DA_ESPIARE");
							tempPenaRes += " " + mCostanti.getProperty("PENA_ARRESTO");
							tempPenaRes += " " + lPena.getStringaArresto();
						}
						if(lPena.getImportoAmmenda().compareTo(tmpIndex)>0){
							tempPenaRes += " " + mCostanti.getProperty("PENA_AMMENDA_EURO");
							tempPenaRes += " " + lPena.getImportoAmmenda();
						}

						lEvento.setStringaPenaResidua(tempPenaRes);
					}
				}
			}			
			this.mEventoStatoEsecuzione = lEvento;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.info("* * * --> Settato Evento" + lEvento);		 

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}