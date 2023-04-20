package f3b.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * <p>
 * Title: StringUtils.java
 * </p>
 * <p>
 * Description: Classe di utilità per la gestione delle Stringhe.
 * </p>
 * <p>
 * Copyright: Bull Italia Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia
 * </p>
 */
public class StringUtils {

	/**
	 * Verifica la validità di un campo di tipo String
	 * <p>
	 * 
	 * @param value
	 * @return
	 */
	public static boolean checkValidValue(String value) {
		boolean ret = false;
		if (value != null && !"".equals(value.trim())) {
			ret = true;
		}
		return ret;
	}

	/**
	 * Normalizza una stringa per lo statement SQL.
	 * <p>
	 * 
	 * @param aValue
	 *            valore da normalizzare.
	 * @return la stringa normalizzata.
	 */
	public static String convertSqlString(String aValue) {
		if(aValue == null)
			return(null);

		int first_char = aValue.indexOf("'");

		/* Da utilizzare altre class Es.: StringTokenizer */

		if (first_char != -1) {
			StringBuffer buff = new StringBuffer();

			char[] chars = aValue.toCharArray();// Non ricordo il nome del metodo
			for (int i = 0; i < chars.length; ++i) {
				if(chars[i] == '\'')
					buff.append('\'');

				buff.append(chars[i]);
			}

			return buff.toString();
		}

		return aValue;
	}

	/**
	 * Replica una stringa (n) volte.
	 * <p>
	 * 
	 * @param aValue
	 *            valore da replicare.
	 * @param nCounts
	 *            numero di volte per cui bisogna replicare la stringa desiderata.
	 * @return ritorna la stringa replicata.
	 */
	public static String replicate(String aValue, int aCounts) {
		String lRet = "";

		if (aValue == null || aCounts == 0)
			return null;

		for(int i=0; i<aCounts; i++)
			lRet += aValue;

		return lRet;
	}

	/**
	 * Adeguamento object per JSP, se l'oggetto è 0 ritorna il valore di default passato come paramentro.
	 * <p>
	 * 
	 * @param aObj
	 *            Valore controllare.
	 * @param aDefaultValue
	 *            valore di default.
	 * @return valore normalizzato.
	 */
	public static String toZerotoStringaVuota(String aObj, String aDefaultValue) {
		if( aObj.equals("0"))
			return aDefaultValue;

		return ( "" + aObj );
	}


	/**
	 * Adeguamento int per JSP, se l'intero è 0 ritorna il valore di default passato come paramentro.
	 * 
	 * @param aInt
	 *            Valore ihnt da controllare.
	 * @param aDefaultValue
	 *            valore di default.
	 * @return valore normalizzato.
	 */
	public static String intZerotoString(int aInt, String aDefaultValue) {
		if( aInt == 0)
			return aDefaultValue;

		return ( "" + aInt );
	}

	/**
	 * Adeguamento int per JSP, se l'intero è 0 ritorna il valore di default impostato a string vuota.
	 * 
	 * @param aInt
	 *            Valore ihnt da controllare.
	 * @return valore normalizzato.
	 */
	public static String intZerotoString(int aInt) {
		if( aInt == 0)
			return "";

		return ( "" + aInt );
	}


	/**
	 * Adeguamento object per JSP, se l'oggetto è null ritorna il valore di default passato come paramentro.
	 * <p>
	 * 
	 * @param aObj
	 *            Valore controllare.
	 * @param aDefaultValue
	 *            valore di default.
	 * @return valore normalizzato.
	 */
	public static String toStringJSP(Object aObj, String aDefaultValue) {
		if( aObj == null )
			return aDefaultValue;

		return ( "" + aObj );
	}


	/**
	 * Se l'oggetto passato e null ritorna uno spazio vuoto.
	 * <p>
	 * 
	 * @param aObj
	 *            oggetto da normalizzara
	 * @return valore normalizzato.
	 */
	public static String toStringJSP(Object aObj) {
		return toStringJSP( aObj, "" );
	}

