<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<jsp:useBean id="provvedimento" scope="request" class="siap.bdmc.sbpren.model.ProvvedimentoModelBDMC"/>

<%@page import="siap.bdmc.sbviewreat.model.SbViewReatModel"%>
<%@page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>
<%@page import="siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel"%>

<html>
<table cellspacing=1 cellpadding=1 width="100%">
    <%
    Vector CapoImpu = provvedimento.getSbViewCapoImpu();
    if(CapoImpu!=null && CapoImpu.size()>0)
    {
    %>
 <tr>
      <td class="LBGISIV" width="10%"><font class="campoLow">Capo Imputazione</font>  </td>
      <td class="LBGISIV" width="20%"><font class="campoLow">Anno/Numero Fascicolo BDMC </font></td>
      <td class="LBGISIV" width="20%"><font class="campoLow">Autorità</font> </td>
      <td class="LBGISIV" width="50%"><font class="campoLow">Reato</font> </td>
 </tr>
   <% Iterator lIterCapoImpu = CapoImpu.iterator();
   while(lIterCapoImpu.hasNext())
      {
          SbViewCapoimpuModel lCapoimpu = (SbViewCapoimpuModel)lIterCapoImpu.next();
          Vector lReati = lCapoimpu.getSbViewReat();
          Iterator lIterReati = lReati.iterator();

          %>
          <tr>
          <td class="c" >
		  <%  // dati Capoimpu
          %>
              <font class="label">
		  <%      out.println("" + lCapoimpu.getNumeProgCapoImpu().toString()+" ");
		  %>  </font>
          </td>
          <td class="c" >
        	  <font class="campo"><%
	   		        out.println("" + lCapoimpu.getAnnoFascBdmc().toString()+"/"+lCapoimpu.getNumeFascBdmc().toString()+" ");
          %>  </font>
          </td>
          <td class="c" >
        	  <font class="campo"><%
	   		        out.println("" + lCapoimpu.getDescriSedeInst()+" ");
          %>  </font>
          </td>
          <td class="c" >
          <font class="campo">
              <%
              while(lIterReati.hasNext()) 
              {
              SbViewReatModel lReato = (SbViewReatModel)lIterReati.next();
                     
              if(lReato.getArtiFontGiur() != null && lReato.getArtiFontGiur().toString().length() >0)
            	  out.println("art."+lReato.getArtiFontGiur());
              if(lReato.getArtiQualFont() != null && lReato.getArtiQualFont().length() >0 && !lReato.getArtiQualFont().equals("-"))
                  out.println(" "+lReato.getArtiQualFont());          
              if(lReato.getCodiFontGiur() != null && lReato.getCodiFontGiur().toString().length() >0)
            	  out.println(" "+lReato.getCodiFontGiur());     
              if(lReato.getCommiArtiFont() != null && lReato.getCommiArtiFont().trim().length() >0)
                  out.println(" c. "+lReato.getCommiArtiFont()); 
              if(lReato.getLettArtiFont() != null && lReato.getLettArtiFont().trim().length() >0)
                        out.println(" l. "+lReato.getLettArtiFont());
             if(lReato.getNumeArtiFont() != null && lReato.getNumeArtiFont().trim().length() >0)
                        out.println(" n. "+lReato.getNumeArtiFont());
                      out.println(", ");
          	  }
	      		//ART. 
	      		if (lCapoimpu.getFlagArti0056().equals("1")){
	      			out.println("art.56 c.p., ");
	      		}
	      		if (lCapoimpu.getFlagArti0061().equals("1")){
	      			if (lCapoimpu.getArti0061Comm() != null && lCapoimpu.getArti0061Comm().length() >0)
	      				out.println("art.61 c.p. comma "+lCapoimpu.getArti0061Comm()+", ");
	      			else
	      				out.println("art.61 c.p., ");
	      		}
	      		if (lCapoimpu.getFlagArti0081().equals("1")){
	      			if (lCapoimpu.getArti0081Comm() != null && lCapoimpu.getArti0081Comm().length() >0)
	      				out.println("art.81 c.p. comma "+lCapoimpu.getArti0081Comm()+", ");
	      				else
	      				out.println("art.81 c.p., ");
	      		}
	      		if (lCapoimpu.getFlagArti0112().equals("1")){
	      			if (lCapoimpu.getArti0112Commi() != null && lCapoimpu.getArti0112Commi().length() >0)
	      				out.println("art.112 c.p. comma "+lCapoimpu.getArti0112Commi()+", ");
	      			else
	      				out.println("art.112 c.p., ");
	      		}
	      		if (lCapoimpu.getFlagArt0110().equals("1")){
	      			out.println("art.110 c.p., ");
	      		}
	      		if (lCapoimpu.getFlagArti0113().equals("1")){
	      			out.println("art.113 c.p., ");
	      		}
	      		if (lCapoimpu.getFlagArti0114().equals("1")){
	      			out.println("art.114 c.p., ");
	      		}
	      		if (lCapoimpu.getFlagArti0116().equals("1")){
	      			out.println("art.116 c.p., ");
	      		}
	      		if (lCapoimpu.getFlagArti0117().equals("1")){
	      			out.println("art.117 c.p., ");
	      		}
	          %>
          </font>
          </td>
          <% 
      }
    }
else
      {%>
       <tr>
        <td class="l">
       <font class="cGrigio">Nessun Capo Imputazione e Reato per il procedimento BDMC</font>
      </td>
    </tr>
 <%}%>
        </table>
</html>