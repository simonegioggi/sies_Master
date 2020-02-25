<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.util.Hashtable"%>

<%@ page import="siap.siep.ulterioresanzionecumulo.model.UlterioreSanzioneCumuloModel"%>
<%@ page import="siap.siep.ulterioresanzionecumulo.action.ICostantiUlterioreSanzioneCumulo"%>



<jsp:useBean id="PosizioneGiuridica"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="lTable"               scope="request" class="java.util.Hashtable" />
<jsp:useBean id="AzioneChiamante"      scope="request" class="java.lang.String" />
<jsp:useBean id="IdPenaResidua"        scope="request" class="java.lang.String" />

<%
//==============================================================================
// FORM per l'inserimento/visualizzazione delle ulteriori sanzioni cumulo
//==============================================================================
%>

<head>
  <title> [S.I.E.S.] - Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  
  <script language="JavaScript">
    var desktop;

    function Verify()
    {

      if (document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_SEMIDETENZIONE%>.value=="" &&document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_SEMIDETENZIONE%>.value==""
          && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_SEMIDETENZIONE%>.value=="" && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LIBERTA%>.value==""
          && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LIBERTA%>.value=="" && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LIBERTA%>.value==""
          && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_ESPULSIONE%>.value=="" && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_ESPULSIONE%>.value==""
          && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_ESPULSIONE%>.value==""  && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_INTERO_IMPORTO_SANZIONE%>.value==""
         && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_DECIMALE_IMPORTO_SANZIONE%>.value=="" && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LAV_SOST%>.value==""
         && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LAV_SOST%>.value=="" && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LAV_SOST%>.value==""
         && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_MILITARE%>.value=="" && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_MILITARE%>.value==""
         && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_MILITARE%>.value=="" && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_INTERO_IMPORTO_MULTA%>.value==""
         && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_DECIMALE_IMPORTO_MULTA%>.value==""  && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_SANZIONI%>.value==""
         && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_SANZIONI%>.value=="" && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_SANZIONI%>.value==""
         && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LAV_SOST_GP%>.value=="" && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LAV_SOST_GP%>.value==""
         && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LAV_SOST_GP%>.value=="" && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LAV_PUB%>.value==""
         && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LAV_PUB%>.value=="" && document.f.<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LAV_PUB%>.value=="")
        {
            alert ("Attenzione : Inserire almeno un sanzione");
            return false;
        }
      return true;
    }
  </script>
</head>

<body class="corpo">

  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name=f>
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.cumulo.action.ActInserisciUlterioriSanzioni">
    <input type="HIDDEN" name="AzioneChiamante" value="<%=AzioneChiamante%>">
    <input type="HIDDEN" name="IdPenaResidua" value="<%=IdPenaResidua%>">

    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Inserimento Ulteriori Sanzioni</font></td>
      </tr>
    </table>
    
    <br>
    
    <table style="width: 95%;">
      <tr><td class=Titolo>Fascicolo Cumulante</td></tr>
      <tr><td><br>
         <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
        <br></td>
      </tr>
      <tr>
        <td class=l>Posizione Giuridica : <font class="campo"><%=PosizioneGiuridica.getDescrPosizioneGiuridica()%></font></td>
      </tr>
    </table>

    <br>
<%
UlterioreSanzioneCumuloModel lUltModSemidetenzione =null;
UlterioreSanzioneCumuloModel lUltModLib=null;
UlterioreSanzioneCumuloModel lUltModEspulsione =null;
UlterioreSanzioneCumuloModel lUltModPenaPec =null;
UlterioreSanzioneCumuloModel lUltModLavSost =null;
UlterioreSanzioneCumuloModel lUltModMilitare =null;
UlterioreSanzioneCumuloModel lUltModDomiciliare =null;
UlterioreSanzioneCumuloModel lUltModLavSostGP =null;
UlterioreSanzioneCumuloModel lUltModLavPub =null;

    if (lTable.get("01") != null)
    {
       lUltModSemidetenzione = (UlterioreSanzioneCumuloModel)lTable.get("01");
    }
    if (lTable.get("02") != null)
    {
       lUltModLib = (UlterioreSanzioneCumuloModel)lTable.get("02");
    }
    if (lTable.get("03") != null)
    {
       lUltModEspulsione = (UlterioreSanzioneCumuloModel)lTable.get("03");
    }
    if (lTable.get("04") != null)
    {
       lUltModPenaPec = (UlterioreSanzioneCumuloModel)lTable.get("04");
    }
    if (lTable.get("05") != null)
    {
       lUltModLavSost = (UlterioreSanzioneCumuloModel)lTable.get("05");
    }

