<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="f3b.web.RedirectTo"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento" %>

<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.richiestaatti.action.ICostantiRichiestaAtti" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige" %>

<jsp:useBean id="TornaQui"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" scope="request" class="siap.sige.fascicolo.model.FascicoloSigeModel"/>

<jsp:useBean id="provvedimenti"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicoloSiusGP" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="flag_valida"  scope="request" class="java.lang.String"/>


<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript">

//funzione per il richiamo alla cancellazione
function conferma(a_action, a_parameter, a_entityname ,a_parameter2 ,a_entityname2)
{
   var documentoRegistrato = a_entityname2;
   if (window.confirm('Confermi la cancellazione ?'))
   {
      if (documentoRegistrato=="S")
      {
       var  desktop = window.open("<%= IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=" + a_action + "&" + a_parameter + "=" +a_entityname + "&" + a_parameter2 + "=" +a_entityname2, "Cancella_provvedimento","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
         window.parent.close();
      }else{
           str = "/jsp/Main.jsp?Action=siap.sige.richiestaatti.action.ActCancellaRichiestaEsitoParere&" +a_parameter +"=" + a_entityname;
               window.location.href=str;
      }
    }
}
</script>

<html>
<head>
  <title>[S.I.E.S.] - Esito Parere</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">
  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Esito Parere</font>
      </td>

  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

    </tr>
   </table>
<br>
<%

  if (flag_valida.equals(""))
    flag_valida="SI";
  if (FascicoloSigeEsteso != null) {
%>
   <table>
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    </tr>
 </table>
<%
  } // endif fascicoloSiusGP
%>
<br>
<%

  // Link alla Gestione Richiesta Parere 
  RedirectTo lRedir = new RedirectTo();
  lRedir.setPage(IWebConstants.PG_MAIN);
  lRedir.setAction("siap.sige.richiestaatti.action.ActRichiestaParere");
  lRedir.setParameter("Provenienza", "Elenco" );
  lRedir.setParameter("TornaQui", TornaQui );
	
  String lLinkRichiestaParere = lRedir.toString();

  if ( provvedimenti.size() == 0 )
  {
%>
          <font class="label"> Non vi sono pareri associati al  provvedimento.</font>
	        <br>
	        <br>
       	  <a class="cliccabile" href="<%=lLinkRichiestaParere%>">Richiesta Parere</a>
<%
  } else
  {
%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaAtti">
  <table width="96%">
  <div align=center>
    <tr>
      <td class="int" width=15%>Data emissione</td>
      <td class="int" width=30%>Tipo Atto</td>
      <td class="int" width=25%>Esito </td>
<%
      if (flag_valida.equals("SI"))
      {
%>
      <td class="int" width=3%>Documento<br>Validato</td>
<%    }
%>
      <td class="int" width=7%>Azioni</td>
    </tr>
  </div>
<%
    Iterator itx = provvedimenti.iterator();
    while ( itx.hasNext())
    {
      ProvvedimentoSigeEventoModel lProvEve = (ProvvedimentoSigeEventoModel)itx.next();	
      EventoNotificaModel lProv = lProvEve.getEventoNotifica();
     
      //EventoModel lProv = (EventoModel)itx.next();
%>
    <tr>
      <td class="l">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(lProv.getEvento().getDataEmissione(),"dd-MM-yyyy"),"-") %>
      </td>
      
      <td class="l" ><%=StringUtils.toStringJSP(lProv.getEvento().getDescrMotivo(),"-")%></td>
      <td class="l" ><%=StringUtils.toStringJSP(lProv.getEvento().getDescrEsito(),"-")%></td>
<%
      if (flag_valida.equals("SI"))
      {
%>
      <td class="c">
      <% if (lProv.getEvento().getFlagDocumentoRegistrato() != null && lProv.getEvento().getFlagDocumentoRegistrato().compareTo("S")==0)
         { 
         	if(lProv.getEvento().getCodEsito() == null || (lProv.getEvento().getCodEsito() != null && (lProv.getEvento().getCodEsito().equals("") || lProv.getEvento().getCodEsito().equals("-")))){
      %>
	          	<!-- Modifica del 02/03/2017 richiesta da Nunzia la possibilità di annullare la validazione, se non è presente l'esito-->
	          	<a href="Javascript:annulla('Vuoi annullare la validazione della richiesta parere? ','siap.sige.provvedimento.action.ActAnnullaValidazioneProvvedimento' ,'<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=lProv.getEvento().getIdEvento()%>');">
	          	 	<img src="/images/TickRed.gif" alt = "Annulla Validazione Richiesta Parere"  border="0">
	          	</a>
<%
	        } else {
%>
				<img src="/images/TickRed.gif" border="0">
<%	        	
	        }
         } else { %>
          -
      <% } %> </td>
<%    } // end if (flag_valida.equals("SI"))
%>
       <td class="l" >
        <%String isBlob = "SI"; if(lProv.getEvento().getFlagDocumentoRegistrato() == null){isBlob="NO";}  %>
<%
      if (flag_valida.equals("SI"))
      {
%>
        <jsp:include page="<%=ICostantiProvvedimento.PG_BUTTONS_ALTRI%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lProvEve.getProvvedimento().getIdProvvedimentoSige()%>" />
        
          
          <jsp:param name="CodTipoProvvedimento" value="<%=lProv.getEvento().getCodTipoProvvedimento()%>" />
          <jsp:param name="Stampa" value="<%=isBlob%>" />
          <jsp:param name="CodEsito" value="<%=lProv.getEvento().getCodEsito()%>" />
        </jsp:include>
<%
      }else {
%>
        <jsp:include page="<%=ICostantiProvvedimento.PG_BUTTONS_NOTIFICHE%>">
        	<jsp:param name="CampoIdEntita" value="<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lProvEve.getProvvedimento().getIdProvvedimentoSige()%>" />
          
          
          <jsp:param name="FLAG_PIU_MENO" value="<%=lProv.getEvento().getFlagPiuMeno()%>" />
        </jsp:include>
<%
}
%>
			
<%
 
%>


       </td>
    </tr>

  <%
   } // endwhile
  %>
  </table>
    </FORM>
<%
  }  // endif provvedimenti.size()
%>
  </body>
</html>