<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"%>

<%@ page import="siap.sico.cssa.model.CSSAModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<%@ page import="siap.sico.evento.model.EventoFascicoloModel"%>

<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<jsp:useBean id="notifiche" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Notifiche  </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

		<script language="JavaScript">
		function Verify()
		{
<%
			String notificheAutorita = "N";
			String notificheDifensore = "N";
			int contAvvocati = 0;
			
			for (int i =0; i<notifiche.size(); i++)
  		{
       	NotificaModel lNot = (NotificaModel)notifiche.get(i);

       	if ( 	 lNot.getCodTipoNotifica().equals("E") 
       	    && lNot.getDataAvvenutaNotifica() == null 
       	    && (   lNot.getAutEstIdAutoritaEsterna() != null 
       	        || lNot.getCssIdCssa() != null 
       	        || lNot.getUffCodUfficio() != null 
       	        || lNot.getIstDetIdIstitutoDetenzione() != null ) 
       	        )
        {
        	notificheAutorita ="S";
        }
       	else   if (   lNot.getCodTipoNotifica().equals("N") 
       	    			 && lNot.getDataAvvenutaNotifica() == null 
       	    			 && lNot.getAvvIdAvvocatoFascicoloSiep() != null )
        {
          notificheDifensore ="S";
          contAvvocati++;
        }
  		}

			if(notificheAutorita.equals("S"))
			{
%>
				if (document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value.length==1)
					document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value='0'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value;
				if (document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value.length==1)
					document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value='0'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value;

				var data_to_verify = document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value+'-'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value+'-'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.value;

		    if (!ControllaDataPassaVuota(data_to_verify) )
				{
		      alert('Data Avvenuta Notifica non valida');
					return false;
				}
<% 
			}
			
			if (notificheDifensore.equals("S") )
			{
			  if(contAvvocati == 1)
			  {
%>
				   if (document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV%>.value.length==1)
					   document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV%>.value='0'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV%>.value;
		       if (document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV%>.value.length==1)
						 document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV%>.value='0'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV%>.value;
		
				   var data_to_verifyAvvocato = document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV%>.value+'-'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV%>.value+'-'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA_AVV%>.value;
		
		       if (!ControllaDataPassaVuota(data_to_verifyAvvocato) )
				   {
		         alert('Data Avvenuta Notifica non valida');
					   return false;
				   }
<%
			  }
			  else
			  {
%>
				   if (document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV%>[0].value.length==1)
					   document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV%>[0].value='0'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV%>[0].value;
		       if (document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV%>[0].value.length==1)
						 document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV%>[0].value='0'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV%>[0].value;
		
				   var data_to_verifyAvvocatoUno = document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV%>[0].value+'-'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV%>[0].value+'-'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA_AVV%>[0].value;
		
		       if (!ControllaDataPassaVuota(data_to_verifyAvvocatoUno) )
				   {
		         alert('Data Avvenuta Notifica non valida');
					   return false;
				   }
				   
				   if (document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV%>[1].value.length==1)
					   document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV%>[1].value='0'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV%>[1].value;
		       if (document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV%>[1].value.length==1)
						 document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV%>[1].value='0'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV%>[1].value;
		
				   var data_to_verifyAvvocatoDue = document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV%>[1].value+'-'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV%>[1].value+'-'+document.LoadModificaNotifiche.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA_AVV%>[1].value;
		
		       if (!ControllaDataPassaVuota(data_to_verifyAvvocatoDue) )
				   {
		         alert('Data Avvenuta Notifica non valida');
					   return false;
				   }			   
<%
			  }
	 		}
%>

			return true;
		}
		</script>

</head>

<body class="corpo">
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadModificaNotifiche">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.notifica.action.ActAggiornaNotificheLS">
<%
	if( !notifiche.isEmpty() )
	{
%>
  	<input type="hidden" name ="idEvento" value="<%=((NotificaModel)notifiche.firstElement()).getEveIdEvento()%>">
<%
	}
