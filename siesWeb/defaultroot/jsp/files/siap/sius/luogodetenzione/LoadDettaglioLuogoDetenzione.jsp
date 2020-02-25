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
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="PosizioneGiuridicaLuogoDetenzioneAltraCausaModel" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = PosizioneGiuridicaLuogoDetenzioneAltraCausaModel.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = PosizioneGiuridicaLuogoDetenzioneAltraCausaModel.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = PosizioneGiuridicaLuogoDetenzioneAltraCausaModel.getAltraCausa();

  if(lPosizione == null)
  {
    lPosizione = new PosizioneGiuridicaModel();
    //lPosizione.setPrimaPosizione(true);
  }

  if(lLuogoDetenzione == null)
  {
    lLuogoDetenzione = new LuogoDetenzioneModel();
  }

  if(lAltraCausa == null)
  {
    lAltraCausa = new AltraCausaModel();
  }
%>

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
<%
            PosizioneGiuridicaModel lPosGiu = new PosizioneGiuridicaModel();
            Date lDataDecorrenza = null;

            lPosGiu = new PosizioneGiuridicaModel(lPosizione);
            lDataDecorrenza = lLuogoDetenzione.getDataInizioDetenzione();
%>
              <font class="campo">Dettaglio Luogo Detenzione</font>
        </td>
        <!-- BOTTONE DI RITORNO -->
          <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

      </tr>
  </table>

  <br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table >

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciLuogoDetenzione">
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="l">
           <font class="campo"><%=lPosizione.getDescrPosizioneGiuridica()%></font>
        </td>
      </tr>
      <tr>
        <td class="l">Data di Decorrenza</td>
        <td class="l">
           <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataDecorrenza,"dd-MM-yyyy") )%></font>
        </td>
       </tr>
      <tr>
        <td class="l">Tipo Istituto</td>
         <%if(lLuogoDetenzione.getIstDetIdIstitutoDetenzione() == null || lLuogoDetenzione.getIstDetIdIstitutoDetenzione().equals("") || lLuogoDetenzione.getIstDetIdIstitutoDetenzione().equals("-"))
           {%>
              <td class="l">
              <font class="campo"></font>
              </td>
         <%}else {%>
              <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrizione())%></font>
              </td>
         <%}%>
      </tr>

      <tr>
        <td class="l">Altro Luogo</td>
        <td class="L">
          <font class="campo">
<%
          if (Utils.isNullObj(lLuogoDetenzione.getAltroLuogo()))
            {%>&nbsp;-<%}
          else{%>
            <%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%> <%}%>
          </font>
        </td>
      </tr>
    </table>
  </form>
</body>
</html>