	/**
	 * Normalizzazione di una stringa da utilizzare per istruzioni JavaScripts.
	 * <p>
	 * 
	 * @param aValue
	 *            stringa da normalizzare.
	 * @return stringa normalizzata.
	 */
	public static String cStrForJS(String aValue) {
		if(aValue == null || aValue == "")
			return("-");

		String lStringRitorno = aValue;

		// Normalizza le stringhe contenenti l'apice singolo
		// NOTA: Da utilizzare altre class Es.: StringTokenizer
		int firstCharApiceSingolo = aValue.indexOf("'");
		if (firstCharApiceSingolo != -1) {
			StringBuffer buff = new StringBuffer();

			char[] chars = aValue.toCharArray();// Non ricordo il nome del metodo
			for (int i = -1; i < chars.length - 1; ++i) {
				if(chars[i+1] == '\'')
					buff.append('\\');

				buff.append(chars[i+1]);
			}

			lStringRitorno = buff.toString();
		}

		// Normalizza le stringhe contenenti l'apice doppio
		int firstCharApiceDoppio = lStringRitorno.indexOf("\"");
		if (firstCharApiceDoppio != -1) {
			lStringRitorno = lStringRitorno.replaceAll("\"", "&quot;");
		}

		return lStringRitorno;
	}

	/**
	 * Ritorna valore Zero se l'oggetto è Null.
	 * <p>
	 * 
	 * @param aObj
	 *            oggetto da verificare.
	 * @return valore oggetto in formato stringa.
	 */
	public static String zeroIfNull(Object aObj) {
		if(aObj == null)
			return("0");

		return (String)aObj;
	}

	/**
	 * Effettua il replace da una Stringa sorgente di un determinato pattern di string con quella desiderata.
	 * <p>
	 * 
	 * @param aSource
	 *            Stringa Sorgente.
	 * @param aPattern
	 *            stringa token da sostituire.
	 * @param aReplace
	 *            valore con il quale sostituire il token stringa.
	 * @return la stringa manipolata.
	 */
	public static String replace(String aSource, String aPattern, String aReplace) {
		StringTokenizer lStrToken = new StringTokenizer(aSource, aPattern);
		String lReturn = new String();

		while (lStrToken.hasMoreTokens()) {
			lReturn += lStrToken.nextToken();
			if( lStrToken.hasMoreTokens() )
				lReturn += aReplace;
		}
		return lReturn;
	}

	/**
	 * Effettua una formattazione di un numero BigDecimal, in valuta Euro.
	 * <p>
	 * 
	 * @param aValue
	 *            valore da formattare.
	 * @return ritorna la formattazione in valuta Euro.
	 */
	public static String toEuroFormat(BigDecimal aValue) {
		if (aValue != null) {
			DecimalFormat lFormat = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.ITALY));

			String lNumber = lFormat.format(aValue.doubleValue());

