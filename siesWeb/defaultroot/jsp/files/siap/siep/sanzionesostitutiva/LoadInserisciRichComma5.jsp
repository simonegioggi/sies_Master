<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.lang.String" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.siep.notifica.model.NotificaModel" %>
<%@ page import="siap.siep.rinnovo.action.ICostantiRinnovo" %>
<%@ page import="siap.siep.rinnovo.model.RinnovoModel" %>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"  />
<jsp:useBean id="soggetto" scope="session" class="siap.sico.soggetto.model.SoggettoModel"  />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>

<jsp:useBean id="ordineIngiunzione" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="notificheAvvocati" scope="request" class="java.util.Vector"  />

<jsp:useBean id="tipoAutorita" scope="request" class="java.lang.String"  />

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

if (lPosizione == null)
	lPosizione = new PosizioneGiuridicaModel();

if (lAltraCausa == null)
	lAltraCausa = new AltraCausaModel();
%>

<% 
   int contaRinnovi = 0;  
   int contaDaValidare = 0;   
   for (int i = 0; i < ordineIngiunzione.getNotifiche().length; i++) {
     NotificaModel lNotMod = ordineIngiunzione.getNotifiche()[i];
 
     Vector lListaRinnovi = lNotMod.getListaRinnovi();
     
     Iterator <RinnovoModel> iterRinnovi = lListaRinnovi.iterator();
     while (iterRinnovi.hasNext()){
       contaRinnovi++;
       RinnovoModel lRinnovo = iterRinnovi.next();   
       if (lRinnovo.getFlagDocumentoRegistrato()==null 
           || lRinnovo.getFlagDocumentoRegistrato().equals("N") )
        contaDaValidare++;       
     }
   }
%>

<html>
  <head>
    <title>[S.I.E.S.] - Richiesta Informazioni Comma 5</title>

    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>  
    <script language="JavaScript">
    
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }      
      
      function radio()
      {
        var nodeDivUno  = document.getElementById('divDifUno');
        var nodeDivDue  = document.getElementById('divDifDue');
        var nodeAltri   = document.getElementById('divAltri');

        if(document.LoadRichiestaInfoComma5.Notifica[0].checked)
        {
          nodeAltri.style.display='none';
          document.LoadRichiestaInfoComma5.altri.value="N";

          nodeDivUno.style.display='block';
          document.LoadRichiestaInfoComma5.primoAvvocato.value="S";

          if(document.LoadRichiestaInfoComma5.numAvv.value == 2)
          {
            nodeDivDue.style.display='block';
            document.LoadRichiestaInfoComma5.secondoAvvocato.value="S";
          }
          else
          {
            nodeDivDue.style.display='none';
          }
        } 
        else if(document.LoadRichiestaInfoComma5.Notifica[1].checked) 
        {
          nodeAltri.style.display='block';
          nodeDivUno.style.display='none';
          nodeDivDue.style.display='none';

          document.LoadRichiestaInfoComma5.altri.value="S";
          if(document.LoadRichiestaInfoComma5.numAvv.value == 2)
          {
           document.LoadRichiestaInfoComma5.secondoAvvocato.value="N";
          }
          document.LoadRichiestaInfoComma5.primoAvvocato.value="N";
        }
      }


      function CancellaRichiesta(idRinnovo){
        if (window.confirm("Confermi l'eliminazione della Richiesta?")) {
          document.cancellaRichiesta.<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>.value = idRinnovo;
          document.cancellaRichiesta.submit();
        }
      }
      
      function downloadStampaRinnovo(idRinnovo)
      {
        var lAzione = "<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadDocumentoRinnovoRicercheOIPP";
        var parametri = lAzione+"&<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>="+idRinnovo;
        stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
      }
      
      
      function Verify()
      {
        if(document.LoadRichiestaInfoComma5.Notifica[0].checked)
        {
          if (document.LoadRichiestaInfoComma5.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value.length==1)
            document.LoadRichiestaInfoComma5.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value='0'+document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value;
          if (document.LoadRichiestaInfoComma5.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value.length==1)
            document.LoadRichiestaInfoComma5.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value='0'+document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value;

          var data_to_verify = document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value
                         +'-'+ document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value
                         +'-'+ document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>.value;

          if (!ControllaDataPassaVuota(data_to_verify) )
          {
            alert('Data Richiesta non valida');
            document.LoadRichiestaInfoComma5.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.focus();
            return false;
          }
            
            
          if(document.LoadRichiestaInfoComma5.numAvv.value == 2)
          {
            if (document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value.length==1)
              document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value='0'+document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value;
            if (document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value.length==1)
              document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value='0'+document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value;

            var data_to_verify = document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value
                           +'-'+ document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value
                           +'-'+ document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>.value;

            if (!ControllaDataPassaVuota(data_to_verify) )
            {
              alert('Data Richiesta non valida');
              document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.focus();
              return false;
            }
          }
          
          
          
          if (   document.LoadRichiestaInfoComma5.<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO%>.value!='22'
        	    && document.LoadRichiestaInfoComma5.<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO%>.value!='-'	  
             ) 
          {
              if (document.LoadRichiestaInfoComma5.<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO%>.value=='') {
                alert("Indicare la sede dell'Autorità di polizia delegata");
                document.LoadRichiestaInfoComma5.<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO%>.focus();
                return false;
              }              
           }
          
        }
        else
        {
          if (document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value.length==1)
            document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value='0'+document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value;
          if (document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value.length==1)
            document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value='0'+document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value;

          var data_to_verify = document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value
                         +'-'+ document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value
                         +'-'+ document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>.value;

          if (!ControllaDataPassaVuota(data_to_verify) )
          {
            alert('Data Richiesta non valida');
            document.LoadRichiestaInfoComma5.<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.focus();
            return false;
          }
          
          if (   document.LoadRichiestaInfoComma5.<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_AL%>.value!='22'
      	      && document.LoadRichiestaInfoComma5.<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_AL%>.value!='-'	  
           ) 
          {
            if (document.LoadRichiestaInfoComma5.<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_AL%>.value=='') {
              alert("Indicare la sede dell'Autorità di polizia delegata");
              document.LoadRichiestaInfoComma5.<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_AL%>.focus();
              return false;
            }              
          }          
        }
        
        return true;
      }
      
      function gestisciCampi(){
          <% if (contaDaValidare>0) { %>
          $('#divInserimento').find("input, select, textarea").attr('disabled','disabled');
          $('#divInserimento').find("img").hide();
          <% } %>
      }      
    </script>
  </head>

