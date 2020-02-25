package siap.siep.calcolopena.action;

/**
 * <p>Title: ActInserisciAnnotazioniManualiMC</p>
 * <p>Description: Classe Action per l'inserimento di Circostanza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.bdmc.sbpren.action.ICostantiSbPren;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

public class ActInserisciAnnotazioniManualiComputo extends ActionSiap
                                                   implements ICostantiAnnotazioneManuale
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  protected String eseguiRichiesta(String aCodTipoAnnotazione, String aCodMotivo)
    throws Exception
  {
//Controllo Presenza del Fascicolo in Sessione
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }
    FascicoloSiepModel lFascicoloMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascicoloMod.getIdFascicoloSiep();

    EventoModel lEveProvvedimentoMod = popolaEvento(lIdFascicolo, aCodMotivo);
    // Festa Carlo Modifiche integrazione Sies BDMC
    //Vector annotazioni = popolaAnnotazione(lIdFascicolo, aCodTipoAnnotazione);
    if ( (!isRequestParameterNullObj("DaAndalR")) && (getRequestStringParameter("DaAndalR").length() !=0)) {
	    AnnotazioneManualeModel lAnnManMod = popolaAnnotazione(lIdFascicolo, aCodTipoAnnotazione);
	
	    IAnnotazioneManuale lAnnManCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
	    lAnnManCtrl.ExInserisciAnnotazioneManualeEvento(lAnnManMod, lEveProvvedimentoMod);
    }
    String[] idPeriodo = null;
    if (!this.isRequestParameterNullObj(ICostantiSbPren.CAMPO_CHECK_PERIODI)) {
    	idPeriodo=this.getRequestStringParameters(ICostantiSbPren.CAMPO_CHECK_PERIODI);
     }
    if (idPeriodo != null){
    	 for (int i=0;i<idPeriodo.length;i++) {
    		    AnnotazioneManualeModel lAnnManMod = popolaAnnotazioneBdmc(lIdFascicolo, aCodTipoAnnotazione,Integer.parseInt(idPeriodo[i]));
    		    if (!this.isRequestParameterNullObj("annoBdmc"+Integer.parseInt(idPeriodo[i]))) 
    		    	lAnnManMod.setAnnoMc(new BigDecimal(getRequestStringParameter("annoBdmc"+Integer.parseInt(idPeriodo[i]))));
    		    if (!this.isRequestParameterNullObj("numeroBdmc"+Integer.parseInt(idPeriodo[i]))) 
    		    	lAnnManMod.setNumeroMc(getRequestStringParameter("numeroBdmc"+Integer.parseInt(idPeriodo[i])));
    		    if (!this.isRequestParameterNullObj("annoRgnr"+Integer.parseInt(idPeriodo[i]))) 
    		    	lAnnManMod.setAnnoRege(new BigDecimal(getRequestStringParameter("annoRgnr"+Integer.parseInt(idPeriodo[i]))));
    		    if (!this.isRequestParameterNullObj("numeroRgnr"+Integer.parseInt(idPeriodo[i]))) 
    		    	lAnnManMod.setNumeroRege(getRequestStringParameter("numeroRgnr"+Integer.parseInt(idPeriodo[i])));

    		    IAnnotazioneManuale lAnnManCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
    		    AnnotazioneManualeModel lAnnMod =  lAnnManCtrl.ExInserisciAnnotazioneManualeEvento(lAnnManMod, lEveProvvedimentoMod);
    		    MisuraCautelareBdmcModel  lMisCautBdmcMod = new MisuraCautelareBdmcModel();
    		    lMisCautBdmcMod = new MisuraCautelareBdmcModel();
    	    	lMisCautBdmcMod.setProgPeriPres(new BigDecimal(getRequestStringParameter("idProg"+Integer.parseInt(idPeriodo[i]))));
    	    	lMisCautBdmcMod.setIdAnnotazioneManuale(lAnnMod.getIdAnnotazioneManuale());
    	    	//lMisCautBdmcMod.setAltroLuogoDetenzione(ProcPena.getDescLuog());
    	    //	lMisCautBdmcMod.setIstDetIdIstitutoDetenzione(ProcPena.getCodiIstiPena());
    	    	lMisCautBdmcMod.setAnnoFascSiep(lFascicoloMod.getChiaveAnno());
    	    	lMisCautBdmcMod.setNumeFascSiep(lFascicoloMod.getChiaveProgr());
    	     	 if (!this.isRequestParameterNullObj("annoBdmc"+Integer.parseInt(idPeriodo[i]))) 
    	     			lMisCautBdmcMod.setAnnoFascBdmc(new BigDecimal(getRequestStringParameter("annoBdmc"+Integer.parseInt(idPeriodo[i]))));	
    	     	if (!this.isRequestParameterNullObj("numeroBdmc"+Integer.parseInt(idPeriodo[i]))) 
    	     			lMisCautBdmcMod.setNumeFascBdmc(new BigDecimal(getRequestStringParameter("numeroBdmc"+Integer.parseInt(idPeriodo[i]))));
    	     	 if (!this.isRequestParameterNullObj("sedeBdmc"+Integer.parseInt(idPeriodo[i]))) 
    	     		lMisCautBdmcMod.setCodUfficioBdmc(getRequestStringParameter("sedeBdmc"+Integer.parseInt(idPeriodo[i])));

    	     	//lMisCautBdmcMod.setCodTipoMisura(lMisCautelareMod.getCodTipoMisura());
    	    	lMisCautBdmcMod.setDataFine(DateUtils.getDate(getRequestStringParameter("dtIni"+Integer.parseInt(idPeriodo[i])), "dd-mm-yyyy"));
    	    	lMisCautBdmcMod.setDataInizio(DateUtils.getDate(getRequestStringParameter("dtFine"+Integer.parseInt(idPeriodo[i])), "dd-mm-yyyy"));
    	    	lMisCautBdmcMod.setIdPren(new BigDecimal(getRequestStringParameter("idPren"+Integer.parseInt(idPeriodo[i]))));
    	    	lMisCautBdmcMod.setFlagStato("I");
    	    	lMisCautBdmcMod.setFasSieIdFascicoloSiep(lFascicoloMod.getIdFascicoloSiep());
    	    	lMisCautBdmcMod.setSogIdSoggetto(lFascicoloMod.getSogIdSoggetto());
    	    	lMisCautBdmcMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
    	    	lMisCautBdmcMod.setDataInserimento(DateUtils.getSysDate());
    	    	lMisCautBdmcMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    	        lMisCautBdmcMod.setFlagComputabile("0");
    	    	lMisCautBdmcMod.setFlagCaricamento("BDMC");
    	    	lMisCautBdmcMod.setStatoTrasmissioneIsc("N");
    	    	lMisCautBdmcMod.setDataInizioUsata(getRequestDateParameter(ICostantiSbPren.CAMPO_ANNO_DALLA_DATA+Integer.parseInt(idPeriodo[i]), ICostantiSbPren.CAMPO_MESE_DALLA_DATA+Integer.parseInt(idPeriodo[i]), ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA+Integer.parseInt(idPeriodo[i])));
    	    	lMisCautBdmcMod.setDataFineUsata(getRequestDateParameter(ICostantiSbPren.CAMPO_ANNO_ALLA_DATA+Integer.parseInt(idPeriodo[i]), ICostantiSbPren.CAMPO_MESE_ALLA_DATA+Integer.parseInt(idPeriodo[i]), ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA+Integer.parseInt(idPeriodo[i])));
    	    	IMisuraCautelareBdmc lCtrl = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
    	    	lCtrl.ExInserisciMisuraCautelareBdmc(lMisCautBdmcMod);
    	 }
    	
    } // fien id Periodo null
    
/************************************************/
    if("0121".equals(aCodMotivo))
      setRequestAttribute("lFlagPage", "D");       //STESSO_TITOLO
    else if("0213".equals(aCodMotivo))
      setRequestAttribute("lFlagPage", "S");       //SENZA_TITOLO
    else if("0212".equals(aCodMotivo))
      setRequestAttribute("lFlagPage", "A");       //ALTRO_TITOLO
