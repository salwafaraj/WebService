/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package org.me.calculator;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;

/**
 *
 * @author salwa
 */
@WebService(serviceName = "CalculatorWS")
@Stateless()
public class CalculatorWS {

    /**
     * This is a sample web service operation
     */
    /**
     * Web service operation
     */
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "rechercher")
    public String rechercher(@WebParam(name = "cin") String cin) {
        String resultat = "NOT FOUUND";
        try {
            // open connection
            javax.naming.InitialContext ctx = new javax.naming.InitialContext();
            javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup("jdbc/myCNSSDS");
            java.sql.Connection conn = ds.getConnection();

            // prepare and run query
            String query = "select num_individu , nom_individu,date_naissance_individu from d_individu where cin_individu='" + cin + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String numero_individu = rs.getString("num_individu");
                String nom_individu = rs.getString("nom_individu");
                Date naissance_individu = rs.getDate("date_naissance_individu");

                DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
                String naissance_formatted = dateFormat.format(naissance_individu);
                System.out.println("-------->");
                resultat = numero_individu + "---" + nom_individu + "----" + naissance_formatted + "----";
            }

        } catch (Exception ex) {
            System.err.println("ERROR MYSQL: " + ex.getMessage());
        }
        return resultat;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "rechercheDossier")
public String rechercheDossier(@WebParam(name = "cin") String cin) {
    String resultat = "NO FILES FOUND";
    try {
        // open connection
        javax.naming.InitialContext ctx = new javax.naming.InitialContext();
        javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup("jdbc/myCNSSDS");
        java.sql.Connection conn = ds.getConnection();

        // prepare and run query
        String query = "SELECT * FROM d_dossier WHERE num_individu = (SELECT num_individu FROM d_individu WHERE cin_individu = '" + cin + "')";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        StringBuilder resultBuilder = new StringBuilder();
        boolean hasResults = false;  // To check if we have any results

        while (rs.next()) {
            String num_dossier = rs.getString("num_dossier");
            String acte = rs.getString("acte");
            int montant = rs.getInt("montant");
            String statut = rs.getString("statut");

            if (hasResults) {
                resultBuilder.append("----");  // Separate each dossier with "----"
            }
            resultBuilder.append(num_dossier).append("---")
                         .append(acte).append("---")
                         .append(montant).append("---")
                         .append(statut);
            hasResults = true;
        }

        if (hasResults) {
            resultat = resultBuilder.toString();
        }
    } catch (Exception ex) {
        System.err.println("ERROR MYSQL: " + ex.getMessage());
    }
    return resultat;
}

}
