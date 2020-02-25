<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Collection"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.avvocato.model.AvvocatoFascicoloSiusModel" %>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>

<jsp:useBean id="avvocato" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="isModificabile" scope="request" class="java.lang.String" />

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
      <td class="int" width=20%>Tipo </td>
    </tr>
  </div>
<%

boolean isAvvocatoForoSoppresso = false;
String strAlertAvvocato = "";
int contaSoppressi = 0;
Collection listaFori = DecodificheManager.getInstance().getForoAll();

    Iterator itx = avvocato.iterator();
    while ( itx.hasNext())
    {
      AvvocatoModel lAvvocato = (AvvocatoModel)itx.next();
       
       
      // Controllo se l'avvocato è associato a un foro soppresso, solo se 
      // fascicolo di competenza e modificabile
      String attributeForo = "";
      String lSoppresso = "";
      if ("SI".equals(isModificabile)) 
      {
        
        String lStatoForo = DecodificheUtils.getCodAltebyCode(listaFori, lAvvocato.getForo()); 
        
        if ("SOPPRESSO".equals(lStatoForo))
        {
          isAvvocatoForoSoppresso = true;
          lSoppresso = " <font class='cRosso'>(foro "+lAvvocato.getForo()+" soppresso)</font> ";
          attributeForo = "foroSoppresso='S'";
          contaSoppressi++;
          
          strAlertAvvocato+= " L’Avvocato "+StringUtils.toStringJSP(lAvvocato.getCognome())+" "
                             +StringUtils.toStringJSP(lAvvocato.getNome())
                             +" risulta iscritto al Foro di "
                             +StringUtils.toStringJSP(lAvvocato.getForo())
                             +" soppresso a seguito dell'accorpamento degli uffici giudiziari. ";
        }
      } // end if di competenza
       
       
%>
       <tr <%=attributeForo%> >
         <td class="L" colspan=1 width=60%>
          <font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getCognome()) +" "+ StringUtils.toStringJSP(lAvvocato.getNome()) %></font><%=lSoppresso%>
         </td>
         <td class="L" colspan=1 width=40%>
          <font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getDescrTipo()) %></font>
         </td>
       </tr>
<%
    } // endwhile sui difensori
    
  if (isAvvocatoForoSoppresso){
    if (contaSoppressi==1)
      strAlertAvvocato += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati dell'Avvocato utilizzando le opportune funzioni.";
    else
      strAlertAvvocato += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati degli Avvocati utilizzando le opportune funzioni.";
  }
%>
  
  <script language="JavaScript">
<% if (isAvvocatoForoSoppresso){%>

  function blinkAvvocato() {
  
    var blinks = document.getElementsByTagName('tr');
    
    for (var i = blinks.length - 1; i >= 0; i--) {
      var s = blinks[i];
      if (s.getAttribute("foroSoppresso")=="S")
        s.style.backgroundColor  = (s.style.backgroundColor == '') ? '#FFFF00' : '';
    }
    window.setTimeout(blinkAvvocato, 1000);  
  }
  
  if (document.addEventListener) document.addEventListener("DOMContentLoaded", blinkAvvocato, false);
  else if (window.addEventListener) window.addEventListener("load", blinkAvvocato, false);
  else if (window.attachEvent) window.attachEvent("onload", blinkAvvocato);
  else window.onload = blinkAvvocato;

  alert("Attenzione!! <%=strAlertAvvocato%>");
<% } %>
</script>
  

<%
  }  // endif provvedimenti.size()
%>
  </table>
</html>