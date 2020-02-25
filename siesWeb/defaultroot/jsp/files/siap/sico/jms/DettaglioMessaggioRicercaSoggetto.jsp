<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.jms.util.ParserMessage"%>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.jms.messaggio.model.ContatoreEsitiModel" %>
<%@ page import="siap.jms.config.JMSProperties" %>
<%@ page import="siap.jms.jmscode.model.JmsCodeModel" %>



<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>


<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Soggetto</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Ricerca Soggetto altre BDI</font>
          </td>
          <td class="LBG">
            <a href="Javascript:history.go(-1);">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            </a>
          </td>
         </tr>
      </table>
    </FORM>
    <br>
    <table cellspacing=2 cellpadding=2>
<!----------- MESSAGGIO --------------------->
       <tr>
        <td class="Titolo" colspan=4>Dati Messaggio</td>
      </tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
      <tr>
        <td class="l"><font class="label">Id JMS</font></td>
        <td class="l"><font class="campo">< %=StringUtils.toStringJSP(Messaggio.getJmsIdMessaggio())%>&nbsp;</font></td>
     </tr>
--%>
     <tr>
        <td class="l"><font class="label">Stato Messaggio</font></td>
<%
//Ricerca su più BDI
if (Messaggio.getDescrBdiDestinataria().equals("TUTTE"))
   {
         if (Messaggio.getMessaggiCorrelati()!=null && Messaggio.getMessaggiCorrelati().size() > 0 )
        {

        if(Messaggio.getMessaggiCorrelati().lastElement() instanceof ContatoreEsitiModel)
         {
           ContatoreEsitiModel lContatore = (ContatoreEsitiModel)Messaggio.getMessaggiCorrelati().lastElement();
      %>
       <td class="l">
        <table>
        <tr><td >
         <font class="cVerde"><%=lContatore.getTrovati()%></font>&nbsp; </td><td  class="label">BDI con soggetto trovato</td> </tr>
         <tr><td class="lNoBord"><font class="cVerde"><%=lContatore.getNonTrovati()%> </font>&nbsp;</td><td  class="label" >BDI con soggetto non trovato</td></tr>
         <tr><td class="lNoBord"><font class="cRosso"><%=lContatore.getNonSpediti()%></font>&nbsp; </td><td  class="label"> BDI Rispedizione in corso </td></tr>
         <tr><td class="lNoBord"><font class="cRosso"><%=lContatore.getInAttesa()%></font> &nbsp;</td><td class="label"> BDI in attesa di risposta</td></tr>
         <tr><td class="lNoBord"><font class="cGrigio"><%=lContatore.getNonCoinvolte()%></font> &nbsp;</td><td class="label"><font class="cGrigio"> BDI non coinvolte per la ricerca</font></td></tr>
         </table>
       </td>

      <%
          }
        }
        else
           {%>
          <td class="lVerde"><font class="campo">Risposta Ricevuta&nbsp;</font></td>
<%
           }
    }
  else //Messaggio ricevuto dalla singola BDI
    {
      if(Messaggio.getMessaggioCorrelato() != null)
         {
       %>
    <% if (Messaggio.getMessaggioCorrelato().getCodEsito().equals("10000"))
       {%>
        <td class="l">Soggetto  <font class="cVerde">trovato</font>  a <%=Messaggio.getDescrBdiDestinataria()%>  </td>
     <%}%>
   <% if (Messaggio.getMessaggioCorrelato().getCodEsito().equals("10001"))
       {%>
        <td class="l">Soggetto<font class="cRosso">  non trovato</font>  a <%=Messaggio.getDescrBdiDestinataria()%>  </td>
     <%}%>
      <% if (Messaggio.getMessaggioCorrelato().getCodEsito().equals("00100"))
       {%>
         <td class="l">richiesta <font class="cRosso">non partita</font> per <%=Messaggio.getDescrBdiDestinataria()%>   </td>
     <%}%>
      <% if (Messaggio.getMessaggioCorrelato().getCodEsito().equals("01000"))
       {%>
         <td class="l">richiesta <font class="cRosso">cancellata</font> per <%=Messaggio.getDescrBdiDestinataria()%>   </td>
     <%}%>
    <%}
    else
      {%>  <td class="l"> <font class="cRosso">In attesa</font> di risposta da <%=Messaggio.getDescrBdiDestinataria()%>     </td>
      <%}
      }
