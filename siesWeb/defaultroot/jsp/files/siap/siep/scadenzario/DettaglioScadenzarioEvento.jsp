<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.lang.String" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.evento.model.EventoNotificaModel" %>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica" %>
<%@ page import="siap.siep.notifica.model.NotificaModel" %>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<html>

<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"  />
<%
      EventoNotificaModel lEveMod = new EventoNotificaModel(eventonotifica);
      NotificaModel lNotMod = new NotificaModel();
%>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<head>
<title>[S.I.E.S.] - Attivazione Scadenzario Simeone</title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<!--<script language="JavaScript" src="/html/conferma.js"></script>-->
<script language="JavaScript">
function Verifica()
{
  var lLungN = document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.length;


  for(var x=0;x<lLungN;x++){

      if (document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[x].value.length==1)
        document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[x].value='0'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[x].value;
      if (document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[x].value.length==1)
        document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[x].value='0'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[x].value;

      var d1=document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[x].value+'/'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[x].value+'/'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>[x].value;
      if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>[x].checked)
      {
       if (! ControllaData(d1)&& d1.length>2)
        {
         alert('Data di Notifica non valida');
         return false;
        }
      }
}
       var cFlag=0;

   for(var tot2=0;tot2<document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.length;tot2++){
      if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>[tot2].checked)
         {cFlag++;
      }
   }

    document.DettaglioNotifica.flag.value=cFlag;
    if (cFlag>0)
    {
       document.DettaglioNotifica.submit();
    } else
    {
      alert ("Devi selezionare almeno un elemento");
    }
}
</script>
<script language="JavaScript">
function VerificaUno()
{
   if (document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value.length==1)
     document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value='0'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value;
   if (document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value.length==1)
     document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value='0'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value;

   var d1=document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value+'/'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value+'/'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.value;
   if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.checked)
   {
     if (! ControllaData(d1))
     {
       alert('Data di Notifica non valida');
       return false;
     }
   }
      if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.checked)
      {
         document.DettaglioNotifica.flag.value=1;
      } else
         document.DettaglioNotifica.flag.value=0;

   if (document.DettaglioNotifica.flag.value=="0")
   { alert ("Devi selezionare almeno un elemento");} else
     document.DettaglioNotifica.submit();
}

 function Abilita(id)
  {

    if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>[id].checked){
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[id].disabled=false;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[id].disabled=false;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>[id].disabled=false;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO%>[id].disabled=false;
    }else
    {
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[id].disabled=true;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[id].disabled=true;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>[id].disabled=true;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO%>[id].disabled=true;
    }
}
function AbilitaUno()
  {
    if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.checked){
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.disabled=false;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.disabled=false;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.disabled=false;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO%>.disabled=false;
    }else
    {
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.disabled=true;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.disabled=true;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.disabled=true;
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO%>.disabled=true;
    }
}
function ControlloCheck()
{
  var check=document.getElementById('<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>');
  if (check != null){
  if (document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.length>1)
  {
    for(var totcheck=0;totcheck<document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.length;totcheck++)
    {
      if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>[totcheck].checked )
      {
        document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[totcheck].disabled=false;
        document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[totcheck].disabled=false;
        document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>[totcheck].disabled=false;
        document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO%>[totcheck].disabled=false;

      }
    }
  }else
     if(document.DettaglioNotifica.<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>.checked )
      {
        document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.disabled=false;
        document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.disabled=false;
        document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.disabled=false;
        document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO%>.disabled=false;

       }
  }
}
</script>
</head>

<body class="corpo"  onLoad="javascript:ControlloCheck()">
<FORM name="comandi">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Attivazione Scadenzario Simeone</font>
      </td>
     </tr>
   </table>
