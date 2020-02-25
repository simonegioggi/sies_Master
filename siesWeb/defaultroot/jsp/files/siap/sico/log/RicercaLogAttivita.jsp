<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.regex.Pattern" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.log.model.LogAttivitaModel" %>

<jsp:useBean id="logattivita" scope="request" class="java.util.Vector" />
<jsp:useBean id="FreeText" scope="request" class="java.lang.String" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - RicercaSoggetto</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
  <style>
  .reverse {
    background-color : Blue;
    color : white;
  }
  </style>
  <body class="corpo">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>" >
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font> <font class=campo>Elenco Attività</font></td>
    </tr>
  </table>
  <br>
  <div align=center>
  <table width=100%>
    <tr>
      <td class="int" width=5%>Utente</td>
      <td class="int" width=5%>Cognome Nome</td>
      <td class="int" width=5%>IP</td>
      <td class="int" width=5%>Data/Ora</td>
      <td class="int" width=5%>Azione</td>
      <td class="int">Parametri</td>
    </tr>
<%
  Iterator itx = logattivita.iterator();
  String AzCon="";
  String[] AzSplit;
  String Rec="";
  String[] tmp;
  int partialLenght=0;

  while ( itx.hasNext() )
  {
    partialLenght=0;
    LogAttivitaModel log = (LogAttivitaModel)itx.next();
    Rec = log.getRecord();
    if (!FreeText.trim().equals(""))
    {
      Pattern p=Pattern.compile(FreeText,Pattern.CASE_INSENSITIVE);
      tmp = p.split(Rec,-5);

      for (int i=0;i<tmp.length-1;i++)
      {
         partialLenght+=tmp[i].length();
         Rec+=tmp[i]+"<font class=reverse>"+log.getRecord().substring(partialLenght,partialLenght+FreeText.length())+"</font>";
         partialLenght+=FreeText.length();
      }

      Rec += tmp[tmp.length-1];
    }

    AzSplit = log.getAzioneContestoJava().split("[.]");

    String lContestoAction = "";
    if(AzSplit.length>2)
      lContestoAction = AzSplit[2];

    String lNomeAction = "";
    if(AzSplit.length>0)
      lNomeAction = AzSplit[(AzSplit.length-1)];

    AzCon = "<font color=red>"+lContestoAction.toUpperCase()+"</font><BR>"+lNomeAction;
%>
    <tr>
      <td class=c><font class="campoSmall"><%=log.getCodOperatore()%></font></td>
      <td class=l><font class="campoSmall"><%=log.getCognome()%>&nbsp;<%=log.getNome()%></td>
      <td class=c><font class="campoSmall"><%=log.getIpUtente()%></font></td>
      <td class=c nowrap><font class="campoSmall"><%=(DateUtils.getDateToString(log.getData(),"dd-MM-yyyy HH:mm:ss")).replaceAll(" ","<br>")%></font></td>
      <td class=l><font class="campoSmall"><%=AzCon%></font></td>
      <td class=l><font class="campoSmall"><%=Rec%></font></td>

    </tr>
<%
  }
%>
  </table>
  </div>
</form>
<br>
</body>
</html>