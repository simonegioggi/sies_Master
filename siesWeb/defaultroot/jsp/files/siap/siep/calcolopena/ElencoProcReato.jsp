<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.DettaglioFascicoloModel" %>
<%@ page import="siap.siep.reato.action.ICostantiReato" %>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel" %>
<%@ page import="siap.siep.modulocumulo.model.ReatoCircostanzaCumuloModel" %>

<jsp:useBean id="fascicoli"         scope="request" class="java.util.Vector" />
<jsp:useBean id="AzioneChiamante"   scope="request" class="java.lang.String" />
<jsp:useBean id="stringaDiRicerca"  scope="request" class="java.lang.String" />

<jsp:useBean id="IntestazioneReato" scope="request" class="java.lang.String" />
<jsp:useBean id="IntestazioneDate"  scope="request" class="java.lang.String" />
<jsp:useBean id="cumulatiSINO"      scope="request" class="java.lang.String" />
<jsp:useBean id="tipologiaProc"     scope="request" class="java.lang.String" />
<jsp:useBean id="DescrizioNazione"  scope="request" class="java.lang.String" />

<jsp:useBean id="IntestazioneAggravanti"  scope="request" class="java.lang.String" />
<jsp:useBean id="ReatooCircostanze"  scope="request" class="java.lang.String" />
<%
String TipoProc="";
String IntestaReato="";
String IntestaDate="";
String DescNazio="";

String IntestaCirco="";
String TipoRicerca="";

%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco Procedimento per Reato</title>

<script language="JavaScript">
function waiting() 
{
    var node=document.getElementById('waiting');
    node.style.visibility='visible';
}

function effettoTree(a) 
{
  var sub=1;
  var id = "elenco"+a+"."+sub;
  var node=document.getElementById(id);
  document.images["image"+a].src = (node.style.display == "none")? "<%=IWebConstants.IMAGES_DIR%>collapse.gif" : "<%=IWebConstants.IMAGES_DIR%>expand.gif";

  while (node!=undefined)
  {
    node.style.display = (node.style.display == "none")? "block" : "none";
    sub++;
    id = "elenco"+a+"."+sub;
    node=document.getElementById(id);
  } 

  return false;
}

function UlerioreReatoCumulo(idRecord)
{
    var id="ReaCum_"+idRecord+".1";
    var riga = document.getElementById(id);  
    
    if (riga.style.display =="none" )
    {
      //riga.style.display = "block";
      document.images["image_"+idRecord].src = "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
    }
    else 
    {
      //riga.style.display = "none";
      document.images["image_"+idRecord].src = "<%=IWebConstants.IMAGES_DIR%>expand.gif";
    }
    
    var sub=1;
    var node=document.getElementById("ReaCum_"+idRecord+"."+sub);
    while (node!=undefined)
    {
      node.style.display = (node.style.display == "none")? "block" : "none";
      sub++;
      id="ReaCum_"+idRecord+"."+sub;
      node=document.getElementById(id);
    }

}
</script>

</head>

<BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Procedimenti per Reato</font></td>

      <!-- NUOVO BOTTONE PER STAMPA EXCEL -->
    <td class=l>
    <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActStampaElencoProcReatoInExcel&CercaCumulati=<%=cumulatiSINO%>&TipoProc=<%=tipologiaProc%>&IntestaReato=<%=IntestazioneReato%>&IntestaDate=<%=IntestazioneDate%>&DescNazio=<%=DescrizioNazione%>&IntestaCirco=<%=IntestazioneAggravanti%>&TipoRicerca=<%=ReatooCircostanze%>"
      onClick="javascript:waiting();">
      <img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
      </a>
    </td>
    <td>
      <div align=center id="waiting" style="visibility:hidden;position:relative;">
        <img src="/images/rotelle3.gif" height="25" width="25" border="0"> <font class="cRosso">Attendere...</font>
        </div>
      </td> 
    </tr>
  </table>
  
<!--      INTESTAZIONE criteri di Ricerca    -->
  <table width="60%">
    <tr>
    <td class="L" style="text-align:left" >
