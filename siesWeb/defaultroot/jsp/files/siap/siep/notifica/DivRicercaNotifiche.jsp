<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
  
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>
  
<%
  // Occorre switchare tra Ricerca Notifiche o Ricerca Comunicazioni.
  // La selezione viene effettuata da un parametro passato alla jsp.
  boolean abilitaJSP = false;
  String lDestinatario = "";
  String lNomeDestinatario[] = new String[6];
  String lValoreDestinatario[]= new String[6];
  
  String lTipoAtto = "Notifiche";
  // Il Tipo di Atto viene passato attraverso parametro
  if (request.getParameter("TipoAtto") != null)
  {
	  lTipoAtto = request.getParameter("TipoAtto");
  
  	if (lTipoAtto.equalsIgnoreCase("Notifiche"))
  	{
		// Ricerca Notifiche 
  		lDestinatario = "UNEP";
  		String lNomeDestinatarioT[] = {"SEDE", "Altre Sedi", "Tutte","Altre Autorità (diverse da UNEP e Ist. Detenzione)","Tutte le Autorità", "Istituti di Detenzione"};
  		String lValoreDestinatarioT[] = {ICostantiNotifica.UNEP_SEDE, ICostantiNotifica.UNEP_ALTRE_SEDI, ICostantiNotifica.UNEP_TUTTI, ICostantiNotifica.UNEP_ESCLUSO,ICostantiNotifica.TUTTI,ICostantiNotifica.ISTITUTO_DETENZIONE};
  		for(int i = 0; i < 6; i++)
  		{
  			lNomeDestinatario[i] = lNomeDestinatarioT[i];
  			lValoreDestinatario[i] = lValoreDestinatarioT[i];
  		}
  		abilitaJSP = true;
  	}
  	else  if (lTipoAtto.equalsIgnoreCase("Comunicazioni"))
  	{
		// Ricerca Comunicazioni 
		// In questo caso i destinatari si differenziano tra 
		// Tribunale ed Ufficio di Sorveglianza.
		if (UtenteConnesso != null && UtenteConnesso.getUfficioUtente() != null && UtenteConnesso.getUfficioUtente().getCodTipoUfficio() != null)
		{	
			String lTipoUfficio = UtenteConnesso.getUfficioUtente().getCodTipoUfficio();
			if (lTipoUfficio.equalsIgnoreCase("TDS"))
			{
				// Ricerca Comunicazioni per TDS
				lDestinatario = "Procura Generale";
				String lNomeDestinatarioT[] = {"SEDE", "Altre Sedi", "Tutte", "Altre Autorità (diverse da Procura Generale)","Tutte le Autorità", "Procura Repubblica c/o Tribunale Ordinario SEDE"};
				String lValoreDestinatarioT[] = {ICostantiNotifica.PGCAP_SEDE, ICostantiNotifica.PGCAP_ALTRE_SEDI, ICostantiNotifica.PGCAP_TUTTI, ICostantiNotifica.PGCAP_ESCLUSO,ICostantiNotifica.TUTTI, ICostantiNotifica.PM_SEDE};
		  		for(int i = 0; i < 6; i++)
		  		{
		  			lNomeDestinatario[i] = lNomeDestinatarioT[i];
		  			lValoreDestinatario[i] = lValoreDestinatarioT[i];
		  		}
				abilitaJSP = true;
			}
			else if (lTipoUfficio.equalsIgnoreCase("UDS"))
			{
				// Ricerca Comunicazioni per UDS
				lDestinatario = "Procura Repubblica c/o Tribunale Ordinario";
				 String lNomeDestinatarioT[] = {"SEDE", "Altre Sedi", "Tutte", "Altre Autorità (diverse da Procura Rep. )","Tutte le Autorità", "Procura Generale SEDE"};
				String lValoreDestinatarioT[] = {ICostantiNotifica.PM_SEDE, ICostantiNotifica.PM_ALTRE_SEDI, ICostantiNotifica.PM_TUTTI, ICostantiNotifica.PM_ESCLUSO,ICostantiNotifica.TUTTI, ICostantiNotifica.PGCAP_SEDE};			
		  		for(int i = 0; i < 6; i++)
		  		{
		  			lNomeDestinatario[i] = lNomeDestinatarioT[i];
		  			lValoreDestinatario[i] = lValoreDestinatarioT[i];
		  		}
				abilitaJSP = true;
			}
  		}
  	}
   }
 if (abilitaJSP)
 {
  %>
    
       <table width="96%">
        <tr>
		<td class="Titolo"  colspan ="4" ><%=lTipoAtto%> relative ai seguenti provvedimenti</td>
        </tr>
       
      	<tr>
          <td class="l">
         &nbsp;&nbsp; 	Tutti <input type="radio" name="<%=ICostantiNotifica.TIPO_PROVVEDIMENTO%>" value="<%=ICostantiNotifica.TUTTI%>" checked>&nbsp;&nbsp;&nbsp; 
			Solo Decreti Citazione
			<input type="radio" name="<%=ICostantiNotifica.TIPO_PROVVEDIMENTO%>" value="<%=ICostantiNotifica.DECRETO_CITAZIONE%>">&nbsp;&nbsp;&nbsp; 
			Solo altri provvedimenti
			<input type="radio" name="<%=ICostantiNotifica.TIPO_PROVVEDIMENTO%>" value="<%=ICostantiNotifica.ALTRI_PROVVEDIMENTI%>"></td>
        </tr>
        </table>
 <br>
 
      <table width="96%">
         <tr>
		<td class="Titolo"  colspan ="4" ><%=lTipoAtto%> da trasmettere ai seguenti destinatari</td>
        </tr>
        </table>
         
         <table >
        
          <tr>
            <td class="c">
          	<%=lDestinatario %>:&nbsp;
			</td>
			 <td class="l">
			<%=lNomeDestinatario[0] %> <input type="radio" name="<%=ICostantiNotifica.TIPO_DESTINATARIO%>" value="<%=lValoreDestinatario[0]%>">&nbsp;&nbsp;&nbsp; 
			<%=lNomeDestinatario[1] %> <input type="radio" name="<%=ICostantiNotifica.TIPO_DESTINATARIO%>" value="<%=lValoreDestinatario[1]%>">&nbsp;&nbsp;&nbsp; 
			<%=lNomeDestinatario[2] %><input type="radio" name="<%=ICostantiNotifica.TIPO_DESTINATARIO%>" value="<%=lValoreDestinatario[2]%>">
           </td>
          </tr>
	
  			<tr>
            <td class="c">
          	<%=lNomeDestinatario[5] %>
            	</td>
			 <td class="l">
			<input type="radio" name="<%=ICostantiNotifica.TIPO_DESTINATARIO%>" value="<%=lValoreDestinatario[5]%>"   > </td>
            </tr>       
    
		<tr>
		     <td class="c">
          	<%=lNomeDestinatario[3]%>
          	</td>
			 <td class="l">
 			<input type="radio" name="<%=ICostantiNotifica.TIPO_DESTINATARIO%>" value="<%=lValoreDestinatario[3]%>">
		  </td>
        </tr>
 			<tr>
            <td class="c">
          	<%=lNomeDestinatario[4] %>
            	</td>
			 <td class="l">
          	
			<input type="radio" name="<%=ICostantiNotifica.TIPO_DESTINATARIO%>" value="<%=lValoreDestinatario[4]%>"   > </td>
        </tr>       
</table>
<br>
      <table >
      
     <%  if (lTipoAtto.equalsIgnoreCase("Comunicazioni")) {%>  
 		<tr>
	        <td class="l">Seleziona solo le <%=lTipoAtto%> annotate per:</td>
	        <td class="l">
	            <select title="tipoRicorso" class=small name="<%=ICostantiNotifica.CAMPO_NOTE%>">
	            	<option value="comunicazione">comunicazione
	            	<option value="esecuzione">esecuzione
	            	<option value="" selected>-
	          	</select>
			</td>
		</tr>
<%} %>     
      
      
      
			<tr>
        	<td class="l">Seleziona solo le <%=lTipoAtto%> inserite 
			dall'utente:</td>
            <td class="l">
						<input Title="Codice Utente" type="text" name="<%=ICostantiNotifica.CAMPO_COD_OPERATORE_INSERIMENTO %>" maxlength="11" size="8" >
         	</td>
      	</tr>
    	</table>
    	
<%
} %>