%>
      </tr>
      <tr>
        <td class="l"><font class="label">Tipo Operazione</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrTipoOperazione())%>&nbsp;</font></td>
      </tr>

      <tr>
        <td class="l"><font class="label">Data Invio</font></td>
        <td class="l"><font class="campo"><%=DateUtils.getDateToString(Messaggio.getDataInvio(),"dd-MM-yyyy  HH:mm:ss")%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Utente Mittente</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getCodiceUtenteMittente())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Ufficio Mittente</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrUfficioMittente() +" "+Messaggio.getDescrSedeUfficioMittente())%>&nbsp;</font></td>
      </tr>
    </table>
<%
 // if (Messaggio.getMessaggiCorrelati() != null  || Messaggio.getMessaggioCorrelato() != null )
 // {
%>
  <table cellspacing=0 cellpadding=0 >
  <tr><td>&nbsp;</td>    </tr>

<%if(soggetto!=null)
{%>
  <tr> <td class="Titolo">Soggetto Cercato</td></tr>
   <tr>
      <td class="L" width=100%>
      
      
     <%
        if (soggetto.getCodAfis()!=null)
        {
%>       <font class="label">Codice CUI:</font>&nbsp;
<font class="campo">
          <%=soggetto.getCodAfis()%>&nbsp;
      </font>&nbsp;
<%      }
        else
        {
%>        
      <font class="label">Soggetto:</font>
      <font class="campo">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
      </font>&nbsp;
<%
        if (soggetto.getSesso().compareTo("F")==0)
        {
%>       <font class="label">nata il :</font>&nbsp;
<%      }
        else
        {
%>       <font class="label">nato il :</font>&nbsp;
<%      }

if(soggetto.getDataNascita() == null)
{
   if(soggetto.getDataNascitaPresunta().equals("S"))
     {%>
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
    <%}
    else
      {%>
      <font class="campo">***</font>&nbsp;
     <%}
   }
   else
   {%>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
 <%}%>
<font class="label">in : </font>
 <font class="campo"><%
   if (soggetto.getDescrComuneNascita()!=null && soggetto.getDescrComuneNascita().length()>1)
      {%>
        <%=soggetto.getDescrComuneNascita()%>
<%    }
   else
      {%>
        <%=soggetto.getDescrStatoNascita()%>
<%    }
   }
}
%>
      </font>
     </td>
    </tr>
 </table>

<br>
<% //Risultati
   Vector allBDI = new Vector();
   allBDI = (Vector)JMSProperties.getInstance().getAllBDI().clone();

