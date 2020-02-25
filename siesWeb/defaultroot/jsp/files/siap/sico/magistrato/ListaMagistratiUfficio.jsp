<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato" %>
<%@ page import="siap.sico.magistratocompetente.action.ICostantiMagistratoCompetente" %>
<%@ page import="siap.sico.magistrato.model.MagistratoModel" %>
<%@ page import="siap.sico.w_magistrato.model.WMagistratoModel" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<jsp:useBean id="magistrati" scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita"   scope="request" class="java.lang.String" />
<jsp:useBean id="formname"   scope="request" class="java.lang.String" />
<jsp:useBean id="codnum"     scope="request" class="java.lang.String" />
  
<!-- 
n.b. Use bean commentato per evitare che, in assenza del fascicolo in sessione,
     venga istanziato un model vuoto e messo in sessione.
xxjsp:useBean id="fascicolo"  scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" / 
-->

<%
// codnum = codiceCSM del magistrato presente nella form di partenza, nel caso 
// di cambio competenza

FascicoloSiepModel fascicolo = null;
fascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");

if (fascicolo==null)
  fascicolo = new FascicoloSiepModel();

%>
<html>
  <head>
    <title>[S.I.E.S.] - Lista Magistrati</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
<%
    if (! modalita.equals("NoPop"))
    {
%>
      <script language="JavaScript">
        function insertIT(cod,cognome,nome)
        {
          window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value=cod;
          window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value=cognome;
          window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_NOME%>.value=nome;
          
          //====================================================================
          // Viene precaricato il campo Data Inizio Competenza con la data di 
          // iscrizione del fascicolo solo se iscrizione primo magistrato.
          // Se cambio magistrato va caricata con data di sistema.
          //====================================================================
          if ('<%=codnum%>' == 'null' && '<%=fascicolo.getDataIscrizione()%>' != 'null')
          {
            //var now = new Date();
            var giorno = <%=DateUtils.getDateToString(fascicolo.getDataIscrizione(), "dd")%>;
            var mese   = <%=DateUtils.getDateToString(fascicolo.getDataIscrizione(), "MM")%>;
            var anno   = <%=DateUtils.getDateToString(fascicolo.getDataIscrizione(), "yyyy")%>;

            if (giorno < 10)
            {
              giorno = '0' + giorno;
            }
            else
            {
              giorno = '' + giorno;
            }

            if (mese < 10)
            {
              mese = '0' + mese;
            }
            else
            {
              mese = '' + mese;
            }

            try {
              window.parent.opener.document.<%=formname%>.<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>.value = giorno;
              window.parent.opener.document.<%=formname%>.<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>.value = mese;
              window.parent.opener.document.<%=formname%>.<%=ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO%>.value = anno;
            }
            catch(err) {
              //alert('I campi data non esistono nella form');
            }
          }
          else
          { // sono in modifica del magistrato, il nuovo magistrato è competente da oggi
            try {
              window.parent.opener.document.<%=formname%>.<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>.value = <%=DateUtils.getDateToString(DateUtils.getSysDate(), "dd")%>;
              window.parent.opener.document.<%=formname%>.<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>.value = <%=DateUtils.getDateToString(DateUtils.getSysDate(), "MM")%>;
              window.parent.opener.document.<%=formname%>.<%=ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO%>.value = <%=DateUtils.getDateToString(DateUtils.getSysDate(), "yyyy")%>;
            }
            catch(err) {
              //alert('I campi data non esistono nella form');
            }
          }

          window.parent.close();

          return;
        }
      </script>
<%
    }
%>
  </head>
  <body class=corpo>
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Magistrati Ufficio</font></td>
      </tr>
    </table>

    <table width="100%">
      <tr>
        <td class=int>Cod</td>
        <td class=int>Nome</td>
        <!-- A8RR084 Oscurata data di nascita -->
        <!--td class=int>Data Nascita</td-->
        <td class=int>Data Fine Validità</td>
        
        <% if (! modalita.equals("NoPop")) { %>
          <td class=int>Seleziona</td>
        <% } %>
      </tr>
      
      <%
      Iterator itx = magistrati.iterator();

      while ( itx.hasNext() )
      {
        MagistratoModel lMag = (MagistratoModel)itx.next();
        
        String lStyle = "";
        String lStyleDataFine = "";
        
        if (lMag.getDataFineValidita()!=null && !DateUtils.isGreater(lMag.getDataFineValidita(),DateUtils.getSysDate()) ){
          lStyle = "style='color: #808080'";
          lStyleDataFine = "style='color: #FF0000'";
        }
      %>
      
      <tr>
        <td class=l <%=lStyle%> ><%=StringUtils.toStringJSP(lMag.getCodMagistrato(),"-")%></td>
        <td class=l <%=lStyle%> ><%=StringUtils.toStringJSP(lMag.getCognome(),"-") + " " + StringUtils.toStringJSP(lMag.getNome(),"-")%></td>
        <!-- A8RR084 Oscurata data di nascita -->
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--td class=l nowrap <%=lStyle%> ><%=DateUtils.getDateToString(lMag.getDataNascita(),"dd-MM-yyyy")%> </td--%>
        <td class=l nowrap <%=lStyleDataFine%> ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMag.getDataFineValidita(),"dd-MM-yyyy"),"&nbsp;")%></td>

        <% if (! modalita.equals("NoPop")) { %>
        <td class=c><a href="Javascript:insertIT('<%=StringUtils.cStrForJS(lMag.getCodMagistrato())%>','<%=StringUtils.cStrForJS(lMag.getCognome())%>','<%=StringUtils.cStrForJS(lMag.getNome())%>');"> <img align="middle" src="/images/fileselected.gif" border=0></a></td>
        <% } %>
      </tr>
    <%
    }
    %>
    </table>
  </body>
</html>