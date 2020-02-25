package siap.siep.statoesecuzione.controller;


import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.statoesecuzione.model.EventoModel;
import siap.siep.statoesecuzione.model.EventoSorveglianzaModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;


public class StatoEsecuzioneMAR extends StatoEsecuzioneElement 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecuzioneMAR()
	{
	}
	
	public StatoEsecuzioneMAR(StatoEsecuzioneElement aStat){	
        super(aStat);
	}

	/**
	 * Elabora l'elemento dello Stato di Esecuzione 
	 */
	public void elabora(siap.sico.evento.model.EventoModel aEvento)
	{
		try{
			EventoModel lEvento = new EventoModel(aEvento);
			lEvento.setFamiglia("MAR");
			//lEvento.setDescrizioneData("emesso in data");
			lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));
			if(lEvento.getCodTipoProvvedimento().equals("26")
					|| lEvento.getCodTipoProvvedimento().equals("12")
					|| lEvento.getCodTipoProvvedimento().equals("03"))
					lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F"));
			
			lEvento.setData(aEvento.getDataEmissione());
			//tags per la composizione delle stringhe nel template
			lEvento.setStringaRevocaDal(mCostanti.getProperty("REVOCA_DAL"));
			
			lEvento.setDescrLuogoEmittente(null);
			lEvento.setDescrUfficioEmittente(null);
			
			
			//Ricerco il decreto o l'ordinanza legata alla MA in questione
			if(mHashEventiRiferimento.containsKey(aEvento.getEveIdEvento()))
			{
				siap.sico.evento.model.EventoModel lEventoOrdinanza = 	(siap.sico.evento.model.EventoModel)mHashEventiRiferimento.get(aEvento.getEveIdEvento());

				EventoSorveglianzaModel lEveSorv = new EventoSorveglianzaModel(lEventoOrdinanza);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("TROVATO SIUS LEGATO A MA = " + lEveSorv);
				if(lEveSorv.getCodTipoProvvedimento().equals("26")
						|| lEveSorv.getCodTipoProvvedimento().equals("12")
						|| lEveSorv.getCodTipoProvvedimento().equals("03"))
					lEveSorv.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F"));
				else
					lEveSorv.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));

				lEvento.setEventoSorveglianza(lEveSorv);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.info("TROVATO SIUS LEGATO A MA = " + lEvento.getEventoSorveglianza());			

				if(mHashMisure.containsKey(lEveSorv.getIdEvento()))
				{
					MisuraAlternativaModel lMisure = (MisuraAlternativaModel)mHashMisure.get(lEveSorv.getIdEvento());
					lEvento.setMisuraAlternativa(lMisure);	
	
					//Valorizzo la Stringa MisuraAlternativaDate (mStringaMisuraDate)					 
					//String lMisuraDate = mCostanti.getProperty("REVOCA_DAL");
					//lMisuraDate += DateUtils.getDateToString(lMisure.getDataInizioMisura(),"dd-MM-yyyy");					

					//lEvento.setStringaMisuraDate(lMisuraDate);
					lEveSorv.setAnnoRegistro(lMisure.getAnnoRegistro());
					lEveSorv.setNumeroRegistro(lMisure.getNumeroRegistro());
	
					// Paolo Cherubini 22/06/2011 
					// aggiungo la data della revoca direttamente nella stringa
					// e la toglo dall'import statoesecuzione.rtf poichè era erroneamente indicata
					// dataemissione
					lEvento.setStringaRevocaDal(lEvento.getStringaRevocaDal() + " " +
							DateUtils.getDateToString(lMisure.getDataInizioRevoca(),"dd-MM-yyyy"));			
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.info("* * * --> Addizionato getStringaRevocaDal" + lEvento.getStringaRevocaDal());
					
				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.info("* * * --> Addizionato MA e SORV" + lEvento);

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
						flagErgastolo = true;
					}else if(lPena.getDataFine()!=null){
						lPenaDate += " " + mCostanti.getProperty("PENA_SCADENZA");
						lPenaDate += " " + DateUtils.getDateToString(lPena.getDataFine(),"dd-MM-yyyy");
					}					
					lEvento.setStringaDecorrenzaPenaResidua(lPenaDate);					
				}
				
				//Valorizzo la Stringa pena residua rideterminata
				//solo se il flag per l'ergastolo = false
				if(!flagErgastolo){
					lPena.calcolaStringaReclusione();
					lPena.calcolaStringaArresto();
					BigDecimal tmpIndex = new BigDecimal(0);					
					String tempPenaRes = "";
					boolean flag_str = false;
					//RECLUSIONE - MULTA
					if(lPena.getStringaReclusioneResidua()!=null){
						flag_str = true;
						tempPenaRes += mCostanti.getProperty("PENA_RIDETERMINATA_DA_ESPIARE") + " ";
						tempPenaRes += mCostanti.getProperty("PENA_RECLUSIONE");
						tempPenaRes += " " + lPena.getStringaReclusioneResidua();
					}
					if(lPena.getImportoMulta().compareTo(tmpIndex)>0){						
						tempPenaRes += " " + mCostanti.getProperty("PENA_MULTA_EURO");
						tempPenaRes += " " + lPena.getImportoMulta();
					}					
					//ARRESTO - AMMENDA 
					if(lPena.getStringaArrestoResidua()!=null){
						if(!flag_str)tempPenaRes += " " + mCostanti.getProperty("PENA_RIDETERMINATA_DA_ESPIARE");
						tempPenaRes += " " + mCostanti.getProperty("PENA_ARRESTO");
						tempPenaRes += " " + lPena.getStringaArrestoResidua();
					}
					if(lPena.getImportoAmmenda().compareTo(tmpIndex)>0){
						tempPenaRes += " " + mCostanti.getProperty("PENA_AMMENDA_EURO");
						tempPenaRes += " " + lPena.getImportoAmmenda();
					}

					lEvento.setStringaPenaResidua(tempPenaRes);
				}

			}

			this.mEventoStatoEsecuzione = lEvento;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.info("AAAA* * * --> Settato Evento" + lEvento);		 
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}