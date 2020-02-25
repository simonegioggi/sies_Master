<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Arrays"%>


<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.modulocumulo.model.DatiFinaliCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiDatiFinaliCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiSoggettoCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiProcedimentoCumulato" %>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel" %>
<%@ page import="siap.siep.modulocumulo.model.SoggettoCumulatoModel" %>


<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="ListaTitoli"  scope="request" class="java.util.Vector"/>


<%
//==============================================================================
// 
//==============================================================================

%>

<html>
<head>
  <title> [S.I.E.S.] - Comunicazioni </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
  
  <script language="JavaScript">    
   
    //==========================================================================
    // Ritorna alla Griglia Della Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formName.submit();
    }
    
    function eseguiFunzione(action)
    {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formName.submit();
    }    

    function stampaSiep(lAzione)
    {
      var  hrefStampa = lAzione;
      var lIndice = hrefStampa.indexOf("?");

      var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
      
      parametri+="&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>";

      stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
    } 
    
  </script>

</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Comunicazioni alle Cancellerie</font>&nbsp;
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia Delle Comunicazioni -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActGrigliaComunicazioni')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>


  
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.">
    
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    
    <br>
    
    <%
    //==========================================================================
    // Tabelle con le eventuali Comunicazioni da effetuare
    //==========================================================================
    %>
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int" >Autorità Emittente</td>

      <td class="int" >Titolo</td><!-- Sentenza/Decreto/Cumulo-->
      <td class="int" >N°</td>
      <td class="int" >Data Titolo</td>      
      
      <td class="int" >Anno/Numero <br>Reg.Gen.</td>
      <td class="int" >Definitivo il</td>
      <!--
      <td class="int" >Anno/Numero <br>SIEP</td>
      <td class="int" >Autorità</td>
      -->
      <td class="int" >Stampa Comunicazione</td>
    </tr>
  
    <%    