if (lTable.get("06") != null)
    {
       lUltModMilitare = (UlterioreSanzioneCumuloModel)lTable.get("06");
    }
if (lTable.get("07") != null)
    {
       lUltModDomiciliare = (UlterioreSanzioneCumuloModel)lTable.get("07");
    }
if (lTable.get("08") != null)
    {
       lUltModLavSostGP = (UlterioreSanzioneCumuloModel)lTable.get("08");
    }
if (lTable.get("09") != null)
    {
       lUltModLavPub = (UlterioreSanzioneCumuloModel)lTable.get("09");
    }

  String lParteInterMultaMilitare = "";
  String lParteDecimaleMultaMilitare = "";
  String lParteInterSanzionePenaPec = "0";
  String lParteDecimaleSanzionePenaPec = "0";

if(lUltModPenaPec!= null && lUltModPenaPec.getSanzione()!= null)
{
    String lImportoSanzionePenaPec = StringUtils.toStringJSP(lUltModPenaPec.getSanzione());
     int lIndexPenaPecSost = lImportoSanzionePenaPec.indexOf(".");
    if(lIndexPenaPecSost == -1)
    {
      lParteInterSanzionePenaPec = lImportoSanzionePenaPec;
       lParteDecimaleSanzionePenaPec = "";
    }
     else
     {
       lParteInterSanzionePenaPec = lImportoSanzionePenaPec.substring(0, lIndexPenaPecSost);
       lParteDecimaleSanzionePenaPec = lImportoSanzionePenaPec.substring(lIndexPenaPecSost+1);;
     }
}
if(lUltModMilitare!= null && lUltModMilitare.getSanzione()!= null)
{

  String lImportoMultaMilitare = StringUtils.toStringJSP(lUltModMilitare.getSanzione());
  int lIndexPenaMultaMilitare = lImportoMultaMilitare.indexOf(".");
  if(lIndexPenaMultaMilitare == -1)
   {
     lParteInterMultaMilitare = lImportoMultaMilitare;
     lParteDecimaleMultaMilitare = "";
    }
   else
  {
     lParteInterMultaMilitare = lImportoMultaMilitare.substring(0, lIndexPenaMultaMilitare);
     lParteDecimaleMultaMilitare = lImportoMultaMilitare.substring(lIndexPenaMultaMilitare+1);;
  }

}
%>