<body class="corpo" onLoad="radio();gestisciCampi();">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Richiesta Informazioni Comma 5&nbsp;</font>
      </td>
      <td class="LBG">
        <a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActLoadInserisciRichInfoComma5">
          <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>      
      </td>      
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan="5">
        <font class="campo">
        <% if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) { %>
          DETENUTO PER ALTRA CAUSA - <%=lAltraCausa.getDescrTipoPosGiuridica()%>
        <% } else { %>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
        <% } %>
        </font>
      </td>
    </tr>
  </table>
  
  <table>
    <tr>
      <td class="L" colspan="5">
        <font class="campo">
              <%=ordineIngiunzione.getEvento().getDescrTipoProvvedimento()%>
              &nbsp;
              <%=ordineIngiunzione.getEvento().getDescrMotivo()%>
              &nbsp;emesso in data&nbsp;
              <%=DateUtils.getDateToString(ordineIngiunzione.getEvento().getDataEmissione(), "dd-MM-yyyy")%>
        </font>
      </td>
    </tr>
  </table>
  
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="cancellaRichiesta">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActCancellaRichInfoComma5">
    <input type="HIDDEN" name="<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>" value="">
  </form>
  
  
<br>

<!-- 
  <table>
    <tr>
      <td class="LGB">
        <a href="Javascript:ListaRichieste();">Elenco Richieste Precedenti</a>
      </td>
    </tr>
  </table>
