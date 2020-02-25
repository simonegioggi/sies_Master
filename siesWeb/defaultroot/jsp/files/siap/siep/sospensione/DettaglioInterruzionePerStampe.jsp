<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep"%>


<jsp:useBean id="decretoordinanza"    scope="request" class="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<%
//  < jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
%>
<jsp:useBean id="flagergastolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="sospensione"   scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();

  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
  if (lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

  //Inizializzazione campi se SOSPENSIONE non trovata
  //(per evitare eventuale NullPointerException)
  if(sospensione.getIdSospensione() == null)
    sospensione.setQuantumZero();
%>


<html>
<head>
<title>[S.I.E.S.] - Interruzione dell'esecuzione </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>

  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  <script language="JavaScript">
    function subm(id)
    {
      if(id==1)
      {
       document.LoadInserisciSosp.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.sospensione.action.ActLoadInserisciSospensioneOE";
      }
      if(id==2)
      {
       document.LoadInserisciSosp.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.sospensione.action.ActLoadInserisciSospensioneOS";
      }
      if(id==3)
      {
       document.LoadInserisciSosp.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.sospensione.action.ActLoadInserisciSospensioneComunicazione";
      }
      document.LoadInserisciSosp.submit();
    }
    
    function Verify()
    {
      return true;
    }
  </script>
  <script language="JavaScript1.2">
  <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      function over_effect(e,state)
      {
        if (document.all)
          source4=event.srcElement
        else if (document.getElementById)
          source4=e.target
        if (source4.className=="menulines")
          source4.style.borderStyle=state
        else
        {
          while(source4.tagName!="TABLE")
          {
            source4=document.getElementById? source4.parentNode : source4.parentElement
            if (source4.className=="menulines")
              source4.style.borderStyle=state
          }
        }
      }
  </script>

  <STYLE>
    .menulines
    {
      border:2.5px solid #BEC6FC;
      text-align : center;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      text-decoration : none;
      height:100%;
    }

    .menulines a
    {
      text-align : center;
      text-decoration:none;
      color:black;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      width:100%;
      height:100%;
    }
  </STYLE>
</head>

<body class="corpo">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciSosp">

  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
        <input type="HIDDEN" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ID_DECRETO_ORDINANZA_SIEP%>" value="<%=decretoordinanza.getIdDecretoOrdinanzaSiep()%>">
      <font class="campo">Dettaglio Interruzione Pena - Stampe</font>
    </td>

    </tr>
  </table>
  <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <%
  //============================================================================
  // Sezione contenente il riepilogo dei dati della posizione giuridica e della
  // pena residua a seguito dell'Interruzione
  //============================================================================
  %>
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=7>
        <font class="campo">
<%
          if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
          {
%>
            DETENUTO PER ALTRA CAUSA
<%
          }
          else
          {
%>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
          }
%>
        </font>
      </td>
    </tr>
<%
    // Se detenuto altra causa     
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null )
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
             //  if(lAltraCausa.getDescrLuogoIstituto()!=null)
              // {
%>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
<%
               //}
%>
             </td>
           </tr>
           
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td >
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if(lLuogoDetenzione.getIstitutoDetenzione()!= null )
        { // Detenuto questa causa
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
             // if(lLuogoDetenzione.getDescrLuogo()!=null)
             // {
%>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
             // }
%>
            </td>
          </tr>
<%
        }
%>


   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
<%
    // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getIstitutoDetenzione() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
              <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
            </tr>
<%
          }
        }
%>


<%
//==============================================================================
// 
//==============================================================================
    if (   sospensione.getNumAnniPenaEspiata().intValue()!=0
        || sospensione.getNumMesiPenaEspiata().intValue()!=0
        || sospensione.getNumGiorniPenaEspiata().intValue()!=0 )
    {
%>
       <tr>
          <td class="l">
            <font class="label">Pena Espiata</font>
          </td>
          <td class="l">
            <font class="label">Anni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaEspiata(), "0")%></font>
            <font class="label">Mesi</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaEspiata(), "0")%></font>
            <font class="label">Giorni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaEspiata(), "0")%></font>
          </td>
        </tr>
<%
    }
    
    //==========================================================================
    // Pena residua dopo interruzione
    //==========================================================================
    if (    flagergastolo.equals("N")
        && (   sospensione.getNumAnniPenaResiduaReclus().intValue()!=0
            || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0
            || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0
            || sospensione.getNumAnniPenaResiduaArres().intValue()!=0
            || sospensione.getNumMesiPenaResiduaArres().intValue()!=0
            || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0
           )
      )
    {
%>
      <tr>
        <td class="l">
          <font class="label">Pena Residua</font>
        </td>
        <td class="l">
<%
          if (   flagergastolo.equals("N")
              && (   sospensione.getNumAnniPenaResiduaReclus().intValue()!=0
                  || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0
                  || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0
                 )
             )
          {
%>
            <font class="label">Reclusione : </font>
            <font class="label">Anni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaReclus(), "0")%></font>
            <font class="label">Mesi</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaReclus(), "0")%></font>
            <font class="label">Giorni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaReclus(), "0")%></font>
