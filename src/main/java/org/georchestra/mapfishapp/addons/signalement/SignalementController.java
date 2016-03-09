package org.georchestra.mapfishapp.addons.signalement;

import org.georchestra.commons.configuration.GeorchestraConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SignalementController {

    @Autowired
    private GeorchestraConfiguration configuration;

    private Map<String, SignalementBackEnd> backends;

    @PostConstruct
    private void init(){

        this.backends = new HashMap<String, SignalementBackEnd>();

        String id;
        String table;
        String jdbcUrl;
        int i = 0;

        while((id = this.configuration.getProperty("signalement." + i + ".id")) != null){
            table = this.configuration.getProperty("signalement." + i + ".table");
            jdbcUrl = this.configuration.getProperty("signalement." + i + ".jdbcUrl");

            this.backends.put(id, new SignalementBackEnd(id, table, jdbcUrl));
            i++;
        }

    }

    @RequestMapping(value = "/signalement/backends", method = RequestMethod.GET)
	public void getBackends(HttpServletRequest request, HttpServletResponse response) throws Exception {

        JSONArray bes = new JSONArray();

        for (String id : this.backends.keySet())
            bes.put(this.backends.get(id).toJson());

        JSONObject res = new JSONObject();
        res.put("backends",bes);
        response.setHeader("Content-Type", "application/json");
        response.getWriter().print(res.toString(4));

    }

    @RequestMapping(value = "/signalement/backend/{backendId}", method = RequestMethod.GET)
	public void getBackend(HttpServletRequest request, HttpServletResponse response, @PathVariable String backendId) throws Exception {

        SignalementBackEnd backend =  this.backends.get(backendId);
        if(backend == null) {
            response.setStatus(404);
            response.getWriter().print("No such backend : " + backendId);
        } else {
            //response.setHeader("Content-Type", "application/json");
            String res = backend.toString() + "<br>" +
                    "<form method=\"POST\">" +
                    "Email : <input type=\"text\" name=\"email\"><br>" +
                    "Comment : <input type=\"text\" name=\"comment\"><br>" +
                    "Map context : <input type=\"text\" name=\"map_context\"><br>" +
                    "Latitude : <input type=\"text\" name=\"latitude\"><br>" +
                    "Longitude : <input type=\"text\" name=\"longitude\"><br>" +
                    "<input type=\"submit\" value=\"Store\">" +
                    "</form>";

            response.setHeader("Content-Type", "text/html");
            response.getWriter().print(res);
        }

    }

    @RequestMapping(value = "/signalement/backend/{backendId}", method = RequestMethod.POST)
    public void storeSignalement(HttpServletRequest request, HttpServletResponse response, @PathVariable String backendId) throws Exception {

        SignalementBackEnd backend =  this.backends.get(backendId);
        if(backend == null) {
            response.setStatus(404);
            response.getWriter().print("No such backend : " + backendId);
        } else {
            double latitude = Double.parseDouble(request.getParameter("latitude"));
            double longitude = Double.parseDouble(request.getParameter("longitude"));

            Signalement signalement = new Signalement(
                    request.getParameter("email"),
                    request.getParameter("comment"),
                    request.getParameter("map_context"),
                    latitude,
                    longitude);

            if (request.getHeader("sec-username") != null)
                signalement.setLogin(request.getHeader("sec-username"));
            backend.store(signalement);
            response.getWriter().print("Signalement stored");
        }

    }

}