boolean isTitoloManuale = false;
      int id_record = 0;
      Iterator itx = ListaTitoli.iterator();
      
      while ( itx.hasNext()) 
      {
          id_record = id_record+1;
  
          TitoloCumulatoModel lTitoloModel = (TitoloCumulatoModel)itx.next();
          
          if (   lTitoloModel.getProcedimentoCumulato()!=null 
              && lTitoloModel.getProcedimentoCumulato().getIdFascicoloSiepOrigine()!=null
              && lTitoloModel.getProcedimentoCumulato().getIdFascicoloSiepOrigine().compareTo(IstruttoriaCumulo.getFasSieIdFascicoloSiep())==0
             )
          {
            //continue; // salto ovviamente il cumulante
          }
          
          String reg      = StringUtils.toStringJSP(lTitoloModel.getTipoRegGen(),"");
          String anno_reg = StringUtils.toStringJSP(lTitoloModel.getAnnoRegGen(),"");
          String num_reg  = StringUtils.toStringJSP(lTitoloModel.getNumeroRegGen(),"");
      
          String AutEmi = lTitoloModel.getDescrTipoAutoritaEmittente()+" di "+lTitoloModel.getDescrLuogoEmittente();
          
          if (lTitoloModel.getNumSezioneAutoritaEmittente()!=null)
             AutEmi += " - sez. "+lTitoloModel.getNumSezioneAutoritaEmittente();
  
          String nSiep = "";
          String AutoritaSiep = "";
          ProcedimentoCumulatoModel lProcedimentoCumulatoModel = lTitoloModel.getProcedimentoCumulato();
          if (lProcedimentoCumulatoModel!=null)
          {
            if ("S".equals(lProcedimentoCumulatoModel.getFlagAccorpato()) ){
              UfficioModel lUfficioOrigine = lProcedimentoCumulatoModel.getUfficioOrigine();

              nSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() +"/"+ lProcedimentoCumulatoModel.getChiaveProgrOrigine();
              nSiep += "<br> <font class=\"cRosso\">(Ex "+lUfficioOrigine.getCodTipoUfficio()+" di "+lUfficioOrigine.getDescrComune()+")</font>"; 
            }
            else {
              nSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() +"/"+ lProcedimentoCumulatoModel.getChiaveProgrFasCumulato();
            }
            
            
            
            AutoritaSiep = StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());
          }
    %>
        <tr>
          <td class="c" >&nbsp;<%=AutEmi%></td>
          <td class="c" nowrap>
            <a href="javascript:dettaglioTitolo('rec_<%=id_record%>')" title="Dettaglio Titolo">
              <% 
              String lDescrtipoTitolo = lTitoloModel.getDescrTipoProvvedimento();
              
              if ("02".equals(lTitoloModel.getCodTipoProvvedimento())) {
                String [] lUfficiSorv = new String[] {"UDS","TDS","UDSM"};
                if (!Arrays.asList(lUfficiSorv).contains(lTitoloModel.getCodTipoAutoritaEmittente())){
                  lDescrtipoTitolo = "Decreto Penale";
                }
              }
              
              String tipoCaricamento = "";
              if (lTitoloModel.getIdSentenzaOrigine()!=null){
                tipoCaricamento = "";
              } else {
                tipoCaricamento = "";
                //tipoCaricamento = " <font class=\"label\"  style=\"font-size:8px;vertical-align: super;\" >(*)</font>";
                isTitoloManuale = true;
              }
              
              %>
              
              <%=lDescrtipoTitolo%></a><%=" "+tipoCaricamento%> 
          </td>
          <td class="c"  nowrap>          
            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActLoadDettaglioTitoloCumulato&<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>=<%=lTitoloModel.getIdTitoloCumulato()%>&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=lTitoloModel.getIstrIdIstruttoriaCumulo()%>" title="Dettaglio Titolo">
              <%=lTitoloModel.getAnnoSentenza()%> / <%=lTitoloModel.getNumeroSentenza()%> </a>&nbsp;
          </td>
          <td class="c"  nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloModel.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></td>
          <td class="c"  nowrap>&nbsp;<%=anno_reg%>/<%=num_reg%>&nbsp;<%=reg%></td>
          <td class="c"  nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloModel.getDataIrrevocabilita(),"dd-MM-yyyy"))%></td>
          
          <!--
          <% if (lProcedimentoCumulatoModel!=null)  { %>
          <td class="c"  nowrap>
            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActLoadDettaglioProcedimentoCumulato&<%=ICostantiProcedimentoCumulato.CAMPO_ID_PROCEDIMENTO_CUMULATO%>=<%=lProcedimentoCumulatoModel.getIdProcedimentoCumulato()%>&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=lTitoloModel.getIstrIdIstruttoriaCumulo()%>" title="Dettaglio Procedimento">
            <%=nSiep%></a>
          </td>
          <% } else { %>
          <td class="c"  nowrap>&nbsp;</td>
          <% } %>
          
          <td class="c"  >&nbsp;<%=AutoritaSiep%></td>
          -->
                    
          <!--  Colonna Azioni  -->
          <td class="c" nowrap>
			<a href="Javascript:stampaSiep('<%="/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActStampaComunicazioniCancellerie&IdTitoloCumulato="+lTitoloModel.getIdTitoloCumulato()%>')">
          		<img  align="middle" src="<%=IWebConstants.IMAGES_DIR%>print.gif" alt="Stampa comunicazione" width="12" height="12" border="0"></a>
          </td>
        </tr>
    <% } // end while su iterator %>
    
  </table>

  <!--
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <td style="text-align:left">
        <INPUT class="bottone" type="button" name="INVIA" style="width:200" 
               title="Invia Comunicazione"
               value="Invia Comunicazioni" 
               onClick="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActGrigliaComunicazioni');" >
      </td>      
    </tr>
  </table>
  -->
  
  </form>
  
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("formName");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
</body>

</html>

