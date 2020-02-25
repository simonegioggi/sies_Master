<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione" %>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa" %>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="FasSigeDetenzione" scope="request" class="siap.sige.detenzione.model.FasSigeDetenzioneModel"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
  function Verify()
  {
      return true;
  }
</script>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Luogo Detenzione </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  </head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG>
          <font class="label">Funzione :</font>&nbsp;
              <font class="campo">Dettaglio Luogo Detenzione</font>
        </td>
        <!-- BOTTONE DI RITORNO -->
          <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

      </tr>
  </table>

  <br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    </tr>
  </table >

  	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciLuogoDetenzione">
		<table cellspacing=2 cellpadding=2>
      	<tr>
        	<td class="l">Posizione Giuridica </td>
        	<td class="l">
           		<font class="campo"><%=FascicoloSigeEsteso.getFascicoloSige().getDescrPosizioneGiuridica()%></font>
        	</td>
      	</tr>
      	<tr>
        	<td class="l">Data di Decorrenza</td>
        	<td class="l">
           		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(FasSigeDetenzione.getLuogoDetenzione().getDataInizioDetenzione(),"dd-MM-yyyy") )%></font>
        	</td>
      	</tr>
      	<tr>
        	<td class="l">Data Fine Detenzione</td>
        	<td class="l">
        		<font class="campo">
        		<%-- 20170703: modifica per la data fine: se non esiste data fine pena prendo Data Fine Detenzione--%>
		        <%
		        if (FascicoloSigeEsteso.getFascicoloSige().getDataFinePena() != null) {
		        %>
		        <%=StringUtils.toStringJSP(DateUtils.getDateToString(FascicoloSigeEsteso.getFascicoloSige().getDataFinePena(),"dd-MM-yyyy"),"-")%>
		        <%
		        } else {
		        %>
		        <%=StringUtils.toStringJSP(DateUtils.getDateToString(FasSigeDetenzione.getLuogoDetenzione().getDataFineDetenzione(),"dd-MM-yyyy"),"-")%>
		        <%
		        }
		        %>
        		</font>
        	</td>
      	</tr>
      	<tr>
        	<td class="l">Tipo Istituto</td>
        	<td class="l">
        		<font class="campo">
        		<%if(Utils.isNullObj(FasSigeDetenzione.getLuogoDetenzione()) || Utils.isNullObj(FasSigeDetenzione.getLuogoDetenzione().getIstitutoDetenzione()) || FasSigeDetenzione.getLuogoDetenzione().getIstDetIdIstitutoDetenzione().equals("") || FasSigeDetenzione.getLuogoDetenzione().getIstDetIdIstitutoDetenzione().equals("-"))
           		{%>&nbsp;-
         		<%}else{%>
              	<%=StringUtils.toStringJSP(FasSigeDetenzione.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(FasSigeDetenzione.getLuogoDetenzione().getIstitutoDetenzione().getDescrizione())%>
         		<%}%>
         		</font>
         	</td>
      	</tr>

      <tr>
        <td class="l">Altro Luogo</td>
        <td class="L">
          <font class="campo">
<%
          if( Utils.isNullObj(FasSigeDetenzione.getLuogoDetenzione().getAltroLuogo()))
            {%>&nbsp;-<%}
          else{%>
            <%=StringUtils.toStringJSP(FasSigeDetenzione.getLuogoDetenzione().getAltroLuogo())%> <%}%>
          </font>

        </td>
      </tr>

    </table>
  </form>

</body>
</html>