<%    if(ReatooCircostanze.compareTo("R") == 0)
    {%>   
			<font style="color:green; font-size: 10pt;" class="label"> Reato : <%=IntestazioneReato %> </font>
<%    }
    else if(ReatooCircostanze.compareTo("A") == 0)
    { %>
	  		<font style="color:green; font-size: 10pt;" class="label"> Circostanza : <%=IntestazioneAggravanti %> </font>
<%    }
    else if(ReatooCircostanze.compareTo("RA") == 0) 
    { %>
	  		<font style="color:green; font-size: 10pt;" class="label"> Reato : <%=IntestazioneReato %> + Circostanza : <%=IntestazioneAggravanti %></font>
<%    } %>            
    </td>
  </tr>  
<%  if(IntestazioneDate != null && !IntestazioneDate.equals("") )
  {%>
   <tr>
    <td class=l>
		<font style="color:green; font-size: 10pt;" class="label"> Periodo : <%=IntestazioneDate %> </font>
    </td>
   </tr> 
<%  }%>
   <tr>
    <td class=l>
		<font style="color:green; font-size: 10pt;" class="label"> <%=tipologiaProc %> </font>
    </td>
   </tr> 
<%  if(!cumulatiSINO.equals("") )
  {%>
   <tr>
    <td class=l>
		<font style="color:green; font-size: 10pt;" class="label"> Solo con Procedimenti di Cumulo </font>
    </td>
   </tr> 
<%  }%>   
<%  if(!DescrizioNazione.equals("") )
  {%>
   <tr>
    <td class=l>
		<font style="color:green; font-size: 10pt;" class="label"> Nazionalità <%=DescrizioNazione %></font>
    </td>
   </tr> 
<%  }%>   
  </table>
  
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

<br>
<br>

  <table cellspacing="2" cellpadding="2" width="170%">
    <tr>
      <td class="int" width="10%">Numero SIEP</td>
      <td class="int" width="10%">Stato Procedimento</td>
      <td class="int" width="10%">Soggetto</td>
      <td class="int" width="5%">Nazione</td>
      <td class="int" width="10%">Regime di Espiazione</td>
      <td class="int" width="5%">Data Fine Pena</td>
      <td class="int" width="12%">
        <table width=100%>
          <tr>
             <td class="int" align=center colspan=2 width=100%>Pena Irrogata in Sentenza</td>
          </tr>
          <tr>
             <td class="int" align=left width=50%>Reclusione</td>
             <td class="int" align=right width=50%>Arresto</td>
          </tr>
        </table>
      </td>
      <td class="int" width="12%">
        <table width=100%>
          <tr>
             <td class="int" align=center colspan=2 width=100%>Pena da Espiare</td>
          </tr>
          <tr>
             <td class="int" align=left width=50%>Reclusione</td>
             <td class="int" align=right width=50%>Arresto</td>
          </tr>
        </table>
      </td>
      <td class="int" width="13%">Reato</td>
      <td class="int" width="10%">Data Reato</td>
      <td class="int" width="3%">Azioni</td>
    </tr>

