package siap.siep.notifica.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActInserisciOmessaNotifica</p>
* <p>Description: Classe Action per la load dettaglio di Scadenzario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActInserisciOmessaNotifica extends ActionSiap implements ICostantiNotifica
{
  public String processRequest() throws F3BException
  {
    BigDecimal lIdEve = this.getRequestBigDecimalParameter("idevento");
    BigDecimal lIdNot = this.getRequestBigDecimalParameter("idnotifica");

    IEventoSimeone lCtrlEve = SICOLookupRemote.getEventoSimeoneRemote();
    EventoModel lEveMod = new EventoModel();
    lEveMod = lCtrlEve.ExRicercaEventoByEveIdEvento(lIdEve);

    IVerbale lCtrlVer = SIEPLookupRemote.getVerbaleRemote();
    VerbaleModel lVermodel = new VerbaleModel();
    if(lEveMod != null && lEveMod.getIdEvento() != null)
    {
      lVermodel = lCtrlVer.ExRicercaVerbaleByCodTipoIdEvento(lEveMod.getIdEvento(), "02");
    }

    VerbaleModel lVerMod = new VerbaleModel();
    RinnovoModel lRinMod = new RinnovoModel();
    RinnovoModel lRinModel = new RinnovoModel();
    ComuneModel lComMod = null;

    //RINNOVO
    lRinMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lRinMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lRinMod.setDataInserimento(DateUtils.getSysDate());
    lRinMod.setNotIdNotifica(lIdNot);

    if (this.getRequestStringParameter("Notifica").equals("FP")) //FORZE DI POLIZIA
    {
      if(lVermodel == null || lVermodel.getIdVerbale() == null)
        throw new SIEPException(SIEPException.USER_MESSAGE, "Non Esiste Nessun Verbale Vane Ricerche Associato all'OE con Sospensione.");

      //lo passo all'inserimento per legare ilrinnovo al verbale
      lVerMod.setIdVerbale(lVermodel.getIdVerbale());

       //Rinnovo
      lRinMod.setCodTipoRinnovo("R");
      if (this.getRequestStringParameter("Rinnovo").equals("RA")) // RINNOVO ALTRA AUTORITA'
      {
        lRinMod.setCodTipoAutoritaRinnovo(this.getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO));
        lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO)));
        lRinMod.setCodLuogoRinnovo(lComMod.getCodComune());
         
        lRinMod.setDataRinnovo(getRequestDateParameter(ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA,
                                                       ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA,
                                                       ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA));
        //indirizzo va dentro note
        lRinMod.setNote(this.getRequestStringParameter(ICostantiRinnovo.CAMPO_NOTE));  
       }
       else if (this.getRequestStringParameter("Rinnovo").equals("RS")) // RINNOVO STESSA AUTORITA'
       {
         lRinMod.setCodTipoAutoritaRinnovo(lVermodel.getCodTipoUfficioFirmatario());
         lRinMod.setCodLuogoRinnovo(lVermodel.getCodLuogoUfficioFirmatario());
         
         lRinMod.setDataRinnovo(getRequestDateParameter(ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO,
                                                        ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO,
                                                        ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO));
       }
     }
     else if (this.getRequestStringParameter("Notifica").equals("UG")) //UFFICIALI GIUDIZIARI
     {
       //VERBALE
       lVerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
       lVerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
       lVerMod.setDataInserimento(DateUtils.getSysDate());
       lVerMod.setEveIdEvento(lIdEve);
       lVerMod.setCodTipoVerbale("04");
       
       lVerMod.setDataEmissione(getRequestDateParameter(ICostantiRinnovo.CAMPO_ANNO_DATA_RELATA,
                                                        ICostantiRinnovo.CAMPO_MESE_DATA_RELATA,
                                                        ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA));
       lVerMod.setCodTipoUfficioFirmatario("22");
       lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO_UG)));
       lVerMod.setCodLuogoUfficioFirmatario(lComMod.getCodComune());

       //Rinnovo
       if (this.getRequestStringParameter("Ufficiali").equals("RN")) // RINNOVO NOTIFICA'
       {
         lRinMod.setCodTipoRinnovo("N");
         lRinMod.setCodTipoAutoritaRinnovo("22");
         
         lRinMod.setDataRinnovo(getRequestDateParameter(ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN,
                                                        ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN,
                                                        ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN));
         
         lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter( ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG)));
         lRinMod.setCodLuogoRinnovo(lComMod.getCodComune());
         lRinMod.setNuovoLuogoNotifica(this.getRequestStringParameter(ICostantiRinnovo.CAMPO_LUOGO_NUOVA_NOTIFICA));
       }
       else if (this.getRequestStringParameter("Ufficiali").equals("AR")) // ATTIVAZIONE RICERCA
       {
         lRinMod.setCodTipoRinnovo("A");
         lRinMod.setCodTipoAutoritaRinnovo(this.getRequestStringParameter(ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_UG));
         
         lRinMod.setDataRinnovo(getRequestDateParameter(ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR,
                                                        ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR,
                                                        ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR));
         
         lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter( ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG_AR)));
         lRinMod.setCodLuogoRinnovo(lComMod.getCodComune());
         lRinMod.setNuovoLuogoNotifica(this.getRequestStringParameter(ICostantiRinnovo.CAMPO_LUOGO_NUOVA_NOTIFICA_UG_AR));
       }
     }

     //INSERIMENTO VERBALE E RINNOVO
     IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
     lRinModel = lCtrl.ExInserisciRinnovoVerbale(lRinMod,lVerMod);

     this.setRequestAttribute("Notifica",getRequestStringParameter("Notifica"));
     this.setRequestAttribute("Ufficiali",getRequestStringParameter("Ufficiali"));
     this.setRequestAttribute("Rinnovo",getRequestStringParameter("Rinnovo"));

     String lPage = "";
     lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.notifica.action.ActLoadDettaglioOmesaNotifica&"+ICostantiRinnovo.CAMPO_ID_RINNOVO+"="+lRinModel.getIdRinnovo().toString();
     
     return lPage;
  }
}