%>
	  <table>
	    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
	      <td class="LBG">
	      	<font class="label">Funzione : Dettaglio Notifiche </font>&nbsp;&nbsp;
	       </td>
	       <td class="LBG">
	         <a href="javascript:history.go(-1);">
	          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
	         </a>
	       </td>
	    </tr>
	  </table>
	  <br>
	   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
	  <br>
	  <table>
<%
	  for (int i =0; i < notifiche.size(); i++)
	  {
	  	NotificaModel lNot = (NotificaModel)notifiche.get(i);
	
	  	if(lNot.getCodTipoNotifica().equals("E"))
	    {
%>
				<tr>
       		<td class="titolo" colspan="2">Condannato</td>
  			</tr>
<%
        if(lNot.getAutoritaEsterna()!= null)
        {
          AutoritaEsternaModel lAut = (AutoritaEsternaModel)lNot.getAutoritaEsterna();
%>
		      <input type="hidden" name ="idNotificaE" value="<%=lNot.getIdNotifica()%>">

					<tr>
						<td class="l">Destinatario</td>
						<td class="l"> <font class="campo"><%=lAut.getDescrTipoAutorita()%></font></td>
					</tr>
					<tr>
					  <td class="l">Data Invio</td>
						<td class="l"> <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNot.getDataInvio(),"dd-MM-yyyy"))%></font></td>
					</tr>
					<tr>
		      	<td class="l">Data Notifica</td>
		     		<td class="l">
			      <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			      <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			      <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		      	</td>
		     </tr>
				 <tr><td>&nbsp;<td><tr>
<%
				}
 		
				if(lNot.getCSSA()!= null)
		    {
			    CSSAModel lCssa= (CSSAModel)lNot.getCSSA();
%>
					<tr>
					  <td class="l">Destinatario</td>
					  <td class="l"> <font class="campo"><%=lCssa.getIndirizzo()%></font> di <font class="campo"><%=lCssa.getComune()%></font></td>
					</tr>
					<tr>
		  	    <td class="l">Data Invio</td>
					  <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNot.getDataInvio(),"dd-MM-yyyy"))%></font></td>
					</tr>
					<tr>
		        <td class="l">Data Notifica</td>
		        <td class="l">
		        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
		        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
		        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		      	</td>
		    	</tr>
					<tr><td>&nbsp;<td><tr>
<%
		    } 
				if(lNot.getUfficio()!= null)
		    {
		    	UfficioModel lCUff= (UfficioModel)lNot.getUfficio();
%>
					<tr>
				    <td class="l">Destinatario</td>
					  <td class="l"> <font class="campo"><%=lCUff.getDescrTipoUfficio()%></font></td>
					</tr>
				  <tr>
		        <td class="l">Data Invio</td>
				    <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNot.getDataInvio(),"dd-MM-yyyy"))%></font></td>
					</tr>
					<tr>
					  <td class="l">Data Notifica</td>
					  <td class="l" >
					    <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
							<input value="" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
							<input value="" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
					  </td>
					</tr>
					<tr><td>&nbsp;<td><tr>
<%
	    }
	
			if(lNot.getIstitutoDetenzione()!= null)
	    {
	    	IstitutoDetenzioneModel lIst= (IstitutoDetenzioneModel)lNot.getIstitutoDetenzione();
%>
		   <tr>
		     <td class="l">Destinatario</td>
		     <td class="l"> <font class="campo"><%=lIst.getDescrTipoIstituto()%></font> di <font class="campo"><%=lIst.getDescrComune()%></font> </td>
			 </tr>
		   <tr>
		     <td class="l">Data Invio</td>
		     <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNot.getDataInvio(),"dd-MM-yyyy"))%></font></td>
			 </tr>
			 <tr>
		     <td class="l" >Data Notifica</td>
		     <td class="l" >
		       <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
		       <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
		       <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		     </td>
		   </tr>
			 <tr><td>&nbsp;<td><tr>
