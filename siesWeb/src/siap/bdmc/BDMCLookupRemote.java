package siap.bdmc;

/**
* <p>Title: BDMCLookupRemote</p>
* <p>Description: Classe Lookup per istanziare il controller</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.bdmc.fascicolosiepbdmc.controller.IFascicoloSiepBdmc;
import siap.bdmc.notifichesies.controller.INotificheSies;
import siap.bdmc.sbperipren.controller.ISbPeripren;
import siap.bdmc.sbpren.controller.IImportaDati;
import siap.bdmc.sbpren.controller.ISbPren;
import siap.bdmc.sbviewcapoimpu.controller.ISbViewCapoimpu;
import siap.bdmc.sbviewnotifiche.controller.ISbViewNotifiche;
import siap.bdmc.sbviewprocpena.controller.ISbViewProcpena;
import siap.bdmc.sbviewreat.controller.ISbViewReat;
import siap.bdmc.statoprenotazionibdmc.controller.IStatoPrenotazioniBdmc;
import f3b.util.F3BException;
import f3b.util.LookupClass;

public class BDMCLookupRemote extends LookupClass {
  /*****************************************************************************
   * Istanzia e restituisce l'interfaccia del controller SiesCapoimputazioneprenotatoController
   * @return Un'istanza dell'interfaccia del controller SiesCapoimputazioneprenotatoController
   * @throws F3BException
   ****************************************************************************/

//	Inserire gli import in testa a file
//	import siap.bdmc.sbpren.controller.ISbPren;
//	import siap.bdmc.sbpren.controller.SbPrenController;

	  /*****************************************************************************
	   * Istanzia e restituisce l'interfaccia del controller SbPrenController
	   * @return Un'istanza dell'interfaccia del controller SbPrenController
	   * @throws F3BException
	   ****************************************************************************/
	  public static ISbPren getSbPrenRemote() throws F3BException {
	    Object lRef;
	    ISbPren lRemote;
	    lRef = lookup("siap.bdmc.sbpren.controller.SbPrenController");
	    lRemote = (ISbPren)lRef;
	    return lRemote;
	  }
//	Inserire gli import in testa a file
//	import siap.bdmc.sbperipren.controller.ISbPeripren;
//	import siap.bdmc.sbperipren.controller.SbPeriprenController;

	  /*****************************************************************************
	   * Istanzia e restituisce l'interfaccia del controller SbPeriprenController
	   * @return Un'istanza dell'interfaccia del controller SbPeriprenController
	   * @throws F3BException
	   ****************************************************************************/
	  public static ISbPeripren getSbPeriprenRemote() throws F3BException {
	    Object lRef;
	    ISbPeripren lRemote;
	    lRef = lookup("siap.bdmc.sbperipren.controller.SbPeriprenController");
	    lRemote = (ISbPeripren)lRef;
	    return lRemote;
	  }

//	Inserire gli import in testa a file
//	import siap.bdmc.sbviewprocpena.controller.ISbViewProcpena;
//	import siap.bdmc.sbviewprocpena.controller.SbViewProcpenaController;

	  /*****************************************************************************
	   * Istanzia e restituisce l'interfaccia del controller SbViewProcpenaController
	   * @return Un'istanza dell'interfaccia del controller SbViewProcpenaController
	   * @throws F3BException
	   ****************************************************************************/
	  public static ISbViewProcpena getSbViewProcpenaRemote() throws F3BException {
	    Object lRef;
	    ISbViewProcpena lRemote;
	    lRef = lookup("siap.bdmc.sbviewprocpena.controller.SbViewProcpenaController");
	    lRemote = (ISbViewProcpena)lRef;
	    return lRemote;
	  }

