<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel" %>
<%@ page import="siap.siep.modulocumulo.model.SoggettoCumulatoModel" %>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel" %>

<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato" %>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<jsp:useBean id="ParentFormName"  scope="request" class="java.lang.String"/>
<jsp:useBean id="ParentFormType"  scope="request" class="java.lang.String"/>

<jsp:useBean id="ListaTitoli"  scope="request" class="java.util.Vector"/>

<%
//==============================================================================
//     
//==============================================================================
FascicoloSiepModel lFascicolo = (FascicoloSiepModel) session.getAttribute("fascicolo");
SoggettoModel lSoggettoFascicolo = lFascicolo.getSoggetto();
%>
<!--              LoadPopupElencoTitoli                -->

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] -Popup di Elenco Titoli associati all'Istruttoria </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
      window.focus();
      
      function insertIT(id_Titolo,
                CodProvv,
                DescrTipoProvv,
                DataIrr,
                DataProvv,
                AnnoProvv, 
                NumeroProvv,
                DescrTipoAutorEmittente,
                ComuneAutorEmittente,
                CodTipoAutorEmittente,
                CodLuogoAutorEmittente,
                strSoggetto
          )
      {

        formname = '<%=ParentFormName%>';
        formtype = '<%=ParentFormType%>';
        
        var lEstrattoSentFormType  = '<%=ICostantiIstruttoria.FORM_TYPE_SENTENZA_INTEGRALE%>';
        var lPenaPecFormType       = '<%=ICostantiIstruttoria.FORM_TYPE_PAGAMENTO_PENA_PEC%>';
        var lCertStatoEsecFormType = '<%=ICostantiIstruttoria.FORM_TYPE_CERTIFICATO_ESECUZIONE%>';
        
        var lEstremiSentenza='';
        lEstremiSentenza += DescrTipoProvv+" N. "+AnnoProvv+"/"+NumeroProvv+" emessa da "+DescrTipoAutorEmittente+" di "+ComuneAutorEmittente;
        lEstremiSentenza +=" il "+DataProvv+" definitiva il "+DataIrr;        

        if(formtype == lEstrattoSentFormType )
        {          
          // Carico i dati della sentenza nel campo ICostantiIstruttoria.ESTREMI_SENTENZA
          window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.ESTREMI_SENTENZA %>.value = lEstremiSentenza;

          if (strSoggetto!=undefined) {
            window.parent.opener.document.getElementById('idTrSoggetto').style.display = "block";
            window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.ESTREMI_SOGGETTO %>.value = strSoggetto;
          } else {
            window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.ESTREMI_SOGGETTO %>.value = "";
            window.parent.opener.document.getElementById('idTrSoggetto').style.display = "none";
          }
        }
        else if(formtype == lPenaPecFormType || formtype == lCertStatoEsecFormType)
        { 
          // Carico i dati della sentenza nel campo ICostantiIstruttoria.CAMPO_NOTE 
          window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.CAMPO_NOTE %>.value = lEstremiSentenza;
        }
        else {
          // Da capire se altri richiamano tale PopUp sembra di no
          window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value=id_Titolo;
  
          window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO %>.value=CodProvv;
          window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.CAMPO_DESCR_PROVVEDIMENTO %>.value=DescrTipoProvv;
          
          window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.CAMPO_DATA_IRREVOCABILITA %>.value=DataIrr;
          window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.CAMPO_DATA_EMISSIONE_PROVV %>.value=DataProvv;
          
          window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiTitoloCumulato.CAMPO_ANNO_SENTENZA %>.value=AnnoProvv;
          window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiTitoloCumulato.CAMPO_NUMERO_SENTENZA %>.value=NumeroProvv;
  
          // Codici Autorita
          window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value=CodTipoAutorEmittente;
          window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>.value=CodLuogoAutorEmittente
          
          // sede Autorita
          window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>.value=ComuneAutorEmittente;
        }
       
        // Tipo Autorita: Se la parent form è 'Notizia Pagamento Pena Pecuniaria', 
        //          il Tipo Autorità deve essere trattato in quanto è un Ufficio particolare (Ufficio Recupero Crediti) 
        if(formtype == lPenaPecFormType )
        {
          var TipoRecuperoCrediti='';
          
          if(CodTipoAutorEmittente=='CAP' || CodTipoAutorEmittente=='CASAP' || CodTipoAutorEmittente=='CAPSM')
          { 
            TipoRecuperoCrediti = '37';   // 37 - Ufficio Recupero Crediti presso la Corte D'Appello
          }
          
          if(CodTipoAutorEmittente=='CAS' || CodTipoAutorEmittente=='DIB' || CodTipoAutorEmittente=='GIP' || CodTipoAutorEmittente=='GUP')
          { 
            TipoRecuperoCrediti = '36';   // 36 - Ufficio Recupero Crediti presso il Tribunale
          }
          
          if( CodTipoAutorEmittente=='DIBM' || CodTipoAutorEmittente=='GIPM' || CodTipoAutorEmittente=='GUPM')
          { 
            TipoRecuperoCrediti = '57';   // 57 - Ufficio Recupero Crediti presso il Tribunale dei Minori
          }
          
          if( CodTipoAutorEmittente=='GP')
          { 
            TipoRecuperoCrediti = '98';   // 98 - Ufficio Recupero Crediti presso il Giudice di Pace
          }
          
          if( CodTipoAutorEmittente=='TRIBSD')
          { 
            TipoRecuperoCrediti = '99';   // 99 - Ufficio Recupero Crediti presso Sezione Distaccata di Tribunale
          }
          
          for(var k=0;k<window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO %>.options.length;k++)
          {
            if(window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO %>[k].value == TipoRecuperoCrediti )
            {
                window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO %>.options[k].selected=true;
                break;
            }
          }        
        }
        else
        { 
          for(var k=0;k<window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO %>.options.length;k++)
          {
              if(window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO %>.options[k].text==DescrTipoAutorEmittente)
              {
                  window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO %>.options[k].selected=true;
                  break;
              }
          }
        } 
  
        // sede Autorita
        window.parent.opener.document.<%=request.getParameter("ParentFormName")%>.<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>.value=ComuneAutorEmittente;
        
        window.parent.close();
      }

      function controlla()
      {
          if(document.elenco.numeroTitoli.value==0)
          {
              alert("Nessun Titolo Presente");
              window.parent.close();
          }
      }

    </script>

  </head>

