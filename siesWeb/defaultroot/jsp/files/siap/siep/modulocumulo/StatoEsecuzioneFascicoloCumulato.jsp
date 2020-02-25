<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>


<%@ page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>

<%@ page import="siap.siep.modulocumulo.util.ModuloCumuloUtils"%>



<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="StatoEsecuzioneFascicolo" scope="request" class="java.util.Vector"/>
<jsp:useBean id="StatoEsecuzioneCaricato"  scope="request" class="java.util.Vector"/>

<%
//==============================================================================
// Popup per la visualizzazione dello Stato di Esecuzione del Fascicolo Originario
//==============================================================================

ProcedimentoCumulatoModel lProcedimento = TitoloInCumulo.getProcedimentoCumulato();
String lNumProcedimento = "";
if (lProcedimento!=null) {
  lNumProcedimento = "N. "+lProcedimento.getChiaveAnnoFasCumulato();
  
  if (!"S".equals (lProcedimento.getFlagAccorpato()) ) { 
    lNumProcedimento+="/"+lProcedimento.getChiaveProgrFasCumulato();
  } else { 
    lNumProcedimento+="/"+lProcedimento.getChiaveProgrOrigine();
  }
}


int lContaEventi = StatoEsecuzioneFascicolo.size();
int lContaSelezionati = StatoEsecuzioneCaricato.size();

String lCheckAll = "";
if (lContaEventi==lContaSelezionati){
  lCheckAll = "checked";
}

%>



