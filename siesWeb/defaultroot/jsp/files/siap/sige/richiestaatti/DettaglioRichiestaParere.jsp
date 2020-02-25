<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<%@ page import="siap.sius.motivazionedecreto.model.MotivazioneDecretoModel"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeModel"%>


<jsp:useBean id="eventoNotifica"  scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="actRet"  scope="request" class="java.lang.String"/>
<jsp:useBean id="codMotivo"  scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile"              scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="motivazioniDecreto" scope="request" class="java.util.Vector" />
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="ufficio" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ProvvedimentoEvento"		scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />


<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
<% EventoNotificaModel lEveEsito = eventoNotifica;%>
<% EventoNotificaModel lEve = ProvvedimentoEvento.getEventoNotifica();%>
<% 
// Estrazione del provvedimento dal model strutturato
	ProvvedimentoSigeModel provvedimento = ProvvedimentoEvento.getProvvedimento();
%>

<html>
  <head>
    <title> [S.I.E.S.] - Dettaglio Richiesta Parere </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
    function lookUpload()
    {
      var node;
      node = document.getElementById('upld');
			node.style.visibility='visible';
    }
    </script>
  </head>



  <%
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
  }
  else
  {%>
      <BODY class="corpo">
  <%}%>
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">DETTAGLIO <%=lEve.getEvento().getDescrMotivo()%></font>
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
  			</tr></table>
    
  			<br>
    			<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
  			<br>
  

  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Data Richiesta Parere</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>

<%
   	if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0){
%>
	    <tr>
	      <td class="l">Data Restituzione</td>
	      <td class="L">
	        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataAvvenutaNotifica(),"dd-MM-yyyy"))%></font>&nbsp;
	      </td>
	    </tr>
<%
	}
%>

  <%
    int lSizeNotifiche = lEve.getNotifiche().length;
    for(int count = 0;count < lSizeNotifiche; count++)
    {
      if (lEve.getNotifiche()[count].getUfficio() != null )
        {%>
          <tr>
            <td class="l">Destinatario</td>
            <td class="L" >
              <font class="campo"><%=lEve.getNotifiche()[count].getUfficio().getDescrTipoUfficio()%></font>&nbsp;
            </td>

            <td class="l">Luogo </td>
            <td class="L" >
              <font class="campo"><%=lEve.getNotifiche()[count].getUfficio().getDescrComune()%></font>&nbsp;
            </td>
          </tr>
        <%
        }else if (lEve.getNotifiche()[count].getAutoritaEsterna() != null )
        {%>
	        <tr>
  	        <td class="l">Destinatario</td>
    	      <td class="L" >
      	      <font class="campo"><%=lEve.getNotifiche()[count].getAutoritaEsterna().getDescrTipoAutorita()%></font>&nbsp;
        	  </td>
          	<td class="l">Luogo </td>
	          <td class="L" >
  	          <font class="campo"><%=lEve.getNotifiche()[count].getAutoritaEsterna().getDescrSede()%></font>&nbsp;
    	      </td>
      	  </tr>
      <%}else if (lEve.getNotifiche()[count].getCSSA() != null )
        {%>
          <tr>
            <td class="l">Destinatario</td>
            <td class="L" colspan="5">
              <font class="campo"><%=lEve.getNotifiche()[count].getCSSA().getTipo()%></font>&nbsp;
            </td>
            <td class="l">Luogo </td>
            <td class="L" colspan="5">
              <font class="campo"><%=lEve.getNotifiche()[count].getCSSA().getComune()%></font>&nbsp;
            </td>
          </tr>
      <%}%>

            <%}%>

   <%
   // Preleva le note
    if (lEve.getCampoNote()!= null && lEve.getCampoNote().length > 0 )
    {
    %>
      <tr>
       <td class="l" >Motivazioni: </td>
      </tr>
     <%
      int lung = lEve.getCampoNote().length;
      for (int i=0; i<lung; i++)
      {
       %>
      <tr>
        <td class="L" colspan="8">
          <font class="campo"><%=StringUtils.toStringJSP( lEve.getCampoNote()[i].getDescr())%></font>&nbsp;
        </td>
      </tr>
    <%
      }
    }
    %>
    
  

   <%
   // Motivazioni decreto inammissibilità
    if (motivazioniDecreto != null && motivazioniDecreto.size() > 0)
    {
    %>
      <tr>
       <td class="l" >Motivazioni di inammissibilità: </td>
      </tr>
     <%
	  int z=0;
    
      Iterator itx = motivazioniDecreto.iterator();
      while ( itx.hasNext())
      {
          MotivazioneDecretoModel motivazione = (MotivazioneDecretoModel)itx.next();

        // Stampa la descrizione motivazione se è != null
        if( motivazione.getDescrMotivazione() != null )
        {
        	
%>
          <tr>
            <td class="l" colspan=2><%=motivazione.getDescrMotivazione()%></td>
          </tr>
<%
        }
        // Stampa la descrizione  Altra motivazione se != null
        else if( motivazione.getAltraMotivazione() != null )
        {
%>
          <tr>
            <td class="l" colspan=2><%=motivazione.getAltraMotivazione()%></td>
          </tr>
<%
        }
      }
    }
%>
<%
String lEsitoParere = "";
if (lEve.getEvento().getCodEsito().compareTo("0755")==0)
{
   lEsitoParere = "Incompetenza";
}
else if (lEve.getEvento().getCodEsito().compareTo("0753")==0)
{
   lEsitoParere = "Parzialmente Favorevole";
}
else if (lEve.getEvento().getCodEsito().compareTo("0752")==0)
{
   lEsitoParere = "Contrario";
}
else if (lEve.getEvento().getCodEsito().compareTo("0751")==0)
{
   lEsitoParere = "Favorevole";
}
else
{
   lEsitoParere = "-";
}
%>
	<tr>
      <td class="l">Esito Parere</td>

       <td class="L">
        <font class="campo"><%=lEsitoParere%></font>&nbsp;
      </td>
  </tr>
   </table>

  <%
    String lAzione;
    // Azione da chiamare per l'inserimento dei dati.
    if (actRet.compareTo("")!= 0 )
    {
      lAzione = actRet;
    }
    else{
      //lAzione = "siap.sius.produzioneatti.action.ActLoadFSPRichiestaParere";
    }
  %>

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
          <input class="bottone"  type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sige.richiestaatti.action.ActLoadDettaglioRichiestaParere">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%=lEve.getEvento().getIdEvento()%>">
        </td>
      </tr>
    </table>
    
    </FORM>
    </div>
    <br>
  </body>

</html>