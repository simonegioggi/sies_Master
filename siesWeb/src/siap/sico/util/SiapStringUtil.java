package siap.sico.util;

import java.math.BigDecimal;

public class SiapStringUtil
{
  public static String formattaQuantum(BigDecimal aAnni, BigDecimal aMesi, BigDecimal aGiorni)
  {
    StringBuffer lBuffQuantum = new StringBuffer();

    if (aAnni != null && aAnni.intValue()!= 0)
    {
      lBuffQuantum.append("Anni " + aAnni);
    }

    if (aMesi!= null && aMesi.intValue()!= 0)
    {
      lBuffQuantum.append(" Mesi " + aMesi);
    }

    if (aGiorni != null && aGiorni.intValue()!= 0)
    {
      lBuffQuantum.append(" Giorni " + aGiorni);
    }

		if ( lBuffQuantum.length() > 1 )
			return lBuffQuantum.toString();
		else
			return null;
  }

  public static String formattaQuantum(int aAnni, int aMesi, int aGiorni)
  {
    StringBuffer lBuffQuantum = new StringBuffer();

    if (aAnni != 0)
    {
      lBuffQuantum.append("Anni " + aAnni);
    }

    if (aMesi != 0)
    {
      lBuffQuantum.append(" Mesi " + aMesi);
    }

    if (aGiorni != 0)
    {
      lBuffQuantum.append(" Giorni " + aGiorni);
    }

		if ( lBuffQuantum.length() > 1 )
			return lBuffQuantum.toString();
		else
			return null;
  }

  public static String formattaCampoNote(String aDesc, String aSostituzione)
  {
    String appo = aDesc.replaceAll("##", aSostituzione);
    return appo;
  }
}