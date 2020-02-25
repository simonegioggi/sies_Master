package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.siep.modulocumulo.controller.IMisuraCautelareCumulo;
import siap.siep.modulocumulo.model.MisuraCautelareCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActRicercaMisureCautelariCumulo extends ActionModuloCumulo implements ICostantiMisuraCautelareCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException 
  {    
    //==========================================================================
    // Recupero i dati dell'Istruttoria, Titolo da passare alla form
    // di DettaglioTitoloCumulato.jsp
    //==========================================================================
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();
    
    //==========================================================================
    // Effettua la ricerca delle Misure Sicurezza Presenti Associate al Titolo
    //==========================================================================
    BigDecimal lIdTitolo = getRequestBigDecimalParameter (ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

    //==========================================================================
    // Istanzio il controller ed effettuo la ricerca delle MC Ordinate per data 
    // Inizio Misura
    //==========================================================================
    IMisuraCautelareCumulo lCtrl = SIEPLookupRemote.getMisuraCautelareCumuloRemote();
    Vector <MisuraCautelareCumuloModel> lVect = lCtrl.ExRicercaMisureCautelariCumuloByIdTitolo (lIdTitolo);

    
    this.elaboraMisureCautelari (lVect);
    
    // solo per debug
    for (int i=0;i<lVect.size(); i++)
    {
      MisuraCautelareCumuloModel lMisuraCautelare = lVect.elementAt(i);
      String str = "";
      str += lMisuraCautelare.getIdMisuraCautelareCumulo();
      str += " - "+DateUtils.getDateToString (lMisuraCautelare.getDataInizio(),"dd/MM/yyyy");
      str += " - "+DateUtils.getDateToString (lMisuraCautelare.getDataFine(),"dd/MM/yyyy");
      str += " - "+lMisuraCautelare.getProgressivoContinuazione();
      str += " - "+lMisuraCautelare.getIsInContinuazione();
      str += " - Anni "+lMisuraCautelare.getNumAnniContinuativi()
              +" Mesi "+lMisuraCautelare.getNumMesiContinuativi()
              +" Giorni "+lMisuraCautelare.getNumGiorniContinuativi();
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("Misure Cautelari: "+str);
      
    }
    
    
    setRequestAttribute("ListaMisureCautelari", lVect);

    return PG_ELENCO_MISURE_CAUTELARI_CUMULO;
  }
  
  /**
   * Effettua l'elaborazione della MS verificando quali possono essere considerate in continuazione
   * tra loro e ricalcolando il totale per quelle in continuazione.
   * 
   * n.b. si assume che le misure vengano passate nel vettore di ingresso ordinate per 
   *      data inizio crescente.
   *      Le misure di tipo CL (Messa alla prova) non vengono considerate continuative
   *      con nessuna altra misura. Ogni periodo è considerato singolarmenente.
   *      Vanno gestiti anche i casi di dati scritti in modo errato: 
   *      
   *      Il metodo verifica anche la congruenza dei quantum a sistema con i 
   *      quantum calcolati con la routine SIEP. Dati migrati potrebbero non 
   *      riportare dati congruenti.
   *      
   *      Il metodo verifica anche i dati iscritti in maniera errata:
   *      es: record senza data inizio, record senza quantum
   *      
   *      
   * @param aElencoMisure
   * @return
   * @throws F3BException
   */
  private Vector <MisuraCautelareCumuloModel> elaboraMisureCautelari (Vector <MisuraCautelareCumuloModel> aElencoMisure) throws F3BException
  {
    MisuraCautelareCumuloModel lMisuraCautelarePrecedente = null;
    
    int progrContinuazione = 0;
    
    CalendarModel lCalendarModelTot = new CalendarModel();
    CalendarUtil lCalendarUtil = new CalendarUtil();
    
    int indicePrimoRecordContinuativo = -1;
    
    for (int i=0;i<aElencoMisure.size(); i++)
    {
      MisuraCautelareCumuloModel lMisuraCautelareCorrente = aElencoMisure.elementAt(i);
      
      if (i==0) {
        // il primo di default a false, verra evetualmente aggiornato dopo a true
        lMisuraCautelareCorrente.setIsInContinuazione (false);
        lMisuraCautelareCorrente.setProgressivoContinuazione (progrContinuazione);
        
        lCalendarModelTot = new CalendarModel();
        lCalendarModelTot.setDataInizio (lMisuraCautelareCorrente.getDataInizio());
        lCalendarModelTot.setDataFine   (lMisuraCautelareCorrente.getDataFine());
        
      }
      else {
        Date lDataInizioMisuraCorrente = lMisuraCautelareCorrente.getDataInizio();
        Date lDataFineMisuraPrecedente = lMisuraCautelarePrecedente.getDataFine();
        
        // La Messa Alla Prova (CO) non viene considerata una Misura Continuativa
        // con altre tipologie di misure dato che i quantum calcolati sono divisi per 3
        // e quindi non ha senso fare il calcolo su un periodo 'Misto'
        if (   !"CL".equals (lMisuraCautelareCorrente.getCodTipoMisura())
            && !"CL".equals (lMisuraCautelarePrecedente.getCodTipoMisura()) 
&& lDataFineMisuraPrecedente!=null
            && (   DateUtils.isEquals      (lDataFineMisuraPrecedente, lDataInizioMisuraCorrente)
                || DateUtils.getIntervallo (lDataFineMisuraPrecedente, lDataInizioMisuraCorrente)==1
               )
           )
        {
          // I due periodi sono continuativi
          if ( !lMisuraCautelarePrecedente.getIsInContinuazione()){
            // se il record precedente era il primo della serie, prendo nota del suo
            // indice per poter poi inserire il record totale in testa alla
            // sequenza di record continuativi
            indicePrimoRecordContinuativo = i-1;
          }
          
          // Marco la precedente come continuativo (con la corrente)
          lMisuraCautelarePrecedente.setIsInContinuazione (true);
          
          // Marco la corrente come continuativa e valorizzo il progrContinuazione
          lMisuraCautelareCorrente.setIsInContinuazione (true);
          lMisuraCautelareCorrente.setProgressivoContinuazione (lMisuraCautelarePrecedente.getProgressivoContinuazione());
        
          // Aggiorno data fine del periodo continuativo
          lCalendarModelTot.setDataFine (lMisuraCautelareCorrente.getDataFine());
          
          // Se ultimo record e in continuazioe devo calcolare qui il totale
          if (i==aElencoMisure.size()-1){
            CalendarModel lCalendarModelRicalcolo = lCalendarUtil.CalcolaNumGiorniMesiAnni (lCalendarModelTot);
            lCalendarModelRicalcolo = lCalendarUtil.ricalcolaGAM (lCalendarModelRicalcolo);
            
//            lMisuraCautelareCorrente.setNumAnniContinuativi   (new BigDecimal (lCalendarModelRicalcolo.getNumAnni()));
//            lMisuraCautelareCorrente.setNumMesiContinuativi   (new BigDecimal (lCalendarModelRicalcolo.getNumMesi()));
//            lMisuraCautelareCorrente.setNumGiorniContinuativi (new BigDecimal (lCalendarModelRicalcolo.getNumGiorni()));
          
            // In alternativa aggiungo un record totale
            MisuraCautelareCumuloModel lMisuraAggragata = new MisuraCautelareCumuloModel();
            
            lMisuraAggragata.setDescrTipoMisura ("Periodo continuativo sofferto in misure di natura differente:");
            lMisuraAggragata.setDataInizio (lCalendarModelTot.getDataInizio());
            lMisuraAggragata.setDataFine   (lCalendarModelTot.getDataFine());
            
            if (lMisuraAggragata.getDataFine()!=null) {
              lMisuraAggragata.setNumAnniContinuativi   (new BigDecimal (lCalendarModelRicalcolo.getNumAnni()));
              lMisuraAggragata.setNumMesiContinuativi   (new BigDecimal (lCalendarModelRicalcolo.getNumMesi()));
              lMisuraAggragata.setNumGiorniContinuativi (new BigDecimal (lCalendarModelRicalcolo.getNumGiorni()));
            }
            
            lMisuraAggragata.setIsInContinuazione (false);
            lMisuraAggragata.setIsAggregato (true);
            
            // Aggiungo il totale
            // aElencoMisure.add(i, lMisuraAggragata); // in coda alla sequenza
            aElencoMisure.add(indicePrimoRecordContinuativo, lMisuraAggragata); // in Testa alla sequenza
            
            // Esco, tanto era l'ultimo record
            break;          
          }
        }
        else {
          //TODO aggiungere test su periodi sovrapposti. Per ora gestiti in form
          
          // La misura corrente non è in continuazione con la precedente
          lMisuraCautelareCorrente.setIsInContinuazione(false);
          progrContinuazione++;
          lMisuraCautelareCorrente.setProgressivoContinuazione (new Integer(progrContinuazione));
          
          // Se la precedente era continuativa, vuol dire che era l'ultimo periodo
          // di una serie di misure continuative. Aggiorna il totale periodo
          if (lMisuraCautelarePrecedente.getIsInContinuazione()){
            CalendarModel lCalendarModelRicalcolo = lCalendarUtil.CalcolaNumGiorniMesiAnni (lCalendarModelTot);
            lCalendarModelRicalcolo = lCalendarUtil.ricalcolaGAM (lCalendarModelRicalcolo);
            
            //lMisuraCautelarePrecedente.setNumAnniContinuativi   (new BigDecimal (lCalendarModelRicalcolo.getNumAnni()));
            //lMisuraCautelarePrecedente.setNumMesiContinuativi   (new BigDecimal (lCalendarModelRicalcolo.getNumMesi()));
            //lMisuraCautelarePrecedente.setNumGiorniContinuativi (new BigDecimal (lCalendarModelRicalcolo.getNumGiorni()));
          
            // In alternativa aggiungo un record totale
            MisuraCautelareCumuloModel lMisuraAggragata = new MisuraCautelareCumuloModel();
            
            lMisuraAggragata.setDescrTipoMisura ("Periodo continuativo sofferto in misure di natura differente:");
            lMisuraAggragata.setDataInizio (lCalendarModelTot.getDataInizio());
            lMisuraAggragata.setDataFine   (lCalendarModelTot.getDataFine());
            
            if (lMisuraAggragata.getDataFine()!=null){
              lMisuraAggragata.setNumAnniContinuativi   (new BigDecimal (lCalendarModelRicalcolo.getNumAnni()));
              lMisuraAggragata.setNumMesiContinuativi   (new BigDecimal (lCalendarModelRicalcolo.getNumMesi()));
              lMisuraAggragata.setNumGiorniContinuativi (new BigDecimal (lCalendarModelRicalcolo.getNumGiorni()));
            }
            
            lMisuraAggragata.setIsInContinuazione (false);
            lMisuraAggragata.setIsAggregato (true);
            
            // Aggiungo il totale
            // aElencoMisure.add(i, lMisuraAggragata); // in coda alla sequenza
            aElencoMisure.add(indicePrimoRecordContinuativo, lMisuraAggragata); // in Testa alla sequenza
            
            // porto avanti il contatore per non rielaborare il record corrente
            i++; 
          }
          
          // Resetto il totale
          lCalendarModelTot = new CalendarModel();
          lCalendarModelTot.setDataInizio (lMisuraCautelareCorrente.getDataInizio());
          lCalendarModelTot.setDataFine   (lMisuraCautelareCorrente.getDataFine());
          
        }        
      }      
      lMisuraCautelarePrecedente = lMisuraCautelareCorrente;
    }
    
    return aElencoMisure;
  }

}