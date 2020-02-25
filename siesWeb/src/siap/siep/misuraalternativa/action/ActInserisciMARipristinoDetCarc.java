package siap.siep.misuraalternativa.action;

/**
* <p>Title: ActInserisciMARipristinoDetCarc</p>
* <p>Description: Classe Action per l'inserimento di Ripristino Detenzione in Carcere</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import java.math.BigDecimal;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActInserisciMARipristinoDetCarc extends ActionSiap implements ICostantiVerbale
{
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

    VerbaleModel lVerMod = new VerbaleModel();
    lVerMod.setCodTipoVerbale("01");
    lVerMod.setDataPervenimento(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO, ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO, ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO));
    lVerMod.setDataEmissione(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE, ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE, ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE));
    lVerMod.setCodTipoUfficioFirmatario(this.getRequestStringParameter(ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO));

    ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO)));
    lVerMod.setCodLuogoUfficioFirmatario(lComMod.getCodComune());
    lVerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lVerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lVerMod.setNote(getRequestStringParameter(ICostantiVerbale.CAMPO_NOTE));
    lVerMod.setDataInserimento(DateUtils.getSysDate());

    //Model Posizione Giuridica
    PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
    IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
    lPos = lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);

    PosizioneGiuridicaModel lPosMod = null;
    if(lPos != null && "12".equals(lPos.getCodPosizioneGiuridica()))
    {
      lPosMod = new PosizioneGiuridicaModel();
      lPosMod.setFasSieIdFascicoloSiep(lIdFascicolo);
      lPosMod.setCodPosizioneGiuridica("03"); // Espiazione Pena in Regime Carcerario
      lPosMod.setDataInizio(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE, ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE,
          ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE));
      lPosMod.setCodPosizioneProcessuale("-");
      lPosMod.setCodOperatoreInserimento(getCodUtenteConnesso());
      lPosMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
      lPosMod.setDataInserimento(DateUtils.getSysDate());
    }

    //Model Luogo Detenzione
    LuogoDetenzioneModel lLuoDetMod = new LuogoDetenzioneModel();
    lLuoDetMod.setFasSieIdFascicoloSiep(lIdFascicolo);
    lLuoDetMod.setDataInserimento(DateUtils.getSysDate());
    lLuoDetMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lLuoDetMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    lLuoDetMod.setIstDetIdIstitutoDetenzione(this.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
    lLuoDetMod.setDataInizioDetenzione(getRequestDateParameter(ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE, ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE, ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE));


    //NEL CONTROLLER GESTISCE LO STATO PROCEDIMENTO
    IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();
    VerbaleModel llVerModRet = lCtrl.ExInserisciRipristinoDetCarc( lFascMod.getIdFascicoloSiep(),
                                                       lVerMod,
                                                       lPosMod,
                                                       lLuoDetMod);
    setRequestAttribute("verbale", llVerModRet);

    //Prepara la pagina di destinazione
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.misuraalternativa.action.ActLoadDettaglioRipristinoDetCarc&"+CAMPO_ID_VERBALE+"="+llVerModRet.getIdVerbale().toString();
    return lPage;
  }
}