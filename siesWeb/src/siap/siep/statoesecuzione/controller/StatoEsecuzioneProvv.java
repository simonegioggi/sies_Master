package siap.siep.statoesecuzione.controller;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.statoesecuzione.model.EventoModel;
import siap.siep.statoesecuzione.model.EventoSorveglianzaModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

/**
 * Provvedimento Generico
 * @author Giselda De Vita
 *
 */
public class StatoEsecuzioneProvv extends StatoEsecuzioneElement
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public StatoEsecuzioneProvv()
	{
	}

	public StatoEsecuzioneProvv(StatoEsecuzioneElement aStat){	
		super(aStat);
	}


	/**
	 * elabora EventoModel
	 */
	public void elabora(siap.sico.evento.model.EventoModel aEvento)
	{
		boolean lSorveglianza = false;

		try{
			EventoModel lEvento = new EventoModel(aEvento);
			lEvento.setFamiglia("PROVV");

			lEvento.setDescrLuogoEmittente(null);
			lEvento.setDescrUfficioEmittente(null);
	
			lEvento.setLegge(aEvento.getLegge()); // Paolo Cherubini 16/06/2011
			

			//lEvento.setDescrizioneData("emesso in data");
			lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));
			if(lEvento.getCodTipoProvvedimento().equals("26")
					|| lEvento.getCodTipoProvvedimento().equals("12")
					|| lEvento.getCodTipoProvvedimento().equals("03")
					|| lEvento.getCodTipoProvvedimento().equals("31"))				
				lEvento.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F"));

			if(mHashEventiRiferimento!=null && aEvento.getEveIdEvento()!=null && mHashEventiRiferimento.containsKey(aEvento.getEveIdEvento()))
			{
				lSorveglianza = true;

				siap.sico.evento.model.EventoModel lEventoOrdinanza = 	(siap.sico.evento.model.EventoModel)mHashEventiRiferimento.get(aEvento.getEveIdEvento());

				EventoSorveglianzaModel lEveSorv = new EventoSorveglianzaModel(lEventoOrdinanza);

				if(lEveSorv.getCodTipoProvvedimento().equals("26")
						|| lEveSorv.getCodTipoProvvedimento().equals("12")
						|| lEveSorv.getCodTipoProvvedimento().equals("03"))	
					lEveSorv.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_F"));
				else
					lEveSorv.setDescrizioneData(mCostanti.getProperty("DATA_EMISSIONE_M"));

				lEvento.setEventoSorveglianza(lEveSorv);


				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
				siesLogger.info("* * * --> Addizionato MA e SORV" + lEvento);
			}
			else
			{//Non c'e' Ordinanza
				//	lEvento.setDescrTipoProvvedimento(lEvento.getDescrTipoProvvedimento() + " " + lEvento.getDescrMotivo());
			}		

			if(mHashPenaResidua.containsKey(aEvento.getIdEvento()) 
					// &&  !aEvento.getCodTipoProvvedimento().equals("12") Paolo Cherubini 13 Luglio 2011 segnalazione di Pina Marchese
			   )
			{
				PenaResiduaModel lPena = (PenaResiduaModel)mHashPenaResidua.get(aEvento.getIdEvento());
				lEvento.setPenaResidua(lPena);
//				flag ergastolo
				boolean flagErgastolo = false;

				//Valorizzo la Stringa Decorrenza pena
				if(lPena.getDataInizio()!=null){
					String lPenaDate = mCostanti.getProperty("PENA_DECORRENZA"); 
					lPenaDate += " " + DateUtils.getDateToString(lPena.getDataInizio(),"dd-MM-yyyy");
					//verifico che non si tratti di ergastolo
					if(lPena.getFlagErgastolo().equals("S")||lPena.getFlagErgastolo().equals("D")){
						lPenaDate += " " + mCostanti.getProperty("PENA_SCADENZA_ERGASTOLO");				
					}else if(lPena.getDataFine()!=null){
						lPenaDate += " " + mCostanti.getProperty("PENA_SCADENZA");
						lPenaDate += " " + DateUtils.getDateToString(lPena.getDataFine(),"dd-MM-yyyy");
					}
					lEvento.setStringaDecorrenzaPenaResidua(lPenaDate);
				}
				//Valorizzo la Stringa pena residua
				//solo se il flag per l'ergastolo = false
				if(!flagErgastolo){
					lPena.calcolaStringaReclusione();
					lPena.calcolaStringaArresto();
					BigDecimal tmpIndex = new BigDecimal(0);					
					String tempPenaRes = "";
					boolean flag_str = false;
					//RECLUSIONE 
					if(lPena.getStringaReclusione()!=null){
						flag_str = true;
						tempPenaRes += mCostanti.getProperty("PENA_DA_ESPIARE") + " ";
						tempPenaRes += mCostanti.getProperty("PENA_RECLUSIONE");
						tempPenaRes += " " + lPena.getStringaReclusione();
					}
					//	RECLUSIONE - MULTA
					if(lPena.getImportoMulta().compareTo(tmpIndex)>0){				
						if(!flag_str)tempPenaRes += " " + mCostanti.getProperty("PENA_DA_ESPIARE");
						flag_str = true;
						tempPenaRes += " " + mCostanti.getProperty("PENA_MULTA_EURO");
						tempPenaRes += " " + lPena.getImportoMulta();
					}					
					//ARRESTO 
					if(lPena.getStringaArresto()!=null){
						if(!flag_str)tempPenaRes += " " + mCostanti.getProperty("PENA_DA_ESPIARE");
						flag_str = true;
						tempPenaRes += " " + mCostanti.getProperty("PENA_ARRESTO");
						tempPenaRes += " " + lPena.getStringaArresto();
					}
					// ARRESTO - AMMENDA 
					if(lPena.getImportoAmmenda().compareTo(tmpIndex)>0){
						if(!flag_str)tempPenaRes += " " + mCostanti.getProperty("PENA_DA_ESPIARE");
						tempPenaRes += " " + mCostanti.getProperty("PENA_AMMENDA_EURO");
						tempPenaRes += " " + lPena.getImportoAmmenda();
					}

					lEvento.setStringaPenaResidua(tempPenaRes);
				}


			}

			//Provv indeterminato
			if(lSorveglianza && lEvento.getCodMotivo().equals("0000"))
				lEvento.setDescrTipoProvvedimento(lEvento.getDescrTipoProvvedimento() + " Provvedimento Indeterminato");

			// MEV_2023-33
			if (mHashRateizzazioni.containsKey(aEvento.getIdEvento())) {
			  lEvento.setListaRateizzazioni( (Vector <RateizzazionePPModel>) mHashRateizzazioni.get(aEvento.getIdEvento()));
			}
		  // MEV_2023-33

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