<%
int jPA =0;
int id_record =0;
Iterator itx = fascicoli.iterator();
while ( itx.hasNext())    {
  DettaglioFascicoloModel fascicolo = (DettaglioFascicoloModel)itx.next();
%>

  <tr>
    <td class="c"><!-- Numero SIEP -->
<%
  if (fascicolo != null && fascicolo.getFascicoloSiep()!= null )
  {
%>
      <font class="label"><%=fascicolo.getFascicoloSiep().getChiaveAnno()%>/<%=fascicolo.getFascicoloSiep().getChiaveProgr()%></font>&nbsp;
<%
    if(fascicolo != null && fascicolo.getFascicoloSiep() != null && 
      fascicolo.getFascicoloSiep().getFlagCumulante() != null && 
      fascicolo.getFascicoloSiep().getFlagCumulante().compareTo("S") == 0 )
    { %>
      <font style="color:red; font-size: 11pt;" class="label">&nbsp;&nbsp;    C </font>
<%    }     
  }
%>
    &nbsp;
    </td>

    <td class="c"><!-- Stato Procedimento -->
<%
  if (fascicolo != null && fascicolo.getFascicoloSiep()!= null ) {
%>
      <font class="label"><%=fascicolo.getFascicoloSiep().getDescrStatoProcedimento()%></font>
<%
  }
%>
    &nbsp;
    </td>

    <td class="c"><!-- Soggetto -->
 <%
  if (fascicolo != null && fascicolo.getFascicoloSiep()!= null && fascicolo.getFascicoloSiep().getSoggetto() != null) {
%>
      <font class="label"><%=fascicolo.getFascicoloSiep().getSoggetto().getCognome() %></font>
      <font class="label"><%=fascicolo.getFascicoloSiep().getSoggetto().getNome()%></font>
<%
  }
%>
    &nbsp;
    </td>

    <td class="c"><!-- Nazione -->
 <%
  if (fascicolo != null && fascicolo.getFascicoloSiep()!= null && fascicolo.getFascicoloSiep().getSoggetto() != null) {
%>
      <font class="label"><%=fascicolo.getFascicoloSiep().getSoggetto().getDescrNazionalita()%></font>
<%
  }
%>
    &nbsp;
    </td>

    <td class="c"><!-- Regime di Espiazione -->
<%
  if (fascicolo != null && fascicolo.getPosizioneGiuridica()!= null && fascicolo.getPosizioneGiuridica().getDescrPosizioneGiuridica() != null) {
%>
    <font class="label"><%=fascicolo.getPosizioneGiuridica().getDescrPosizioneGiuridica()%></font>
<%
  }
%>
    &nbsp;
    </td>

    <td class="c"><!-- Data Fine Pena -->
<%
  if(fascicolo != null && fascicolo.getPenaResidua() != null && fascicolo.getPenaResidua().getDataFine() != null) {
%>
    <font class="label"><%=DateUtils.getDateToString(fascicolo.getPenaResidua().getDataFine(),"dd/MM/yyyy")%></font>
<%
  } else {
%>
    -
<%
  }
%>
    </td>

    <td class="c"><!-- Pena Irrogata in Sentenza  / Pena Complessiva-->
<%
  if( fascicolo != null && fascicolo.getPenaComplessivaSanzioneSostitutiva() != null && fascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva() != null) 
  {
%>
      <table width=100%><tr>

        <td align=left><!-- Reclusione -->
<%
    if(fascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumAnniReclusione() != null || fascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumMesiReclusione() != null  || fascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumGiorniReclusione() != null ) {
%>
        <font class="label">AA:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumAnniReclusione())%></font>
        <font class="label"> MM:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumMesiReclusione())%></font>
        <font class="label"> GG:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumGiorniReclusione())%></font>
<%
    }
%>
        &nbsp;
        </td>

        <td align=right><!-- Arresto -->
<%
    if(fascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumAnniArresto() != null || fascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumMesiArresto() != null  || fascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumGiorniArresto() != null ) {
%>
        <font class="label">AA:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumAnniArresto())%></font>
        <font class="label"> MM:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumMesiArresto())%></font>
        <font class="label"> GG:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getNumGiorniArresto())%></font>
<%
    }
%>
        &nbsp;
        </td>

      </tr></table>
      
      <!-- Pena Irrogata in Sentenza  / Pena Cumulo-->
<%  } 
  else if(fascicolo != null && fascicolo.getPenaCumulo() != null)
  {
%>
    <table width=100%>
      <tr>
        <td align=left><!-- Reclusione -->  
<%
    if(fascicolo.getPenaCumulo().getNumAnniReclusione() != null || 
      fascicolo.getPenaCumulo().getNumMesiReclusione() != null || 
      fascicolo.getPenaCumulo().getNumGiorniReclusione() != null )
    {
%>
      <font class="label">AA:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaCumulo().getNumAnniReclusione())%></font>
      <font class="label"> MM:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaCumulo().getNumMesiReclusione())%></font>
      <font class="label"> GG:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaCumulo().getNumGiorniReclusione())%></font>
<%    }%>
        &nbsp;
        </td>

        <td align=right><!-- Arresto -->
