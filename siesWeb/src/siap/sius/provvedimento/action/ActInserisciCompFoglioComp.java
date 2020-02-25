package siap.sius.provvedimento.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.sedegiudiziaria.controller.ISedeGiudiziaria;
import siap.siep.sedegiudiziaria.model.SedeGiudiziariaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.fogliocomplementare.action.ICostantiFoglioComp;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
/**
 * <p>Title: ActInserisciCompFoglioComp </p>
 * <p>Description: Azione specializzazione per .
 * <p>Copyright: Bull Italia S.p.A.Copyright (c) 2002</p>
 * <p>Company: Bull Italia S.p.A.</p>
 * @version 1.0
 */

public class ActInserisciCompFoglioComp extends ActionSiap
implements ICostantiProvvedimento
{

  public String processRequest() throws Exception
  {
    // Gestione del punto di ritorno
    setLinkRitorno();
    
    String lDescSedeGiudiziaria = null;
    
    // Eventuale modifica casellario
    if (isRequestChecked(CHK_CAMBIO_CASELLARIO))
    {
    	// Controllo esistenza Sede Giudiziaria
    	lDescSedeGiudiziaria = verificaSedeGiudiziaria(getRequestStringParameter(NUOVO_CASELLARIO));
    }
    // Controller del DocumentoAllegato
    IDocumentoAllegato lDocCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
   
    // Evento. Leggo l'evento collegato al decreto.
    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    EventoModel lEveMod = new EventoModel();
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

    // Valorizzazione del Documento Allegato
    DocumentoAllegatoModel aDocAllegato = new DocumentoAllegatoModel();
    aDocAllegato.setDataEmissione( getRequestDateParameter( ICostantiProvvedimento.CAMPO_ANNO_DATA_EMISSIONE,
                                                            ICostantiProvvedimento.CAMPO_MESE_DATA_EMISSIONE,
                                                            ICostantiProvvedimento.CAMPO_GIORNO_DATA_EMISSIONE ) );

    aDocAllegato.setCodTipoDocumento("06");
    aDocAllegato.setAnnoFoglioComplementare(new BigDecimal(DateUtils.getSysDate("yyyy")));

    
    
    aDocAllegato.setDataTrasmissione(getRequestDateParameter( ICostantiProvvedimento.CAMPO_ANNO_DATA_TRASMISSIONE,
                                                            ICostantiProvvedimento.CAMPO_MESE_DATA_TRASMISSIONE,
                                                            ICostantiProvvedimento.CAMPO_GIORNO_DATA_TRASMISSIONE ) );
    
    
    aDocAllegato.setDataInsMan(getRequestDateParameter( ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE,
    		ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE,
    		ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE ));  
    
    aDocAllegato.setEveIdEvento(lIdEvento);
    aDocAllegato.setCodOperatoreInserimento(this.getCodUtenteConnesso());
    aDocAllegato.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
    aDocAllegato.setDataInserimento(DateUtils.getSysDate());
    aDocAllegato.setComuneSedeGiudiziaria(lDescSedeGiudiziaria);

    // Prelevo Decreto/Ordinanza
    DepositoDecretoModel llDecMod = null;
    if(lEveMod.getCodTipoProvvedimento().equals("02"))
    {
      IDepositoDecreto lCtrlDD = SIUSLookupRemote.getDepositoDecretoRemote();
      llDecMod = lCtrlDD.ExRicercaDepositoDecretoByIdEvento(lIdEvento);
      aDocAllegato = lDocCtrl.ExInserisciFoglioComplementare(aDocAllegato, lEveMod.getCodTipoProvvedimento(), llDecMod.getIdDepositoDecreto());
    }

    // Prelevo Decreto/Ordinanza
    DepositoOrdinanzaPcModel llDepMod = null;
    if(lEveMod.getCodTipoProvvedimento().equals("03"))
    {
      IDepositoOrdinanzaPc lCtrlOrd = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
      llDepMod = lCtrlOrd.ExRicercaDepositoOrdinanzaPcByEvento(lIdEvento);
      aDocAllegato = lDocCtrl.ExInserisciFoglioComplementare(aDocAllegato, lEveMod.getCodTipoProvvedimento(), llDepMod.getIdDepositoOrdinanzaPc());
    }

    //return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.provvedimento.action.ActLoadInserisciCompFoglioComp&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lEveMod.getIdEvento().toString();
    return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.provvedimento.action.ActLoadDettaglioCompFoglioComp&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lEveMod.getIdEvento().toString()+"&Provenienza=null";
    }
  

  private String verificaSedeGiudiziaria(String aDescComune) throws Exception
  {
	  String retValue = "";
	  
      ISedeGiudiziaria lCtrl = SIEPLookupRemote.getSedeGiudiziariaRemote();
      
      SedeGiudiziariaModel lSedGiuMod = lCtrl.ExRicercaSedeGiudiziariaByDescrizione(aDescComune);
      if (lSedGiuMod == null)
  		throw new SIUSException(SIUSException.USER_MESSAGE, "La sede Giudiziaria indicata non esiste!");
      else
    	  retValue = lSedGiuMod.getDescrizione();
      return  retValue;
	  
  }
  
  
}