<%
     }
   }
 	 else  if (lNot.getCodTipoNotifica().equals("N"))
	 {
		 if(lNot.getAvvSiep() != null)
		 {
			 AvvocatoSiepModel lAvv = (AvvocatoSiepModel)lNot.getAvvSiep();
%>
	      <input type="hidden" name ="idNotifica" value="<%=lNot.getIdNotifica()%>">
				<tr>
	      	<td class="titolo" colspan="2">Difensore</td>
	   		</tr>
	   		<tr>
	    		<td class="l">Difensore</td>
	    		<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
	    		<td class="l"> <font class="campo"><%=lAvv.getAvvocato().getCognome()%>&nbsp;<%=lAvv.getAvvocato().getNome()%></font></td>
				</tr>
	   		<tr>
	    		<td class="l">Tipo Difensore</td>
	    	 <td class="l"> <font class="campo"><%=lAvv.getAvvocato().getDescrTipo()%></font></td>
			 </tr>
	   	 <tr>
	    	 <td class="l">Data Invio</td>
	    	 <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNot.getDataInvio(),"dd-MM-yyyy"))%></font></td>
		   </tr>
		   <tr>
	   	   <td class="l">Data Notifica</td>
	       <td class="l" >
	     	   <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
	         <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
	         <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA_AVV%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	       </td>
		   </tr>
		   <tr><td>&nbsp;<td><tr>
<%    
		}      
   }
	}
%>
	 <tr>
   	 <td>
       <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.notifica.action.ActAggiornaNotificheLS">
       <input class="bottone" type="submit" name="INSERISCI" value="Conferma" >
     </td>
	 </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
   var frmvalidator  = new Validator("LoadModificaNotifiche");
<%
	 String notificheAutoritaVer = "N";
	 String notificheDifensoreVer = "N";

   for ( int i =0; i<notifiche.size(); i++ )
   {
   	 NotificaModel lNot = (NotificaModel)notifiche.get(i);

     if (   lNot.getCodTipoNotifica().equals("E") 
         && lNot.getDataAvvenutaNotifica() == null 
         && (    lNot.getAutEstIdAutoritaEsterna() != null 
    	        || lNot.getCssIdCssa() != null 
    	        || lNot.getUffCodUfficio() != null 
    	        || lNot.getIstDetIdIstitutoDetenzione() != null )    
         )
     {
       notificheAutoritaVer = "S";
     }
     else if (   lNot.getCodTipoNotifica().equals("N") 
         			&& lNot.getDataAvvenutaNotifica() == null 
         			&& lNot.getAvvIdAvvocatoFascicoloSiep() != null )
     {
       notificheDifensoreVer = "S";
     }
   }
   
	 if( notificheAutoritaVer.equals("S") )
	 {
%>
	   frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>","numeric","Il campo Giorno Data Avvenuta Notifica è numerico");
	   frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>","numeric","Il campo Mese Data Avvenuta Notifica è numerico");
	   frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>","numeric","Il campo Anno Data Avvenuta Notifica è numerico");
	   frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>","gt=1900");
	   frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>","lt=2050");
<% 
	 }

	 if( notificheDifensoreVer.equals("S") )
	 {
	   for(int i = 0; i < contAvvocati; i++)
	   {
%>
		   frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA_AVV%>","<%=i%>","numeric","Il campo Giorno Data Avvenuta Notifica è numerico");
		   frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA_AVV%>","<%=i%>","numeric","Il campo Mese Data Avvenuta Notifica è numerico");
		   frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA_AVV%>","<%=i%>","numeric","Il campo Anno Data Avvenuta Notifica è numerico");
		   frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA_AVV%>","<%=i%>","gt=1900");
		   frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA_AVV%>","<%=i%>","lt=2050");
<%
	   }
	 }
%>
   frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>