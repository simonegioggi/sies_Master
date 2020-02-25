<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.unificazione.action.ICostantiUnificazione"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>

<jsp:useBean id="fasUnificante" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="fasDaUnificare" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<html>
  <head>
  <title>[S.I.E.S.] - Unificazione Soggetti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
    function Verify()
    {
      // Controllo selezione Casella di controllo.
      if (document.LoadInsUnificazioneSoggetti.<%=ICostantiUnificazione.CAMPO_CHECKBOX%>.checked == false)
      {
        alert ("Non è stata richiesta l'unificazione. ");
        return false;
      }

      // Controllo soggetti.
      // Vincenzo 25/01/2007 Controllo di uguaglianza soggetti esteso alla data e luogo di nascita.
      var soggettoUnificante = '<%=fasUnificante.getFascicoloSiusModel().getSoggetto().getNome()%>'+'<%=fasUnificante.getFascicoloSiusModel().getSoggetto().getCognome()%>';
      var soggettoDaUnificare = '<%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getNome()%>'+'<%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCognome()%>';
      var dataSoggettoUnificante = '<%=DateUtils.getDateToString(fasUnificante.getFascicoloSiusModel().getSoggetto().getDataNascita(),  "dd-MM-yyyy" ) %>';
      var dataSoggettoDaUnificare = '<%=DateUtils.getDateToString(fasDaUnificare.getFascicoloSiusModel().getSoggetto().getDataNascita(),  "dd-MM-yyyy" ) %>';
      var luogoSoggettoUnificante = '<%=fasUnificante.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita()%>'+'<%=fasUnificante.getFascicoloSiusModel().getSoggetto().getCodStatoNascita()%>';
      var luogoSoggettoDaUnificare = '<%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita()%>'+'<%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCodStatoNascita()%>';
      if (soggettoUnificante != soggettoDaUnificare ||
          dataSoggettoUnificante != dataSoggettoDaUnificare ||
          luogoSoggettoUnificante != luogoSoggettoDaUnificare )
      {
        if(! confirm("I soggetti non sono riferiti allo stesso nominativo. Si vuole continuare ?" ) )
          return false;
      }

      if(! confirm("Saranno Unificati i Soggetti riferiti ai due Procedimenti. Si vuole continuare ?" ) )
        return false;
    return true;
    }
  </script>

  </head>

  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Unificazione Soggetti</font>
        </td>

        <!-- BOTTONE DI RITORNO -->
        <td class="LBG">
          <a href="javascript:history.go(-1);">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>

      </tr>
    </table>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadInsUnificazioneSoggetti'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.unificazione.action.ActInsUnificazioneSoggetti">
    <input type="HIDDEN" name="<%=ICostantiUnificazione.CAMPO_ID_FASCICOLO_UNIFICANTE%>" value="<%=fasUnificante.getFascicoloSiusModel().getIdFascicoloSius()%>">
    <input type="HIDDEN" name="<%=ICostantiUnificazione.CAMPO_ID_FASCICOLO_UNIFICATO%>" value="<%=fasDaUnificare.getFascicoloSiusModel().getIdFascicoloSius()%>">
    <input type="HIDDEN" name="<%=ICostantiUnificazione.CAMPO_ID_SOGGETTO_UNIFICANTE%>" value="<%=fasUnificante.getFascicoloSiusModel().getSogIdSoggetto()%>">
    <input type="HIDDEN" name="<%=ICostantiUnificazione.CAMPO_ID_SOGGETTO_UNIFICATO%>" value="<%=fasDaUnificare.getFascicoloSiusModel().getSogIdSoggetto()%>">

 <table cellspacing=1 cellpadding=1 width="96%">
   <tr>
     <td class="LBG" colspan="3" >
       <font class="label">Estremi del Procedimento relativo al Soggetto da Unificare : </font>&nbsp;
     </td>
   </tr>
   <tr>
     <td class="label" width=15% valign=top > Procedimento </td>
     <td>
       <table cellspacing=1 cellpadding=1 width="96%">

         <tr>
           <td class="L" colspan="3"><font class="label"> Numero </font>
             <font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getChiaveAnno()%>/<%=fasDaUnificare.getFascicoloSiusModel().getChiaveProgr()%> </font>
             <font class="label">Ufficio: </font> <font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getDescrTipoUfficio()%> </font> <font class="L"> di </font><font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getDescrComuneUfficio()%> </font>
         </tr>

         <tr>
           <td class="L" colspan="3"><font class="label">Iscritto il </font> <font class="campo"> <%=DateUtils.getDateToString(fasDaUnificare.getFascicoloSiusModel().getDataIscrizione(), "dd-MM-yyyy" )%> </font></td>
         </tr>
       </table>
     </td>
   </tr>

   <tr>
     <td class="label" width=15% valign=top > Soggetto </td>
     <td>
       <table cellspacing=1 cellpadding=1 width="96%">
         <tr>
           <td class="L" colspan="3"><font class="label"> Cognome Nome </font>
             <font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCognome() %> &nbsp;<%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getNome()%> </font>
             <%
             if ((fasDaUnificare.getFascicoloSiusModel().getSoggetto().getSesso() != null ) &&
                 (fasDaUnificare.getFascicoloSiusModel().getSoggetto().getSesso() == "F"  ))
             {%>
               <font class="label"> nata il </font>
           <%}else{%>
               <font class="label"> nato il </font>
           <%}%>
             <font class="campo"><%=DateUtils.getDateToString(fasDaUnificare.getFascicoloSiusModel().getSoggetto().getDataNascita(),  "dd-MM-yyyy" ) %></font>
             <font class="label"> in </font> <font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita()%> </font>&nbsp;&nbsp;&nbsp;&nbsp;
             <font class="label">( Codice ID = </font> <font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getSogIdSoggetto()%> </font> <font class="L">)</font>
           </td>
         </tr>
         <tr>
           <td class="L" colspan="3">
           <%
           if ((fasDaUnificare.getFascicoloSiusModel().getSoggetto().getPaternita() != null ) &&
               (fasDaUnificare.getFascicoloSiusModel().getSoggetto().getPaternita().trim().length()>1   ))
           {%>
             <font class="label">Paternità </font> <font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getPaternita()%> </font>
         <%}
           if ((fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCognomeMadre() != null ) &&
               (fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCognomeMadre().trim().length() > 1  ))
           {%>
           		<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
             <font class="label">Madre </font> <font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCognomeMadre()%>&nbsp;<%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getNomeMadre()%></font>
         <%}
           if ((fasDaUnificare.getFascicoloSiusModel().getSoggetto().getAttoNascita() != null ) &&
               (fasDaUnificare.getFascicoloSiusModel().getSoggetto().getAttoNascita().length() > 1  ))
           {%>
             <font class="label"> atto di nascita </font> <font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getAttoNascita()%></font>
         <%}
           if ((fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCodFiscale() != null ) &&
               (fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCodFiscale().length() > 1  ))
           {%>
             <font class="label"> cod. fiscale </font> <font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCodFiscale()%> </font>
         <%}
           if ((fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCodCs() != null ) &&
               (fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCodCs().length() > 1  ))
          {%>
             <font class="label"> codice CS </font> <font class="campo"><%=fasDaUnificare.getFascicoloSiusModel().getSoggetto().getCodCs()%> </font>
        <%}%>
         </tr>
       </table>
     </td>

   </tr>
 </table>

 <BR>

 <table cellspacing=1 cellpadding=1 width="96%">
   <tr>
     <td class="LBG" colspan="3" >
       <font class="label">Estremi del Procedimento relativo al Soggetto Unificante : </font>&nbsp;
     </td>
   </tr>
   <tr>
     <td class="label" width=15% valign=top > Procedimento </td>
     <td>
       <table cellspacing=1 cellpadding=1 width="96%">

         <tr>
           <td class="L" colspan="3"><font class="label"> Numero </font>
             <font class="campo"><%=fasUnificante.getFascicoloSiusModel().getChiaveAnno()%>/<%=fasUnificante.getFascicoloSiusModel().getChiaveProgr()%> </font>
             <font class="label">Ufficio: </font> <font class="campo"><%=fasUnificante.getFascicoloSiusModel().getDescrTipoUfficio()%> </font> <font class="L"> di </font><font class="campo"><%=fasUnificante.getFascicoloSiusModel().getDescrComuneUfficio()%> </font>
         </tr>

         <tr>
           <td class="L" colspan="3"><font class="label">Iscritto il </font> <font class="campo"> <%=DateUtils.getDateToString(fasUnificante.getFascicoloSiusModel().getDataIscrizione(), "dd-MM-yyyy" )%> </font></td>
         </tr>
       </table>
     </td>
   </tr>

   <tr>
     <td class="label" width=15% valign=top > Soggetto </td>
     <td>
       <table cellspacing=1 cellpadding=1 width="96%">
         <tr>
           <td class="L" colspan="3"><font class="label"> Cognome Nome </font>
             <font class="campo"><%=fasUnificante.getFascicoloSiusModel().getSoggetto().getCognome() %> &nbsp;<%=fasUnificante.getFascicoloSiusModel().getSoggetto().getNome()%> </font>
             <%
             if ((fasUnificante.getFascicoloSiusModel().getSoggetto().getSesso() != null ) &&
                 (fasUnificante.getFascicoloSiusModel().getSoggetto().getSesso() == "F"  ))
             {%>
               <font class="label"> nata il </font>
           <%}else{%>
               <font class="label"> nato il </font>
           <%}%>
             <font class="campo"><%=DateUtils.getDateToString(fasUnificante.getFascicoloSiusModel().getSoggetto().getDataNascita(),  "dd-MM-yyyy" ) %></font>
             <font class="label"> in </font> <font class="campo"><%=fasUnificante.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita()%> </font>&nbsp;&nbsp;&nbsp;&nbsp;
             <font class="label">( Codice ID = </font> <font class="campo"><%=fasUnificante.getFascicoloSiusModel().getSogIdSoggetto()%> </font> <font class="L">)</font>
           </td>
         </tr>
         <tr>
           <td class="L" colspan="3">
           <%
           if ((fasUnificante.getFascicoloSiusModel().getSoggetto().getPaternita() != null ) &&
               (fasUnificante.getFascicoloSiusModel().getSoggetto().getPaternita().trim().length()>1   ))
           {%>
             <font class="label">Paternità </font> <font class="campo"><%=fasUnificante.getFascicoloSiusModel().getSoggetto().getPaternita()%> </font>
         <%}
           if ((fasUnificante.getFascicoloSiusModel().getSoggetto().getCognomeMadre() != null ) &&
               (fasUnificante.getFascicoloSiusModel().getSoggetto().getCognomeMadre().trim().length() > 1  ))
           {%>
           <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
             <font class="label">Madre </font> <font class="campo"><%=fasUnificante.getFascicoloSiusModel().getSoggetto().getCognomeMadre()%>&nbsp;<%=fasUnificante.getFascicoloSiusModel().getSoggetto().getNomeMadre()%></font>
         <%}
           if ((fasUnificante.getFascicoloSiusModel().getSoggetto().getAttoNascita() != null ) &&
               (fasUnificante.getFascicoloSiusModel().getSoggetto().getAttoNascita().length() > 1  ))
           {%>
             <font class="label"> atto di nascita </font> <font class="campo"><%=fasUnificante.getFascicoloSiusModel().getSoggetto().getAttoNascita()%></font>
         <%}
           if ((fasUnificante.getFascicoloSiusModel().getSoggetto().getCodFiscale() != null ) &&
               (fasUnificante.getFascicoloSiusModel().getSoggetto().getCodFiscale().length() > 1  ))
           {%>
             <font class="label"> cod. fiscale </font> <font class="campo"><%=fasUnificante.getFascicoloSiusModel().getSoggetto().getCodFiscale()%> </font>
         <%}
           if ((fasUnificante.getFascicoloSiusModel().getSoggetto().getCodCs() != null ) &&
               (fasUnificante.getFascicoloSiusModel().getSoggetto().getCodCs().length() > 1  ))
          {%>
             <font class="label"> codice CS </font> <font class="campo"><%=fasUnificante.getFascicoloSiusModel().getSoggetto().getCodCs()%> </font>
        <%}%>
         </tr>
       </table>
     </td>

   </tr>
 </table>

 <BR>

 <table cellspacing=1 cellpadding=1 width="96%">
   <tr>
     <td class="label">Selezionare la casella di controllo per procedere all'unificazione &nbsp;
       <input type=checkbox name="<%=ICostantiUnificazione.CAMPO_CHECKBOX%>" >
     </td>
   </tr>
 </table>

 <BR>

 <table cellspacing=1 cellpadding=1 width="96%">
  <tr>
    <td>
      <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
    </td>
  </tr>

  </table>

    <input type="HIDDEN" name="<%= ICostantiUnificazione.CAMPO_ANNO_DA_UNIF %>" value="<%=fasDaUnificare.getFascicoloSiusModel().getChiaveAnno()%>">
    <input type="HIDDEN" name="<%= ICostantiUnificazione.CAMPO_NUMERO_DA_UNIF %>" value="<%=fasDaUnificare.getFascicoloSiusModel().getChiaveProgr()%>">
    <input type="HIDDEN" name="<%= ICostantiUnificazione.CAMPO_ANNO_UNIFICANTE %>" value="<%=fasUnificante.getFascicoloSiusModel().getChiaveAnno()%>">
    <input type="HIDDEN" name="<%= ICostantiUnificazione.CAMPO_NUMERO_UNIFICANTE %>" value="<%=fasUnificante.getFascicoloSiusModel().getChiaveProgr()%>">

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInsUnificazioneSoggetti");
  </script>
  </body>
</html>