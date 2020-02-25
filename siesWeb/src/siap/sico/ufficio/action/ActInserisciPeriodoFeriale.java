package siap.sico.ufficio.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import siap.sico.web.ActionSiap;
import siap.siep.parametro.action.ICostantiParametro;
import siap.siep.parametro.controller.IParametro;
import siap.siep.parametro.model.ParametroModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
/**
* <p>Title: ActInserisciPeriodoFeriale</p>
* <p>Description: Classe Action per la load inserisci di inserimento peridodo feriale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActInserisciPeriodoFeriale extends ActionSiap 
                                        implements ICostantiUfficio, ICostantiParametro
{
  public String processRequest() throws F3BException
  {
    IParametro lCtrlPar = SIEPLookupRemote.getParametroRemote();

    ParametroModel lParMod = lCtrlPar.ExRicercaParametroUfficioConnesso(PERIODO_FERIALE, this.getCodUfficioUtenteConnesso());

    if(lParMod != null)
    {
      throw new F3BException(F3BException.USER_MESSAGE, "Per l'ufficio già esiste un periodo feriale personalizzato, utilizzare la funzione di modifica.");
    }
    
    lParMod = new ParametroModel();
    
    lParMod.setNomeParametro(PERIODO_FERIALE);
    lParMod.setDataInizioValidita(getRequestDateParameter( CAMPO_ANNO_CORRENTE, CAMPO_MESE_DATA_INIZIO_PERIODO_FERIALE, CAMPO_GIORNO_DATA_INIZIO_PERIODO_FERIALE) );
    lParMod.setDataFineValidita(getRequestDateParameter( CAMPO_ANNO_CORRENTE, CAMPO_MESE_DATA_FINE_PERIODO_FERIALE, CAMPO_GIORNO_DATA_FINE_PERIODO_FERIALE) );
    lParMod.setCodUfficioValidita(this.getRequestStringParameter(CAMPO_COD_UFFICIO));
    lParMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
    lParMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    lParMod.setDataInserimento(DateUtils.getSysDate());
    
    // Calcola quanti giorni intercorrono tra le due date
    Calendar lDataDa = new GregorianCalendar( getRequestIntParameter(CAMPO_ANNO_CORRENTE),
                                              getRequestIntParameter(CAMPO_MESE_DATA_INIZIO_PERIODO_FERIALE),
                                              getRequestIntParameter(CAMPO_GIORNO_DATA_INIZIO_PERIODO_FERIALE)
                                             ); 
    
    Calendar lDataA = new GregorianCalendar( getRequestIntParameter(CAMPO_ANNO_CORRENTE),
                                             getRequestIntParameter(CAMPO_MESE_DATA_FINE_PERIODO_FERIALE),
                                             getRequestIntParameter(CAMPO_GIORNO_DATA_FINE_PERIODO_FERIALE)
                                            ); 

    int lGiorni = DateUtils.getDaysBetween(lDataDa, lDataA);
    
/*
    CalendarModel lCalModel = new CalendarModel();
    lCalModel.setNumGiorni(new BigDecimal(lGiorni)); 

    CalendarUtil lCalUtil = new CalendarUtil();
    lCalModel = lCalUtil.ricalcolaGAM(lCalModel);
    
    lParMod.setAnni( new BigDecimal(lCalModel.getNumAnni()));
    lParMod.setMesi( new BigDecimal(lCalModel.getNumMesi()));
    lParMod.setGiorni( new BigDecimal(lCalModel.getNumGiorni()));
*/
    
    lParMod.setAnni( new BigDecimal(0) );
    lParMod.setMesi( new BigDecimal(0 ));
    lParMod.setGiorni( new BigDecimal(lGiorni+1));

    lParMod = lCtrlPar.ExInserisciParametro(lParMod);
    
    setRequestAttribute("Parametro", lParMod);
    
    //Prepara la "pagina" di destinazione
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.sico.ufficio.action.ActDettaglioPeriodoFeriale" );
    lRedirigi.setParameter( CAMPO_ID_PARAMETRO, ""+lParMod.getIdParametro() );

    return ""+lRedirigi;
  }
}
