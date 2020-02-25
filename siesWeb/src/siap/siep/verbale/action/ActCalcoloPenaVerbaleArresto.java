package siap.siep.verbale.action;


/**
* <p>Title: ActCalcoloPenaVerbaleArresto</p>
* <p>Description: Classe Action per l'inserimento di PenaResidua</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

public class ActCalcoloPenaVerbaleArresto extends ActionSiap implements ICostantiVerbale
{
  /**
  * Azione di Inserimento Aggiornamento del PenaResidua
  * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws F3BException
  {

     FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

     // il model VERBALE
    /* String lId = getRequestStringParameter(CAMPO_ID_VERBALE);
     VerbaleModel lVerMod = new VerbaleModel();

     IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();
     lVerMod = lCtrl.ExRicercaVerbaleByKey(new BigDecimal(lId));*/


  /*   PenaResiduaModel lPenModFin = new PenaResiduaModel();

     String lGiorno = DateUtils.getDayToString(lVerMod.getDataEmissione());
     String lMese = DateUtils.getMonthToString(lVerMod.getDataEmissione());
     String lAnno = DateUtils.getYearToString(lVerMod.getDataEmissione());
     String vedoDataIntermedia = "N";

     Date lDataFinePena = null;
     Date lDataInizioArresto = null;
     Date lDataFineReclusione= null;

     Date lDataInizio = DateUtils.getDate(lAnno,lMese,lGiorno);

     PenaResiduaModel lPenMod = new PenaResiduaModel();
     IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
     lPenMod = IPenRes.ExRicercaPenaResiduaUltima(lFascMod.getIdFascicoloSiep());


/***********************************NO*ERGASTOLO********************************************/
/*   if(lPenMod.getFlagErgastolo().equals("N"))
   {

     ICalcoloPena ICalPen = SIEPLookupRemote.getCalcoloPenaRemote();

     Vector lVectFine =  ICalPen.exCalcolaDataFinePena(lDataInizio,lPenMod);

     if(lVectFine.size() == 0 )
     {
        throw new F3BException(F3BException.USER_MESSAGE,"Impossibile calcolare la data di Fine Pena");
     }

     if(lVectFine.size() == 1 )
     {
       lDataFinePena = (Date) lVectFine.get(0);
       lDataInizioArresto = lDataInizio;

     }
     if(lVectFine.size() == 2)
     {
       lDataFinePena = (Date) lVectFine.get(1);
       lDataFineReclusione = (Date) lVectFine.get(0);
       lDataInizioArresto = DateUtils.moveDateTo(lDataFineReclusione,Calendar.DAY_OF_MONTH,1);
       lPenMod.setDataFineReclusione(lDataFineReclusione);
       vedoDataIntermedia = "S";

     }
     lPenMod.setDataFinePresunta(lDataFinePena);
     lPenMod.setDataFine(lDataFinePena);
     lPenMod.setDataInizioArresto(lDataInizioArresto);

   }else        //inizio ergastolo
   {
       Date lDataFinePenaErga = DateUtils.getDate(9999,12,31);
       lPenMod.setDataFine(lDataFinePenaErga);
   }            //fine ergastolo

     lPenMod.setDataInizio(lDataInizio);



     if(lPenMod.getFlagValidato().equals( "S"))
    {

      lPenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
      lPenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
      lPenMod.setDataInserimento(DateUtils.getSysDate());

    }

    if(lPenMod.getFlagValidato().equals("N"))
    {
      lPenMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
      lPenMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
      lPenMod.setDataAggiornamento(DateUtils.getSysDate());

    }
      lPenModFin = IPenRes.ExInserisciAggiornaPenaResidua(lPenMod);

//passaggio del model alla jsp
*/
//prepara il model LUOGO DETENZIONE

      LuogoDetenzioneModel lLuogDet = new LuogoDetenzioneModel();

     // lLuogDet.setDescrTipoIstituto(this.getRequestStringParameter("tipostituto"));
     // lLuogDet.setDescrLuogo(this.getRequestStringParameter("Luogodetenzione"));

      ILuogoDetenzione lCtrlDet = SIEPLookupRemote.getLuogoDetenzioneRemote();
      lLuogDet = lCtrlDet.ExRicercaLuogoDetenzioneCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

//prepara il model VERBALE

     /*VerbaleModel lVerMod = new VerbaleModel();

     String lGiornoP = this.getRequestStringParameter("ggpervenimento");
     String lMeseP = this.getRequestStringParameter("mmpervenimento");
     String lAnnoP = this.getRequestStringParameter("aapervenimento");
     Date lDataPerv = DateUtils.getDate(lAnnoP,lMeseP,lGiornoP);


     lVerMod.setNote(this.getRequestStringParameter("Note"));
     lVerMod.setDescrTipoUfficioFirmatario(this.getRequestStringParameter("autorita"));
     lVerMod.setDescrLuogoUfficioFirmatario(this.getRequestStringParameter("luogo"));
     lVerMod.setDataEmissione(lDataInizio);
     lVerMod.setDataPervenimento(lDataPerv);*/

//preparo il model di pena residua
    PenaResiduaModel lPenMod = new PenaResiduaModel();
    IPenaResidua IPenRes = SIEPLookupRemote.getPenaResiduaRemote();
    //lPenMod = IPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
    lPenMod = IPenRes.ExRicercaPenaResiduaUltimaByDate(lFascMod.getIdFascicoloSiep());


//prepara i dati per la jsp
       setRequestAttribute("penaresidua", lPenMod);
       setRequestAttribute("vedoDataIntermedia", this.getRequestAttribute("vedoDataIntermedia"));
       setRequestAttribute("verbale",this.getRequestAttribute("verbale"));
       setRequestAttribute("luogodetenzione",lLuogDet);




       return PG_LOAD_DETTAGLIO_PENA_RESIDUA_VERBALE_ARRESTO;


      }

}