<table style="width: 95%;">
<%
//==============================================================================
//                            Sanzioni Sostitutive
//------------------------------------------------------------------------------
// 01 - Sanzione Sostitutiva : Semidetenzione      
// 02 - Sanzione Sostitutiva : Liberta' Controllata
// 03 - Sanzione Sostitutiva : Espulsione          
// 04 - Sanzione Sostitutiva : Pena Pecuniaria     
//==============================================================================
%>
  <tr><td class="Titolo" colspan=4 >Sanzioni&nbsp; Sostitutive</td></tr>

  <tr>
    <td class="l" >Semidetenzione</td>
    <td class="l" >
      <%if(lUltModSemidetenzione != null){%>
            Anni&nbsp;<input Title="Anni Semidetenzione" value="<%=StringUtils.toStringJSP(lUltModSemidetenzione.getNumAnni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_SEMIDETENZIONE%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Semidetenzione" value="<%=StringUtils.toStringJSP(lUltModSemidetenzione.getNumMesi())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_SEMIDETENZIONE%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Semidetenzione" value="<%=StringUtils.toStringJSP(lUltModSemidetenzione.getNumGiorni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_SEMIDETENZIONE%>" maxlength="2" size="2">
            <input type="hidden" name="Semidetenzione" value="s">
            <input type="hidden" name="IDSemidetenzione" value="<%=lUltModSemidetenzione.getIdUlterioreSanzioneCumulo()%>">
      <%}else{%>
            Anni&nbsp;<input Title="Anni Semidetenzione" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_SEMIDETENZIONE%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Semidetenzione" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_SEMIDETENZIONE%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Semidetenzione" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_SEMIDETENZIONE%>" maxlength="2" size="2">
            <input type="hidden" name="Semidetenzione" value="">
            <input type="hidden" name="IDSemidetenzione" value="">

       <%}%>
     </td>
  </tr>
  
  <tr>
    <td class="l" >Liberta'controllata</td>
    <td class="l" >
      <%if(lUltModLib != null){%>
            Anni&nbsp;<input Title="Anni Liberta'controllata" value="<%=StringUtils.toStringJSP(lUltModLib.getNumAnni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LIBERTA%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Liberta'controllata" value="<%=StringUtils.toStringJSP(lUltModLib.getNumMesi())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LIBERTA%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Liberta'controllata" value="<%=StringUtils.toStringJSP(lUltModLib.getNumGiorni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LIBERTA%>" maxlength="2" size="2">
            <input type="hidden" name="Liberta" value="s">
            <input type="hidden" name="IDLiberta" value="<%=lUltModLib.getIdUlterioreSanzioneCumulo()%>">
      <%}else{%>
            Anni&nbsp;<input Title="Anni Liberta'controllata" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LIBERTA%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Liberta'controllata" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LIBERTA%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Liberta'controllata" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LIBERTA%>" maxlength="2" size="2">
            <input type="hidden" name="Liberta" value="">
            <input type="hidden" name="IDLiberta" value="">

      <%}%>
    </td>
  </tr>

  <tr>
    <td class="l" >Espulsione</td>
    <td class="l" >
      <%if(lUltModEspulsione != null){%>
            Anni&nbsp;<input Title="Anni Espulsione" value="<%=StringUtils.toStringJSP(lUltModEspulsione.getNumAnni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_ESPULSIONE%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Espulsione" value="<%=StringUtils.toStringJSP(lUltModEspulsione.getNumMesi())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_ESPULSIONE%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Espulsione" value="<%=StringUtils.toStringJSP(lUltModEspulsione.getNumGiorni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_ESPULSIONE%>" maxlength="2" size="2">
            <input type="hidden" name="Espulsione" value="s">
            <input type="hidden" name="IDEspulsione" value="<%=lUltModEspulsione.getIdUlterioreSanzioneCumulo()%>">
      <%}else{%>
            Anni&nbsp;<input Title="Anni Espulsione" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_ESPULSIONE%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Espulsione" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_ESPULSIONE%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Espulsione" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_ESPULSIONE%>" maxlength="2" size="2">
            <input type="hidden" name="Espulsione" value="">
            <input type="hidden" name="IDEspulsione" value="">
      <%}%>
    </td>
  </tr>
  
  <tr>
    <td class="l" >Pena pecuniaria</td>
    <td class="l" >
      <%if(lUltModPenaPec != null ){%>
            <input type="hidden" name="Pecuniaria" value="s">
            <input type="hidden" name="IDPecuniaria" value="<%=lUltModPenaPec.getIdUlterioreSanzioneCumulo()%>">
      <%}%>
            <input type="hidden" name="Pecuniaria" value="">
            <input type="hidden" name="IDPecuniaria" value="">

            <input Title="PenaPecuniaria" size=7 maxlength=7 value="<%=StringUtils.toStringJSP(lParteInterSanzionePenaPec)%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_INTERO_IMPORTO_SANZIONE%>">
            ,
            <input Title="PenaPecuniaria" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lParteDecimaleSanzionePenaPec)%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_DECIMALE_IMPORTO_SANZIONE%>">
    </td>
  </tr>

