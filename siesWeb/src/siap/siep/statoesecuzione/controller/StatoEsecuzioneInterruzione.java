package siap.siep.statoesecuzione.controller;

//import java.sql.Connection;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

//import f3b.log.LogF3B;
//import f3b.util.F3BException;
//import f3b.controller.GenericController;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.statoesecuzione.model.EventoModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

//import siap.siep.sospensione.dao.SospensioneSqlDAO;
//import siap.siep.sospensione.model.SospensioneModel;

/**
 * StatoEsecuzioneInterruzione - 
 * @author Giselda De Vita
 *
 */
public class StatoEsecuzioneInterruzione extends StatoEsecuzioneElement 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public StatoEsecuzioneInterruzione(){	
		/*this.mHashEventiRiferimento = StatoEsecuzioneElement.getInstance().mHashEventiRiferimento;
		this.mHashPenaResidua = StatoEsecuzioneElement.getInstance().mHashPenaResidua;
		this.mHashMisure = StatoEsecuzioneElement.getInstance().mHashMisure;*/
	}
	
	public StatoEsecuzioneInterruzione(StatoEsecuzioneElement aStat){	
		super(aStat);
	}

	public void elabora(siap.sico.evento.model.EventoModel aEvento){
		try{
			EventoModel lEvento = new EventoModel(aEvento);
			lEvento.setFamiglia("INTERR");
			//lEvento.setDescrMotivo(null);
			lEvento.setDescrLuogoEmittente(null);
			lEvento.setDescrUfficioEmittente(null);
			
			// Paolo Cherubini 13 Luglio 2011 aggiunto su segnalazione di Pina Marchese
			if(mHashPenaResidua.containsKey(aEvento.getIdEvento()) )
			{		
				PenaResiduaModel lPena = (PenaResiduaModel)mHashPenaResidua.get(aEvento.getIdEvento());
				lEvento.setPenaResidua(lPena);
	//			flag ergastolo
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
			// fine Paolo

		
			
			//descrizione_data
			lEvento.setDescrizioneData(mCostanti.getProperty("INTERRUZIONE_IN_DATA"));			
			
			//data
			lEvento.setData(aEvento.getDataEmissione());
			lEvento.setLegge(aEvento.getLegge()); // Paolo Cherubini 16/06/2011
			
			//Lettura della spospensione
			StatoEsecuzioneController lController = new StatoEsecuzioneController();
			Date lDataSospensione = null;
			try{
				lDataSospensione = lController.getDataSospensione(aEvento.getFasSieIdFascicoloSiep());
			}
			catch (Exception sqe)
			{
				sqe.printStackTrace();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("StatoEsecuzioneInterruzione.appendStatoEsecuzione: ", sqe);	      
			}		
			
			lEvento.setData(lDataSospensione);
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
	}
}