if(Messaggio.getDescrBdiDestinataria().equals("TUTTE"))
 {
   %><table cellspacing=1 cellpadding=0><%
   if (Messaggio.getMessaggiCorrelati()!=null)
   {
 %>
    <tr>
      <td class="int">BDI</td>
      <td class="int">Data Arrivo</td>
      <td class="int">Esito</td>
      <td class="int">Dettaglio</td>
     </tr>
<%
    Iterator lItx = Messaggio.getMessaggiCorrelati().iterator();
    while (lItx.hasNext())
    {
      MessaggioModel lMessEsito = new MessaggioModel();
      try
      {
        Object lObj = lItx.next();

        if (lObj instanceof MessaggioModel )
             lMessEsito = (MessaggioModel) lObj;
        else
             lMessEsito = null;

        for (int i = 0; i < allBDI.size();i++)
          {   //elimino le BDI che hanno rispedito un esito....

          JmsCodeModel lTempMod = (JmsCodeModel)allBDI.get(i);
          if(lMessEsito!=null)
          {
          if (lTempMod.getDescrizione().equals(lMessEsito.getDescrBdiMittente()))
              { //Rimuovo BDI che hanno spedito un esito
                allBDI.removeElementAt(i);
                break;
              }
          }
          if (lTempMod.getDescrizione().equals(Messaggio.getDescrBdiMittente()))
              {//ELimino BDI che ha risposto
                allBDI.removeElementAt(i);
                break;
              }
           }/*

      if(lMessEsito!=null)
      {
          for (int i = 0; i < allBDI.size();i++)
          {   //elimino le BDI che hanno rispedito un esito....
            JmsCodeModel lTempMod = (JmsCodeModel)allBDI.get(i);
            if (lTempMod.getDescrizione().equals(lMessEsito.getDescrBdiMittente()))
              {
                allBDI.removeElementAt(i);
                //break;
             }
          }
      }*/
      }
      catch(ClassCastException ex)
      {
       // ex.printStackTrace();
        break;
      }
      ParserMessage lParser = null;

      if(lMessEsito!=null)
      {
      lParser = new ParserMessage(lMessEsito.getTreeModel());
      if ((lParser.getSoggetto() != null)||lMessEsito.getDescrEsito().equals("ELEMENTO NON TROVATO"))
      { //Una risposta c'e'...
%>     <tr>
        <td class="lVerde"><%=lMessEsito.getDescrBdiMittente()%></td>
        <td class="lVerde"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMessEsito.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>


       <% if(lMessEsito.getDescrEsito().equals("ELEMENTO TROVATO"))
       {%>
       <td class="lVerde">SOGGETTO TROVATO</td>

       <td class=C>
         <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.jms.action.ActDettaglioSoggettoTrovato&IdMessaggio=<%=lMessEsito.getIdMessaggio()%>&daElenco=NO ">
             <img src="/images/dettagli.gif" width="12" height="12" alt="Visto" border="0">
         </a>
       </td>

     <%}
      else
         {%>
            <td class="lVerde">SOGGETTO NON TROVATO</td>
            <td class="c"><font class="label">&nbsp;</font></td>
            <%
         }%>
      </tr>
<%
      }
    else
      {
      // lParser = new ParserMessage(Messaggio.getTreeModel());
%>
   <tr>
    <td class="lRosso"><%= lMessEsito.getDescrBdiMittente()%></td>
    <td class="lRosso"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMessEsito.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
    <td class="lRosso"><%= lMessEsito.getDescrEsito()%></td>
     <td class="c"><font class="label">&nbsp;</font></td>
    </tr>
    <%}%>
  <%}
    }
  } //Fine while


     Iterator lItxBdi = allBDI.iterator();
       while(lItxBdi.hasNext())
       {
         JmsCodeModel lMod = (JmsCodeModel)lItxBdi.next();%>
      <tr>
        <td class="lRosso"><%= lMod.getDescrizione()%></td>
        <td class="lRosso"><%= StringUtils.toStringJSP(DateUtils.getDateToString(Messaggio.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
        <td class="lRosso">In Attesa di risposta</td>
        <td class="c"><font class="label">&nbsp;</font></td>
       </tr>
        <%
        }
      %>

</table>
<%
} //Fine if sulle ricerche per BDI multiple
else //La ricerca non e' multipla ma effettuata su una sola BDI
{
  String lBdiDest = Messaggio.getDescrBdiDestinataria();

  if (Messaggio.getMessaggioCorrelato() != null)
  {
    %>
  <table>
    <% if (Messaggio.getMessaggioCorrelato().getCodEsito().equals("10000"))
       {%>
        <td class="l">Soggetto <font class="cVerde">trovato</font> a <%=lBdiDest%>  </td>
        <td class=C>
         <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.jms.action.ActDettaglioSoggettoTrovato&IdMessaggio=<%=Messaggio.getMessaggioCorrelato().getIdMessaggio()%>&daElenco=NO ">
             <img src="/images/dettagli.gif" width="12" height="12" alt="Visto" border="0">
         </a></td>
     <%}%>
   <% if (Messaggio.getMessaggioCorrelato().getCodEsito().equals("10001"))
       {%>
        <td class="l">Soggetto<font class="cRosso"> non trovato</font> a  <%=lBdiDest%>   </td><td></td>
     <%}%>
      <% if (Messaggio.getMessaggioCorrelato().getCodEsito().equals("00100"))
       {%>
         <td class="l">richiesta <font class="cRosso">non partita</font> per  <%=lBdiDest%>    </td><td></td>
     <%}%>
      <% if (Messaggio.getMessaggioCorrelato().getCodEsito().equals("01000"))
       {%>
         <td class="l">richiesta <font class="cRosso">cancellata</font> per <%=lBdiDest%>   </td><td></td>
     <%}%>
     </table>
<%
  }
  else
  { %><table><tr>
      <td class="l">Richiesta arrivata a  <%=lBdiDest%>  e in attesa di risposta</td></tr>
      </table>
      <%
  }
}
%>
  </body>
</html>