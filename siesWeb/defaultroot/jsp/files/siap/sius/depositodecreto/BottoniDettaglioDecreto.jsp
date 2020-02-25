<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>

<jsp:useBean id="depositoDecretoMotivazioni" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="acdest"                     scope="request" class="java.lang.String"/>
<jsp:useBean id="ElencoTemplate"     scope="request" class="java.lang.String"/>
<jsp:useBean id="AutoTemplate"     scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile"              scope="request" class="java.lang.String"/>
<jsp:useBean id="Stampabile"              scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

 <script language="JavaScript">
 /*
   function stampa()
    {
       var  link = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositodecreto.action.ActStampaEmissioneDecreto&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>&ListaTemplate=";
      var template = document.dettaglio.ListaTemplate.options[document.dettaglio.ListaTemplate.options.selectedIndex].value;
       link = link + template;
      //alert(link);
      window.location.href=link;
       return link;
    }

   function stampaDecreto()
   {
       var template = "&ListaTemplate=";
       var  hrefStampa = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositodecreto.action.ActStampaEmissioneDecreto&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>";

       if (document.dettaglio.ListaTemplate != undefined)
       {
          template =  template + document.dettaglio.ListaTemplate.value;
          hrefStampa = hrefStampa + template;
          //alert("template ->" + template);
       }
     stampa(hrefStampa);
   }
*/

 </script>

<%
if (Modificabile == null || Modificabile.trim().length() < 1)
    Modificabile = "SI";
if (Stampabile == null || Stampabile.trim().length() < 1)
    Stampabile = "SI";

String azione = "siap.sius.depositodecreto.action.ActLoadFPSEmissioneDecreto";
if (acdest.compareTo("")!= 0 )
{
   azione = acdest;
}
       if (Stampabile.compareTo("SI") == 0)
       {
         if ((ElencoTemplate != null) && (ElencoTemplate.trim().length() > 0))
         {
%>
  <!-- BOTTONE DI STAMPA -->
    <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIUS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
          <jsp:param name="ValoreIdEntita" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>"/>
          </jsp:include>

<%
         } /* endif esistenza ElencoTemplate */
       }  /* endif Stampabile = SI */
       
        /* ANGELA è stato aggiunto il bottone di modifica*/
  	if( Modificabile.compareTo("SI") == 0) 
   	{
   
%>
 	<td class="LBG">
	  <a href="/jsp/Main.jsp?Action=siap.sius.depositodecreto.action.ActLoadModificaDecreto&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>">
      	<img  align="middle" src="/images/modifica24.gif" alt="Modifica Decreto" width="24" height="24" border="0">
      </a>
    </td>    	            
<%
   	}    
    /*  FINE  ********************************************/
       if (Modificabile.compareTo("SI") == 0)
       {
%>
         <!-- BOTTONE DI CANCELLAZIONE -->
         <td class="LBG">
           <a href="Javascript:conferma('siap.sius.depositodecreto.action.ActCancellaDepositoDecreto','<%=ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO%>','<%=depositoDecretoMotivazioni.getDepositoDecreto().getIdDepositoDecreto()%>','TornaQui','<%=TornaQui%>');">
           <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
           </a>
         </td>
<%
       }  /* endif Modificabile = SI */
  %>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