<%
//==============================================================================
//                   Pena da conversione pena pecuniaria
//------------------------------------------------------------------------------
// 05 - Pena Pecuniaria : Lavoro Sostitutivo      
//==============================================================================
%>
  <tr><td class="Titolo" colspan=2 >Pena da conversione pena pecuniaria</td></tr>

  <tr>
    <td class="l" >Lavoro Sostitutivo</td>
    <td class="l" >
      <%if(lUltModLavSost != null){%>
            Anni&nbsp;<input Title="Anni Lavoro Sostitutivo" value="<%=StringUtils.toStringJSP(lUltModLavSost.getNumAnni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LAV_SOST%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Lavoro Sostitutivo" value="<%=StringUtils.toStringJSP(lUltModLavSost.getNumMesi())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LAV_SOST%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Lavoro Sostitutivo" value="<%=StringUtils.toStringJSP(lUltModLavSost.getNumGiorni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LAV_SOST%>" maxlength="2" size="2">
             <input type="hidden" name="Sostitutivo" value="s">
            <input type="hidden" name="IDSostitutivo" value="<%=lUltModLavSost.getIdUlterioreSanzioneCumulo()%>">

      <%}else{%>
            Anni&nbsp;<input Title="Anni Lavoro Sostitutivo" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LAV_SOST%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Lavoro Sostitutivo" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LAV_SOST%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Lavoro Sostitutivo" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LAV_SOST%>" maxlength="2" size="2">
             <input type="hidden" name="Sostitutivo" value="">
            <input type="hidden" name="IDSostitutivo" value="">
      <%}%>
    </td>
  </tr>
  
  
<%
//==============================================================================
//                               Pena Militare
//------------------------------------------------------------------------------
// 06 - Pena Militare : Reclusione     
//==============================================================================
%>
  <tr><td class="Titolo" colspan=4 >Pena Militare</td></tr>

  <tr>
    <td class="l" >Reclusione</td>
    <td class="l" >
      <%if(lUltModMilitare != null){%>

            Anni&nbsp;<input Title="Anni Reclusione" value="<%=StringUtils.toStringJSP(lUltModMilitare.getNumAnni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_MILITARE%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Reclusione" value="<%=StringUtils.toStringJSP(lUltModMilitare.getNumMesi())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_MILITARE%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Reclusione" value="<%=StringUtils.toStringJSP(lUltModMilitare.getNumGiorni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_MILITARE%>" maxlength="2" size="2">
            Multa&nbsp;
            <input Title="Multa" size=7 maxlength=7 value="<%=StringUtils.toStringJSP(lParteInterMultaMilitare)%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_INTERO_IMPORTO_MULTA%>">
            ,
            <input Title="Multa" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lParteDecimaleMultaMilitare)%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_DECIMALE_IMPORTO_MULTA%>">
            <input type="hidden" name="Reclusione" value="s">
            <input type="hidden" name="IDReclusione" value="<%=lUltModMilitare.getIdUlterioreSanzioneCumulo()%>">

      <%}else{%>
            <input type="hidden" name="Reclusione" value="">
            <input type="hidden" name="IDReclusione" value="">


            Anni&nbsp;<input Title="Anni Reclusione" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_MILITARE%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Reclusione" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_MILITARE%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Reclusione" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_MILITARE%>" maxlength="2" size="2">
            Multa&nbsp;
            <input Title="Multa" size=7 maxlength=7 value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_INTERO_IMPORTO_MULTA%>">
            ,
            <input Title="Multa" size=2 maxlength=2 value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_DECIMALE_IMPORTO_MULTA%>">
      <%}%>
    </td>
  </tr>
  