/************************************************/

    // Se i controlli sono andati a buon fine ritorna null
    return null;
  }

  private EventoModel popolaEvento(BigDecimal aIdFascicolo, String aCodMotivo)
    throws Exception
  {
    EventoModel lEveProvvedimentoMod = new EventoModel();

    lEveProvvedimentoMod.setCodTipoEvento("01");
    lEveProvvedimentoMod.setCodTipoProvvedimento("04"); // PROVVEDIMENTO
    lEveProvvedimentoMod.setCodMotivo(aCodMotivo);
    //--NO----lEveProvvedimentoMod.setFlagDocumentoRegistrato("N");
    lEveProvvedimentoMod.setFlagStampaSiep("S");
    lEveProvvedimentoMod.setFlagVideoSiep("S");
    lEveProvvedimentoMod.setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
    lEveProvvedimentoMod.setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
    lEveProvvedimentoMod.setFasSieIdFascicoloSiep(aIdFascicolo);
    
    // 16/05/2008
    // Devo eliminare minuti e secondi dalla sysdate altrimenti nello stato di 
    // esecuzione il provvedimento viene posto dopo gli altri eventi emessi
    // lo steso giorno (vedi OE per ridet. Pena)
    Date lOggi = DateUtils.getSysDate();
    String lGiorno = DateUtils.getDateToString(lOggi,"dd");
    String lMese   = DateUtils.getDateToString(lOggi,"MM");
    String lAnno   = DateUtils.getDateToString(lOggi,"yyyy");
    Date lDataEmissione =  DateUtils.getDate( lAnno, lMese, lGiorno );

    lEveProvvedimentoMod.setDataEmissione( lDataEmissione );

    lEveProvvedimentoMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lEveProvvedimentoMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lEveProvvedimentoMod.setDataInserimento(DateUtils.getSysDate());

    return lEveProvvedimentoMod;
  }

  private AnnotazioneManualeModel popolaAnnotazione(BigDecimal aIdFascicolo, String aCodTipoAnnotazione)
    throws Exception
  {
    BigDecimal IdReato = null;

    AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();

    lAnnMod.setFlagValidato("N");
    lAnnMod.setCodFonte("-");
    lAnnMod.setCodSottonumerazione("-");

    String PM=getRequestStringParameter("PM");
    lAnnMod.setFlagPiuMeno(PM);

    lAnnMod.setCodTipoAnnotazione(aCodTipoAnnotazione);

    if (getRequestStringParameter("TipoOrd").equals("Conforme"))
      lAnnMod.setFlagConforme("C");
    else if(getRequestStringParameter("TipoOrd").equals("Difforme"))
      lAnnMod.setFlagConforme("D");
    else
      lAnnMod.setFlagConforme("-");

    Date lDataRecDa = getRequestDateParameter( "DaAndalR", "DaMedalR", "DaGidalR" );
    Date lDataRecA = getRequestDateParameter( "DaAnalR", "DaMealR", "DaGialR" );
    if (!isRequestParameterNullObj("DaAndalR"))
    	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    	siesLogger.debug("Periodo Singono non presente");
    
    lAnnMod.setDataReclusioneDa( lDataRecDa );
    lAnnMod.setDataReclusioneA( lDataRecA );

    CalendarModel lCal = new CalendarModel();
    CalendarUtil lCUt = new CalendarUtil();

    lCal.setDataInizio(lDataRecDa);
    lCal.setDataFine(lDataRecA);

    lCal = lCUt.CalcolaNumGiorniMesiAnni(lCal);
    //lCal = lCUt.ricalcolaGAM(lCal);

    BigDecimal AnRec  = new BigDecimal(lCal.getNumAnni());
    BigDecimal MeRec  = new BigDecimal(lCal.getNumMesi());
    BigDecimal GioRec = new BigDecimal(lCal.getNumGiorni());

    lAnnMod.setNumAnniReclusione(AnRec);
    lAnnMod.setNumMesiReclusione(MeRec);
    lAnnMod.setNumGiorniReclusione(GioRec);

    lAnnMod.setFasSieIdFascicoloSiep(aIdFascicolo);
    if (!isRequestParameterNullObj("IdReato"))
    {
      IdReato = getRequestBigDecimalParameter("IdReato");
      lAnnMod.setReaIdReato(IdReato);
    }

    lAnnMod.setNoteReclusione(getRequestStringParameter("noteRec"));

    lAnnMod.setCodDpr("-");
    lAnnMod.setFlagAppProvvisoria("-");

    if (!isRequestParameterNullObj("annoSentenza"))
    {
      lAnnMod.setAnnoSentenzaSiap(getRequestBigDecimalParameter("annoSentenza"));
    }

    if (!isRequestParameterNullObj("numeroSentenza"))
    {
      lAnnMod.setNumeroSentenzaSiap(getRequestStringParameter("numeroSentenza"));
    }

    if (!isRequestParameterNullObj("DaGiSent"))
    {
      lAnnMod.setDataSentenzaSiap(getRequestDateParameter("DaAnSent", "DaMeSent", "DaGiSent"));
    }

    if (!isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_COD_CAUSALE_COMPUTO))
    {
      lAnnMod.setCodCausaleComputo(getRequestStringParameter(CAMPO_COD_CAUSALE_COMPUTO));
    }
    else
      lAnnMod.setCodCausaleComputo("-");


    if (!isRequestParameterNullObj("annoReGe"))
    {
      lAnnMod.setAnnoRege(getRequestBigDecimalParameter("annoReGe"));
    }

    if (!isRequestParameterNullObj("numeroReGe"))
    {
      lAnnMod.setNumeroRege(getRequestStringParameter("numeroReGe"));
    }

    if (!isRequestParameterNullObj("annoMc"))
    {
      lAnnMod.setAnnoMc(getRequestBigDecimalParameter("annoMc"));
    }

    if (!isRequestParameterNullObj("numeroMc"))
    {
      lAnnMod.setNumeroMc(getRequestStringParameter("numeroMc"));
    }

    if (!isRequestParameterNullObj("GiornoDataIstanza"))
    {
      lAnnMod.setDataRichiesta(getRequestDateParameter("AnnoDataIstanza", "MeseDataIstanza", "GiornoDataIstanza"));
    }

    lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lAnnMod.setDataInserimento(DateUtils.getSysDate());

    return lAnnMod;
  }
  private AnnotazioneManualeModel popolaAnnotazioneBdmc(BigDecimal aIdFascicolo, String aCodTipoAnnotazione,int indicePeriodo)
  throws Exception
{
  BigDecimal IdReato = null;

  AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();

  lAnnMod.setFlagValidato("N");
  lAnnMod.setCodFonte("-");
  lAnnMod.setCodSottonumerazione("-");
  String PM=getRequestStringParameter("PM");
  lAnnMod.setFlagPiuMeno(PM);

  lAnnMod.setCodTipoAnnotazione(aCodTipoAnnotazione);

  if (getRequestStringParameter("TipoOrd").equals("Conforme"))
    lAnnMod.setFlagConforme("C");
  else if(getRequestStringParameter("TipoOrd").equals("Difforme"))
    lAnnMod.setFlagConforme("D");
  else
    lAnnMod.setFlagConforme("-");

  Date lDataRecDa = getRequestDateParameter(ICostantiSbPren.CAMPO_ANNO_DALLA_DATA+indicePeriodo, ICostantiSbPren.CAMPO_MESE_DALLA_DATA+indicePeriodo, ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA+indicePeriodo);
  Date lDataRecA = getRequestDateParameter(ICostantiSbPren.CAMPO_ANNO_ALLA_DATA+indicePeriodo, ICostantiSbPren.CAMPO_MESE_ALLA_DATA+indicePeriodo, ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA+indicePeriodo);
  
  lAnnMod.setDataReclusioneDa( lDataRecDa );
  lAnnMod.setDataReclusioneA( lDataRecA );

  CalendarModel lCal = new CalendarModel();
  CalendarUtil lCUt = new CalendarUtil();

  lCal.setDataInizio(lDataRecDa);
  lCal.setDataFine(lDataRecA);

  lCal = lCUt.CalcolaNumGiorniMesiAnni(lCal);
  //lCal = lCUt.ricalcolaGAM(lCal);

  BigDecimal AnRec  = new BigDecimal(lCal.getNumAnni());
  BigDecimal MeRec  = new BigDecimal(lCal.getNumMesi());
  BigDecimal GioRec = new BigDecimal(lCal.getNumGiorni());

  lAnnMod.setNumAnniReclusione(AnRec);
  lAnnMod.setNumMesiReclusione(MeRec);
  lAnnMod.setNumGiorniReclusione(GioRec);

  lAnnMod.setFasSieIdFascicoloSiep(aIdFascicolo);
  if (!isRequestParameterNullObj("IdReato"))
  {
    IdReato = getRequestBigDecimalParameter("IdReato");
    lAnnMod.setReaIdReato(IdReato);
  }

  lAnnMod.setNoteReclusione(getRequestStringParameter("noteRec"));

  lAnnMod.setCodDpr("-");
  lAnnMod.setFlagAppProvvisoria("-");

  if (!isRequestParameterNullObj("annoSentenza"))
  {
    lAnnMod.setAnnoSentenzaSiap(getRequestBigDecimalParameter("annoSentenza"));
  }

  if (!isRequestParameterNullObj("numeroSentenza"))
  {
    lAnnMod.setNumeroSentenzaSiap(getRequestStringParameter("numeroSentenza"));
  }

  if (!isRequestParameterNullObj("DaGiSent"))
  {
    lAnnMod.setDataSentenzaSiap(getRequestDateParameter("DaAnSent", "DaMeSent", "DaGiSent"));
  }

  if (!isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_COD_CAUSALE_COMPUTO))
  {
    lAnnMod.setCodCausaleComputo(getRequestStringParameter(CAMPO_COD_CAUSALE_COMPUTO));
  }
  else
    lAnnMod.setCodCausaleComputo("-");


  if (!isRequestParameterNullObj("annoReGe"))
  {
    lAnnMod.setAnnoRege(getRequestBigDecimalParameter("annoReGe"));
  }

  if (!isRequestParameterNullObj("numeroReGe"))
  {
    lAnnMod.setNumeroRege(getRequestStringParameter("numeroReGe"));
  }

  if (!isRequestParameterNullObj("annoMc"))
  {
    lAnnMod.setAnnoMc(getRequestBigDecimalParameter("annoMc"));
  }

  if (!isRequestParameterNullObj("numeroMc"))
  {
    lAnnMod.setNumeroMc(getRequestStringParameter("numeroMc"));
  }

  if (!isRequestParameterNullObj("GiornoDataIstanza"))
  {
    lAnnMod.setDataRichiesta(getRequestDateParameter("AnnoDataIstanza", "MeseDataIstanza", "GiornoDataIstanza"));
  }

  lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
  lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
  lAnnMod.setDataInserimento(DateUtils.getSysDate());

  return lAnnMod;
}
}