<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="data_definizione" scope="request" class="java.util.Date" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="Modificabile" scope="request" class="java.lang.String" />

<%
boolean readonly = false;
if (modalita.equalsIgnoreCase("dettaglio"))
{
   readonly = true;
   data_definizione = fascicolo.getDataArchiviazione();
}

/* Valorizzazione data minima */
 String data1;
 if (fascicolo.getDataIscrizione() != null )
  data1 = DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd/MM/yyyy");
 else if (fascicolo.getDataInserimento() != null )
	data1 = DateUtils.getDateToString(fascicolo.getDataInserimento(),"dd/MM/yyyy");
 else
	 data1 = DateUtils.getSysDate("dd/MM/yyyy");
	 
%>

<html>
  <head>
    <title>[S.I.E.S.] - Archiviazione Manuale Procedimento SIEP</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

    <script language="JavaScript">
      var desktop;
      function  Verifica()
      {
        var ritorno = true;
        var data_minima = '<%=data1%>';
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
        var data_definizione = document.LoadArchiviazioneProcedimento.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARCHIVIAZIONE%>.value+'/'+document.LoadArchiviazioneProcedimento.<%=ICostantiFascicoloSiep.CAMPO_MESE_ARCHIVIAZIONE%>.value+'/'+document.LoadArchiviazioneProcedimento.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ARCHIVIAZIONE%>.value;

          // Controllo della data di Definizione
          if (ritorno && (! ControllaData(data_definizione)))
          {
            alert('Data Archiviazione non valida: '+ data_definizione );
            return false;
          }
          // Controllo data di sistema >= Data Definizione .
          else if( !CompareDate( data_definizione, data_sistema) )
          {
            alert('Data Archiviazione non può essere superiore alla data odierna!');
            ritorno =  false;
          }
         else if ( !CompareDate( data_minima, data_definizione) )
         {
            alert("Data Archiviazione non può precedere: " + data_minima);
            ritorno =  false;
         }
        return ritorno;
      }
    </script>

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  </head>
<%
    String lAction = "";
    String lDocumento = null;
    String lTitolo = "";
    String lActRet = null;

    if (modalita.equalsIgnoreCase("inserimento"))
    {
       lAction = "siap.siep.nuovaistanza.action.ActArchiviazioneManuale";
       lTitolo = "Archiviazione Manuale Procedimento";
    } 
    else if (modalita.equalsIgnoreCase("dettaglio"))
    {
       lTitolo = "Dettaglio Archiviazione Manuale Procedimento";
    } 
    else if (modalita.equalsIgnoreCase("modifica"))
    {
       lTitolo = "Modifica Archiviazione Manuale Procedimento";
       lAction = "siap.sige.fascicolo.action.ActDefinizioneProcedimento";
    }
%>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :  </font>&nbsp;
          <font class="campo"><%=lTitolo%></font>
      </td>
<% if (modalita.equalsIgnoreCase("dettaglio"))
{
%>
      <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="Modificabile" value="<%=Modificabile%>" />
          <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getIdFascicoloSiep().toString()%>" />
          </jsp:include>
     </td>
 <% } %>
   <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
   
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadArchiviazioneProcedimento">
    <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Data Archiviazione<font class="ob">(*)</font></td>
      <td class="L">
        <input <% if (readonly) {%> readonly <%}%> value="<%=DateUtils.getDateToString(data_definizione,"dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARCHIVIAZIONE%>" onBlur="javascript:value=FillDM(value)" > /
        <input <% if (readonly) {%> readonly <%}%> value="<%=DateUtils.getDateToString(data_definizione,"MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSiep.CAMPO_MESE_ARCHIVIAZIONE %>" onBlur="javascript:value=FillDM(value)" > /
        <input <% if (readonly) {%> readonly <%}%> value="<%=DateUtils.getDateToString(data_definizione,"yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_ARCHIVIAZIONE%>" >
      </td>
    </tr>

<%
  if (!readonly)
{
%>
  <tr>
    <td>
      <input class="bottone" type="submit" value="Conferma">
    </td>
  </tr>
<%} %>
  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >

  </FORM>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadArchiviazioneProcedimento");
     //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verifica");
 </script>

  </body>
</html>