<%     
    if( (fascicolo.getPenaCumulo().getNumAnniArresto() != null && !fascicolo.getPenaCumulo().getNumAnniArresto().equals(0) ) || 
      (fascicolo.getPenaCumulo().getNumMesiArresto() != null && !fascicolo.getPenaCumulo().getNumMesiArresto().equals(0) ) || 
      (fascicolo.getPenaCumulo().getNumGiorniArresto() != null && !fascicolo.getPenaCumulo().getNumGiorniArresto().equals(0) ) ) 
    { %>
      <font class="label">AA:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaCumulo().getNumAnniArresto())%></font>
      <font class="label"> MM:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaCumulo().getNumMesiArresto())%></font>
      <font class="label"> GG:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaCumulo().getNumGiorniArresto())%></font>
<%    }%>   
        &nbsp;
        </td>
      </tr>
    </table>    
              
<%  }
  else
  { 
%>
      &nbsp;
<%  } %>
    </td>

    <td class="c"><!-- Pena da Espiare -->
<%
  if( fascicolo != null && fascicolo.getCalendario() != null && fascicolo.getCalendario().getDataFine()!=null ) 
  {
%>
    <table width=100%><tr><td><!-- Reclusione -->
    
<%    if( fascicolo.getCalendario().getDataFine().after(DateUtils.getSysDate()) )
    { %>      
      <font class="label">AA:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getCalendario().getNumAnni())%></font>
      <font class="label"> MM:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getCalendario().getNumMesi())%></font>
      <font class="label"> GG:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getCalendario().getNumGiorni())%></font>
<%    }
    else if( fascicolo.getCalendario().getDataFine().compareTo(DateUtils.getSysDate()) == 0 )
    { %>
      <font class="label">AA:</font><font class="campo"> 0 </font>
      <font class="label"> MM:</font><font class="campo"> 0 </font>
      <font class="label"> GG:</font><font class="campo"> 0 </font>
<%    } %>
              
    </td></tr></table>
<%
  } else {
%>

<%
    if( fascicolo != null && fascicolo.getPenaResidua() != null) {
%>
      <table width=100%><tr>

        <td align=left><!-- Reclusione -->
<%
      if(fascicolo.getPenaResidua().getNumAnniReclusione() != null || fascicolo.getPenaResidua().getNumMesiReclusione() != null  || fascicolo.getPenaResidua().getNumGiorniReclusione() != null ) {
%>
        <font class="label">AA:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaResidua().getNumAnniReclusione())%></font>
        <font class="label"> MM:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaResidua().getNumMesiReclusione())%></font>
        <font class="label"> GG:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaResidua().getNumGiorniReclusione())%></font>
<%
      }
%>
        &nbsp;
        </td>

        <td align=right><!-- Arresto -->
<%
      if(fascicolo.getPenaResidua().getNumAnniArresto() != null || fascicolo.getPenaResidua().getNumMesiArresto() != null  || fascicolo.getPenaResidua().getNumGiorniArresto() != null ) {
%>
        <font class="label">AA:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaResidua().getNumAnniArresto())%></font>
        <font class="label"> MM:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaResidua().getNumMesiArresto())%></font>
        <font class="label"> GG:</font><font class="campo"><%=StringUtils.toStringJSP(fascicolo.getPenaResidua().getNumGiorniArresto())%></font>
<%
      }
%>
        &nbsp;
        </td>
      </tr></table>
<%
    } else {
%>
      &nbsp;
<%
    }
%>

<%
  }
%>
    </td>

<%
  Vector<ReatoCircostanzaModel> reatiCircostanzeFascicolo = null; 
  Vector<ReatoCircostanzaCumuloModel> reatiCircostanzeCumulo = null; 
  
  if( fascicolo != null && fascicolo.getReatiCircostanze() != null) {
    reatiCircostanzeFascicolo = (Vector<ReatoCircostanzaModel>)fascicolo.getReatiCircostanze();
  }
  if( fascicolo != null && fascicolo.getReatoCircoCumulo() != null) {
    reatiCircostanzeCumulo = (Vector<ReatoCircostanzaCumuloModel>)fascicolo.getReatoCircoCumulo();
  }
%>

    <!-- Reato (CUMULO) -->
