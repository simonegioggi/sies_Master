package siap.sius.permesso.util;

public class PermessoUtils  {
  public static String getDescrTipoRicerca(String aValue) {
    String lDescrValue = new String();
    
    if(aValue.equalsIgnoreCase("ALLP"))
      lDescrValue = "Tutti";
    else if(aValue.equalsIgnoreCase("PP"))		// Permesso Premio.
      lDescrValue = "Permesso Premio";
    else if(aValue.equalsIgnoreCase("PN"))		// Permesso Necessità.
      lDescrValue = "Permesso Necessità";
    else if(aValue.equalsIgnoreCase("PI"))		// Permesso Internati.
      lDescrValue = "Permesso Internati";    
    else if(aValue.equalsIgnoreCase("LC"))		// Licenza Semilibertà.	
      lDescrValue = "Licenza Semilibertà";
    else if(aValue.equalsIgnoreCase("LI"))		// Licenza Internati.
    	lDescrValue = "Licenza Internati";
    
    return lDescrValue;
  } 
  
  public static String getCodMotivo( String aValue ) {
    String lCodMotivo = new String();
    
    if(aValue != null )
      if(aValue.equalsIgnoreCase("PP")) 				// Permesso Premio.
        lCodMotivo = "2020";
      else if (aValue.equalsIgnoreCase("PN"))		// Permesso Necessità.
        lCodMotivo = "2021";
      else if (aValue.equalsIgnoreCase("PI")) 	// Permesso Intenrati 
        lCodMotivo = "2680";
      else if (aValue.equalsIgnoreCase("LC"))		// Licenza Semilibertàa
        lCodMotivo = "2025";
      else if (aValue.equalsIgnoreCase("LI"))		// Licenza Internati
      	lCodMotivo = "2450,2451,2452,2460,2461";
      else if (aValue.equalsIgnoreCase("ALLP")) // Tutti i Permessi
        lCodMotivo = "2020,2021,2680";
      
    return lCodMotivo;
  }
}
