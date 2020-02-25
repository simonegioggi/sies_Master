<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sige.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeModel"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>

<jsp:useBean id="Modificabile"    scope="request" class="java.lang.String"/>
<jsp:useBean id="Stampabile"    scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"        scope="request" class="java.lang.String"/>
<jsp:useBean id="ElencoTemplate"  scope="request" class="java.lang.String"/>

<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="ufficio" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ProvvedimentoEvento"		scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />




<% EventoNotificaModel lEve = eventonotifica;%>
<% 
// Estrazione del provvedimento dal model strutturato
	ProvvedimentoSigeModel provvedimento = ProvvedimentoEvento.getProvvedimento();
%>
<html>

  <head>
    <title> [S.I.E.S.] - Dettaglio Richiesta Al PM - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>

  </head>


<%
  if (Stampabile == null || Stampabile.trim().length() < 1)
	Stampabile = "SI";

  if (Modificabile == null || Modificabile.trim().length() < 1)
    Modificabile = "SI";

  if(lEve.getEvento().getFlagDocumentoRegistrato()!= null)
  {
    if (lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0)
    {%>
      <BODY class="corpo" onload="javascript:lookUpload();">
  <%}
    else
    {%>
      <BODY class="corpo">
  <%}
  }else{%>
    <BODY class="corpo">
<%}%>

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Richiesta Al PM</font>
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
  		}
%>

  			<!-- BOTTONE DI RITORNO -->
    		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
  			</table>
    
  			<br>
    			<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
  			<br>

  	<table cellspacing=4 cellpadding=4>

    <tr>
      <td class="l">Data Emissione</td>
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

    <tr>
       <td class="l">Autorita Destinatario</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP( ufficio.getDescrTipoUfficio() )%></font>&nbsp;
         di
        <font class="campo"><%=StringUtils.toStringJSP( ufficio.getDescrComune())%></font>&nbsp;
      </td>
     </tr>
     <tr>
      
		</tr>
	

<%
   // Preleva le note
    if (lEve.getCampoNote()!= null && lEve.getCampoNote().length > 0 || lEve.getNotifiche().length>0 )
    {
    %>
      <tr>
      	<td class="l"> </td>
        <td class="L" colspan="5">
          Ulteriori Informazioni
        </td>
      </tr>
      
      <%
	  int lSizeNotifiche = lEve.getNotifiche().length;
      for(int count = 0;count < lSizeNotifiche; count++)
      {
		// Gestione Note
            if (lEve.getNotifiche()[count].getNote() != null )
            {
                %>
                  <tr>
                    
                    <td class="L" colspan="6">
                      <font class="campo"><%=lEve.getNotifiche()[count].getNote()%></font>&nbsp;
                    </td>
                  </tr>
                <%
             }
      }
    %>
    <%

    int lSizeCampoNote = lEve.getCampoNote().length;
    for(int count = 0;count < lSizeCampoNote; count++)
    {
      %>
      <tr>
        <td class="L" colspan="6">
          <font class="campo"><%=StringUtils.toStringJSP( lEve.getCampoNote()[count].getDescr())%></font>&nbsp;
        </td>
      </tr>
      <%
     }
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
             <tr><td class="L">
                 <input  class=bottone  type="submit" value="Conferma">
                 <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
                 <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= lEve.getEvento().getIdEvento() %>">
                 <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sige.richiestaatti.action.ActDettaglioRichiestaPM">
                 
                 </td> </tr>
              </table>

          </FORM>
      </div>
    <br>
</body>

</html>