<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>


<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>


<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.A.P.] - Elenco dei Procedimenti relativi all' Esecuzione della Misura Alternativa</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Elenco dei Procedimenti relativi all' Esecuzione della Misura Alternativa </font></td>

<%
       FascicoloGPModel fascicoloUno = (FascicoloGPModel) fascicoli.get(0);
%>

    </tr>
     <tr> </tr>
     <tr> </tr>
  <br>
   <table>
        <tr>
        <br>
        <br>
        <td class="c">N.ro Registro: <font class="label"><%=fascicoloUno.getGeneraleProcedimentoModel().getAnnoS1()%>/<%=fascicoloUno.getGeneraleProcedimentoModel().getProgrS1()%>
                     <td class="c">  relativo a: <%=fascicoloUno.getGeneraleProcedimentoModel().getDescrTipoMittenteAtto()%></td>
               </font></td> </font></td>
     </tr>


      <tr>
        <td class="c">Ordinanaza N.ro : <font class="label"><%=fascicoloUno.getFascicoloSiusModel().getChiaveAnnoS22()%>/<%=fascicoloUno.getFascicoloSiusModel().getChiaveProgrS22()%>
                        <td class="c"><font class="label"> <%=fascicoloUno.getGeneraleProcedimentoModel().getDescrUfficioAggiornamento()%>
                           del: <%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloUno.getFascicoloSiusModel().getDataIscrizione(),"dd-MM-yyyy"),"-")%>

               </font></td> </font></td>
     </tr>

       <tr>
        <td class="c">Soggetto: <font class="label"><%=fascicoloUno.getFascicoloSiusModel().getSoggetto().getCognome()%>
             <%=fascicoloUno.getFascicoloSiusModel().getSoggetto().getNome()%>
             <td class="c"><font class="label">  nato/a il: <%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloUno.getFascicoloSiusModel().getSoggetto().getDataNascita(),"dd-MM-yyyy"),"-")%>
               in : <%=fascicoloUno.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita()%>
             </font></td> </font></td>
        </tr>

         <tr>
        <td class="c">Titolo Esecutivo N.ro Siep : <font class="label"><%=fascicoloUno.getFascicoloSiusModel().getChiaveAnnoSIEP()%>/<%=fascicoloUno.getFascicoloSiusModel().getChiaveProgrSIEP()%>
          <td class="c"><font class="label"> <%=fascicoloUno.getGeneraleProcedimentoModel().getDescrAutoritaDelegata()%>
                         del: <%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloUno.getFascicoloSiusModel().getDataAggiornamento(),"dd-MM-yyyy"),"-")%>

                 </font></td>
            </font></td>
        </tr>
   </table>


  </table>

  <br>

  <table cellspacing=2 cellpadding=2>

    <tr>
      <td class="int">Numero SIUS</td>
      <td class="int">Data Iscrizione</td>
      <td class="int">Contenuto</td>
      <td class="int">Provvedimento</td>
      <td class="int">Data Emissione</td>
      <td class="int">Motivo Provvedimento</td>
      <td class="int">Esito</td>

    </tr>

<%
    Iterator itx = fascicoli.iterator();
    String sUfficio = "Procura";
%>

<%

    while ( itx.hasNext())
    {
      FascicoloGPModel fascicolo = (FascicoloGPModel)itx.next();
%>
      <tr>

        <td class="c"><font class="label">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicolo.getFascicoloSiusModel().getIdFascicoloSius()%>" Title="Dettaglio Procedimento SIUS">
            <%=fascicolo.getFascicoloSiusModel().getChiaveAnno()%>
            /
            <%=fascicolo.getFascicoloSiusModel().getChiaveProgr()%>
          </a>
        </font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getFascicoloSiusModel().getDataIscrizione(),"dd-MM-yyyy"),"-")%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font></td>
       <!-- genny 19/01/2004 utilizzo setDescrMittente come vettore per Evento.Descr_Tipo_Provvedimento -->
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrMittente()%></font></td>
       <!-- genny 12/01/2004 utilizzo setDataDefinizione come vettore per Evento.DataEmissione -->
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataDefinizione(),"dd-MM-yyyy"),"-")%></font></td>
       <!-- genny 19/01/2004 utilizzo setDescrDefinizione come vettore per Motivo Provvedimento -->
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrDefinizione()%></font></td>
       <!-- genny 19/01/2004 utilizzo setDescrRichiestaDelegazione come vettore per Evento.DescrProvvedimento -->
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrRichiestaDelegazione()%> </font></td>


      </tr>
<%
  }
%>
    </table>
  </FORM>
  <br>

  </body>
</html>