<%
            if(sospensione.getMultaResidua()!=null && sospensione.getMultaResidua().compareTo(new BigDecimal(0))!=0)
            {
%>
              <font class="label">Multa </font>
              <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getMultaResidua())%></font>&nbsp;€&nbsp;
<%
            }
          }
          
          if (  flagergastolo.equals("N")
             && (sospensione.getNumAnniPenaResiduaArres().intValue()!=0
             || sospensione.getNumMesiPenaResiduaArres().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0) )
          {
%>
            <font class="label"> Arresto : </font>
            <font class="label">Anni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaArres(), "0")%></font>
            <font class="label">Mesi</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaArres(), "0")%></font>
            <font class="label">Giorni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaArres(), "0")%></font>
<%
             if(sospensione.getAmmendaResidua()!=null && sospensione.getAmmendaResidua().compareTo(new BigDecimal(0))!=0)
            {
%>
              <font class="label">Ammenda </font>
              <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getAmmendaResidua())%></font>&nbsp;€&nbsp;
<%
            }
         }

          if (  flagergastolo.equals("N")
             && (sospensione.getNumAnniPenaResiduaReclus().intValue()!=0
             || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0
             || sospensione.getNumAnniPenaResiduaArres().intValue()!=0
             || sospensione.getNumMesiPenaResiduaArres().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0)
            )
          {
%>
              </td>
            </tr>
<%
          }
    }

    if(flagergastolo.equals("S"))
    {
%>
      <tr>
        <td class="l">
          <font class="label">Pena Complessiva</font>
        </td>
        <td class="l">
          <font class="campo">ERGASTOLO</font>
        </td>
      </tr>
<%
    }
    else if(flagergastolo.equals("D"))
    {
%>
      <tr>
        <td class="l">
          <font class="label">Pena Complessiva</font>
        </td>
        <td class="l">
          <font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font>
        </td>
      </tr>
<%
    }
%>
    </table>
    <br>
    
<%
//==============================================================================
//           Sezione con il dettaglio dei dati dell'interruzione
//==============================================================================
%>
<table style="width: 95%;">
  <tr>
    <td colspan=2 class="titolo">Interruzione Esecuzione Pena</td>
  </tr>
  <tr>
    <td class="l" width="30%">
       Data Interruzione esecuzione
    </td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataInterruzionePena(),"dd-MM-yyyy"))%></font>
    </td>
  </tr>
  
<%if(decretoordinanza.getDataEmissioneProvvedimento() != null){%>
  <tr>
    <td class="l">
     Data comunicazione evento
    </td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataEmissioneProvvedimento(),"dd-MM-yyyy"))%></font>
    </td>
  </tr>
<%}%>

<%if(decretoordinanza.getDataRicezioneProvvedimento() != null){%>
  <tr>
    <td class="l">
       Data ricezione evento
    </td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRicezioneProvvedimento(),"dd-MM-yyyy"))%></font>
    </td>
  </tr>
<%}%>

<%if(decretoordinanza.getProtocollo() != null){%>
  <tr>
    <td class="l">
       Protocollo
    </td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getProtocollo())%></font>
    </td>
  </tr>
<%}%>

  <tr>
    <td class="l" >
      Autorità
    </td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getAltraAutorita())%></font> di <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getAltroLuogo())%></font>
    </td>
  </tr>
  <tr>
    <td class="l">
       Motivo interruzione
    </td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrOggettoProcedimento())%></font>
    </td>
  </tr>
  
<%if(decretoordinanza.getMotivazioni()!= null){%>
  <tr>
    <td class="l">
       Annotazioni
    </td>
    <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getMotivazioni())%></font>
    </td>
  </tr>
<%}%>
</table>
  
<%
//==============================================================================
//                            Sezione con le stampe 
//==============================================================================
%>
<table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td class="titolo" colspan=3>Stampe Provvedimenti</td>
  </tr>
  <tr>
    <td width="32%" class="menulines" nowrap><a href="javascript:subm(1);">Ordine di Esecuzione </a></td>
    <td width="32%" class="menulines" nowrap><a href="javascript:subm(2);">Ordine di Scarcerazione</a></td>
    <td width="32%" class="menulines" nowrap><a href="javascript:subm(3);">Comunicazione Estradizione </a></td>
  </tr>
</table>
</form>


  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciSosp");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
  </body>
</html>