<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Modulo Cumulo </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
    
    <script language="JavaScript">
      window.focus();
      
      function SelezionaTutti(checkObject)
      {
        var jQueryObj = $(checkObject);
        
        if (jQueryObj.prop('checked')){
          $('#idTabStatoEsec input[type=checkbox]').prop('checked',true);
        } else {
          $('#idTabStatoEsec input[type=checkbox]').prop('checked',false);
        }
      }


      function stampaSiep(lAzione)
      {
        var  hrefStampa = lAzione;
        var lIndice = hrefStampa.indexOf("?");
  
        var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
        
        parametri+="&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>";
        parametri+="&<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>=<%=TitoloInCumulo.getIdTitoloCumulato()%>";
  
        stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
      } 
      
     
      function effettoTree(a)
      {
        node=document.getElementById("elenco"+a);
        node.style.display = (node.style.display == "none")? "block" : "none";
        document.images["image"+a].src = (node.style.display == "none")? "<%=IWebConstants.IMAGES_DIR%>expand.gif" : "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
        return false;
      }      

      function Verify(){
        return true;
      }
    </script>
  </head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>
        <font class="campo">Stato Esecuzione Procedimento SIEP <%=lNumProcedimento%> &nbsp;</font>
      </td>
      
      <td class="LBG">
        <a href="Javascript:stampaSiep('/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActStampaStatoEsecuzioneTitoloCumulato')">
          <img  align="middle" src="<%=IWebConstants.IMAGES_DIR%>print24.gif" alt="Stampa Stato Esecuzione Fascicolo" width="24" height="24" border="0">
        </a>
      </td>
      
    </tr>
  </table>
  
  <br>

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="GrigliaStatoEsecuzione">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActAggiornaStatoEsecuzioneTitolo">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">


  <table cellspacing="2" cellpadding="2" align="center" width="100%">
    <tr>
      <td colspan="4" class="Titolonocap">Stato di esecuzione fascicolo </td>
    </tr> 
    <tr>
      <td colspan="4">
        <table cellspacing="2" cellpadding="2" width="100%" id="idTabStatoEsec">
          <tr>
            <td class="int" nowrap>Data Emissione</td>
            <td class="int">Provvedimento</td>
            <td class="int">Esito</td>
            <td class="int">Autorità</td>
            <td class="int" nowrap> <input type="checkbox" onClick="Javascript:SelezionaTutti(this);" <%=lCheckAll%> >Azioni</td>
          </tr>

          <%
          int ProgrId = 0;
          Iterator itxFascicolo = StatoEsecuzioneFascicolo.iterator();
          while ( itxFascicolo.hasNext())
          {
            MisuraAlternativaAggregatoModel lAggre = (MisuraAlternativaAggregatoModel) itxFascicolo.next();
            EventoModel lEvento = lAggre.getEventoNotifica().getEvento();
            
            //
            boolean lGiaCaricato = false;
            Iterator itxCaricato= StatoEsecuzioneCaricato.iterator();
            while ( itxCaricato.hasNext())
            {
              StatoEsecTitoloCumulatoModel lSETModel = (StatoEsecTitoloCumulatoModel) itxCaricato.next();
              
              if (   lSETModel.getIdEventoOrigine()!=null 
                  && lEvento.getIdEvento().compareTo(lSETModel.getIdEventoOrigine())==0 
                 )
              {
                lGiaCaricato = true;
                break;
              }              
            }
            
            
            %>
            <tr>
              <td class="c" nowrap><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"dd-MM-yyyy"))%>&nbsp;</td>
              <td class="l"><%=StringUtils.toStringJSP(lEvento.getDescrTipoProvvedimento())%>&nbsp;<%=StringUtils.toStringJSP(lEvento.getDescrMotivo(),"-")%></td>
              <%
              if(   lAggre.getTenori() != null 
                 && lAggre.getTenori().length>0 
                 && lAggre.getTenori()[0].getCodEsitoTenore() != null 
                 && !lAggre.getTenori()[0].getCodEsitoTenore().equals("")
                )
              {
              %>              
                <td class="c"><%=StringUtils.toStringJSP(lAggre.getTenori()[0].getDescrEsitoTenore())%>
                  <% if(lAggre.getTenori().length>1) { %>
                  <a href="#1" onClick="return effettoTree(<%=ProgrId%>)"><img name="image<%=ProgrId%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Altri Esiti" ></a>
                  <% } %>
                </td>              
              <% } else { %>
                <td class="c">-</td>
                <!--td class="c"><%=StringUtils.toStringJSP(lEvento.getIdEvento(),"&nbsp;")%></td-->                
              <% } %>
              
              <td class="l"><%=lEvento.getDescrUfficioEmittente()+ " " + lEvento.getDescrLuogoEmittente()%></td>
              <td class="c" nowrap>
               <input type="checkbox" 
                      name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  
                      id="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"
                      value="<%=StringUtils.toStringJSP(lEvento.getIdEvento())%>"
                      <%=(lGiaCaricato)?"checked":""%>
                      onClick="Javascript:null"
                      >                 
                <a href="Javascript:stampaSiep('<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActLoadDocumento&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=lEvento.getIdEvento()%>')">
                  <img src="<%=IWebConstants.IMAGES_DIR%>print.gif" alt="Visualizza Stampa" width="12" height="12" border="0"></a>
              </td>
            </tr>
            
            <%
            //==================================================================
            // Caricamento Altri Esiti. DA VERIFICARE !!!!!!!!!!!!!!!!!!!!
            //==================================================================
            if (lAggre.getTenori() != null && lAggre.getTenori().length>1)
            {
              //il contatore parte da uno perchè il primo esito nn deve essere visualizzato nella lista
              Vector lTenori = new Vector();
              for(int j=1; j<lAggre.getTenori().length;j++)
              {
                TenoreModel lTenMod = new TenoreModel (lAggre.getTenori()[j]);
                lTenori.add(lTenMod);
              }
              %>

              <tr id="elenco<%=ProgrId%>" style="display:none">
                <td colspan="100%">
                <%@include file="/jsp/files/siap/sius/tenore/ListaTenori.jsp" %>
                </td>
              </tr>
            <%
              ProgrId++;
            }
            %>
            
           
            
          <%
          } // End While sugli eventi
          %>
      </table>
    </td>
  </tr>
</table>

<% if (ModuloCumuloUtils.isMev42Abilitata()) { %>
<table cellspacing="2" cellpadding="2" align="center" width="100%">
  <tr>
    <td>
      <input type="submit" name="Aggiorna" value="Aggiorna">
    </td>
  </tr>
</table>
<% } %>
</form>


</body>

<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("GrigliaStatoEsecuzione");
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>


</html>