<%    if(reatiCircostanzeCumulo!=null && reatiCircostanzeCumulo.size()>0 && reatiCircostanzeCumulo.get(0).getReatoCum()!=null)
    {  %>
      <td class="c">
        <font class="label"><%=reatiCircostanzeCumulo.get(0).getReatoCum().getDescrTipoReato() %></font>
      
<%      if(reatiCircostanzeCumulo.size()>1 )
      { %>
        <a href="javascript:UlerioreReatoCumulo(<%=id_record%>)"><img name="image_<%=id_record%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Ulteriori Reati" ></a>

<%      } %>
      
      &nbsp;
      </td>
      <!-- Data Reato (CUMULO) -->
      <td class="c">
        <font class="label"><%=reatiCircostanzeCumulo.get(0).getReatoCum().getDescrPeriodoConsumazioneCum()%></font>
      &nbsp;
      </td>
<%    } 
    else
    { 
      if (reatiCircostanzeFascicolo!=null && reatiCircostanzeFascicolo.size()>0 && reatiCircostanzeFascicolo.get(0).getReato()!=null)
      {
%>        <!-- Reato -->
        <td class="c">
          <font class="label"><%=reatiCircostanzeFascicolo.get(0).getReato().getDescrTipoReato()%></font>
<%
        if (reatiCircostanzeFascicolo.size() > 1)
        { %>
          <a href="#1" onClick="return effettoTree(<%=jPA%>)"><img name="image<%=jPA%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Ulteriori Reati" ></a>
<%
        } %>
        &nbsp;
        </td>
        <!-- Data Reato -->
        <td class="c">
          <font class="label"><%=reatiCircostanzeFascicolo.get(0).getReato().getDescrPeriodoConsumazione()%></font>
        &nbsp;
        </td> 
<%      }
    } 
%>

    <td class="c">
      <jsp:include page="<%=ICostantiReato.PG_BUTTONS_REATI%>">
        <jsp:param name="CampoAzioneChiamante" value="NomeAzione" />
        <jsp:param name="ValoreAzioneChiamante" value="<%=AzioneChiamante%>" />
        <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>"/>
        <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getFascicoloSiep().getIdFascicoloSiep()%>"/>
        <jsp:param name="stringaDiRicerca" value="<%=stringaDiRicerca%>"/>
      </jsp:include>
    </td>

  </tr>
  
   <!-- Record Hidden con la descrizione del Luogo esecuzione Misura-->
<%
  if (reatiCircostanzeCumulo != null && reatiCircostanzeCumulo.size() > 1) {
    int numReatoC=1;
    Iterator itrC = reatiCircostanzeCumulo.iterator();
    itrC.next();  //salta il primo
    while (itrC.hasNext())
    {
      ReatoCircostanzaCumuloModel reatoCircoCum = (ReatoCircostanzaCumuloModel)itrC.next();
%>   
    <tr style="display:none" id="ReaCum_<%=id_record%>.<%=numReatoC%>">
      <td colspan="8"></td>
      <td class="c"><%=(reatoCircoCum.getReatoCum()!=null)? reatoCircoCum.getReatoCum().getDescrTipoReato():"" %>&nbsp;</td>
      <td class="c"><%=(reatoCircoCum.getReatoCum()!=null)? reatoCircoCum.getReatoCum().getDescrPeriodoConsumazioneCum():"" %>&nbsp;</td>
      <td></td>
    </tr>
<%      numReatoC++;
    }
  } %>  

<%
  if (reatiCircostanzeFascicolo != null && reatiCircostanzeFascicolo.size() > 1) {
    int numReato=1;
    Iterator itr = reatiCircostanzeFascicolo.iterator();
    itr.next(); //salta il primo
    while (itr.hasNext())    {
      ReatoCircostanzaModel reatoCircostanza = (ReatoCircostanzaModel)itr.next();
%>
  <tr id="elenco<%=jPA%>.<%=numReato%>" style="display:none">
    <td colspan="8"></td>
    <td class="c">
      <%=(reatoCircostanza.getReato()!=null)?reatoCircostanza.getReato().getDescrTipoReato():""%>
      &nbsp;
    </td>
    <td class="c">
      <%=(reatoCircostanza.getReato()!=null)?reatoCircostanza.getReato().getDescrPeriodoConsumazione():""%>
      &nbsp;
    </td>
    <td></td>
  </tr>
<%
      numReato++;
    }
  }
%>

<%
  jPA++;
  id_record++;
}
%>
    </table>
  </FORM>
  <br>

  </body>
</html>