//	Inserire gli import in testa a file
//	import siap.bdmc.sbviewcapoimpu.controller.ISbViewCapoimpu;
//	import siap.bdmc.sbviewcapoimpu.controller.SbViewCapoimpuController;

	  /*****************************************************************************
	   * Istanzia e restituisce l'interfaccia del controller SbViewCapoimpuController
	   * @return Un'istanza dell'interfaccia del controller SbViewCapoimpuController
	   * @throws F3BException
	   ****************************************************************************/
	  public static ISbViewCapoimpu getSbViewCapoimpuRemote() throws F3BException {
	    Object lRef;
	    ISbViewCapoimpu lRemote;
	    lRef = lookup("siap.bdmc.sbviewcapoimpu.controller.SbViewCapoimpuController");
	    lRemote = (ISbViewCapoimpu)lRef;
	    return lRemote;
	  }

//	Inserire gli import in testa a file
//	import siap.bdmc.sbviewreat.controller.ISbViewReat;
//	import siap.bdmc.sbviewreat.controller.SbViewReatController;

	  /*****************************************************************************
	   * Istanzia e restituisce l'interfaccia del controller SbViewReatController
	   * @return Un'istanza dell'interfaccia del controller SbViewReatController
	   * @throws F3BException
	   ****************************************************************************/
	  public static ISbViewReat getSbViewReatRemote() throws F3BException {
	    Object lRef;
	    ISbViewReat lRemote;
	    lRef = lookup("siap.bdmc.sbviewreat.controller.SbViewReatController");
	    lRemote = (ISbViewReat)lRef;
	    return lRemote;
	  }

	  public static IFascicoloSiepBdmc getFascicoloSiepBdmcRemote() throws F3BException {
		    Object lRef;
		    IFascicoloSiepBdmc lRemote;
		    lRef = lookup("siap.bdmc.fascicolosiepbdmc.controller.FascicoloSiepBdmcController");
		    lRemote = (IFascicoloSiepBdmc)lRef;
		    return lRemote;
		  }

	  /*****************************************************************************
	   * Istanzia e restituisce l'interfaccia del controller SbViewNotificheController
	   * @return Un'istanza dell'interfaccia del controller SbViewNotificheController
	   * @throws F3BException
	   ****************************************************************************/
	  public static ISbViewNotifiche getSbViewNotificheRemote() throws F3BException {
	    Object lRef;
	    ISbViewNotifiche lRemote;
	    lRef = lookup("siap.bdmc.sbviewnotifiche.controller.SbViewNotificheController");
	    lRemote = (ISbViewNotifiche)lRef;
	    return lRemote;
	  }

	    public static IImportaDati getImportaDati() throws F3BException
		 {
			 Object lRef;
			 IImportaDati lRemote;
			 lRef = lookup("siap.bdmc.sbpren.controller.ImportaDatiInBDMCController");
			 lRemote = (IImportaDati)lRef;
			 return lRemote;
		 }
	    /*****************************************************************************
		   * Istanzia e restituisce l'interfaccia del controller NotificheSiesController
		   * @return Un'istanza dell'interfaccia del controller NotificheSiesBdmcController
		   * @throws F3BException
		   ****************************************************************************/
		 
		public static INotificheSies getNotificheSiesRemote() throws F3BException {
		    Object lRef;
		    INotificheSies lRemote;
		    lRef = lookup("siap.bdmc.notifichesies.controller.NotificheSiesController");
		    lRemote = (INotificheSies)lRef;
		    return lRemote;
			  }
		
		  /*****************************************************************************
		   * Istanzia e restituisce l'interfaccia del controller StatoPrenotazioniBdmcController
		   * @return Un'istanza dell'interfaccia del controller StatoPrenotazioniBdmcController
		   * @throws F3BException
		   ****************************************************************************/
		  public static IStatoPrenotazioniBdmc getStatoPrenotazioniBdmcRemote() throws F3BException {
		    Object lRef;
		    IStatoPrenotazioniBdmc lRemote;
		    lRef = lookup("siap.bdmc.statoprenotazionibdmc.controller.StatoPrenotazioniBdmcController");
		    lRemote = (IStatoPrenotazioniBdmc)lRef;
		    return lRemote;
		  }

}

