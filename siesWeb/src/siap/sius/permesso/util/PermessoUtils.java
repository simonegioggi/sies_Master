package siap.sius.permesso.util;

public class PermessoUtils {

	public static String getDescrTipoRicerca(String aValue) {

		String lDescrValue = new String();

		if (aValue.equalsIgnoreCase("ALLP"))
			lDescrValue = "Tutti";
		else if (aValue.equalsIgnoreCase("PP")) // Permesso Premio.
			lDescrValue = "Permesso Premio";
		else if (aValue.equalsIgnoreCase("PN")) // Permesso Necessità.
			lDescrValue = "Permesso Necessità";
		else if (aValue.equalsIgnoreCase("PI")) // Permesso Internati.
			lDescrValue = "Permesso Internati";
		else if (aValue.equalsIgnoreCase("LC")) // Licenza Semilibertà.
			lDescrValue = "Licenza Semilibertà";
		else if (aValue.equalsIgnoreCase("LI")) // Licenza Internati.
			lDescrValue = "Licenza Internati";
		// MEV_2023-35: aggiungo Licenza pene sostitutive (LP)
		else if (aValue.equalsIgnoreCase("LP"))
			lDescrValue = "Licenza Pene Sostitutive";

		return lDescrValue;
	}

	public static String getCodMotivo(String aValue) {

		String lCodMotivo = new String();

		if (aValue != null) {
			if (aValue.equalsIgnoreCase("PP")) // Permesso Premio
				lCodMotivo = "2020";
			else if (aValue.equalsIgnoreCase("PN")) // Permesso Necessita'
				lCodMotivo = "2021";
			else if (aValue.equalsIgnoreCase("PI")) // Permesso Intenrati
				lCodMotivo = "2680";
			else if (aValue.equalsIgnoreCase("LC")) // Licenza Semiliberta'
				lCodMotivo = "2025";
			else if (aValue.equalsIgnoreCase("LI")) // Licenza Internati
				lCodMotivo = "2450,2451,2452,2460,2461";
			else if (aValue.equalsIgnoreCase("ALLP")) // Tutti i Permessi
				lCodMotivo = "2020,2021,2680";
			// MEV_2023-35: aggiungo per Licenza pene sostitutive (LP) i codici 3130, 3150 e 3151
			else if (aValue.equalsIgnoreCase("LP"))
				lCodMotivo = "3130,3150,3151";
		}

		return lCodMotivo;
	}

	public static void main(String[] args) {
		String lCodMotivo = "2460";
		if ("2450,2451,2452,2460,2461".contains(lCodMotivo) /*> -1*/) // Licenza Internati
			System.out.println("OK contains");
		if (lCodMotivo.indexOf("2450,2451,2452,2460,2461") > -1)
			System.out.println("OK indexof");
	}
}