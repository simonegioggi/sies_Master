<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.model.DecodeModel"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Collections"%>
<%@ page import="siap.sige.motivazioneprovvedimento.model.MotivazioneProvvedimentoSigeModel"%>
<%@ page import="siap.sige.motivazioneprovvedimento.action.ICostantiMotivazioneProvvedimento"%>

<jsp:useBean id="motivi" scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="ProvvedimentoEvento"		scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />

<%    
if (motivi.size() > 0)
{
 %>
	<table cellspacing=2 cellpadding=2 width="95%">
      <tr>
      	<td class="Titolo"colspan=4 >Indicare Motivi di Inammissibilità:</td>
      </tr>
<%
	boolean isMotivi = false;
	Vector  lMotivi = null;

	// Presenza di Motivazioni già inserite 
	if (modalita.equalsIgnoreCase("M"))
	{
		lMotivi = ProvvedimentoEvento.getMotiviProvvedSige();
		if (lMotivi != null && lMotivi.size() > 0)
		isMotivi = true;
		Collections.sort(lMotivi);
	}

      Iterator lMotivCorr = motivi.iterator();
      while (lMotivCorr.hasNext())
      {
        DecodeModel lDecod = (DecodeModel) lMotivCorr.next();
        // Codici 90, 91 trattati in maniera speciale
        if (lDecod.getCode().compareTo("90") != 0   && lDecod.getCode().compareTo("91") != 0)
        {
          String lDescrizione = lDecod.getDescription();
          lDescrizione = lDescrizione.replace('<', 'x');
          lDescrizione = lDescrizione.replace('>', 'z');
          lDescrizione = lDescrizione.replace('?', '0');
          String pat = "x0z";
          String lSubPat = "x01z";

        // Si separa la descrizione iniziale in tante quante sono separate dai campi1
        // che non devono essere più di 1
          String[] lDescrizioni = lDescrizione.split(pat);
          if (lDescrizioni != null && lDescrizioni.length > 0)
          {
%>
      <tr>
        <td class="l"><input value="<%=lDecod.getCode()%>" type="checkbox" id="CheckMotivo<%=lDecod.getCode() %>" name="<%=ICostantiMotivazioneProvvedimento.CAMPO_CK_01%>"  <%if (isMotivi &&  Collections.binarySearch(lMotivi, new MotivazioneProvvedimentoSigeModel(lDecod.getCode()) )>= 0) {%> checked <%}%> ></td>

         <td class="l">
<%
         int i = 0; // indice sottostringhe separate da campo1
         for (; i <lDescrizioni.length; i++)
         {
            String lDescrizCorr = lDescrizioni[i];

            if (i == 1 )
            {
             // Individuato campo1, solo 1
%>
          <input value="" type="text" name="<%=ICostantiMotivazioneProvvedimento.CAMPO_DESCR_MOTIVAZIONE+lDecod.getCode()%>" size=35>
<%
            }
            // Si suddivide ogni stringa in sub-stringhe separate dal secondo campo
            String[] lSubDescrizioni = lDescrizCorr.split(lSubPat);
            int j = 0; // indice sottostringhe separate da campo2
            for (; j <lSubDescrizioni.length; j++)
            {
            if (j == 1 )
            {
             // Individuato campo2, solo 1
%>
          <input value="" type="text" name="<%=ICostantiMotivazioneProvvedimento.CAMPO_ALTRA_MOTIVAZIONE+lDecod.getCode()%>" size=35>
<%
            }

%>
        <%=(lSubDescrizioni[j] != null ? lSubDescrizioni[j] : "-")%>
<%
            } // for j
         } // for i
%>

        </td>
      </tr>
<%
      } // endif
    }   // caso 90
%>
  
 <%   
 }    // endwhile
 
 // Estrazione descrizione di eventuali motivazioni di tipo 90 e 91 già presenti
 int ind90 = -1;
 int ind91 = -1;
 String lMotivo90 = "";
 String lMotivo91 = "";
 if (isMotivi)
 {
	 ind90 = Collections.binarySearch(lMotivi, new MotivazioneProvvedimentoSigeModel("90" ));
	 if (ind90 >= 0)
	 {
		 lMotivo90 = ((MotivazioneProvvedimentoSigeModel) lMotivi.get(ind90)).getAltraMotivazione();
	 }
	 ind91 = Collections.binarySearch(lMotivi, new MotivazioneProvvedimentoSigeModel("91" ));
	 if (ind91 >= 0)
	 {
		 lMotivo91 = ((MotivazioneProvvedimentoSigeModel) lMotivi.get(ind91)).getAltraMotivazione();
	 }
 }
 
 %>
 <tr>
 <td class="l"><input value="90" id="CheckMotivo90" type="checkbox" name="<%= ICostantiMotivazioneProvvedimento.CAMPO_CK_01%>"  <%if (isMotivi &&  lMotivo90.trim().length() > 0) {%> checked <%}%> ></td>
 <td class="l">
   <textarea id="AltraMotivazione90" name=<%=ICostantiMotivazioneProvvedimento.CAMPO_ALTRA_MOTIVAZIONE+"90"%> cols=90 rows=2 ><%=lMotivo90%> </textarea>
 </td>
</tr>

<tr>
 <td class="l"><input value="91" id="CheckMotivo91" type="checkbox" name="<%= ICostantiMotivazioneProvvedimento.CAMPO_CK_01%>"  <%if (isMotivi &&  lMotivo91.trim().length() > 0) {%> checked <%}%> ></td>
 <td class="l">
   <textarea id="AltraMotivazione91" name=<%=ICostantiMotivazioneProvvedimento.CAMPO_ALTRA_MOTIVAZIONE+"91"%> cols=90 rows=2 ><%=lMotivo91%>  </textarea>
 </td>
</tr>

</table>

 <%
    }   // motivi > 0
 %>
 