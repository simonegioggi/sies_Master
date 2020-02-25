package siap.sius.misuraalternativa.action;

/**
* <p>Title: ActInserisciDataInizioMisuraAlternativa</p>
* <p>Description: Classe Action per l'inserimento di Verbale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import siap.sius.ActionSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;
import siap.sius.scadenzario.action.ICostantiScadenzarioSius;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciDataInizioMisuraAlternativa extends ActionSius implements ICostantiVerbale
{
/**
* Azione di Inserimento del Verbale
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws Exception
{
      BigDecimal lIdFasSius = null;
      FascicoloGPModel lFasGPMod = null;

      // Fascicolo Sius
      lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
      lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

     VerbaleModel lVerMod = new VerbaleModel();
     lVerMod.setEveIdEvento(getRequestBigDecimalParameter ("idEvento"));
     lVerMod.setCodTipoVerbale("03");
     lVerMod.setDataPervenimento(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO, ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO, ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO));
     lVerMod.setDataEmissione(getRequestDateParameter(ICostantiScadenzarioSius.CAMPO_ANNO_DATA_FINE_SCADENZA, ICostantiScadenzarioSius.CAMPO_MESE_DATA_FINE_SCADENZA, ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_FINE_SCADENZA));

     // Controlla CSSA
     if(!this.isRequestParameterNullObj(ICostantiRichiestaAtti.CAMPO_SEDE ))
     {
      // Preleva l'id del CSSA attraverso la propria descrizione.
      String lDescrCSSA        = getRequestStringParameter(ICostantiRichiestaAtti.CAMPO_SEDE);
      BigDecimal lIdCSSA = this.getIdCSSAByDescrComune( lDescrCSSA);
       lVerMod.setCssIdCssa(lIdCSSA);
     }

     if(!this.isRequestParameterNullObj(ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE))
    {
      lVerMod.setIstDetIdIstitutoDetenzione(this.getRequestStringParameter(ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE));
    }else
    {
      lVerMod.setIstDetIdIstitutoDetenzione("-");
    }

    if(!this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO)
        && !this.isRequestParameterNullObj(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO))
   {
     ComuneModel lComMod = this.getCodComuneByDescr(getRequestStringParameter(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO));
     lVerMod.setCodLuogoUfficioFirmatario(lComMod.getCodComune());
     lVerMod.setCodTipoUfficioFirmatario(this.getRequestStringParameter(ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO));
   }else
   {
     lVerMod.setCodTipoUfficioFirmatario("-");
     lVerMod.setCodLuogoUfficioFirmatario("-");
   }

   lVerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
   lVerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
   lVerMod.setNote(getRequestStringParameter(ICostantiVerbale.CAMPO_NOTE));
   lVerMod.setDataInserimento(DateUtils.getSysDate());
/*
   IFascicoloSius lCtrlFas = SIUSLookupRemote.getFascicoloSiusRemote();
   Vector lVectFas = null;
   lVectFas = lCtrlFas.ExRicercaFascicoliXOrigine(lIdFasSius);

   Iterator itx4 = lVectFas.iterator();
   while ( itx4.hasNext())
   {
     FascicoloSiusModel lFasOrigine = (FascicoloSiusModel)itx4.next();
     // E' previsto un solo fascicolo ma non si sa mai
   }
*/
   //all'interno del controller aggiorna la posizione giuridica
   IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();
   VerbaleModel llVerModRet = lCtrl.ExInserisciDataInizioMisuraAlternativa(lIdFasSius, lVerMod);

   setRequestAttribute("verbale", llVerModRet);
   //Prepara la pagina di destinazione
   String lPage = "";
   lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.misuraalternativa.action.ActLoadDettaglioDataInizioMisuraAlternativa";
   return lPage;
 }

}
