<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"%>

<%@ page import="siap.sico.cssa.model.CSSAModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<jsp:useBean id="notifiche" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Notifiche  </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
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
         <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActRicercaOmesseNotificheOE">
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
        	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNot.getDataAvvenutaNotifica(),"dd-MM-yyyy"))%>&nbsp;</font>
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
        	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNot.getDataAvvenutaNotifica(),"dd-MM-yyyy"))%>&nbsp;</font>
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
     	 <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNot.getDataAvvenutaNotifica(),"dd-MM-yyyy"))%>&nbsp;</font>
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
        	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNot.getDataAvvenutaNotifica(),"dd-MM-yyyy"))%>&nbsp;</font>
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
       	 <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNot.getDataAvvenutaNotifica(),"dd-MM-yyyy"))%>&nbsp;</font>
       </td>
	   </tr>
	   <tr><td>&nbsp;<td><tr>
<%    
		}      
   }
	}
%>
  </table>
</form>
</body>
</html>