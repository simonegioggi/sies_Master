<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notiziareato.model.NotiziaReatoModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeModel"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>


<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="elenconotiziareato" scope="request" class="java.util.Vector" />
<jsp:useBean id="ListaTemplate" scope="request" class="java.lang.String"/>
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />

<jsp:useBean id="Modificabile"    scope="request" class="java.lang.String"/>
<jsp:useBean id="Stampabile"    scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"        scope="request" class="java.lang.String"/>
<jsp:useBean id="ElencoTemplate"  scope="request" class="java.lang.String"/>

<jsp:useBean id="ufficio" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ProvvedimentoEvento"		scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />

<% 
	ProvvedimentoSigeModel provvedimento = ProvvedimentoEvento.getProvvedimento();
	EventoNotificaModel lEve = ProvvedimentoEvento.getEventoNotifica();
%>
<% //EventoNotificaModel lEve = new EventoNotificaModel(eventonotifica);%>

<html>

  <head>
    <title> [S.I.E.S.] - Dettaglio Richiesta  Arresto e/o Denuncia- </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
	
  </head>

<%
if(lEve.getEvento().getFlagDocumentoRegistrato()!= null)
{
  	if (lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0){%>
   	<BODY class="corpo" onload="javascript:lookUpload();">
<%	}
	else
	{%>
	<BODY class="corpo">
<%	}
}
else
{%>
   <BODY class="corpo">
<%
}%>
    <table>

      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Richiesta Codice CUI e Cartellino Dattiloscopico</font>
        </td>
  <%     
 if ( (lEve.getEvento().getFlagDocumentoRegistrato()==null ) || (lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0))
				{%> 
    <!-- BOTTONE DI STAMPA -->
       <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIGE%>">
  					<jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
    				<jsp:param name="ValoreIdEntita" value="<%=ProvvedimentoEvento.getEventoNotifica().getEvento().getIdEvento()%>"/>
  				</jsp:include>
  	<%
       		if (Modificabile.compareTo("SI") == 0)
       		{%>
         
         		<!--- BOTTONE DI CANCELLAZIONE -->
    					<td class="LBG">
      					<a href="Javascript:conferma('siap.sige.provvedimento.action.ActCancellaProvvedimento','<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>','<%=provvedimento.getIdProvvedimentoSige()%>','TornaQui','<%=TornaQui%>');">
        					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
      					</a>
    					</td>
<%
       		}  /* endif Modificabile = SI */			
     }%>
     <!-- BOTTONE DI RITORNO -->
    		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>
  <br>
    	<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
  <br>

  <table cellspacing=4 cellpadding=4>
    <tr>
      <td class="l">Autorità Destinatarie</td>
      <td class="L">
      	<%for(int a=0;a<lEve.getNotifiche().length;a++){%>
	        <font class="campo"><%=StringUtils.toStringJSP(lEve.getNotifiche()[a].getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(lEve.getNotifiche()[a].getNote())%></font>
			<br>
		<%}%>
      </td>
    </tr>
    
    <tr>
      <td class="l">Sede di</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lEve.getNotifiche()[0].getAutoritaEsterna().getDescrSede())%></font>&nbsp;
     
      </td>
    </tr>    
    
    <tr>
				<td class="l" colspan="2">
					<table>
		    		<tr>
						<td class=c colspan=10>
							NOTIZIE DI REATO
						</td>
					</tr>
				<%
				if(elenconotiziareato.size()>0){
				%>
		    		<tr>			    			
		    			<td class="int">Num. Reg. Autorità</td>
		    			<td class="int">Descr. Fonte</td>
		    			<td class="int">Luogo provenienza</td>
		    			<td class="int">Comune Fonte</td>
		    			<td class="int">Fotosegnalato</td>
		    			<td class="int">Data Arresto</td>	
		    		</tr>
		    		<%
					String notizie = "";
					//int cont = 1;
					//Iterator itx = elenconotiziareato.iterator();
					//while ( itx.hasNext())
					//{
						notizie = "";
						//NotiziaReatoModel nreato = (NotiziaReatoModel)itx.next();
						//PRENDO SOLO L'ULTIMA NOTIZIA DI REATO INSERITA
						NotiziaReatoModel nreato = (NotiziaReatoModel)elenconotiziareato.get(elenconotiziareato.size()-1);
						notizie += "<td class=\"c\">&nbsp;" + StringUtils.toStringJSP(nreato.getNumRegAutorita()) + "&nbsp;</td>";
						notizie += "<td class=\"c\">&nbsp;" + StringUtils.toStringJSP(nreato.getDescrizioneFonte()) + "&nbsp;</td>";
						notizie += "<td class=\"c\">&nbsp;" + StringUtils.toStringJSP(nreato.getLuogoProvenienza()) + "&nbsp;</td>";
						notizie += "<td class=\"c\">&nbsp;" + StringUtils.toStringJSP(nreato.getDescrComuneFonte()) + "&nbsp;</td>";
						notizie += "<td class=\"c\">&nbsp;" + StringUtils.toStringJSP(nreato.getFlagFotosegnalato()) + "&nbsp;</td>";
						notizie += "<td class=\"c\">&nbsp;" + StringUtils.toStringJSP(DateUtils.getDateToString(nreato.getDataArresto(), "dd-MM-yyyy")) + "&nbsp;</td>";
					
						//cont = cont + 1;
						%>
						<tr>
						<%=notizie%>
						</tr>
						<%						
					//}
				}
				else{%>
					<tr>
						<td class="int" width="1000">						
							NESSUNA NOTIZIA DI REATO INSERITA
						</td>
					</tr>
				<%	
				}
				%>
				
		    	</table>
				</td>
    		</tr>
    
    <tr>
      <td class="l">Annotazioni </td>
      <td class="L" >
      	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--<font class="campo"><%=StringUtils.toStringJSP(lEve.getNotifiche()[0].getNote())%></font>&nbsp;--%>
        <font class="campo">
        	<%if(lEve.getCampoNote()!= null && lEve.getCampoNote().length>0){ %>
        		<%=StringUtils.toStringJSP(lEve.getCampoNote()[0].getDescr())%>
        	<%}%>	
        </font>&nbsp;
      </td>
	</tr><tr>  <td class="l">Data Richiesta</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>   

<%
   	if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0){
%>
	    <tr>
	      <td class="l">Data Restituzione</td>
	      <td class="L" colspan=5>
	        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataAvvenutaNotifica(),"dd-MM-yyyy"))%></font>&nbsp;
	      </td>
	    </tr>
<%
	}
%>
  </table>

<%
  if( lEve.getEvento().getFlagDocumentoRegistrato() == null ||
      lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0 )
  {
    if( lEve.getEvento().getTemIdTemplate() == null )
    {
    %>
      <form name="dettaglio">
        <jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
      </form>
    <%
    }
    
  }
  %>

 <div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post">
     <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
       <tr>
        <td class="L">
          <input  class=bottone  type="submit" value="Conferma">
           <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
           <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= lEve.getEvento().getIdEvento() %>">
           <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="<%=AzioneChiamante%>">          
        </td>
       </tr>
      </table>

  </FORM>
 </div>
  <br>
</body>
</html>