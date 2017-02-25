/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.cinvestav.tamps.crawling.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *Implements several test services.
 * @author ealdana@tamps.cinvestav.mx
 */
@Path("math")
public class ArithmeticServices {
    
    @GET
    @Path("multiplicacion")
    @Produces(MediaType.TEXT_PLAIN)
    public Response multiplica(@QueryParam("val1") String val1,
                               @QueryParam("val2") String val2) {
        float fVal1=Float.parseFloat(val1);
        float fVal2=Float.parseFloat(val2);
        float res=fVal1*fVal2;
        return Response.status(200)
                       .entity("<H1>"+fVal1+"*"+fVal2+"="+res+"</H1>")
                       .type("text/html; charset=utf-8")
                       .build();
    }
    
}