</FORM>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <form name="DettaglioNotifica" method="POST" action="/jsp/Main.jsp">
    <table cellspacing=2 cellpadding=2>
       <%  int lLungNot = lEveMod.getNotifiche().length;
           boolean vedosubmit=false;
           int contaabilita = 0;
           int totabilita = 0;
           int PosAvvocato = 0;

           for(int i=0;i<lLungNot;i++)
           {
             if(lEveMod.getNotifiche()[i].getDataAvvenutaNotifica() == null)
             {
               contaabilita++;
             }
           }
           for(int i=0;i<lLungNot;i++)
           {
        %> <tr>
             <input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lEveMod.getNotifiche()[i].getEveIdEvento()%>">
              <td class="l">Data Invio</td>
              <td class="l"><font class="campo"><%=DateUtils.getDateToString(lEveMod.getNotifiche()[i].getDataInvio(),"dd/MM/yyyy")%></font></td>

         <%
           //Controllo se Esiste la Data di Avvenuta Notifica
           if(lEveMod.getNotifiche()[i].getDataAvvenutaNotifica() != null)
             { %>
               <td class="l">Data Notifica</td>
               <td class="l"><font class="campo"><%=DateUtils.getDateToString(lEveMod.getNotifiche()[i].getDataAvvenutaNotifica(),"dd/MM/yyyy")%></font></td>
             </tr>
             <tr>

        <%   if(lEveMod.getNotifiche()[i].getSogIdSoggetto() !=null)
          {
           if(fascicolo.getSoggetto() != null)
             {
        %>    <td class="l">Soggetto</td>
              <td class="l"><font class="campo"><%=fascicolo.getSoggetto().getCognome()%></font>&nbsp;<font class="campo"><%=fascicolo.getSoggetto().getNome()%></font></td>
        <%
             }
            }
        else if(lEveMod.getNotifiche()[i].getUfficio()!= null)
             {
       %>   <td class="l">Ufficio</td>
             <td class="l"><font class="campo"><%=lEveMod.getNotifiche()[i].getUfficio().getDescrTipoUfficio()%>&nbsp;di&nbsp; <%=lEveMod.getNotifiche()[i].getUfficio().getDescrComune()%></font></td>
       <%   }
       else    if(lEveMod.getNotifiche()[i].getAutoritaEsterna()!= null)
            {
       %>    <td class="l">Autorità Esterna</td>
             <td class="l"><font class="campo"><%=lEveMod.getNotifiche()[i].getAutoritaEsterna().getDescrTipoAutorita()%></font>&nbsp; di &nbsp; <font class="campo"><%=lEveMod.getNotifiche()[i].getAutoritaEsterna().getDescrSede()%></font></td>
       <%   }

       else  if(lEveMod.getNotifiche()[i].getAvvIdAvvocatoFascicoloSiep()!=null)
          {
          if( lEveMod.getAvvocati() !=null)
           { if( lEveMod.getAvvocati()[PosAvvocato] !=null)
             {%>

         <tr>
         <td class="l">Avvocato</td>
         <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lEveMod.getAvvocati()[PosAvvocato].getAvvocato().getCognome() +" "+lEveMod.getAvvocati()[PosAvvocato].getAvvocato().getNome())%></font>&nbsp;
         </td></tr>
       <%

        PosAvvocato++;
             }
         }
      }

       else  if(lEveMod.getMagistrato()!= null)
            {
       %>    <td class="l">Magistrato</td>
             <td class="l"><font class="campo"><%=lEveMod.getMagistrato().getCognome()%></font>&nbsp;<font class="campo"><%=lEveMod.getMagistrato().getNome()%></font></td>
       <%   }
       %>
         </tr>
         <tr>
            <td>&nbsp;</td>
         </tr>
       <%}
          else //Non Esiste la Data di Avvenuta Notifica
         {
           vedosubmit = true;
          if(contaabilita >1) //Esiste piu' di una check box
          {
        %>
           <input type="hidden" disabled name="<%=ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO%>" value="<%=lEveMod.getNotifiche()[i].getSogIdSoggetto()%>">
            <td class="l"><input type="checkbox" onclick="Javascript:Abilita('<%=totabilita%>');" name="<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>" value="<%=lEveMod.getNotifiche()[i].getIdNotifica()%>" ></td>
            <td class="l">Data Notifica</td>
            <td class="l"><font class="campo">
            <input type="text" disabled  name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>" maxlength="2" size="2">
            -
            <input type="text" disabled name="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>"  maxlength="2" size="2">
            -
            <input type="text" disabled name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>"   maxlength="4" size="4">

            </font></td>
          </tr>
            <% totabilita++;
             }
            else //Esiste  una check box
             {
                vedosubmit = true;
             %>
             <input type="hidden" disabled name="<%=ICostantiNotifica.CAMPO_SOG_ID_SOGGETTO%>" value="<%=lEveMod.getNotifiche()[i].getSogIdSoggetto()%>">
            <td class="l"><input type="checkbox" onclick="Javascript:AbilitaUno();" name="<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>" value="<%=lEveMod.getNotifiche()[i].getIdNotifica()%>" ></td>
            <td class="l">Data Notifica</td>
            <td class="l"><font class="campo">
            <input type="text" disabled  name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>" maxlength="2" size="2">
            -
            <input type="text" disabled name="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>"  maxlength="2" size="2">
            -
            <input type="text" disabled name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>"   maxlength="4" size="4">

            </font></td>
          </tr>
           <%
            }%>
          <tr>

        <%
          if(lEveMod.getNotifiche()[i].getSogIdSoggetto() !=null)
          {
             if(fascicolo.getSoggetto() != null)
             {
        %>    <td class="l">Soggetto</td>
              <td class="l"><font class="campo"><%=fascicolo.getSoggetto().getCognome()%></font>&nbsp;<font class="campo"><%=fascicolo.getSoggetto().getNome()%></font></td>
        <%
             }
          }
        else    if(lEveMod.getNotifiche()[i].getUfficio()!= null)
             {
       %>    <td class="l">Ufficio</td>
             <td class="l"><font class="campo"><%=lEveMod.getNotifiche()[i].getUfficio().getDescrTipoUfficio()%>&nbsp;di&nbsp; <%=lEveMod.getNotifiche()[i].getUfficio().getDescrComune()%></font></td>
       <%   }
       else if(lEveMod.getNotifiche()[i].getAutoritaEsterna()!= null)
            {
       %>    <td class="l">Autorita Esterna</td>
             <td class="l"><font class="campo"><%=lEveMod.getNotifiche()[i].getAutoritaEsterna().getDescrTipoAutorita()%></font>&nbsp; di &nbsp; <font class="campo"><%=lEveMod.getNotifiche()[i].getAutoritaEsterna().getDescrSede()%></font></td>

        <%
           }
       else
        if(lEveMod.getNotifiche()[i].getAvvIdAvvocatoFascicoloSiep()!=null)
          {
          if( lEveMod.getAvvocati() !=null)
           { if( lEveMod.getAvvocati()[PosAvvocato] !=null)
             {%>
      <tr>
         <td class="l">Avvocato</td>
         <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lEveMod.getAvvocati()[PosAvvocato].getAvvocato().getCognome() +" "+lEveMod.getAvvocati()[PosAvvocato].getAvvocato().getNome())%></font>&nbsp;
         </td></tr>
       <%

        PosAvvocato++;
             }
         }
      }

       else if(lEveMod.getMagistrato()!= null)
            {
       %>     <td class="l">Magistrato</td>
             <td class="l"><font class="campo"><%=lEveMod.getMagistrato().getCognome()%></font>&nbsp;<font class="campo"><%=lEveMod.getMagistrato().getNome()%></font></td>
       <%   }
       %>
         </tr>
          <tr>
            <td>&nbsp;</td>
        </tr>
      <% }%>
   <%} //---------FINE FOR %>
 <%
  if(vedosubmit){
   if(contaabilita > 1){%>
     <tr>
       <td>
         <input type="hidden" name="flag" value="">
         <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordineesecuzione.action.ActAggiornaAvvenutaNotifica">
         <input class="bottone" type="button" name="INSERISCI" value="Conferma" onClick="javascript:Verifica()">
       </td>
     </tr>
    <%}else{%>
     <tr>
      <td>
       <input type="hidden" name="flag" value="">
        <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordineesecuzione.action.ActAggiornaAvvenutaNotifica">

       <input class="bottone" type="button" name="INSERISCI" value="Conferma" onClick="javascript:VerificaUno()">
      </td>
    </tr>
  <%}}%>
  </table>
  </form>
</body>
</html>