<body class="corpo" onload="controlla();">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">

  <input type="HIDDEN" name="ParentFormName" value="<%=ParentFormName%>">
  <input type="HIDDEN" name="ParentFormType" value="<%=ParentFormType%>">
  <input type="hidden" name="numeroTitoli" value="<%=ListaTitoli.size()%>">
  
  
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
      </td>
      <td class="LBG">
        <font class=label>Funzione :</font>
        <font class=campo>Elenco Titoli associati all'Istruttoria </font>
      </td>
    </tr>
  </table>
    <br>
    
     <!--       Elenco Titoli       -->  

  <table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      <td class="titolo" colspan=3 width=100%>Elenco Titoli in Istruttoria</td>
    </tr>
  </table>  
  
  <table>
    <!--tr>
      <td class="int" width="8%"  >Titolo</td>
      <td class="int" width="10%" >Data Titolo </td>
      <td class="int" width="8%"  >Numero sentenza</td>
      <td class="int" width="18%" >Autorità Titolo Esecutivo</td>
      <td class="int" width="10%" >Data Irrevocabilita</td>
      <td class="int" width="8%"  >Numero SIEP</td>
      <td class="int" width="25%" >Ufficio Esecuzione</td>
      <td class="int" width="5%"  >Azioni</td>
    </tr-->
    
    <tr>
      <td class="int"  >Titolo</td>
      <td class="int"  >Data Titolo </td>
      <td class="int"  >Numero sentenza</td>
      <td class="int"  >Autorità Titolo Esecutivo</td>
      <td class="int"  >Data Irrevocabilita</td>
      <td class="int"  >Numero SIEP</td>
      <td class="int"  >Ufficio Esecuzione</td>
      <td class="int"  >Azioni</td>
    </tr>
    