-->
   
  <% if (contaDaValidare > 0) {  %>
  <table>
    <tr>
      <td class="l" style="color:red;">
        Attenzione, esiste una richiesta ancora da validare
      </td>
    </tr>
  </table>
  <% } %>    

  <div id="divListaRicerche" style="width: 100%; display:block;" >
    <table width="90%">
      <tr>
        <td class="int">Tipo Richiesta</td>        
        <td class="int">Data Richiesta Notifica</td>
        <td class="int">Avvocato</td>
        <td class="int">Autorita' di polizia delegata</td>
        <td class="int">Validato</td>
        <td class="int">Azioni</td>
      </tr>
      <% if (contaRinnovi == 0) {  %>
      <tr>
        <td class="L" colspan="6">Nessuna richiesta presente per il provvedimento selezionato</td>
      </tr>
      <% } %>  
  <% 
      for (int i = 0; i < ordineIngiunzione.getNotifiche().length; i++) {
        NotificaModel lNotMod = ordineIngiunzione.getNotifiche()[i];
    
        Vector lListaRinnovi = lNotMod.getListaRinnovi();
        
        Iterator <RinnovoModel> iterRinnovi = lListaRinnovi.iterator();
        while (iterRinnovi.hasNext()){
          RinnovoModel lRinnovo = iterRinnovi.next();
          
          String strAvvocato = "&nbsp;";
          if(lRinnovo.getCodTipoRinnovo().equals("D")) {
            AvvocatoModel lAvvocato = lNotMod.getAvvSiep().getAvvocato();
            strAvvocato = lAvvocato.getCognome()+" "+lAvvocato.getNome();
          }
          
          String lActDettaglio = "siap.siep.sanzionesostitutiva.action.ActLoadDettaglioRichInfoComma5";
          lActDettaglio += "&"+ICostantiRinnovo.CAMPO_ID_RINNOVO+"="+lRinnovo.getIdRinnovo();
          String lActCancella = "siap.siep.sanzionesostitutiva.action.ActCancellaRinnovoRicercheOIPP";
          lActCancella += "&"+ICostantiRinnovo.CAMPO_ID_RINNOVO+"="+lRinnovo.getIdRinnovo();
          String lActStampa = "";
      %>
      <tr>
        <td class="c"><%=StringUtils.toStringJSP(lRinnovo.getDescrTipoRinnovo())%></td>
        <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRinnovo.getDataRinnovo(),"dd-MM-yyyy"),"")%></td>
        <td class="c"><%=strAvvocato%></td>
        <td class="c"><%=StringUtils.toStringJSP(lRinnovo.getDescrTipoAutoritaRinnovo())%> di <%=StringUtils.toStringJSP(lRinnovo.getDescrLuogoRinnovo())%></td>
        <td class="c">
        <% if ("S".equals(lRinnovo.getFlagDocumentoRegistrato())) { %>
          <img  alt="Validato" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0"></a>
        <% } else { %>
        &nbsp;<font style="color:red;">(da validare)</font>
        <% } %>
        </td>
        <td class="r" nowrap>
          <% if (!"S".equals(lRinnovo.getFlagDocumentoRegistrato())) { %>
          <a href="Javascript:CancellaRichiesta('<%=lRinnovo.getIdRinnovo()%>')">
            <img  alt="Cancella" src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border="0"></a>
          <% } %>
          <% if ("S".equals(lRinnovo.getFlagDocumentoRegistrato()) || "N".equals(lRinnovo.getFlagDocumentoRegistrato())) { %>
          <a href="Javascript:downloadStampaRinnovo('<%=lRinnovo.getIdRinnovo()%>')">
            <img  alt="Stampa" src="<%=IWebConstants.IMAGES_DIR%>print.gif" border="0"></a>
          <% } %>
          <a  href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>">
            <img  alt="Dettaglio" src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" border="0"></a>&nbsp;
        </td>
      </tr>
      <% } %>
      <% } %>
    </table>
  </div>
  <br>  
  
  
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRichiestaInfoComma5">
  <div id="divInserimento">
    <table width="90%">
      <tr>
        <td class="c">Richiesta Informazioni al Difensore &nbsp;<input type="radio" name="Notifica" value="FP" checked  onClick="radio();">
                      Richiesta Informazioni ad altri  &nbsp;<input type="radio" name="Notifica" value="UG"  onClick="radio();">
        </td>
      </tr>
    </table>
    
    
    <%
      List lNotificheAvv = new ArrayList();
      Iterator iter = notificheAvvocati.iterator();
      while (iter.hasNext())
      {
        NotificaModel lNotMod = (NotificaModel)iter.next();
        if (lNotMod.getAvvSiep()!=null)
          lNotificheAvv.add(lNotMod);
      }

      AvvocatoSiepModel lPrimoAvv = ((NotificaModel) lNotificheAvv.get(0)).getAvvSiep();
      NotificaModel lNotPrimoAvvModel = (NotificaModel) lNotificheAvv.get(0);
    %>

    <input type="hidden" name="numAvv" value="<%=lNotificheAvv.size()%>">
    
    <%
    // =====================================================================
    //  Richiesta Info al primo difensore
    // =====================================================================
    %>    
    <div id="divDifUno" style="display:none; width:100%;">
      <input type="hidden" name="primoAvvocato" value="">
      <input type="hidden" name="idPrimaNotifica" value="<%=lNotPrimoAvvModel.getIdNotifica()%>">
      
      <table width="90%">
        <% if(lPrimoAvv != null && lPrimoAvv.getAvvocato() != null) {%>
        <tr>
          <td class="l" width=25%>Avvocato</td>
          <td class="l" colspan=3>
             <font class="campo"><%=lPrimoAvv.getAvvocato().getCognome()%>&nbsp;<%=lPrimoAvv.getAvvocato().getNome()%></font>
                &nbsp; Foro di &nbsp;<font class="campo"><%=lPrimoAvv.getAvvocato().getForo()%></font>
          </td>
        </tr>
        <tr>
          <td class="l">Tipo Difensore</td>
          <td class="l" colspan=3>
             <font class="campo"><%=lPrimoAvv.getAvvocato().getDescrTipo()%></font>
          </td>
        </tr>
        <%}%>    
    
        <tr>
          <td class="l" >Data Richiesta</td>
          <td class="l" colspan=3>
          <input title="Giorno Richiesta" type="text" size="2" maxlength="2" 
                 value="<%=DateUtils.getSysDate("dd")%>"  name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          -
          <input title="Mese Richiesta" type="text" size="2" maxlength="2" 
                 value="<%=DateUtils.getSysDate("MM")%>" name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input title="Anno Richiesta" type="text" size="4" maxlength="4" 
                 value="<%=DateUtils.getSysDate("yyyy")%>" name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
        </tr>
        
        <tr>
          <td class="l">Autorita' di polizia delegata</td>
          <td class="l" colspan="3">
            <select title="TipoAutorita" name="<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO%>">
              <%=tipoAutorita%>
            </select>
          </td>
        </tr>
        
        <tr>
          <td class="l">Luogo</td>
          <td class="L">
            <input title="Luogo" type="text" name="<%=  ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO%>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadRichiestaInfoComma5','<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO %>');">
            <img src="/images/filefolder.gif" border=0>
            </a>
          </td>
          <td class="l">Indirizzo</td>
          <td class="L">
              <TEXTAREA title="Note" name="<%=ICostantiRinnovo.CAMPO_NOTE%>" cols=30></textarea>
          </td>
        </tr>
      </table>
    </div>
    
    <%
    // =====================================================================
    //  Richiesta Info al secondo difensore
    // =====================================================================
    %> 
    <div id="divDifDue" style="display:none; width:100%;">
    <%if(lNotificheAvv.size()>1)
    {
      AvvocatoSiepModel lAvvSecondo = ((NotificaModel) lNotificheAvv.get(1)).getAvvSiep();
      NotificaModel lNotSecondoAvvMod = (NotificaModel) lNotificheAvv.get(1);
    %>    
      <input type="hidden" name ="idSecondaNotifica" value="<%=lNotSecondoAvvMod.getIdNotifica()%>">
      <input type="hidden" name ="secondoAvvocato" value="">
<br>
      <table width="90%">    
        <% if(lAvvSecondo != null && lAvvSecondo.getAvvocato() != null) {%>
        <tr>
          <td class="l" width=25%>Avvocato</td>
          <td class="l" colspan="3">
              <font class="campo"><%=lAvvSecondo.getAvvocato().getCognome()%>&nbsp;<%=lAvvSecondo.getAvvocato().getNome()%> </font>
          &nbsp; Foro di &nbsp;<font class="campo"><%=lAvvSecondo.getAvvocato().getForo()%> </font>
          </td>
        </tr>
        <tr>
          <td class="l">Tipo Difensore</td>
          <td class="l" colspan="3">
             <font class="campo"><%=lAvvSecondo.getAvvocato().getDescrTipo()%></font>
          </td>
        </tr>
        <%}%>

        <tr>
          <td class="l" >Data Richiesta</td>
          <td class="l" colspan="3">
            <input title="Giorno Richiesta" type="text" size="2" maxlength="2"
                   value="<%=DateUtils.getSysDate("dd")%>" name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input title="Mese Richiesta" type="text" size="2" maxlength="2" 
                   value="<%=DateUtils.getSysDate("MM")%>"  name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">                 
            -
            <input title="Anno Richiesta" type="text" size="4" maxlength="4" 
                   value="<%=DateUtils.getSysDate("yyyy")%>"  name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
        </tr>
        <tr>
          <td class="l">Autorita' di polizia delegata</td>
          <td class="l" colspan="3">
            <select title="TipoAutorita" name="<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_A%>">
              <%=tipoAutorita%>
            </select>
          </td>
        </tr>
        <tr>
           <td class="l">Luogo</td>
           <td class="L">
              <input title="Luogo" type="text" name="<%=  ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_A%>"  maxlength="35" size="35">
              <a href="Javascript:ListaComuni('LoadRichiestaInfoComma5','<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_A %>');">
                <img src="/images/filefolder.gif" border=0>
              </a>
           </td>
           <td class="l">Indirizzo</td>
           <td class="L">
                <TEXTAREA title="Note" name="<%=ICostantiRinnovo.CAMPO_NOTE_A%>" cols=30></textarea>
           </td>
        </tr>
      </table>
      <%}%>
    </div>

    <%
    // =====================================================================
    //  Richiesta Info ad altri
    // =====================================================================
    NotificaModel[] lListaNot = ordineIngiunzione.getNotifiche();
    NotificaModel lNotModAltri = null;
    for (int i=0; i<lListaNot.length; i++){
      if ( lListaNot[i].getCodTipoNotifica().equals("E")) 
        lNotModAltri = lListaNot[i];
    }
    %> 
    <div id="divAltri" style="display:none; width:100%;">
      <input type="hidden" name ="altri" value="">
      <% if(lNotModAltri!=null) { %>
        <input type="hidden" name ="idAltraNotifica" value="<%=lNotModAltri.getIdNotifica()%>">
      <%}%>
      <table width="100%">
        <tr>
          <td class="l" >Data Richiesta</td>
          <td class="l" colspan="3">
            <input title="Giorno Richiesta" type="text" size="2" maxlength="2" 
                   value="<%=DateUtils.getSysDate("dd")%>"  name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            -
            <input title="Mese Richiesta" type="text" size="2" maxlength="2" 
                   value="<%=DateUtils.getSysDate("MM")%>"  name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>" 
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input title="Anno Richiesta" type="text" size="4" maxlength="4" 
                   value="<%=DateUtils.getSysDate("yyyy")%>"  name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>"  
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            </td>
        </tr>
        <tr>
          <td class="l">Autorita' di polizia delegata</td>
          <td class="l" colspan="3">
            <select title="TipoAutorita" name="<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_AL%>">
              <%=tipoAutorita%>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Luogo</td>
          <td class="L">
            <input title="Luogo" type="text" name="<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_AL%>"  maxlength="35" size="35">
              <a href="Javascript:ListaComuni('LoadRichiestaInfoComma5','<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_AL%>');">
                <img src="/images/filefolder.gif" border=0>
              </a>
          </td>
          <td class="l">Indirizzo</td>
          <td class="L">
              <TEXTAREA title="Note" name="<%=ICostantiRinnovo.CAMPO_NOTE_AL%>" cols=30></textarea>
          </td>
        </tr>
      </table>
    </div>
    
    <div id="divBottone" style="width:100%;">
      <table>
         <tr>
          <td>
           <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActInserisciRichInfoComma5">
           <input class="bottone" type="submit" name="INSERISCI" value="Conferma" >
          </td>
        </tr>
      </table>
    </div>
</div>
  </form>


  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadRichiestaInfoComma5");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>



