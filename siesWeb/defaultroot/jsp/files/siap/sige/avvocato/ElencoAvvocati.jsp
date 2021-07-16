<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.avvocato.model.AvvocatoModel"%>

<%@ page import="siap.sige.avvocato.model.AvvocatoFascicoloSigeModel" %>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>

<jsp:useBean id="avvocato" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="Modificabile" scope="request" class="java.lang.String" />

<html>
  <table width="100%" >

<%
  if ( avvocato.size() == 0 )
  {
%>
        <td class="int" align="left">Non ci sono avvocati assegnati al procedimento.</td>
<%
  } else
  {
%>
  <div align=center>
    <tr>
      <td class="int" width=15%>Avvocato</td>
      <td class="int" width=20%>Foro </td>
      <td class="int" width=20%>Studio </td>
      <td class="int" width=20%>Tipo </td>
    </tr>
  </div>
<%

boolean isAvvocatoForoSoppresso = false;
String strAlertAvvocato = "";
int contaSoppressi = 0;
Collection listaFori = DecodificheManager.getInstance().getForoAll();

boolean isAvvocatoForoRegInde = false;
String strAlertAvvocatoReginde = "";
int contaRegInde = 0;

    Iterator itx = avvocato.iterator();
    while ( itx.hasNext())
    {
      AvvocatoSigeModel lAvvocato = (AvvocatoSigeModel)itx.next();
      
      // Controllo se l'avvocato è associato a un foro soppresso, solo se 
      // fascicolo di competenza e modificabile
      
      String attributeForo = "";
      String lSoppresso = "";
      
      String lCertRegInde = "";
      String attributeForoReginde = "";
      if ("SI".equals(Modificabile)) 
      {
        String lStatoForo = DecodificheUtils.getCodAltebyCode(listaFori, lAvvocato.getAvvocato().getForo()); 
        
        if ("SOPPRESSO".equals(lStatoForo))
        {
          isAvvocatoForoSoppresso = true;
          lSoppresso = " <font class='cRosso'>(foro "+lAvvocato.getAvvocato().getForo()+" soppresso)</font> ";
          attributeForo = "foroSoppresso='S'";
          contaSoppressi++;
          
          strAlertAvvocato+= " L’Avvocato "+StringUtils.toStringJSP(lAvvocato.getAvvocato().getCognome())+" "
                             +StringUtils.toStringJSP(lAvvocato.getAvvocato().getNome())
                             +" risulta iscritto al Foro di "
                             +StringUtils.toStringJSP(lAvvocato.getAvvocato().getForo())
                             +" soppresso a seguito dell'accorpamento degli uffici giudiziari. ";
        }
        
        // INIZIO: MEV_21 (avvocati)
        if ("NO".equals(lAvvocato.getAvvocato().getFlagRegInde())) {
           isAvvocatoForoRegInde = true;
           attributeForoReginde = "foroRegInde='S'";
           lCertRegInde = " <font class='cRosso'>(non certificato su RegInde)</font> ";
           contaRegInde++;
           strAlertAvvocatoReginde+= " L'anagrafica dell'Avvocato "+StringUtils.toStringJSP(lAvvocato.getAvvocato().getCognome())+" "
                             +StringUtils.toStringJSP(lAvvocato.getAvvocato().getNome())
                             +" non risulta certificata su RegInde. "
                             +"  ";
         
        }
        // FINE: MEV_21        
      }
      
%>
       <tr <%=attributeForo%> <%=attributeForoReginde%>>
         <td class="c" colspan=1 width=40% style="text-align:left !important;">
          	<font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getCognome()) +" "+ StringUtils.toStringJSP(lAvvocato.getAvvocato().getNome()) %></font>
         </td>
         
         <td class="c" colspan=1 width=20% style="text-align:left !important;">
          	<font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getForo()) %></font>
         </td>
         
         <td class="c" colspan=1 width=30% style="text-align:left !important;">
          	<font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getIndirizzo()) %></font>
         </td>
         
         <td class="c" colspan=1 width=30% style="text-align:left !important;">
          	<font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getDescrTipo()) %></font>
         </td>
       </tr>
<%
    } // endwhile
    
  if (isAvvocatoForoSoppresso){
    if (contaSoppressi==1)
      strAlertAvvocato += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati dell'Avvocato utilizzando le opportune funzioni.";
    else
      strAlertAvvocato += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati degli Avvocati utilizzando le opportune funzioni.";
  }
  
  // INIZIO: MEV_21 (avvocati)
  if (isAvvocatoForoRegInde){
    if (contaRegInde==1)
      strAlertAvvocatoReginde += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati dell'Avvocato utilizzando le opportune funzioni.";
    else
      strAlertAvvocatoReginde += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati degli Avvocati utilizzando le opportune funzioni.";
  }
  // FINE: MEV_21    
  
%>

  <script language="JavaScript">
<% if (isAvvocatoForoSoppresso || isAvvocatoForoRegInde ){%>

  function blinkAvvocato() {
  
    var blinks = document.getElementsByTagName('tr');
    
    for (var i = blinks.length - 1; i >= 0; i--) {
      var s = blinks[i];
      if (s.getAttribute("foroSoppresso")=="S" || s.getAttribute("foroRegInde")=="S")
        s.style.backgroundColor  = (s.style.backgroundColor == '') ? '#FFFF00' : '';
    }
    window.setTimeout(blinkAvvocato, 1000);  
  }
  
  if (document.addEventListener) document.addEventListener("DOMContentLoaded", blinkAvvocato, false);
  else if (window.addEventListener) window.addEventListener("load", blinkAvvocato, false);
  else if (window.attachEvent) window.attachEvent("onload", blinkAvvocato);
  else window.onload = blinkAvvocato;

  <% if (isAvvocatoForoSoppresso){%>
  alert("Attenzione!! <%=strAlertAvvocato%>");
  <%}%>
  
  <% if (isAvvocatoForoRegInde){ //INIZIO: MEV_21 (avvocati) %>
  alert("Attenzione!! <%=strAlertAvvocatoReginde%>");
  <%} // FINE: MEV_21 %> 
<% } %>
</script>
  
<%
  }  // endif provvedimenti.size()
%>
  </table>
</html>