<%
  Iterator itx = ListaTitoli.iterator();
  int conta = 0;
  
  while ( itx.hasNext())
  {
      conta++;
  
      TitoloCumulatoModel lTitoCum = (TitoloCumulatoModel) itx.next();    
      
      String nSiep = null;
      ProcedimentoCumulatoModel lProcedimentoCumulatoModel = lTitoCum.getProcedimentoCumulato();
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
      }      
      
      SoggettoCumulatoModel lSoggettoCumulato = lTitoCum.getSoggettoCumulato();
        
      String lStrSoggetto = "";
      boolean isStessoSoggetto = true;
      
      if (lSoggettoCumulato!=null) 
      {
        isStessoSoggetto = lSoggettoCumulato.isStessoSoggetto(lSoggettoFascicolo);
      
      
        if ("F".equals(lSoggettoCumulato.getSesso())){
          lStrSoggetto += "condannata";
        }else{
          lStrSoggetto += "condannato";
        }        
        
        lStrSoggetto+=" con le generalità di ";
        lStrSoggetto+=lSoggettoCumulato.getCognome()+" " + lSoggettoCumulato.getNome()+",";
        
        if ("F".equals(lSoggettoCumulato.getSesso())){
          lStrSoggetto += " nata";
        }else{
          lStrSoggetto += " nato";
        }        
        
        // Luogo di nascita
        lStrSoggetto += " a ";
        if (lSoggettoCumulato.getDescrComuneNascita().compareTo("-")==0) {
         lStrSoggetto += StringUtils.toStringJSP(lSoggettoCumulato.getDescComuneNascitaEstero(),"")+" ("+lSoggettoCumulato.getDescrStatoNascita().toUpperCase()+")";
        }
        else {
          lStrSoggetto += StringUtils.toStringJSP(lSoggettoCumulato.getDescrComuneNascita(),"")+" ("+lSoggettoCumulato.getCodProvinciaNascita()+")";
        }        
        
        //Data nascita
        if(lSoggettoCumulato.getDataNascita() == null)
        {
          if(lSoggettoCumulato.getDataNascitaPresunta().equals("S")) {
            lStrSoggetto += "il "+StringUtils.toStringJSP(lSoggettoCumulato.getAnnoNascita());
          } else {
            lStrSoggetto += " il ***";
          }
        }else{
          lStrSoggetto += " il "+StringUtils.toStringJSP(DateUtils.getDateToString(lSoggettoCumulato.getDataNascita(),"dd-MM-yyyy"));
        }        
        
        lStrSoggetto +=" Codice CUI:";
        if (lSoggettoCumulato.getCodAfis()==null || lSoggettoCumulato.getCodAfis().equals("")) {
          lStrSoggetto +=" n.d.";
        } else {
          lStrSoggetto +=" "+StringUtils.toStringJSP(lSoggettoCumulato.getCodAfis());
        }
        
      }

%>
    <tr>
      <td class="L">
        <%=StringUtils.toStringJSP(lTitoCum.getDescrTipoProvvedimento(),"")%>
      </td>
      <td class="C" nowrap>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"dd-MM-yyyy"))%>
      </td>
      <td class="C" nowrap>
        <%=StringUtils.toStringJSP(lTitoCum.getAnnoSentenza())%>/<%=StringUtils.toStringJSP(lTitoCum.getNumeroSentenza(),"") %>
      </td>
      <td class="C" >
        <%=StringUtils.toStringJSP(lTitoCum.getDescrTipoAutoritaEmittente()+" "+lTitoCum.getDescrLuogoEmittente())%> 
      </td>
      <td class="C" nowrap>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"dd-MM-yyyy"))%>
      </td>
      <td class="C" >
        <% if (lTitoCum.getProcedimentoCumulato()!=null) { %>
        <%=StringUtils.toStringJSP(nSiep,"")%>
        <% } else { %>
        &nbsp;
        <% } %>
      </td>
      <td class="C" >
        <% if (lTitoCum.getProcedimentoCumulato()!=null) { %>
        <%=StringUtils.toStringJSP(lTitoCum.getProcedimentoCumulato().getDescrTipoUfficioFasCumulato()+" di "+lTitoCum.getProcedimentoCumulato().getDescrLuogoUfficioFasCumulato() )%>
        <% } else { %>
        &nbsp;
        <% } %>
      </td>
      <td class="C" >
        <a href="Javascript:insertIT( '<%=StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato())%>', 
                        '<%=StringUtils.toStringJSP(lTitoCum.getCodTipoProvvedimento())%>',
                        '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lTitoCum.getDescrTipoProvvedimento() ),"-") %>',
                        '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"dd-MM-yyyy"),"")%>',
                        '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"dd-MM-yyyy"),"")%>',
                        '<%=StringUtils.toStringJSP(lTitoCum.getAnnoSentenza())%>',
                        '<%=StringUtils.toStringJSP(lTitoCum.getNumeroSentenza())%>',
                      
                        '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lTitoCum.getDescrTipoAutoritaEmittente() ),"-") %>',
                        '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lTitoCum.getDescrLuogoEmittente() ),"-") %>',
                      
                        '<%=StringUtils.toStringJSP(lTitoCum.getCodTipoAutoritaEmittente())%>',
                        '<%=StringUtils.toStringJSP(lTitoCum.getCodLuogoEmittente())%>'
                        
                        <% if (lSoggettoCumulato!=null && !isStessoSoggetto) { %>
                        ,'<%=StringUtils.toStringJSP(lStrSoggetto)%>'
                        <% } %>
                      );">
          <img align="middle" src="/images/fileselected.gif" border=0>
        </a>
      </td>
    </tr>
    
    <% if (!isStessoSoggetto) { %>
    <tr>
      <td class="L" colspan="7">
        <font class="cRosso"><%=StringUtils.toStringJSP(lStrSoggetto,"&nbsp;")%></font>
      </td>
    </tr>
    <% } %>
    
<% } // end while sui titoli %>
    </table>
  </form>
  </body>
</html>