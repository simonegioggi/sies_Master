<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.Utils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.beneficio.action.ICostantiBeneficio"%>
<%@ page import="siap.siep.beneficio.model.BeneficioModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.beneficio.model.BeneficioPenaAccessoriaModel" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="beneficiopenaaccessoria" scope="request" class="java.util.Vector" />
<jsp:useBean id="ComingFromInsert" 		  scope="request" class="java.lang.String"/>
<jsp:useBean id="ComingFrom" 		      scope="request" class="java.lang.String"/>
<jsp:useBean id="lPenaResMod"    	 	  scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>

<%
	// Gestione funzione SIGE
	boolean modoSIGE = false;
	String   lModificabile = "";
	String   lCancellabile = "";

	if (modo != null && modo.equalsIgnoreCase("SIGE"))
	{
		modoSIGE = true;
		lModificabile = (String)request.getAttribute("Modificabile");
		lCancellabile =  (String)request.getAttribute("Cancellabile");
	}

	FascicoloSiepModel lFascicolo = null;
	if (!modoSIGE)
  		lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Benefici</title>
    <script language="JavaScript" src="/html/conferma.js"></script>

  </head>
  <body class="corpo">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font> <font class=campo>Elenco Benefici</font></td>
   
     <% 
     if(modoSIGE)
     {
  %>     
      <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>"/>
         <jsp:param name="ValoreIdEntita" value="0"/>
          <jsp:param name="Modificabile" value="<%=lModificabile%>"/>
          <jsp:param name="Cancellabile" value="<%=lCancellabile%>"/>
        </jsp:include>
     </td> <%} %>     
  	 <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
     </tr>
   </table>
	<br>
  <%if(!modoSIGE){%>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <%} else {%>
	   	<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
		 	<jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
  <%}%>
  <br>

  <%	if(beneficiopenaaccessoria == null || beneficiopenaaccessoria.size() == 0)
	  	{
 %>
      <table width="80%">
    <tr>
          <td class="int" align="left">Nessun Beneficio definito </td>
  	</tr>
  	</table>
  	<%} else { %>
  
  <div align=center>
  <table>
    <tr>
      <td class="int">Tipo Beneficio</td>
      <td class="int">Provvedimento di concessione</td>
      <td class="int">Quantum Reclusione</td>
      <td class="int">Importo Multa</td>
      <td class="int">Quantum Arresto</td>
      <td class="int">Importo Ammenda</td>
      <td class="int">Pena Accessoria</td>
      <td class="int" width=5%>Azioni</td>
    </tr>
<%
  Iterator itx = beneficiopenaaccessoria.iterator();
  while ( itx.hasNext())
  {

	  BeneficioPenaAccessoriaModel lBenPenMod = (BeneficioPenaAccessoriaModel)itx.next();
	  BeneficioModel beneficio = lBenPenMod.getBeneficio();
	  
	  if("03".equals(beneficio.getCodTipoBeneficio()) || "04".equals(beneficio.getCodTipoBeneficio()))
	  {
%>
    <tr>
      <td class=c>
          <%=StringUtils.toStringJSP(beneficio.getDescrTipoBeneficio())%>
      </td>
      <td class=c><%=StringUtils.toStringJSP(beneficio.getDescrDpr(),"-")%></td>
      <td class=l>
<%
      if (beneficio.getNumAnniReclusione()!=null || beneficio.getNumMesiReclusione()!=null || beneficio.getNumGiorniReclusione()!=null)
      {
%>
          Anni&nbsp;<%=StringUtils.toStringJSP(beneficio.getNumAnniReclusione(),"0")%><br>
          Mesi&nbsp;<%=StringUtils.toStringJSP(beneficio.getNumMesiReclusione(),"0")%><br>
          Giorni&nbsp;<%=StringUtils.toStringJSP(beneficio.getNumGiorniReclusione(),"0")%>
<%
      }
      else
      {
%>
          -
<%
      }
%>
      </td>
<%
      String multa=new String("");
      if ( beneficio.getImportoMulta()== null || beneficio.getImportoMulta().compareTo(new BigDecimal(0))==0  )

      {
        multa="-";
      }
      else
      {
        multa="€ "+ StringUtils.toEuroFormat(beneficio.getImportoMulta());
      }
%>
    <td class=r><%=multa%></td>
      
      <td class=l>
<%
      if (beneficio.getNumAnniArresto()!=null || beneficio.getNumMesiArresto()!=null || beneficio.getNumGiorniArresto()!=null)
      {
%>
          Anni&nbsp;<%=StringUtils.toStringJSP(beneficio.getNumAnniArresto(),"0")%><br>
          Mesi&nbsp;<%=StringUtils.toStringJSP(beneficio.getNumMesiArresto(),"0")%><br>
          Giorni&nbsp;<%=StringUtils.toStringJSP(beneficio.getNumGiorniArresto(),"0")%>
<%
      }
      else
      {
%>
          -
<%
      }
%>
      </td>

<%
      String ammenda=new String("");
      if (  beneficio.getImportoAmmenda()== null || beneficio.getImportoAmmenda().compareTo(new BigDecimal(0))==0  )

      {

        ammenda = "-";
      }
      else
      {

        ammenda = "€ "+ StringUtils.toEuroFormat(beneficio.getImportoAmmenda());

      }
%>
    <td class=r><%=ammenda%></td>
<%if(lBenPenMod != null && lBenPenMod.getPresenzaPenaAccessoria()) 
{%>    
    <td class=C><img src="/images/TickRed.gif"> </td>
<%}else{ %>
    <td class=c>&nbsp;</td>
<% 
}
%>
    <td class=c>
 <% if(modoSIGE) { %> 
         <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
         <jsp:param name="CampoIdEntita" value="<%=ICostantiBeneficio.CAMPO_ID_BENEFICIO%>" />
         <jsp:param name="ValoreIdEntita" value="<%=beneficio.getIdBeneficio()%>" />
           <jsp:param name="Modificabile" value="<%=lModificabile%>" />
        </jsp:include>
<% 
 }else{
%>
      <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_GESTIONE_FASCICOLO_VALIDATO%>">
         <jsp:param name="CampoIdEntita" value="<%=ICostantiBeneficio.CAMPO_ID_BENEFICIO%>" />
         <jsp:param name="ValoreIdEntita" value="<%=beneficio.getIdBeneficio()%>" />
         <jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
      </jsp:include>
<%
   }
 %>
 </td>
 </tr>
<%
  }
  } // ndwhile
%>
    </table>
  </div>
  
   <br>
  <div align=center>
  <table>
    <tr>
      <td class="int">Tipo Beneficio</td>
      <td class="int">Natura Beneficio</td>
      <td class="int">Tipologia obbligo</td>
      <td class="int">Durata prestazione</td>
      <td class="int">Durata sospensione condizionale della pena</td>
      <td class="int" width=5%>Azioni</td>
    </tr>
<%
  Iterator itxS = beneficiopenaaccessoria.iterator();
  while ( itxS.hasNext())
  {
	  BeneficioPenaAccessoriaModel lBenPenMod = (BeneficioPenaAccessoriaModel)itxS.next();
	  BeneficioModel beneficio = lBenPenMod.getBeneficio();
	  if("01".equals(beneficio.getCodTipoBeneficio()) || "02".equals(beneficio.getCodTipoBeneficio()))
	  {
%>
    <tr>
       <td class=c>
          <%=StringUtils.toStringJSP(beneficio.getDescrTipoBeneficio())%>
      </td>
      <td class=c>
          <%=StringUtils.toStringJSP(beneficio.getDescrSottotipoBeneficio())%>
      </td>
      <td class=c><%=StringUtils.toStringJSP(beneficio.getDescrTipoSospSubordinata(),"-")%></td>
      <td class=l>
<%
      if (beneficio.getNumMesiPrestazione()!=null || beneficio.getNumGiorniPrestazione()!=null)
      {
%>
          Mesi&nbsp;<%=StringUtils.toStringJSP(beneficio.getNumMesiPrestazione(),"0")%><br>
          Giorni&nbsp;<%=StringUtils.toStringJSP(beneficio.getNumGiorniPrestazione(),"0")%>
<%
      }
      else
      {
%>
          -
<%
      }
%>
      </td>
       <td class=l>
<%
      if (beneficio.getNumAnniSospensione() != null)
      {
%>
          Anni&nbsp;<%=StringUtils.toStringJSP(beneficio.getNumAnniSospensione(),"0")%>
<%
      }
      else
      {
%>
          -
<%
      }
%>
      </td>     

    <td class=c>
 <% if(modoSIGE) { %> 
         <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
         <jsp:param name="CampoIdEntita" value="<%=ICostantiBeneficio.CAMPO_ID_BENEFICIO%>" />
         <jsp:param name="ValoreIdEntita" value="<%=beneficio.getIdBeneficio()%>" />
           <jsp:param name="Modificabile" value="<%=lModificabile%>" />
        </jsp:include>
<% 
 }else{
%>
      <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_GESTIONE_FASCICOLO_VALIDATO%>">
         <jsp:param name="CampoIdEntita" value="<%=ICostantiBeneficio.CAMPO_ID_BENEFICIO%>" />
         <jsp:param name="ValoreIdEntita" value="<%=beneficio.getIdBeneficio()%>" />
         <jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
      </jsp:include>
<%
   }
 %>
  </td>
  </tr>
<%
	 }
  }  // endwhile
%>
    </table>
  </div> 
  
  </form>
  <br>
  

 <%
    // richiesta asir a9/rr/075 04-06-2009 SIEP MEV - Warning sul primo calcolo della pena 
    // paolo cherubini lunedi 11/10/2010

     if (lPenaResMod != null  && lPenaResMod.getFlagValidato().equals("N"))  {%>
    <FORM name="calcolopena" >   
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActLoadCalcoloPena">
	<table>
	<tr>
		<td class="lRosso">
			<font class="lRosso">
				Attenzione! Primo calcolo della pena già effettuato. Per computare i benefici modificati,
				è necessario procedere nuovamente con il calcolo della pena
			</font>
		</td>
	</tr>
	 <tr>
       <td>
        <INPUT  class="bottone" type="submit" name="CALCOLA" value="Calcola Fine Pena" onClick="">
       </td>
     </tr>
	</table>
	</FORM>
 <%   }
    // fine a9/rr/075 
  	} // endif beneficiopenaaccessoria == null ... 
 %>
<%
  if (ComingFromInsert !=null && ComingFromInsert.equals("YES"))
  {
%>
  <br>
  	<table>
      <tr>
        <td class=LGB>
<%
          if (ComingFrom != null && ComingFrom.equals("C")) //Benefici CONCESSI
          {
%>
            <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.beneficio.action.ActLoadInserisciBeneficio&ComingFromInsert=YES">Inserimento ulteriori Indulti</a>
<%
          }
          else //Benefici REVOCATI
          {
%>
            <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.beneficio.action.ActLoadInserisciBeneficioRevocato&ComingFromInsert=YES">Inserimento ulteriori Indulti</a>
<%
          }
%>
        </td>
      </tr>
    </table>
<%
  }
%>
  </body>
</html>