<%
//==============================================================================
//                      Sanzioni del giudice di pace
//------------------------------------------------------------------------------
// 07 - Giudice di Pace : Permanenza Domiciliare  
// 08 - Giudice di Pace : Lavoro Sostitutivo      
// 09 - Giudice di Pace : Lavoro Pubblica Utilita'
//==============================================================================
%>
  <tr><td class="Titolo" colspan=2 >Sanzioni&nbsp; del giudice di pace</td></tr>

  <tr>
    <td class="l" >Permanenza Domiciliare</td>
    <td class="l" >
      <%if(lUltModDomiciliare!= null){%>

            Anni&nbsp;<input Title="Anni Permanenza Domiciliare" value="<%=StringUtils.toStringJSP(lUltModDomiciliare.getNumAnni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_SANZIONI%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Permanenza Domiciliare" value="<%=StringUtils.toStringJSP(lUltModDomiciliare.getNumMesi())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_SANZIONI%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Permanenza Domiciliare" value="<%=StringUtils.toStringJSP(lUltModDomiciliare.getNumGiorni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_SANZIONI%>" maxlength="2" size="2">
            <input type="hidden" name="Domiciliare" value="s">
            <input type="hidden" name="IDDomiciliare" value="<%=lUltModDomiciliare.getIdUlterioreSanzioneCumulo()%>">
     <%}else{%>
            Anni&nbsp;<input Title="Anni Permanenza Domiciliare" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_SANZIONI%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Permanenza Domiciliare" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_SANZIONI%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Permanenza Domiciliare" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_SANZIONI%>" maxlength="2" size="2">
            <input type="hidden" name="Domiciliare" value="">
            <input type="hidden" name="IDDomiciliare" value="">
      <%}%>
    </td>
  </tr>
  
  <tr>
    <td class="l" >Lavoro sostitutivo</td>
    <td class="l">
      <%if(lUltModLavSostGP!= null){%>
            Anni&nbsp;<input Title="Anni Lavoro sostitutivo" value="<%=StringUtils.toStringJSP(lUltModLavSostGP.getNumAnni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LAV_SOST_GP%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Lavoro sostitutivo" value="<%=StringUtils.toStringJSP(lUltModLavSostGP.getNumMesi())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LAV_SOST_GP%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Lavoro sostitutivo" value="<%=StringUtils.toStringJSP(lUltModLavSostGP.getNumGiorni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LAV_SOST_GP%>" maxlength="2" size="2">
            <input type="hidden" name="sostitutivoGP" value="s">
            <input type="hidden" name="IDSostitutivoGP" value="<%=lUltModLavSostGP.getIdUlterioreSanzioneCumulo()%>">
      <%}else{%>
            Anni&nbsp;<input Title="Anni Lavoro sostitutivo" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LAV_SOST_GP%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Lavoro sostitutivo" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LAV_SOST_GP%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Lavoro sostitutivo" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LAV_SOST_GP%>" maxlength="2" size="2">
            <input type="hidden" name="sostitutivoGP" value="">
            <input type="hidden" name="IDSostitutivoGP" value="">
      <%}%>
    </td>
  </tr>
  
  <tr>
    <td class="l" >Lavoro pubblica utilità</td>
    <td class="l" >
      <%if(lUltModLavPub!= null){%>
            Anni&nbsp;<input Title="Anni Lavoro pubblica utilità" value="<%=StringUtils.toStringJSP(lUltModLavPub.getNumAnni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LAV_PUB%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Lavoro pubblica utilità" value="<%=StringUtils.toStringJSP(lUltModLavPub.getNumMesi())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LAV_PUB%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Lavoro pubblica utilità" value="<%=StringUtils.toStringJSP(lUltModLavPub.getNumGiorni())%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LAV_PUB%>" maxlength="2" size="2">
            <input type="hidden" name="pubblica" value="s">
            <input type="hidden" name="IDPubblica" value="<%=lUltModLavPub.getIdUlterioreSanzioneCumulo()%>">
      <%}else{%>
            Anni&nbsp;<input Title="Anni Lavoro pubblica utilità" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LAV_PUB%>" maxlength="2" size="2">
            Mesi&nbsp;<input Title="Mesi Lavoro pubblica utilità" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LAV_PUB%>" maxlength="2" size="2">
            Giorni&nbsp;<input Title="Giorni Lavoro pubblica utilità" value="<%=StringUtils.toStringJSP("0")%>" type="text" name="<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LAV_PUB%>" maxlength="2" size="2">
            <input type="hidden" name="pubblica" value="">
            <input type="hidden" name="IDPubblica" value="">
      <%}%>
    </td>
  </tr>
  
  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
    </td>
  </tr>

</table>

</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");

  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_SEMIDETENZIONE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_SEMIDETENZIONE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_SEMIDETENZIONE%>","numeric");

  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LIBERTA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LIBERTA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LIBERTA%>","numeric");

  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_ESPULSIONE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_ESPULSIONE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_ESPULSIONE%>","numeric");


  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LAV_SOST%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LAV_SOST%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LAV_SOST%>","numeric");

  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_MILITARE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_MILITARE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_DECIMALE_IMPORTO_MULTA%>","numeric");

  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_SANZIONI%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_SANZIONI%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_SANZIONI%>","numeric");

  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LAV_SOST_GP%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LAV_SOST_GP%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LAV_SOST_GP%>","numeric");

  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_ANNI_LAV_PUB%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_MESI_LAV_PUB%>","numeric");
  frmvalidator.addValidation("<%=ICostantiUlterioreSanzioneCumulo.CAMPO_NUM_GIORNI_LAV_PUB%>","numeric");

  frmvalidator.setAddnlValidationFunction("Verify");

</script>

</body>
</html>

