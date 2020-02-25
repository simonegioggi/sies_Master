<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>


<jsp:useBean id="TipoDefinizione" scope="request" class="java.lang.String" />
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="descrizione" scope="request" class="java.lang.String" />
<jsp:useBean id="data_definizione" scope="request" class="java.util.Date" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />

<%
boolean readonly = false;
if (modalita.equalsIgnoreCase("dettaglio"))
   readonly = true;
/* Estrazione della data udienza  o data iscrizione */
 String data1;
 if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd/MM/yyyy");
 else
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy");
%>

<html>
  <head>
    <title>[S.I.E.S.] - Load Definizione Procedimento SIUS</title>
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
        var data_definizione = document.LoadDefinizioneProcedimento.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.LoadDefinizioneProcedimento.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.LoadDefinizioneProcedimento.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;

          // Controllo della data di Definizione
          if (ritorno && (! ControllaData(data_definizione)))
          {
            alert('Data Definizione non valida: '+ data_definizione );
            return false;
          }
          // Controllo data di sistema >= Data Definizione .
          else if( !CompareDate( data_definizione, data_sistema) )
          {
            alert('Data Definizione non può essere superiore alla data odierna!');
            ritorno =  false;
          }
         else if ( !CompareDate( data_minima, data_definizione) )
         {
            alert("Data Definizione non può precedere: " + data_minima);
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
       lAction = "siap.sius.fascicolo.action.ActInserisciDefinizioneProcedimento";
       lTitolo = "Inserimento Definizione Procedimento";
    } else if (modalita.equalsIgnoreCase("dettaglio"))
    {
       lTitolo = "Dettaglio Definizione Procedimento";
    } else if (modalita.equalsIgnoreCase("modifica"))
    {
       lTitolo = "Modifica Definizione Procedimento";
       lAction = "siap.sius.fascicolo.action.ActInserisciDefinizioneProcedimento";
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
          <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" />
          <jsp:param name="ValoreIdEntita" value="<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>" />
          </jsp:include>
     </td>
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
<% } %>

    </tr>

    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>


  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadDefinizioneProcedimento">
    <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Data Definizione<font class="ob">(*)</font></td>
      <td class="L">
        <input <% if (readonly) {%> readonly <%}%> value="<%=DateUtils.getDateToString(data_definizione,"dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_DEFINIZIONE%>" onBlur="javascript:value=FillDM(value)" > /
        <input <% if (readonly) {%> readonly <%}%> value="<%=DateUtils.getDateToString(data_definizione,"MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_DEFINIZIONE %>" onBlur="javascript:value=FillDM(value)" > /
        <input <% if (readonly) {%> readonly <%}%> value="<%=DateUtils.getDateToString(data_definizione,"yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_DEFINIZIONE %>" >
      </td>
    </tr>

  <tr>
    <td class="l">Tipo Definizione<font class="ob">(*)</font></td>
    <td class="l">
    <select title="TipoDefinizione" name="<%=ICostantiFascicoloSius.CAMPO_TIPO_DEFINIZIONE%>" <% if (readonly) {%> disabled <%}%>  >
     <%=TipoDefinizione%>
     </select>
    </td>
  </tr>

  <tr>
    <td class="l">Ulteriore Descrizione</td>
    <td class="l">
        <input <% if (readonly) {%> readonly <%}%>  Title="Descrizione" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_DEFINIZIONE%>" value="<%=descrizione%>" size=75 >
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
    var frmvalidator = new Validator("LoadDefinizioneProcedimento");
     //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verifica");
 </script>

  </body>
</html>