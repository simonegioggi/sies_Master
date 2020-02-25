package siap.siep.provvedimentopm.action;


/**
* <p>Title: ActModificaProvvedimento</p>
* <p>Description: Classe Action per la modifica di Provvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
//import siap.siep.provvedimentopm.controller.ProvvedimentoController;
import siap.siep.provvedimentopm.controller.IProvvedimento;
import siap.siep.provvedimentopm.model.ProvvedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActModificaProvvedimento extends ActionSiap implements ICostantiProvvedimento
{
  /**
   * Azione di Modifica del Provvedimento
   * @return Nome della pagina JSP da visualizzare
   * al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {
      String lId = getRequestStringParameter(CAMPO_ID_PROVVEDIMENTO);
      // riempie il model
      ProvvedimentoModel lProMod = new ProvvedimentoModel ();
      lProMod.setIdProvvedimento( new BigDecimal(lId) );
      lProMod.setIdProvvedimento( getRequestBigDecimalParameter( CAMPO_ID_PROVVEDIMENTO) );
      lProMod.setCodTipo( getRequestStringParameter( CAMPO_COD_TIPO) );
      lProMod.setCodMotivo( getRequestStringParameter( CAMPO_COD_MOTIVO) );
      lProMod.setData( getRequestDateParameter( CAMPO_ANNO_DATA,CAMPO_MESE_DATA,CAMPO_GIORNO_DATA) );
      //lProMod.setData( getRequestDateParameter( CAMPO_DATA) );
      lProMod.setCodEsito( getRequestStringParameter( CAMPO_COD_ESITO) );
      lProMod.setFlagPiuMeno( getRequestStringParameter( CAMPO_FLAG_PIU_MENO) );
      lProMod.setDataTrasmissioneAtti( getRequestDateParameter( CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI,CAMPO_MESE_DATA_TRASMISSIONE_ATTI,CAMPO_ANNO_DATA_TRASMISSIONE_ATTI) );
      //lProMod.setDataTrasmissioneAtti( getRequestDateParameter( CAMPO_DATA_TRASMISSIONE_ATTI) );
      lProMod.setDataScadenza( getRequestDateParameter( CAMPO_ANNO_DATA_SCADENZA,CAMPO_MESE_DATA_SCADENZA,CAMPO_GIORNO_DATA_SCADENZA) );
      //lProMod.setDataScadenza( getRequestDateParameter( CAMPO_DATA_SCADENZA) );
      lProMod.setAnnoProtocollo( getRequestBigDecimalParameter( CAMPO_ANNO_PROTOCOLLO) );
      lProMod.setProgrProtocollo( getRequestBigDecimalParameter( CAMPO_PROGR_PROTOCOLLO) );
      lProMod.setDataInserimento( getRequestDateParameter( CAMPO_GIORNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_ANNO_DATA_INSERIMENTO) );
      lProMod.setCodOperatoreAggiornamento( getRequestStringParameter( CAMPO_COD_OPERATORE_AGGIORNAMENTO) );
      UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
      lProMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
      lProMod.setDataAggiornamento(DateUtils.getSysDate());
      //lProMod.setDataAggiornamento( getRequestDateParameter( CAMPO_ANNO_DATA_AGGIORNAMENTO,CAMPO_MESE_DATA_AGGIORNAMENTO,CAMPO_GIORNO_DATA_AGGIORNAMENTO) );
      //lProMod.setDataAggiornamento( getRequestDateParameter( CAMPO_DATA_AGGIORNAMENTO) );
      //UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
      //lProMod.setDataAggiornamento(lUtenteMod.getCodUtente());
      lProMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );
      lProMod.setMagCodMagistrato( getRequestStringParameter( CAMPO_MAG_COD_MAGISTRATO) );
      // chiama il controller
      IProvvedimento lCtrl = SIEPLookupRemote.getProvvedimentoRemote();
      //ProvvedimentoController lCtrl = new ProvvedimentoController();
      lCtrl.ExModificaProvvedimento(lProMod);

      setRequestAttribute("modalita", "M");
      setRequestAttribute("provvedimento", lProMod);

      return PG_LOAD_INSERISCIPROVVEDIMENTO;
  }

}