			// return ((BigDecimal)aValue.divide(new
			// BigDecimal(1),2,java.math.BigDecimal.ROUND_HALF_UP)).toString();
			return lNumber;
		} else
			return ("0,00");
	}

	/**
	 * Effettua la codifica di una stringa affinche il browser WEB non interpreti i caratteri speciali in essa
	 * contenuti.
	 * <p>
	 * 
	 * @strIn stringa da codificare.
	 * @return ritorna la stringa codificata.
	 */
	public static String urlEncode(String strIn) {
		StringBuffer strOut = new StringBuffer(strIn.length());
		for (int i = 0; i < strIn.length(); i++) {
			char ch = strIn.charAt(i);
			switch (ch) {
			case '&':
				strOut.append("%26");
				break;
			case '=':
				strOut.append("%3D");
				break;
			case '?':
				strOut.append("%3F");
				break;
			case ' ':
				strOut.append("&20");
				break;
			default:
				strOut.append(ch);
				break;
			}
		}
		return new String(strOut);
	}


	/**
	 * Effettua la pulizia del campo migrato comune nascita estero se necessario
	 * <p>
	 * 
	 * @strIn stringa da pulire.
	 * @strConfronto stringa da confrontare.
	 * @return ritorna una stringa.
	 */
	public static String pulisciCampo(String strIn, String strConfronto) {
		String lNazione = null;
		String strOut = null;

		for (int i = 0; i < strIn.length(); i++) {
			int lPrima = strIn.indexOf("(");
			if (lPrima > 0) {
				lNazione =  strIn.substring(lPrima+1,strIn.length()-1);
				if (lNazione.equals(strConfronto)) {
					strOut =  strIn.substring(0,lPrima);
					return strOut;
				}
			}
		}
		return strIn;
	}

	/*****************************************************************************
	 * Restituisce la parte intera di un campo big decimal. Utile nella visualizzazione degli importi nelle
	 * pagine jsp quando si deve separare la parte intera da quella decimale per caricarle in due input fields
	 * separati. es: aValue = 100,01 return = 100 es: aValue = 0,01 return = 0
	 * <p>
	 * 
	 * @param aValue
	 *            valore da formattare.
	 * @return ritorna parte intera o una stringa vuota se il campo in input è null
	 ****************************************************************************/
	public static String getParteIntera (BigDecimal aValue) {
		String strValue = "";
		String strValueInt = "";
		String strValueDec = "";
		if (aValue != null)  {
			strValue = aValue.toString();

			if (!strValue.equals("")) {
				if (strValue.indexOf(".") > 0) {
					strValueInt = strValue.substring(0, strValue.indexOf("."));
					strValueDec = strValue.substring(strValue.indexOf(".") + 1);
					if (strValueDec.length() == 1)
						strValueDec = strValueDec + "0";
				} else {
					strValueInt = strValue;
					strValueDec = "00";
				}
			}
			return strValueInt;
		} else
			return ("");
	}

	/*****************************************************************************
	 * Restituisce la parte decimale di un campo big decimal. Utile nella visualizzazione degli importi nelle
	 * pagine jsp quando si deve separare la parte intera da quella decimale per caricarle in due input fields
	 * separati
	 * <p>
	 * 
	 * @param aValue
	 *            valore da formattare.
	 * @return ritorna la parte decimale.
	 ****************************************************************************/
	public static String getParteDecimale (BigDecimal aValue) {
		String strValue = "";
//		String strValueInt = "";
		String strValueDec = "";
		if (aValue != null)  {
      DecimalFormat lFormat = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.ITALY));

      strValue = lFormat.format(aValue.doubleValue());
      strValueDec = strValue.substring(strValue.indexOf(",") + 1);
      return strValueDec;
		  
		  /*
			strValue = aValue.toString();

			if (!strValue.equals("")) {
				if (strValue.indexOf(".") > 0) {
//					strValueInt = strValue.substring(0, strValue.indexOf("."));
					strValueDec = strValue.substring(strValue.indexOf(".") + 1);
					if (strValueDec.length() == 1)
						strValueDec = strValueDec + "0";
				} else {
//					strValueInt = strValue;
					strValueDec = "00";
				}
			}			
			return strValueDec;
			*/
		} else
			return ("");
	}


	/**
	 * Capitalize the first letter of a word.
	 * 
	 * @param s
	 *            java.lang.String
	 */
	public static String capitalize(String str) {
		
		if (str != null) {
			String s = str.toLowerCase();
			char chars[] = s.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			return new String(chars);
		} else
			return ("");
	}
	
	/**
	 * <p>
	 * Capitalizes all the delimiter separated words in a String. Only the first letter of each word is
	 * changed.
     *
	 * <p>
	 * The delimiters represent a set of characters understood to separate words. The first string character
	 * and the first non-delimiter character after a delimiter will be capitalized.
	 * </p>
     *
	 * <p>
	 * A <code>null</code> input String returns <code>null</code>. Capitalization uses the unicode title case,
	 * normally equivalent to upper case.
	 * </p>
     *
	 * @param str
	 *            the String to capitalize, may be null
	 * @param delimiters
	 *            set of characters to determine capitalization, null means whitespace
     * @return capitalized String, <code>null</code> if null String input
     */
    public static String capitalize(String str, char[] delimiters) {
        int delimLen = (delimiters == null ? -1 : delimiters.length);
        if (str == null || str.length() == 0 || delimLen == 0) {
            return str;
        }
        int strLen = str.length();
        StringBuffer buffer = new StringBuffer(strLen);
        boolean capitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);

            if (isDelimiter(ch, delimiters)) {
                buffer.append(ch);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer.append(Character.toTitleCase(ch));
                capitalizeNext = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }
    
    public static boolean isNullOrWhiteSpace (String str) {
    	boolean flag=false;
    	if (str==null || "".equals(str))
    		flag=true;
    	
    	return flag;
    }
    
    /**
     * Is the character a delimiter.
     *
	 * @param ch
	 *            the character to check
	 * @param delimiters
	 *            the delimiters
     * @return true if it is a delimiter
     */
    private static boolean isDelimiter(char ch, char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        }
        for (int i = 0, isize = delimiters.length; i < isize; i++) {
            if (ch == delimiters[i]) {
                return true;
            }
        }
        return false;
    }

    
   public static String encodeHTMLAll(String s)
   {
     //System.out.println(s);
     if (s==null) {
       return "";
     }
     StringBuffer out = new StringBuffer();
     for(int i=0; i<s.length(); i++)
     {
       char c = s.charAt(i);
       out.append("&#"+(int)c+";");
//       if ( (int)c>127   ){
//       if ( s.indexOf("Neonato di basso peso")>-1 ){
//         System.out.println(c+" "+(int)c);
//       }
     }
     return out.toString();
   }  
   
   
   public static String encodeHTML(String s)
   {
       if (s==null) {
         return "";
       }
       //System.out.println("s = "+s);
       StringBuffer out = new StringBuffer();
       for(int i=0; i<s.length(); i++)
       {
         char c = s.charAt(i);
         
         //System.out.println(c+" "+(int)c);
         
         if (   c=='#'
             || c=='&'
             || c==';'
             || c=='\''
             || c=='\''
             || c=='"'
             || c=='|'
             || c=='*'
             || c=='?'
             || c=='~'
             || c=='<'
             || c=='>'
             || c=='^'
             || c=='('
             || c==')'
             || c=='['
             || c==']'
             || c=='{'
             || c=='}'
             || c=='$'
             || c=='\\'
             //|| c=='´'
             || (int)c==8217
         )
         {
            out.append("&#"+(int)c+";");
         }
         else
         {
             out.append(c);
         }
       }
       return out.toString();
   }
   
   /**
    * Ticket#20230419018 - I nomi dei fogli Excel non possono contenere alcuni caratteri per cui si aggiunge tale metodo 
    * per bonificare il nome del foglio soprattutto nei casi in cui viene creato dinamicamente 
    * es con il nominativo magistrato
    * https://poi.apache.org/apidocs/dev/org/apache/poi/hssf/usermodel/HSSFWorkbook.html#createSheet-java.lang.String- 
    * 
    * POI's SpreadsheetAPI silently truncates the input argument to 31 characters.
    * @param s
    * @return
    */
   public static String encodeExcelSheetName (String sheetName)
   {
	   sheetName=sheetName.trim();
	   sheetName=sheetName.replace(":","")
			              .replace("\\", "")
			              .replace("*", "")
			              .replace("?", "")
			              .replace("/", "")
			              .replace("/", "")
			              .replace("[", "")
			              .replace("]", "");
	   
	   // Non può iniziare per ' quindi aggiungo uno spazio iniziale
	   if (sheetName.startsWith("'"))
		   sheetName = " "+sheetName; 
	   
	   // Non può terminare per ' quindi aggiungo uno spazio alla fine del nome ma solo se la lunghezza
	   // è <> 31. Infatti il nome della scheda viene trovncato automaticamente a 31 per cui 
	   // in questo caso lo spazio aggiunto verrebe rimossi e viene segnalato comunque un errore.
	   if (sheetName.endsWith("'") ) {
		   sheetName = sheetName.substring(0,sheetName.length()-1);
//		 if (sheetName.length()<31) {
//			 sheetName = sheetName+" ";
//		 }
//		 else { 
//			 sheetName = sheetName.replace("\'",""); // lo rimuovo 
//		 }
	   }
	   
	   // n.b. non si eliminano gli apostrofi perchè potrebbero essere legati al mone del magistrato es MARILU'
	   return sheetName;
   }

}