package siap.regesies.util;

import siap.regesies.regecircostanza.controller.IRegeCircostanza;
import siap.regesies.regenotiziareato.controller.IRegeNotiziaReato;
import siap.regesies.regereato.controller.IRegeReato;
import siap.regesies.regeresidenza.controller.IRegeResidenza;
import siap.regesies.regesentenza.controller.IImportaDati;
import siap.regesies.regesentenza.controller.IRegeSentenza;
import siap.regesies.regesoggetto.controller.IRegeSoggetto;
import f3b.util.F3BException;
import f3b.util.LookupClass;


/**
* <p>Title: SIEPLookupRemote</p>
* <p>Description: Classe DAO che rappresenta la tabella RegeSentenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class RegeSiesLookupRemote extends LookupClass
{
	public static IRegeSentenza getRegeSentenzaRemote() throws F3BException
		 {
			 Object lRef;
			 IRegeSentenza lRemote;
			 lRef = lookup("siap.regesies.regesentenza.controller.RegeSentenzaController");
			 lRemote = (IRegeSentenza)lRef;
			 return lRemote;
		 }


  public static IRegeSoggetto getRegeSoggettoRemote() throws F3BException
		 {
			 Object lRef;
			 IRegeSoggetto lRemote;
			 lRef = lookup("siap.regesies.regesoggetto.controller.RegeSoggettoController");
			 lRemote = (IRegeSoggetto)lRef;
			 return lRemote;
		 }

  public static IRegeResidenza getRegeResidenzaRemote() throws F3BException
		 {
			 Object lRef;
			 IRegeResidenza lRemote;
			 lRef = lookup("siap.regesies.regeresidenza.controller.RegeResidenzaController");
			 lRemote = (IRegeResidenza)lRef;
			 return lRemote;
		 }

  public static IRegeReato getRegeReatoRemote() throws F3BException
		 {
			 Object lRef;
			 IRegeReato lRemote;
			 lRef = lookup("siap.regesies.regereato.controller.RegeReatoController");
			 lRemote = (IRegeReato)lRef;
			 return lRemote;
		 }

  public static IRegeCircostanza getRegeCircostanzaRemote() throws F3BException
		 {
			 Object lRef;
			 IRegeCircostanza lRemote;
			 lRef = lookup("siap.regesies.regecircostanza.controller.RegeCircostanzaController");
			 lRemote = (IRegeCircostanza)lRef;
			 return lRemote;
		 }

    public static IRegeNotiziaReato getRegeNotiziaReatoRemote() throws F3BException
		 {
			 Object lRef;
			 IRegeNotiziaReato lRemote;
			 lRef = lookup("siap.regesies.regenotiziareato.controller.RegeNotiziaReatoController");
			 lRemote = (IRegeNotiziaReato)lRef;
			 return lRemote;
		 }

     public static IImportaDati getImportaDati() throws F3BException
		 {
			 Object lRef;
			 IImportaDati lRemote;
			 lRef = lookup("siap.regesies.regesentenza.controller.ImportaDatiInRegeController");
			 lRemote = (IImportaDati)lRef;
			 return lRemote;
		 }

}