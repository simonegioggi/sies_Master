<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.presaincarico.action.ICostantiPresaincarico"%>
<%@ page import="siap.sico.jms.action.ICostantiSicoJMS"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>


<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioPM"    scope="request" class="java.lang.String"/>
<jsp:useBean id="listaEsiti"   scope="request" class="java.lang.String"/>



<%
FascicoloSiepModel lFascicoloModel = null;
BigDecimal lIdFascicolo = null;
BigDecimal chiave_anno = null;
BigDecimal chiave_progr = null;
if ( session.getAttribute("fascicolo")!=null) {
  lFascicoloModel = (FascicoloSiepModel) session.getAttribute("fascicolo");
  lIdFascicolo = lFascicoloModel.getIdFascicoloSiep();
  chiave_anno  = lFascicoloModel.getChiaveAnno();
  chiave_progr = lFascicoloModel.getChiaveProgr();
}


%>

<html>
<head>
  <title>[S.I.E.S.] - Misure di Sicurezza - Ricerca Atti Trasmessi per Competenza</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">
  function Verify()
  {
    var data_inizio = document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>.value
                 +'/'+document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>.value
                 +'/'+document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>.value;
                 
    var data_fine   = document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.value
                 +'/'+document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>.value
                 +'/'+document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>.value;

    if(     document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO%>.value.length == 0
         && document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO%>.value != '-' 
      )
    {
      alert("Il campo Sede Ufficio è obbligatorio");
      return false;
    }
    
    if (!ControllaDataPassaVuota(data_inizio)){
      alert("La data di trasmissione iniziale non ha un formato valido");
      return false;
    }
    
    if (!ControllaDataPassaVuota(data_fine)){
      alert("La data di trasmissione finale non ha un formato valido");
      return false;
    }
    
    if(ControllaData(data_inizio) && ControllaData(data_fine)){
       if(!CompareDate(data_inizio,data_fine))
       {
         alert('La Data Trasmissione finale non può precedere la data di iniziale');
         return false;
       }
    }

    return true;

  }
  </script>

  <script language="JavaScript">
      var desktop;   
      function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio){
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
      
      function checkTipoUfficio (){
        if (document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO%>.value=='-')
          document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO%>.value = '';
      }
      
  </script>
  
  <script language="JavaScript">

    var idFascicolo = <%=StringUtils.toStringJSP(lIdFascicolo,"0")%>;
    var chiave_anno = <%=StringUtils.toStringJSP(chiave_anno,"0")%>;
    var chiave_progr = <%=StringUtils.toStringJSP(chiave_progr,"0")%>;
    
    function checkFascicolo(){
      if (document.LoadRicercaProvTrasmessiMisSic.checkFascicoloCorrente.checked){
        if (idFascicolo>0){
          //alert("1");
          document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value = chiave_anno;
          document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value = chiave_progr;
        }
        else {
          //alert("2");
          document.LoadRicercaProvTrasmessiMisSic.checkFascicoloCorrente.checked=false;
          alert("Nessun fascicolo attualmente aperto");
        }
      }
      else {
        document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value = '';
        document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value = '';
      }
    }
  </script>

</head>
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo"> Verifica esito trasmissioni atti per Competenza </font></td>
      <td class="LBG">
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActGestioneMisureSicurezza">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
    
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadRicercaProvTrasmessiMisSic'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActRicercaAttiTrasmessi">
    <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
    
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="Titolo" colspan="2">Ufficio Destinatario del Provvedimento </td>
      </tr>
      <tr>
        <td class="l">Ufficio del Pubblico Ministero</td>
        <td class="L">
          <select title="tipoUfficioSIEP" class=small name="<%=ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO%>" 
                  onChange="javascript:checkTipoUfficio ()">
            <%= ufficioPM %>           
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede</td>        
        <td class="l">
           <input Title="Sede Ufficio" name="<%=ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO%>" type="text" maxlength="35" size="35"
                  value="<%=StringUtils.toStringJSP(request.getParameter(ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO),"")%>">
           <a href="Javascript:ListaUfficiComuni('LoadRicercaProvTrasmessiMisSic'
                                                ,'<%= ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO %>'
                                                , document.LoadRicercaProvTrasmessiMisSic.<%=ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO%>.value);">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>
    </table>
    
    <br>    
    
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="Titolo" colspan="6"> Selezione del periodo di trasmissione</td>
      </tr>

        
      <tr>
        <td class="l">Dalla data </td>
        <td class="l" colspan ='2'>
          <input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"
                 value="<%=StringUtils.toStringJSP(request.getParameter(ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO),"")%>">
           -
          <input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" 
                 value="<%=StringUtils.toStringJSP(request.getParameter(ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO),"")%>" >
           -
          <input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" 
                 value="<%=StringUtils.toStringJSP(request.getParameter(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO),"")%>" >
        </td>
      
        <td class="l">Alla data </td>
        <td class="l" colspan ='2'>
          <input Title="Data di trasmissione termine" type="text" name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"
                 value="<%=StringUtils.toStringJSP(request.getParameter(ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE),"")%>">
           -
          <input Title="Data di trasmissione termine" type="text" name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" 
                 value="<%=StringUtils.toStringJSP(request.getParameter(ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE),"")%>">
           -
          <input Title="Data di trasmissione termine" type="text" name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" 
                 value="<%=StringUtils.toStringJSP(request.getParameter(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE),"")%>">
        </td>
      </tr>

    </table>
          
    <!--table cellspacing=2 cellpadding=2>
      <tr>
        <td class="lVerdeNB" >
          N.B.: Se le date di Trasmissione non sono valorizzate, il sistema estrae gli atti relativi agli ultimi 2 mesi.
        </td>
      </tr>
    </table-->
    
    <%
    //==========================================================================
    //
    //==========================================================================
    %>
    <br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="Titolo" colspan="6">Selezione lo Stato della Trasmissione</td>
      </tr>
      <tr>
        <td class="l">
          <table cellspacing=2 cellpadding=2>
            <tr>
              <td class="label">&nbsp;Stato Trasmissione </td>
              <td class="Label">
                <select title="esito" class="small" name="<%=ICostantiSicoJMS.CAMPO_TIPO_ESITO%>" >
                <%= listaEsiti %>
                </select>   
               </td>
            </tr>
          </table>
        </td>
        <!--td class="label"> 
          <input type="checkbox" name="checkEscludiIscrittiInClasseIV" value="1"> in corso di definizione
        </td-->
      </tr>
    </table>
      
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label"> 
          <input type="checkbox" name="checkFascicoloCorrente" onclick="checkFascicolo();">Solo fascicolo corrente
        </td>
      </tr>
      <tr>
        <td class="l">Numero SIEP (Anno/Progressivo)</td>        
        <td class="l">
          <input Title="Anno SIEP "  type="text" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
                 value="<%=StringUtils.toStringJSP(request.getParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO),"")%>">
          /
          <input Title="Numero SIEP " type="text" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" maxlength="6" size="15"
                 value="<%=StringUtils.toStringJSP(request.getParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR),"")%>">
        </td>
      </tr>
      
    </table>
    
    
    <br>   
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input onclick="Javascript:return Verify();" class="bottone" type="submit" name="RICERCA" value="Ricerca">
        </td>
      </tr>
    </table>
  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadRicercaProvTrasmessiMisSic");

    //frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","numeric");
    //frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","numeric");
    
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","minlen=2","La lunghezza minima per il giorno di inizio è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","minlen=2","La lunghezza minima per il mese di inizio è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","lt=3000");

    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","minlen=2","La lunghezza minima per il giorno di fine è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","minlen=2","La lunghezza minima per il mese di fine è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","lt=3000");

  </script>
  </body>
</html>