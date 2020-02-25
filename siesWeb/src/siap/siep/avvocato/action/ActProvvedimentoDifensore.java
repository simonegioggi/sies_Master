package siap.siep.avvocato.action;

/**
 * <p>Title: ActSostituzioneDifensore</p>
 * <p>Description: Classe Action per la sostituzione di un Avvocato su un 
 *    fascicolo SIEP</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
import java.math.BigDecimal;

import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActProvvedimentoDifensore    extends ActionSiap 
		implements ICostantiAvvocato
{

  /*****************************************************************************
   * Azione per la creazione del Provvedimento di Nomina di un avvocato d'ufficio
   *	
   * @return -
   * @throws F3BException
   ************************************************************************** */

	protected EventoNotificaModel CreoProvvedimento(AvvocatoFascicoloSiepModel lAvvFascMod)
		throws F3BException {

   EventoNotificaModel lEve = new EventoNotificaModel();
   
	// conto le notifiche per definire l'array
	int i = 0;
	if (!lAvvFascMod.getCodTipoAutorita().equals("-")){
		i++; // condannato
	}
	if (lAvvFascMod.getIstDetIdIstitutoDetenzione() != null
	&& !lAvvFascMod.getIstDetIdIstitutoDetenzione().equals("")) {
		i++; // istituto detenzione   
	}
	if (!lAvvFascMod.getCodTipoAutoritaDif().equals("-")){
		i++; // difensore  
	}
		NotificaModel lNotifiche[] = new NotificaModel[i];
		   
   i = 0;
   // notifica al condannato inserisco come notifica autorita esterna
   if (!lAvvFascMod.getCodTipoAutorita().equals("-")){
	   	NotificaModel lNotModTDS = new NotificaModel();
		lNotModTDS.setCodEsito("-");
		
		lNotModTDS.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lNotModTDS.setDataInserimento(DateUtils.getSysDate());
		lNotModTDS.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		
		lNotModTDS.setCodTipoNotifica("NC");
		lNotModTDS.setDataInvio(DateUtils.getSysDate());	
		
		AutoritaEsternaModel lAut = new AutoritaEsternaModel();
	    lAut.setCodTipoAutorita(lAvvFascMod.getCodTipoAutorita());
	    lAut.setCodSede( lAvvFascMod.getSedeAutorita() );
	    
	    lAut.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	    lAut.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	    lAut.setDataInserimento(DateUtils.getSysDate());
	    
	    lNotModTDS.setAutoritaEsterna(lAut);
	    lNotifiche[i++] = lNotModTDS;
   }
	   
   // notifica all'istituto di detenzione come ufficio censito
   if (lAvvFascMod.getIstDetIdIstitutoDetenzione()!= null
	   && !lAvvFascMod.getIstDetIdIstitutoDetenzione().equals("")) {
	   	NotificaModel lNotModTDS = new NotificaModel();
		lNotModTDS.setCodEsito("-");
		lNotModTDS.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lNotModTDS.setDataInserimento(DateUtils.getSysDate());
		lNotModTDS.setAvvIdAvvocatoFascicoloSiep(lAvvFascMod.getAvvIdAvvocato());
		lNotModTDS.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lNotModTDS.setCodTipoNotifica("AA");
		lNotModTDS.setDataInvio(DateUtils.getSysDate());
		lNotModTDS.setUffCodUfficio(lAvvFascMod.getIstDetIdIstitutoDetenzione());
	    lNotifiche[i++] = lNotModTDS;
   }
   // notifica al difensore inserisco come notifica autorita esterna
   if (!lAvvFascMod.getCodTipoAutoritaDif().equals("-")){			
	   	NotificaModel lNotModTDS = new NotificaModel();
		lNotModTDS.setCodEsito("-");
		
		lNotModTDS.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lNotModTDS.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lNotModTDS.setDataInserimento(DateUtils.getSysDate());
		
		lNotModTDS.setAvvIdAvvocatoFascicoloSiep(lAvvFascMod.getAvvIdAvvocato());
		lNotModTDS.setCodTipoNotifica("ND");
		lNotModTDS.setDataInvio(DateUtils.getSysDate());	
		
		AutoritaEsternaModel lAut = new AutoritaEsternaModel();
	    lAut.setCodTipoAutorita(lAvvFascMod.getCodTipoAutorita());
	    lAut.setCodSede( lAvvFascMod.getSedeAutorita() );
	    lAut.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	    lAut.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	    lAut.setDataInserimento(DateUtils.getSysDate());
	    lNotModTDS.setAutoritaEsterna(lAut);
	    lNotifiche[i++] = lNotModTDS;
   }
  
    lEve.setNotifiche(lNotifiche);
    // Tipo Evento = comunicazione 
	lEve.getEvento().setCodTipoEvento("01");  
	lEve.getEvento().setCodTipoProvvedimento("12"); 
	//06/04/2010 Revisione Codici Motivo per Pene Accessorie.
	//lEve.getEvento().setCodMotivo("5137");
	lEve.getEvento().setCodMotivo("5407");
	FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
	lEve.getEvento().setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );

	lEve.getEvento().setDataEmissione(DateUtils.getDate(DateUtils.getYearToString(DateUtils.getSysDate()),
            DateUtils.getMonthToString(DateUtils.getSysDate()),
            DateUtils.getDayToString(DateUtils.getSysDate())));
   UfficioModel lUff = this.getUfficioUtenteConnesso();
   lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
   lEve.getEvento().setCodLuogoEmittente(lUff.getCodComune());
   lEve.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
   lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
   lEve.getEvento().setCodUfficioInserimento(lUff.getCodUfficio());
   lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
   lEve.getEvento().setCodEsito("-");
   lEve.getEvento().setCodLuogoDestinatario("-");
   lEve.getEvento().setCodUfficioDestinatario("-");	   
   lEve.getEvento().setCodTipoUfficioDestinatario("-");
   lEve.getEvento().setFlagStampaSiep("N");
   //lEve.getEvento().setFlagVideoSiep("N");
   // il provvedimento di comunicazione al difensore d'ufficio deve essere viuslaizzato
   lEve.getEvento().setFlagVideoSiep("S"); 
   
   //  inserisco il magistrato del fascicolo
   BigDecimal id_fasc_siep = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
   IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
   MagistratoCompetenteMagistratoModel lMagi = lMagCtrl.ExRicercaMagistratoCompetenteByFascicoloDataFine(id_fasc_siep);
   if (lMagi != null && lMagi.getMagistrato() != null &&  lMagi.getMagistrato().getCodMagistrato() != null){ 
   		lEve.getEvento().setCodMagistrato(lMagi.getMagistrato().getCodMagistrato());
   }else{
	    throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Magistrato assegnato al procedimento. Impossibile emettere il provvedimento");
   }   
   return lEve;
	}
}