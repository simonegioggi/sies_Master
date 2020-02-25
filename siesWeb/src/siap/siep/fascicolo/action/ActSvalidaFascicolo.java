package siap.siep.fascicolo.action;

import java.math.BigDecimal;

import siap.SIAPException;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActSvalidaFascicolo extends ActionSiap implements ICostantiFascicoloSiep
{
  public String processRequest() throws Exception
  {
		UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

    //Viene cercato l'eventuale fascicolo da validare tra quelli dell'ufficio utente
    BigDecimal lChiaveProgr = getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR);
    BigDecimal lChiaveAnno = getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO);
    String lChiaveUfficio = lUtenteMod.getUfficioUtente().getCodUfficio();
    FascicoloSiepModel lFasMod = new FascicoloSiepModel();
    IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

    lFasMod.setChiaveProgr(lChiaveProgr);
    lFasMod.setChiaveAnno(lChiaveAnno);
    lFasMod.setChiaveUfficio(lChiaveUfficio);

    FascicoloSiepModel lFasModel = null;
    lFasModel = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

    if(lFasModel == null)
    {
      throw new SIEPException(SIAPException.USER_MESSAGE,"Il Fascicolo è inesistente o non appartiene all'ufficio dell'utente");
    }

    this.setSessionAttribute("fascicolo", lFasModel);//fascicolo prima della validazione

    if(lFasModel.getFlagValidato() != null && lFasModel.getFlagValidato().equals("N"))
    {
      throw new SIEPException(SIAPException.USER_MESSAGE," Il Fascicolo non è  validato");
    }

     lFasModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
     lFasModel.setDataAggiornamento(DateUtils.getSysDate());
     lFasModel.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
     lFasModel.setFlagValidato("N");
     lFasModel.setCodStatoFascicolo("02");

 //setto lo stato del procedimento
     StatoProcedimentoModel lStat = new StatoProcedimentoModel();
     lStat.setFasSieIdFascicoloSiep(lFasModel.getIdFascicoloSiep());
     lStat.setProgressivo(new BigDecimal(1));
     lStat.setDataInserimento(DateUtils.getSysDate());
     lStat.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
     lStat.setCodOperatoreInserimento(this.getCodUtenteConnesso());
     lStat.setCodStatoProcedimento("0108"); //ISCRITTO

     lFasMod = lCtrl.ExValidazione(lFasModel,lStat);

     this.setSessionAttribute("fascicolo", lFasMod);//fascicolo dopo della validazione

    return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"+CAMPO_ID_FASCICOLO_SIEP+"="+lFasMod.getIdFascicoloSiep().toString();

  }
}
